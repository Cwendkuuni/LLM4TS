import org.apache.commons.math3.random.BitsStreamGenerator;
import org.junit.Test;
import static org.junit.Assert.*;

public class BitsStreamGeneratorTest {

    @Test
    public void testNextBoolean() {
        // Test code for nextBoolean() method goes here
    }

    @Test
    public void testNextBytes() {
        // Test code for nextBytes(byte[]) method goes here
    }

    @Test
    public void testNextDouble() {
        // Test code for nextDouble() method goes here
    }

    @Test
    public void testNextFloat() {
        // Test code for nextFloat() method goes here
    }

    @Test
    public void testNextGaussian() {
        // Test code for nextGaussian() method goes here
    }

    @Test
    public void testNextInt() {
        // Test code for nextInt() method goes here
    }

    @Test
    public void testNextLong() {
        // Test code for nextLong() method goes here
    }

    @Test
    public void testClear() {
        BitsStreamGenerator generator = new BitsStreamGenerator();
        double[] gaussians = new double[10];
        for (int i = 0; i < gaussians.length; i++) {
            gaussians[i] = generator.nextGaussian();
        }
        generator.clear();
        for (int i = 0; i < gaussians.length; i++) {
            assertEquals(Double.NaN, generator.nextGaussian());
        }
    }
}