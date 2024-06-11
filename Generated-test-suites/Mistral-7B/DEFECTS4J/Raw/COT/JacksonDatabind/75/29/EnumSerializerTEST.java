import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.json.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.std.EnumSerializer;
import com.fasterxml.jackson.databind.util.EnumValues;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class EnumSerializerTest {

 private EnumSerializer serializer;
 private EnumValues enumValues;
 private ObjectMapper mapper;

 @Before
 public void setup() {
 MyEnum enumType = MyEnum.RED;
 enumValues = new EnumValues(enumType.getDeclaredEnumConstants()[0], enumType.getDeclaredEnumConstants());
 serializer = new EnumSerializer(enumValues, false);
 mapper = new ObjectMapper();
 }

 @Test
 public void testSerializeEnumAsStringWithDefaultSerialization() throws JsonProcessingException {
 String expected = "RED";
 JsonNode actual = mapper.writeValueAsTree(serializer);
 ObjectNode objectNode = (ObjectNode) actual;
 assertEquals(expected, objectNode.get("$value").textValue());
 }

 @Test
 public void testSerializeEnumAsStringWithToStringSerialization() throws JsonProcessingException {
 SerializerProvider serializers = mapper.getSerializationConfig().getDefaultSerializerProvider();
 serializer = new EnumSerializer(enumValues, null);
 String expected = "MyEnum.RED";
 JsonNode actual = mapper.writeValueAsTree(serializer);
 ObjectNode objectNode = (ObjectNode) actual;
 assertEquals(expected, objectNode.get("$value").textValue());
 }

 @Test
 public void testSerializeEnumAsNumberWithIndexSerialization() throws JsonProcessingException {
 SerializerProvider serializers = mapper.getSerializationConfig().getDefaultSerializerProvider();
 serializer = new EnumSerializer(enumValues, true);
 int expected = 0;
 JsonGenerator gen = mapper.getFactory().createGenerator();
 serializer.serialize(enumValues.getEnumConstantByName("RED"), gen, serializers);
 gen.flush();
 JsonNode actual = gen.getOutputFactory().createParser(gen.getOutputStream()).readValueAsTree();
 assertEquals(expected, actual.get("numberValue").numberValue().intValue());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSerializeEnumWithInvalidSerializationShape() throws JsonProcessingException {
 JsonFormat.Value format = new JsonFormat.Value();
 JsonFormat.Shape shape = JsonFormat.Shape.HEX;
 serializer = new EnumSerializer(enumValues, null);
 SerializerProvider serializers = mapper.getSerializationConfig().getDefaultSerializerProvider();
 serializer.createContextual(serializers, null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSerializeEnumWithNullFormat() throws JsonProcessingException {
 JsonFormat.Value format = null;
 serializer = new EnumSerializer(enumValues, null);
 SerializerProvider serializers = mapper.getSerializationConfig().getDefaultSerializerProvider();
 serializer.createContextual(serializers, null);
 }

 @Test
 public void testGetSchema() throws JsonProcessingException {
 JsonNode expected = mapper.writeValueAsTree(serializer.getSchema(mapper.getSerializationConfig(), Enum.class.getType()));
 ArrayNode enumNode = (ArrayNode) expected.path("enum");
 assertEquals(3, enumNode.size());
 assertEquals("RED", enumNode.get(0).textValue());
 assertEquals("GREEN", enumNode.get(1).textValue());
 assertEquals("BLUE", enumNode.get(2).textValue());
 }
}

enum MyEnum {
 RED, GREEN, BLUE
}