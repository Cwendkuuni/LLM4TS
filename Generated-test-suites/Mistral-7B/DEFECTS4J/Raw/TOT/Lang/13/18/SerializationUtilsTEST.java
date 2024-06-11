package org.apache.commons.lang3.test;

import org.apache.commons.lang3.SerializationUtils;
import org.apache.commons.lang3.test.util.TestUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Map;

import static org.apache.commons.lang3.SerializationUtils.clone;
import static org.apache.commons.lang3.SerializationUtils.deserialize;
import static org.apache.commons.lang3.SerializationUtils.serialize;
import static org.junit.Assert.*;

/**
 * Test cases for {@link SerializationUtils}.
 */
@Category(UnitTests.class)
public class SerializationUtilsTest {

    private static final Object SERIALIZABLE_OBJECT = new HashMap<>();
    private static final byte[] OBJECT_DATA_SERIALIZABLE = serialize(SERIALIZABLE_OBJECT);

    @Before
    public void setUp() {
        // empty
    }

    @Test
    public void testClone() {
        // Clone test
        Map<String, Object> clonedMap = clone(SERIALIZABLE_OBJECT);
        assertTrue(clonedMap instanceof Map);
        assertTrue(clonedMap.getClass() == Map.class);
        assertEquals(SERIALIZABLE_OBJECT, clonedMap);
        assertNotSame(SERIALIZABLE_OBJECT, clonedMap);
        assertTrue(clonedMap.isEmpty());
        clonedMap.put("testKey", "testValue");
        assertFalse(clonedMap.equals(SERIALIZABLE_OBJECT));
    }

    @Test
    public void testSerialize() {
        // Serialize test
        byte[] serializedData = serialize(SERIALIZABLE_OBJECT);
        assertNotNull(serializedData);
        assertFalse(serializedData.isEmpty());
        assertEquals(Object.class.getCanonicalName(), ObjectStreamClass.lookup(serializedData, null).getName());
        Object deserializedObject = deserialize(new ByteArrayInputStream(serializedData));
        assertTrue(deserializedObject instanceof Map);
        assertTrue(deserializedObject.getClass() == Map.class);
        assertEquals(SERIALIZABLE_OBJECT, deserializedObject);
    }

    @Test
    public void testDeserialize() {
        // Deserialize test
        Object deserializedObject = deserialize(new ByteArrayInputStream(OBJECT_DATA_SERIALIZABLE));
        assertTrue(deserializedObject instanceof Map);
        assertTrue(deserializedObject.getClass() == Map.class);
        assertEquals(SERIALIZABLE_OBJECT, deserializedObject);
    }

    @Test(expected = NullPointerException.class)
    public void testNullInputObjects() {
        // Null Input Objects test
        SerializationUtils.clone(null);
        SerializationUtils.deserialize(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullOutputStream() {
        // Null OutputStream test
        SerializationUtils.serialize(null, new ByteArrayOutputStream());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullInputstream() {
        // Null InputStream test
        SerializationUtils.deserialize(null);
    }

    @Test
    public void testIOException() {
        // IOException test
        byte[] serializedData = serialize(SERIALIZABLE_OBJECT);
        try {
            Object deserializedObject = deserialize(new ByteArrayInputStream(serializedData));
            fail("Exception not thrown");
        } catch (IOException e) {
            // expected
        }
    }

    @Test
    public void testSerializationException() {
        // SerializationException test
        try {
            SerializationUtils.clone((Serializable) null);
            fail("Exception not thrown");
        } catch (SerializationException e) {
            // expected
        }
    }

    @Test
    public void testDeepClone() {
        // Deep clone test
        Map<String, Object> map1 = new HashMap<>();
        map1.put("map1", new HashMap<>());
        Map<String, Object> map2 = new HashMap<>();
        map2.put("map2", map1);
        Map<String, Object> clonedMap = SerializationUtils.clone(map2);
        assertTrue(clonedMap instanceof Map);
        assertTrue(clonedMap.getClass() == Map.class);
        assertEquals(map2, clonedMap);
        assertNotSame(map2, clonedMap);
        Map<String, Object> clonedMapMap1 = (Map<String, Object>) clonedMap.get("map1");
        Map<String, Object> originalMapMap1 = (Map<String, Object>) map2.get("map1");
        assertTrue(clonedMapMap1 instanceof Map);
        assertTrue(clonedMapMap1.getClass() == Map.class);
        assertEquals(originalMapMap1, clonedMapMap1);
        assertNotSame(originalMapMap1, clonedMapMap1);
    }

}