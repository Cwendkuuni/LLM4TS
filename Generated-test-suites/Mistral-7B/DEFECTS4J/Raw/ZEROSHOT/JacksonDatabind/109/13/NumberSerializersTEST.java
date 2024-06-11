import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.*;
import com.fasterxml.jackson.databind.ser.std.*;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class NumberSerializersTest {

 private ObjectMapper objectMapper;

 @Before
 public void setup() {
 objectMapper = new ObjectMapper();
 }

 @Test
 public void testAddAll() {
 Map<String, JsonSerializer<?>> allDeserializers = new HashMap<>();
 NumberSerializers.addAll(allDeseralizers);
 assertTrue(allDeseralizers.containsKey(Integer.class.getName()));
 assertTrue(allDeseralizers.containsKey(Integer.TYPE.getName()));
 // ... add assertions for other classes
 }

 @Test
 public void testIntegerSerializer() throws JsonProcessingException {
 Integer input = 123;
 String output = objectMapper.writerWithDefaultPrettyPrinter()
 .writeValueAsString(input);
 assertEquals("123", output);
 }

 @Test
 public void testIntLikeSerializer() throws JsonProcessingException {
 Number input = 123;
 String output = objectMapper.writerWithDefaultPrettyPrinter()
 .writeValueAsString(input);
 assertEquals("123", output);
 }

 @Test
 public void testShortSerializer() throws JsonProcessingException {
 Short input = 123;
 String output = objectMapper.writerWithDefaultPrettyPrinter()
 .writeValueAsString(input);
 assertEquals("123", output);
 }

 @Test
 public void testLongSerializer() throws JsonProcessingException {
 Long input = 123L;
 String output = objectMapper.writerWithDefaultPrettyPrinter()
 .writeValueAsString(input);
 assertEquals("123", output);
 }

 @Test
 public void testFloatSerializer() throws JsonProcessingException {
 Float input = 123.45F;
 String output = objectMapper.writerWithDefaultPrettyPrinter()
 .writeValueAsString(input);
 assertEquals("123.45", output);
 }

 @Test
 public void testDoubleSerializer() throws JsonProcessingException {
 Double input = 123.45;
 String output = objectMapper.writerWithDefaultPrettyPrinter()
 .writeValueAsString(input);
 assertEquals("123.45", output);
 }

 @Test
 public void testContextualSerialization() throws JsonProcessingException {
 ObjectMapper mapper = new ObjectMapper();
 ObjectWriter writer = mapper.writerWithDefaultPrettyPrinter();
 Map<String, Object> values = new HashMap<>();
 values.put("int", 123);
 values.put("long", 123L);
 values.put("float", 123.45F);
 values.put("double", 123.45);
 String json = writer.writeValueAsString(values);
 JsonNode node = objectMapper.readTree(json);
 JsonNode intNode = node.get("int");
 JsonNode longNode = node.get("long");
 JsonNode floatNode = node.get("float");
 JsonNode doubleNode = node.get("double");
 assertEquals("123", intNode.asText());
 assertEquals("123", longNode.asText());
 assertEquals("123.45", floatNode.asText());
 assertEquals("123.45", doubleNode.asText());
 }

 @Test
 public void testJsonFormat() throws JsonProcessingException {
 ObjectMapper mapper = new ObjectMapper();
 ObjectWriter writer = mapper.writerWithDefaultPrettyPrinter();
 @JsonFormat(shape = JsonFormat.Shape.STRING)
 Integer intValue = 123;
 @JsonFormat(shape = JsonFormat.Shape.STRING, serializableAs = Long.class)
 Long longValue = 123L;
 Map<String, Object> values = new HashMap<>();
 values.put("int", intValue);
 values.put("long", longValue);
 String json = writer.writeValueAsString(values);
 JsonNode node = objectMapper.readTree(json);
 JsonNode intNode = node.get("int");
 JsonNode longNode = node.get("long");
 assertEquals("123", intNode.asText());
 assertEquals("123", longNode.asText());
 }
}