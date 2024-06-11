import static org.junit.Assert.*;

import org.junit.Test;

public class StringArrayDeserializerTest {

    @Test
    public void testDeserialize() {
        // Test deserialization of a valid array of strings
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        String[] expected = new String[]{"test1", "test2", "test3"};
        String json = "[\"test1\", \"test2\", \"test3\"]";
        String[] result = deserializer.deserialize(json);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testDeserializeWithNullValues() {
        // Test deserialization with null values in the array
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        String[] expected = new String[]{"test1", null, "test3"};
        String json = "[\"test1\", null, \"test3\"]";
        String[] result = deserializer.deserialize(json);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testDeserializeEmptyArray() {
        // Test deserialization of an empty array
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        String[] expected = new String[]{};
        String json = "[]";
        String[] result = deserializer.deserialize(json);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testDeserializeWithSingleValue() {
        // Test deserialization of a single value as an array
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        String[] expected = new String[]{"test"};
        String json = "\"test\"";
        String[] result = deserializer.deserialize(json);
        assertArrayEquals(expected, result);
    }

    @Test(expected = IOException.class)
    public void testDeserializeInvalidArray() {
        // Test deserialization of an invalid array
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        String[] expected = new String[]{"test"}; // Expected result, should throw IOException
        String json = "{ \"key\": \"value\" }";
        String[] result = deserializer.deserialize(json);
        assertArrayEquals(expected, result);
    }
}