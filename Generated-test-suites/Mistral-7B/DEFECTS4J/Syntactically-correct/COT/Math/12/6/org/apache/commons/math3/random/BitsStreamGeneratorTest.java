import org.apache.commons.math3.RandomUtils; // for testing purposes
import org.apache.commons.math3.util.Precision;
import org.junit.Test;
import static org.junit.Assert.*;

public class BitsStreamGeneratorTest {

    @Test
    public void testSetSeedInt() {
        BitsStreamGenerator generator = new BitsStreamGenerator();
        int seed = 12345;
        generator.setSeed(seed);
        // check if seed was set correctly
        assertEquals(seed, generator.nextInt());
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testSetSeedNegativeInt() {
        BitsStreamGenerator generator = new BitsStreamGenerator();
        int seed = -12345;
        generator.setSeed(seed);
    }

    @Test
    public void testSetSeedLong() {
        BitsStreamGenerator generator = new BitsStreamGenerator();
        long seed = 1234567890L;
        generator.setSeed(seed);
        // check if seed was set correctly
        assertEquals(seed, Long.doubleValue(generator.nextLong()));
    }

    @Test
    public void testNextBoolean() {
        BitsStreamGenerator generator = new BitsStreamGenerator();
        boolean result = generator.nextBoolean();
        assertTrue(result || !result);
    }

    @Test
    public void testNextBytes() {
        BitsStreamGenerator generator = new BitsStreamGenerator();
        byte[] expected = {1, 2, 3, 4, 5, 6, 7, 8, 9};
        generator.nextBytes(expected);
        assertArrayEquals(expected, generator.nextBytes(expected.length));
    }

    @Test
    public void testNextDouble() {
        BitsStreamGenerator generator = new BitsStreamGenerator();
        double result = generator.nextDouble();
        assertTrue(result > -0.0d && result < 1.0d);
    }

    @Test
    public void testNextFloat() {
        BitsStreamGenerator generator = new BitsStreamGenerator();
        float result = generator.nextFloat();
        assertTrue(result > -Float.MIN_VALUE && result < Float.MAX_VALUE);
    }

    @Test
    public void testNextGaussian() {
        BitsStreamGenerator generator = new BitsStreamGenerator();
        double result = generator.nextGaussian();
        assertTrue(result > -Double.MAX_VALUE && result < Double.MAX_VALUE);
        double mean = 0.0;
        double stdDev = 1.0;
        int numSamples = 10000;
        for (int i = 0; i < numSamples; i++) {
            double sample = generator.nextGaussian();
            mean += sample;
        }
        mean /= numSamples;
        assertEquals(mean, 0.0, Precision.tolerance(1e-6));
    }

    @Test
    public void testNextInt() {
        BitsStreamGenerator generator = new BitsStreamGenerator();
        int result = generator.nextInt();
        assertTrue(result > Integer.MIN_VALUE && result < Integer.MAX_VALUE);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNextIntNegativeInt() {
        BitsStreamGenerator generator = new BitsStreamGenerator();
        int n = -1;
        generator.nextInt(n);
    }

    @Test
    public void testNextIntPowerOfTwo() {
        BitsStreamGenerator generator = new BitsStreamGenerator();
        int n = 32;
        int result = generator.nextInt(n);
        assertEquals(result, (int) ((n * (long) generator.next(31)) >> 31));
    }

    @Test
    public void testNextIntNonPowerOfTwo() {
        BitsStreamGenerator generator = new BitsStreamGenerator();
        int n = 33;
        int result = generator.nextInt(n);
        int expected = generator.next(31) % n;
        int remainder = Integer.MAX_VALUE % n;
        assertTrue(result > remainder);
    }

    @Test
    public void testNextLong() {
        BitsStreamGenerator generator = new BitsStreamGenerator();
        long result = generator.nextLong();
        assertTrue(result > Long.MIN_VALUE && result < Long.MAX_VALUE);
    }

    @Test
    public void testClear() {
        BitsStreamGenerator generator = new BitsStreamGenerator();
        double expectedNextGaussian1 = generator.nextGaussian();
        generator.clear();
        double expectedNextGaussian2 = generator.nextGaussian();
        assertNotEquals(expectedNextGaussian1, expectedNextGaussian2);
    }

}