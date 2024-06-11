
import static org.junit.Assert.*;
import org.junit.Test;

public class SerializationUtilsTest{

    @Test
    public void testClone(){
        // Test null input
        assertNull(SerializationUtils.clone(null));

        // Test non-null input
        Integer integer = new Integer(10);
        Integer clonedInteger;
        clonedInteger = SerializationUtils.clone(integer);
        assertNotSame(integer, clonedInteger);
        assertEquals(integer, clonedInteger);

        // Test deep clone
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("key", new Integer(10));
        Map<String, Object> clonedMap = SerializationUtils.clone(map);
        assertNotSame(map, clonedMap);
        assertEquals(map, clonedMap);
        assertNotSame(map.get("key"), clonedMap.get("key"));
        assertEquals(map.get("key"), clonedMap.get("key"));
    }

    @Test(expected = SerializationException.class)
    public void testCloneSerializationException() {
        // Test serialization exception
        Object object = new Object();
        SerializationUtils.clone(object);
    }

    @Test
    public void testSerialize() throws IOException {
        // Test null input
        try {
            SerializationUtils.serialize(null, new ByteArrayOutputStream());
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException ex) {
            // Expected exception
        }

        // Test non-null input
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        SerializationUtils.serialize(new Integer(10), baos);
        byte[] bytes = baos.toByteArray();
        assertNotNull(bytes);
        assertTrue(bytes.length > 0);
    }

    @Test
    public void testDeserialize() {
        // Test null input
        try {
            SerializationUtils.deserialize(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException ex) {
            // Expected exception
        }

        // Test non-null input
        byte[] bytes = {1, 2, 3, 4, 5};
        Object object = SerializationUtils.deserialize(bytes);
        assertNotNull(object);
    }

    @Test(expected = SerializationException.class)
    public void testDeserializeSerializationException() {
        // Test serialization exception
        byte[] bytes = {1, 2, 3, 4, 5};
        SerializationUtils.deserialize(bytes);
    }
    
}
