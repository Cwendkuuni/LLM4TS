import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class StdKeySerializerTest {

 private StdKeySerializer serializer;
 private ObjectMapper mapper;

 @Before
 public void setup() {
 serializer = new StdKeySerializer();
 mapper = new ObjectMapper();
 }

 @Test
 public void testSerializeDate() throws JsonProcessingException {
 Date date = new Date();
 JsonGenerator jgen = mapper.getFactory().createGenerator(new StringWriter());
 serializer.serialize(date, jgen, mapper.getSerializerProvider());
 JsonParser parser = mapper.getFactory().createParser(jgen.getOutputStream().toString());
 assertEquals("\"\"", parser.getTextValue()); // Quotes are added by the default serializeDateKey method
 }

 @Test
 public void testSerializeString() throws JsonProcessingException {
 ObjectNode rootNode = mapper.createObjectNode();
 rootNode.put("key", "value");
 JsonGenerator jgen = mapper.getFactory().createGenerator(new StringWriter());
 serializer.serialize(rootNode.toString(), jgen, mapper.getSerializerProvider());
 JsonParser parser = mapper.getFactory().createParser(jgen.getOutputStream().toString());
 JsonNode actualNode = parser.readValueAsTree();
 JsonNode expectedNode = mapper.readTree("{\"key\":\"value\"}");
 assertEquals(expectedNode, actualNode);
 }

 @Test(expected = JsonProcessingException.class)
 public void testSerializeNull() throws JsonProcessingException {
 JsonGenerator jgen = mapper.getFactory().createGenerator(new StringWriter());
 serializer.serialize(null, jgen, mapper.getSerializerProvider());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSerializeNonSerializableObject() {
 assertThrows(IllegalArgumentException.class, () -> serializer.serialize(new Object() {}, mapper.getFactory().createGenerator(new StringWriter()), mapper.getSerializerProvider()));
 }

 @Test
 public void testGetSchema() throws JsonMappingException {
 JsonNode schemaNode = serializer.getSchema(mapper.getSerializerProvider(), Object.class);
 JsonNode expectedNode = mapper.createObjectNode().putPOJO("type", "string");
 assertEquals(expectedNode, schemaNode);
 }

 @Test
 public void testAcceptJsonFormatVisitor() throws JsonMappingException {
 JsonFormatVisitorWrapper visitor = mapper.getSerializerProvider().getDefaultVisibilityChecker().acceptJsonFormatVisitor(SerializerProvider.StandardSerializers.STRING_SERIALIZER, Object.class);
 serializer.acceptJsonFormatVisitor(visitor, Object.class);
 visitor.close();
 }
}