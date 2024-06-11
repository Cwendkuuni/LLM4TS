import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.StdKeyDeserializer;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class StdKeyDeserializerTest {

 private DeserializationContext ctxt;

 @Before
 public void setup() {
 ctxt = new TestContext();
 }

 @Test
 public void testBooleanDeserialization() throws Exception {
 StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Boolean.class);
 Object result = deserializer.deserializeKey("true", ctxt);
 assertEquals(Boolean.TRUE, result);

 result = deserializer.deserializeKey("false", ctxt);
 assertEquals(Boolean.FALSE, result);
 }

 @Test
 public void testByteDeserialization() throws Exception {
 StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Byte.class);
 Object result = deserializer.deserializeKey("10", ctxt);
 assertEquals((Byte) 10, result);

 result = deserializer.deserializeKey("-5", ctxt);
 assertEquals((Byte) -5, result);
 }

 @Test
 public void testShortDeserialization() throws Exception {
 StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Short.class);
 Object result = deserializer.deserializeKey("123", ctxt);
 assertEquals((Short) 123, result);

 result = deserializer.deserializeKey("-256", ctxt);
 assertEquals((Short) -256, result);
 }

 @Test
 public void testCharDeserialization() throws Exception {
 StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Character.class);
 Object result = deserializer.deserializeKey("a", ctxt);
 assertEquals(Character.valueOf('a'), result);
 }

 @Test
 public void testIntDeserialization() throws Exception {
 StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Integer.class);
 Object result = deserializer.deserializeKey("100", ctxt);
 assertEquals(Integer.valueOf(100), result);
 }

 @Test
 public void testLongDeserialization() throws Exception {
 StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Long.class);
 Object result = deserializer.deserializeKey("123456789012345L", ctxt);
 assertEquals(Long.valueOf(123456789012345L), result);
 }

 @Test
 public void testFloatDeserialization() throws Exception {
 StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Float.class);
 Object result = deserializer.deserializeKey("3.14159265", ctxt);
 assertEquals(Float.valueOf((float) 3.14159265), result);
 }

 @Test
 public void testDoubleDeserialization() throws Exception {
 StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Double.class);
 Object result = deserializer.deserializeKey("3.14159265358979323846", ctxt);
 assertEquals(Double.valueOf(3.14159265358979323846), result);
 }

 @Test
 public void testLocaleDeserialization() throws Exception {
 StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Locale.class);
 Object result = deserializer.deserializeKey("en_US", ctxt);
 assertEquals(Locale.forLanguageTag("en_US"), result);
 }

 @Test
 public void testCurrencyDeserialization() throws Exception {
 StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Currency.class);
 Object result = deserializer.deserializeKey("USD", ctxt);
 assertEquals(Currency.getInstance("USD"), result);
 }

 @Test
 public void testDateDeserialization() throws Exception {
 StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Date.class);
 Object result = deserializer.deserializeKey("2022-03-01", ctxt);
 assertEquals(new Date(2022, Calendar.MARCH, 1), result);
 }

 @Test
 public void testCalendarDeserialization() throws Exception {
 StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Calendar.class);
 Object result = deserializer.deserializeKey("2022-03-01", ctxt);
 assertEquals(Calendar.getInstance(), result);
 }

 @Test
 public void testUUIDDeserialization() throws Exception {
 StdKeyDeserializer deserializer = StdKeyDeserializer.forType(UUID.class);
 Object result = deserializer.deserializeKey("6ba7b810-9dad-11d1-80b4-00c04fd430c8", ctxt);
 assertEquals(UUID.fromString("6ba7b810-9dad-11d1-80b4-00c04fd430c8"), result);
 }

 @Test
 public void testURIDeserialization() throws Exception {
 StdKeyDeserializer deserializer = StdKeyDeserializer.forType(URI.class);
 Object result = deserializer.deserializeKey("https://www.example.com", ctxt);
 assertEquals(new URI("https://www.example.com"), result);
 }

 @Test
 public void testURLDeserialization() throws Exception {
 StdKeyDeserializer deserializer = StdKeyDeserializer.forType(URL.class);
 Object result = deserializer.deserializeKey("https://www.example.com", ctxt);
 assertEquals(new URL("https://www.example.com"), result);
 }

 @Test
 public void testClassDeserialization() throws Exception {
 StdKeyDeserializer deserializer = StdKeyDeserializer.forType(String.class); // using StringKD
 Object result = deserializer.deserializeKey("java.lang.String", ctxt);
 assertEquals(String.class, result);
 }

 @Test
 public void testUnknownEnumDeserialization() throws Exception {
 StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Enum.class);
 Enum e = (Enum) deserializer.deserializeKey("UnknownEnumValue", ctxt);
 assertNull(e);
 }

 @Test
 public void testMalformedKey() throws Exception {
 StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Integer.class);
 try {
 deserializer.deserializeKey("InvalidKey", ctxt);
 fail();
 } catch (IOException ex) {
 // expected
 }
 }

 @Test
 public void testNegativeNumberDeserialization() throws Exception {
 StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Byte.class);
 try {
 deserializer.deserializeKey("-10", ctxt);
 fail();
 } catch (IOException ex) {
 // expected
 }
 }

 @Test
 public void testLongMaxNumberDeserialization() throws Exception {
 StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Long.class);
 try {
 deserializer.deserializeKey("9223372036854775808", ctxt);
 fail();
 } catch (IOException ex) {
 // expected
 }
 }

 @Test
 public void testFloatNaNDeserialization() throws Exception {
 StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Float.class);
 try {
 deserializer.deserializeKey("NaN", ctxt);
 fail();
 } catch (IOException ex) {
 // expected
 }
 }

 @Test
 public void testDoubleNaNDeserialization() throws Exception {
 StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Double.class);
 try {
 deserializer.deserializeKey("NaN", ctxt);
 fail();
 } catch (IOException ex) {
 // expected
 }
 }

 @Test
 public void testNullKeyDeserialization() throws Exception {
 StdKeyDeserializer deserializer = StdKeyDeserializer.forType(String.class);
 Object result = deserializer.deserializeKey(null, ctxt);
 assertNull(result);
 }
}