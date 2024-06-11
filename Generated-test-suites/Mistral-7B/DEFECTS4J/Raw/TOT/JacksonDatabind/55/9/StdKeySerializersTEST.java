import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import static org.junit.Assert.*;

public class StdKeySerializersTest {

 private static final ObjectMapper mapper = new ObjectMapper();

 @Test
 public void testGetStdKeySerializer() throws JsonProcessingException {
 Class<?> rawKeyType = String.class;
 SerializationConfig config = new SerializationConfig();
 JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(config, rawKeyType, false);
 assertNotNull(serializer);
 assertEquals(StdKeySerializers.DEFAULT_STRING_SERIALIZER.getClass(), serializer.getClass());

 rawKeyType = Date.class;
 config = new SerializationConfig();
 serializer = StdKeySerializers.getStdKeySerializer(config, rawKeyType, false);
 assertNotNull(serializer);
 assertEquals(StdKeySerializers.Default.TYPE_DATE, serializer._typeId);

 rawKeyType = Calendar.class;
 config = new SerializationConfig();
 serializer = StdKeySerializers.getStdKeySerializer(config, rawKeyType, false);
 assertNotNull(serializer);
 assertEquals(StdKeySerializers.Default.TYPE_CALENDAR, serializer._typeId);

 rawKeyType = int.class;
 config = new SerializationConfig();
 serializer = StdKeySerializers.getStdKeySerializer(config, rawKeyType, false);
 assertNull(serializer);
 }

 @Test
 public void testGetFallbackKeySerializer() throws JsonProcessingException {
 Class<?> rawKeyType = String.class;
 SerializationConfig config = new SerializationConfig();
 JsonSerializer<Object> serializer = StdKeySerializers.getFallbackKeySerializer(config, rawKeyType);
 assertNotNull(serializer);
 assertEquals(StdKeySerializers.DEFAULT_STRING_SERIALIZER.getClass(), serializer.getClass());

 rawKeyType = Enum.class;
 config = new SerializationConfig();
 serializer = StdKeySerializers.getFallbackKeySerializer(config, rawKeyType);
 assertNotNull(serializer);
 assertEquals(StdKeySerializers.Default.TYPE_ENUM, serializer._typeId);

 rawKeyType = int.class;
 config = new SerializationConfig();
 serializer = StdKeySerializers.getFallbackKeySerializer(config, rawKeyType);
 assertNull(serializer);
 }

 @Test
 public void testDynamicKeySerializer() throws JsonProcessingException {
 SerializationConfig config = new SerializationConfig();
 ObjectMapper mapper = new ObjectMapper(config);
 ObjectMapper oldMapper = this.mapper;
 try {
 this.mapper = mapper;

 Object key = new Object() {
 @JsonValue
 public String value() {
 return "test";
 }
 };
 JsonSerializer<Object> serializer = mapper.getFactory().findValueSerializer(key.getClass(), null);
 assertNull(serializer);

 Class<?> keyClass = key.getClass();
 PropertySerializerMap serializers = mapper.getSerializerProvider().getSerializerMap();
 JsonSerializer<Object> dynamicSerializer = serializers.getSerializerProvider().getDynamicSerializer(keyClass, null);
 assertNotNull(dynamicSerializer);

 mapper.writeValueAsString(new ObjectNode().put("test", key));
 } finally {
 this.mapper = oldMapper;
 }
 }

 @Test
 public void testStringKeySerializer() throws JsonProcessingException {
 StringKeySerializer serializer = new StringKeySerializers.StringKeySerializer();
 ObjectMapper mapper = new ObjectMapper();
 mapper.registerSerializer(String.class, serializer);

 ObjectNode node = mapper.createObjectNode();
 node.put("test", "string key");
 String result = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(node);
 assertTrue(result.startsWith("\"test\":\""));
 assertTrue(result.endsWith("\",\n"));
 }

}