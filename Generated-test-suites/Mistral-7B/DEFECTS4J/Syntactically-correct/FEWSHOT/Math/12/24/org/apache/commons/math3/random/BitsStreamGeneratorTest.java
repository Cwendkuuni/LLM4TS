package org.apache.commons.math3.random;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class BitsStreamGeneratorTest {

    @Test
    public void testNextBoolean() {
        BitsStreamGenerator generator = new MersenneTwisterFastTest_018();
        assertTrue(generator.nextBoolean());
        assertTrue(generator.nextBoolean());
        assertFalse(generator.nextBoolean());
        assertFalse(generator.nextBoolean());
    }

    @Test
    public void testNextBytes() {
        BitsStreamGenerator generator = new MersenneTwisterFastTest_018();
        byte[] bytes = new byte[10];
        generator.nextBytes(bytes);
        for (byte b : bytes) {
            assertTrue(b >= 0 && b <= 0xff);
        }
        byte[] bytes1 = new byte[]{-1, 0x30, (byte) 0x80};
        generator.nextBytes(bytes1);
        assertArrayEquals(bytes1, generator.nextBytes(bytes1.length));
    }

    @Test
    public void testNextDouble() {
        BitsStreamGenerator generator = new MersenneTwisterFastTest_018();
        assertEquals(0.0, generator.nextDouble(), 1e-12);
        assertEquals(1.0, generator.nextDouble(), 1e-12);
        double min = Double.MIN_VALUE;
        double max = Double.MAX_VALUE;
        assertEquals(min, generator.nextDouble(), 1e-12);
        assertEquals(max, generator.nextDouble(), 1e-12);
        for (int i = 0; i < 100000; i++) {
            double d = generator.nextDouble();
            assertTrue(d >= min && d <= max);
        }
    }

    @Test
    public void testNextFloat() {
        BitsStreamGenerator generator = new MersenneTwisterFastTest_018();
        assertEquals(0.0f, generator.nextFloat(), 1e-5f);
        assertEquals(1.0f, generator.nextFloat(), 1e-5f);
        float min = Float.MIN_VALUE;
        float max = Float.MAX_VALUE;
        assertEquals(min, generator.nextFloat(), 1e-5f);
        assertEquals(max, generator.nextFloat(), 1e-5f);
        for (int i = 0; i < 100000; i++) {
            float f = generator.nextFloat();
            assertTrue(f >= min && f <= max);
        }
    }

    @Test
    public void testNextGaussian() {
        BitsStreamGenerator generator = new MersenneTwisterFastTest_018();
        double mean = 0.0;
        double stdDev = 1.0;
        for (int i = 0; i < 10000; i++) {
            double gaussian = generator.nextGaussian(mean, stdDev);
            assertTrue(gaussian >= mean - 3 * stdDev && gaussian <= mean + 3 * stdDev);
        }
        generator.clear();
        double mean1 = 3.141592653589793;
        double stdDev1 = 2.718281828459045;
        for (int i = 0; i < 10000; i++) {
            double gaussian = generator.nextGaussian(mean1, stdDev1);
            assertTrue(gaussian >= mean1 - 3 * stdDev1 && gaussian <= mean1 + 3 * stdDev1);
        }
    }

    @Test
    public void testNextInt() {
        BitsStreamGenerator generator = new MersenneTwisterFastTest_018();
        for (int i = Integer.MIN_VALUE; i <= Integer.MAX_VALUE; i++) {
            int next = generator.nextInt();
            assertEquals(i, next);
            generator.nextInt();
        }
    }

    @Test
    public void testNextIntNegative() {
        BitsStreamGenerator generator = new MersenneTwisterFastTest_018();
        int n = -10;
        try {
            generator.nextInt(n);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testNextIntZero() {
        BitsStreamGenerator generator = new MersenneTwisterFastTest_018();
        int n = 0;
        try {
            generator.nextInt(n);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testNextIntPositive() {
        BitsStreamGenerator generator = new MersenneTwisterFastTest_018();
        int n = 10;
        int expected = 0;
        for (int i = 0; i < 10000; i++) {
            int next = generator.nextInt(n);
            assertTrue(next >= 0 && next < n);
            expected++;
            assertEquals(expected % n, next);
        }
    }

    @Test
    public void testNextLong() {
        BitsStreamGenerator generator = new MersenneTwisterFastTest_018();
        long min = Long.MIN_VALUE;
        long max = Long.MAX_VALUE;
        for (int i = 0; i < 10000; i++) {
            long next = generator.nextLong();
            assertTrue(next >= min && next <= max);
        }
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testNextLongNegative() {
        BitsStreamGenerator generator = new MersenneTwisterFastTest_018();
        long n = -10;
        generator.nextLong(n);
    }
}