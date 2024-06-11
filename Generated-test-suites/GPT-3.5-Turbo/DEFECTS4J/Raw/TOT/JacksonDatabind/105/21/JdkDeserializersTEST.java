import org.junit.*;
import static org.junit.Assert.*;

public class JdkDeserializersTest {

    @Test
    public void testFind_classNameNotFound() {
        Class<?> rawType = Integer.class;
        String clsName = "com.example.NonExistentClass";

        JsonDeserializer<?> result = JdkDeserializers.find(rawType, clsName);

        assertNull(result);
    }

    @Test
    public void testFind_UUID() {
        Class<?> rawType = UUID.class;
        String clsName = UUID.class.getName();

        JsonDeserializer<?> result = JdkDeserializers.find(rawType, clsName);

        assertNotNull(result);
        assertTrue(result instanceof UUIDDeserializer);
    }

    @Test
    public void testFind_StackTraceElement() {
        Class<?> rawType = StackTraceElement.class;
        String clsName = StackTraceElement.class.getName();

        JsonDeserializer<?> result = JdkDeserializers.find(rawType, clsName);

        assertNotNull(result);
        assertTrue(result instanceof StackTraceElementDeserializer);
    }

    @Test
    public void testFind_AtomicBoolean() {
        Class<?> rawType = AtomicBoolean.class;
        String clsName = AtomicBoolean.class.getName();

        JsonDeserializer<?> result = JdkDeserializers.find(rawType, clsName);

        assertNotNull(result);
        assertTrue(result instanceof AtomicBooleanDeserializer);
    }

    @Test
    public void testFind_ByteBuffer() {
        Class<?> rawType = ByteBuffer.class;
        String clsName = ByteBuffer.class.getName();

        JsonDeserializer<?> result = JdkDeserializers.find(rawType, clsName);

        assertNotNull(result);
        assertTrue(result instanceof ByteBufferDeserializer);
    }

    @Test
    public void testFind_InvalidDeserializer() {
        Class<?> rawType = Integer.class;
        String clsName = UUID.class.getName(); // Using UUID class name as invalid deserializer

        JsonDeserializer<?> result = JdkDeserializers.find(rawType, clsName);

        assertNull(result);
    }

    @Test
    public void testFind_PrimitiveType() {
        Class<?> rawType = int.class;
        String clsName = int.class.getName();

        JsonDeserializer<?> result = JdkDeserializers.find(rawType, clsName);

        assertNull(result);
    }
}