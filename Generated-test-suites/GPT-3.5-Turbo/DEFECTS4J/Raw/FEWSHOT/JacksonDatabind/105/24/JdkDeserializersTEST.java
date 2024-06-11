import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import org.junit.Test;

public class JdkDeserializersTest {
    @Test
    public void testFind() {
        assertNull(JdkDeserializers.find(Object.class, "java.lang.String"));
        assertEquals(new UUIDDeserializer().getClass(), JdkDeserializers.find(UUID.class, "java.util.UUID").getClass());
        assertEquals(new StackTraceElementDeserializer().getClass(), JdkDeserializers.find(StackTraceElement.class, "java.lang.StackTraceElement").getClass());
        assertEquals(new AtomicBooleanDeserializer().getClass(), JdkDeserializers.find(AtomicBoolean.class, "java.util.concurrent.atomic.AtomicBoolean").getClass());
        assertEquals(new ByteBufferDeserializer().getClass(), JdkDeserializers.find(ByteBuffer.class, "java.nio.ByteBuffer").getClass());
    }
}