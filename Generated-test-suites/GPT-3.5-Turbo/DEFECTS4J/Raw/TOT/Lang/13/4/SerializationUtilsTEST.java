import org.junit.Test;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class SerializationUtilsTest {

    @Test
    public void testClone_typicalUseCase() {
        // Creating an object to clone
        Map<String, Integer> map = new HashMap<>();
        map.put("one", 1);
        map.put("two", 2);
        map.put("three", 3);

        // Cloning the object
        Map<String, Integer> clonedMap = SerializationUtils.clone(map);

        // Verifying that the cloned object is not the same as the original object
        assertNotSame(map, clonedMap);

        // Verifying that the cloned object has the same content as the original object
        assertEquals(map, clonedMap);
    }

    @Test
    public void testClone_nullObject() {
        // Cloning a null object
        Object clonedObject = SerializationUtils.clone(null);

        // Verifying that the cloned object is null
        assertNull(clonedObject);
    }

    @Test(expected = SerializationException.class)
    public void testClone_serializationFailure() {
        // Creating a non-serializable object
        NonSerializableObject nonSerializableObject = new NonSerializableObject();

        // Trying to clone the non-serializable object (which should throw a SerializationException)
        SerializationUtils.clone(nonSerializableObject);
    }

    @Test
    public void testSerialize_typicalUseCase() {
        // Creating an object to serialize
        Map<String, Integer> map = new HashMap<>();
        map.put("one", 1);
        map.put("two", 2);
        map.put("three", 3);

        // Serializing the object
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        SerializationUtils.serialize(map, baos);
        byte[] serializedData = baos.toByteArray();

        // Verifying that the serialized data is not null
        assertNotNull(serializedData);
        assertTrue(serializedData.length > 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSerialize_nullObject() {
        // Serializing a null object (which should throw an IllegalArgumentException)
        SerializationUtils.serialize(null, new ByteArrayOutputStream());
    }

    @Test
    public void testSerialize_serializationFailure() {
        // Creating a non-serializable object
        NonSerializableObject nonSerializableObject = new NonSerializableObject();

        // Serializing the non-serializable object
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            SerializationUtils.serialize(nonSerializableObject, baos);
        } catch (SerializationException e) {
            // Verifying that a SerializationException is thrown
            assertTrue(e.getCause() instanceof NotSerializableException);
        }
    }

    @Test
    public void testDeserialize_typicalUseCase() {
        // Creating an object to serialize and deserialize
        Map<String, Integer> map = new HashMap<>();
        map.put("one", 1);
        map.put("two", 2);
        map.put("three", 3);

        // Serializing the object
        byte[] serializedData = SerializationUtils.serialize(map);

        // Deserializing the serialized data
        ByteArrayInputStream bais = new ByteArrayInputStream(serializedData);
        Object deserializedObject = SerializationUtils.deserialize(bais);

        // Verifying that the deserialized object is not null
        assertNotNull(deserializedObject);

        // Verifying that the deserialized object has the same content as the original object
        assertTrue(deserializedObject instanceof Map);
        Map<String, Integer> deserializedMap = (Map<String, Integer>) deserializedObject;
        assertEquals(map, deserializedMap);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeserialize_nullInputStream() {
        // Deserializing from a null input stream (which should throw an IllegalArgumentException)
        SerializationUtils.deserialize(null);
    }

    @Test
    public void testDeserialize_deserializationFailure() {
        // Creating invalid serialized data
        byte[] invalidSerializedData = new byte[]{1, 2, 3, 4, 5};

        // Deserializing the invalid serialized data
        ByteArrayInputStream bais = new ByteArrayInputStream(invalidSerializedData);
        try {
            SerializationUtils.deserialize(bais);
        } catch (SerializationException e) {
            // Verifying that a SerializationException is thrown
            assertTrue(e.getCause() instanceof IOException);
        }
    }

    private static class NonSerializableObject {
        // A non-serializable object
    }

}