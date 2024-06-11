import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.StdKeyDeserializer;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class StdKeyDeserializerTest {

 private DeserializationContext ctxt;

 @Before
 public void setUp() {
 ctxt = new TestDeserializationContext();
 }

 @Test
 public void testBooleanType() {
 // Basic functionality
 StdKeyDeserializer serializer = StdKeyDeserializer.forType(Boolean.class);
 Object key = serializer.deserializeKey("true", ctxt);
 assertEquals(Boolean.TRUE, key);

 // Edge cases
 key = serializer.deserializeKey("FALSE", ctxt);
 assertEquals(Boolean.FALSE, key);
 key = serializer.deserializeKey("tRuE", ctxt);
 assertEquals(Boolean.TRUE, key);
 key = serializer.deserializeKey("FAlse", ctxt);
 assertEquals(Boolean.FALSE, key);

 // Exception handling
 try {
 key = serializer.deserializeKey("123", ctxt);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test
 public void testByteType() {
 // Basic functionality
 StdKeyDeserializer serializer = StdKeyDeserializer.forType(Byte.class);
 Object key = serializer.deserializeKey("127", ctxt);
 assertEquals((byte) 127, key);

 // Edge cases
 try {
 key = serializer.deserializeKey("-128", ctxt);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // Expected
 }

 try {
 key = serializer.deserializeKey("256", ctxt);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // Expected
 }

 // Exception handling
 try {
 key = serializer.deserializeKey("xyz", ctxt);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 // ... similar test cases for other types (Short, Char, Int, Long, Float, Double, Locale, Date, Calendar, UUID, URI, URL, Class)

 @Test
 public void testUnknownType() {
 // edge case
 StdKeyDeserializer serializer = StdKeyDeserializer.forType(String.class);
 Object key = serializer.deserializeKey("XYZ", ctxt);
 assertNull(key);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidKey() {
 // exception handling
 StdKeyDeserializer serializer = StdKeyDeserializer.forType(Byte.class);
 serializer.deserializeKey(null, ctxt);
 }
}