import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.impl.DefaultSerializerProvider;
import com.fasterxml.jackson.databind.ser.impl.PropertySerializerMap;

import java.io.IOException;
import java.time.Instant;
import java.util.*;

import static org.junit.Assert.*;

public class StdKeySerializersTest {

 private static final ObjectMapper MAPPER = new ObjectMapper();

 @SuppressWarnings("unchecked")
 @Test
 public void testGetStdKeySerializer() throws IOException {
 String input = "{\"foo\": 1, \"bar\": \"hello\", \"baz\": true, \"qux\": [], \"quux\": null, \"date\": \"2022-01-01\", \"calendar\": {\"year\": 2022, \"month\": 1, \"dayOfMonth\": 1, \"hour\": 12, \"minute\": 30, \"second\": 0, \"millisOfSecond\": 0}, \"enum\": ENUM_VALUE, \"class\": String.class, \"uuid\": \"123e4567-e89b-12d3-a456-426614174000\", \"custom\": CustomClass.class}";
 Map<String, Object> inputMap = MAPPER.readValue(input, Map.class);
 SerializationConfig config = MAPPER.getSerializationConfig();

 Class<?> rawKeyType;
 Object result;
 for (Map.Entry<String, Object> entry : inputMap.entrySet()) {
 rawKeyType = entry.getValue().getClass();
 result = StdKeySerializers.getStdKeySerializer(config, rawKeyType, false);
 assertTrue(result instanceof JsonSerializer<?>);

 switch (rawKeyType.getTypeName()) {
 case "int":
 assertSame(StdKeySerializers.DEFAULT_KEY_SERIALIZER, result);
 break;
 case "java.lang.String":
 assertSame(StdKeySerializers.DEFAULT_STRING_SERIALIZER, result);
 break;
 case "boolean":
 assertSame(StdKeySerializers.DEFAULT_KEY_SERIALIZER, result);
 break;
 case "java.util.Date":
 assertSame(StdKeySerializers.Default.TYPE_DATE, result.getClass().getField("_typeId").getInt(result));
 break;
 case "java.util.Calendar":
 assertSame(StdKeySerializers.Default.TYPE_CALENDAR, result.getClass().getField("_typeId").getInt(result));
 break;
 case "java.lang.Class":
 assertSame(StdKeySerializers.Default.TYPE_CLASS, result.getClass().getField("_typeId").getInt(result));
 break;
 case "java.util.Enum":
 assertSame(StdKeySerializers.Default.TYPE_ENUM, result.getClass().getField("_typeId").getInt(result));
 break;
 case "java.util.UUID":
 assertSame(StdKeySerializers.Default.TYPE_TO_STRING, result.getClass().getField("_typeId").getInt(result));
 break;
 case "java.lang.Enum":
 case "com.example.CustomClass":
 assertNotNull(result);
 break;
 default:
 fail("Unexpected key type: " + rawKeyType.getTypeName());
 }
 }
 }

 @Test
 public void testGetFallbackKeySerializer() throws IOException {
 String input = "{\"foo\": 1, \"bar\": null, \"baz\": null, \"qux\": [], \"quux\": null, \"date\": null, \"calendar\": null, \"enum\": null, \"class\": null, \"uuid\": null, \"custom\": null}";
 Map<String, Object> inputMap = MAPPER.readValue(input, Map.class);
 SerializationConfig config = MAPPER.getSerializationConfig();

 Class<?> rawKeyType;
 Object result;
 for (Map.Entry<String, Object> entry : inputMap.entrySet()) {
 rawKeyType = entry.getValue() == null ? Object.class : entry.getValue().getClass();
 result = StdKeySerializers.getFallbackKeySerializer(config, rawKeyType);
 assertTrue(result instanceof JsonSerializer<?>);
 }
 }

 @Test
 public void testGetDefault() throws IOException {
 assertSame(StdKeySerializers.DEFAULT_KEY_SERIALIZER, StdKeySerializers.getDefault());
 }

 @Test(expected = DeprecationException.class)
 public void testGetDefaultDeprecated() {
 StdKeySerializers.getDefault();
 }

 @Test
 public void testDynamicKeySerializer() throws IOException {
 SerializationConfig config = MAPPER.getSerializationConfig();
 Map<String, Object> inputMap = new HashMap<>();
 inputMap.put("customObject", new CustomObject());
 PropertySerializerMap map = new PropertySerializerMap();
 JsonSerializer<Object> serializer = StdKeySerializers.getFallbackKeySerializer(config, Map.class);
 map.serializerFor("customObject", new DefaultSerializerProvider(), null);
 serializer = (JsonSerializer<Object>) map.serializerFor(CustomObject.class, new DefaultSerializerProvider(), null);
 assertTrue(serializer instanceof Dynamic);
 }

 @Test
 public void testStringKeySerializer() throws IOException {
 String key = "test";
 JsonGenerator g = MAPPER.getFactory().createGenerator();
 StdKeySerializers.StringKeySerializer serializer = new StdKeySerializers.StringKeySerializer();
 serializer.serialize(key, g, new DefaultSerializerProvider());
 assertEquals(key, g.getOutputShippedRootValueAsString());
 }

 @Test
 public void testEnumKeySerializer() throws IOException {
 Enum testEnum = Enum.valueOf(CustomEnum.class, "TEST");
 JsonGenerator g = MAPPER.getFactory().createGenerator();
 StdKeySerializers.Default enumSerializer = new StdKeySerializers.Default(StdKeySerializers.Default.TYPE_ENUM, CustomEnum.class);
 enumSerializer.serialize(testEnum, g, new DefaultSerializerProvider());
 assertEquals(testEnum.name(), g.getOutputString());
 }

 @Test
 public void testDateKeySerializer() throws IOException {
 Date date = new Date();
 JsonGenerator g = MAPPER.getFactory().createGenerator();
 StdKeySerializers.Default dateSerializer = new StdKeySerializers.Default(StdKeySerializers.Default.TYPE_DATE, Date.class);
 dateSerializer.serialize(date, g, new DefaultSerializerProvider());
 assertEquals(date.toString(), g.getOutputString());
 }

 @Test
 public void testCalendarKeySerializer() throws IOException {
 Calendar calendar = Calendar.getInstance();
 JsonGenerator g = MAPPER.getFactory().createGenerator();
 Instant instant = Instant.now();
 TypeSerializer timeSerializer = MAPPER.getTypeSerializer();
 StdKeySerializers.Default calendarSerializer = new StdKeySerializers.Default(StdKeySerializers.Default.TYPE_CALENDAR, Calendar.class);
 calendarSerializer.serializeWithType(calendar, g, new DefaultSerializerProvider(), timeSerializer);
 assertEquals(String.format("%tF %<tT%z", calendar), g.getOutputString());
 }
}

class CustomClass {}
enum CustomEnum { TEST, VALUE }
class CustomObject {}