import org.junit.Test;
import org.apache.commons.math3.optimization.general.GaussNewtonOptimizer;

import static org.junit.Assert.*;

public class GaussNewtonOptimizerTest {

    @Test
    public void testGaussNewtonOptimizer_DefaultSettings() {
        // Create an instance of the GaussNewtonOptimizer
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer();
        
        // Define the target values and start point
        double[] target = {1, 3, 5};
        double[] startPoint = {0, 0, 0};
        
        // Set the target values and start point
        optimizer.setTarget(target);
        optimizer.setStartPoint(startPoint);
        
        // Optimize
        double[] result = optimizer.optimize();
        
        // Check the result
        assertArrayEquals(new double[]{1, 3, 5}, result, 0.001);
    }

    @Test
    public void testGaussNewtonOptimizer_ConvergenceChecker() {
        // Create an instance of the GaussNewtonOptimizer
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(new SimpleVectorValueChecker());
        
        // Define the target values and start point
        double[] target = {1, 3, 5};
        double[] startPoint = {0, 0, 0};
        
        // Set the target values and start point
        optimizer.setTarget(target);
        optimizer.setStartPoint(startPoint);
        
        // Optimize
        double[] result = optimizer.optimize();
        
        // Check the result
        assertArrayEquals(new double[]{1, 3, 5}, result, 0.001);
    }

    @Test
    public void testGaussNewtonOptimizer_UseLU() {
        // Create an instance of the GaussNewtonOptimizer
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(true);
        
        // Define the target values and start point
        double[] target = {1, 3, 5};
        double[] startPoint = {0, 0, 0};
        
        // Set the target values and start point
        optimizer.setTarget(target);
        optimizer.setStartPoint(startPoint);
        
        // Optimize
        double[] result = optimizer.optimize();
        
        // Check the result
        assertArrayEquals(new double[]{1, 3, 5}, result, 0.001);
    }

    @Test(expected = ConvergenceException.class)
    public void testGaussNewtonOptimizer_SingularMatrixException() {
        // Create an instance of the GaussNewtonOptimizer
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(true);
        
        // Define the target values and start point
        double[] target = {1, 3, 5};
        double[] startPoint = {0, 0, 0};
        
        // Set the target values and start point
        optimizer.setTarget(target);
        optimizer.setStartPoint(startPoint);
        
        // Optimize
        double[] result = optimizer.optimize();
    }

    @Test(expected = NullArgumentException.class)
    public void testGaussNewtonOptimizer_NullArgumentException() {
        // Create an instance of the GaussNewtonOptimizer
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(true);
        
        // Define the target values and start point
        double[] target = {1, 3, 5};
        double[] startPoint = null;
        
        // Set the target values and start point
        optimizer.setTarget(target);
        optimizer.setStartPoint(startPoint);
        
        // Optimize
        double[] result = optimizer.optimize();
    }

}