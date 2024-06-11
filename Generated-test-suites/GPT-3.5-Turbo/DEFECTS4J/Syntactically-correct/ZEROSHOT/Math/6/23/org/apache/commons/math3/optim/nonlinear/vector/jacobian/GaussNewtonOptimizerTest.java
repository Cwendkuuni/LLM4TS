import org.junit.Assert;
import org.junit.Test;

public class GaussNewtonOptimizerTest {
    
    @Test
    public void testDoOptimize() {
        // Create an instance of GaussNewtonOptimizer
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer();
        
        // Set the target values
        double[] target = {1.0, 2.0, 3.0};
        optimizer.setTarget(target);
        
        // Set the weight matrix
        double[][] weight = {
            {1.0, 0.0, 0.0},
            {0.0, 1.0, 0.0},
            {0.0, 0.0, 1.0}
        };
        optimizer.setWeight(new Array2DRowRealMatrix(weight));
        
        // Set the start point
        double[] startPoint = {0.0, 0.0, 0.0};
        optimizer.setStartPoint(startPoint);
        
        // Set the convergence checker
        optimizer.setConvergenceChecker(new SimpleVectorValueChecker());
        
        // Call the doOptimize method
        PointVectorValuePair result = optimizer.doOptimize();
        
        // Assert the result values
        double[] expectedPoint = {1.0, 2.0, 3.0};
        double[] expectedObjective = {1.0, 2.0, 3.0};
        Assert.assertArrayEquals(expectedPoint, result.getPoint(), 0.00001);
        Assert.assertArrayEquals(expectedObjective, result.getValue(), 0.00001);
    }
    
}