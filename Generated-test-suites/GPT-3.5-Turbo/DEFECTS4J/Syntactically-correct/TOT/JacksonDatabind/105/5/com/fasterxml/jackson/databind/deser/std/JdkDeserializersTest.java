import org.junit.Test;
import static org.junit.Assert.*;

public class JdkDeserializersTest {

    @Test
    public void testFind_whenClassIsUUID_thenReturnUUIDDeserializer() {
        Class<?> rawType = UUID.class;
        String clsName = UUID.class.getName();

        JsonDeserializer<?> result = JdkDeserializers.find(rawType, clsName);

        assertNotNull(result);
        assertTrue(result instanceof UUIDDeserializer);
    }

    @Test
    public void testFind_whenClassIsStackTraceElement_thenReturnStackTraceElementDeserializer() {
        Class<?> rawType = StackTraceElement.class;
        String clsName = StackTraceElement.class.getName();

        JsonDeserializer<?> result = JdkDeserializers.find(rawType, clsName);

        assertNotNull(result);
        assertTrue(result instanceof StackTraceElementDeserializer);
    }

    @Test
    public void testFind_whenClassIsAtomicBoolean_thenReturnAtomicBooleanDeserializer() {
        Class<?> rawType = AtomicBoolean.class;
        String clsName = AtomicBoolean.class.getName();

        JsonDeserializer<?> result = JdkDeserializers.find(rawType, clsName);

        assertNotNull(result);
        assertTrue(result instanceof AtomicBooleanDeserializer);
    }

    @Test
    public void testFind_whenClassIsByteBuffer_thenReturnByteBufferDeserializer() {
        Class<?> rawType = ByteBuffer.class;
        String clsName = ByteBuffer.class.getName();

        JsonDeserializer<?> result = JdkDeserializers.find(rawType, clsName);

        assertNotNull(result);
        assertTrue(result instanceof ByteBufferDeserializer);
    }

    @Test
    public void testFind_whenClassIsUnknown_thenReturnNull() {
        Class<?> rawType = UnknownClass.class;
        String clsName = UnknownClass.class.getName();

        JsonDeserializer<?> result = JdkDeserializers.find(rawType, clsName);

        assertNull(result);
    }

    @Test
    public void testFind_whenClassIsNull_thenReturnNull() {
        Class<?> rawType = null;
        String clsName = "com.example.UnknownClass";

        JsonDeserializer<?> result = JdkDeserializers.find(rawType, clsName);

        assertNull(result);
    }
}