import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.jsonschema.SchemaAware;
import org.junit.Test;
import static org.junit.Assert.*;

public class JsonValueSerializerTest {

 private static final Object TEST_OBJECT = new TestClass();

 @Test
 public void testSerialization() throws JsonProcessingException {
 JsonFactory jsonFactory = new JsonFactory();
 JsonSerializer<Object> jsonSerializer = new JsonValueSerializer(
 _accessorMethod("getValue", TestClass.class, TestClass.class), null);
 ObjectWriter objectWriter = jsonFactory.writer();
 objectWriter.writeValue(System.out, jsonSerializer.serialize(TEST_OBJECT));
 }

 @Test
 public void testSerializationWithContextualizedSerializer() throws JsonProcessingException {
 JsonFactory jsonFactory = new JsonFactory();
 JsonSerializer<Object> jsonSerializer = new JsonValueSerializer(
 _accessorMethod("getValue", TestClass.class, TestClass.class), null,
 new TestBeanProperty(), new JsonSerializer<String>() {
 @Override
 public void serialize(String value, JsonGenerator gen, SerializerProvider provider) throws IOException {
 gen.writeString(value);
 }
 }, false);
 ObjectWriter objectWriter = jsonFactory.writer();
 objectWriter.writeValue(System.out, jsonSerializer.serialize(TEST_OBJECT));
 }

 @Test
 public void testSerializationWithCustomSerializer() throws JsonProcessingException {
 JsonFactory jsonFactory = new JsonFactory();
 ObjectWriter objectWriter = jsonFactory.writer();
 ObjectWriter objectWriterWithCustomSerializer = jsonFactory.writerWith(
 new JsonSerializer<Object>() {
 @Override
 public void serialize(Object value, JsonGenerator gen, SerializerProvider provider) throws IOException {
 gen.writeString("custom_serializer");
 }
 });
 JsonSerializer<Object> jsonSerializer = new JsonValueSerializer(
 _accessorMethod("getValue", TestClass.class, TestClass.class),
 jsonSerializerWithCustomSerializer, null, false);
 objectWriterWithCustomSerializer.writeValue(System.out, jsonSerializer.serialize(TEST_OBJECT));
 }

 @Test
 public void testSerializationWithEnum() throws JsonProcessingException {
 JsonFactory jsonFactory = new JsonFactory();
 JsonSerializer<Object> jsonSerializer = new JsonValueSerializer(
 _accessorMethod("getValue", TestEnumClass.class, TestEnumClass.class), null);
 ObjectWriter objectWriter = jsonFactory.writer();
 objectWriter.writeValue(System.out, jsonSerializer.serialize(new TestEnumClass("TestValue")));
 }

 @Test(expected = JsonProcessingException.class)
 public void testNullValue() throws JsonProcessingException {
 JsonFactory jsonFactory = new JsonFactory();
 JsonSerializer<Object> jsonSerializer = new JsonValueSerializer(
 _accessorMethod("getValue", TestClass.class, TestClass.class), null);
 ObjectWriter objectWriter = jsonFactory.writer();
 jsonSerializer.serialize(null, objectWriter.getOutputFactory(), null);
 }

 @Test(expected = InvocationTargetException.class)
 public void testExceptionInAccessMethod() throws JsonProcessingException, IllegalAccessException, InstantiationException {
 JsonFactory jsonFactory = new JsonFactory();
 AnnotatedMethod accessorMethod = new AnnotatedMethod(TestClass.class.getMethod("getValueException"), TestClass.class);
 JsonSerializer<Object> jsonSerializer = new JsonValueSerializer(
 accessorMethod, null
 );
 ObjectWriter objectWriter = jsonFactory.writer();
 Object o = new TestClass();
 jsonSerializer.serialize(o, objectWriter.getOutputFactory(), null);
 fail("Method should have thrown InvocationTargetException");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidType() {
 JsonSerializer<Object> jsonSerializer = new JsonValueSerializer(
 _accessorMethod("getValue", TestClass.class, String.class), null
 );
 try {
 jsonSerializer.serialize(TEST_OBJECT, null, null);
 fail("Method should have thrown IllegalArgumentException");
 } catch (JsonProcessingException e) {
 // expected
 }
 }

 @Test
 public void testGetSchema() throws JsonProcessingException {
 JsonFactory jsonFactory = new JsonFactory();
 JsonSerializer<Object> jsonSerializer = new JsonValueSerializer(
 _accessorMethod("getValue", TestClass.class, TestClass.class), null);
 SchemaAware schemaAware = (SchemaAware) jsonSerializer;
 JsonNode schemaNode = jsonSerializer.getSchema(jsonFactory.getSerializerProvider(), null);
 // check the schema node
 // ...
 }

 private AnnotatedMethod _accessorMethod(String methodName, Class<?> returnType, Class<?> clazz) {
 return new AnnotatedMethod(clazz.getMethod(methodName), clazz);
 }

}

class TestClass {
 @JsonValue
 public int getValue() { return 42; }

 @JsonValue
 public String getValueException() throws Exception { throw new Exception(); }
}

enum TestEnumClass {
 @JsonValue("TestValue")
 TEST_VALUE
}