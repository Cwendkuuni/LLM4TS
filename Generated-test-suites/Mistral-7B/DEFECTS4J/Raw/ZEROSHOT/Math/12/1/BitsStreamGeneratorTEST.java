package org.apache.commons.math3.random;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.util.FastMath;
import org.junit.Test;

/**
 * Test class for {@link BitsStreamGenerator}.
 */
public class BitsStreamGeneratorTest {

    /** Test instance. */
    private BitsStreamGenerator generator;

    /**
     * Sets up the test environment.
     */
    @SuppressWarnings("PMD.AssumeNull")
    @Before
    public void setUp() {
        generator = new BitsStreamGenerator() {
            @Override
            public void setSeed(int seed) {
                // not implemented
            }

            @Override
            public void setSeed(int[] seed) {
                // not implemented
            }

            @Override
            public void setSeed(long seed) {
                // not implemented
            }

            @Override
            protected int next(int bits) {
                return (int) (Math.random() * (1 << bits));
            }
        };
    }

    /**
     * Tests {@link BitsStreamGenerator#nextBoolean()}.
     */
    @Test
    public void testNextBoolean() {
        final boolean expected = Math.random() > 0.5;
        final boolean actual  = generator.nextBoolean();
        assertTrue(expected == actual);
    }

    /**
     * Tests {@link BitsStreamGenerator#nextBytes(byte[])}.
     */
    @Test
    public void testNextBytes() {
        final byte[] expected = new byte[]{(byte) 0x1a, (byte) 0x2b, (byte) 0x3c,
                (byte) 0x4d, (byte) 0x5e, (byte) 0x6f, (byte) 0x70, (byte) 0x71};
        final byte[] actual  = new byte[8];
        generator.nextBytes(actual);
        assertArrayEquals(expected, actual);
    }

    /**
     * Tests {@link BitsStreamGenerator#nextDouble()}.
     */
    @Test
    public void testNextDouble() {
        final double expected = Math.random();
        final double actual  = generator.nextDouble();
        assertEquals(expected, actual, 1e-14);
    }

    /**
     * Tests {@link BitsStreamGenerator#nextFloat()}.
     */
    @Test
    public void testNextFloat() {
        final float expected = (float) Math.random();
        final float actual  = generator.nextFloat();
        assertEquals(expected, actual, 1e-6f);
    }

    /**
     * Tests {@link BitsStreamGenerator#nextGaussian()}.
     */
    @Test
    public void testNextGaussian() {
        final double expectedMean = 0.0;
        final double expectedStdDev = 1.0;
        final int numberOfTests = 100000;
        for (int i = 0; i < numberOfTests; i++) {
            final double actual = generator.nextGaussian();
            final double actualMean = FastMath.mean(expectedMean, actual);
            final double actualStdDev = FastMath.stddev(expectedStdDev, actual);
            assertEquals(expectedMean, actualMean, 1e-5);
            assertEquals(expectedStdDev, actualStdDev, 1e-3);
        }
    }

    /**
     * Tests {@link BitsStreamGenerator#nextInt()}.
     */
    @Test
    public void testNextInt() {
        final int expected = (int) (Math.random() * Integer.MAX_VALUE);
        final int actual  = generator.nextInt();
        assertEquals(expected, actual);
    }

    /**
     * Tests {@link BitsStreamGenerator#nextInt(int)}.
     */
    @Test
    public void testNextIntN() {
        final int expected = 7;
        final int actual  = generator.nextInt(10);
        assertEquals(expected, actual);

        // negative test
        try {
            generator.nextInt(-1);
            fail();
        } catch (NotStrictlyPositiveException e) {
            // expected
        }

        final int expected2 = Integer.MAX_VALUE - 4;
        final int actual2  = generator.nextInt(4);
        assertEquals(expected2, actual2);
    }

    /**
     * Tests {@link BitsStreamGenerator#nextLong()}.
     */
    @Test
    public void testNextLong() {
        final long expected = (long) (Math.random() * Long.MAX_VALUE);
        final long actual  = generator.nextLong();
        assertEquals(expected, actual);
    }

    /**
     * Tests {@link BitsStreamGenerator#clear()}.
     */
    @Test
    public void testClear() {
        // generate a pair of independent gaussian numbers
        generator.nextGaussian();
        generator.nextGaussian();

        // clear the cache
        generator.clear();

        // the cache should be cleared
        assertEquals(Double.NaN, generator.nextGaussian(), 0.0);
        assertEquals(Double.NaN, generator.nextGaussian(), 0.0);
    }
}