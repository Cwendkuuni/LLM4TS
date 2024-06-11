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
    }

    @Test
    public void testNextBytes() {
        BitsStreamGenerator generator = new MersenneTwisterFastRandomGenerator();
        byte[] bytes = new byte[10];
        generator.nextBytes(bytes);
        for (byte b : bytes) {
            assertNotEquals((byte) 0, b);
        }
    }

    @Test
    public void testNextDouble() {
        BitsStreamGenerator generator = new MersenneTwisterFastRandomGenerator();
        double expected = Math.random();
        double actual = generator.nextDouble();
        assertEquals(expected, actual, 0.0001);
    }

    @Test
    public void testNextFloat() {
        BitsStreamGenerator generator = new MersenneTwisterFastRandomGenerator();
        float expected = (float) Math.random();
        float actual = generator.nextFloat();
        assertEquals(expected, actual, 0.001f);
    }

    @Test
    public void testNextGaussian() {
        BitsStreamGenerator generator = new NormalDistributionGenerator();
        double expected = Math.random() * 3.0 - 1.5;
        double actual = generator.nextGaussian();
        assertEquals(expected, actual, 0.01);
    }

    @Test
    public void testNextInt() {
        BitsStreamGenerator generator = new MersenneTwisterFastRandomGenerator();
        int expected = (int) (Math.random() * 10);
        int actual = generator.nextInt(10);
        assertEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNextIntNegative() {
        BitsStreamGenerator generator = new MersenneTwisterFastRandomGenerator();
        generator.nextInt(-1);
    }

    @Test
    public void testNextIntPowerOfTwo() {
        BitsStreamGenerator generator = new MersenneTwisterFastRandomGenerator();
        int expected = (int) (Math.random() * 8);
        int actual = generator.nextInt(1 << 3);
        assertEquals(expected, actual);
    }

    @Test
    public void testNextIntNotPowerOfTwo() {
        BitsStreamGenerator generator = new MersenneTwisterFastRandomGenerator();
        int expected = 7;
        int actual;
        do {
            actual = generator.nextInt(11);
        } while (actual % 11 != 0);
        assertEquals(expected, actual);
    }

    @Test
    public void testNextLong() {
        BitsStreamGenerator generator = new MersenneTwisterFastRandomGenerator();
        long expected = Math.random() * 10L;
        long actual = generator.nextLong();
        assertEquals(expected, actual);
    }

    @Test
    public void testClear() {
        BitsStreamGenerator generator = new NormalDistributionGenerator();
        generator.nextGaussian();
        generator.clear();
        assertNull(generator.nextGaussian());
    }

}