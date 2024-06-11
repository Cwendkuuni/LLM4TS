import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class NumberSerializersTest {

 private ObjectMapper objectMapper;

 @BeforeAll
 public void setup() {
 objectMapper = new ObjectMapper();
 }

 @Test
 public void testAddAll() {
 Map<String, JsonSerializer<?>> allDeserializers = new HashMap<>();
 NumberSerializers.addAll(allDeserializers);
 assertEquals(6, allDeserializers.size());
 }

 @Test
 public void testIntegerSerializer() throws JsonProcessingException {
 ObjectNode input = objectMapper.createObjectNode();
 input.put("intValue", 123);
 JsonNode result = objectMapper.readTree(new StringReader(objectMapper.writerWithRootValue(input).writeValueAsString()));
 assertEquals(123, result.get("intValue").intValue());
 }

 @Test
 public void testIntegerSerializerWithType() throws JsonProcessingException {
 ObjectNode input = objectMapper.createObjectNode();
 input.put("intValue", 123);
 JsonNode result = objectMapper.readTree(new StringReader(objectMapper.writerWithRootValue(input).writeValueAsString()), ObjectMapper.DefaultTyping.JAVA_LANG_INTEGER);
 assertEquals(123, result.get("intValue").asInt());
 }

 @Test
 public void testIntLikeSerializer() throws JsonProcessingException {
 ObjectNode input = objectMapper.createObjectNode();
 input.put("intValue", 123);
 JsonNode result = objectMapper.readTree(new StringReader(objectMapper.writerWithRootValue(input).writeValueAsString()));
 assertEquals(123, result.get("intValue").intValue());
 }

 @Test
 public void testIntLikeSerializerWithType() throws JsonProcessingException {
 ObjectNode input = objectMapper.createObjectNode();
 input.put("intValue", 123L);
 JsonNode result = objectMapper.readTree(new StringReader(objectMapper.writerWithRootValue(input).writeValueAsString()), ObjectMapper.DefaultTyping.JAVA_LANG_NUMBER);
 assertEquals(123, result.get("intValue").intValue());
 }

 @Test
 public void testShortSerializer() throws JsonProcessingException {
 ObjectNode input = objectMapper.createObjectNode();
 input.put("shortValue", (short) 123);
 JsonNode result = objectMapper.readTree(new StringReader(objectMapper.writerWithRootValue(input).writeValueAsString()));
 assertEquals(123, result.get("shortValue").intValue());
 }

 @Test
 public void testShortSerializerWithType() throws JsonProcessingException {
 ObjectNode input = objectMapper.createObjectNode();
 input.put("shortValue", (short) 123);
 JsonNode result = objectMapper.readTree(new StringReader(objectMapper.writerWithRootValue(input).writeValueAsString()), ObjectMapper.DefaultTyping.JAVA_LANG_SHORT);
 assertEquals(123, result.get("shortValue").shortValue());
 }

 @Test
 public void testLongSerializer() throws JsonProcessingException {
 ObjectNode input = objectMapper.createObjectNode();
 input.put("longValue", 1234567890L);
 JsonNode result = objectMapper.readTree(new StringReader(objectMapper.writerWithRootValue(input).writeValueAsString()));
 assertEquals(1234567890, result.get("longValue").longValue());
 }

 @Test
 public void testLongSerializerWithType() throws JsonProcessingException {
 ObjectNode input = objectMapper.createObjectNode();
 input.put("longValue", 1234567890L);
 JsonNode result = objectMapper.readTree(new StringReader(objectMapper.writerWithRootValue(input).writeValueAsString()), ObjectMapper.DefaultTyping.JAVA_LANG_LONG);
 assertEquals(1234567890, result.get("longValue").longValue());
 }

 @Test
 public void testFloatSerializer() throws JsonProcessingException {
 ObjectNode input = objectMapper.createObjectNode();
 input.put("floatValue", 123.45F);
 JsonNode result = objectMapper.readTree(new StringReader(objectMapper.writerWithRootValue(input).writeValueAsString()));
 assertEquals(123.45f, result.get("floatValue").floatValue(), 0.001);
 }

 @Test
 public void testFloatSerializerWithType() throws JsonProcessingException {
 ObjectNode input = objectMapper.createObjectNode();
 input.put("floatValue", 123.45F);
 JsonNode result = objectMapper.readTree(new StringReader(objectMapper.writerWithRootValue(input).writeValueAsString()), ObjectMapper.DefaultTyping.JAVA_LANG_FLOAT);
 assertEquals(123.45f, result.get("floatValue").floatValue(), 0.001);
 }

 @Test
 public void testDoubleSerializer() throws JsonProcessingException {
 ObjectNode input = objectMapper.createObjectNode();
 input.put("doubleValue", 123.456789d);
 JsonNode result = objectMapper.readTree(new StringReader(objectMapper.writerWithRootValue(input).writeValueAsString()));
 assertEquals(123.456789, result.get("doubleValue").doubleValue(), 0.0001);
 }

 @Test
 public void testDoubleSerializerWithType() throws JsonProcessingException {
 ObjectNode input = objectMapper.createObjectNode();
 input.put("doubleValue", 123.456789d);
 JsonNode result = objectMapper.readTree(new StringReader(objectMapper.writerWithRootValue(input).writeValueAsString()), ObjectMapper.DefaultTyping.JAVA_LANG_DOUBLE);
 assertEquals(123.456789, result.get("doubleValue").doubleValue(), 0.0001);
 }

 @Test(expected = JsonMappingException.class)
 public void testInvalidType() throws JsonProcessingException {
 ObjectNode input = objectMapper.createObjectNode();
 input.put("intValue", new Object());
 StringReader reader = new StringReader(objectMapper.writerWithRootValue(input).writeValueAsString());
 objectMapper.readValue(reader, Integer.class);
 }

 @Test(expected = JsonMappingException.class)
 public void testJsonFormatAnnotation() throws JsonProcessingException {
 JsonFormat jsonFormat = new JsonFormat();
 jsonFormat.setShape(JsonFormat.Shape.STRING);
 ObjectNode input = objectMapper.createObjectNode();
 input.put("intValue", 123);
 JsonSerializer<Object> serializer = new IntegerSerializer(Integer.class);
 ObjectNode rootNode = objectMapper.writerWithRootValue(input).writeValueAsObject();
 ObjectReader objectReader = objectMapper.readerForTree(rootNode);
 ObjectReader readerWithAnnotation = objectMapper.readerForTree(rootNode).withResolved(JsonSerializers.Creator.instance.createValueSerializer(JsonSerializers.Base.String.class, jsonFormat));
 JsonNode result = objectReader.readValueAs(JsonNode.class);
 JsonNode resultWithAnnotation = readerWithAnnotation.readValueAs(JsonNode.class);
 assertEquals(result.toString(), resultWithAnnotation.toString());
 }
}