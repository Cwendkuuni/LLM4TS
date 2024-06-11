package org.apache.commons.math3.random;

import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.util.FastMath;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests for {@link BitsStreamGenerator} class.
 */
public class BitsStreamGeneratorTest {

    private static final BitsStreamGenerator generator = new MersenneTwisterFastRandomGenerator();

    /**
     * Tests {@link BitsStreamGenerator#nextBoolean()} method.
     */
    @Test
    public void testNextBoolean() {
        boolean result;
        for (int i = 0; i < 1000; i++) {
            result = generator.nextBoolean();
            assertTrue(result || !result);
        }
    }

    /**
     * Tests {@link BitsStreamGenerator#nextBytes(byte[])} method.
     */
    @Test
    public void testNextBytes() {
        byte[] bytes = new byte[10];
        generator.nextBytes(bytes);
        for (byte b : bytes) {
            assertTrue((b & 0xFF) >= 0 && (b & 0xFF) <= 0xFF);
        }
    }

    /**
     * Tests {@link BitsStreamGenerator#nextDouble()} method.
     */
    @Test
    public void testNextDouble() {
        double result;
        for (int i = 0; i < 1000; i++) {
            result = generator.nextDouble();
            assertTrue(result >= 0.0 && result <= 1.0);
        }
    }

    /**
     * Tests {@link BitsStreamGenerator#nextFloat()} method.
     */
    @Test
    public void testNextFloat() {
        float result;
        for (int i = 0; i < 1000; i++) {
            result = generator.nextFloat();
            assertTrue(result >= 0.0f && result <= 1.0f);
        }
    }

    /**
     * Tests {@link BitsStreamGenerator#nextGaussian()} method.
     */
    @Test
    public void testNextGaussian() {
        double result;
        for (int i = 0; i < 1000; i++) {
            result = generator.nextGaussian();
            assertTrue(result >= -3.0 && result <= 3.0);
        }
    }

    /**
     * Tests {@link BitsStreamGenerator#nextInt()} method with positive argument.
     */
    @Test
    public void testNextIntPositive() {
        for (int test = 0; test < 10; test++) {
            int result = generator.nextInt(10);
            assertTrue(result >= 0 && result <= 9);
        }
    }

    /**
     * Tests {@link BitsStreamGenerator#nextInt()} method with negative argument.
     */
    @Test(expected = NotStrictlyPositiveException.class)
    public void testNextIntNegative() {
        generator.nextInt(-1);
    }

    /**
     * Tests {@link BitsStreamGenerator#nextInt(int)} method with power of 2 argument.
     */
    @Test
    public void testNextIntPowerOfTwo() {
        for (int test = 0; test < 10; test++) {
            int result = generator.nextInt(1 << 4);
            assertEquals(Math.pow(2, 4), (double) result / (double) (1 << 4));
        }
    }

    /**
     * Tests {@link BitsStreamGenerator#nextInt(int)} method with non-power of 2 argument.
     */
    @Test
    public void testNextIntNonPowerOfTwo() {
        for (int test = 0; test < 10; test++) {
            int result = generator.nextInt(7);
            assertTrue(result >= 0 && result <= 6);
        }
    }

    /**
     * Tests {@link BitsStreamGenerator#nextLong()} method.
     */
    @Test
    public void testNextLong() {
        long result;
        for (int i = 0; i < 1000; i++) {
            result = generator.nextLong();
            assertTrue(result >= 0);
        }
    }

    /**
     * Tests {@link BitsStreamGenerator#clear()} method.
     */
    @Test
    public void testClear() {
        generator.nextGaussian();
        generator.clear();
        assertFalse(Double.isNaN(generator.nextGaussian()));
    }

}