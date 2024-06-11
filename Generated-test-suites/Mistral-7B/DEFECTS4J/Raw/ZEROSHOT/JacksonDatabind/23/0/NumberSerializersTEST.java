import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class NumberSerializersTest {

 private ObjectMapper mapper;

 @Before
 public void setup() {
 mapper = new ObjectMapper();
 }

 @Test
 public void testAddAll() throws JsonMappingException {
 Map<String, JsonSerializer<?>> allDeserializers = new java.util.HashMap<>();
 NumberSerializers.addAll(allDeserializers);
 assertEquals(10, allDeserializers.size());
 }

 @Test
 public void testShortSerializer() throws JsonGenerationException, JsonMappingException, JsonProcessingException {
 Short testShort = Short.parseShort("127");
 JsonNode node = mapper.readTree(mapper.writeValueAsString(testShort));
 assertEquals(127, node.asInt());
 }

 @Test
 public void testIntegerSerializer() throws JsonGenerationException, JsonMappingException, JsonProcessingException {
 Integer testInteger = 128;
 JsonNode node = mapper.readTree(mapper.writeValueAsString(testInteger));
 assertEquals(128, node.asInt());
 }

 @Test
 public void testIntLikeSerializer() throws JsonGenerationException, JsonMappingException, JsonProcessingException {
 Byte testByte = new Byte((byte) 129);
 JsonNode node = mapper.readTree(mapper.writeValueAsString(testByte));
 assertEquals(129, node.asInt());
 }

 @Test
 public void testLongSerializer() throws JsonGenerationException, JsonMappingException, JsonProcessingException {
 Long testLong = Long.parseLong("12345678901234567890");
 JsonNode node = mapper.readTree(mapper.writeValueAsString(testLong));
 assertEquals(12345678901234567890L, node.asLong());
 }

 @Test
 public void testFloatSerializer() throws JsonGenerationException, JsonMappingException, JsonProcessingException {
 Float testFloat = new Float(3.1415f);
 JsonNode node = mapper.readTree(mapper.writeValueAsString(testFloat));
 assertEquals(3.1415f, node.asFloat(), 0.001);
 }

 @Test
 public void testDoubleSerializer() throws JsonGenerationException, JsonMappingException, JsonProcessingException {
 Double testDouble = new Double(3.1415);
 JsonNode node = mapper.readTree(mapper.writeValueAsString(testDouble));
 assertEquals(3.1415, node.asDouble(), 0.001);
 }

 @Test
 public void testJsonFormatVisitorWrapper() throws JsonMappingException {
 JsonFormatVisitorWrapper visitor = mapper.schemaFactory().getNumberFormatVisitor(Short.class, null);
 assertNotNull(visitor);
 assertEquals(JsonParser.NumberType.INT, visitor.getNumberType());
 }

 @Test
 public void testContextualSerializer() throws JsonMappingException {
 JsonSerializer<Integer> intSerializer = mapper.findValueSerializer(Integer.class);
 assertEquals(IntegerSerializer.class, intSerializer.getClass());
 }

 @Test
 public void testIntegerFormatVisitor() throws JsonMappingException {
 JsonFormatVisitorWrapper visitor = mapper.schemaFactory().getIntegerFormatVisitor(null);
 JsonIntegerFormatVisitor intVisitor = (JsonIntegerFormatVisitor) visitor;
 assertNotNull(intVisitor);
 assertEquals(JsonParser.NumberType.INT, intVisitor.getNumberType());
 }

 @Test
 public void testNumberFormatVisitor() throws JsonMappingException {
 JsonFormatVisitorWrapper visitor = mapper.schemaFactory().getNumberFormatVisitor(Double.class, null);
 JsonNumberFormatVisitor doubleVisitor = (JsonNumberFormatVisitor) visitor;
 assertNotNull(doubleVisitor);
 assertEquals(JsonParser.NumberType.DOUBLE, doubleVisitor.getNumberType());
 }
}