import static org.junit.Assert.*;

import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.junit.Before;
import org.junit.Test;

public class BitsStreamGeneratorTest {

    private BitsStreamGenerator generator;

    @Before
    public void setup() {
        generator = new BitsStreamGeneratorImpl();
    }

    @Test
    public void testNextBoolean() {
        boolean result = generator.nextBoolean();
        assertTrue(result || !result);
    }

    @Test
    public void testNextBytes() {
        byte[] bytes = new byte[10];
        generator.nextBytes(bytes);

        for (byte b : bytes) {
            assertEquals(0, b & 0x80);
        }
    }

    @Test
    public void testNextDouble() {
        double result = generator.nextDouble();
        assertTrue(result >= 0.0 && result < 1.0);
    }

    @Test
    public void testNextFloat() {
        float result = generator.nextFloat();
        assertTrue(result >= 0.0 && result < 1.0);
    }

    @Test
    public void testNextGaussian() {
        double result = generator.nextGaussian();
        assertTrue(result >= Double.NEGATIVE_INFINITY && result <= Double.POSITIVE_INFINITY);
    }

    @Test
    public void testNextInt() {
        int result = generator.nextInt();
        assertNotNull(result);
    }

    @Test
    public void testNextIntWithN() {
        int n = 10;
        int result = generator.nextInt(n);
        assertTrue(result >= 0 && result < n);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testNextIntWithNegativeN() {
        int n = -10;
        int result = generator.nextInt(n);
    }

    @Test
    public void testNextLong() {
        long result = generator.nextLong();
        assertNotNull(result);
    }

    @Test
    public void testClear() {
        generator.clear();
        double result = generator.nextGaussian();
        assertTrue(result >= Double.NEGATIVE_INFINITY && result <= Double.POSITIVE_INFINITY);
    }
}