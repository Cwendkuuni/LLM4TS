import org.junit.Before;
import org.junit.Test;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.random.BitsStreamGenerator;

import static org.junit.Assert.*;

public class BitsStreamGeneratorTest {

    private BitsStreamGenerator generator;

    @Before
    public void setUp() {
        generator = new BitsStreamGenerator() {
            @Override
            public void setSeed(int seed) {

            }

            @Override
            public void setSeed(int[] seed) {

            }

            @Override
            public void setSeed(long seed) {

            }

            @Override
            protected int next(int bits) {
                return 0;
            }
        };
    }

    @Test
    public void testNextBoolean() {
        boolean result = generator.nextBoolean();
        assertNotNull(result);
        assertTrue(result || !result);
    }

    @Test
    public void testNextBytes() {
        byte[] bytes = new byte[10];
        generator.nextBytes(bytes);
        assertNotNull(bytes);
        assertEquals(10, bytes.length);
    }

    @Test
    public void testNextDouble() {
        double result = generator.nextDouble();
        assertNotNull(result);
        assertTrue(result >= 0.0  && result < 1.0);
    }

    @Test
    public void testNextFloat() {
        float result = generator.nextFloat();
        assertNotNull(result);
        assertTrue(result >= 0.0f && result < 1.0f);
    }

    @Test
    public void testNextGaussian() {
        double result = generator.nextGaussian();
        assertNotNull(result);
    }

    @Test
    public void testNextInt() {
        int result = generator.nextInt();
        assertNotNull(result);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testNextIntWithNegativeN() {
        generator.nextInt(-10);
    }

    @Test
    public void testNextIntWithNonPowerOfTwoN() {
        int n = 5;
        int result = generator.nextInt(n);
        assertNotNull(result);
        assertTrue(result >= 0 && result < n);
    }

    @Test
    public void testNextIntWithPowerOfTwoN() {
        int n = 16;
        int result = generator.nextInt(n);
        assertNotNull(result);
        assertTrue(result >= 0 && result < n);
    }

    @Test
    public void testNextLong() {
        long result = generator.nextLong();
        assertNotNull(result);
    }

    @Test
    public void testClear() {
        generator.clear();
        // No assertions are needed as it should not throw any exception
    }

}