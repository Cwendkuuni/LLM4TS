import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.ser.std.MapSerializer;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.TestBase;
import com.fasterxml.jackson.databind.util.TestObject;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class AnyGetterWriterTest extends TestBase {

 private AnyGetterWriter writer;
 private JsonGenerator gen;
 private SerializerProvider provider;
 private Object bean;

 @Before
 public void setup() throws Exception {
 ObjectMapper mapper = new ObjectMapper();
 provider = mapper.getSerializerProvider();
 gen = createMock(JsonGenerator.class);
 bean = new TestObject();
 AnnotatedMember accessor = mock(AnnotatedMember.class);
 when(accessor.getName()).thenReturn("anyGetter");
 when(accessor.getValue(bean)).thenReturn(new HashMap<>());
 BeanProperty property = mock(BeanProperty.class);
 when(property.getName()).thenReturn("anyProperty");
 writer = new AnyGetterWriter(property, accessor, null);
 }

 @Test
 public void testGetAndSerialize() throws Exception {
 Map<String, Object> map = new HashMap<>();
 map.put("key1", "value1");
 map.put("key2", "value2");
 when(accessor.getValue(bean)).thenReturn(map);
 writer.getAndSerialize(bean, gen, provider);
 ObjectWriter writer = mapper.writer(new DefaultSerializerProvider());
 Map<String, Object> expected = new LinkedHashMap<>();
 expected.put("key1", "value1");
 expected.put("key2", "value2");
 String json = writer.writeValueAsString(expected);
 verify(gen).writeStartObject();
 verify(gen).writeFieldName("key1");
 verify(gen).writeString("value1");
 verify(gen).writeFieldName("key2");
 verify(gen).writeString("value2");
 verify(gen).writeEndObject();
 }

 @Test
 public void testGetAndFilter() throws Exception {
 PropertyFilter filter = mock(PropertyFilter.class);
 when(filter.include(eq("key1"))).thenReturn(true);
 when(filter.include(eq("key2"))).thenReturn(false);
 Map<String, Object> map = new HashMap<>();
 map.put("key1", "value1");
 map.put("key2", "value2");
 map.put("key3", "value3");
 when(accessor.getValue(bean)).thenReturn(map);
 writer.getAndFilter(bean, gen, provider, filter);
 String json = mapper.writeValueAsString(map);
 // check that only key1 is included in the output
 JsonParser parser = mapper.getFactory().createParser(json);
 Map<String, Object> readValue = mapper.readValue(parser, Map.class);
 assertEquals(1, readValue.size());
 assertEquals("value1", readValue.get("key1"));
 }

 @Test(expected = JsonMappingException.class)
 public void testInvalidValueType() throws Exception {
 when(accessor.getValue(bean)).thenReturn(42);
 writer.getAndSerialize(bean, gen, provider);
 }

 @Test(expected = JsonMappingException.class)
 public void testInvalidMapSerializer() throws Exception {
 writer = new AnyGetterWriter(property, accessor, null);
 writer.resolve(provider);
 when(provider.handlePrimaryContextualization(any(MapSerializer.class), any(BeanProperty.class)))
 .thenThrow(new JsonMappingException("Test error"));
 writer.getAndSerialize(bean, gen, provider);
 }

 @Test
 public void testJsonInclude() throws Exception {
 JsonInclude.Include include = JsonInclude.Include.NON_NULL;
 ObjectMapper mapper = new ObjectMapper();
 mapper.setSerializationInclusion(include);
 SerializerProvider provider = mapper.getSerializerProvider();
 MapSerializer mapSerializer = new MapSerializer(ObjectMapSerializer.Std.instance, null, null, null, null, null, null, null, null);
 provider.setPrimaryContextualizationHandler(mapSerializer);
 writer = new AnyGetterWriter(property, accessor, mapSerializer);
 writer.resolve(provider);
 Map<String, Object> map = new HashMap<>();
 map.put("key1", "value1");
 map.put("key2", null);
 map.put("key3", "value3");
 when(accessor.getValue(bean)).thenReturn(map);
 writer.getAndFilter(bean, gen, provider, filter);
 String json = mapper.writeValueAsString(map);
 // check that only key1 and key3 are included in the output
 JsonParser parser = mapper.getFactory().createParser(json);
 Map<String, Object> readValue = mapper.readValue(parser, Map.class);
 assertEquals(3, readValue.size());
 List<String> keys = new ArrayList<>(readValue.keySet());
 assertTrue(keys.contains("key1"));
 assertTrue(keys.contains("key3"));
 assertFalse(keys.contains("key2"));
 }
}