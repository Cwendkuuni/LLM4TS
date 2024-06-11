import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.*;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;

public class StdDelegatingSerializerTest {

 private ObjectMapper objectMapper;
 private JsonSerializer<Object> delegateSerializer;
 private Converter<Object,?> converter;
 private StdDelegatingSerializer stdDelegatingSerializer;

 @Before
 public void setUp() throws Exception {
 objectMapper = new ObjectMapper();
 delegateSerializer = mock(JsonSerializer.class);
 converter = mock(Converter.class);

 stdDelegatingSerializer = new StdDelegatingSerializer(converter);
 stdDelegatingSerializer = stdDelegatingSerializer.withDelegate(converter, null, delegateSerializer);
 }

 @Test
 public void testSerialize() throws JsonGenerationException, IOException {
 Object value = new Object();
 JsonGenerator jsonGenerator = mock(JsonGenerator.class);
 SerializerProvider serializerProvider = mock(SerializerProvider.class);

 when(delegateSerializer.serialize(any(), eq(jsonGenerator), eq(serializerProvider))).thenReturn(null);

 stdDelegatingSerializer.serialize(value, jsonGenerator, serializerProvider);
 verify(delegateSerializer).serialize(any(), eq(jsonGenerator), eq(serializerProvider));
 }

 @Test
 public void testSerializeWithType() throws JsonGenerationException, IOException {
 Object value = new Object();
 TypeSerializer typeSerializer = mock(TypeSerializer.class);
 JsonGenerator jsonGenerator = mock(JsonGenerator.class);
 SerializerProvider serializerProvider = mock(SerializerProvider.class);

 when(delegateSerializer.serializeWithType(any(), eq(jsonGenerator), eq(serializerProvider), eq(typeSerializer))).thenReturn(null);

 stdDelegatingSerializer.serializeWithType(value, jsonGenerator, serializerProvider, typeSerializer);
 verify(delegateSerializer).serializeWithType(any(), eq(jsonGenerator), eq(serializerProvider), eq(typeSerializer));
 }

 @Test
 public void testGetDelegatee() {
 assertSame(delegateSerializer, stdDelegatingSerializer.getDelegatee());
 }

 @Test
 public void testConvertValue() {
 Object input = new Object();
 Object output = converter.convert(input);
 verify(converter).convert(input);
 assertEquals(output, stdDelegatingSerializer.convertValue(input));
 }

 @Test(expected = IllegalStateException.class)
 public void testSubclassWithoutWithDelegateMethod() {
 class TestSerializer extends StdDelegatingSerializer {
 // This subclass should override 'withDelegate' method
 }

 TestSerializer testSerializer = new TestSerializer();
 }
}