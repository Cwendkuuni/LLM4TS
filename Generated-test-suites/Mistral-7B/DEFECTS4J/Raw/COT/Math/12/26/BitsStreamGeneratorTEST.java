package org.apache.commons.math3.random;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.util.FastMath;
import org.junit.Test;

/**
 * Test class for {@link BitsStreamGenerator}.
 */
public class BitsStreamGeneratorTest {

    /** Test instance of {@link BitsStreamGenerator}. */
    private BitsStreamGenerator generator = new BitsStreamGenerator() {
        @Override
        protected int next(int bits) {
            // Provide a mock implementation for testing purpose.
            return (int) (Math.random() * (1 << bits));
        }
    };

    /**
     * Test method for {@link BitsStreamGenerator#nextBoolean()}.
     */
    @Test
    public void testNextBoolean() {
        assertTrue(generator.nextBoolean());
        assertTrue(generator.nextBoolean());
        assertFalse(generator.nextBoolean());
        assertFalse(generator.nextBoolean());
    }

    /**
     * Test method for {@link BitsStreamGenerator#nextBytes(byte[])}.
     */
    @Test
    public void testNextBytes() {
        byte[] bytes = new byte[10];
        generator.nextBytes(bytes);
        assertArrayEquals(bytes, new byte[] {-12, 53, 34, 37, -119, -126, -112, 52, -72, 120});
    }

    /**
     * Test method for {@link BitsStreamGenerator#nextDouble()}.
     */
    @Test
    public void testNextDouble() {
        double expected = FastMath.nextGaussian();
        double actual   = generator.nextDouble();
        assertTrue(Math.abs(expected - actual) < 1e-9);
    }

    /**
     * Test method for {@link BitsStreamGenerator#nextFloat()}.
     */
    @Test
    public void testNextFloat() {
        float expected = FastMath.nextGaussian() > 0 ? FastMath.nextGaussian() : -FastMath.nextGaussian();
        float actual   = generator.nextFloat();
        assertTrue(Math.abs(expected - actual) < 1e-6);
    }

    /**
     * Test method for {@link BitsStreamGenerator#nextGaussian()}.
     */
    @Test
    public void testNextGaussian() {
        double actual = generator.nextGaussian();
        double expected = FastMath.nextGaussian();
        assertTrue(Math.abs(expected - actual) < 1e-9);
    }

    /**
     * Test method for {@link BitsStreamGenerator#nextInt()}.
     */
    @Test
    public void testNextInt() {
        for (int i = 0; i < 10; i++) {
            int actual = generator.nextInt();
            assertTrue(actual >= 0 && actual < Integer.MAX_VALUE);
        }
    }

    /**
     * Test method for {@link BitsStreamGenerator#nextInt(int)}.
     */
    @Test
    public void testNextIntWithN() {
        for (int n = 1; n <= 10; n++) {
            for (int i = 0; i < 10000; i++) {
                int actual = generator.nextInt(n);
                assertTrue(actual >= 0 && actual < n);
            }
        }
    }

    /**
     * Test method for {@link BitsStreamGenerator#nextLong()}.
     */
    @Test
    public void testNextLong() {
        long expected = Math.abs(Long.MIN_VALUE) + (long) (Math.random() * (Long.MAX_VALUE - Math.abs(Long.MIN_VALUE)));
        long actual   = generator.nextLong();
        assertTrue(expected == actual || (expected - actual) == (Long.MAX_VALUE - Math.abs(Long.MIN_VALUE)));
    }

    /**
     * Test method for {@link BitsStreamGenerator#clear()}.
     */
    @Test
    public void testClear() {
        double expected = FastMath.nextGaussian();
        generator.nextGaussian();
        generator.clear();
        double actual = generator.nextGaussian();
        assertTrue(expected == actual);
    }

}