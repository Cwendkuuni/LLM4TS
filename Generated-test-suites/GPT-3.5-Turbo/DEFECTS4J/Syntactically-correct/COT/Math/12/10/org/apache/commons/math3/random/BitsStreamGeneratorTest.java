import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class BitsStreamGeneratorTest {

    private BitsStreamGenerator generator;

    @Before
    public void setup() {
        generator = new BitsStreamGenerator() {
            @Override
            public void setSeed(int seed) {
                // Implementation not needed for testing
            }

            @Override
            public void setSeed(int[] seed) {
                // Implementation not needed for testing
            }

            @Override
            public void setSeed(long seed) {
                // Implementation not needed for testing
            }

            @Override
            protected int next(int bits) {
                // Implementation not needed for testing
                return 0;
            }
        };
    }

    @Test
    public void testNextBoolean() {
        boolean result = generator.nextBoolean();
        Assert.assertNotNull(result);
    }

    @Test
    public void testNextBytes() {
        byte[] bytes = new byte[10];
        generator.nextBytes(bytes);
        Assert.assertArrayEquals(new byte[10], bytes);
    }

    @Test
    public void testNextDouble() {
        double result = generator.nextDouble();
        Assert.assertNotNull(result);
    }

    @Test
    public void testNextFloat() {
        float result = generator.nextFloat();
        Assert.assertNotNull(result);
    }

    @Test
    public void testNextGaussian() {
        double result = generator.nextGaussian();
        Assert.assertNotNull(result);
    }

    @Test
    public void testNextInt() {
        int result = generator.nextInt();
        Assert.assertNotNull(result);
    }

    @Test
    public void testNextIntWithPositiveN() {
        int result = generator.nextInt(10);
        Assert.assertNotNull(result);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testNextIntWithNegativeN() {
        generator.nextInt(-10);
    }

    @Test
    public void testNextLong() {
        long result = generator.nextLong();
        Assert.assertNotNull(result);
    }

    @Test
    public void testClear() {
        generator.clear();
        double result = generator.nextGaussian();
        Assert.assertNotNull(result);
    }
}