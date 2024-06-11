import org.apache.commons.math3.RandomUtils; // for testing purpose, as BitsStreamGenerator is an abstract class and cannot be instantiated
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.junit.Test;
import static org.junit.Assert.*;

public class BitsStreamGeneratorTest {

    @Test
    public void testSetSeedInt() {
        BitsStreamGenerator generator = new MersenneTwister(); // assuming MersenneTwister is a concrete class extending BitsStreamGenerator
        generator.setSeed(12345);
        long expectedSeed = 12345;
        long actualSeed = generator.nextLong();
        long seedAfterNextNumber = generator.nextLong();
        assertEquals(expectedSeed, actualSeed);
        assertNotEquals(expectedSeed, seedAfterNextNumber);
    }

    @Test
    public void testSetSeedIntArray() {
        BitsStreamGenerator generator = new MersenneTwister();
        int[] seeds = {1, 2, 3, 4, 5};
        generator.setSeed(seeds);
        long[] expectedSeeds = {1, 2, 3, 4, 5};
        long[] actualSeeds = new long[5];
        for (int i = 0; i < 5; i++) {
            actualSeeds[i] = generator.nextLong();
        }
        assertArrayEquals(expectedSeeds, actualSeeds);
    }

    @Test
    public void testSetSeedLong() {
        BitsStreamGenerator generator = new MersenneTwister();
        long seed = 1234567890123L;
        generator.setSeed(seed);
        long expectedSeed = seed;
        long actualSeed = generator.nextLong();
        long seedAfterNextNumber = generator.nextLong();
        assertEquals(expectedSeed, actualSeed);
        assertNotEquals(expectedSeed, seedAfterNextNumber);
    }

    @Test
    public void testNextBoolean() {
        BitsStreamGenerator generator = new MersenneTwister();
        boolean expectedResult = true;
        boolean actualResult = generator.nextBoolean();
        assertTrue(expectedResult == actualResult || !expectedResult && actualResult);
    }

    @Test
    public void testNextBytesByteArray() {
        BitsStreamGenerator generator = new MersenneTwister();
        byte[] expectedBytes = {1, 2, 3, 4, 5};
        byte[] actualBytes = new byte[5];
        generator.nextBytes(actualBytes);
        assertArrayEquals(expectedBytes, actualBytes);
    }

    @Test
    public void testNextDouble() {
        BitsStreamGenerator generator = new MersenneTwister();
        double expectedResult = 0.4123;
        double actualResult = generator.nextDouble();
        double tolerance = 0.0001;
        assertEquals(expectedResult, actualResult, tolerance);
    }

    @Test
    public void testNextFloat() {
        BitsStreamGenerator generator = new MersenneTwister();
        float expectedResult = 0.41f;
        float actualResult = generator.nextFloat();
        float tolerance = 0.001f;
        assertEquals(expectedResult, actualResult, tolerance);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testNextGaussianNegativeSeed() {
        BitsStreamGenerator generator = new MersenneTwister();
        generator.clear();
        generator.setSeed(-1);
        generator.nextGaussian();
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testNextGaussianZeroSeed() {
        BitsStreamGenerator generator = new MersenneTwister();
        generator.clear();
        generator.setSeed(0);
        generator.nextGaussian();
    }

    @Test
    public void testNextInt() {
        BitsStreamGenerator generator = new MersenneTwister();
        int expectedResult = 123;
        int actualResult = generator.nextInt();
        int tolerance = 1;
        assertEquals(expectedResult, actualResult, tolerance);
    }

    @Test
    public void testNextIntWithLimit() {
        BitsStreamGenerator generator = new MersenneTwister();
        int limit = 10;
        for (int i = 0; i < 10; i++) {
            int actualResult = generator.nextInt(limit);
            assertTrue(actualResult >= 0 && actualResult < limit);
        }
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testNextIntNegativeLimit() {
        BitsStreamGenerator generator = new MersenneTwister();
        int limit = -1;
        generator.nextInt(limit);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testNextIntZeroLimit() {
        BitsStreamGenerator generator = new MersenneTwister();
        int limit = 0;
        generator.nextInt(limit);
    }

    @Test
    public void testNextLong() {
        BitsStreamGenerator generator = new MersenneTwister();
        long expectedResult = 1234567890123L;
        long actualResult = generator.nextLong();
        long tolerance = 1;
        assertEquals(expectedResult, actualResult, tolerance);
    }

    @Test
    public void testClear() {
        BitsStreamGenerator generator = new MersenneTwister();
        double nextGaussianBeforeClear = generator.nextGaussian();
        generator.clear();
        double nextGaussianAfterClear = generator.nextGaussian();
        assertNotSame(nextGaussianBeforeClear, nextGaussianAfterClear);
    }
}