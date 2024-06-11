import org.junit.Test;
import static org.junit.Assert.*;

public class BooleanUtilsTest {

    @Test
    public void testNegate() {
        // Typical use case
        assertEquals(Boolean.FALSE, BooleanUtils.negate(Boolean.TRUE));
        
        // Edge case
        assertNull(BooleanUtils.negate(null));
    }
    
    @Test
    public void testIsTrue() {
        // Typical use case
        assertTrue(BooleanUtils.isTrue(Boolean.TRUE));
        
        // Edge case
        assertFalse(BooleanUtils.isTrue(null));
    }
    
    @Test
    public void testIsNotTrue() {
        // Typical use case
        assertFalse(BooleanUtils.isNotTrue(Boolean.TRUE));
        
        // Edge case
        assertTrue(BooleanUtils.isNotTrue(null));
    }
    
    @Test
    public void testIsFalse() {
        // Typical use case
        assertTrue(BooleanUtils.isFalse(Boolean.FALSE));
        
        // Edge case
        assertFalse(BooleanUtils.isFalse(null));
    }
    
    @Test
    public void testIsNotFalse() {
        // Typical use case
        assertFalse(BooleanUtils.isNotFalse(Boolean.FALSE));
        
        // Edge case
        assertTrue(BooleanUtils.isNotFalse(null));
    }
    
    // More test cases for other methods in the BooleanUtils class can be added here
}