import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.deser.std.JdkDeserializers;

public class JdkDeserializersTest {

    @Test
    public void testFind_UUID_ValidClass_ReturnsDeserializer() {
        Class<?> rawType = UUID.class;
        String clsName = UUID.class.getName();

        JsonDeserializer<?> deserializer = JdkDeserializers.find(rawType, clsName);

        assertNotNull(deserializer);
        assertTrue(deserializer instanceof UUIDDeserializer);
    }

    @Test
    public void testFind_StackTraceElement_ValidClass_ReturnsDeserializer() {
        Class<?> rawType = StackTraceElement.class;
        String clsName = StackTraceElement.class.getName();

        JsonDeserializer<?> deserializer = JdkDeserializers.find(rawType, clsName);

        assertNotNull(deserializer);
        assertTrue(deserializer instanceof StackTraceElementDeserializer);
    }

    @Test
    public void testFind_AtomicBoolean_ValidClass_ReturnsDeserializer() {
        Class<?> rawType = AtomicBoolean.class;
        String clsName = AtomicBoolean.class.getName();

        JsonDeserializer<?> deserializer = JdkDeserializers.find(rawType, clsName);

        assertNotNull(deserializer);
        assertTrue(deserializer instanceof AtomicBooleanDeserializer);
    }

    @Test
    public void testFind_ByteBuffer_ValidClass_ReturnsDeserializer() {
        Class<?> rawType = ByteBuffer.class;
        String clsName = ByteBuffer.class.getName();

        JsonDeserializer<?> deserializer = JdkDeserializers.find(rawType, clsName);

        assertNotNull(deserializer);
        assertTrue(deserializer instanceof ByteBufferDeserializer);
    }

    @Test
    public void testFind_InvalidClass_ReturnsNull() {
        Class<?> rawType = HashSet.class;
        String clsName = HashSet.class.getName();

        JsonDeserializer<?> deserializer = JdkDeserializers.find(rawType, clsName);

        assertNull(deserializer);
    }

    @Test
    public void testFind_FromStringDeserializer_ValidClass_ReturnsDeserializer() {
        Class<?> rawType = Integer.class;
        String clsName = Integer.class.getName();

        JsonDeserializer<?> deserializer = JdkDeserializers.find(rawType, clsName);

        assertNotNull(deserializer);
        assertTrue(deserializer instanceof FromStringDeserializer);
    }
}