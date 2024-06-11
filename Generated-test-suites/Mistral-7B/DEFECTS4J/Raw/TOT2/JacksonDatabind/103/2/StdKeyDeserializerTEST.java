import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper.DefaultTyping;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

public class StdKeyDeserializerTest {

 private ObjectMapper objectMapper;

 @Before
 public void setUp() {
 objectMapper = new ObjectMapper();
 objectMapper.setSerializationInclusion(JsonMapper.SerializationConfig.FindNulls.NON_NULL);
 objectMapper.setSerializationInclusion(JsonMapper.SerializationConfig.FindNulls.NON_EMPTY);
 objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
 objectMapper.configure(DeserializationFeature.FAIL_ON_EMPTY_BEANS, false);
 objectMapper.configure(DeserializationFeature.FAIL_ON_INVALID_FORMAT, false);
 objectMapper.configure(DeserializationFeature.FAIL_ON_MISSING_CREATOR_FOR_ARRAY, false);
 objectMapper.configure(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_AS_NULL, true);
 objectMapper.configure(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_AS_NULL, true);
 objectMapper.setDefaultTyping(DefaultTyping.OBJECT_AND_WRAPPER_FQCN);
 }

 @Test
 public void testBooleanKeyDeserialization() throws JsonProcessingException {
 StdKeyDeserializer stdKeyDeserializer = StdKeyDeserializer.forType(Boolean.class);
 DeserializationContext context = objectMapper.getDeserializationContext();
 assertEquals(Boolean.TRUE, stdKeyDeserializer.deserializeKey("true", context));
 assertEquals(Boolean.FALSE, stdKeyDeserializer.deserializeKey("false", context));
 assertNull(stdKeyDeserializer.deserializeKey("invalid", context));
 }

 @Test
 public void testByteKeyDeserialization() throws JsonProcessingException {
 StdKeyDeserializer stdKeyDeserializer = StdKeyDeserializer.forType(Byte.class);
 DeserializationContext context = objectMapper.getDeserializationContext();
 assertEquals((byte) 127, stdKeyDeserializer.deserializeKey("127", context));
 assertEquals((byte) 0, stdKeyDeserializer.deserializeKey("0", context));
 assertThrows(IOException.class, () -> stdKeyDeserializer.deserializeKey("256", context));
 }

 @Test
 public void testShortKeyDeserialization() throws JsonProcessingException {
 StdKeyDeserializer stdKeyDeserializer = StdKeyDeserializer.forType(Short.class);
 DeserializationContext context = objectMapper.getDeserializationContext();
 assertEquals((short) 12345, stdKeyDeserializer.deserializeKey("12345", context));
 assertEquals((short) 0, stdKeyDeserializer.deserializeKey("0", context));
 assertThrows(IOException.class, () -> stdKeyDeserializer.deserializeKey("32000", context));
 }

 @Test
 public void testCharKeyDeserialization() throws JsonProcessingException {
 StdKeyDeserializer stdKeyDeserializer = StdKeyDeserializer.forType(Character.class);
 DeserializationContext context = objectMapper.getDeserializationContext();
 assertEquals('A', stdKeyDeserializer.deserializeKey("A", context));
 assertNull(stdKeyDeserializer.deserializeKey("invalid", context));
 }

 @Test
 public void testIntKeyDeserialization() throws JsonProcessingException {
 StdKeyDeserializer stdKeyDeserializer = StdKeyDeserializer.forType(Integer.class);
 DeserializationContext context = objectMapper.getDeserializationContext();
 assertEquals(12345, stdKeyDeserializer.deserializeKey("12345", context));
 assertEquals(0, stdKeyDeserializer.deserializeKey("0", context));
 }

 @Test
 public void testLongKeyDeserialization() throws JsonProcessingException {
 StdKeyDeserializer stdKeyDeserializer = StdKeyDeserializer.forType(Long.class);
 DeserializationContext context = objectMapper.getDeserializationContext();
 assertEquals(1234567890L, stdKeyDeserializer.deserializeKey("1234567890", context));
 assertEquals(0L, stdKeyDeserializer.deserializeKey("0", context));
 }

 @Test
 public void testFloatKeyDeserialization() throws JsonProcessingException {
 StdKeyDeserializer stdKeyDeserializer = StdKeyDeserializer.forType(Float.class);
 DeserializationContext context = objectMapper.getDeserializationContext();
 assertEquals(123.45f, stdKeyDeserializer.deserializeKey("123.45", context), 0.001f);
 assertThrows(IOException.class, () -> stdKeyDeserializer.deserializeKey("invalid", context));
 }

 @Test
 public void testDoubleKeyDeserialization() throws JsonProcessingException {
 StdKeyDeserializer stdKeyDeserializer = StdKeyDeserializer.forType(Double.class);
 DeserializationContext context = objectMapper.getDeserializationContext();
 assertEquals(123.45, stdKeyDeserializer.deserializeKey("123.45", context), 0.001);
 assertThrows(IOException.class, () -> stdKeyDeserializer.deserializeKey("invalid", context));
 }

 @Test
 public void testEnumKeyDeserialization() throws JsonProcessingException {
 StdKeyDeserializer stdKeyDeserializer = StdKeyDeserializer.forType(Locale.class);
 DeserializationContext context = objectMapper.getDeserializationContext();
 assertEquals(Locale.US, stdKeyDeserializer.deserializeKey("US", context));
 assertNull(stdKeyDeserializer.deserializeKey("invalid", context));
 }

 @Test
 public void testDateKeyDeserialization() throws JsonProcessingException {
 StdKeyDeserializer stdKeyDeserializer = StdKeyDeserializer.forType(Date.class);
 DeserializationContext context = objectMapper.getDeserializationContext();
 assertEquals(new Date(1609459200000L), stdKeyDeserializer.deserializeKey("2020-12-31", context));
 assertNull(stdKeyDeserializer.deserializeKey("invalid", context));
 }

 @Test
 public void testCalendarKeyDeserialization() throws JsonProcessingException {
 StdKeyDeserializer stdKeyDeserializer = StdKeyDeserializer.forType(Calendar.class);
 DeserializationContext context = objectMapper.getDeserializationContext();
 assertEquals(Calendar.getInstance(), stdKeyDeserializer.deserializeKey("", context));
 assertNull(stdKeyDeserializer.deserializeKey("invalid", context));
 }

 @Test
 public void testUUIDKeyDeserialization() throws JsonProcessingException {
 StdKeyDeserializer stdKeyDeserializer = StdKeyDeserializer.forType(UUID.class);
 DeserializationContext context = objectMapper.getDeserializationContext();
 assertEquals(UUID.fromString("00000000-0000-0000-0000-000000000000"), stdKeyDeserializer.deserializeKey("00000000-0000-0000-0000-000000000000", context));
 assertNull(stdKeyDeserializer.deserializeKey("invalid", context));
 }

 @Test
 public void testURIKeyDeserialization() throws JsonProcessingException {
 StdKeyDeserializer stdKeyDeserializer = StdKeyDeserializer.forType(URI.class);
 DeserializationContext context = objectMapper.getDeserializationContext();
 assertEquals(new URI("http://example.com"), stdKeyDeserializer.deserializeKey("http://example.com", context));
 assertNull(stdKeyDeserializer.deserializeKey("invalid", context));
 }

 @Test
 public void testURLKeyDeserialization() throws JsonProcessingException {
 StdKeyDeserializer stdKeyDeserializer = StdKeyDeserializer.forType(URL.class);
 DeserializationContext context = objectMapper.getDeserializationContext();
 assertEquals(new URL("http://example.com"), stdKeyDeserializer.deserializeKey("http://example.com", context));
 assertNull(stdKeyDeserializer.deserializeKey("invalid", context));
 }

 @Test
 public void testClassKeyDeserialization() throws JsonProcessingException {
 StdKeyDeserializer stdKeyDeserializer = StdKeyDeserializer.forType(String[].class);
 DeserializationContext context = objectMapper.getDeserializationContext();
 assertEquals(new String[] {"one", "two", "three"}, stdKeyDeserializer.deserializeKey("[\"one\",\"two\",\"three\"]", context));
 assertNull(stdKeyDeserializer.deserializeKey("invalid", context));
 }
}