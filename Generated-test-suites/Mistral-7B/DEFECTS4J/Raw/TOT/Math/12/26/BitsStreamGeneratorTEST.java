package org.apache.commons.math3.random;

import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.util.Precision;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Test class for {@link BitsStreamGenerator}.
 */
public class BitsStreamGeneratorTest {

    /** The instance under test. */
    private BitsStreamGenerator generator;

    /**
     * Sets up a new instance of {@link BitsStreamGenerator}.
     */
    @Before
    public void setUp() {
        generator = new BitsStreamGenerator();
    }

    /**
     * Test method for {@link BitsStreamGenerator#nextBoolean()}.
     */
    @Test
    public void testNextBoolean() {
        Assert.assertTrue(generator.nextBoolean());
        Assert.assertTrue(generator.nextBoolean());
        Assert.assertFalse(generator.nextBoolean());
        Assert.assertFalse(generator.nextBoolean());
    }

    /**
     * Test method for {@link BitsStreamGenerator#nextBytes(byte[])}.
     */
    @Test
    public void testNextBytes() {
        final byte[] bytes = new byte[10];
        generator.nextBytes(bytes);
        for (int i = 0; i < bytes.length; i++) {
            Assert.assertNotEquals((byte) i, bytes[i]);
        }
    }

    /**
     * Test method for {@link BitsStreamGenerator#nextDouble()}.
     */
    @Test
    public void testNextDouble() {
        final double expectedMean = 0.5;
        final double expectedVariance = 1.0;
        final int trials = 100_000;
        double sum = 0.0;
        for (int i = 0; i < trials; i++) {
            sum += generator.nextDouble();
        }
        final double mean = sum / trials;
        final double variance = Precision.pow(sum / trials, 2)
                - Precision.pow(mean, 2);
        Assert.assertEquals(expectedMean, mean, 0.001);
        Assert.assertEquals(expectedVariance, variance, 0.001);
    }

    /**
     * Test method for {@link BitsStreamGenerator#nextFloat()}.
     */
    @Test
    public void testNextFloat() {
        final double expectedMean = 0.5f;
        final double expectedVariance = 1.0f;
        final int trials = 100_000;
        float sum = 0.0f;
        for (int i = 0; i < trials; i++) {
            sum += generator.nextFloat();
        }
        final float mean = sum / trials;
        final float variance = (float) (sum / trials) * (float) (sum / trials)
                - Precision.pow(mean, 2);
        Assert.assertEquals(expectedMean, mean, 0.001f);
        Assert.assertEquals(expectedVariance, variance, 0.001f);
    }

    /**
     * Test method for {@link BitsStreamGenerator#nextGaussian()}.
     */
    @Test
    public void testNextGaussian() {
        final double expectedMean = 0.0;
        final double expectedVariance = 1.0;
        final int trials = 10_000;
        double sum = 0.0;
        for (int i = 0; i < trials; i++) {
            sum += generator.nextGaussian();
        }
        final double mean = sum / trials;
        final double variance = Precision.pow(sum / trials, 2)
                - Precision.pow(mean, 2);
        Assert.assertEquals(expectedMean, mean, 0.01);
        Assert.assertEquals(expectedVariance, variance, 0.01);
    }

    /**
     * Test method for {@link BitsStreamGenerator#nextInt()}.
     */
    @Test
    public void testNextInt() {
        final int max = 10;
        for (int i = 0; i < 10; i++) {
            final int result = generator.nextInt(max);
            Assert.assertTrue(result >= 0);
            Assert.assertTrue(result < max);
        }
    }

    /**
     * Test method for {@link BitsStreamGenerator#nextInt(int)} with a negative
     * argument.
     */
    @Test(expected = NotStrictlyPositiveException.class)
    public void testNextIntNegativeArgument() {
        generator.nextInt(-1);
    }

    /**
     * Test method for {@link BitsStreamGenerator#nextLong()}.
     */
    @Test
    public void testNextLong() {
        final long max = Long.MAX_VALUE;
        for (int i = 0; i < 10; i++) {
            final long result = generator.nextLong();
            Assert.assertTrue(result >= 0);
            Assert.assertTrue(result <= max);
        }
    }

    /**
     * Test method for {@link BitsStreamGenerator#clear()}.
     */
    @Test
    public void testClear() {
        generator.nextGaussian();
        generator.clear();
        Assert.assertTrue(Double.isNaN(generator.nextGaussian()));
    }

}