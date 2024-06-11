import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class StdKeyDeserializerTest {

 private ObjectMapper objectMapper;

 @BeforeAll
 public void setup() {
 objectMapper = new ObjectMapper();
 }

 @Test
 public void testBooleanDeserialization() throws JsonProcessingException {
 String trueKey = "true";
 String falseKey = "false";

 StdKeyDeserializer booleanDeserializer = StdKeyDeserializer.forType(Boolean.class);
 DeserializationContext context = mock(DeserializationContext.class);

 Object key = booleanDeserializer.deserializeKey(trueKey, context);
 assertTrue(key instanceof Boolean);
 assertEquals(true, ((Boolean) key).booleanValue());

 key = booleanDeserializer.deserializeKey(falseKey, context);
 assertTrue(key instanceof Boolean);
 assertEquals(false, ((Boolean) key).booleanValue());
 }

 @Test
 public void testByteDeserialization() throws JsonProcessingException {
 int minByteValue = Byte.MIN_VALUE;
 int maxByteValue = Byte.MAX_VALUE;
 int validByteValue = 50;

 StdKeyDeserializer byteDeserializer = StdKeyDeserializer.forType(Byte.class);
 DeserializationContext context = mock(DeserializationContext.class);

 Object key = byteDeserializer.deserializeKey("" + minByteValue, context);
 assertNotNull(key);
 assertTrue(key instanceof Byte);
 assertEquals(minByteValue, ((Byte) key).byteValue());

 key = byteDeserializer.deserializeKey("" + maxByteValue, context);
 assertNotNull(key);
 assertTrue(key instanceof Byte);
 assertEquals(maxByteValue, ((Byte) key).byteValue());

 key = byteDeserializer.deserializeKey("" + validByteValue, context);
 assertNotNull(key);
 assertTrue(key instanceof Byte);
 assertEquals(validByteValue, ((Byte) key).byteValue());

 key = byteDeserializer.deserializeKey("127", context); // max value for 7-bit signed byte
 assertNotNull(key);
 assertTrue(key instanceof Byte);
 assertEquals(127, ((Byte) key).byteValue());

 key = byteDeserializer.deserializeKey("-128", context); // min value for 8-bit signed byte
 assertNotNull(key);
 assertTrue(key instanceof Byte);
 assertEquals((byte) 128, ((Byte) key).byteValue());
 }

 @Test
 public void testShortDeserialization() throws JsonProcessingException {
 short minShortValue = Short.MIN_VALUE;
 short maxShortValue = Short.MAX_VALUE;
 short validShortValue = 100;

 StdKeyDeserializer shortDeserializer = StdKeyDeserializer.forType(Short.class);
 DeserializationContext context = mock(DeserializationContext.class);

 Object key = shortDeserializer.deserializeKey("" + minShortValue, context);
 assertNotNull(key);
 assertTrue(key instanceof Short);
 assertEquals(minShortValue, ((Short) key).shortValue());

 key = shortDeserializer.deserializeKey("" + maxShortValue, context);
 assertNotNull(key);
 assertTrue(key instanceof Short);
 assertEquals(maxShortValue, ((Short) key).shortValue());

 key = shortDeserializer.deserializeKey("" + validShortValue, context);
 assertNotNull(key);
 assertTrue(key instanceof Short);
 assertEquals(validShortValue, ((Short) key).shortValue());
 }

 @Test
 public void testCharDeserialization() throws JsonProcessingException {
 char validCharValue = 'A';
 char invalidCharValue = '\u0000'; // null char

 StdKeyDeserializer charDeserializer = StdKeyDeserializer.forType(Character.class);
 DeserializationContext context = mock(DeserializationContext.class);

 Object key = charDeserializer.deserializeKey("" + validCharValue, context);
 assertNotNull(key);
 assertTrue(key instanceof Character);
 assertEquals(validCharValue, ((Character) key).charValue());

 key = charDeserializer.deserializeKey("", context); // empty string
 assertNotNull(key);
 assertTrue(key instanceof Character);
 assertEquals(0, ((Character) key).charValue());

 key = charDeserializer.deserializeKey("" + invalidCharValue, context);
 assertNotNull(key);
 assertTrue(key instanceof Character);
 assertEquals(0, ((Character) key).charValue());
 }

 @Test
 public void testIntDeserialization() throws JsonProcessingException {
 int minIntValue = Integer.MIN_VALUE;
 int maxIntValue = Integer.MAX_VALUE;
 int validIntValue = 200;

 StdKeyDeserializer intDeserializer = StdKeyDeserializer.forType(Integer.class);
 DeserializationContext context = mock(DeserializationContext.class);

 Object key = intDeserializer.deserializeKey("" + minIntValue, context);
 assertNotNull(key);
 assertTrue(key instanceof Integer);
 assertEquals(minIntValue, ((Integer) key).intValue());

 key = intDeserializer.deserializeKey("" + maxIntValue, context);
 assertNotNull(key);
 assertTrue(key instanceof Integer);
 assertEquals(maxIntValue, ((Integer) key).intValue());

 key = intDeserializer.deserializeKey("" + validIntValue, context);
 assertNotNull(key);
 assertTrue(key instanceof Integer);
 assertEquals(validIntValue, ((Integer) key).intValue());
 }

 @Test
 public void testLongDeserialization() throws JsonProcessingException {
 long minLongValue = Long.MIN_VALUE;
 long maxLongValue = Long.MAX_VALUE;
 long validLongValue = 300L;

 StdKeyDeserializer longDeserializer = StdKeyDeserializer.forType(Long.class);
 DeserializationContext context = mock(DeserializationContext.class);

 Object key = longDeserializer.deserializeKey("" + minLongValue, context);
 assertNotNull(key);
 assertTrue(key instanceof Long);
 assertEquals(minLongValue, ((Long) key).longValue());

 key = longDeserializer.deserializeKey("" + maxLongValue, context);
 assertNotNull(key);
 assertTrue(key instanceof Long);
 assertEquals(maxLongValue, ((Long) key).longValue());

 key = longDeserializer.deserializeKey("" + validLongValue, context);
 assertNotNull(key);
 assertTrue(key instanceof Long);
 assertEquals(validLongValue, ((Long) key).longValue());
 }

 @Test
 public void testFloatDeserialization() throws JsonProcessingException {
 float minFloatValue = Float.MIN_VALUE;
 float maxFloatValue = Float.MAX_VALUE;
 float validFloatValue = 2.5f;

 StdKeyDeserializer floatDeserializer = StdKeyDeserializer.forType(Float.class);
 DeserializationContext context = mock(DeserializationContext.class);

 Object key = floatDeserializer.deserializeKey("" + minFloatValue, context);
 assertNotNull(key);
 assertTrue(key instanceof Float);
 assertEquals(minFloatValue, ((Float) key).floatValue());

 key = floatDeserializer.deserializeKey("" + maxFloatValue, context);
 assertNotNull(key);
 assertTrue(key instanceof Float);
 assertEquals(maxFloatValue, ((Float) key).floatValue());

 key = floatDeserializer.deserializeKey("" + validFloatValue, context);
 assertNotNull(key);
 assertTrue(key instanceof Float);
 assertEquals(validFloatValue, ((Float) key).floatValue());
 }

 @Test
 public void testDoubleDeserialization() throws JsonProcessingException {
 double minDoubleValue = Double.MIN_VALUE;
 double maxDoubleValue = Double.MAX_VALUE;
 double validDoubleValue = 3.14;

 StdKeyDeserializer doubleDeserializer = StdKeyDeserializer.forType(Double.class);
 DeserializationContext context = mock(DeserializationContext.class);

 Object key = doubleDeserializer.deserializeKey("" + minDoubleValue, context);
 assertNotNull(key);
 assertTrue(key instanceof Double);
 assertEquals(minDoubleValue, ((Double) key).doubleValue());

 key = doubleDeserializer.deserializeKey("" + maxDoubleValue, context);
 assertNotNull(key);
 assertTrue(key instanceof Double);
 assertEquals(maxDoubleValue, ((Double) key).doubleValue());

 key = doubleDeserializer.deserializeKey("" + validDoubleValue, context);
 assertNotNull(key);
 assertTrue(key instanceof Double);
 assertEquals(validDoubleValue, ((Double) key).doubleValue());
 }

 @Test
 public void testLocaleDeserialization() throws JsonProcessingException {
 String localeString = "en_US";

 StdKeyDeserializer localeDeserializer = StdKeyDeserializer.forType(Locale.class);
 DeserializationContext context = mock(DeserializationContext.class);

 Object key = localeDeserializer.deserializeKey(localeString, context);
 assertNotNull(key);
 assertTrue(key instanceof Locale);
 assertEquals(localeString, key.toString());
 }

 @Test
 public void testDateDeserialization() throws JsonProcessingException {
 String dateString = "2022-03-01";

 StdKeyDeserializer dateDeserializer = StdKeyDeserializer.forType(Date.class);
 DeserializationContext context = mock(DeserializationContext.class);

 Object key = dateDeserializer.deserializeKey(dateString, context);
 assertNotNull(key);
 // We cannot directly compare Dates using assertEquals as JUnit does not support it.
 // Instead, let's compare the number of milliseconds since 1970-01-01 (UTC)
 long expectedDateMilliseconds = 164630400000L;
 long actualDateMilliseconds = ((Date) key).getTime();
 assertEquals(expectedDateMilliseconds, actualDateMilliseconds);
 }

 @Test
 public void testCalendarDeserialization() throws JsonProcessingException {
 String dateString = "2022-03-01";

 StdKeyDeserializer calendarDeserializer = StdKeyDeserializer.forType(Calendar.class);
 DeserializationContext context = mock(DeserializationContext.class);

 Object key = calendarDeserializer.deserializeKey(dateString, context);
 assertNotNull(key);
 // We cannot directly compare Calendars using assertEquals as JUnit does not support it.
 // Instead, let's compare the number of milliseconds since 1970-01-01 (UTC)
 long expectedCalendarMilliseconds = 164630400000L;
 long actualCalendarMilliseconds = ((Calendar) key).getTimeInMillis();
 assertEquals(expectedCalendarMilliseconds, actualCalendarMilliseconds);
 }

 @Test
 public void testURIDeserialization() throws JsonProcessingException {
 String uriString = "http://www.example.com";

 StdKeyDeserializer uriDeserializer = StdKeyDeserializer.forType(URI.class);
 DeserializationContext context = mock(DeserializationContext.class);

 Object key = uriDeserializer.deserializeKey(uriString, context);
 assertNotNull(key);
 assertTrue(key instanceof URI);
 assertEquals(uriString, key.toString());
 }

 @Test
 public void testURLDeserialization() throws JsonProcessingException {
 String urlString = "http://www.example.com";

 StdKeyDeserializer urlDeserializer = StdKeyDeserializer.forType(URL.class);
 DeserializationContext context = mock(DeserializationContext.class);

 Object key = urlDeserializer.deserializeKey(urlString, context);
 assertNotNull(key);
 assertTrue(key instanceof URL);
 assertEquals(urlString, key.toString());
 }

 @Test
 public void testEnumDeserialization() throws JsonProcessingException {
 JsonNode jsonNode = objectMapper.readTree("{\"name\": \"TEST_ENUM_VALUE\"}");
 ObjectNode objectNode = (ObjectNode) jsonNode;
 String enumClassName = "com.example.MyCustomEnum";
 Class<? extends Enum> enumClass = (Class<? extends Enum>) Class.forName(enumClassName);

 StdKeyDeserializer enumDeserializer = StdKeyDeserializer.forType(enumClass);
 DeserializationContext context = mock(DeserializationContext.class);

 Object key = enumDeserializer.deserializeKey(objectNode.get("name").asText(), context);
 assertNotNull(key);
 assertTrue(key instanceof Enum);
 }

 @Test
 public void testClassDeserialization() throws JsonProcessingException {
 String className = "java.lang.String";

 StdKeyDeserializer classDeserializer = StdKeyDeserializer.forType(Class.forName(className));
 DeserializationContext context = mock(DeserializationContext.class);

 Object key = classDeserializer.deserializeKey(className, context);
 assertNotNull(key);
 assertTrue(key instanceof Class<?>);
 assertEquals(className, key.toString());
 }

 @Test
 public void testInvalidDeserialization() throws JsonProcessingException {
 StdKeyDeserializer invalidDeserializer = StdKeyDeserializer.forType(BigInteger.class);
 DeserializationContext context = mock(DeserializationContext.class);

 assertThrows(IOException.class, () -> {
 invalidDeserializer.deserializeKey("invalid_key", context);
 });
 }

 @AfterAll
 public void teardown() {
 objectMapper = null;
 }
}