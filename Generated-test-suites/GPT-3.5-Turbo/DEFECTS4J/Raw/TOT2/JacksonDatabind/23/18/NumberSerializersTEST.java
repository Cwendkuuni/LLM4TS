import static org.junit.Assert.*;
import org.junit.Test;

public class NumberSerializersTest {

    @Test
    public void testShortSerializer() {
        ShortSerializer shortSerializer = new ShortSerializer();
        // Typical use case
        // Test a positive value
        assertEquals("Serialized value is incorrect", "1", shortSerializer.serialize((short) 1));
        // Edge case
        // Test the maximum value for a short
        assertEquals("Serialized value is incorrect", "32767", shortSerializer.serialize(Short.MAX_VALUE));
        // Error scenario
        // Test a null value
        assertNull("Serialized value should be null", shortSerializer.serialize(null));
    }

    @Test
    public void testIntegerSerializer() {
        IntegerSerializer integerSerializer = new IntegerSerializer();
        // Typical use case
        // Test a positive value
        assertEquals("Serialized value is incorrect", "2", integerSerializer.serialize(2));
        // Edge case
        // Test the minimum value for an integer
        assertEquals("Serialized value is incorrect", "-2147483648", integerSerializer.serialize(Integer.MIN_VALUE));
        // Error scenario
        // Test a non-integer value
        assertThrows("Should throw exception when non-integer value is passed", IOException.class,
            () -> integerSerializer.serialize("test"));
    }

    @Test
    public void testIntLikeSerializer() {
        IntLikeSerializer intLikeSerializer = new IntLikeSerializer();
        // Typical use case
        // Test a positive value
        assertEquals("Serialized value is incorrect", "3", intLikeSerializer.serialize(3.0));
        // Edge case
        // Test a negative value
        assertEquals("Serialized value is incorrect", "-4", intLikeSerializer.serialize(-4.0));
        // Error scenario
        // Test a null value
        assertNull("Serialized value should be null", intLikeSerializer.serialize(null));
    }

    @Test
    public void testLongSerializer() {
        LongSerializer longSerializer = new LongSerializer();
        // Typical use case
        // Test a positive value
        assertEquals("Serialized value is incorrect", "1234567890", longSerializer.serialize(1234567890L));
        // Edge case
        // Test the maximum value for a long
        assertEquals("Serialized value is incorrect", "9223372036854775807", longSerializer.serialize(Long.MAX_VALUE));
        // Error scenario
        // Test a non-long value
        assertThrows("Should throw exception when non-long value is passed", IOException.class,
            () -> longSerializer.serialize(12.34));
    }

    @Test
    public void testFloatSerializer() {
        FloatSerializer floatSerializer = new FloatSerializer();
        // Typical use case
        // Test a positive value
        assertEquals("Serialized value is incorrect", "2.5", floatSerializer.serialize(2.5f));
        // Edge case
        // Test a negative value
        assertEquals("Serialized value is incorrect", "-3.75", floatSerializer.serialize(-3.75f));
        // Error scenario
        // Test a null value
        assertNull("Serialized value should be null", floatSerializer.serialize(null));
    }

    @Test
    public void testDoubleSerializer() {
        DoubleSerializer doubleSerializer = new DoubleSerializer();
        // Typical use case
        // Test a positive value
        assertEquals("Serialized value is incorrect", "3.14", doubleSerializer.serialize(3.14));
        // Edge case
        // Test a negative value
        assertEquals("Serialized value is incorrect", "-2.718", doubleSerializer.serialize(-2.718));
        // Error scenario
        // Test a non-double value
        assertThrows("Should throw exception when non-double value is passed", IOException.class,
            () -> doubleSerializer.serialize("test"));
    }
}