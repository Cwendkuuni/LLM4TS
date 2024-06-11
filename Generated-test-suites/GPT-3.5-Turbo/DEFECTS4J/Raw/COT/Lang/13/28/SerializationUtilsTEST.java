import org.junit.Test;

import static org.junit.Assert.*;

public class SerializationUtilsTest {

    @Test
    public void testClone() {
        // Test cloning a null object
        assertNull(SerializationUtils.clone(null));
        
        // Test cloning an object
        String original = "Hello";
        String cloned = SerializationUtils.clone(original);
        assertEquals(original, cloned);
        assertNotSame(original, cloned);
    }

    @Test
    public void testSerialize() {
        // Test serialization of a null object
        assertNull(SerializationUtils.serialize(null));
        
        // Test serialization of an object
        String object = "Hello";
        byte[] serializedObject = SerializationUtils.serialize(object);
        assertNotNull(serializedObject);
    }

    @Test
    public void testDeserialize() {
        // Test deserialization of a null input stream
        assertNull(SerializationUtils.deserialize(null));
        
        // Test deserialization of an object
        byte[] serializedObject = SerializationUtils.serialize("Hello");
        Object deserializedObject = SerializationUtils.deserialize(serializedObject);
        assertNotNull(deserializedObject);
        assertTrue(deserializedObject instanceof String);
        assertEquals("Hello", (String) deserializedObject);
    }
}