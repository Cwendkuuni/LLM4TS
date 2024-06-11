import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.ser.std.EnumSerializer;
import com.fasterxml.jackson.databind.ser.std.StdScalarSerializer;
import com.fasterxml.jackson.databind.util.EnumValues;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

@SuppressWarnings("unchecked")
public class EnumSerializerTest {

 private ObjectMapper objectMapper;
 private EnumSerializer enumSerializer;
 private EnumValues enumValues;

 @Before
 public void setUp() throws Exception {
 objectMapper = new ObjectMapper();
 EnumSerializer.StdSerializerProvider stdProvider = new EnumSerializer.StdSerializerProvider(objectMapper.getSerializationConfig(), null, null);
 enumSerializer = EnumSerializer.construct(String.class, stdProvider, null, null);
 enumValues = (EnumValues) Enum.class.getField("EnumConstantNames").get(null);
 }

 @Test
 public void testSerializeAsIndex() throws Exception {
 JsonGenerator generator = new JsonFactory().createGenerator(new ByteArrayOutputStream());
 JsonSerializerProvider serializers = new StandardSerializerProvider();
 ObjectNode node = new ObjectMapper().createObjectNode();
 node.putBoolean("writeEnumsUsingIndex", true);
 SerializationConfig config = new SerializationConfig(new StandardSerializerProvider(), node);
 EnumSerializer serializerWithIndex = EnumSerializer.construct(String.class, config, null, null);
 Enum[] values = Enum.values();
 for (Enum value : values) {
 generator.writeStartObject();
 generator.writeFieldName("value");
 if (serializerWithIndex.isEnabled(SerializationFeature.WRITE_ENUMS_USING_INDEX)) {
 generator.writeNumber(value.ordinal());
 } else {
 generator.writeString(serializerWithIndex.getEnumValues().serializedValueFor(value));
 }
 generator.writeEndObject();
 }
 generator.close();

 ByteArrayOutputStream outputStream = (ByteArrayOutputStream) generator.getOutputBoundary();
 String serializedWithIndex = new String(outputStream.toByteArray());
 generator.close();

 generator = new JsonFactory().createGenerator(new ByteArrayOutputStream());
 ObjectNode node2 = new ObjectMapper().createObjectNode();
 node2.putBoolean("writeEnumsUsingToString", true);
 SerializationConfig config2 = new SerializationConfig(new StandardSerializerProvider(), node2);
 EnumSerializer serializerWithToString = EnumSerializer.construct(String.class, config2, null, null);
 for (Enum value : values) {
 generator.writeStartObject();
 generator.writeFieldName("value");
 if (serializerWithToString.isEnabled(SerializationFeature.WRITE_ENUMS_USING_TO_STRING)) {
 generator.writeString(value.toString());
 } else {
 generator.writeString(serializerWithToString.getEnumValues().serializedValueFor(value));
 }
 generator.writeEndObject();
 }
 generator.close();

 ByteArrayOutputStream outputStream2 = (ByteArrayOutputStream) generator.getOutputBoundary();
 String serializedWithToString = new String(outputStream2.toByteArray());

 assertEquals("[" + serializedWithIndex + "]", serializedWithIndex);
 assertEquals("[" + serializedWithToString + "]", serializedWithToString);

 generator = new JsonFactory().createGenerator(new ByteArrayOutputStream());
 ObjectNode node3 = new ObjectMapper().createObjectNode();
 node3.putBoolean("writeEnumsUsingIndex", false);
 node3.putBoolean("writeEnumsUsingToString", false);
 SerializationConfig config3 = new SerializationConfig(new StandardSerializerProvider(), node3);
 EnumSerializer serializer = new EnumSerializer(enumValues, null);
 for (Enum value : values) {
 generator.writeStartObject();
 generator.writeFieldName("value");
 serializer.serialize(value, generator, serializers);
 generator.writeEndObject();
 }
 generator.close();

 ByteArrayOutputStream outputStream3 = (ByteArrayOutputStream) generator.getOutputBoundary();
 String serialized = new String(outputStream3.toByteArray());

 assertEquals("[\"EAGER\",\"LAZY\",\"UNKNOWN\"]", serialized);
 }

 @Test
 public void testCreateContextual() throws Exception {
 ObjectMapper objectMapper = new ObjectMapper();
 ObjectNode node = new ObjectMapper().createObjectNode();
 node.putBoolean("writeEnumsUsingIndex", true);
 SerializationConfig config = new SerializationConfig(new StandardSerializerProvider(), node);
 EnumSerializer serializerWithIndex = EnumSerializer.construct(String.class.getField("EnumConstantNames").get(null), config, null, null);
 JsonSerializer<?> serializer = objectMapper.findValueSerializer(String.class, null, null);
 JsonSerializer<?> serializerWithContext = objectMapper.findValueSerializer(String.class, null, null).createContextual(objectMapper.getSerializerProvider(), null);
 assertEquals(serializerWithIndex, serializerWithContext);
 }

 @Test
 public void testAcceptJsonFormatVisitor() throws Exception {
 ObjectMapper objectMapper = new ObjectMapper();
 ObjectNode node = new ObjectMapper().createObjectNode();
 node.putBoolean("writeEnumsUsingIndex", true);
 JsonFormat.Value format = new JsonFormat.Value();
 format.setShape(JsonFormat.Shape.INT);
 SerializationConfig config = new SerializationConfig(new StandardSerializerProvider(), node);
 JsonFormatVisitorWrapper visitor = objectMapper.getFactory().createJsonFormatVisitor(config, null, null, null, null, null, null);
 EnumSerializer serializerWithIndex = EnumSerializer.construct(String.class, config, null, null);
 serializerWithIndex.acceptJsonFormatVisitor(visitor, null);
 assertTrue(visitor.asIntFormat().getNumberType().isInt());
 }

 @Test
 public void testGetSchema() throws Exception {
 ObjectMapper objectMapper = new ObjectMapper();
 JsonSerializer<String> serializer = objectMapper.findValueSerializer(String.class, null, null);
 ObjectNode schemaNode = serializer.getSchema(objectMapper.getSerializerProvider(), null);
 JsonNode schema = schemaNode.get("properties").get("value").get("enum");
 assertNotNull(schema);
 assertArrayEquals(new String[]{"EAGER", "LAZY", "UNKNOWN"}, schema.asText().split(", "));
 }
}