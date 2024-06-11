import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class JsonValueSerializerTest {

 private ObjectMapper objectMapper;

 @Before
 public void setup() {
 objectMapper = new ObjectMapper();
 }

 @Test
 public void testSerializerForSimpleValueMethod() throws JsonProcessingException {
 JsonValueSerializer serializer = new JsonValueSerializer(
 new AnnotatedMethod(MyClass.class.getMethod("getValue")), null);
 JsonGenerator generator = objectMapper.getFactory().createGenerator(System.out);
 Object testObject = new MyClass();
 serializer.serialize(testObject, generator, objectMapper.getSerializerProvider());
 String expectedResult = "{\"value\":1}";
 String actualResult = generator.getOutputBuffer().toString();
 assertEquals(expectedResult, actualResult);
 }

 @Test
 public void testSerializerForMethodThatReturnsNull() throws JsonProcessingException {
 JsonValueSerializer serializer = new JsonValueSerializer(
 new AnnotatedMethod(MyClass.class.getMethod("getValue")), null);
 JsonGenerator generator = objectMapper.getFactory().createGenerator(System.out);
 Object testObject = new MyClassWithNullValue();
 serializer.serialize(testObject, generator, objectMapper.getSerializerProvider());
 assertTrue(generator.getOutputBuffer().toString().isEmpty());
 }

 @Test
 public void testSerializerForMethodWithException() throws JsonMappingException, InstantiationException, IllegalAccessException, InvocationTargetException {
 JsonValueSerializer serializer = new JsonValueSerializer(
 new AnnotatedMethod(MyExceptionClass.class.getMethod("getValue")), null);
 Object testObject = new MyExceptionClass();
 JsonGenerator generator = objectMapper.getFactory().createGenerator(System.out);
 try {
 serializer.serialize(testObject, generator, objectMapper.getSerializerProvider());
 fail("Expected exception to be thrown");
 } catch (JsonMappingException e) {
 assertEquals("Unexpected exception thrown", "exceptionMessage", e.getOriginalMessage());
 }
 }

 @Test
 public void testSerializerForMethodThatReturnsEnum() throws JsonProcessingException {
 JsonValueSerializer serializer = new JsonValueSerializer(
 new AnnotatedMethod(MyEnumClass.class.getMethod("getValue")), null);
 JsonGenerator generator = objectMapper.getFactory().createGenerator(System.out);
 MyEnumClass testObject = new MyEnumClass("VALUE_1");
 serializer.serialize(testObject, generator, objectMapper.getSerializerProvider());
 String expectedResult = "{\"value\":\"VALUE_1\"}";
 String actualResult = generator.getOutputBuffer().toString();
 assertEquals(expectedResult, actualResult);
 }

 @Test
 public void testSerializerForMethodThatReturnsString() throws JsonProcessingException {
 JsonValueSerializer serializer = new JsonValueSerializer(
 new AnnotatedMethod(MyStringClass.class.getMethod("getValue")), null);
 JsonGenerator generator = objectMapper.getFactory().createGenerator(System.out);
 MyStringClass testObject = new MyStringClass("Test String");
 serializer.serialize(testObject, generator, objectMapper.getSerializerProvider());
 String expectedResult = "{\"value\":\"Test String\"}";
 String actualResult = generator.getOutputBuffer().toString();
 assertEquals(expectedResult, actualResult);
 }

 @Test
 public void testSerializerForMethodThatReturnsCustomType() throws JsonProcessingException {
 JsonValueSerializer serializer = new JsonValueSerializer(
 new AnnotatedMethod(MyCustomClass.class.getMethod("getValue")), null);
 JsonSerializer<MyCustomClass> customSerializer = objectMapper.findValueSerializer(MyCustomClass.class, null);
 serializer.setValueSerializer(customSerializer);
 JsonGenerator generator = objectMapper.getFactory().createGenerator(System.out);
 MyCustomClass testObject = new MyCustomClass();
 serializer.serialize(testObject, generator, objectMapper.getSerializerProvider());
 String expectedResult = "{\"value\":\"Custom Object\"}";
 String actualResult = generator.getOutputBuffer().toString();
 assertEquals(expectedResult, actualResult);
 }

 @Test
 public void testSerializerForMethodThatReturnsArray() throws JsonProcessingException {
 JsonValueSerializer serializer = new JsonValueSerializer(
 new AnnotatedMethod(MyArrayClass.class.getMethod("getValue")), null);
 JsonSerializer<int[]> arraySerializer = objectMapper.findSerializerFactory().findValueSerializer(int[].class, null);
 serializer.setValueSerializer(arraySerializer);
 JsonGenerator generator = objectMapper.getFactory().createGenerator(System.out);
 MyArrayClass testObject = new MyArrayClass(new int[] {1, 2, 3});
 serializer.serialize(testObject, generator, objectMapper.getSerializerProvider());
 String expectedResult = "{\"value\":[1,2,3]}";
 String actualResult = generator.getOutputBuffer().toString();
 assertEquals(expectedResult, actualResult);
 }

 @Test
 public void testSerializerForMethodThatReturnsComplexType() throws JsonProcessingException {
 JsonValueSerializer serializer = new JsonValueSerializer(
 new AnnotatedMethod(MyComplexClass.class.getMethod("getValue")), null);
 JsonSerializer<MyComplexInnerClass> innerSerializer = objectMapper.findValueSerializer(MyComplexInnerClass.class, null);
 serializer.setValueSerializer(innerSerializer);
 JsonGenerator generator = objectMapper.getFactory().createGenerator(System.out);
 MyComplexClass testObject = new MyComplexClass();
 serializer.serialize(testObject, generator, objectMapper.getSerializerProvider());
 String expectedResult = "{\"value\":{\"innerValue\":1}}";
 String actualResult = generator.getOutputBuffer().toString();
 assertEquals(expectedResult, actualResult);
 }

 @Test
 public void testSerializerForMethodWithCustomSerializer() throws JsonProcessingException {
 JsonValueSerializer serializer = new JsonValueSerializer(
 new AnnotatedMethod(MyCustomSerializerClass.class.getMethod("getValue")), null);
 JsonSerializer<MyCustomSerializerClass> customSerializer = new CustomSerializer();
 serializer.setValueSerializer(customSerializer);
 JsonGenerator generator = objectMapper.getFactory().createGenerator(System.out);
 MyCustomSerializerClass testObject = new MyCustomSerializerClass();
 serializer.serialize(testObject, generator, objectMapper.getSerializerProvider());
 String expectedResult = "{\"value\":\"Custom Serialized Value\"}";
 String actualResult = generator.getOutputBuffer().toString();
 assertEquals(expectedResult, actualResult);
 }

 @Test
 public void testSerializerForMethodWithDefaultSerializer() throws JsonProcessingException {
 JsonValueSerializer serializer = new JsonValueSerializer(
 new AnnotatedMethod(MyDefaultSerializerClass.class.getMethod("getValue")), null);
 JsonGenerator generator = objectMapper.getFactory().createGenerator(System.out);
 MyDefaultSerializerClass testObject = new MyDefaultSerializerClass();
 serializer.serialize(testObject, generator, objectMapper.getSerializerProvider());
 String expectedResult = "{\"value\":42}";
 String actualResult = generator.getOutputBuffer().toString();
 assertEquals(expectedResult, actualResult);
 }

 @Test
 public void testSerializerForMethodWithCustomTypeId() throws JsonProcessingException {
 JsonValueSerializer serializer = new JsonValueSerializer(
 new AnnotatedMethod(MyCustomTypeIdClass.class.getMethod("getValue")), null);
 JsonSerializer<MyCustomTypeIdClass> customSerializer = new CustomTypeIdSerializer();
 serializer.setValueSerializer(customSerializer);
 JsonGenerator generator = objectMapper.getFactory().createGenerator(System.out);
 MyCustomTypeIdClass testObject = new MyCustomTypeIdClass();
 serializer.serialize(testObject, generator, objectMapper.getSerializerProvider());
 String expectedResult = "{\"value\":\"MyCustomTypeIdClass\",\"innerValue\":\"Test Inner Value\"}";
 String actualResult = generator.getOutputBuffer().toString();
 assertEquals(expectedResult, actualResult);
 }

 @Test
 public void testSerializerForMethodWithContextualSerialization() throws JsonProcessingException {
 JsonValueSerializer serializer = new JsonValueSerializer(
 new AnnotatedMethod(MyContextualSerializationClass.class.getMethod("getValue")), null, true, null);
 JsonSerializer<MyContextualSerializationClass> contextualSerializer = new ContextualSerializationSerializer();
 serializer.setValueSerializer(contextualSerializer);
 JsonGenerator generator = objectMapper.getFactory().createGenerator(System.out);
 MyContextualSerializationClass testObject = new MyContextualSerializationClass();
 serializer.serialize(testObject, generator, objectMapper.getSerializerProvider());
 String expectedResult = "{\"value\":4}";
 String actualResult = generator.getOutputBuffer().toString();
 assertEquals(expectedResult, actualResult);
 }

}