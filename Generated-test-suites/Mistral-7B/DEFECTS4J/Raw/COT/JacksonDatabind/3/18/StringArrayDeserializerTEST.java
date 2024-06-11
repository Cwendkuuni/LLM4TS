import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class StringArrayDeserializerTest {

 private static final StringArrayDeserializer instance = StringArrayDeserializer.instance;

 @Test
 public void testDeserialize() throws IOException {
 String[] result = instance.deserialize(JsonTestUtils.createParser("[\"abc\"]"), new DeserializationContext());
 assertArrayEquals(new String[]{"abc"}, result);
 }

 @Test
 public void testDeserializeWithNullElements() throws IOException {
 String[] result = instance.deserialize(JsonTestUtils.createParser("[\", null]"), new DeserializationContext());
 assertArrayEquals(new String[]{null, null}, result);
 }

 @Test
 public void testDeserializeWithCustomElementDeserializer() throws IOException {
 String[] result = instance.deserialize(JsonTestUtils.createParser("[\"1\", \"2\"]"), new DeserializationContext(), new IntDeserializer());
 assertArrayEquals(new String[]{"1", "2"}, result);
 }

 @Test(expected = IOException.class)
 public void testDeserializeWithNonArray() throws IOException {
 String[] result = instance.deserialize(JsonTestUtils.createParser("{\"name\": \"John\"}"), new DeserializationContext());
 fail("Expected IOException for non-array input");
 }

 @Test
 public void testDeserializeWithEmptyArray() throws IOException {
 String[] result = instance.deserialize(JsonTestUtils.createParser("[]"), new DeserializationContext());
 assertArrayEquals(new String[]{}, result);
 }

 @Test
 public void testDeserializeWithNullInput() throws IOException {
 String[] result = instance.deserialize(null, new DeserializationContext());
 assertNull(result);
 }

 @Test(expected = JsonMappingException.class)
 public void testDeserializeWithInvalidInput() throws IOException {
 String[] result = instance.deserialize(JsonTestUtils.createParser("\"abc\""), new DeserializationContext());
 fail("Expected JsonMappingException for invalid input");
 }

 @Test
 public void testCreateContextual() throws JsonMappingException {
 DeserializationContext ctxt = new DeserializationContext();
 JsonDeserializer<?> deser = instance.createContextual(ctxt, null);
 assertNull(deser);
}

 @Test
 public void testCreateContextualWithStringDeserializer() throws JsonMappingException {
 DeserializationContext ctxt = new DeserializationContext();
 JsonDeserializer<String> stringDeserializer = new StringDeserializer();
 JsonDeserializer<?> deser = instance.createContextual(ctxt, new BeanPropertyModel("testProperty", String.class));
 assertEquals(StringArrayDeserializer.class, deser.getClass());
 JsonDeserializer<?> deser2 = ((StringArrayDeserializer) deser)._elementDeserializer;
 assertEquals(stringDeserializer, deser2);
}

 @Test
 public void testCreateContextualWithCustomDeserializer() throws JsonMappingException {
 DeserializationContext ctxt = new DeserializationContext();
 JsonDeserializer<Integer> intDeserializer = new IntDeserializer();
 JsonDeserializer<?> deser = instance.createContextual(ctxt, new BeanPropertyModel("testProperty", Integer.class));
 assertEquals(StringArrayDeserializer.class, deser.getClass());
 JsonDeserializer<?> deser2 = ((StringArrayDeserializer) deser)._elementDeserializer;
 assertNull(deser2);
}

}