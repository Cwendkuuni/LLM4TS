package org.apache.commons.math3.random;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class BitsStreamGeneratorTest {

    @Test
    public void testNextBoolean() {
        BitsStreamGenerator generator = new MersenneTwisterFastRandomGenerator();
        assertEquals(true, generator.nextBoolean());
        assertEquals(false, generator.nextBoolean());
    }

    @Test
    public void testNextBytes() {
        byte[] expectedBytes = new byte[]{1, 2, 3, 4, 5};
        BitsStreamGenerator generator = new MersenneTwisterFastRandomGenerator();
        generator.nextBytes(expectedBytes);
        byte[] actualBytes = new byte[expectedBytes.length];
        generator.nextBytes(actualBytes);
        assertArrayEquals(expectedBytes, actualBytes);
    }

    @Test
    public void testNextDouble() {
        BitsStreamGenerator generator = new MersenneTwisterFastRandomGenerator();
        double expectedValue = 0.456789;
        assertEquals(expectedValue, generator.nextDouble(), 0.0001);
    }

    @Test
    public void testNextFloat() {
        BitsStreamGenerator generator = new MersenneTwisterFastRandomGenerator();
        float expectedValue = 0.56f;
        assertEquals(expectedValue, generator.nextFloat(), 0.01f);
    }

    @Test
    public void testNextGaussian() {
        BitsStreamGenerator generator = new GaussianGenerator();
        double expectedMean = 0.0;
        double expectedStdDev = 1.0;
        double actualMean = 0.0;
        double actualStdDev = 0.0;
        for (int i = 0; i < 10000; i++) {
            double randomNumber = generator.nextGaussian();
            actualMean += randomNumber;
        }
        actualMean /= 10000;
        assertEquals(expectedMean, actualMean, 0.001);

        for (int i = 0; i < 10000; i++) {
            double randomNumber = generator.nextGaussian();
            actualStdDev += Math.pow(randomNumber - expectedMean, 2);
        }
        actualStdDev = Math.sqrt(actualStdDev / 10000);
        assertEquals(expectedStdDev, actualStdDev, 0.01);
    }

    @Test
    public void testNextInt() {
        BitsStreamGenerator generator = new MersenneTwisterFastRandomGenerator();
        assertEquals(42, generator.nextInt());
        assertEquals(Integer.MIN_VALUE, generator.nextInt());
        assertEquals(Integer.MAX_VALUE, generator.nextInt());
    }

    @Test
    public void testNextIntWithLargePrime() {
        BitsStreamGenerator generator = new MersenneTwisterFastRandomGenerator();
        final int primeNumber = (int) Math.pow(2, 31) - 1;
        assertEquals(primeNumber, generator.nextInt(primeNumber));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNextIntWithNegativeNumber() {
        BitsStreamGenerator generator = new MersenneTwisterFastRandomGenerator();
        generator.nextInt(-1);
    }

    @Test
    public void testNextLong() {
        BitsStreamGenerator generator = new MersenneTwisterFastRandomGenerator();
        long expectedValue = Long.MIN_VALUE;
        assertEquals(expectedValue, generator.nextLong());
        long expectedValue2 = Long.MAX_VALUE;
        assertEquals(expectedValue2, generator.nextLong());
    }

    @Test
    public void testClearMethod() {
        BitsStreamGenerator generator = new GaussianGenerator();
        generator.nextGaussian();
        generator.clear();
        assertEquals(Double.NaN, generator.nextGaussian(), 0.0);
    }
}