package org.example.junit.bitsstreamgenerator;

import org.apache.commons.math3.random.BitsStreamGenerator;
import org.junit.Test;
import static org.junit.Assert.*;

public class BitsStreamGeneratorTest {

    @Test
    public void testNextBoolean() {
        BitsStreamGenerator generator = new BitsStreamGenerator();
        assertTrue(generator.nextBoolean());
        assertTrue(generator.nextBoolean());
        assertTrue(generator.nextBoolean());
        assertFalse(generator.nextBoolean());
        assertFalse(generator.nextBoolean());
        assertFalse(generator.nextBoolean());
    }

    @Test
    public void testNextBytesEmptyByteArray() {
        BitsStreamGenerator generator = new BitsStreamGenerator();
        byte[] emptyByteArray = new byte[0];
        generator.nextBytes(emptyByteArray);
        assertEquals(0, emptyByteArray.length);
    }

    @Test
    public void testNextBytesByteArray() {
        BitsStreamGenerator generator = new BitsStreamGenerator();
        byte[] byteArray = new byte[10];
        generator.nextBytes(byteArray);
        // An assertion on the byte array's content would depend on the specific
        // implementation of the BitsStreamGenerator and is not possible here
        // as a general test.
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNextBytesNullByteArray() {
        BitsStreamGenerator generator = new BitsStreamGenerator();
        byte[] nullByteArray = null;
        generator.nextBytes(nullByteArray);
    }

    @Test
    public void testNextDouble() {
        BitsStreamGenerator generator = new BitsStreamGenerator();
        double result = generator.nextDouble();
        assertTrue(result > 0);
        double minDouble = Double.MIN_VALUE;
        double maxDouble = Double.MAX_VALUE;
        assertEquals(minDouble, generator.nextDouble(), 0.0001);
        assertEquals(maxDouble, generator.nextDouble(), 0.0001);
        assertEquals(minDouble, generator.nextDouble(), 0.0001);
        assertEquals(maxDouble, generator.nextDouble(), 0.0001);
    }

    @Test
    public void testNextFloat() {
        BitsStreamGenerator generator = new BitsStreamGenerator();
        float result = generator.nextFloat();
        assertTrue(result > 0);
        float minFloat = Float.MIN_VALUE;
        float maxFloat = Float.MAX_VALUE;
        assertEquals(minFloat, generator.nextFloat(), 0.0001f);
        assertEquals(maxFloat, generator.nextFloat(), 0.0001f);
        assertEquals(minFloat, generator.nextFloat(), 0.0001f);
        assertEquals(maxFloat, generator.nextFloat(), 0.0001f);
    }

    @Test
    public void testNextGaussian() {
        BitsStreamGenerator generator = new BitsStreamGenerator();
        double result = generator.nextGaussian();
        assertTrue(result > 0);
        double minGaussian = Math.E * Math.sqrt(-2 * Math.log(1.0d / 2.0d));
        double maxGaussian = Math.sqrt(2) * Math.sqrt(Math.PI);
        assertEquals(minGaussian, generator.nextGaussian(), 0.0001);
        assertEquals(maxGaussian, generator.nextGaussian(), 0.0001);
        assertEquals(minGaussian, generator.nextGaussian(), 0.0001);
        assertEquals(maxGaussian, generator.nextGaussian(), 0.0001);
    }

    @Test
    public void testNextInt() {
        BitsStreamGenerator generator = new BitsStreamGenerator();
        int result = generator.nextInt();
        assertTrue(result > 0);
        int minimumInt = Integer.MIN_VALUE;
        int maximumInt = Integer.MAX_VALUE;
        assertEquals(minimumInt, generator.nextInt(), 1);
        assertEquals(maximumInt, generator.nextInt(), 1);
        assertEquals(minimumInt, generator.nextInt(), 1);
        assertEquals(maximumInt, generator.nextInt(), 1);
        assertEquals(minimumInt, generator.nextInt(), 1);
        assertEquals(maximumInt, generator.nextInt(), 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNextIntNegative() {
        BitsStreamGenerator generator = new BitsStreamGenerator();
        int negativeNumber = -3;
        generator.nextInt(negativeNumber);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNextIntMaxInt() {
        BitsStreamGenerator generator = new BitsStreamGenerator();
        int maxInt = Integer.MAX_VALUE;
        generator.nextInt(maxInt);
    }

    @Test
    public void testNextIntZero() {
        BitsStreamGenerator generator = new BitsStreamGenerator();
        int zero = 0;
        int result = generator.nextInt(zero);
        assertEquals(zero, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNextIntNegativeArraySize() {
        BitsStreamGenerator generator = new BitsStreamGenerator();
        int negativeArraySize = -3;
        byte[] byteArray = new byte[negativeArraySize];
        generator.nextBytes(byteArray);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNextIntMaxArraySize() {
        BitsStreamGenerator generator = new BitsStreamGenerator();
        int maxArraySize = Integer.MAX_VALUE;
        byte[] byteArray = new byte[maxArraySize];
        generator.nextBytes(byteArray);
    }

    @Test
    public void testNextIntArraySize() {
        BitsStreamGenerator generator = new BitsStreamGenerator();
        int arraySize = 10;
        byte[] byteArray = new byte[arraySize];
        generator.nextBytes(byteArray);
        // An assertion on the byte array's content would depend on the specific
        // implementation of the BitsStreamGenerator and is not possible here
        // as a general test.
    }

    @Test
    public void testNextLong() {
        BitsStreamGenerator generator = new BitsStreamGenerator();
        long result = generator.nextLong();
        assertTrue(result > 0);
        long minimumLong = Long.MIN_VALUE;
        long maximumLong = Long.MAX_VALUE;
        assertEquals(minimumLong, generator.nextLong(), 1);
        assertEquals(maximumLong, generator.nextLong(), 1);
        assertEquals(minimumLong, generator.nextLong(), 1);
        assertEquals(maximumLong, generator.nextLong(), 1);
        assertEquals(minimumLong, generator.nextLong(), 1);
        assertEquals(maximumLong, generator.nextLong(), 1);
    }

    @Test
    public void testSetSeed() {
        BitsStreamGenerator generator = new BitsStreamGenerator();
        int seed = 12345;
        generator.setSeed(seed);
        int seed2 = 54321;
        generator.setSeed(seed2);
        int seed3 = 67890;
        generator.setSeed(seed3);
        int result = generator.nextInt();
        int expected = seed3;
        assertEquals(expected, result);
    }

    @Test
    public void testSetSeedArray() {
        BitsStreamGenerator generator = new BitsStreamGenerator();
        int[] seedArray = {1, 2, 3};
        generator.setSeed(seedArray);
        int[] seedArray2 = {4, 5, 6};
        generator.setSeed(seedArray2);
        int[] seedArray3 = {7, 8, 9};
        generator.setSeed(seedArray3);
        int result = generator.nextInt();
        int expected = seedArray3[0];
        assertEquals(expected, result);
    }

    @Test
    public void testSetSeedLong() {
        BitsStreamGenerator generator = new BitsStreamGenerator();
        long seed = 123456789L;
        generator.setSeed(seed);
        long seed2 = 987654321L;
        generator.setSeed(seed2);
        long seed3 = 6789012345L;
        generator.setSeed(seed3);
        long result = generator.nextLong();
        long expected = seed3;
        assertEquals(expected, result);
    }

    @Test
    public void testClear() {
        BitsStreamGenerator generator = new BitsStreamGenerator();
        double nextGaussianBeforeClear = generator.nextGaussian();
        generator.clear();
        double nextGaussianAfterClear = generator.nextGaussian();
        assertFalse(nextGaussianBeforeClear == nextGaussianAfterClear);
    }
}