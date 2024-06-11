import org.junit.Test;
import static org.junit.Assert.*;

public class JdkDeserializersTest {

    @Test
    public void testFind_CorrectRawTypeAndClassName_ReturnsDeserializer() {
        // Arrange
        Class<?> rawType = UUID.class;
        String clsName = "java.util.UUID";

        // Act
        JsonDeserializer<?> result = JdkDeserializers.find(rawType, clsName);

        // Assert
        assertTrue(result instanceof UUIDDeserializer);
    }

    @Test
    public void testFind_IncorrectRawTypeAndClassName_ReturnsNull() {
        // Arrange
        Class<?> rawType = UUID.class;
        String clsName = "com.example.InvalidClass";

        // Act
        JsonDeserializer<?> result = JdkDeserializers.find(rawType, clsName);

        // Assert
        assertNull(result);
    }

    @Test
    public void testFind_InvalidRawType_ReturnsNull() {
        // Arrange
        Class<?> rawType = Float.class;
        String clsName = "java.lang.Float";

        // Act
        JsonDeserializer<?> result = JdkDeserializers.find(rawType, clsName);

        // Assert
        assertNull(result);
    }

    @Test
    public void testFind_CorrectRawTypeButNoDeserializer_ReturnsNull() {
        // Arrange
        Class<?> rawType = String.class;
        String clsName = "java.lang.String";

        // Act
        JsonDeserializer<?> result = JdkDeserializers.find(rawType, clsName);

        // Assert
        assertNull(result);
    }

    @Test
    public void testFind_RawTypeIsAtomicBoolean_ReturnsDeserializer() {
        // Arrange
        Class<?> rawType = AtomicBoolean.class;
        String clsName = "java.util.concurrent.atomic.AtomicBoolean";

        // Act
        JsonDeserializer<?> result = JdkDeserializers.find(rawType, clsName);

        // Assert
        assertTrue(result instanceof AtomicBooleanDeserializer);
    }

    @Test
    public void testFind_RawTypeIsStackTraceElement_ReturnsDeserializer() {
        // Arrange
        Class<?> rawType = StackTraceElement.class;
        String clsName = "java.lang.StackTraceElement";

        // Act
        JsonDeserializer<?> result = JdkDeserializers.find(rawType, clsName);

        // Assert
        assertTrue(result instanceof StackTraceElementDeserializer);
    }

    @Test
    public void testFind_RawTypeIsByteBuffer_ReturnsDeserializer() {
        // Arrange
        Class<?> rawType = ByteBuffer.class;
        String clsName = "java.nio.ByteBuffer";

        // Act
        JsonDeserializer<?> result = JdkDeserializers.find(rawType, clsName);

        // Assert
        assertTrue(result instanceof ByteBufferDeserializer);
    }

    // Add additional test cases as needed

}