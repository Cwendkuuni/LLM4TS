package org.apache.commons.math3.random;

import static org.junit.Assert.*;

import org.junit.Test;

public class BitsStreamGeneratorTest {

    @Test
    public void testNextBoolean() {
        BitsStreamGenerator generator = new MersenneTwisterFastRandomGenerator();
        assertTrue(generator.nextBoolean());
        assertFalse(generator.nextBoolean());
        assertTrue(generator.nextBoolean());
        // Test edge case: generating multiple boolean values from the same generator
        boolean previousValue = generator.nextBoolean();
        int count = 10000;
        for (int i = 0; i < count; i++) {
            boolean currentValue = generator.nextBoolean();
            assertEquals(previousValue, currentValue);
            previousValue = currentValue;
        }
    }

    @Test
    public void testNextBytes() {
        byte[] expected = new byte[]{(byte) 0x1, (byte) 0x2, (byte) 0x3, (byte) 0x4};
        byte[] buffer = new byte[4];

        BitsStreamGenerator generator = new MersenneTwisterFastRandomGenerator();
        generator.nextBytes(buffer);

        assertArrayEquals(expected, buffer);

        generator.nextBytes(new byte[10]);
    }

    @Test
    public void testNextDouble() {
        BitsStreamGenerator generator = new MersenneTwisterFastRandomGenerator();

        double min = Double.MIN_VALUE;
        double max = Double.MAX_VALUE;
        double expected;

        for (int i = 0; i < 10000; i++) {
            expected = generator.nextDouble();
            assertTrue(min < expected && expected < max);
        }
    }

    @Test
    public void testNextFloat() {
        BitsStreamGenerator generator = new MersenneTwisterFastRandomGenerator();

        float min = Float.MIN_VALUE;
        float max = Float.MAX_VALUE;
        float expected;

        for (int i = 0; i < 10000; i++) {
            expected = generator.nextFloat();
            assertTrue(min < expected && expected < max);
        }
    }

    @Test
    public void testNextGaussian() {
        BitsStreamGenerator generator = new NormalRandomGenerator();

        double expectedMean = 0.0;
        double expectedStdDev = 1.0;
        double actualMean = 0.0;
        double actualStdDev = 0.0;
        int count = 10000;

        for (int i = 0; i < count; i++) {
            double value = generator.nextGaussian();
            actualMean += value;
        }

        actualMean /= count;

        for (int i = 0; i < count; i++) {
            double value = generator.nextGaussian();
            double difference = Math.pow(value - expectedMean, 2.0);
            actualStdDev += difference;
        }

        actualStdDev /= count;

        assertEquals(expectedMean, actualMean, 0.001);
        assertEquals(expectedStdDev, actualStdDev, 0.001);
    }

    @Test
    public void testNextInt() {
        BitsStreamGenerator generator = new MersenneTwisterFastRandomGenerator();

        for (int i = Integer.MIN_VALUE; i <= Integer.MAX_VALUE; i++) {
            int value = generator.nextInt();
            assertEquals(i, value);
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNextIntWithNegativeNumber() {
        BitsStreamGenerator generator = new MersenneTwisterFastRandomGenerator();
        generator.nextInt(-1);
    }

    @Test
    public void testNextIntPowerOfTwo() {
        BitsStreamGenerator generator = new MersenneTwisterFastRandomGenerator();

        for (int i = 0; i < 16; i++) {
            int number = 1 << i;
            int value = generator.nextInt(number);
            assertEquals(number, value);
        }
    }

    @Test
    public void testNextIntNotPowerOfTwo() {
        BitsStreamGenerator generator = new MersenneTwisterFastRandomGenerator();

        int number = 17;
        int value;

        do {
            value = generator.nextInt(number);
        } while (value % number != 0);

        assertEquals(number, value);
    }

    @Test
    public void testNextLong() {
        BitsStreamGenerator generator = new MersenneTwisterFastRandomGenerator();

        long min = Long.MIN_VALUE;
        long max = Long.MAX_VALUE;
        long expected;

        for (int i = 0; i < 10000; i++) {
            expected = generator.nextLong();
            assertTrue(min < expected && expected < max);
        }
    }

    @Test
    public void testSetSeed() {
        long seed = 0x1234567890L;
        BitsStreamGenerator generator = new MersenneTwisterFastRandomGenerator();

        generator.setSeed(seed);

        BitsStreamGenerator newGenerator = new MersenneTwisterFastRandomGenerator();
        newGenerator.setSeed(seed);

        for (int i = 0; i < 10; i++) {
            int value1 = generator.nextInt();
            int value2 = newGenerator.nextInt();
            assertEquals(value1, value2);
        }
    }

    @Test
    public void testSetSeedWithArray() {
        int[] seed = {1, 2, 3};
        int seedLength = 3;
        BitsStreamGenerator generator = new MersenneTwisterFastRandomGenerator();

        generator.setSeed(seed, 0, seedLength);

        BitsStreamGenerator newGenerator = new MersenneTwisterFastRandomGenerator();
        newGenerator.setSeed(seed, 0, seedLength);

        for (int i = 0; i < 10; i++) {
            int value1 = generator.nextInt();
            int value2 = newGenerator.nextInt();
            assertEquals(value1, value2);
        }
    }
}