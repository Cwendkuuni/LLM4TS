import static org.junit.Assert.*;
import org.junit.Test;

public class BooleanUtilsTest {

    @Test
    public void testNegate() {
        assertTrue(BooleanUtils.negate(Boolean.TRUE));
        assertFalse(BooleanUtils.negate(Boolean.FALSE));
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
        assertFalse(BooleanUtils.isFalse(Boolean.TRUE));
        assertTrue(BooleanUtils.isFalse(Boolean.FALSE));
        assertFalse(BooleanUtils.isFalse(null));
    }

    @Test
    public void testIsNotFalse() {
        assertTrue(BooleanUtils.isNotFalse(Boolean.TRUE));
        assertFalse(BooleanUtils.isNotFalse(Boolean.FALSE));
        assertTrue(BooleanUtils.isNotFalse(null));
    }

    @Test
    public void testToBoolean() {
        assertFalse(BooleanUtils.toBoolean(null));
        assertFalse(BooleanUtils.toBoolean(Boolean.FALSE));
        assertTrue(BooleanUtils.toBoolean(Boolean.TRUE));
        assertTrue(BooleanUtils.toBoolean(Boolean.TRUE));
    }
    
    // Include more test cases for the remaining methods

}