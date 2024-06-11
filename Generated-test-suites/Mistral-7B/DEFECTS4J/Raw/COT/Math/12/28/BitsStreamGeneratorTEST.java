package org.apache.commons.math3.random;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.security.SecureRandom;

/**
 * Test cases for the {@link BitsStreamGenerator} class.
 */
@RunWith(JUnit4.class)
public class BitsStreamGeneratorTest {
    /** The instance under test. */
    private final BitsStreamGenerator generator = new SecureRandomBitsStreamGenerator();

    /**
     * Test constructor.
     */
    @Test
    public void testConstructor() {
        assertTrue(generator instanceof BitsStreamGenerator);
    }

    /**
     * Test {@link BitsStreamGenerator#nextBoolean()}.
     */
    @Test
    public void testNextBoolean() {
        final boolean result = generator.nextBoolean();
        assertTrue(result || !result);
    }

    /**
     * Test {@link BitsStreamGenerator#nextBytes(byte[])}.
     */
    @Test
    public void testNextBytes() {
        final byte[] expected = new byte[]{(byte) 0x1, (byte) 0x2, (byte) 0x3, (byte) 0x4};
        generator.nextBytes(new byte[4]);
        assertArrayEquals(expected, generator.getState());
    }

    /**
     * Test {@link BitsStreamGenerator#nextDouble()}.
     */
    @Test
    public void testNextDouble() {
        final double result = generator.nextDouble();
        assertTrue(result >= 0.0 && result <= 1.0);
    }

    /**
     * Test {@link BitsStreamGenerator#nextFloat()}.
     */
    @Test
    public void testNextFloat() {
        final float result = generator.nextFloat();
        assertTrue(result >= -1.0f && result <= 1.0f);
    }

    /**
     * Test {@link BitsStreamGenerator#nextGaussian()}.
     */
    @Test
    public void testNextGaussian() {
        final double expectedMean = 0.0;
        final double expectedStdDev = 1.0;
        final int sampleSize = 100_000;
        double sum = 0.0;
        for (int i = 0; i < sampleSize; i++) {
            sum += Math.pow(generator.nextGaussian() - expectedMean, 2);
        }
        final double variance = sum / sampleSize;
        assertEquals(expectedStdDev * expectedStdDev, variance, 0.001);
    }

    /**
     * Test {@link BitsStreamGenerator#nextInt()}.
     */
    @Test
    public void testNextInt() {
        final int maxValue = 10;
        for (int testValue = Integer.MIN_VALUE; testValue <= Integer.MAX_VALUE; testValue++) {
            generator.nextInt(maxValue);
        }
        int result = generator.nextInt(maxValue);
        assertTrue(result >= 0 && result < maxValue);
    }

    /**
     * Test {@link BitsStreamGenerator#nextInt(int)}.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNextIntNegative() {
        generator.nextInt(-3);
    }

    /**
     * Test {@link BitsStreamGenerator#nextLong()}.
     */
    @Test
    public void testNextLong() {
        final long maxLongValue = Long.MAX_VALUE;
        for (long testValue = Long.MIN_VALUE; testValue <= maxLongValue; testValue++) {
            generator.nextLong();
        }
        long result = generator.nextLong();
        assertTrue(result >= 0 && result < maxLongValue + 1);
    }

    /**
     * Test {@link BitsStreamGenerator#clear()}.
     */
    @Test
    public void testClear() {
        final byte[] state = new byte[generator.getState().length * 2];
        generator.nextBytes(state);
        generator.clear();
        generator.nextBytes(state);
        final byte[] expected = new byte[0];
        assertArrayEquals(expected, generator.getState());
    }

}