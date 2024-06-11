package org.apache.commons.lang3;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamClass;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class SerializationUtilsTest {
    @Test
    public void testClone() {
        // Create a test object
        TestObject obj = new TestObject("test", 10);

        // Clone the test object using serialization
        TestObject clonedObj = SerializationUtils.clone(obj);

        // Test for equality between the original and cloned objects
        assertEquals(obj, clonedObj);
    }

    @Test
    public void testSerializeDeserialize() throws IOException, ClassNotFoundException {
        // Create a test object
        TestObject obj = new TestObject("test", 10);

        // Serialize the test object to a byte array
        byte[] objectData = SerializationUtils.serialize(obj);

        // Deserialize the byte array back to an object
        TestObject deserializedObj = (TestObject) SerializationUtils.deserialize(objectData);

        // Test for equality between the original and deserialized objects
        assertEquals(obj, deserializedObj);
    }

    // Helper class for testing serialization
    private static class TestObject implements Serializable {
        private static final long serialVersionUID = 1L;
        private String name;
        private int value;

        TestObject(String name, int value) {
            this.name = name;
            this.value = value;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }
            TestObject other = (TestObject) obj;
            return name.equals(other.name) && value == other.value;
        }
    }
}