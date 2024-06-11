
package org.apache.commons.lang3;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class SerializationUtilsTest {
    @Test
    public void testClone() {
        Map<String, Integer> map = new HashMap<>();
        map.put("one", 1);
        map.put("two", 2);
        map.put("three", 3);

        Map<String, Integer> clonedMap = SerializationUtils.clone(map);
        assertEquals(map, clonedMap);
    }

    @Test
    public void testSerialize() {
        Map<String, Integer> map = new HashMap<>();
        map.put("one", 1);
        map.put("two", 2);
        map.put("three", 3);

        byte[] serializedData = SerializationUtils.serialize(map);
        assertNotNull(serializedData);
    }

    @Test
    public void testDeserialize() {
        Map<String, Integer> map = new HashMap<>();
        map.put("one", 1);
        map.put("two", 2);
        map.put("three", 3);

        byte[] serializedData = SerializationUtils.serialize(map);
        assertNotNull(serializedData);

        Map<String, Integer> deserializedMap = (Map<String, Integer>) SerializationUtils.deserialize(serializedData);
        assertEquals(map, deserializedMap);
    }
}
