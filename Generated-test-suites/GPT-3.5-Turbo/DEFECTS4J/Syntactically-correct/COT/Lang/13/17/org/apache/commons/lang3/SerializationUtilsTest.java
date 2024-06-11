
import org.junit.Test;
import static org.junit.Assert.*;

public class SerializationUtilsTest {

    // Clone tests
    @Test
    public void testClone_NullInput_ShouldReturnNull() {
        // Arrange
        Object obj = null;

        // Act
        Object result = SerializationUtils.clone(obj);

        // Assert
        assertNull(result);
    }

    @Test
    public void testClone_ValidInput_ShouldReturnClonedObject() {
        // Arrange
        Map<String, String> originalObject = new HashMap<>();
        originalObject.put("key1", "value1");
        originalObject.put("key2", "value2");

        // Act
        Map<String, String> clonedObject = SerializationUtils.clone(originalObject);

        // Assert
        assertNotSame(originalObject, clonedObject);
        assertEquals(originalObject, clonedObject);
    }

    // Serialize tests
    @Test
    public void testSerialize_NullObject_ShouldThrowIllegalArgumentException() {
        // Arrange
        Serializable obj = null;
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        // Act and Assert
        assertThrows(IllegalArgumentException.class, () -> SerializationUtils.serialize(obj, outputStream));
    }

    @Test
    public void testSerialize_ValidObject_ShouldSerializeObject() {
        // Arrange
        Map<String, String> objectToSerialize = new HashMap<>();
        objectToSerialize.put("key1", "value1");
        objectToSerialize.put("key2", "value2");
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        // Act
        SerializationUtils.serialize(objectToSerialize, outputStream);
        byte[] serializedData = outputStream.toByteArray();

        // Assert
        assertNotNull(serializedData);
        assertTrue(serializedData.length > 0);
    }

    // Deserialize tests
    @Test
    public void testDeserialize_NullInputStream_ShouldThrowIllegalArgumentException() {
        // Arrange
        InputStream inputStream = null;

        // Act and Assert
        assertThrows(IllegalArgumentException.class, () -> SerializationUtils.deserialize(inputStream));
    }

    @Test
    public void testDeserialize_ValidInputStream_ShouldDeserializeObject() {
        // Arrange
        Map<String, String> serializedObject = new HashMap<>();
        serializedObject.put("key1", "value1");
        serializedObject.put("key2", "value2");
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        SerializationUtils.serialize(serializedObject, outputStream);
        byte[] serializedData = outputStream.toByteArray();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(serializedData);

        // Act
        Object deserializedObject = SerializationUtils.deserialize(inputStream);

        // Assert
        assertNotNull(deserializedObject);
        assertTrue(deserializedObject instanceof Map);
        Map<String, String> deserializedMap = (Map<String, String>) deserializedObject;
        assertEquals(serializedObject, deserializedMap);
    }

    // ClassLoaderAwareObjectInputStream tests
    // TODO: Add tests for ClassLoaderAwareObjectInputStream

}

