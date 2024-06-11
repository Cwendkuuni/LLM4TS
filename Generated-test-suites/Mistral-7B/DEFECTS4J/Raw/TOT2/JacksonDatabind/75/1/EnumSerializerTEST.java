import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class EnumSerializerTest {

 private EnumSerializer serializer;
 private ObjectMapper mapper;

 @BeforeAll
 public void setup() throws JsonProcessingException {
 mapper = new ObjectMapper();
 serializer = mapper.findValueSerializer(Enum.class);
 }

 // Test for constructor with EnumValues
 @Test
 public void testEnumSerializerWithEnumValues() {
 EnumValues enumValues = EnumValues.allValues(MyEnum.class);
 EnumSerializer enumSerializer = new EnumSerializer(enumValues, null);
 assertNotNull(enumSerializer);
 }

 // Test for createContextual method
 @Test
 public void testCreateContextualMethod() throws JsonProcessingException {
 MyEnumProperty property = new MyEnumProperty();
 ObjectNode node = mapper.createObjectNode();
 node.put("myEnumProperty", property);
 JsonObjectEncoder encoder = mapper.treeAsEncoder(node);
 EnumSerializer enumSerializer = serializer.createContextual(mapper.getSerializerProvider(), property);
 assertEquals(MyEnum.VALUE1.name(), enumSerializer.serialize(MyEnum.VALUE1, new JsonGenerator(encoder), mapper.getSerializerProvider()).getValueAsString());
 }

 // Test for serialization with default configuration
 @Test
 public void testSerializationWithDefaultConfiguration() throws JsonProcessingException {
 MyEnum value = MyEnum.VALUE1;
 ObjectNode objectNode = mapper.createObjectNode();
 objectNode.put("myEnumField", value);
 JsonObjectEncoder encoder = mapper.treeAsEncoder(objectNode);
 JsonGenerator jsonGenerator = encoder.getUnderlyingGenerator();
 EnumSerializer enumSerializer = serializer; // use the default serializer instance
 String result = enumSerializer.serialize(value, jsonGenerator, mapper.getSerializerProvider()).getValueAsString();
 assertEquals(value.name(), result);
 }

 // Test for serialization with TO_STRING feature enabled
 @Test
 public void testSerializationWithToStringFeatureEnabled() throws JsonProcessingException {
 ObjectMapper mapperWithToStringFeature = new ObjectMapper(new Version("2.12.0"));
 mapperWithToStringFeature.enable(SerializationFeature.WRITE_ENUMS_USING_TO_STRING);
 MyEnum value = MyEnum.VALUE1;
 ObjectNode objectNode = mapperWithToStringFeature.createObjectNode();
 objectNode.put("myEnumField", value);
 JsonObjectEncoder encoder = mapperWithToStringFeature.treeAsEncoder(objectNode);
 JsonGenerator jsonGenerator = encoder.getUnderlyingGenerator();
 EnumSerializer enumSerializer = serializer; // use the default serializer instance
 String result = enumSerializer.serialize(value, jsonGenerator, mapperWithToStringFeature.getSerializerProvider()).getValueAsString();
 assertEquals(value.toString(), result);
 }

 // Test for serialization with index format
 @Test
 public void testSerializationWithIndexFormat() throws JsonProcessingException {
 MyEnum value = MyEnum.VALUE1;
 ObjectNode objectNode = mapper.createObjectNode();
 objectNode.put("myEnumField", value);
 JsonObjectEncoder encoder = mapper.treeAsEncoder(objectNode);
 JsonGenerator jsonGenerator = encoder.getUnderlyingGenerator();
 EnumSerializer enumSerializer = new EnumSerializer(EnumValues.constructFromClass(MyEnum.class), true);
 String result = enumSerializer.serialize(value, jsonGenerator, mapper.getSerializerProvider()).getValueAsString();
 assertEquals("0", result);
 }

 // Test for serialization with custom EnumValues
 @Test
 public void testSerializationWithCustomEnumValues() throws JsonProcessingException {
 MyEnumCustomValues enumValues = new MyEnumCustomValues();
 EnumSerializer enumSerializer = new EnumSerializer(enumValues, null);
 MyEnum value = MyEnum.VALUE1;
 ObjectNode objectNode = mapper.createObjectNode();
 objectNode.put("myEnumField", value);
 JsonObjectEncoder encoder = mapper.treeAsEncoder(objectNode);
 JsonGenerator jsonGenerator = encoder.getUnderlyingGenerator();
 String result = enumSerializer.serialize(value, jsonGenerator, mapper.getSerializerProvider()).getValueAsString();
 assertEquals(enumValues.getSerializedValueFor(value), result);
 }

 // Test for deserialization with default configuration
 @Test
 public void testDeserializationWithDefaultConfiguration() throws JsonProcessingException {
 String jsonString = "{\"myEnumField\":\"VALUE1\"}";
 ObjectNode objectNode = mapper.readValue(jsonString, ObjectNode.class);
 MyEnum value = mapper.readValue(objectNode, MyEnum.class);
 assertEquals(MyEnum.VALUE1, value);
 }

 // Test for deserialization with TO_STRING feature enabled
 @Test
 public void testDeserializationWithToStringFeatureEnabled() throws JsonProcessingException {
 String jsonString = "{\"myEnumField\":\"VALUE_STRING\"}";
 ObjectMapper mapperWithToStringFeature = new ObjectMapper(new Version("2.12.0"));
 mapperWithToStringFeature.enable(DeserializationFeature.READ_ENUMS_USING_TO_STRING);
 MyEnum value = mapperWithToStringFeature.readValue(jsonString, MyEnum.class);
 assertEquals(MyEnum.VALUE_STRING, value);
 }

 // Test for deserialization with index format
 @Test
 public void testDeserializationWithIndexFormat() throws JsonProcessingException {
 String jsonString = "{\"myEnumField\":0}";
 ObjectMapper mapperWithIndexFormat = new ObjectMapper();
 mapperWithIndexFormat.enable(DeserializationFeature.READ_ENUMS_AS_INT);
 MyEnum value = mapperWithIndexFormat.readValue(jsonString, MyEnum.class);
 assertEquals(MyEnum.VALUE1, value);
 }

 // Test for deserialization with custom EnumValues
 @Test
 public void testDeserializationWithCustomEnumValues() throws JsonProcessingException {
 String jsonString = "{\"myEnumField\":\"CUSTOM_VALUE\"}";
 ObjectMapper mapperWithCustomEnumValues = new ObjectMapper();
 MyEnumCustomValues enumValues = new MyEnumCustomValues();
 mapperWithCustomEnumValues.registerValueProcessor(MyEnum.class, new EnumValueProcessor(enumValues));
 MyEnum value = mapperWithCustomEnumValues.readValue(jsonString, MyEnum.class);
 assertEquals(MyEnum.CUSTOM_VALUE, value);
 }
}