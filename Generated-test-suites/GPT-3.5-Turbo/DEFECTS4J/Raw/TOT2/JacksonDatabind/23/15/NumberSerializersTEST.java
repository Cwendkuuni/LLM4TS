import org.junit.Test;
import static org.junit.Assert.*;

public class NumberSerializersTest {

    @Test
    public void testShortSerializer() {
        ShortSerializer serializer = new ShortSerializer();
        // Test typical use case
        Short value = 10;
        //assert statement
        assertEquals("Serialized value should match", "10", serializer.serialize(value));
        // Test edge case
        value = Short.MAX_VALUE;
        assertEquals("Serialized value should match", "32767", serializer.serialize(value));
        // Test error scenario (if applicable)
    }

    @Test
    public void testIntegerSerializer() {
        IntegerSerializer serializer = new IntegerSerializer();
        // Test typical use case
        Integer value = 100;
        assertEquals("Serialized value should match", "100", serializer.serialize(value));
        // Test edge case
        value = Integer.MAX_VALUE;
        assertEquals("Serialized value should match", "2147483647", serializer.serialize(value));
        // Test error scenario (if applicable)
    }

    @Test
    public void testIntLikeSerializer() {
        IntLikeSerializer serializer = new IntLikeSerializer();
        // Test typical use case
        Number value = 25;
        assertEquals("Serialized value should match", "25", serializer.serialize(value));
        // Test edge case
        value = 2147483647;
        assertEquals("Serialized value should match", "2147483647", serializer.serialize(value));
        // Test error scenario (if applicable)
    }

    @Test
    public void testLongSerializer() {
        LongSerializer serializer = new LongSerializer();
        // Test typical use case
        Long value = 1000L;
        assertEquals("Serialized value should match", "1000", serializer.serialize(value));
        // Test edge case
        value = Long.MAX_VALUE;
        assertEquals("Serialized value should match", "9223372036854775807", serializer.serialize(value));
        // Test error scenario (if applicable)
    }

    @Test
    public void testFloatSerializer() {
        FloatSerializer serializer = new FloatSerializer();
        // Test typical use case
        Float value = 10.5f;
        assertEquals("Serialized value should match", "10.5", serializer.serialize(value));
        // Test edge case
        value = Float.MAX_VALUE;
        assertEquals("Serialized value should match", "3.4028235E38", serializer.serialize(value));
        // Test error scenario (if applicable)
    }

    @Test
    public void testDoubleSerializer() {
        DoubleSerializer serializer = new DoubleSerializer();
        // Test typical use case
        Double value = 25.75;
        assertEquals("Serialized value should match", "25.75", serializer.serialize(value));
        // Test edge case
        value = Double.MAX_VALUE;
        assertEquals("Serialized value should match", "1.7976931348623157E308", serializer.serialize(value));
        // Test error scenario (if applicable)
    }
}