package org.apache.commons.math3.random;

import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.util.FastMath;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test class for {@link BitsStreamGenerator}.
 */
public class BitsStreamGeneratorTest {

    private static final int TEST_SIZE = 10;
    private static final int TEST_MAX = 100;
    private static final int TEST_MIN = 0;
    private static final int TEST_POWER_OF_TWO = 16;

    /**
     * Test constructor.
     */
    @Test
    public void testConstructor() {
        new BitsStreamGenerator();
    }

    /**
     * Test {@link BitsStreamGenerator#nextBoolean()}.
     */
    @Test
    public void testNextBoolean() {
        BitsStreamGenerator generator = new MyBitsStreamGenerator();
        for (int i = 0; i < TEST_SIZE; i++) {
            boolean result = generator.nextBoolean();
            assertTrue(result || !result);
        }
    }

    /**
     * Test {@link BitsStreamGenerator#nextBytes(byte[])}.
     */
    @Test
    public void testNextBytes() {
        BitsStreamGenerator generator = new MyBitsStreamGenerator();
        byte[] result = new byte[TEST_SIZE];
        generator.nextBytes(result);
        for (int i = 0; i < TEST_SIZE; i++) {
            for (int j = 0; j < 8; j++) {
                int bit = (result[i] >>> j) & 0x00000001;
                assertTrue(bit == 0 || bit == 1);
            }
        }
    }

    /**
     * Test {@link BitsStreamGenerator#nextDouble()}.
     */
    @Test
    public void testNextDouble() {
        BitsStreamGenerator generator = new MyBitsStreamGenerator();
        for (int i = 0; i < TEST_SIZE; i++) {
            double result = generator.nextDouble();
            assertTrue(result >= 0.0 && result <= 1.0);
        }
    }

    /**
     * Test {@link BitsStreamGenerator#nextFloat()}.
     */
    @Test
    public void testNextFloat() {
        BitsStreamGenerator generator = new MyBitsStreamGenerator();
        for (int i = 0; i < TEST_SIZE; i++) {
            float result = generator.nextFloat();
            assertTrue(Float.isFinite(result));
            assertTrue(result >= 0.0f && result <= 1.0f);
        }
    }

    /**
     * Test {@link BitsStreamGenerator#nextGaussian()}.
     */
    @Test
    public void testNextGaussian() {
        BitsStreamGenerator generator = new MyBitsStreamGenerator();
        for (int i = 0; i < TEST_SIZE; i++) {
            double result = generator.nextGaussian();
            assertTrue(result >= -3.0 && result <= 3.0);
        }
    }

    /**
     * Test {@link BitsStreamGenerator#nextInt()}.
     */
    @Test
    public void testNextInt() {
        BitsStreamGenerator generator = new MyBitsStreamGenerator();
        for (int i = 0; i < TEST_SIZE; i++) {
            int result = generator.nextInt();
            assertTrue(result >= 0 && result <= Integer.MAX_VALUE);
        }
    }

    /**
     * Test {@link BitsStreamGenerator#nextInt(int)}.
     */
    @Test
    public void testNextIntWithLimit() {
        BitsStreamGenerator generator = new MyBitsStreamGenerator();
        for (int i = 0; i < TEST_SIZE; i++) {
            int limit = (int) (Math.random() * TEST_MAX) + TEST_MIN;
            int result = generator.nextInt(limit);
            assertTrue(result >= 0 && result <= limit);
        }
    }

    /**
     * Test {@link BitsStreamGenerator#nextInt(int)} with negative limit.
     *
     * @expectedException java.lang.IllegalArgumentException
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNextIntNegativeLimit() {
        BitsStreamGenerator generator = new MyBitsStreamGenerator();
        int limit = TEST_MIN - 1;
        generator.nextInt(limit);
    }

    /**
     * Test {@link BitsStreamGenerator#nextInt(int)} with zero limit.
     *
     * @expectedException java.lang.IllegalArgumentException
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNextIntZeroLimit() {
        BitsStreamGenerator generator = new MyBitsStreamGenerator();
        generator.nextInt(0);
    }

    /**
     * Test {@link BitsStreamGenerator#nextInt(int)} with non-power-of-2 limit.
     */
    @Test
    public void testNextIntWithNonPowerOfTwoLimit() {
        BitsStreamGenerator generator = new MyBitsStreamGenerator();
        for (int i = 0; i < TEST_SIZE; i++) {
            int limit = TEST_POWER_OF_TWO * 2 + TEST_POWER_OF_TWO / 2;
            int result = generator.nextInt(limit);
            int mask = limit - 1;
            int bitCount = Integer.bitCount(mask);
            int expectedBitCount = (int) Math.ceil(Math.log(limit) / Math.log(2));
            assertEquals(expectedBitCount, bitCount);
            assertTrue(result >= 0 && result <= limit);
        }
    }

    /**
     * Test {@link BitsStreamGenerator#nextInt(int)} with power-of-2 limit.
     */
    @Test
    public void testNextIntWithPowerOfTwoLimit() {
        BitsStreamGenerator generator = new MyBitsStreamGenerator();
        for (int i = 0; i < TEST_SIZE; i++) {
            int limit = 1 << TEST_POWER_OF_TWO;
            int result = generator.nextInt(limit);
            int mask = limit - 1;
            int bitCount = Integer.bitCount(mask);
            int expectedBitCount = TEST_POWER_OF_TWO;
            assertEquals(expectedBitCount, bitCount);
            assertTrue(result >= 0 && result <= limit);
        }
    }

    /**
     * Test {@link BitsStreamGenerator#nextLong()}.
     */
    @Test
    public void testNextLong() {
        BitsStreamGenerator generator = new MyBitsStreamGenerator();
        for (int i = 0; i < TEST_SIZE; i++) {
            long result = generator.nextLong();
            assertTrue(result >= 0 && result <= Long.MAX_VALUE);
        }
    }

    /**
     * Test {@link BitsStreamGenerator#setSeed(int)}.
     */
    @Test
    public void testSetSeedInt() {
        BitsStreamGenerator generator = new MyBitsStreamGenerator();
        int seed = (int) (Math.random() * Integer.MAX_VALUE);
        generator.setSeed(seed);
        int result = generator.nextInt();
        int expected = seed;
        assertEquals(expected, result);
    }

    /**
     * Test {@link BitsStreamGenerator#setSeed(int[])}.
     */
    @Test
    public void testSetSeedIntArray() {
        BitsStreamGenerator generator = new MyBitsStreamGenerator();
        int[] seed = new int[3];
        for (int i = 0; i < seed.length; i++) {
            seed[i] = (int) (Math.random() * Integer.MAX_VALUE);
        }
        generator.setSeed(seed);
        int result = generator.nextInt();
        int expected = seed[seed.length - 1];
        assertEquals(expected, result);
    }

    /**
     * Test {@link BitsStreamGenerator#setSeed(long)}.
     */
    @Test
    public void testSetSeedLong() {
        BitsStreamGenerator generator = new MyBitsStreamGenerator();
        long seed = Math.abs(Math.random() * Long.MAX_VALUE);
        generator.setSeed(seed);
        long result = generator.nextLong();
        assertEquals(seed, result);
    }

    /**
     * Test {@link BitsStreamGenerator#clear()}.
     */
    @Test
    public void testClear() {
        BitsStreamGenerator generator = new MyBitsStreamGenerator();
        double gaussian = generator.nextGaussian();
        generator.clear();
        assertFalse(Double.isNaN(gaussian));
        generator.nextGaussian();
        assertTrue(Double.isNaN(gaussian));
    }

    /**
     * Test {@link BitsStreamGenerator#next(int)} with positive argument.
     */
    @Test
    public void testNextWithPositiveArgument() {
        BitsStreamGenerator generator = new MyBitsStreamGenerator();
        for (int i = 0; i < TEST_SIZE; i++) {
            int bits = 1 + (int) (Math.random() * TEST_MAX);
            int result = generator.next(bits);
            assertTrue(result >= 0 && result <= (1 << bits) - 1);
        }
    }

    /**
     * Test {@link BitsStreamGenerator#next(int)} with negative argument.
     *
     * @expectedException java.lang.IllegalArgumentException
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNextWithNegativeArgument() {
        BitsStreamGenerator generator = new MyBitsStreamGenerator();
        int bits = -1;
        generator.next(bits);
    }

    /**
     * Test {@link BitsStreamGenerator#next(int)} with zero argument.
     *
     * @expectedException java.lang.IllegalArgumentException
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNextWithZeroArgument() {
        BitsStreamGenerator generator = new MyBitsStreamGenerator();
        int bits = 0;
        generator.next(bits);
    }

    /**
     * Test {@link BitsStreamGenerator#next(int)} with argument equal to {@code Integer.SIZE}.
     */
    @Test
    public void testNextWithSizeArgument() {
        BitsStreamGenerator generator = new MyBitsStreamGenerator();
        int bits = Integer.SIZE;
        int result = generator.next(bits);
        int mask = bits - 1;
        int bitCount = Integer.bitCount(mask);
        int expectedBitCount = Integer.SIZE;
        assertEquals(expectedBitCount, bitCount);
        assertEquals(result, 0xFFFFFFFF << (32 - bitCount));
    }

    /**
     * Test {@link BitsStreamGenerator#next(int)} with argument greater than {@code Integer.SIZE}.
     */
    @Test
    public void testNextWithSizePlusOneArgument() {
        BitsStreamGenerator generator = new MyBitsStreamGenerator();
        int bits = Integer.SIZE + 1;
        int result = generator.next(bits);
        fail("Should have thrown an IllegalArgumentException");
    }

    private static class MyBitsStreamGenerator extends BitsStreamGenerator {
        // Empty implementation
    }

}