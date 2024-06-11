import com.fasterxml.jackson.databind.deser.std.*;
import com.fasterxml.jackson.databind.*;
import org.junit.*;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class JdkDeserializersTest {

    @Test
    public void testFind() {
        JsonDeserializer<?> deserializer;

        deserializer = JdkDeserializers.find(UUID.class, "java.util.UUID");
        Assert.assertTrue(deserializer instanceof UUIDDeserializer);

        deserializer = JdkDeserializers.find(AtomicBoolean.class, "java.util.concurrent.atomic.AtomicBoolean");
        Assert.assertTrue(deserializer instanceof AtomicBooleanDeserializer);

        deserializer = JdkDeserializers.find(StackTraceElement.class, "java.lang.StackTraceElement");
        Assert.assertTrue(deserializer instanceof StackTraceElementDeserializer);

        deserializer = JdkDeserializers.find(ByteBuffer.class, "java.nio.ByteBuffer");
        Assert.assertTrue(deserializer instanceof ByteBufferDeserializer);
    }
}