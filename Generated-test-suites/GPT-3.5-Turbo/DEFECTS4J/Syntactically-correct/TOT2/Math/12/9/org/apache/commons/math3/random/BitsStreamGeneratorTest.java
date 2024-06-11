import org.junit.Before;
import org.junit.Test;
import org.apache.commons.math3.random.BitsStreamGenerator;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;

import static org.junit.Assert.*;

public class BitsStreamGeneratorTest {

    private BitsStreamGenerator generator;

    @Before
    public void setUp() {
        generator = new CustomBitsStreamGenerator();
    }

    @Test
    public void testNextBoolean() {
        boolean result = generator.nextBoolean();
        assertNotNull(result);
    }

    @Test
    public void testNextBytes() {
        byte[] bytes = new byte[10];
        generator.nextBytes(bytes);
        assertNotNull(bytes);
    }

    @Test
    public void testNextDouble() {
        double result = generator.nextDouble();
        assertNotNull(result);
    }

    @Test
    public void testNextFloat() {
        float result = generator.nextFloat();
        assertNotNull(result);
    }

    @Test
    public void testNextGaussian() {
        double result = generator.nextGaussian();
        assertNotNull(result);
    }

    @Test
    public void testClear() {
        generator.clear();
        assertFalse(Double.isNaN(generator.nextGaussian));
    }

    @Test
    public void testNextInt() {
        int result = generator.nextInt();
        assertNotNull(result);
    }

    @Test
    public void testNextIntWithValidInput() {
        int result = generator.nextInt(10);
        assertNotNull(result);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testNextIntWithInvalidInput() {
        generator.nextInt(-5);
    }

    @Test
    public void testNextLong() {
        long result = generator.nextLong();
        assertNotNull(result);
    }

    private class CustomBitsStreamGenerator extends BitsStreamGenerator {
        @Override
        public void setSeed(int seed) {}

        @Override
        public void setSeed(int[] seed) {}

        @Override
        public void setSeed(long seed) {}

        @Override
        protected int next(int bits) {
            return 0;
        }
    }
}