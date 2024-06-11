import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.std.MapSerializer;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class AnyGetterWriterTest {

 private ObjectMapper mapper;
 private AnyGetterWriter writer;

 @Before
 public void setup() {
 mapper = new ObjectMapper();
 AnnotatedClass ac = mapper.getTypeFactory().constructType(AnyGetterWriter.class);
 ObjectNode testObject = mapper.createObjectNode();
 testObject.put("key1", "value1");
 testObject.put("key2", 2);
 AnnotatedMember accessor = ac.findMember("_accessor");
 writer = new AnyGetterWriter(mapper.findProperty("testProperty", testObject), accessor, mapper.filterMapper().findSerializer(Map.class));
 }

 @Test
 public void testGetAndSerialize() throws JsonMappingException, JsonGenerationException {
 Object bean = mapper.readerForUpdating(Object.class).readValue(mapper.writeValueAsString(testObject));
 JsonGenerator gen = mapper.getFactory().createGenerator(new ByteArrayOutputStream(), mapper.getSerializerProvider());
 writer.getAndSerialize(bean, gen, mapper.getSerializerProvider());
 ObjectNode result = mapper.readTree(gen.getOutputStream());
 assertEquals("{\"key1\":\"value1\",\"key2\":2}", mapper.writerWithDefaultPrettyPrinter().writeValueAsString(result));
 }

 @Test
 public void testGetAndFilter() throws JsonMappingException, JsonGenerationException {
 PropertyFilter filter = new FilterProvider().withPreFilters(new JsonFilter("testFilter") {
 @Override
 public boolean filterOut(SerializerProvider provider, Serializer serializer, Object bean, TypeSerializer valueTypeSerializer) {
 return false;
 }
 });
 Object bean = mapper.readerForUpdating(Object.class).readValue(mapper.writeValueAsString(testObject));
 JsonGenerator gen = mapper.getFactory().createGenerator(new ByteArrayOutputStream(), mapper.getSerializerProvider());
 writer.getAndFilter(bean, gen, mapper.getSerializerProvider(), filter);
 ObjectNode result = mapper.readTree(gen.getOutputStream());
 assertTrue(result.isEmpty());
 }

 @Test(expected = JsonMappingException.class)
 public void testGetAndSerializeWithNullValue() throws JsonMappingException, JsonGenerationException {
 Object bean = new Object();
 writer.getAndSerialize(bean, null, mapper.getSerializerProvider());
 }

 @Test(expected = JsonMappingException.class)
 public void testGetAndSerializeWithNonMapValue() throws JsonMappingException, JsonGenerationException {
 Object bean = new Object() {{
 testProperty = new int[1];
 }};
 writer.getAndSerialize(bean, null, mapper.getSerializerProvider());
 }

 @Test
 public void testResolve() {
 MapSerializer mapSerializer = (MapSerializer) mapper.getSerializerProvider().findValueSerializer(Map.class);
 WriterProvider wp = mapper.getSerializerProvider().getWriterProvider();
 AnyGetterWriter newWriter = new AnyGetterWriter(mapper.findProperty("testProperty2", new Object()), mapper.findMember("_accessor2"), mapper.filterMapper().findSerializer(Map.class));
 newWriter.resolve(mapper.getSerializerProvider());
 MapSerializer newMapSerializer = (MapSerializer) newWriter.getMapSerializer();
 assertEquals(mapSerializer, newMapSerializer);
 }
}