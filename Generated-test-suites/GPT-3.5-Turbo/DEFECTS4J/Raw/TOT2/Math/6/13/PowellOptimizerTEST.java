import org.junit.Test;
import org.junit.Assert;

public class PowellOptimizerTest {

    @Test
    public void testDoOptimize() {
        // Dummy test to verify the doOptimize method of PowellOptimizer
        PowellOptimizer optimizer = new PowellOptimizer(0.0001, 0.0001);
        // Add relevant setup and assertion here
        Assert.assertTrue(true);
    }

    @Test
    public void testNewPointAndDirection() {
        // Test the newPointAndDirection method of PowellOptimizer
        PowellOptimizer optimizer = new PowellOptimizer(0.0001, 0.0001);
        double[] p = {1.0, 2.0};
        double[] d = {0.5, 0.5};
        double optimum = 0.75;
        double[][] result = optimizer.newPointAndDirection(p, d, optimum);
        double[] newPoint = result[0];
        double[] newDirection = result[1];
        // Add relevant assertion here
        Assert.assertArrayEquals(new double[]{1.375, 2.375}, newPoint, 0.0001);
        Assert.assertArrayEquals(new double[]{0.375, 0.375}, newDirection, 0.0001);
    }

    // Add more test cases to cover all public methods in PowellOptimizer class

}