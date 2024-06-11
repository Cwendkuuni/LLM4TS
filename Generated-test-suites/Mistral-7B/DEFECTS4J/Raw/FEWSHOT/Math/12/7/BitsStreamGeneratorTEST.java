package org.apache.commons.math3.random;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Random;

import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.random.BitsStreamGenerator;
import org.apache.commons.math3.random.MersenneTwister;
import org.apache.commons.math3.util.FastMath;
import org.junit.Test;

/**
 * Test cases for {@link BitsStreamGenerator}.
 */
public class BitsStreamGeneratorTest {

    /**
     * Test for {@link BitsStreamGenerator#nextBoolean()}.
     */
    @Test
    public void testNextBoolean() {
        BitsStreamGenerator generator = new MersenneTwister();
        assertTrue(generator.nextBoolean());
        assertTrue(generator.nextBoolean());
        assertFalse(generator.nextBoolean());
    }

    /**
     * Test for {@link BitsStreamGenerator#nextBytes(byte[])}.
     */
    @Test
    public void testNextBytes() {
        final byte[] expected = new byte[] { 0x2a, 0x65, 0x00, 0x03, 0x76, 0x65, 0x00, 0x13 };
        BitsStreamGenerator generator = new MersenneTwister();
        generator.nextBytes(expected);
        assertArrayEquals(expected, expected);
    }

    /**
     * Test for {@link BitsStreamGenerator#nextDouble()}.
     */
    @Test
    public void testNextDouble() {
        BitsStreamGenerator generator = new MersenneTwister();
        double actual = generator.nextDouble();
        assertEquals(0.17438153, actual, 0.0000001);
    }

    /**
     * Test for {@link BitsStreamGenerator#nextFloat()}.
     */
    @Test
    public void testNextFloat() {
        BitsStreamGenerator generator = new MersenneTwister();
        float actual = generator.nextFloat();
        assertEquals(0.5123456f, actual, 0.00001f);
    }

    /**
     * Test for {@link BitsStreamGenerator#nextGaussian()}.
     */
    @Test
    public void testNextGaussian() {
        BitsStreamGenerator generator = new MersenneTwister();
        double actual = generator.nextGaussian();
        double expected = 0.84567897;
        assertEquals(expected, actual, 0.00001);
        generator.clear();
        expected = 1.23456789;
        actual = generator.nextGaussian();
        assertEquals(expected, actual, 0.00001);
    }

    /**
     * Test for {@link BitsStreamGenerator#nextInt()}.
     */
    @Test
    public void testNextInt() {
        BitsStreamGenerator generator = new MersenneTwister();
        int actual = generator.nextInt();
        int expected = 123;
        assertEquals(expected, actual);
    }

    /**
     * Test for {@link BitsStreamGenerator#nextInt(int)}.
     */
    @Test
    public void testNextIntN() {
        BitsStreamGenerator generator = new MersenneTwister();
        int actual, expected;
        // test positive number
        expected = 123;
        actual = generator.nextInt(expected);
        assertEquals(expected, actual);
        // test zero
        expected = 0;
        actual = generator.nextInt(expected);
        assertEquals(expected, actual);
        // test negative number
        expected = -123;
        try {
            actual = generator.nextInt(expected);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
        // test power of 2
        expected = 1 << 31;
        actual = generator.nextInt(expected);
        assertEquals(expected, actual);
        // test non power of 2
        expected = 127;
        actual = generator.nextInt(expected);
        assertEquals(expected, actual);
    }

    /**
     * Test for {@link BitsStreamGenerator#nextLong()}.
     */
    @Test
    public void testNextLong() {
        BitsStreamGenerator generator = new MersenneTwister();
        long actual = generator.nextLong();
        long expected = Long.parseUnsignedLong("1234567890123456", new Random(1));
        assertEquals(expected, actual);
    }

}