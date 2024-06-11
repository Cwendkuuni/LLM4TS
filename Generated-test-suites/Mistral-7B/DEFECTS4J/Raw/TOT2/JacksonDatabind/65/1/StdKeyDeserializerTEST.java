import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.StdKeyDeserializer;
import org.junit.Test;
import static org.junit.Assert.*;

public class StdKeyDeserializerTest {

 // Test for standard methods with basic data types as keys
 @Test
 public void testStandardMethods() throws Exception {
 StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Boolean.class);
 Object key = keyDeserializer.deserializeKey("true", new TestContext());
 assertEquals(Boolean.TRUE, key);

 keyDeserializer = StdKeyDeserializer.forType(Byte.class);
 key = keyDeserializer.deserializeKey("127", new TestContext());
 assertEquals((byte) 127, key);

 keyDeserializer = StdKeyDeserializer.forType(Short.class);
 key = keyDeserializer.deserializeKey("32767", new TestContext());
 assertEquals((short) 32767, key);

 keyDeserializer = StdKeyDeserializer.forType(Integer.class);
 key = keyDeserializer.deserializeKey("12345", new TestContext());
 assertEquals(12345, key);

 keyDeserializer = StdKeyDeserializer.forType(Long.class);
 key = keyDeserializer.deserializeKey("9223372036854775807", new TestContext()); // Long.MAX_VALUE
 assertEquals(9223372036854775807L, key);

 keyDeserializer = StdKeyDeserializer.forType(Float.class);
 key = keyDeserializer.deserializeKey("3.14159265358979323846", new TestContext());
 assertEquals(3.14159265358979323846f, key, 0.0001f);

 keyDeserializer = StdKeyDeserializer.forType(Double.class);
 key = keyDeserializer.deserializeKey("6.28318530718", new TestContext()); // Math.PI
 assertEquals(6.28318530718, key, 0.00001);
 }

 // Test for edge cases and exception handling
 @Test(expected = JsonProcessingException.class)
 public void testInvalidBooleanKey() throws Exception {
 StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Boolean.class);
 keyDeserializer.deserializeKey("x", new TestContext());
 }

 @Test(expected = JsonProcessingException.class)
 public void testNegativeByteKey() throws Exception {
 StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Byte.class);
 keyDeserializer.deserializeKey("-1", new TestContext());
 }

 @Test(expected = JsonProcessingException.class)
 public void testNegativeShortKey() throws Exception {
 StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Short.class);
 keyDeserializer.deserializeKey("-32768", new TestContext()); // Short.MIN_VALUE
 }

 @Test(expected = JsonProcessingException.class)
 public void testNegativeIntKey() throws Exception {
 StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Integer.class);
 keyDeserializer.deserializeKey("-12345", new TestContext());
 }

 @Test(expected = JsonProcessingException.class)
 public void testNegativeLongKey() throws Exception {
 StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Long.class);
 keyDeserializer.deserializeKey("-9223372036854775808", new TestContext()); // Long.MIN_VALUE
 }

 @Test(expected = JsonProcessingException.class)
 public void testFloatOutOfRangeKey() throws Exception {
 StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Float.class);
 keyDeserializer.deserializeKey("3.4E+38", new TestContext()); // Float.POSITIVE_INFINITY
 }

 @Test(expected = JsonProcessingException.class)
 public void testDoubleOutOfRangeKey() throws Exception {
 StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Double.class);
 keyDeserializer.deserializeKey("1.8E+308", new TestContext()); // Double.POSITIVE_INFINITY
 }

 @Test(expected = JsonProcessingException.class)
 public void testInvalidLocaleKey() throws Exception {
 StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Locale.class);
 keyDeserializer.deserializeKey("en_US_Invalid", new TestContext());
 }

 @Test(expected = JsonProcessingException.class)
 public void testInvalidURLKey() throws Exception {
 StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(URL.class);
 keyDeserializer.deserializeKey("invalid://localhost:8080/test", new TestContext());
 }

 @Test(expected = MalformedURLException.class)
 public void testMalformedURLKey() throws Exception {
 StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(URL.class);
 keyDeserializer.deserializeKey("http://test:8080/test#fragment", new TestContext());
 }

 @Test(expected = IOException.class)
 public void testInvalidUUIDKey() throws Exception {
 StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(UUID.class);
 keyDeserializer.deserializeKey("{Invalid-uuid}", new TestContext());
 }
}