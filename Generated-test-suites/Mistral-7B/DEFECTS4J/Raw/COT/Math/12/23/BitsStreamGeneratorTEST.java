package org.apache.commons.math3.random;

import static org.junit.Assert.*;

import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.random.BitsStreamGenerator;
import org.apache.commons.math3.util.FastMath;
import org.junit.Test;

/**
 * Test class for {@link BitsStreamGenerator}.
 */
public class BitsStreamGeneratorTest {

    /**
     * Test constructor.
     */
    @Test
    public void testConstructor() {
        BitsStreamGenerator generator = new BitsStreamGenerator();
        assertNotNull(generator);
    }

    // --------------------------------------------------------------------

    /**
     * Test {@link BitsStreamGenerator#setSeed(int)}.
     */
    @Test
    public void testSetSeedInt() {
        BitsStreamGenerator generator = new BitsStreamGenerator();
        int seed = 123456;
        generator.setSeed(seed);
        // check seed
        generator.nextInt();
        long expected = FastMath.nextLong(seed);
        long actual = generator.nextLong();
        assertEquals("Unexpected seed value", expected, actual);
    }

    /**
     * Test {@link BitsStreamGenerator#setSeed(int[])}.
     */
    @Test
    public void testSetSeedIntArray() {
        BitsStreamGenerator generator = new BitsStreamGenerator();
        int[] seeds = { 1, 2, 3, 4, 5 };
        generator.setSeed(seeds);
        // check seeds
        for (int i = 0; i < seeds.length; i++) {
            long expected = FastMath.nextLong(seeds[i]);
            long actual = generator.nextLong();
            assertEquals("Unexpected seed value", expected, actual);
            generator.nextInt();
        }
    }

    /**
     * Test {@link BitsStreamGenerator#setSeed(long)}.
     */
    @Test
    public void testSetSeedLong() {
        BitsStreamGenerator generator = new BitsStreamGenerator();
        long seed = 1234567890123L;
        generator.setSeed(seed);
        // check seed
        long expected = FastMath.nextLong(seed);
        long actual = generator.nextLong();
        assertEquals("Unexpected seed value", expected, actual);
    }

    // --------------------------------------------------------------------

    /**
     * Test {@link BitsStreamGenerator#next(int)}.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testNextIntAbstract() {
        BitsStreamGenerator generator = new BitsStreamGenerator();
        int bits = 3;
        generator.next(bits);
    }

    /**
     * Test {@link BitsStreamGenerator#nextBoolean()}.
     */
    @Test
    public void testNextBoolean() {
        BitsStreamGenerator generator = new BitsStreamGenerator();
        boolean expected = true;
        boolean actual = generator.nextBoolean();
        assertEquals("Unexpected boolean value", expected, actual);

        expected = false;
        actual = generator.nextBoolean();
        assertEquals("Unexpected boolean value", expected, actual);
    }

    /**
     * Test {@link BitsStreamGenerator#nextBytes(byte[])}.
     */
    @Test
    public void testNextBytes() {
        BitsStreamGenerator generator = new BitsStreamGenerator();
        byte[] bytes = new byte[10];
        generator.nextBytes(bytes);
        for (byte b : bytes) {
            assertTrue("Unexpected byte value: " + b, b >= 0 && b <= 0xff);
        }

        byte[] bytes2 = new byte[1];
        generator.nextBytes(bytes2);
        assertTrue("Unexpected byte value: " + bytes2[0], bytes2[0] >= 0 && bytes2[0] <= 0xff);
    }

    /**
     * Test {@link BitsStreamGenerator#nextDouble()}.
     */
    @Test
    public void testNextDouble() {
        BitsStreamGenerator generator = new BitsStreamGenerator();
        double expectedMin = 0.0;
        double expectedMax = 1.0;
        double actual = generator.nextDouble();
        assertTrue("Unexpected double value: " + actual, actual >= expectedMin && actual <= expectedMax);
    }

    /**
     * Test {@link BitsStreamGenerator#nextFloat()}.
     */
    @Test
    public void testNextFloat() {
        BitsStreamGenerator generator = new BitsStreamGenerator();
        float expectedMin = 0.0f;
        float expectedMax = 1.0f;
        float actual = generator.nextFloat();
        assertTrue("Unexpected float value: " + actual, actual >= expectedMin && actual <= expectedMax);
    }

    /**
     * Test {@link BitsStreamGenerator#nextGaussian()}.
     */
    @Test
    public void testNextGaussian() {
        BitsStreamGenerator generator = new BitsStreamGenerator();
        double expectedMean = 0.0;
        double expectedStdDev = 1.0;
        double actual;
        for (int i = 0; i < 100; i++) {
            actual = generator.nextGaussian();
            double deviation = Math.abs(actual - expectedMean);
            assertTrue("Unexpected gaussian value: " + actual, deviation <= expectedStdDev);
            generator.clear();
        }
    }

    /**
     * Test {@link BitsStreamGenerator#nextInt()}.
     */
    @Test
    public void testNextInt() {
        BitsStreamGenerator generator = new BitsStreamGenerator();
        int expectedMin = Integer.MIN_VALUE;
        int expectedMax = Integer.MAX_VALUE;
        int actual = generator.nextInt();
        assertTrue("Unexpected int value: " + actual, actual >= expectedMin && actual <= expectedMax);

        int bits = 3;
        int actualBits = generator.next(bits);
        int expectedBits = FastMath.nextInt(1 << bits);
        assertEquals("Unexpected number of bits: " + actualBits, expectedBits, actualBits);

        expectedMin = 0;
        expectedMax = 5;
        actual = generator.nextInt(expectedMax + 1);
        assertTrue("Unexpected int value: " + actual, actual >= expectedMin && actual <= expectedMax);

        expectedMin = -128;
        expectedMax = 127;
        actual = generator.nextInt(expectedMax + 1);
        assertTrue("Unexpected int value: " + actual, actual >= expectedMin && actual <= expectedMax);
    }

    /**
     * Test {@link BitsStreamGenerator#nextLong()}.
     */
    @Test
    public void testNextLong() {
        BitsStreamGenerator generator = new BitsStreamGenerator();
        long expectedMin = Long.MIN_VALUE;
        long expectedMax = Long.MAX_VALUE;
        long actual = generator.nextLong();
        assertTrue("Unexpected long value: " + actual, actual >= expectedMin && actual <= expectedMax);
    }

    /**
     * Test {@link BitsStreamGenerator#clear()}.
     */
    @Test
    public void testClear() {
        BitsStreamGenerator generator = new BitsStreamGenerator();
        double expectedMean = 0.0;
        double expectedStdDev = 1.0;
        double actual1, actual2;
        for (int i = 0; i < 5; i++) {
            actual1 = generator.nextGaussian();
            generator.clear();
            actual2 = generator.nextGaussian();
            assertTrue("Unexpected gaussian values: " + actual1 + ", " + actual2,
                    Math.abs(actual1 - expectedMean) <= expectedStdDev &&
                            Math.abs(actual2 - expectedMean) <= expectedStdDev);
        }
    }

    // --------------------------------------------------------------------

    /**
     * Test {@link BitsStreamGenerator#nextInt(int)} with negative size.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNextIntNegativeSize() {
        BitsStreamGenerator generator = new BitsStreamGenerator();
        int size = -1;
        generator.nextInt(size);
    }

    /**
     * Test {@link BitsStreamGenerator#nextInt(int)} with zero size.
     */
    @Test
    public void testNextIntZeroSize() {
        BitsStreamGenerator generator = new BitsStreamGenerator();
        int size = 0;
        int actual = generator.nextInt(size);
        assertEquals("Unexpected int value: " + actual, 0, actual);
    }

    /**
     * Test {@link BitsStreamGenerator#nextInt(int)} with size larger than available bits.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNextIntLargeSize() {
        BitsStreamGenerator generator = new BitsStreamGenerator();
        int size = 64;
        generator.nextInt(size);
    }

    // --------------------------------------------------------------------

    /**
     * Test {@link BitsStreamGenerator#nextLong()} with negative size.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNextLongNegativeSize() {
        BitsStreamGenerator generator = new BitsStreamGenerator();
        long size = -1;
        generator.nextLong(size);
    }

    /**
     * Test {@link BitsStreamGenerator#nextLong()} with zero size.
     */
    @Test
    public void testNextLongZeroSize() {
        BitsStreamGenerator generator = new BitsStreamGenerator();
        long size = 0;
        long actual = generator.nextLong(size);
        assertEquals("Unexpected long value: " + actual, 0, actual);
    }

    /**
     * Test {@link BitsStreamGenerator#nextLong()} with size larger than available bits.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNextLongLargeSize() {
        BitsStreamGenerator generator = new BitsStreamGenerator();
        long size = 64;
        generator.nextLong(size);
    }

    // --------------------------------------------------------------------

    /**
     * Test {@link BitsStreamGenerator#setSeed(int)} with negative seed.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSetSeedNegativeSeed() {
        BitsStreamGenerator generator = new BitsStreamGenerator();
        int seed = -1;
        generator.setSeed(seed);
    }

    /**
     * Test {@link BitsStreamGenerator#setSeed(int[])}.
     */
    @Test
    public void testSetSeedIntArrayEmpty() {
        BitsStreamGenerator generator = new BitsStreamGenerator();
        int[] seeds = {};
        generator.setSeed(seeds);
    }

    /**
     * Test {@link BitsStreamGenerator#setSeed(int[])}.
     */
    @Test
    public void testSetSeedIntArrayNull() {
        BitsStreamGenerator generator = new BitsStreamGenerator();
        int[] seeds = null;
        try {
            generator.setSeed(seeds);
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // success
        }
    }

    /**
     * Test {@link BitsStreamGenerator#setSeed(int[])}.
     */
    @Test
    public void testSetSeedIntArrayTooShort() {
        BitsStreamGenerator generator = new BitsStreamGenerator();
        int[] seeds = { 1, 2 };
        try {
            generator.setSeed(seeds);
            fail("IllegalArgumentException expected");
        } catch (NotStrictlyPositiveException e) {
            // success
        }
    }

    /**
     * Test {@link BitsStreamGenerator#setSeed(int[])}.
     */
    @Test
    public void testSetSeedIntArray() {
        BitsStreamGenerator generator = new BitsStreamGenerator();
        int[] seeds = { 1, 2, 3, 4 };
        generator.setSeed(seeds);
        long expected = FastMath.nextLong(seeds[0]);
        long actual = generator.nextLong();
        assertEquals("Unexpected seed value", expected, actual);

        // check remaining seeds
        for (int i = 1; i < seeds.length; i++) {
            long expected2 = FastMath.nextLong(seeds[i]);
            long actual2 = generator.nextLong();
            assertEquals("Unexpected seed value", expected2, actual2);
            generator.nextLong();
        }
    }

    // --------------------------------------------------------------------

    /**
     * Test {@link BitsStreamGenerator#setSeed(long)} with negative seed.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSetSeedNegativeLongSeed() {
        BitsStreamGenerator generator = new BitsStreamGenerator();
        long seed = -1;
        generator.setSeed(seed);
    }

    /**
     * Test {@link BitsStreamGenerator#setSeed(long)} with zero seed.
     */
    @Test
    public void testSetSeedZeroLongSeed() {
        BitsStreamGenerator generator = new BitsStreamGenerator();
        long seed = 0;
        generator.setSeed(seed);
    }

    /**
     * Test {@link BitsStreamGenerator#setSeed(long)} with seed larger than Long.MAX_VALUE.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSetSeedLargeLongSeed() {
        BitsStreamGenerator generator = new BitsStreamGenerator();
        long seed = Long.MAX_VALUE + 1;
        generator.setSeed(seed);
    }

    /**
     * Test {@link BitsStreamGenerator#setSeed(long)}.
     */
    @Test
    public void testSetSeedLong() {
        BitsStreamGenerator generator = new BitsStreamGenerator();
        long seed = 1234567890123L;
        generator.setSeed(seed);
        long expected = FastMath.nextLong(seed);
        long actual = generator.nextLong();
        assertEquals("Unexpected seed value", expected, actual);
    }

    // --------------------------------------------------------------------

    /**
     * Test {@link BitsStreamGenerator#nextInt(int)} with negative size.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNextIntNegativeSize() {
        BitsStreamGenerator generator = new BitsStreamGenerator();
        int size = -1;
        generator.nextInt(size);
    }

    /**
     * Test {@link BitsStreamGenerator#nextInt(int)} with zero size.
     */
    @Test
    public void testNextIntZeroSize() {
        BitsStreamGenerator generator = new BitsStreamGenerator();
        int size = 0;
        int actual = generator.nextInt(size);
        assertEquals("Unexpected int value", 0, actual);
    }

    /**
     * Test {@link BitsStreamGenerator#nextInt(int)} with size larger than available bits.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNextIntLargeSize() {
        BitsStreamGenerator generator = new BitsStreamGenerator();
        int size = 64;
        generator.nextInt(size);
    }

    // --------------------------------------------------------------------

    /**
     * Test {@link BitsStreamGenerator#nextLong()} with negative size.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNextLongNegativeSize() {
        BitsStreamGenerator generator = new BitsStreamGenerator();
        long size = -1;
        generator.nextLong(size);
    }

    /**
     * Test {@link BitsStreamGenerator#nextLong()} with zero size.
     */
    @Test
    public void testNextLongZeroSize() {
        BitsStreamGenerator generator = new BitsStreamGenerator();
        long size = 0;
        long actual = generator.nextLong(size);
        assertEquals("Unexpected long value", 0, actual);
    }

    /**
     * Test {@link BitsStreamGenerator#nextLong()} with size larger than available bits.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNextLongLargeSize() {
        BitsStreamGenerator generator = new BitsStreamGenerator();
        long size = 64;
        generator.nextLong(size);
    }

}