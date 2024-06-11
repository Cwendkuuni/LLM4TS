import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class AnyGetterWriterTest {

 private ObjectMapper mapper;
 private AnyGetterWriter writer;
 private TestBean bean;

 @Before
 public void setup() {
 mapper = new ObjectMapper();
 bean = new TestBean();
 writer = new AnyGetterWriter(mapper.findBeanProperty(_TestBean.class, "mapProperty"),
 mapper.findAnnotatedMember(_TestBean.class, "getMapProperty"),
 mapper.getDefaultInstance(MapSerializer.class));
 }

 @Test
 public void testGetAndSerialize_ValidMap() throws Exception {
 ObjectNode expected = mapper.readTree("{\"key1\": \"value1\", \"key2\": \"value2\"}");
 JsonGenerator gen = new JsonFactory().createGenerator(new ByteArrayOutputStream(), mapper.getSerializerProvider());
 writer.getAndSerialize(bean, gen, mapper.getSerializerProvider());
 ObjectNode actual = mapper.readTree(gen.getOutputStreamAsString());
 assertEquals(expected, actual);
 }

 @Test(expected = JsonMappingException.class)
 public void testGetAndSerialize_InvalidType() throws Exception {
 ObjectNode invalidBean = mapper.readTree("{\"invalidType\": 123}");
 writer.getAndSerialize(invalidBean, new JsonGenerator(new ByteArrayOutputStream(), mapper.getSerializerProvider()), mapper.getSerializerProvider());
 fail("Expected JsonMappingException");
 }

 @Test
 public void testGetAndFilter_ValidMap() throws Exception {
 ObjectNode expected = mapper.createObjectNode();
 expected.put("key1", "value1");
 JsonGenerator gen = new JsonFactory().createGenerator(new ByteArrayOutputStream(), mapper.getSerializerProvider());
 writer.getAndFilter(bean, gen, mapper.getSerializerProvider(), new SimpleFilterProvider().emptyFilterProvider());
 ObjectNode actual = mapper.readTree(gen.getOutputStreamAsString());
 assertEquals(expected, actual);
 }

 @Test
 public void testGetAndFilter_EmptyMap() throws Exception {
 ObjectNode expected = mapper.createObjectNode();
 JsonGenerator gen = new JsonFactory().createGenerator(new ByteArrayOutputStream(), mapper.getSerializerProvider());
 writer.getAndFilter(bean, gen, mapper.getSerializerProvider(), new SimpleFilterProvider().emptyFilterProvider());
 ObjectNode actual = mapper.readTree(gen.getOutputStreamAsString());
 assertTrue(actual.isNull());
 }

 @Test
 public void testResolve() throws Exception {
 MapSerializer serializer = mapper.getDefaultInstance(MapSerializer.class);
 AnyGetterWriter newWriter = new AnyGetterWriter(mapper.findBeanProperty(_TestBean.class, "mapProperty"),
 mapper.findAnnotatedMember(_TestBean.class, "getMapProperty"), serializer);
 assertSame(serializer, newWriter._mapSerializer);
 }

}

class TestBean {
 private Map<String, String> mapProperty;

 public Map<String, String> getMapProperty() {
 return mapProperty;
 }

 public void setMapProperty(Map<String, String> mapProperty) {
 this.mapProperty = mapProperty;
 }
}