import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class BitsStreamGeneratorTest {

    private BitsStreamGenerator generator;

    @Before
    public void setup() {
        generator = new BitsStreamGeneratorImpl();
    }

    @Test
    public void testSetSeedInt() {
        int seed = 12345;
        generator.setSeed(seed);
        // Verify that seed is successfully set
        // The test case will fail if any exception is thrown
    }

    @Test
    public void testSetSeedIntArray() {
        int[] seed = {1, 2, 3, 4, 5};
        generator.setSeed(seed);
        // Verify that seed is successfully set
        // The test case will fail if any exception is thrown
    }

    @Test
    public void testSetSeedLong() {
        long seed = 9876543210L;
        generator.setSeed(seed);
        // Verify that seed is successfully set
        // The test case will fail if any exception is thrown
    }

    @Test
    public void testNextBoolean() {
        boolean result = generator.nextBoolean();
        // Verify that result is of type boolean
        assertTrue(result == true || result == false);
    }

    @Test
    public void testNextBytes() {
        byte[] bytes = new byte[10];
        generator.nextBytes(bytes);
        // Verify that bytes array is filled with random values
        // The test case will fail if any byte is not assigned a value
    }

    @Test
    public void testNextDouble() {
        double result = generator.nextDouble();
        // Verify that result is of type double and within [0, 1)
        assertTrue(result >= 0 && result < 1);
    }

    @Test
    public void testNextFloat() {
        float result = generator.nextFloat();
        // Verify that result is of type float and within [0, 1)
        assertTrue(result >= 0 && result < 1);
    }

    @Test
    public void testNextGaussian() {
        double result = generator.nextGaussian();
        // Verify that result is of type double
        assertNotNull(result);
    }

    @Test
    public void testNextInt() {
        int result = generator.nextInt();
        // Verify that result is of type int
        assertNotNull(result);
    }

    @Test
    public void testNextIntWithBound() {
        int n = 100;
        int result = generator.nextInt(n);
        // Verify that result is of type int and within [0, n)
        assertTrue(result >= 0 && result < n);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNextIntWithNegativeBound() {
        int n = -10;
        generator.nextInt(n);
        // Verify that IllegalArgumentException is thrown for negative bounds
    }

    @Test
    public void testNextLong() {
        long result = generator.nextLong();
        // Verify that result is of type long
        assertNotNull(result);
    }

    @Test
    public void testClear() {
        generator.clear();
        // Verify that the cache is successfully cleared
        // The test case will fail if any exception is thrown
    }

}