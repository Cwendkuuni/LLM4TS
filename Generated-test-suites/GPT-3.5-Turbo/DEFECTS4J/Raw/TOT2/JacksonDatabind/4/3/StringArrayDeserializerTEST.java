import static org.junit.Assert.*;
import org.junit.Test;

public class StringArrayDeserializerTest {

    @Test
    public void testDeserialize() {
        StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
        String[] expected = {"value1", "value2", null};
        String json = "[\"value1\", \"value2\", null]";
        String[] result = deserializer.deserialize(json);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testDeserializeEmptyArray() {
        StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
        String[] expected = {};
        String json = "[]";
        String[] result = deserializer.deserialize(json);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testDeserializeSingleValue() {
        StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
        String[] expected = {"single value"};
        String json = "\"single value\"";
        String[] result = deserializer.deserialize(json);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testDeserializeWithCustomDeserializer() {
        // Test using a custom deserializer
        StringArrayDeserializer deserializer = new StringArrayDeserializer(new CustomStringDeserializer());
        String[] expected = {"custom value1", "custom value2"};
        String json = "[\"custom value1\", \"custom value2\"]";
        String[] result = deserializer.deserialize(json);
        assertArrayEquals(expected, result);
    }

    @Test(expected = IOException.class)
    public void testDeserializeInvalidJson() throws IOException {
        // Test with invalid JSON
        StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
        String json = "invalid json";
        String[] result = deserializer.deserialize(json);
    }
}