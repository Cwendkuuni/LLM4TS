import org.junit.Test;
import static org.junit.Assert.*;

public class JdkDeserializersTest {

    @Test
    public void testFind_WithValidClassAndValidClsName_ReturnsDeserializer() {
        Class<?> rawType = UUID.class;
        String clsName = "java.util.UUID";
        JsonDeserializer<?> result = JdkDeserializers.find(rawType, clsName);
        assertNotNull(result);
    }

    @Test
    public void testFind_WithValidClassAndInvalidClsName_ReturnsNull() {
        Class<?> rawType = AtomicBoolean.class;
        String clsName = "com.example.InvalidClassName";
        JsonDeserializer<?> result = JdkDeserializers.find(rawType, clsName);
        assertNull(result);
    }

    @Test
    public void testFind_WithInvalidClassAndValidClsName_ReturnsNull() {
        Class<?> rawType = Error.class; // Invalid class
        String clsName = "java.lang.String";
        JsonDeserializer<?> result = JdkDeserializers.find(rawType, clsName);
        assertNull(result);
    }

    @Test
    public void testFind_WithInvalidClassAndInvalidClsName_ReturnsNull() {
        Class<?> rawType = Error.class; // Invalid class
        String clsName = "com.example.InvalidClassName";
        JsonDeserializer<?> result = JdkDeserializers.find(rawType, clsName);
        assertNull(result);
    }

    @Test
    public void testFind_WithValidClassAndNullClsName_ReturnsNull() {
        Class<?> rawType = AtomicBoolean.class;
        String clsName = null;
        JsonDeserializer<?> result = JdkDeserializers.find(rawType, clsName);
        assertNull(result);
    }

    @Test
    public void testFind_WithNullClassAndValidClsName_ReturnsNull() {
        Class<?> rawType = null;
        String clsName = "java.util.UUID";
        JsonDeserializer<?> result = JdkDeserializers.find(rawType, clsName);
        assertNull(result);
    }

    @Test
    public void testFind_WithNullClassAndNullClsName_ReturnsNull() {
        Class<?> rawType = null;
        String clsName = null;
        JsonDeserializer<?> result = JdkDeserializers.find(rawType, clsName);
        assertNull(result);
    }
}