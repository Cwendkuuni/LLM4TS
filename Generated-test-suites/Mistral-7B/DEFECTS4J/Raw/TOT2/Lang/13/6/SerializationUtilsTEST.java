import org.apache.commons.lang3.SerializationUtils;
import org.apache.commons.lang3.Cloneable;
import org.apache.commons.lang3.SerializationException;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class SerializationUtilsTest {

    @Test
    public void testClone() throws SerializationException {
        // Given: An instance of a cloneable class
        Cloneable original = new HashMap<>();
        
        // When: Cloning the instance using SerializationUtils.clone()
        Cloneable cloned = SerializationUtils.clone(original);
        
        // Then: The cloned instance should be equal to the original instance
        assertEquals(original, cloned);
    }

    @Test
    public void testSerializeAndDeserializeByteArray() throws SerializationException {
        // Given: An instance of a Serializable class
        Serializable original = new HashMap<>();
        
        // When: Serializing the instance to a byte array using SerializationUtils.serialize()
        byte[] serialized = SerializationUtils.serialize(original);
        
        // And: Deserializing the byte array to a new instance using SerializationUtils.deserialize()
        Serializable deserialized = SerializationUtils.deserialize(serialized);
        
        // Then: The deserialized instance should be equal to the original instance
        assertEquals(original, deserialized);
    }

    @Test(expected = SerializationException.class)
    public void testSerializationExceptionNullInput() {
        // Given: A null input
        SerializationUtils.serialize(null, new ByteArrayOutputStream());
    }

    @Test(expected = SerializationException.class)
    public void testSerializationExceptionIOException() {
        // Given: An InputStream or OutputStream that throws an IOException
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream() {
            @Override
            public void close() throws IOException {
                throw new IOException();
            }
        };
        Serializable obj = new Object();

        // When: Trying to serialize an object to the OutputStream
        SerializationUtils.serialize(obj, outputStream);
    }

    @Test
    public void testClassLoaderAwareObjectInputStream() throws SerializationException, IOException, ClassNotFoundException {
        // Given: A custom ClassLoader
        ClassLoader customClassLoader = this.getClass().getClassLoader();

        // And: An instance of a Serializable class
        Serializable original = new HashMap<>();

        // When: Serializing the instance to a byte array using SerializationUtils.serialize()
        byte[] serialized = SerializationUtils.serialize(original);

        // And: Creating a ClassLoaderAwareObjectInputStream with the custom ClassLoader
        InputStream inputStream = new ByteArrayInputStream(serialized);
        ObjectInputStream in = new SerializationUtils.ClassLoaderAwareObjectInputStream(inputStream, customClassLoader);

        // And: Deserializing the byte array to a new instance using the ClassLoaderAwareObjectInputStream
        Serializable deserialized = (Serializable) in.readObject();

        // Then: The deserialized instance should be equal to the original instance
        assertEquals(original, deserialized);
    }
}