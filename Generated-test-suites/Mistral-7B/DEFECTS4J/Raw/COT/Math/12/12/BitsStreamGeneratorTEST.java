package org.apache.commons.math3.random;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Random;

import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.util.FastMath;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;

/**
 * Test class for {@link BitsStreamGenerator}.
 */
public class BitsStreamGeneratorTest {

    /** Tested instance of {@link BitsStreamGenerator}. */
    private BitsStreamGenerator generator;

    /**
     * Sets up a new test instance.
     */
    @SuppressWarnings("PMD.SetMagicNumber")
    @Before
    public void setUp() {
        generator = new BitsStreamGenerator();
    }

    // ------------------ Abstract Methods ------------------

    /**
     * Test for {@link BitsStreamGenerator#next(int)}.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testNextAbstract() {
        try {
            generator.next(0);
        } catch (IllegalArgumentException e) {
            throw new AssertionError("Unexpected exception", e);
        }
        generator.next(1); // Call to check that no exception is thrown

        // Test that calling this method directly throws an exception
        generator.getClass().getDeclaredMethod("next", int.class).setAccessible(true);
        generator.getClass().getDeclaredMethod("next", int.class).invoke(generator, 2);
    }

    // ------------------------ SetSeed ------------------------

    /**
     * Test for {@link BitsStreamGenerator#setSeed(int)}.
     */
    @Test
    public void testSetSeedInt() {
        generator.setSeed(0);
        assertEquals(0, generator.nextInt());
    }

    /**
     * Test for {@link BitsStreamGenerator#setSeed(int[])}.
     */
    @Test
    public void testSetSeedIntArray() {
        int[] seed = { 0, 1, 1, 2, 1, 5, 7, 12, 17, 21, 31, 41, 42, 53, 83, 87 };
        generator.setSeed(seed);
        int expectedSeed = 1263833466;
        for (int i = 0; i < seed.length; ++i) {
            assertEquals(seed[i], (int) (generator.next(32) >> (32 - Integer.numberOfLeadingZeros(seed[i]))) & 0xFFFFFFFF);
        }
        assertEquals(expectedSeed, generator.nextInt());
    }

    /**
     * Test for {@link BitsStreamGenerator#setSeed(long)}.
     */
    @Test
    public void testSetSeedLong() {
        final long seed = 0xABCDEF1234567890L;
        generator.setSeed(seed);
        long expectedSeed = seed;
        for (int i = 0; i < 10; ++i) {
            assertEquals(expectedSeed & 0xFFFFFFFFL, (long) (generator.next(32) >> 32));
            expectedSeed = (expectedSeed << 32) | generator.next(32);
        }
        assertEquals(generator.nextLong(), expectedSeed);
    }

    // ------------------------ nextBoolean ------------------------

    /**
     * Test for {@link BitsStreamGenerator#nextBoolean()}.
     */
    @Test
    public void testNextBoolean() {
        for (int test = 0; test < 100; ++test) {
            assertTrue(generator.nextBoolean());
            assertTrue(generator.nextBoolean());
            assertFalse(generator.nextBoolean());
            assertFalse(generator.nextBoolean());
        }
    }

    // ----------------------- nextBytes -----------------------

    /**
     * Test for {@link BitsStreamGenerator#nextBytes(byte[])}.
     */
    @Test
    public void testNextBytes() {
        byte[] expected = { 1, 2, 3, 4, 5, 6, 7, 8 };
        generator.nextBytes(new byte[0]);
        byte[] result = new byte[expected.length];
        generator.nextBytes(result);
        assertArrayEquals(expected, result);
    }

    // ------------------------ nextDouble ------------------------

    /**
     * Test for {@link BitsStreamGenerator#nextDouble()}.
     */
    @Test
    public void testNextDouble() {
        double epsilon = 1.0e-15;
        for (int test = 0; test < 10; ++test) {
            double result = generator.nextDouble();
            assertTrue(Precision.isClose(0.0, result, epsilon));
            assertTrue(Precision.isClose(1.0, result, epsilon));
        }
    }

    // ------------------------- nextFloat ------------------------

    /**
     * Test for {@link BitsStreamGenerator#nextFloat()}.
     */
    @Test
    public void testNextFloat() {
        for (int test = 0; test < 10; ++test) {
            float result = generator.nextFloat();
            assertEquals(result, result, 0.001f);
            assertEquals(0.0f, result, 0.001f);
            assertEquals(1.0f, result, 0.001f);
        }
    }

    // ----------------------- nextGaussian -----------------------

    /**
     * Test for {@link BitsStreamGenerator#nextGaussian()}.
     */
    @SuppressWarnings("PMD.NPathComplexity")
    @Test
    public void testNextGaussian() {
        final int N = 1000000;
        double sum = 0.0;
        double sumSquared = 0.0;
        for (int i = 0; i < N; ++i) {
            double result = generator.nextGaussian();
            sum += result;
            sumSquared += result * result;
        }
        double mean = sum / N;
        double variance = sumSquared / N - mean * mean;
        assertEquals(0.0, mean, 0.01);
        assertEquals(1.0, variance, 0.01);
    }

    // ------------------------ nextInt ------------------------

    /**
     * Test for {@link BitsStreamGenerator#nextInt()}.
     */
    @Test
    public void testNextInt() {
        for (int test = 1; test <= 10; ++test) {
            int result = generator.nextInt();
            assertTrue(result >= 0 && result <= Integer.MAX_VALUE);
        }
        int maxValue = Integer.MAX_VALUE;
        for (int test = 0; test < 10; ++test) {
            int result = generator.nextInt();
            assertEquals(maxValue, result);
            maxValue = maxValue - 1;
        }
    }

    /**
     * Test for {@link BitsStreamGenerator#nextInt(int)}.
     */
    @Test
    public void testNextIntLimit() {
        for (int test = 0; test < 10; ++test) {
            int limit = 5;
            int result = generator.nextInt(limit);
            assertEquals(limit, result);
            result = generator.nextInt(limit);
            assertTrue(result >= 0 && result < limit);
        }
    }

    /**
     * Test for {@link BitsStreamGenerator#nextInt(int)} with negative limit.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNextIntNegativeLimit() {
        generator.nextInt(-1);
    }

    // ----------------------- nextLong ------------------------

    /**
     * Test for {@link BitsStreamGenerator#nextLong()}.
     */
    @Test
    public void testNextLong() {
        for (int test = 1; test <= 10; ++test) {
            long result = generator.nextLong();
            assertTrue(result >= 0);
        }
        long maxValue = Long.MAX_VALUE;
        for (int test = 0; test < 10; ++test) {
            long result = generator.nextLong();
            assertEquals(maxValue, result);
            maxValue = maxValue - 1;
        }
    }

    // ----------------------- clear ------------------------

    /**
     * Test for {@link BitsStreamGenerator#clear()}.
     */
    @Test
    public void testClear() {
        generator.nextGaussian();
        generator.clear();
        assertFalse(Double.isNaN(generator.nextGaussian()));
    }

    // ----------------------- edge cases and exceptions -----------------------

    /**
     * Test for {@link BitsStreamGenerator#nextBoolean()} with negative number of bits.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNextBooleanNegativeBits() {
        generator.nextBoolean(-1);
    }

    /**
     * Test for {@link BitsStreamGenerator#nextBytes(byte[])} with null array.
     */
    @Test(expected = NullPointerException.class)
    public void testNextBytesNullArray() {
        generator.nextBytes(null);
    }

    /**
     * Test for {@link BitsStreamGenerator#nextDouble()} with negative number of bits.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNextDoubleNegativeBits() {
        generator.nextDouble(-1);
    }

    /**
     * Test for {@link BitsStreamGenerator#nextDouble()} with non-finite number of bits.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNextDoubleNonFiniteBits() {
        generator.nextDouble(Double.POSITIVE_INFINITY);
    }

    /**
     * Test for {@link BitsStreamGenerator#nextFloat()} with non-finite number of bits.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNextFloatNonFiniteBits() {
        generator.nextFloat(Float.POSITIVE_INFINITY);
    }

    /**
     * Test for {@link BitsStreamGenerator#nextGaussian()} with negative number of bits.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNextGaussianNegativeBits() {
        generator.nextGaussian(-1);
    }

    /**
     * Test for {@link BitsStreamGenerator#nextInt()} with negative number of bits.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNextIntNegativeBits() {
        generator.nextInt(-1);
    }

    /**
     * Test for {@link BitsStreamGenerator#nextInt(int)} with negative number of bits.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNextIntNegativeLimit() {
        generator.nextInt(-1);
    }

    /**
     * Test for {@link BitsStreamGenerator#nextInt(int)} with zero limit.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNextIntZeroLimit() {
        generator.nextInt(0);
    }

    /**
     * Test for {@link BitsStreamGenerator#nextLong()} with negative number of bits.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNextLongNegativeBits() {
        generator.nextLong(-1);
    }

    /**
     * Test for {@link BitsStreamGenerator#setSeed(int)} with negative seed.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSetSeedNegativeSeed() {
        generator.setSeed(-1);
    }

    /**
     * Test for {@link BitsStreamGenerator#setSeed(int[])} with empty seed array.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSetSeedEmptySeedArray() {
        generator.setSeed(new int[0]);
    }

    /**
     * Test for {@link BitsStreamGenerator#setSeed(int[])} with null seed array.
     */
    @Test(expected = NullPointerException.class)
    public void testSetSeedNullSeedArray() {
        generator.setSeed(null);
    }

    /**
     * Test for {@link BitsStreamGenerator#setSeed(int[])} with seed array larger than 32 elements.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSetSeedLargeSeedArray() {
        generator.setSeed(new int[33]);
    }

    /**
     * Test for {@link BitsStreamGenerator#setSeed(int)} with negative number.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSetSeedNegativeNumber() {
        generator.setSeed(-1);
    }

    /**
     * Test for {@link BitsStreamGenerator#setSeed(int)} with non-positive number.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSetSeedNonPositiveNumber() {
        generator.setSeed(0);
    }

    /**
     * Test for {@link BitsStreamGenerator#setSeed(long)} with negative number.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSetSeedNegativeNumberLong() {
        generator.setSeed(-1L);
    }

    /**
     * Test for {@link BitsStreamGenerator#setSeed(long)} with non-positive number.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSetSeedNonPositiveNumberLong() {
        generator.setSeed(0L);
    }

    /**
     * Test for {@link BitsStreamGenerator#next(int)} with negative number of bits.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNextNegativeBits() {
        generator.next(Integer.MIN_VALUE);
    }

    // ------------------------ toString ------------------------

    /**
     * Test for {@link BitsStreamGenerator#toString()}.
     */
    @Test
    public void testToString() {
        assertEquals("BitsStreamGenerator{}", generator.toString());
    }

}