package org.apache.commons.math3.random;

import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.util.FastMath;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test class for {@link BitsStreamGenerator}.
 */
public class BitsStreamGeneratorTest {

    @Test
    public void testNextBoolean() {
        BitsStreamGenerator generator = new NoopBitsStreamGenerator();
        assertTrue(generator.nextBoolean());
        assertFalse(generator.nextBoolean());
        assertFalse(generator.nextBoolean());
        assertTrue(generator.nextBoolean());
        // test edge cases
        assertFalse(generator.nextBoolean());
        assertFalse(generator.nextBoolean());
        assertTrue(generator.nextBoolean());
    }

    @Test
    public void testNextBytes() {
        BitsStreamGenerator generator = new NoopBitsStreamGenerator();
        byte[] bytes = new byte[10];
        generator.nextBytes(bytes);
        for (byte b : bytes) {
            assertTrue(b >= 0);
        }
        // test edge cases
        byte[] bytes2 = new byte[1];
        generator.nextBytes(bytes2);
        assertEquals(0, bytes2[0]);
    }

    @Test
    public void testNextDouble() {
        BitsStreamGenerator generator = new NoopBitsStreamGenerator();
        assertEquals(0.0, generator.nextDouble(), 1e-10);
        assertEquals(1.0, generator.nextDouble(), 1e-10);
        // test edge cases
        assertEquals(Double.MIN_VALUE, generator.nextDouble(), 1e-10);
        assertEquals(Double.MAX_VALUE, generator.nextDouble(), 1e-10);
    }

    @Test
    public void testNextFloat() {
        BitsStreamGenerator generator = new NoopBitsStreamGenerator();
        assertEquals(0.0f, generator.nextFloat(), 1e-5f);
        assertEquals(1.0f, generator.nextFloat(), 1e-5f);
        // test edge cases
        assertEquals(Float.MIN_NORMAL, generator.nextFloat(), 1e-5f);
        assertEquals(Float.MAX_VALUE, generator.nextFloat(), 1e-5f);
    }

    @Test
    public void testNextGaussian() {
        BitsStreamGenerator generator = new NormalBitsStreamGenerator();
        assertEquals(0.0, generator.nextGaussian(), 1e-5);
        assertEquals(0.0, generator.nextGaussian(), 1e-5);
        assertEquals(-0.5, generator.nextGaussian(), 1e-5);
        assertEquals(0.5, generator.nextGaussian(), 1e-5);
        // test edge cases
        assertEquals(-3.0, generator.nextGaussian(), 1e-5);
        assertEquals(3.0, generator.nextGaussian(), 1e-5);
    }

    @Test
    public void testNextInt() {
        BitsStreamGenerator generator = new NoopBitsStreamGenerator();
        for (int i = Integer.MIN_VALUE; i < Integer.MAX_VALUE; i++) {
            assertEquals(i, generator.nextInt());
            assertEquals(i, generator.nextInt());
        }
        // test edge cases
        assertEquals(Integer.MIN_VALUE, generator.nextInt());
        assertEquals(Integer.MAX_VALUE, generator.nextInt());
    }

    @Test
    public void testNextIntWithLimit() {
        BitsStreamGenerator generator = new NoopBitsStreamGenerator();
        int limit = 10;
        for (int i = 0; i < 10 * 1000; i++) {
            int next = generator.nextInt(limit);
            assertTrue(next >= 0);
            assertTrue(next < limit);
        }
        // test edge cases
        assertEquals(0, generator.nextInt(limit));
        assertEquals(limit - 1, generator.nextInt(limit));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNextIntWithNegativeLimit() {
        BitsStreamGenerator generator = new NoopBitsStreamGenerator();
        int limit = -1;
        generator.nextInt(limit);
    }

    @Test
    public void testNextLong() {
        BitsStreamGenerator generator = new NoopBitsStreamGenerator();
        for (long i = Long.MIN_VALUE; i <= Long.MAX_VALUE; i++) {
            assertEquals(i, generator.nextLong());
            assertEquals(i, generator.nextLong());
        }
        // test edge cases
        assertEquals(Long.MIN_VALUE, generator.nextLong());
        assertEquals(Long.MAX_VALUE, generator.nextLong());
    }

    @Test
    public void testClear() {
        BitsStreamGenerator generator = new NormalBitsStreamGenerator();
        generator.nextGaussian();
        generator.clear();
        assertFalse(Double.isNaN(generator.nextGaussian()));
    }

    private static class NoopBitsStreamGenerator extends BitsStreamGenerator {
        @Override
        public void setSeed(int seed) {
        }

        @Override
        public void setSeed(int[] seeds) {
        }

        @Override
        public void setSeed(long seed) {
        }

        @Override
        protected int next(int bits) {
            return 0;
        }
    }

    private static class NormalBitsStreamGenerator extends BitsStreamGenerator {
        @Override
        public void setSeed(int seed) {
        }

        @Override
        public void setSeed(int[] seeds) {
        }

        @Override
        public void setSeed(long seed) {
        }

        @Override
        protected int next(int bits) {
            return (int) (Math.random() * ((1 << bits) - 1));
        }
    }

}