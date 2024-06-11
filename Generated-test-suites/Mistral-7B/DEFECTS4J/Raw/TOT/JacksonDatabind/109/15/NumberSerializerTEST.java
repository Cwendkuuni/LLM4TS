import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class NumberSerializerTest {

 private final ObjectMapper mapper = new ObjectMapper();

 @Test
 public void testSerializeBigDecimal() throws JsonMappingException, JsonGenerationException {
 NumberSerializer numberSerializer = NumberSerializer.instance;
 BigDecimal bigDecimal = new BigDecimal("123.456");

 // Test serializing a BigDecimal
 String json = mapper.writerWithRootValue(numberSerializer).writeValueAsString(bigDecimal);
 assertEquals("123.456", json);
 }

 @Test
 public void testSerializeBigInteger() throws JsonMappingException, JsonGenerationException {
 NumberSerializer numberSerializer = NumberSerializer.instance;
 BigInteger bigInteger = new BigInteger("123456789012345678901234567890");

 // Test serializing a BigInteger
 String json = mapper.writerWithRootValue(numberSerializer).writeValueAsString(bigInteger);
 assertEquals("123456789012345678901234567890", json);
 }

 @Test
 public void testSerializeOtherNumberTypes() throws JsonMappingException, JsonGenerationException {
 NumberSerializer numberSerializer = NumberSerializer.instance;

 // Test serializing an Integer
 Integer integer = 123;
 String json = mapper.writerWithRootValue(numberSerializer).writeValueAsString(integer);
 assertEquals("123", json);

 // Test serializing a Long
 Long longNumber = 1234567890L;
 String json = mapper.writerWithRootValue(numberSerializer).writeValueAsString(longNumber);
 assertEquals("1234567890", json);

 // Test serializing a Double
 Double doubleNumber = 123.456;
 String json = mapper.writerWithRootValue(numberSerializer).writeValueAsString(doubleNumber);
 assertEquals("123.456", json);

 // Test serializing a Float
 Float floatNumber = 123.45f;
 String json = mapper.writerWithRootValue(numberSerializer).writeValueAsString(floatNumber);
 assertEquals("123.45", json);
 }

 @Test(expected = JsonMappingException.class)
 public void testSerializeInvalidType() throws JsonMappingException, JsonGenerationException {
 NumberSerializer numberSerializer = NumberSerializer.instance;
 String invalidType = new Object();

 // Test serializing an invalid type should throw JsonMappingException
 String json = mapper.writerWithRootValue(numberSerializer).writeValueAsString(invalidType);
 }
}