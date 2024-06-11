import com.fasterxml.jackson.databind.*;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class StdKeyDeserializerTest {

 private DeserializationContext context;
 private ObjectMapper mapper;

 @Before
 public void setup() {
 mapper = new ObjectMapper();
 context = mapper.getDeserializationContext();
 }

 @Test
 public void testBooleanType() throws Exception {
 Object key = mapper.readValue("\"true\"", StdKeyDeserializer.class);
 assertEquals(Boolean.TRUE, key);

 key = mapper.readValue("\"false\"", StdKeyDeserializer.class);
 assertEquals(Boolean.FALSE, key);
 }

 @Test
 public void testByteType() throws Exception {
 Object key = mapper.readValue("\"127\"", StdKeyDeserializer.class);
 assertEquals(Byte.valueOf((byte) 127), key);

 key = mapper.readValue("\"-128\"", StdKeyDeserializer.class);
 assertEquals(Byte.valueOf((byte) -128), key);
 }

 @Test
 public void testShortType() throws Exception {
 Object key = mapper.readValue("\"32767\"", StdKeyDeserializer.class);
 assertEquals(Short.valueOf((short) 32767), key);

 key = mapper.readValue("\"-32768\"", StdKeyDeserializer.class);
 assertEquals(Short.valueOf((short) -32768), key);
 }

 @Test
 public void testCharType() throws Exception {
 Object key = mapper.readValue("\"A\"", StdKeyDeserializer.class);
 assertEquals(Character.valueOf('A'), key);
 }

 @Test(expected = IOException.class)
 public void testInvalidCharType() throws Exception {
 mapper.readValue("\"ABC\"", StdKeyDeserializer.class);
 }

 @Test
 public void testIntType() throws Exception {
 Object key = mapper.readValue("\"12345\"", StdKeyDeserializer.class);
 assertEquals(Integer.valueOf(12345), key);
 }

 @Test
 public void testLongType() throws Exception {
 Object key = mapper.readValue("\"12345678901234567890\"", StdKeyDeserializer.class);
 assertEquals(Long.valueOf(12345678901234567890L), key);
 }

 @Test
 public void testFloatType() throws Exception {
 Object key = mapper.readValue("\"1.23456E-7\"", StdKeyDeserializer.class);
 assertEquals(Float.valueOf(1.23456E-7f), key);
 }

 @Test
 public void testDoubleType() throws Exception {
 Object key = mapper.readValue("\"1.23456789E+10\"", StdKeyDeserializer.class);
 assertEquals(Double.valueOf(1.23456789E10d), key);
 }

 @Test
 public void testLocaleType() throws Exception {
 Object key = mapper.readValue("\"en_US\"", StdKeyDeserializer.class);
 assertEquals(Locale.US, key);
 }

 @Test(expected = IOException.class)
 public void testInvalidLocaleType() throws Exception {
 mapper.readValue("\"en_XX\"", StdKeyDeserializer.class);
 }

 @Test
 public void testDateType() throws Exception {
 Object key = mapper.readValue("\"2022-01-01\"", StdKeyDeserializer.class);
 assertEquals(java.util.Date.valueOf("2022-01-01"), key);
 }

 @Test
 public void testCalendarType() throws Exception {
 Object key = mapper.readValue("\"2022-01-01\"", StdKeyDeserializer.class);
 assertEquals(java.util.GregorianCalendar.getInstance(2022, 0, 1), key);
 }

 @Test
 public void testUriType() throws Exception {
 Object key = mapper.readValue("\"http://example.com\"", StdKeyDeserializer.class);
 assertEquals(new URI("http://example.com"), key);
 }

 @Test(expected = IOException.class)
 public void testInvalidUriType() throws Exception {
 mapper.readValue("\"http://example.com:123\"", StdKeyDeserializer.class);
 }

 @Test
 public void testUrlType() throws Exception {
 Object key = mapper.readValue("\"http://example.com\"", StdKeyDeserializer.class);
 assertEquals(new URL("http://example.com"), key);
 }

 @Test(expected = MalformedURLException.class)
 public void testInvalidUrlType() throws Exception {
 mapper.readValue("\"http://example.com:123\"", StdKeyDeserializer.class);
 }

 @Test
 public void testClassType() throws Exception {
 Object key = mapper.readValue("\"java.util.HashMap\"", StdKeyDeserializer.class);
 assertEquals(HashMap.class, key);
 }

 @Test
 public void testEnumType() throws Exception {
 Object key = mapper.readValue("\"MONDAY\"", StdKeyDeserializer.class);
 assertEquals(java.time.DayOfWeek.MONDAY, key);
 }

 @Test(expected = IOException.class)
 public void testInvalidEnumType() throws Exception {
 mapper.readValue("\"INVALID\"", StdKeyDeserializer.class);
 }

 @Test
 public void testByteArrayType() throws Exception {
 Object key = mapper.readValue("\"Rm9udCAoKQ==\"", StdKeyDeserializer.class);
 assertEquals(new byte[] { 0, 1, 2, 3 }, key);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidByteArrayType() throws Exception {
 mapper.readValue("\"Rm9udCAoKQ==X\"", StdKeyDeserializer.class);
 }

 @Test
 public void testNullType() throws Exception {
 Object key = mapper.readValue("\"null\"", StdKeyDeserializer.class);
 assertNull(key);
 }

 @Test
 public void testUnknownType() throws Exception {
 Object key = mapper.readValue("\"unknown\"", StdKeyDeserializer.class);
 assertNull(key);
 }
}