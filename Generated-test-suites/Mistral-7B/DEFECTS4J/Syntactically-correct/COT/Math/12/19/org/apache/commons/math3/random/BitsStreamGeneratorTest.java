package org.apache.commons.math3.random;

import static org.junit.Assert.*;

import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.random.BitsStreamGenerator;
import org.apache.commons.math3.util.FastMath;
import org.junit.Test;

/**
 * Tests for {@link BitsStreamGenerator} class.
 */
public class BitsStreamGeneratorTest {

    private BitsStreamGenerator generator = new BitsStreamGenerator() {
        @Override
        public void setSeed(int seed) {
            // no-op
        }

        @Override
        public void setSeed(int[] seed) {
            // no-op
        }

        @Override
        public void setSeed(long seed) {
            // no-op
        }

        @Override
        protected int next(int bits) {
            // implementation under test
            throw new UnsupportedOperationException("not implemented");
        }
    };

    @Test(expected = UnsupportedOperationException.class)
    public void testSetSeed() {
        generator.setSeed(12345L);
    }

    @Test
    public void testNextBoolean() {
        assertTrue(generator.nextBoolean());
        assertTrue(generator.nextBoolean());
        assertFalse(generator.nextBoolean());
    }

    @Test
    public void testNextBytes() {
        byte[] bytes = new byte[16];
        generator.nextBytes(bytes);
        for (byte b : bytes) {
            assertTrue(b >= 0);
            assertTrue(b <= 0xff);
        }
    }

    @Test
    public void testNextDouble() {
        double expectedMin = FastMath.nextDown(0);
        double expectedMax = FastMath.nextUp(1);

        double d = generator.nextDouble();
        assertTrue(d >= expectedMin);
        assertTrue(d <= expectedMax);

        d = generator.nextDouble();
        assertTrue(d >= expectedMin);
        assertTrue(d <= expectedMax);
    }

    @Test
    public void testNextFloat() {
        float expectedMin = Float.MIN_VALUE;
        float expectedMax = Float.MAX_VALUE;

        float f = generator.nextFloat();
        assertTrue(f >= expectedMin);
        assertTrue(f <= expectedMax);

        f = generator.nextFloat();
        assertTrue(f >= expectedMin);
        assertTrue(f <= expectedMax);
    }

    @Test
    public void testNextGaussian() {
        double expectedMean = 0.0;
        double expectedStdDev = 1.0;

        double mean = 0.0;
        double stdDev = 0.0;
        for (int i = 0; i < 10000; i++) {
            double gaussian = generator.nextGaussian();
            mean += gaussian;
            stdDev += (gaussian - expectedMean) * (gaussian - expectedMean);
        }

        mean /= 10000.0;
        stdDev = Math.sqrt(stdDev / 10000.0);

        assertEquals(expectedMean, mean, 0.0001);
        assertEquals(expectedStdDev, stdDev, 0.0001);
    }

    @Test
    public void testNextInt() {
        for (int i = 0; i < 32; i++) {
            int result = generator.nextInt();
            assertEquals(i, result % 32);
        }
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testNextIntNegative() {
        generator.nextInt(-1);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testNextIntZero() {
        generator.nextInt(0);
    }

    @Test
    public void testNextLong() {
        long expectedMin = Long.MIN_VALUE;
        long expectedMax = Long.MAX_VALUE;

        long l = generator.nextLong();
        assertTrue(l >= expectedMin);
        assertTrue(l <= expectedMax);

        l = generator.nextLong();
        assertTrue(l >= expectedMin);
        assertTrue(l <= expectedMax);
    }

    @Test
    public void testClear() {
        generator.nextGaussian();
        generator.clear();
        assertFalse(Double.isFinite(generator.nextGaussian()));
    }
}