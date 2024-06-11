import org.apache.commons.math3.random.BitsStreamGenerator;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class BitsStreamGeneratorTest {
    
    private BitsStreamGenerator generator;
    
    @Before
    public void setUp() {
        generator = new BitsStreamGeneratorImpl();
    }
    
    @Test
    public void testNextBoolean() {
        boolean result = generator.nextBoolean();
        assertEquals("Result should be boolean", true, result);
    }
    
    @Test
    public void testNextBytes() {
        byte[] bytes = new byte[10];
        generator.nextBytes(bytes);
        assertEquals("Length of bytes should be equal to 10", 10, bytes.length);
    }
    
    @Test
    public void testNextDouble() {
        double result = generator.nextDouble();
        assertEquals("Result should be of type double", true, result instanceof Double);
    }
    
    @Test
    public void testNextFloat() {
        float result = generator.nextFloat();
        assertEquals("Result should be of type float", true, result instanceof Float);
    }
    
    @Test
    public void testNextGaussian() {
        double result = generator.nextGaussian();
        assertEquals("Result should be of type double", true, result instanceof Double);
    }
    
    @Test
    public void testNextInt() {
        int result = generator.nextInt();
        assertEquals("Result should be of type int", true, result instanceof Integer);
    }
    
    @Test
    public void testNextIntWithPositiveN() {
        int result = generator.nextInt(10);
        assertEquals("Result should be less than or equal to 10", true, result <= 10);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testNextIntWithZeroN() {
        generator.nextInt(0);
    }
    
    @Test
    public void testNextLong() {
        long result = generator.nextLong();
        assertEquals("Result should be of type long", true, result instanceof Long);
    }
}

class BitsStreamGeneratorImpl extends BitsStreamGenerator {
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
}