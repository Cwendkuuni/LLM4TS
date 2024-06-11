package org.apache.commons.math3.random;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.util.Random;

import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.random.BitsStreamGenerator;
import org.apache.commons.math3.random.MersenneTwisterFast;
import org.apache.commons.math3.util.FastMath;
import org.junit.Before;
import org.junit.Test;

/**
 * Test for {@link BitsStreamGenerator} class.
 */
public class BitsStreamGeneratorTest {

    private BitsStreamGenerator generator;

    @Before
    public void setup() {
        generator = new MersenneTwisterFast();
    }

    /**
     * Test for {@link BitsStreamGenerator#nextBoolean()}.
     */
    @Test
    public void testNextBoolean() {
        assertFalse(generator.nextBoolean());
        assertTrue(generator.nextBoolean());
    }

    /**
     * Test for {@link BitsStreamGenerator#nextBytes(byte[])}.
     */
    @Test
    public void testNextBytes() {
        final byte[] expected = {0, 1, 2, 3};
        final byte[] actual   = new byte[4];
        generator.nextBytes(actual);
        assertArrayEquals(expected, actual);
    }

    /**
     * Test for {@link BitsStreamGenerator#nextDouble()}.
     */
    @Test
    public void testNextDouble() {
        final double expected = 0.123456789;
        final double actual   = generator.nextDouble();
        assertEquals(expected, actual, 1e-9);
    }

    /**
     * Test for {@link BitsStreamGenerator#nextFloat()}.
     */
    @Test
    public void testNextFloat() {
        final float expected = 0.123456f;
        final float actual   = generator.nextFloat();
        assertEquals(expected, actual, 1e-6f);
    }

    /**
     * Test for {@link BitsStreamGenerator#nextGaussian()}.
     */
    @Test
    public void testNextGaussian() {
        final double expectedMean = 0.0;
        final double expectedStdDev = 1.0;
        final int numTests = 10000;
        int count = 0;
        for (int i = 0; i < numTests; i++) {
            final double actual = generator.nextGaussian();
            final double zScore = (actual - expectedMean) / expectedStdDev;
            assertTrue(zScore > -3.0 && zScore < 3.0);
            count++;
        }
        assertEquals("Generates " + numTests + " gaussians.", numTests, count);
    }

    /**
     * Test for {@link BitsStreamGenerator#nextInt()}.
     */
    @Test
    public void testNextInt() {
        final int expected = 123;
        final int actual   = generator.nextInt();
        assertEquals(expected, actual);
    }

    /**
     * Test for {@link BitsStreamGenerator#nextInt(int)}.
     */
    @Test
    public void testNextInt_int() {
        final int expected = 123;
        final int n          = 42;
        final int actual     = generator.nextInt(n);
        assertTrue(actual >= 0 && actual < n);
        assertEquals(expected, actual % n);
    }

    /**
     * Test for {@link BitsStreamGenerator#nextInt(int)}.
     * When the input is negative.
     */
    @Test(expected = NotStrictlyPositiveException.class)
    public void testNextInt_intNegative() {
        generator.nextInt(-42);
    }

    /**
     * Test for {@link BitsStreamGenerator#nextLong()}.
     */
    @Test
    public void testNextLong() {
        final long expected = 12345678912345L;
        final long actual   = generator.nextLong();
        assertEquals(expected, actual);
    }

    /**
     * Test for {@link BitsStreamGenerator#clear()}.
     */
    @Test
    public void testClear() {
        final int numTests = 10;
        int count = 0;
        for (int i = 0; i < numTests; i++) {
            final double actualGaussian = generator.nextGaussian();
            generator.clear();
            final double actualGaussian2 = generator.nextGaussian();
            assertNotSame(actualGaussian, actualGaussian2);
            count++;
        }
        assertEquals("Generates " + numTests + " gaussians.", numTests, count);
    }

}