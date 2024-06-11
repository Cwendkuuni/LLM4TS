import static org.junit.Assert.*;
import org.junit.Test;
import com.fasterxml.jackson.databind.JsonDeserializer;

public class JdkDeserializersTest {

    @Test
    public void testFind_UUIDDeserializer() {
        Class<?> rawType = UUID.class;
        String className = UUID.class.getName();

        JsonDeserializer<?> result = JdkDeserializers.find(rawType, className);

        assertTrue(result instanceof UUIDDeserializer);
    }

    @Test
    public void testFind_StackTraceElementDeserializer() {
        Class<?> rawType = StackTraceElement.class;
        String className = StackTraceElement.class.getName();

        JsonDeserializer<?> result = JdkDeserializers.find(rawType, className);

        assertTrue(result instanceof StackTraceElementDeserializer);
    }

    @Test
    public void testFind_AtomicBooleanDeserializer() {
        Class<?> rawType = AtomicBoolean.class;
        String className = AtomicBoolean.class.getName();

        JsonDeserializer<?> result = JdkDeserializers.find(rawType, className);

        assertTrue(result instanceof AtomicBooleanDeserializer);
    }

    @Test
    public void testFind_ByteBufferDeserializer() {
        Class<?> rawType = ByteBuffer.class;
        String className = ByteBuffer.class.getName();

        JsonDeserializer<?> result = JdkDeserializers.find(rawType, className);

        assertTrue(result instanceof ByteBufferDeserializer);
    }

    @Test
    public void testFind_NonJdkClass() {
        Class<?> rawType = String.class;
        String className = String.class.getName();

        JsonDeserializer<?> result = JdkDeserializers.find(rawType, className);

        assertNull(result);
    }

}