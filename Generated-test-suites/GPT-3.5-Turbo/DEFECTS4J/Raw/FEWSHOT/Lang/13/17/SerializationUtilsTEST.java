package org.apache.commons.lang3;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertArrayEquals;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamClass;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.HashMap;
import org.junit.Assert;
import java.util.Map;

public class SerializationUtilsTest {
    @Test
    public void testClone() {
        // Test for a simple Serializable object
        TestObject testObject = new TestObject("test");
        TestObject clonedObject = SerializationUtils.clone(testObject);
        assertEquals(testObject.getValue(), clonedObject.getValue());
        assertNotSame(testObject, clonedObject);

        // Test for a complex Serializable object
        TestObject testObject2 = new TestObject("test", new HashMap<>());
        testObject2.getMap().put("key1", "value1");
        testObject2.getMap().put("key2", "value2");
        TestObject clonedObject2 = SerializationUtils.clone(testObject2);
        assertEquals(testObject2.getValue(), clonedObject2.getValue());
        assertEquals(testObject2.getMap(), clonedObject2.getMap());
        assertNotSame(testObject2, clonedObject2);

        // Test for a null object
        assertNull(SerializationUtils.clone(null));
    }

    @Test
    public void testSerializeDeserialize() throws IOException, ClassNotFoundException {
        // Test for a simple Serializable object
        TestObject testObject = new TestObject("test");
        byte[] serialized = SerializationUtils.serialize(testObject);
        TestObject deserializedObject = (TestObject) SerializationUtils.deserialize(serialized);
        assertEquals(testObject.getValue(), deserializedObject.getValue());
        assertNotSame(testObject, deserializedObject);

        // Test for a complex Serializable object
        TestObject testObject2 = new TestObject("test", new HashMap<>());
        testObject2.getMap().put("key1", "value1");
        testObject2.getMap().put("key2", "value2");
        byte[] serialized2 = SerializationUtils.serialize(testObject2);
        TestObject deserializedObject2 = (TestObject) SerializationUtils.deserialize(serialized2);
        assertEquals(testObject2.getValue(), deserializedObject2.getValue());
        assertEquals(testObject2.getMap(), deserializedObject2.getMap());
        assertNotSame(testObject2, deserializedObject2);

        // Test for a null object
        assertNull(SerializationUtils.deserialize(null));
    }

    // Helper class for testing serialization
    private static class TestObject implements Serializable {
        private String value;
        private Map<String, String> map;

        public TestObject(String value) {
            this.value = value;
            this.map = null;
        }

        public TestObject(String value, Map<String, String> map) {
            this.value = value;
            this.map = map;
        }

        public String getValue() {
            return value;
        }

        public Map<String, String> getMap() {
            return map;
        }
    }
}