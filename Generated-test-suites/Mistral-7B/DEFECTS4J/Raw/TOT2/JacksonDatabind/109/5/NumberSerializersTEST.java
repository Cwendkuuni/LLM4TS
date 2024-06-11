import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class NumberSerializersTest {

 // Test for addAll method
 @Test
 public void testAddAll() throws JsonProcessingException {
 ObjectMapper mapper = new ObjectMapper();
 Map<String, JsonSerializer<?>> allDeserializers = new HashMap<>();
 NumberSerializers.addAll(allDeserializers);
 JsonSerializer<?> intSerializer = allDeserializers.get(Integer.class.getName());
 JsonSerializer<?> longSerializer = allDeserializers.get(Long.class.getName());

 assertEquals(intSerializer.getClass(), JsonSerializer.class);
 assertEquals(intSerializer.getClass(), allDeserializers.get(Integer.TYPE.getName()).getClass());
 assertEquals(longSerializer.getClass(), JsonSerializer.class);
 assertEquals(longSerializer.getClass(), allDeserializers.get(Long.TYPE.getName()).getClass());
 }

 // Test for IntegerSerializer
 @Test
 public void testIntegerSerializer() throws JsonProcessingException {
 Integer input = 123;
 ObjectMapper mapper = new ObjectMapper();
 String jsonOutput = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(input);

 JsonParser parser = mapper.getFactory().createParser(jsonOutput);
 JsonToken token = parser.nextToken();
 assertEquals(JsonToken.VALUE_NUMBER_INT, token);
 assertEquals(123, parser.getValueAsInt());
 }

 // Test for IntegerSerializer with negative number
 @Test
 public void testIntegerSerializerNegativeNumber() throws JsonProcessingException {
 Integer input = -123;
 ObjectMapper mapper = new ObjectMapper();
 String jsonOutput = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(input);

 JsonParser parser = mapper.getFactory().createParser(jsonOutput);
 JsonToken token = parser.nextToken();
 assertEquals(JsonToken.VALUE_NUMBER_INT, token);
 assertEquals(-123, parser.getValueAsInt());
 }

 // Test for IntegerSerializer with maximum value
 @Test(expected = JsonProcessingException.class)
 public void testIntegerSerializerMaxValue() throws JsonProcessingException {
 Integer input = Integer.MAX_VALUE;
 ObjectMapper mapper = new ObjectMapper();
 String jsonOutput = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(input);

 JsonParser parser = mapper.getFactory().createParser(jsonOutput);
 parser.nextToken();
 }

 // Test for LongSerializer
 @Test
 public void testLongSerializer() throws JsonProcessingException {
 Long input = 1234567890123L;
 ObjectMapper mapper = new ObjectMapper();
 String jsonOutput = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(input);

 JsonParser parser = mapper.getFactory().createParser(jsonOutput);
 JsonToken token = parser.nextToken();
 assertEquals(JsonToken.VALUE_NUMBER_LONG, token);
 assertEquals(1234567890123L, parser.getValueAsLong());
 }

 // Test for LongSerializer with negative number
 @Test
 public void testLongSerializerNegativeNumber() throws JsonProcessingException {
 Long input = -1234567890123L;
 ObjectMapper mapper = new ObjectMapper();
 String jsonOutput = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(input);

 JsonParser parser = mapper.getFactory().createParser(jsonOutput);
 JsonToken token = parser.nextToken();
 assertEquals(JsonToken.VALUE_NUMBER_LONG, token);
 assertEquals(-1234567890123L, parser.getValueAsLong());
 }

 // Test for LongSerializer with maximum value
 @Test(expected = JsonProcessingException.class)
 public void testLongSerializerMaxValue() throws JsonProcessingException {
 Long input = new Long("9223372036854775807"); // Long.MAX_VALUE + 1
 ObjectMapper mapper = new ObjectMapper();
 String jsonOutput = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(input);

 JsonParser parser = mapper.getFactory().createParser(jsonOutput);
 parser.nextToken();
 }
}