import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class NumberSerializersTest {

 // Test for addAll method
 @Test
 public void testAddAll() throws JsonProcessingException {
 ObjectMapper objectMapper = new ObjectMapper();
 Map<String, JsonSerializer<?>> allDeserializers = new HashMap<>();
 NumberSerializers.addAll(allDeserializers);

 ObjectNode jsonNode = objectMapper.createObjectNode();
 JsonParser jsonParser = objectMapper.getFactory().createParser(jsonNode);

 JsonDeserializer<Integer> intDeserializer = allDeserializers.get(Integer.class.getName());
 JsonDeserializer<Integer> intTypeDeserializer = allDeserializers.get(Integer.TYPE.getName());
 JsonDeserializer<Long> longDeserializer = allDeserializers.get(Long.class.getName());
 JsonDeserializer<Long> longTypeDeserializer = allDeserializers.get(Long.TYPE.getName());

 jsonParser.nextValue(); // Consume the root node

 assertEquals(intDeserializer, jsonParser.readValueAs(JsonDeserializer.class));
 assertEquals(intTypeDeserializer, jsonParser.readValueAs(JsonDeserializer.class));
 assertEquals(longDeserializer, jsonParser.readValueAs(JsonDeserializer.class));
 assertEquals(longTypeDeserializer, jsonParser.readValueAs(JsonDeserializer.class));
 }

 // Test for IntegerSerializer
 @Test
 public void testIntegerSerializer() throws JsonProcessingException {
 ObjectMapper objectMapper = new ObjectMapper();
 IntegerSerializer integerSerializer = new IntegerSerializer(Integer.class);

 ObjectNode jsonNode = objectMapper.createObjectNode();
 JsonParser jsonParser = objectMapper.getFactory().createParser(jsonNode);

 jsonParser.setCodec(objectMapper);
 jsonParser.setContext(new ObjectCodecContext(objectMapper.getSerializationConfig()));
 jsonParser.nextValue(JsonParser.CurrentName.FIELD_NAME); // Consume the field name
 jsonParser.nextValue(JsonParser.CurrentName.FIELD_NAME); // Consume the second field name
 jsonParser.nextValue(JsonParser.CurrentName.FIELD_NAME); // Consume the third field name

 jsonParser.nextValue(JsonParser.Current.VALUE_NUMBER_INT); // Consume an integer value
 jsonParser.nextValue(JsonParser.Current.VALUE_TRUE); // Consume a boolean value indicating serialization

 JsonSerializer<?> serializer = integerSerializer.createContextual(null, null);
 ObjectNode serializedNode = JsonNodeFactory.instance.objectNode();
 serializer.serializeValues(serializer, new ObjectWriter(objectMapper), jsonParser, serializedNode);

 int expectedValue = 42;
 ObjectNode expectedNode = objectMapper.createObjectNode();
 expectedNode.put("intValue", expectedValue);

 assertEquals(expectedNode, serializedNode);
 }

 // Test for IntLikeSerializer
 @Test
 public void testIntLikeSerializer() throws JsonProcessingException {
 ObjectMapper objectMapper = new ObjectMapper();
 IntLikeSerializer intLikeSerializer = IntLikeSerializer.instance;

 ObjectNode jsonNode = objectMapper.createObjectNode();
 JsonParser jsonParser = objectMapper.getFactory().createParser(jsonNode);

 jsonParser.setCodec(objectMapper);
 jsonParser.setContext(new ObjectCodecContext(objectMapper.getSerializationConfig()));
 jsonParser.nextValue(JsonParser.CurrentName.FIELD_NAME); // Consume the field name
 jsonParser.nextValue(JsonParser.CurrentName.FIELD_NAME); // Consume the second field name
 jsonParser.nextValue(JsonParser.Current.VALUE_NUMBER_INT); // Consume an integer value

 JsonSerializer<?> serializer = intLikeSerializer.createContextual(null, null);
 ObjectNode serializedNode = JsonNodeFactory.instance.objectNode();
 serializer.serializeValues(serializer, new ObjectWriter(objectMapper), jsonParser, serializedNode);

 int expectedValue = 42;
 ObjectNode expectedNode = objectMapper.createObjectNode();
 expectedNode.put("intValue", expectedValue);

 assertEquals(expectedNode, serializedNode);
 }

 // Test for LongSerializer
 @Test
 public void testLongSerializer() throws JsonProcessingException {
 ObjectMapper objectMapper = new ObjectMapper();
 LongSerializer longSerializer = new LongSerializer(Long.class);

 ObjectNode jsonNode = objectMapper.createObjectNode();
 JsonParser jsonParser = objectMapper.getFactory().createParser(jsonNode);

 jsonParser.setCodec(objectMapper);
 jsonParser.setContext(new ObjectCodecContext(objectMapper.getSerializationConfig()));
 jsonParser.nextValue(JsonParser.CurrentName.FIELD_NAME); // Consume the field name
 jsonParser.nextValue(JsonParser.CurrentName.FIELD_NAME); // Consume the second field name
 jsonParser.nextValue(JsonParser.Current.VALUE_NUMBER_LONG); // Consume a long value

 JsonSerializer<?> serializer = longSerializer.createContextual(null, null);
 ObjectNode serializedNode = JsonNodeFactory.instance.objectNode();
 serializer.serializeValues(serializer, new ObjectWriter(objectMapper), jsonParser, serializedNode);

 long expectedValue = 42L;
 ObjectNode expectedNode = objectMapper.createObjectNode();
 expectedNode.put("longValue", expectedValue);

 assertEquals(expectedNode, serializedNode);
 }

 // Test for FloatSerializer
 @Test
 public void testFloatSerializer() throws JsonProcessingException {
 ObjectMapper objectMapper = new ObjectMapper();
 FloatSerializer floatSerializer = FloatSerializer.instance;

 ObjectNode jsonNode = objectMapper.createObjectNode();
 JsonParser jsonParser = objectMapper.getFactory().createParser(jsonNode);

 jsonParser.setCodec(objectMapper);
 jsonParser.setContext(new ObjectCodecContext(objectMapper.getSerializationConfig()));
 jsonParser.nextValue(JsonParser.CurrentName.FIELD_NAME); // Consume the field name
 jsonParser.nextValue(JsonParser.CurrentName.FIELD_NAME); // Consume the second field name
 jsonParser.nextValue(JsonParser.Current.VALUE_NUMBER_FLOAT); // Consume a float value

 JsonSerializer<?> serializer = floatSerializer.createContextual(null, null);
 ObjectNode serializedNode = JsonNodeFactory.instance.objectNode();
 serializer.serializeValues(serializer, new ObjectWriter(objectMapper), jsonParser, serializedNode);

 float expectedValue = 42.0f;
 ObjectNode expectedNode = objectMapper.createObjectNode();
 expectedNode.put("floatValue", expectedValue);

 assertEquals(expectedNode, serializedNode);
 }

 // Test for DoubleSerializer
 @Test
 public void testDoubleSerializer() throws JsonProcessingException {
 ObjectMapper objectMapper = new ObjectMapper();
 DoubleSerializer doubleSerializer = DoubleSerializer.instance;

 ObjectNode jsonNode = objectMapper.createObjectNode();
 JsonParser jsonParser = objectMapper.getFactory().createParser(jsonNode);

 jsonParser.setCodec(objectMapper);
 jsonParser.setContext(new ObjectCodecContext(objectMapper.getSerializationConfig()));
 jsonParser.nextValue(JsonParser.CurrentName.FIELD_NAME); // Consume the field name
 jsonParser.nextValue(JsonParser.CurrentName.FIELD_NAME); // Consume the second field name
 jsonParser.nextValue(JsonParser.Current.VALUE_NUMBER_FLOAT); // Consume a double value

 JsonSerializer<?> serializer = doubleSerializer.createContextual(null, null);
 ObjectNode serializedNode = JsonNodeFactory.instance.objectNode();
 serializer.serializeValues(serializer, new ObjectWriter(objectMapper), jsonParser, serializedNode);

 double expectedValue = 42.0;
 ObjectNode expectedNode = objectMapper.createObjectNode();
 expectedNode.put("doubleValue", expectedValue);

 assertEquals(expectedNode, serializedNode);
 }
}