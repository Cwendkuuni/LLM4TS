package org.apache.commons.math3.random;

import static org.junit.Assert.*;

import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.junit.Test;

public class BitsStreamGeneratorTest {

    @Test(expected = NotStrictlyPositiveException.class)
    public void testNextIntNegativeArgument() {
        BitsStreamGenerator generator = new MersenneTwisterFastRandomGenerator(); // Assuming a valid implementation
        generator.nextInt(-1);
    }

    @Test
    public void testNextBoolean() {
        BitsStreamGenerator generator = new MersenneTwisterFastRandomGenerator(); // Assuming a valid implementation
        assertTrue(generator.nextBoolean());
        assertTrue(generator.nextBoolean());
        assertFalse(generator.nextBoolean());
        assertFalse(generator.nextBoolean());
        assertTrue(generator.nextBoolean());
    }

    @Test
    public void testNextBytes_ByteArray() {
        BitsStreamGenerator generator = new MersenneTwisterFastRandomGenerator(); // Assuming a valid implementation
        byte[] bytes = new byte[10];
        generator.nextBytes(bytes);
        for (byte b : bytes) {
            assertTrue(b >= 0 && b <= 0xFF);
        }
        generator.nextBytes(bytes);
        for (byte b : bytes) {
            assertTrue(b >= 0 && b <= 0xFF);
        }
    }

    @Test
    public void testNextDouble() {
        BitsStreamGenerator generator = new MersenneTwisterFastRandomGenerator(); // Assuming a valid implementation
        for (int i = 0; i < 100; i++) {
            double random = generator.nextDouble();
            assertTrue(random >= 0 && random <= 1d);
        }
    }

    @Test
    public void testNextFloat() {
        BitsStreamGenerator generator = new MersenneTwisterFastRandomGenerator(); // Assuming a valid implementation
        for (int i = 0; i < 100; i++) {
            float random = generator.nextFloat();
            assertTrue(random >= 0f && random <= 1f);
        }
    }

    @Test
    public void testNextGaussian() {
        BitsStreamGenerator generator = new MersenneTwisterFastRandomGenerator(); // Assuming a valid implementation
        for (int i = 0; i < 100; i++) {
            double random = generator.nextGaussian();
            assertTrue(random >= -3 && random <= 3);
        }
    }

    @Test
    public void testNextInt() {
        BitsStreamGenerator generator = new MersenneTwisterFastRandomGenerator(); // Assuming a valid implementation
        int max = Integer.MAX_VALUE;
        for (int i = 0; i < 100; i++) {
            int random = generator.nextInt();
            assertTrue(random >= 0 && random <= max);
        }
    }

    @Test
    public void testNextIntPowerOf2() {
        BitsStreamGenerator generator = new MersenneTwisterFastRandomGenerator(); // Assuming a valid implementation
        for (int i = 0; i < 10; i++) {
            int powerOfTwo = 1 << i;
            int random = generator.nextInt(powerOfTwo);
            assertEquals(powerOfTwo, random);
        }
    }

    @Test
    public void testNextIntNonPowerOf2() {
        BitsStreamGenerator generator = new MersenneTwisterFastRandomGenerator(); // Assuming a valid implementation
        for (int n = 1; n <= 10; n++) {
            for (int i = 0; i < 100; i++) {
                int random = generator.nextInt(n);
                int remainder = Integer.MAX_VALUE % n;
                assertTrue(random >= 0 && random < Integer.MAX_VALUE);
                assertTrue(random % n == remainder);
            }
        }
    }

    @Test
    public void testNextLong() {
        BitsStreamGenerator generator = new MersenneTwisterFastRandomGenerator(); // Assuming a valid implementation
        long max = Long.MAX_VALUE;
        for (int i = 0; i < 100; i++) {
            long random = generator.nextLong();
            assertTrue(random >= 0 && random <= max);
        }
    }

    @Test
    public void testClear() {
        BitsStreamGenerator generator = new MersenneTwisterFastRandomGenerator(); // Assuming a valid implementation
        generator.nextGaussian();
        generator.clear();
        assertFalse(generator.isNextGaussianSet());
    }
}