import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.*;
import java.util.Calendar;
import java.util.Locale;

public class StdKeyDeserializerTest {

 @Test
 public void testForTypeMethod() throws Exception {
 Class<?> keyClass = Integer.class;
 StdKeyDeserializer deserializer = StdKeyDeserializer.forType(keyClass);
 assertEquals(TYPE_INT, deserializer.getKind());
 assertEquals(keyClass, deserializer.getKeyClass());
 }

 @Test
 public void testDeserializeKeyMethod_String() throws Exception {
 StdKeyDeserializer deserializer = new StdKeyDeserializer(TYPE_INT, Integer.class);
 Object result = deserializer.deserializeKey("10", new DeserializationContext(new ObjectMapper()));
 assertEquals(10, result);
 }

 @Test(expected = IOException.class)
 public void testDeserializeKeyMethod_InvalidString() throws Exception {
 StdKeyDeserializer deserializer = new StdKeyDeserializer(TYPE_INT, Integer.class);
 deserializer.deserializeKey("abc", new DeserializationContext(new ObjectMapper()));
 }

 @Test
 public void testParseMethod_Boolean() throws Exception {
 StdKeyDeserializer deserializer = new StdKeyDeserializer(TYPE_BOOLEAN, Boolean.class);
 Object result = deserializer._parse("true", new DeserializationContext(new ObjectMapper()));
 assertTrue((Boolean) result);
 }

 @Test
 public void testParseMethod_Boolean_False() throws Exception {
 StdKeyDeseralizer deserializer = new StdKeyDeserializer(TYPE_BOOLEAN, Boolean.class);
 Object result = deserializer._parse("false", new DeserializationContext(new ObjectMapper()));
 assertFalse((Boolean) result);
 }

 @Test(expected = IOException.class)
 public void testParseMethod_Boolean_InvalidString() throws Exception {
 StdKeyDeserializer deserializer = new StdKeyDeserializer(TYPE_BOOLEAN, Boolean.class);
 deserializer._parse("invalid", new DeserializationContext(new ObjectMapper()));
 }

 @Test
 public void testparseIntMethod() throws Exception {
 StdKeyDeserializer deserializer = new StdKeyDeserializer();
 int result = deserializer._parseInt("10");
 assertEquals(10, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testparseIntMethod_NegativeNumber() throws Exception {
 StdKeyDeserializer deserializer = new StdKeyDeserializer();
 deserializer._parseInt("-10");
 }

 @Test
 public void testparseLongMethod() throws Exception {
 StdKeyDeserializer deserializer = new StdKeyDeserializer();
 long result = deserializer._parseLong("100");
 assertEquals(100L, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testparseLongMethod_NegativeNumber() throws Exception {
 StdKeyDeserializer deserializer = new StdKeyDeserializer();
 deserializer._parseLong("-100");
 }

 @Test
 public void testparseDoubleMethod() throws Exception {
 StdKeyDeserializer deserializer = new StdKeyDeserializer();
 double result = deserializer._parseDouble("3.14");
 assertEquals(3.14, result, 0.001);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testparseDoubleMethod_InvalidString() throws Exception {
 StdKeyDeserializer deserializer = new StdKeyDeserializer();
 deserializer._parseDouble("invalid");
 }

 @Test
 public void testDeserializeKeyMethod_Locale() throws Exception {
 StdKeyDeserializer deserializer = new StdKeyDeserializer(TYPE_LOCALE, Locale.class);
 Object result = deserializer.deserializeKey("en_US", new DeserializationContext(new ObjectMapper()));
 assertEquals(Locale.US, result);
 }

 @Test(expected = IOException.class)
 public void testDeserializeKeyMethod_InvalidLocaleString() throws Exception {
 StdKeyDeserializer deserializer = new StdKeyDeserializer(TYPE_LOCALE, Locale.class);
 deserializer.deserializeKey("invalid", new DeserializationContext(new ObjectMapper()));
 }

 @Test
 public void testDeserializeKeyMethod_UUID() throws Exception {
 StdKeyDeserializer deserializer = new StdKeyDeserializer(TYPE_UUID, UUID.class);
 Object result = deserializer.deserializeKey("b8d256b9-76c3-4774-a717-abd42a133e11", new DeserializationContext(new ObjectMapper()));
 assertEquals("b8d256b9-76c3-4774-a717-abd42a133e11", result.toString());
 }

 @Test(expected = IOException.class)
 public void testDeserializeKeyMethod_InvalidUUIDString() throws Exception {
 StdKeyDeserializer deserializer = new StdKeyDeserializer(TYPE_UUID, UUID.class);
 deserializer.deserializeKey("invalid", new DeserializationContext(new ObjectMapper()));
 }

 @Test
 public void testDeserializeKeyMethod_URL() throws Exception {
 StdKeyDeserializer deserializer = new StdKeyDeserializer(TYPE_URL, URL.class);
 Object result = deserializer.deserializeKey("https://www.google.com", new DeserializationContext(new ObjectMapper()));
 assertEquals("https://www.google.com", result.toString());
 }

 @Test(expected = IOException.class)
 public void testDeserializeKeyMethod_InvalidURLString() throws Exception {
 StdKeyDeserializer deserializer = new StdKeyDeserializer(TYPE_URL, URL.class);
 deserializer.deserializeKey("invalid", new DeserializationContext(new ObjectMapper()));
 }

 @Test
 public void testDeserializeKeyMethod_Enum() throws Exception {
 StdKeyDeserializer deserializer = new EnumKD(Locale.class.getEnumConstants()[0].getClass().getEnumConstants(),
 Locale.class.getMethod("valueOf", String.class));
 Object result = deserializer.deserializeKey("ENGLISH", new DeserializationContext(new ObjectMapper()));
 assertEquals(Locale.ENGLISH, result);
 }

 @Test(expected = IOException.class)
 public void testDeserializeKeyMethod_InvalidEnumString() throws Exception {
 StdKeyDeserializer deserializer = new EnumKD(Locale.class.getEnumConstants()[0].getClass().getEnumConstants(),
 Locale.class.getMethod("valueOf", String.class));
 deserializer.deserializeKey("invalid", new DeserializationContext(new ObjectMapper()));
 }
}