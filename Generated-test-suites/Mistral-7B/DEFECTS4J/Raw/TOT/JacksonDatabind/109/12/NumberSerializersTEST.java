import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class NumberSerializersTest {

 private final ObjectMapper mapper = new ObjectMapper();

 @Test
 public void testIntegerSerializer() throws JsonProcessingException {
 Integer value = 123;
 String json = mapper.writerWithTypeValidator(false).writeValueAsString(value);
 assertEquals("123", json);

 Integer deserializedValue = mapper.readValue(json, Integer.class);
 assertEquals(value.intValue(), deserializedValue.intValue());
 }

 @Test
 public void testIntegerSerializerWithNegativeNumber() throws JsonProcessingException {
 Integer value = -123;
 String json = mapper.writerWithTypeValidator(false).writeValueAsString(value);
 assertEquals("-123", json);

 Integer deserializedValue = mapper.readValue(json, Integer.class);
 assertEquals(value.intValue(), deserializedValue.intValue());
 }

 @Test(expected = JsonMappingException.class)
 public void testIntegerSerializerWithNonIntegerValue() throws JsonProcessingException {
 String nonIntegerValue = "hello";
 String json = mapper.writerWithTypeValidator(false).writeValueAsString(nonIntegerValue);

 try {
 mapper.readValue(json, Integer.class);
 } catch (JsonMappingException e) {
 assertEquals("Cannot deserialize value of type [simple type, class java.lang.Integer] from JSON String \"hello\": not a numeric value", e.getMessage());
 }
 }

 @Test
 public void testFloatSerializer() throws JsonProcessingException {
 Float value = 123.45f;
 String json = mapper.writerWithTypeValidator(false).writeValueAsString(value);
 assertEquals("123.45", json);

 Float deserializedValue = mapper.readValue(json, Float.class);
 assertEquals(value.floatValue(), deserializedValue.floatValue(), 0.001f);
 }

 @Test
 public void testFloatSerializerWithNegativeNumber() throws JsonProcessingException {
 Float value = -123.45f;
 String json = mapper.writerWithTypeValidator(false).writeValueAsString(value);
 assertEquals("-123.45", json);

 Float deserializedValue = mapper.readValue(json, Float.class);
 assertEquals(value.floatValue(), deserializedValue.floatValue(), 0.001f);
 }

 @Test(expected = JsonMappingException.class)
 public void testFloatSerializerWithNonFloatValue() throws JsonProcessingException {
 String nonFloatValue = "hello";
 String json = mapper.writerWithTypeValidator(false).writeValueAsString(nonFloatValue);

 try {
 mapper.readValue(json, Float.class);
 } catch (JsonMappingException e) {
 assertEquals("Cannot deserialize value of type [simple type, class java.lang.Float] from JSON String \"hello\": not a Number value", e.getMessage());
 }
 }

 @Test
 public void testLongSerializer() throws JsonProcessingException {
 Long value = 1234567890123L;
 String json = mapper.writerWithTypeValidator(false).writeValueAsString(value);
 assertEquals("1234567890123", json);

 Long deserializedValue = mapper.readValue(json, Long.class);
 assertEquals(value.longValue(), deserializedValue.longValue());
 }

 @Test
 public void testLongSerializerWithNegativeNumber() throws JsonProcessingException {
 Long value = -1234567890123L;
 String json = mapper.writerWithTypeValidator(false).writeValueAsString(value);
 assertEquals("-1234567890123", json);

 Long deserializedValue = mapper.readValue(json, Long.class);
 assertEquals(value.longValue(), deserializedValue.longValue());
 }

 @Test(expected = JsonMappingException.class)
 public void testLongSerializerWithNonLongValue() throws JsonProcessingException {
 String nonLongValue = "hello";
 String json = mapper.writerWithTypeValidator(false).writeValueAsString(nonLongValue);

 try {
 mapper.readValue(json, Long.class);
 } catch (JsonMappingException e) {
 assertEquals("Cannot deserialize value of type [simple type, class java.lang.Long] from JSON String \"hello\": not a Number value", e.getMessage());
 }
 }

 @Test
 public void testShortSerializer() throws JsonProcessingException {
 Short value = (short) 123;
 String json = mapper.writerWithTypeValidator(false).writeValueAsString(value);
 assertEquals("123", json);

 Short deserializedValue = mapper.readValue(json, Short.class);
 assertEquals(value.shortValue(), deserializedValue.shortValue());
 }

 @Test
 public void testShortSerializerWithNegativeNumber() throws JsonProcessingException {
 Short value = (short) -123;
 String json = mapper.writerWithTypeValidator(false).writeValueAsString(value);
 assertEquals("-123", json);

 Short deserializedValue = mapper.readValue(json, Short.class);
 assertEquals(value.shortValue(), deserializedValue.shortValue());
 }

 @Test(expected = JsonMappingException.class)
 public void testShortSerializerWithNonShortValue() throws JsonProcessingException {
 String nonShortValue = "hello";
 String json = mapper.writerWithTypeValidator(false).writeValueAsString(nonShortValue);

 try {
 mapper.readValue(json, Short.class);
 } catch (JsonMappingException e) {
 assertEquals("Cannot deserialize value of type [simple type, class java.lang.Short] from JSON String \"hello\": not a Number value", e.getMessage());
 }
 }
}