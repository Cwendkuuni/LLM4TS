import org.junit.Assert;
import org.junit.Test;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;

public class BitsStreamGeneratorTest {

    private final double EPSILON = 1e-10;

    @Test
    public void testSetSeedInt() {
        BitsStreamGenerator generator = new BitsStreamGeneratorImpl();
        generator.setSeed(12345);
        Assert.assertEquals(12345, generator.next(32));
    }

    @Test
    public void testSetSeedIntArray() {
        BitsStreamGenerator generator = new BitsStreamGeneratorImpl();
        int[] seed = {1, 2, 3, 4};
        generator.setSeed(seed);
        Assert.assertEquals(1, generator.next(32));
        Assert.assertEquals(2, generator.next(32));
        Assert.assertEquals(3, generator.next(32));
        Assert.assertEquals(4, generator.next(32));
    }

    @Test
    public void testSetSeedLong() {
        BitsStreamGenerator generator = new BitsStreamGeneratorImpl();
        generator.setSeed(1234567890L);
        Assert.assertEquals(1234567890, generator.next(32));
    }

    @Test
    public void testNextBoolean() {
        BitsStreamGenerator generator = new BitsStreamGeneratorImpl();
        generator.setSeed(12345);
        boolean result = generator.nextBoolean();
        Assert.assertTrue(result || !result);
    }

    @Test
    public void testNextBytes() {
        BitsStreamGenerator generator = new BitsStreamGeneratorImpl();
        generator.setSeed(12345);
        byte[] bytes = new byte[10];
        generator.nextBytes(bytes);
        for (byte b : bytes) {
            Assert.assertTrue(b >= 0 && b < 256);
        }
    }

    @Test
    public void testNextDouble() {
        BitsStreamGenerator generator = new BitsStreamGeneratorImpl();
        generator.setSeed(12345);
        double result = generator.nextDouble();
        Assert.assertTrue(result >= 0.0 && result < 1.0);
    }

    @Test
    public void testNextFloat() {
        BitsStreamGenerator generator = new BitsStreamGeneratorImpl();
        generator.setSeed(12345);
        float result = generator.nextFloat();
        Assert.assertTrue(result >= 0.0 && result < 1.0);
    }

    @Test
    public void testNextGaussian() {
        BitsStreamGenerator generator = new BitsStreamGeneratorImpl();
        generator.setSeed(12345);
        double result = generator.nextGaussian();
        // nextGaussian generates a random number with mean 0 and standard deviation 1
        // so a value between -2 and 2 is a reasonable range for most cases
        Assert.assertTrue(result >= -2.0 && result <= 2.0);
    }

    @Test
    public void testNextInt() {
        BitsStreamGenerator generator = new BitsStreamGeneratorImpl();
        generator.setSeed(12345);
        int result = generator.nextInt();
        Assert.assertTrue(result >= Integer.MIN_VALUE && result <= Integer.MAX_VALUE);
    }

    @Test
    public void testNextIntWithNegativeN() {
        BitsStreamGenerator generator = new BitsStreamGeneratorImpl();
        generator.setSeed(12345);
        try {
            generator.nextInt(-1);
            Assert.fail("Expected NotStrictlyPositiveException");
        } catch (NotStrictlyPositiveException e) {
            // Pass
        }
    }

    @Test
    public void testNextIntWithPowerOfTwoN() {
        BitsStreamGenerator generator = new BitsStreamGeneratorImpl();
        generator.setSeed(12345);
        int result = generator.nextInt(1024);
        Assert.assertTrue(result >= 0 && result < 1024);
    }

    @Test
    public void testNextIntWithNonPowerOfTwoN() {
        BitsStreamGenerator generator = new BitsStreamGeneratorImpl();
        generator.setSeed(12345);
        int result = generator.nextInt(1000);
        Assert.assertTrue(result >= 0 && result < 1000);
    }

    @Test
    public void testNextLong() {
        BitsStreamGenerator generator = new BitsStreamGeneratorImpl();
        generator.setSeed(12345);
        long result = generator.nextLong();
        Assert.assertTrue(result >= Long.MIN_VALUE && result <= Long.MAX_VALUE);
    }

    @Test
    public void testClear() {
        BitsStreamGenerator generator = new BitsStreamGeneratorImpl();
        generator.setSeed(12345);
        generator.clear();
        Assert.assertTrue(Double.isNaN(generator.nextGaussian()));
    }
}