
import org.apache.commons.lang3.SerializationException;
import org.junit.Test;

import java.io.*;

import static org.junit.Assert.*;

public class SerializationUtilsTest {

    @Test
    public void testCloneNullObject() {
        assertNull(SerializationUtils.clone(null));
    }

    @Test(expected = SerializationException.class)
    public void testCloneNonSerializableObject() {
        SerializationUtils.clone(new NonSerializableObject());
    }

    @Test
    public void testSerializeNullObject() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            SerializationUtils.serialize(null, outputStream);
        } catch (IllegalArgumentException e) {
            assertEquals("The OutputStream must not be null", e.getMessage());
        }
    }

    @Test
    public void testSerializeNullOutputStream() {
        try {
            SerializationUtils.serialize(new SerializableObject(), null);
        } catch (IllegalArgumentException e) {
            assertEquals("The OutputStream must not be null", e.getMessage());
        }
    }

    @Test
    public void testSerializeIOException() {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream() {
                @Override
                public synchronized void write(byte[] b, int off, int len) {
                    throw new RuntimeException("IOException while writing");
                }
            };

            SerializationUtils.serialize(new SerializableObject(), outputStream);
        } catch (SerializationException e) {
            assertTrue(e.getCause() instanceof RuntimeException);
            assertEquals("IOException while writing", e.getCause().getMessage());
        }
    }

    @Test
    public void testSerializeNullObject() {
        assertNull(SerializationUtils.serialize(null));
    }

    @Test
    public void testDeserializeNullInputStream() {
        try {
            SerializationUtils.deserialize(null);
        } catch (IllegalArgumentException e) {
            assertEquals("The InputStream must not be null", e.getMessage());
        }
    }

    @Test
    public void testDeserializeIOException() {
        try {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[0]) {
                @Override
                public synchronized int read(byte[] b, int off, int len) {
                    throw new RuntimeException("IOException while reading");
                }
            };

            SerializationUtils.deserialize(inputStream);
        } catch (SerializationException e) {
            assertTrue(e.getCause() instanceof RuntimeException);
            assertEquals("IOException while reading", e.getCause().getMessage());
        }
    }

    @Test
    public void testDeserializeNullObjectData() {
        try {
            SerializationUtils.deserialize(null);
        } catch (IllegalArgumentException e) {
            assertEquals("The byte[] must not be null", e.getMessage());
        }
    }

    private static class SerializableObject implements Serializable {
    }

    private static class NonSerializableObject {
    }
}
