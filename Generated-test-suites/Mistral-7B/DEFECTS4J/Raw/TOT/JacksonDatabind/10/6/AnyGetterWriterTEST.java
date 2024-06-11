import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import static com.fasterxml.jackson.core.Version.VersionValue.VERSION_2_13;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.std.MapSerializer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class AnyGetterWriterTest {

 private ObjectMapper mapper;
 private AnyGetterWriter writer;

 @Before
 public void setup() {
 mapper = new ObjectMapper();
 mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
 mapper.setVersion(VERSION_2_13);
 writer = new AnyGetterWriter(mapper.findProperty("testBean", "anyMap"),
 mapper.findNamedValue("testBean", "anyMapGetter"), new MapSerializer(mapper.getFactory(), MapSerializer.StdSerializer.STD_SERIALIZER));
 }

 @Test
 public void testGetAndSerialize() throws Exception {
 Object bean = new TestBean();
 JsonGenerator gen = mapper.getFactory().createGenerator(new ByteArrayOutputStream(), 0);
 writer.getAndSerialize(bean, gen, mapper.getSerializerProvider());
 JsonParser parser = mapper.getFactory().createParser(new ByteArrayInputStream(gen.getOutputBuffer().toByteArray()));
 ObjectNode node = mapper.readValue(parser, ObjectNode.class);
 Map<String, Object> result = node.deepDeserializeValueAsTree();
 assertEquals("expectedMap", result);
 }

 @Test
 public void testGetAndFilter() throws Exception {
 Object bean = new TestBean();
 ObjectMapper filterMapper = new ObjectMapper();
 filterMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
 filterMapper.setVersion(VERSION_2_13);
 PropertyFilter filter = new FilterTest();
 JsonGenerator gen = mapper.getFactory().createGenerator(new ByteArrayOutputStream(), 0);
 writer.getAndFilter(bean, gen, mapper.getSerializerProvider(), filter);
 JsonParser parser = mapper.getFactory().createParser(new ByteArrayInputStream(gen.getOutputBuffer().toByteArray()));
 ObjectNode node = filterMapper.readValue(parser, ObjectNode.class);
 Map<String, Object> result = node.deepDeserializeValueAsTree();
 assertNull("filteredMap should be empty", result);
 }

 @Test(expected = JsonMappingException.class)
 public void testGetAndSerializeInvalidType() throws Exception {
 Object bean = new TestBean();
 writer.getAndSerialize(bean, mapper.getFactory().createGenerator(new ByteArrayOutputStream(), 0), mapper.getSerializerProvider());
 }

 @Test
 public void testResolve() throws Exception {
 ObjectMapper resolvedMapper = new ObjectMapper();
 resolvedMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
 resolvedMapper.setVersion(VERSION_2_13);
 SerializerProvider provider = mapper.getSerializerProvider();
 AnyGetterWriter resolvedWriter = new AnyGetterWriter(mapper.findProperty("testBean", "anyMap"),
 mapper.findNamedValue("testBean", "anyMapGetter"), null);
 resolvedWriter.resolve(provider);
 ObjectMapper resolvedObjectMapper = (ObjectMapper) provider.getActiveValueWriter("ObjectMapper");
 assertEquals(mapper, resolvedObjectMapper);
 }

 @Test(expected = NullPointerException.class)
 public void testNullProperty() {
 AnyGetterWriter nullWriter = new AnyGetterWriter(null, mapper.findNamedValue("testBean", "anyMapGetter"), new MapSerializer(mapper.getFactory(), MapSerializer.StdSerializer.STD_SERIALIZER));
 }
}

class TestBean {
 @JsonAnyGetter
 public Map<String, Object> anyMap;
}

class FilterTest implements PropertyFilter {
 @Override
 public void serializeValue(Map.Entry<String, ?> entry, JsonGenerator gen, SerializerProvider serializers) throws IOException {
 fail("Should not be called");
 }
}