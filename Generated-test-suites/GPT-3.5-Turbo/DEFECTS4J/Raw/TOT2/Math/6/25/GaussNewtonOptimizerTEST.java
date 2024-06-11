import static org.junit.Assert.*;
import org.junit.Test;

public class GaussNewtonOptimizerTest {

    @Test
    public void testDoOptimize() {
        // Test normal optimization process
        double[] targetValues = {1.0, 2.0, 3.0};
        RealMatrix weightMatrix = new Array2DRowRealMatrix(new double[][]{{1.0, 0.0, 0.0}, {0.0, 1.0, 0.0}, {0.0, 0.0, 1.0}});
        double[] startPoint = {0.0, 0.0, 0.0};

        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer();
        optimizer.setTarget(targetValues);
        optimizer.setWeight(weightMatrix);
        optimizer.setStartPoint(startPoint);

        PointVectorValuePair result = optimizer.doOptimize();

        // Assert the result is not null
        assertNotNull(result);

        // Assert the optimized point is not null
        assertNotNull(result.getPoint());

        // Assert the cost is not NaN
        assertFalse(Double.isNaN(result.getValue()));
    }

    @Test
    public void testSingularMatrixException() {
        // Test when the problem is singular
        double[] targetValues = {1.0, 2.0, 3.0};
        RealMatrix weightMatrix = new Array2DRowRealMatrix(new double[][]{{1.0, 0.0, 0.0}, {0.0, 1.0, 0.0}, {0.0, 0.0, 1.0}});
        double[] startPoint = {1.0, 1.0, 1.0};

        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer();
        optimizer.setTarget(targetValues);
        optimizer.setWeight(weightMatrix);
        optimizer.setStartPoint(startPoint);

        // Test for SingularMatrixException
        try {
            optimizer.doOptimize();
            fail("Expected SingularMatrixException");
        } catch (ConvergenceException e) {
            assertEquals(LocalizedFormats.UNABLE_TO_SOLVE_SINGULAR_PROBLEM, e.getPattern());
        }
    }

    // Add more test cases for edge cases and potential exceptions

}