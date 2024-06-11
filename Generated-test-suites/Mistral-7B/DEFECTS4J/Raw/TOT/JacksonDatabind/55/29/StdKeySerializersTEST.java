import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.EnumSet;
import java.util.Properties;

public class StdKeySerializersTest {

 private ObjectMapper objectMapper;

 @Before
 public void setup() {
 objectMapper = new ObjectMapper();
 }

 /**
 * Test for getStdKeySerializer method when rawKeyType is null
 */
 @Test
 public void testGetStdKeySerializerNullRawKeyType() {
 JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(null, null, true);
 Assert.assertNotNull(serializer);
 Assert.assertEquals(StdKeySerializers.Dynamic.class, serializer.getClass());
 }

 /**
 * Test for getStdKeySerializer method when rawKeyType is String
 */
 @Test
 public void testGetStdKeySerializerStringRawKeyType() {
 JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(null, String.class, true);
 Assert.assertNotNull(serializer);
 Assert.assertEquals(StdKeySerializers.StringKeySerializer.class, serializer.getClass());
 }

 /**
 * Test for getStdKeySerializer method when rawKeyType is Number
 */
 @Test
 public void testGetStdKeySerializerNumberRawKeyType() {
 JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(null, Integer.class, true);
 Assert.assertNotNull(serializer);
 Assert.assertEquals(StdKeySerializers.DEFAULT_KEY_SERIALIZER.class, serializer.getClass());
 }

 /**
 * Test for getStdKeySerializer method when rawKeyType is not supported
 */
 @Test
 public void testGetStdKeySerializerUnsupportedRawKeyType() {
 JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(null, MyCustomClass.class, true);
 Assert.assertNull(serializer);
 }

 /**
 * Test for getStdKeySerializer method when rawKeyType is Date
 */
 @Test
 public void testGetStdKeySerializerDateRawKeyType() {
 JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(null, Date.class, true);
 Assert.assertNotNull(serializer);
 Assert.assertEquals(StdKeySerializers.Default.class, serializer.getClass());
 }

 /**
 * Test for getStdKeySerializer method when rawKeyType is Calendar
 */
 @Test
 public void testGetStdKeySerializerCalendarRawKeyType() {
 JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(null, Calendar.class, true);
 Assert.assertNotNull(serializer);
 Assert.assertEquals(StdKeySerializers.Default.class, serializer.getClass());
 }

 /**
 * Test for getStdKeySerializer method when rawKeyType is UUID
 */
 @Test
 public void testGetStdKeySerializerUUIDRawKeyType() {
 JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(null, java.util.UUID.class, true);
 Assert.assertNotNull(serializer);
 Assert.assertEquals(StdKeySerializers.Default.class, serializer.getClass());
 }

 /**
 * Test for getFallbackKeySerializer method when rawKeyType is Enum
 */
 @Test
 public void testGetFallbackKeySerializerEnumRawKeyType() {
 JsonSerializer<Object> serializer = StdKeySerializers.getFallbackKeySerializer(null, Enum.class);
 Assert.assertNotNull(serializer);
 Assert.assertEquals(StdKeySerializers.Dynamic.class, serializer.getClass());
 }

 /**
 * Test for getFallbackKeySerializer method when rawKeyType is not Enum
 */
 @Test
 public void testGetFallbackKeySerializerNonEnumRawKeyType() {
 JsonSerializer<Object> serializer = StdKeySerializers.getFallbackKeySerializer(null, String.class);
 Assert.assertEquals(StdKeySerializers.DEFAULT_KEY_SERIALIZER.class, serializer.getClass());
 }

 /**
 * Test for Dynamic key serializer
 */
 @Test
 public void testDynamicKeySerializer() throws JsonGenerationException, JsonMappingException, IOException {
 ObjectNode root = objectMapper.createObjectNode();
 root.put("dynamic", new MyCustomClass());
 StringJsonNode dynamicNode = (StringJsonNode) root.get("dynamic");
 JsonSerializer<Object> dynamicSerializer = StdKeySerializers.getStdKeySerializer(objectMapper.getSerializationConfig(), dynamicNode.asClass(), true);
 ObjectNode result = mapper.writeValueAsObject(new Object() {});
 JsonNode actualDynamicNode = result.get("dynamic");
 Assert.assertEquals(actualDynamicNode.getClass(), dynamicNode.getClass());
 Assert.assertEquals(actualDynamicNode.asText(), dynamicNode.asText());
 }

 /**
 * Test for Default key serializer for Date
 */
 @Test
 public void testDefaultSerializerForDate() throws JsonGenerationException, JsonMappingException, IOException {
 Date date = new Date();
 ObjectNode root = objectMapper.createObjectNode();
 root.put("date", date);
 ObjectNode result = mapper.writeValueAsObject(root);
 JsonNode actualDateNode = result.get("date");
 String expectedDateString = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ").format(date);
 Assert.assertEquals(actualDateNode.asText(), expectedDateString);
 }

 /**
 * Test for Default key serializer for Calendar
 */
 @Test
 public void testDefaultSerializerForCalendar() throws JsonGenerationException, JsonMappingException, IOException {
 Calendar calendar = Calendar.getInstance();
 ObjectNode root = objectMapper.createObjectNode();
 root.put("calendar", calendar);
 ObjectNode result = mapper.writeValueAsObject(root);
 JsonNode actualCalendarNode = result.get("calendar");
 String expectedCalendarString = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ").format(calendar.getTimeInMillis());
 Assert.assertEquals(actualCalendarNode.asText(), expectedCalendarString);
 }

 /**
 * Test for Default key serializer for Enum
 */
 @Test
 public void testDefaultSerializerForEnum() throws JsonGenerationException, JsonMappingException, IOException {
 EnumSet<MyCustomEnum> enumSet = EnumSet.allOf(MyCustomEnum.class);
 ObjectNode root = objectMapper.createObjectNode();
 root.put("enum", enumSet);
 ObjectNode result = mapper.writeValueAsObject(root);
 JsonNode actualEnumNode = result.get("enum");
 String expectedEnumString = actualEnumNode.toString();
 String expectedEnumStringWithQuotes = "'" + expectedEnumString.replaceAll("\\s", "") + "'";
 Assert.assertEquals(actualEnumNode.asText(), expectedEnumStringWithQuotes);
 }

 /**
 * Test for StringKeySerializer
 */
 @Test
 public void testStringKeySerializer() throws JsonGenerationException, JsonMappingException, IOException {
 ObjectNode root = objectMapper.createObjectNode();
 root.put("string", "testString");
 ObjectNode result = mapper.writeValueAsObject(root);
 JsonNode actualStringNode = result.get("string");
 Assert.assertEquals(actualStringNode.asText(), "testString");
 }
}