import static org.junit.Assert.*;
import org.junit.*;

public class JdkDeserializersTest {

    // Test case for find method
    @Test
    public void testFind() {
        // Test for UUID class
        Class<?> rawType = UUID.class;
        String clsName = UUID.class.getName();
        JsonDeserializer<?> deserializer = JdkDeserializers.find(rawType, clsName);
        assertNotNull(deserializer);
        assertTrue(deserializer instanceof UUIDDeserializer);

        // Test for StackTraceElement class
        rawType = StackTraceElement.class;
        clsName = StackTraceElement.class.getName();
        deserializer = JdkDeserializers.find(rawType, clsName);
        assertNotNull(deserializer);
        assertTrue(deserializer instanceof StackTraceElementDeserializer);

        // Test for AtomicBoolean class
        rawType = AtomicBoolean.class;
        clsName = AtomicBoolean.class.getName();
        deserializer = JdkDeserializers.find(rawType, clsName);
        assertNotNull(deserializer);
        assertTrue(deserializer instanceof AtomicBooleanDeserializer);

        // Test for ByteBuffer class
        rawType = ByteBuffer.class;
        clsName = ByteBuffer.class.getName();
        deserializer = JdkDeserializers.find(rawType, clsName);
        assertNotNull(deserializer);
        assertTrue(deserializer instanceof ByteBufferDeserializer);

        // Test for unknown class
        rawType = Random.class;
        clsName = Random.class.getName();
        deserializer = JdkDeserializers.find(rawType, clsName);
        assertNull(deserializer);
    }
}