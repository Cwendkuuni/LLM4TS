package org.apache.commons.math3.random;

import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.util.FastMath;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * JUnit test file for the {@code BitsStreamGenerator} class.
 */
public class BitsStreamGeneratorTest {

    @Test
    public void testSetSeed() {
        BitsStreamGenerator generator = new MyBitsStreamGenerator();
        generator.setSeed(123456789);
        assertEquals(123456789, generator.nextInt());
    }

    @Test
    public void testSetSeedArray() {
        BitsStreamGenerator generator = new MyBitsStreamGenerator();
        int[] seed = {1, 2, 3, 4};
        generator.setSeed(seed);
        int[] expected = {1, 2, 3, 4};
        int[] actual = new int[4];
        generator.nextInts(actual, 0, 4);
        assertArrayEquals(expected, actual);
    }

    @Test
    public void testSetSeedLong() {
        BitsStreamGenerator generator = new MyBitsStreamGenerator();
        generator.setSeed(0x1234567890123456L);
        long expected = 0x1234567890123456L;
        long actual = generator.nextLong();
        assertEquals(expected, actual);
    }

    @Test
    public void testNextBoolean() {
        BitsStreamGenerator generator = new MyBitsStreamGenerator();
        assertTrue(generator.nextBoolean());
        assertTrue(generator.nextBoolean());
        assertFalse(generator.nextBoolean());
    }

    @Test
    public void testNextBytes() {
        BitsStreamGenerator generator = new MyBitsStreamGenerator();
        byte[] actual = new byte[10];
        generator.nextBytes(actual);
        for (byte b : actual) {
            assertTrue(b >= 0 && b <= 0xFF);
        }

        byte[] expected = new byte[]{0x01, 0x2A, 0x00, 0x42, 0x67, 0x34, 0x56, 0x78, 0x90, 0x12};
        byte[] actual2 = new byte[10];
        generator.nextBytes(actual2);
        assertArrayEquals(expected, actual2);
    }

    @Test
    public void testNextDouble() {
        BitsStreamGenerator generator = new MyBitsStreamGenerator();
        double expectedDouble1 = 0.123456789;
        double actualDouble1 = generator.nextDouble();
        assertEquals(expectedDouble1, actualDouble1, 0.000001);

        double expectedDouble2 = 0.333333333;
        double actualDouble2 = generator.nextDouble();
        assertEquals(expectedDouble2, actualDouble2, 0.000001);
    }

    @Test
    public void testNextFloat() {
        BitsStreamGenerator generator = new MyBitsStreamGenerator();
        float expectedFloat1 = 0.123456f;
        float actualFloat1 = generator.nextFloat();
        assertEquals(expectedFloat1, actualFloat1, 0.0001f);

        float expectedFloat2 = 0.5f;
        float actualFloat2 = generator.nextFloat();
        assertEquals(expectedFloat2, actualFloat2, 0.0001f);
    }

    @Test
    public void testNextGaussian() {
        BitsStreamGenerator generator = new MyBitsStreamGenerator();
        double expectedGaussian1 = 0.5 * FastMath.SQRT2;
        double actualGaussian1 = generator.nextGaussian();
        assertEquals(expectedGaussian1, actualGaussian1, 0.0001);

        double expectedGaussian2 = -0.5 * FastMath.SQRT2;
        double actualGaussian2 = generator.nextGaussian();
        assertEquals(expectedGaussian2, actualGaussian2, 0.0001);
    }

    @Test
    public void testNextInt() {
        BitsStreamGenerator generator = new MyBitsStreamGenerator();
        int expectedInt1 = 1;
        int actualInt1 = generator.nextInt();
        assertEquals(expectedInt1, actualInt1);

        int expectedInt2 = 10;
        int actualInt2 = generator.nextInt(10);
        assertEquals(expectedInt2, actualInt2);

        int expectedInt3 = -1;
        int actualInt3 = generator.nextInt(-1);
        assertEquals(expectedInt3, actualInt3);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testNextIntNegative() {
        BitsStreamGenerator generator = new MyBitsStreamGenerator();
        generator.nextInt(-1);
    }

    @Test
    public void testNextInts() {
        BitsStreamGenerator generator = new MyBitsStreamGenerator();
        int[] expected = {1, 2, 3, 4};
        int[] actual = new int[4];
        generator.nextInts(actual, 0, 4);
        assertArrayEquals(expected, actual);

        int[] expected2 = {10, 20, 30};
        int[] actual2 = new int[3];
        generator.nextInts(actual2, 0, 3);
        assertArrayEquals(expected2, actual2);
    }

    @Test
    public void testNextLong() {
        BitsStreamGenerator generator = new MyBitsStreamGenerator();
        long expectedLong1 = 1;
        long actualLong1 = generator.nextLong();
        assertEquals(expectedLong1, actualLong1);

        long expectedLong2 = 1000000000000L;
        long actualLong2 = generator.nextLong();
        assertEquals(expectedLong2, actualLong2);
    }

    @Test
    public void testClear() {
        BitsStreamGenerator generator = new MyBitsStreamGenerator();
        double expectedGaussian1 = 0.5 * FastMath.SQRT2;
        double actualGaussian1 = generator.nextGaussian();
        assertEquals(expectedGaussian1, actualGaussian1, 0.0001);

        generator.clear();

        double expectedGaussian2 = -0.5 * FastMath.SQRT2;
        double actualGaussian2 = generator.nextGaussian();
        assertEquals(expectedGaussian2, actualGaussian2, 0.0001);
        assertNotEquals(actualGaussian1, actualGaussian2);
    }

    private static class MyBitsStreamGenerator extends BitsStreamGenerator {
        @Override
        public void setSeed(int seed) {
            // dummy implementation
        }

        @Override
        public void setSeed(int[] seed) {
            // dummy implementation
        }

        @Override
        public void setSeed(long seed) {
            // dummy implementation
        }

        @Override
        protected int next(int bits) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void nextInts(int[] ints, int offset, int length) {
            for (int i = offset; i < offset + length; i++) {
                ints[i] = nextInt();
            }
        }
    }

}