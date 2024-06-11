import static org.junit.Assert.*;

import org.apache.commons.lang3.SerializationException;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamClass;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class SerializationUtilsTest {

    // Clone
    //-----------------------------------------------------------------------

    // Test case for cloning a Serializable object
    @Test
    public void testClone_SerializableObject() {
        // Create a Serializable object
        SerializableObject object = new SerializableObject("test", 100);

        // Clone the object using SerializationUtils
        SerializableObject clonedObject = SerializationUtils.clone(object);

        // Check if the cloned object is not null
        assertNotNull(clonedObject);

        // Check if the cloned object is a different instance from the original object
        assertNotSame(object, clonedObject);

        // Check if the cloned object has the same values as the original object
        assertEquals(object.getName(), clonedObject.getName());
        assertEquals(object.getValue(), clonedObject.getValue());
    }

    // Test case for cloning a null object
    @Test
    public void testClone_NullObject() {
        // Clone a null object using SerializationUtils
        SerializableObject clonedObject = SerializationUtils.clone(null);

        // Check if the cloned object is null
        assertNull(clonedObject);
    }

    // Serialize
    //-----------------------------------------------------------------------

    // Test case for serializing a Serializable object to OutputStream
    @Test
    public void testSerialize_SerializableObjectToOutputStream() {
        // Create a Serializable object
        SerializableObject object = new SerializableObject("test", 100);

        // Create a ByteArrayOutputStream to store the serialized object
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        // Serialize the object using SerializationUtils
        SerializationUtils.serialize(object, outputStream);

        // Check if the serialized object is not empty
        assertTrue(outputStream.toByteArray().length > 0);
    }

    // Test case for serializing a null object to OutputStream
    @Test(expected = IllegalArgumentException.class)
    public void testSerialize_NullObjectToOutputStream() {
        // Create a ByteArrayOutputStream to store the serialized object
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        // Serialize a null object using SerializationUtils
        SerializationUtils.serialize(null, outputStream);
    }

    // Test case for serializing a Serializable object to byte array
    @Test
    public void testSerialize_SerializableObjectToByteArray() {
        // Create a Serializable object
        SerializableObject object = new SerializableObject("test", 100);

        // Serialize the object using SerializationUtils
        byte[] serializedObject = SerializationUtils.serialize(object);

        // Check if the serialized object is not empty
        assertTrue(serializedObject.length > 0);
    }

    // Test case for serializing a null object to byte array
    @Test(expected = IllegalArgumentException.class)
    public void testSerialize_NullObjectToByteArray() {
        // Serialize a null object using SerializationUtils
        SerializationUtils.serialize(null);
    }

    // Deserialize
    //-----------------------------------------------------------------------

    // Test case for deserializing an object from InputStream
    @Test
    public void testDeserialize_ObjectFromInputStream() {
        // Create a Serializable object
        SerializableObject object = new SerializableObject("test", 100);

        // Serialize the object to byte array
        byte[] serializedObject = SerializationUtils.serialize(object);

        // Create a ByteArrayInputStream from the serialized object
        ByteArrayInputStream inputStream = new ByteArrayInputStream(serializedObject);

        // Deserialize the object using SerializationUtils
        SerializableObject deserializedObject = (SerializableObject) SerializationUtils.deserialize(inputStream);

        // Check if the deserialized object is not null
        assertNotNull(deserializedObject);

        // Check if the deserialized object is a different instance from the original object
        assertNotSame(object, deserializedObject);

        // Check if the deserialized object has the same values as the original object
        assertEquals(object.getName(), deserializedObject.getName());
        assertEquals(object.getValue(), deserializedObject.getValue());
    }

    // Test case for deserializing an object from null InputStream
    @Test(expected = IllegalArgumentException.class)
    public void testDeserialize_NullInputStream() {
        // Deserialize an object from a null InputStream using SerializationUtils
        SerializationUtils.deserialize(null);
    }

    // Test case for deserializing an object from byte array
    @Test
    public void testDeserialize_ObjectFromByteArray() {
        // Create a Serializable object
        SerializableObject object = new SerializableObject("test", 100);

        // Serialize the object to byte array
        byte[] serializedObject = SerializationUtils.serialize(object);

        // Deserialize the object using SerializationUtils
        SerializableObject deserializedObject = (SerializableObject) SerializationUtils.deserialize(serializedObject);

        // Check if the deserialized object is not null
        assertNotNull(deserializedObject);

        // Check if the deserialized object is a different instance from the original object
        assertNotSame(object, deserializedObject);

        // Check if the deserialized object has the same values as the original object
        assertEquals(object.getName(), deserializedObject.getName());
        assertEquals(object.getValue(), deserializedObject.getValue());
    }

    // Test case for deserializing an object from null byte array
    @Test(expected = IllegalArgumentException.class)
    public void testDeserialize_NullByteArray() {
        // Deserialize an object from a null byte array using SerializationUtils
        SerializationUtils.deserialize(null);
    }

    // Helper Class for Serialization
    private static class SerializableObject implements Serializable {
        private String name;
        private int value;

        public SerializableObject(String name, int value) {
            this.name = name;
            this.value = value;
        }

        public String getName() {
            return name;
        }

        public int getValue() {
            return value;
        }
    }
}