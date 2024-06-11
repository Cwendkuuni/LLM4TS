import org.junit.Test;
import static org.junit.Assert.*;

public class StringArrayDeserializerTest {

    @Test
    public void testDeserialize() throws Exception {
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        String[] expected = {"hello", "world"};
        String[] result = deserializer.deserialize(getJsonParser(expected), getDeserializationContext());
        assertArrayEquals(expected, result);
    }
    
    @Test
    public void testDeserialize_withNullValues() throws Exception {
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        String[] expected = {"hello", null, "world", null};
        String[] result = deserializer.deserialize(getJsonParser(expected), getDeserializationContext());
        assertArrayEquals(expected, result);
    }
    
    @Test
    public void testDeserialize_withEmptyArray() throws Exception {
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        String[] expected = {};
        String[] result = deserializer.deserialize(getJsonParser(expected), getDeserializationContext());
        assertArrayEquals(expected, result);
    }
    
    @Test
    public void testDeserialize_withEmptyStringAsNull() throws Exception {
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        String[] expected = null;
        String[] result = deserializer.deserialize(getJsonParser(""), getDeserializationContext());
        assertArrayEquals(expected, result);
    }
    
    @Test(expected = IOException.class)
    public void testDeserialize_withNonArray() throws Exception {
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        deserializer.deserialize(getJsonParser("not an array"), getDeserializationContext());
    }
    
    private JsonParser getJsonParser(String[] values) throws IOException {
        JsonFactory factory = new JsonFactory();
        JsonParser parser = factory.createParser("[");
        for (int i = 0; i < values.length; i++) {
            parser.nextToken();
            parser.setValue(values[i]);
        }
        parser.nextToken();
        parser.close();
        return factory.createParser("[");
    }
    
    private JsonParser getJsonParser(String value) throws IOException {
        JsonFactory factory = new JsonFactory();
        return factory.createParser(value);
    }
    
    private DeserializationContext getDeserializationContext() {
        return new DeserializationContext();
    }
}