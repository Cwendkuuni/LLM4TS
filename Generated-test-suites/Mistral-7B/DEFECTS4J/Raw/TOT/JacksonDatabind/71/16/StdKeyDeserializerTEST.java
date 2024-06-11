import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class StdKeyDeserializerTest {

 private ObjectMapper objectMapper;

 @Before
 public void setup() {
 objectMapper = new ObjectMapper();
 }

 @Test
 public void testBooleanDeserializer() throws JsonProcessingException {
 StdKeyDeserializer booleanDeserializer = StdKeyDeserializer.forType(Boolean.class);
 Object key = booleanDeserializer.deserializeKey("true", objectMapper.getDeserializationContext());
 assertEquals(Boolean.TRUE, key);
 key = booleanDeserializer.deserializeKey("false", objectMapper.getDeserializationContext());
 assertEquals(Boolean.FALSE, key);
 }

 @Test
 public void testByteDeserializer() throws JsonProcessingException {
 StdKeyDeserializer byteDeserializer = StdKeyDeserializer.forType(Byte.class);
 Object key = byteDeserializer.deserializeKey("127", objectMapper.getDeserializationContext());
 assertEquals((Byte) 127, key);
 key = byteDeserializer.deserializeKey("128", objectMapper.getDeserializationContext());
 assertNull(key);
 }

 @Test
 public void testShortDeserializer() throws JsonProcessingException {
 StdKeyDeserializer shortDeserializer = StdKeyDeserializer.forType(Short.class);
 Object key = shortDeserializer.deserializeKey("32767", objectMapper.getDeserializationContext());
 assertEquals((Short) 32767, key);
 key = shortDeserializer.deserializeKey("32768", objectMapper.getDeserializationContext());
 assertNull(key);
 }

 @Test
 public void testCharDeserializer() throws JsonProcessingException {
 StdKeyDeserializer charDeserializer = StdKeyDeserializer.forType(Character.class);
 Object key = charDeserializer.deserializeKey("A", objectMapper.getDeserializationContext());
 assertEquals(Character.valueOf('A'), key);
 key = charDeserializer.deserializeKey("B", objectMapper.getDeserializationContext());
 assertNull(key);
 }

 @Test
 public void testIntDeserializer() throws JsonProcessingException {
 StdKeyDeserializer intDeserializer = StdKeyDeserializer.forType(Integer.class);
 Object key = intDeserializer.deserializeKey("123", objectMapper.getDeserializationContext());
 assertEquals(Integer.valueOf(123), key);
 }

 @Test
 public void testLongDeserializer() throws JsonProcessingException {
 StdKeyDeserializer longDeserializer = StdKeyDeserializer.forType(Long.class);
 Object key = longDeserializer.deserializeKey("123456789012345", objectMapper.getDeserializationContext());
 assertEquals(Long.valueOf(123456789012345L), key);
 }

 @Test
 public void testDateDeserializer() throws JsonProcessingException, ParseException {
 StdKeyDeserializer dateDeserializer = StdKeyDeserializer.forType(Date.class);
 Object key = dateDeserializer.deserializeKey("2022-12-31", objectMapper.getDeserializationContext());
 assertEquals(new Date(2022, 11, 30), key); // Note: Java's Date class uses 11 instead of 12 for December
 }

 @Test
 public void testCalendarDeserializer() throws JsonProcessingException, ParseException {
 StdKeyDeserializer calendarDeserializer = StdKeyDeserializer.forType(Calendar.class);
 Object key = calendarDeserializer.deserializeKey("2022-12-31", objectMapper.getDeserializationContext());
 assertEquals(Calendar.getInstance().set(Calendar.YEAR, 2022)
         .set(Calendar.MONTH, Calendar.DECEMBER)
         .set(Calendar.DAY_OF_MONTH, 31), key);
 }

 @Test
 public void testUuidDeserializer() throws JsonProcessingException {
 StdKeyDeserializer uuidDeserializer = StdKeyDeserializer.forType(UUID.class);
 Object key = uuidDeserializer.deserializeKey("d5a23c3c-546e-4b3b-b887-6d41db8e6a83", objectMapper.getDeserializationContext());
 assertEquals(UUID.fromString("d5a23c3c-546e-4b3b-b887-6d41db8e6a83"), key);
 }

 @Test
 public void testUriDeserializer() throws JsonProcessingException {
 StdKeyDeserializer uriDeserializer = StdKeyDeserializer.forType(URI.class);
 Object key = uriDeserializer.deserializeKey("https://example.com", objectMapper.getDeserializationContext());
 assertEquals(new URI("https://example.com"), key);
 }

 @Test
 public void testUrlDeserializer() throws JsonProcessingException {
 StdKeyDeserializer urlDeserializer = StdKeyDeserializer.forType(URL.class);
 Object key = urlDeserializer.deserializeKey("https://example.com", objectMapper.getDeserializationContext());
 assertEquals(new URL("https://example.com"), key);
 }

 @Test
 public void testClassDeserializer() throws JsonProcessingException, ClassNotFoundException {
 StdKeyDeserializer classDeserializer = StdKeyDeserializer.forType(String.class);
 Object key = classDeserializer.deserializeKey("java.lang.String", objectMapper.getDeserializationContext());
 assertEquals(String.class, key);
 }

 @Test
 public void testLocaleDeserializer() throws JsonProcessingException {
 StdKeyDeserializer localeDeserializer = StdKeyDeserializer.forType(Locale.class);
 Object key = localeDeserializer.deserializeKey("en_US", objectMapper.getDeserializationContext());
 assertEquals(Locale.US, key);
 }

 @Test
 public void testCurrencyDeserializer() throws JsonProcessingException {
 StdKeyDeserializer currencyDeserializer = StdKeyDeserializer.forType(Currency.class);
 Object key = currencyDeserializer.deserializeKey("USD", objectMapper.getDeserializationContext());
 assertEquals(Currency.getInstance("USD"), key);
 }

 @Test
 public void testUnknownDeserializer() throws JsonProcessingException {
 StdKeyDeserializer unknownDeserializer = StdKeyDeserializer.forType(Float.class);
 try {
 unknownDeserializer.deserializeKey("invalid", objectMapper.getDeserializationContext());
 } catch (IllegalArgumentException e) {
 assertEquals("not a valid representation", e.getMessage());
 }
 }
}