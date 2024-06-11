package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.*;

public class SerializationUtilsTest {

    @Test
    public void testClone() {
        // Typical use case
        String original = "Hello, world!";
        String cloned = SerializationUtils.clone(original);
        assertEquals(original, cloned);
        
        // Edge case: Null object
        String nullObject = null;
        String clonedNull = SerializationUtils.clone(nullObject);
        assertNull(clonedNull);
        
        // Edge case: Empty string
        String emptyString = "";
        String clonedEmpty = SerializationUtils.clone(emptyString);
        assertEquals(emptyString, clonedEmpty);
        
        // Error scenario: Not serializable object
        NotSerializableObject notSerializable = new NotSerializableObject();
        try {
            NotSerializableObject clonedNotSerializable = SerializationUtils.clone(notSerializable);
            fail("SerializationException expected but not thrown");
        } catch (SerializationException e) {
            // Expected exception
        }
    }

    @Test
    public void testSerialize() {
        // Typical use case
        String original = "Hello, world!";
        byte[] serialized = SerializationUtils.serialize(original);
        assertNotNull(serialized);
        
        // Edge case: Null object
        String nullObject = null;
        try {
            SerializationUtils.serialize(nullObject);
            fail("IllegalArgumentException expected but not thrown");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
        
        // Edge case: Empty string
        String emptyString = "";
        byte[] serializedEmpty = SerializationUtils.serialize(emptyString);
        assertNotNull(serializedEmpty);
        
        // Error scenario: Not serializable object
        NotSerializableObject notSerializable = new NotSerializableObject();
        try {
            SerializationUtils.serialize(notSerializable);
            fail("SerializationException expected but not thrown");
        } catch (SerializationException e) {
            // Expected exception
        }
    }

    @Test
    public void testDeserialize() {
        // Typical use case
        String original = "Hello, world!";
        byte[] serialized = SerializationUtils.serialize(original);
        String deserialized = (String) SerializationUtils.deserialize(serialized);
        assertEquals(original, deserialized);
        
        // Edge case: Null input stream
        InputStream nullInputStream = null;
        try {
            SerializationUtils.deserialize(nullInputStream);
            fail("IllegalArgumentException expected but not thrown");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
        
        // Edge case: Empty byte array
        byte[] emptyBytes = new byte[0];
        try {
            SerializationUtils.deserialize(emptyBytes);
            fail("IllegalArgumentException expected but not thrown");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
        
        // Error scenario: Invalid input stream
        ByteArrayInputStream invalidInputStream = new ByteArrayInputStream("invalid".getBytes());
        try {
            SerializationUtils.deserialize(invalidInputStream);
            fail("SerializationException expected but not thrown");
        } catch (SerializationException e) {
            // Expected exception
        }
    }
    
    private static class NotSerializableObject {
        // Class definition intentionally left empty
    }

}