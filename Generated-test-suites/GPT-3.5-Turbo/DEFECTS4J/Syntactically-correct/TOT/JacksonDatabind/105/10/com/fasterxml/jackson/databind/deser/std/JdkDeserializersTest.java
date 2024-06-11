import org.junit.Test;
import static org.junit.Assert.*;

public class JdkDeserializersTest {

    @Test
    public void testFind_withExistingClass_ReturnsDeserializer() {
        // Typical use case
        Class<?> rawType = UUID.class;
        String clsName = "java.util.UUID";

        JsonDeserializer<?> result = JdkDeserializers.find(rawType, clsName);

        assertNotNull(result);
        assertTrue(result instanceof UUIDDeserializer);
    }

    @Test
    public void testFind_withNonExistingClass_ReturnsNull() {
        // Typical use case
        Class<?> rawType = String.class;
        String clsName = "java.lang.String";

        JsonDeserializer<?> result = JdkDeserializers.find(rawType, clsName);

        assertNull(result);
    }

    @Test
    public void testFind_withInvalidRawType_ReturnsNull() {
        // Error scenario
        Class<?> rawType = null;
        String clsName = "java.util.UUID";

        JsonDeserializer<?> result = JdkDeserializers.find(rawType, clsName);

        assertNull(result);
    }

    @Test
    public void testFind_withInvalidClassName_ReturnsNull() {
        // Error scenario
        Class<?> rawType = UUID.class;
        String clsName = null;

        JsonDeserializer<?> result = JdkDeserializers.find(rawType, clsName);

        assertNull(result);
    }

    @Test
    public void testFind_withAtomicBoolean_ReturnsDeserializer() {
        // Typical use case
        Class<?> rawType = AtomicBoolean.class;
        String clsName = "java.util.concurrent.atomic.AtomicBoolean";

        JsonDeserializer<?> result = JdkDeserializers.find(rawType, clsName);

        assertNotNull(result);
        assertTrue(result instanceof AtomicBooleanDeserializer);
    }

    @Test
    public void testFind_withStackTraceElement_ReturnsDeserializer() {
        // Typical use case
        Class<?> rawType = StackTraceElement.class;
        String clsName = "java.lang.StackTraceElement";

        JsonDeserializer<?> result = JdkDeserializers.find(rawType, clsName);

        assertNotNull(result);
        assertTrue(result instanceof StackTraceElementDeserializer);
    }

    @Test
    public void testFind_withByteBuffer_ReturnsDeserializer() {
        // Typical use case
        Class<?> rawType = ByteBuffer.class;
        String clsName = "java.nio.ByteBuffer";

        JsonDeserializer<?> result = JdkDeserializers.find(rawType, clsName);

        assertNotNull(result);
        assertTrue(result instanceof ByteBufferDeserializer);
    }
}