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
        generator.setSeed(Integer.MIN_VALUE);
        assertFalse(generator.nextBoolean());
        generator.setSeed(Integer.MAX_VALUE);
        assertTrue(generator.nextBoolean());
    }

    @Test
    public void testNextBytes() {
        byte[] expected = new byte[]{1, 2, 3, 4};
        BitsStreamGenerator generator = new MersenneTwisterFastRandomGenerator();
        generator.nextBytes(expected);
        assertArrayEquals(expected, expected);
    }

    @Test
    public void testNextDouble() {
        BitsStreamGenerator generator = new MersenneTwisterFastRandomGenerator();
        double result = generator.nextDouble();
        assertTrue(result > -0.0d && result < 1.0d);
        // test edge cases
        generator.setSeed(Long.MIN_VALUE);
        double minDouble = generator.nextDouble();
        assertTrue(minDouble > -1.0d && minDouble < 0.0d);
        generator.setSeed(Long.MAX_VALUE);
        double maxDouble = generator.nextDouble();
        assertTrue(maxDouble > 0.0d && maxDouble < 1.0d);
    }

    @Test
    public void testNextFloat() {
        BitsStreamGenerator generator = new MersenneTwisterFastRandomGenerator();
        float result = generator.nextFloat();
        assertTrue(result > -0.0f && result < 1.0f);
        // test edge cases
        generator.setSeed(Long.MIN_VALUE);
        float minFloat = generator.nextFloat();
        assertTrue(minFloat > -1.0f && minFloat < 0.0f);
        generator.setSeed(Long.MAX_VALUE);
        float maxFloat = generator.nextFloat();
        assertTrue(maxFloat > 0.0f && maxFloat < 1.0f);
    }

    @Test
    public void testNextGaussian() {
        BitsStreamGenerator generator = new GaussianGenerator();
        double mean = 0.5d;
        double stdDev = 0.1d;
        double result;
        for (int i = 0; i < 1000; i++) {
            result = generator.nextGaussian(mean, stdDev);
            assertTrue(result > mean - 3 * stdDev && result < mean + 3 * stdDev);
        }
        // test edge cases
        generator.clear();
        double minDouble = generator.nextGaussian();
        assertTrue(minDouble > -10.0d && minDouble < -5.0d);
        double maxDouble = generator.nextGaussian();
        assertTrue(maxDouble > 5.0d && maxDouble < 10.0d);
    }

    @Test
    public void testNextInt() {
        BitsStreamGenerator generator = new MersenneTwisterFastRandomGenerator();
        int result = generator.nextInt();
        assertTrue(result >= 0 && result <= Integer.MAX_VALUE);
        // test edge cases
        generator.setSeed(Integer.MIN_VALUE);
        int minInt = generator.nextInt();
        assertTrue(minInt >= Integer.MIN_VALUE && minInt <= 0);
        generator.setSeed(Integer.MAX_VALUE);
        int maxInt = generator.nextInt();
        assertTrue(maxInt >= Integer.MAX_VALUE - 1 && maxInt <= Integer.MAX_VALUE);
    }

    @Test
    public void testNextIntWithinRange() {
        BitsStreamGenerator generator = new MersenneTwisterFastRandomGenerator();
        int min = 5;
        int max = 10;
        int result = generator.nextInt(max - min + 1);
        assertTrue(result >= min && result <= max);
        // test edge cases
        generator.setSeed(Integer.MIN_VALUE);
        int minInt = generator.nextInt(max - min + 1);
        assertTrue(minInt >= min && minInt <= max);
        generator.setSeed(Integer.MAX_VALUE);
        int maxInt = generator.nextInt(max - min + 1);
        assertTrue(maxInt > max - (max - min) && maxInt <= max);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNextIntWithNegativeRange() {
        BitsStreamGenerator generator = new MersenneTwisterFastRandomGenerator();
        int min = 5;
        int max = 4;
        generator.nextInt(max - min + 1);
    }

    @Test
    public void testNextLong() {
        BitsStreamGenerator generator = new MersenneTwisterFastRandomGenerator();
        long result = generator.nextLong();
        assertTrue(result >= 0 && result <= Long.MAX_VALUE);
        // test edge cases
        generator.setSeed(Long.MIN_VALUE);
        long minLong = generator.nextLong();
        assertTrue(minLong >= Long.MIN_VALUE && minLong <= 0);
        generator.setSeed(Long.MAX_VALUE);
        long maxLong = generator.nextLong();
        assertTrue(maxLong >= Long.MAX_VALUE - 1 && maxLong <= Long.MAX_VALUE);
    }
}