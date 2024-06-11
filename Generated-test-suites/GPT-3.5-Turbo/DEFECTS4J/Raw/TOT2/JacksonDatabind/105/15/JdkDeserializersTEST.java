import static org.junit.Assert.*;
import org.junit.Test;

public class JdkDeserializersTest {

    @Test
    public void testFind() {
        Class<?> rawType = UUID.class;
        String clsName = UUID.class.getName();
        JsonDeserializer<?> deserializer = JdkDeserializers.find(rawType, clsName);
        assertNotNull(deserializer);
        assertTrue(deserializer instanceof UUIDDeserializer);
    }

    @Test
    public void testFindUnknownClass() {
        Class<?> rawType = HashMap.class;
        String clsName = HashMap.class.getName();
        JsonDeserializer<?> deserializer = JdkDeserializers.find(rawType, clsName);
        assertNull(deserializer);
    }

    @Test
    public void testFindAtomicBoolean() {
        Class<?> rawType = AtomicBoolean.class;
        String clsName = AtomicBoolean.class.getName();
        JsonDeserializer<?> deserializer = JdkDeserializers.find(rawType, clsName);
        assertNotNull(deserializer);
        assertTrue(deserializer instanceof AtomicBooleanDeserializer);
    }

    @Test
    public void testFindByteBuffer() {
        Class<?> rawType = ByteBuffer.class;
        String clsName = ByteBuffer.class.getName();
        JsonDeserializer<?> deserializer = JdkDeserializers.find(rawType, clsName);
        assertNotNull(deserializer);
        assertTrue(deserializer instanceof ByteBufferDeserializer);
    }

    @Test
    public void testFindNullClass() {
        Class<?> rawType = null;
        String clsName = "some.class.Name";
        JsonDeserializer<?> deserializer = JdkDeserializers.find(rawType, clsName);
        assertNull(deserializer);
    }
}