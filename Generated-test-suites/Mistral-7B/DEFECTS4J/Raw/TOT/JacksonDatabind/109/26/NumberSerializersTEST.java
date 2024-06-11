import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class NumberSerializersTest {

 @Test
 public void testIntegerSerializer() throws JsonProcessingException {
 Integer value = 123;
 ObjectMapper mapper = new ObjectMapper();
 String json = mapper.writeValueAsString(value);
 JsonParser parser = mapper.getFactory().createParser(json);
 assertEquals(value.intValue(), parser.readValueAs(Integer.class));
 }

 @Test
 public void testIntegerSerializerWithNegativeValue() throws JsonProcessingException {
 Integer value = -123;
 ObjectMapper mapper = new ObjectMapper();
 String json = mapper.writeValueAsString(value);
 JsonParser parser = mapper.getFactory().createParser(json);
 assertEquals(value.intValue(), parser.readValueAs(Integer.class));
 }

 @Test
 public void testIntegerSerializerWithMaxValue() throws JsonProcessingException {
 Integer value = Integer.MAX_VALUE;
 ObjectMapper mapper = new ObjectMapper();
 String json = mapper.writeValueAsString(value);
 JsonParser parser = mapper.getFactory().createParser(json);
 assertEquals(value.intValue(), parser.readValueAs(Integer.class));
 }

 @Test(expected = JsonProcessingException.class)
 public void testIntegerSerializerWithStringValue() throws JsonProcessingException {
 ObjectMapper mapper = new ObjectMapper();
 String json = "abc";
 JsonParser parser = mapper.getFactory().createParser(json);
 parser.nextToken(); // skip INT_VALUE token
 parser.nextToken(); // read STRING_VALUE token
 parser.readValueAs(Integer.class); // throws JsonProcessingException
 }

 @Test
 public void testIntLikeSerializer() throws JsonProcessingException {
 Number value = 123;
 ObjectMapper mapper = new ObjectMapper();
 String json = mapper.writeValueAsString(value);
 JsonParser parser = mapper.getFactory().createParser(json);
 assertEquals(value.intValue(), parser.readValueAs(Integer.class));
 }

 @Test
 public void testIntLikeSerializerWithLongValue() throws JsonProcessingException {
 Number value = 123L;
 ObjectMapper mapper = new ObjectMapper();
 String json = mapper.writeValueAsString(value);
 JsonParser parser = mapper.getFactory().createParser(json);
 assertEquals(value.intValue(), parser.readValueAs(Integer.class));
 }

 @Test
 public void testLongSerializer() throws JsonProcessingException {
 Long value = 123L;
 ObjectMapper mapper = new ObjectMapper();
 String json = mapper.writeValueAsString(value);
 JsonParser parser = mapper.getFactory().createParser(json);
 assertEquals(value.longValue(), parser.readValueAs(Long.class));
 }

 @Test
 public void testLongSerializerWithMaxValue() throws JsonProcessingException {
 Long value = Long.MAX_VALUE;
 ObjectMapper mapper = new ObjectMapper();
 String json = mapper.writeValueAsString(value);
 JsonParser parser = mapper.getFactory().createParser(json);
 assertEquals(value.longValue(), parser.readValueAs(Long.class));
 }

 @Test(expected = JsonProcessingException.class)
 public void testLongSerializerWithStringValue() throws JsonProcessingException {
 ObjectMapper mapper = new ObjectMapper();
 String json = "abc";
 JsonParser parser = mapper.getFactory().createParser(json);
 parser.nextToken(); // skip LONG_VALUE token
 parser.nextToken(); // read STRING_VALUE token
 parser.readValueAs(Long.class); // throws JsonProcessingException
 }

 @Test
 public void testFloatSerializer() throws JsonProcessingException {
 Float value = 123.45f;
 ObjectMapper mapper = new ObjectMapper();
 String json = mapper.writeValueAsString(value);
 JsonParser parser = mapper.getFactory().createParser(json);
 assertEquals(value.floatValue(), parser.readValueAs(Float.class));
 }

 @Test
 public void testFloatSerializerWithNegativeValue() throws JsonProcessingException {
 Float value = -123.45f;
 ObjectMapper mapper = new ObjectMapper();
 String json = mapper.writeValueAsString(value);
 JsonParser parser = mapper.getFactory().createParser(json);
 assertEquals(value.floatValue(), parser.readValueAs(Float.class));
 }

 @Test
 public void testFloatSerializerWithMaxValue() throws JsonProcessingException {
 Float value = Float.POSITIVE_INFINITY;
 ObjectMapper mapper = new ObjectMapper();
 String json = mapper.writeValueAsString(value);
 JsonParser parser = mapper.getFactory().createParser(json);
 assertEquals(value, parser.readValueAs(Float.class));
 }

 @Test(expected = JsonProcessingException.class)
 public void testFloatSerializerWithStringValue() throws JsonProcessingException {
 ObjectMapper mapper = new ObjectMapper();
 String json = "abc";
 JsonParser parser = mapper.getFactory().createParser(json);
 parser.nextToken(); // skip FLOAT_VALUE token
 parser.nextToken(); // read STRING_VALUE token
 parser.readValueAs(Float.class); // throws JsonProcessingException
 }

 @Test
 public void testDoubleSerializer() throws JsonProcessingException {
 Double value = 123.45;
 ObjectMapper mapper = new ObjectMapper();
 String json = mapper.writeValueAsString(value);
 JsonParser parser = mapper.getFactory().createParser(json);
 assertEquals(value.doubleValue(), parser.readValueAs(Double.class));
 }

 @Test
 public void testDoubleSerializerWithNegativeValue() throws JsonProcessingException {
 Double value = -123.45;
 ObjectMapper mapper = new ObjectMapper();
 String json = mapper.writeValueAsString(value);
 JsonParser parser = mapper.getFactory().createParser(json);
 assertEquals(value.doubleValue(), parser.readValueAs(Double.class));
 }

 @Test
 public void testDoubleSerializerWithMaxValue() throws JsonProcessingException {
 Double value = Double.POSITIVE_INFINITY;
 ObjectMapper mapper = new ObjectMapper();
 String json = mapper.writeValueAsString(value);
 JsonParser parser = mapper.getFactory().createParser(json);
 assertEquals(value, parser.readValueAs(Double.class));
 }

 @Test(expected = JsonProcessingException.class)
 public void testDoubleSerializerWithStringValue() throws JsonProcessingException {
 ObjectMapper mapper = new ObjectMapper();
 String json = "abc";
 JsonParser parser = mapper.getFactory().createParser(json);
 parser.nextToken(); // skip DOUBLE_VALUE token
 parser.nextToken(); // read STRING_VALUE token
 parser.readValueAs(Double.class); // throws JsonProcessingException
 }
}