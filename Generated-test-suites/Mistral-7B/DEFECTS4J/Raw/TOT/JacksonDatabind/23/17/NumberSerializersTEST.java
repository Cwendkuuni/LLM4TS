import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import static org.junit.Assert.*;

public class NumberSerializersTest {

 private ObjectMapper mapper = new ObjectMapper();

 @Test
 public void testIntegerSerializer() throws JsonProcessingException {
 Integer intValue = 123;
 String json = mapper.writeValueAsString(intValue);
 assertEquals("123", json);

 JsonParser parser = mapper.getFactory().createParser(json);
 Integer deserializedInt = mapper.readValue(parser, Integer.class);
 assertEquals(intValue, deserializedInt);
 }

 @Test
 public void testIntegerSerializerWithNegativeNumber() throws JsonProcessingException {
 Integer negativeIntValue = -123;
 String json = mapper.writeValueAsString(negativeIntValue);
 assertEquals("-123", json);

 JsonParser parser = mapper.getFactory().createParser(json);
 Integer deserializedInt = mapper.readValue(parser, Integer.class);
 assertEquals(negativeIntValue, deserializedInt);
 }

 @Test(expected = JsonMappingException.class)
 public void testIntegerSerializerWithNonNumber() throws JsonProcessingException {
 String nonNumberJson = "\"abc\"";
 JsonParser parser = mapper.getFactory().createParser(nonNumberJson);
 mapper.readValue(parser, Integer.class);
 }

 @Test
 public void testFloatSerializer() throws JsonProcessingException {
 Float floatValue = 123.45f;
 String json = mapper.writeValueAsString(floatValue);
 assertEquals("123.45", json);

 JsonParser parser = mapper.getFactory().createParser(json);
 Float deserializedFloat = mapper.readValue(parser, Float.class);
 assertEquals(floatValue, deserializedFloat, 0.001f);
 }

 @Test
 public void testFloatSerializerWithNegativeNumber() throws JsonProcessingException {
 Float negativeFloatValue = -123.45f;
 String json = mapper.writeValueAsString(negativeFloatValue);
 assertEquals("-123.45", json);

 JsonParser parser = mapper.getFactory().createParser(json);
 Float deserializedFloat = mapper.readValue(parser, Float.class);
 assertEquals(negativeFloatValue, deserializedFloat, 0.001f);
 }

 @Test(expected = JsonMappingException.class)
 public void testFloatSerializerWithNonNumber() throws JsonProcessingException {
 String nonNumberJson = "\"abc\"";
 JsonParser parser = mapper.getFactory().createParser(nonNumberJson);
 mapper.readValue(parser, Float.class);
 }

 @Test
 public void testLongSerializer() throws JsonProcessingException {
 Long longValue = 1234567890123L;
 String json = mapper.writeValueAsString(longValue);
 assertEquals("1234567890123", json);

 JsonParser parser = mapper.getFactory().createParser(json);
 Long deserializedLong = mapper.readValue(parser, Long.class);
 assertEquals(longValue, deserializedLong);
 }

 @Test
 public void testLongSerializerWithNegativeNumber() throws JsonProcessingException {
 Long negativeLongValue = -1234567890123L;
 String json = mapper.writeValueAsString(negativeLongValue);
 assertEquals("-1234567890123", json);

 JsonParser parser = mapper.getFactory().createParser(json);
 Long deserializedLong = mapper.readValue(parser, Long.class);
 assertEquals(negativeLongValue, deserializedLong);
 }

 @Test(expected = JsonMappingException.class)
 public void testLongSerializerWithNonNumber() throws JsonProcessingException {
 String nonNumberJson = "\"abc\"";
 JsonParser parser = mapper.getFactory().createParser(nonNumberJson);
 mapper.readValue(parser, Long.class);
 }
}