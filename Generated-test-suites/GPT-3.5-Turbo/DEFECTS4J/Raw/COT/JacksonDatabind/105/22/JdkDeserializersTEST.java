
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.deser.std.JdkDeserializers;
import org.junit.Test;
import java.nio.ByteBuffer;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;
import static org.junit.Assert.*;

public class JdkDeserializersTest {

    @Test
    public void testFindWithValidClass() {
        Class<?> rawType = UUID.class;
        String clsName = rawType.getName();
        JsonDeserializer<?> result = JdkDeserializers.find(rawType, clsName);
        assertNotNull(result);
        assertTrue(result instanceof UUIDDeserializer);
    }

    @Test
    public void testFindWithInvalidClass() {
        Class<?> rawType = Integer.class;
        String clsName = rawType.getName();
        JsonDeserializer<?> result = JdkDeserializers.find(rawType, clsName);
        assertNull(result);
    }

    @Test
    public void testFindWithFromStringDeserializer() {
        Class<?> rawType = Integer.class;
        String clsName = rawType.getName();
        JsonDeserializer<?> result = JdkDeserializers.find(rawType, clsName);
        assertNotNull(result);
        assertTrue(result instanceof FromStringDeserializer);
    }

    @Test
    public void testFindWithUUIDClass() {
        Class<?> rawType = UUID.class;
        String clsName = rawType.getName();
        JsonDeserializer<?> result = JdkDeserializers.find(rawType, clsName);
        assertNotNull(result);
        assertTrue(result instanceof UUIDDeserializer);
    }

    @Test
    public void testFindWithStackTraceElementClass() {
        Class<?> rawType = StackTraceElement.class;
        String clsName = rawType.getName();
        JsonDeserializer<?> result = JdkDeserializers.find(rawType, clsName);
        assertNotNull(result);
        assertTrue(result instanceof StackTraceElementDeserializer);
    }

    @Test
    public void testFindWithAtomicBooleanClass() {
        Class<?> rawType = AtomicBoolean.class;
        String clsName = rawType.getName();
        JsonDeserializer<?> result = JdkDeserializers.find(rawType, clsName);
        assertNotNull(result);
        assertTrue(result instanceof AtomicBooleanDeserializer);
    }

    @Test
    public void testFindWithByteBufferClass() {
        Class<?> rawType = ByteBuffer.class;
        String clsName = rawType.getName();
        JsonDeserializer<?> result = JdkDeserializers.find(rawType, clsName);
        assertNotNull(result);
        assertTrue(result instanceof ByteBufferDeserializer);
    }
}