import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class AnyGetterWriterTest {

 private ObjectNode testObject;
 private AnyGetterWriter anyGetterWriter;

 @Before
 public void setup() throws JsonProcessingException {
 testObject = ObjectMapperBuilder.json().build()
 .valueFactory()
 .objectNodeBuilder()
 .build();
 testObject.put("key1", "value1");
 testObject.put("key2", "value2");
 }

 @Test
 public void testGetAndSerialize() throws Exception {
 Object bean = testObject.asTree().asTokens().next().getValueAsTree();
 ObjectMapper mapper = new ObjectMapper();
 AnyGetterWriter anyGetterWriter = new AnyGetterWriter(
 new BeanProperty(testObject.getClass(), "asMap"),
 new AnnotatedMember(testObject.getClass().getMethod("getAnyMap")),
 mapper.getSerializerProvider().findValueSerializer(Map.class)
 );
 JsonGenerator gen = mapper.getFactory().createGenerator(new ByteArrayOutputStream(), null);
 anyGetterWriter.getAndSerialize(bean, gen, mapper.getSerializerProvider());
 String json = gen.getOutputString();
 JsonFactory factory = new JsonFactory();
 JsonParser parser = factory.createParser(new ByteArrayInputStream(json.getBytes()));
 ObjectMapper mapper2 = new ObjectMapper();
 Map<String, Object> result = mapper2.readValue(parser, Map.class);
 assertEquals(testObject, result);
 }

 @Test(expected = JsonMappingException.class)
 public void testGetAndSerializeWithNullValue() throws Exception {
 Object bean = new Object();
 AnyGetterWriter anyGetterWriter = new AnyGetterWriter(
 new BeanProperty(Object.class, "anyGetter"),
 new AnnotatedMember(bean.getClass().getMethod("getAnyGetter")),
 null
 );
 JsonGenerator gen = null; // this will cause an exception when invoked
 anyGetterWriter.getAndSerialize(bean, gen, null);
 }

 @Test(expected = JsonMappingException.class)
 public void testGetAndSerializeWithNonMapValue() throws Exception {
 Object bean = new Object();
 AnyGetterWriter anyGetterWriter = new AnyGetterWriter(
 new BeanProperty(Object.class, "anyGetter"),
 new AnnotatedMember(bean.getClass().getMethod("getAnyGetter")),
 null
 );
 Object value = new Object();
 anyGetterWriter._accessor.setValue(bean, value);
 JsonGenerator gen = null; // this will cause an exception when invoked
 anyGetterWriter.getAndSerialize(bean, gen, null);
 }

 @Test
 public void testGetAndFilter() throws Exception {
 ObjectMapper mapper = new ObjectMapper();
 Object bean = testObject.asTree().asTokens().next().getValueAsTree();
 PropertyFilter filter = new FilterProvider().filter("value", null);
 AnyGetterWriter anyGetterWriter = new AnyGetterWriter(
 new BeanProperty(testObject.getClass(), "asMap"),
 new AnnotatedMember(testObject.getClass().getMethod("getAnyMap")),
 mapper.getSerializerProvider().findValueSerializer(Map.class)
 );
 JsonGenerator gen = mapper.getFactory().createGenerator(new ByteArrayOutputStream(), null);
 anyGetterWriter.getAndFilter(bean, gen, mapper.getSerializerProvider(), filter);
 String json = gen.getOutputString();
 ObjectMapper mapper2 = new ObjectMapper();
 Map<String, Object> result = mapper2.readValue(json, Map.class);
 assertEquals(1, result.size());
 assertTrue(result.containsKey("key1"));
 assertFalse(result.containsKey("key2"));
 }

 @Test
 public void testResolve() throws Exception {
 ObjectMapper mapper = new ObjectMapper();
 AnyGetterWriter anyGetterWriter = new AnyGetterWriter(
 new BeanProperty(testObject.getClass(), "asMap"),
 new AnnotatedMember(testObject.getClass().getMethod("getAnyMap")),
 mapper.getSerializerProvider().findValueSerializer(Map.class)
 );
 SerializerProvider provider = mapper.getSerializerProvider();
 anyGetterWriter.resolve(provider);
 ObjectSerializer mapSerializer = provider.findValueSerializer(Map.class);
 assertSame(anyGetterWriter._mapSerializer, mapSerializer);
 }
}