import static org.junit.Assert.*;
import org.junit.Test;

public class JdkDeserializersTest {

    @Test
    public void testFind_withValidClassNameAndRawType_returnDeserializer() {
        Class<?> rawType = UUID.class;
        String className = "java.util.UUID";
        JsonDeserializer<?> result = JdkDeserializers.find(rawType, className);
        assertNotNull(result);
        assertTrue(result instanceof UUIDDeserializer);
    }

    @Test
    public void testFind_withInvalidClassName_returnNull() {
        Class<?> rawType = Integer.class;
        String className = "java.lang.Integer";
        JsonDeserializer<?> result = JdkDeserializers.find(rawType, className);
        assertNull(result);
    }

    @Test
    public void testFind_withValidClassNameButUnsupportedRawType_returnNull() {
        Class<?> rawType = HashMap.class;
        String className = "java.util.HashMap";
        JsonDeserializer<?> result = JdkDeserializers.find(rawType, className);
        assertNull(result);
    }

    @Test
    public void testFind_withNullClassName_returnNull() {
        Class<?> rawType = AtomicBoolean.class;
        JsonDeserializer<?> result = JdkDeserializers.find(rawType, null);
        assertNull(result);
    }

}