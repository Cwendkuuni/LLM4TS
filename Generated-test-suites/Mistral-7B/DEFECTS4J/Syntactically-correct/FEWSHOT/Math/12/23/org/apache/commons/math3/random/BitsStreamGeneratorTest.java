package org.apache.commons.math3.random;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class BitsStreamGeneratorTest {

    @Test
    public void testNextBoolean() {
        BitsStreamGenerator generator = new MersenneTwisterFastRandomGenerator();
        assertEquals(true, generator.nextBoolean());
        assertEquals(false, generator.nextBoolean());
        assertEquals(true, generator.nextBoolean());
        assertEquals(false, generator.nextBoolean());
    }

    @Test
    public void testNextBytes() {
        BitsStreamGenerator generator = new MersenneTwisterFastRandomGenerator();
        byte[] bytes1 = new byte[10];
        generator.nextBytes(bytes1);
        byte[] bytes2 = new byte[10];
        generator.nextBytes(bytes2);
        assertArrayEquals(bytes1, bytes2);

        byte[] bytes3 = new byte[1];
        generator.nextBytes(bytes3);
        assertEquals(bytes3[0], (byte) 0x0); // first byte is 0
    }

    @Test
    public void testNextDouble() {
        BitsStreamGenerator generator = new MersenneTwisterFastRandomGenerator();
        double expected = 0.123456789;
        double actual = generator.nextDouble();
        assertEquals(expected, actual, 0.0000001);
    }

    @Test
    public void testNextFloat() {
        BitsStreamGenerator generator = new MersenneTwisterFastRandomGenerator();
        float expected = 0.123456f;
        float actual = generator.nextFloat();
        assertEquals(expected, actual, 0.000001f);
    }

    @Test
    public void testNextGaussian() {
        BitsStreamGenerator generator = new NormalDistributionGenerator();
        double actual = generator.nextGaussian();
        assertEquals(0.0, actual, 0.0001); // zero mean
        generator.clear();
        double actual2 = generator.nextGaussian();
        assertEquals(1.0, actual2, 0.0001); // positive standard deviation
        generator.clear();
        double actual3 = generator.nextGaussian();
        assertEquals(-1.0, actual3, 0.0001); // negative standard deviation
    }

    @Test
    public void testNextInt() {
        BitsStreamGenerator generator = new MersenneTwisterFastRandomGenerator();
        assertEquals(1, generator.nextInt(3));
        assertEquals(2, generator.nextInt(3));
        assertEquals(1, generator.nextInt(3));

        assertEquals(Integer.MIN_VALUE, generator.nextInt(Integer.MIN_VALUE + 1));
        assertEquals(Integer.MAX_VALUE, generator.nextInt(Integer.MAX_VALUE + 1));
        assertEquals(Integer.MIN_VALUE, generator.nextInt(-1));
        assertEquals(0, generator.nextInt(0));
    }

    @Test
    public void testNextIntWithPositivePowerOfTwo() {
        BitsStreamGenerator generator = new MersenneTwisterFastRandomGenerator();
        int pow2 = 32;
        int expected = pow2;
        int actual = generator.nextInt(expected);
        assertEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNextIntWithNegativeNumber() {
        BitsStreamGenerator generator = new MersenneTwisterFastRandomGenerator();
        generator.nextInt(-1);
    }

    @Test
    public void testNextIntWithZero() {
        BitsStreamGenerator generator = new MersenneTwisterFastRandomGenerator();
        int expected = 0;
        int actual = generator.nextInt(expected);
        assertEquals(expected, actual);
    }

    @Test
    public void testNextIntWithLargeNumber() {
        BitsStreamGenerator generator = new MersenneTwisterFastRandomGenerator();
        long expected = Long.MAX_VALUE;
        long actual = generator.nextLong();
        assertEquals(expected, actual);
    }

    @Test
    public void testNextLong() {
        BitsStreamGenerator generator = new MersenneTwisterFastRandomGenerator();
        long expected = 123456789l;
        long actual = generator.nextLong();
        assertEquals(expected, actual);
    }

    @Test
    public void testNextLongWithNegativeNumber() {
        BitsStreamGenerator generator = new MersenneTwisterFastRandomGenerator();
        generator.nextLong(); // clear the cache
        long expected = Long.MIN_VALUE;
        long actual = generator.nextLong();
        assertEquals(expected, actual);
    }
}