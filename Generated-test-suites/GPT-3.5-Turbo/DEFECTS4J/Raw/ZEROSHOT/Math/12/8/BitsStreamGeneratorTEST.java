import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.util.FastMath;

public class BitsStreamGeneratorTest {

    private BitsStreamGenerator generator;

    @Before
    public void setUp() {
        generator = new BitsStreamGenerator() {
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
        };
    }

    @Test
    public void testNextBoolean() {
        boolean result = generator.nextBoolean();
        assertNotNull(result);
    }

    @Test
    public void testNextBytes() {
        byte[] bytes = new byte[100];
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
    public void testNextInt() {
        int result = generator.nextInt();
        assertNotNull(result);
    }

    @Test
    public void testNextIntWithPositiveArgument() {
        int result = generator.nextInt(10);
        assertNotNull(result);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testNextIntWithZeroArgument() {
        generator.nextInt(0);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testNextIntWithNegativeArgument() {
        generator.nextInt(-10);
    }

    @Test
    public void testNextLong() {
        long result = generator.nextLong();
        assertNotNull(result);
    }

    @Test
    public void testClear() {
        generator.clear();
    }
}