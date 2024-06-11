import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class StdSerializerTest {

 private StdSerializer<String> stringSerializer;
 private StdSerializer<Integer> intSerializer;

 @Before
 public void setup() {
 stringSerializer = new StdSerializer<String>(String.class);
 intSerializer = new StdSerializer<Integer>(Integer.class);
 }

 // Test method for serialize() of StdSerializer<String>
 @Test
 public void testSerializeString() throws JsonProcessingException {
 String input = "Hello World";
 ObjectNode expectedOutput = createExpectedOutput("{\"@type\":\"String\",\"value\":\"Hello World\"}", String.class);
 ObjectNode actualOutput = mapper.readTree(new StringWriter());
 stringSerializer.serialize(input, mapper.getFactory().createGenerator(new StringWriter()), mapper.getSerializationConfig());
 stringSerializer.acceptJsonFormatVisitor(mapper.getSerializerProvider().getRootValueSerializer(), null);
 mapper.getSerializerProvider().defaultSerializeValue(null, mapper.getObjectWriter(), stringSerializer);
 mapper.readTree(new StringReader(actualOutput.toString()));
 assertEquals(expectedOutput, mapper.readTree(new StringReader(actualOutput.toString())));
 }

 // Test method for serialize() of StdSerializer<Integer>
 @Test
 public void testSerializeInteger() throws JsonProcessingException {
 Integer input = 123;
 ObjectNode expectedOutput = createExpectedOutput("{\"@type\":\"Number\",\"value\":123}", Integer.class);
 ObjectNode actualOutput = mapper.readTree(new StringWriter());
 intSerializer.serialize(input, mapper.getFactory().createGenerator(new StringWriter()), mapper.getSerializationConfig());
 intSerializer.acceptJsonFormatVisitor(mapper.getSerializerProvider().getRootValueSerializer(), null);
 mapper.getSerializerProvider().defaultSerializeValue(null, mapper.getObjectWriter(), intSerializer);
 mapper.readTree(new StringReader(actualOutput.toString()));
 assertEquals(expectedOutput, mapper.readTree(new StringReader(actualOutput.toString())));
 }

 // Test method for getSchema() of StdSerializer<String>
 @Test
 public void testGetSchemaString() throws JsonProcessingException {
 ObjectNode expectedOutput = createExpectedOutput("{\"@type\":\"String\"}", String.class);
 ObjectNode actualOutput = stringSerializer.getSchema(mapper.getSerializerProvider(), String.class);
 assertEquals(expectedOutput, actualOutput);
 }

 // Test method for getSchema() of StdSerializer<Integer>
 @Test
 public void testGetSchemaInteger() throws JsonProcessingException {
 ObjectNode expectedOutput = createExpectedOutput("{\"@type\":\"Number\"}", Integer.class);
 ObjectNode actualOutput = intSerializer.getSchema(mapper.getSerializerProvider(), Integer.class);
 assertEquals(expectedOutput, actualOutput);
 }

 // Test method for acceptJsonFormatVisitor() of StdSerializer<String>
 @Test(expected = JsonMappingException.class)
 public void testAcceptJsonFormatVisitorStringFailed() throws JsonProcessingException {
 stringSerializer.acceptJsonFormatVisitor(mapper.getSerializerProvider().getRootValueSerializer(), null);
 }

 // Test method for acceptJsonFormatVisitor() of StdSerializer<Integer>
 @Test
 public void testAcceptJsonFormatVisitorInteger() throws JsonProcessingException {
 JsonFormatVisitorWrapper visitor = mapper.getSerializerProvider().getJsonFormatVisitorWrapper(SerializerProvider.DefaultSerializableObjectVisitors.OBJECT);
 stringSerializer.acceptJsonFormatVisitor(visitor, null);
 intSerializer.acceptJsonFormatVisitor(visitor, null);
 assertTrue(visitor.hasAcceptedAnyFormat(String.class));
 assertTrue(visitor.hasAcceptedAnyFormat(Integer.class));
 }

 // Test method for wrapAndThrow() with IOException
 @Test(expected = IOException.class)
 public void testWrapAndThrowIOException() throws JsonProcessingException {
 String input = "Error Occurred";
 try {
 stringSerializer.serialize(input, mapper.getFactory().createGenerator(new StringWriter()), mapper.getSerializationConfig());
 } catch (IOException e) {
 stringSerializer.wrapAndThrow(mapper.getSerializerProvider(), e, null, null);
 }
 }

 // Test method for wrapAndThrow() with JsonMappingException
 @Test
 public void testWrapAndThrowJsonMappingException() throws JsonProcessingException {
 String input = "Error Occurred";
 try {
 stringSerializer.serialize(input, mapper.getFactory().createGenerator(new StringWriter()), mapper.getSerializationConfig());
 } catch (JsonMappingException e) {
 stringSerializer.wrapAndThrow(mapper.getSerializerProvider(), e, null, null);
 }
 }

 // Test method for isDefaultSerializer()
 @Test
 public void testIsDefaultSerializer() {
 assertTrue(stringSerializer.isDefaultSerializer(stringSerializer));
 assertFalse(stringSerializer.isDefaultSerializer(new CustomSerializer<String>()));
 }

 private ObjectNode createExpectedOutput(String json, Class<?> type) {
 ObjectNode output = mapper.readTree(new StringReader(json));
 output.set("@class", type.getName());
 return output;
 }

}