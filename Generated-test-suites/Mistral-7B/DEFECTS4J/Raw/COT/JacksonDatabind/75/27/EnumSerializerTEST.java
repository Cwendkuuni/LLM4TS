import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class EnumSerializerTest {

 private EnumSerializer serializer;
 private EnumValues enumValues;

 @BeforeAll
 public void setup() throws JsonProcessingException {
 EnumSerializer.EnumSerializerBuilder builder = new EnumSerializer.EnumSerializerBuilder(
 Enum.class.getEnumConstants(), null);
 ObjectMapper objectMapper = new ObjectMapper();
 SerializerProvider serializerProvider = objectMapper.getSerializerProvider();
 this.serializer = builder.build(serializerProvider);
 this.enumValues = builder.getEnumValues();
 }

 @Test
 public void testSerializeEnumeration() throws JsonProcessingException {
 Enum testEnum = Enum.values()[0];
 ObjectNode result = mapper.writeValueAsTree(testEnum, new ObjectMapper().getSerializerProvider());
 ObjectNode expected = mapper.readValue(new ObjectMapper().writeValueAsString(testEnum.name()), ObjectNode.class);
 assertEquals(expected, result);
 }

 @Test
 public void testSerializeAsIndex() throws JsonProcessingException {
 ObjectNode result = mapper.valueToTree(Enum.values()[0].ordinal());
 JsonGenerator gen = mapper.getFactory().createGenerator(new ByteArrayOutputStream(), null);
 serializer.serialize(Enum.values()[0], gen, serializer);
 assertEquals(result.toString(), gen.getOutputContent().toString());
 }

 @Test
 public void testSerializeAsString() {
 ObjectNode result = mapper.valueToTree("TEST_STRING");
 JsonGenerator gen = mapper.getFactory().createGenerator(new ByteArrayOutputStream(), null);
 serializer.serialize(null, gen, serializer);
 gen.writeString("TEST_STRING");
 assertEquals(result.toString(), gen.getOutputContent().toString());
 }

 @Test
 public void testGetSchema() throws JsonProcessingException {
 JsonNode schema = serializer.getSchema(serializer.getProvider(), Enum.class);
 ObjectNode expectedSchema = mapper.readValue(Objects.requireNonNull(EnumSerializerTest.class.getResourceAsStream("/enumSerializerTestSchema.json")), ObjectNode.class);
 assertEquals(expectedSchema, schema);
 }

 @Test
 public void testAcceptJsonFormatVisitor() {
 JsonFormatVisitorWrapper visitor = mapper.getFactory().createSerializerProvider().getProviderInstance(JsonFormatVisitorWrapper.class);
 ObjectNode expectedObjectNode = mapper.readValue(Objects.requireNonNull(EnumSerializerTest.class.getResourceAsStream("/expectedJsonFormatVisitor.json")), ObjectNode.class);
 serializer.acceptJsonFormatVisitor(visitor, Enum.class);
 assertTrue(visitor.asPrefixTree().isDeeplyEqual(expectedObjectNode));
 }

 @Test
 public void testNullValue() {
 assertThrows(JsonProcessingException.class, () -> serializer.serialize(null, null, null));
 }

 @Test
 public void testUnknownEnumValue() {
 Enum unknownEnum = new Enum("UnknownEnumValue") {
 };
 assertThrows(JsonMappingException.class, () -> serializer.serialize(unknownEnum, null, null));
 }

 @Test
 public void testSerializeUsingToString() throws JsonProcessingException {
 SerializerProvider serializerProvider = new StandardSerializerProvider();
 ObjectMapper objectMapper = new ObjectMapper();
 ObjectNode result = mapper.writeValueAsTree(Enum.values()[0], serializerProvider);
 JsonGenerator gen = mapper.getFactory().createGenerator(new ByteArrayOutputStream(), null);
 serializer.setSerializeAsIndex(false);
 ObjectNode expected = mapper.readValue(Objects.requireNonNull(EnumSerializerTest.class.getResourceAsStream("/expectedSerializeUsingToString.json")), ObjectNode.class);
 serializer.serialize(Enum.values()[0], gen, serializerProvider);
 gen.writeString(Enum.values()[0].toString());
 assertEquals(expected, result);
 }

 @Test
 public void testSerializeUsingName() throws JsonProcessingException {
 SerializerProvider serializerProvider = new StandardSerializerProvider();
 ObjectMapper objectMapper = new ObjectMapper();
 ObjectNode result = mapper.writeValueAsTree(Enum.values()[0], serializerProvider);
 JsonGenerator gen = mapper.getFactory().createGenerator(new ByteArrayOutputStream(), null);
 serializer.setSerializeAsIndex(true);
 ObjectNode expected = mapper.readValue(Objects.requireNonNull(EnumSerializerTest.class.getResourceAsStream("/expectedSerializeUsingName.json")), ObjectNode.class);
 serializer.serialize(Enum.values()[0], gen, serializerProvider);
 gen.writeNumber(Enum.values()[0].ordinal());
 assertEquals(expected, result);
 }

 @Test
 public void testSerializeWithJsonFormatAnnotation() throws JsonProcessingException {
 JsonFormat jsonFormat = new JsonFormat();
 JsonFormat.Shape shape = JsonFormat.Shape.STRING;
 JsonFormat.Value value = new JsonFormat.Value();
 value.setShape(shape);
 serializer = new EnumSerializer(enumValues, true);
 ObjectNode result = mapper.writeValueAsTree(Enum.values()[0], new ObjectMapper().getSerializerProvider());
 ObjectNode expected = mapper.readValue(Objects.requireNonNull(EnumSerializerTest.class.getResourceAsStream("/expectedSerializeWithJsonFormatAnnotation.json")), ObjectNode.class);
 serializer.acceptJsonFormatVisitor(mapper.getFactory().createSerializerProvider().getProviderInstance(JsonFormatVisitorWrapper.class), Enum.class, value);
 assertEquals(expected, result);
 }
}