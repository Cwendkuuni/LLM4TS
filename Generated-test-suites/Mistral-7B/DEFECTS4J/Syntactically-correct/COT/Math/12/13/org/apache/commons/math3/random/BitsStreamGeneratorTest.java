package org.apache.commons.math3.random;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Random;

import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.random.BitsStreamGenerator;
import org.apache.commons.math3.random.MersenneTwister;
import org.apache.commons.math3.util.FastMath;
import org.apache.commons.math3.util.Precision;
import org.junit.Before;
import org.junit.Test;

/**
 * Test class for {@link BitsStreamGenerator}.
 */
public class BitsStreamGeneratorTest {

    /** The instance under test. */
    private BitsStreamGenerator generator;

    /** The random number generator used for comparison. */
    private Random random;

    @Before
    public void setUp() {
        generator = new MersenneTwister();
        random = new Random();
    }

    @Test
    public void testSetSeed() {
        int seed = 12345;
        generator.setSeed(seed);
        long expectedSeed = FastMath.nextLong((long) seed);
        assertEquals(expectedSeed, generator.nextLong());
        assertEquals(seed, generator.nextInt());
        generator.clear();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetSeedNegative() {
        generator.setSeed(-1);
    }

    @Test
    public void testNextBoolean() {
        assertTrue(generator.nextBoolean());
        assertTrue(generator.nextBoolean());
        assertFalse(generator.nextBoolean());
        generator.clear();
    }

    @Test
    public void testNextBytes() {
        byte[] expected = new byte[] { 1, 2, 3, 4, 5 };
        generator.nextBytes(expected);
        byte[] actual = new byte[expected.length];
        generator.nextBytes(actual);
        assertArrayEquals(expected, actual);
        generator.clear();
    }

    @Test
    public void testNextDouble() {
        final double epsilon = 1.0e-9;
        double actual = generator.nextDouble();
        double expected = random.nextDouble();
        assertEquals(expected, Precision.isClose(actual, expected, epsilon));
        generator.clear();
    }

    @Test
    public void testNextFloat() {
        float actual = generator.nextFloat();
        float expected = random.nextFloat();
        assertEquals(expected, actual, 0.001f);
        generator.clear();
    }

    @Test
    public void testNextGaussian() {
        final double epsilon = 1.0e-6;
        double actual = generator.nextGaussian();
        double expected = random.nextGaussian();
        assertEquals(expected, Precision.isClose(actual, expected, epsilon));
        generator.clear();
    }

    @Test
    public void testNextInt() {
        int min = Integer.MIN_VALUE;
        int max = Integer.MAX_VALUE;
        int actual = generator.nextInt();
        int expected = random.nextInt();
        assertTrue(min <= expected && expected <= max);
        generator.clear();

        int n = 10;
        int actualInt = generator.nextInt(n);
        int expectedInt = random.nextInt(n);
        assertEquals(expectedInt, actualInt);
        generator.clear();

        int powerOfTwo = 32;
        int actualPowerOfTwo = generator.nextInt(powerOfTwo);
        int expectedPowerOfTwo = random.nextInt(powerOfTwo);
        assertEquals(expectedPowerOfTwo, actualPowerOfTwo);
        assertEquals(actualPowerOfTwo, actualPowerOfTwo << 31 >> 31);
        generator.clear();
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testNextIntNegative() {
        generator.nextInt(-1);
    }

    @Test
    public void testNextLong() {
        long actual = generator.nextLong();
        long expected = random.nextLong();
        assertEquals(expected, actual);
        generator.clear();
    }

    @Test
    public void testClear() {
        generator.nextInt();
        generator.clear();
        long actual = generator.nextLong();
        assertEquals(0L, actual);
        generator.clear();
    }

}