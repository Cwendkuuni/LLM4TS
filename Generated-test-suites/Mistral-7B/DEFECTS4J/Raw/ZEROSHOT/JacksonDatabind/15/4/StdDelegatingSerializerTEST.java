import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;

public class StdDelegatingSerializerTest {

 private Converter<Object, ?> converterMock;
 private JavaType delegateTypeMock;
 private JsonSerializer<Object> delegateSerializerMock;
 private StdDelegatingSerializer serializer;

 @Before
 public void setUp() {
 converterMock = mock(Converter.class);
 delegateTypeMock = mock(JavaType.class);
 delegateSerializerMock = mock(JsonSerializer.class);
 serializer = new StdDelegatingSerializer(converterMock, delegateTypeMock, delegateSerializerMock);
 }

 @Test
 public void testSerializationWithNullValue() throws JsonProcessingException {
 Object value = null;
 JsonGenerator gen = mock(JsonGenerator.class);
 SerializerProvider provider = mock(SerializerProvider.class);

 when(delegateSerializerMock.serialize(any(), eq(gen), eq(provider))).thenReturn(null);

 serializer.serialize(value, gen, provider);

 verify(provider, times(1)).defaultSerializeNull(eq(gen));
 }

 @Test
 public void testSerializationWithValue() throws JsonProcessingException {
 Object value = new Object();
 JsonGenerator gen = mock(JsonGenerator.class);
 SerializerProvider provider = mock(SerializerProvider.class);
 TypeSerializer typeSerializerMock = mock(TypeSerializer.class);

 when(delegateSerializerMock.serialize(any(), eq(gen), eq(provider), eq(typeSerializerMock))).thenReturn(null);

 serializer.serialize(value, gen, provider);
 serializer.serializeWithType(value, gen, provider, typeSerializerMock);

 verify(delegateSerializerMock, times(1)).serialize(any(), eq(gen), eq(provider), eq(typeSerializerMock));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSerializationWithSameType() throws JsonProcessingException {
 Object value = new Object();
 JsonGenerator gen = mock(JsonGenerator.class);
 SerializerProvider provider = mock(SerializerProvider.class);

 when(delegateSerializerMock.serialize(any(), eq(gen), eq(provider))).thenReturn(null);

 serializer = new StdDelegatingSerializer(converterMock, (JavaType) getClass(), delegateSerializerMock);

 serializer.serialize(value, gen, provider);
 }

 @Test
 public void testGetConverter() {
 assertSame(converterMock, serializer.getConverter());
 }

 @Test
 public void testGetDelegatee() {
 assertSame(delegateSerializerMock, serializer.getDelegatee());
 }
}