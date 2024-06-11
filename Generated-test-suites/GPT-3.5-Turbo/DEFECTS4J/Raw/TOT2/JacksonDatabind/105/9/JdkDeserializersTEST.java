import static org.junit.Assert.*;
import org.junit.Test;
import com.fasterxml.jackson.databind.deser.std.JdkDeserializers;
import com.fasterxml.jackson.databind.JsonDeserializer;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;
import java.nio.ByteBuffer;
import java.lang.StackTraceElement;

public class JdkDeserializersTest {

    @Test
    public void testFind() {
        JsonDeserializer<?> deserializer;

        // Test for UUID class
        deserializer = JdkDeserializers.find(UUID.class, UUID.class.getName());
        assertNotNull(deserializer);

        // Test for AtomicBoolean class
        deserializer = JdkDeserializers.find(AtomicBoolean.class, AtomicBoolean.class.getName());
        assertNotNull(deserializer);

        // Test for StackTraceElement class
        deserializer = JdkDeserializers.find(StackTraceElement.class, StackTraceElement.class.getName());
        assertNotNull(deserializer);

        // Test for ByteBuffer class
        deserializer = JdkDeserializers.find(ByteBuffer.class, ByteBuffer.class.getName());
        assertNotNull(deserializer);

        // Test for unknown class
        deserializer = JdkDeserializers.find(String.class, "java.lang.String");
        assertNull(deserializer);
    }
}