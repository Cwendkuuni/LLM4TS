package org.apache.commons.math3.random;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class BitsStreamGeneratorTest {
    @Test
    public void testSetSeed() {
        BitsStreamGenerator generator = new MersenneTwisterFastRandomGenerator();
        int seed = 12345;

        generator.setSeed(seed);
        assertEquals(seed, generator.nextInt());
        generator.setSeed(seed);
        assertEquals(seed, generator.nextInt());

        generator.clear();
        assertFalse(Double.isFinite(generator.nextGaussian()));
        generator.setSeed(seed);
        assertEquals(seed, generator.nextInt());
        assertTrue(Double.isFinite(generator.nextGaussian()));
    }

    @Test
    public void testNextBoolean() {
        BitsStreamGenerator generator = new MersenneTwisterFastRandomGenerator();
        assertTrue(generator.nextBoolean());
        assertTrue(generator.nextBoolean());
        assertFalse(generator.nextBoolean());
        assertFalse(generator.nextBoolean());
    }

    @Test
    public void testNextBytes() {
        BitsStreamGenerator generator = new MersenneTwisterFastRandomGenerator();
        byte[] bytes = new byte[4];
        generator.nextBytes(bytes);
        assertArrayEquals(new byte[]{0x0, 0x1, 0x2, 0x3}, bytes);
    }

    @Test
    public void testNextDouble() {
        BitsStreamGenerator generator = new MersenneTwisterFastRandomGenerator();
        double expected = 0.123456789;
        assertEquals(expected, generator.nextDouble(), 0.0000001);
    }

    @Test
    public void testNextDoubleMultipleTimes() {
        BitsStreamGenerator generator = new MersenneTwisterFastRandomGenerator();
        double expected = 0.123456789;
        double actual = generator.nextDouble();
        assertEquals(expected, actual, 0.0000001);
        actual = generator.nextDouble();
        assertEquals(expected, actual, 0.0000001);
        assertNotSame(actual, expected);
    }

    @Test
    public void testNextFloat() {
        BitsStreamGenerator generator = new MersenneTwisterFastRandomGenerator();
        float expected = 0.123456f;
        assertEquals(expected, generator.nextFloat(), 0.000001f);
    }

    @Test
    public void testNextGaussian() {
        BitsStreamGenerator generator = new NormalDistributionGenerator();
        double expectedMean = 0.123456;
        double expectedStandardDeviation = 0.15;
        double actualMean = 0d;
        double actualStandardDeviation = 0d;
        int sampleSize = 10000;

        for (int i = 0; i < sampleSize; i++) {
            double value = generator.nextGaussian();
            actualMean += value;
        }

        actualMean /= sampleSize;
        assertEquals(expectedMean, actualMean, 0.0001);

        for (int i = 0; i < sampleSize; i++) {
            double value = generator.nextGaussian();
            actualStandardDeviation += Math.pow(value - expectedMean, 2);
        }

        actualStandardDeviation = Math.sqrt(actualStandardDeviation / sampleSize);
        assertEquals(expectedStandardDeviation, actualStandardDeviation, 0.001);
    }

    @Test
    public void testNextInt() {
        BitsStreamGenerator generator = new MersenneTwisterFastRandomGenerator();
        assertEquals(1, generator.nextInt(2));
        assertEquals(0, generator.nextInt(2));
        assertEquals(42, generator.nextInt(43));
        assertEquals(42, generator.nextInt(43));
        assertEquals(Integer.MIN_VALUE, generator.nextInt(1));
        assertEquals(Integer.MAX_VALUE, generator.nextInt(1));
    }

    @Test
    public void testNextIntMultipleTimes() {
        BitsStreamGenerator generator = new MersenneTwisterFastRandomGenerator();
        int expected = 42;
        int actual = generator.nextInt();
        assertEquals(expected, actual);
        actual = generator.nextInt();
        assertNotSame(actual, expected);
    }

    @Test
    public void testNextIntNegativeParameter() {
        BitsStreamGenerator generator = new MersenneTwisterFastRandomGenerator();
        try {
            generator.nextInt(-1);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testNextLong() {
        BitsStreamGenerator generator = new MersenneTwisterFastRandomGenerator();
        long expected = 12345678912345L;
        assertEquals(expected, generator.nextLong());
    }

    @Test
    public void testNextLongMultipleTimes() {
        BitsStreamGenerator generator = new MersenneTwisterFastRandomGenerator();
        long expected = 12345678912345L;
        long actual = generator.nextLong();
        assertEquals(expected, actual);
        actual = generator.nextLong();
        assertNotSame(actual, expected);
    }
}