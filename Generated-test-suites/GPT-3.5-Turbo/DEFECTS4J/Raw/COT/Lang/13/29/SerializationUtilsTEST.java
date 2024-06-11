
import org.junit.Test;
import static org.junit.Assert.*;

public class SerializationUtilsTest {

    @Test
    public void testClone() {
        // Test cloning an object
        MyClass obj = new MyClass("Hello, world!");
        MyClass clonedObj = SerializationUtils.clone(obj);
        assertNotSame(obj, clonedObj);
        assertEquals(obj, clonedObj);
    }

    // Test serialize and deserialize methods

    @Test
    public void testSerialize() {
        // Test serializing an object to a byte array
        MyClass obj = new MyClass("Hello, world!");
        byte[] serializedData = SerializationUtils.serialize(obj);
        assertNotNull(serializedData);
        assertTrue(serializedData.length > 0);
    }

    @Test
    public void testDeserialize() {
        // Test deserializing an object from a byte array
        MyClass obj = new MyClass("Hello, world!");
        byte[] serializedData = SerializationUtils.serialize(obj);
        Object deserializedObj = SerializationUtils.deserialize(serializedData);
        assertNotNull(deserializedObj);
        assertEquals(obj, deserializedObj);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSerializeNullOutputStream() {
        // Test passing null outputStream to serialize method
        MyClass obj = new MyClass("Hello, world!");
        SerializationUtils.serialize(obj, null);
    }

    @Test(expected = SerializationException.class)
    public void testSerializeIOException() {
        // Test handling IOException while serializing an object
        MyClass obj = new MyClass("Hello, world!");
        OutputStream outputStream = new MockOutputStream(true); // Throws IOException when writing
        SerializationUtils.serialize(obj, outputStream);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeserializeNullInputStream() {
        // Test passing null inputStream to deserialize method
        SerializationUtils.deserialize(null);
    }

    @Test(expected = SerializationException.class)
    public void testDeserializeIOException() {
        // Test handling IOException while deserializing an object
        InputStream inputStream = new MockInputStream(true); // Throws IOException when reading
        SerializationUtils.deserialize(inputStream);
    }

    // Custom test classes

    private static class MyClass implements Serializable {
        private String message;

        public MyClass(String message) {
            this.message = message;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null || getClass() != obj.getClass())
                return false;
            MyClass myClass = (MyClass) obj;
            return Objects.equals(message, myClass.message);
        }

        @Override
        public int hashCode() {
            return Objects.hash(message);
        }
    }

    private static class MockOutputStream extends OutputStream {
        private boolean throwException;

        public MockOutputStream(boolean throwException) {
            this.throwException = throwException;
        }

        @Override
        public void write(int b) throws IOException {
            if (throwException) {
                throw new IOException("MockIOException");
            }
        }
    }

    private static class MockInputStream extends InputStream {
        private boolean throwException;

        public MockInputStream(boolean throwException) {
            this.throwException = throwException;
        }

        @Override
        public int read() throws IOException {
            if (throwException) {
                throw new IOException("MockIOException");
            }
            return 0;
        }
    }
}

