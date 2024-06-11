
import org.junit.Test;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.junit.Assert;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;
import java.nio.ByteBuffer;
import java.util.HashSet;
import com.fasterxml.jackson.databind.deser.std.JdkDeserializers;
import com.fasterxml.jackson.databind.deser.std.UUIDDeserializer;
import com.fasterxml.jackson.databind.deser.std.StackTraceElementDeserializer;
import com.fasterxml.jackson.databind.deser.std.AtomicBooleanDeserializer;
import com.fasterxml.jackson.databind.deser.std.FromStringDeserializer;
import com.fasterxml.jackson.databind.deser.std.ByteBufferDeserializer;

public class JdkDeserializersTest {

    @Test
    public void testFind() {
        Class<?> rawType = UUID.class;
        String clsName = "java.util.UUID";
        JsonDeserializer<?> deserializer = JdkDeserializers.find(rawType, clsName);
        Assert.assertNotNull("Deserializer should not be null", deserializer);
        Assert.assertEquals("Deserializer should be an instance of UUIDDeserializer", UUIDDeserializer.class, deserializer.getClass());
    }

    @Test
    public void testFind_withAtomicBoolean() {
        Class<?> rawType = AtomicBoolean.class;
        String clsName = "java.util.concurrent.atomic.AtomicBoolean";
        JsonDeserializer<?> deserializer = JdkDeserializers.find(rawType, clsName);
        Assert.assertNotNull("Deserializer should not be null", deserializer);
        Assert.assertEquals("Deserializer should be an instance of AtomicBooleanDeserializer", AtomicBooleanDeserializer.class, deserializer.getClass());
    }

    @Test
    public void testFind_withStackTraceElement() {
        Class<?> rawType = StackTraceElement.class;
        String clsName = "java.lang.StackTraceElement";
        JsonDeserializer<?> deserializer = JdkDeserializers.find(rawType, clsName);
        Assert.assertNotNull("Deserializer should not be null", deserializer);
        Assert.assertEquals("Deserializer should be an instance of StackTraceElementDeserializer", StackTraceElementDeserializer.class, deserializer.getClass());
    }

    @Test
    public void testFind_withByteBuffer() {
        Class<?> rawType = ByteBuffer.class;
        String clsName = "java.nio.ByteBuffer";
        JsonDeserializer<?> deserializer = JdkDeserializers.find(rawType, clsName);
        Assert.assertNotNull("Deserializer should not be null", deserializer);
        Assert.assertEquals("Deserializer should be an instance of ByteBufferDeserializer", ByteBufferDeserializer.class, deserializer.getClass());
    }

    // Test for edge case: Trying to find a class that is not supported
    @Test
    public void testFind_withUnsupportedClass() {
        Class<?> rawType = HashSet.class;
        String clsName = "java.util.HashSet";
        JsonDeserializer<?> deserializer = JdkDeserializers.find(rawType, clsName);
        Assert.assertNull("Deserializer should be null for an unsupported class", deserializer);
    }
}
