import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class BitsStreamGeneratorTest {

    private BitsStreamGenerator generator;

    @Before
    public void setUp() {
        generator = new ExampleBitsStreamGenerator();
    }

    // Test nextBoolean() method
    @Test
    public void testNextBoolean() {
        boolean result = generator.nextBoolean();
        assertTrue(result); // True case

        result = generator.nextBoolean();
        assertFalse(result); // False case
    }


    // Test nextBytes() method
    @Test
    public void testNextBytes() {
        byte[] bytes = new byte[4];
        generator.nextBytes(bytes);
        assertNotNull(bytes); // Check if bytes array is not null

        // Check if every element in the bytes array is within the valid range
        for (byte b : bytes) {
            assertTrue(b >= 0 && b <= 255);
        }
    }


    // Test nextDouble() method
    @Test
    public void testNextDouble() {
        double result = generator.nextDouble();
        assertNotNull(result); // Check if the result is not null

        // Check if the result is within the valid range
        assertTrue(result >= 0.0 && result < 1.0);
    }


    // Test nextFloat() method
    @Test
    public void testNextFloat() {
        float result = generator.nextFloat();
        assertNotNull(result); // Check if the result is not null

        // Check if the result is within the valid range
        assertTrue(result >= 0.0f && result < 1.0f);
    }


    // Test nextGaussian() method
    @Test
    public void testNextGaussian() {
        double result = generator.nextGaussian();
        assertNotNull(result); // Check if the result is not null
    }


    // Test nextInt() method
    @Test
    public void testNextInt() {
        int result = generator.nextInt();
        assertNotNull(result); // Check if the result is not null
    }


    // Test nextInt(int n) method
    @Test
    public void testNextIntWithValidN() {
        int n = 10;
        int result = generator.nextInt(n);
        assertNotNull(result); // Check if the result is not null

        // Check if the result is within the valid range
        assertTrue(result >= 0 && result < n);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testNextIntWithInvalidN() {
        int n = -10;
        generator.nextInt(n);
    }


    // Test nextLong() method
    @Test
    public void testNextLong() {
        long result = generator.nextLong();
        assertNotNull(result); // Check if the result is not null
    }


    // Test clear() method
    @Test
    public void testClear() {
        generator.clear();
        assertEquals(Double.NaN, generator.nextGaussian(), 0.0);
    }
}