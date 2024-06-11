import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonObjectNode;
import com.fasterxml.jackson.databind.ser.std.NumberSerializers;
import org.junit.Test;
import static org.junit.Assert.*;

public class NumberSerializersTest {

 @Test
 public void testNumberSerializers() throws JsonProcessingException, JsonGenerationException {
 // Testing NumberSerializers.addAll() method
 ObjectMapper mapper = new ObjectMapper();
 Map<String, JsonSerializer<?>> serializers = new HashMap<>();
 NumberSerializers.addAll(serializers, mapper.getSerializationConfig().getDefaultSerializerProvider());

 // Testing IntegerSerializer
 Object integer = 123;
 JsonSerializer<Object> intSerializer = serializers.get(Integer.class.getName());
 JsonNode serializedInt = mapNode(intSerializer, integer);
 assertEquals("123", serializedInt.asText());

 // Testing IntegerSerializer with JsonFormat annotation
 @JsonFormat(shape = JsonFormat.Shape.STRING)
 Integer formattedInt = 456;
 JsonNode serializedFormattedInt = mapNode(intSerializer, formattedInt);
 assertEquals("\"456\"", serializedFormattedInt.asText());

 // Testing LongSerializer
 Object longValue = 987654321L;
 JsonSerializer<Object> longSerializer = serializers.get(Long.class.getName());
 JsonNode serializedLong = mapNode(longSerializer, longValue);
 assertEquals("987654321", serializedLong.asText());

 // Testing FloatSerializer
 Object floatValue = 123.45f;
 JsonSerializer<Object> floatSerializer = serializers.get(Float.class.getName());
 JsonNode serializedFloat = mapNode(floatSerializer, floatValue);
 assertEquals("123.45", serializedFloat.asText());

 // Testing DoubleSerializer
 Object doubleValue = 123.456789;
 JsonSerializer<Object> doubleSerializer = serializers.get(Double.class.getName());
 JsonNode serializedDouble = mapNode(doubleSerializer, doubleValue);
 assertEquals("123.456789", serializedDouble.asText());

 // Testing ShortSerializer
 Object shortValue = (short) 123;
 JsonSerializer<Object> shortSerializer = serializers.get(Short.class.getName());
 JsonNode serializedShort = mapNode(shortSerializer, shortValue);
 assertEquals("123", serializedShort.asText());

 // Testing ByteSerializer (delegated to IntLikeSerializer)
 Object byteValue = (byte) 127;
 JsonSerializer<Object> byteSerializer = serializers.get(Byte.class.getName());
 JsonNode serializedByte = mapNode(byteSerializer, byteValue);
 assertEquals("127", serializedByte.asText());
 }

 // Helper method to serialize and deserialize objects using given serializer
 private JsonNode mapNode(JsonSerializer<Object> serializer, Object value)
 throws JsonProcessingException, JsonGenerationException {
 ObjectWriter writer = new ObjectMapper().writerWithDefaultPrettyPrintingStyle();
 ObjectNode rootNode = writer.writeValueAsObject(value);
 JsonSerializer<ObjectNode> nodeSerializer = new DefaultSerializerProvider().findValueSerializer(ObjectNode.class, null);
 return nodeSerializer.serializeValues(new ObjectMapper().readTree(rootNode.traverse()), new ObjectWriter(new ByteArrayOutputStream()));
 }
}