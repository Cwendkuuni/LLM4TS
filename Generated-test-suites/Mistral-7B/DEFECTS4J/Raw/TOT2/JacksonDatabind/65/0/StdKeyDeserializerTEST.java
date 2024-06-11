import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.*;
import com.fasterxml.jackson.databind.deser.std.*;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

public class StdKeyDeserializerTest {

 @Test
 public void testBooleanDeserialization() throws JsonProcessingException {
 StringKeyDeserializer stdKeyDeserializer = StdKeyDeserializer.forType(Boolean.class);
 ObjectMapper mapper = new ObjectMapper();
 String key = "true";
 Object result = mapper.readValue(key, Map.class.getType(), stdKeyDeserializer);
 assertEquals(Boolean.TRUE, result);
 }

 @Test
 public void testBooleanDeserializationWithInvalidKey() throws JsonProcessingException {
 StringKeyDeserializer stdKeyDeserializer = StdKeyDeserializer.forType(Boolean.class);
 ObjectMapper mapper = new ObjectMapper();
 String key = "invalid";
 assertNull(mapper.readValue(key, Map.class.getType(), stdKeyDeserializer));
 }

 @Test
 public void testByteDeserialization() throws JsonProcessingException {
 StringKeyDeserializer stdKeyDeserializer = StdKeyDeserializer.forType(Byte.class);
 ObjectMapper mapper = new ObjectMapper();
 String key = "127";
 Object result = mapper.readValue(key, Map.class.getType(), stdKeyDeserializer);
 assertEquals((Byte) 127, result);
 }

 @Test
 public void testByteDeserializationWithOverflowKey() throws JsonProcessingException {
 StringKeyDeserializer stdKeyDeserializer = StdKeyDeserializer.forType(Byte.class);
 ObjectMapper mapper = new ObjectMapper();
 String key = "256";
 assertThrows(JsonMappingException.class, () -> mapper.readValue(key, Map.class.getType(), stdKeyDeserializer));
 }

 @Test
 public void testShortDeserialization() throws JsonProcessingException {
 StringKeyDeserializer stdKeyDeserializer = StdKeyDeserializer.forType(Short.class);
 ObjectMapper mapper = new ObjectMapper();
 String key = "12345";
 Object result = mapper.readValue(key, Map.class.getType(), stdKeyDeserializer);
 assertEquals((Short) 12345, result);
 }

 @Test
 public void testShortDeserializationWithOverflowKey() throws JsonProcessingException {
 StringKeyDeserializer stdKeyDeserializer = StdKeyDeserializer.forType(Short.class);
 ObjectMapper mapper = new ObjectMapper();
 String key = "32768";
 assertThrows(JsonMappingException.class, () -> mapper.readValue(key, Map.class.getType(), stdKeyDeserializer));
 }

 @Test
 public void testCharDeserialization() throws JsonProcessingException {
 StringKeyDeserializer stdKeyDeserializer = StdKeyDeserializer.forType(Character.class);
 ObjectMapper mapper = new ObjectMapper();
 String key = "A";
 Object result = mapper.readValue(key, Map.class.getType(), stdKeyDeserializer);
 assertEquals((Character) 'A', result);
 }

 @Test
 public void testCharDeserializationWithInvalidLengthKey() throws JsonProcessingException {
 StringKeyDeserializer stdKeyDeserializer = StdKeyDeserializer.forType(Character.class);
 ObjectMapper mapper = new ObjectMapper();
 String key = "AB";
 assertNull(mapper.readValue(key, Map.class.getType(), stdKeyDeserializer));
 }

 @Test
 public void testIntDeserialization() throws JsonProcessingException {
 StringKeyDeserializer stdKeyDeserializer = StdKeyDeserializer.forType(Integer.class);
 ObjectMapper mapper = new ObjectMapper();
 String key = "123456";
 Object result = mapper.readValue(key, Map.class.getType(), stdKeyDeserializer);
 assertEquals((Integer) 123456, result);
 }

 @Test
 public void testLongDeserialization() throws JsonProcessingException {
 StringKeyDeserializer stdKeyDeserializer = StdKeyDeserializer.forType(Long.class);
 ObjectMapper mapper = new ObjectMapper();
 String key = "12345678901234567890";
 Object result = mapper.readValue(key, Map.class.getType(), stdKeyDeserializer);
 assertEquals((Long) 12345678901234567890L, result);
 }

 @Test
 public void testDoubleDeserialization() throws JsonProcessingException {
 StringKeyDeserializer stdKeyDeserializer = StdKeyDeserializer.forType(Double.class);
 ObjectMapper mapper = new ObjectMapper();
 String key = "3.141592653589";
 Object result = mapper.readValue(key, Map.class.getType(), stdKeyDeserializer);
 assertEquals((Double) 3.141592653589D, result);
 }

 @Test
 public void testDateDeserialization() throws JsonProcessingException {
 StringKeyDeserializer stdKeyDeserializer = StdKeyDeserializer.forType(Date.class);
 ObjectMapper mapper = new ObjectMapper();
 String key = "2022-12-31";
 Object result = mapper.readValue(key, Map.class.getType(), stdKeyDeserializer);
 assertEquals(new Date(1671139200000L), result);
 }

 @Test
 public void testCalendarDeserialization() throws JsonProcessingException {
 StringKeyDeserializer stdKeyDeserializer = StdKeyDeserializer.forType(Calendar.class);
 ObjectMapper mapper = new ObjectMapper();
 String key = "2022-12-31";
 Object result = mapper.readValue(key, Map.class.getType(), stdKeyDeserializer);
 java.util.Calendar calendar = (java.util.Calendar) result;
 assertEquals(2022, calendar.get(Calendar.YEAR));
 assertEquals(12, calendar.get(Calendar.MONTH));
 assertEquals(31, calendar.get(Calendar.DAY_OF_MONTH));
 }

 @Test
 public void testUuidDeserialization() throws JsonProcessingException {
 StringKeyDeserializer stdKeyDeserializer = StdKeyDeserializer.forType(UUID.class);
 ObjectMapper mapper = new ObjectMapper();
 String key = "00000000-0000-1000-8000-00805f4efb4d";
 Object result = mapper.readValue(key, Map.class.getType(), stdKeyDeserializer);
 assertEquals(new UUID("00000000-0000-1000-8000-00805f4efb4d"), result);
 }

 @Test
 public void testUriDeserialization() throws JsonProcessingException {
 StringKeyDeserializer stdKeyDeserializer = StdKeyDeserializer.forType(URI.class);
 ObjectMapper mapper = new ObjectMapper();
 String key = "https://www.example.com";
 Object result = mapper.readValue(key, Map.class.getType(), stdKeyDeserializer);
 assertEquals(new URI("https://www.example.com"), result);
 }

 @Test
 public void testUrlDeserialization() throws JsonProcessingException {
 StringKeyDeserializer stdKeyDeserializer = StdKeyDeserializer.forType(URL.class);
 ObjectMapper mapper = new ObjectMapper();
 String key = "https://www.example.com";
 Object result = mapper.readValue(key, Map.class.getType(), stdKeyDeserializer);
 assertEquals(new URL("https://www.example.com"), result);
 }

 @Test
 public void testClassDeserialization() throws JsonProcessingException {
 StringKeyDeserializer stdKeyDeserializer = StdKeyDeserializer.forType(String.class);
 ObjectMapper mapper = new ObjectMapper();
 String key = "java.lang.String";
 Object result = mapper.readValue(key, Map.class.getType(), stdKeyDeserializer);
 assertEquals(String.class, result);
 }

 @Test
 public void testLocaleDeserialization() throws JsonProcessingException {
 StringKeyDeserializer stdKeyDeserializer = StdKeyDeserializer.forType(Locale.class);
 ObjectMapper mapper = new ObjectMapper();
 String key = "en_US";
 Object result = mapper.readValue(key, Map.class.getType(), stdKeyDeserializer);
 assertEquals(Locale.US, result);
 }

 @Test
 public void testCurrencyDeserialization() throws JsonProcessingException {
 StringKeyDeserializer stdKeyDeserializer = StdKeyDeserializer.forType(Currency.class);
 ObjectMapper mapper = new ObjectMapper();
 String key = "USD";
 Object result = mapper.readValue(key, Map.class.getType(), stdKeyDeserializer);
 assertEquals(Currency.getInstance("USD"), result);
 }

 @Test
 public void testDeserializationWithNullKey() throws JsonProcessingException {
 StringKeyDeserializer stdKeyDeserializer = StdKeyDeserializer.forType(String.class);
 ObjectMapper mapper = new ObjectMapper();
 Object result = mapper.readValue(null, Map.class.getType(), stdKeyDeserializer);
 assertNull(result);
 }

 @Test
 public void testDeserializationWithInvalidKey() throws JsonProcessingException {
 StringKeyDeserializer stdKeyDeserializer = StdKeyDeserializer.forType(String.class);
 ObjectMapper mapper = new ObjectMapper();
 String key = "invalid";
 assertNull(mapper.readValue(key, Map.class.getType(), stdKeyDeserializer));
 }
}