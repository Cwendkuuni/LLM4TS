package org.apache.commons.math3.random;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Random;

import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.util.FastMath;
import org.apache.commons.math3.util.Precision;
import org.junit.Before;
import org.junit.Test;

/**
 * Test class for {@link BitsStreamGenerator}.
 */
public class BitsStreamGeneratorTest {
    private Random random;

    @Before
    public void setUp() {
        random = new BitsStreamGenerator();
    }

    @Test
    public void testNextBoolean() {
        for (int i = 0; i < 10; i++) {
            assertEquals(Math.random(), random.nextBoolean());
        }
    }

    @Test
    public void testNextBytes() {
        final byte[] expected = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 };
        final byte[] actual = new byte[expected.length];
        random.nextBytes(actual);
        assertArrayEquals(expected, actual);
    }

    @Test
    public void testNextDouble() {
        for (int i = 0; i < 10; i++) {
            double value = random.nextDouble();
            assertTrue(Precision.isClose(1.0, value + 1.0E-6, 1.0E-6));
            assertTrue(Precision.isClose(0.0, 1.0 - value, 1.0E-6));
        }
    }

    @Test
    public void testNextFloat() {
        for (int i = 0; i < 10; i++) {
            float value = random.nextFloat();
            assertTrue(Precision.isClose(1.0f, value + 1.0E-6f, 1.0E-6f));
            assertTrue(Precision.isClose(0.0f, 1.0f - value, 1.0E-6f));
        }
    }

    @Test
    public void testNextGaussian() {
        final double mean = 0.0;
        final double stdDev = 1.0;
        for (int i = 0; i < 10; i++) {
            final double value = random.nextGaussian();
            assertEquals(mean, value, stdDev);
        }
    }

    @Test
    public void testNextInt() {
        for (int i = 0; i < 10; i++) {
            assertEquals(new Random().nextInt(), random.nextInt());
        }
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testNextIntNegative() {
        random.nextInt(-1);
    }

    @Test
    public void testNextIntPowerOfTwo() {
        for (int i = 0; i < 10; i++) {
            final int n = 1 << i;
            assertEquals(n, random.nextInt(n));
        }
    }

    @Test
    public void testNextIntRandom() {
        final int max = 10;
        for (int i = 0; i < 10; i++) {
            final int n = random.nextInt(max);
            assertEquals(n, random.nextInt(max));
            assertEquals(Math.abs(max - n), random.nextInt(Math.abs(max - n)));
            assertEquals(n, random.nextInt(n));
            assertEquals(Math.abs(max - n), random.nextInt(Math.abs(max - n)));
            assertEquals(n, random.nextInt(n + 1));
            assertEquals(Math.abs(max - n), random.nextInt(Math.abs(max - n) + 1));
        }
    }

    @Test
    public void testNextLong() {
        for (int i = 0; i < 10; i++) {
            assertEquals(new Random().nextLong(), random.nextLong());
        }
    }

    @Test
    public void testClear() {
        random.nextGaussian();
        random.clear();
        assertFalse(Double.isNaN(random.nextGaussian()));
    }
}