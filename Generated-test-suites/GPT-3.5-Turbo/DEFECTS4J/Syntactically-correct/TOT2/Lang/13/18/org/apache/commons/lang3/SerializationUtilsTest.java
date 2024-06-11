import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class SerializationUtilsTest {

    @Test
    public void testClone() {
        Map<String, String> original = new HashMap<>();
        original.put("key", "value");

        Map<String, String> cloned = SerializationUtils.clone(original);

        assertNotNull(cloned);
        assertEquals(original, cloned);
    }

    @Test
    public void testCloneNullInput() {
        Map<String, String> original = null;

        Map<String, String> cloned = SerializationUtils.clone(original);

        assertNull(cloned);
    }

    @Test
    public void testSerialize() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        Map<String, String> map = new HashMap<>();
        map.put("key", "value");

        SerializationUtils.serialize(map, baos);

        byte[] serialized = baos.toByteArray();

        assertNotNull(serialized);
        assertTrue(serialized.length > 0);
    }

    @Test
    public void testSerializeNullInput() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        SerializationUtils.serialize(null, baos);

        byte[] serialized = baos.toByteArray();

        assertNotNull(serialized);
        assertEquals(0, serialized.length);
    }

    @Test
    public void testDeserialize() {
        Map<String, String> original = new HashMap<>();
        original.put("key", "value");

        byte[] serialized = SerializationUtils.serialize(original);

        Map<String, String> deserialized = (Map<String, String>) SerializationUtils.deserialize(serialized);

        assertNotNull(deserialized);
        assertEquals(original, deserialized);
    }

    @Test
    public void testDeserializeNullInput() {
        byte[] serialized = null;

        try {
            SerializationUtils.deserialize(serialized);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testDeserializeInvalidData() {
        byte[] serialized = new byte[] {1, 2, 3};

        try {
            SerializationUtils.deserialize(serialized);
            fail("Expected SerializationException");
        } catch (SerializationException e) {
            // expected
        }
    }
    
    @Test
    public void testClassloaderAwareObjectInputStream() {
        // Test the custom classloader aware object input stream
        // This test is not complete but shows how to add additional tests
        
        //TODO: Add more comprehensive tests for ClassLoaderAwareObjectInputStream
    }

}