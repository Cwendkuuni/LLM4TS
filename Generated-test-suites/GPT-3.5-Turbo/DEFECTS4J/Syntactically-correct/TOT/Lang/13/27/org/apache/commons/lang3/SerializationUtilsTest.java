import org.junit.Test;
import org.apache.commons.lang3.SerializationException;
import org.apache.commons.lang3.SerializationUtils;

import static org.junit.Assert.*;

public class SerializationUtilsTest {

    // clone method tests
    @Test
    public void testClone_withNullObject_returnsNull() {
        assertNull(SerializationUtils.clone(null));
    }

    @Test
    public void testClone_withSerializableObject_returnsClonedObject() {
        // Create a serializable object
        SerializableObject original = new SerializableObject("Test");

        // Clone the object
        SerializableObject cloned = SerializationUtils.clone(original);

        // Check that the cloned object is not the same as the original object
        assertNotSame(original, cloned);

        // Check that the cloned object is equal to the original object
        assertEquals(original, cloned);
    }

    // serialize method tests
    @Test
    public void testSerialize_withNullObject_throwsIllegalArgumentException() {
        try {
            SerializationUtils.serialize(null);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            // Test passed
        }
    }

    @Test
    public void testSerialize_withSerializableObject_returnsByteArray() {
        // Create a serializable object
        SerializableObject object = new SerializableObject("Test");

        // Serialize the object
        byte[] serialized = SerializationUtils.serialize(object);

        // Check that the serialized byte array is not null
        assertNotNull(serialized);

        // Check that the serialized byte array is not empty
        assertTrue(serialized.length > 0);
    }

    // deserialize method tests
    @Test
    public void testDeserialize_withNullInputStream_throwsIllegalArgumentException() {
        try {
            SerializationUtils.deserialize(null);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            // Test passed
        }
    }

    @Test
    public void testDeserialize_withValidInputStream_returnsDeserializedObject() {
        // Create a serializable object
        SerializableObject original = new SerializableObject("Test");

        // Serialize the object
        byte[] serialized = SerializationUtils.serialize(original);

        // Deserialize the object
        SerializableObject deserialized = (SerializableObject) SerializationUtils.deserialize(serialized);

        // Check that the deserialized object is not null
        assertNotNull(deserialized);

        // Check that the deserialized object is equal to the original object
        assertEquals(original, deserialized);
    }

    @Test(expected = SerializationException.class)
    public void testDeserialize_withInvalidInputStream_throwsSerializationException() {
        // Create an invalid input stream
        InputStream invalidInputStream = new ByteArrayInputStream(new byte[] {});

        // Deserialize the object
        SerializationUtils.deserialize(invalidInputStream);
    }

    // helper class for testing serialization
    private static class SerializableObject implements Serializable {
        private static final long serialVersionUID = 1L;
        
        private String data;
        
        public SerializableObject(String data) {
            this.data = data;
        }
        
        public String getData() {
            return data;
        }
        
        public void setData(String data) {
            this.data = data;
        }
        
        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }
            SerializableObject other = (SerializableObject) obj;
            return Objects.equals(data, other.data);
        }
        
        @Override
        public int hashCode() {
            return Objects.hash(data);
        }
    }
}