import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class EnumSerializerTest {

 private EnumSerializer enumSerializer;
 private EnumValues enumValues;

 @BeforeEach
 public void setup() {
 // Assuming EnumValues is a static class or singleton, we can create it before each test
 enumValues = new EnumValues(MyEnum.class);
 enumSerializer = new EnumSerializer(enumValues, false);
 }

 @Test
 public void testSerializeEnum() throws JsonProcessingException {
 MyEnum enumValue = MyEnum.VALUE_1;
 ObjectNode objectNode = new ObjectMapper().createObjectNode();
 JsonGenerator jsonGenerator = objectNode.getFactory().createGenerator(objectNode);
 enumSerializer.serialize(enumValue, jsonGenerator, null);
 assertEquals("VALUE_1", jsonGenerator.getOutputFile().toString());
 }

 @Test
 public void testSerializeEnumIndex() throws JsonProcessingException {
 MyEnum enumValue = MyEnum.VALUE_1;
 ObjectNode objectNode = new ObjectMapper().createObjectNode();
 JsonGenerator jsonGenerator = objectNode.getFactory().createGenerator(objectNode);
 if (enumSerializer._serializeAsIndex(null)) {
 jsonGenerator.writeNumber(enumValue.ordinal());
 } else {
 jsonGenerator.writeString(enumValue.toString());
 }
 assertEquals("0", objectNode.toString()); // Assuming MyEnum has only one value and VALUE_1 is the first one
 }

 @Test
 public void testSerializeEnumToString() throws JsonProcessingException {
 MyEnum enumValue = MyEnum.VALUE_2;
 ObjectMapper objectMapper = new ObjectMapper();
 objectMapper.configure(SerializationFeature.WRITE_ENUMS_USING_TO_STRING, true);
 JsonGenerator jsonGenerator = objectMapper.getFactory().createGenerator(new ByteArrayOutputStream(), null);
 EnumSerializer enumSerializer = new EnumSerializer(enumValues, null);
 enumSerializer.serialize(enumValue, jsonGenerator, null);
 assertEquals("VALUE_2", jsonGenerator.getOutputFile().toString());
 }

 @Test
 public void testGetSchema() {
 ObjectNode schemaNode = enumSerializer.getSchema(null, null);
 assertTrue(schemaNode.isObject());
 assertTrue(schemaNode.hasNonNull("type"));
 assertTrue(schemaNode.hasNonNull("properties"));
 assertFalse(schemaNode.hasNonNull("items"));
 }

 @Test
 public void testAcceptJsonFormatVisitor() throws JsonProcessingException {
 ObjectMapper objectMapper = new ObjectMapper();
 JsonFormat.Value jsonFormat = objectMapper.getSerializationConfig().getFormatValue("MyEnum");
 JsonFormatVisitorWrapper jsonFormatVisitor = objectMapper.getFactory().constructJsonFormatVisitor(null, JsonParser.NumberType.INT);
 assertTrue(enumSerializer.acceptJsonFormatVisitor(jsonFormatVisitor, null) instanceof JsonSerializer.NumberSerializer);
 }

 @Test
 public void testAcceptJsonFormatVisitorToString() throws JsonProcessingException {
 ObjectMapper objectMapper = new ObjectMapper();
 JsonFormat.Value jsonFormat = objectMapper.getSerializationConfig().getFormatValue("MyEnum");
 JsonFormatVisitorWrapper jsonFormatVisitor = objectMapper.getFactory().constructJsonFormatVisitor(null, JsonParser.NumberType.STRING);
 assertTrue(enumSerializer.acceptJsonFormatVisitor(jsonFormatVisitor, null) instanceof JsonSerializer.StringSerializer);
 }

 @Test
 public void testAcceptJsonFormatVisitorEnumValues() throws JsonProcessingException {
 ObjectMapper objectMapper = new ObjectMapper();
 JsonFormat.Value jsonFormat = objectMapper.getSerializationConfig().getFormatValue("MyEnum");
 JsonStringFormatVisitor jsonFormatVisitor = jsonFormat.acceptJsonFormatVisitor(objectMapper.getFactory(), null);
 ArrayNode enumNode = jsonFormatVisitor.getSchema().get("enum");
 assertEquals(2, enumNode.size());
 }

 @Test
 public void testCreateContextual() {
 SerializerProvider serializerProvider = new SerializerProvider();
 BeanProperty beanProperty = new BeanProperty("myProperty", MyEnum.class);
 JsonFormat.Value jsonFormat = new JsonFormat.Value();
 assertNotNull(enumSerializer.createContextual(serializerProvider, beanProperty));
 }

 @Test
 public void testCreateContextualNullBeansProperty() {
 SerializerProvider serializerProvider = new SerializerProvider();
 assertNull(enumSerializer.createContextual(serializerProvider, null));
 }

 @Test
 public void testCreateContextualJsonFormat() {
 SerializerProvider serializerProvider = new SerializerProvider();
 BeanProperty beanProperty = new BeanProperty("myProperty", MyEnum.class);
 JsonFormat.Value jsonFormat = new JsonFormat.Value();
 JsonFormat.Shape shape = JsonFormat.Shape.STRING;
 jsonFormat.setShape(shape);
 assertNotNull(enumSerializer.createContextual(serializerProvider, beanProperty));
 }

 @Test
 public void testCreateContextualJsonFormatNullShape() {
 SerializerProvider serializerProvider = new SerializerProvider();
 BeanProperty beanProperty = new BeanProperty("myProperty", MyEnum.class);
 JsonFormat.Value jsonFormat = new JsonFormat.Value();
 assertNull(enumSerializer.createContextual(serializerProvider, beanProperty));
 }
}