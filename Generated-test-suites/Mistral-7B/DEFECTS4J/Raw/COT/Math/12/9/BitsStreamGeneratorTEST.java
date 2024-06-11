package org.apache.commons.math3.random;

import static org.junit.Assert.*;

import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.random.BitsStreamGenerator;
import org.apache.commons.math3.util.FastMath;
import org.junit.Test;

/**
 * Test class for {@link BitsStreamGenerator}.
 */
public class BitsStreamGeneratorTest {

    @Test
    public void testSetSeedInt() {
        BitsStreamGenerator generator = new MyBitsStreamGenerator();
        int seed = 12345;
        generator.setSeed(seed);
        int expectedSeed = seed;
        assertEquals(expectedSeed, generator.getSeed());
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testSetSeedNegativeInt() {
        BitsStreamGenerator generator = new MyBitsStreamGenerator();
        int seed = -12345;
        generator.setSeed(seed);
    }

    @Test
    public void testSetSeedLong() {
        BitsStreamGenerator generator = new MyBitsStreamGenerator();
        long seed = 12345678912345L;
        generator.setSeed(seed);
        long expectedSeed = seed;
        assertEquals(expectedSeed, generator.getSeed());
    }

    @Test
    public void testNextBoolean() {
        BitsStreamGenerator generator = new MyBitsStreamGenerator();
        boolean expectedResult = true;
        boolean result = generator.nextBoolean();
        assertEquals(expectedResult, result);
    }

    @Test
    public void testNextBytes() {
        BitsStreamGenerator generator = new MyBitsStreamGenerator();
        byte[] expectedBytes = new byte[]{1, 2, 3, 4};
        byte[] testBytes = new byte[4];
        generator.nextBytes(testBytes);
        assertArrayEquals(expectedBytes, testBytes);
    }

    @Test
    public void testNextDouble() {
        BitsStreamGenerator generator = new MyBitsStreamGenerator();
        double expectedResult = 0.12345;
        double result = generator.nextDouble();
        assertEquals(expectedResult, result, 0.00001);
    }

    @Test
    public void testNextFloat() {
        BitsStreamGenerator generator = new MyBitsStreamGenerator();
        float expectedResult = 0.12345f;
        float result = generator.nextFloat();
        assertEquals(expectedResult, result, 0.001f);
    }

    @Test
    public void testNextGaussian() {
        BitsStreamGenerator generator = new MyBitsStreamGenerator();
        double expectedResult = 1.2345;
        double result = generator.nextGaussian();
        assertEquals(expectedResult, result, 0.001);
    }

    @Test
    public void testNextInt() {
        BitsStreamGenerator generator = new MyBitsStreamGenerator();
        int expectedResult = 12345;
        int result = generator.nextInt();
        assertEquals(expectedResult, result);
    }

    @Test
    public void testNextIntWithLimit() {
        BitsStreamGenerator generator = new MyBitsStreamGenerator();
        int expectedResult = 10;
        int limit = 20;
        int result = generator.nextInt(limit);
        assertTrue(result <= limit);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testNextIntNegativeLimit() {
        BitsStreamGenerator generator = new MyBitsStreamGenerator();
        int limit = -20;
        generator.nextInt(limit);
    }

    @Test
    public void testNextLong() {
        BitsStreamGenerator generator = new MyBitsStreamGenerator();
        long expectedResult = 12345678912345L;
        long result = generator.nextLong();
        assertEquals(expectedResult, result);
    }

    @Test
    public void testClear() {
        BitsStreamGenerator generator = new MyBitsStreamGenerator();
        generator.clear();
        double nextGaussian = generator.nextGaussian();
        assertTrue(Double.isNaN(nextGaussian));
    }

}

class MyBitsStreamGenerator extends BitsStreamGenerator {

    @Override
    protected int next(int bits) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getSeed() {
        throw new UnsupportedOperationException();
    }

}