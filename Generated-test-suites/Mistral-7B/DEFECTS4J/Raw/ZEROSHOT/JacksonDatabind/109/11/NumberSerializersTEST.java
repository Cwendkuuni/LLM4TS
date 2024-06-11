import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.std.Base;
import com.fasterxml.jackson.databind.ser.std.NumberSerializers;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class NumberSerializersTest {

 private ObjectMapper objectMapper;

 @Before
 public void setUp() {
 objectMapper = new ObjectMapper();
 }

 @Test
 public void testAddAll() {
 Map<String, JsonSerializer<?>> allDeserializers = new HashMap<>();
 NumberSerializers.addAll(allDeserializers);
 assertEquals(7, allDeserializers.size());
 }

 @Test
 public void testShortSerializer() throws JsonProcessingException {
 Short shortValue = Short.parseShort("123");
 JsonNode jsonNode = objectMapper.readTree(objectMapper.writerWithDefaultPrettyPrinter()
 .writeValueAsString(shortValue));
 assertEquals("123", jsonNode.asText());
 }

 @Test
 public void testIntegerSerializer() throws JsonProcessingException {
 Integer integerValue = 123;
 JsonNode jsonNode = objectMapper.readTree(objectMapper.writerWithDefaultPrettyPrinter()
 .writeValueAsString(integerValue));
 assertEquals("123", jsonNode.asText());
 }

 @Test
 public void testIntLikeSerializer() throws JsonProcessingException {
 Byte byteValue = 123;
 JsonNode jsonNode = objectMapper.readTree(objectMapper.writerWithDefaultPrettyPrinter()
 .writeValueAsString(byteValue.intValue()));
 assertEquals("123", jsonNode.asText());
 }

 @Test
 public void testLongSerializer() throws JsonProcessingException {
 Long longValue = 123L;
 JsonNode jsonNode = objectMapper.readTree(objectMapper.writerWithDefaultPrettyPrinter()
 .writeValueAsString(longValue));
 assertEquals("123", jsonNode.asText());
 }

 @Test
 public void testFloatSerializer() throws JsonProcessingException {
 Float floatValue = 123.45f;
 JsonNode jsonNode = objectMapper.readTree(objectMapper.writerWithDefaultPrettyPrinter()
 .writeValueAsString(floatValue));
 assertEquals("123.45", jsonNode.asText());
 }

 @Test
 public void testDoubleSerializer() throws JsonProcessingException {
 Double doubleValue = 123.45;
 JsonNode jsonNode = objectMapper.readTree(objectMapper.writerWithDefaultPrettyPrinter()
 .writeValueAsString(doubleValue));
 assertEquals("123.45", jsonNode.asText());
 }

 @Test
 public void testAcceptJsonFormatVisitor() throws JsonProcessingException {
 ObjectMapper objectMapper = new ObjectMapper();
 JsonFormat.Shape shape = JsonFormat.Shape.STRING;
 JsonFormat jsonFormat = new JsonFormat(shape, null, null, null);
 Integer integerValue = 123;
 JsonSerializer<?> serializer = new IntegerSerializer(Integer.class);
 serializer.acceptJsonFormatVisitor(objectMapper.getSerializerProvider().getJsonFactory(), integerValue.getClass());
 assertEquals(shape, serializer.getFormat());
 }
}