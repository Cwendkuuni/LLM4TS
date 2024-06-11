import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import static org.junit.Assert.*;

public class NumberSerializersTest {

 @SuppressWarnings("unchecked")
 @org.junit.Test
 public void testNumberSerializers() throws JsonMappingException, JsonProcessingException {
 ObjectMapper mapper = new ObjectMapper();

 // Test serialization of integers
 Integer intValue = 123;
 JsonNode intNode = mapper.valueToTree(intValue);
 assertEquals(JsonNodeType.NUMBER, intNode.getNodeType());
 assertEquals("123", intNode.textValue());

 Short shortValue = 127;
 JsonNode shortNode = mapper.valueToTree(shortValue);
 assertEquals(JsonNodeType.NUMBER, shortNode.getNodeType());
 assertEquals("127", shortNode.textValue());

 Byte byteValue = 128;
 JsonNode byteNode = mapper.valueToTree(byteValue);
 assertEquals(JsonNodeType.NUMBER, byteNode.getNodeType());
 assertEquals("128", byteNode.textValue());

 // Test serialization of longs
 Long longValue = 1234567890123L;
 JsonNode longNode = mapper.valueToTree(longValue);
 assertEquals(JsonNodeType.NUMBER, longNode.getNodeType());
 assertEquals("1234567890123", longNode.textValue());

 // Test serialization of floats
 Float floatValue = 123.45f;
 JsonNode floatNode = mapper.valueToTree(floatValue);
 assertEquals(JsonNodeType.NUMBER, floatNode.getNodeType());
 assertEquals("123.45", floatNode.textValue());

 // Test serialization of doubles
 Double doubleValue = 123.456789;
 JsonNode doubleNode = mapper.valueToTree(doubleValue);
 assertEquals(JsonNodeType.NUMBER, doubleNode.getNodeType());
 assertEquals("123.456789", doubleNode.textValue());

 // Test JSON format annotations
 @JsonFormat(shape = JsonFormat.Shape.STRING)
 int annotatedInt = 123;
 @JsonFormat(shape = JsonFormat.Shape.STRING)
 short annotatedShort = 127;
 @JsonFormat(shape = JsonFormat.Shape.STRING)
 byte annotatedByte = 128;
 @JsonFormat(shape = JsonFormat.Shape.STRING)
 long annotatedLong = 1234567890123L;
 @JsonFormat(shape = JsonFormat.Shape.STRING)
 float annotatedFloat = 123.45f;
 @JsonFormat(shape = JsonFormat.Shape.STRING)
 double annotatedDouble = 123.456789;

 JsonNode annotatedIntNode = mapper.valueToTree(annotatedInt);
 assertEquals(JsonNodeType.STRING, annotatedIntNode.getNodeType());
 assertEquals("123", annotatedIntNode.textValue());

 JsonNode annotatedShortNode = mapper.valueToTree(annotatedShort);
 assertEquals(JsonNodeType.STRING, annotatedShortNode.getNodeType());
 assertEquals("127", annotatedShortNode.textValue());

 JsonNode annotatedByteNode = mapper.valueToTree(annotatedByte);
 assertEquals(JsonNodeType.STRING, annotatedByteNode.getNodeType());
 assertEquals("128", annotatedByteNode.textValue());

 JsonNode annotatedLongNode = mapper.valueToTree(annotatedLong);
 assertEquals(JsonNodeType.STRING, annotatedLongNode.getNodeType());
 assertEquals("1234567890123", annotatedLongNode.textValue());

 JsonNode annotatedFloatNode = mapper.valueToTree(annotatedFloat);
 assertEquals(JsonNodeType.STRING, annotatedFloatNode.getNodeType());
 assertEquals("123.45", annotatedFloatNode.textValue());

 JsonNode annotatedDoubleNode = mapper.valueToTree(annotatedDouble);
 assertEquals(JsonNodeType.STRING, annotatedDoubleNode.getNodeType());
 assertEquals("123.456789", annotatedDoubleNode.textValue());
 }
}