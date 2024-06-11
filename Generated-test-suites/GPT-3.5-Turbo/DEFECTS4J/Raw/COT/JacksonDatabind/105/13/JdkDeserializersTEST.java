import static org.junit.Assert.*;
import org.junit.Test;
import java.nio.ByteBuffer;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import com.fasterxml.jackson.databind.*;

public class JdkDeserializersTest {

    @Test
    public void testFind_uuidDeserializer() {
        Class<?> rawType = UUID.class;
        String clsName = "java.util.UUID";
        JsonDeserializer<?> deserializer = JdkDeserializers.find(rawType, clsName);
        assertTrue(deserializer instanceof UUIDDeserializer);
    }

    @Test
    public void testFind_stackTraceElementDeserializer() {
        Class<?> rawType = StackTraceElement.class;
        String clsName = "java.lang.StackTraceElement";
        JsonDeserializer<?> deserializer = JdkDeserializers.find(rawType, clsName);
        assertTrue(deserializer instanceof StackTraceElementDeserializer);
    }

    @Test
    public void testFind_atomicBooleanDeserializer() {
        Class<?> rawType = AtomicBoolean.class;
        String clsName = "java.util.concurrent.atomic.AtomicBoolean";
        JsonDeserializer<?> deserializer = JdkDeserializers.find(rawType, clsName);
        assertTrue(deserializer instanceof AtomicBooleanDeserializer);
    }

    @Test
    public void testFind_byteBufferDeserializer() {
        Class<?> rawType = ByteBuffer.class;
        String clsName = "java.nio.ByteBuffer";
        JsonDeserializer<?> deserializer = JdkDeserializers.find(rawType, clsName);
        assertTrue(deserializer instanceof ByteBufferDeserializer);
    }

    @Test
    public void testFind_primitiveType() {
        Class<?> rawType = int.class;
        String clsName = "int";
        JsonDeserializer<?> deserializer = JdkDeserializers.find(rawType, clsName);
        assertNull(deserializer);
    }

    @Test
    public void testFind_invalidClassName() {
        Class<?> rawType = UUID.class;
        String clsName = "com.example.InvalidClassName";
        JsonDeserializer<?> deserializer = JdkDeserializers.find(rawType, clsName);
        assertNull(deserializer);
    }
}