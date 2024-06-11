import org.junit.Test;
import static org.junit.Assert.*;

public class BooleanUtilsTest {

    @Test
    public void testNegate() {
        assertEquals(Boolean.FALSE, BooleanUtils.negate(Boolean.TRUE));
        assertEquals(Boolean.TRUE, BooleanUtils.negate(Boolean.FALSE));
        assertNull(BooleanUtils.negate(null));
    }

    @Test
    public void testIsTrue() {
        assertTrue(BooleanUtils.isTrue(Boolean.TRUE));
        assertFalse(BooleanUtils.isTrue(Boolean.FALSE));
        assertFalse(BooleanUtils.isTrue(null));
    }

    @Test
    public void testIsNotTrue() {
        assertFalse(BooleanUtils.isNotTrue(Boolean.TRUE));
        assertTrue(BooleanUtils.isNotTrue(Boolean.FALSE));
        assertTrue(BooleanUtils.isNotTrue(null));
    }

    @Test
    public void testIsFalse() {
        assertTrue(BooleanUtils.isFalse(Boolean.FALSE));
        assertFalse(BooleanUtils.isFalse(Boolean.TRUE));
        assertFalse(BooleanUtils.isFalse(null));
    }

    @Test
    public void testIsNotFalse() {
        assertFalse(BooleanUtils.isNotFalse(Boolean.FALSE));
        assertTrue(BooleanUtils.isNotFalse(Boolean.TRUE));
        assertTrue(BooleanUtils.isNotFalse(null));
    }

    @Test
    public void testToBoolean() {
        assertTrue(BooleanUtils.toBoolean(Boolean.TRUE));
        assertFalse(BooleanUtils.toBoolean(Boolean.FALSE));
        assertFalse(BooleanUtils.toBoolean(null));
    }

    // Additional test cases for the remaining methods should be added

}