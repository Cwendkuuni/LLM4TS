import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.DeserializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

public class StdKeyDeserializerTest {

 private ObjectMapper mapper;

 @SuppressWarnings("unchecked")
 @BeforeClass
 public void setup() throws Exception {
 mapper = new ObjectMapper();
 SimpleModule module = new SimpleModule();
 module.addDeserializer(String.class, StdKeyDeserializer.StringKD.forType(String.class));
 mapper.registerModule(module);
 }

 @Test
 public void testBooleanDeserialization() throws JsonMappingException, JsonProcessingException {
 StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Boolean.class);
 DeserializationContext context = mapper.getDeserializationContext();
 assertEquals(Boolean.TRUE, deserializer.deserializeKey("true", context));
 assertEquals(Boolean.FALSE, deserializer.deserializeKey("false", context));
 }

 @Test
 public void testByteDeserialization() throws JsonMappingException, JsonProcessingException {
 StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Byte.class);
 DeserializationContext context = mapper.getDeserializationContext();
 assertEquals((byte) 1, deserializer.deserializeKey("1", context));
 assertThrows(JsonMappingException.class, () -> deserializer.deserializeKey("-128", context));
 }

 @Test
 public void testShortDeserialization() throws JsonMappingException, JsonProcessingException {
 StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Short.class);
 DeserializationContext context = mapper.getDeserializationContext();
 assertEquals((short) 1, deserializer.deserializeKey("1", context));
 assertThrows(JsonMappingException.class, () -> deserializer.deserializeKey("-32768", context));
 }

 @Test
 public void testCharDeserialization() throws JsonMappingException, JsonProcessingException {
 StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Character.class);
 DeserializationContext context = mapper.getDeserializationContext();
 assertEquals('A', deserializer.deserializeKey("A", context));
 assertThrows(JsonMappingException.class, () -> deserializer.deserializeKey("ABC", context));
 }

 @Test
 public void testIntDeserialization() throws JsonMappingException, JsonProcessingException {
 StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Integer.class);
 DeserializationContext context = mapper.getDeserializationContext();
 assertEquals(1, deserializer.deserializeKey("1", context));
 assertThrows(JsonMappingException.class, () -> deserializer.deserializeKey("-2147483648", context));
 }

 @Test
 public void testLongDeserialization() throws JsonMappingException, JsonProcessingException {
 StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Long.class);
 DeserializationContext context = mapper.getDeserializationContext();
 assertEquals(1L, deserializer.deserializeKey("1", context));
 assertThrows(JsonMappingException.class, () -> deserializer.deserializeKey("-9223372036854775808", context));
 }

 @Test
 public void testFloatDeserialization() throws JsonMappingException, JsonProcessingException {
 StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Float.class);
 DeserializationContext context = mapper.getDeserializationContext();
 assertEquals(1.0f, deserializer.deserializeKey("1.0", context), 0.001f);
 assertThrows(JsonMappingException.class, () -> deserializer.deserializeKey("NaN", context));
 }

 @Test
 public void testDoubleDeserialization() throws JsonMappingException, JsonProcessingException {
 StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Double.class);
 DeserializationContext context = mapper.getDeserializationContext();
 assertEquals(1.0d, deserializer.deserializeKey("1.0", context), 0.001d);
 assertThrows(JsonMappingException.class, () -> deserializer.deserializeKey("NaN", context));
 }

 @Test
 public void testLocaleDeserialization() throws JsonMappingException, JsonProcessingException {
 StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Locale.class);
 DeserializationContext context = mapper.getDeserializationContext();
 assertEquals(Locale.ENGLISH, deserializer.deserializeKey("en_US", context));
 assertThrows(JsonMappingException.class, () -> deserializer.deserializeKey("es_ES", context));
 }

 @Test
 public void testCurrencyDeserialization() throws JsonMappingException, JsonProcessingException {
 StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Currency.class);
 DeserializationContext context = mapper.getDeserializationContext();
 assertEquals(Currency.getInstance("USD"), deserializer.deserializeKey("USD", context));
 assertThrows(JsonMappingException.class, () -> deserializer.deserializeKey("EUR", context));
 }

 @Test
 public void testDateDeserialization() throws JsonMappingException, JsonProcessingException {
 StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Date.class);
 DeserializationContext context = mapper.getDeserializationContext();
 assertEquals(new Date(), deserializer.deserializeKey("2022-12-31T23:59:59Z", context));
 assertThrows(JsonMappingException.class, () -> deserializer.deserializeKey("InvalidDate", context));
 }

 @Test
 public void testCalendarDeserialization() throws JsonMappingException, JsonProcessingException {
 StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Calendar.class);
 DeserializationContext context = mapper.getDeserializationContext();
 JavaTimeZone timeZone = JavaTimeZone.systemDefault();
 assertEquals(Calendar.getInstance(timeZone), deserializer.deserializeKey("2022-12-31", context));
 assertNull(deserializer.deserializeKey("InvalidDate", context));
 }

 @Test
 public void testUUIDDeserialization() throws JsonMappingException, JsonProcessingException {
 StdKeyDeserializer deserializer = StdKeyDeserializer.forType(UUID.class);
 DeserializationContext context = mapper.getDeserializationContext();
 assertEquals(UUID.fromString("123e4567-e89b-12d3-a456-426614174000"), deserializer.deserializeKey("123e4567-e89b-12d3-a456-426614174000", context));
 assertThrows(JsonMappingException.class, () -> deserializer.deserializeKey("Invalid UUID", context));
 }

 @Test
 public void testURIDeserialization() throws JsonMappingException, JsonProcessingException {
 StdKeyDeserializer deserializer = StdKeyDeserializer.forType(URI.class);
 DeserializationContext context = mapper.getDeserializationContext();
 assertEquals(new URI("https://www.example.com"), deserializer.deserializeKey("https://www.example.com", context));
 assertThrows(JsonMappingException.class, () -> deserializer.deserializeKey("InvalidURI", context));
 }

 @Test
 public void testURLDeserialization() throws JsonMappingException, JsonProcessingException {
 StdKeyDeserializer deserializer = StdKeyDeserializer.forType(URL.class);
 DeserializationContext context = mapper.getDeserializationContext();
 assertEquals(new URL("https://www.example.com"), deserializer.deserializeKey("https://www.example.com", context));
 assertThrows(JsonMappingException.class, () -> deserializer.deserializeKey("InvalidURL", context));
 }

 @Test
 public void testClassDeserialization() throws JsonMappingException, JsonProcessingException {
 StdKeyDeserializer deserializer = StdKeyDeserializer.forType(String[].class);
 DeserializationContext context = mapper.getDeserializationContext();
 assertEquals(new String[]{"a", "b"}, deserializer.deserializeKey("[\"a\", \"b\"]", context));
 assertThrows(JsonMappingException.class, () -> deserializer.deserializeKey("[1, 2]", context));
 }

 @Test
 public void testEnumDeserialization() throws JsonMappingException, JsonProcessingException {
 StdKeyDeserializer deserializer = StdKeyDeserializer.forType(MyCustomEnum.class);
 DeserializationContext context = mapper.getDeserializationContext();
 assertEquals(MyCustomEnum.VALUE1, deserializer.deserializeKey("VALUE1", context));
 assertThrows(JsonMappingException.class, () -> deserializer.deserializeKey("INVALID_VALUE", context));
 }

 @Test
 public void testUnknownEnumDeserialization() throws JsonMappingException, JsonProcessingException {
 StdKeyDeserializer deserializer = StdKeyDeserializer.forType(MyCustomEnum.class);
 DeserializationContext context = mapper.getDeserializationContext();
 DeserializerProvider provider = mapper.getDeserializerProvider();
 assertNull(deserializer.deserializeKey("UnknownValue", context));
 assertNull(provider.findValueDeserializer(MyCustomEnum.class, context));
 }

 @Test
 public void testUnknownEnumDeserializationWithNullHandling() throws JsonMappingException, JsonProcessingException {
 StdKeyDeserializer deserializer = StdKeyDeserializer.forType(MyCustomEnum.class);
 DeserializationContext context = mapper.getDeserializationContext();
 DeserializerProvider provider = mapper.getDeserializerProvider();
 context.setConfig(mapper.getConfig().with(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_AS_NULL));
 assertEquals(null, deserializer.deserializeKey("UnknownValue", context));
 assertNull(provider.findValueDeserializer(MyCustomEnum.class, context));
 }
}