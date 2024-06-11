package org.apache.commons.math3.random;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class BitsStreamGeneratorTest {

    @Test
    public void testNextBoolean() {
        BitsStreamGenerator generator = new MersenneTwisterFastTest_53664510931415();
        assertTrue(generator.nextBoolean());
        assertTrue(generator.nextBoolean());
        assertFalse(generator.nextBoolean());
        assertFalse(generator.nextBoolean());
        assertFalse(generator.nextBoolean());
        assertFalse(generator.nextBoolean());
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testNextIntNegative() {
        BitsStreamGenerator generator = new MersenneTwisterFastTest_53664510931415();
        generator.nextInt(-1);
    }

    @Test
    public void testNextIntZero() {
        BitsStreamGenerator generator = new MersenneTwisterFastTest_53664510931415();
        assertEquals(0, generator.nextInt(0));
    }

    @Test
    public void testNextIntPowerOfTwo() {
        BitsStreamGenerator generator = new MersenneTwisterFastTest_53664510931415();
        for (int i = 1; i <= 32; i++) {
            int num = 1 << i;
            int result = generator.nextInt(num);
            assertTrue(result >= 0 && result < num);
        }
    }

    @Test
    public void testNextIntNotPowerOfTwo() {
        BitsStreamGenerator generator = new MersenneTwisterFastTest_53664510931415();
        for (int i = 33; i <= 32767; i++) {
            int result = generator.nextInt(i);
            assertTrue(result >= 0 && result < i);
        }
    }

    @Test
    public void testNextBytes() {
        BitsStreamGenerator generator = new MersenneTwisterFastTest_53664510931415();
        byte[] bytes = new byte[10];
        generator.nextBytes(bytes);
        for (byte b : bytes) {
            assertTrue(b >= 0 && b <= 255);
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNextBytesNull() {
        BitsStreamGenerator generator = new MersenneTwisterFastTest_53664510931415();
        generator.nextBytes(null);
    }

    @Test
    public void testNextDouble() {
        BitsStreamGenerator generator = new MersenneTwisterFastTest_53664510931415();
        assertEquals(0.0, generator.nextDouble(), 1e-8);
        assertEquals(1.0, generator.nextDouble(), 1e-8);
        double min = 1.5e-49d;
        double max = 1.0 - min;
        double value = generator.nextDouble();
        assertTrue(value > min && value < max);
    }

    @Test
    public void testNextFloat() {
        BitsStreamGenerator generator = new MersenneTwisterFastTest_53664510931415();
        float value = generator.nextFloat();
        assertTrue(value > 0.0f && value <= 1.0f);
    }

    @Test
    public void testNextGaussian() {
        BitsStreamGenerator generator = new MersenneTwisterFastTest_53664510931415();
        double mean = 0.0;
        double sigma = 1.0;
        double result = generator.nextGaussian(mean, sigma);
        assertEquals(mean, result, sigma * 3);
    }

    @Test
    public void testNextGaussianClear() {
        BitsStreamGenerator generator = new MersenneTwisterFastTest_53664510931415();
        generator.nextGaussian();
        generator.clear();
        double result = generator.nextGaussian();
        assertEquals(Double.NaN, result, 0.0);
    }

}

class MersenneTwisterFastTest_53664510931415 extends BitsStreamGenerator {
    private static final long MASK64 = 0xfffffffffffffL;

    public MersenneTwisterFastTest_53664510931415() {
        super();
    }

    @Override
    public synchronized void setSeed(int seed) {
        next(seed);
    }

    @Override
    public synchronized void setSeed(int[] seed) {
        long seed64 = 0;
        for (int i = 0; i < seed.length; i++) {
            seed64 = (seed64 << 32) | seed[i];
        }
        next(seed64);
    }

    @Override
    public synchronized void setSeed(long seed) {
        next(seed);
    }

    @Override
    protected int next(int bits) {
        long result = nextLong();
        return (int) (result >>> (32 - bits));
    }

    @Override
    protected long next(int bits) {
        long result = nextLong();
        return result >>> (32 - bits);
    }

    @Override
    public void nextBytes(byte[] bytes) {
        super.nextBytes(bytes);
        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = (byte) ((bytes[i] & 0x7f) | (nextByte() << 7));
        }
    }

    private byte nextByte() {
        long result = nextLong();
        return (byte) (result >>> 24);
    }
}