package org.apache.commons.math3.random;

import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.util.FastMath;
import org.junit.*;
import static org.junit.Assert.*;

public class BitsStreamGeneratorTest {

    private BitsStreamGenerator generator;

    @Before
    public void setUp() {
        generator = new BitsStreamGenerator();
    }

    @Test
    public void testNextBoolean() {
        assertTrue(generator.nextBoolean());
        assertTrue(generator.nextBoolean());
        assertFalse(generator.nextBoolean());
        assertFalse(generator.nextBoolean());
        generator.setSeed(1);
        assertTrue(generator.nextBoolean());
        assertFalse(generator.nextBoolean());
        assertFalse(generator.nextBoolean());
        assertFalse(generator.nextBoolean());
    }

    @Test
    public void testNextBytes() {
        byte[] expected = new byte[]{1, 2, 3, 4};
        byte[] actual = new byte[4];
        generator.nextBytes(expected);
        generator.nextBytes(actual);
        assertArrayEquals(expected, actual);
        byte[] expected2 = new byte[]{0};
        byte[] actual2 = new byte[1];
        generator.nextBytes(expected2);
        generator.nextBytes(actual2);
        assertArrayEquals(expected2, actual2);
        generator.setSeed(1);
        byte[] expected3 = new byte[]{1, 2, 3, 4};
        byte[] actual3 = new byte[4];
        generator.nextBytes(expected3);
        generator.nextBytes(actual3);
        assertArrayEquals(expected3, actual3);
    }

    @Test
    public void testNextDouble() {
        double expected = 0.12345;
        double actual = generator.nextDouble();
        assertEquals(expected, actual, 0.00001);
        double expected2 = Double.MAX_VALUE;
        double actual2 = generator.nextDouble();
        assertTrue(actual2 > Double.MAX_VALUE - 0.00001);
        double expected3 = Double.MIN_VALUE;
        double actual3 = generator.nextDouble();
        assertTrue(actual3 < Double.MIN_VALUE + 0.00001);
        generator.setSeed(1);
        double expected4 = 0.12345;
        double actual4 = generator.nextDouble();
        assertEquals(expected4, actual4, 0.00001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNextIntWithNegativeNumber() {
        generator.nextInt(-1);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testNextIntWithZero() {
        generator.nextInt(0);
    }

    @Test
    public void testNextInt() {
        int expected = 123;
        int actual = generator.nextInt();
        assertEquals(expected, actual);
        int expected2 = 456;
        int actual2 = generator.nextInt();
        assertEquals(expected2, actual2);
        generator.setSeed(1);
        int expected3 = 123;
        int actual3 = generator.nextInt();
        assertEquals(expected3, actual3);
        int expected4 = 456;
        int actual4 = generator.nextInt();
        assertEquals(expected4, actual4);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNextIntWithNegativeSeed() {
        generator.setSeed(-1);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testNextIntWithZeroSeed() {
        generator.setSeed(0);
    }

    @Test
    public void testNextLong() {
        long expected = 9876543210987654321L;
        long actual = generator.nextLong();
        assertEquals(expected, actual);
        long expected2 = Long.MAX_VALUE;
        long actual2 = generator.nextLong();
        assertTrue(actual2 > Long.MAX_VALUE - 1);
        long expected3 = Long.MIN_VALUE;
        long actual3 = generator.nextLong();
        assertTrue(actual3 < Long.MIN_VALUE + 1);
        generator.setSeed(1);
        long expected4 = 9876543210987654321L;
        long actual4 = generator.nextLong();
        assertEquals(expected4, actual4);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNextLongWithNegativeNumber() {
        generator.nextLong(-1);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testNextLongWithZero() {
        generator.nextLong(0);
    }

    @Test
    public void testClear() {
        generator.nextGaussian();
        generator.clear();
        assertNaN(generator.nextGaussian());
    }
}