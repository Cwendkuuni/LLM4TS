import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class StdSerializerTest {

 private ObjectMapper objectMapper;
 private StdSerializer<String> stringSerializer;

 @Before
 public void setup() {
 objectMapper = new ObjectMapper();
 stringSerializer = new StdSerializer<String>(String.class);
 }

 @Test
 public void testSerializeNormalString() throws JsonProcessingException {
 String input = "Hello, World!";
 ObjectNode output = objectMapper.readValue(stringSerializer.serialize(input, new JsonGenerator(new StringWriter()), objectMapper.getSerializerProvider()), ObjectNode.class);
 assertEquals("\"Hello, World!\"", output.toString());
 }

 @Test
 public void testSerializeNull() throws JsonProcessingException {
 ObjectNode output = objectMapper.readValue(stringSerializer.serialize(null, new JsonGenerator(new StringWriter()), objectMapper.getSerializerProvider()), ObjectNode.class);
 assertNull(output);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSerializeInvalidType() {
 ObjectNode output;
 try {
 output = objectMapper.readValue(stringSerializer.serialize(new Object(), new JsonGenerator(new StringWriter()), objectMapper.getSerializerProvider()), ObjectNode.class);
 fail();
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test
 public void testGetSchemaNormalString() throws JsonMappingException {
 ObjectNode expectedSchema = objectMapper.createObjectNode();
 expectedSchema.put("type", "string");
 ObjectNode actualSchema = stringSerializer.getSchema(objectMapper.getSerializerProvider(), String.class);
 assertEquals(expectedSchema.toString(), actualSchema.toString());
 }

 @Test
 public void testGetSchemaOptionalString() throws JsonMappingException {
 ObjectNode expectedSchema = objectMapper.createObjectNode();
 expectedSchema.put("type", "string");
 expectedSchema.put("required", false);
 ObjectNode actualSchema = stringSerializer.getSchema(objectMapper.getSerializerProvider(), String.class, true);
 assertEquals(expectedSchema.toString(), actualSchema.toString());
 }

 @Test(expected = JsonMappingException.class)
 public void testGetSchemaInvalidType() throws JsonMappingException {
 stringSerializer = new StdSerializer<Object>(Object.class);
 ObjectNode actualSchema = stringSerializer.getSchema(objectMapper.getSerializerProvider(), String.class);
 fail();
 }

 @Test
 public void testAcceptJsonFormatVisitor() throws JsonMappingException {
 JsonFormatVisitorWrapper visitor = new JsonFormatVisitorWrapper(objectMapper.getSerializerProvider().getSerializerProvider()) {
 @Override
 public void visitObject(JsonFormatVisitorWrapper.ObjectFormatVisitor visitor) throws JsonMappingException {
 fail();
 }
 };
 stringSerializer.acceptJsonFormatVisitor(visitor, null);
 }

 @Test
 public void testWrapAndThrowNull() throws IOException {
 SerializerProvider provider = new BaseSerializerProvider();
 try {
 stringSerializer.wrapAndThrow(provider, null, null, 0);
 fail();
 } catch (JsonMappingException e) {
 // Expected
 }
 }

 @Test
 public void testWrapAndThrowEmptyList() throws IOException {
 ObjectNode bean = createEmptyListNode();
 try {
 stringSerializer.wrapAndThrow(objectMapper.getSerializerProvider(), new IOException("Test error"), bean, 0);
 fail();
 } catch (JsonMappingException e) {
 // Expected
 }
 }

 @Test(expected = JsonMappingException.class)
 public void testWrapAndThrowNullBean() throws IOException {
 try {
 stringSerializer.wrapAndThrow(objectMapper.getSerializerProvider(), new IOException("Test error"), null, 0);
 fail();
 } catch (JsonMappingException e) {
 // Expected
 }
 }

 @Test
 public void testIsDefaultSerializer() {
 assertTrue(StdSerializer.class.isInstance(stringSerializer));
 assertFalse(MyCustomSerializer.class.isInstance(stringSerializer));
 }

 private ObjectNode createEmptyListNode() throws JsonProcessingException {
 ObjectMapper emptyListMapper = new ObjectMapper();
 ObjectNode emptyList = emptyListMapper.createValueTreeFactory().getArrayNode();
 return emptyListMapper.valueToTree(emptyList);
 }

}