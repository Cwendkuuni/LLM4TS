package org.apache.commons.math3.random;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class BitsStreamGeneratorTest {

    @Test
    public void testNextBoolean() {
        BitsStreamGenerator generator = new MersenneTwisterFastRandomGenerators_SpecifiedSeed();
        assertTrue(generator.nextBoolean());
        assertTrue(generator.nextBoolean());
        assertFalse(generator.nextBoolean());
        assertFalse(generator.nextBoolean());
        assertFalse(generator.nextBoolean());
    }

    @Test
    public void testNextBytes() {
        byte[] expected = new byte[]{-31, 28, -85, 6, -24, 105, 117, 111, -102, -115, -124, -54, 101, 102, 107, 121};
        BitsStreamGenerator generator = new MersenneTwisterFastRandomGenerators_SpecifiedSeed();
        generator.nextBytes(expected.length);
        assertArrayEquals(expected, generator.getState());
    }

    @Test
    public void testNextDouble() {
        BitsStreamGenerator generator = new MersenneTwisterFastRandomGenerators_SpecifiedSeed();
        assertEquals(1.23456789, generator.nextDouble(), 0.0000001);
        assertEquals(1.23456789, generator.nextDouble(), 0.0000001);
        assertEquals(-1.23456789, generator.nextDouble(), 0.0000001);
        assertEquals(-1.23456789, generator.nextDouble(), 0.0000001);
    }

    @Test
    public void testNextDoubleBounded() {
        BitsStreamGenerator generator = new MersenneTwisterFastRandomGenerators_SpecifiedSeed();
        assertEquals(0.0, generator.nextDouble(1.0), 0.0);
        assertEquals(0.5, generator.nextDouble(1.0), 0.0);
        assertEquals(1.0, generator.nextDouble(1.0), 0.0);
    }

    @Test
    public void testNextDoubleNegativeBounded() {
        BitsStreamGenerator generator = new MersenneTwisterFastRandomGenerators_SpecifiedSeed();
        assertEquals(-0.5, generator.nextDouble(-1.0), 0.0);
        assertEquals(-0.5, generator.nextDouble(-1.0), 0.0);
        assertEquals(-1.0, generator.nextDouble(-1.0), 0.0);
    }

    @Test
    public void testNextFloat() {
        BitsStreamGenerator generator = new MersenneTwisterFastRandomGenerators_SpecifiedSeed();
        assertEquals(1.234567f, generator.nextFloat(), 0.001f);
        assertEquals(1.234567f, generator.nextFloat(), 0.001f);
        assertEquals(-1.234567f, generator.nextFloat(), 0.001f);
        assertEquals(-1.234567f, generator.nextFloat(), 0.001f);
    }

    @Test
    public void testNextGaussian() {
        BitsStreamGenerator generator = new MersenneTwisterFastRandomGenerators_SpecifiedSeed();
        assertEquals(0.12345678, generator.nextGaussian(), 0.0001);
        assertEquals(1.23456789, generator.nextGaussian(), 0.0001);
        assertEquals(-0.12345678, generator.nextGaussian(), 0.0001);
        assertEquals(-1.23456789, generator.nextGaussian(), 0.0001);
    }

    @Test
    public void testNextInt() {
        BitsStreamGenerator generator = new MersenneTwisterFastRandomGenerators_SpecifiedSeed();
        assertEquals(42, generator.nextInt(100));
        assertEquals(0, generator.nextInt(100));
        assertEquals(1, generator.nextInt(100));
        assertEquals(Integer.MAX_VALUE, generator.nextInt(Integer.MAX_VALUE));
    }

    @Test
    public void testNextIntNegativeBounded() {
        BitsStreamGenerator generator = new MersenneTwisterFastRandomGenerators_SpecifiedSeed();
        assertEquals(-1, generator.nextInt(1), 0);
        assertEquals(-1, generator.nextInt(1), 0);
        assertEquals(0, generator.nextInt(1), 0);
        assertEquals(0, generator.nextInt
            (1), 0);
    }

    @Test
    public void testNextIntPositiveBounded() {
        BitsStreamGenerator generator = new MersenneTwisterFastRandomGenerators_SpecifiedSeed();
        assertEquals(0, generator.nextInt(5), 0);
        assertEquals(1, generator.nextInt(5), 0);
        assertEquals(2, generator.nextInt(5), 0);
        assertEquals(3, generator.nextInt(5), 0);
        assertEquals(4, generator.nextInt(5), 0);
    }

    @Test
    public void testNextLong() {
        BitsStreamGenerator generator = new MersenneTwisterFastRandomGenerators_SpecifiedSeed();
        assertEquals(1234567890123456789L, generator.nextLong(), 0);
        assertEquals(1234567890123456789L, generator.nextLong(), 0);
        assertEquals(-1234567890123456789L, generator.nextLong(), 0);
        assertEquals(-1234567890123456789L, generator.nextLong(), 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNextIntNegativeBound() {
        BitsStreamGenerator generator = new MersenneTwisterFastRandomGenerators_SpecifiedSeed();
        generator.nextInt(-1);
    }

    @Test
    public void testSetSeed() {
        BitsStreamGenerator generator = new MersenneTwisterFastRandomGenerators_SpecifiedSeed();
        long seed = 1234567890123456789L;
        generator.setSeed(seed);
        assertEquals(seed, generator.getSeed(), 0);
    }

    @Test
    public void testSetSeedArray() {
        BitsStreamGenerator generator = new MersenneTwisterFastRandomGenerators_SpecifiedSeed();
        long[] seeds = new long[]{1234567890123456789L, 1234567890123456788L};
        generator.setSeed(seeds);
        long[] expected = new long[]{1234567890123456789L, 1234567890123456788L};
        assertEquals(expected[0], generator.getSeed(), 0);
        assertEquals(expected[1], generator.getSeed(), 0);
    }

    @Test
    public void testSetSeedLong() {
        BitsStreamGenerator generator = new MersenneTwisterFastRandomGenerators_SpecifiedSeed();
        long seed = 1234567890123456789L;
        generator.setSeed(seed);
        assertEquals(seed, generator.getSeed(), 0);
    }

    @Test
    public void testClear() {
        BitsStreamGenerator generator = new MersenneTwisterFastRandomGenerators_SpecifiedSeed();
        generator.clear();
        assertEquals(Double.NaN, generator.nextGaussian());
    }

}