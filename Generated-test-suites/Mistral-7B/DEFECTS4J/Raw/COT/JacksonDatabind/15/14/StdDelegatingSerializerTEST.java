import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class StdDelegatingSerializerTest {

 private ObjectMapper objectMapper;
 private StdDelegatingSerializer serializer;
 private JsonSerializer<Object> delegateSerializer;
 private Converter<Object, ?> converter;

 @Before
 public void setup() throws JsonMappingException {
 objectMapper = new ObjectMapper();
 converter = new StdConverter();
 delegateSerializer = new SimpleDelegateSerializer();
 serializer = new StdDelegatingSerializer(converter, null, delegateSerializer);
 }

 @Test
 public void testEmptyConstructor() {
 assertEquals(Object.class, serializer.getType());
 assertNull(serializer.getDelegatee());
 }

 @Test
 public void testConstructorWithConverter() throws JsonMappingException {
 assertEquals(Object.class, serializer.getType());
 assertNull(serializer.getDelegatee());
 }

 @Test
 public void testConstructorWithClass() throws JsonMappingException {
 assertEquals(String.class, serializer.getType());
 assertNull(serializer.getDelegatee());
 }

 @Test
 public void testConstructorWithConverterDelegateSerializer() throws JsonMappingException {
 assertEquals(String.class, serializer.getType());
 assertEquals(delegateSerializer, serializer.getDelegatee());
 }

 @Test
 public void testConvertValue() throws JsonMappingException {
 Object value = new Object();
 Object delegateValue = serializer.convertValue(value);
 assertEquals(delegateValue.getClass(), delegateSerializer.handleTypeSerialization(null, null, null));
 }

 @Test(expected = JsonMappingException.class)
 public void testConvertValueWithNullConverter() {
 Object value = new Object();
 Converter<Object, ?> nullConverter = null;
 StdDelegatingSerializer serializer = new StdDelegatingSerializer(nullConverter, null, delegateSerializer);
 serializer.convertValue(value);
 }

 @Test(expected = IllegalStateException.class)
 public void testConstructWithNullDelegateSerializer() {
 Converter<Object, ?> nullConverter = new StdConverter();
 StdDelegatingSerializer serializer = new StdDelegatingSerializer(nullConverter, null, null);
 }

 @Test(expected = IllegalStateException.class)
 public void testConstructWithNullDelegateType() {
 Converter<Object, ?> nullConverter = new StdConverter();
 JsonSerializer<Object> nullSerializer = new SimpleDelegateSerializer();
 StdDelegatingSerializer serializer = new StdDelegatingSerializer(nullConverter, null, nullSerializer);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConstructWithSameTypes() {
 Converter<Object, String> converter = new StdConverter<>();
 JsonSerializer<String> delegateSerializer = new SimpleDelegateSerializer<>();
 StdDelegatingSerializer serializer = new StdDelegatingSerializer(converter, String.class, delegateSerializer);
 }

 @Test
 public void testSerializeNullValue() throws JsonProcessingException {
 ObjectNode result = objectMapper.writeValueAsTree(serializer.serialize(null, objectMapper.getFactory(), null));
 assertEquals("null", result.toString());
 }

 @Test
 public void testSerializeEmptyValue() throws JsonProcessingException {
 ObjectNode result = objectMapper.writeValueAsTree(serializer.serialize(new Object(), objectMapper.getFactory(), null));
 assertEquals("", result.toString());
 }

 @Test
 public void testSerializeSimpleValue() throws JsonProcessingException {
 ObjectNode result = objectMapper.writeValueAsTree(serializer.serialize(new String("test"), objectMapper.getFactory(), null));
 assertEquals("\"test\"", result.toString());
 }

 @Test
 public void testSerializeComplexValue() throws JsonProcessingException {
 ObjectNode result = objectMapper.writeValueAsTree(serializer.serialize(new TestClass("test"), objectMapper.getFactory(), null));
 assertEquals("{\"stringField\":\"test\"}", result.toString());
 }

 @Test
 public void testSerializeWithType() throws JsonProcessingException {
 ObjectNode result = objectMapper.writeValueAsTree(serializer.serializeWithType(new TestClass("test"), objectMapper.getFactory(), null, objectMapper.getTypeFactory().constructType(String.class)));
 assertEquals("\"test\"", result.toString());
 }

 @Test(expected = JsonGenerationException.class)
 public void testSerializeWithNullDelegateSerializer() throws JsonProcessingException {
 ObjectNode result = objectMapper.writeValueAsTree(serializer.serializeWithType(new TestClass("test"), objectMapper.getFactory(), null, objectMapper.getTypeFactory().constructType(String.class)));
 }

 @Test
 public void testGetSchema() throws JsonMappingException {
 ObjectNode schema = serializer.getSchema(objectMapper.getFactory(), null);
 assertEquals("null", schema.toString());
 }

 @Test
 public void testAcceptJsonFormatVisitor() throws JsonMappingException {
 ObjectNode schema = objectMapper.createObjectNode();
 schema.putPOJO("class", TestClass.class.getName());
 JsonFormatterVisitor visitor = new JsonFormatterVisitor(schema);
 serializer.acceptJsonFormatVisitor(visitor, null);
 assertEquals("testStringField", schema.get("propertyName"));
 assertEquals("java.lang.String", schema.get("type"));
 }

 private static class SimpleDelegateSerializer extends StdSerializer<Object> {
 @Override
 public void serialize(Object value, JsonGenerator gen, SerializerProvider provider) throws IOException {
 gen.writeString("delegate");
 }
 }

 private static class TestClass {
 private final String stringField;

 public TestClass(String stringField) {
 this.stringField = stringField;
 }

 public String getStringField() {
 return stringField;
 }
 }

 private static class StdConverter implements Converter<Object, ?> {
 @Override
 public Object convert(Object value) {
 return value;
 }

 @Override
 public Type getInputType(SerializerProvider serializers) {
 return Object.class;
 }

 @Override
 public Type getOutputType(SerializerProvider serializers, Type typeHint) {
 return null;
 }
 }

}