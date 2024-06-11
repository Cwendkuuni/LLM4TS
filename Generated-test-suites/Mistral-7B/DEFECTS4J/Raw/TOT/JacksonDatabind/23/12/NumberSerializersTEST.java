import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class NumberSerializersTest {

 private ObjectMapper objectMapper;

 @Before
 public void setup() {
 objectMapper = new ObjectMapper();
 }

 @Test
 public void testIntegerSerializer() throws JsonProcessingException {
 Integer number = 123;
 String jsonString = "123";

 // Test serialization
 JsonGenerator gen = objectMapper.getFactory().createGenerator(System.out);
 objectMapper.writeValue(gen, number);
 gen.flush();

 // Test deserialization
 JsonParser parser = objectMapper.getFactory().createParser(new StringReader(jsonString));
 JsonToken token = parser.nextToken();
 assertEquals(JsonToken.VALUE_NUMBER_INT, token);
 assertEquals(number, objectMapper.readValue(parser, Integer.class));
 }

 @Test
 public void testIntegerSerializerWithNegativeNumber() throws JsonProcessingException {
 Integer number = -123;
 String jsonString = "-123";

 // Test serialization
 JsonGenerator gen = objectMapper.getFactory().createGenerator(System.out);
 objectMapper.writeValue(gen, number);
 gen.flush();

 // Test deserialization
 JsonParser parser = objectMapper.getFactory().createParser(new StringReader(jsonString));
 JsonToken token = parser.nextToken();
 assertEquals(JsonToken.VALUE_NUMBER_INT, token);
 assertEquals(number, objectMapper.readValue(parser, Integer.class));
 }

 @Test
 public void testIntegerSerializerWithLargeNumber() throws JsonProcessingException {
 Integer number = Integer.MAX_VALUE;
 String jsonString = Integer.toString(number);

 // Test serialization
 JsonGenerator gen = objectMapper.getFactory().createGenerator(System.out);
 objectMapper.writeValue(gen, number);
 gen.flush();

 // Test deserialization
 JsonParser parser = objectMapper.getFactory().createParser(new StringReader(jsonString));
 JsonToken token = parser.nextToken();
 assertEquals(JsonToken.VALUE_NUMBER_INT, token);
 assertEquals(number, objectMapper.readValue(parser, Integer.class));
 }

 @Test(expected = JsonProcessingException.class)
 public void testIntegerSerializerWithString() throws JsonProcessingException {
 String string = "abc";

 // Test serialization will throw exception
 JsonGenerator gen = objectMapper.getFactory().createGenerator(System.out);
 objectMapper.writeValue(gen, string);
 gen.flush();

 // Test deserialization will also throw exception
 JsonParser parser = objectMapper.getFactory().createParser(new StringReader(string));
 objectMapper.readValue(parser, Integer.class);
 }

 @Test
 public void testShortSerializer() throws JsonProcessingException {
 Short number = (short) 123;
 String jsonString = "123";

 // Test serialization
 JsonGenerator gen = objectMapper.getFactory().createGenerator(System.out);
 objectMapper.writeValue(gen, number);
 gen.flush();

 // Test deserialization
 JsonParser parser = objectMapper.getFactory().createParser(new StringReader(jsonString));
 JsonToken token = parser.nextToken();
 assertEquals(JsonToken.VALUE_NUMBER_INT, token);
 assertEquals(number, objectMapper.readValue(parser, Short.class));
 }

 // ... Repeat similar tests for LongSerializer, FloatSerializer, and DoubleSerializer

 // ... Add more test cases to cover edge cases and error scenarios
}