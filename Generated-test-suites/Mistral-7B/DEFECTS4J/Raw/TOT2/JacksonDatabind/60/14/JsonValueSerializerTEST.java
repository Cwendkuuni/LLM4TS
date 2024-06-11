import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.*;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class JsonValueSerializerTest {

 private JsonFactory jsonFactory;
 private ObjectMapper mapper;
 private JsonValueSerializer jsonValueSerializer;

 @Before
 public void setup() throws JsonProcessingException {
 jsonFactory = new JsonFactory();
 mapper = new ObjectMapper(jsonFactory);
 jsonValueSerializer = new JsonValueSerializer(null, null, null, false);
 }

 @Test
 public void testSerializeNull() throws JsonProcessingException {
 Object testObject = null;
 JsonGenerator gen = jsonFactory.createGenerator(System.out);
 jsonValueSerializer.serialize(testObject, gen, mapper.getSerializerProvider());
 String result = gen.getOutputContentAsString();
 assertEquals("null", result);
 }

 @Test
 public void testSerializeBasicType() throws JsonProcessingException {
 Object testObject = 42;
 String expected = "42";
 JsonGenerator gen = jsonFactory.createGenerator(System.out);
 jsonValueSerializer.serialize(testObject, gen, mapper.getSerializerProvider());
 String result = gen.getOutputContentAsString();
 assertEquals(expected, result);
 }

 @Test
 public void testSerializeObject() throws JsonProcessingException {
 String testObjectName = "testObject";
 String expected = "\"" + testObjectName + "\"";
 JsonGenerator gen = jsonFactory.createGenerator(System.out);
 jsonValueSerializer.serialize(testObjectName, gen, mapper.getSerializerProvider());
 String result = gen.getOutputContentAsString();
 assertEquals(expected, result);
 }

 @Test
 public void testSerializeMethodCall() throws JsonProcessingException {
 class TestClass {
 @JsonValue
 public int getValue() {
 return 42;
 }
 }
 Object testObject = new TestClass();
 String expected = "42";
 JsonGenerator gen = jsonFactory.createGenerator(System.out);
 jsonValueSerializer.serialize(testObject, gen, mapper.getSerializerProvider());
 String result = gen.getOutputContentAsString();
 assertEquals(expected, result);
 }

 @Test(expected = JsonMappingException.class)
 public void testSerializeNonExistentMethod() throws JsonProcessingException {
 class TestClass {
 @JsonValue
 public int getNonExistentMethod() {
 throw new RuntimeException("Method does not exist.");
 }
 }
 Object testObject = new TestClass();
 JsonGenerator gen = jsonFactory.createGenerator(System.out);
 jsonValueSerializer.serialize(testObject, gen, mapper.getSerializerProvider());
 }

 @Test
 public void testSerializeMethodWithException() throws JsonProcessingException {
 class TestClass {
 @JsonValue
 public int getValue() throws IOException {
 throw new IOException("Error occurred.");
 }
 }
 Object testObject = new TestClass();
 JsonGenerator gen = jsonFactory.createGenerator(System.out);
 jsonValueSerializer.serialize(testObject, gen, mapper.getSerializerProvider());
 }

 @Test(expected = JsonMappingException.class)
 public void testSerializeMethodWithError() throws JsonProcessingException {
 class TestClass {
 @JsonValue
 public Error getValue() {
 throw new Error("Error occurred.");
 }
 }
 Object testObject = new TestClass();
 JsonGenerator gen = jsonFactory.createGenerator(System.out);
 jsonValueSerializer.serialize(testObject, gen, mapper.getSerializerProvider());
 }

 @Test
 public void testSerializeMethodWithNullReturn() throws JsonProcessingException {
 class TestClass {
 @JsonValue
 public Integer getValue() {
 return null;
 }
 }
 Object testObject = new TestClass();
 JsonGenerator gen = jsonFactory.createGenerator(System.out);
 jsonValueSerializer.serialize(testObject, gen, mapper.getSerializerProvider());
 String result = gen.getOutputContentAsString();
 assertNull(result);
 }

 @Test
 public void testSerializeMethodWithArrayReturn() throws JsonProcessingException {
 class TestClass {
 @JsonValue
 public int[] getValue() {
 return new int[] {1, 2, 3};
 }
 }
 Object testObject = new TestClass();
 JsonGenerator gen = jsonFactory.createGenerator(System.out);
 jsonValueSerializer.serialize(testObject, gen, mapper.getSerializerProvider());
 String result = gen.getOutputContentAsString();
 assertEquals("[1, 2, 3]", result);
 }

 @Test
 public void testSerializeMethodWithListReturn() throws JsonProcessingException {
 class TestClass {
 @JsonValue
 public java.util.List<Integer> getValue() {
 return new ArrayList<Integer>(){{add(1); add(2); add(3);}};
 }
 }
 Object testObject = new TestClass();
 JsonGenerator gen = jsonFactory.createGenerator(System.out);
 jsonValueSerializer.serialize(testObject, gen, mapper.getSerializerProvider());
 String result = gen.getOutputContentAsString();
 assertEquals("[1, 2, 3]", result);
 }

 @Test
 public void testSerializeMethodWithObjectReturn() throws JsonProcessingException {
 class TestClass {
 @JsonValue
 public TestClassObj getValue() {
 return new TestClassObj();
 }
 }
 class TestClassObj {
 @JsonValue
 public int getValue() {
 return 42;
 }
 }
 Object testObject = new TestClass();
 JsonGenerator gen = jsonFactory.createGenerator(System.out);
 jsonValueSerializer.serialize(testObject, gen, mapper.getSerializerProvider());
 String result = gen.getOutputContentAsString();
 assertEquals("42", result);
 }

 @Test
 public void testSerializeMethodWithCustomSerializer() throws JsonProcessingException {
 class TestClass {
 @JsonValue
 public TestClassObj getValue() {
 return new TestClassObj();
 }
 }
 class TestClassObj {
 @JsonSerializer(using = MyCustomSerializer.class)
 public int getCustomValue() {
 return 42;
 }
 }
 class MyCustomSerializer extends JsonSerializer<TestClassObj> {
 @Override
 public void serialize(TestClassObj value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
 gen.writeString("CustomValue");
 }
 }
 Object testObject = new TestClass();
 JsonGenerator gen = jsonFactory.createGenerator(System.out);
 jsonValueSerializer.serialize(testObject, gen, mapper.getSerializerProvider());
 String result = gen.getOutputContentAsString();
 assertEquals("CustomValue", result);
 }
}