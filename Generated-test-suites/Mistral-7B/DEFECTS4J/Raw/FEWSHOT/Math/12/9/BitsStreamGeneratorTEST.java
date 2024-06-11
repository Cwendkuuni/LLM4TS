package org.apache.commons.math3.random;

import static org.junit.Assert.*;

import org.junit.Test;

public class BitsStreamGeneratorTest {

    @Test(expected = IllegalArgumentException.class)
    public void testNextIntWithNegativeArgument() {
        BitsStreamGenerator generator = new MersenneTwisterFastRandomGenerator();
        generator.nextInt(-1);
    }

    @Test
    public void testNextBoolean() {
        BitsStreamGenerator generator = new MersenneTwisterFastRandomGenerator();
        assertTrue(generator.nextBoolean());
        assertTrue(generator.nextBoolean());
        assertFalse(generator.nextBoolean());
        assertFalse(generator.nextBoolean());
        assertTrue(generator.nextBoolean());
    }

    @Test
    public void testNextBytes() {
        BitsStreamGenerator generator = new MersenneTwisterFastRandomGenerator();
        byte[] testBytes = new byte[10];
        generator.nextBytes(testBytes);
        for (byte b : testBytes) {
            assertTrue(b >= 0 && b <= 255);
        }
    }

    @Test
    public void testNextDouble() {
        BitsStreamGenerator generator = new MersenneTwisterFastRandomGenerator();
        double lowerLimit = 0.0;
        double upperLimit = 1.0;
        double expected = 0.5;
        double actual;
        for (int i = 0; i < 10000; i++) {
            actual = generator.nextDouble();
            assertTrue(actual >= lowerLimit && actual <= upperLimit);
        }
        actual = generator.nextDouble();
        assertEquals(expected, actual, 0.00001);
    }

    @Test
    public void testNextFloat() {
        BitsStreamGenerator generator = new MersenneTwisterFastRandomGenerator();
        float lowerLimit = 0.0f;
        float upperLimit = 1.0f;
        float expected = 0.5f;
        float actual;
        for (int i = 0; i < 10000; i++) {
            actual = generator.nextFloat();
            assertTrue(actual >= lowerLimit && actual <= upperLimit);
        }
        actual = generator.nextFloat();
        assertEquals(expected, actual, 0.0001f);
    }

    @Test
    public void testNextGaussian() {
        BitsStreamGenerator generator = new MersenneTwisterFastRandomGenerator();
        double mean = 0.0;
        double stdDev = 1.0;
        double expectedMean, expectedStdDev, actualMean, actualStdDev;
        for (int i = 0; i < 10000; i++) {
            actualMean = generator.nextGaussian();
            actualStdDev = Math.abs(generator.nextGaussian());
            assertEquals(mean, actualMean, 0.001);
            assertEquals(stdDev, actualStdDev, 0.001);
        }
        expectedMean = 0.25;
        expectedStdDev = 0.5;
        for (int i = 0; i < 1000; i++) {
            actualMean = generator.nextGaussian();
            actualStdDev = Math.abs(generator.nextGaussian());
            assertEquals(expectedMean, actualMean, 0.01);
            assertEquals(expectedStdDev, actualStdDev, 0.01);
        }
    }

    @Test
    public void testNextInt() {
        BitsStreamGenerator generator = new MersenneTwisterFastRandomGenerator();
        int expectedMin = Integer.MIN_VALUE;
        int expectedMax = Integer.MAX_VALUE;
        int expectedValue = 42;
        int actual;
        for (int i = 0; i < 10000; i++) {
            actual = generator.nextInt();
            assertTrue(actual >= expectedMin && actual <= expectedMax);
        }
        actual = generator.nextInt(10);
        assertEquals(expectedValue, actual);
    }

    @Test
    public void testNextIntWithPowerOfTwoArgument() {
        BitsStreamGenerator generator = new MersenneTwisterFastRandomGenerator();
        int expected = 1 << 15;
        int actual = generator.nextInt(1 << 15);
        assertEquals(expected, actual);
    }

    @Test
    public void testNextLong() {
        BitsStreamGenerator generator = new MersenneTwisterFastRandomGenerator();
        long lowerLimit = Long.MIN_VALUE;
        long upperLimit = Long.MAX_VALUE;
        long expected = 123456789012345L;
        long actual;
        for (int i = 0; i < 10000; i++) {
            actual = generator.nextLong();
            assertTrue(actual >= lowerLimit && actual <= upperLimit);
        }
        actual = generator.nextLong();
        assertEquals(expected, actual);
    }

    @Test
    public void testSetSeed() {
        BitsStreamGenerator generator = new MersenneTwisterFastRandomGenerator();
        long seed = 1234567890L;
        generator.setSeed(seed);
        long actual = generator.nextLong();
        assertEquals(seed, actual);
    }

    @Test
    public void testSetSeedWithArray() {
        BitsStreamGenerator generator = new MersenneTwisterFastRandomGenerator();
        long[] seedArray = {1, 2, 3, 4, 5};
        generator.setSeed(seedArray);
        long actual = generator.nextLong();
        long expected = 0x0102030405010203L;
        assertEquals(expected, actual);
    }

    @Test
    public void testSetSeedWithInvalidArray() {
        BitsStreamGenerator generator = new MersenneTwisterFastRandomGenerator();
        int[] seedArray = {1, 2, 3, 4, 5};
        try {
            generator.setSeed(seedArray);
            fail();
        } catch (IllegalArgumentException e) {
            //expected
        }
    }

    @Test
    public void testSetSeedWithLongArray() {
        BitsStreamGenerator generator = new MersenneTwisterFastRandomGenerator();
        long[] seedArray = {1, 2, 3, 4, 5};
        generator.setSeed(seedArray);
        long actual = generator.nextLong();
        long expected = 0x0102030405010203L;
        assertEquals(expected, actual);
    }

    @Test
    public void testSetSeedWithInvalidLongArray() {
        BitsStreamGenerator generator = new MersenneTwisterFastRandomGenerator();
        int[] seedArray = {1, 2, 3, 4, 5};
        try {
            generator.setSeed(seedArray);
            fail();
        } catch (IllegalArgumentException e) {
            //expected
        }
    }

    @Test
    public void testClear() {
        BitsStreamGenerator generator = new MersenneTwisterFastRandomGenerator();
        generator.clear();
        double actualNextGaussian = generator.nextGaussian();
        assertEquals(Double.NaN, actualNextGaussian);
    }
}