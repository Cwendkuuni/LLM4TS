import com.fasterxml.jackson.databind.*;
import org.junit.Test;
import java.nio.ByteBuffer;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import static org.junit.Assert.*;

public class JdkDeserializersTest {
    @Test
    public void testFind() {
        JsonDeserializer<?> deserializer = JdkDeserializers.find(UUID.class, UUID.class.getName());
        assertNotNull(deserializer);
        assertTrue(deserializer instanceof UUIDDeserializer);

        deserializer = JdkDeserializers.find(AtomicBoolean.class, AtomicBoolean.class.getName());
        assertNotNull(deserializer);
        assertTrue(deserializer instanceof AtomicBooleanDeserializer);

        deserializer = JdkDeserializers.find(StackTraceElement.class, StackTraceElement.class.getName());
        assertNotNull(deserializer);
        assertTrue(deserializer instanceof StackTraceElementDeserializer);

        deserializer = JdkDeserializers.find(ByteBuffer.class, ByteBuffer.class.getName());
        assertNotNull(deserializer);
        assertTrue(deserializer instanceof ByteBufferDeserializer);

        deserializer = JdkDeserializers.find(Date.class, Date.class.getName());
        assertNull(deserializer);
    }
}