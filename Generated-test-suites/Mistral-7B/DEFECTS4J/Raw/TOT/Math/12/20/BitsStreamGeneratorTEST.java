package org.apache.commons.math3.random;

import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.util.FastMath;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests for {@link BitsStreamGenerator}.
 */
public class BitsStreamGeneratorTest {

    private static final int SEED = 123456789;
    private static final int[] SEED_ARRAY = {1, 2, 3, 4};
    private static final long LONG_SEED = 9876543210L;
    private static final BitsStreamGenerator generator = new MersenneTwisterFastRandomGenerator();

    @Test
    public void testNextBoolean() {
        generator.setSeed(SEED);
        for (int i = 0; i < 100; i++) {
            assertTrue(generator.nextBoolean());
        }
        generator.setSeed(SEED);
        for (int i = 0; i < 100; i++) {
            assertFalse(generator.nextBoolean());
        }
    }

    @Test
    public void testNextBytes() {
        byte[] expected = new byte[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        generator.setSeed(SEED);
        generator.nextBytes(expected);
        byte[] actual = new byte[expected.length];
        generator.nextBytes(actual);
        assertArrayEquals(expected, actual);
    }

    @Test
    public void testNextDouble() {
        generator.setSeed(SEED);
        for (int i = 0; i < 100; i++) {
            double d = generator.nextDouble();
            assertFalse(Double.isNaN(d));
            assertTrue(d >= 0.0 && d <= 1.0);
        }
    }

    @Test
    public void testNextFloat() {
        generator.setSeed(SEED);
        for (int i = 0; i < 100; i++) {
            float f = generator.nextFloat();
            assertFalse(Float.isNaN(f));
            assertTrue(f >= 0.0f && f <= 1.0f);
        }
    }

    @Test
    public void testNextGaussian() {
        generator.setSeed(SEED);
        for (int i = 0; i < 100; i++) {
            double actual = generator.nextGaussian();
            double expected = FastMath.nextGaussian();
            assertEquals(expected, actual, 0.001d);
        }
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testNextIntNegativeSeed() {
        generator.setSeed(-123);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testNextIntZeroSeed() {
        generator.setSeed(0);
    }

    @Test
    public void testNextInt() {
        generator.setSeed(SEED);
        for (int i = 0; i < 100; i++) {
            int result = generator.nextInt();
            int max = Integer.MAX_VALUE;
            assertTrue(result >= 0 && result <= max);
        }
    }

    @Test
    public void testNextIntSpecifiedBitLength() {
        generator.setSeed(SEED);
        for (int i = 0; i < 100; i++) {
            int result = generator.nextInt(3);
            assertTrue(result >= 0 && result <= 3);
        }
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testNextIntNegativeBitLength() {
        generator.setSeed(SEED);
        generator.nextInt(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNextIntZeroBitLength() {
        generator.setSeed(SEED);
        generator.nextInt(0);
    }

    @Test
    public void testNextLong() {
        generator.setSeed(LONG_SEED);
        for (int i = 0; i < 100; i++) {
            long result = generator.nextLong();
            long max = Long.MAX_VALUE;
            assertTrue(result >= 0 && result <= max);
        }
    }

    @Test
    public void testSetSeed() {
        generator.setSeed(SEED);
        long originalSeed = generator.nextLong();
        generator.setSeed(SEED_ARRAY);
        long newSeed = generator.nextLong();
        assertNotEquals(originalSeed, newSeed);
        assertArrayEquals(SEED_ARRAY, new long[]{newSeed >> 32, newSeed & 0xFFFFFFFFL});
    }

    @Test
    public void testClear() {
        generator.setSeed(SEED);
        generator.nextGaussian();
        generator.clear();
        assertTrue(Double.isNaN(generator.nextGaussian()));
    }

}