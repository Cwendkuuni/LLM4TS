
import org.junit.*;
import static org.junit.Assert.*;

public class JdkDeserializersTest {

    @Test
    public void testFind_whenRawTypeIsUUIDAndClsNameIsInClassNames_returnsUUIDDeserializer() {
        JsonDeserializer<?> deserializer = JdkDeserializers.find(UUID.class, "java.util.UUID");
        assertTrue(deserializer instanceof UUIDDeserializer);
    }
    
    @Test
    public void testFind_whenRawTypeIsStackTraceElementAndClsNameIsInClassNames_returnsStackTraceElementDeserializer() {
        JsonDeserializer<?> deserializer = JdkDeserializers.find(StackTraceElement.class, "java.lang.StackTraceElement");
        assertTrue(deserializer instanceof StackTraceElementDeserializer);
    }
    
    @Test
    public void testFind_whenRawTypeIsAtomicBooleanAndClsNameIsInClassNames_returnsAtomicBooleanDeserializer() {
        JsonDeserializer<?> deserializer = JdkDeserializers.find(AtomicBoolean.class, "java.util.concurrent.atomic.AtomicBoolean");
        assertTrue(deserializer instanceof AtomicBooleanDeserializer);
    }
    
    @Test
    public void testFind_whenRawTypeIsByteBufferAndClsNameIsInClassNames_returnsByteBufferDeserializer() {
        JsonDeserializer<?> deserializer = JdkDeserializers.find(ByteBuffer.class, "java.nio.ByteBuffer");
        assertTrue(deserializer instanceof ByteBufferDeserializer);
    }
    
    @Test
    public void testFind_whenRawTypeIsNotInClassNames_returnsNull() {
        JsonDeserializer<?> deserializer = JdkDeserializers.find(ArrayList.class, "java.util.ArrayList");
        assertNull(deserializer);
    }
}

