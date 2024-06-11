import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.ClassUtil;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class StdKeyDeserializerTest {

 private ObjectMapper objectMapper;

 @Before
 public void setUp() throws Exception {
 objectMapper = new ObjectMapper();
 objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
 }

 @Test
 public void testBooleanDeserializer() throws Exception {
 StdKeyDeserializer booleanDeserializer = StdKeyDeserializer.forType(Boolean.class);
 DeserializationContext context = null;
 Object result = booleanDeserializer.deserializeKey("true", context);
 assertEquals(Boolean.TRUE, result);
 result = booleanDeserializer.deserializeKey("false", context);
 assertEquals(Boolean.FALSE, result);
 }

 @Test
 public void testByteDeserializer() throws Exception {
 StdKeyDeserializer byteDeserializer = StdKeyDeserializer.forType(Byte.class);
 DeserializationContext context = null;
 Object result = byteDeserializer.deserializeKey("127", context);
 assertEquals((byte) 127, result);
 result = byteDeserializer.deserializeKey("128", context);
 assertNull(result);
 }

 @Test
 public void testShortDeserializer() throws Exception {
 StdKeyDeserializer shortDeserializer = StdKeyDeserializer.forType(Short.class);
 DeserializationContext context = null;
 Object result = shortDeserializer.deserializeKey("32767", context);
 assertEquals((short) 32767, result);
 result = shortDeserializer.deserializeKey("32768", context);
 assertNull(result);
 }

 @Test
 public void testCharDeserializer() throws Exception {
 StdKeyDeserializer charDeserializer = StdKeyDeserializer.forType(Character.class);
 DeserializationContext context = null;
 Object result = charDeserializer.deserializeKey("A", context);
 assertEquals('A', result);
 result = charDeserializer.deserializeKey("B", context);
 assertNull(result);
 }

 @Test
 public void testIntDeserializer() throws Exception {
 StdKeyDeserializer intDeserializer = StdKeyDeserializer.forType(Integer.class);
 DeserializationContext context = null;
 Object result = intDeserializer.deserializeKey("2147483647", context);
 assertEquals(Integer.MAX_VALUE, result);
 result = intDeserializer.deserializeKey("2147483648", context);
 assertNull(result);
 }

 @Test
 public void testLongDeserializer() throws Exception {
 StdKeyDeserializer longDeserializer = StdKeyDeserializer.forType(Long.class);
 DeserializationContext context = null;
 Object result = longDeserializer.deserializeKey("9223372036854775807", context);
 assertEquals(Long.MAX_VALUE, result);
 result = longDeserializer.deserializeKey("9223372036854775808", context);
 assertNull(result);
 }

 @Test
 public void testFloatDeserializer() throws Exception {
 StdKeyDeserializer floatDeserializer = StdKeyDeserializer.forType(Float.class);
 DeserializationContext context = null;
 Object result = floatDeserializer.deserializeKey("3.14159265359", context);
 assertEquals(3.14159265359F, result, 0.00001F);
 result = floatDeserializer.deserializeKey("invalid", context);
 assertNull(result);
 }

 @Test
 public void testDoubleDeserializer() throws Exception {
 StdKeyDeserializer doubleDeserializer = StdKeyDeserializer.forType(Double.class);
 DeserializationContext context = null;
 Object result = doubleDeserializer.deserializeKey("3.14159265359", context);
 assertEquals(3.14159265359, result, 0.00001);
 result = doubleDeserializer.deserializeKey("invalid", context);
 assertNull(result);
 }

 @Test
 public void testLocaleDeserializer() throws Exception {
 StdKeyDeserializer localeDeserializer = StdKeyDeserializer.forType(Locale.class);
 DeserializationContext context = null;
 Object result = localeDeserializer.deserializeKey("en_US", context);
 assertEquals(Locale.US, result);
 result = localeDeserializer.deserializeKey("invalid", context);
 assertNull(result);
 }

 @Test
 public void testDateDeserializer() throws Exception {
 StdKeyDeserializer dateDeserializer = StdKeyDeserializer.forType(Date.class);
 DeserializationContext context = null;
 Object result = dateDeserializer.deserializeKey("2022-03-01", context);
 assertNotNull(result);
 assertEquals("Wed Mar 01 00:00:00 CET 2022", result.toString());
 result = dateDeserializer.deserializeKey("invalid", context);
 assertNull(result);
 }

 @Test
 public void testCalendarDeserializer() throws Exception {
 StdKeyDeserializer calendarDeserializer = StdKeyDeserializer.forType(Calendar.class);
 DeserializationContext context = null;
 Object result = calendarDeserializer.deserializeKey("2022-03-01", context);
 assertNotNull(result);
 assertEquals("Wed Mar 01 00:00:00 CET 2022", result.toString());
 result = calendarDeserializer.deserializeKey("invalid", context);
 assertNull(result);
 }

 @Test
 public void testUUIDDeserializer() throws Exception {
 StdKeyDeserializer uuidDeserializer = StdKeyDeserializer.forType(UUID.class);
 DeserializationContext context = null;
 Object result = uuidDeserializer.deserializeKey("2a7d4d34-d3f0-4778-8e6b-c23e4b9e4cba", context);
 assertNotNull(result);
 assertEquals("2a7d4d34-d3f0-4778-8e6b-c23e4b9e4cba", result.toString());
 result = uuidDeserializer.deserializeKey("invalid", context);
 assertNull(result);
 }

 @Test
 public void testURIDeserializer() throws Exception {
 StdKeyDeserializer uriDeserializer = StdKeyDeserializer.forType(URI.class);
 DeserializationContext context = null;
 Object result = uriDeserializer.deserializeKey("http://example.com", context);
 assertNotNull(result);
 assertEquals("http://example.com", result.toString());
 result = uriDeserializer.deserializeKey("invalid", context);
 assertNull(result);
 }

 @Test
 public void testURLDeserializer() throws Exception {
 StdKeyDeserializer urlDeserializer = StdKeyDeserializer.forType(URL.class);
 DeserializationContext context = null;
 Object result = urlDeserializer.deserializeKey("http://example.com", context);
 assertNotNull(result);
 assertEquals("http://example.com", result.toString());
 result = urlDeserializer.deserializeKey("invalid", context);
 assertNull(result);
 }

 @Test
 public void testClassDeserializer() throws Exception {
 StdKeyDeserializer classDeserializer = StdKeyDeserializer.forType(String.class);
 TypeDeserializer typeDeserializer = objectMapper.getTypeFactory().constructValueTypeDeserializer(String.class, Class.class);
 TypeFactory typeFactory = objectMapper.getTypeFactory();
 Object result = classDeserializer.deserializeKey("java.lang.String", context);
 assertEquals(String.class, result.getClass());
 result = classDeserializer.deserializeKey("java.util.Date", context);
 assertEquals(Date.class, result.getClass());
 result = classDeserializer.deserializeKey("java.util.ArrayList", context);
 assertEquals(ArrayList.class, result.getClass());
 result = classDeserializer.deserializeKey("java.lang.Object", context);
 assertEquals(Object.class, result.getClass());
 result = classDeserializer.deserializeKey("invalid", context);
 assertNull(result);
 result = typeDeserializer.deserializeKey("java.lang.String", context);
 assertEquals(String.class, result.getClass());
 }
}