import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.deser.std.JdkDeserializers;
import org.junit.Assert;
import org.junit.Test;

import java.nio.ByteBuffer;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

public class JdkDeserializersTest {

    @Test
    public void testFind() {
        Class<?> rawType = UUID.class;
        String clsName = UUID.class.getName();

        JsonDeserializer<?> deserializer = JdkDeserializers.find(rawType, clsName);
        Assert.assertNotNull(deserializer);
        Assert.assertTrue(deserializer instanceof UUIDDeserializer);
    }

    @Test
    public void testFindNoDeserializer() {
        Class<?> rawType = String.class;
        String clsName = String.class.getName();

        JsonDeserializer<?> deserializer = JdkDeserializers.find(rawType, clsName);
        Assert.assertNull(deserializer);
    }


    @Test
    public void testFindAtomicBoolean() {
        Class<?> rawType = AtomicBoolean.class;
        String clsName = AtomicBoolean.class.getName();

        JsonDeserializer<?> deserializer = JdkDeserializers.find(rawType, clsName);
        Assert.assertNotNull(deserializer);
        Assert.assertTrue(deserializer instanceof AtomicBooleanDeserializer);
    }

    @Test
    public void testFindByteBuffer() {
        Class<?> rawType = ByteBuffer.class;
        String clsName = ByteBuffer.class.getName();

        JsonDeserializer<?> deserializer = JdkDeserializers.find(rawType, clsName);
        Assert.assertNotNull(deserializer);
        Assert.assertTrue(deserializer instanceof ByteBufferDeserializer);
    }

    @Test
    public void testFindStackTraceElement() {
        Class<?> rawType = StackTraceElement.class;
        String clsName = StackTraceElement.class.getName();

        JsonDeserializer<?> deserializer = JdkDeserializers.find(rawType, clsName);
        Assert.assertNotNull(deserializer);
        Assert.assertTrue(deserializer instanceof StackTraceElementDeserializer);
    }

}