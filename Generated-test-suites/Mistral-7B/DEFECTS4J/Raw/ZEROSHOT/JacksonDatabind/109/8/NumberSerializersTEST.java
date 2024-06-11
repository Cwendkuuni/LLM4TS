import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import org.junit.Test;
import static org.junit.Assert.*;

public class NumberSerializersTest {

 @Test
 public void testNumberSerializers() throws Exception {
 ObjectMapper mapper = new ObjectMapper();

 // Test Integer serialization
 Integer intValue = 123;
 JsonNode intNode = mapper.valueToTree(intValue);
 assertEquals("123", intNode.asText());

 JsonNode intNodeWithType = mapper.valueToTree(intValue, new TypeIdentifierSerializer(IntType.class));
 assertEquals("123", intNodeWithType.get(0).asText());
 assertEquals("integer", intNodeWithType.get(1).asText());

 // Test IntegerSerializer contextual serialization
 ObjectMapper mapperWithContextualSerialization = new ObjectMapper(new Version(2, 11, 0));
 mapperWithContextualSerialization.registerModule(new SimpleModule()
 .setSerializerFactory(new JsonSerializer.ValueSerializerFactory() {
 @Override
 public JsonSerializer<?> createValueSerializer(SerializerProvider provider, Class<?> type) {
 if (type == Integer.class) {
 return new IntegerSerializer(Integer.class);
 }
 return null;
 }
 }));
 JavaType intType = mapperWithContextualSerialization.getTypeFactory().constructValueType(Integer.class);
 Integer contextualInt = 456;
 String intString = mapperWithContextualSerialization.writeValueAsString(contextualInt);
 assertEquals("456", intString);
 JsonNode intContextualNode = mapperWithContextualSerialization.readTree(intString);
 assertEquals("456", intContextualNode.asText());
 assertEquals("integer", intContextualNode.getClass().getSimpleName());

 // Test IntegerSerializer with JsonFormat annotation
 @JsonFormat(shape = JsonFormat.Shape.STRING)
 Integer jsonFormatInt = 789;
 String jsonFormatIntString = mapper.writeValueAsString(jsonFormatInt);
 assertEquals("789", jsonFormatIntString);
 JsonNode jsonFormatIntNode = mapper.readTree(jsonFormatIntString);
 assertEquals("789", jsonFormatIntNode.asText());

 // Test other number serializations
 Long longValue = 1L;
 Float floatValue = 1.2f;
 Double doubleValue = 3.14;

 // Test Long serialization
 JsonNode longNode = mapper.valueToTree(longValue);
 assertEquals("1", longNode.asText());

 // Test Float serialization
 FloatSerializer floatSerializer = mapper.findValueSerializer(Float.class);
 JsonNode floatNode = mapper.valueToTree(floatValue, floatSerializer);
 assertEquals("1.2", floatNode.asText());

 // Test Double serialization
 DoubleSerializer doubleSerializer = mapper.findValueSerializer(Double.class);
 JsonNode doubleNode = mapper.valueToTree(doubleValue, doubleSerializer);
 assertEquals("3.14", doubleNode.asText());
 }
}

class IntType extends SimpleType {
 @Override
 public String toString() {
 return "int";
 }
}