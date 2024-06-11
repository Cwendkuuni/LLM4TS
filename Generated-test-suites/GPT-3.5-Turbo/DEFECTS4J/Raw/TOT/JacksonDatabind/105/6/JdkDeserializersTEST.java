import static org.junit.Assert.*;
import org.junit.*;

import com.fasterxml.jackson.databind.*;

public class JdkDeserializersTest {

    @Test
    public void testFind_withValidClass_shouldReturnCorrectDeserializer() {
        // Arrange
        Class<?> rawType = UUID.class;
        String className = rawType.getName();

        // Act
        JsonDeserializer<?> deserializer = JdkDeserializers.find(rawType, className);

        // Assert
        assertNotNull(deserializer);
        assertTrue(deserializer instanceof UUIDDeserializer);
    }

    @Test
    public void testFind_withInvalidClass_shouldReturnNull() {
        // Arrange
        Class<?> rawType = String.class;
        String className = rawType.getName();

        // Act
        JsonDeserializer<?> deserializer = JdkDeserializers.find(rawType, className);

        // Assert
        assertNull(deserializer);
    }

    @Test
    public void testFind_withAtomicBooleanClass_shouldReturnAtomicBooleanDeserializer() {
        // Arrange
        Class<?> rawType = AtomicBoolean.class;
        String className = rawType.getName();

        // Act
        JsonDeserializer<?> deserializer = JdkDeserializers.find(rawType, className);

        // Assert
        assertNotNull(deserializer);
        assertTrue(deserializer instanceof AtomicBooleanDeserializer);
        assertEquals(AtomicBoolean.class, deserializer.handledType());
    }

    @Test
    public void testFind_withStackTraceElementClass_shouldReturnStackTraceElementDeserializer() {
        // Arrange
        Class<?> rawType = StackTraceElement.class;
        String className = rawType.getName();

        // Act
        JsonDeserializer<?> deserializer = JdkDeserializers.find(rawType, className);

        // Assert
        assertNotNull(deserializer);
        assertTrue(deserializer instanceof StackTraceElementDeserializer);
        assertEquals(StackTraceElement.class, deserializer.handledType());
    }

    @Test
    public void testFind_withByteBufferClass_shouldReturnByteBufferDeserializer() {
        // Arrange
        Class<?> rawType = ByteBuffer.class;
        String className = rawType.getName();

        // Act
        JsonDeserializer<?> deserializer = JdkDeserializers.find(rawType, className);

        // Assert
        assertNotNull(deserializer);
        assertTrue(deserializer instanceof ByteBufferDeserializer);
        assertEquals(ByteBuffer.class, deserializer.handledType());
    }
}