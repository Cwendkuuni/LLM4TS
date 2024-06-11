import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleVectorValueChecker;
import org.apache.commons.math3.optim.UnconstrainedOptimizationTest;
import org.apache.commons.math3.optimization.leastsquares.GaussNewtonOptimizer;
import org.apache.commons.math3.optimization.linear.GoalType;
import org.apache.commons.math3.optimization.linear.LinearConstraint;
import org.apache.commons.math3.optimization.linear.LinearObjectiveFunction;
import org.apache.commons.math3.optimization.linear.LinearConstraints;
import org.apache.commons.math3.optimization.linear.LinearObjectiveFunctionTest;
import org.apache.commons.math3.optimization.linear.LinearConstraintTest;
import org.junit.Test;
import static org.junit.Assert.*;

public class GaussNewtonOptimizerTest {

    @Test
    public void testGaussNewtonOptimizer() {
        // Test with default settings
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer();
        LinearObjectiveFunction function = new LinearObjectiveFunctionTest.DummyFunction();
        PointValuePair solution = optimizer.optimize(function);
        assertNotNull("Solution should not be null", solution);
        assertEquals("Cost should be zero", 0.0, solution.getValue(), 1e-10);

        // Test with custom convergence checker
        SimpleVectorValueChecker checker = new SimpleVectorValueChecker(1e-6);
        GaussNewtonOptimizer optimizerWithChecker = new GaussNewtonOptimizer(checker);
        LinearObjectiveFunction functionWithConstraints = new LinearObjectiveFunctionTest.DummyFunctionWithConstraints();
        LinearConstraints constraints = new LinearConstraints(new LinearConstraint[] {
                new LinearConstraint(new double[] { 0.5 }, 1.0, GoalType.EQUALS, 1.0),
                new LinearConstraint(new double[] { 1.5 }, 1.0, GoalType.LEQS, 0.5)
        });
        PointValuePair solutionWithChecker = optimizerWithChecker.optimize(functionWithConstraints, constraints);
        assertNotNull("Solution should not be null", solutionWithChecker);
        assertEquals("Cost should be zero", 0.0, solutionWithChecker.getValue(), 1e-10);
        assertArrayEquals("Solution should match the expected value", new double[] { 0.5, 0.5 }, solutionWithChecker.getPoint(), 1e-6);

        // Test with LU decomposition
        GaussNewtonOptimizer optimizerLU = new GaussNewtonOptimizer(true);
        PointValuePair solutionLU = optimizerLU.optimize(function);
        assertNotNull("Solution should not be null", solutionLU);
        assertEquals("Cost should be zero", 0.0, solutionLU.getValue(), 1e-10);

        // Test with QR decomposition
        GaussNewtonOptimizer optimizerQR = new GaussNewtonOptimizer(false);
        PointValuePair solutionQR = optimizerQR.optimize(function);
        assertNotNull("Solution should not be null", solutionQR);
        assertEquals("Cost should be zero", 0.0, solutionQR.getValue(), 1e-10);

        // Test with null function
        GaussNewtonOptimizer optimizerNullFunction = new GaussNewtonOptimizer();
        try {
            optimizerNullFunction.optimize(null);
            fail("NullPointerException expected");
        } catch (NullArgumentException e) {
            // Expected
        }

        // Test with null start point
        GaussNewtonOptimizer optimizerNullStartPoint = new GaussNewtonOptimizer();
        try {
            optimizerNullStartPoint.optimize(function, null);
            fail("NullPointerException expected");
        } catch (NullArgumentException e) {
            // Expected
        }

        // Test with null weight matrix
        GaussNewtonOptimizer optimizerNullWeightMatrix = new GaussNewtonOptimizer();
        try {
            optimizerNullWeightMatrix.optimize(function, null, null);
            fail("NullPointerException expected");
        } catch (NullArgumentException e) {
            // Expected
        }

        // Test with non-square weight matrix
        GaussNewtonOptimizer optimizerNonSquareWeightMatrix = new GaussNewtonOptimizer();
        try {
            optimizerNonSquareWeightMatrix.optimize(function, new double[][]{{1.0, 0.0}, {0.0, 1.0}, {0.0, 1.0}});
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }
}