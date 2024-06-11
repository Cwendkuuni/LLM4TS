import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class NumberSerializersTest {

 private ObjectMapper objectMapper;

 @Before
 public void setup() {
 objectMapper = JsonMapper.builder().build();
 }

 @Test
 public void testIntegerSerializer() throws JsonProcessingException {
 Integer input = 123;
 ObjectNode output = mapper.writeValueAsTree(input);
 Assert.assertEquals("123", output.getTextValue("1"));
 }

 @Test
 public void testIntegerSerializerWithType() throws JsonProcessingException {
 Integer input = 123;
 ObjectNode output = mapper.writeValueAsTree(input);
 ObjectNode expected = mapper.createObjectNode();
 expected.put("value", 123);
 expected.put("numberType", "int");
 ObjectNode actual = mapper.readTree(output.toString());
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testIntegerSerializerContextual() throws JsonProcessingException {
 Integer input = 123;
 ObjectNode output = mapper.writeValueAsTree(input);
 ObjectNode expected = mapper.createObjectNode();
 expected.put("value", "123");
 ObjectNode actual = mapper.readTree(output.toString());
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testIntegerSerializerContextualWithType() throws JsonProcessingException {
 Integer input = 123;
 ObjectNode output = mapper.writeValueAsTree(input);
 ObjectNode expected = mapper.createObjectNode();
 expected.put("value", "123");
 expected.put("type", "INTEGER");
 ObjectNode actual = mapper.readTree(output.toString());
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testShortSerializer() throws JsonProcessingException {
 Short input = 123;
 ObjectNode output = mapper.writeValueAsTree(input);
 Assert.assertEquals("123", output.getTextValue("1"));
 }

 @Test
 public void testShortSerializerWithType() throws JsonProcessingException {
 Short input = 123;
 ObjectNode output = mapper.writeValueAsTree(input);
 ObjectNode expected = mapper.createObjectNode();
 expected.put("value", 123);
 expected.put("numberType", "int");
 ObjectNode actual = mapper.readTree(output.toString());
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testShortSerializerContextual() throws JsonProcessingException {
 Short input = 123;
 ObjectNode output = mapper.writeValueAsTree(input);
 ObjectNode expected = mapper.createObjectNode();
 expected.put("value", "123");
 ObjectNode actual = mapper.readTree(output.toString());
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testShortSerializerContextualWithType() throws JsonProcessingException {
 Short input = 123;
 ObjectNode output = mapper.writeValueAsTree(input);
 ObjectNode expected = mapper.createObjectNode();
 expected.put("value", "123");
 expected.put("type", "SHORT");
 ObjectNode actual = mapper.readTree(output.toString());
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testLongSerializer() throws JsonProcessingException {
 Long input = 123L;
 ObjectNode output = mapper.writeValueAsTree(input);
 Assert.assertEquals("123", output.getTextValue("1"));
 }

 @Test
 public void testLongSerializerWithType() throws JsonProcessingException {
 Long input = 123L;
 ObjectNode output = mapper.writeValueAsTree(input);
 ObjectNode expected = mapper.createObjectNode();
 expected.put("value", 123);
 expected.put("numberType", "long");
 ObjectNode actual = mapper.readTree(output.toString());
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testLongSerializerContextual() throws JsonProcessingException {
 Long input = 123L;
 ObjectNode output = mapper.writeValueAsTree(input);
 ObjectNode expected = mapper.createObjectNode();
 expected.put("value", "123");
 ObjectNode actual = mapper.readTree(output.toString());
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testLongSerializerContextualWithType() throws JsonProcessingException {
 Long input = 123L;
 ObjectNode output = mapper.writeValueAsTree(input);
 ObjectNode expected = mapper.createObjectNode();
 expected.put("value", "123");
 expected.put("type", "LONG");
 ObjectNode actual = mapper.readTree(output.toString());
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testFloatSerializer() throws JsonProcessingException {
 Float input = 123.45f;
 ObjectNode output = mapper.writeValueAsTree(input);
 Assert.assertEquals("123.45", output.getTextValue("1"));
 }

 @Test
 public void testFloatSerializerWithType() throws JsonProcessingException {
 Float input = 123.45f;
 ObjectNode output = mapper.writeValueAsTree(input);
 ObjectNode expected = mapper.createObjectNode();
 expected.put("value", 123.45);
 expected.put("numberType", "float");
 ObjectNode actual = mapper.readTree(output.toString());
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testFloatSerializerContextual() throws JsonProcessingException {
 Float input = 123.45f;
 ObjectNode output = mapper.writeValueAsTree(input);
 ObjectNode expected = mapper.createObjectNode();
 expected.put("value", "123.45");
 ObjectNode actual = mapper.readTree(output.toString());
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testFloatSerializerContextualWithType() throws JsonProcessingException {
 Float input = 123.45f;
 ObjectNode output = mapper.writeValueAsTree(input);
 ObjectNode expected = mapper.createObjectNode();
 expected.put("value", "123.45");
 expected.put("type", "FLOAT");
 ObjectNode actual = mapper.readTree(output.toString());
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testDoubleSerializer() throws JsonProcessingException {
 Double input = 123.45;
 ObjectNode output = mapper.writeValueAsTree(input);
 Assert.assertEquals("123.45", output.getTextValue("1"));
 }

 @Test
 public void testDoubleSerializerWithType() throws JsonProcessingException {
 Double input = 123.45;
 ObjectNode output = mapper.writeValueAsTree(input);
 ObjectNode expected = mapper.createObjectNode();
 expected.put("value", 123.45);
 expected.put("numberType", "double");
 ObjectNode actual = mapper.readTree(output.toString());
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testDoubleSerializerContextual() throws JsonProcessingException {
 Double input = 123.45;
 ObjectNode output = mapper.writeValueAsTree(input);
 ObjectNode expected = mapper.createObjectNode();
 expected.put("value", "123.45");
 ObjectNode actual = mapper.readTree(output.toString());
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testDoubleSerializerContextualWithType() throws JsonProcessingException {
 Double input = 123.45;
 ObjectNode output = mapper.writeValueAsTree(input);
 ObjectNode expected = mapper.createObjectNode();
 expected.put("value", "123.45");
 expected.put("type", "DOUBLE");
 ObjectNode actual = mapper.readTree(output.toString());
 Assert.assertEquals(expected, actual);
 }
}