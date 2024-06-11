import org.junit.*;
import static org.junit.Assert.*;

public class SerializationUtilsTest {
    
    @Test
    public void testClone_NullObject_ReturnsNull() {
        Object obj = null;
        Object clonedObj = SerializationUtils.clone(obj);
        assertNull(clonedObj);
    }
    
    @Test
    public void testClone_SerializedObject_ReturnsClonedObject() {
        String obj = "Test";
        String clonedObj = SerializationUtils.clone(obj);
        assertEquals(obj, clonedObj);
    }
    
    @Test(expected = SerializationException.class)
    public void testClone_SerializationException_ThrowsSerializationException() {
        Object obj = new Object();
        SerializationUtils.clone(obj);
    }
    
    @Test
    public void testSerialize_NullObject_ThrowsIllegalArgumentException() {
        Serializable obj = null;
        OutputStream outputStream = new ByteArrayOutputStream();
        try {
            SerializationUtils.serialize(obj, outputStream);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException ex) {
            // Expected
        }
    }
    
    @Test
    public void testSerialize_SerializedObject_ReturnsCorrectByteArray() {
        String obj = "Test";
        byte[] bytes = SerializationUtils.serialize(obj);
        assertNotNull(bytes);
    }
    
    @Test(expected = SerializationException.class)
    public void testSerialize_SerializationException_ThrowsSerializationException() {
        Serializable obj = new Object();
        OutputStream outputStream = new ByteArrayOutputStream();
        SerializationUtils.serialize(obj, outputStream);
    }
    
    @Test
    public void testDeserialize_NullInputStream_ThrowsIllegalArgumentException() {
        InputStream inputStream = null;
        try {
            SerializationUtils.deserialize(inputStream);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException ex) {
            // Expected
        }
    }
    
    @Test
    public void testDeserialize_SerializedByteArray_ReturnsDeserializedObject() {
        String obj = "Test";
        byte[] bytes = SerializationUtils.serialize(obj);
        Object deserializedObj = SerializationUtils.deserialize(bytes);
        assertEquals(obj, deserializedObj);
    }
    
    @Test(expected = SerializationException.class) 
    public void testDeserialize_SerializationException_ThrowsSerializationException() {
        byte[] bytes = new byte[] {1, 2, 3};
        SerializationUtils.deserialize(bytes);
    }
    
}