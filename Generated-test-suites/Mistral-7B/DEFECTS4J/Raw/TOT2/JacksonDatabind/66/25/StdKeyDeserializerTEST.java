import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class StdKeyDeserializerTest {

 private ObjectMapper objectMapper;

 @BeforeAll
 public void setup() {
 objectMapper = new ObjectMapper();
 SimpleModule module = new SimpleModule();
 module.addDeserializer(URI.class, new StdKeyDeserializer(StdKeyDeserializer.TYPE_URI, URI.class));
 module.addDeserializer(URL.class, new StdKeyDeserializer(StdKeyDeserializer.TYPE_URL, URL.class));
 objectMapper.registerModule(module);
 }

 @Test
 public void testBoolean() throws IOException, JsonMappingException {
 Object result = objectMapper.readValue("\"true\"", StdKeyDeserializer.class);
 assertTrue((Boolean) result.deserializeKey("true", new TestContext()));
 }

 @Test
 public void testByte() throws IOException, JsonMappingException {
 Object result = objectMapper.readValue("\"123\"", StdKeyDeserializer.class);
 assertEquals((byte) 123, result.deserializeKey("123", new TestContext()));
 }

 @Test
 public void testShort() throws IOException, JsonMappingException {
 Object result = objectMapper.readValue("\"12345\"", StdKeyDeserializer.class);
 assertEquals((short) 12345, result.deserializeKey("12345", new TestContext()));
 }

 @Test
 public void testChar() throws IOException, JsonMappingException {
 Object result = objectMapper.readValue("\"a\"", StdKeyDeserializer.class);
 assertEquals('a', result.deserializeKey("a", new TestContext()));
 }

 @Test
 public void testInt() throws IOException, JsonMappingException {
 Object result = objectMapper.readValue("\"123456\"", StdKeyDeserializer.class);
 assertEquals(123456, result.deserializeKey("123456", new TestContext()));
 }

 @Test
 public void testLong() throws IOException, JsonMappingException {
 Object result = objectMapper.readValue("\"12345678901234567890\"", StdKeyDeserializer.class);
 assertEquals(12345678901234567890L, result.deserializeKey("12345678901234567890", new TestContext()));
 }

 @Test
 public void testFloat() throws IOException, JsonMappingException {
 Object result = objectMapper.readValue("\"123.45\"", StdKeyDeserializer.class);
 assertEquals(123.45f, result.deserializeKey("123.45", new TestContext()));
 }

 @Test
 public void testDouble() throws IOException, JsonMappingException {
 Object result = objectMapper.readValue("\"123.45.67\"", StdKeyDeserializer.class);
 assertEquals(123.4567, result.deserializeKey("123.45.67", new TestContext()), 0.001);
 }

 @Test
 public void testLocale() throws IOException, JsonMappingException {
 Object result = objectMapper.readValue("\"en_US\"", StdKeyDeserializer.class);
 assertEquals(Locale.US, result.deserializeKey("en_US", new TestContext()));
 }

 @Test
 public void testDate() throws IOException, JsonMappingException {
 ObjectMapper mapper = new ObjectMapper();
 mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
 String json = "{\"date\":\"2022-03-12\"}";
 Object result = mapper.readValue(json, Map.class).get("date");
 assertEquals(new Date(1647046400000L), result.deserializeKey("2022-03-12", new TestContext()));
 }

 @Test
 public void testCalendar() throws IOException, JsonMappingException {
 ObjectMapper mapper = new ObjectMapper();
 mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
 String json = "{\"calendar\":\"2022-03-12\"}";
 Object result = mapper.readValue(json, Map.class).get("calendar");
 Calendar calendar = new GregorianCalendar(2022, 2, 12);
 assertEquals(calendar, result.deserializeKey("2022-03-12", new TestContext()));
 }

 @Test
 public void testEnum() throws IOException, JsonMappingException {
 ObjectMapper mapper = new ObjectMapper();
 mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
 mapper.configure(DeserializationFeature.READ_ENUMS_USING_TO_STRING, true);
 String json = "{\"color\":\"RED\"}";
 Object result = mapper.readValue(json, Map.class).get("color");
 assertEquals(Color.RED, result.deserializeKey("RED", new TestContext()));
 }

 @Test
 public void testUri() throws IOException, JsonMappingException {
 Object result = objectMapper.readValue("\"http://test.com/path\"", StdKeyDeserializer.class);
 assertEquals(new URI("http://test.com/path"), result.deserializeKey("http://test.com/path", new TestContext()));
 }

 @Test
 public void testUrl() throws IOException, JsonMappingException {
 Object result = objectMapper.readValue("\"http://test.com/path\"", StdKeyDeserializer.class);
 assertEquals(new URL("http://test.com/path"), result.deserializeKey("http://test.com/path", new TestContext()));
 }

 @Test
 public void testClass() throws IOException, JsonMappingException {
 Object result = objectMapper.readValue("\"java.lang.String\"", StdKeyDeserializer.class);
 assertEquals(String.class, result.deserializeKey("java.lang.String", new TestContext()));
 }

 @Test
 public void testInvalidInteger() throws IOException, JsonMappingException {
 Object result = objectMapper.readValue("\"abc\"", StdKeyDeserializer.class);
 assertThrows(JsonMappingException.class, () -> result.deserializeKey("abc", new TestContext()));
 }

 @Test
 public void testInvalidLong() throws IOException, JsonMappingException {
 Object result = objectMapper.readValue("\"abcdef\"", StdKeyDeserializer.class);
 assertThrows(JsonMappingException.class, () -> result.deserializeKey("abcdef", new TestContext()));
 }

 @Test
 public void testInvalidFloat() throws IOException, JsonMappingException {
 Object result = objectMapper.readValue("\"abc.def\"", StdKeyDeserializer.class);
 assertThrows(JsonMappingException.class, () -> result.deserializeKey("abc.def", new TestContext()));
 }

 @Test
 public void testInvalidDouble() throws IOException, JsonMappingException {
 Object result = objectMapper.readValue("\"abc.def.ghi\"", StdKeyDeserializer.class);
 assertThrows(JsonMappingException.class, () -> result.deserializeKey("abc.def.ghi", new TestContext()));
 }

 @Test
 public void testInvalidLocale() throws IOException, JsonMappingException {
 Object result = objectMapper.readValue("\"ab:cd_ef\"", StdKeyDeserializer.class);
 assertThrows(JsonMappingException.class, () -> result.deserializeKey("ab:cd_ef", new TestContext()));
 }

 @Test
 public void testInvalidDate() throws IOException, JsonMappingException {
 Object result = objectMapper.readValue("\"2022-13-12\"", StdKeyDeserializer.class);
 assertThrows(JsonMappingException.class, () -> result.deserializeKey("2022-13-12", new TestContext()));
 }

 @Test
 public void testInvalidCalendar() throws IOException, JsonMappingException {
 Object result = objectMapper.readValue("\"2022-13-13\"", StdKeyDeserializer.class);
 assertThrows(JsonMappingException.class, () -> result.deserializeKey("2022-13-13", new TestContext()));
 }

 @Test
 public void testInvalidBoolean() throws IOException, JsonMappingException {
 Object result = objectMapper.readValue("\"invalid\"", StdKeyDeserializer.class);
 assertThrows(JsonMappingException.class, () -> result.deserializeKey("invalid", new TestContext()));
 }

 @Test
 public void testInvalidEnum() throws IOException, JsonMappingException {
 ObjectMapper mapper = new ObjectMapper();
 mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
 mapper.configure(DeserializationFeature.READ_ENUMS_USING_TO_STRING, true);
 String json = "{\"color\":\"INVALID\"}";
 Object result = mapper.readValue(json, Map.class).get("color");
 assertThrows(JsonMappingException.class, () -> result.deserializeKey("INVALID", new TestContext()));
 }

 @Test
 public void testInvalidUri() throws IOException, JsonMappingException {
 Object result = objectMapper.readValue("\"<http://test.com/path>\"", StdKeyDeserializer.class);
 assertThrows(JsonMappingException.class, () -> result.deserializeKey("<http://test.com/path>", new TestContext()));
 }

 @Test
 public void testInvalidUrl() throws IOException, JsonMappingException {
 Object result = objectMapper.readValue("\"<http://test.com/path>\"", StdKeyDeserializer.class);
 assertThrows(JsonMappingException.class, () -> result.deserializeKey("<http://test.com/path>", new TestContext()));
 }

 @Test
 public void testInvalidClass() throws IOException, JsonMappingException {
 Object result = objectMapper.readValue("\"java.non.existent.String\"", StdKeyDeserializer.class);
 assertThrows(JsonMappingException.class, () -> result.deserializeKey("java.non.existent.String", new TestContext()));
 }

 @Test
 public void testNullKey() throws IOException, JsonMappingException {
 Object result = objectMapper.readValue("\"null\"", StdKeyDeserializer.class);
 assertNull(result.deserializeKey("null", new TestContext()));
 }

 @Test
 public void testEmptyString() throws IOException, JsonMappingException {
 Object result = objectMapper.readValue("\"\"", StdKeyDeserializer.class);
 assertNull(result.deserializeKey("", new TestContext()));
 }

 class TestContext extends DeserializationContext {
 }
}