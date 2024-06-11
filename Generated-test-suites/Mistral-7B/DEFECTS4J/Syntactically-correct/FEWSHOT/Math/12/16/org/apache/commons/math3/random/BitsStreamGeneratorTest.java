package org.apache.commons.math3.random;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class BitsStreamGeneratorTest {

    @Test(expected = NotStrictlyPositiveException.class)
    public void testNextIntNegativeArgument() {
        BitsStreamGenerator generator = new MersenneTwisterFastRandomGenerator();
        generator.nextInt(-3);
    }

    @Test
    public void testNextBoolean() {
        BitsStreamGenerator generator = new MersenneTwisterFastRandomGenerator();
        for (int i = 0; i < 10; i++) {
            assertTrue(generator.nextBoolean());
        }
    }

    @Test
    public void testNextBooleanMultipleFalse() {
        BitsStreamGenerator generator = new MersenneTwisterFastRandomGenerator();
        for (int i = 0; i < 10; i++) {
            assertFalse(generator.nextBoolean());
        }
    }

    @Test
    public void testNextBytesEmpty() {
        BitsStreamGenerator generator = new MersenneTwisterFastRandomGenerator();
        byte[] bytes = new byte[0];
        generator.nextBytes(bytes);
        assertArrayEquals(new byte[0], bytes);
    }

    @Test
    public void testNextBytes() {
        BitsStreamGenerator generator = new MersenneTwisterFastRandomGenerator();
        byte[] bytes = new byte[5];
        generator.nextBytes(bytes);
        for (int i = 0; i < bytes.length; i++) {
            assertTrue(bytes[i] >= 0 && bytes[i] <= 0xff);
        }
    }

    @Test
    public void testNextBytesTwiceSame() {
        BitsStreamGenerator generator = new MersenneTwisterFastRandomGenerator();
        byte[] bytes1 = new byte[5];
        generator.nextBytes(bytes1);
        byte[] bytes2 = new byte[5];
        generator.nextBytes(bytes2);
        assertArrayEquals(bytes1, bytes2);
    }

    @Test
    public void testNextBytesTwiceDifferent() {
        BitsStreamGenerator generator = new MersenneTwisterFastRandomGenerator();
        byte[] bytes1 = new byte[5];
        generator.nextBytes(bytes1);
        byte[] bytes2 = new byte[5];
        generator.nextBytes(bytes2);
        assertNotSame(bytes1, bytes2);
    }

    @Test
    public void testNextDouble() {
        BitsStreamGenerator generator = new MersenneTwisterFastRandomGenerator();
        double d = generator.nextDouble();
        assertTrue(d >= 0.0 && d <= 1.0);
    }

    @Test
    public void testNextFloat() {
        BitsStreamGenerator generator = new MersenneTwisterFastRandomGenerator();
        float f = generator.nextFloat();
        assertTrue(f >= 0.0f && f <= 1.0f);
    }

    @Test
    public void testNextGaussian() {
        BitsStreamGenerator generator = new MersenneTwisterGaussianRandomGenerator();
        for (int i = 0; i < 10; i++) {
            double gaussian1 = generator.nextGaussian();
            double gaussian2 = generator.nextGaussian();
            assertEquals(gaussian1, gaussian2, 0.0001);
        }
    }

    @Test
    public void testNextInt() {
        BitsStreamGenerator generator = new MersenneTwisterFastRandomGenerator();
        for (int i = 0; i < 10; i++) {
            int num = generator.nextInt();
            assertTrue(num >= 0 && num <= Integer.MAX_VALUE);
        }
    }

    @Test
    public void testNextIntPowerOfTwo() {
        BitsStreamGenerator generator = new MersenneTwisterFastRandomGenerator();
        for (int i = 0; i < 5; i++) {
            int num = generator.nextInt(1 << i);
            assertEquals(num, (int) ((1 << i) * (next(31) >> (31 - i))));
        }
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testNextIntNegativePowerOfTwo() {
        BitsStreamGenerator generator = new MersenneTwisterFastRandomGenerator();
        generator.nextInt(-1 << 5);
    }

    @Test
    public void testNextIntNotPowerOfTwo() {
        BitsStreamGenerator generator = new MersenneTwisterFastRandomGenerator();
        int num = generator.nextInt(7);
        int numExpected = next(6) % 7;
        assertEquals(numExpected, num);
    }

    @Test
    public void testNextLong() {
        BitsStreamGenerator generator = new MersenneTwisterFastRandomGenerator();
        long num = generator.nextLong();
        assertTrue(num >= 0L && num <= Long.MAX_VALUE);
    }

    @Test
    public void testClearNextGaussian() {
        BitsStreamGenerator generator = new MersenneTwisterGaussianRandomGenerator();
        double gaussian1 = generator.nextGaussian();
        generator.clear();
        double gaussian2 = generator.nextGaussian();
        assertNotSame(gaussian1, gaussian2);
    }
}