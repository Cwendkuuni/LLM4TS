package org.apache.commons.math3.random;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.security.SecureRandom;
import org.apache.commons.math3.random.BitsStreamGenerator;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;

/**
 * Test class for BitsStreamGenerator.
 */
public class BitsStreamGeneratorTest {

    /** Instance of BitsStreamGenerator for testing. */
    private BitsStreamGenerator generator = new MersenneTwisterFastRandomGenerator();

    /**
     * Test method for {@link BitsStreamGenerator#BitsStreamGenerator()}.
     */
    @Test
    public void testConstructor() {
        // Test default constructor
        new BitsStreamGenerator();
    }

    /**
     * Test method for {@link BitsStreamGenerator#setSeed(int)}.
     */
    @Test
    public void testSetSeedInt() {
        final int seed = 123456789;
        generator.setSeed(seed);
        // seed should be set correctly
        assertEquals(seed, generator.nextInt());
    }

    /**
     * Test method for {@link BitsStreamGenerator#setSeed(int[])}.
     */
    @Test
    public void testSetSeedIntArray() {
        final int[] seed = { 1, 2, 3, 4, 5 };
        generator.setSeed(seed);
        // seed should be set correctly
        for (int i = 0; i < seed.length; i++) {
            assertEquals(seed[i], generator.nextInt());
        }
    }

    /**
     * Test method for {@link BitsStreamGenerator#setSeed(long)}.
     */
    @Test
    public void testSetSeedLong() {
        final long seed = 987654321L;
        generator.setSeed(seed);
        // seed should be set correctly
        assertEquals(seed, generator.nextLong());
    }

    /**
     * Test method for {@link BitsStreamGenerator#next(int)}.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testNextIntAbstract() {
        // Calling this method directly should throw an UnsupportedOperationException
        generator.next(10);
    }

    /**
     * Test method for {@link BitsStreamGenerator#nextBoolean()}.
     */
    @Test
    public void testNextBoolean() {
        // Test nextBoolean() with expected output, generated using a SecureRandom
        SecureRandom secureRandom = new SecureRandom();
        final int times = 10000;
        final int trueCount = 0;
        final int falseCount = 0;

        for (int i = 0; i < times; i++) {
            final boolean value = generator.nextBoolean();
            if (value) {
                trueCount++;
            } else {
                falseCount++;
            }
        }

        // Check that the number of true and false values is close to the expected 50% ratio
        final double expectedRatio = 0.5;
        final double trueProb = (double) trueCount / times;
        final double falseProb = (double) falseCount / times;
        assertTrue(Precision.isClose(expectedRatio, trueProb, 0.1));
        assertTrue(Precision.isClose(expectedRatio, falseProb, 0.1));

        // Test nextBoolean() with same seed value, generated using a SecureRandom
        final long seed = secureRandom.nextLong();
        generator.setSeed(seed);
        final boolean value = generator.nextBoolean();
        final boolean previousValue = generator.nextBoolean();

        // Check that the output of nextBoolean() is uncorrelated
        assertFalse(value == previousValue);
    }

    /**
     * Test method for {@link BitsStreamGenerator#nextBytes(byte[])}.
     */
    @Test
    public void testNextBytes() {
        final byte[] expected = new byte[]{(byte) 0x41, (byte) 0x42, (byte) 0x43,
                (byte) 0x44, (byte) 0x45};
        final byte[] result = new byte[expected.length];
        generator.nextBytes(result);

        // Test nextBytes() with expected output, generated using a SecureRandom
        final SecureRandom secureRandom = new SecureRandom();
        secureRandom.nextBytes(result);

        // Check that the output bytes are the same
        assertArrayEquals(expected, result);
    }

    /**
     * Test method for {@link BitsStreamGenerator#nextDouble()}.
     */
    @Test
    public void testNextDouble() {
        final double epsilon = 1e-6;
        final double expectedMin = 0.0;
        final double expectedMax = 1.0;

        // Test nextDouble() with expected output, generated using a SecureRandom
        final SecureRandom secureRandom = new SecureRandom();
        final int times = 10000;
        double sum = 0.0;

        for (int i = 0; i < times; i++) {
            final double value = generator.nextDouble();
            sum += value;
        }

        // Check that the average value of nextDouble() is close to 0.5
        final double average = sum / times;
        assertEquals(expectedMin + (expectedMax - expectedMin) / 2.0,
                average, epsilon);

        // Test nextDouble() multiple times with the same seed value, generated using a SecureRandom
        final long seed = secureRandom.nextLong();
        generator.setSeed(seed);
        final int timesTwo = 2 * times;
        double sumTwo = 0.0;

        for (int i = 0; i < timesTwo; i++) {
            final double value = generator.nextDouble();
            sumTwo += value;
        }

        // Check that the output of nextDouble() with the same seed value is reproducible
        assertEquals(sum / times, sumTwo / timesTwo, epsilon);
    }

    /**
     * Test method for {@link BitsStreamGenerator#nextFloat()}.
     */
    @Test
    public void testNextFloat() {
        final float epsilon = 1e-6f;
        final float expectedMin = 0.0f;
        final float expectedMax = 1.0f;

        // Test nextFloat() with expected output, generated using a SecureRandom
        final SecureRandom secureRandom = new SecureRandom();
        final int times = 10000;
        float sum = 0.0f;

        for (int i = 0; i < times; i++) {
            final float value = generator.nextFloat();
            sum += value;
        }

        // Check that the average value of nextFloat() is close to 0.5
        final float average = sum / times;
        assertEquals(expectedMin + (expectedMax - expectedMin) / 2.0f,
                average, epsilon);

        // Test nextFloat() multiple times with the same seed value, generated using a SecureRandom
        final long seed = secureRandom.nextLong();
        generator.setSeed(seed);
        final int timesTwo = 2 * times;
        float sumTwo = 0.0f;

        for (int i = 0; i < timesTwo; i++) {
            final float value = generator.nextFloat();
            sumTwo += value;
        }

        // Check that the output of nextFloat() with the same seed value is reproducible
        assertEquals(sum / times, sumTwo / timesTwo, epsilon);
    }

    /**
     * Test method for {@link BitsStreamGenerator#nextGaussian()}.
     */
    @Test
    public void testNextGaussian() {
        final double epsilon = 1e-6;

        // Test nextGaussian() with expected output, generated using a SecureRandom
        final SecureRandom secureRandom = new SecureRandom();
        final int times = 10000;
        final double mean = 0.0;
        final double stdDev = 1.0;
        double sumSqr = 0.0;

        for (int i = 0; i < times; i++) {
            final double value = generator.nextGaussian();
            sumSqr += Math.pow(value - mean, 2);
        }

        // Check that the variance of nextGaussian() is close to 1.0
        final double variance = sumSqr / times;
        assertEquals(stdDev * stdDev, variance, epsilon);

        // Test nextGaussian() multiple times with the same seed value, generated using a SecureRandom
        final long seed = secureRandom.nextLong();
        generator.setSeed(seed);
        final int timesTwo = 2 * times;
        double sumSqrTwo = 0.0;

        for (int i = 0; i < timesTwo; i++) {
            final double value = generator.nextGaussian();
            sumSqrTwo += Math.pow(value - mean, 2);
        }

        // Check that the output of nextGaussian() with the same seed value is reproducible
        assertEquals(sumSqr / times, sumSqrTwo / timesTwo, epsilon);
    }

    /**
     * Test method for {@link BitsStreamGenerator#nextInt()}.
     */
    @Test
    public void testNextInt() {
        final int min = Integer.MIN_VALUE;
        final int max = Integer.MAX_VALUE;

        // Test nextInt() with expected output, generated using a SecureRandom
        final SecureRandom secureRandom = new SecureRandom();
        final int times = 10000;
        final int sum = 0;

        for (int i = 0; i < times; i++) {
            sum += generator.nextInt();
        }

        // Check that the average value of nextInt() is close to the middle value between min and max
        final int average = sum / times;
        assertEquals((min + max) / 2, average, epsilon);

        // Test nextInt() multiple times with the same seed value, generated using a SecureRandom
        final long seed = secureRandom.nextLong();
        generator.setSeed(seed);
        final int timesTwo = 2 * times;
        int sumTwo = 0;

        for (int i = 0; i < timesTwo; i++) {
            sumTwo += generator.nextInt();
        }

        // Check that the output of nextInt() with the same seed value is reproducible
        assertEquals(sum / times, sumTwo / timesTwo, epsilon);

        // Test nextInt() with negative arguments
        try {
            generator.nextInt(-1);
            fail("nextInt() should throw an IllegalArgumentException for negative arguments.");
        } catch (IllegalArgumentException ex) {
            // Expected exception
        }

        // Test nextInt() with zero argument, generated using a SecureRandom
        final int expectedZero = secureRandom.nextInt(0);
        final int resultZero = generator.nextInt();

        // Check that the output of nextInt() with a zero argument is the same as nextInt() with a positive argument
        assertEquals(expectedZero, resultZero);
    }

    /**
     * Test method for {@link BitsStreamGenerator#nextLong()}.
     */
    @Test
    public void testNextLong() {
        final long min = Long.MIN_VALUE;
        final long max = Long.MAX_VALUE;

        // Test nextLong() with expected output, generated using a SecureRandom
        final SecureRandom secureRandom = new SecureRandom();
        final int times = 10000;
        long sum = 0L;

        for (int i = 0; i < times; i++) {
            sum += generator.nextLong();
        }

        // Check that the average value of nextLong() is close to the middle value between min and max
        final long average = sum / times;
        assertEquals((min + max) / 2, average, epsilon);

        // Test nextLong() multiple times with the same seed value, generated using a SecureRandom
        final long seed = secureRandom.nextLong();
        generator.setSeed(seed);
        final int timesTwo = 2 * times;
        long sumTwo = 0L;

        for (int i = 0; i < timesTwo; i++) {
            sumTwo += generator.nextLong();
        }

        // Check that the output of nextLong() with the same seed value is reproducible
        assertEquals(sum / times, sumTwo / timesTwo, epsilon);

        // Test nextLong() with negative arguments
        try {
            generator.nextLong(-1);
            fail("nextLong() should throw an IllegalArgumentException for negative arguments.");
        } catch (IllegalArgumentException ex) {
            // Expected exception
        }

        // Test nextLong() with zero argument, generated using a SecureRandom
        final long expectedZero = secureRandom.nextLong(0);
        final long resultZero = generator.nextLong();

        // Check that the output of nextLong() with a zero argument is the same as nextLong() with a positive argument
        assertEquals(expectedZero, resultZero);
    }

    /**
     * Test method for {@link BitsStreamGenerator#clear()}.
     */
    @Test
    public void testClear() {
        final double expectedNextGaussian = 0.0;

        // Test clear() method
        generator.clear();
        assertEquals(expectedNextGaussian, generator.nextGaussian(), epsilon);
    }

}