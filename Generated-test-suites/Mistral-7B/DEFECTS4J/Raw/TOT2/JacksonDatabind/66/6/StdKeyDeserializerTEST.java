import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.test.BaseMapTest;
import com.fasterxml.jackson.databind.type.TypeFactory;

import static org.junit.Assert.*;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.*;

public class StdKeyDeserializerTest extends BaseMapTest {

 private static ObjectMapper objectMapper;

 static {
 objectMapper = new ObjectMapper();
 objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
 }

 @SuppressWarnings("unchecked")
 @Test
 public void testDeserializeBooleanKey() throws IOException {
 String key = "true";
 StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_BOOLEAN, Boolean.class);
 assertEquals(Boolean.TRUE, deserializer.deserializeKey(key, context));
 key = "false";
 assertEquals(Boolean.FALSE, deserializer.deserializeKey(key, context));
 }

 @SuppressWarnings("unchecked")
 @Test
 public void testDeserializeByteKey() throws IOException {
 String key = "127";
 StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_BYTE, Byte.class);
 assertEquals((byte) 127, deserializer.deserializeKey(key, context));
 key = "128";
 try {
 deserializer.deserializeKey(key, context);
 fail("Should have thrown exception");
 } catch (IOException e) {
 assertEquals("overflow, value can not be represented as 8-bit value", e.getMessage());
 }
 }

 @SuppressWarnings("unchecked")
 @Test
 public void testDeserializeShortKey() throws IOException {
 String key = "32767";
 StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_SHORT, Short.class);
 assertEquals((short) 32767, deserializer.deserializeKey(key, context));
 key = "32768";
 try {
 deserializer.deserializeKey(key, context);
 fail("Should have thrown exception");
 } catch (IOException e) {
 assertEquals("overflow, value can not be represented as 16-bit value", e.getMessage());
 }
 }

 @SuppressWarnings("unchecked")
 @Test
 public void testDeserializeCharKey() throws IOException {
 String key = "A";
 StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_CHAR, Character.class);
 assertEquals('A', deserializer.deserializeKey(key, context));
 key = "Zö";
 try {
 deserializer.deserializeKey(key, context);
 fail("Should have thrown exception");
 } catch (IOException e) {
 assertEquals("can only convert 1-character Strings", e.getMessage());
 }
 }

 @SuppressWarnings("unchecked")
 @Test
 public void testDeserializeIntKey() throws IOException {
 String key = "42";
 StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_INT, Integer.class);
 assertEquals(42, deserializer.deserializeKey(key, context));
 }

 @SuppressWarnings("unchecked")
 @Test
 public void testDeserializeLongKey() throws IOException {
 String key = "9223372036854775807";
 StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_LONG, Long.class);
 assertEquals(9223372036854775807L, deserializer.deserializeKey(key, context));
 key = "9223372036854775808";
 try {
 deserializer.deserializeKey(key, context);
 fail("Should have thrown exception");
 } catch (IOException e) {
 assertEquals("overflow, value can not be represented as 64-bit value", e.getMessage());
 }
 }

 @SuppressWarnings("unchecked")
 @Test
 public void testDeserializeFloatKey() throws IOException {
 String key = "3.14159265";
 StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_FLOAT, Float.class);
 assertEquals(3.14159265f, deserializer.deserializeKey(key, context), 0.001f);
 }

 @SuppressWarnings("unchecked")
 @Test
 public void testDeserializeDoubleKey() throws IOException {
 String key = "6.28318530718";
 StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_DOUBLE, Double.class);
 assertEquals(6.28318530718, deserializer.deserializeKey(key, context), 0.001);
 }

 @SuppressWarnings("unchecked")
 @Test
 public void testDeserializeLocaleKey() throws IOException {
 String key = "en_US";
 StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_LOCALE, Locale.class);
 assertEquals(Locale.US, deserializer.deserializeKey(key, context));
 key = "es_ES";
 assertEquals(Locale.SPAIN, deserializer.deserializeKey(key, context));
 }

 @SuppressWarnings("unchecked")
 @Test
 public void testDeserializeCurrencyKey() throws IOException {
 String key = "EUR";
 StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_CURRENCY, Currency.class);
 assertEquals(Currency.EUR, deserializer.deserializeKey(key, context));
 }

 @SuppressWarnings("unchecked")
 @Test
 public void testDeserializeEnumKey() throws IOException {
 Class<?> enumClass = Color.class;
 Enum[] expectedValues = enumClass.getEnumConstants();
 StdKeyDeserializer deserializer = new StdKeyDeserializer(enumClass, Color.class);

 for (Enum value : expectedValues) {
 String key = value.name();
 Object result = deserializer.deserializeKey(key, context);
 assertEquals(value, result);
 }

 String unknownKey = "InvalidColor";
 try {
 deserializer.deserializeKey(unknownKey, context);
 fail("Should have thrown exception");
 } catch (IOException e) {
 assertEquals("not one of values excepted for Enum class: [" + String.join(", ", deserializer.getEnumIds()) + "]", e.getMessage());
 }
 }

 @SuppressWarnings("unchecked")
 @Test
 public void testDeserializeDateKey() throws IOException {
 String key = "2022-03-01";
 StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_DATE, Date.class);
 Date expectedDate = new Date(2022, 2, 1);
 assertEquals(expectedDate, deserializer.deserializeKey(key, context));
 }

 @SuppressWarnings("unchecked")
 @Test
 public void testDeserializeCalendarKey() throws IOException {
 String key = "2022-03-01";
 StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_CALENDAR, Calendar.class);
 Calendar expectedCalendar = Calendar.getInstance();
 expectedCalendar.setTime(new Date(2022, 2, 1));
 assertEquals(expectedCalendar, deserializer.deserializeKey(key, context));
 }

 @SuppressWarnings("unchecked")
 @Test
 public void testDeserializeURIKey() throws IOException {
 String key = "https://example.com";
 StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_URI, URI.class);
 URI expectedUri = new URI("https", "example.com", null);
 assertEquals(expectedUri, deserializer.deserializeKey(key, context));
 }

 @SuppressWarnings("unchecked")
 @Test
 public void testDeserializeURLKey() throws IOException {
 String key = "https://example.com";
 StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_URL, URL.class);
 URL expectedUrl = new URL("https", "example.com", null);
 assertEquals(expectedUrl, deserializer.deserializeKey(key, context));
 }

 @SuppressWarnings("unchecked")
 @Test
 public void testDeserializeClassKey() throws IOException {
 String key = "java.lang.String";
 StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_CLASS, Class.class);
 assertEquals(String.class, deserializer.deserializeKey(key, context));
 }

 @SuppressWarnings("unchecked")
 @Test
 public void testDeserializeNullKey() throws IOException {
 StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_BOOLEAN, Boolean.class);
 assertNull(deserializer.deserializeKey(null, context));
 }

 @SuppressWarnings("unchecked")
 @Test
 public void testDeserializeEmptyStringKey() throws IOException {
 StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_BOOLEAN, Boolean.class);
 assertNull(deserializer.deserializeKey("", context));
 }

 @SuppressWarnings("unchecked")
 @Test
 public void testDeserializeEmptyArrayKey() throws IOException {
 String[] keys = new String[0];
 StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_BOOLEAN, Boolean.class);
 assertNull(deserializer.deserializeKey(keys, context));
 }

 @SuppressWarnings("unchecked")
 @Test
 public void testDeserializeNullValueKey() throws IOException {
 String key = null;
 StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_BOOLEAN, Boolean.class);
 try {
 deserializer.deserializeKey(key, context);
 fail("Should have thrown exception");
 } catch (IOException e) {
 assertEquals("not a valid representation", e.getMessage());
 }
 }

 @SuppressWarnings("unchecked")
 @Test
 public void testDeserializeInvalidFormatKey() throws IOException {
 String key = "Invalid_Format";
 StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_BOOLEAN, Boolean.class);
 try {
 deserializer.deserializeKey(key, context);
 fail("Should have thrown exception");
 } catch (IOException e) {
 assertEquals("not a valid representation", e.getMessage());
 }
 }
}