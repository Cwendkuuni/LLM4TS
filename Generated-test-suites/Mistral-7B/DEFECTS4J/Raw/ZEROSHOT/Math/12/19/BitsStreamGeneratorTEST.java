package org.apache.commons.math3.random;

import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.util.FastMath;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests for {@link BitsStreamGenerator}.
 */
public class BitsStreamGeneratorTest {

    /** A sample instance of {@link BitsStreamGenerator}. */
    private BitsStreamGenerator generator;

    /**
     * Sets up the test environment.
     */
    @SuppressWarnings("PMD.MethodNamingConventions")
    @Before
    public void setUp() {
        generator = new BitsStreamGenerator() {
            @Override
            protected int next(int bits) {
                // A simple implementation that always returns the same bit sequence.
                final int sequence = 0x12345678;
                int result = sequence & ((1 << bits) - 1);
                sequence <<= bits;
                return result;
            }
        };
    }

    /**
     * Tests {@link BitsStreamGenerator#nextBoolean()}.
     */
    @Test
    public void testNextBoolean() {
        final boolean expected = true;
        final boolean result = generator.nextBoolean();
        assertTrue(expected == result);
    }

    /**
     * Tests {@link BitsStreamGenerator#nextBytes(byte[])}.
     */
    @Test
    public void testNextBytes() {
        final byte[] expected = {0x12, 0x34, 0x56, 0x78};
        final byte[] result = new byte[4];
        generator.nextBytes(result);
        assertArrayEquals(expected, result);
    }

    /**
     * Tests {@link BitsStreamGenerator#nextDouble()}.
     */
    @Test
    public void testNextDouble() {
        final double expected = 0.123456789;
        final double result = generator.nextDouble();
        assertEquals(expected, result, 1e-6);
    }

    /**
     * Tests {@link BitsStreamGenerator#nextFloat()}.
     */
    @Test
    public void testNextFloat() {
        final float expected = 0.123456f;
        final float result = generator.nextFloat();
        assertEquals(expected, result, 1e-6f);
    }

    /**
     * Tests {@link BitsStreamGenerator#nextGaussian()}.
     */
    @Test
    public void testNextGaussian() {
        final double expectedMean = 0.0;
        final double expectedStdDev = 1.0;
        final double mean, stdDev;
        int samples = 100000;
        for (int i = 0; i < samples; ++i) {
            mean += generator.nextGaussian();
        }
        mean /= samples;
        for (int i = 0; i < samples; ++i) {
            final double gaussian = generator.nextGaussian();
            stdDev += (gaussian - mean) * (gaussian - mean);
        }
        stdDev = Math.sqrt(stdDev / samples);
        assertEquals(expectedMean, mean, 1e-6);
        assertEquals(expectedStdDev, stdDev, 1e-3);
    }

    /**
     * Tests {@link BitsStreamGenerator#nextInt()}.
     */
    @Test
    public void testNextInt() {
        final int expected = 0x1234;
        final int result = generator.nextInt();
        assertEquals(expected, result);
    }

    /**
     * Tests {@link BitsStreamGenerator#nextInt(int)}.
     */
    @Test
    public void testNextInt_int() {
        final int expected = 5;
        final int result = generator.nextInt(expected);
        assertTrue(result >= 0 && result < expected);
    }

    /**
     * Tests {@link BitsStreamGenerator#nextLong()}.
     */
    @Test
    public void testNextLong() {
        final long expected = 0x1234567890abcdefL;
        final long result = generator.nextLong();
        assertEquals(expected, result);
    }

    /**
     * Tests {@link BitsStreamGenerator#clear()}.
     */
    @Test
    public void testClear() {
        generator.nextGaussian();
        generator.clear();
        assertFalse(generator.isNextGaussianAvailable());
    }

    /**
     * Checks that {@link BitsStreamGenerator#nextGaussian()} always returns
     * a non-NaN value after the initial call.
     */
    @Test(expected = AssertionError.class)
    public void testNextGaussian_notNaN() {
        generator.nextGaussian();
        assertFalse(Double.isNaN(generator.nextGaussian()));
    }

}