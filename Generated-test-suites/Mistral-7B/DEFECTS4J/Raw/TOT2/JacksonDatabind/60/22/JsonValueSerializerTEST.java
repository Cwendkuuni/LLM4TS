import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.jsonschema.JsonSchema;
import com.fasterxml.jackson.databind.ser.std.JsonValueSerializer;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class JsonValueSerializerTest {

 private ObjectMapper objectMapper;

 @BeforeAll
 public void setup() throws Exception {
 objectMapper = new ObjectMapper();
 }

 @Test
 public void testJsonValueSerializer_NormalCase() {
 Class<MyClass> clazz = MyClass.class;
 MyClass obj = new MyClass();
 ObjectMapper mapper = new ObjectMapper();
 JsonSerializer<Object> valueSerializer = mapper.findValueSerializer(String.class, null);
 JsonValueSerializer jsonValueSerializer = new JsonValueSerializer(
 new AnnotatedMethod(MyClass.class.getMethod("getValue"), clazz), valueSerializer);
 JsonFormatVisitorWrapper jsonFormatVisitorWrapper = objectMapper.schemaParser();
 jsonValueSerializer.acceptJsonFormatVisitor(jsonFormatVisitorWrapper, clazz);
 JsonSchema schema = jsonFormatVisitorWrapper.getSchema();
 ObjectWriter writer = mapper.writerWithDefaultPrettyPrinter();
 String json = writer.writeValueAsString(obj);
 assertNotNull(json);
 assertFalse(json.isEmpty());
 assertEquals(MyClass.class.getSimpleName(), schema.getTitle());
 }

 @Test
 public void testJsonValueSerializer_NullValue() throws Exception {
 Class<MyClass> clazz = MyClass.class;
 MyClass obj = new MyClass();
 ObjectMapper mapper = new ObjectMapper();
 JsonSerializer<Object> valueSerializer = mapper.findValueSerializer(String.class, null);
 JsonValueSerializer jsonValueSerializer = new JsonValueSerializer(
 new AnnotatedMethod(MyClass.class.getMethod("getValue"), clazz), valueSerializer);
 ObjectWriter writer = mapper.writerWithDefaultPrettyPrinter();
 String json = writer.writeValueAsString(new MyClass(null));
 assertEquals("null", json);
 }

 @Test
 public void testJsonValueSerializer_EmptyList() throws Exception {
 Class<MyClass> clazz = MyClass.class;
 MyClass obj = new MyClass(new LinkedList<>());
 ObjectMapper mapper = new ObjectMapper();
 JsonSerializer<Object> valueSerializer = mapper.findValueSerializer(ArrayList.class, null);
 JsonValueSerializer jsonValueSerializer = new JsonValueSerializer(
 new AnnotatedMethod(MyClass.class.getMethod("getValue"), clazz), valueSerializer);
 ObjectWriter writer = mapper.writerWithDefaultPrettyPrinter();
 String json = writer.writeValueAsString(obj);
 assertNotNull(json);
 assertFalse(json.isEmpty());
 assertJsonStringEquals("[]", json);
 }

 @Test
 public void testJsonValueSerializer_EmptyMap() throws Exception {
 Class<MyClass> clazz = MyClass.class;
 MyClass obj = new MyClass(new LinkedHashMap<>());
 ObjectMapper mapper = new ObjectMapper();
 JsonSerializer<Object> valueSerializer = mapper.findValueSerializer(Map.class, null);
 JsonValueSerializer jsonValueSerializer = new JsonValueSerializer(
 new AnnotatedMethod(MyClass.class.getMethod("getValue"), clazz), valueSerializer);
 ObjectWriter writer = mapper.writerWithDefaultPrettyPrinter();
 String json = writer.writeValueAsString(obj);
 assertNotNull(json);
 assertFalse(json.isEmpty());
 assertJsonStringEquals("{}", json);
 }

 @Test
 public void testJsonValueSerializer_InvalidJson() throws Exception {
 Class<MyClass> clazz = MyClass.class;
 MyClass obj = new MyClass();
 ObjectMapper mapper = new ObjectMapper();
 JsonSerializer<Object> valueSerializer = mapper.findValueSerializer(String.class, null);
 JsonValueSerializer jsonValueSerializer = new JsonValueSerializer(
 new AnnotatedMethod(MyClass.class.getMethod("getValue"), clazz), valueSerializer);
 String invalidJson = "{\"name\":[\"John\", \"Doe\"]}";
 JsonParseException exception = assertThrows(JsonParseException.class, () -> {
 objectMapper.readValue(invalidJson, clazz);
 });
 assertEquals("Unexpected token (START_ARRAY)", exception.getOriginalMessage());
 JsonFormatVisitorWrapper jsonFormatVisitorWrapper = objectMapper.schemaParser();
 jsonValueSerializer.acceptJsonFormatVisitor(jsonFormatVisitorWrapper, clazz);
 JsonSchema schema = jsonFormatVisitorWrapper.getSchema();
 String json = objectMapper.writeValueAsString(obj);
 assertJsonStringEquals(json, schema.getDescription());
 }

 @Test
 public void testJsonValueSerializer_ClassCastException() throws Exception {
 Class<MyClass> clazz = MyClass.class;
 MyClass obj = new MyClass(new ArrayList<>());
 ObjectMapper mapper = new ObjectMapper();
 JsonSerializer<Object> valueSerializer = mapper.findValueSerializer(Object[].class, null);
 JsonValueSerializer jsonValueSerializer = new JsonValueSerializer(
 new AnnotatedMethod(MyClass.class.getMethod("getValue"), clazz), valueSerializer);
 assertThrows(ClassCastException.class, () -> {
 jsonValueSerializer.createContextual(null, null);
 });
 }

 @Test
 public void testJsonValueSerializer_NullMethod() throws Exception {
 Class<MyClass> clazz = MyClass.class;
 MyClass obj = new MyClass();
 ObjectMapper mapper = new ObjectMapper();
 JsonSerializer<Object> valueSerializer = mapper.findValueSerializer(String.class, null);
 JsonValueSerializer jsonValueSerializer = new JsonValueSerializer(null, valueSerializer);
 assertNull(jsonValueSerializer);
 }

 @Test
 public void testJsonValueSerializer_NullValueSerializer() throws Exception {
 Class<MyClass> clazz = MyClass.class;
 MyClass obj = new MyClass();
 ObjectMapper mapper = new ObjectMapper();
 JsonValueSerializer jsonValueSerializer = new JsonValueSerializer(
 new AnnotatedMethod(MyClass.class.getMethod("getValue"), clazz), null);
 assertNull(jsonValueSerializer);
 }

}

class MyClass {
 @JsonSerialize(using = JsonValueSerializer.class)
 private Object value;

 public MyClass() {
 }

 public MyClass(Object value) {
 this.value = value;
 }

 public Object getValue() {
 return value;
 }
}