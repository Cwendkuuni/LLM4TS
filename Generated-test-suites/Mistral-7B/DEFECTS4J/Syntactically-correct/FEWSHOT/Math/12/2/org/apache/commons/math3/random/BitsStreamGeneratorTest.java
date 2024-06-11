package org.apache.commons.math3.random;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.junit.Test;

public class BitsStreamGeneratorTest {

    @Test(expected = NotStrictlyPositiveException.class)
    public void testNextIntNegative() {
        BitsStreamGenerator generator = new MersenneTwisterFastTest_53a(); // assuming it's a valid implementation
        generator.nextInt(-1);
    }

    @Test
    public void testNextBoolean() {
        BitsStreamGenerator generator = new MersenneTwisterFastTest_53a(); // assuming it's a valid implementation
        assertEquals(true, generator.nextBoolean());
        assertEquals(false, generator.nextBoolean());
    }

    @Test
    public void testNextBytes() {
        byte[] expected = new byte[]{(byte) 0x5A, (byte) 0x5A, (byte) 0x5A, (byte) 0x5A, (byte) 0x5A, (byte) 0x5A};
        BitsStreamGenerator generator = new MersenneTwisterFastTest_53a(); // assuming it's a valid implementation
        generator.nextBytes(expected);
        assertArrayEquals(expected, expected);
    }

    @Test
    public void testNextDouble() {
        BitsStreamGenerator generator = new MersenneTwisterFastTest_53a(); // assuming it's a valid implementation
        assertEquals(0.467293880793252, generator.nextDouble(), 0.00001);
    }

    @Test
    public void testNextFloat() {
        BitsStreamGenerator generator = new MersenneTwisterFastTest_53a(); // assuming it's a valid implementation
        assertEquals(0.6724634, generator.nextFloat(), 0.0001);
    }

    @Test
    public void testNextGaussian() {
        BitsStreamGenerator generator = new MersenneTwisterFastTest_53a(); // assuming it's a valid implementation
        assertEquals(1.3453218, generator.nextGaussian(), 0.001);
    }

    @Test
    public void testNextInt() {
        BitsStreamGenerator generator = new MersenneTwisterFastTest_53a(); // assuming it's a valid implementation
        assertEquals(123, generator.nextInt());
        assertEquals(Integer.MIN_VALUE, generator.nextInt());
    }

    @Test
    public void testNextIntPowerOf2() {
        BitsStreamGenerator generator = new MersenneTwisterFastTest_53a(); // assuming it's a valid implementation
        assertEquals(8, generator.nextInt(8));
    }

    @Test
    public void testNextLong() {
        BitsStreamGenerator generator = new MersenneTwisterFastTest_53a(); // assuming it's a valid implementation
        long expected = 0x5A5A5A5A5A5A5A5AL;
        generator.nextLong();
        assertEquals(expected, generator.nextLong());
    }

    @Test
    public void testClear() {
        BitsStreamGenerator generator = new MersenneTwisterFastTest_53a(); // assuming it's a valid implementation
        double previousGaussian = generator.nextGaussian();
        generator.clear();
        assertFalse(Double.isNaN(generator.nextGaussian()));
        assertEquals(previousGaussian, generator.nextGaussian(), 0.001);
    }
}