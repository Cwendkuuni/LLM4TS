import static org.junit.Assert.*;
import org.junit.Test;

public class JdkDeserializersTest {

    @Test
    public void testFind_UUID() {
        assertEquals(new UUIDDeserializer(), JdkDeserializers.find(UUID.class, UUID.class.getName()));
    }

    @Test
    public void testFind_StackTraceElement() {
        assertEquals(new StackTraceElementDeserializer(), JdkDeserializers.find(StackTraceElement.class, StackTraceElement.class.getName()));
    }

    @Test
    public void testFind_AtomicBoolean() {
        assertEquals(new AtomicBooleanDeserializer(), JdkDeserializers.find(AtomicBoolean.class, AtomicBoolean.class.getName()));
    }

    @Test
    public void testFind_ByteBuffer() {
        assertEquals(new ByteBufferDeserializer(), JdkDeserializers.find(ByteBuffer.class, ByteBuffer.class.getName()));
    }

    @Test
    public void testFind_NotFound() {
        assertNull(JdkDeserializers.find(String.class, String.class.getName()));
    }

    @Test
    public void testFind_NullClsName() {
        assertNull(JdkDeserializers.find(UUID.class, null));
    }

    @Test
    public void testFind_NullRawType() {
        assertNull(JdkDeserializers.find(null, UUID.class.getName()));
    }

}