package org.apache.commons.math3.random;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class BitsStreamGeneratorTest {

    @Test
    public void testNextBoolean() {
        BitsStreamGenerator generator = new MersenneTwisterFastRandomGenerator();
        assertTrue(generator.nextBoolean());
        assertTrue(generator.nextBoolean());
        assertFalse(generator.nextBoolean());
        assertFalse(generator.nextBoolean());
        assertFalse(generator.nextBoolean());
        assertFalse(generator.nextBoolean());
        assertFalse(generator.nextBoolean());
        assertFalse(generator.nextBoolean());
    }

    @Test
    public void testNextBytes() {
        BitsStreamGenerator generator = new MersenneTwisterFastRandomGenerator();
        byte[] bytes = new byte[10];
        generator.nextBytes(bytes);
        for (int i = 0; i < bytes.length; i++) {
            assertTrue(bytes[i] >= 0 && bytes[i] <= 0xff);
        }
        bytes = new byte[1];
        generator.nextBytes(bytes);
        assertTrue(bytes[0] >= 0 && bytes[0] <= 0xff);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNextIntNegative() {
        BitsStreamGenerator generator = new MersenneTwisterFastRandomGenerator();
        generator.nextInt(-1);
    }

    @Test
    public void testNextInt() {
        BitsStreamGenerator generator = new MersenneTwisterFastRandomGenerator();
        for (int i = 0; i < 10; i++) {
            int number = generator.nextInt();
            assertTrue(number >= 0 && number <= Integer.MAX_VALUE);
        }
    }

    @Test
    public void testNextIntPowerOfTwo() {
        BitsStreamGenerator generator = new MersenneTwisterFastRandomGenerator();
        for (int i = 0; i < 15; i++) {
            int number = generator.nextInt(1 << i);
            assertEquals(number, number % (1 << i));
        }
    }

    @Test
    public void testNextIntNotPowerOfTwo() {
        BitsStreamGenerator generator = new MersenneTwisterFastRandomGenerator();
        for (int i = 2; i <= 10; i++) {
            int number = generator.nextInt(i);
            int remainder = number % Integer.MAX_VALUE;
            int expectedRemainder = Math.abs(Integer.MAX_VALUE % i);
            assertEquals(expectedRemainder, remainder);
        }
    }

    @Test
    public void testNextLong() {
        BitsStreamGenerator generator = new MersenneTwisterFastRandomGenerator();
        long number = generator.nextLong();
        assertNotNull(number);
        long maxLong = Long.MAX_VALUE;
        assertTrue(number <= maxLong);
        number = generator.nextLong();
        assertTrue(number >= 0);
        maxLong = Long.MIN_VALUE;
        assertTrue(number > maxLong);
    }

    @Test
    public void testNextDouble() {
        BitsStreamGenerator generator = new MersenneTwisterFastRandomGenerator();
        double number = generator.nextDouble();
        assertTrue(number >= 0);
        double maxDouble = Double.MAX_VALUE;
        assertTrue(number <= maxDouble);
        number = generator.nextDouble();
        assertTrue(number >= 0);
        double minDouble = Double.MIN_VALUE;
        assertTrue(number > minDouble);
    }

    @Test
    public void testNextFloat() {
        BitsStreamGenerator generator = new MersenneTwisterFastRandomGenerator();
        float number = generator.nextFloat();
        assertTrue(number >= 0);
        float maxFloat = Float.MAX_VALUE;
        assertTrue(number <= maxFloat);
        number = generator.nextFloat();
        assertTrue(number >= 0);
        float minFloat = Float.MIN_VALUE;
        assertTrue(number > minFloat);
    }

    @Test
    public void testNextGaussian() {
        BitsStreamGenerator generator = new MersenneTwisterFastRandomGenerator();
        for (int i = 0; i < 10; i++) {
            double number = generator.nextGaussian();
            double mean = 0;
            double stdDev = 1;
            assertEquals(mean, number, stdDev);
        }
    }

    @Test
    public void testClear() {
        BitsStreamGenerator generator = new MersenneTwisterFastRandomGenerator();
        generator.nextGaussian();
        generator.clear();
        assertNull(generator.nextGaussian());
    }

}