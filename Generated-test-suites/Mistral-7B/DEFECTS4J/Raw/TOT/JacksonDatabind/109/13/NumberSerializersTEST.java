import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class NumberSerializersTest {

 private ObjectMapper mapper;

 @Before
 public void setup() {
 mapper = new ObjectMapper();
 }

 @Test
 public void testIntegerSerializer() throws JsonProcessingException {
 Integer value = 123;
 String json = mapper.writeValueAsString(value);
 JsonParser parser = mapper.getFactory().createParser(json);
 assertEquals(value.intValue(), parser.readValueAs(Integer.class));
 }

 @Test
 public void testIntegerSerializerWithNegativeValue() throws JsonProcessingException {
 Integer value = -123;
 String json = mapper.writeValueAsString(value);
 JsonParser parser = mapper.getFactory().createParser(json);
 assertEquals(value.intValue(), parser.readValueAs(Integer.class));
 }

 @Test
 public void testIntegerSerializerWithMaxValue() throws JsonProcessingException {
 Integer value = Integer.MAX_VALUE;
 String json = mapper.writeValueAsString(value);
 JsonParser parser = mapper.getFactory().createParser(json);
 assertEquals(value.intValue(), parser.readValueAs(Integer.class));
 }

 @Test
 public void testIntegerSerializerWithMinValue() throws JsonProcessingException {
 Integer value = Integer.MIN_VALUE;
 String json = mapper.writeValueAsString(value);
 JsonParser parser = mapper.getFactory().createParser(json);
 assertEquals(value.intValue(), parser.readValueAs(Integer.class));
 }

 @Test
 public void testIntegerSerializerWithStringValue() throws JsonProcessingException {
 Integer value = 123;
 String json = "123";
 String jsonWithType = "{\"number\":\"123\",\"@class\":\"java.lang.Integer\"}";
 ObjectMapper mapperWithType = new ObjectMapper();
 JsonNode parsedJson = mapperWithType.readTree(jsonWithType);
 JsonNode parsedJsonWithoutType = mapper.readTree(json);
 assertEquals(parsedJson.get("number"), parsedJsonWithoutType.get("number"));
 assertEquals(value.getClass(), parsedJsonWithType.getClass());
 }

 @Test
 public void testIntegerSerializerWithStringValueNaN() throws JsonProcessingException {
 String json = "NaN";
 JsonParser parser = mapper.getFactory().createParser(json);
 assertNull(parser.readValueAs(Integer.class));
 }

 @Test
 public void testIntegerSerializerWithStringValueInfinity() throws JsonProcessingException {
 String json = "Infinity";
 JsonParser parser = mapper.getFactory().createParser(json);
 assertNull(parser.readValueAs(Integer.class));
 }

 @Test
 public void testIntegerSerializerWithStringValueInfNegative() throws JsonProcessingException {
 String json = "-Infinity";
 JsonParser parser = mapper.getFactory().createParser(json);
 assertNull(parser.readValueAs(Integer.class));
 }

 @Test
 public void testIntegerSerializerWithStringValueNull() throws JsonProcessingException {
 String json = "null";
 JsonParser parser = mapper.getFactory().createParser(json);
 assertNull(parser.readValueAs(Integer.class));
 }

 @Test
 public void testLongSerializer() throws JsonProcessingException {
 Long value = 123L;
 String json = mapper.writeValueAsString(value);
 JsonParser parser = mapper.getFactory().createParser(json);
 assertEquals(value.longValue(), parser.readValueAs(Long.class));
 }

 @Test
 public void testLongSerializerWithNegativeValue() throws JsonProcessingException {
 Long value = -123L;
 String json = mapper.writeValueAsString(value);
 JsonParser parser = mapper.getFactory().createParser(json);
 assertEquals(value.longValue(), parser.readValueAs(Long.class));
 }

 @Test
 public void testLongSerializerWithMaxValue() throws JsonProcessingException {
 Long value = Long.MAX_VALUE;
 String json = mapper.writeValueAsString(value);
 JsonParser parser = mapper.getFactory().createParser(json);
 assertEquals(value.longValue(), parser.readValueAs(Long.class));
 }

 @Test
 public void testLongSerializerWithMinValue() throws JsonProcessingException {
 Long value = Long.MIN_VALUE;
 String json = mapper.writeValueAsString(value);
 JsonParser parser = mapper.getFactory().createParser(json);
 assertEquals(value.longValue(), parser.readValueAs(Long.class));
 }

 @Test
 public void testLongSerializerWithStringValue() throws JsonProcessingException {
 Long value = 123L;
 String json = "123";
 String jsonWithType = "{\"number\":\"123\",\"@class\":\"java.lang.Long\"}";
 ObjectMapper mapperWithType = new ObjectMapper();
 JsonNode parsedJson = mapperWithType.readTree(jsonWithType);
 JsonNode parsedJsonWithoutType = mapper.readTree(json);
 assertEquals(parsedJson.get("number"), parsedJsonWithoutType.get("number"));
 assertEquals(value.getClass(), parsedJsonWithType.getClass());
 }

 @Test
 public void testLongSerializerWithStringValueNaN() throws JsonProcessingException {
 String json = "NaN";
 JsonParser parser = mapper.getFactory().createParser(json);
 assertNull(parser.readValueAs(Long.class));
 }

 @Test
 public void testLongSerializerWithStringValueInfinity() throws JsonProcessingException {
 String json = "Infinity";
 JsonParser parser = mapper.getFactory().createParser(json);
 assertNull(parser.readValueAs(Long.class));
 }

 @Test
 public void testLongSerializerWithStringValueInfNegative() throws JsonProcessingException {
 String json = "-Infinity";
 JsonParser parser = mapper.getFactory().createParser(json);
 assertNull(parser.readValueAs(Long.class));
 }

 @Test
 public void testLongSerializerWithStringValueNull() throws JsonProcessingException {
 String json = "null";
 JsonParser parser = mapper.getFactory().createParser(json);
 assertNull(parser.readValueAs(Long.class));
 }
}