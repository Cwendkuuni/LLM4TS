import static org.junit.Assert.*;
import org.junit.Test;

public class NumberSerializersTest {

    @Test
    public void testShortSerializer() {
        ShortSerializer serializer = new ShortSerializer();
        Short input = 10;
        // Typical use case
        assertEquals("Test typical use case", "10", serializer.serialize(input));
        // Edge case
        Short negativeInput = -10;
        assertEquals("Test edge case - negative input", "-10", serializer.serialize(negativeInput));
        // Error scenario - null input
        assertThrows(NullPointerException.class, () -> serializer.serialize(null));
    }

    @Test
    public void testIntegerSerializer() {
        IntegerSerializer serializer = new IntegerSerializer();
        Integer input = 100;
        // Typical use case
        assertEquals("Test typical use case", "100", serializer.serialize(input));
        // Edge case
        Integer maxIntegerValue = Integer.MAX_VALUE;
        assertEquals("Test edge case - max integer value", Integer.toString(Integer.MAX_VALUE), serializer.serialize(maxIntegerValue));
        // Error scenario - invalid input
        assertThrows(ClassCastException.class, () -> serializer.serialize("Invalid"));
    }

    @Test
    public void testIntLikeSerializer() {
        IntLikeSerializer serializer = new IntLikeSerializer();
        Number input = 35;
        // Typical use case
        assertEquals("Test typical use case", "35", serializer.serialize(input));
        // Edge case
        Number negativeInput = -50;
        assertEquals("Test edge case - negative input", "-50", serializer.serialize(negativeInput));
        // Error scenario - invalid input
        assertThrows(ClassCastException.class, () -> serializer.serialize("Invalid"));
    }

    @Test
    public void testLongSerializer() {
        LongSerializer serializer = new LongSerializer();
        Long input = 1000L;
        // Typical use case
        assertEquals("Test typical use case", "1000", serializer.serialize(input));
        // Edge case
        Long maxLongValue = Long.MAX_VALUE;
        assertEquals("Test edge case - max long value", Long.toString(Long.MAX_VALUE), serializer.serialize(maxLongValue));
        // Error scenario - null input
        assertThrows(NullPointerException.class, () -> serializer.serialize(null));
    }

    @Test
    public void testFloatSerializer() {
        FloatSerializer serializer = new FloatSerializer();
        Float input = 10.5f;
        // Typical use case
        assertEquals("Test typical use case", "10.5", serializer.serialize(input));
        // Edge case
        Float negativeInput = -7.25f;
        assertEquals("Test edge case - negative input", "-7.25", serializer.serialize(negativeInput));
        // Error scenario - invalid input
        assertThrows(ClassCastException.class, () -> serializer.serialize("Invalid"));
    }

    @Test
    public void testDoubleSerializer() {
        DoubleSerializer serializer = new DoubleSerializer();
        Double input = 25.75;
        // Typical use case
        assertEquals("Test typical use case", "25.75", serializer.serialize(input));
        // Edge case
        Double negativeInput = -15.5;
        assertEquals("Test edge case - negative input", "-15.5", serializer.serialize(negativeInput));
        // Error scenario - null input
        assertThrows(NullPointerException.class, () -> serializer.serialize(null));
    }

}