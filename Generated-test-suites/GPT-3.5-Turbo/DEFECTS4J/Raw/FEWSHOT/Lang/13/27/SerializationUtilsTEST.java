package org.apache.commons.lang3;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class SerializationUtilsTest {
    @Test
    public void testClone() {
        // Test cloning of a non-null object
        String str = "Hello";
        String clone = SerializationUtils.clone(str);
        assertEquals(str, clone);

        // Test cloning of a null object
        assertNull(SerializationUtils.clone(null));
    }

    @Test
    public void testSerialize() {
        // Test serialization of a non-null object
        Map<String, Integer> map = new HashMap<>();
        map.put("one", 1);
        map.put("two", 2);
        byte[] data = SerializationUtils.serialize(map);
        assertNotNull(data);

        // Test serialization of a null object
        assertNull(SerializationUtils.serialize(null));
    }

    @Test
    public void testDeserialize() {
        // Test deserialization of a non-null object
        byte[] data = {1, 2, 3, 4};
        Object obj = SerializationUtils.deserialize(data);
        assertNotNull(obj);

        // Test deserialization of a null object
        assertNull(SerializationUtils.deserialize(null));
    }
}