package com.google.common.math;
import static org.junit.Assert.*;
import org.junit.Test;
import com.google.common.math.BigIntegerMath;
import java.math.BigInteger;
import java.math.RoundingMode;

public class BigIntegerMathTest {
    
    @Test
    public void testIsPowerOfTwo() {
        assertTrue(BigIntegerMath.isPowerOfTwo(BigInteger.ONE));
        assertFalse(BigIntegerMath.isPowerOfTwo(BigInteger.ZERO));
    }
    
    @Test
    public void testLog2() {
        assertEquals(3, BigIntegerMath.log2(BigInteger.valueOf(8), RoundingMode.FLOOR));
        assertEquals(4, BigIntegerMath.log2(BigInteger.valueOf(8), RoundingMode.CEILING));
    }
    
    @Test
    public void testLog10() {
        assertEquals(10, BigIntegerMath.log10(BigInteger.valueOf(1000), RoundingMode.FLOOR));
        assertEquals(3, BigIntegerMath.log10(BigInteger.valueOf(1000), RoundingMode.CEILING));
    }
    
    @Test
    public void testSqrt() {
        assertEquals(BigInteger.valueOf(4), BigIntegerMath.sqrt(BigInteger.valueOf(16), RoundingMode.FLOOR));
        assertEquals(BigInteger.valueOf(5), BigIntegerMath.sqrt(BigInteger.valueOf(25), RoundingMode.CEILING));
    }
    
    @Test
    public void testDivide() {
        assertEquals(BigInteger.valueOf(5), BigIntegerMath.divide(BigInteger.valueOf(25), BigInteger.valueOf(5), RoundingMode.FLOOR));
        assertEquals(BigInteger.valueOf(2), BigIntegerMath.divide(BigInteger.valueOf(10), BigInteger.valueOf(4), RoundingMode.CEILING));
    }
    
    @Test
    public void testFactorial() {
        assertEquals(BigInteger.valueOf(24), BigIntegerMath.factorial(4));
        assertEquals(BigInteger.valueOf(720), BigIntegerMath.factorial(6));
    }
    
    @Test
    public void testBinomial() {
        assertEquals(BigInteger.valueOf(10), BigIntegerMath.binomial(5, 2));
        assertEquals(BigInteger.valueOf(15), BigIntegerMath.binomial(6, 2));
    }
}