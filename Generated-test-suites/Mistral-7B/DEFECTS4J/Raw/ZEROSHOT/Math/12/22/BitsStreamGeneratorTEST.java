package org.apache.commons.math3.random;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.junit.Test;

/**
 * Tests for the {@link BitsStreamGenerator} class.
 */
public class BitsStreamGeneratorTest {

    /** Test instance. */
    private BitsStreamGenerator generator = new MyBitsStreamGenerator();

    /**
     * Test constructor.
     */
    @Test
    public void testConstructor() {
        // no test needed
    }

    /**
     * Test {@link BitsStreamGenerator#nextBoolean()}.
     */
    @Test
    public void testNextBoolean() {
        boolean b1 = generator.nextBoolean();
        boolean b2 = generator.nextBoolean();
        boolean b3 = generator.nextBoolean();
        assertEquals(Boolean.TRUE, b1 || b2);
        assertEquals(Boolean.TRUE, b1 || b3);
        assertEquals(Boolean.TRUE, b2 || b3);
        assertEquals(Boolean.TRUE, (b1 && b2) || b3);
        assertEquals(Boolean.TRUE, (b1 && b3) || b2);
        assertEquals(Boolean.TRUE, (b2 && b3) || b1);
    }

    /**
     * Test {@link BitsStreamGenerator#nextBytes(byte[])}.
     */
    @Test
    public void testNextBytes() {
        byte[] bytes1 = new byte[10];
        generator.nextBytes(bytes1);
        byte[] bytes2 = new byte[10];
        generator.nextBytes(bytes2);
        assertArrayEquals(bytes1, bytes2);
        byte[] bytes3 = new byte[1];
        generator.nextBytes(bytes3);
        assertEquals(bytes3.length, 1);
    }

    /**
     * Test {@link BitsStreamGenerator#nextDouble()}.
     */
    @Test
    public void testNextDouble() {
        for (int i = 0; i < 10; i++) {
            double d = generator.nextDouble();
            assertEquals(true, Double.isFinite(d));
        }
    }

    /**
     * Test {@link BitsStreamGenerator#nextFloat()}.
     */
    @Test
    public void testNextFloat() {
        for (int i = 0; i < 10; i++) {
            float f = generator.nextFloat();
            assertEquals(true, Float.isFinite(f));
        }
    }

    /**
     * Test {@link BitsStreamGenerator#nextGaussian()}.
     */
    @Test
    public void testNextGaussian() {
        for (int i = 0; i < 10; i++) {
            double g = generator.nextGaussian();
            assertEquals(true, Double.isFinite(g));
            assertEquals(true, Double.isNaN(g) || FastMath.abs(g) > 0, g);
        }
    }

    /**
     * Test {@link BitsStreamGenerator#nextInt()}.
     */
    @Test
    public void testNextInt() {
        assertEquals(Integer.MIN_VALUE, generator.nextInt(Integer.MIN_VALUE + 1));
        assertEquals(Integer.MAX_VALUE, generator.nextInt(Integer.MAX_VALUE + 1));
        for (int i = 0; i < 10; i++) {
            int n = generator.nextInt(10);
            assertEquals(true, n >= 0 && n < 10);
        }
    }

    /**
     * Test {@link BitsStreamGenerator#nextInt(int)}.
     */
    @Test
    public void testNextIntWithArgumentCheck() {
        try {
            generator.nextInt(-1);
            fail();
        } catch (NotStrictlyPositiveException e) {
            // expected
        }
        try {
            generator.nextInt(Integer.MAX_VALUE + 1);
            fail();
        } catch (NotStrictlyPositiveException e) {
            // expected
        }
    }

    /**
     * Test {@link BitsStreamGenerator#nextInt(int)}.
     */
    @Test
    public void testNextIntWithPowerOfTwoArgument() {
        for (int i = 0; i < 10; i++) {
            int n = generator.nextInt(1 << 3);
            assertEquals(true, (n & (n - 1)) == 0);
            assertEquals(true, n < (1 << 3));
        }
    }

    /**
     * Test {@link BitsStreamGenerator#nextInt(int)}.
     */
    @Test
    public void testNextIntWithRandomArgument() {
        for (int i = 0; i < 10; i++) {
            int n = generator.nextInt(10);
            int r = Integer.numberOfTrailingZeros(10 - 1)
                - Integer.numberOfTrailingZeros(n - 1);
            int expected = Integer.numberOfTrailingZeros(10)
                - Integer.numberOfTrailingZeros(n);
            assertEquals(expected, r);
        }
    }

    /**
     * Test {@link BitsStreamGenerator#nextLong()}.
     */
    @Test
    public void testNextLong() {
        assertEquals(Long.MIN_VALUE, generator.nextLong(Long.MIN_VALUE + 1));
        assertEquals(Long.MAX_VALUE, generator.nextLong(Long.MAX_VALUE + 1));
    }

    /**
     * Test {@link BitsStreamGenerator#clear()}.
     */
    @Test
    public void testClear() {
        double g1 = generator.nextGaussian();
        double g2 = generator.nextGaussian();
        generator.clear();
        double g3 = generator.nextGaussian();
        assertEquals(true, g1 != g2 && g1 != g3 && g2 != g3);
    }

    /**
     * Test utility method {@link #next(int)}.
     */
    private int next(int bits) {
        return generator.next(bits);
    }

    /**
     * Test implementation of {@link BitsStreamGenerator#next(int)}.
     */
    private static class MyBitsStreamGenerator
        extends BitsStreamGenerator {

        @Override
        protected int next(int bits) {
            int result = 0;
            for (int i = 0; i < bits; i++) {
                result |= (1 << (bits - i - 1)) & (1 << (int) (nextDouble() * 32.0));
            }
            return result;
        }

        @Override
        public void setSeed(int seed) {
            // no test needed
        }

        @Override
        public void setSeed(int[] seeds) {
            // no test needed
        }

        @Override
        public void setSeed(long seed) {
            // no test needed
        }

    }

}