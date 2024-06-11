import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.std.EnumSerializer;
import com.fasterxml.jackson.databind.util.EnumValues;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class EnumSerializerTest {

 @BeforeAll
 static void setup() {
 ObjectMapper mapper = new ObjectMapper();
 }

 @Test
 @DisplayName("Test serialization of Enum with index as value")
 void testSerializationIndex() throws JsonProcessingException {
 EnumSerializer serializer = new EnumSerializer(EnumValues.constructFromClass(MyEnum.class), true);
 MyEnum value = MyEnum.VALUE_1;
 String expected = String.valueOf(value.ordinal());
 String actual = mapper.writerWithView(JsonSerializationView.StandardSerializers.class)
 .writeValueAsString(value);
 assertEquals(expected, actual);
 }

 @Test
 @DisplayName("Test serialization of Enum with name as value (default)")
 void testSerializationName() throws JsonProcessingException {
 EnumSerializer serializer = new EnumSerializer(EnumValues.constructFromClass(MyEnum.class), false);
 MyEnum value = MyEnum.VALUE_1;
 String expected = value.name();
 String actual = mapper.writerWithView(JsonSerializationView.StandardSerializers.class)
 .writeValueAsString(value);
 assertEquals(expected, actual);
 }

 @Test
 @DisplayName("Test serialization of Enum with custom format (String) using getSchema method")
 void testSerializationCustomFormat() throws JsonProcessingException {
 JsonFormat customFormat = new JsonFormat.Value();
 customFormat.setShape(JsonFormat.Shape.STRING);
 JsonSerializer<MyEnum> serializer = new EnumSerializer(EnumValues.constructFromClass(MyEnum.class), false).createContextual(null, null);
 MyEnum value = MyEnum.VALUE_1;
 String expected = value.name();
 JsonNode actual = mapper.writerWithView(JsonSerializationView.StandardSerializers.class)
 .writeValueAsTree(value, customFormat);
 ObjectNode schemaNode = serializer.getSchema(mapper.getSerializerProvider(), MyEnum.class.getType());
 ArrayNode enumNode = schemaNode.path("enum");
 String enumValue = enumNode.get(0).asText();
 assertEquals(expected, enumValue);
 }

 @Test
 @DisplayName("Test deserialization of Enum with index as value")
 void testDeserializationIndex() throws JsonProcessingException, JsonMappingException {
 ObjectMapper mapper = new ObjectMapper();
 String json = String.format("{\"@class\":\"%s\",\"c\":%d}", MyEnum.class.getName(), 0);
 JsonNode node = mapper.readTree(json);
 MyEnum value = mapper.readerForUpdating(MyEnum.class).readValue(node);
 assertEquals(MyEnum.VALUE_1, value);
 }

 @Test
 @DisplayName("Test deserialization of Enum with name as value (default)")
 void testDeserializationName() throws JsonProcessingException, JsonMappingException {
 ObjectMapper mapper = new ObjectMapper();
 String json = "{\"@class\":\"MyEnum\",\"c\":\"VALUE_1\"}";
 MyEnum value = mapper.readerForUpdating(MyEnum.class).readValue(json);
 assertEquals(MyEnum.VALUE_1, value);
 }

 @Test
 @DisplayName("Test deserialization of Enum with custom format (String) using acceptJsonFormatVisitor method")
 void testDeserializationCustomFormat() throws JsonProcessingException, JsonMappingException {
 ObjectMapper mapper = new ObjectMapper();
 String json = "{\"@class\":\"MyEnum\",\"c\":\"VALUE_1\"}";
 JsonNode node = mapper.readTree(json);
 JsonFormat customFormat = new JsonFormat.Value();
 customFormat.setShape(JsonFormat.Shape.STRING);
 ObjectReader reader = mapper.readerForUpdating(MyEnum.class)
 .with(JsonDeserializer.ofBeanUsing(EnumSerializer.class).withContextualSerializer(new EnumSerializer(EnumValues.constructFromClass(MyEnum.class), false).createContextual(null, null)));
 MyEnum value = reader.readValue(node, MyEnum.class);
 assertEquals(MyEnum.VALUE_1, value);
 }

 @Test
 @DisplayName("Test deserialization of Enum with invalid index value")
 void testDeserializationInvalidIndex() {
 ObjectMapper mapper = new ObjectMapper();
 String json = "{\"@class\":\"MyEnum\",\"c\":-1}";
 assertThrows(JsonProcessingException.class, () -> mapper.readerForUpdating(MyEnum.class).readValue(json, MyEnum.class));
 }

 @Test
 @DisplayName("Test deserialization of Enum with invalid name value")
 void testDeserializationInvalidName() {
 ObjectMapper mapper = new ObjectMapper();
 String json = "{\"@class\":\"MyEnum\",\"c\":\"INVALID_VALUE\"}";
 assertThrows(JsonMappingException.class, () -> mapper.readerForUpdating(MyEnum.class).readValue(json, MyEnum.class));
 }
}

enum MyEnum {
 @JsonFormat(shape = JsonFormat.Shape.STRING, value = "VALUE_1")
 VALUE_1,
 @JsonFormat(shape = JsonFormat.Shape.STRING, value = "VALUE_2")
 VALUE_2,
 @JsonFormat(shape = JsonFormat.Shape.STRING, value = "VALUE_3")
 VALUE_3;
}