import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.ser.std.MapSerializer;
import java.util.HashMap;
import java.util.Map;

public class AnyGetterWriterTest {

 private Object testObject;
 private AnyGetterWriter anyGetterWriter;
 private JsonGenerator mockJsonGenerator;
 private SerializerProvider mockSerializerProvider;
 private Object mapValue;

 @Before
 public void setup() {
 testObject = mock(Object.class);
 mapValue = new HashMap<>();
 mapValue.put("key1", "value1");
 mapValue.put("key2", "value2");

 anyGetterWriter = new AnyGetterWriter(new BeanProperty(null, "anyGetter", null),
 new AnnotatedMember(new BeanProperty("anyGetter", Map.class), null), new MapSerializer(new StandardSerializer(Map.class), null));
 mockJsonGenerator = mock(JsonGenerator.class);
 mockSerializerProvider = mock(SerializerProvider.class);
 when(mockSerializerProvider.findValueSerializer(Map.class)).thenReturn(anyGetterWriter._mapSerializer);
 }

 @Test
 public void testGetAndSerialize() throws Exception {
 when(anyGetterWriter._accessor.getValue(testObject)).thenReturn(mapValue);
 anyGetterWriter.getAndSerialize(testObject, mockJsonGenerator, mockSerializerProvider);

 // Verify serializeFields method is called
 verify(mockJsonGenerator).writeStartObject();
 for (Map.Entry<String, Object> entry : mapValue.entrySet()) {
 verify(mockJsonGenerator).writeFieldName(entry.getKey());
 verify(mockJsonGenerator).writeValue(entry.getValue());
 }
 verify(mockJsonGenerator).writeEndObject();
 }

 @Test(expected = JsonMappingException.class)
 public void testGetAndSerializeWithInvalidValue() throws Exception {
 when(anyGetterWriter._accessor.getValue(testObject)).thenReturn(new Object());
 anyGetterWriter.getAndSerialize(testObject, mockJsonGenerator, mockSerializerProvider);

 // Verify exception is thrown
 verify(mockJsonGenerator, never()).writeStartObject();
 }

 @Test
 public void testGetAndFilter() throws Exception {
 when(anyGetterWriter._accessor.getValue(testObject)).thenReturn(mapValue);
 PropertyFilter filter = mock(PropertyFilter.class);
 anyGetterWriter.getAndFilter(testObject, mockJsonGenerator, mockSerializerProvider, filter);

 // Verify serializeFilteredFields method is called
 verify(mockJsonGenerator).writeStartObject();
 for (Map.Entry<String, Object> entry : mapValue.entrySet()) {
 if (!entry.getKey().equals("key1")) {
 verify(mockJsonGenerator, never()).writeFieldName(entry.getKey());
 verify(mockJsonGenerator, never()).writeValue(entry.getValue());
 } else {
 verify(mockJsonGenerator).writeFieldName(entry.getKey());
 verify(mockJsonGenerator).writeValue(entry.getValue());
 }
 }
 verify(mockJsonGenerator).writeEndObject();
 }

 @Test(expected = JsonMappingException.class)
 public void testGetAndFilterWithInvalidValue() throws Exception {
 when(anyGetterWriter._accessor.getValue(testObject)).thenReturn(new Object());
 anyGetterWriter.getAndFilter(testObject, mockJsonGenerator, mockSerializerProvider, mock(PropertyFilter.class));

 // Verify exception is thrown
 verify(mockJsonGenerator, never()).writeStartObject();
 }

 @Test
 public void testResolve() throws JsonMappingException {
 SerializerProvider provider = mock(SerializerProvider.class);
 when(provider.handlePrimaryContextualization(anyGetterWriter._mapSerializer, new BeanProperty(null, "property", null))).thenReturn(anyGetterWriter._mapSerializer);
 anyGetterWriter.resolve(provider);

 // Verify mapSerializer is resolved
 verify(provider).handlePrimaryContextualization(eq(anyGetterWriter._mapSerializer), eq(new BeanProperty(null, "property", null)));
 }
}