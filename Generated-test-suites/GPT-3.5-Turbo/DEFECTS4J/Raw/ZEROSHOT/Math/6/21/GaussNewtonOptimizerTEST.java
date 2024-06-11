import org.junit.Test;
import static org.junit.Assert.*;

public class GaussNewtonOptimizerTest {

    @Test
    public void testDoOptimize() {
        // Create an instance of GaussNewtonOptimizer
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer();

        // Set the target values
        double[] target = {1.0, 2.0, 3.0};
        optimizer.setTarget(target);

        // Set the weight matrix
        double[][] weightMatrix = {{1.0, 0.0, 0.0}, {0.0, 1.0, 0.0}, {0.0, 0.0, 1.0}};
        optimizer.setWeight(new Array2DRowRealMatrix(weightMatrix));

        // Set the start point
        double[] startPoint = {0.0, 0.0, 0.0};
        optimizer.setStartPoint(startPoint);

        // Set the convergence checker
        optimizer.setConvergenceChecker(new SimpleVectorValueChecker());

        // Call the doOptimize() method
        PointVectorValuePair result = optimizer.doOptimize();

        // Check the result
        double[] expectedPoint = {1.0, 2.0, 3.0};
        double[] expectedValue = {0.0, 0.0, 0.0};
        assertArrayEquals(expectedPoint, result.getPoint(), 0.0001);
        assertArrayEquals(expectedValue, result.getValue(), 0.0001);
    }

    @Test(expected = NullArgumentException.class)
    public void testDoOptimizeWithNullChecker() {
        // Create an instance of GaussNewtonOptimizer
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer();

        // Set the target values
        double[] target = {1.0, 2.0, 3.0};
        optimizer.setTarget(target);

        // Set the start point
        double[] startPoint = {0.0, 0.0, 0.0};
        optimizer.setStartPoint(startPoint);

        // Call the doOptimize() method with null checker
        optimizer.doOptimize();
    }

    @Test(expected = ConvergenceException.class)
    public void testDoOptimizeWithSingularMatrixException() {
        // Create an instance of GaussNewtonOptimizer
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer();

        // Set the target values
        double[] target = {1.0, 2.0, 3.0};
        optimizer.setTarget(target);

        // Set the weight matrix
        double[][] weightMatrix = {{1.0, 0.0, 0.0}, {0.0, 1.0, 0.0}, {0.0, 0.0, 0.0}};
        optimizer.setWeight(new Array2DRowRealMatrix(weightMatrix));

        // Set the start point
        double[] startPoint = {0.0, 0.0, 0.0};
        optimizer.setStartPoint(startPoint);

        // Set the convergence checker
        optimizer.setConvergenceChecker(new SimpleVectorValueChecker());

        // Call the doOptimize() method with singular weight matrix
        optimizer.doOptimize();
    }

}