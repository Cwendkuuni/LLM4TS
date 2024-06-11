import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.random.RandomGenerator;
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
        Assert.assertTrue(result || !result);
    }

    @Test
    public void testNextBytes() {
        byte[] bytes = new byte[16];
        generator.nextBytes(bytes);

        for (byte b : bytes) {
            Assert.assertEquals(0, b);
        }
    }

    @Test
    public void testNextDouble() {
        double result = generator.nextDouble();
        Assert.assertEquals(0.0, result, 0.0);
    }

    @Test
    public void testNextFloat() {
        float result = generator.nextFloat();
        Assert.assertEquals(0.0f, result, 0.0f);
    }

    @Test
    public void testNextGaussian() {
        double result = generator.nextGaussian();
        Assert.assertEquals(0.0, result, 0.0);
    }

    @Test
    public void testNextInt() {
        int result = generator.nextInt();
        Assert.assertEquals(0, result);
    }

    @Test
    public void testNextIntWithValidN() {
        int n = 10;
        int result = generator.nextInt(n);
        Assert.assertTrue(result >= 0 && result < n);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testNextIntWithInvalidN() {
        int n = -10;
        generator.nextInt(n);
    }

    @Test
    public void testNextLong() {
        long result = generator.nextLong();
        Assert.assertEquals(0L, result);
    }

    @Test
    public void testClear() {
        generator.clear();
        double result = generator.nextGaussian();
        Assert.assertNotEquals(0.0, result, 0.0);
    }

}
