package org.apache.commons.lang3;

import org.junit.Test;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class SerializationUtilsTest {

    // Clone tests
    //-----------------------------------------------------------------------

    @Test
    public void testClone_NullObject_ReturnsNull() {
        // Arrange
        Object obj = null;

        // Act
        Object result = SerializationUtils.clone(obj);

        // Assert
        assertNull(result);
    }

    @Test
    public void testClone_SerializableObject_ReturnsClonedObject() {
        // Arrange
        TestObject obj = new TestObject(10, "test");

        // Act
        TestObject result = SerializationUtils.clone(obj);

        // Assert
        assertNotSame(obj, result);
        assertEquals(obj.getValue(), result.getValue());
        assertEquals(obj.getName(), result.getName());
    }

    // Serialize tests
    //-----------------------------------------------------------------------

    @Test(expected = IllegalArgumentException.class)
    public void testSerialize_NullObject_ThrowsIllegalArgumentException() {
        // Arrange
        Serializable obj = null;
        OutputStream outputStream = new ByteArrayOutputStream();

        // Act
        SerializationUtils.serialize(obj, outputStream);

        // Assert
        // Expects IllegalArgumentException to be thrown
    }

    @Test
    public void testSerialize_SerializableObject_ReturnsByteArray() {
        // Arrange
        TestObject obj = new TestObject(10, "test");
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        // Act
        SerializationUtils.serialize(obj, outputStream);
        byte[] result = outputStream.toByteArray();

        // Assert
        assertNotNull(result);
        assertTrue(result.length > 0);
    }

    // Deserialize tests
    //-----------------------------------------------------------------------

    @Test(expected = IllegalArgumentException.class)
    public void testDeserialize_NullInputStream_ThrowsIllegalArgumentException() {
        // Arrange
        InputStream inputStream = null;

        // Act
        SerializationUtils.deserialize(inputStream);

        // Assert
        // Expects IllegalArgumentException to be thrown
    }

    @Test
    public void testDeserialize_ByteArray_ReturnsDeserializedObject() {
        // Arrange
        TestObject obj = new TestObject(10, "test");
        byte[] data = SerializationUtils.serialize(obj);
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);

        // Act
        Object result = SerializationUtils.deserialize(inputStream);

        // Assert
        assertNotNull(result);
        assertTrue(result instanceof TestObject);
        TestObject deserializedObj = (TestObject) result;
        assertEquals(obj.getValue(), deserializedObj.getValue());
        assertEquals(obj.getName(), deserializedObj.getName());
    }

    // Helper classes
    //-----------------------------------------------------------------------

    private static class TestObject implements Serializable {
        private int value;
        private String name;

        public TestObject(int value, String name) {
            this.value = value;
            this.name = name;
        }

        public int getValue() {
            return value;
        }

        public String getName() {
            return name;
        }
    }
}