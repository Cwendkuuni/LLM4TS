import static org.junit.Assert.*;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.IOException;
import java.io.Serializable;

public class SerializationUtilsTest {

    @Test
    public void testClone() {
        // Test null object
        assertNull(SerializationUtils.clone(null));

        // Test non-null object
        Serializable obj = new SerializableObject();
        Serializable clone = SerializationUtils.clone(obj);
        assertNotNull(clone);
        assertNotSame(obj, clone);
        assertTrue(clone instanceof SerializableObject);
    }

    @Test(expected = SerializationException.class)
    public void testCloneWithSerializationException() {
        // Test object that throws SerializationException
        Serializable obj = new ExceptionObject();
        SerializationUtils.clone(obj);
    }

    @Test
    public void testSerialize() {
        // Test null object
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            SerializationUtils.serialize(null, baos);
        } catch (IllegalArgumentException e) {
            assertEquals("The OutputStream must not be null", e.getMessage());
        }

        // Test non-null object
        Serializable obj = new SerializableObject();
        baos = new ByteArrayOutputStream();
        SerializationUtils.serialize(obj, baos);
        byte[] bytes = baos.toByteArray();
        assertNotNull(bytes);
        assertTrue(bytes.length > 0);
    }

    @Test
    public void testSerializeWithSerializationException() {
        // Test object that throws SerializationException
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Serializable obj = new ExceptionObject();
        try {
            SerializationUtils.serialize(obj, baos);
        } catch (SerializationException e) {
            assertEquals("java.io.IOException", e.getCause().getClass().getName());
        }
    }

    @Test
    public void testDeserialize() {
        // Test null input stream
        try {
            SerializationUtils.deserialize(null);
        } catch (IllegalArgumentException e) {
            assertEquals("The InputStream must not be null", e.getMessage());
        }

        // Test non-null input stream
        byte[] bytes = {1, 2, 3, 4, 5};
        ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
        Object obj = SerializationUtils.deserialize(bais);
        assertNotNull(obj);
    }

    @Test
    public void testDeserializeWithSerializationException() {
        // Test object that throws SerializationException
        byte[] bytes = {1, 2, 3, 4, 5};
        ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
        Object obj = SerializationUtils.deserialize(bais);
        assertNull(obj);
    }

    private static class SerializableObject implements Serializable {
        private static final long serialVersionUID = 1L;
    }

    private static class ExceptionObject implements Serializable {
        private static final long serialVersionUID = 1L;

        private void writeObject(ObjectOutputStream out) throws IOException {
            throw new IOException();
        }
    }
}