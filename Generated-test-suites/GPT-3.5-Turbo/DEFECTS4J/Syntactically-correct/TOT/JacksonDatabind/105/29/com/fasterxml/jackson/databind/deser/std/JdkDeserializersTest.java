import static org.junit.Assert.*;
import org.junit.Test;

public class JdkDeserializersTest {

    @Test
    public void testFind_withValidRawTypeAndClsName_shouldReturnDeserializer() {
        // Arrange
        Class<?> rawType = UUID.class;
        String clsName = UUID.class.getName();

        // Act
        JsonDeserializer<?> deserializer = JdkDeserializers.find(rawType, clsName);

        // Assert
        assertNotNull(deserializer);
        assertTrue(deserializer instanceof UUIDDeserializer);
    }

    @Test
    public void testFind_withInvalidRawTypeAndClsName_shouldReturnNull() {
        // Arrange
        Class<?> rawType = Integer.class;
        String clsName = Integer.class.getName();

        // Act
        JsonDeserializer<?> deserializer = JdkDeserializers.find(rawType, clsName);

        // Assert
        assertNull(deserializer);
    }

    @Test
    public void testFind_withInvalidClsName_shouldReturnNull() {
        // Arrange
        Class<?> rawType = UUID.class;
        String clsName = "com.example.InvalidClass";

        // Act
        JsonDeserializer<?> deserializer = JdkDeserializers.find(rawType, clsName);

        // Assert
        assertNull(deserializer);
    }

    @Test
    public void testFind_withValidRawTypeButNoMatchingDeserializer_shouldReturnNull() {
        // Arrange
        Class<?> rawType = String.class;
        String clsName = String.class.getName();

        // Act
        JsonDeserializer<?> deserializer = JdkDeserializers.find(rawType, clsName);

        // Assert
        assertNull(deserializer);
    }

    @Test
    public void testFind_withValidRawTypeAndClsName_shouldReturnAtomicBooleanDeserializer() {
        // Arrange
        Class<?> rawType = AtomicBoolean.class;
        String clsName = AtomicBoolean.class.getName();

        // Act
        JsonDeserializer<?> deserializer = JdkDeserializers.find(rawType, clsName);

        // Assert
        assertNotNull(deserializer);
        assertTrue(deserializer instanceof AtomicBooleanDeserializer);
    }

    @Test
    public void testFind_withValidRawTypeAndClsName_shouldReturnStackTraceElementDeserializer() {
        // Arrange
        Class<?> rawType = StackTraceElement.class;
        String clsName = StackTraceElement.class.getName();

        // Act
        JsonDeserializer<?> deserializer = JdkDeserializers.find(rawType, clsName);

        // Assert
        assertNotNull(deserializer);
        assertTrue(deserializer instanceof StackTraceElementDeserializer);
    }

    @Test
    public void testFind_withValidRawTypeAndClsName_shouldReturnByteBufferDeserializer() {
        // Arrange
        Class<?> rawType = ByteBuffer.class;
        String clsName = ByteBuffer.class.getName();

        // Act
        JsonDeserializer<?> deserializer = JdkDeserializers.find(rawType, clsName);

        // Assert
        assertNotNull(deserializer);
        assertTrue(deserializer instanceof ByteBufferDeserializer);
    }
}