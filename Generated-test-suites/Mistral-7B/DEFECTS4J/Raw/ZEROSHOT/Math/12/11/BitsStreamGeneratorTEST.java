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

    /** Test instance. */
    private BitsStreamGenerator generator;

    /**
     * Sets up the test environment.
     */
    @SuppressWarnings("PMD.MissingAssertion")
    @Before
    public void setUp() {
        generator = new BitsStreamGenerator();
    }

    /**
     * Tests the {@link BitsStreamGenerator#nextBoolean()} method.
     */
    @Test
    public void testNextBoolean() {
        boolean result;
        for (int i = 0; i < 100; i++) {
            result = generator.nextBoolean();
            assertTrue(result || !result);
        }
    }

    /**
     * Tests the {@link BitsStreamGenerator#nextBytes(byte[])} method.
     */
    @Test
    public void testNextBytes() {
        byte[] bytes = new byte[10];
        generator.nextBytes(bytes);
        for (byte b : bytes) {
            assertTrue(b >= 0 && b <= 255);
        }
    }

    /**
     * Tests the {@link BitsStreamGenerator#nextDouble()} method.
     */
    @Test
    public void testNextDouble() {
        double result;
        for (int i = 0; i < 100; i++) {
            result = generator.nextDouble();
            assertTrue(result >= 0.0 && result <= 1.0);
        }
    }

    /**
     * Tests the {@link BitsStreamGenerator#nextFloat()} method.
     */
    @Test
    public void testNextFloat() {
        float result;
        for (int i = 0; i < 100; i++) {
            result = generator.nextFloat();
            assertTrue(Float.isFinite(result));
        }
    }

    /**
     * Tests the {@link BitsStreamGenerator#nextGaussian()} method.
     */
    @Test
    public void testNextGaussian() {
        double result;
        double mean = 0.0;
        double stdDev = 1.0;
        for (int i = 0; i < 1000; i++) {
            result = generator.nextGaussian();
            assertEquals(mean, result, stdDev);
        }
    }

    /**
     * Tests the {@link BitsStreamGenerator#nextInt()} method.
     */
    @Test
    public void testNextInt() {
        int result;
        for (int i = 0; i < 100; i++) {
            result = generator.nextInt();
            assertTrue(result >= 0 && result <= Integer.MAX_VALUE);
        }
    }

    /**
     * Tests the {@link BitsStreamGenerator#nextInt(int)} method.
     */
    @Test
    public void testNextIntWithLimit() {
        int result;
        for (int limit = 1; limit <= 5; limit++) {
            for (int i = 0; i < 10; i++) {
                result = generator.nextInt(limit);
                assertTrue(result >= 0 && result < limit);
            }
        }
    }

    /**
     * Tests the {@link BitsStreamGenerator#nextLong()} method.
     */
    @Test
    public void testNextLong() {
        long result;
        for (int i = 0; i < 100; i++) {
            result = generator.nextLong();
            assertTrue(result >= Long.MIN_VALUE && result <= Long.MAX_VALUE);
        }
    }

    /**
     * Tests the {@link BitsStreamGenerator#clear()} method.
     */
    @Test
    public void testClear() {
        boolean result;
        generator.nextGaussian();
        result = generator.nextBoolean();
        assertFalse(result);
        generator.clear();
        result = generator.nextBoolean();
        assertTrue(result);
    }

    /**
     * Tests the {@link BitsStreamGenerator#setSeed(int)} method.
     */
    @Test
    public void testSetSeedInt() {
        final int seed = 12345;
        generator.setSeed(seed);
        final int result1 = generator.nextInt();
        final int result2 = generator.nextInt();
        final int result3 = generator.nextInt();
        int seedCopy = seed;
        for (int i = 0; i < 10; i++) {
            generator.nextInt();
            seedCopy = generator.nextInt();
        }
        assertEquals(seedCopy, result1);
        assertEquals(seedCopy, result2);
        assertEquals(seedCopy, result3);
    }

    /**
     * Tests the {@link BitsStreamGenerator#setSeed(int[])} method.
     */
    @Test
    public void testSetSeedIntArray() {
        final int[] seed = new int[] { 1, 2, 3, 4 };
        generator.setSeed(seed);
        final int result1 = generator.nextInt();
        final int result2 = generator.nextInt();
        final int result3 = generator.nextInt();
        final int[] seedCopy = seed.clone();
        for (int i = 0; i < seed.length; i++) {
            generator.nextInt();
            seedCopy[i] = generator.nextInt();
        }
        assertArrayEquals(seedCopy, new int[] { result1, result2, result3 });
    }

    /**
     * Tests the {@link BitsStreamGenerator#setSeed(long)} method.
     */
    @Test
    public void testSetSeedLong() {
        final long seed = 9876543210987654321L;
        generator.setSeed(seed);
        final long result1 = generator.nextLong();
        final long result2 = generator.nextLong();
        final long result3 = generator.nextLong();
        long seedCopy = seed;
        for (int i = 0; i < 10; i++) {
            generator.nextLong();
            seedCopy = generator.nextLong();
        }
        assertEquals(seedCopy, result1);
        assertEquals(seedCopy, result2);
        assertEquals(seedCopy, result3);
    }

    /**
     * Tests the {@link BitsStreamGenerator#next(int)} method.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testNextIntWithUnsupportedOverriding() {
        generator.next(32);
    }
}