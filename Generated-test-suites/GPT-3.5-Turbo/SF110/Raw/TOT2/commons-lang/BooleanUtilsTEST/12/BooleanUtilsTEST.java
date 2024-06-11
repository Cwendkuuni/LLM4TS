import static org.junit.Assert.*;
import org.junit.Test;

public class BooleanUtilsTest {
    
    @Test
    public void testNegate() {
        assertNull(BooleanUtils.negate(null));
        assertTrue(BooleanUtils.negate(true));
        assertFalse(BooleanUtils.negate(false));
    }
    
    @Test
    public void testIsTrue() {
        assertTrue(BooleanUtils.isTrue(true));
        assertFalse(BooleanUtils.isTrue(null));
        assertFalse(BooleanUtils.isTrue(false));
    }
    
    @Test
    public void testIsNotTrue() {
        assertFalse(BooleanUtils.isNotTrue(true));
        assertTrue(BooleanUtils.isNotTrue(null));
        assertTrue(BooleanUtils.isNotTrue(false));
    }
    
    @Test
    public void testIsFalse() {
        assertTrue(BooleanUtils.isFalse(false));
        assertFalse(BooleanUtils.isFalse(null));
        assertFalse(BooleanUtils.isFalse(true));
    }
    
    @Test
    public void testIsNotFalse() {
        assertFalse(BooleanUtils.isNotFalse(false));
        assertTrue(BooleanUtils.isNotFalse(null));
        assertTrue(BooleanUtils.isNotFalse(true));
    }
    
    @Test
    public void testToBoolean() {
        assertTrue(BooleanUtils.toBoolean(Boolean.TRUE));
        assertFalse(BooleanUtils.toBoolean(Boolean.FALSE));
        assertFalse(BooleanUtils.toBoolean(null));
    }
    
    @Test
    public void testToBooleanDefaultIfNull() {
        assertTrue(BooleanUtils.toBooleanDefaultIfNull(Boolean.TRUE, true));
        assertFalse(BooleanUtils.toBooleanDefaultIfNull(Boolean.FALSE, true));
        assertTrue(BooleanUtils.toBooleanDefaultIfNull(null, true));
    }
    
    // Additional test cases can be added for other methods in the BooleanUtils class
    
}