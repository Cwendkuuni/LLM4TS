import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.junit.Test;
import static org.junit.Assert.*;

public class AsPropertyTypeDeserializerTest {

 private ObjectMapper objectMapper;

 @Before
 public void setup() {
 objectMapper = new ObjectMapper();
 }

 @Test
 public void testDeserializeTypedFromObjectWithProperty() throws JsonProcessingException, JsonMappingException {
 // Given
 JavaType javaType = TypeFactory.defaultInstance()
 .constructType(AsPropertyTypeDeserializer.class, String.class, null, String.class, null, As.PROPERTY);
 ObjectNode jsonObject = objectMapper.createObjectNode();
 jsonObject.put("typePropertyName", "typeId");
 jsonObject.put("typeId", "java.lang.String");
 JsonParser jsonParser = objectMapper.getFactory().createParser(jsonObject);
 DeserializationContext context = objectMapper.getDeserializationContext();
 AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(javaType, context.getTypeIdResolver(), "typePropertyName", true, null, As.PROPERTY);

 // When
 Object result = deserializer.deserializeTypedFromObject(jsonParser, context);

 // Then
 assertEquals("Expected String", result.getClass(), String.class);
 }

 @Test
 public void testDeserializeTypedFromObjectWithMissingProperty() throws JsonProcessingException, JsonMappingException {
 // Given
 JavaType javaType = TypeFactory.defaultInstance()
 .constructType(AsPropertyTypeDeserializer.class, String.class, null, String.class, null, As.PROPERTY);
 ObjectNode jsonObject = objectMapper.createObjectNode();
 JsonParser jsonParser = objectMapper.getFactory().createParser(jsonObject);
 DeserializationContext context = objectMapper.getDeserializationContext();
 AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(javaType, context.getTypeIdResolver(), "typePropertyName", true, null, As.PROPERTY);

 // When, Then
 assertNull("Expected null", deserializer.deserializeTypedFromObject(jsonParser, context));
 }

 @Test
 public void testDeserializeTypedFromObjectWithTypeIdInvisible() throws JsonProcessingException, JsonMappingException {
 // Given
 JavaType javaType = TypeFactory.defaultInstance()
 .constructType(AsPropertyTypeDeserializer.class, String.class, null, String.class, null, As.PROPERTY);
 ObjectNode jsonObject = objectMapper.createObjectNode();
 jsonObject.put("typePropertyName", "typeId");
 jsonObject.put("typeId", "java.lang.String");
 JsonParser jsonParser = objectMapper.getFactory().createParser(jsonObject);
 DeserializationContext context = objectMapper.getDeserializationContext();
 AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(javaType, context.getTypeIdResolver(), "typePropertyName", false, null, As.PROPERTY);

 // When
 Object result = deserializer.deserializeTypedFromObject(jsonParser, context);

 // Then
 assertNull("Expected null, typeId not visible", result);
 }

 @Test
 public void testDeserializeTypedFromObjectWithInvalidTypeId() throws JsonProcessingException, JsonMappingException {
 // Given
 JavaType javaType = TypeFactory.defaultInstance()
 .constructType(AsPropertyTypeDeserializer.class, String.class, null, String.class, null, As.PROPERTY);
 ObjectNode jsonObject = objectMapper.createObjectNode();
 jsonObject.put("typePropertyName", "typeId");
 jsonObject.put("typeId", "com.not.a.String");
 JsonParser jsonParser = objectMapper.getFactory().createParser(jsonObject);
 DeserializationContext context = objectMapper.getDeserializationContext();
 AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(javaType, context.getTypeIdResolver(), "typePropertyName", true, null, As.PROPERTY);

 // When, Then
 assertNull("Expected null, invalid type id", deserializer.deserializeTypedFromObject(jsonParser, context));
 }

 @Test
 public void testDeserializeTypedFromObjectWithNativeTypeId() throws JsonProcessingException, JsonMappingException {
 // Given
 JavaType javaType = TypeFactory.defaultInstance()
 .constructType(AsPropertyTypeDeserializer.class, String.class, null, String.class, null, As.PROPERTY);
 ObjectNode jsonObject = objectMapper.createObjectNode();jsonObject.put("typePropertyName", "typeId");
 jsonObject.put("typeId", "@type/java.lang.String");
 JsonParser jsonParser = objectMapper.getFactory().createParser(jsonObject);
 DeserializationContext context = objectMapper.getDeserializationContext();
 AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(javaType, context.getTypeIdResolver(), "typePropertyName", true, null, As.PROPERTY);

 // When
 Object result = deserializer.deserializeTypedFromObject(jsonParser, context);

 // Then
 assertEquals("Expected String", result.getClass(), String.class);
 }

 @Test
 public void testDeserializeTypedFromArray() throws JsonProcessingException, JsonMappingException {
 // Given
 JavaType javaType = TypeFactory.defaultInstance()
 .constructType(AsPropertyTypeDeserializer.class, String[].class, null, String.class, null, As.PROPERTY);
 ObjectNode jsonObject = objectMapper.createObjectNode();
 jsonObject.put("value", "[\"a\", \"b\", \"c\"]");
 JsonParser jsonParser = objectMapper.getFactory().createParser(jsonObject);
 DeserializationContext context = objectMapper.getDeserializationContext();
 AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(javaType, context.getTypeIdResolver(), "value", true, null, As.PROPERTY);

 // When
 Object result = deserializer.deserializeTypedFromArray(jsonParser, context);

 // Then
 assertArrayEquals("Expected String[]", new String[]{"a", "b", "c"}, result);
 }

 @Test
 public void testDeserializeTypedFromScalar() throws JsonProcessingException, JsonMappingException {
 // Given
 JavaType javaType = TypeFactory.defaultInstance()
 .constructType(AsPropertyTypeDeserializer.class, Integer.class, null, null, null, As.PROPERTY);
 ObjectNode jsonObject = objectMapper.createObjectNode();
 jsonObject.put("value", "42");
 JsonParser jsonParser = objectMapper.getFactory().createParser(jsonObject);
 DeserializationContext context = objectMapper.getDeserializationContext();
 AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(javaType, context.getTypeIdResolver(), "value", true, null, As.PROPERTY);

 // When
 Object result = deserializer.deserializeTypedFromScalar(jsonParser, context);

 // Then
 assertEquals("Expected Integer", result, 42);
 }

 @Test
 public void testDeserializeTypedFromUnknown() throws JsonProcessingException, JsonMappingException {
 // Given
 JavaType javaType = TypeFactory.defaultInstance()
 .constructType(AsPropertyTypeDeserializer.class, String.class, null, String.class, null, As.PROPERTY);
 ObjectNode jsonObject = objectMapper.createObjectNode();
 JsonParser jsonParser = objectMapper.getFactory().createParser(jsonObject);
 DeserializationContext context = objectMapper.getDeserializationContext();
 AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(javaType, context.getTypeIdResolver(), "typePropertyName", true, null, As.PROPERTY);

 // When
 Object result = deserializer.deserializeTypedFromAny(jsonParser, context);

 // Then
 assertEquals("Expected String", result.getClass(), String.class);
 }

 @Test
 public void testDeserializeTypedFromUnknownWithArray() throws JsonProcessingException, JsonMappingException {
 // Given
 JavaType javaType = TypeFactory.defaultInstance()
 .constructType(AsPropertyTypeDeserializer.class, Integer[].class, null, null, null, As.PROPERTY);
 ObjectNode jsonObject = objectMapper.createObjectNode();
 JsonParser jsonParser = objectMapper.getFactory().createParser(jsonObject);
 DeserializationContext context = objectMapper.getDeserializationContext();
 AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(javaType, context.getTypeIdResolver(), "value", true, null, As.PROPERTY);

 // When
 Object result = deserializer.deserializeTypedFromAny(jsonParser, context);

 // Then
 assertArrayEquals("Expected Integer[]", new Integer[]{42}, result);
 }

 @Test
 public void testDeserializeTypedFromUnknownWithScalar() throws JsonProcessingException, JsonMappingException {
 // Given
 JavaType javaType = TypeFactory.defaultInstance()
 .constructType(AsPropertyTypeDeserializer.class, String.class, null, String.class, null, As.PROPERTY);
 ObjectNode jsonObject = objectMapper.createObjectNode();
 JsonParser jsonParser = objectMapper.getFactory().createParser(jsonObject);
 DeserializationContext context = objectMapper.getDeserializationContext();
 AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(javaType, context.getTypeIdResolver(), "value", true, null, As.PROPERTY);

 // When
 Object result = deserializer.deserializeTypedFromAny(jsonParser, context);

 // Then
 assertEquals("Expected String", result, "42");
 }
}