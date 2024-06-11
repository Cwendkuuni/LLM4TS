import static org.junit.Assert.*;
import org.junit.Test;

public class BooleanUtilsTest {
    
    @Test
    public void testNegate() {
        assertNull(BooleanUtils.negate(null));
        assertEquals(Boolean.FALSE, BooleanUtils.negate(Boolean.TRUE));
        assertEquals(Boolean.TRUE, BooleanUtils.negate(Boolean.FALSE));
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
    }
    
    @Test
    public void testToBooleanDefaultIfNull() {
        assertFalse(BooleanUtils.toBooleanDefaultIfNull(null, false));
        assertTrue(BooleanUtils.toBooleanDefaultIfNull(null, true));
        assertFalse(BooleanUtils.toBooleanDefaultIfNull(Boolean.FALSE, true));
        assertTrue(BooleanUtils.toBooleanDefaultIfNull(Boolean.TRUE, false));
    }
    
    // ... add more test methods for the remaining methods in BooleanUtils class
    
    // ...
    
    // ... test methods for Array-based methods (and, or, xor)
    
    @Test
    public void testAnd() {
        assertFalse(BooleanUtils.and(false, false, false));
        assertFalse(BooleanUtils.and(false, true, false));
        assertTrue(BooleanUtils.and(true, true, true));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testAndWithNullArray() {
        BooleanUtils.and(null);
    }
    
    // ... continue with test methods for other Array-based methods (or and xor)
    
    // ...
    
}