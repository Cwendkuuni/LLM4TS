import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.std.MapSerializer;
import com.fasterxml.jackson.databind.ser.std.Serializers;
import static com.fasterxml.jackson.databind.ser.std.Serializers.FindNullSerializers;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fasterxml.jackson.databind.util.BeanUtil;
import com.fasterxml.jackson.databind.util.NameTransformer;
import com.fasterxml.jackson.databind.util.SerializerBuilder;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class AnyGetterWriterTest {

 private ObjectMapper objectMapper;
 private AnyGetterWriter anyGetterWriter;
 private SerializerProvider serializerProvider;
 private JsonGenerator jsonGeneratorMock;

 @Before
 public void setUp() throws Exception {
 objectMapper = new ObjectMapper();
 SerializerProvider serializerProvider = objectMapper.getSerializerProvider();
 serializerProvider.setNullSerializers(FindNullSerializers.instance);
 jsonGeneratorMock = mock(JsonGenerator.class);
 anyGetterWriter = new AnyGetterWriter(createBeanPropertyMock(), createAnnotatedMemberMock(), new MapSerializer(serializerProvider, null));
 }

 @Test
 public void testGetAndSerialize() throws Exception {
 Object bean = new HashMap<>();
 Object value = new HashMap<>();
 value.put("key1", "value1");
 value.put("key2", "value2");
 when(createBeanPropertyMock().getValue(bean)).thenReturn(value);

 anyGetterWriter.getAndSerialize(bean, jsonGeneratorMock, serializerProvider);

 verify(jsonGeneratorMock, times(1)).writeStartObject();
 verify(jsonGeneratorMock, times(2)).writeFieldName(eq("key1"));
 verify(jsonGeneratorMock, times(1)).writeString("value1");
 verify(jsonGeneratorMock, times(1)).writeFieldName(eq("key2"));
 verify(jsonGeneratorMock, times(1)).writeString("value2");
 verify(jsonGeneratorMock, times(1)).writeEndObject();
 }

 @Test
 public void testGetAndSerializeWithNullValue() throws Exception {
 Object bean = new HashMap<>();
 when(createBeanPropertyMock().getValue(bean)).thenReturn(null);

 anyGetterWriter.getAndSerialize(bean, jsonGeneratorMock, serializerProvider);

 verify(jsonGeneratorMock, never()).writeStartObject();
 }

 @Test
 public void testGetAndSerializeWithNonMapValue() throws Exception {
 Object bean = new HashMap<>();
 Object value = new String("not a map");
 when(createBeanPropertyMock().getValue(bean)).thenReturn(value);

 try {
 anyGetterWriter.getAndSerialize(bean, jsonGeneratorMock, serializerProvider);
 fail("Expected JsonMappingException");
 } catch (JsonMappingException e) {
 // expected
 }
 }

 @Test
 public void testGetAndFilter() throws Exception {
 Object bean = new HashMap<>();
 Object value = new HashMap<>();
 value.put("key1", "value1");
 value.put("key2", "value2");
 when(createBeanPropertyMock().getValue(bean)).thenReturn(value);

 PropertyFilter filter = mock(PropertyFilter.class);
 when(filter.include("key1")).thenReturn(true);
 when(filter.include("key2")).thenReturn(false);

 anyGetterWriter.getAndFilter(bean, jsonGeneratorMock, serializerProvider, filter);

 verify(jsonGeneratorMock, never()).writeFieldName("key2");
 }

 @Test
 public void testGetAndFilterWithNullValue() throws Exception {
 Object bean = new HashMap<>();
 when(createBeanPropertyMock().getValue(bean)).thenReturn(null);

 PropertyFilter filter = mock(PropertyFilter.class);
 when(filter.include("key1")).thenReturn(true);
 when(filter.include("key2")).thenReturn(false);

 anyGetterWriter.getAndFilter(bean, jsonGeneratorMock, serializerProvider, filter);

 verify(jsonGeneratorMock, never().writeStartObject());
 }

 @Test
 public void testGetAndFilterWithNonMapValue() throws Exception {
 Object bean = new HashMap<>();
 Object value = new String("not a map");
 when(createBeanPropertyMock().getValue(bean)).thenReturn(value);

 PropertyFilter filter = mock(PropertyFilter.class);
 when(filter.include("key1")).thenReturn(true);
 when(filter.include("key2")).thenReturn(false);
 try {
 anyGetterWriter.getAndFilter(bean, jsonGeneratorMock, serializerProvider, filter);
 fail("Expected JsonMappingException");
 } catch (JsonMappingException e) {
 // expected
 }
 }

 @Test
 public void testResolve() throws Exception {
 ObjectMapper objectMapperSpy = spy(objectMapper);
 MapSerializer mapSerializer = new MapSerializer(serializerProvider, null);
 when(objectMapperSpy.handlePrimaryContextualization(mapSerializer, anyInstance())).thenReturn(mapSerializer);

 anyGetterWriter.resolve(objectMapperSpy.getSerializerProvider());

 verify(objectMapperSpy).handlePrimaryContextualization(eq(mapSerializer), anyInstance());
 }

 private BeanProperty createBeanPropertyMock() {
 return mock(BeanProperty.class);
 }

 private AnnotatedMember createAnnotatedMemberMock() {
 return mock(AnnotatedMember.class);
 }
}