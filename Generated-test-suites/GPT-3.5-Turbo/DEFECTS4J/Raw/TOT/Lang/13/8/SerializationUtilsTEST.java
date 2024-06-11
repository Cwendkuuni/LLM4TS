import org.junit.Test;
import static org.junit.Assert.*;

public class SerializationUtilsTest {

    // Test cases for clone() method
    @Test
    public void testClone_typicalCase() {
        // Typical case: clone a non-null Serializable object
        TestObject obj = new TestObject("test", 10);
        TestObject clone = SerializationUtils.clone(obj);
        assertEquals(obj.getName(), clone.getName());
        assertEquals(obj.getValue(), clone.getValue());
        assertNotSame(obj, clone);
    }

    @Test
    public void testClone_nullObject() {
        // Edge case: clone a null object
        TestObject obj = null;
        TestObject clone = SerializationUtils.clone(obj);
        assertNull(clone);
    }

    // Test cases for serialize() method
    @Test
    public void testSerialize_typicalCase() {
        // Typical case: serialize a non-null Serializable object
        TestObject obj = new TestObject("test", 10);
        byte[] data = SerializationUtils.serialize(obj);
        assertNotNull(data);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSerialize_nullOutputStream() {
        // Error scenario: serialize an object with null OutputStream
        TestObject obj = new TestObject("test", 10);
        SerializationUtils.serialize(obj, null);
    }

    // Test cases for deserialize() method
    @Test
    public void testDeserialize_typicalCase() {
        // Typical case: deserialize a byte array into an object
        TestObject obj = new TestObject("test", 10);
        byte[] data = SerializationUtils.serialize(obj);
        TestObject deserializedObj = (TestObject) SerializationUtils.deserialize(data);
        assertNotNull(deserializedObj);
        assertEquals(obj.getName(), deserializedObj.getName());
        assertEquals(obj.getValue(), deserializedObj.getValue());
        assertNotSame(obj, deserializedObj);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeserialize_nullInputStream() {
        // Error scenario: deserialize an object with null InputStream
        SerializationUtils.deserialize(null);
    }

    // Test case for ClassLoaderAwareObjectInputStream.resolveClass() method
    @Test
    public void testResolveClass_typicalCase() {
        // Typical case: resolve a class using the provided ClassLoader
        SerializationUtils.ClassLoaderAwareObjectInputStream in = null;
        try {
            in = new SerializationUtils.ClassLoaderAwareObjectInputStream(
                    new ByteArrayInputStream(new byte[0]), getClass().getClassLoader());
            Class<?> clazz = in.resolveClass(new ObjectStreamClass() {});
            assertNotNull(clazz);
        } catch (Exception e) {
            fail("Exception should not be thrown");
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (Exception e) {
                    fail("Exception should not be thrown");
                }
            }
        }
    }

    // Helper class for testing serialize() and deserialize() methods
    private static class TestObject implements Serializable {
        private String name;
        private int value;

        public TestObject(String name, int value) {
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