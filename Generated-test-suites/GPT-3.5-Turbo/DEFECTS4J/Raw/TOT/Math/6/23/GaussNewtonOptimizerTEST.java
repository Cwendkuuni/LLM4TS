import org.junit.Test;
import static org.junit.Assert.*;

public class GaussNewtonOptimizerTest {

    @Test
    public void testDoOptimize_typicalUseCase() {
        // Create the optimizer
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer();

        // Set the target values
        double[] targetValues = {1.0, 2.0, 3.0};
        optimizer.setTarget(targetValues);

        // Set the start point
        double[] startPoint = {0.0, 0.0, 0.0};
        optimizer.setStartPoint(startPoint);

        // Set the weight matrix
        double[][] weightMatrix = {{1.0, 0.0, 0.0},
                                   {0.0, 1.0, 0.0},
                                   {0.0, 0.0, 1.0}};
        optimizer.setWeight(weightMatrix);

        // Run the optimization
        PointVectorValuePair result = optimizer.doOptimize();

        // Verify the result
        double[] expectedPoint = {0.0, 0.0, 0.0};
        assertArrayEquals(expectedPoint, result.getPoint(), 0.0001);
    }

    @Test
    public void testDoOptimize_edgeCase() {
        // Create the optimizer
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer();

        // Set the target values
        double[] targetValues = {1.0, 1.0, 1.0};
        optimizer.setTarget(targetValues);

        // Set the start point
        double[] startPoint = {0.0, 0.0, 0.0};
        optimizer.setStartPoint(startPoint);

        // Set the weight matrix
        double[][] weightMatrix = {{1.0, 0.0, 0.0},
                                   {0.0, 1.0, 0.0},
                                   {0.0, 0.0, 1.0}};
        optimizer.setWeight(weightMatrix);

        // Run the optimization
        PointVectorValuePair result = optimizer.doOptimize();

        // Verify the result
        double[] expectedPoint = {0.0, 0.0, 0.0};
        assertArrayEquals(expectedPoint, result.getPoint(), 0.0001);
    }

    @Test(expected = ConvergenceException.class)
    public void testDoOptimize_errorScenario() {
        // Create the optimizer
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer();

        // Set the target values
        double[] targetValues = {1.0, 2.0, 3.0};
        optimizer.setTarget(targetValues);

        // Set the start point
        double[] startPoint = {0.0, 0.0, 0.0};
        optimizer.setStartPoint(startPoint);

        // Set the weight matrix
        double[][] weightMatrix = {{1.0, 0.0, 0.0},
                                   {0.0, 1.0, 0.0},
                                   {0.0, 0.0, 1.0}};
        optimizer.setWeight(weightMatrix);

        // Run the optimization
        optimizer.doOptimize();
    }

}