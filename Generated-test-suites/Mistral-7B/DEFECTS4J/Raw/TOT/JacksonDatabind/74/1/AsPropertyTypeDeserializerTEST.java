import com.fasterxml.jackson.annotation.JsonInclude.As;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import static org.junit.Assert.*;

public class AsPropertyTypeDeserializerTest {

 private static final ObjectMapper mapper = new ObjectMapper();

 @Test
 public void testDeserializeTypedPropertyWithValidTypeId() throws Exception {
 AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(
 new TypeReference<String>() {}.getType(), mapper.getTypeIdResolver(), "typePropertyName", true, String.class, As.PROPERTY);
 Object result = deserializer.deserializeTypedFromObject(mapper.createParser("{\"typePropertyName\":\"String\"}"), new TestContext());
 assertEquals("String", result);
 }

 @Test
 public void testDeserializeTypedPropertyWithMissingTypeId() throws Exception {
 AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(
 new TypeReference<String>() {}.getType(), mapper.getTypeIdResolver(), "typePropertyName", true, String.class, As.PROPERTY);
 Object result = deserializer.deserializeTypedFromObject(mapper.createParser("{\"typePropertyName\":\"invalidType\"}"), new TestContext());
 assertNull(result);
 }

 @Test
 public void testDeserializeTypedPropertyWithInvalidToken() throws Exception {
 AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(
 new TypeReference<String>() {}.getType(), mapper.getTypeIdResolver(), "typePropertyName", true, String.class, As.PROPERTY);
 Object result = deserializer.deserializeTypedFromObject(mapper.createParser("{\"invalidToken\":\"invalidType\"}"), new TestContext());
 assertNull(result);
 }

 @Test
 public void testDeserializeTypedPropertyWithMissingFieldName() throws Exception {
 AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(
 new TypeReference<String>() {}.getType(), mapper.getTypeIdResolver(), "typePropertyName", true, String.class, As.PROPERTY);
 Object result = deserializer.deserializeTypedFromObject(mapper.createParser("{\"invalidFieldName\":\"String\"}"), new TestContext());
 assertNull(result);
 }

 @Test
 public void testDeserializeTypedPropertyWithNonMatchingFieldName() throws Exception {
 AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(
 new TypeReference<String>() {}.getType(), mapper.getTypeIdResolver(), "typePropertyName", true, String.class, As.PROPERTY);
 Object result = deserializer.deserializeTypedFromObject(mapper.createParser("{\"otherPropertyName\":\"String\"}"), new TestContext());
 assertNull(result);
 }

 @Test
 public void testDeserializeTypedPropertyWithDefaultValue() throws Exception {
 AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(
 new TypeReference<String>() {}.getType(), mapper.getTypeIdResolver(), "typePropertyName", true, String.class, As.PROPERTY);
 Object result = deserializer.deserializeTypedFromObject(mapper.createParser("{}"), new TestContext());
 assertNull(result);
 }

 @Test
 public void testDeserializeTypedPropertyWithNativeTypeId() throws Exception {
 AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(
 new TypeReference<String>() {}.getType(), mapper.getTypeIdResolver(), "typePropertyName", true, String.class, As.PROPERTY);
 Object result = deserializer.deserializeTypedFromObject(mapper.createParser("{\"@type\":\"java.lang.String\"}"), new TestContext());
 assertEquals("String", result);
 }

 @Test
 public void testDeserializeTypedPropertyWithArrayWrapper() throws Exception {
 AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(
 new TypeReference<List<String>>() {}.getType(), mapper.getTypeIdResolver(), "typePropertyName", true, ArrayList.class, As.PROPERTY);
 Object result = deserializer.deserializeTypedFromObject(mapper.createParser("[\n" +
 "  {\"typePropertyName\":\"java.lang.String\",\"value\":\"String1\"},\n" +
 "  {\"typePropertyName\":\"java.lang.String\",\"value\":\"String2\"}\n" +
 "]"), new TestContext());
 assertEquals(Arrays.asList("String1", "String2"), result);
 }

 @Test
 public void testDeserializeTypedPropertyWithWrongType() throws Exception {
 AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(
 new TypeReference<String>() {}.getType(), mapper.getTypeIdResolver(), "typePropertyName", true, String.class, As.PROPERTY);
 Object result = deserializer.deserializeTypedFromObject(mapper.createParser("{\"typePropertyName\":1}"), new TestContext());
 assertNull(result);
 }

 @Test
 public void testDeserializeTypedPropertyWithNullValue() throws Exception {
 AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(
 new TypeReference<String>() {}.getType(), mapper.getTypeIdResolver(), "typePropertyName", true, String.class, As.PROPERTY);
 Object result = deserializer.deserializeTypedFromObject(mapper.createParser("{\"typePropertyName\":null}"), new TestContext());
 assertNull(result);
 }

 @Test
 public void testDeserializeTypedPropertyWithEmptyArray() throws Exception {
 AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(
 new TypeReference<List<String>>() {}.getType(), mapper.getTypeIdResolver(), "typePropertyName", true, ArrayList.class, As.PROPERTY);
 Object result = deserializer.deserializeTypedFromObject(mapper.createParser("{}"), new TestContext());
 assertNotNull(result);
 assertEquals(Collections.emptyList(), result);
 }

 @Test
 public void testDeserializeTypedPropertyWithEmptyObject() throws Exception {
 AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(
 new TypeReference<String>() {}.getType(), mapper.getTypeIdResolver(), "typePropertyName", true, String.class, As.PROPERTY);
 Object result = deserializer.deserializeTypedFromObject(mapper.createParser("{}"), new TestContext());
 assertNull(result);
 }

 @Test
 public void testDeserializeTypedPropertyWithEmptyString() throws Exception {
 AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(
 new TypeReference<String>() {}.getType(), mapper.getTypeIdResolver(), "typePropertyName", true, String.class, As.PROPERTY);
 Object result = deserializer.deserializeTypedFromObject(mapper.createParser("{}"), new TestContext());
 assertNull(result);
 }

 private static class TestContext extends DeserializationContext {
 }

}