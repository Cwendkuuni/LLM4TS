import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.Test;
import static org.junit.Assert.*;

public class StdKeySerializersTest {

 @Test
 public void testGetStdKeySerializer() throws JsonProcessingException {
 ObjectMapper mapper = new ObjectMapper();
 // Test with null rawKeyType
 JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(mapper.getSerializationConfig(), null, false);
 assertNull(serializer);

 // Test with String rawKeyType
 serializer = StdKeySerializers.getStdKeySerializer(mapper.getSerializationConfig(), String.class, false);
 assertEquals(StdKeySerializers.DEFAULT_STRING_SERIALIZER.getClass(), serializer.getClass());

 // Test with Number rawKeyType
 serializer = StdKeySerializers.getStdKeySerializer(mapper.getSerializationConfig(), Integer.class, false);
 assertEquals(StdKeySerializers.DEFAULT_KEY_SERIALIZER.getClass(), serializer.getClass());

 // Test with Class rawKeyType
 serializer = StdKeySerializers.getStdKeySerializer(mapper.getSerializationConfig(), Class.class, false);
 assertEquals(StdKeySerializers.Default.class.getName(), serializer.getClass().getName());

 // Test with Date rawKeyType
 Date date = new Date();
 serializer = StdKeySerializers.getStdKeySerializer(mapper.getSerializationConfig(), Date.class, false);
 assertEquals(StdKeySerializers.Default.class.getName(), serializer.getClass().getName());

 // Test with Calendar rawKeyType
 Calendar calendar = Calendar.getInstance();
 serializer = StdKeySerializers.getStdKeySerializer(mapper.getSerializationConfig(), Calendar.class, false);
 assertEquals(StdKeySerializers.Default.class.getName(), serializer.getClass().getName());

 // Test with Enum rawKeyType
 enum TestEnum { A, B }
 serializer = StdKeySerializers.getStdKeySerializer(mapper.getSerializationConfig(), TestEnum.class, false);
 assertEquals(StdKeySerializers.Default.TYPE_ENUM, serializer._typeId);

 // Test with UUID rawKeyType
 serializer = StdKeySerializers.getStdKeySerializer(mapper.getSerializationConfig(), java.util.UUID.class, false);
 assertEquals(StdKeySerializers.Default.TYPE_TO_STRING, serializer._typeId);
 }

 @Test
 public void testGetFallbackKeySerializer() throws JsonProcessingException {
 ObjectMapper mapper = new ObjectMapper();
 // Test with null rawKeyType
 JsonSerializer<Object> serializer = StdKeySerializers.getFallbackKeySerializer(mapper.getSerializationConfig(), null);
 assertNull(serializer);

 // Test with String rawKeyType
 serializer = StdKeySerializers.getFallbackKeySerializer(mapper.getSerializationConfig(), String.class);
 assertEquals(StdKeySerializers.DEFAULT_STRING_SERIALIZER.getClass(), serializer.getClass());

 // Test with Number rawKeyType
 serializer = StdKeySerializers.getFallbackKeySerializer(mapper.getSerializationConfig(), Integer.class);
 assertEquals(StdKeySerializers.DEFAULT_KEY_SERIALIZER.getClass(), serializer.getClass());

 // Test with Class rawKeyType
 serializer = StdKeySerializers.getFallbackKeySerializer(mapper.getSerializationConfig(), Class.class);
 assertEquals(StdKeySerializers.Default.class.getName(), serializer.getClass().getName());

 // Test with Date rawKeyType
 Date date = new Date();
 serializer = StdKeySerializers.getFallbackKeySerializer(mapper.getSerializationConfig(), Date.class);
 assertEquals(StdKeySerializers.Default.class.getName(), serializer.getClass().getName());

 // Test with Calendar rawKeyType
 Calendar calendar = Calendar.getInstance();
 serializer = StdKeySerializers.getFallbackKeySerializer(mapper.getSerializationConfig(), Calendar.class);
 assertEquals(StdKeySerializers.Default.class.getName(), serializer.getClass().getName());

 // Test with Enum rawKeyType
 enum TestEnum { A, B }
 serializer = StdKeySerializers.getFallbackKeySerializer(mapper.getSerializationConfig(), TestEnum.class);
 assertEquals(StdKeySerializers.Default.class.getName(), serializer.getClass().getName());
 }

 @Test
 public void testGetStdKeySerializerWithEnumType() throws JsonProcessingException {
 ObjectMapper mapper = new ObjectMapper();
 // Configure to write Enum keys as strings
 mapper.configure(SerializationFeature.WRITE_ENUMS_USING_TO_STRING, true);

 // Test with Enum rawKeyType
 enum TestEnum { A, B }
 JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(mapper.getSerializationConfig(), TestEnum.class, false);
 assertEquals(StdKeySerializers.Default.TYPE_ENUM, serializer._typeId);

 ObjectNode root = mapper.writeValueAsTree(new Object[] { TestEnum.A, TestEnum.B });
 JsonGenerator generator = mapper.getFactory().createGenerator();
 serializer.serialize(TestEnum.A, generator, mapper.getSerializationConfig());
 generator.flush();
 String jsonKeyA = generator.getOutputBuffer().toString();
 generator.close();

 generator = mapper.getFactory().createGenerator();
 serializer.serialize(TestEnum.B, generator, mapper.getSerializationConfig());
 generator.flush();
 String jsonKeyB = generator.getOutputBuffer().toString();

 // Check that jsonKeyA and jsonKeyB are equal to their corresponding enum names
 assertEquals(TestEnum.A.name(), jsonKeyA);
 assertEquals(TestEnum.B.name(), jsonKeyB);
 }

 @Test
 public void testGetStdKeySerializerWithDynamicKeyType() throws JsonProcessingException {
 ObjectMapper mapper = new ObjectMapper();
 // Create a map with dynamic key serializers
 PropertySerializerMap dynamicSerializers = new PropertySerializerMap();
 JsonSerializer<Object> serializer = new StdKeySerializers.Dynamic();
 dynamicSerializers.putSerializer(String.class, serializer);

 // Test serialization with dynamic key type
 ObjectNode root = mapper.readTree("{\"foo\": 1, \"bar\": 2}");
 Object value = root.get("foo");
 JsonSerializer<Object> foundSerializer = StdKeySerializers.getStdKeySerializer(mapper.getSerializationConfig(), value.getClass(), false);
 assertEquals(serializer.getClass(), foundSerializer.getClass());

 // Check serialization result
 JsonGenerator generator = mapper.getFactory().createGenerator();
 StdKeySerializers.Dynamic dynamicSerializer = (StdKeySerializers.Dynamic) foundSerializer;
 dynamicSerializer.serialize(value, generator, mapper.getSerializationConfig());
 String jsonKey = generator.getOutputBuffer().toString();
 assertEquals("foo", jsonKey);
 }
}