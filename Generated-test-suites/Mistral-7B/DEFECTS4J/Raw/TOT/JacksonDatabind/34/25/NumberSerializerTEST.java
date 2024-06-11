import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class NumberSerializerTest {

 private static final ObjectMapper mapper = new ObjectMapper();

 @Test
 public void testSerializeBigDecimal() throws JsonProcessingException {
 NumberSerializer serializer = NumberSerializer.instance;
 BigDecimal number = new BigDecimal("123.456");
 ObjectNode json = mapper.readValue("{}", ObjectNode.class);

 // Serialize
 serializer.serialize(number, mapper.getJsonFactory().createGenerator(json), new SerializerProvider().builder()
 .build());
 mapper.writeValueAsString(json);

 // Verify
 String expected = "123.456";
 String actual = json.get("$value").asText();
 assertEquals(expected, actual);
 }

 @Test
 public void testSerializeBigInteger() throws JsonProcessingException {
 NumberSerializer serializer = NumberSerializer.instance;
 BigInteger number = BigInteger.valueOf(1234567890L);
 ObjectNode json = mapper.readValue("{}", ObjectNode.class);

 // Serialize
 serializer.serialize(number, mapper.getJsonFactory().createGenerator(json), new SerializerProvider().builder()
 .build());
 mapper.writeValueAsString(json);

 // Verify
 String expected = "1234567890";
 String actual = json.get("$value").asText();
 assertEquals(expected, actual);
 }

 @Test
 public void testSerializeInteger() throws JsonProcessingException {
 NumberSerializer serializer = NumberSerializer.instance;
 Integer number = 123;
 ObjectNode json = mapper.readValue("{}", ObjectNode.class);

 // Serialize
 serializer.serialize(number, mapper.getJsonFactory().createGenerator(json), new SerializerProvider().builder()
 .build());
 mapper.writeValueAsString(json);

 // Verify
 String expected = "123";
 String actual = json.get("$value").asText();
 assertEquals(expected, actual);
 }

 @Test
 public void testSerializeLong() throws JsonProcessingException {
 NumberSerializer serializer = NumberSerializer.instance;
 Long number = 1234567890L;
 ObjectNode json = mapper.readValue("{}", ObjectNode.class);

 // Serialize
 serializer.serialize(number, mapper.getJsonFactory().createGenerator(json), new SerializerProvider().builder()
 .build());
 mapper.writeValueAsString(json);

 // Verify
 String expected = "1234567890";
 String actual = json.get("$value").asText();
 assertEquals(expected, actual);
 }

 @Test
 public void testSerializeDouble() throws JsonProcessingException {
 NumberSerializer serializer = NumberSerializer.instance;
 Double number = 123.456;
 ObjectNode json = mapper.readValue("{}", ObjectNode.class);

 // Serialize
 serializer.serialize(number, mapper.getJsonFactory().createGenerator(json), new SerializerProvider().builder()
 .build());
 mapper.writeValueAsString(json);

 // Verify
 String expected = "123.456";
 String actual = json.get("$value").asText();
 assertEquals(expected, actual);
 }

 @Test
 public void testSerializeFloat() throws JsonProcessingException {
 NumberSerializer serializer = NumberSerializer.instance;
 Float number = 123.45f;
 ObjectNode json = mapper.readValue("{}", ObjectNode.class);

 // Serialize
 serializer.serialize(number, mapper.getJsonFactory().createGenerator(json), new SerializerProvider().builder()
 .build());
 mapper.writeValueAsString(json);

 // Verify
 String expected = "123.45";
 String actual = json.get("$value").asText();
 assertEquals(expected, actual);
 }

 @Test
 public void testSerializeByte() throws JsonProcessingException {
 NumberSerializer serializer = NumberSerializer.instance;
 Byte number = (byte) 123;
 ObjectNode json = mapper.readValue("{}", ObjectNode.class);

 // Serialize
 serializer.serialize(number, mapper.getJsonFactory().createGenerator(json), new SerializerProvider().builder()
 .build());
 mapper.writeValueAsString(json);

 // Verify
 String expected = "123";
 String actual = json.get("$value").asText();
 assertEquals(expected, actual);
 }

 @Test
 public void testSerializeShort() throws JsonProcessingException {
 NumberSerializer serializer = NumberSerializer.instance;
 Short number = (short) 123;
 ObjectNode json = mapper.readValue("{}", ObjectNode.class);

 // Serialize
 serializer.serialize(number, mapper.getJsonFactory().createGenerator(json), new SerializerProvider().builder()
 .build());
 mapper.writeValueAsString(json);

 // Verify
 String expected = "123";
 String actual = json.get("$value").asText();
 assertEquals(expected, actual);
 }

 @Test(expected = JsonMappingException.class)
 public void testSerializeUnsupportedType() throws JsonProcessingException {
 NumberSerializer serializer = NumberSerializer.instance;
 Object number = new Object();
 ObjectNode json = mapper.readValue("{}", ObjectNode.class);

 // Serialize
 try {
 serializer.serialize(number, mapper.getJsonFactory().createGenerator(json), new SerializerProvider().builder()
 .build());
 mapper.writeValueAsString(json);
 } catch (JsonMappingException e) {
 // Expected
 }

 // Verify
 String expected = "null";
 String actual = json.get("$value").asText();
 assertEquals(expected, actual);
 }
}