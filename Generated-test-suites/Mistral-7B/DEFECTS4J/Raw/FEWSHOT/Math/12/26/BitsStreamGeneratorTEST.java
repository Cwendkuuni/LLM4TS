package org.apache.commons.math3.random;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;

import java.util.Random;

import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.random.BitsStreamGenerator;
import org.apache.commons.math3.util.FastMath;
import org.junit.Test;

/**
 * Test cases for {@link BitsStreamGenerator} class.
 */
public class BitsStreamGeneratorTest {

    /**
     * Test for {@link BitsStreamGenerator#nextBoolean()}.
     */
    @Test
    public void testNextBoolean() {
        BitsStreamGenerator generator = new MersenneTwisterFastRandomGenerator();
        assertTrue(generator.nextBoolean());
        assertTrue(generator.nextBoolean());
        assertFalse(generator.nextBoolean());
        assertFalse(generator.nextBoolean());
    }

    /**
     * Test for {@link BitsStreamGenerator#nextBytes(byte[])}.
     */
    @Test
    public void testNextBytes() {
        final int length = 10;
        byte[] expected = new byte[length];
        new Random().nextBytes(expected);

        BitsStreamGenerator generator = new MersenneTwisterFastRandomGenerator();
        generator.nextBytes(expected.clone());

        assertArrayEquals(expected, generator.nextBytes(expected.clone()));
        assertArrayEquals(expected, generator.nextBytes(new byte[length]));
    }

    /**
     * Test for {@link BitsStreamGenerator#nextDouble()}.
     */
    @Test
    public void testNextDouble() {
        BitsStreamGenerator generator = new MersenneTwisterFastRandomGenerator();
        double actual = generator.nextDouble();
        double expected = Math.random();

        assertEquals(expected, actual, 0.0001);
        assertNotEquals(expected, actual, 0.00001);
    }

    /**
     * Test for {@link BitsStreamGenerator#nextFloat()}.
     */
    @Test
    public void testNextFloat() {
        BitsStreamGenerator generator = new MersenneTwisterFastRandomGenerator();
        float actual = generator.nextFloat();
        float expected = (float) Math.random();

        assertEquals(expected, actual, 0.0001f);
        assertNotEquals(expected, actual, 0.00001f);
    }

    /**
     * Test for {@link BitsStreamGenerator#nextGaussian()}.
     */
    @Test
    public void testNextGaussian() {
        BitsStreamGenerator generator = new GaussianGenerator();

        double actual1 = generator.nextGaussian();
        double expected1 = FastMath.gauss(0.0, 1.0);
        assertEquals(expected1, actual1, 0.0001);

        double actual2 = generator.nextGaussian();
        double expected2 = FastMath.gauss(0.0, 1.0);
        assertEquals(expected2, actual2, 0.0001);

        assertNotEquals(actual1, actual2, 0.001);
    }

    /**
     * Test for {@link BitsStreamGenerator#nextInt()}.
     */
    @Test
    public void testNextInt() {
        BitsStreamGenerator generator = new MersenneTwisterFastRandomGenerator();

        for (int i = Integer.MIN_VALUE; i <= Integer.MAX_VALUE; i++) {
            int actual = generator.nextInt();
            assertEquals(i, actual);
        }
    }

    /**
     * Test for {@link BitsStreamGenerator#nextInt(int)}.
     */
    @Test
    public void testNextIntWithBound() {
        BitsStreamGenerator generator = new MersenneTwisterFastRandomGenerator();

        for (int i = 0; i <= 10; i++) {
            for (int test = 0; test < 1000; test++) {
                int actual = generator.nextInt(11);
                assertEquals(i, actual % 11);
            }
        }

        try {
            generator.nextInt(-1);
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }

        try {
            generator.nextInt(0);
            fail();
        } catch (NotStrictlyPositiveException e) {
            // expected
        }

        try {
            generator.nextInt(Integer.MAX_VALUE + 1);
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    /**
     * Test for {@link BitsStreamGenerator#nextLong()}.
     */
    @Test
    public void testNextLong() {
        BitsStreamGenerator generator = new MersenneTwisterFastRandomGenerator();
        long actual = generator.nextLong();
        long expected = new Random().nextLong();

        assertEquals(expected, actual);
    }

    /**
     * Test for {@link BitsStreamGenerator#clear()}.
     */
    @Test
    public void testClear() {
        BitsStreamGenerator generator = new GaussianGenerator();
        generator.nextGaussian();
        generator.clear();
        assertNull(generator.nextGaussian());
    }

}