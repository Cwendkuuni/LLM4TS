import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.ser.std.MapSerializer;
import com.fasterxml.jackson.databind.ser.test.BeanPropertyWriterTestBase;
import static org.junit.Assert.*;

public class AnyGetterWriterTest extends BeanPropertyWriterTestBase {

 private AnyGetterWriter _anyGetterWriter;
 private Object _testBean;
 private AnnotatedMember _testAccessor;
 private MapSerializer _mapSerializer;

 @Before
 public void setUp() throws Exception {
 _testBean = createTestBean();
 _testAccessor = Mockito.spy(getTestProperty());
 _mapSerializer = Mockito.spy(new MapSerializer(objectMapper.getFactory(), null));
 _anyGetterWriter = new AnyGetterWriter(_property, _testAccessor, _mapSerializer);
 }

 @Test
 public void testGetAndSerialize() throws Exception {
 Object value = new HashMap<>();
 _testAccessor.setValue(_testBean, value);
 JavaType valueType = mapType(Map.class, String.class, String.class);
 JsonGenerator gen = createJsonGenerator();
 SerializerProvider provider = createSerializerProvider();
 _anyGetterWriter.getAndSerialize(_testBean, gen, provider);
 Map<String, Object> serializedMap = mapper.readValue(gen.getOutputStream(), valueType);
 assertTrue(serializedMap.isEmpty());

 value.put("key1", "value1");
 value.put("key2", "value2");
 gen.reset();
 _anyGetterWriter.getAndSerialize(_testBean, gen, provider);
 Map<String, Object> expectedMap = new HashMap<>();
 expectedMap.put("key1", "value1");
 expectedMap.put("key2", "value2");
 Map<String, Object> serializedMap2 = mapper.readValue(gen.getOutputStream(), valueType);
 assertEquals(expectedMap, serializedMap2);
 }

 @Test(expected = JsonMappingException.class)
 public void testGetAndSerializeWithNullValue() throws Exception {
 _testAccessor.setValue(_testBean, null);
 _anyGetterWriter.getAndSerialize(_testBean, mockJsonGenerator(), mockSerializerProvider());
 }

 @Test(expected = JsonMappingException.class)
 public void testGetAndSerializeWithNonMapValue() throws Exception {
 Object nonMapValue = new Object();
 _testAccessor.setValue(_testBean, nonMapValue);
 _anyGetterWriter.getAndSerialize(_testBean, mockJsonGenerator(), mockSerializerProvider());
 }

 @Test
 public void testGetAndFilter() throws Exception {
 Object value = new HashMap<>();
 value.put("key1", "value1");
 value.put("key2", "value2");
 _testAccessor.setValue(_testBean, value);
 PropertyFilter filter = Mockito.mock(PropertyFilter.class);
 JavaType valueType = mapType(Map.class, String.class, String.class);
 JsonGenerator gen = createJsonGenerator();
 SerializerProvider provider = createSerializerProvider();
 _anyGetterWriter.getAndFilter(_testBean, gen, provider, filter);
 Map<String, Object> serializedMap = mapper.readValue(gen.getOutputStream(), valueType);
 assertTrue(serializedMap.isEmpty());

 PropertyFilter.FilterProvider filterProvider = Mockito.mock(PropertyFilter.FilterProvider.class);
 filter.setFilterProvider(filterProvider);
 gen.reset();
 _anyGetterWriter.getAndFilter(_testBean, gen, provider, filter);
 Map<String, Object> expectedMap = new HashMap<>();
 expectedMap.put("key1", "value1");
 Map<String, Object> serializedMap2 = mapper.readValue(gen.getOutputStream(), valueType);
 assertEquals(expectedMap, serializedMap2);
 }

 @Test(expected = JsonMappingException.class)
 public void testGetAndFilterWithNullValue() throws Exception {
 _testAccessor.setValue(_testBean, null);
 _anyGetterWriter.getAndFilter(_testBean, mockJsonGenerator(), mockSerializerProvider(), Mockito.mock(PropertyFilter.class));
 }

 @Test(expected = JsonMappingException.class)
 public void testGetAndFilterWithNonMapValue() throws Exception {
 Object nonMapValue = new Object();
 _testAccessor.setValue(_testBean, nonMapValue);
 _anyGetterWriter.getAndFilter(_testBean, mockJsonGenerator(), mockSerializerProvider(), Mockito.mock(PropertyFilter.class));
 }

 @Test
 public void testResolve() throws Exception {
 SerializerProvider provider = createSerializerProvider();
 _anyGetterWriter.resolve(provider);
 assertNotNull(_anyGetterWriter.getMapSerializer());
 }

 @Test(expected = JsonMappingException.class)
 public void testResolveWithNullMapSerializer() throws Exception {
 SerializerProvider provider = Mockito.spy(new SerializerProvider() {});
 _anyGetterWriter.resolve(provider);
 _anyGetterWriter.getMapSerializer();
 Mockito.verify(provider, Mockito.times(1)).handlePrimaryContextualization(Mockito.any(MapSerializer.class), Mockito.any(BeanProperty.class));
 _anyGetterWriter.getMapSerializer();
 }
}