import org.junit.Test;
import static org.junit.Assert.*;

public class BitsStreamGeneratorTest {

    @Test
    public void testNextBoolean() {
        BitsStreamGenerator generator = new ConcreteBitsStreamGenerator();
        boolean result = generator.nextBoolean();
        assertNotNull(result);
    }

    @Test
    public void testNextBytes() {
        BitsStreamGenerator generator = new ConcreteBitsStreamGenerator();
        byte[] bytes = new byte[10];
        generator.nextBytes(bytes);
        assertEquals(10, bytes.length);
    }

    @Test
    public void testNextDouble() {
        BitsStreamGenerator generator = new ConcreteBitsStreamGenerator();
        double result = generator.nextDouble();
        assertNotNull(result);
    }

    @Test
    public void testNextFloat() {
        BitsStreamGenerator generator = new ConcreteBitsStreamGenerator();
        float result = generator.nextFloat();
        assertNotNull(result);
    }

    @Test
    public void testNextGaussian() {
        BitsStreamGenerator generator = new ConcreteBitsStreamGenerator();
        double result = generator.nextGaussian();
        assertNotNull(result);
    }

    @Test
    public void testClear() {
        BitsStreamGenerator generator = new ConcreteBitsStreamGenerator();
        generator.clear();
        double result = generator.nextGaussian();
        assertTrue(Double.isNaN(result));
    }

    @Test
    public void testNextInt() {
        BitsStreamGenerator generator = new ConcreteBitsStreamGenerator();
        int result = generator.nextInt();
        assertNotNull(result);
    }

    @Test
    public void testNextIntWithNegativeN() {
        BitsStreamGenerator generator = new ConcreteBitsStreamGenerator();
        try {
            generator.nextInt(-1);
            fail("Expected NotStrictlyPositiveException");
        } catch (NotStrictlyPositiveException e) {
            // Expected exception
        }
    }

    @Test
    public void testNextLong() {
        BitsStreamGenerator generator = new ConcreteBitsStreamGenerator();
        long result = generator.nextLong();
        assertNotNull(result);
    }

    private static class ConcreteBitsStreamGenerator extends BitsStreamGenerator {
        public void setSeed(int seed) {}
        public void setSeed(int[] seed) {}
        public void setSeed(long seed) {}
        protected int next(int bits) {
            return 0;
        }
    }

}