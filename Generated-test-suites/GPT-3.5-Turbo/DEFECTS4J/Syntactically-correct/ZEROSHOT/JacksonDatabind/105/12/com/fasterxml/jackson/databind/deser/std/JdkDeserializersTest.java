import org.junit.Test;
import static org.junit.Assert.*;

public class JdkDeserializersTest {

    @Test
    public void testFindWithUuidClass() {
        Class<?> rawType = UUID.class;
        String clsName = UUID.class.getName();

        JsonDeserializer<?> deserializer = JdkDeserializers.find(rawType, clsName);

        assertTrue(deserializer instanceof UUIDDeserializer);
    }

    @Test
    public void testFindWithStackTraceElementClass() {
        Class<?> rawType = StackTraceElement.class;
        String clsName = StackTraceElement.class.getName();

        JsonDeserializer<?> deserializer = JdkDeserializers.find(rawType, clsName);

        assertTrue(deserializer instanceof StackTraceElementDeserializer);
    }

    @Test
    public void testFindWithAtomicBooleanClass() {
        Class<?> rawType = AtomicBoolean.class;
        String clsName = AtomicBoolean.class.getName();

        JsonDeserializer<?> deserializer = JdkDeserializers.find(rawType, clsName);

        assertTrue(deserializer instanceof AtomicBooleanDeserializer);
    }

    @Test
    public void testFindWithByteBufferClass() {
        Class<?> rawType = ByteBuffer.class;
        String clsName = ByteBuffer.class.getName();

        JsonDeserializer<?> deserializer = JdkDeserializers.find(rawType, clsName);

        assertTrue(deserializer instanceof ByteBufferDeserializer);
    }

    @Test
    public void testFindWithUnknownClass() {
        Class<?> rawType = String.class;
        String clsName = String.class.getName();

        JsonDeserializer<?> deserializer = JdkDeserializers.find(rawType, clsName);

        assertNull(deserializer);
    }
}