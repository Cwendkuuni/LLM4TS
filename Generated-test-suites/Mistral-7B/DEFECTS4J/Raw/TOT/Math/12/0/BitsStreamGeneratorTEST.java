package org.apache.commons.math3.random;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.random.BitsStreamGenerator;
import org.apache.commons.math3.random.MersenneTwisterFast;
import org.junit.Test;

/**
 * Tests for {@link BitsStreamGenerator}.
 */
public class BitsStreamGeneratorTest {

    /** A test implementation of {@link BitsStreamGenerator}. */
    private static final BitsStreamGenerator TEST_GENERATOR = new MersenneTwisterFast();

    /**
     * Tests that {@link BitsStreamGenerator#nextBoolean()} returns the correct
     * value for a single boolean.
     */
    @Test
    public void testNextBoolean() {
        assertTrue(TEST_GENERATOR.nextBoolean());
    }

    /**
     * Tests that {@link BitsStreamGenerator#nextBoolean()} returns the correct
     * value for multiple booleans.
     */
    @Test
    public void testNextBooleanMultiple() {
        final int size = 1000;
        final boolean[] expected = new boolean[size];
        for (int i = 0; i < size; i++) {
            expected[i] = TEST_GENERATOR.nextBoolean();
        }
        final boolean[] actual = new boolean[size];
        for (int i = 0; i < size; i++) {
            actual[i] = TEST_GENERATOR.nextBoolean();
        }
        assertArrayEquals(expected, actual);
    }

    /**
     * Tests that {@link BitsStreamGenerator#nextBytes(byte[])} generates the
     * correct bytes for a given byte array.
     */
    @Test
    public void testNextBytes() {
        final byte[] expected = new byte[] { 1, 2, 3, 4, 5, 6, 7, 8 };
        final byte[] actual = new byte[expected.length];
        TEST_GENERATOR.nextBytes(actual);
        assertArrayEquals(expected, actual);
    }

    /**
     * Tests that {@link BitsStreamGenerator#nextBytes(byte[])} generates the
     * correct bytes for multiple byte arrays.
     */
    @Test
    public void testNextBytesMultiple() {
        final int size = 1000;
        final byte[][] expected = new byte[size][10];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < 10; j++) {
                expected[i][j] = (byte) (Math.random() * 256);
            }
        }
        final byte[][] actual = new byte[size][10];
        for (int i = 0; i < size; i++) {
            TEST_GENERATOR.nextBytes(actual[i]);
        }
        for (int i = 0; i < size; i++) {
            assertArrayEquals(expected[i], actual[i]);
        }
    }

    /**
     * Tests that {@link BitsStreamGenerator#nextDouble()} returns the correct
     * value for a single double.
     */
    @Test
    public void testNextDouble() {
        final double expected = Math.random();
        final double actual = TEST_GENERATOR.nextDouble();
        assertTrue(Math.abs(expected - actual) < 1e-12);
    }

    /**
     * Tests that {@link BitsStreamGenerator#nextDouble()} returns the correct
     * value for multiple doubles.
     */
    @Test
    public void testNextDoubleMultiple() {
        final int size = 1000;
        final double[] expected = new double[size];
        for (int i = 0; i < size; i++) {
            expected[i] = Math.random();
        }
        final double[] actual = new double[size];
        for (int i = 0; i < size; i++) {
            actual[i] = TEST_GENERATOR.nextDouble();
        }
        assertArrayEquals(expected, actual, 1e-12);
    }

    /**
     * Tests that {@link BitsStreamGenerator#nextFloat()} returns the correct
     * value for a single float.
     */
    @Test
    public void testNextFloat() {
        final float expected = (float) Math.random();
        final float actual = TEST_GENERATOR.nextFloat();
        assertTrue(Float.floatToIntBits(expected) == Float.floatToIntBits(actual));
    }

    /**
     * Tests that {@link BitsStreamGenerator#nextFloat()} returns the correct
     * value for multiple floats.
     */
    @Test
    public void testNextFloatMultiple() {
        final int size = 1000;
        final float[] expected = new float[size];
        for (int i = 0; i < size; i++) {
            expected[i] = (float) Math.random();
        }
        final float[] actual = new float[size];
        for (int i = 0; i < size; i++) {
            actual[i] = TEST_GENERATOR.nextFloat();
        }
        assertArrayEquals(expected, actual, 1e-6f);
    }

    /**
     * Tests that {@link BitsStreamGenerator#nextGaussian()} returns the correct
     * value for a single gaussian.
     */
    @Test
    public void testNextGaussian() {
        final double expected = Math.random() * 2 + 1;
        final double actual = TEST_GENERATOR.nextGaussian();
        assertTrue(Math.abs(expected - actual) < 1e-6);
    }

    /**
     * Tests that {@link BitsStreamGenerator#nextGaussian()} returns the correct
     * value for multiple gaussians.
     */
    @Test
    public void testNextGaussianMultiple() {
        final int size = 1000;
        final double[] expected = new double[size];
        for (int i = 0; i < size; i++) {
            expected[i] = Math.random() * 2 + 1;
        }
        final double[] actual = new double[size];
        for (int i = 0; i < size; i++) {
            actual[i] = TEST_GENERATOR.nextGaussian();
        }
        assertArrayEquals(expected, actual, 1e-6);
    }

    /**
     * Tests that {@link BitsStreamGenerator#nextInt()} returns the correct value
     * for a single int.
     */
    @Test
    public void testNextInt() {
        final int expected = (int) (Math.random() * Integer.MAX_VALUE);
        final int actual = TEST_GENERATOR.nextInt();
        assertTrue(expected == actual || (expected == Integer.MIN_VALUE && actual == Integer.MIN_VALUE));
    }

    /**
     * Tests that {@link BitsStreamGenerator#nextInt()} returns the correct value
     * for multiple ints with a given limit.
     */
    @Test
    public void testNextIntLimit() {
        final int expectedSize = 100;
        final int limit = 10;
        final int[] expected = new int[expectedSize];
        for (int i = 0; i < expectedSize; i++) {
            expected[i] = Math.abs((int) (Math.random() * limit));
        }
        final int[] actual = new int[expectedSize];
        for (int i = 0; i < expectedSize; i++) {
            actual[i] = TEST_GENERATOR.nextInt(limit);
        }
        assertArrayEquals(expected, actual);
    }

    /**
     * Tests that {@link BitsStreamGenerator#nextInt()} returns the correct value
     * for multiple ints without a given limit.
     */
    @Test
    public void testNextIntNoLimit() {
        final int expectedSize = 100;
        final int[] expected = new int[expectedSize];
        for (int i = 0; i < expectedSize; i++) {
            expected[i] = (int) (Math.random() * Integer.MAX_VALUE);
        }
        final int[] actual = new int[expectedSize];
        for (int i = 0; i < expectedSize; i++) {
            actual[i] = TEST_GENERATOR.nextInt();
        }
        assertArrayEquals(expected, actual);
    }

    /**
     * Tests that {@link BitsStreamGenerator#nextLong()} returns the correct value
     * for a single long.
     */
    @Test
    public void testNextLong() {
        final long expected = Math.random() * Long.MAX_VALUE;
        final long actual = TEST_GENERATOR.nextLong();
        assertTrue(expected == actual);
    }

    /**
     * Tests that {@link BitsStreamGenerator#nextLong()} returns the correct value
     * for multiple longs.
     */
    @Test
    public void testNextLongMultiple() {
        final int size = 100;
        final long[] expected = new long[size];
        for (int i = 0; i < size; i++) {
            expected[i] = Math.random() * Long.MAX_VALUE;
        }
        final long[] actual = new long[size];
        for (int i = 0; i < size; i++) {
            actual[i] = TEST_GENERATOR.nextLong();
        }
        assertArrayEquals(expected, actual);
    }

    /**
     * Tests that {@link BitsStreamGenerator#setSeed(int)} sets the seed correctly.
     */
    @Test
    public void testSetSeedInt() {
        final int seed = (int) (Math.random() * Integer.MAX_VALUE);
        TEST_GENERATOR.setSeed(seed);
        final int generatedSeed = TEST_GENERATOR.nextInt();
        assertTrue(seed == generatedSeed);
    }

    /**
     * Tests that {@link BitsStreamGenerator#setSeed(int[])} sets the seed correctly.
     */
    @Test
    public void testSetSeedIntArray() {
        final int seedSize = 10;
        final int[] seed = new int[seedSize];
        for (int i = 0; i < seedSize; i++) {
            seed[i] = Math.abs((int) (Math.random() * Integer.MAX_VALUE));
        }
        TEST_GENERATOR.setSeed(seed);
        final int[] generatedSeed = new int[seedSize];
        for (int i = 0; i < seedSize; i++) {
            generatedSeed[i] = TEST_GENERATOR.nextInt();
        }
        assertArrayEquals(seed, generatedSeed);
    }

    /**
     * Tests that {@link BitsStreamGenerator#setSeed(long)} sets the seed correctly.
     */
    @Test
    public void testSetSeedLong() {
        final long seed = Math.random() * Long.MAX_VALUE;
        TEST_GENERATOR.setSeed(seed);
        final long generatedSeed = TEST_GENERATOR.nextLong();
        assertTrue(seed == generatedSeed);
    }

    /**
     * Tests that {@link BitsStreamGenerator#clear()} clears the cache correctly.
     */
    @Test
    public void testClear() {
        TEST_GENERATOR.nextGaussian();
        TEST_GENERATOR.clear();
        assertTrue(Double.isNaN(TEST_GENERATOR.nextGaussian()));
    }

    /**
     * Tests that {@link BitsStreamGenerator#setSeed(int)} throws an exception
     * when given a negative seed.
     */
    @Test(expected = NotStrictlyPositiveException.class)
    public void testSetSeedNegativeInt() {
        TEST_GENERATOR.setSeed(-1);
    }

    /**
     * Tests that {@link BitsStreamGenerator#setSeed(int[])} throws an exception
     * when given a negative seed.
     */
    @Test(expected = NotStrictlyPositiveException.class)
    public void testSetSeedNegativeIntArray() {
        TEST_GENERATOR.setSeed(new int[] { -1 });
    }

    /**
     * Tests that {@link BitsStreamGenerator#setSeed(long)} throws an exception
     * when given a negative seed.
     */
    @Test(expected = NotStrictlyPositiveException.class)
    public void testSetSeedNegativeLong() {
        TEST_GENERATOR.setSeed(-1L);
    }

}