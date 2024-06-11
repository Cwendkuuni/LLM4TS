package org.apache.commons.math3.random;

import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.util.FastMath;
import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Test class for {@link BitsStreamGenerator}.
 */
public class BitsStreamGeneratorTest {

    /** Test instance. */
    private final BitsStreamGenerator generator = new MersenneTwisterFastRandomGenerators_016();

    /**
     * Test {@link BitsStreamGenerator#nextBoolean()}.
     */
    @Test
    public void testNextBoolean() {
        for (int i = 0; i < 10000; i++) {
            final boolean result = generator.nextBoolean();
            final boolean expected = Math.random() < 0.5;
            assertEquals(expected, result);
        }
    }

    /**
     * Test {@link BitsStreamGenerator#nextBytes(byte[])}.
     */
    @Test
    public void testNextBytes() {
        final byte[] expected = new byte[10];
        generator.nextBytes(expected);
        final byte[] result = new byte[10];
        generator.nextBytes(result);
        assertArrayEquals(expected, result);

        final byte[] seed = new byte[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 };
        generator.setSeed(seed);
        final byte[] result2 = new byte[10];
        generator.nextBytes(result2);
        assertArrayEquals(seed, result2);

        final byte[] empty = new byte[0];
        generator.nextBytes(empty);
        // empty array test is not meaningful

        try {
            generator.nextBytes(null);
            fail("Argument cannot be null.");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    /**
     * Test {@link BitsStreamGenerator#nextDouble()}.
     */
    @Test
    public void testNextDouble() {
        for (int i = 0; i < 10000; i++) {
            final double result = generator.nextDouble();
            final double expected = Math.random();
            assertEquals(expected, result, 1e-12);
        }
    }

    /**
     * Test {@link BitsStreamGenerator#nextFloat()}.
     */
    @Test
    public void testNextFloat() {
        for (int i = 0; i < 10000; i++) {
            final float result = generator.nextFloat();
            final float expected = (float) (Math.random() * 0x1.0p-23f);
            assertEquals(expected, result, 1e-6f);
        }
    }

    /**
     * Test {@link BitsStreamGenerator#nextGaussian()}.
     */
    @Test
    public void testNextGaussian() {
        for (int i = 0; i < 10000; i++) {
            final double result = generator.nextGaussian();
            final double mean = 0.0;
            final double stdDev = 1.0;
            final double expected = FastMath.gauss(mean, stdDev);
            assertEquals(expected, result, 1e-6);
        }
    }

    /**
     * Test {@link BitsStreamGenerator#nextInt()}.
     */
    @Test
    public void testNextInt() {
        for (int i = 0; i < 10000; i++) {
            final int result = generator.nextInt();
            final int expected = (int) (Math.random() * Integer.MAX_VALUE);
            assertEquals(expected, result);
        }
    }

    /**
     * Test {@link BitsStreamGenerator#nextInt(int)}.
     */
    @Test
    public void testNextIntInt() {
        for (int n = 1; n <= Integer.MAX_VALUE / 2; n *= 2) {
            final int result = generator.nextInt(n);
            final int expected = (int) (Math.random() * (Integer.MAX_VALUE / n)) % n;
            assertEquals(expected, result);
        }
        try {
            generator.nextInt(-1);
            fail("Argument must be positive.");
        } catch (IllegalArgumentException e) {
            // expected
        }
        try {
            generator.nextInt(Integer.MAX_VALUE + 1);
            fail("Argument cannot be greater than Integer.MAX_VALUE.");
        } catch (IllegalArgumentException e) {
            // expected
        }
        try {
            generator.nextInt(0);
            fail("Argument must be positive.");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    /**
     * Test {@link BitsStreamGenerator#nextLong()}.
     */
    @Test
    public void testNextLong() {
        for (int i = 0; i < 10000; i++) {
            final long result = generator.nextLong();
            final long expected = (long) (Math.random() * Long.MAX_VALUE);
            assertEquals(expected, result);
        }
    }

    /**
     * Test {@link BitsStreamGenerator#setSeed(int)}.
     */
    @Test
    public void testSetSeedInt() {
        final int seed = 123456789;
        generator.setSeed(seed);
        final byte[] expected = new byte[] {
            (byte) ((seed >>> 0 << 24) & 0xff),
            (byte) ((seed >>> 8  & 0xff) | ((seed >>> 24) & 0x01)),
            (byte) ((seed >>> 16 & 0xff) | ((seed >>> 8) >> 8)),
            (byte) ((seed >>> 24 & 0xff) | ((seed >>> 16) >> 8)),
            (byte) ((seed & 0xff00) >> 4),
            (byte) ((seed >>> 8) & 0xff),
            (byte) ((seed >>> 16) & 0xff),
            (byte) ((seed >>> 24) & 0xff),
            (byte) (seed & 0xff),
            (byte) (seed >>> 8),
            (byte) (seed >>> 16),
            (byte) (seed >>> 24)
        };
        final byte[] result = new byte[13];
        generator.nextBytes(result);
        assertArrayEquals(expected, result);
    }

    /**
     * Test {@link BitsStreamGenerator#setSeed(int[])}.
     */
    @Test
    public void testSetSeedIntArray() {
        final int[] seed = { 1, 2, 3, 4, 5, 6, 7, 8, 9 };
        generator.setSeed(seed);
        final byte[] expected = new byte[seed.length * 2];
        for (int i = 0; i < seed.length; i++) {
            final int index = i * 2;
            expected[index] = (byte) ((seed[i] >>> 8) & 0xff);
            expected[index + 1] = (byte) ((seed[i] << 24) & 0xff);
        }
        final byte[] result = new byte[seed.length * 2];
        generator.nextBytes(result);
        assertArrayEquals(expected, result);
    }

    /**
     * Test {@link BitsStreamGenerator#setSeed(long)}.
     */
    @Test
    public void testSetSeedLong() {
        final long seed = 123456789012345L;
        generator.setSeed(seed);
        final byte[] expected = new byte[8];
        for (int i = 0; i < 8; i++) {
            expected[i] = (byte) ((seed >>> (i * 8)) & 0xff);
        }
        final byte[] result = new byte[8];
        generator.nextBytes(result);
        assertArrayEquals(expected, result);
    }

}