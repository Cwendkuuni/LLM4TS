import org.junit.*;
import static org.junit.Assert.*;

public class SerializationUtilsTest {
    // Test cases for clone() method
    @Test
    public void testClone_NullObject_ReturnsNull() {
        // Arrange
        Object obj = null;
        
        // Act
        Object result = SerializationUtils.clone(obj);
        
        // Assert
        assertNull(result);
    }
    
    @Test
    public void testClone_SerializableObject_ReturnsDeepClone() {
        // Arrange
        MySerializableObject obj = new MySerializableObject(10, "Test");
        
        // Act
        MySerializableObject result = SerializationUtils.clone(obj);
        
        // Assert
        assertNotSame(obj, result); // Object reference should be different
        assertEquals(obj.getValue(), result.getValue());
        assertEquals(obj.getName(), result.getName());
    }
    
    @Test
    public void testClone_ObjectNotSerializable_ThrowsException() {
        // Arrange
        NonSerializableObject obj = new NonSerializableObject();
        
        // Act & Assert
        assertThrows(SerializationException.class, () -> {
            SerializationUtils.clone(obj);
        });
    }
    
    // Test cases for serialize() methods
    @Test
    public void testSerialize_NullObject_ThrowsException() {
        // Arrange
        Object obj = null;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            SerializationUtils.serialize(obj, baos);
        });
    }
    
    @Test
    public void testSerialize_ValidObject_ReturnsByteArray() {
        // Arrange
        MySerializableObject obj = new MySerializableObject(10, "Test");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        
        // Act
        SerializationUtils.serialize(obj, baos);
        byte[] result = baos.toByteArray();
        
        // Assert
        assertNotNull(result);
        assertTrue(result.length > 0);
    }
    
    // Test cases for deserialize() methods
    @Test
    public void testDeserialize_NullInputStream_ThrowsException() {
        // Arrange
        InputStream inputStream = null;
        
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            SerializationUtils.deserialize(inputStream);
        });
    }
    
    @Test
    public void testDeserialize_ValidInputStream_ReturnsDeserializedObject() {
        // Arrange
        MySerializableObject obj = new MySerializableObject(10, "Test");
        byte[] serializedData = SerializationUtils.serialize(obj);
        ByteArrayInputStream bais = new ByteArrayInputStream(serializedData);
        
        // Act
        Object result = SerializationUtils.deserialize(bais);
        
        // Assert
        assertNotNull(result);
        assertTrue(result instanceof MySerializableObject);
        assertEquals(obj.getValue(), ((MySerializableObject) result).getValue());
        assertEquals(obj.getName(), ((MySerializableObject) result).getName());
    }
    
    // Additional test cases
    @Test
    public void testCustomClassLoaderAwareObjectInputStream() throws Exception {
        // Arrange
        ClassLoaderAwareObjectInputStream in = new ClassLoaderAwareObjectInputStream(null, this.getClass().getClassLoader());
        ObjectStreamClass desc = ObjectStreamClass.lookup(MySerializableObject.class);
        
        // Act
        Class<?> result = in.resolveClass(desc);
        
        // Assert
        assertNotNull(result);
        assertEquals(MySerializableObject.class, result);
    }
}

class MySerializableObject implements Serializable {
    private int value;
    private String name;
    
    public MySerializableObject(int value, String name) {
        this.value = value;
        this.name = name;
    }
    
    public int getValue() {
        return value;
    }
    
    public String getName() {
        return name;
    }
}

class NonSerializableObject {
    // Non-serializable object
}