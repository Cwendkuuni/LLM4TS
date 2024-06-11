import org.junit.Assert;
import org.junit.Test;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class SerializationUtilsTest {

    @Test
    public void testClone() {
        // Create objects to clone
        Map<String, Integer> originalMap = new HashMap<>();
        originalMap.put("One", 1);
        originalMap.put("Two", 2);
        originalMap.put("Three", 3);

        // Clone the original map
        Map<String, Integer> clonedMap = SerializationUtils.clone(originalMap);

        // Check if the cloned map is equal to the original map
        Assert.assertEquals(originalMap, clonedMap);
    }

    @Test
    public void testSerializeAndDeserialize() {
        // Create object to serialize
        Map<String, Integer> map = new HashMap<>();
        map.put("One", 1);
        map.put("Two", 2);
        map.put("Three", 3);

        // Serialize the map
        byte[] serializedData = SerializationUtils.serialize(map);

        // Deserialize the serialized data
        Map<String, Integer> deserializedMap = (Map<String, Integer>) SerializationUtils.deserialize(serializedData);

        // Check if the deserialized map is equal to the original map
        Assert.assertEquals(map, deserializedMap);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSerializeNullObject() {
        // Try to serialize a null object
        SerializationUtils.serialize(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSerializeNullOutputStream() {
        // Create object to serialize
        Map<String, Integer> map = new HashMap<>();
        map.put("One", 1);
        map.put("Two", 2);
        map.put("Three", 3);

        // Try to serialize the map with a null output stream
        SerializationUtils.serialize(map, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeserializeNullInputStream() {
        // Try to deserialize a null input stream
        SerializationUtils.deserialize(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeserializeNullObjectData() {
        // Try to deserialize a null object data
        SerializationUtils.deserialize(null);
    }

}