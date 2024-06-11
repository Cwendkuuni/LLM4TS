package org.apache.commons.math3.random;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class BitsStreamGeneratorTest {

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
        byte[] expected = new byte[]{(byte) 0x01, (byte) 0x2a, (byte) 0x0f, (byte) 0x42};
        BitsStreamGenerator generator = new MersenneTwisterFastRandomGenerator();
        generator.nextBytes(expected);
        assertArrayEquals(expected, generator.nextBytes(expected.length));
    }

    @Test
    public void testNextDouble() {
        BitsStreamGenerator generator = new MersenneTwisterFastRandomGenerator();
        assertEquals(0.123456789, generator.nextDouble(), 0.00000001);
        assertEquals(0.123456789, generator.nextDouble(), 0.00000001);
        assertEquals(0.87654321, generator.nextDouble(), 0.00000001);
        assertEquals(0.87654321, generator.nextDouble(), 0.00000001);
    }

    @Test
    public void testNextGaussian() {
        BitsStreamGenerator generator = new NormalDistributionGenerator();
        assertEquals(0.123456789, generator.nextGaussian(), 0.0000001);
        assertEquals(-0.123456789, generator.nextGaussian(), 0.0000001);
        assertEquals(0.87654321, generator.nextGaussian(), 0.0000001);
        assertEquals(-0.87654321, generator.nextGaussian(), 0.0000001);
    }

    @Test
    public void testNextInt() {
        BitsStreamGenerator generator = new MersenneTwisterFastRandomGenerator();
        assertEquals(1, generator.nextInt());
        assertEquals(1, generator.nextInt());
        assertEquals(42, generator.nextInt(43));
        assertEquals(42, generator.nextInt(43));
        assertEquals(-42, generator.nextInt(43));
        assertEquals(-42, generator.nextInt(43));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNextIntNegativeSize() {
        BitsStreamGenerator generator = new MersenneTwisterFastRandomGenerator();
        generator.nextInt(-1);
    }

    @Test
    public void testNextLong() {
        BitsStreamGenerator generator = new MersenneTwisterFastRandomGenerator();
        assertEquals(1, generator.nextLong());
        assertEquals(1, generator.nextLong());
        assertEquals(42, generator.nextLong() & 0xffffffffL);
        assertEquals(42, generator.nextLong() & 0xffffffffL);
        assertEquals(-42, generator.nextLong() & 0xffffffffL);
        assertEquals(-42, generator.nextLong() & 0xffffffffL);
    }

    @Test
    public void testSetSeed() {
        BitsStreamGenerator generator = new MersenneTwisterFastRandomGenerator();
        long seed = 123456789;
        generator.setSeed(seed);
        assertEquals(seed, generator.nextLong());
    }

    @Test
    public void testSetSeedArray() {
        BitsStreamGenerator generator = new MersenneTwisterFastRandomGenerator();
        long[] seeds = {1, 2, 3, 4, 5};
        generator.setSeed(seeds);
        long[] expected = new long[]{1, 2, 3, 4, 5};
        for (int i = 0; i < seeds.length; i++) {
            assertEquals(expected[i], generator.nextLong());
        }
    }
}