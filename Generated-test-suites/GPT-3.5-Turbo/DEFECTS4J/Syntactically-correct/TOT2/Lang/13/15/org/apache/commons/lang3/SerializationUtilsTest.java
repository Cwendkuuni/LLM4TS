import static org.junit.Assert.*;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import org.apache.commons.lang3.SerializationUtils;

public class SerializationUtilsTest {

    @Test
    // Typical use case for clone method
    public void testCloneTypical() {
        String original = "Hello world";
        String cloned = SerializationUtils.clone(original);
        assertEquals(original, cloned);
    }

    @Test
    // Edge case for clone method with null input
    public void testCloneNullInput() {
        assertNull(SerializationUtils.clone(null));
    }

    @Test
    // Error scenario for clone method with IOException during serialization
    public void testCloneSerializationException() {
        // Creating an object that throws IOException during serialization
        SerializableObjectWithIOException obj = new SerializableObjectWithIOException();
        assertThrows(SerializationException.class, () -> {
            SerializationUtils.clone(obj);
        });
    }

    @Test
    // Typical use case for serialize method
    public void testSerializeTypical() {
        String data = "Test data";
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        SerializationUtils.serialize(data, baos);
        byte[] serializedData = baos.toByteArray();
        assertNotNull(serializedData);
    }

    @Test
    // Edge case for serialize method with null input
    public void testSerializeNullInput() {
        assertThrows(IllegalArgumentException.class, () -> {
            SerializationUtils.serialize(null, new ByteArrayOutputStream());
        });
    }

    @Test
    // Error scenario for serialize method with IOException during serialization
    public void testSerializeIOException() {
        // Creating an object that throws IOException during serialization
        SerializableObjectWithIOException obj = new SerializableObjectWithIOException();
        assertThrows(SerializationException.class, () -> {
            SerializationUtils.serialize(obj, new ByteArrayOutputStream());
        });
    }

    @Test
    // Typical use case for deserialize method
    public void testDeserializeTypical() {
        String data = "Test data";
        byte[] serializedData = SerializationUtils.serialize(data);
        ByteArrayInputStream bais = new ByteArrayInputStream(serializedData);
        Object deserialized = SerializationUtils.deserialize(bais);
        assertEquals(data, deserialized);
    }

    @Test
    // Edge case for deserialize method with null input
    public void testDeserializeNullInput() {
        assertThrows(IllegalArgumentException.class, () -> {
            SerializationUtils.deserialize((InputStream) null);
        });
    }

    @Test
    // Error scenario for deserialize method with ClassNotFoundException
    public void testDeserializeClassNotFoundException() {
        byte[] invalidData = { 0x01, 0x02, 0x03 };
        ByteArrayInputStream bais = new ByteArrayInputStream(invalidData);
        assertThrows(SerializationException.class, () -> {
            SerializationUtils.deserialize(bais);
        });
    }

    @Test
    // Error scenario for deserialize method with IOException
    public void testDeserializeIOException() {
        // Creating an object that throws IOException during deserialization
        DeserializableObjectWithIOException obj = new DeserializableObjectWithIOException();
        byte[] serializedData = SerializationUtils.serialize(obj);
        ByteArrayInputStream bais = new ByteArrayInputStream(serializedData);
        assertThrows(SerializationException.class, () -> {
            SerializationUtils.deserialize(bais);
        });
    }

    // Helper classes for testing error scenarios
    private static class SerializableObjectWithIOException implements Serializable {
        private void writeObject(ObjectOutputStream out) throws IOException {
            throw new IOException("IOException during serialization");
        }
    }

    private static class DeserializableObjectWithIOException implements Serializable {
        private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
            throw new IOException("IOException during deserialization");
        }
    }
}