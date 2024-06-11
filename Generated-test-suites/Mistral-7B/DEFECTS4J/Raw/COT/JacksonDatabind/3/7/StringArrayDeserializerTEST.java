import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class StringArrayDeserializerTest {

 private static final StringArrayDeserializer instance = StringArrayDeserializer.instance;

 @Test
 public void testDeserializeStandard() throws IOException {
 String[] result = instance.deserialize(createJsonParser("[\"test1\",\"test2\",\"test3\"]"), createContextualDeserializationContext());
 assertArrayEquals(new String[]{"test1", "test2", "test3"}, result);
 }

 @Test
 public void testDeserializeWithCustomElementDeserializer() throws IOException {
 JsonDeserializer<String> customDeserializer = createMockDeserializer(); // Implement mock deserializer here
 StringArrayDeserializer deserializerWithCustomElementDeserializer = new StringArrayDeserializer(customDeserializer);
 String[] result = deserializerWithCustomElementDeserializer.deserialize(createJsonParser("[\"test1\",\"test2\",\"test3\"]"), createContextualDeserializationContext());
 assertArrayEquals(new String[]{"test1", "test2", "test3"}, result);
 }

 @Test
 public void testDeserializeEmptyArray() throws IOException {
 String[] result = instance.deserialize(createJsonParser("[]"), createContextualDeserializationContext());
 assertArrayEquals(new String[0], result);
 }

 @Test
 public void testDeserializeEmptyStringAsNull() throws IOException {
 ObjectContextualDeserializationContext ctx = createContextualDeserializationContext();
 ctx.setEnabled(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);
 String[] result = instance.deserialize(createJsonParser("[]"), ctx);
 assertArrayEquals(new String[]{}, result);
 String[] result2 = instance.deserialize(createJsonParser("[]"), ctx); // Test that it returns the same instance of null array
 assertArrayEquals(new String[]{}, result2);
 String[] result3 = instance.deserialize(createJsonParser("[\"\"]"), ctx); // Test that it returns null for empty string
 assertNull(result3);
 }

 @Test(expected = IOException.class)
 public void testDeserializeNonArray() throws IOException {
 String[] result = instance.deserialize(createJsonParser("\"test\""), createContextualDeserializationContext());
 fail("Expected IOException when non-array token is found");
 }

 @Test
 public void testDeserializeNullElement() throws IOException {
 String[] result = instance.deserialize(createJsonParser("[\null]"), createContextualDeserializationContext());
 assertArrayEquals(new String[]{null}, result);
 }

 @Test(expected = IOException.class)
 public void testDeserializeNonStringElement() throws IOException {
 JsonParser jsonParser = createJsonParser("[\"test\",1]"); // Replace '1' with any non-String value
 String[] result = instance.deserialize(jsonParser, createContextualDeserializationContext());
 fail("Expected IOException when non-String element is found");
 }

 @Test
 public void testDeserializeWithCustomNullValue() throws IOException {
 StringArrayDeserializer deserializer = new StringArrayDeserializer();
 deserializer.setNullValue("customNullValue");
 String[] result = deserializer.deserialize(createJsonParser("[]"), createContextualDeserializationContext());
 assertArrayEquals(new String[]{deserializer.getNullValue()}, result);
 }

 @Test
 public void testDeserializeWithType() throws IOException {
 ObjectMapper mapper = new ObjectMapper();
 TypeReference<String[]> typeReference = new TypeReference<>() {};
 String[] result = mapper.readValue(createJsonParser("[\"test1\",\"test2\",\"test3\"]"), typeReference);
 assertArrayEquals(new String[]{"test1", "test2", "test3"}, result);
 }

 private JsonParser createJsonParser(String jsonString) throws IOException {
 JsonFactory jsonFactory = new JsonFactory();
 return jsonFactory.createParser(jsonString);
 }

 private DeserializationContext createContextualDeserializationContext() {
 ObjectMapper objectMapper = new ObjectMapper();
 return objectMapper.getDeserializationContext();
 }

 private JsonDeserializer<String> createMockDeserializer() {
 // Implement mock deserializer here
 return null;
 }

}