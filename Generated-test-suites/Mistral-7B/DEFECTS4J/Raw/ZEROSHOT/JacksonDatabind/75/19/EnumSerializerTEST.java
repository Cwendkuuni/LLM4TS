import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.ser.std.EnumSerializer;
import com.fasterxml.jackson.databind.ser.std.StdScalarSerializer;
import com.fasterxml.jackson.databind.util.EnumValues;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class EnumSerializerTest {

 private EnumSerializer enumSerializer;
 private EnumValues enumValues;

 @Before
 public void setup() {
 enumValues = EnumValues.valuesOf(MyEnum.class);
 enumSerializer = new EnumSerializer(enumValues, false);
 }

 @Test
 public void testSerializeEnum() throws JsonProcessingException {
 ObjectMapper mapper = new ObjectMapper();
 JsonGenerator gen = mapper.getFactory().createGenerator(new ByteArrayOutputStream(), null);

 enumSerializer.serialize(MyEnum.VALUE_1, gen, null);
 gen.flush();

 byte[] actual = gen.getOutputBuffer().toByteArray();
 String expected = "\"VALUE_1\"";

 assertEquals(expected.length(), actual.length);
 assertEquals(expected, new String(actual));
 }

 @Test
 public void testSerializeEnumAsIndex() throws JsonProcessingException {
 ObjectMapper mapper = new ObjectMapper();
 mapper.configure(SerializationFeature.WRITE_ENUMS_USING_TO_STRING, false);
 JsonGenerator gen = mapper.getFactory().createGenerator(new ByteArrayOutputStream(), null);

 enumSerializer.serialize(MyEnum.VALUE_1, gen, null);
 gen.flush();

 byte[] actual = gen.getOutputBuffer().toByteArray();
 assertEquals(1, actual.length);
 assertEquals((byte) MyEnum.VALUE_1.ordinal(), actual[0]);
 }

 @Test
 public void testSerializeEnumWithCustomValues() throws JsonProcessingException {
 ObjectMapper mapper = new ObjectMapper();
 mapper.configure(SerializationFeature.WRITE_ENUMS_USING_TO_STRING, false);
 ObjectNode node = mapper.createObjectNode();
 node.put("foo", mapper.valueToTree(MyEnum.VALUE_2));

 JsonSerializer<ObjectNode> nodeSerializer = mapper.findValueSerializer(ObjectNode.class);
 nodeSerializer.serialize(node, new JsonGenerator(new ByteArrayOutputStream(), null), null);
 gen.flush();

 byte[] actual = gen.getOutputBuffer().toByteArray();
 String expected = "{\"foo\":\"VALUE_2\"}";

 assertEquals(expected.length(), actual.length);
 assertEquals(expected, new String(actual));
 }

 @Test
 public void testAcceptJsonFormatVisitor() throws JsonMappingException {
 ObjectMapper mapper = new ObjectMapper();
 JsonFormat.Value format = new JsonFormat.Value();
 format.setShape(JsonFormat.Shape.STRING);
 JsonFormatVisitorWrapper visitor = mapper.schemaFactory().getJsonFormatVisitor(null, null, MyEnum.class);

 enumSerializer.acceptJsonFormatVisitor(visitor, null);
 JsonStringFormatVisitor stringVisitor = visitor.expectStringFormat(null);

 Set<String> expected = new LinkedHashSet<>();
 expected.add("VALUE_1");
 expected.add("VALUE_2");

 assertEquals(expected, stringVisitor.enumTypes());
 }

 @Test
 public void testGetSchema() throws JsonProcessingException {
 ObjectMapper mapper = new ObjectMapper();
 JsonNode schema = enumSerializer.getSchema(mapper.getSerializationConfig(), null);

 ArrayNode actualEnumNode = (ArrayNode) schema.path("enum");

 assertEquals(2, actualEnumNode.size());
 assertEquals("VALUE_1", actualEnumNode.get(0).asText());
 assertEquals("VALUE_2", actualEnumNode.get(1).asText());
 }

 @Test
 public void testCreateContextual() throws JsonMappingException {
 ObjectMapper mapper = new ObjectMapper();
 BeanProperty property = new BeanProperty("foo", MyEnum.class);
 JsonFormat.Value format = new JsonFormat.Value();
 format.setShape(JsonFormat.Shape.STRING);
 JsonFormat.Value formatOverrides = new JsonFormat.Value(format);
 formatOverrides.setShape(JsonFormat.Shape.INT);

 JsonSerializer<EnumSerializer> serializer = mapper.findValueSerializer(EnumSerializer.class);
 JsonSerializer<?> serializerWithFormat = serializer.createContextual(mapper.getSerializerProvider(), property);

 assertEquals(serializer, serializerWithFormat);

 JsonSerializer<EnumSerializer> serializerWithFormatOverride = serializer.createContextual(mapper.getSerializerProvider(), property);
 serializerWithFormatOverride = serializerWithFormatOverride.createContextual(mapper.getSerializerProvider(), property);

 assertEquals(new EnumSerializer(enumValues, true), serializerWithFormatOverride);
 }

 @Test
 public void testIsSerializeAsIndex() {
 assertTrue(enumSerializer.isSerializeAsIndex(null));
 assertFalse(enumSerializer.isSerializeAsIndex(new ObjectMapper().getSerializationConfig()));
 }

 private enum MyEnum {
 @JsonFormat(shape = JsonFormat.Shape.STRING, value = "VALUE_1")
 VALUE_1,
 @JsonFormat(shape = JsonFormat.Shape.STRING, value = "VALUE_2")
 VALUE_2
 }
}