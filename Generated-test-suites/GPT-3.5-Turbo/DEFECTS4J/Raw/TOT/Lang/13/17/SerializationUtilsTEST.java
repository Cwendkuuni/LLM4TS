import org.junit.Test;
import static org.junit.Assert.*;

public class SerializationUtilsTest {

    private static final String TEST_STRING = "test string";
    private static final int TEST_INT = 10;
    private static final byte[] TEST_BYTE_ARRAY = {1, 2, 3};
    
    // Test cases for clone() method
    
    @Test
    public void testClone_ValidObject_ReturnsClonedObject() {
        // Arrange
        MyObject obj = new MyObject(TEST_STRING, TEST_INT, TEST_BYTE_ARRAY);
        
        // Act
        MyObject clonedObj = SerializationUtils.clone(obj);
        
        // Assert
        assertNotSame(obj, clonedObj);
        assertEquals(obj.getString(), clonedObj.getString());
        assertEquals(obj.getInt(), clonedObj.getInt());
        assertArrayEquals(obj.getByteArray(), clonedObj.getByteArray());
    }
    
    // Test cases for serialize(Serializable obj, OutputStream outputStream) method
    
    @Test(expected = IllegalArgumentException.class)
    public void testSerialize_NullObject_ThrowsIllegalArgumentException() {
        // Arrange
        MyObject obj = null;
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        
        // Act
        SerializationUtils.serialize(obj, outputStream);
        
        // Assert
        // IllegalArgumentException is expected to be thrown
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testSerialize_NullOutputStream_ThrowsIllegalArgumentException() {
        // Arrange
        MyObject obj = new MyObject(TEST_STRING, TEST_INT, TEST_BYTE_ARRAY);
        OutputStream outputStream = null;
        
        // Act
        SerializationUtils.serialize(obj, outputStream);
        
        // Assert
        // IllegalArgumentException is expected to be thrown
    }
    
    @Test
    public void testSerialize_ValidObjectAndOutputStream_SerializesObject() {
        // Arrange
        MyObject obj = new MyObject(TEST_STRING, TEST_INT, TEST_BYTE_ARRAY);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        
        // Act
        SerializationUtils.serialize(obj, outputStream);
        byte[] serializedData = outputStream.toByteArray();
        
        // Assert
        assertNotNull(serializedData);
        assertTrue(serializedData.length > 0);
    }
    
    // Test cases for serialize(Serializable obj) method
    
    @Test(expected = IllegalArgumentException.class)
    public void testSerialize_NullObject_ThrowsIllegalArgumentException() {
        // Arrange
        MyObject obj = null;
        
        // Act
        SerializationUtils.serialize(obj);
        
        // Assert
        // IllegalArgumentException is expected to be thrown
    }
    
    @Test
    public void testSerialize_ValidObject_SerializesObject() {
        // Arrange
        MyObject obj = new MyObject(TEST_STRING, TEST_INT, TEST_BYTE_ARRAY);
        
        // Act
        byte[] serializedData = SerializationUtils.serialize(obj);
        
        // Assert
        assertNotNull(serializedData);
        assertTrue(serializedData.length > 0);
    }
    
    // Test cases for deserialize(InputStream inputStream) method
    
    @Test(expected = IllegalArgumentException.class)
    public void testDeserialize_NullInputStream_ThrowsIllegalArgumentException() {
        // Arrange
        InputStream inputStream = null;
        
        // Act
        SerializationUtils.deserialize(inputStream);
        
        // Assert
        // IllegalArgumentException is expected to be thrown
    }
    
    @Test(expected = SerializationException.class)
    public void testDeserialize_InvalidInputStream_ThrowsSerializationException() {
        // Arrange
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        
        // Act
        SerializationUtils.deserialize(inputStream);
        
        // Assert
        // SerializationException is expected to be thrown
    }
    
    @Test
    public void testDeserialize_ValidInputStream_DeserializesObject() {
        // Arrange
        MyObject obj = new MyObject(TEST_STRING, TEST_INT, TEST_BYTE_ARRAY);
        byte[] serializedData = SerializationUtils.serialize(obj);
        InputStream inputStream = new ByteArrayInputStream(serializedData);
        
        // Act
        Object deserializedObj = SerializationUtils.deserialize(inputStream);
        
        // Assert
        assertTrue(deserializedObj instanceof MyObject);
        MyObject deserializedMyObject = (MyObject) deserializedObj;
        assertEquals(obj.getString(), deserializedMyObject.getString());
        assertEquals(obj.getInt(), deserializedMyObject.getInt());
        assertArrayEquals(obj.getByteArray(), deserializedMyObject.getByteArray());
    }
    
    // Test cases for deserialize(byte[] objectData) method
    
    @Test(expected = IllegalArgumentException.class)
    public void testDeserialize_NullObjectData_ThrowsIllegalArgumentException() {
        // Arrange
        byte[] objectData = null;
        
        // Act
        SerializationUtils.deserialize(objectData);
        
        // Assert
        // IllegalArgumentException is expected to be thrown
    }
    
    @Test(expected = SerializationException.class)
    public void testDeserialize_InvalidObjectData_ThrowsSerializationException() {
        // Arrange
        byte[] objectData = new byte[0];
        
        // Act
        SerializationUtils.deserialize(objectData);
        
        // Assert
        // SerializationException is expected to be thrown
    }
    
    @Test
    public void testDeserialize_ValidObjectData_DeserializesObject() {
        // Arrange
        MyObject obj = new MyObject(TEST_STRING, TEST_INT, TEST_BYTE_ARRAY);
        byte[] serializedData = SerializationUtils.serialize(obj);
        
        // Act
        Object deserializedObj = SerializationUtils.deserialize(serializedData);
        
        // Assert
        assertTrue(deserializedObj instanceof MyObject);
        MyObject deserializedMyObject = (MyObject) deserializedObj;
        assertEquals(obj.getString(), deserializedMyObject.getString());
        assertEquals(obj.getInt(), deserializedMyObject.getInt());
        assertArrayEquals(obj.getByteArray(), deserializedMyObject.getByteArray());
    }
    
    private static class MyObject implements Serializable {
        private String string;
        private int integer;
        private byte[] byteArray;
        
        public MyObject(String string, int integer, byte[] byteArray) {
            this.string = string;
            this.integer = integer;
            this.byteArray = byteArray;
        }
        
        public String getString() {
            return string;
        }
        
        public int getInt() {
            return integer;
        }
        
        public byte[] getByteArray() {
            return byteArray;
        }
    }

}