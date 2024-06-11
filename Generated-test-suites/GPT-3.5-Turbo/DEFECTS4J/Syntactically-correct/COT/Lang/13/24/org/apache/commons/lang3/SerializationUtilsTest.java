
import static org.junit.Assert.*;

import org.junit.Test;

public class SerializationUtilsTest {
    
    @Test
    public void testClone() {
        // Test cloning an object
        Object obj = new Object();
        Object clone = SerializationUtils.clone(obj);
        assertNotSame(obj, clone);
    }
    
    @Test
    public void testSerialize() {
        // Test serializing an object to a stream
        Object obj = new Object();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        SerializationUtils.serialize(obj, outputStream);
        byte[] serialized = outputStream.toByteArray();
        assertNotNull(serialized);
        assertTrue(serialized.length > 0);
    }
    
    @Test
    public void testDeserialize() {
        // Test deserializing an object from a stream
        byte[] serialized = new byte[] {1, 2, 3};
        ByteArrayInputStream inputStream = new ByteArrayInputStream(serialized);
        Object obj = SerializationUtils.deserialize(inputStream);
        assertNotNull(obj);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testNullSerialize() {
        // Test serializing a null object
        SerializationUtils.serialize(null);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testNullDeserialize() {
        // Test deserializing a null object
        SerializationUtils.deserialize(null);
    }
    
    @Test(expected = SerializationException.class)
    public void testInvalidDeserialize() {
        // Test deserializing an invalid object
        byte[] serialized = new byte[] {1, 2, 3};
        ByteArrayInputStream inputStream = new ByteArrayInputStream(serialized);
        SerializationUtils.deserialize(inputStream);
    }
}
