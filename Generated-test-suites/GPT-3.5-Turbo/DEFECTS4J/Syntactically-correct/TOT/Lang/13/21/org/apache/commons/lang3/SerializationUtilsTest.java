import org.junit.Test;
import static org.junit.Assert.*;

public class SerializationUtilsTest {

    @Test
    public void testClone_WithNullObject_ReturnsNull() {
        // Arrange
        Object object = null;

        // Act
        Object result = SerializationUtils.clone(object);

        // Assert
        assertNull(result);
    }

    @Test
    public void testClone_WithSerializableObject_ReturnsClonedObject() {
        // Arrange
        MyClass obj = new MyClass();
        obj.setId(1);
        obj.setName("Test");

        // Act
        MyClass result = SerializationUtils.clone(obj);

        // Assert
        assertNotSame(obj, result);
        assertEquals(obj.getId(), result.getId());
        assertEquals(obj.getName(), result.getName());
    }

    @Test(expected = SerializationException.class)
    public void testClone_WithNonSerializableObject_ThrowsSerializationException() {
        // Arrange
        NonSerializableClass obj = new NonSerializableClass();

        // Act
        SerializationUtils.clone(obj);

        // Assert: Exception is expected
    }

    @Test
    public void testSerialize_WithNullObject_ThrowsIllegalArgumentException() {
        // Arrange
        Object object = null;
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        try {
            // Act
            SerializationUtils.serialize(object, outputStream);

            // Assert: IllegalArgumentException is expected

        } catch (IllegalArgumentException ex) {
            // Assert
            assertEquals("The object to serialize must not be null", ex.getMessage());
        }
    }

    @Test
    public void testSerialize_WithSerializableObject_SerializesSuccessfully() {
        // Arrange
        MyClass obj = new MyClass();
        obj.setId(1);
        obj.setName("Test");
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        // Act
        SerializationUtils.serialize(obj, outputStream);
        byte[] serializedData = outputStream.toByteArray();

        // Assert
        assertNotNull(serializedData);
        assertTrue(serializedData.length > 0);
    }

    @Test
    public void testDeserialize_WithNullInputStream_ThrowsIllegalArgumentException() {
        // Arrange
        InputStream inputStream = null;

        try {
            // Act
            SerializationUtils.deserialize(inputStream);

            // Assert: IllegalArgumentException is expected

        } catch (IllegalArgumentException ex) {
            // Assert
            assertEquals("The input stream must not be null", ex.getMessage());
        }
    }

    @Test
    public void testDeserialize_WithValidInputStream_DeserializesSuccessfully() {
        // Arrange
        MyClass obj = new MyClass();
        obj.setId(1);
        obj.setName("Test");
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        SerializationUtils.serialize(obj, outputStream);
        byte[] serializedData = outputStream.toByteArray();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(serializedData);

        // Act
        Object result = SerializationUtils.deserialize(inputStream);

        // Assert
        assertNotNull(result);
        assertTrue(result instanceof MyClass);
        MyClass deserializedObj = (MyClass) result;
        assertEquals(obj.getId(), deserializedObj.getId());
        assertEquals(obj.getName(), deserializedObj.getName());
    }
}