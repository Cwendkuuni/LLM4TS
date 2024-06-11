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
 public void testJsonValueSerializer_BasicUsage() throws JsonProcessingException {
 ObjectMapper objectMapper = new ObjectMapper();
 JsonValueSerializer jsonValueSerializer = objectMapper.findValueSerializer(Object.class, null);

 // Given
 Object sourceObject = new MyClassWithJsonValueAnnotation();

 // When
 JsonGenerator jsonGenerator = new JsonFactory().createGenerator(System.out);
 jsonValueSerializer.serialize(sourceObject, jsonGenerator, objectMapper.getSerializerProvider());

 // Then
 String expectedJson = "{\"value\":\"MyValue\"}";
 assertTrue(jsonGenerator.toString().equals(expectedJson));
 }

 @Test
 public void testJsonValueSerializer_NullValue() throws JsonProcessingException {
 ObjectMapper objectMapper = new ObjectMapper();
 JsonValueSerializer jsonValueSerializer = objectMapper.findValueSerializer(Object.class, null);

 // Given
 Object sourceObject = null;

 // When
 JsonGenerator jsonGenerator = new JsonFactory().createGenerator(System.out);
 jsonValueSerializer.serialize(sourceObject, jsonGenerator, objectMapper.getSerializerProvider());

 // Then
 String expectedJson = "null";
 assertTrue(jsonGenerator.toString().equals(expectedJson));
 }

 @Test(expected = JsonMappingException.class)
 public void testJsonValueSerializer_MethodDoesNotExist() throws JsonProcessingException {
 ObjectMapper objectMapper = new ObjectMapper();
 JsonValueSerializer jsonValueSerializer = objectMapper.findValueSerializer(Object.class, null);

 // Given
 Object sourceObject = new MyClassWithoutJsonValueAnnotation();

 // When
 // Then exception is thrown since the method does not exist
 jsonValueSerializer.serialize(sourceObject, new JsonFactory().createGenerator(System.out), objectMapper.getSerializerProvider());
 }

 @Test(expected = JsonMappingException.class)
 public void testJsonValueSerializer_MethodReturnsWrongType() throws JsonProcessingException {
 ObjectMapper objectMapper = new ObjectMapper();
 JsonValueSerializer jsonValueSerializer = objectMapper.findValueSerializer(Object.class, null);

 // Given
 Object sourceObject = new MyClassWithJsonValueAnnotationReturningWrongType();

 // When
 // Then exception is thrown since the method returns a wrong type
 jsonValueSerializer.serialize(sourceObject, new JsonFactory().createGenerator(System.out), objectMapper.getSerializerProvider());
 }

 @Test(expected = InvocationTargetException.class)
 public void testJsonValueSerializer_MethodThrowsException() throws JsonProcessingException {
 ObjectMapper objectMapper = new ObjectMapper();
 JsonValueSerializer jsonValueSerializer = objectMapper.findValueSerializer(Object.class, null);

 // Given
 Object sourceObject = new MyClassWithJsonValueAnnotationThrowingException();

 // When
 // Then an InvocationTargetException is thrown
 jsonValueSerializer.serialize(sourceObject, new JsonFactory().createGenerator(System.out), objectMapper.getSerializerProvider());
 }

 @Test
 public void testJsonValueSerializer_WithContextualSerialization() throws JsonProcessingException {
 ObjectMapper objectMapper = new ObjectMapper();
 JsonValueSerializer jsonValueSerializer = objectMapper.findValueSerializer(MyContextualClass.class, null);

 // Given
 Object sourceObject = new MyContextualClass();

 // When
 JsonGenerator jsonGenerator = new JsonFactory().createGenerator(System.out);
 jsonValueSerializer.serialize(sourceObject, jsonGenerator, objectMapper.getSerializerProvider());

 // Then
 String expectedJson = "{\"value\":2}";
 assertTrue(jsonGenerator.toString().equals(expectedJson));
 }

 @Test
 public void testJsonValueSerializer_WithTypeSerialization() throws JsonProcessingException {
 ObjectMapper objectMapper = new ObjectMapper();
 JsonValueSerializer jsonValueSerializer = objectMapper.findValueSerializer(Object.class, null);

 // Given
 Object sourceObject = new MyClassWithJsonValueAnnotation();

 // When
 JsonGenerator jsonGenerator = new JsonFactory().createGenerator(System.out);
 jsonValueSerializer.serializeWithType(sourceObject, jsonGenerator, objectMapper.getSerializerProvider(), objectMapper.getTypeSerializer());

 // Then
 String expectedJson = "{\"@class\":\"com.example.MyClassWithJsonValueAnnotation\",\"value\":\"MyValue\"}";
 assertTrue(jsonGenerator.toString().equals(expectedJson));
 }

 @Test
 public void testJsonValueSerializer_WithEnumValues() throws JsonProcessingException {
 ObjectMapper objectMapper = new ObjectMapper();
 JsonValueSerializer jsonValueSerializer = objectMapper.findValueSerializer(MyEnum.class, null);

 // Given
 Object sourceObject = MyEnum.VALUE_ONE;

 // When
 JsonGenerator jsonGenerator = new JsonFactory().createGenerator(System.out);
 jsonValueSerializer.serialize(sourceObject, jsonGenerator, objectMapper.getSerializerProvider());

 // Then
 String expectedJson = "{\"value\":\"VALUE_ONE\"}";
 assertTrue(jsonGenerator.toString().equals(expectedJson));
 }
}

class MyClassWithJsonValueAnnotation {
 @JsonValue
 public String getValue() {
 return "MyValue";
 }
}

class MyClassWithJsonValueAnnotationReturningWrongType {
 @JsonValue
 public Integer getValue() {
 return 2;
 }
}

class MyClassWithoutJsonValueAnnotation {
 // No @JsonValue annotation
}

class MyContextualClass {
 @JsonProperty
 private int value;

 @JsonValue
 public int getValue() {
 return value;
 }
}

enum MyEnum {
 @JsonValue
 VALUE_ONE("Value One"),
 VALUE_TWO("Value Two"),
 VALUE_THREE("Value Three");

 private final String value;

 MyEnum(String value) {
 this.value = value;
 }

 public String getValue() {
 return value;
 }
}