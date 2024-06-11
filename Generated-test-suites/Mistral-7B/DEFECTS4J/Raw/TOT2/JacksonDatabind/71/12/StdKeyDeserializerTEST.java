import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class StdKeyDeserializerTest {

 private static ObjectMapper objectMapper;

 @BeforeAll
 static void setup() {
 objectMapper = new ObjectMapper();
 }

 @Test
 void testBooleanTypeDeserialization() throws Exception {
 String key = "true";
 Object result = new StdKeyDeserializer(StdKeyDeserializer.TYPE_BOOLEAN, Boolean.class).deserializeKey(key, objectMapper.getDeserializationContext());
 assertTrue(result instanceof Boolean);
 assertTrue((Boolean) result);
 }

 @Test
 void testBooleanTypeDeserializationWithFalse() throws Exception {
 String key = "false";
 Object result = new StdKeyDeserializer(StdKeyDeserializer.TYPE_BOOLEAN, Boolean.class).deserializeKey(key, objectMapper.getDeserializationContext());
 assertFalse(result instanceof Boolean);
 assertFalse((Boolean) result);
 }

 @Test
 void testByteTypeDeserialization() throws Exception {
 String key = "5";
 Object result = new StdKeyDeserializer(StdKeyDeserializer.TYPE_BYTE, Byte.class).deserializeKey(key, objectMapper.getDeserializationContext());
 assertEquals(Byte.valueOf((byte) 5), result);
 }

 @Test
 void testByteTypeDeserializationWithOverflow() throws Exception {
 String key = "256";
 assertThrows(JsonMappingException.class, () -> new StdKeyDeserializer(StdKeyDeserializer.TYPE_BYTE, Byte.class).deserializeKey(key, objectMapper.getDeserializationContext()));
 }

 @Test
 void testShortTypeDeserialization() throws Exception {
 String key = "100";
 Object result = new StdKeyDeserializer(StdKeyDeserializer.TYPE_SHORT, Short.class).deserializeKey(key, objectMapper.getDeserializationContext());
 assertEquals(Short.valueOf((short) 100), result);
 }

 @Test
 void testShortTypeDeserializationWithOverflow() throws Exception {
 String key = "32768";
 assertThrows(JsonMappingException.class, () -> new StdKeyDeserializer(StdKeyDeserializer.TYPE_SHORT, Short.class).deserializeKey(key, objectMapper.getDeserializationContext()));
 }

 @Test
 void testCharTypeDeserialization() throws Exception {
 String key = "'A'";
 Object result = new StdKeyDeserializer(StdKeyDeserializer.TYPE_CHAR, Character.class).deserializeKey(key, objectMapper.getDeserializationContext());
 assertEquals(Character.valueOf('A'), result);
 }

 @Test
 void testCharTypeDeserializationWithWrongLength() throws Exception {
 String key = "AB";
 assertThrows(JsonMappingException.class, () -> new StdKeyDeserializer(StdKeyDeserializer.TYPE_CHAR, Character.class).deserializeKey(key, objectMapper.getDeserializationContext()));
 }

 @Test
 void testIntTypeDeserialization() throws Exception {
 String key = "10";
 Object result = new StdKeyDeserializer(StdKeyDeserializer.TYPE_INT, Integer.class).deserializeKey(key, objectMapper.getDeserializationContext());
 assertEquals(Integer.valueOf(10), result);
 }

 @Test
 void testLongTypeDeserialization() throws Exception {
 String key = "1000000";
 Object result = new StdKeyDeserializer(StdKeyDeserializer.TYPE_LONG, Long.class).deserializeKey(key, objectMapper.getDeserializationContext());
 assertEquals(Long.valueOf(1000000), result);
 }

 @Test
 void testFloatTypeDeserialization() throws Exception {
 String key = "3.14";
 Object result = new StdKeyDeserializer(StdKeyDeserializer.TYPE_FLOAT, Float.class).deserializeKey(key, objectMapper.getDeserializationContext());
 assertEquals(Float.valueOf(3.14f), result);
 }

 @Test
 void testDoubleTypeDeserialization() throws Exception {
 String key = "3.141592653589793238462643383279502884197169399375105820974944592307816406286208998628034825342117067982148086513282306647093844609550582231725359408128481117450284102701938521105559644622948954930381964428810975665933446128475648233778137538880097288317250115516924330612206201842776821219544453094172321214581765680755001370795866564308287547135296394280525367201425492131120110864248814105727";
 Object result = new StdKeyDeserializer(StdKeyDeserializer.TYPE_DOUBLE, Double.class).deserializeKey(key, objectMapper.getDeserializationContext());
 assertEquals(Double.valueOf(Math.PI), result);
 }

 @Test
 void testLocaleTypeDeserialization() throws Exception {
 String key = "en_US";
 Object result = new StdKeyDeserializer(StdKeyDeserializer.TYPE_LOCALE, Locale.class).deserializeKey(key, objectMapper.getDeserializationContext());
 assertEquals(Locale.forLanguageTag("en-US"), result);
 }

 @Test
 void testLocaleTypeDeserializationWithWrongLength() throws Exception {
 String key = "en_US_";
 assertThrows(JsonMappingException.class, () -> new StdKeyDeserializer(StdKeyDeserializer.TYPE_LOCALE, Locale.class).deserializeKey(key, objectMapper.getDeserializationContext()));
 }

 @Test
 void testDateTypeDeserialization() throws Exception {
 ObjectNode dateNode = objectMapper.readValue("{\"date\":\"2022-01-01\"}", ObjectNode.class);
 Object result = new StdKeyDeserializer(StdKeyDeserializer.TYPE_DATE, Date.class).deserializeKey(dateNode.get("date").asText(), objectMapper.getDeserializationContext());
 assertEquals(new Date(2022, 0, 1), result);
 }

 @Test
 void testCalendarTypeDeserialization() throws Exception {
 ObjectNode calendarNode = objectMapper.readValue("{\"calendar\":{\"timeZoneOffset\":-3600000,\"timeZoneID\":\"America/New_York\",\"isAD\":false,\"fields\":{\"year\":2022,\"month\":1,\"dayOfMonth\":5,\"dayOfWeek\":2,\"dayOfYear\":15,\"era\":1,\"yearDay\":1,\"millis\":123456789,\"zone\":\"America/New_York\",\"weekYear\":2022,\"weekNumber\":1,\"weekDay\":2,\"dayOfWeekInWeek\":5,\"dayOfYearInWeek\":5,\"dayOfMonthInMonth\":5,\"dayOfWeekInMonth\":5,\"dayOfYearInYear\":15,\"dayOfWeekInYear\":2,\"dayOfYearInWeekYear\":15,\"dayOfWeekInWeekYear\":5,\"dayOfWeekInMonth\":5}}}", ObjectNode.class);
 Object result = new StdKeyDeserializer(StdKeyDeserializer.TYPE_CALENDAR, Calendar.class).deserializeKey(calendarNode.get("calendar").toString(), objectMapper.getDeserializationContext());
 assertEquals(Calendar.getInstance(), result);
 }

 @Test
 void testUUIDTypeDeserialization() throws Exception {
 String key = "123e4567-e89b-12d3-a456-426655440000";
 Object result = new StdKeyDeserializer(StdKeyDeserializer.TYPE_UUID, UUID.class).deserializeKey(key, objectMapper.getDeserializationContext());
 assertEquals(UUID.fromString(key), result);
 }

 @Test
 void testURITypeDeserialization() throws Exception {
 String key = "http://example.com/test";
 Object result = new StdKeyDeserializer(StdKeyDeserializer.TYPE_URI, URI.class).deserializeKey(key, objectMapper.getDeserializationContext());
 assertEquals(new URI(key), result);
 }

 @Test
 void testURLTypeDeserialization() throws Exception {
 String key = "http://example.com/test";
 Object result = new StdKeyDeserializer(StdKeyDeserializer.TYPE_URL, URL.class).deserializeKey(key, objectMapper.getDeserializationContext());
 assertEquals(new URL(key), result);
 }

 @Test
 void testClassTypeDeserialization() throws Exception {
 String key = "java.lang.String";
 Object result = new StdKeyDeserializer(StdKeyDeserializer.TYPE_CLASS, Class.class).deserializeKey(key, objectMapper.getDeserializationContext());
 assertEquals(Class.forName(key), result);
 }

 @Test
 void testLocaleTypeDeserializationWithUnknownLocale() throws Exception {
 String key = "xx_XX";
 assertThrows(JsonMappingException.class, () -> new StdKeyDeserializer(StdKeyDeserializer.TYPE_LOCALE, Locale.class).deserializeKey(key, objectMapper.getDeserializationContext()));
 }

 @Test
 void testCurrencyTypeDeserialization() throws Exception {
 String key = "USD";
 Object result = new StdKeyDeserializer(StdKeyDeserializer.TYPE_CURRENCY, Currency.class).deserializeKey(key, objectMapper.getDeserializationContext());
 assertEquals(Currency.getInstance("USD"), result);
 }

 @Test
 void testCurrencyTypeDeserializationWithUnknownCurrency() throws Exception {
 String key = "XXX";
 assertThrows(JsonMappingException.class, () -> new StdKeyDeserializer(StdKeyDeserializer.TYPE_CURRENCY, Currency.class).deserializeKey(key, objectMapper.getDeserializationContext()));
 }
}