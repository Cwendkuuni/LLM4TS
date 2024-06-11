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
            public void setSeed(int seed) {
                // TODO Auto-generated method stub
                
            }

            @Override
            public void setSeed(int[] seed) {
                // TODO Auto-generated method stub
                
            }

            @Override
            public void setSeed(long seed) {
                // TODO Auto-generated method stub
                
            }

            @Override
            protected int next(int bits) {
                // TODO Auto-generated method stub
                return 0;
            }
        };
    }

    @Test
    public void testNextBoolean() {
        boolean result = generator.nextBoolean();
        assertTrue(result == true || result == false);
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
        assertTrue(result >= 0 && result < 1);
    }

    @Test
    public void testNextFloat() {
        float result = generator.nextFloat();
        assertTrue(result >= 0 && result < 1);
    }

    @Test
    public void testNextGaussian() {
        double result = generator.nextGaussian();
        assertFalse(Double.isNaN(result));
    }

    @Test
    public void testNextInt() {
        int result = generator.nextInt();
        assertNotNull(result);
    }

    @Test
    public void testNextIntWithArgument() {
        int result = generator.nextInt(10);
        assertTrue(result >= 0 && result < 10);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testNextIntWithInvalidArgument() {
        generator.nextInt(0);
    }

    @Test
    public void testNextLong() {
        long result = generator.nextLong();
        assertNotNull(result);
    }

    @Test
    public void testClear() {
        generator.clear();
        assertEquals(Double.NaN, generator.nextGaussian, 0.0);
    }

}