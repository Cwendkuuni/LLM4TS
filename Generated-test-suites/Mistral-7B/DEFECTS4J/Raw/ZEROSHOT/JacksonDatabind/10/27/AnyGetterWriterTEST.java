import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.MapSerializer;
import com.fasterxml.jackson.databind.util.Converter;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

public class AnyGetterWriterTest {

 private AnyGetterWriter anyGetterWriter;
 private BeanProperty property;
 private AnnotatedMember accessor;
 private MapSerializer mapSerializer;
 private SerializerProvider serializerProvider;
 private ObjectNode testObjectNode;
 private JsonGenerator testJsonGenerator;

 @Before
 public void setup() {
 property = mock(BeanProperty.class);
 accessor = mock(AnnotatedMember.class);
 mapSerializer = mock(MapSerializer.class);
 serializerProvider = mock(SerializerProvider.class);
 testObjectNode = mock(ObjectNode.class);
 testJsonGenerator = mock(JsonGenerator.class);

 anyGetterWriter = new AnyGetterWriter(property, accessor, mapSerializer);
 }

 @Test
 public void testGetAndSerialize() throws JsonMappingException, JsonProcessingException {
 when(accessor.getValue(anything(Object.class))).thenReturn(mock(Map.class));

 anyGetterWriter.getAndSerialize(anything(Object.class), testJsonGenerator, serializerProvider);

 verify(mapSerializer, times(1)).serializeFields((Map<?,?>) anyCaptor.capture(Map.class), testJsonGenerator, serializerProvider);
 Map<String, Object> capturedMap = anyCaptor.getValue();
 assertEquals("Test key", capturedMap.get("Test key"));
 assertEquals(1, capturedMap.size());
 }

 @Test(expected = JsonMappingException.class)
 public void testGetAndSerializeWithNullValue() throws JsonMappingException, JsonProcessingException {
 when(accessor.getValue(anything(Object.class))).thenReturn(null);

 anyGetterWriter.getAndSerialize(anything(Object.class), testJsonGenerator, serializerProvider);

 verifyNoMoreInteractions(mapSerializer);
 verify(testJsonGenerator, times(1)).close();
 }

 @Test(expected = JsonMappingException.class)
 public void testGetAndSerializeWithNonMapValue() throws JsonMappingException, JsonProcessingException {
 when(accessor.getValue(anything(Object.class))).thenReturn(new String("Test String"));

 anyGetterWriter.getAndSerialize(anything(Object.class), testJsonGenerator, serializerProvider);

 verifyNoMoreInteractions(mapSerializer);
 verify(testJsonGenerator, times(1)).close();
 }

 @Test
 public void testGetAndFilter() throws JsonMappingException, JsonProcessingException {
 when(accessor.getValue(anything(Object.class))).thenReturn(mock(Map.class));
 PropertyFilter filter = mock(PropertyFilter.class);

 anyGetterWriter.getAndFilter(anything(Object.class), testJsonGenerator, serializerProvider, filter);

 verify(mapSerializer, times(1)).serializeFilteredFields((Map<?,?>) anyCaptor.capture(Map.class), testJsonGenerator, serializerProvider, filter, null);
 Map<String, Object> capturedMap = anyCaptor.getValue();
 assertEquals("Test key", capturedMap.get("Test key"));
 assertEquals(1, capturedMap.size());
 }

 @Test
 public void testResolve() throws JsonMappingException {
 AnyGetterWriter anyGetterWriter = spy(new AnyGetterWriter(property, accessor, mapSerializer));
 SerializerProvider serializerProvider = mock(SerializerProvider.class);
 Converter converter = mock(Converter.class);
 ObjectMapper objectMapper = mock(ObjectMapper.class);
 when(serializerProvider.getConverter(ObjectMapper.class)).thenReturn(converter);
 when(converter.findValueConverter(Map.class, Object.class)).thenReturn(null);
 when(serializerProvider.findValueSerializer(Map.class, ObjectMapper.DefaultTyping.JAVA_LANG_OBJECT_ARRAY)).thenReturn(mapSerializer);
 when(objectMapper.findBeanProperties(anything(Class.class), ObjectMapper.DefaultTyping.JAVA_LANG_OBJECT)).thenReturn(new BeanProperty[0]);
 when(objectMapper.findPropertiesWithAnnotation(property, JsonAnyGetter.class)).thenReturn(new BeanProperty[]{property});

 anyGetterWriter.resolve(serializerProvider);

 verify(mapSerializer, times(1)).serializeFields((Map<?,?>) anyCaptor.capture(Map.class), testJsonGenerator, serializerProvider);
 Map<String, Object> capturedMap = anyCaptor.getValue();
 assertEquals("Test key", capturedMap.get("Test key"));
 assertEquals(1, capturedMap.size());
 }
}