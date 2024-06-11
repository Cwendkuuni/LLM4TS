package org.apache.commons.math3.random;

import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.util.FastMath;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test class for {@link BitsStreamGenerator}.
 */
public class BitsStreamGeneratorTest {

    private static final int N = 10;
    private static final int M = 5;

    @Test
    public void testSetSeed() {
        BitsStreamGenerator generator = new MyBitsStreamGenerator();
        generator.setSeed(1);
        assertEquals(1, generator.nextInt());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetNegativeSeed() {
        BitsStreamGenerator generator = new MyBitsStreamGenerator();
        generator.setSeed(-1);
    }

    @Test
    public void testNextBoolean() {
        BitsStreamGenerator generator = new MyBitsStreamGenerator();
        assertTrue(generator.nextBoolean());
        assertTrue(generator.nextBoolean());
        assertFalse(generator.nextBoolean());
        assertFalse(generator.nextBoolean());
        assertTrue(generator.nextBoolean());
    }

    @Test
    public void testNextBytes() {
        BitsStreamGenerator generator = new MyBitsStreamGenerator();
        byte[] expected = new byte[N];
        generator.nextBytes(expected);
        for (int i = 0; i < N; i++) {
            byte b = expected[i];
            boolean isZero = (b == 0);
            for (int j = 0; j < 8; j++) {
                boolean expectedBit = (i * 8 + j) % 2 == 0;
                boolean actualBit = ((b & (1 << j)) != 0);
                assertEquals(expectedBit, actualBit);
            }
        }
    }

    @Test
    public void testNextDouble() {
        BitsStreamGenerator generator = new MyBitsStreamGenerator();
        for (int i = 0; i < M; i++) {
            double d = generator.nextDouble();
            double e = generator.nextDouble();
            double sum = d + e;
            assertTrue(sum > 0.0);
            assertTrue(sum < 2.0);
        }
    }

    @Test
    public void testNextFloat() {
        BitsStreamGenerator generator = new MyBitsStreamGenerator();
        for (int i = 0; i < M; i++) {
            float f = generator.nextFloat();
            assertTrue(f > 0.0f);
            assertTrue(f < 1.0f);
        }
    }

    @Test
    public void testNextGaussian() {
        BitsStreamGenerator generator = new MyBitsStreamGenerator();
        for (int i = 0; i < M; i++) {
            double g1 = generator.nextGaussian();
            double g2 = generator.nextGaussian();
            double sum = g1 + g2;
            double mean = 0.0;
            double variance = 1.0;
            double expected = mean + FastMath.sqrt(variance) * (FastMath.random() * 2.0 - 1.0);
            assertTrue(sum > mean - 3.0 * FastMath.sqrt(variance));
            assertTrue(sum < mean + 3.0 * FastMath.sqrt(variance));
        }
    }

    @Test
    public void testNextInt() {
        BitsStreamGenerator generator = new MyBitsStreamGenerator();
        for (int i = 0; i < N; i++) {
            int n = 2 * i + 1;
            int result = generator.nextInt(n);
            assertTrue(result >= 0);
            assertTrue(result < n);
        }
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testNextIntNegative() {
        BitsStreamGenerator generator = new MyBitsStreamGenerator();
        generator.nextInt(-1);
    }

    @Test
    public void testNextLong() {
        BitsStreamGenerator generator = new MyBitsStreamGenerator();
        for (long i = 0; i < 10; i++) {
            long result = generator.nextLong();
            assertTrue(result > 0);
            assertTrue(result < Long.MAX_VALUE);
        }
    }

    @Test
    public void testClear() {
        BitsStreamGenerator generator = new MyBitsStreamGenerator();
        generator.nextGaussian();
        generator.clear();
        assertFalse(Double.isNaN(generator.nextGaussian()));
    }

    private static class MyBitsStreamGenerator extends BitsStreamGenerator {
        @Override
        public void setSeed(int seed) {
            super.setSeed(seed);
        }

        @Override
        public void setSeed(int[] seed) {
            super.setSeed(seed);
        }

        @Override
        public void setSeed(long seed) {
            super.setSeed(seed);
        }

        @Override
        public void setSeed(long[] seed) {
            super.setSeed(seed);
        }
    }

}