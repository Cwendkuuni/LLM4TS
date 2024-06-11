import static org.junit.Assert.*;
import org.junit.Test;

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
        assertNotNull(allDeserializers.get(Float.class.getName()));
        assertNotNull(allDeserializers.get(Float.TYPE.getName()));
        assertNotNull(allDeserializers.get(Double.class.getName()));
        assertNotNull(allDeserializers.get(Double.TYPE.getName()));
    }

    @Test
    public void testShortSerializer() {
        ShortSerializer serializer = new ShortSerializer();
        assertNotNull(serializer);
        assertEquals(Short.class, serializer.handledType());
    }

    @Test
    public void testIntegerSerializer() {
        IntegerSerializer serializer = new IntegerSerializer();
        assertNotNull(serializer);
        assertEquals(Integer.class, serializer.handledType());
    }

    @Test
    public void testIntLikeSerializer() {
        IntLikeSerializer serializer = new IntLikeSerializer();
        assertNotNull(serializer);
        assertEquals(Number.class, serializer.handledType());
    }

    @Test
    public void testLongSerializer() {
        LongSerializer serializer = new LongSerializer();
        assertNotNull(serializer);
        assertEquals(Long.class, serializer.handledType());
    }

    @Test
    public void testFloatSerializer() {
        FloatSerializer serializer = new FloatSerializer();
        assertNotNull(serializer);
        assertEquals(Float.class, serializer.handledType());
    }

    @Test
    public void testDoubleSerializer() {
        DoubleSerializer serializer = new DoubleSerializer();
        assertNotNull(serializer);
        assertEquals(Double.class, serializer.handledType());
    }
}