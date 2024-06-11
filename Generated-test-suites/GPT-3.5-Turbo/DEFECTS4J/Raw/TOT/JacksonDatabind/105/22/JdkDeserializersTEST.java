
import org.junit.Test;
import static org.junit.Assert.*;

public class JdkDeserializersTest {

    @Test
    public void testFind() {
        // Testing typical use cases

        // Test UUID
        JsonDeserializer<?> deserializer1 = JdkDeserializers.find(UUID.class, "java.util.UUID");
        assertNotNull(deserializer1);
        assertTrue(deserializer1 instanceof FromStringDeserializer);
        // Add more test cases for UUID if necessary

        // Test AtomicBoolean
        JsonDeserializer<?> deserializer2 = JdkDeserializers.find(AtomicBoolean.class, "java.util.concurrent.atomic.AtomicBoolean");
        assertNotNull(deserializer2);
        assertTrue(deserializer2 instanceof AtomicBooleanDeserializer);
        // Add more test cases for AtomicBoolean if necessary

        // Test StackTraceElement
        JsonDeserializer<?> deserializer3 = JdkDeserializers.find(StackTraceElement.class, "java.lang.StackTraceElement");
        assertNotNull(deserializer3);
        assertTrue(deserializer3 instanceof StackTraceElementDeserializer);
        // Add more test cases for StackTraceElement if necessary

        // Test ByteBuffer
        JsonDeserializer<?> deserializer4 = JdkDeserializers.find(ByteBuffer.class, "java.nio.ByteBuffer");
        assertNotNull(deserializer4);
        assertTrue(deserializer4 instanceof ByteBufferDeserializer);
        // Add more test cases for ByteBuffer if necessary

        // Testing edge cases

        // Test unknown class name
        JsonDeserializer<?> deserializer5 = JdkDeserializers.find(UUID.class, "com.example.Unknown");
        assertNull(deserializer5);

        // Test rawType not in _classNames set
        JsonDeserializer<?> deserializer6 = JdkDeserializers.find(ArrayList.class, "java.util.ArrayList");
        assertNull(deserializer6);

        // Testing error scenarios

        // Test null rawType
        JsonDeserializer<?> deserializer7 = JdkDeserializers.find(null, "java.util.UUID");
        assertNull(deserializer7);

        // Test null clsName
        JsonDeserializer<?> deserializer8 = JdkDeserializers.find(UUID.class, null);
        assertNull(deserializer8);
    }
}

