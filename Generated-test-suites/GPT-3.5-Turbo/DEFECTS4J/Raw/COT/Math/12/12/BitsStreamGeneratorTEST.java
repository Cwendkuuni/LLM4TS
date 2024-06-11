import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class BitsStreamGeneratorTest {

    private BitsStreamGenerator generator;

    @Before
    public void setUp() {
        generator = new BitsStreamGeneratorImpl();
    }

    @After
    public void tearDown() {
        generator = null;
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
        Assert.assertNotNull(bytes);
        Assert.assertEquals(10, bytes.length);
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
        int result = generator.nextInt(100);
        Assert.assertNotNull(result);
        Assert.assertTrue(result >= 0 && result < 100);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testNextIntWithNegativeN() {
        generator.nextInt(-100);
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
        Assert.assertEquals(Double.NaN, result, 0.0);
    }
}