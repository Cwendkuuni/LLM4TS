import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.*;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class StdKeySerializersTest {

 private ObjectMapper mapper;

 @BeforeClass
 public static void setupClass() {
 mapper = new ObjectMapper();
 }

 @Test
 public void testDefaultKeySerializer() throws JsonProcessingException, JsonGenerationException {
 Object key = new Date(System.currentTimeMillis());
 String expectedKey = "\""; // Jackson automatically serializes Date to ISO-formatted string
 JsonSerializer<Object> keySerializer = StdKeySerializers.getDefaultKeySerializer(mapper.getSerializationConfig(), null, false);
 String actualKey = keySerializer.serializeValuesAsStrings(new Object[] { key }, null, null)[0];
 assertEquals(expectedKey, actualKey);
 }

 @Test
 public void testStringKeySerializer() throws JsonProcessingException, JsonGenerationException {
 String key = "testKey";
 JsonSerializer<Object> keySerializer = StdKeySerializers.DEFAULT_STRING_SERIALIZER;
 String actualKey = keySerializer.serializeValuesAsStrings(new Object[] { key }, null, null)[0];
 assertEquals(key, actualKey);
 }

 @Test
 public void testDynamicKeySerializer() throws JsonProcessingException, JsonGenerationException {
 Object unknownKey = new Object() {
 };
 JsonSerializer<Object> keySerializer = StdKeySerializers.getDefaultKeySerializer(mapper.getSerializationConfig(), unknownKey.getClass(), false);
 assertEquals(unknownKey.getClass().getName(), keySerializer.serializeValuesAsStrings(new Object[] { unknownKey }, null, null)[0]);
 }

 @Test(expected = JsonMappingException.class)
 public void testMissingSerializerForPrimitiveType() throws JsonProcessingException, JsonGenerationException {
 int key = 1;
 JsonSerializer<Object> keySerializer = StdKeySerializers.getDefaultKeySerializer(mapper.getSerializationConfig(), int.class, false);
 keySerializer.serialize(key, mapper.getFactory().createGenerator(), mapper.getSerializerProvider());
 }

 @Test
 public void testCalendarKeySerializer() throws JsonProcessingException, JsonGenerationException {
 Calendar key = Calendar.getInstance();
 JsonSerializer<Object> keySerializer = StdKeySerializers.getStdKeySerializer(mapper.getSerializationConfig(), Calendar.class, false);
 String expectedKey = String.format("\"%s\"", key.getTimeInMillis());
 String actualKey = keySerializer.serializeValuesAsStrings(new Object[] { key }, null, null)[0];
 assertEquals(expectedKey, actualKey);
 }

 @Test
 public void testEnumKeySerializer() throws JsonProcessingException, JsonGenerationException {
 Enum testEnum = Enum.valueOf(MyEnum.class, "TEST");
 JsonSerializer<Object> keySerializer = StdKeySerializers.getFallbackKeySerializer(mapper.getSerializationConfig(), Enum.class);
 String expectedKey = "\"" + testEnum.name() + "\"";
 String actualKey = keySerializer.serializeValuesAsStrings(new Object[] { testEnum }, null, null)[0];
 assertEquals(expectedKey, actualKey);
 }

 @Test
 public void testClassKeySerializer() throws JsonProcessingException, JsonGenerationException {
 Class<?> keyType = String[].class;
 JsonSerializer<Object> keySerializer = StdKeySerializers.getStdKeySerializer(mapper.getSerializationConfig(), keyType, false);
 assertEquals(keyType.getName(), keySerializer.serializeValuesAsStrings(new Object[] { keyType }, null, null)[0]);
 }

}