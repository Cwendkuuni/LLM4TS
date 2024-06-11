import static org.junit.Assert.*;
import org.junit.Test;

public class JdkDeserializersTest {

    @Test
    public void testFind() {
        // Typical use case
        Class<?> rawType = UUID.class;
        String clsName = UUID.class.getName();
        JsonDeserializer<?> result = JdkDeserializers.find(rawType, clsName);
        assertNotNull(result);

        // Edge case: rawType is not in _classNames
        rawType = Integer.class;
        clsName = Integer.class.getName();
        result = JdkDeserializers.find(rawType, clsName);
        assertNull(result);

        // Edge case: rawType is UUID but clsName is not matching
        rawType = UUID.class;
        clsName = Integer.class.getName();
        result = JdkDeserializers.find(rawType, clsName);
        assertNull(result);

        // Error scenario: clsName is null
        rawType = AtomicBoolean.class;
        clsName = null;
        result = JdkDeserializers.find(rawType, clsName);
        assertNull(result);
    }
}