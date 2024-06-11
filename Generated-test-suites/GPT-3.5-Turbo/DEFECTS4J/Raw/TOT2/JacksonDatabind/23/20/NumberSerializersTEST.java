import org.junit.Test;
import static org.junit.Assert.*;

public class NumberSerializersTest {

    @Test
    public void testShortSerializer() {
        // Test typical use case
        ShortSerializer shortSerializer = new ShortSerializer();
        Short value = 10;
        assertEquals("Serializing Short value should match", "10", shortSerializer.serialize(value));
        
        // Test edge case
        value = Short.MAX_VALUE;
        assertEquals("Serializing Short max value should match", String.valueOf(Short.MAX_VALUE), shortSerializer.serialize(value));
    }

    @Test
    public void testIntegerSerializer() {
        // Test typical use case
        IntegerSerializer integerSerializer = new IntegerSerializer();
        Integer value = 100;
        assertEquals("Serializing Integer value should match", "100", integerSerializer.serialize(value));
        
        // Test edge case
        value = Integer.MIN_VALUE;
        assertEquals("Serializing Integer min value should match", String.valueOf(Integer.MIN_VALUE), integerSerializer.serialize(value));
    }

    @Test
    public void testIntLikeSerializer() {
        // Test typical use case
        IntLikeSerializer intLikeSerializer = new IntLikeSerializer();
        Number value = 50;
        assertEquals("Serializing Number value should match", "50", intLikeSerializer.serialize(value));
        
        // Test edge case
        value = Long.MAX_VALUE;
        assertEquals("Serializing Long max value should match", String.valueOf(Long.MAX_VALUE), intLikeSerializer.serialize(value));
    }

    @Test
    public void testLongSerializer() {
        // Test typical use case
        LongSerializer longSerializer = new LongSerializer();
        Long value = 1000L;
        assertEquals("Serializing Long value should match", "1000", longSerializer.serialize(value));
        
        // Test edge case
        value = Long.MIN_VALUE;
        assertEquals("Serializing Long min value should match", String.valueOf(Long.MIN_VALUE), longSerializer.serialize(value));
    }

    @Test
    public void testFloatSerializer() {
        // Test typical use case
        FloatSerializer floatSerializer = new FloatSerializer();
        Float value = 25.5f;
        assertEquals("Serializing Float value should match", "25.5", floatSerializer.serialize(value));
        
        // Test edge case
        value = Float.MAX_VALUE;
        assertEquals("Serializing Float max value should match", String.valueOf(Float.MAX_VALUE), floatSerializer.serialize(value));
    }

    @Test
    public void testDoubleSerializer() {
        // Test typical use case
        DoubleSerializer doubleSerializer = new DoubleSerializer();
        Double value = 50.123;
        assertEquals("Serializing Double value should match", "50.123", doubleSerializer.serialize(value));
        
        // Test edge case
        value = Double.MIN_VALUE;
        assertEquals("Serializing Double min value should match", String.valueOf(Double.MIN_VALUE), doubleSerializer.serialize(value));
    }

}