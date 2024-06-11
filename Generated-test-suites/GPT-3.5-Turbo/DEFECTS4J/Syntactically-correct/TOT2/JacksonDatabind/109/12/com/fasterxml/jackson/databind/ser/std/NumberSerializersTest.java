import org.junit.Test;
import static org.junit.Assert.*;

public class NumberSerializersTest {

    @Test
    public void testAddAll() {
        Map<String, JsonSerializer<?>> allDeserializers = new HashMap<>();
        NumberSerializers.addAll(allDeserializers);

        assertNotNull(allDeserializers.get(Integer.class.getName()));
        assertNotNull(allDeserializers.get(Integer.TYPE.getName()));
        assertNotNull(allDeserializers.get(Long.class.getName()));
        assertNotNull(allDeserializers.get(Long.TYPE.getName()));
        assertNotNull(allDeserializers.get(Byte.class.getName()));
        assertNotNull(allDeserializers.get(Byte.TYPE.getName()));
        assertNotNull(allDeserializers.get(Short.class.getName()));
        assertNotNull(allDeserializers.get(Short.TYPE.getName()));
        assertNotNull(allDeserializers.get(Double.class.getName()));
        assertNotNull(allDeserializers.get(Double.TYPE.getName()));
        assertNotNull(allDeserializers.get(Float.class.getName()));
        assertNotNull(allDeserializers.get(Float.TYPE.getName()));
    }

    @Test
    public void testShortSerializer() {
        // Test ShortSerializer functionality
    }

    @Test
    public void testIntegerSerializer() {
        // Test IntegerSerializer functionality
    }

    @Test
    public void testIntLikeSerializer() {
        // Test IntLikeSerializer functionality
    }

    @Test
    public void testLongSerializer() {
        // Test LongSerializer functionality
    }

    @Test
    public void testFloatSerializer() {
        // Test FloatSerializer functionality
    }

    @Test
    public void testDoubleSerializer() {
        // Test DoubleSerializer functionality
    }
}