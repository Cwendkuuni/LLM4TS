import static org.junit.Assert.*;
import org.junit.Test;

public class SerializationUtilsTest {

    private static final String TEST_STRING = "Hello World";

    @Test
    public void testClone_NormalCase() {
        String original = TEST_STRING;
        String cloned = SerializationUtils.clone(original);
        assertEquals(original, cloned);
        assertNotSame(original, cloned);
    }

    @Test
    public void testSerialize_NormalCase() {
        String object = TEST_STRING;
        byte[] serialized = SerializationUtils.serialize(object);
        assertNotNull(serialized);
    }

    @Test
    public void testSerialize_NullObject() {
        String object = null;
        try {
            SerializationUtils.serialize(object);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("The OutputStream must not be null", e.getMessage());
        }
    }

    @Test
    public void testSerialize_IOException() {
        // Create a mock output stream that throws an IOException when writeObject is called
        OutputStreamMock mockOutputStream = new OutputStreamMock();
        String object = TEST_STRING;

        try {
            SerializationUtils.serialize(object, mockOutputStream);
            fail("Expected SerializationException");
        } catch (SerializationException e) {
            assertEquals("java.io.IOException", e.getCause().getClass().getName());
        }
    }

    @Test
    public void testDeserialize_NormalCase() {
        String object = TEST_STRING;
        byte[] serialized = SerializationUtils.serialize(object);
        Object deserialized = SerializationUtils.deserialize(serialized);
        assertEquals(object, deserialized);
    }

    @Test
    public void testDeserialize_NullInputStream() {
        InputStream inputStream = null;
        try {
            SerializationUtils.deserialize(inputStream);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("The InputStream must not be null", e.getMessage());
        }
    }

    @Test
    public void testDeserialize_IOException() {
        // Create a mock input stream that throws an IOException when readObject is called
        InputStreamMock mockInputStream = new InputStreamMock();
        byte[] serialized = new byte[1];

        try {
            SerializationUtils.deserialize(mockInputStream);
            fail("Expected SerializationException");
        } catch (SerializationException e) {
            assertEquals("java.io.IOException", e.getCause().getClass().getName());
        }
    }

    private static class OutputStreamMock extends OutputStream {
        @Override
        public void write(int b) throws IOException {
            throw new IOException();
        }
        
        @Override
        public void write(byte[] b) throws IOException {
            throw new IOException();
        }
        
        @Override
        public void write(byte[] b, int off, int len) throws IOException {
            throw new IOException();
        }
    }

    private static class InputStreamMock extends InputStream {
        @Override
        public int read() throws IOException {
            throw new IOException();
        }
    }
}