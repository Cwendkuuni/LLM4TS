import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.io.ByteArrayOutputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class SerializationUtilsTest {

    private static final Map<String, Integer> TEST_MAP = new HashMap<>();
    
    @Before
    public void setUp() {
        TEST_MAP.put("key1", 1);
        TEST_MAP.put("key2", 2);
        TEST_MAP.put("key3", 3);
    }
    
    // Clone
    //-----------------------------------------------------------------------
    @Test
    public void testClone_withNullObject_returnsNull() {
        // Arrange
        Object obj = null;

        // Act
        Object result = SerializationUtils.clone(obj);

        // Assert
        assertNull(result);
    }
    
    @Test
    public void testClone_withSerializableObject_returnsClonedObject() {
        // Arrange
        Serializable obj = new HashMap<>(TEST_MAP);

        // Act
        Map<String, Integer> result = SerializationUtils.clone(obj);

        // Assert
        assertEquals(TEST_MAP, result);
        assertNotSame(TEST_MAP, result);
    }
    
    // Serialize
    //-----------------------------------------------------------------------
    @Test
    public void testSerialize_withNullObject_ThrowsIllegalArgumentException() {
        // Arrange
        Serializable obj = null;
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            SerializationUtils.serialize(obj, outputStream);
        });
    }
    
    @Test
    public void testSerialize_withSerializableObject_returnsSerializedByteArray() throws IOException {
        // Arrange
        Serializable obj = new HashMap<>(TEST_MAP);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        // Act
        SerializationUtils.serialize(obj, outputStream);
        byte[] serializedBytes = outputStream.toByteArray();
        
        // Assert
        assertNotNull(serializedBytes);
        assertTrue(serializedBytes.length > 0);
    }
    
    // Deserialize
    //-----------------------------------------------------------------------
    @Test
    public void testDeserialize_withNullInputStream_ThrowsIllegalArgumentException() {
        // Arrange
        ByteArrayInputStream inputStream = null;

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            SerializationUtils.deserialize(inputStream);
        });
    }
    
    @Test
    public void testDeserialize_withSerializedByteArray_returnsDeserializedObject() throws IOException {
        // Arrange
        Serializable obj = new HashMap<>(TEST_MAP);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        SerializationUtils.serialize(obj, outputStream);
        byte[] serializedBytes = outputStream.toByteArray();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(serializedBytes);

        // Act
        Object result = SerializationUtils.deserialize(inputStream);
        
        // Assert
        assertNotNull(result);
        assertTrue(result instanceof Serializable);
        assertTrue(result instanceof Map);
        Map<String, Integer> deserializedMap = (Map<String, Integer>) result;
        assertEquals(TEST_MAP, deserializedMap);
    }
    
}