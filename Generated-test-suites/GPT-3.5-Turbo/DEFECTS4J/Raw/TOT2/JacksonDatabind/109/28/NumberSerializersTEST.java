import static org.junit.Assert.*;
import org.junit.Test;
import com.fasterxml.jackson.databind.ser.std.*;

public class NumberSerializersTest {

    @Test
    public void testShortSerializer() {
        ShortSerializer shortSerializer = new ShortSerializer();
        Short value = 100;
        assertEquals("Expected serialization of Short value", 100, shortSerializer.serialize(value));
    }

    @Test
    public void testIntegerSerializer() {
        IntegerSerializer intSerializer = new IntegerSerializer(Integer.class);
        Integer value = 1000;
        assertEquals("Expected serialization of Integer value", 1000, intSerializer.serialize(value));
    }

    @Test
    public void testIntLikeSerializer() {
        IntLikeSerializer intLikeSerializer = new IntLikeSerializer();
        Number value = 1500;
        assertEquals("Expected serialization of Number value as int", 1500, intLikeSerializer.serialize(value));
    }

    @Test
    public void testLongSerializer() {
        LongSerializer longSerializer = new LongSerializer(Long.class);
        Long value = 1000000L;
        assertEquals("Expected serialization of Long value", 1000000L, longSerializer.serialize(value));
    }

    @Test
    public void testFloatSerializer() {
        FloatSerializer floatSerializer = new FloatSerializer();
        Float value = 3.14f;
        assertEquals("Expected serialization of Float value", 3.14f, floatSerializer.serialize(value), 0.0);
    }

    @Test
    public void testDoubleSerializer() {
        DoubleSerializer doubleSerializer = new DoubleSerializer(Double.class);
        Double value = 3.14159;
        assertEquals("Expected serialization of Double value", 3.14159, doubleSerializer.serialize(value), 0.0);
    }

    @Test
    public void testAddAll() {
        Map<String, JsonSerializer<?>> allDeserializers = new HashMap<>();
        NumberSerializers.addAll(allDeserializers);
        assertEquals("Expected IntegerSerializer added to allDeserializers", IntegerSerializer.class, allDeserializers.get(Integer.class.getName()).getClass());
        assertEquals("Expected IntegerSerializer added to allDeserializers", IntegerSerializer.class, allDeserializers.get(Integer.TYPE.getName()).getClass());
    }
}