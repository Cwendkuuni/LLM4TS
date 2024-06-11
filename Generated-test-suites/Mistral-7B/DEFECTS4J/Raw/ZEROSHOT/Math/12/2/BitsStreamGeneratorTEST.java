package org.apache.commons.math3.random;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.util.FastMath;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests for {@link BitsStreamGenerator} class.
 */
public class BitsStreamGeneratorTest {

    private BitsStreamGenerator generator;

    @Before
    public void setUp() {
        generator = new BitsStreamGenerator() {
            @Override
            public void setSeed(int seed) {
                // not used in this test
            }

            @Override
            public void setSeed(long seed) {
                // not used in this test
            }

            @Override
            public void setSeed(int[] seed) {
                // not used in this test
            }

            @Override
            protected int next(int bits) {
                throw new UnsupportedOperationException("not implemented");
            }
        };
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetSeedInt() {
        generator.setSeed(123);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetSeedIntArray() {
        generator.setSeed(new int[]{1, 2, 3});
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetSeedLong() {
        generator.setSeed(123L);
    }

    @Test
    public void testNextBoolean() {
        for (int i = 0; i < 10; i++) {
            assertTrue(generator.nextBoolean());
        }
        for (int i = 0; i < 10; i++) {
            assertFalse(generator.nextBoolean());
        }
    }

    @Test
    public void testNextBytes() {
        byte[] expected = new byte[10];
        generator.nextBytes(expected);
        byte[] actual = new byte[10];
        generator.nextBytes(actual);
        assertArrayEquals(expected, actual);
    }

    @Test
    public void testNextDouble() {
        for (int i = 0; i < 10; i++) {
            double expected = Math.random();
            double actual = generator.nextDouble();
            assertEquals(expected, actual, 1e-6);
        }
    }

    @Test
    public void testNextFloat() {
        for (int i = 0; i < 10; i++) {
            float expected = (float) Math.random();
            float actual = generator.nextFloat();
            assertEquals(expected, actual, 1e-6f);
        }
    }

    @Test
    public void testNextGaussian() {
        for (int i = 0; i < 10; i++) {
            double actual = generator.nextGaussian();
            double expectedMean = 0;
            double expectedStdDev = 1;
            assertEquals(expectedMean, actual, 1e-6);
            assertEquals(expectedStdDev, FastMath.sqrt(FastMath.pow(actual, 2) + FastMath.pow(generator.nextGaussian(), 2)) / 2, 1e-6);
        }
    }

    @Test
    public void testNextInt() {
        for (int i = 0; i < 10; i++) {
            int expected = (int) (Math.random() * Integer.MAX_VALUE);
            int actual = generator.nextInt();
            assertEquals(expected, actual);
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNextIntNegative() {
        generator.nextInt(-1);
    }

    @Test
    public void testNextIntPowerOfTwo() {
        int expected = 1 << 10;
        int actual = generator.nextInt();
        assertEquals(expected, actual);
    }

    @Test
    public void testNextIntNotPowerOfTwo() {
        int expected = 17;
        int actual;
        do {
            actual = generator.nextInt();
        } while (actual % 17 != 0);
        assertEquals(expected, actual / 17 * 17);
    }

    @Test
    public void testNextLong() {
        for (int i = 0; i < 10; i++) {
            long expected = (long) (Math.random() * Long.MAX_VALUE);
            long actual = generator.nextLong();
            assertEquals(expected, actual);
        }
    }

    @Test
    public void testClear() {
        generator.nextGaussian();
        generator.clear();
        assertEquals(Double.NaN, generator.nextGaussian(), 0);
    }

}