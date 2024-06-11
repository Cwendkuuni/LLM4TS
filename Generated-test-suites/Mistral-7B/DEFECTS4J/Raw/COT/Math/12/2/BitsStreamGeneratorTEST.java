package org.apache.commons.math3.random;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;

import java.util.Random;

import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.random.BitsStreamGenerator;
import org.apache.commons.math3.random.MersenneTwister;
import org.apache.commons.math3.util.FastMath;
import org.junit.Before;
import org.junit.Test;

/**
 * Test cases for {@link BitsStreamGenerator}.
 */
public class BitsStreamGeneratorTest {

    private BitsStreamGenerator generator;
    private MersenneTwister underlyingGenerator;

    @Before
    public void setUp() {
        underlyingGenerator = new MersenneTwister();
        generator = new BitsStreamGenerator() {
            @Override
            public void setSeed(int seed) {
                underlyingGenerator.setSeed(seed);
            }

            @Override
            public void setSeed(long seed) {
                underlyingGenerator.setSeed(seed);
            }

            @Override
            public void setSeed(int[] seed) {
                underlyingGenerator.setSeed(seed);
            }

            @Override
            public void nextBytes(byte[] bytes) {
                underlyingGenerator.nextBytes(bytes);
            }

            @Override
            public double nextGaussian() {
                return underlyingGenerator.nextGaussian();
            }

            @Override
            public void clear() {
                underlyingGenerator.clear();
            }
        };
    }

    @Test
    public void testNextBoolean() {
        assertEquals(true, generator.nextBoolean());
        assertEquals(false, generator.nextBoolean());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNextBooleanNegativeNumberOfBits() {
        generator.nextBoolean((-1) << 3);
    }

    @Test
    public void testNextBytes() {
        byte[] bytes = new byte[10];
        generator.nextBytes(bytes);
        for (int i = 0; i < bytes.length; i++) {
            assertEquals(Byte.MIN_VALUE + (int) (Math.random() * (Byte.MAX_VALUE - Byte.MIN_VALUE + 1)), bytes[i]);
        }
    }

    @Test
    public void testNextBytesNullArray() {
        generator.nextBytes(null);
    }

    @Test
    public void testNextDouble() {
        double expected = Math.random();
        double actual = generator.nextDouble();
        assertEquals(expected, actual, 1E-14);
    }

    @Test
    public void testNextDoubleNegativeNumberOfBits() {
        generator.nextDouble((-1) << 6);
    }

    @Test
    public void testNextFloat() {
        float expected = (float) Math.random();
        float actual = generator.nextFloat();
        assertEquals(expected, actual, 1E-14f);
    }

    @Test
    public void testNextFloatNegativeNumberOfBits() {
        generator.nextFloat((-1) << 2);
    }

    @Test
    public void testNextGaussian() {
        double expectedMean = Math.random();
        double expectedStandardDeviation = Math.random();
        double actualMean = generator.nextGaussian();
        double actualStandardDeviation = generator.nextGaussian();
        assertEquals(expectedMean, actualMean, 1E-14);
        assertEquals(expectedStandardDeviation, actualStandardDeviation, 1E-14);
        assertEquals(FastMath.sqrt(Math.PI / 2), FastMath.erf(actualMean / (2 * FastMath.SQRT2 * expectedStandardDeviation)), 1E-14);
    }

    @Test
    public void testNextGaussianNegativeNumberOfBits() {
        generator.nextGaussian((-1) << 6);
    }

    @Test
    public void testNextInt() {
        int expected = (int) (Math.random() * Integer.MAX_VALUE);
        int actual = generator.nextInt();
        assertEquals(expected, actual);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testNextIntNegativeNumberOfBits() {
        generator.nextInt((-1) << 31);
    }

    @Test
    public void testNextIntWithZeroNumberOfBits() {
        int actual = generator.nextInt();
        assertEquals(0, actual);
    }

    @Test
    public void testNextIntWithPowerOfTwoNumberOfBits() {
        int numberOfBits = 8;
        int expected = (int) (Math.random() * (1 << numberOfBits));
        int actual = generator.nextInt();
        int mask = (1 << numberOfBits) - 1;
        assertEquals(expected & mask, actual & mask);
    }

    @Test
    public void testNextLong() {
        long expected = Math.random() * Long.MAX_VALUE;
        long actual = generator.nextLong();
        assertEquals(expected, actual);
    }

    @Test
    public void testNextLongNegativeNumberOfBits() {
        generator.nextLong((-1) << 62);
    }

    @Test
    public void testNextLongWithPowerOfTwoNumberOfBits() {
        long numberOfBits = 32;
        long expected = Math.random() * (1L << numberOfBits);
        long actual = generator.nextLong();
        long mask = (1L << numberOfBits) - 1;
        assertEquals(expected & mask, actual & mask);
    }

    @Test
    public void testSetSeed() {
        int seed = (int) (Math.random() * Integer.MAX_VALUE);
        generator.setSeed(seed);
        byte[] bytes = new byte[8];
        generator.nextBytes(bytes);
        int expected = seed;
        int actual = FastMath.combineBytesToInt(bytes);
        assertEquals(expected, actual);
    }

    @Test
    public void testClear() {
        generator.clear();
        assertFalse(generator.hasNextGaussian());
    }

    @Test
    public void testHasNextGaussian() {
        generator.nextGaussian();
        assertTrue(generator.hasNextGaussian());
        generator.clear();
        assertFalse(generator.hasNextGaussian());
    }

    @Test
    public void testNextGaussianMultipleTimesWithSameSeed() {
        int seed = (int) (Math.random() * Integer.MAX_VALUE);
        generator.setSeed(seed);
        double firstGaussian = generator.nextGaussian();
        generator.clear();
        double secondGaussian = generator.nextGaussian();
        assertEquals(firstGaussian, secondGaussian, 1E-14);
        generator.setSeed(seed);
        double thirdGaussian = generator.nextGaussian();
        assertEquals(firstGaussian, thirdGaussian, 1E-14);
    }

    @Test
    public void testSetSeedWithNullSeed() {
        generator.setSeed(null);
    }

    @Test
    public void testSetSeedWithZeroLengthSeedArray() {
        generator.setSeed(new int[0]);
    }

    @Test
    public void testSetSeedWithNonZeroLengthSeedArray() {
        int[] seed = new int[3];
        for (int i = 0; i < seed.length; i++) {
            seed[i] = (int) (Math.random() * Integer.MAX_VALUE);
        }
        generator.setSeed(seed);
        byte[] bytes = new byte[12];
        generator.nextBytes(bytes);
        for (int i = 0; i < seed.length; i++) {
            int expected = seed[i];
            int actual = FastMath.bytesToInt(new byte[] {bytes[i * 4], bytes[i * 4 + 1], bytes[i * 4 + 2], bytes[i * 4 + 3]});
            assertEquals(expected, actual);
        }
    }

    @Test
    public void testSetSeedWithNegativeLengthSeedLong() {
        generator.setSeed((-1) << 32);
    }

    @Test
    public void testSetSeedWithNegativeLengthSeedIntArray() {
        generator.setSeed(new int[] {(-1) << 31, -1});
    }

    @Test
    public void testSetSeedWithNegativeLengthSeedLongArray() {
        generator.setSeed(new long[] {(-1) << 64, (-1) << 32});
    }

    @Test
    public void testNextGaussianNextInt() {
        double expectedGaussian = Math.random();
        double actualGaussian = generator.nextGaussian();
        int expectedInt = (int) (Math.random() * Integer.MAX_VALUE);
        int actualInt = generator.nextInt();
        assertEquals(expectedGaussian, actualGaussian, 1E-14);
        assertEquals(expectedInt, actualInt);
        assertNotSame(expectedGaussian, actualInt);
    }

    @Test
    public void testNextGaussianNextBoolean() {
        double expectedGaussian = Math.random();
        double actualGaussian = generator.nextGaussian();
        boolean expectedBoolean = Math.random() < 0.5;
        boolean actualBoolean = generator.nextBoolean();
        assertEquals(expectedGaussian, actualGaussian, 1E-14);
        assertEquals(expectedBoolean, actualBoolean);
        assertNotSame(expectedGaussian, actualBoolean);
    }

    @Test
    public void testNextGaussianNextDouble() {
        double expectedGaussian = Math.random();
        double actualGaussian = generator.nextGaussian();
        double expectedDouble = Math.random();
        double actualDouble = generator.nextDouble();
        assertEquals(expectedGaussian, actualGaussian, 1E-14);
        assertEquals(expectedDouble, actualDouble, 1E-14);
        assertNotSame(expectedGaussian, expectedDouble);
    }

    @Test
    public void testNextGaussianNextFloat() {
        double expectedGaussian = Math.random();
        double actualGaussian = generator.nextGaussian();
        float expectedFloat = (float) Math.random();
        float actualFloat = generator.nextFloat();
        assertEquals(expectedGaussian, actualGaussian, 1E-14);
        assertEquals(expectedFloat, actualFloat, 1E-14f);
        assertNotSame(expectedGaussian, expectedFloat);
    }

    @Test
    public void testNextGaussianNextLong() {
        double expectedGaussian = Math.random();
        double actualGaussian = generator.nextGaussian();
        long expectedLong = Math.random() * Long.MAX_VALUE;
        long actualLong = generator.nextLong();
        assertEquals(expectedGaussian, actualGaussian, 1E-14);
        assertEquals(expectedLong, actualLong);
        assertNotSame(expectedGaussian, expectedLong);
    }

    @Test
    public void testNextGaussianNextBytes() {
        double expectedGaussian = Math.random();
        double actualGaussian = generator.nextGaussian();
        byte[] expectedBytes = new byte[16];
        generator.nextBytes(expectedBytes);
        byte[] actualBytes = new byte[16];
        generator.nextBytes(actualBytes);
        for (int i = 0; i < expectedBytes.length; i++) {
            assertEquals(expectedBytes[i], actualBytes[i]);
        }
        assertEquals(expectedGaussian, actualGaussian, 1E-14);
    }

    @Test
    public void testNextGaussianNextIntWithPowerOfTwoNumberOfBits() {
        double expectedGaussian = Math.random();
        double actualGaussian = generator.nextGaussian();
        int numberOfBits = 8;
        int expectedInt = (int) (Math.random() * (1 << numberOfBits));
        int actualInt = generator.nextInt();
        int mask = (1 << numberOfBits) - 1;
        assertEquals(expectedGaussian, actualGaussian, 1E-14);
        assertEquals(expectedInt & mask, actualInt & mask);
    }

    @Test
    public void testNextGaussianNextLongWithPowerOfTwoNumberOfBits() {
        double expectedGaussian = Math.random();
        double actualGaussian = generator.nextGaussian();
        long numberOfBits = 32;
        long expectedLong = Math.random() * (1L << numberOfBits);
        long actualLong = generator.nextLong();
        long mask = (1L << numberOfBits) - 1;
        assertEquals(expectedGaussian, actualGaussian, 1E-14);
        assertEquals(expectedLong & mask, actualLong & mask);
    }

}