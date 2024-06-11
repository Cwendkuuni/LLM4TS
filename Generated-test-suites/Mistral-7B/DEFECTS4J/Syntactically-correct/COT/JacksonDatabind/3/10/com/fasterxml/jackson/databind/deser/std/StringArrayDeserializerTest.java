import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import org.junit.Assert;
import org.junit.Test;

public class StringArrayDeserializerTest {

 private static final StringArrayDeserializer instance = StringArrayDeserializer.instance;

 @Test
 public void testDeserializeStandard() throws IOException {
 String[] expected = new String[]{"test1", "test2", "test3"};
 JsonParser jsonParser = createJsonParser("[\"" + "test1" + "\",\"" + "test2" + "\",\"" + "test3" + "\"]");
 String[] actual = instance.deserialize(jsonParser, new TestContext());
 Assert.assertArrayEquals(expected, actual);
 }

 @Test
 public void testDeserializeCustom() throws IOException {
 String[] expected = new String[]{"test1", "test2", "test3"};
 JsonParser jsonParser = createJsonParser("[\"" + "test1" + "\",\"" + "test2" + "\",\"" + "test3" + "\"]");
 JsonDeserializer<String> elementDeserializer = createStringDeserializer();
 StringArrayDeserializer deserializer = new StringArrayDeserializer(elementDeserializer);
 String[] actual = deserializer.deserialize(jsonParser, new TestContext());
 Assert.assertArrayEquals(expected, actual);
 }

 @Test(expected = IOException.class)
 public void testDeserializeEmptyArray() throws IOException {
 JsonParser jsonParser = createJsonParser("[]");
 instance.deserialize(jsonParser, new TestContext());
 }

 @Test(expected = IOException.class)
 public void testDeserializeEmptyString() throws IOException {
 JsonParser jsonParser = createJsonParser("[\"\"]");
 instance.deserialize(jsonParser, new TestContext());
 }

 @Test(expected = IOException.class)
 public void testDeserializeNullValue() throws IOException {
 JsonParser jsonParser = createJsonParser("null");
 instance.deserialize(jsonParser, new TestContext());
 }

 @Test(expected = IOException.class)
 public void testDeserializeInvalidJson() throws IOException {
 JsonParser jsonParser = createJsonParser("{\"test\":[]}");
 instance.deserialize(jsonParser, new TestContext());
 }

 @Test
 public void testDeserializeEmptyArrayWithCustomValueDeserializer() throws IOException {
 String[] expected = new String[]{};
 JsonParser jsonParser = createJsonParser("[]");
 JsonDeserializer<String> elementDeserializer = createStringDeserializer();
 StringArrayDeserializer deserializer = new StringArrayDeserializer(elementDeserializer);
 String[] actual = deserializer.deserializeWithType(jsonParser, new TestContext(), new ArrayTypeDeserializer(String.class, elementDeserializer));
 Assert.assertArrayEquals(expected, actual);
 }

 @Test
 public void testDeserializeNullArray() throws IOException {
 String[] expected = null;
 JsonParser jsonParser = createJsonParser("null");
 String[] actual = instance.deserializeWithType(jsonParser, new TestContext(), new ArrayTypeDeserializer(String[].class, null));
 Assert.assertArrayEquals(expected, actual);
 }

 private JsonParser createJsonParser(String jsonString) throws IOException {
 ObjectMapper objectMapper = new ObjectMapper();
 return objectMapper.getFactory().createParser(jsonString);
 }

 private JsonDeserializer<String> createStringDeserializer() {
 return new StringDeserializer();
 }

 private static class TestContext extends DeserializationContext {
 @Override
 public ObjectBuffer leaseObjectBuffer() {
 return new ObjectBuffer();
 }

 @Override
 public Object _valueInstantiate(Class<?> valueType, Object valueHandler) throws InstantiationException, IllegalAccessException {
 return valueType.newInstance();
 }
 }

}