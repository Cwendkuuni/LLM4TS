import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.Test;
import static org.junit.Assert.*;

public class EnumSerializerTest {

 private static final ObjectMapper objectMapper = new ObjectMapper();

 @Test
 public void testSerializeEnumAsIndex() throws JsonProcessingException {
 EnumSerializer serializer = new EnumSerializer(EnumValues.allValues(MyEnum.class), true);
 Enum<?> enumValue = MyEnum.VALUE_1;
 JsonGenerator gen = new TestJsonGenerator();

 serializer.serialize(enumValue, gen, new SerializerProvider().builder().build());

 assertEquals("0", gen.getOutputContent().toString().trim());
 }

 @Test
 public void testSerializeEnumAsString() throws JsonProcessingException {
 EnumSerializer serializer = new EnumSerializer(EnumValues.allValues(MyEnum.class), false);
 Enum<?> enumValue = MyEnum.VALUE_1;
 JsonGenerator gen = new TestJsonGenerator();

 serializer.serialize(enumValue, gen, new SerializerProvider().builder().build());

 assertEquals("VALUE_1", gen.getOutputContent().toString().trim());
 }

 @Test
 public void testSerializeEnumWithCustomValues() throws JsonProcessingException {
 EnumSerializer serializer = new EnumSerializer(new EnumValues() {{
 add("CUSTOM_VALUE_1");
 add("CUSTOM_VALUE_2");
 }}, false);
 MyCustomEnum enumValue = MyCustomEnum.CUSTOM_VALUE_1;
 JsonGenerator gen = new TestJsonGenerator();

 serializer.serialize(enumValue, gen, new SerializerProvider().builder().build());

 assertEquals("CUSTOM_VALUE_1", gen.getOutputContent().toString().trim());
 }

 @Test(expected = JsonMappingException.class)
 public void testSerializeNullEnum() throws JsonProcessingException {
 EnumSerializer serializer = new EnumSerializer(EnumValues.allValues(MyEnum.class), false);
 Enum<?> enumValue = null;
 JsonGenerator gen = new TestJsonGenerator();

 serializer.serialize(enumValue, gen, new SerializerProvider().builder().build());
 }

 @Test
 public void testDeserializeEnumFromIndex() throws JsonProcessingException {
 ObjectNode input = objectMapper.createObjectNode();
 input.put("i", 0);
 JsonParser parser = objectMapper.getFactory().createParser(input.toString());
 EnumDeserializer deserializer = new EnumDeserializer(MyEnum.class);
 Enum<?> enumValue = deserializer.deserialize(parser, null, null);

 assertEquals(MyEnum.VALUE_1, enumValue);
 }

 @Test
 public void testDeserializeEnumFromString() throws JsonProcessingException {
 ObjectNode input = objectMapper.createObjectNode();
 input.put("s", "VALUE_1");
 JsonParser parser = objectMapper.getFactory().createParser(input.toString());
 EnumDeserializer deserializer = new EnumDeserializer(MyEnum.class);
 Enum<?> enumValue = deserializer.deserialize(parser, null, null);

 assertEquals(MyEnum.VALUE_1, enumValue);
 }

 @Test
 public void testDeserializeEnumFromCustomValues() throws JsonProcessingException {
 ObjectNode input = objectMapper.createObjectNode();
 input.put("s", "CUSTOM_VALUE_1");
 JsonParser parser = objectMapper.getFactory().createParser(input.toString());
 EnumDeserializer deserializer = new EnumDeserializer(MyCustomEnum.class);
 MyCustomEnum enumValue = (MyCustomEnum) deserializer.deserialize(parser, null, null);

 assertEquals(MyCustomEnum.CUSTOM_VALUE_1, enumValue);
 }

 @Test(expected = JsonMappingException.class)
 public void testDeserializeNullInput() throws JsonProcessingException {
 ObjectNode input = objectMapper.createObjectNode();
 JsonParser parser = objectMapper.getFactory().createParser(input.toString());
 EnumDeserializer deserializer = new EnumDeserializer(MyEnum.class);
 Enum<?> enumValue = deserializer.deserialize(parser, null, null);
 }
}

enum MyEnum {
 VALUE_1,
 VALUE_2,
 @JsonValue("CUSTOM_VALUE_1")
 CUSTOM_VALUE_1,
 @JsonValue("CUSTOM_VALUE_2")
 CUSTOM_VALUE_2;
}

enum MyCustomEnum {
 CUSTOM_VALUE_1,
 CUSTOM_VALUE_2;
}