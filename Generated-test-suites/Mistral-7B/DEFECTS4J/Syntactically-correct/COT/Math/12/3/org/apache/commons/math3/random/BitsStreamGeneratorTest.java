package org.apache.commons.math3.random;

import static org.junit.Assert.*;

import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.random.BitsStreamGenerator;
import org.apache.commons.math3.util.FastMath;
import org.junit.Before;
import org.junit.Test;

/**
 * Test for {@link BitsStreamGenerator} class.
 */
public class BitsStreamGeneratorTest {

    private BitsStreamGenerator generator;

    @Before
    public void setup() {
        generator = new BitsStreamGenerator() {
            @Override
            public void setSeed(int seed) {
                // Not used in this test
            }

            @Override
            public void setSeed(int[] seed) {
                // Not used in this test
            }

            @Override
            public void setSeed(long seed) {
                // Not used in this test
            }

            @Override
            protected int next(int bits) {
                // Implementation for testing only.
                return (int) (Math.random() * (1 << bits));
            }
        };
    }

    @Test
    public void testNextBoolean() {
        boolean expected = true;
        boolean actual = generator.nextBoolean();
        assertEquals(expected, actual);

        expected = false;
        actual = generator.nextBoolean();
        assertEquals(expected, actual);
    }

    @Test
    public void testNextBytes() {
        byte[] expected = new byte[]{1, 2, 3, 4};
        byte[] actual = new byte[4];
        generator.nextBytes(actual);
        assertArrayEquals(expected, actual);
    }

    @Test
    public void testNextDouble() {
        double expected = 0.123456789;
        double actual = generator.nextDouble();
        assertEquals(expected, actual, 0.0000001);
    }

    @Test
    public void testNextFloat() {
        float expected = 0.123456f;
        float actual = generator.nextFloat();
        assertEquals(expected, actual, 0.0001f);
    }

    @Test
    public void testNextGaussian() {
        double expectedMean = 0.123456789;
        double expectedStdDev = 0.0123456789;
        double actualMean = generator.nextGaussian();
        double actualStdDev = generator.nextGaussian();

        double meanSquare = FastMath.pow(expectedMean, 2) + FastMath.pow(expectedStdDev, 2);
        double variance = FastMath.pow(expectedStdDev, 2);

        assertEquals(expectedMean, actualMean, 0.00001);
        assertEquals(expectedStdDev, Math.sqrt(FastMath.pow(actualMean, 2) + FastMath.pow(actualStdDev, 2) - meanSquare), 0.0001);
    }

    @Test
    public void testNextInt() {
        int expected = 12345;
        int actual = generator.nextInt();
        assertEquals(expected, actual);

        expected = 0;
        actual = generator.nextInt();
        assertEquals(expected, actual);

        expected = Integer.MAX_VALUE;
        actual = generator.nextInt();
        assertEquals(expected, actual);

        expected = Integer.MIN_VALUE;
        actual = generator.nextInt();
        assertEquals(expected, actual);

        expected = 0x7FFF_FFFF;
        actual = generator.nextInt();
        assertEquals(expected, actual);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testNextIntNegative() {
        generator.nextInt(-1);
    }

    @Test
    public void testNextIntPowerOfTwo() {
        int expected = 1 << 16;
        int actual = generator.nextInt();
        assertEquals(expected, actual);
    }

    @Test
    public void testNextLong() {
        long expected = 0x1234567890ABCDEF;
        long actual = generator.nextLong();
        assertEquals(expected, actual);

        expected = 0;
        actual = generator.nextLong();
        assertEquals(expected, actual);

        expected = Long.MAX_VALUE;
        actual = generator.nextLong();
        assertEquals(expected, actual);

        expected = Long.MIN_VALUE;
        actual = generator.nextLong();
        assertEquals(expected, actual);
    }

    @Test
    public void testClear() {
        double expectedMean = 0.123456789;
        double expectedStdDev = 0.0123456789;
        double actualMean = generator.nextGaussian();
        double actualStdDev = generator.nextGaussian();

        generator.clear();

        double newActualMean = generator.nextGaussian();
        double newActualStdDev = generator.nextGaussian();

        assertEquals(expectedMean, actualMean, 0.00001);
        assertEquals(expectedStdDev, Math.sqrt(FastMath.pow(actualMean, 2) + FastMath.pow(actualStdDev, 2) - FastMath.pow(newActualMean, 2) - FastMath.pow(newActualStdDev, 2)), 0.0001);
    }

}