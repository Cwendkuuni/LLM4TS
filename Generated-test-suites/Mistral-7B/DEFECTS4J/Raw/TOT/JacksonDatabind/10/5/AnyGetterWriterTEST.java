import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.ser.std.MapSerializer;
import com.fasterxml.jackson.databind.ser.test.BeanPropertyWriterTestBase;
import com.fasterxml.jackson.databind.ser.test.MapSerializationTestBase;
import com.fasterxml.jackson.databind.util.TestObjectIdGenerator;

public class AnyGetterWriterTest extends BeanPropertyWriterTestBase {

 private AnyGetterWriter writer;
 private ObjectMapper mapper;

 @SuppressWarnings("unchecked")
 @Before
 public void setUp() throws Exception {
 mapper = new ObjectMapper();
 Object idGen = new TestObjectIdGenerator();
 writer = new AnyGetterWriter(new BeanProperty.Std(_propertyName, _ClassKey, _PropertyTypeKey, idGen),
 _accessorMock, (MapSerializer)_mapSerializerMock);
 }

 @Test
 public void testGetAndSerializeNullMap() throws Exception {
 Object bean = createTestBean();
 JsonGenerator gen = mock(JsonGenerator.class);
 SerializerProvider provider = mock(SerializerProvider.class);

 writer.getAndSerialize(bean, gen, provider);
 verify(gen, never()).writeStartObject();
 }

 @Test
 public void testGetAndSerializeEmptyMap() throws Exception {
 Object bean = createTestBean();
 Map<String, Object> map = new HashMap<>();
 JsonGenerator gen = mock(JsonGenerator.class);
 SerializerProvider provider = mock(SerializerProvider.class);

 writer.getAndSerialize(bean, gen, provider);
 verify(gen, times(1)).writeStartObject();
 verify(gen, times(1)).writeFieldName("{}");
 verify(gen, never()).writeEndObject();
 }

 @Test
 public void testGetAndSerializeMapWithValues() throws Exception {
 Object bean = createTestBean();
 Map<String, Object> map = new HashMap<>();
 map.put("key1", "value1");
 map.put("key2", "value2");
 JsonGenerator gen = mock(JsonGenerator.class);
 SerializerProvider provider = mock(SerializerProvider.class);

 writer.getAndSerialize(bean, gen, provider);
 verify(gen, times(1)).writeStartObject();
 verify(gen, times(1)).writeFieldName("key1");
 verify(gen, times(1)).writeString("value1");
 verify(gen, times(1)).writeFieldName("key2");
 verify(gen, times(1)).writeString("value2");
 verify(gen, times(1)).writeEndObject();
 }

 @Test(expected = JsonMappingException.class)
 public void testGetAndSerializeNonMapValue() throws Exception {
 Object bean = createTestBean();
 JsonGenerator gen = mock(JsonGenerator.class);
 SerializerProvider provider = mock(SerializerProvider.class);

 writer.getAndSerialize(bean, gen, provider);
 Object value = new Object();
 when(_accessorMock.getValue(bean)).thenReturn(value);

 writer.getAndSerialize(bean, gen, provider);
 verify(_accessorMock, times(1)).getValue(bean);
 verify(gen, never()).writeStartObject();
 throw new JsonMappingException("Value returned by 'any-getter' not java.util.Map");
 }

 @Test
 public void testGetAndFilterNullMap() throws Exception {
 Object bean = createTestBean();
 JsonGenerator gen = mock(JsonGenerator.class);
 SerializerProvider provider = mock(SerializerProvider.class);
 PropertyFilter filter = mock(PropertyFilter.class);

 writer.getAndFilter(bean, gen, provider, filter);
 verify(gen, never()).writeStartObject();
 }

 @Test
 public void testGetAndFilterEmptyMap() throws Exception {
 Object bean = createTestBean();
 Map<String, Object> map = new HashMap<>();
 JsonGenerator gen = mock(JsonGenerator.class);
 SerializerProvider provider = mock(SerializerProvider.class);
 PropertyFilter filter = mock(PropertyFilter.class);

 writer.getAndFilter(bean, gen, provider, filter);
 verify(gen, times(1)).writeStartObject();
 verify(gen, times(1)).writeFieldName("{}");
 verify(gen, never()).writeEndObject();
 }

 @Test
 public void testGetAndFilterMapWithValues() throws Exception {
 Object bean = createTestBean();
 Map<String, Object> map = new HashMap<>();
 map.put("key1", "value1");
 map.put("key2", "value2");
 JsonGenerator gen = mock(JsonGenerator.class);
 SerializerProvider provider = mock(SerializerProvider.class);
 PropertyFilter filter = mock(PropertyFilter.class);

 writer.getAndFilter(bean, gen, provider, filter);
 verify(gen, times(1)).writeStartObject();
 verify(gen, times(1)).writeFieldName("key1");
 verify(gen, times(1)).writeString("value1");
 verify(gen, times(1)).writeFieldName("key2");
 verify(gen, times(1)).writeString("value2");
 verify(gen, times(1)).writeEndObject();
 }

 @Test(expected = JsonMappingException.class)
 public void testGetAndFilterNonMapValue() throws Exception {
 Object bean = createTestBean();
 JsonGenerator gen = mock(JsonGenerator.class);
 SerializerProvider provider = mock(SerializerProvider.class);
 PropertyFilter filter = mock(PropertyFilter.class);

 writer.getAndFilter(bean, gen, provider, filter);
 Object value = new Object();
 when(_accessorMock.getValue(bean)).thenReturn(value);

 writer.getAndFilter(bean, gen, provider, filter);
 verify(_accessorMock, times(1)).getValue(bean);
 verify(gen, never()).writeStartObject();
 throw new JsonMappingException("Value returned by 'any-getter' not java.util.Map");
 }

 @Test
 public void testResolveNullSerializerProvider() throws Exception {
 Object idGen = new TestObjectIdGenerator();
 AnyGetterWriter writer = new AnyGetterWriter(new BeanProperty.Std(_propertyName, _ClassKey, _PropertyTypeKey, idGen),
 _accessorMock, null);

 writer.resolve(mock(SerializerProvider.class));
 verifyNoMoreInteractions(mapper);
 }

 @Test
 public void testResolveWithMapSerializer() throws Exception {
 Object idGen = new TestObjectIdGenerator();
 ObjectMapper mapperSpy = spy(mapper);
 AnyGetterWriter writer = new AnyGetterWriter(new BeanProperty.Std(_propertyName, _ClassKey, _PropertyTypeKey, idGen),
 _accessorMock, _mapSerializerMock);

 writer.resolve(mock(SerializerProvider.class));
 verify(mapperSpy, times(1)).handlePrimaryContextualization(_mapSerializerMock, _propertyMock);
 }

 @Test
 public void test_mapSerializerIsSetInConstructor() throws Exception {
 Object idGen = new TestObjectIdGenerator();
 AnyGetterWriter writer = new AnyGetterWriter(new BeanProperty.Std(_propertyName, _ClassKey, _PropertyTypeKey, idGen),
 _accessorMock, _mapSerializerMock);

 ObjectMapper mapper = mock(ObjectMapper.class);
 when(mapper.getSerializerProvider()).thenReturn(mock(SerializerProvider.class));
 when(mapper.findValueSerializer(_mapSerializerMock.getClass())).thenReturn(_mapSerializerMock);

 writer.resolve(mapper);
 verify(_mapSerializerMock, times(1)).serializeFields((Map<?,?>) anyObject(), any(JsonGenerator.class), any(SerializerProvider.class));
 }
}