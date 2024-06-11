import static org.junit.Assert.*;
import org.junit.Test;

public class JdkDeserializersTest {

    @Test
    public void testFind_withValidClass() {
        Class<?> rawType = UUID.class;
        String clsName = UUID.class.getName();

        JsonDeserializer<?> actual = JdkDeserializers.find(rawType, clsName);

        assertNotNull(actual);
        assertTrue(actual instanceof UUIDDeserializer);
    }

    @Test
    public void testFind_withInvalidClass() {
        Class<?> rawType = ArrayList.class;
        String clsName = ArrayList.class.getName();

        JsonDeserializer<?> actual = JdkDeserializers.find(rawType, clsName);

        assertNull(actual);
    }

    @Test
    public void testFind_withNullClass() {
        Class<?> rawType = null;
        String clsName = UUID.class.getName();

        JsonDeserializer<?> actual = JdkDeserializers.find(rawType, clsName);

        assertNull(actual);
    }

    @Test
    public void testFind_withNullClassName() {
        Class<?> rawType = UUID.class;
        String clsName = null;

        JsonDeserializer<?> actual = JdkDeserializers.find(rawType, clsName);

        assertNull(actual);
    }
}