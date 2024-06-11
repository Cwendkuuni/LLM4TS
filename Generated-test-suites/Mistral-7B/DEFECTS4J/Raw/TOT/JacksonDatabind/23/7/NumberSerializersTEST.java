import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class NumberSerializersTest {

 @Test
 public void testIntegerSerializer() throws JsonProcessingException {
 ObjectMapper mapper = new ObjectMapper();
 Integer value = 123;
 String json = mapper.writeValueAsString(value);
 JsonParser parser = mapper.getFactory().createParser(json);
 Integer deserializedValue = mapper.readValue(parser, Integer.class);
 assertEquals(value, deserializedValue);
 }

 @Test
 public void testIntegerSerializerWithMinValue() throws JsonProcessingException {
 ObjectMapper mapper = new ObjectMapper();
 Integer minValue = Integer.MIN_VALUE;
 String json = mapper.writeValueAsString(minValue);
 JsonParser parser = mapper.getFactory().createParser(json);
 Integer deserializedValue = mapper.readValue(parser, Integer.class);
 assertEquals(minValue, deserializedValue);
 }

 @Test(expected = JsonMappingException.class)
 public void testIntegerSerializerWithString() throws JsonProcessingException {
 ObjectMapper mapper = new ObjectMapper();
 String json = "\"hello\"";
 JsonParser parser = mapper.getFactory().createParser(json);
 IntSerializer intSerializer = new IntSerializer();
 int deserializedValue = intSerializer.deserialize(parser, Integer.class, null);
 fail("Expected JsonMappingException but got: " + deserializedValue);
 }

 @Test
 public void testLongSerializer() throws JsonProcessingException {
 ObjectMapper mapper = new ObjectMapper();
 Long value = 123L;
 String json = mapper.writeValueAsString(value);
 JsonParser parser = mapper.getFactory().createParser(json);
 Long deserializedValue = mapper.readValue(parser, Long.class);
 assertEquals(value, deserializedValue);
 }

 @Test
 public void testLongSerializerWithMinValue() throws JsonProcessingException {
 ObjectMapper mapper = new ObjectMapper();
 Long minValue = Long.MIN_VALUE;
 String json = mapper.writeValueAsString(minValue);
 JsonParser parser = mapper.getFactory().createParser(json);
 Long deserializedValue = mapper.readValue(parser, Long.class);
 assertEquals(minValue, deserializedValue);
 }

 @Test(expected = JsonMappingException.class)
 public void testLongSerializerWithString() throws JsonProcessingException {
 ObjectMapper mapper = new ObjectMapper();
 String json = "\"hello\"";
 JsonParser parser = mapper.getFactory().createParser(json);
 LongSerializer longSerializer = new LongSerializer();
 long deserializedValue = longSerializer.deserialize(parser, Long.class, null);
 fail("Expected JsonMappingException but got: " + deserializedValue);
 }
}