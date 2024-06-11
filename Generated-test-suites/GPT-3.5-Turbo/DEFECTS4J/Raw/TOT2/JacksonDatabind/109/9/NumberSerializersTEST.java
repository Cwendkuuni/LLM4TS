import static org.junit.Assert.*;
import org.junit.Test;
import java.util.HashMap;

public class NumberSerializersTest {

    @Test
    public void testAddAll() {
        HashMap<String, JsonSerializer<?>> allDeserializers = new HashMap<>();
        NumberSerializers.addAll(allDeserializers);

        // Check if all the expected serializers are added
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
        NumberSerializers.ShortSerializer shortSerializer = NumberSerializers.ShortSerializer.instance;

        // Check serialization of a short value
        // Add more test cases for different short values
    }

    @Test
    public void testIntegerSerializer() {
        NumberSerializers.IntegerSerializer integerSerializer = new NumberSerializers.IntegerSerializer(Integer.class);

        // Check serialization of integer value
        // Add edge cases for Integer.MAX_VALUE and Integer.MIN_VALUE
    }

    @Test
    public void testIntLikeSerializer() {
        NumberSerializers.IntLikeSerializer intLikeSerializer = NumberSerializers.IntLikeSerializer.instance;

        // Check serialization of a Number value as integer
        // Add edge cases for different Number types
    }

    @Test
    public void testLongSerializer() {
        NumberSerializers.LongSerializer longSerializer = new NumberSerializers.LongSerializer(Long.class);

        // Check serialization of Long value
        // Add edge cases for Long.MAX_VALUE and Long.MIN_VALUE
    }

    @Test
    public void testFloatSerializer() {
        NumberSerializers.FloatSerializer floatSerializer = NumberSerializers.FloatSerializer.instance;

        // Check serialization of Float value
        // Add edge cases for positive/negative Float values
    }

    @Test
    public void testDoubleSerializer() {
        NumberSerializers.DoubleSerializer doubleSerializer = new NumberSerializers.DoubleSerializer(Double.class);

        // Check serialization of Double value
        // Add edge cases for Double.MAX_VALUE and Double.MIN_VALUE
    }

}