import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.EnumDeserializer;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class EnumSerializerTest {

 private EnumSerializer serializer;
 private EnumValues enumValues;
 private ObjectMapper objectMapper;

 @Before
 public void setup() {
 ObjectMapper mapper = new ObjectMapper();
 enumValues = EnumValues.valuesOf(MyEnum.class);
 serializer = new EnumSerializer(enumValues, true);
 objectMapper = mapper.builder().addSerializer(MyEnum.class, serializer).build();
 }

 @Test
 public void testSerializeAsIndex() throws JsonProcessingException {
 Enum<MyEnum> enumValue = MyEnum.VALUE_1;
 JsonGenerator jsonGenerator = objectMapper.getFactory().createGenerator(new byte[0], objectMapper.getOutputFactory());
 serializer.serialize(enumValue, jsonGenerator, objectMapper.getSerializerProvider());
 jsonGenerator.flush();
 String serializedValue = jsonGenerator.getOutputContent();
 assertEquals("0", serializedValue);
 }

 @Test
 public void testSerializeAsString() throws JsonProcessingException {
 Enum<MyEnum> enumValue = MyEnum.VALUE_1;
 JsonGenerator jsonGenerator = objectMapper.getFactory().createGenerator(new byte[0], objectMapper.getOutputFactory());
 JsonSerializer<Enum<MyEnum>> jsonSerializer = objectMapper.getSerializerProvider().findValueSerializer(MyEnum.class, null);
 jsonSerializer.serialize(enumValue, jsonGenerator, objectMapper.getSerializerProvider());
 jsonGenerator.flush();
 String serializedValue = jsonGenerator.getOutputContent();
 assertEquals("VALUE_1", serializedValue);
 }

 @Test
 public void testGetSchema() throws JsonProcessingException {
 JsonNode schema = serializer.getSchema(objectMapper.getSerializerProvider(), MyEnum.class.getType());
 ObjectNode objectNode = (ObjectNode) schema;
 ArrayNode enumNode = objectNode.path("enum");
 assertEquals(2, enumNode.size());
 assertEquals("VALUE_0", enumNode.get(0).asText());
 assertEquals("VALUE_1", enumNode.get(1).asText());
 }

 @Test
 public void testAcceptJsonFormatVisitor() throws JsonProcessingException {
 ObjectNode rootNode = objectMapper.readValue("{\"enum\": [\"VALUE_0\", \"VALUE_1\"]}", new TypeReference<ObjectNode>() {});
 JsonFormatVisitorWrapper visitorWrapper = objectMapper.treeAsTokens(rootNode);
 JsonFormatVisitor jsonFormatVisitor = visitorWrapper.expectStringFormat(MyEnum.class.getType());
 SerializerProvider serializerProvider = visitorWrapper.getProvider();
 serializer = new EnumSerializer(enumValues, false);
 serializer.acceptJsonFormatVisitor(jsonFormatVisitor, MyEnum.class.getType());
 assertEquals(MyEnum.class, jsonFormatVisitor.getCurrentType());
 }

 @Test
 public void testCreateContextual() throws JsonProcessingException {
 ObjectMapper mapper = new ObjectMapper();
 SerializerProvider serializerProvider = mapper.getSerializerProvider();
 JsonFormat.Value format = new JsonFormat.Value();
 format.setShape(JsonFormat.Shape.STRING);
 JsonSerializer<Enum<MyEnum>> jsonSerializer = serializerProvider.findValueSerializer(MyEnum.class, null);
 EnumSerializer contextualSerializer = jsonSerializer.createContextual(serializerProvider, null);
 assertEquals(contextualSerializer.getClass(), EnumSerializer.class);
 assertTrue(contextualSerializer._serializeAsIndex.booleanValue());
 }

 @Test
 public void testDeserialize() throws JsonProcessingException {
 String json = "\"VALUE_1\"";
 ObjectMapper mapper = new ObjectMapper();
 EnumDeserializer deserializer = (EnumDeserializer) mapper.getDeserializationConfig().findValueDeserializer(MyEnum.class, null);
 MyEnum deserializedEnum = deserializer.deserialize(json, MyEnum.class, null);
 assertEquals(MyEnum.VALUE_1, deserializedEnum);
 }

 @Test(expected = JsonMappingException.class)
 public void testDeserializeWithInvalidValue() throws JsonProcessingException {
 String json = "\"INVALID_VALUE\"";
 ObjectMapper mapper = new ObjectMapper();
 EnumDeserializer deserializer = (EnumDeserializer) mapper.getDeserializationConfig().findValueDeserializer(MyEnum.class, null);
 deserializer.deserialize(json, MyEnum.class, null);
 }
}

 enum MyEnum {
 @JsonFormat(shape = JsonFormat.Shape.STRING, value = "VALUE_0")
 VALUE_0,
 @JsonFormat(shape = JsonFormat.Shape.STRING, value = "VALUE_1")
 VALUE_1;
}