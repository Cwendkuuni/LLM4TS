
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.junit.Assert;
import org.junit.Test;

public class JdkDeserializersTest {

    @Test
    public void testFind() {
        Class<?> rawType = UUID.class;
        String clsName = "java.util.UUID";
        JsonDeserializer<?> result = JdkDeserializers.find(rawType, clsName);
        Assert.assertNotNull(result);
    }

    @Test
    public void testFindInvalidClass() {
        Class<?> rawType = Integer.class;
        String clsName = "java.lang.Integer";
        JsonDeserializer<?> result = JdkDeserializers.find(rawType, clsName);
        Assert.assertNull(result);
    }

    @Test
    public void testFindUUID() {
        Class<?> rawType = UUID.class;
        String clsName = "java.util.UUID";
        JsonDeserializer<?> result = JdkDeserializers.find(rawType, clsName);
        Assert.assertEquals(UUIDDeserializer.class, result.getClass());
    }

    @Test
    public void testFindStackTraceElement() {
        Class<?> rawType = StackTraceElement.class;
        String clsName = "java.lang.StackTraceElement";
        JsonDeserializer<?> result = JdkDeserializers.find(rawType, clsName);
        Assert.assertEquals(StackTraceElementDeserializer.class, result.getClass());
    }

    @Test
    public void testFindAtomicBoolean() {
        Class<?> rawType = AtomicBoolean.class;
        String clsName = "java.util.concurrent.atomic.AtomicBoolean";
        JsonDeserializer<?> result = JdkDeserializers.find(rawType, clsName);
        Assert.assertEquals(AtomicBooleanDeserializer.class, result.getClass());
    }

    @Test
    public void testFindByteBuffer() {
        Class<?> rawType = ByteBuffer.class;
        String clsName = "java.nio.ByteBuffer";
        JsonDeserializer<?> result = JdkDeserializers.find(rawType, clsName);
        Assert.assertEquals(ByteBufferDeserializer.class, result.getClass());
    }
}
