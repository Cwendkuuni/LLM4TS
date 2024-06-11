import org.apache.commons.math3.util.Precision;
import org.junit.Test;
import static org.junit.Assert.*;

public class BitsStreamGeneratorTest {

    @Test
    public void testSetSeedMethods() {
        BitsStreamGenerator generator = new MyBitsStreamGenerator(); // Assuming MyBitsStreamGenerator is the actual implementation of BitsStreamGenerator

        generator.setSeed(12345);
        assertEquals(12345, generator.nextInt());

        int[] seedArray = {1, 2, 3, 4};
        generator.setSeed(seedArray);
        int expectedSeed = 1680078012; // Calculate the expected seed value based on the given seed array and implementation details
        assertEquals(expectedSeed, generator.nextInt());

        long seedLong = 9876543210L;
        generator.setSeed(seedLong);
        long expectedLongSeed = 1125346247247784101L; // Calculate the expected seed value based on the given seed long and implementation details
        assertEquals(expectedLongSeed, generator.nextLong());
    }

    @Test
    public void testNextBoolean() {
        BitsStreamGenerator generator = new MyBitsStreamGenerator();

        assertTrue(generator.nextBoolean());
        assertFalse(generator.nextBoolean());
        assertTrue(generator.nextBoolean());

        generator.setSeed(1); // Seed the generator to make it predictable
        assertTrue(generator.nextBoolean());
        assertTrue(generator.nextBoolean());
        assertTrue(generator.nextBoolean());
    }

    @Test
    public void testNextBytes() {
        BitsStreamGenerator generator = new MyBitsStreamGenerator();

        byte[] result = new byte[10];
        generator.nextBytes(result);

        for (byte b : result) {
            assertTrue(b >= 0 && b <= 255);
        }

        // Test with seeded generator for deterministic output
        generator.setSeed(1);
        byte[] seededResult = new byte[10];
        generator.nextBytes(seededResult);

        for (int i = 0; i < result.length; i++) {
            assertEquals(result[i], seededResult[i]);
        }
    }

    @Test
    public void testNextDouble() {
        BitsStreamGenerator generator = new MyBitsStreamGenerator();

        double expectedMin = -1.0d;
        double expectedMax = 0.99999999999999995d; // Just below 1.0 to ensure the test covers the limit before 1.0

        for (int i = 0; i < 10000; i++) {
            double result = generator.nextDouble();
            assertTrue(Precision.isClose(expectedMin, result, 0.000000000000001));
            assertTrue(Precision.isClose(expectedMax, result, 0.000000000000001));
        }
    }

    @Test
    public void testNextFloat() {
        BitsStreamGenerator generator = new MyBitsStreamGenerator();

        float expectedMin = -3.40282347E+38f;
        float expectedMax = 3.40282346E+38f;

        for (int i = 0; i < 10000; i++) {
            float result = generator.nextFloat();
            assertTrue(Float.isFinite(result));
            assertTrue(Precision.isClose(expectedMin, result, 0.0000001f));
            assertTrue(Precision.isClose(expectedMax, result, 0.0000001f));
        }
    }

    @Test
    public void testNextGaussian() {
        BitsStreamGenerator generator = new MyBitsStreamGenerator();

        double mean = 0.0d;
        double stdDev = 1.0d;

        for (int i = 0; i < 10000; i++) {
            double result = generator.nextGaussian();
            double expected = mean + stdDev * Math.random(); // For comparison, use Java's Math.random() instead of the tested generator
            assertTrue(Precision.isClose(mean, result, 0.0001));
            assertTrue(Precision.isClose(stdDev, Math.abs(result - mean), 0.0001));
        }
    }

    @Test
    public void testNextInt() {
        BitsStreamGenerator generator = new MyBitsStreamGenerator();

        for (int i = 0; i < 10000; i++) {
            int result = generator.nextInt();
            assertTrue(result >= 0 && result <= Integer.MAX_VALUE);
        }

        // Test with seeded generator for deterministic output
        generator.setSeed(1);
        int seededResult = generator.nextInt();
        int expected = 1234; // Choose an arbitrary value for testing
        assertEquals(expected, seededResult);
    }

    @Test
    public void testNextLong() {
        BitsStreamGenerator generator = new MyBitsStreamGenerator();

        long expectedMin = Long.MIN_VALUE;
        long expectedMax = Long.MAX_VALUE - 1;

        for (int i = 0; i < 10000; i++) {
            long result = generator.nextLong();
            assertTrue(result >= expectedMin);
            assertTrue(result <= expectedMax);
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNextIntWithNegativeN() {
        BitsStreamGenerator generator = new MyBitsStreamGenerator();
        generator.nextInt(-1);
    }

    @Test
    public void testClear() {
        BitsStreamGenerator generator = new MyBitsStreamGenerator();
        double nextGaussian = generator.nextGaussian();
        generator.clear();
        assertFalse(generator.hasNextGaussian());
    }
}