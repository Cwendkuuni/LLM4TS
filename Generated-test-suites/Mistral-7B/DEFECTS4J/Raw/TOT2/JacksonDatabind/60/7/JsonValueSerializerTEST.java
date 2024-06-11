import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Arrays;

public class JsonValueSerializerTest {

 private ObjectMapper objectMapper;

 @SuppressWarnings("unchecked")
 @Before
 public void setUp() throws Exception {
 objectMapper = new ObjectMapper();
 }

 @Test
 public void testJsonValueSerializer_BasicUsage() throws IOException {
 Class<JsonValueSerializer> jsonValueSerializerClass = JsonValueSerializer.class;
 AnnotatedMethod accessorMethod = JsonValue.class.getDeclaredMethod("getValue");
 JsonSerializer<Object> valueSerializer = StdSerializer.finder.findValueSerializer(String.class, null);
 JsonValueSerializer jsonValueSerializer = new JsonValueSerializer(accessorMethod, valueSerializer);
 ObjectMapper objectMapper = new ObjectMapper();
 ObjectWriter writer = objectMapper.writer();

 Object testObject = new TestObject("Test String");
 JsonNode actualJsonNode = writer.writeValueAsTree(testObject);
 JsonNode expectedJsonNode = Json.createObjectNode();
 expectedJsonNode.put("value", "Test String");
 assertEquals(expectedJsonNode, actualJsonNode);

 JsonSerializer<Object> serializedJsonValueSerializer = objectMapper.findValueSerializer(JsonValueSerializer.class);
 JsonGenerator jsonGenerator = new JsonFactory().createGenerator(System.out);
 serializedJsonValueSerializer.serialize(testObject, jsonGenerator, null);
 assertEquals("{\"value\":\"Test String\"}", jsonGenerator.getOutputContent().toString());
 }

 @Test
 public void testJsonValueSerializer_NullValue() throws IOException {
 TestObject testObject = new TestObject(null);
 JsonNode actualJsonNode = objectMapper.writer().writeValueAsTree(testObject);
 JsonNode expectedJsonNode = Json.createObjectNode();
 assertEquals(expectedJsonNode, actualJsonNode);

 JsonSerializer<Object> serializedJsonValueSerializer = objectMapper.findValueSerializer(JsonValueSerializer.class);
 JsonGenerator jsonGenerator = new JsonFactory().createGenerator(System.out);
 serializedJsonValueSerializer.serialize(testObject, jsonGenerator, null);
 assertEquals("{}", jsonGenerator.getOutputContent().toString());
 }

 @Test
 public void testJsonValueSerializer_EdgeCase_Array() throws IOException {
 TestObject[] testObjects = {new TestObject("Test 1"), new TestObject("Test 2")};
 ObjectMapper objectMapper = new ObjectMapper();
 ObjectWriter writer = objectMapper.writer();
 ObjectNode actualJsonNode = (ObjectNode) writer.writeValueAsTree(testObjects);
 ObjectNode expectedJsonNode = Json.createObjectNode();
 expectedJsonNode.put("value", Arrays.asList("Test 1", "Test 2"));
 assertEquals(expectedJsonNode, actualJsonNode);

 JsonSerializer<Object> serializedJsonValueSerializer = objectMapper.findValueSerializer(JsonValueSerializer.class);
 JsonGenerator jsonGenerator = new JsonFactory().createGenerator(System.out);
 serializedJsonValueSerializer.serialize(testObjects, jsonGenerator, null);
 assertEquals("[{\"value\":\"Test 1\"},{\"value\":\"Test 2\"}]", jsonGenerator.getOutputContent().toString());
 }

 @Test
 public void testJsonValueSerializer_EdgeCase_List() throws IOException {
 TestObjectList testObjectList = new TestObjectList();
 testObjectList.add(new TestObject("Test 1"));
 testObjectList.add(new TestObject("Test 2"));
 ObjectMapper objectMapper = new ObjectMapper();
 ObjectWriter writer = objectMapper.writer();
 ObjectNode actualJsonNode = (ObjectNode) writer.writeValueAsTree(testObjectList);
 ObjectNode expectedJsonNode = Json.createObjectNode();
 expectedJsonNode.put("value", Arrays.asList("Test 1", "Test 2"));
 assertEquals(expectedJsonNode, actualJsonNode);

 JsonSerializer<Object> serializedJsonValueSerializer = objectMapper.findValueSerializer(JsonValueSerializer.class);
 JsonGenerator jsonGenerator = new JsonFactory().createGenerator(System.out);
 serializedJsonValueSerializer.serialize(testObjectList, jsonGenerator, null);
 assertEquals("[{\"value\":\"Test 1\"},{\"value\":\"Test 2\"}]", jsonGenerator.getOutputContent().toString());
 }

 @Test
 public void testJsonValueSerializer_ExceptionHandling_NullSerializer() throws IOException {
 TestObject testObject = new TestObject(null);
 JsonSerializer<Object> nullSerializer = null;
 JsonValueSerializer jsonValueSerializer = new JsonValueSerializer(objectMapper.getSerializationConfig().findBeanPropertyFilter(SimpleBeanPropertyFilter.FilterFlag.ALL), nullSerializer);
 JsonGenerator jsonGenerator = new JsonFactory().createGenerator(System.out);
 try {
 jsonValueSerializer.serialize(testObject, jsonGenerator, null);
 fail();
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test
 public void testJsonValueSerializer_ExceptionHandling_NullValueMethod() throws IOException {
 TestObject testObject = new TestObject(null);
 AnnotatedMethod nullAccessorMethod = null;
 JsonSerializer<Object> valueSerializer = StdSerializer.finder.findValueSerializer(String.class, null);
 JsonValueSerializer jsonValueSerializer = new JsonValueSerializer(nullAccessorMethod, valueSerializer);
 JsonGenerator jsonGenerator = new JsonFactory().createGenerator(System.out);
 try {
 jsonValueSerializer.serialize(testObject, jsonGenerator, null);
 fail();
 } catch (NoSuchMethodException e) {
 // Expected
 }
 }

}

class TestObject {
 @JsonValue
 public String getValue() {
 return value;
 }

 private String value;

 public TestObject(String value) {
 this.value = value;
 }
}

class TestObjectList extends ArrayList<TestObject> {}