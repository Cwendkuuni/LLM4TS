import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class EnumSerializerTest {

 private EnumSerializer serializer;
 private ObjectMapper mapper;

 @Before
 public void setup() {
 mapper = new ObjectMapper();
 serializer = mapper.findValueSerializer(Integer.class);
 }

 @Test
 public void testSerializeEnumByValue() throws JsonProcessingException {
 EnumSerializer enumSerializer = new EnumSerializer(EnumValues.allValues(MyEnum.class), null);
 JsonGenerator gen = new TestJsonGenerator();

 assertEquals("RED", enumSerializer.serializeValue(MyEnum.RED, gen, null));
 }

 @Test
 public void testSerializeEnumByOrdinal() throws JsonProcessingException {
 EnumSerializer enumSerializer = new EnumSerializer(EnumValues.allValues(MyEnum.class), true);
 JsonGenerator gen = new TestJsonGenerator();

 assertEquals("0", enumSerializer.serializeValue(MyEnum.RED, gen, null));
 }

 @Test
 public void testSerializeEnumWithCustomSerialzedValue() throws JsonProcessingException {
 EnumSerializer enumSerializer = new EnumSerializer(EnumValues.allValues(MyEnumWithCustomValues.class), null);
 JsonGenerator gen = new TestJsonGenerator();

 assertEquals("APPLE", enumSerializer.serializeValue(MyEnumWithCustomValues.APPLE, gen, null));
 }

 @Test
 public void testSerializeEnumWithEmptyValues() throws JsonProcessingException {
 EnumSerializer enumSerializer = new EnumSerializer(EnumValues.emptyEnumValues(), null);
 JsonGenerator gen = new TestJsonGenerator();

 assertNull(enumSerializer.serializeValue(null, gen, null));
 }

 @Test
 public void testSerializeEnumToString() throws JsonProcessingException {
 SerializationConfig config = new SerializationConfig();
 config.disable(SerializationFeature.WRITE_ENUMS_USING_TO_STRING);
 ObjectNode node = mapper.readValue(getClass().getResource("/serialization-test.json"), ObjectNode.class);
 ObjectNode enumNode = node.get("enum");
 EnumSerializer enumSerializer = mapper.findValueSerializer(MyEnum.class);

 assertEquals("RED", enumSerializer.serializeValue(MyEnum.RED, null, null));
 assertEquals("GREEN", enumSerializer.serializeValue(MyEnum.GREEN, null, null));
 assertEquals("BLUE", enumSerializer.serializeValue(MyEnum.BLUE, null, null));

 SerializationConfig configWithToString = new SerializationConfig();
 configWithToString.enable(SerializationFeature.WRITE_ENUMS_USING_TO_STRING);
 JsonGenerator gen = mapper.getPolymorphicSerializer(MyEnum.class).getSerializerProvider().getGeneratorForCurrentOutput(ObjectWriter.DefaultPrettyPrinter.instance(), MyEnum.class);

 assertEquals("RED", enumSerializer.serializeValue(MyEnum.RED, gen, null));
 assertEquals("GREEN", enumSerializer.serializeValue(MyEnum.GREEN, gen, null));
 assertEquals("BLUE", enumSerializer.serializeValue(MyEnum.BLUE, gen, null));
 assertEquals("MY_RED", enumSerializer.serializeValue(MyEnumWithCustomValues.MY_RED, gen, null));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSerializeEnumUnsupportedShape() throws JsonProcessingException {
 EnumSerializer enumSerializer = new EnumSerializer(EnumValues.allValues(MyEnumWithUnsupportedShape.class), null);
 JsonGenerator gen = new TestJsonGenerator();

 enumSerializer.serializeValue(MyEnumWithUnsupportedShape.SHAPE, gen, null);
 }

 @Test
 public void testSerializeEnumContextual() throws JsonProcessingException {
 ObjectNode node = mapper.readValue(getClass().getResource("/serialization-test.json"), ObjectNode.class);
 ObjectNode enumNode = node.get("enum");
 String propertyName = "color";
 ObjectNode propertyNode = node.get(propertyName);
 EnumSerializer enumSerializer = mapper.findContextualSerializer(MyEnum.class, propertyNode);

 assertEquals("RED", enumSerializer.serializeValue(MyEnum.RED, null, null));
 assertEquals("GREEN", enumSerializer.serializeValue(MyEnum.GREEN, null, null));
 assertEquals("BLUE", enumSerializer.serializeValue(MyEnum.BLUE, null, null));
 }

 @Test
 public void testSerializeEnumMultipleValues() throws JsonProcessingException {
 EnumSerializer enumSerializer = new EnumSerializer(EnumValues.allValues(MyEnumWithArrays.class), null);
 JsonGenerator gen = new TestJsonGenerator();

 ObjectNode node = mapper.createObjectNode();
 ArrayNode arrayNode = node.putArray("enumArray");
 arrayNode.add(ObjectNode.wrap("RED"));
 arrayNode.add(ObjectNode.wrap("GREEN"));
 arrayNode.add(ObjectNode.wrap("BLUE"));
 ObjectNode rootNode = mapper.writeValueAsTree(node);
 ObjectReader reader = mapper.readerForTree(ObjectNode.class);
 ObjectNode readNode = reader.readValue(new ByteArrayInputStream(rootNode.toString().getBytes()));
 ObjectNode enumNode = readNode.get("enumArray");

 assertEquals("RED", enumSerializer.serializeValue(MyEnumWithArrays.RED, null, null));
 assertEquals("GREEN", enumSerializer.serializeValue(MyEnumWithArrays.GREEN, null, null));
 assertEquals("BLUE", enumSerializer.serializeValue(MyEnumWithArrays.BLUE, null, null));
 }
}

class MyEnum {
 @JsonFormat(shape = Shape.STRING)
 public static enum MyEnum { RED, GREEN, BLUE }
}

class MyEnumWithCustomValues {
 @JsonFormat(shape = Shape.STRING, serializedValue = "APPLE")
 public static enum MyEnumWithCustomValues { APPLE, BANANA, CLEMENTINE }
}

class MyEnumWithUnsupportedShape {
 public static enum MyEnumWithUnsupportedShape { SHAPE, NUMBER, BOOLEAN }
}

class MyEnumWithArrays {
 public static enum MyEnumWithArrays { RED, GREEN, BLUE }
}