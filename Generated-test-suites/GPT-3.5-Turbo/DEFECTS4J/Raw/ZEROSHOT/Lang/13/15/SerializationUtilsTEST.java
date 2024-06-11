import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamClass;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class SerializationUtilsTest {

    @Test
    public void testClone() {
        // Test clone with non-null object
        Object obj1 = new Object();
        Object obj2 = SerializationUtils.clone(obj1);
        assertNotSame(obj1, obj2);
        assertEquals(obj1.getClass(), obj2.getClass());
        
        // Test clone with null object
        assertNull(SerializationUtils.clone(null));
    }

    @Test
    public void testSerialize() {
        // Test serialize with non-null object
        Serializable obj1 = "test";
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        SerializationUtils.serialize(obj1, baos);
        byte[] bytes = baos.toByteArray();
        assertTrue(bytes.length > 0);
        
        // Test serialize with null object
        try {
            SerializationUtils.serialize(null, baos);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }

    @Test
    public void testDeserialize() {
        // Test deserialize with non-null object
        byte[] bytes = "test".getBytes();
        ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
        Object obj1 = SerializationUtils.deserialize(bais);
        assertNotNull(obj1);
        assertEquals(String.class, obj1.getClass());
        
        // Test deserialize with null object
        try {
            SerializationUtils.deserialize((InputStream) null);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
        
        // Test deserialize with null byte array
        try {
            SerializationUtils.deserialize((byte[]) null);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }
}