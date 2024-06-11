import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class StdKeyDeserializerTest {

 private DeserializationContext ctxt;

 @Before
 public void setup() {
 ctxt = new TestContext();
 }

 @Test
 public void testBooleanKeyDeserialization() throws JsonProcessingException {
 String key = "true";
 Object deserializedKey = new StdKeyDeserializer(StdKeyDeserializer.TYPE_BOOLEAN, Boolean.class)
 .deserializeKey(key, ctxt);
 assertTrue((Boolean) deserializedKey);
 }

 @Test
 public void testInvalidBooleanKeyDeserialization() throws JsonProcessingException {
 String key = "invalid";
 Object deserializedKey = new StdKeyDeserializer(StdKeyDeserializer.TYPE_BOOLEAN, Boolean.class)
 .deserializeKey(key, ctxt);
 assertNull(deserializedKey);
 }

 @Test
 public void testByteKeyDeserialization() throws JsonProcessingException {
 String key = "127";
 Object deserializedKey = new StdKeyDeserializer(StdKeyDeserializer.TYPE_BYTE, Byte.class)
 .deserializeKey(key, ctxt);
 assertEquals((Byte) new Byte((byte) 127), deserializedKey);
 }

 @Test
 public void testInvalidByteKeyDeserialization() throws JsonProcessingException {
 String key = "256";
 Object deserializedKey = new StdKeyDeserializer(StdKeyDeserializer.TYPE_BYTE, Byte.class)
 .deserializeKey(key, ctxt);
 assertNull(deserializedKey);
 }

 @Test
 public void testShortKeyDeserialization() throws JsonProcessingException {
 String key = "32767";
 Object deserializedKey = new StdKeyDeserializer(StdKeyDeserializer.TYPE_SHORT, Short.class)
 .deserializeKey(key, ctxt);
 assertEquals((Short) new Short((short) 32767), deserializedKey);
 }

 @Test
 public void testInvalidShortKeyDeserialization() throws JsonProcessingException {
 String key = "32768";
 Object deserializedKey = new StdKeyDeserializer(StdKeyDeserializer.TYPE_SHORT, Short.class)
 .deserializeKey(key, ctxt);
 assertNull(deserializedKey);
 }

 @Test
 public void testCharKeyDeserialization() throws JsonProcessingException {
 String key = "A";
 Object deserializedKey = new StdKeyDeserializer(StdKeyDeserializer.TYPE_CHAR, Character.class)
 .deserializeKey(key, ctxt);
 assertEquals((Character) 'A', deserializedKey);
 }

 @Test
 public void testInvalidCharKeyDeserialization() throws JsonProcessingException {
 String key = "invalid";
 Object deserializedKey = new StdKeyDeserializer(StdKeyDeserializer.TYPE_CHAR, Character.class)
 .deserializeKey(key, ctxt);
 assertNull(deserializedKey);
 }

 @Test
 public void testIntKeyDeserialization() throws JsonProcessingException {
 String key = "123456";
 Object deserializedKey = new StdKeyDeserializer(StdKeyDeserializer.TYPE_INT, Integer.class)
 .deserializeKey(key, ctxt);
 assertEquals((Integer) new Integer(123456), deserializedKey);
 }

 @Test
 public void testLongKeyDeserialization() throws JsonProcessingException {
 String key = "12345678901234567890";
 Object deserializedKey = new StdKeyDeserializer(StdKeyDeserializer.TYPE_LONG, Long.class)
 .deserializeKey(key, ctxt);
 assertEquals((Long) new Long(12345678901234567890L), deserializedKey);
 }

 @Test
 public void testInvalidLongKeyDeserialization() throws JsonProcessingException {
 String key = "invalid";
 Object deserializedKey = new StdKeyDeserializer(StdKeyDeserializer.TYPE_LONG, Long.class)
 .deserializeKey(key, ctxt);
 assertNull(deserializedKey);
 }

 @Test
 public void testFloatKeyDeserialization() throws JsonProcessingException {
 String key = "3.14159265358979323846";
 Object deserializedKey = new StdKeyDeserializer(StdKeyDeserializer.TYPE_FLOAT, Float.class)
 .deserializeKey(key, ctxt);
 assertEquals((Float) new Float(3.14159265358979323846f), deserializedKey);
 }

 @Test
 public void testInvalidFloatKeyDeserialization() throws JsonProcessingException {
 String key = "invalid";
 Object deserializedKey = new StdKeyDeserializer(StdKeyDeserializer.TYPE_FLOAT, Float.class)
 .deserializeKey(key, ctxt);
 assertNull(deserializedKey);
 }

 @Test
 public void testDoubleKeyDeserialization() throws JsonProcessingException {
 String key = "3.14159265358979323846264338327950288419716939937510582097494459230781640628620899862803482534211706798214808651328230664709384460955058223172535940812848111745028410270193852110555";
 Object deserializedKey = new StdKeyDeserializer(StdKeyDeserializer.TYPE_DOUBLE, Double.class)
 .deserializeKey(key, ctxt);
 assertEquals((Double) new Double(3.14159265358979323846264338327950288419716939937510582097494459230781640628620899862803482534211706798214808651328230664709384460955058223172535940812848111745028410270193852110555), deserializedKey);
 }

 @Test
 public void testInvalidDoubleKeyDeserialization() throws JsonProcessingException {
 String key = "invalid";
 Object deserializedKey = new StdKeyDeserializer(StdKeyDeserializer.TYPE_DOUBLE, Double.class)
 .deserializeKey(key, ctxt);
 assertNull(deserializedKey);
 }

 @Test
 public void testLocaleKeyDeserialization() throws JsonProcessingException {
 String key = "en_US";
 Object deserializedKey = new StdKeyDeserializer(StdKeyDeserializer.TYPE_LOCALE, Locale.class)
 .deserializeKey(key, ctxt);
 assertEquals(Locale.US, deserializedKey);
 }

 @Test
 public void testInvalidLocaleKeyDeserialization() throws JsonProcessingException {
 String key = "invalid";
 Object deserializedKey = new StdKeyDeserializer(StdKeyDeserializer.TYPE_LOCALE, Locale.class)
 .deserializeKey(key, ctxt);
 assertNull(deserializedKey);
 }

 @Test
 public void testDateKeyDeserialization() throws JsonProcessingException, ParseException {
 String key = "2022-12-31";
 Object deserializedKey = new StdKeyDeserializer(StdKeyDeserializer.TYPE_DATE, Date.class)
 .deserializeKey(key, ctxt);
 assertEquals(new Date(System.currentTimeMillis()), new Date("Fri Dec 31 00:00:00 CET 2022"));
 }

 @Test
 public void testInvalidDateKeyDeserialization() throws JsonProcessingException {
 String key = "invalid";
 Object deserializedKey = new StdKeyDeserializer(StdKeyDeserializer.TYPE_DATE, Date.class)
 .deserializeKey(key, ctxt);
 assertNull(deserializedKey);
 }

 @Test
 public void testCalendarKeyDeserialization() throws JsonProcessingException, ParseException {
 String key = "2022-12-31";
 Object deserializedKey = new StdKeyDeserializer(StdKeyDeserializer.TYPE_CALENDAR, Calendar.class)
 .deserializeKey(key, ctxt);
 Calendar calendar = Calendar.getInstance();
 calendar.setTime(new Date("Fri Dec 31 00:00:00 CET 2022"));
 assertEquals(calendar, deserializedKey);
 }

 @Test
 public void testInvalidCalendarKeyDeserialization() throws JsonProcessingException {
 String key = "invalid";
 Object deserializedKey = new StdKeyDeserializer(StdKeyDeserializer.TYPE_CALENDAR, Calendar.class)
 .deserializeKey(key, ctxt);
 assertNull(deserializedKey);
 }

 @Test
 public void testUUIDKeyDeserialization() throws JsonProcessingException {
 String key = "123e4567-e89b-12d3-a456-426614174000";
 Object deserializedKey = new StdKeyDeserializer(StdKeyDeserializer.TYPE_UUID, UUID.class)
 .deserializeKey(key, ctxt);
 assertEquals(UUID.fromString(key), deserializedKey);
 }

 @Test
 public void testInvalidUUIDKeyDeserialization() throws JsonProcessingException {
 String key = "invalid";
 Object deserializedKey = new StdKeyDeserializer(StdKeyDeserializer.TYPE_UUID, UUID.class)
 .deserializeKey(key, ctxt);
 assertNull(deserializedKey);
 }

 @Test
 public void testURIGridKeyDeserialization() throws JsonProcessingException, URISyntaxException {
 String key = "https://example.com/path/to/file.txt";
 Object deserializedKey = new StdKeyDeserializer(StdKeyDeserializer.TYPE_URI, URI.class)
 .deserializeKey(key, ctxt);
 assertEquals(new URI(key), deserializedKey);
 }

 @Test
 public void testInvalidURIGridKeyDeserialization() throws JsonProcessingException {
 String key = "invalid";
 Object deserializedKey = new StdKeyDeserializer(StdKeyDeserializer.TYPE_URI, URI.class)
 .deserializeKey(key, ctxt);
 assertNull(deserializedKey);
 }

 @Test
 public void testURLKeyDeserialization() throws JsonProcessingException, MalformedURLException {
 String key = "https://example.com/path/to/file.txt";
 Object deserializedKey = new StdKeyDeserializer(StdKeyDeserializer.TYPE_URL, URL.class)
 .deserializeKey(key, ctxt);
 assertEquals(new URL(key), deserializedKey);
 }

 @Test
 public void testInvalidURLKeyDeserialization() throws JsonProcessingException {
 String key = "invalid";
 Object deserializedKey = new StdKeyDeserializer(StdKeyDeserializer.TYPE_URL, URL.class)
 .deserializeKey(key, ctxt);
 assertNull(deserializedKey);
 }

 @Test
 public void testClassKeyDeserialization() throws JsonProcessingException, ClassNotFoundException {
 String key = "java.util.ArrayList";
 Object deserializedKey = new StdKeyDeserializer(StdKeyDeserializer.TYPE_CLASS, Class.class)
 .deserializeKey(key, ctxt);
 assertEquals(ArrayList.class, deserializedKey);
 }

 @Test
 public void testInvalidClassKeyDeserialization() throws JsonProcessingException {
 String key = "invalid";
 Object deserializedKey = new StdKeyDeserializer(StdKeyDeserializer.TYPE_CLASS, Class.class)
 .deserializeKey(key, ctxt);
 assertNull(deserializedKey);
 }
}