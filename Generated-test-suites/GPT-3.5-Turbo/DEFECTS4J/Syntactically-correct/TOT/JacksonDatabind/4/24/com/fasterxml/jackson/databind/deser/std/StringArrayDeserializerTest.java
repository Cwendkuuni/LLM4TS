import org.junit.Test;
import static org.junit.Assert.*;

public class StringArrayDeserializerTest {

    @Test
    public void testDeserialize_NormalArray() {
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        String[] expected = {"Hello", "World"};
        String[] result = deserializer.deserialize(createJsonParser("[\"Hello\", \"World\"]"), createDeserializationContext());
        assertArrayEquals(expected, result);
    }
    
    @Test
    public void testDeserialize_EmptyArray() {
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        String[] expected = {};
        String[] result = deserializer.deserialize(createJsonParser("[]"), createDeserializationContext());
        assertArrayEquals(expected, result);
    }
    
    @Test
    public void testDeserialize_NullArray() {
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        String[] expected = null;
        String[] result = deserializer.deserialize(createJsonParser("null"), createDeserializationContext());
        assertArrayEquals(expected, result);
    }
    
    @Test
    public void testDeserialize_MixedArray() {
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        String[] expected = {"Hello", null, "World"};
        String[] result = deserializer.deserialize(createJsonParser("[\"Hello\", null, \"World\"]"), createDeserializationContext());
        assertArrayEquals(expected, result);
    }
    
    @Test
    public void testDeserialize_EmptyStringAsNull() {
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        String[] expected = null;
        String[] result = deserializer.deserialize(createJsonParser("[\"\"]"), createDeserializationContextWithEmptyStringAsNull());
        assertArrayEquals(expected, result);
    }
    
    @Test(expected = IOException.class)
    public void testDeserialize_NotArray() {
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        deserializer.deserialize(createJsonParser("\"Not an array\""), createDeserializationContext());
    }
    
    @Test
    public void testDeserialize_WithCustomDeserializer() {
        StringArrayDeserializer deserializer = new StringArrayDeserializer(createCustomDeserializer());
        String[] expected = {"Hello", "World"};
        String[] result = deserializer.deserialize(createJsonParser("[\"Hello\", \"World\"]"), createDeserializationContext());
        assertArrayEquals(expected, result);
    }
    
    private JsonParser createJsonParser(String json) {
        // Implement the JSON parser creation
    }
    
    private DeserializationContext createDeserializationContext() {
        // Implement the DeserializationContext creation
    }
    
    private DeserializationContext createDeserializationContextWithEmptyStringAsNull() {
        // Implement the DeserializationContext creation with empty string as null
    }
    
    private JsonDeserializer<String> createCustomDeserializer() {
        // Implement the custom deserializer creation
    }
}