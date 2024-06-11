import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.Test;
import static org.junit.Assert.*;

public class StdKeySerializerTest {

 @Test
 public void testSerializeDate() throws JsonGenerationException, JsonMappingException {
 ObjectMapper mapper = new ObjectMapper();
 Date date = new Date();
 StdKeySerializer serializer = new StdKeySerializer();

 ObjectNode resultNode = mapper.treeParser().readValueAsTree(mapper.writeValueAsString(serializer, date));
 String key = resultNode.path("@class").asText();
 String expectedKey = "java.util.Date";
 assertEquals(expectedKey, key);
 }

 @Test
 public void testSerializeString() throws JsonGenerationException, JsonMappingException {
 ObjectMapper mapper = new ObjectMapper();
 String value = "testValue";
 StdKeySerializer serializer = new StdKeySerializer();

 ObjectNode resultNode = mapper.treeParser().readValueAsTree(mapper.writeValueAsString(serializer, value));
 String key = resultNode.path("@class").asText();
 String expectedKey = "java.lang.String";
 assertEquals(expectedKey, key);
 }

 @Test
 public void testSerializeNull() throws JsonGenerationException, JsonMappingException {
 ObjectMapper mapper = new ObjectMapper();
 StdKeySerializer serializer = new StdKeySerializer();

 ObjectNode resultNode = mapper.treeParser().readValueAsTree(mapper.writeValueAsString(serializer, null));
 assertNull(resultNode.path("@class").asText());
 }

 @Test(expected = JsonMappingException.class)
 public void testSerializeInvalidType() throws JsonGenerationException, JsonMappingException {
 ObjectMapper mapper = new ObjectMapper();
 int value = 1;
 StdKeySerializer serializer = new StdKeySerializer();

 ObjectNode resultNode = mapper.treeParser().readValueAsTree(mapper.writeValueAsString(serializer, value));
 fail("Expected JsonMappingException when serializing invalid type: java.lang.Integer");
 }

 @Test
 public void testGetSchema() throws JsonMappingException {
 ObjectMapper mapper = new ObjectMapper();
 StdKeySerializer serializer = new StdKeySerializer();

 ObjectNode schemaNode = serializer.getSchema(mapper.getSerializerProvider(), Object.class.getType());
 String type = schemaNode.path("type").asText();
 String expectedType = "string";
 assertEquals(expectedType, type);
 }

 @Test
 public void testAcceptJsonFormatVisitor() {
 // JsonFormatVisitorWrapper does not have a public constructor, so we can't test this method directly
}
}