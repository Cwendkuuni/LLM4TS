package org.apache.commons.math3.random;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.random.BitsStreamGenerator;
import org.apache.commons.math3.random.MersenneTwisterFast;
import org.apache.commons.math3.util.FastMath;
import org.junit.Before;
import org.junit.Test;

/**
 * Test cases for {@link BitsStreamGenerator}.
 */
public class BitsStreamGeneratorTest {

    private BitsStreamGenerator generator;

    @Before
    public void setUp() {
        generator = new MersenneTwisterFast();
    }

    @Test
    public void testSetSeedMethods() {
        generator.setSeed(1);
        generator.setSeed(new int[] {1, 2, 3, 4, 5});
        generator.setSeed(0x1234567890123456L);

        // SetSeed methods should not change the random number generation
        assertEquals(nextByte(), nextByte());
        assertEquals(nextDouble(), nextDouble());
        assertEquals(nextGaussian(), nextGaussian());
        assertEquals(nextInt(), nextInt());
        assertEquals(nextLong(), nextLong());
    }

    @Test
    public void testNextBoolean() {
        assertEquals(false, nextBoolean());
        assertEquals(true,  nextBoolean());
    }

    @Test
    public void testNextBytes() {
        final byte[] bytes1 = new byte[10];
        final byte[] bytes2 = new byte[10];
        nextBytes(bytes1);
        nextBytes(bytes2);

        assertArrayEquals(bytes1, bytes2);

        final byte[] bytes3 = new byte[1];
        nextBytes(bytes3);
        assertEquals(bytes3.length, 1);
    }

    @Test
    public void testNextDouble() {
        final double min = FastMath.nextDown(Double.MIN_VALUE);
        final double max = FastMath.nextUp(Double.MAX_VALUE);

        assertTrue(nextDouble() >= min);
        assertTrue(nextDouble() <= max);
    }

    @Test
    public void testNextFloat() {
        final float min = Float.MIN_VALUE;
        final float max = Float.MAX_VALUE;

        assertTrue(nextFloat() >= min);
        assertTrue(nextFloat() <= max);
    }

    @Test
    public void testNextGaussian() {
        final double mean  = 0.5;
        final double stdDev = 0.25;

        assertEquals(mean, mean + nextGaussian(), 0.001);
        assertEquals(mean, mean + nextGaussian(), 0.001);

        double sum = 0.0;
        double n = 1000;
        for (int i = 0; i < n; i++) {
            sum += Math.pow(nextGaussian() - mean, 2);
        }
        assertEquals(stdDev * stdDev * n, sum, 0.001 * stdDev * stdDev * n);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNextIntNegative() {
        generator.setSeed(0);
        generator.nextInt(-1);
    }

    @Test
    public void testNextInt() {
        final int minInt = Integer.MIN_VALUE;
        final int maxInt = Integer.MAX_VALUE;

        int value = nextInt();
        assertTrue(value >= minInt);
        assertTrue(value <= maxInt);

        value = nextInt(10);
        assertTrue(value >= 0 && value <= 9);

        value = nextInt(10);
        value = nextInt(10);
        assertNotSame(value, nextInt(10));
    }

    @Test
    public void testNextLong() {
        final long minLong = Long.MIN_VALUE;
        final long maxLong = Long.MAX_VALUE;

        assertTrue(nextLong() >= minLong);
        assertTrue(nextLong() <= maxLong);
    }

    @Test
    public void testClear() {
        double gaussian1 = nextGaussian();
        double gaussian2 = nextGaussian();

        generator.clear();

        assertNotSame(gaussian1, nextGaussian());
    }

    private byte nextByte() {
        final byte[] bytes = new byte[1];
        nextBytes(bytes);
        return bytes[0];
    }
}