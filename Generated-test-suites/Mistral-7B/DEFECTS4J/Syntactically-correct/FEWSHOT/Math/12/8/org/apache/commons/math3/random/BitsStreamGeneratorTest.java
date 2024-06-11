package org.apache.commons.math3.random;

import static org.junit.Assert.*;

import org.junit.Test;

public class BitsStreamGeneratorTest {

    @Test
    public void testNextBoolean() {
        BitsStreamGenerator generator = new MersenneTwisterFastRandomGenerator();
        assertTrue(generator.nextBoolean());
        assertFalse(generator.nextBoolean());
        assertTrue(generator.nextBoolean());
        // test edge cases
        for (int i = 0; i < 100000; i++) {
            if (generator.nextBoolean()) {
                assertEquals(i % 2, 1);
            } else {
                assertEquals(i % 2, 0);
            }
        }
    }

    @Test
    public void testNextBytes() {
        BitsStreamGenerator generator = new MersenneTwisterFastRandomGenerator();
        byte[] expected = new byte[]{1, 2, 3, 4};
        byte[] bytes = new byte[4];
        generator.nextBytes(bytes);
        assertArrayEquals(expected, bytes);
        // test edge cases
        byte[] bytes1 = new byte[1];
        generator.nextBytes(bytes1);
        assertNotNull(bytes1);
        assertEquals(bytes1.length, 1);
    }

    @Test
    public void testNextDouble() {
        BitsStreamGenerator generator = new MersenneTwisterFastRandomGenerator();
        double expected = Math.random();
        double actual = generator.nextDouble();
        assertEquals(expected, actual, 0.0001);
        // test edge cases
        double min = Double.MIN_VALUE;
        double max = Double.MAX_VALUE;
        for (int i = 0; i < 100000; i++) {
            double actual = generator.nextDouble();
            if (actual < min) {
                min = actual;
            }
            if (actual > max) {
                max = actual;
            }
        }
        assertTrue(min > -1.0e308);
        assertTrue(max < 1.0e308);
    }

    @Test
    public void testNextFloat() {
        BitsStreamGenerator generator = new MersenneTwisterFastRandomGenerator();
        float expected = (float) Math.random();
        float actual = generator.nextFloat();
        assertEquals(expected, actual, 0.001f);
        // test edge cases
        float min = Float.MIN_VALUE;
        float max = Float.MAX_VALUE;
        for (int i = 0; i < 100000; i++) {
            float actual = generator.nextFloat();
            if (actual < min) {
                min = actual;
            }
            if (actual > max) {
                max = actual;
            }
        }
        assertTrue(min > -3.402823E38f);
        assertTrue(max < 3.402823E38f);
    }

    @Test
    public void testNextGaussian() {
        BitsStreamGenerator generator = new GaussianGenerator();
        double expected = 0;
        double actual;
        for (int i = 0; i < 1000; i++) {
            actual = generator.nextGaussian();
            expected += actual * actual;
        }
        double variance = 1.0 / (1000.0 * 2.0);
        assertEquals(1.0, expected, variance);
        // test edge cases
        for (int i = 0; i < 10000; i++) {
            double actual = generator.nextGaussian();
            if (actual < -3.0) {
                fail("NextGaussian generated " + actual);
            }
            if (actual > 3.0) {
                fail("NextGaussian generated " + actual);
            }
        }
    }

    @Test
    public void testNextInt() {
        BitsStreamGenerator generator = new MersenneTwisterFastRandomGenerator();
        int expected = 42;
        int actual = generator.nextInt();
        assertNotEquals(expected, actual);
        // test edge cases
        int min = Integer.MIN_VALUE;
        int max = Integer.MAX_VALUE;
        for (int i = 0; i < 100000; i++) {
            int actual = generator.nextInt();
            if (actual < min) {
                min = actual;
            }
            if (actual > max) {
                max = actual;
            }
        }
        assertTrue(min > Integer.MIN_VALUE);
        assertTrue(max < Integer.MAX_VALUE);
    }

    @Test
    public void testNextIntWithPowerOfTwoSize() {
        BitsStreamGenerator generator = new MersenneTwisterFastRandomGenerator();
        int size = 1 << 31;
        int expected = (int) (Math.random() * (1 << 31));
        int actual = generator.nextInt(size);
        assertEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNextIntWithNegativeSize() {
        BitsStreamGenerator generator = new MersenneTwisterFastRandomGenerator();
        int size = -1;
        generator.nextInt(size);
        fail("Expected IllegalArgumentException but didn't get it");
    }

    @Test
    public void testNextLong() {
        BitsStreamGenerator generator = new MersenneTwisterFastRandomGenerator();
        long expected = Math.random() * Long.MAX_VALUE;
        long actual = generator.nextLong();
        assertEquals(expected, actual);
        // test edge cases
        long min = Long.MIN_VALUE;
        long max = Long.MAX_VALUE;
        for (int i = 0; i < 100000; i++) {
            long actual = generator.nextLong();
            if (actual < min) {
                min = actual;
            }
            if (actual > max) {
                max = actual;
            }
        }
        assertTrue(min > Long.MIN_VALUE);
        assertTrue(max < Long.MAX_VALUE);
    }

    @Test
    public void testSetSeed() {
        BitsStreamGenerator generator = new MersenneTwisterFastRandomGenerator();
        long seed = 12345;
        generator.setSeed(seed);
        long expected = seed;
        long actual = generator.nextLong();
        // test that seed is set correctly
        assertEquals(expected, actual);
        // test that next number is not generated before seed is set
        long actualBeforeSeed = generator.nextLong();
        assertNotEquals(expected, actualBeforeSeed);
    }

    @Test
    public void testSetSeedWithArray() {
        BitsStreamGenerator generator = new MersenneTwisterFastRandomGenerator();
        long[] seed = {1, 2, 3, 4};
        generator.setSeed(seed);
        long expected = 0; // computationally infeasible to calculate
        for (long element : seed) {
            expected ^= element;
        }
        long actual = generator.nextLong();
        assertEquals(expected, actual);
    }

    @Test
    public void testSetSeedWithLongSeed() {
        BitsStreamGenerator generator = new MersenneTwisterFastRandomGenerator();
        long seed = 123456789;
        generator.setSeed(seed);
        long expected = seed;
        long actual = generator.nextLong();
        assertEquals(expected, actual);
    }

    @Test
    public void testClear() {
        BitsStreamGenerator generator = new GaussianGenerator();
        generator.nextGaussian();
        generator.clear();
        assertNull(generator.nextGaussian());
    }

}