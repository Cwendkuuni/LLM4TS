import static org.junit.Assert.*;
import org.junit.Test;

public class NumberSerializersTest {

    @Test
    public void testAddAll() {
        // Test adding all serializers to the map
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
        // Test ShortSerializer class
        ShortSerializer shortSerializer = new ShortSerializer();
        assertNotNull(shortSerializer);
    }

    @Test
    public void testIntegerSerializer() {
        // Test IntegerSerializer class
        IntegerSerializer integerSerializer = new IntegerSerializer(Integer.class);
        assertNotNull(integerSerializer);
    }

    @Test
    public void testIntLikeSerializer() {
        // Test IntLikeSerializer class
        IntLikeSerializer intLikeSerializer = new IntLikeSerializer();
        assertNotNull(intLikeSerializer);
    }

    @Test
    public void testLongSerializer() {
        // Test LongSerializer class
        LongSerializer longSerializer = new LongSerializer(Long.class);
        assertNotNull(longSerializer);
    }

    @Test
    public void testFloatSerializer() {
        // Test FloatSerializer class
        FloatSerializer floatSerializer = new FloatSerializer();
        assertNotNull(floatSerializer);
    }

    @Test
    public void testDoubleSerializer() {
        // Test DoubleSerializer class
        DoubleSerializer doubleSerializer = new DoubleSerializer(Double.class);
        assertNotNull(doubleSerializer);
    }
}