import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class EnumSerializerTest {

 private static final ObjectMapper MAPPER = new ObjectMapper();

 @Test
 public void testSerializeEnumAsString() throws JsonProcessingException {
 EnumSerializer serializer = new EnumSerializer(MyEnum.values(), false);
 ObjectNode node = MAPPER.createObjectNode();
 node.putPOJO("myEnum", MyEnum.FIRST);
 ObjectWriter writer = MAPPER.writer().withValueWriterFactory(new MyEnumValueWriterFactory());
 String json = writer.writeValueAsString(node);
 assertEquals("\"FIRST\"", json);
 }

 @Test
 public void testSerializeEnumAsIndex() throws JsonProcessingException {
 EnumSerializer serializer = new EnumSerializer(MyEnum.values(), true);
 ObjectNode node = MAPPER.createObjectNode();
 node.putPOJO("myEnum", MyEnum.FIRST);
 ObjectWriter writer = MAPPER.writer().withValueWriterFactory(new MyEnumValueWriterFactory());
 String json = writer.writeValueAsString(node);
 assertEquals("0", json);
 }

 @Test(expected = JsonMappingException.class)
 public void testSerializeUnknownEnumValue() throws JsonProcessingException {
 EnumSerializer serializer = new EnumSerializer(MyEnum.values(), false);
 ObjectNode node = MAPPER.createObjectNode();
 node.putPOJO("myEnum", new Object());
 ObjectWriter writer = MAPPER.writer().withValueWriterFactory(new MyEnumValueWriterFactory());
 String json = writer.writeValueAsString(node);
 fail("Expected JsonMappingException");
 }

 @Test
 public void testDeserializeEnumAsString() throws JsonProcessingException {
 EnumSerializer deserializer = new EnumSerializer(MyEnum.class, false, null);
 JsonParser parser = MAPPER.parserForObject(Arrays.asList("\"FIRST\"", "\"SECOND\"").toArray(new String[0]));
 ObjectNode node = parser.readValueAsTree();
 assertEquals(MyEnum.FIRST, deserializer.deserialize(parser, null));
 }

 @Test
 public void testDeserializeEnumAsIndex() throws JsonProcessingException {
 EnumSerializer deserializer = new EnumSerializer(MyEnum.class, false, null);
 JsonParser parser = MAPPER.parserForObject(Arrays.asList("0", "1").toArray(new String[0]));
 ObjectNode node = parser.readValueAsTree();
 assertEquals(MyEnum.FIRST, deserializer.deserialize(parser, null));
 }

 @Test(expected = JsonMappingException.class)
 public void testDeserializeUnknownJsonValue() throws JsonProcessingException {
 EnumSerializer deserializer = new EnumSerializer(MyEnum.class, false, null);
 JsonParser parser = MAPPER.parserForObject("\"UNKNOWN\"");
 ObjectNode node = parser.readValueAsTree();
 fail("Expected JsonMappingException");
 }

 @Test
 public void testSerializeEnumWithCustomFormat() throws JsonProcessingException {
 JsonFormat.Value format = new JsonFormat.Value();
 format.setShape(JsonFormat.Shape.STRING);
 format.setSerializer(new EnumSerializer.MyEnumFormatSerializer());
 ObjectNode node = MAPPER.createObjectNode();
 node.putPOJO("myEnum", MyEnum.FIRST);
 ObjectWriter writer = MAPPER.writer().withValueWriterFactory(new MyEnumValueWriterFactory())
 .withSerializerFactory(new JacksonStdImpl.StdSerializerFactory(MyEnum.class))
 .withSerializerModification(MyEnumSerializer.class, new EnumSerializer.MyEnumFormatSerializer());
 String json = writer.writeValueAsString(node);
 assertEquals("\"CUSTOM_FIRST\"", json);
 }

 @Test
 public void testDeserializeEnumWithCustomFormat() throws JsonProcessingException {
 JsonParser parser = MAPPER.parserForObject("[\"CUSTOM_FIRST\", \"CUSTOM_SECOND\"]");
 ObjectNode node = parser.readValueAsTree();
 ObjectReader reader = MAPPER.readerForTree(node);
 assertEquals(MyEnum.FIRST, reader.readValue(MyEnum.class));
 }

 class MyEnumFormatSerializer extends JsonSerializer<MyEnum> {
 @Override
 public void serialize(MyEnum value, JsonGenerator gen, SerializerProvider serializers)
 throws IOException, JsonProcessingException {
 gen.writeString("CUSTOM_" + value.name());
 }
 }

 class MyEnumValueWriterFactory extends StdValueWriterFactory {
 @Override
 public JsonValueWriter createValueWriter(SerializerProvider serializers, TypeSerializer contentSerializer,
 String name, JsonSerializer serializer) {
 return new MyEnumValueWriter(serializers, contentSerializer, name, serializer);
 }
 }

 class MyEnumValueWriter extends BaseValueWriter {
 private final EnumSerializer serializer;

 public MyEnumValueWriter(SerializerProvider serializers, TypeSerializer contentSerializer,
 String name, JsonSerializer serializer) {
 super(serializers, contentSerializer, name);
 this.serializer = (EnumSerializer) serializer;
 }

 @Override
 public void writeValue(JsonGenerator gen, Object value) throws IOException {
 if (value instanceof Enum) {
 Enum<?> enumValue = (Enum<?>) value;
 String serializedValue;
 if (serializer._serializeAsIndex(serializers)) {
 serializedValue = Integer.toString(enumValue.ordinal());
 } else {
 serializedValue = serializer._values.serializedValueFor(enumValue);
 }
 gen.writeString(serializedValue);
 } else {
 super.writeValue(gen, value);
 }
 }
}
}

enum MyEnum {
 @JsonFormat(shape = Shape.STRING)
 FIRST,
 @JsonFormat(shape = Shape.ARRAY, serializers = {MyEnumFormatSerializer.class})
 SECOND
}