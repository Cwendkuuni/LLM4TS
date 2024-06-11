import org.junit.Test;
import static org.junit.Assert.*;

public class JdkDeserializersTest {

    @Test
    public void testFind_withValidRawTypeAndValidClsName_ReturnsDeserializer() {
        Class<?> rawType = UUID.class;
        String clsName = UUID.class.getName();

        JsonDeserializer<?> result = JdkDeserializers.find(rawType, clsName);

        assertNotNull(result);
        assertTrue(result instanceof UUIDDeserializer);
    }

    @Test
    public void testFind_withValidRawTypeAndInvalidClsName_ReturnsNull() {
        Class<?> rawType = UUID.class;
        String clsName = "InvalidClass";

        JsonDeserializer<?> result = JdkDeserializers.find(rawType, clsName);

        assertNull(result);
    }

    @Test
    public void testFind_withInvalidRawTypeAndValidClsName_ReturnsNull() {
        Class<?> rawType = Integer.class;
        String clsName = AtomicBoolean.class.getName();

        JsonDeserializer<?> result = JdkDeserializers.find(rawType, clsName);

        assertNull(result);
    }

    @Test
    public void testFind_withInvalidRawTypeAndInvalidClsName_ReturnsNull() {
        Class<?> rawType = Integer.class;
        String clsName = "InvalidClass";

        JsonDeserializer<?> result = JdkDeserializers.find(rawType, clsName);

        assertNull(result);
    }
}