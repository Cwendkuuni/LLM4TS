package org.apache.commons.math3.random;

import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.math3.random.BitsStreamGenerator;

public class BitsStreamGeneratorTest {

    @Test
    public void testNextBoolean() {
        BitsStreamGenerator generator = new BitsStreamGenerator();
        assertTrue(generator.nextBoolean());
        assertFalse(generator.nextBoolean());
        assertTrue(generator.nextBoolean());
        // Add more assertions as needed
    }

    @Test
    public void testNextBytes() {
        BitsStreamGenerator generator = new BitsStreamGenerator();
        byte[] expected = new byte[]{1, 2, 3, 4, 5};
        byte[] actual = new byte[5];
        generator.nextBytes(actual);
        assertArrayEquals(expected, actual);
        // Add more assertions as needed
    }

    @Test
    public void testNextDouble() {
        BitsStreamGenerator generator = new BitsStreamGenerator();
        double expected = 0.5;
        double actual = generator.nextDouble();
        assertEquals(expected, actual, 0.001);
        // Add more assertions as needed
    }

    @Test
    public void testNextInt() {
        BitsStreamGenerator generator = new BitsStreamGenerator();
        int expected = 123;
        int actual = generator.nextInt();
        assertEquals(expected, actual);
        // Add more assertions as needed
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNextIntNegative() {
        BitsStreamGenerator generator = new BitsStreamGenerator();
        generator.nextInt(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNextIntZeroSize() {
        BitsStreamGenerator generator = new BitsStreamGenerator();
        generator.nextBytes(new byte[0]);
    }

    @Test
    public void testNextLong() {
        BitsStreamGenerator generator = new BitsStreamGenerator();
        long expected = 9876543210987654321L;
        long actual = generator.nextLong();
        assertEquals(expected, actual);
        // Add more assertions as needed
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNextLongNegativeSize() {
        BitsStreamGenerator generator = new BitsStreamGenerator();
        generator.nextLong(-1);
    }

    @Test
    public void testClear() {
        BitsStreamGenerator generator = new BitsStreamGenerator();
        double expected = Double.NaN;
        double actual = generator.nextGaussian();
        assertEquals(expected, actual);
        generator.clear();
        actual = generator.nextGaussian();
        assertNotNaN(actual);
        // Add more assertions as needed
    }

    @Test
    public void testSetSeed() {
        BitsStreamGenerator generator = new BitsStreamGenerator();
        long seed = 1234567890L;
        generator.setSeed(seed);
        double expected = 12345.6789;
        double actual = generator.nextDouble();
        assertEquals(expected, actual, 0.001);
        // Add more assertions as needed
    }

    private void assertNotNaN(Double value) {
        assertNotNull(value);
        assertTrue(Double.isFinite(value));
    }
}