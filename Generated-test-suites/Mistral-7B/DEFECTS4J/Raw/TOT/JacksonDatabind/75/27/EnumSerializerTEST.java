import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.std.EnumSerializer;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

public class EnumSerializerTest {

 private static final ObjectMapper MAPPER = new ObjectMapper();

 @Test
 public void testSerializeEnum() throws JsonMappingException, IOException {
 EnumSerializer serializer = new EnumSerializer(EnumValues.valuesOf(MyEnum.class));

 MyEnum enumValue = MyEnum.VALUE_1;
 JsonGenerator gen = MAPPER.getFactory().createGenerator(new ByteArrayOutputStream(), null);
 serializer.serialize(enumValue, gen, null);
 gen.flush();
 ByteArrayOutputStream jsonBytes = (ByteArrayOutputStream) gen.getOutput();
 ObjectNode jsonNode = MAPPER.readTree(jsonBytes.toByteArray());
 assertEquals("VALUE_1", jsonNode.get("$value").asText());
 }

 @Test
 public void testSerializeEnumWithCustomValues() throws JsonMappingException, IOException {
 EnumSerializer serializer = new EnumSerializer(new EnumValues() {{
 addEnumValue("CUSTOM_VALUE_1", MyEnum.CUSTOM_VALUE_1);
 addEnumValue("CUSTOM_VALUE_2", MyEnum.CUSTOM_VALUE_2);
 }}, MyEnum.class, true);

 MyEnum enumValue = MyEnum.CUSTOM_VALUE_1;
 JsonGenerator gen = MAPPER.getFactory().createGenerator(new ByteArrayOutputStream(), null);
 serializer.serialize(enumValue, gen, null);
 gen.flush();
 ByteArrayOutputStream jsonBytes = (ByteArrayOutputStream) gen.getOutput();
 ObjectNode jsonNode = MAPPER.readTree(jsonBytes.toByteArray());
 assertEquals("CUSTOM_VALUE_1", jsonNode.get("$value").asText());
 }

 @Test
 public void testSerializeEnumWithToString() throws JsonMappingException, IOException {
 SerializerProvider serializers = MAPPER.getSerializerProvider();
 EnumSerializer serializer = serializer.findValueSerializer(MyEnum.class, serializers);
 serializer = (EnumSerializer) serializer;
 serializers.setEnabled(SerializationFeature.WRITE_ENUMS_USING_TO_STRING, true);
 MyEnum enumValue = MyEnum.VALUE_1;
 JsonGenerator gen = MAPPER.getFactory().createGenerator(new ByteArrayOutputStream(), null);
 serializer.serialize(enumValue, gen, serializers);
 gen.flush();
 ByteArrayOutputStream jsonBytes = (ByteArrayOutputStream) gen.getOutput();
 ObjectNode jsonNode = MAPPER.readTree(jsonBytes.toByteArray());
 assertEquals("VALUE_1.VALUE_1", jsonNode.get("$value").asText());
 }

 @Test
 public void testSerializeEnumWithOrdinal() throws JsonMappingException, IOException {
 SerializerProvider serializers = MAPPER.getSerializerProvider();
 EnumSerializer serializer = serializer.findValueSerializer(MyEnum.class, serializers);
 serializer = (EnumSerializer) serializer;
 serializers.setEnabled(SerializationFeature.WRITE_ENUMS_USING_TO_STRING, false);
 MyEnum enumValue = MyEnum.VALUE_1;
 JsonGenerator gen = MAPPER.getFactory().createGenerator(new ByteArrayOutputStream(), null);
 serializer.serialize(enumValue, gen, serializers);
 gen.flush();
 ByteArrayOutputStream jsonBytes = (ByteArrayOutputStream) gen.getOutput();
 ObjectNode jsonNode = MAPPER.readTree(jsonBytes.toByteArray());
 assertEquals("0", jsonNode.get("$value").asText());
 }

 @Test
 public void testSerializeEnumWithNull() throws JsonMappingException, IOException {
 EnumSerializer serializer = new EnumSerializer(EnumValues.valuesOf(MyEnum.class));
 MyEnum enumValue = null;
 JsonGenerator gen = MAPPER.getFactory().createGenerator(new ByteArrayOutputStream(), null);
 try {
 serializer.serialize(enumValue, gen, null);
 gen.flush();
 fail("Expected JsonMappingException");
 } catch (JsonMappingException ex) {
 // Expected
 }
 ByteArrayOutputStream jsonBytes = (ByteArrayOutputStream) gen.getOutput();
 ObjectNode jsonNode = MAPPER.readTree(jsonBytes.toByteArray());
 assertNull(jsonNode);
 }

 @Test
 public void testDeserializeEnum() throws IOException {
 ObjectNode jsonNode = MAPPER.readTree(getClass().getResource("/enum_serializer_test.json"));
 MyEnum deserializedEnum = MAPPER.treeToValue(jsonNode, MyEnum.class);
 assertEquals(MyEnum.VALUE_1, deserializedEnum);
 }

 @Test
 public void testDeserializeEnumWithCustomValues() throws IOException {
 ObjectNode jsonNode = MAPPER.readTree(getClass().getResource("/enum_serializer_test_custom_values.json"));
 MyEnum deserializedEnum = MAPPER.treeToValue(jsonNode, MyEnum.class);
 assertEquals(MyEnum.CUSTOM_VALUE_1, deserializedEnum);
 }

 @Test
 public void testDeserializeEnumWithToString() throws IOException {
 ObjectNode jsonNode = MAPPER.readTree(getClass().getResource("/enum_serializer_test_to_string.json"));
 MyEnum deserializedEnum = null;
 try {
 deserializedEnum = MAPPER.treeToValue(jsonNode, MyEnum.class);
 fail("Expected JsonMappingException");
 } catch (JsonMappingException ex) {

 // Expected
 assertEquals("VALUE_1.VALUE_1", jsonNode.get("$value").asText());
 deserializedEnum = MAPPER.readerForUpdating(MyEnum.class).readValue(jsonNode);
 }
 assertEquals(MyEnum.VALUE_1, deserializedEnum);
 }

 @Test
 public void testDeserializeEnumWithOrdinal() throws IOException {
 ObjectNode jsonNode = MAPPER.readTree(getClass().getResource("/enum_serializer_test_ordinal.json"));
 MyEnum deserializedEnum = null;
 try {
 deserializedEnum = MAPPER.treeToValue(jsonNode, MyEnum.class);
 fail("Expected JsonMappingException");
 } catch (JsonMappingException ex) {

 // Expected
 assertEquals("0", jsonNode.get("$value").asText());
 deserializedEnum = MAPPER.readerForUpdating(MyEnum.class).readValue(jsonNode);
 }
 assertEquals(MyEnum.VALUE_1, deserializedEnum);
 }
}

enum MyEnum {
 VALUE_1,
 VALUE_2,
 @JsonFormat(shape = Shape.STRING, value = "CUSTOM_VALUE_1")
 CUSTOM_VALUE_1,
 @JsonFormat(shape = Shape.STRING, value = "CUSTOM_VALUE_2")
 CUSTOM_VALUE_2
}