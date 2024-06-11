import static org.junit.Assert.*;
import org.junit.Test;

public class NumberSerializersTest {

    @Test
    public void testShortSerializer() {
        ShortSerializer shortSerializer = new ShortSerializer();
        
        // Typical use case
        assertEquals("Short value not serialized correctly", "42", shortSerializer.serialize((short) 42));
        
        // Edge case
        assertEquals("Negative Short value not serialized correctly", "-10", shortSerializer.serialize((short) -10));
        
        // Error scenario (not applicable for ShortSerializer)
    }

    @Test
    public void testIntegerSerializer() {
        IntegerSerializer integerSerializer = new IntegerSerializer();
        
        // Typical use case
        assertEquals("Integer value not serialized correctly", "100", integerSerializer.serialize(100));
        
        // Edge case
        assertEquals("Negative Integer value not serialized correctly", "-500", integerSerializer.serialize(-500));
        
        // Error scenario (not applicable for IntegerSerializer)
    }

    @Test
    public void testIntLikeSerializer() {
        IntLikeSerializer intLikeSerializer = new IntLikeSerializer();
        
        // Typical use case
        assertEquals("Number value not serialized correctly", "250", intLikeSerializer.serialize(250));
        
        // Edge case
        assertEquals("Large Number value not serialized correctly", "1000000", intLikeSerializer.serialize(1000000));
        
        // Error scenario (not applicable for IntLikeSerializer)
    }

    @Test
    public void testLongSerializer() {
        LongSerializer longSerializer = new LongSerializer();
        
        // Typical use case
        assertEquals("Long value not serialized correctly", "5000000000", longSerializer.serialize(5000000000L));
        
        // Edge case
        assertEquals("Negative Long value not serialized correctly", "-1234567890", longSerializer.serialize(-1234567890L));
        
        // Error scenario (not applicable for LongSerializer)
    }

    @Test
    public void testFloatSerializer() {
        FloatSerializer floatSerializer = new FloatSerializer();
        
        // Typical use case
        assertEquals("Float value not serialized correctly", "3.14", floatSerializer.serialize(3.14f));
        
        // Edge case
        assertEquals("Large Float value not serialized correctly", "123456.789", floatSerializer.serialize(123456.789f));
        
        // Error scenario (not applicable for FloatSerializer)
    }

    @Test
    public void testDoubleSerializer() {
        DoubleSerializer doubleSerializer = new DoubleSerializer();
        
        // Typical use case
        assertEquals("Double value not serialized correctly", "1234.56789", doubleSerializer.serialize(1234.56789));
        
        // Edge case
        assertEquals("Negative Double value not serialized correctly", "-9876.54321", doubleSerializer.serialize(-9876.54321));
        
        // Error scenario (not applicable for DoubleSerializer)
    }
}