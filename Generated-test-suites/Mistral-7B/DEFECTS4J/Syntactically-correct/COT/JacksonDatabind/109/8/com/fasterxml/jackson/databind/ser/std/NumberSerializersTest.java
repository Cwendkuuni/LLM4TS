import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.Test;
import static org.junit.Assert.*;

public class NumberSerializersTest {

 private static ObjectMapper mapper = new ObjectMapper();

 @Test
 public void testAddAll() {
 Map<String, JsonSerializer<?>> allDeserializers = new HashMap<>();
 NumberSerializers.addAll(allDeserializers);
 assertEquals(11, allDeserializers.size());
 }

 @Test
 public void testIntegerSerializer() throws Exception {
 Integer serializeValue = 1;
 ObjectNode result = mapper.readValue(new ObjectMapper().writeValueAsString(serializeValue), ObjectNode.class);
 assertEquals("1", result.get(0).asText());
 }

 @Test
 public void testIntegerSerializerWithType() throws Exception {
 Integer serializeValue = 1;
 ObjectNode result = mapper.readValue(mapper.writeValueAsString(serializeValue), ObjectNode.class);
 ObjectNode typeNode = result.get(1);
 assertEquals("integer", typeNode.get("type").asText());
 }

 @Test
 public void testIntegerSerializerNegativeValue() throws Exception {
 Integer serializeValue = -1;
 ObjectNode result = mapper.readValue(mapper.writeValueAsString(serializeValue), ObjectNode.class);
 assertEquals("-1", result.get(0).asText());
 }

 @Test
 public void testIntegerSerializerLargeValue() throws Exception {
 Long serializeValue = Long.MAX_VALUE;
 ObjectNode result = mapper.readValue(mapper.writeValueAsString(serializeValue), ObjectNode.class);
 assertEquals(Long.toString(serializeValue), result.get(0).asText());
 }

 @Test
 public void testIntegerSerializerMinusInfinity() throws Exception {
 Double serializeValue = Double.NEGATIVE_INFINITY;
 ObjectNode result = mapper.readValue(mapper.writeValueAsString(serializeValue), ObjectNode.class);
 assertEquals("-Infinity", result.get(0).asText());
 }

 @Test
 public void testIntegerSerializerNaN() throws Exception {
 Float serializeValue = Float.NaN;
 ObjectNode result = mapper.readValue(mapper.writeValueAsString(serializeValue), ObjectNode.class);
 assertEquals("NaN", result.get(0).asText());
 }

 @Test
 public void testShortSerializer() throws Exception {
 Short serializeValue = (short) 1;
 ObjectNode result = mapper.readValue(mapper.writeValueAsString(serializeValue), ObjectNode.class);
 assertEquals("1", result.get(0).asText());
 }

 @Test
 public void testShortSerializerNegativeValue() throws Exception {
 Short serializeValue = (short) -1;
 ObjectNode result = mapper.readValue(mapper.writeValueAsString(serializeValue), ObjectNode.class);
 assertEquals("-1", result.get(0).asText());
 }

 @Test
 public void testShortSerializerLargeValue() throws Exception {
 Short serializeValue = Short.MAX_VALUE;
 ObjectNode result = mapper.readValue(mapper.writeValueAsString(serializeValue), ObjectNode.class);
 assertEquals(Short.toString(serializeValue), result.get(0).asText());
 }

 @Test
 public void testLongSerializer() throws Exception {
 Long serializeValue = 1L;
 ObjectNode result = mapper.readValue(mapper.writeValueAsString(serializeValue), ObjectNode.class);
 assertEquals("1", result.get(0).asText());
 }

 @Test
 public void testLongSerializerNegativeValue() throws Exception {
 Long serializeValue = -1L;
 ObjectNode result = mapper.readValue(mapper.writeValueAsString(serializeValue), ObjectNode.class);
 assertEquals("-1", result.get(0).asText());
 }

 @Test
 public void testLongSerializerLargeValue() throws Exception {
 Long serializeValue = Long.MAX_VALUE;
 ObjectNode result = mapper.readValue(mapper.writeValueAsString(serializeValue), ObjectNode.class);
 assertEquals(Long.toString(serializeValue), result.get(0).asText());
 }

 @Test
 public void testLongSerializerMinusInfinity() throws Exception {
 Double serializeValue = Double.NEGATIVE_INFINITY;
 ObjectNode result = mapper.readValue(mapper.writeValueAsString(serializeValue), ObjectNode.class);
 assertEquals("-Infinity", result.get(0).asText());
 }

 @Test
 public void testLongSerializerNaN() throws Exception {
 Float serializeValue = Float.NaN;
 ObjectNode result = mapper.readValue(mapper.writeValueAsString(serializeValue), ObjectNode.class);
 assertEquals("NaN", result.get(0).asText());
 }

 @Test
 public void testFloatSerializer() throws Exception {
 Float serializeValue = Float.POSITIVE_INFINITY;
 ObjectNode result = mapper.readValue(mapper.writeValueAsString(serializeValue), ObjectNode.class);
 assertEquals("Infinity", result.get(0).asText());
 }

 @Test
 public void testFloatSerializerNegativeValue() throws Exception {
 Float serializeValue = -1.0f;
 ObjectNode result = mapper.readValue(mapper.writeValueAsString(serializeValue), ObjectNode.class);
 assertEquals("-1.0", result.get(0).asText());
 }

 @Test
 public void testFloatSerializerLargeValue() throws Exception {
 Float serializeValue = Float.MAX_VALUE;
 ObjectNode result = mapper.readValue(mapper.writeValueAsString(serializeValue), ObjectNode.class);
 assertEquals(Float.toString(serializeValue), result.get(0).asText());
 }

 @Test
 public void testFloatSerializerMinusInfinity() throws Exception {
 Double serializeValue = Double.NEGATIVE_INFINITY;
 ObjectNode result = mapper.readValue(mapper.writeValueAsString(serializeValue), ObjectNode.class);
 assertEquals("-Infinity", result.get(0).asText());
 }

 @Test
 public void testFloatSerializerNaN() throws Exception {
 Float serializeValue = Float.NaN;
 ObjectNode result = mapper.readValue(mapper.writeValueAsString(serializeValue), ObjectNode.class);
 assertEquals("NaN", result.get(0).asText());
 }

 @Test(expected = JsonMappingException.class)
 public void testIntegerSerializerNullValue() throws Exception {
 ObjectNode result = mapper.readValue(mapper.writeValueAsString(null), ObjectNode.class);
 fail("Expected JsonMappingException but got: " + result);
 }

 @Test(expected = JsonMappingException.class)
 public void testIntegerSerializerNonNumberValue() throws Exception {
 ObjectNode result = mapper.readValue(mapper.writeValueAsString("nonNumber"), ObjectNode.class);
 fail("Expected JsonMappingException but got: " + result);
 }

 @Test(expected = JsonMappingException.class)
 public void testIntegerSerializerStringValue() throws Exception {
 ObjectNode result = mapper.readValue(mapper.writeValueAsString("stringValue"), ObjectNode.class);
 fail("Expected JsonMappingException but got: " + result);
 }
}