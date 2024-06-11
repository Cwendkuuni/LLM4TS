package org.apache.commons.math3.random;

import org.junit.Test;
import static org.junit.Assert.*;

import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.random.BitsStreamGenerator;
import org.apache.commons.math3.util.FastMath;

/**
 * Test class for {@link BitsStreamGenerator}.
 */
public class BitsStreamGeneratorTest {

    private final BitsStreamGenerator generator = new MyBitsStreamGenerator();

    /**
     * Test method for {@link BitsStreamGenerator#BitsStreamGenerator()}.
     */
    @Test
    public void testConstructor() {
        assertNotNull(generator);
    }

    /**
     * Test method for {@link BitsStreamGenerator#setSeed(int)}.
     */
    @Test
    public void testSetSeedInt() {
        int seed = 12345;
        generator.setSeed(seed);
        assertEquals(seed, generator.nextInt());
    }

    /**
     * Test method for {@link BitsStreamGenerator#setSeed(int[])}.
     */
    @Test
    public void testSetSeedIntArray() {
        int[] seeds = {123, 456, 789};
        generator.setSeed(seeds);
        int expectedSeed = seeds[0];
        for (int i = 1; i < seeds.length; i++) {
            expectedSeed = (expectedSeed << 32) | seeds[i];
        }
        assertEquals(expectedSeed, generator.nextLong());
    }

    /**
     * Test method for {@link BitsStreamGenerator#setSeed(long)}.
     */
    @Test
    public void testSetSeedLong() {
        long seed = 123456789012345L;
        generator.setSeed(seed);
        assertEquals(seed, generator.nextLong());
    }

    /**
     * Test method for {@link BitsStreamGenerator#next(int)}.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testNextIntAbstract() {
        generator.next(1);
    }

    /**
     * Test method for {@link BitsStreamGenerator#nextBoolean()}.
     */
    @Test
    public void testNextBoolean() {
        boolean expected = true;
        boolean actual = generator.nextBoolean();
        assertEquals(expected, actual);
    }

    /**
     * Test method for {@link BitsStreamGenerator#nextBytes(byte[])}.
     */
    @Test
    public void testNextBytes() {
        byte[] expected = new byte[]{(byte) 0x01, (byte) 0x02, (byte) 0x03, (byte) 0x04};
        byte[] actual = new byte[4];
        generator.nextBytes(actual);
        assertArrayEquals(expected, actual);
    }

    /**
     * Test method for {@link BitsStreamGenerator#nextDouble()}.
     */
    @Test
    public void testNextDouble() {
        double expected = 0.123456789;
        double actual = generator.nextDouble();
        assertEquals(expected, actual, 0.0000001);
    }

    /**
     * Test method for {@link BitsStreamGenerator#nextFloat()}.
     */
    @Test
    public void testNextFloat() {
        float expected = 0.123456f;
        float actual = generator.nextFloat();
        assertEquals(expected, actual, 0.00001f);
    }

    /**
     * Test method for {@link BitsStreamGenerator#nextGaussian()}.
     */
    @Test
    public void testNextGaussian() {
        double expectedMean = 0.0;
        double expectedStdDev = 1.0;
        double actualMean = 0.0;
        double actualStdDev = 0.0;
        int numSamples = 10000;
        for (int i = 0; i < numSamples; i++) {
            double sample = generator.nextGaussian();
            actualMean += sample;
        }
        actualMean /= numSamples;
        for (int i = 0; i < numSamples; i++) {
            double sample = generator.nextGaussian();
            double diff = sample - expectedMean;
            actualStdDev += diff * diff;
        }
        actualStdDev = Math.sqrt(actualStdDev / numSamples);
        assertEquals(expectedMean, actualMean, 0.0001);
        assertEquals(expectedStdDev, actualStdDev, 0.0001);
    }

    /**
     * Test method for {@link BitsStreamGenerator#nextInt()}.
     */
    @Test
    public void testNextInt() {
        int maxValue = Integer.MAX_VALUE;
        int minValue = Integer.MIN_VALUE;
        int expected = (int) (Math.random() * (maxValue - minValue + 1) + minValue);
        int actual = generator.nextInt();
        assertEquals(expected, actual);
    }

    /**
     * Test method for {@link BitsStreamGenerator#nextInt(int)}.
     */
    @Test
    public void testNextIntBoundary() {
        int maxValue = Integer.MAX_VALUE;
        int minValue = Integer.MIN_VALUE;
        for (int n = 0; n < 10; n++) {
            int expected = (int) (Math.random() * (maxValue - minValue + 1) + minValue);
            try {
                int actual = generator.nextInt(n);
                fail("Expected exception: IllegalArgumentException");
            } catch (IllegalArgumentException e) {
                // expected
            }
            int actual = generator.nextInt();
            int actualValue = actual % n;
            assertEquals(expected, actualValue);
        }
    }

    /**
     * Test method for {@link BitsStreamGenerator#nextLong()}.
     */
    @Test
    public void testNextLong() {
        long maxLongValue = Long.MAX_VALUE;
        long minLongValue = Long.MIN_VALUE;
        long expected = (long) (Math.random() * (maxLongValue - minLongValue + 1) + minLongValue);
        long actual = generator.nextLong();
        assertEquals(expected, actual);
    }

    /**
     * Test method for {@link BitsStreamGenerator#clear()}.
     */
    @Test
    public void testClear() {
        generator.nextGaussian();
        generator.clear();
        assertFalse(generator.nextGaussianIsSet());
    }

    /**
     * Test method for {@link BitsStreamGenerator#nextGaussianIsSet()}.
     */
    @Test
    public void testNextGaussianIsSet() {
        generator.nextGaussian();
        assertTrue(generator.nextGaussianIsSet());
        generator.clear();
        assertFalse(generator.nextGaussianIsSet());
    }

    /**
     * Test method for edge cases and exceptions handling.
     */
    @Test(expected = NotStrictlyPositiveException.class)
    public void testSetSeedNegativeInt() {
        generator.setSeed(-1);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testSetSeedNegativeLong() {
        generator.setSeed(-1L);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testNextIntNegativeNumber() {
        int n = -1;
        try {
            generator.nextInt(n);
            fail("Expected exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testNextIntNegativeNumberArray() {
        int[] n = {-1};
        try {
            generator.setSeed(n);
            fail("Expected exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testNextIntNegativeNumberLong() {
        long n = -1L;
        try {
            generator.setSeed(n);
            fail("Expected exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetSeedNullArray() {
        int[] seeds = null;
        try {
            generator.setSeed(seeds);
            fail("Expected exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNextIntArrayNull() {
        int[] array = null;
        try {
            generator.nextBytes(array);
            fail("Expected exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }
}

class MyBitsStreamGenerator extends BitsStreamGenerator {

    @Override
    protected int next(int bits) {
        return (int) (Math.random() * ((1 << bits) - 1));
    }

    boolean nextGaussianIsSet = false;

    @Override
    protected double nextGaussian() {
        nextGaussianIsSet = true;
        double x = Math.random();
        double y = Math.random();
        double alpha = 2 * FastMath.PI * x;
        double r = Math.sqrt(-2 * FastMath.log(y));
        return r * FastMath.cos(alpha);
    }
}