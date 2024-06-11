import static org.junit.Assert.*;
import org.junit.Test;

public class SerializationUtilsTest {
    
    @Test
    public void testClone() {
        // Test case 1: Test cloning of null object
        assertNull(SerializationUtils.clone(null));
        
        // Test case 2: Test cloning of object
        Map<String, Integer> original = new HashMap<>();
        original.put("One", 1);
        original.put("Two", 2);
        Map<String, Integer> clone = SerializationUtils.clone(original);
        assertEquals(original, clone);
    }
    
    @Test
    public void testSerialize() {
        // Test case 1: Test serialization of null object
        ByteArrayOutputStream baos1 = new ByteArrayOutputStream();
        SerializationUtils.serialize(null, baos1);
        assertEquals(0, baos1.toByteArray().length);
        
        // Test case 2: Test serialization of object
        ByteArrayOutputStream baos2 = new ByteArrayOutputStream();
        Map<String, Integer> object = new HashMap<>();
        object.put("One", 1);
        SerializationUtils.serialize(object, baos2);
        assertFalse(baos2.toByteArray().length == 0);
    }
    
    @Test
    public void testDeserialize() {
        // Test case 1: Test deserialization of null object
        ByteArrayInputStream bais1 = new ByteArrayInputStream(new byte[0]);
        assertNull(SerializationUtils.deserialize(bais1));
        
        // Test case 2: Test deserialization of object
        ByteArrayInputStream bais2 = new ByteArrayInputStream(new byte[]{(byte) 1, (byte) 2});
        assertNotNull(SerializationUtils.deserialize(bais2));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testSerializeNullOutputStream() {
        SerializationUtils.serialize(new Object(), null);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testDeserializeNullInputStream() {
        SerializationUtils.deserialize(null);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testDeserializeNullByteArray() {
        SerializationUtils.deserialize(null);
    }
}