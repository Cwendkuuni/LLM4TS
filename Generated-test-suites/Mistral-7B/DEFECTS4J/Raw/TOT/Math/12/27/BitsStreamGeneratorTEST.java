package org.apache.commons.math3.random;

import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests for {@link BitsStreamGenerator}.
 */
public class BitsStreamGeneratorTest {

    /** The instance under test. */
    private BitsStreamGenerator generator;

    /**
     * Sets up the test.
     */
    @org.junit.Before
    public void setUp() {
        generator = new MersenneTwisterFastRandomGenerator();
    }

    /**
     * Tests {@link BitsStreamGenerator#nextBoolean()}.
     */
    @Test
    public void testNextBoolean() {
        for (int i = 0; i < 1000; i++) {
            boolean result = generator.nextBoolean();
            assertTrue(result || !result);
        }
    }

    /**
     * Tests {@link BitsStreamGenerator#nextBytes(byte[])}.
     */
    @Test
    public void testNextBytes() {
        final byte[] expected = new byte[10];
        generator.nextBytes(expected);
        for (byte b : expected) {
            assertTrue(b >= 0 && b <= 255);
        }
        // check that the same seed produces the same sequence
        byte[] bytes = new byte[10];
        generator.nextBytes(bytes);
        generator.setSeed(bytes);
        generator.nextBytes(expected);
        assertArrayEquals(expected, bytes);
    }

    /**
     * Tests {@link BitsStreamGenerator#nextDouble()}.
     */
    @Test
    public void testNextDouble() {
        for (int i = 0; i < 1000; i++) {
            double result = generator.nextDouble();
            assertTrue(result > -1.0 && result < 1.0);
            if (Double.isFinite(result)) {
                assertTrue(Precision.isClose(result, result, 1.0e-10));
            }
        }
    }

    /**
     * Tests {@link BitsStreamGenerator#nextFloat()}.
     */
    @Test
    public void testNextFloat() {
        for (int i = 0; i < 1000; i++) {
            float result = generator.nextFloat();
            assertTrue(result > -1.0f && result < 1.0f);
            assertTrue(Float.isFinite(result));
        }
    }

    /**
     * Tests {@link BitsStreamGenerator#nextGaussian()}.
     */
    @Test
    public void testNextGaussian() {
        for (int i = 0; i < 1000; i++) {
            double mean  = 0.0;
            double stdDev = 1.0;
            double result = generator.nextGaussian(mean, stdDev);
            assertTrue(result > mean - 2 * stdDev && result < mean + 2 * stdDev);
        }
    }

    /**
     * Tests {@link BitsStreamGenerator#nextInt()}.
     */
    @Test
    public void testNextInt() {
        for (int i = 0; i < 1000; i++) {
            int result = generator.nextInt();
            assertTrue(result >= 0 && result <= Integer.MAX_VALUE);
        }
        for (int test : new int[]{-1, Integer.MIN_VALUE, Integer.MAX_VALUE + 1}) {
            try {
                generator.nextInt(test);
                fail("IllegalArgumentException expected");
            } catch (IllegalArgumentException e) {
                // expected
            }
        }
    }

    /**
     * Tests {@link BitsStreamGenerator#nextInt(int)}.
     */
    @Test
    public void testNextIntWithLimit() {
        for (int test = 0; test < 100; test++) {
            for (int n = 1; n <= 10; n++) {
                int result = generator.nextInt(n);
                assertTrue(result >= 0 && result < n);
            }
        }
    }

    /**
     * Tests {@link BitsStreamGenerator#nextLong()}.
     */
    @Test
    public void testNextLong() {
        for (int i = 0; i < 1000; i++) {
            long result = generator.nextLong();
            assertTrue(result >= Long.MIN_VALUE && result <= Long.MAX_VALUE);
        }
    }

    /**
     * Tests {@link BitsStreamGenerator#setSeed(int)}.
     */
    @Test
    public void testSetSeedWithInt() {
        final int seed = 12345;
        generator.setSeed(seed);
        byte[] bytes = new byte[8];
        generator.nextBytes(bytes);
        long expected = 0x123456789012345L;
        long result = 0;
        for (int i = 0; i < 8; i++) {
            result <<= 8;
            result |= bytes[i] & 0xff;
        }
        assertEquals(expected, result);
    }

    /**
     * Tests {@link BitsStreamGenerator#setSeed(int[])}.
     */
    @Test
    public void testSetSeedWithIntArray() {
        final int[] seeds = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        generator.setSeed(seeds);
        byte[] bytes = new byte[10 * 8];
        generator.nextBytes(bytes);
        long expected = 0;
        for (int i = 0; i < seeds.length; i++) {
            expected <<= 32;
            expected |= seeds[i] & 0xffffffffL;
            expected <<= 8 * (40 - 4 * (i + 1));
        }
        long result = 0;
        for (int i = 0; i < 10 * 8; i++) {
            result <<= 8;
            result |= bytes[i] & 0xff;
        }
        assertEquals(expected, result);
    }

    /**
     * Tests {@link BitsStreamGenerator#setSeed(long)}.
     */
    @Test
    public void testSetSeedWithLong() {
        final long seed = 123456789012345L;
        generator.setSeed(seed);
        byte[] bytes = new byte[8];
        generator.nextBytes(bytes);
        long expected = seed;
        long result = 0;
        for (int i = 0; i < 8; i++) {
            result <<= 8;
            result |= bytes[i] & 0xff;
        }
        assertEquals(expected, result);
    }

    /**
     * Tests {@link BitsStreamGenerator#clear()}.
     */
    @Test
    public void testClear() {
        generator.nextGaussian();
        generator.clear();
        assertTrue(Double.isNaN(generator.nextGaussian()));
    }

}