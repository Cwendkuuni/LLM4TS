import com.fasterxml.jackson.databind.JsonDeserializer;
import org.junit.Test;
import java.nio.ByteBuffer;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;
import static org.junit.Assert.*;

public class JdkDeserializersTest {

    @Test
    public void testFind_WithUUIDClass_ReturnsUUIDDeserializer() {
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
    public void testFind_WithAtomicBooleanClass_ReturnsAtomicBooleanDeserializer() {
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
    public void testFind_WithStackTraceElementClass_ReturnsStackTraceElementDeserializer() {
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
    public void testFind_WithByteBufferClass_ReturnsByteBufferDeserializer() {
        // Arrange
        Class<?> rawType = ByteBuffer.class;
        String clsName = ByteBuffer.class.getName();

        // Act
        JsonDeserializer<?> deserializer = JdkDeserializers.find(rawType, clsName);

        // Assert
        assertNotNull(deserializer);
        assertTrue(deserializer instanceof ByteBufferDeserializer);
    }

    @Test
    public void testFind_WithInvalidClass_ReturnsNull() {
        // Arrange
        Class<?> rawType = ArrayList.class;
        String clsName = ArrayList.class.getName();

        // Act
        JsonDeserializer<?> deserializer = JdkDeserializers.find(rawType, clsName);

        // Assert
        assertNull(deserializer);
    }

    @Test
    public void testFind_WithNullClsName_ReturnsNull() {
        // Arrange
        Class<?> rawType = UUID.class;
        String clsName = null;

        // Act
        JsonDeserializer<?> deserializer = JdkDeserializers.find(rawType, clsName);

        // Assert
        assertNull(deserializer);
    }
}