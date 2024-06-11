
import org.junit.Test;
import static org.junit.Assert.*;

public class JdkDeserializersTest {

    @Test
    public void testFind() {
        Class<?> rawType = UUID.class;
        String clsName = "java.util.UUID";
        JsonDeserializer<?> deserializer = JdkDeserializers.find(rawType, clsName);
        assertTrue(deserializer instanceof UUIDDeserializer);
    }

    @Test
    public void testFind_withInvalidClass() {
        Class<?> rawType = ArrayList.class;
        String clsName = "java.util.ArrayList";
        JsonDeserializer<?> deserializer = JdkDeserializers.find(rawType, clsName);
        assertNull(deserializer);
    }

    @Test
    public void testFind_withFromStringDeserializerClasses() {
        Class<?> rawType = Integer.class;
        String clsName = "java.lang.Integer";
        JsonDeserializer<?> deserializer = JdkDeserializers.find(rawType, clsName);
        assertTrue(deserializer instanceof FromStringDeserializer);
    }

    @Test
    public void testFind_withStackTraceElementClass() {
        Class<?> rawType = StackTraceElement.class;
        String clsName = "java.lang.StackTraceElement";
        JsonDeserializer<?> deserializer = JdkDeserializers.find(rawType, clsName);
        assertTrue(deserializer instanceof StackTraceElementDeserializer);
    }

    @Test
    public void testFind_withAtomicBooleanClass() {
        Class<?> rawType = AtomicBoolean.class;
        String clsName = "java.util.concurrent.atomic.AtomicBoolean";
        JsonDeserializer<?> deserializer = JdkDeserializers.find(rawType, clsName);
        assertTrue(deserializer instanceof AtomicBooleanDeserializer);
    }

    @Test
    public void testFind_withByteBufferClass() {
        Class<?> rawType = ByteBuffer.class;
        String clsName = "java.nio.ByteBuffer";
        JsonDeserializer<?> deserializer = JdkDeserializers.find(rawType, clsName);
        assertTrue(deserializer instanceof ByteBufferDeserializer);
    }
  
}
