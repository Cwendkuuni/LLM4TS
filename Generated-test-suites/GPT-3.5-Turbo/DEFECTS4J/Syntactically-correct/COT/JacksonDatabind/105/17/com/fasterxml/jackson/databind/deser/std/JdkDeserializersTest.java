import org.junit.*;
import static org.junit.Assert.*;

import com.fasterxml.jackson.databind.*;



public class JdkDeserializersTest {

    @Test
    public void testFind_returnsNullForUnknownClass() {
        assertNull(JdkDeserializers.find(String.class, "com.example.UnknownClass"));
    }

    @Test
    public void testFind_returnsFromStringDeserializerForKnownClass() {
        assertNotNull(JdkDeserializers.find(String.class, "java.lang.String"));
        assertNotNull(JdkDeserializers.find(Integer.class, "java.lang.Integer"));
    }

    @Test
    public void testFind_returnsUUIDDeserializerForUUIDClass() {
        assertTrue(JdkDeserializers.find(UUID.class, "java.util.UUID") instanceof UUIDDeserializer);
    }

    @Test
    public void testFind_returnsStackTraceElementDeserializerForStackTraceElementClass() {
        assertTrue(JdkDeserializers.find(StackTraceElement.class, "java.lang.StackTraceElement") 
                        instanceof StackTraceElementDeserializer);
    }

    @Test
    public void testFind_returnsAtomicBooleanDeserializerForAtomicBooleanClass() {
        assertTrue(JdkDeserializers.find(AtomicBoolean.class, "java.util.concurrent.atomic.AtomicBoolean") 
                        instanceof AtomicBooleanDeserializer);
    }

    @Test
    public void testFind_returnsByteBufferDeserializerForByteBufferClass() {
        assertTrue(JdkDeserializers.find(ByteBuffer.class, "java.nio.ByteBuffer") 
                        instanceof ByteBufferDeserializer);
    }
}

