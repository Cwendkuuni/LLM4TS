import static org.junit.Assert.*;
import org.junit.Test;

public class JdkDeserializersTest {

    @Test
    public void testFind() {
        // Test typical use case with UUID
        Class<?> rawType = UUID.class;
        String className = UUID.class.getName();
        JsonDeserializer<?> deserializer = JdkDeserializers.find(rawType, className);
        assertTrue(deserializer instanceof UUIDDeserializer);

        // Test typical use case with AtomicBoolean
        rawType = AtomicBoolean.class;
        className = AtomicBoolean.class.getName();
        deserializer = JdkDeserializers.find(rawType, className);
        assertTrue(deserializer instanceof AtomicBooleanDeserializer);

        // Test typical use case with StackTraceElement
        rawType = StackTraceElement.class;
        className = StackTraceElement.class.getName();
        deserializer = JdkDeserializers.find(rawType, className);
        assertTrue(deserializer instanceof StackTraceElementDeserializer);

        // Test typical use case with ByteBuffer
        rawType = ByteBuffer.class;
        className = ByteBuffer.class.getName();
        deserializer = JdkDeserializers.find(rawType, className);
        assertTrue(deserializer instanceof ByteBufferDeserializer);

        // Test unique class name not in _classNames
        rawType = Object.class;
        className = Object.class.getName();
        deserializer = JdkDeserializers.find(rawType, className);
        assertNull(deserializer);

        // Test null class name
        className = null;
        deserializer = JdkDeserializers.find(rawType, className);
        assertNull(deserializer);

    }
}