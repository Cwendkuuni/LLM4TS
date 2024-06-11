import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.junit.Test;

public class SerializationUtilsTest {

    @Test
    public void testClone() {
        // Typical use case
        Map<String, Integer> map = new HashMap<>();
        map.put("key1", 1);
        map.put("key2", 2);
        Map<String, Integer> clonedMap = SerializationUtils.clone(map);
        assertNotSame(map, clonedMap);
        assertEquals(map, clonedMap);
        
        // Edge case - null input
        assertNull(SerializationUtils.clone(null));
        
        // Error scenario - ClassNotFoundException
        try {
            SerializationUtils.clone(new Object());
            fail("Expected SerializationException to be thrown");
        } catch (SerializationException e) {
            // Expected
        }
    }

    @Test
    public void testSerialize() {
        // Typical use case
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        SerializationUtils.serialize("Test", out);
        byte[] data = out.toByteArray();
        assertNotNull(data);
        
        // Edge case - null input
        try {
            SerializationUtils.serialize(null, out);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            // Expected
        }
        
        // Error scenario - IOException
        try {
            SerializationUtils.serialize("Test", null);
            fail("Expected SerializationException to be thrown");
        } catch (SerializationException e) {
            // Expected
        }
    }

    @Test
    public void testDeserialize() {
        // Typical use case
        byte[] bytes = "Test".getBytes();
        ByteArrayInputStream in = new ByteArrayInputStream(bytes);
        Object obj = SerializationUtils.deserialize(in);
        assertEquals("Test", obj);
        
        // Edge case - null input
        try {
            SerializationUtils.deserialize((InputStream) null);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            // Expected
        }
        
        // Error scenario - ClassNotFoundException
        try {
            SerializationUtils.deserialize(new byte[] {0});
            fail("Expected SerializationException to be thrown");
        } catch (SerializationException e) {
            // Expected
        }
        
        // Error scenario - IOException
        try {
            SerializationUtils.deserialize((byte[]) null);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

}