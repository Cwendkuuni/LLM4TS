package org.apache.commons.math3.random;

import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test cases for {@link BitsStreamGenerator}.
 */
public class BitsStreamGeneratorTest {

    /** The generator to test. */
    private static final BitsStreamGenerator GENERATOR = new MersenneTwisterFastTest_53();

    /**
     * Tests {@link BitsStreamGenerator#nextBoolean()}.
     */
    @Test
    public void testNextBoolean() {
        final boolean expectedTrue = true;
        final boolean expectedFalse = false;
        for (int i = 0; i < 100; i++) {
            final boolean actual = GENERATOR.nextBoolean();
            Assert.assertEquals("Unexpected boolean value.", expectedTrue, actual);
            expectedTrue = !expectedTrue;
        }
        for (int i = 0; i < 100; i++) {
            final boolean actual = GENERATOR.nextBoolean();
            Assert.assertEquals("Unexpected boolean value.", expectedFalse, actual);
            expectedFalse = !expectedFalse;
        }
    }

    /**
     * Tests {@link BitsStreamGenerator#nextBytes(byte[])}.
     */
    @Test
    public void testNextBytes() {
        final byte[] expected = new byte[10];
        GENERATOR.nextBytes(expected);
        for (int i = 0; i < expected.length; i++) {
            Assert.assertNotNull("Unexpected null value.", expected[i]);
        }
        for (int i = 0; i < expected.length; i++) {
            final byte actual = GENERATOR.nextBytes()[i];
            Assert.assertNotNull("Unexpected null value.", actual);
            Assert.assertEquals("Unexpected byte value.", expected[i], actual);
        }
    }

    /**
     * Tests {@link BitsStreamGenerator#nextDouble()}.
     */
    @Test
    public void testNextDouble() {
        final double expectedMin = -1.0;
        final double expectedMax = 1.0;
        for (int i = 0; i < 100; i++) {
            final double actual = GENERATOR.nextDouble();
            Assert.assertNotNaN("Unexpected NaN value.", actual);
            Assert.assertTrue("Unexpected value is below minimum.", actual >= expectedMin);
            Assert.assertTrue("Unexpected value is above maximum.", actual <= expectedMax);
        }
    }

    /**
     * Tests {@link BitsStreamGenerator#nextFloat()}.
     */
    @Test
    public void testNextFloat() {
        final float expectedMin = -1.0f;
        final float expectedMax = 1.0f;
        for (int i = 0; i < 100; i++) {
            final float actual = GENERATOR.nextFloat();
            Assert.assertNotNaN("Unexpected NaN value.", actual);
            Assert.assertTrue("Unexpected value is below minimum.", actual >= expectedMin);
            Assert.assertTrue("Unexpected value is above maximum.", actual <= expectedMax);
        }
    }

    /**
     * Tests {@link BitsStreamGenerator#nextGaussian()}.
     */
    @Test
    public void testNextGaussian() {
        final double expectedMean = 0.0;
        final double expectedVariance = 1.0;
        final int numSamples = 10000;
        double sumX = 0.0;
        double sumXSquared = 0.0;
        for (int i = 0; i < numSamples; i++) {
            final double actual = GENERATOR.nextGaussian();
            sumX += actual;
            sumXSquared += actual * actual;
        }
        final double sampleMean = sumX / numSamples;
        final double sampleVariance = (sumXSquared / numSamples) - (sampleMean * sampleMean);
        Assert.assertFalse("Unexpected mean is not close to expected mean.", Math.abs(sampleMean - expectedMean) > 0.01);
        Assert.assertTrue("Unexpected variance is not close to expected variance.", Math.abs(sampleVariance - expectedVariance) < 0.01);
    }

    /**
     * Tests {@link BitsStreamGenerator#nextInt()}.
     */
    @Test
    public void testNextInt() {
        final int expectedMin = Integer.MIN_VALUE;
        final int expectedMax = Integer.MAX_VALUE;
        for (int i = 0; i < 100; i++) {
            final int actual = GENERATOR.nextInt();
            Assert.assertTrue("Unexpected value is below minimum.", actual >= expectedMin);
            Assert.assertTrue("Unexpected value is above maximum.", actual <= expectedMax);
        }
    }

    /**
     * Tests {@link BitsStreamGenerator#nextLong()}.
     */
    @Test
    public void testNextLong() {
        final long expectedMin = Integer.MIN_VALUE;
        final long expectedMax = Long.MAX_VALUE;
        for (int i = 0; i < 100; i++) {
            final long actual = GENERATOR.nextLong();
            Assert.assertTrue("Unexpected value is below minimum.", actual >= expectedMin);
            Assert.assertTrue("Unexpected value is above maximum.", actual <= expectedMax);
        }
    }

    /**
     * Tests {@link BitsStreamGenerator#clear()}.
     */
    @Test
    public void testClear() {
        GENERATOR.nextGaussian(); // initialize cache
        GENERATOR.clear();
        final double actual = GENERATOR.nextGaussian();
        Assert.assertTrue("Unexpected value is not NaN after clear.", Double.isNaN(actual));
    }

    /**
     * Tests {@link BitsStreamGenerator#setSeed(int)}.
     */
    @Test
    public void testSetSeedInt() {
        GENERATOR.setSeed(12345);
        final int expected = 12345;
        for (int i = 0; i < 10; i++) {
            final int actual = GENERATOR.nextInt();
            Assert.assertEquals("Unexpected seed value.", expected, actual);
        }
    }

    /**
     * Tests {@link BitsStreamGenerator#setSeed(int[])}.
     */
    @Test
    public void testSetSeedIntArray() {
        final int[] seed = { 1, 2, 3, 4 };
        GENERATOR.setSeed(seed);
        final int expected = seed[0];
        for (int i = 0; i < 10; i++) {
            final int actual = GENERATOR.nextInt();
            Assert.assertEquals("Unexpected seed value.", expected, actual);
            expected = seed[i % seed.length];
        }
    }

    /**
     * Tests {@link BitsStreamGenerator#setSeed(long)}.
     */
    @Test
    public void testSetSeedLong() {
        GENERATOR.setSeed(123456789012345L);
        final long expected = 123456789012345L;
        for (int i = 0; i < 10; i++) {
            final long actual = GENERATOR.nextLong();
            Assert.assertEquals("Unexpected seed value.", expected, actual);
        }
    }

}