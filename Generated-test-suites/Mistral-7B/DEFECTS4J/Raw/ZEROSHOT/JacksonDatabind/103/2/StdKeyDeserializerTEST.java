import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class StdKeyDeserializerTest {

 private ObjectMapper objectMapper;

 @Before
 public void setup() {
 objectMapper = new ObjectMapper();
 }

 @Test
 public void testBooleanDeserializer() throws JsonProcessingException {
 StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Boolean.class);
 assertEquals(Boolean.TRUE, deserializer.deserializeKey("true", objectMapper.getDeserializationContext()));
 assertEquals(Boolean.FALSE, deserializer.deserializeKey("false", objectMapper.getDeserializationContext()));
 }

 @Test
 public void testByteDeserializer() throws JsonProcessingException {
 StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Byte.class);
 assertEquals((byte) 1, deserializer.deserializeKey("1", objectMapper.getDeserializationContext()));
 assertEquals((byte) -128, deserializer.deserializeKey("-128", objectMapper.getDeserializationContext()));
 }

 @Test
 public void testShortDeserializer() throws JsonProcessingException {
 StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Short.class);
 assertEquals((short) 1, deserializer.deserializeKey("1", objectMapper.getDeserializationContext()));
 assertEquals((short) -32768, deserializer.deserializeKey("-32768", objectMapper.getDeserializationContext()));
 }

 @Test
 public void testCharDeserializer() throws JsonProcessingException {
 StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Character.class);
 assertEquals('a', deserializer.deserializeKey("a", objectMapper.getDeserializationContext()));
 }

 @Test
 public void testIntDeserializer() throws JsonProcessingException {
 StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Integer.class);
 assertEquals(1, deserializer.deserializeKey("1", objectMapper.getDeserializationContext()));
 }

 @Test
 public void testLongDeserializer() throws JsonProcessingException {
 StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Long.class);
 assertEquals(1L, deserializer.deserializeKey("1", objectMapper.getDeserializationContext()));
 assertEquals(-9223372036854775808L, deserializer.deserializeKey("-9223372036854775808", objectMapper.getDeserializationContext()));
 }

 @Test
 public void testFloatDeserializer() throws JsonProcessingException {
 StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Float.class);
 assertEquals(1.0f, deserializer.deserializeKey("1.0", objectMapper.getDeserializationContext()), 0.01f);
 }

 @Test
 public void testDoubleDeserializer() throws JsonProcessingException {
 StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Double.class);
 assertEquals(1.0, deserializer.deserializeKey("1.0", objectMapper.getDeserializationContext()), 0.001);
 }

 @Test
 public void testUriDeserializer() throws JsonProcessingException {
 StdKeyDeserializer deserializer = StdKeyDeserializer.forType(URI.class);
 assertEquals(new URI("http://example.com"), deserializer.deserializeKey("http://example.com", objectMapper.getDeserializationContext()));
 }

 @Test
 public void testUrlDeserializer() throws JsonProcessingException {
 StdKeyDeserializer deserializer = StdKeyDeserializer.forType(URL.class);
 assertEquals(new URL("http://example.com"), deserializer.deserializeKey("http://example.com", objectMapper.getDeserializationContext()));
 }

 @Test
 public void testClassDeserializer() throws JsonProcessingException {
 StdKeyDeserializer deserializer = StdKeyDeserializer.forType(String[].class);
 assertEquals(new String[]{"test"}, deserializer.deserializeKey("[\"test\"]", objectMapper.getDeserializationContext()));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testUnknownTypeDeserializer() throws JsonProcessingException {
 StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Object.class);
 deserializer.deserializeKey("test", objectMapper.getDeserializationContext());
 }

 @Test
 public void testNullDeserializer() throws JsonProcessingException {
 StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Object.class);
 assertNull(deserializer.deserializeKey("null", objectMapper.getDeserializationContext()));
 }

 @Test(expected = IOException.class)
 public void testInvalidKeyDeserializer() throws JsonProcessingException {
 StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Object.class);
 deserializer.deserializeKey("invalid", objectMapper.getDeserializationContext());
 }
}