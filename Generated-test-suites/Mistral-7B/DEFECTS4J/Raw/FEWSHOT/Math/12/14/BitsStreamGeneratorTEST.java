package org.apache.commons.math3.random;

import static org.junit.Assert.*;

import org.junit.Test;

public class BitsStreamGeneratorTest {
    // Test for abstract methods that cannot be tested directly

    @Test(expected = UnsupportedOperationException.class)
    public void testAbstractMethods() {
        BitsStreamGenerator generator = new MersenneTwisterFastRandomGenerator();
        try {
            generator.setSeed(1);
            generator.setSeed(new int[]{1, 2, 3});
            generator.setSeed(1L);
        } catch (IllegalArgumentException e) {
            // Expected for the int[]. This test checks the UnsupportedOperationException
            // for the other types.
            if (!(e.getCause() instanceof UnsupportedOperationException)) {
                throw e;
            }
        }
        generator.next(32);
        generator.nextBoolean();
        generator.nextBytes(new byte[1]);
        generator.nextDouble();
        generator.nextFloat();
        generator.nextGaussian();
        generator.nextInt();
        generator.nextInt(3);
        generator.nextInt(Integer.MAX_VALUE);
        generator.nextLong();
    }

    // Test for next(int bits) method

    @Test
    public void testNextBits() {
        MersenneTwisterFastRandomGenerator generator = new MersenneTwisterFastRandomGenerator();
        for (int i = 1; i <= 32; i++) {
            int bits = generator.next(i);
            assertTrue("next(" + i + ") should return a non-negative integer", bits >= 0);
            assertEquals("next(" + i + ") should return the expected number of random bits", i, Integer.bitCount(bits));
        }
    }

    // Test for nextBoolean() method

    @Test
    public void testNextBoolean() {
        MersenneTwisterFastRandomGenerator generator = new MersenneTwisterFastRandomGenerator();
        boolean result;
        for (int i = 0; i < 1000; i++) {
            result = generator.nextBoolean();
            assertTrue("nextBoolean() should return a boolean value", result instanceof Boolean);
            assertEquals("nextBoolean() should return either true or false", result, Math.random() > 0.5d);
        }
    }

    // Test for nextBytes(byte[]) method

    @Test
    public void testNextBytes() {
        MersenneTwisterFastRandomGenerator generator = new MersenneTwisterFastRandomGenerator();
        byte[] bytes = new byte[10];
        generator.nextBytes(bytes);
        for (byte b : bytes) {
            assertTrue("nextBytes() should return random bytes", b >= 0 && b <= 0xFF);
        }
    }

    // Test for nextDouble() method

    @Test
    public void testNextDouble() {
        MersenneTwisterFastRandomGenerator generator = new MersenneTwisterFastRandomGenerator();
        double result;
        for (int i = 0; i < 1000; i++) {
            result = generator.nextDouble();
            assertTrue("nextDouble() should return a non-negative double value", result >= 0.0);
            assertTrue("nextDouble() should return a finite double value", !Double.isNaN(result) && !Double.isInfinite(result));
            assertTrue("nextDouble() should return a random value", Math.abs(result - Math.random()) < 1e-6);
        }
    }

    // Test for nextFloat() method

    @Test
    public void testNextFloat() {
        MersenneTwisterFastRandomGenerator generator = new MersenneTwisterFastRandomGenerator();
        float result;
        for (int i = 0; i < 1000; i++) {
            result = generator.nextFloat();
            assertTrue("nextFloat() should return a float value", Float.floatToIntBits(result) >= 0);
            assertTrue("nextFloat() should return a random value", Math.abs(result - Math.randomFloat()) < 1e-6f);
        }
    }

    // Test for nextGaussian() method

    @Test
    public void testNextGaussian() {
        MersenneTwisterFastRandomGenerator generator = new MersenneTwisterFastRandomGenerator();
        double result;
        for (int i = 0; i < 1000; i++) {
            result = generator.nextGaussian();
            assertTrue("nextGaussian() should return a finite double value", !Double.isNaN(result) && !Double.isInfinite(result));
            assertTrue("nextGaussian() should follow the gaussian distribution", Math.abs(result - Math.sqrt(Math.random()) * 2 * Math.PI) < 2);
        }
    }

    // Test for nextInt() method

    @Test
    public void testNextInt() {
        MersenneTwisterFastRandomGenerator generator = new MersenneTwisterFastRandomGenerator();
        for (int i = 1; i <= Integer.MAX_VALUE; i++) {
            int result = generator.nextInt();
            assertTrue("nextInt() should return an integer value", result >= 0 && result <= Integer.MAX_VALUE);
            assertNotEquals("nextInt() should return a different value each time", result, i);
        }
    }

    // Test for nextInt(int n) method

    @Test
    public void testNextInt_int() {
        MersenneTwisterFastRandomGenerator generator = new MersenneTwisterFastRandomGenerator();
        for (int n = 1; n <= 10; n++) {
            for (int i = 0; i < 1000; i++) {
                int result = generator.nextInt(n);
                assertTrue("nextInt(" + n + ") should return a number between 0 and " + (n - 1), result >= 0 && result < n);
                assertNotEquals("nextInt(" + n + ") should return a different value each time", result, i % n);
            }
        }
    }

    // Test for nextLong() method

    @Test
    public void testNextLong() {
        MersenneTwisterFastRandomGenerator generator = new MersenneTwisterFastRandomGenerator();
        long result;
        for (int i = 0; i < 1000; i++) {
            result = generator.nextLong();
            assertTrue("nextLong() should return a long value", result >= 0);
            assertNotEquals("nextLong() should return a different value each time", result, i);
        }
    }

    // Test for clear() method

    @Test
    public void testClear() {
        MersenneTwisterFastRandomGenerator generator = new MersenneTwisterFastRandomGenerator();
        generator.nextGaussian();
        generator.clear();
        assertFalse("nextGaussian() after clear() should return NaN", !Double.isNaN(generator.nextGaussian()));
    }
}