import static org.junit.Assert.*;
import org.junit.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.*;

public class JdkDeserializersTest {

    @Test
    public void testFind() {
        JsonDeserializer<?> deserializer;

        deserializer = JdkDeserializers.find(UUID.class, "java.util.UUID");
        assertTrue(deserializer instanceof UUIDDeserializer);

        deserializer = JdkDeserializers.find(AtomicBoolean.class, "java.util.concurrent.atomic.AtomicBoolean");
        assertTrue(deserializer instanceof AtomicBooleanDeserializer);

        deserializer = JdkDeserializers.find(StackTraceElement.class, "java.lang.StackTraceElement");
        assertTrue(deserializer instanceof StackTraceElementDeserializer);

        deserializer = JdkDeserializers.find(ByteBuffer.class, "java.nio.ByteBuffer");
        assertTrue(deserializer instanceof ByteBufferDeserializer);

        deserializer = JdkDeserializers.find(Integer.class, "java.lang.Integer");
        assertNull(deserializer);
    }
}