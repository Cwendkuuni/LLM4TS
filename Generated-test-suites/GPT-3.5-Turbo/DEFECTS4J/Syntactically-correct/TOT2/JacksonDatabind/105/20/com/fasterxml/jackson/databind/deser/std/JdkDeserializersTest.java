import static org.junit.Assert.*;
import org.junit.Test;

public class JdkDeserializersTest {

    @Test
    public void testFind_UUID() {
        Class<?> rawType = UUID.class;
        String clsName = UUID.class.getName();
        JsonDeserializer<?> deserializer = JdkDeserializers.find(rawType, clsName);

        assertTrue(deserializer instanceof UUIDDeserializer);
    }

    @Test
    public void testFind_StackTraceElement() {
        Class<?> rawType = StackTraceElement.class;
        String clsName = StackTraceElement.class.getName();
        JsonDeserializer<?> deserializer = JdkDeserializers.find(rawType, clsName);

        assertTrue(deserializer instanceof StackTraceElementDeserializer);
    }

    @Test
    public void testFind_AtomicBoolean() {
        Class<?> rawType = AtomicBoolean.class;
        String clsName = AtomicBoolean.class.getName();
        JsonDeserializer<?> deserializer = JdkDeserializers.find(rawType, clsName);

        assertTrue(deserializer instanceof AtomicBooleanDeserializer);
    }

    @Test
    public void testFind_ByteBuffer() {
        Class<?> rawType = ByteBuffer.class;
        String clsName = ByteBuffer.class.getName();
        JsonDeserializer<?> deserializer = JdkDeserializers.find(rawType, clsName);

        assertTrue(deserializer instanceof ByteBufferDeserializer);
    }

    @Test
    public void testFind_Null() {
        Class<?> rawType = String.class;
        String clsName = String.class.getName();
        JsonDeserializer<?> deserializer = JdkDeserializers.find(rawType, clsName);

        assertNull(deserializer);
    }
}