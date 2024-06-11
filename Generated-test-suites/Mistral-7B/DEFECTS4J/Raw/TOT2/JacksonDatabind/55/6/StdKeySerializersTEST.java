import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.ser.std.StdKeySerializers;
import org.junit.Test;
import static org.junit.Assert.*;

public class StdKeySerializersTest {

 // Basic test cases for getStdKeySerializer method
 @Test
 public void testGetStdKeySerializerWithValidArguments() throws JsonProcessingException {
 Object result = StdKeySerializers.getStdKeySerializer(new ObjectMapper(), String.class, false);
 assertEquals("Default String Serializer", StdKeySerializers.DEFAULT_STRING_SERIALIZER, result);
 }

 @Test
 public void testGetStdKeySerializerWithNullRawKeyType() throws JsonProcessingException {
 Object result = StdKeySerializers.getStdKeySerializer(new ObjectMapper(), null, false);
 assertNull("Should return null when rawKeyType is null", result);
 }

 // Edge cases and exception handling test cases for getStdKeySerializer method
 @Test(expected = IllegalArgumentException.class)
 public void testGetStdKeySerializerWithInvalidRawKeyType() throws JsonProcessingException {
 ObjectMapper mapper = new ObjectMapper();
 Class<?> invalidType = new Object(){}.getClass();
 Object result = StdKeySerializers.getStdKeySerializer(mapper, invalidType, false);
 fail("getStdKeySerializer should throw IllegalArgumentException for invalid rawKeyType");
 }

 @Test
 public void testGetStdKeySerializerWithUseDefaultTrue() throws JsonProcessingException {
 ObjectMapper mapper = new ObjectMapper();
 Object result = StdKeySerializers.getStdKeySerializer(mapper, Object.class, true);
 assertEquals("Default key serializer", StdKeySerializers.DEFAULT_KEY_SERIALIZER, result);
 }

 // Basic test cases for getFallbackKeySerializer method
 @Test
 public void testGetFallbackKeySerializerWithValidArguments() throws JsonProcessingException {
 Object result = StdKeySerializers.getFallbackKeySerializer(new ObjectMapper(), String.class);
 assertEquals("Default key serializer", StdKeySerializers.DEFAULT_KEY_SERIALIZER, result);
 }

 @Test
 public void testGetFallbackKeySerializerWithEnumType() throws JsonProcessingException {
 Object result = StdKeySerializers.getFallbackKeySerializer(new ObjectMapper(), Enum.class);
 assertEquals("Enum key serializer", new StdKeySerializers.Default(StdKeySerializers.Default.TYPE_ENUM, Enum.class), result);
 }

 // Edge cases and exception handling test cases for getFallbackKeySerializer method
 @Test(expected = IllegalArgumentException.class)
 public void testGetFallbackKeySerializerWithInvalidType() throws JsonProcessingException {
 ObjectMapper mapper = new ObjectMapper();
 Class<?> invalidType = new Object(){}.getClass();
 Object result = StdKeySerializers.getFallbackKeySerializer(mapper, invalidType);
 fail("getFallbackKeySerializer should throw IllegalArgumentException for invalid type");
 }

 // Test Dynamic class
 @Test
 public void testDynamicClassSerialization() throws JsonProcessingException {
 ObjectMapper mapper = new ObjectMapper();
 Object dynamicObj = new Object() {
 @JsonValue
 public String getValue() {
 return "DynamicObj";
 }
 };
 Object result = mapper.writeValueAsString(new ObjectNode(mapper.createObjectNode())
 .put("key", dynamicObj));
 assertEquals("{\"key\":\"DynamicObj\"}", result);
 }

 // Test Default class
 @Test
 public void testDefaultClassSerialization() throws JsonProcessingException {
 ObjectMapper mapper = new ObjectMapper();
 Calendar calendar = Calendar.getInstance();
 Date date = calendar.getTime();
 ObjectNode rootNode = mapper.createObjectNode();
 rootNode.put("dateKey", date);
 rootNode.put("calendarKey", calendar);
 Object result = mapper.writeValueAsString(rootNode);
 assertEquals("{\"calendarKey\":1467643550000,\"dateKey\":1467643550000}", result);
 }

 // Test StringKeySerializer class
 @Test
 public void testStringKeySerializer() throws JsonProcessingException {
 ObjectMapper mapper = new ObjectMapper();
 ObjectNode rootNode = mapper.createObjectNode();
 rootNode.put("stringKey", "StringKey");
 Object result = mapper.writeValueAsString(rootNode);
 assertEquals("{\"stringKey\":\"StringKey\"}", result);
 }

 // Test EnumKeySerializer class
 @Test
 public void testEnumKeySerializer() throws JsonProcessingException {
 ObjectMapper mapper = new ObjectMapper();
 Enum testEnum = Enum.valueOf(String.class, "TEST_ENUM_VALUE");
 ObjectNode rootNode = mapper.createObjectNode();
 rootNode.put("enumKey", testEnum);
 Object result = mapper.writeValueAsString(rootNode);
 assertEquals("{\"enumKey\":\"TEST_ENUM_VALUE\"}", result);
 }

}