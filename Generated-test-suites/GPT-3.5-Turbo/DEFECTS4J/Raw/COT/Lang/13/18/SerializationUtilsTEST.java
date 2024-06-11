import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.lang3.SerializationException;
import org.apache.commons.lang3.SerializationUtils;

public class SerializationUtilsTest {
    
    // Test clone method
    
    @Test
    public void testClone() {
        // Test cloning of a Serializable object
        SerializableObject obj1 = new SerializableObject();
        SerializableObject obj2 = SerializationUtils.clone(obj1);
        assertNotSame(obj1, obj2);
        assertEquals(obj1.toString(), obj2.toString());
        
        // Test cloning of a null object
        SerializableObject obj3 = null;
        SerializableObject obj4 = SerializationUtils.clone(obj3);
        assertNull(obj4);
    }
    
    // Test serialize methods
    
    @Test
    public void testSerializeOutputStream() {
        // Test serialization of a Serializable object
        SerializableObject obj1 = new SerializableObject();
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        SerializationUtils.serialize(obj1, output);
        byte[] bytes = output.toByteArray();
        assertNotNull(bytes);
        
        // Test serialization of a null object
        SerializableObject obj2 = null;
        ByteArrayOutputStream output2 = new ByteArrayOutputStream();
        SerializationUtils.serialize(obj2, output2);
        byte[] bytes2 = output2.toByteArray();
        assertNotNull(bytes2);
    }
    
    @Test
    public void testSerialize() {
        // Test serialization of a Serializable object
        SerializableObject obj1 = new SerializableObject();
        byte[] bytes = SerializationUtils.serialize(obj1);
        assertNotNull(bytes);
        
        // Test serialization of a null object
        SerializableObject obj2 = null;
        byte[] bytes2 = SerializationUtils.serialize(obj2);
        assertNotNull(bytes2);
    }
    
    // Test deserialize methods
    
    @Test
    public void testDeserializeInputStream() {
        // Test deserialization of a serialized object
        SerializableObject obj1 = new SerializableObject();
        byte[] bytes = SerializationUtils.serialize(obj1);
        ByteArrayInputStream input = new ByteArrayInputStream(bytes);
        Object obj2 = SerializationUtils.deserialize(input);
        assertNotNull(obj2);
        assertTrue(obj2 instanceof SerializableObject);
        
        // Test deserialization of a null input stream
        ByteArrayInputStream input2 = null;
        Object obj3 = SerializationUtils.deserialize(input2);
        assertNull(obj3);
    }
    
    @Test
    public void testDeserialize() {
        // Test deserialization of a serialized object
        SerializableObject obj1 = new SerializableObject();
        byte[] bytes = SerializationUtils.serialize(obj1);
        Object obj2 = SerializationUtils.deserialize(bytes);
        assertNotNull(obj2);
        assertTrue(obj2 instanceof SerializableObject);
        
        // Test deserialization of a null byte array
        byte[] bytes2 = null;
        Object obj3 = SerializationUtils.deserialize(bytes2);
        assertNull(obj3);
    }
    
    // Test ClassLoaderAwareObjectInputStream
    
    // No need to test this class separately as it is used internally by SerializationUtils
    
}

class SerializableObject implements Serializable {
    private static final long serialVersionUID = 1L;

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hashCode = 17;
        return hashCode;
    }

    @Override
    public String toString() {
        return "SerializableObject";
    }
}