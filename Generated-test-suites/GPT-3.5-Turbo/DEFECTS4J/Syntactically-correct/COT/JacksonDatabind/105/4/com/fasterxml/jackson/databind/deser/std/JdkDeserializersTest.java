
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import org.junit.Test;
import com.fasterxml.jackson.databind.JsonDeserializer;
import java.nio.ByteBuffer;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;
import com.fasterxml.jackson.databind.deser.std.JdkDeserializers;

public class JdkDeserializersTest {
    
    @Test
    public void testFindWithExistingClass() {
        Class<?> rawType = UUID.class;
        String clsName = rawType.getName();
        JsonDeserializer<?> deserializer = JdkDeserializers.find(rawType, clsName);
        assertEquals(new UUIDDeserializer().getClass(), deserializer.getClass());
    }
    
    @Test
    public void testFindWithNonExistingClass() {
        Class<?> rawType = Double.class;
        String clsName = rawType.getName();
        JsonDeserializer<?> deserializer = JdkDeserializers.find(rawType, clsName);
        assertNull(deserializer);
    }
    
    @Test
    public void testFindWithNullClass() {
        Class<?> rawType = null;
        String clsName = "com.example.NonExistingClass";
        JsonDeserializer<?> deserializer = JdkDeserializers.find(rawType, clsName);
        assertNull(deserializer);
    }
    
    @Test
    public void testFindWithNullClassName() {
        Class<?> rawType = UUID.class;
        String clsName = null;
        JsonDeserializer<?> deserializer = JdkDeserializers.find(rawType, clsName);
        assertNull(deserializer);
    }
}
