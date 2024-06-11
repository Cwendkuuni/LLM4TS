
import static org.junit.Assert.*;
import org.junit.Test;
import com.fasterxml.jackson.databind.JsonDeserializer;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;
import java.nio.ByteBuffer;
import com.fasterxml.jackson.databind.deser.std.*;

public class JdkDeserializersTest {

    @Test
    public void testFind() {
        Class<?> rawType = UUID.class;
        String clsName = rawType.getName();
        JsonDeserializer<?> deserializer = JdkDeserializers.find(rawType, clsName);
        assertTrue(deserializer instanceof UUIDDeserializer);
    }
    
    @Test
    public void testFindAtomicBoolean() {
        Class<?> rawType = AtomicBoolean.class;
        String clsName = rawType.getName();
        JsonDeserializer<?> deserializer = JdkDeserializers.find(rawType, clsName);
        assertTrue(deserializer instanceof AtomicBooleanDeserializer);
    }
    
    @Test
    public void testFindStackTraceElement() {
        Class<?> rawType = StackTraceElement.class;
        String clsName = rawType.getName();
        JsonDeserializer<?> deserializer = JdkDeserializers.find(rawType, clsName);
        assertTrue(deserializer instanceof StackTraceElementDeserializer);
    }
    
    @Test
    public void testFindByteBuffer() {
        Class<?> rawType = ByteBuffer.class;
        String clsName = rawType.getName();
        JsonDeserializer<?> deserializer = JdkDeserializers.find(rawType, clsName);
        assertTrue(deserializer instanceof ByteBufferDeserializer);
    }
    
    @Test
    public void testFindUnknownClass() {
        Class<?> rawType = Object.class;
        String clsName = rawType.getName();
        JsonDeserializer<?> deserializer = JdkDeserializers.find(rawType, clsName);
        assertNull(deserializer);
    }
}
