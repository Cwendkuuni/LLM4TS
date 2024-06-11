import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.*;

public class POJONodeTest {

 @Test
 public void testPOJONodeWithNullValue() {
 POJONode nullNode = new POJONode(null);
 JsonFactory factory = new JsonFactory();
 ObjectMapper mapper = new ObjectMapper(factory);

 // Test as text
 assertEquals("null", nullNode.asText());
 // Test serialization
 ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
 JsonGenerator gen = factory.createGenerator(byteStream);
 nullNode.serialize(gen, new SerializerProvider());
 gen.close();
 assertEquals("null", mapper.readTree(byteStream.toByteArray()).getTextValue());
 }

 @Test
 public void testPOJONodeWithStringValue() {
 POJONode stringNode = new POJONode("Hello World");
 JsonFactory factory = new JsonFactory();
 ObjectMapper mapper = new ObjectMapper(factory);

 // Test as text
 assertEquals("Hello World", stringNode.asText());
 // Test serialization
 ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
 JsonGenerator gen = factory.createGenerator(byteStream);
 stringNode.serialize(gen, new SerializerProvider());
 gen.close();
 assertEquals("\"Hello World\"", mapper.readTree(byteStream.toByteArray()).getTextValue());
 }

 @Test
 public void testPOJONodeWithNumberValue() {
 POJONode numberNode = new POJONode(123);
 JsonFactory factory = new JsonFactory();
 ObjectMapper mapper = new ObjectMapper(factory);

 // Test as text
 assertEquals("123", numberNode.asText());
 // Test serialization
 ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
 JsonGenerator gen = factory.createGenerator(byteStream);
 numberNode.serialize(gen, new SerializerProvider());
 gen.close();
 assertEquals("123", mapper.readTree(byteStream.toByteArray()).getTextValue());
 }

 @Test
 public void testPOJONodeWithBooleanValue() {
 POJONode booleanNode = new POJONode(true);
 JsonFactory factory = new JsonFactory();
 ObjectMapper mapper = new ObjectMapper(factory);

 // Test as text
 assertEquals("true", booleanNode.asText());
 // Test serialization
 ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
 JsonGenerator gen = factory.createGenerator(byteStream);
 booleanNode.serialize(gen, new SerializerProvider());
 gen.close();
 assertEquals("true", mapper.readTree(byteStream.toByteArray()).getTextValue());
 }

 @Test
 public void testPOJONodeWithPOJOBinaryValue() {
 byte[] binaryData = new byte[] { 0x00, 0x01, 0x02, 0x03 };
 POJONode binaryNode = new POJONode(binaryData);
 JsonFactory factory = new JsonFactory();
 ObjectMapper mapper = new ObjectMapper(factory);

 // Test as binary
 assertArrayEquals(binaryData, binaryNode.binaryValue());
 // Test as text
 assertEquals("(binary value of 4 bytes)", binaryNode.asText());
 // Test serialization
 ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
 JsonGenerator gen = factory.createGenerator(byteStream);
 binaryNode.serialize(gen, new SerializerProvider());
 gen.close();
 byte[] deserializedBinaryData = mapper.readValue(byteStream.toByteArray(), byte[].class);
 assertArrayEquals(binaryData, deserializedBinaryData);
 }

 @Test
 public void testPOJONodeWithJsonSerializableValue() {
 class TestClass implements JsonSerializable {
 private int id;

 public TestClass(int id) { this.id = id; }

 @Override
 public void serialize(JsonGenerator gen, SerializerProvider serializers) throws IOException {
 gen.writeNumber(id);
 }
 }
 POJONode serializableNode = new POJONode(new TestClass(42));
 JsonFactory factory = new JsonFactory();
 ObjectMapper mapper = new ObjectMapper(factory);

 // Test as text
 fail("Should not be able to call asText on POJONode with JsonSerializable value");
 // Test serialization
 ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
 JsonGenerator gen = factory.createGenerator(byteStream);
 serializableNode.serialize(gen, new SerializerProvider());
 gen.close();
 assertEquals("42", mapper.readTree(byteStream.toByteArray()).getTextValue());
 }

 @Test
 public void testPOJONodeWithDefaultValue() {
 POJONode defaultNode = new POJONode(null);
 JsonFactory factory = new JsonFactory();
 ObjectMapper mapper = new ObjectMapper(factory);

 // Test as text
 assertEquals("null", defaultNode.asText(new String("Default Value")));
 // Test serialization
 ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
 JsonGenerator gen = factory.createGenerator(byteStream);
 defaultNode.serialize(gen, new SerializerProvider());
 gen.close();
 assertEquals("null", mapper.readTree(byteStream.toByteArray()).getTextValue());
 }

 @Test(expected = IOException.class)
 public void testPOJONodeWithInvalidValue() {
 POJONode invalidNode = new POJONode(new Object());
 JsonFactory factory = new JsonFactory();
 ObjectMapper mapper = new ObjectMapper(factory);

 // Test serialization fails because Object is not a valid value for POJONode
 ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
 JsonGenerator gen = factory.createGenerator(byteStream);
 // This test case should throw an IOException
 invalidNode.serialize(gen, new SerializerProvider());
 }
}