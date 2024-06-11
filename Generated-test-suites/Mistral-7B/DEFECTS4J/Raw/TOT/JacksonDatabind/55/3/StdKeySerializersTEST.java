import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.*;
import org.junit.Test;

import static org.junit.Assert.*;

public class StdKeySerializersTest {

 private JsonFactory factory = new JsonFactory();

 @Test
 public void testGetStdKeySerializer() throws JsonProcessingException {
 ObjectMapper mapper = new ObjectMapper();
 SerializationConfig config = mapper.getSerializationConfig();

 // Test with null rawKeyType
 JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(config, null, false);
 assertNull(serializer);

 // Test with String rawKeyType
 serializer = StdKeySerializers.getStdKeySerializer(config, String.class, false);
 assertEquals(StdKeySerializers.DEFAULT_STRING_SERIALIZER, serializer);

 // Test with Number rawKeyType
 serializer = StdKeySerializers.getStdKeySerializer(config, int.class, false);
 assertEquals(StdKeySerializers.DEFAULT_KEY_SERIALIZER, serializer);

 // Test with Class rawKeyType
 serializer = StdKeySerializers.getStdKeySerializer(config, String[].class, false);
 assertEquals(StdKeySerializers.DEFAULT_KEY_SERIALIZER, serializer);

 // Test with Date rawKeyType
 serializer = StdKeySerializers.getStdKeySerializer(config, Date.class, false);
 assertEquals(StdKeySerializers.DEFAULT_TYPE_DATE, serializer._typeId);

 // Test with Calendar rawKeyType
 serializer = StdKeySerializers.getStdKeySerializer(config, Calendar.class, false);
 assertEquals(StdKeySerializers.DEFAULT_TYPE_CALENDAR, serializer._typeId);

 // Test with UUID rawKeyType
 serializer = StdKeySerializers.getStdKeySerializer(config, java.util.UUID.class, false);
 assertEquals(StdKeySerializers.DEFAULT_TYPE_TO_STRING, serializer._typeId);
 }

 @Test
 public void testGetFallbackKeySerializer() throws JsonProcessingException {
 ObjectMapper mapper = new ObjectMapper();
 SerializationConfig config = mapper.getSerializationConfig();

 // Test with null rawKeyType
 assertNull(StdKeySerializers.getFallbackKeySerializer(config, null));

 // Test with String rawKeyType
 JsonSerializer<Object> serializer = StdKeySerializers.getFallbackKeySerializer(config, String.class);
 assertEquals(StdKeySerializers.DEFAULT_STRING_SERIALIZER, serializer);

 // Test with Number rawKeyType
 serializer = StdKeySerializers.getFallbackKeySerializer(config, int.class);
 assertEquals(StdKeySerializers.DEFAULT_KEY_SERIALIZER, serializer);

 // Test with Class rawKeyType
 serializer = StdKeySerializers.getFallbackKeySerializer(config, String[].class);
 assertEquals(StdKeySerializers.DEFAULT_KEY_SERIALIZER, serializer);

 // Test with Date rawKeyType
 serializer = StdKeySerializers.getFallbackKeySerializer(config, Date.class);
 assertEquals(StdKeySerializers.DEFAULT_TYPE_DATE, serializer._typeId);

 // Test with Calendar rawKeyType
 serializer = StdKeySerializers.getFallbackKeySerializer(config, Calendar.class);
 assertEquals(StdKeySerializers.DEFAULT_TYPE_CALENDAR, serializer._typeId);

 // Test with Enum rawKeyType
 serializer = StdKeySerializers.getFallbackKeySerializer(config, Enum.class);
 assertEquals(StdKeySerializers.Dynamic.class, serializer.getClass());
 }

 @Test
 public void testDynamicSerializer() throws JsonProcessingException {
 ObjectMapper mapper = new ObjectMapper();
 SerializationConfig config = mapper.getSerializationConfig();

 // Create dynamic serializer
 JsonSerializer<Object> serializer = StdKeySerializers.getFallbackKeySerializer(config, Object.class);

 // Test serialization with known types
 JsonGenerator generator = factory.createGenerator(new StringWriter());
 serializer.serialize(new Date(), generator, config);
 generator.flush();
 assertEquals("\"2022-12-31T12:00:00.000Z\"", generator.getOutputContent().toString());

 serializer.serialize(new Enum("TEST"), generator, config);
 generator.flush();
 assertEquals("\"TEST\"", generator.getOutputContent().toString());

 // Test serialization with unknown type
 serializer.serialize(new UnknownClass("TEST"), generator, config);
 generator.flush();
 assertNotNull(generator.getOutputContent().toString());
 }
}