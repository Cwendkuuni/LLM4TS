package org.apache.commons.math3.random;

import static org.junit.Assert.*;

import org.junit.Test;

public class BitsStreamGeneratorTest {

    @Test
    public void testNextBoolean() {
        // Create an instance of BitsStreamGenerator
        BitsStreamGenerator generator = new MersenneTwisterFastRandomGenerator();

        // Test nextBoolean() method
        assertTrue(generator.nextBoolean());
        assertFalse(generator.nextBoolean());
        assertTrue(generator.nextBoolean());
        assertFalse(generator.nextBoolean());
        // ... add more test cases as needed
    }

    @Test
    public void testNextBytes() {
        // Create an instance of BitsStreamGenerator
        BitsStreamGenerator generator = new MersenneTwisterFastRandomGenerator();

        // Test nextBytes() method
        byte[] bytes = new byte[10];
        generator.nextBytes(bytes);
        // Check that each byte has been set to a random value
        for (byte b : bytes) {
            assertTrue(b >= 0 && b <= 0xff);
        }
        // ... add more test cases as needed
    }

    @Test
    public void testNextDouble() {
        // Create an instance of BitsStreamGenerator
        BitsStreamGenerator generator = new MersenneTwisterFastRandomGenerator();

        // Test nextDouble() method
        double expectedMin = 0.0;
        double expectedMax = 1.0;
        double actual = generator.nextDouble();
        assertTrue(actual >= expectedMin && actual <= expectedMax);
        // Test that the method generates different values each time it is called
        double actual2 = generator.nextDouble();
        assertNotEquals(actual, actual2);
        // Test that the method generates values close to 0.5 with the same frequency as values close to 0 or 1
        int count = 0;
        double threshold = 0.5;
        for (int i = 0; i < 10000; i++) {
            double actualValue = generator.nextDouble();
            if (Math.abs(actualValue - threshold) < 0.001) {
                count++;
            }
        }
        double proportion = (double) count / 10000;
        assertEquals(0.5, proportion, 0.001);
        // ... add more test cases as needed
    }

    @Test
    public void testNextFloat() {
        // Create an instance of BitsStreamGenerator
        BitsStreamGenerator generator = new MersenneTwisterFastRandomGenerator();

        // Test nextFloat() method
        float expectedMin = 0.0f;
        float expectedMax = 1.0f;
        float actual = generator.nextFloat();
        assertTrue(actual >= expectedMin && actual <= expectedMax);
        // Test that the method generates different values each time it is called
        float actual2 = generator.nextFloat();
        assertNotEquals(actual, actual2);
        // Test that the method generates values close to 0.5 with the same frequency as values close to 0 or 1
        int count = 0;
        float threshold = 0.5f;
        for (int i = 0; i < 10000; i++) {
            float actualValue = generator.nextFloat();
            if (Math.abs(actualValue - threshold) < 0.001f) {
                count++;
            }
        }
        float proportion = (float) count / 10000;
        assertEquals(0.5f, proportion, 0.001f);
        // ... add more test cases as needed
    }

    @Test
    public void testNextGaussian() {
        // Create an instance of BitsStreamGenerator
        BitsStreamGenerator generator = new GaussRng();

        // Test nextGaussian() method
        double expectedMean = 0.0;
        double expectedStdDev = 1.0;
        double actual = generator.nextGaussian();
        assertTrue(Double.isFinite(actual));
        // Test that the method generates values close to the expected mean with the same frequency as values far from it
        int count = 0;
        double threshold = expectedMean - 3 * expectedStdDev;
        double threshold2 = expectedMean + 3 * expectedStdDev;
        for (int i = 0; i < 10000; i++) {
            double actualValue = generator.nextGaussian();
            if (actualValue < threshold || actualValue > threshold2) {
                count++;
            }
        }
        double proportion = (double) count / 10000;
        assertEquals(0.6827, proportion, 0.01);
        // Test that the method generates values with the correct mean and standard deviation
        for (int i = 0; i < 1000; i++) {
            double actualValue = generator.nextGaussian();
            double mean = expectedMean;
            double stdDev = expectedStdDev;
            double expected = mean + stdDev * Math.random();
            assertTrue(Math.abs(actualValue - expected) < 5 * stdDev);
        }
        // ... add more test cases as needed
    }

    @Test
    public void testNextInt() {
        // Create an instance of BitsStreamGenerator
        BitsStreamGenerator generator = new MersenneTwisterFastRandomGenerator();

        // Test nextInt() method
        int expectedMin = Integer.MIN_VALUE;
        int expectedMax = Integer.MAX_VALUE;
        int actual = generator.nextInt();
        assertTrue(actual >= expectedMin && actual <= expectedMax);
        // Test that the method generates different values each time it is called
        int actual2 = generator.nextInt();
        assertNotEquals(actual, actual2);
        // Test that the method generates values uniformly distributed between the minimum and maximum values
        int count = 0;
        int threshold = (expectedMin + expectedMax) / 2;
        for (int i = 0; i < 10000; i++) {
            int actualValue = generator.nextInt();
            if (actualValue < threshold) {
                count++;
            }
        }
        int proportion = count / 10000;
        assertEquals(0.5, proportion);
        // Test that the method generates values correctly when the maximum value is a power of 2
        generator = new MersenneTwisterFastRandomGenerator();
        int max = 1 << 30;
        for (int i = 0; i < 1000; i++) {
            int actualValue = generator.nextInt(max);
            assertEquals((int) (Math.random() * max), actualValue);
        }
        // ... add more test cases as needed
    }

    @Test
    public void testNextLong() {
        // Create an instance of BitsStreamGenerator
        BitsStreamGenerator generator = new MersenneTwisterFastRandomGenerator();

        // Test nextLong() method
        long expectedMin = Long.MIN_VALUE;
        long expectedMax = Long.MAX_VALUE;
        long actual = generator.nextLong();
        assertTrue(actual >= expectedMin && actual <= expectedMax);
        // Test that the method generates different values each time it is called
        long actual2 = generator.nextLong();
        assertNotEquals(actual, actual2);
        // Test that the method generates values uniformly distributed between the minimum and maximum values
        long count = 0;
        long threshold = (expectedMin + expectedMax) / 2;
        for (int i = 0; i < 10000; i++) {
            long actualValue = generator.nextLong();
            if (actualValue < threshold) {
                count++;
            }
        }
        long proportion = count / 10000;
        assertEquals(0.5, proportion);
        // Test that the method generates values correctly when the maximum value is a power of 2
        generator = new MersenneTwisterFastRandomGenerator();
        long max = 1L << 32;
        for (int i = 0; i < 1000; i++) {
            long actualValue = generator.nextLong();
            assertEquals((long) (Math.random() * max), actualValue);
        }
        // ... add more test cases as needed
    }

    @Test
    public void testSetSeed() {
        // Create an instance of BitsStreamGenerator
        BitsStreamGenerator generator = new MersenneTwisterFastRandomGenerator();

        // Test setSeed() method
        long seed = 0x1234567890123456L;
        generator.setSeed(seed);
        long actual = generator.nextLong();
        long expected = seed;
        assertEquals(expected, actual);
        // Test that the method sets the seed correctly
        generator.setSeed(seed);
        generator.nextInt();
        generator.nextInt();
        generator.nextInt();
        long actual2 = generator.nextLong();
        assertEquals(expected, actual2);
        // Test that the method generates different values each time it is called after setting the seed
        long actual3 = generator.nextLong();
        assertNotEquals(actual, actual3);
        // Test that the method generates values based on the seed
        generator.setSeed(seed);
        int count = 0;
        long expectedValue = seed;
        for (int i = 0; i < 1000; i++) {
            long actualValue = generator.nextLong();
            assertEquals(expectedValue, actualValue);
            expectedValue = (expectedValue * 1664525L + 1013904223L) & 0xfffffffffffffL;
        }
        // ... add more test cases as needed
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetSeedNegativeNumber() {
        // Create an instance of BitsStreamGenerator
        BitsStreamGenerator generator = new MersenneTwisterFastRandomGenerator();

        // Test setSeed() method with a negative number
        int seed = -1;
        generator.setSeed(seed);
    }

    @Test
    public void testClear() {
        // Create an instance of BitsStreamGenerator
        BitsStreamGenerator generator = new GaussRng();

        // Test clear() method
        generator.clear();
        assertFalse(generator.hasNextGaussian());
    }
}