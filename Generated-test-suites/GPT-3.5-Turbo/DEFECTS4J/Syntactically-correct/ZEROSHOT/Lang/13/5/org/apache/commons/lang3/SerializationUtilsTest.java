import org.junit.Test;
import org.apache.commons.lang3.SerializationException;
import org.apache.commons.lang3.SerializationUtils;

import static org.junit.Assert.*;

public class SerializationUtilsTest {

    @Test
    public void testClone_NullObject_ReturnsNull() {
        Object clonedObject = SerializationUtils.clone(null);
        assertNull(clonedObject);
    }
    
    @Test
    public void testClone_SerializableObject_ReturnsClonedObject() {
        TestObject testObject = new TestObject(1, "Test");
        TestObject clonedObject = SerializationUtils.clone(testObject);
        assertNotSame(testObject, clonedObject);
        assertEquals(testObject.getId(), clonedObject.getId());
        assertEquals(testObject.getName(), clonedObject.getName());
    }
    
    @Test(expected = SerializationException.class)
    public void testClone_NonSerializableObject_ThrowsSerializationException() {
        NonSerializableObject nonSerializableObject = new NonSerializableObject();
        SerializationUtils.clone(nonSerializableObject);
    }
    
    @Test
    public void testSerialize_NullObject_ThrowsIllegalArgumentException() {
        try {
            SerializationUtils.serialize(null, null);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("The OutputStream must not be null", e.getMessage());
        }
    }
    
    @Test
    public void testSerialize_SerializableObject_ReturnsByteArray() {
        TestObject testObject = new TestObject(1, "Test");
        byte[] serializedObject = SerializationUtils.serialize(testObject);
        assertNotNull(serializedObject);
        assertTrue(serializedObject.length > 0);
    }
    
    @Test
    public void testDeserialize_NullInputStream_ThrowsIllegalArgumentException() {
        try {
            SerializationUtils.deserialize(null);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("The InputStream must not be null", e.getMessage());
        }
    }
    
    @Test
    public void testDeserialize_ByteArray_ReturnsDeserializedObject() {
        TestObject testObject = new TestObject(1, "Test");
        byte[] serializedObject = SerializationUtils.serialize(testObject);
        Object deserializedObject = SerializationUtils.deserialize(serializedObject);
        assertNotNull(deserializedObject);
        assertEquals(testObject.getClass(), deserializedObject.getClass());
        TestObject deserializedTestObject = (TestObject) deserializedObject;
        assertEquals(testObject.getId(), deserializedTestObject.getId());
        assertEquals(testObject.getName(), deserializedTestObject.getName());
    }
    
    @Test(expected = SerializationException.class)
    public void testDeserialize_InvalidByteArray_ThrowsSerializationException() {
        byte[] invalidSerializedObject = {0x01, 0x02, 0x03};
        SerializationUtils.deserialize(invalidSerializedObject);
    }

    private static class TestObject implements Serializable {
        private int id;
        private String name;

        public TestObject(int id, String name) {
            this.id = id;
            this.name = name;
        }

        public int getId() {
            return id;
        }

        public String getName() {
            return name;
        }
    }
    
    private static class NonSerializableObject {
        // Class without Serializable interface
    }
}