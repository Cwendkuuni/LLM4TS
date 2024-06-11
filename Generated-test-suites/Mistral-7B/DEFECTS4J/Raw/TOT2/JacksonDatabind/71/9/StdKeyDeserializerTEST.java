import com.fasterxml.jackson.core.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

public class StdKeyDeserializerTest {

 private static ObjectMapper mapper;

 static {
 mapper = new ObjectMapper();
 mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
 }

 @Test
 public void testBoolean() throws JsonMappingException, IOException {
 StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Boolean.class);
 DeserializationContext context = mapper.getDeserializationContext();
 assertEquals(Boolean.TRUE, deserializer.deserializeKey("true", context));
 assertEquals(Boolean.FALSE, deserializer.deserializeKey("false", context));
 assertThrows(IOException.class, () -> deserializer.deserializeKey("invalid", context));
 }

 @Test
 public void testByte() throws JsonMappingException, IOException {
 StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Byte.class);
 DeserializationContext context = mapper.getDeserializationContext();
 assertEquals((byte) 1, deserializer.deserializeKey("1", context));
 assertThrows(IOException.class, () -> deserializer.deserializeKey("-1", context));
 }

 @Test
 public void testShort() throws JsonMappingException, IOException {
 StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Short.class);
 DeserializationContext context = mapper.getDeserializationContext();
 assertEquals((short) 1, deserializer.deserializeKey("1", context));
 assertThrows(IOException.class, () -> deserializer.deserializeKey("-1", context));
 }

 @Test
 public void testChar() throws JsonMappingException, IOException {
 StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Character.class);
 DeserializationContext context = mapper.getDeserializationContext();
 assertEquals('\u0001', deserializer.deserializeKey("\u0001", context));
 assertThrows(IOException.class, () -> deserializer.deserializeKey("Invalid", context));
 }

 @Test
 public void testInt() throws JsonMappingException, IOException {
 StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Integer.class);
 DeserializationContext context = mapper.getDeserializationContext();
 assertEquals(1, deserializer.deserializeKey("1", context));
 assertThrows(IOException.class, () -> deserializer.deserializeKey("-1", context));
 }

 @Test
 public void testLong() throws JsonMappingException, IOException {
 StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Long.class);
 DeserializationContext context = mapper.getDeserializationContext();
 assertEquals(1L, deserializer.deserializeKey("1", context));
 assertThrows(IOException.class, () -> deserializer.deserializeKey("-1", context));
 }

 @Test
 public void testFloat() throws JsonMappingException, IOException {
 StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Float.class);
 DeserializationContext context = mapper.getDeserializationContext();
 assertEquals((float) 1.0, deserializer.deserializeKey("1.0", context), 0.001F);
 assertThrows(IOException.class, () -> deserializer.deserializeKey("-1", context));
 }

 @Test
 public void testDouble() throws JsonMappingException, IOException {
 StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Double.class);
 DeserializationContext context = mapper.getDeserializationContext();
 assertEquals(1.0, deserializer.deserializeKey("1.0", context), 0.001);
 assertThrows(IOException.class, () -> deserializer.deserializeKey("-1", context));
 }

 @Test
 public void testLocale() throws JsonMappingException, IOException {
 StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Locale.class);
 DeserializationContext context = mapper.getDeserializationContext();
 assertEquals(Locale.ENGLISH, deserializer.deserializeKey("en_US", context));
 assertThrows(IOException.class, () -> deserializer.deserializeKey("invalid", context));
 }

 @Test
 public void testDate() throws JsonMappingException, IOException {
 StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Date.class);
 DeserializationContext context = mapper.getDeserializationContext();
 assertEquals(new Date(1609459200000L), deserializer.deserializeKey("2021-01-01", context));
 assertThrows(IOException.class, () -> deserializer.deserializeKey("invalid", context));
 }

 @Test
 public void testCalendar() throws JsonMappingException, IOException {
 StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Calendar.class);
 DeserializationContext context = mapper.getDeserializationContext();
 Calendar calendar = Calendar.getInstance();
 calendar.setTime(new Date(1609459200000L));
 assertEquals(calendar, deserializer.deserializeKey("2021-01-01", context));
 assertThrows(IOException.class, () -> deserializer.deserializeKey("invalid", context));
 }

 @Test
 public void testUUID() throws JsonMappingException, IOException {
 StdKeyDeserializer deserializer = StdKeyDeserializer.forType(UUID.class);
 DeserializationContext context = mapper.getDeserializationContext();
 assertEquals(UUID.fromString("1b9d6bcd-bbfd-4b2d-9b5d-ab8dfbbd4bed"), deserializer.deserializeKey("1b9d6bcd-bbfd-4b2d-9b5d-ab8dfbbd4bed", context));
 assertThrows(IOException.class, () -> deserializer.deserializeKey("invalid", context));
 }

 @Test
 public void testURI() throws JsonMappingException, IOException {
 StdKeyDeserializer deserializer = StdKeyDeserializer.forType(URI.class);
 DeserializationContext context = mapper.getDeserializationContext();
 assertEquals(new URI("http://example.com"), deserializer.deserializeKey("http://example.com", context));
 assertThrows(IOException.class, () -> deserializer.deserializeKey("invalid", context));
 }

 @Test
 public void testURL() throws JsonMappingException, IOException {
 StdKeyDeserializer deserializer = StdKeyDeserializer.forType(URL.class);
 DeserializationContext context = mapper.getDeserializationContext();
 assertEquals(new URL("http://example.com"), deserializer.deserializeKey("http://example.com", context));
 assertThrows(IOException.class, () -> deserializer.deserializeKey("invalid", context));
 }

 @Test
 public void testClass() throws JsonMappingException, IOException, ClassNotFoundException {
 StdKeyDeserializer deserializer = StdKeyDeserializer.forType(String[].class);
 DeserializationContext context = mapper.getDeserializationContext();
 assertEquals(new String[]{"test"}, deserializer.deserializeKey("[\"test\"]", context));
 assertThrows(IOException.class, () -> deserializer.deserializeKey("invalid", context));
 }

 @Test
 public void testEnum() throws JsonMappingException, IOException {
 StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Enum.class);
 DeserializationContext context = mapper.getDeserializationContext();
 assertEquals(MyEnum.RED, deserializer.deserializeKey("RED", context));
 assertThrows(IOException.class, () -> deserializer.deserializeKey("INVALID", context));
 }

 @Test
 public void testEmptyString() throws JsonMappingException, IOException {
 StdKeyDeserializer deserializer = StdKeyDeserializer.forType(String.class);
 DeserializationContext context = mapper.getDeserializationContext();
 assertNull(deserializer.deserializeKey("", context));
 }

 @Test
 public void testNull() throws JsonMappingException, IOException {
 StdKeyDeserializer deserializer = StdKeyDeserializer.forType(String.class);
 DeserializationContext context = mapper.getDeserializationContext();
 assertNull(deserializer.deserializeKey(null, context));
 }

 @Test
 public void testUnknownType() throws JsonMappingException, IOException {
 StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Object.class);
 DeserializationContext context = mapper.getDeserializationContext();
 assertNull(deserializer.deserializeKey("{}", context));
 }
}