package org.apache.commons.math3.random;

import static org.junit.Assert.*;

import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.random.BitsStreamGenerator;
import org.apache.commons.math3.random.MersenneTwisterFast;
import org.apache.commons.math3.util.Precision;

import org.junit.Before;
import org.junit.Test;

/**
 * Tests for {@link BitsStreamGenerator} class.
 */
public class BitsStreamGeneratorTest {
    private BitsStreamGenerator generator;

    @Before
    public void setUp() {
        generator = new MersenneTwisterFast();
    }

    /**
     * Test for {@link BitsStreamGenerator#nextBoolean()} method.
     */
    @Test
    public void testNextBoolean() {
        for (int i = 0; i < 1000; i++) {
            boolean result = generator.nextBoolean();
            assertTrue("Expected true or false, but got: " + result, result == (Math.random() > 0.5));
        }
    }

    /**
     * Test for {@link BitsStreamGenerator#nextBytes(byte[])} method.
     */
    @Test
    public void testNextBytes() {
        final byte[] expected = new byte[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        final byte[] result   = new byte[expected.length];

        generator.nextBytes(result);
        assertArrayEquals("Expected and received arrays to be equal", expected, result);
    }

    /**
     * Test for {@link BitsStreamGenerator#nextDouble()} method.
     */
    @Test
    public void testNextDouble() {
        for (int i = 0; i < 1000; i++) {
            double result = generator.nextDouble();
            assertTrue("Expected a number between 0 and 1, but got: " + result, result >= 0 && result <= 1);
            assertTrue("Expected a number close to a given value, but got: " + result + " instead of " + 0.5,
                    Precision.abs(result - 0.5) < 1e-7);
        }
    }

    /**
     * Test for {@link BitsStreamGenerator#nextFloat()} method.
     */
    @Test
    public void testNextFloat() {
        for (int i = 0; i < 1000; i++) {
            float result = generator.nextFloat();
            assertTrue("Expected a number between 0 and 1, but got: " + result, result >= 0 && result <= 1);
            assertTrue("Expected a number close to a given value, but got: " + result + " instead of " + 0.5f,
                    Precision.abs(result - 0.5f) < 1e-5f);
        }
    }

    /**
     * Test for {@link BitsStreamGenerator#nextGaussian()} method.
     * <p>The method should generate numbers with zero mean and standard deviation of 1.</p>
     */
    @Test
    public void testNextGaussian() {
        for (int i = 0; i < 1000; i++) {
            double result = generator.nextGaussian();
            double mean   = 0;
            double stdDev = 1;
            assertTrue("Expected normal distribution, but got: " + result, Precision.abs(result - mean) < stdDev);
        }
    }

    /**
     * Test for {@link BitsStreamGenerator#nextInt()} method.
     */
    @Test
    public void testNextInt() {
        for (int i = 0; i < 100; i++) {
            for (int j = 0; j < 1000; j++) {
                int result = generator.nextInt();
                int expected = (int) (Math.random() * Integer.MAX_VALUE);
                assertEquals("Expected and received integers to be equal", expected, result);
            }
        }
    }

    /**
     * Test for {@link BitsStreamGenerator#nextInt(int)} method.
     */
    @Test
    public void testNextIntWithBound() {
        for (int i = 0; i < 100; i++) {
            for (int j = 0; j < 1000; j++) {
                int bound = 10;
                int result = generator.nextInt(bound);
                int expected = (int) (Math.random() * bound);
                assertEquals("Expected and received integers to be equal", expected, result);
            }
        }
    }

    /**
     * Test for {@link BitsStreamGenerator#nextLong()} method.
     */
    @Test
    public void testNextLong() {
        for (int i = 0; i < 100; i++) {
            long result = generator.nextLong();
            long expected = (long) (Math.random() * Long.MAX_VALUE);
            assertEquals("Expected and received longs to be equal", expected, result);
        }
    }

    /**
     * Test for {@link BitsStreamGenerator#clear()} method.
     */
    @Test
    public void testClear() {
        int count = 0;
        final int expectedValue = 1;
        int result;

        for (int i = 0; i < 1000; i++) {
            generator.nextBoolean();
            result = generator.nextInt();
            if (result == expectedValue) {
                count++;
            }
            generator.clear();
        }

        // After clearing, nextInt() should no longer return the expected value
        for (int i = 0; i < 1000; i++) {
            result = generator.nextInt();
            assertNotEquals("Expected and received integers to be different", expectedValue, result);
        }

        // Clearing should not affect nextBoolean()
        for (int i = 0; i < 1000; i++) {
            boolean resultBoolean = generator.nextBoolean();
            assertTrue("Expected true or false, but got: " + resultBoolean, resultBoolean == (Math.random() > 0.5));
        }
    }

    /**
     * Test for {@link BitsStreamGenerator#nextBytes(byte[])} method with large arrays.
     */
    @Test
    public void testNextBytesWithLargeArrays() {
        final int size = 100000;
        byte[] result = new byte[size];

        generator.nextBytes(result);

        // Check for correct distribution
        for (int i = 0; i < size; i++) {
            int bitCount = Integer.bitCount(result[i] & 0xFF);
            // The distribution is uniform on the set of numbers with
            // k bits set, thus the expected number of occurrences is 2^k / 256
            double expected = Math.pow(0.5, bitCount) / 256.0;
            // The actual number of occurrences is the number of bytes with this number of set bits
            int occurrences = 0;
            for (int j = 0; j < size; j++) {
                if (Integer.bitCount(result[j] & 0xFF) == bitCount) {
                    occurrences++;
                }
            }
            // Check that the observed distribution is close to the expected one
            double tolerance = Math.pow(0.1, bitCount);
            assertTrue("Expected a number close to " + expected + ", but got " + occurrences + " instead",
                    Precision.abs(occurrences / (double) size - expected) < tolerance);
        }
    }

    /**
     * Test for {@link BitsStreamGenerator#nextDouble()} method with negative seed.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNegativeSeed() {
        generator.setSeed(-1);
    }

    /**
     * Test for {@link BitsStreamGenerator#nextDouble()} method with large seed.
     */
    @Test
    public void testLargeSeed() {
        long largeSeed = Long.MAX_VALUE + 1;
        generator.setSeed(largeSeed);
        for (int i = 0; i < 100_000; i++) {
            double result = generator.nextDouble();
            // The generator should behave as if it were seeded with a small seed
            // The sequence of numbers generated should be the same as if the seed were small
            double expected = Math.random();
            assertEquals("Expected and received numbers to be equal", expected, result, 1e-15);
        }
    }
}