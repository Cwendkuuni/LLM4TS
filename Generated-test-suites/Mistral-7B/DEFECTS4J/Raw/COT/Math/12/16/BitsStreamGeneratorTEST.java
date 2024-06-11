package org.apache.commons.math3.random;

import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.util.FastMath;
import org.junit.Assert;
import org.junit.Test;

public class BitsStreamGeneratorTest {

    @Test
    public void testSetSeedInt() {
        BitsStreamGenerator generator = new BitsStreamGenerator() {
            @Override
            public void setSeed(int seed) {
                // no-op in this test case
            }
        };
        generator.setSeed(12345);
        // check if seed has been set
        int seed = generator.nextInt();
        Assert.assertEquals(12345, seed);
    }

    @Test
    public void testSetSeedIntArray() {
        BitsStreamGenerator generator = new BitsStreamGenerator() {
            @Override
            public void setSeed(int[] seed) {
                // no-op in this test case
            }
        };
        int[] seeds = {1, 2, 3};
        generator.setSeed(seeds);
        // check if seeds have been set
        int[] expectedSeeds = {1, 2, 3};
        for (int i = 0; i < expectedSeeds.length; i++) {
            int seed = generator.nextInt();
            Assert.assertEquals(expectedSeeds[i], seed);
        }
    }

    @Test
    public void testSetSeedLong() {
        BitsStreamGenerator generator = new BitsStreamGenerator() {
            @Override
            public void setSeed(long seed) {
                // no-op in this test case
            }
        };
        generator.setSeed(1234567890123L);
        // check if seed has been set
        long seed = generator.nextLong();
        Assert.assertEquals(1234567890123L, seed);
    }

    @Test
    public void testNextBoolean() {
        BitsStreamGenerator generator = new BitsStreamGenerator();
        boolean result = generator.nextBoolean();
        Assert.assertTrue(result);
        result = generator.nextBoolean();
        Assert.assertTrue(result);
        result = generator.nextBoolean();
        Assert.assertFalse(result);
    }

    @Test
    public void testNextBytes() {
        BitsStreamGenerator generator = new BitsStreamGenerator();
        byte[] bytes = new byte[10];
        generator.nextBytes(bytes);
        // check that all bytes have been generated
        for (byte b : bytes) {
            Assert.assertNotNull(b);
        }
    }

    @Test
    public void testNextDouble() {
        BitsStreamGenerator generator = new BitsStreamGenerator();
        double result = generator.nextDouble();
        Assert.assertNotNaN(result);
        Assert.assertTrue(result > 0.0);
        Assert.assertTrue(result < 1.0);
        result = generator.nextDouble();
        Assert.assertNotNaN(result);
        Assert.assertTrue(result > 0.0);
        Assert.assertTrue(result < 1.0);
    }

    @Test
    public void testNextFloat() {
        BitsStreamGenerator generator = new BitsStreamGenerator();
        float result = generator.nextFloat();
        Assert.assertNotNaN(result);
        Assert.assertTrue(result > 0.0);
        Assert.assertTrue(result < 1.0);
        result = generator.nextFloat();
        Assert.assertNotNaN(result);
        Assert.assertTrue(result > 0.0);
        Assert.assertTrue(result < 1.0);
    }

    @Test
    public void testNextGaussian() {
        BitsStreamGenerator generator = new BitsStreamGenerator();
        double result = generator.nextGaussian();
        Assert.assertNotNaN(result);
        Assert.assertTrue(result > -3.0);
        Assert.assertTrue(result < 3.0);
        result = generator.nextGaussian();
        Assert.assertNotNaN(result);
        Assert.assertTrue(result > -3.0);
        Assert.assertTrue(result < 3.0);
    }

    @Test
    public void testNextInt() {
        BitsStreamGenerator generator = new BitsStreamGenerator();
        int result = generator.nextInt();
        Assert.assertTrue(result >= 0);
        Assert.assertTrue(result <= Integer.MAX_VALUE);
        result = generator.nextInt();
        Assert.assertTrue(result >= 0);
        Assert.assertTrue(result <= Integer.MAX_VALUE);
    }

    @Test
    public void testNextIntNegative() {
        BitsStreamGenerator generator = new BitsStreamGenerator();
        try {
            generator.nextInt(-1);
            Assert.fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testNextIntMaxValuePlusOne() {
        BitsStreamGenerator generator = new BitsStreamGenerator();
        try {
            generator.nextInt(Integer.MAX_VALUE + 1);
            Assert.fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testNextLong() {
        BitsStreamGenerator generator = new BitsStreamGenerator();
        long result = generator.nextLong();
        Assert.assertTrue(result >= 0);
        Assert.assertTrue(result <= Long.MAX_VALUE);
        result = generator.nextLong();
        Assert.assertTrue(result >= 0);
        Assert.assertTrue(result <= Long.MAX_VALUE);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testNextIntNegativeException() {
        BitsStreamGenerator generator = new BitsStreamGenerator();
        generator.nextInt(-1);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testNextIntMaxValuePlusOneException() {
        BitsStreamGenerator generator = new BitsStreamGenerator();
        generator.nextInt(Long.MAX_VALUE + 1);
    }
}