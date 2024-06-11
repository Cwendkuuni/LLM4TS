import static org.junit.Assert.*;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class SerializationUtilsTest {

    @Test
    public void testClone() {
        // Test case: Object is null
        assertNull(SerializationUtils.clone(null));

        // Test case: Object is not null
        TestObject obj = new TestObject(1);
        TestObject clonedObj = SerializationUtils.clone(obj);
        assertNotNull(clonedObj);
        assertNotSame(obj, clonedObj);
        assertEquals(obj.getId(), clonedObj.getId());
    }

    @Test
    public void testSerialize() {
        // Test case: Object is null
        try {
            SerializationUtils.serialize(null, null);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }

        // Test case: Object is not null
        TestObject obj = new TestObject(1);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        SerializationUtils.serialize(obj, baos);
        byte[] serializedObj = baos.toByteArray();
        assertNotNull(serializedObj);
        assertTrue(serializedObj.length > 0);
    }

    @Test
    public void testDeserialize() {
        // Test case: Input stream is null
        try {
            SerializationUtils.deserialize(null);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }

        // Test case: Object is not null
        TestObject obj = new TestObject(1);
        byte[] serializedObj = serializeObject(obj);
        ByteArrayInputStream bais = new ByteArrayInputStream(serializedObj);
        TestObject deserializedObj = (TestObject) SerializationUtils.deserialize(bais);
        assertNotNull(deserializedObj);
        assertNotSame(obj, deserializedObj);
        assertEquals(obj.getId(), deserializedObj.getId());
    }

    // Helper method to serialize an object to byte array
    private byte[] serializeObject(Serializable obj) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream out = null;
        try {
            out = new ObjectOutputStream(baos);
            out.writeObject(obj);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return baos.toByteArray();
    }

    // Helper class for testing
    private static class TestObject implements Serializable {
        private static final long serialVersionUID = 1L;
        private int id;

        public TestObject(int id) {
            this.id = id;
        }

        public int getId() {
            return id;
        }
    }
}