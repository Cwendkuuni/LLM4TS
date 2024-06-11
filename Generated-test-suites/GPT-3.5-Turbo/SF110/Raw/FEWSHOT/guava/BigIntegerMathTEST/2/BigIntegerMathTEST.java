
package com.google.common.math;

import static org.junit.Assert.assertEquals;

import java.math.BigInteger;
import java.math.RoundingMode;

import org.junit.Test;

public class BigIntegerMathTest {
    
    @Test
    public void testIsPowerOfTwo() {
        BigInteger num = new BigInteger("2");
        assertEquals(true, BigIntegerMath.isPowerOfTwo(num));
        
        num = new BigInteger("0");
        assertEquals(false, BigIntegerMath.isPowerOfTwo(num));
        
        num = new BigInteger("5");
        assertEquals(false, BigIntegerMath.isPowerOfTwo(num));
        
        num = new BigInteger("256");
        assertEquals(true, BigIntegerMath.isPowerOfTwo(num));
    }
    
    @Test
    public void testLog2() {
        BigInteger num = new BigInteger("16");
        assertEquals(4, BigIntegerMath.log2(num, RoundingMode.UNNECESSARY));
        
        num = new BigInteger("128");
        assertEquals(7, BigIntegerMath.log2(num, RoundingMode.FLOOR));
        
        num = new BigInteger("256");
        assertEquals(8, BigIntegerMath.log2(num, RoundingMode.CEILING));
        
        num = new BigInteger("1024");
        assertEquals(10, BigIntegerMath.log2(num, RoundingMode.DOWN));
        
        num = new BigInteger("1000");
        assertEquals(10, BigIntegerMath.log2(num, RoundingMode.HALF_DOWN));
    }
    
    @Test
    public void testLog10() {
        BigInteger num = new BigInteger("10");
        assertEquals(1, BigIntegerMath.log10(num, RoundingMode.UNNECESSARY));
        
        num = new BigInteger("100");
        assertEquals(2, BigIntegerMath.log10(num, RoundingMode.FLOOR));
        
        num = new BigInteger("1000");
        assertEquals(3, BigIntegerMath.log10(num, RoundingMode.CEILING));
        
        num = new BigInteger("10000");
        assertEquals(4, BigIntegerMath.log10(num, RoundingMode.DOWN));
        
        num = new BigInteger("1024");
        assertEquals(3, BigIntegerMath.log10(num, RoundingMode.HALF_EVEN));
    }
    
    @Test
    public void testSqrt() {
        BigInteger num = new BigInteger("16");
        assertEquals(new BigInteger("4"), BigIntegerMath.sqrt(num, RoundingMode.HALF_EVEN));
        
        num = new BigInteger("100");
        assertEquals(new BigInteger("10"), BigIntegerMath.sqrt(num, RoundingMode.FLOOR));
        
        num = new BigInteger("256");
        assertEquals(new BigInteger("16"), BigIntegerMath.sqrt(num, RoundingMode.CEILING));
        
        num = new BigInteger("1024");
        assertEquals(new BigInteger("32"), BigIntegerMath.sqrt(num, RoundingMode.DOWN));
    }
    
    @Test
    public void testDivide() {
        BigInteger num1 = new BigInteger("10");
        BigInteger num2 = new BigInteger("2");
        assertEquals(new BigInteger("5"), BigIntegerMath.divide(num1, num2, RoundingMode.UNNECESSARY));
        
        num1 = new BigInteger("100");
        num2 = new BigInteger("5");
        assertEquals(new BigInteger("20"), BigIntegerMath.divide(num1, num2, RoundingMode.FLOOR));
        
        num1 = new BigInteger("256");
        num2 = new BigInteger("16");
        assertEquals(new BigInteger("16"), BigIntegerMath.divide(num1, num2, RoundingMode.CEILING));
        
        num1 = new BigInteger("1024");
        num2 = new BigInteger("32");
        assertEquals(new BigInteger("32"), BigIntegerMath.divide(num1, num2, RoundingMode.DOWN));
    }
    
    @Test
    public void testFactorial() {
        BigInteger num = new BigInteger("0");
        assertEquals(BigInteger.valueOf(1), BigIntegerMath.factorial(num.intValue()));
        
        num = new BigInteger("5");
        assertEquals(BigInteger.valueOf(120), BigIntegerMath.factorial(num.intValue()));
        
        num = new BigInteger("10");
        assertEquals(new BigInteger("3628800"), BigIntegerMath.factorial(num.intValue()));
        
        num = new BigInteger("20");
        assertEquals(new BigInteger("2432902008176640000"), BigIntegerMath.factorial(num.intValue()));
    }
    
    @Test
    public void testBinomial() {
        int n = 5;
        int k = 2;
        assertEquals(BigInteger.valueOf(10), BigIntegerMath.binomial(n, k));
        
        n = 10;
        k = 3;
        assertEquals(BigInteger.valueOf(120), BigIntegerMath.binomial(n, k));
        
        n = 15;
        k = 6;
        assertEquals(BigInteger.valueOf(5005), BigIntegerMath.binomial(n, k));
        
        n = 50;
        k = 10;
        assertEquals(new BigInteger("10272278170"), BigIntegerMath.binomial(n, k));
    }
}
