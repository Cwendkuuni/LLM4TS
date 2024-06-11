import org.junit.*;
import static org.junit.Assert.*;

public class MathUtilsTest {
    
    @Test
    public void testHash_double() {
        assertEquals(0, MathUtils.hash(0.0));
        assertEquals(2147483647, MathUtils.hash(Double.MAX_VALUE));
        assertEquals(-2147483648, MathUtils.hash(Double.MIN_VALUE));
    }
    
    @Test
    public void testHash_doubleArray() {
        double[] array1 = {1.0, 2.0, 3.0};
        double[] array2 = {4.0, 5.0, 6.0};
        
        assertEquals(1527, MathUtils.hash(array1)); // Typical use case
        assertEquals(1251, MathUtils.hash(array2)); // Typical use case
        assertNotEquals(MathUtils.hash(array1), MathUtils.hash(array2)); // Edge case
    }
    
    @Test
    public void testNormalizeAngle() {
        assertEquals(0.0, MathUtils.normalizeAngle(0.0, 0.0), 0.0); // Typical use case
        assertEquals(0.0, MathUtils.normalizeAngle(6.283185307179586, 0.0), 0.0); // Edge case
        assertEquals(-0.1, MathUtils.normalizeAngle(6.183185307179586, 0.0), 0.0); // Error scenario
    }
    
    @Test
    public void testReduce() {
        assertEquals(0.0, MathUtils.reduce(0.0, 2 * Math.PI, 0.0), 0.0); // Typical use case
        assertEquals(0.0, MathUtils.reduce(6.283185307179586, 2 * Math.PI, 0.0), 0.0); // Edge case
        assertEquals(-0.1, MathUtils.reduce(6.183185307179586, 2 * Math.PI, 0.0), 0.0); // Error scenario
    }
    
    @Test
    public void testCopySign_byte() {
        assertEquals(1, MathUtils.copySign((byte) 1, (byte) 1)); // Typical use case
        assertEquals(-1, MathUtils.copySign((byte) 1, (byte) -1)); // Typical use case
        assertThrows(MathArithmeticException.class, () -> {
            MathUtils.copySign((byte) 1, (byte) -128); // Error scenario
        });
    }
    
    @Test
    public void testCopySign_short() {
        assertEquals(1, MathUtils.copySign((short) 1, (short) 1)); // Typical use case
        assertEquals(-1, MathUtils.copySign((short) 1, (short) -1)); // Typical use case
        assertThrows(MathArithmeticException.class, () -> {
            MathUtils.copySign((short) 1, (short) -32768); // Error scenario
        });
    }
    
    @Test
    public void testCopySign_int() {
        assertEquals(1, MathUtils.copySign(1, 1)); // Typical use case
        assertEquals(-1, MathUtils.copySign(1, -1)); // Typical use case
        assertThrows(MathArithmeticException.class, () -> {
            MathUtils.copySign(1, Integer.MIN_VALUE); // Error scenario
        });
    }
    
    @Test
    public void testCopySign_long() {
        assertEquals(1L, MathUtils.copySign(1L, 1L)); // Typical use case
        assertEquals(-1L, MathUtils.copySign(1L, -1L)); // Typical use case
        assertThrows(MathArithmeticException.class, () -> {
            MathUtils.copySign(1L, Long.MIN_VALUE); // Error scenario
        });
    }
    
    @Test
    public void testCheckFinite_double() {
        MathUtils.checkFinite(0.0); // Typical use case
        
        assertThrows(NotFiniteNumberException.class, () -> {
            MathUtils.checkFinite(Double.POSITIVE_INFINITY); // Error scenario
        });
        
        assertThrows(NotFiniteNumberException.class, () -> {
            MathUtils.checkFinite(Double.NaN); // Error scenario
        });
    }
    
    @Test
    public void testCheckFinite_doubleArray() {
        double[] array1 = {1.0, 2.0, 3.0};
        double[] array2 = {1.0, Double.POSITIVE_INFINITY, 3.0};
        
        MathUtils.checkFinite(array1); // Typical use case
        
        assertThrows(NotFiniteNumberException.class, () -> {
            MathUtils.checkFinite(array2); // Error scenario
        });
    }
    
    @Test
    public void testCheckNotNull() {
        MathUtils.checkNotNull(new Object()); // Typical use case
        
        assertThrows(NullArgumentException.class, () -> {
            MathUtils.checkNotNull(null); // Error scenario
        });
    }
    
    @Test
    public void testCheckNotNull_pattern_args() {
        MathUtils.checkNotNull(new Object(), () -> "Pattern", 1); // Typical use case
        
        assertThrows(NullArgumentException.class, () -> {
            MathUtils.checkNotNull(null, () -> "Pattern", 1); // Error scenario
        });
    }
}