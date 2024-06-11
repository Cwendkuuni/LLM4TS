import org.apache.commons.math3.analysis.MultivariateFunction;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.optimization.ConvergenceChecker;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.PointValuePair;
import org.apache.commons.math3.optimization.direct.PowellOptimizer;
import org.junit.Test;
import static org.junit.Assert.*;

public class PowellOptimizerTest {

    @Test
    public void testDoOptimize() {

        // Create the test function
        MultivariateFunction function = new MultivariateFunction() {
            public double value(double[] point) {
                return Math.pow(point[0], 2) + Math.pow(point[1], 2);
            }
        };

        // Create the convergence checker
        ConvergenceChecker<PointValuePair> checker = new ConvergenceChecker<PointValuePair>() {
            public boolean converged(int iteration, PointValuePair previous, PointValuePair current) {
                return Math.abs(current.getValue() - previous.getValue()) < 0.001;
            }
        };

        // Create the optimizer
        PowellOptimizer optimizer = new PowellOptimizer(0.0001, 0.0001, checker);
        optimizer.setConvergenceChecker(checker);
        optimizer.setGoalType(GoalType.MINIMIZE);
        optimizer.setMaxIterations(1000);
        optimizer.setStartPoint(new double[] {1, 1});

        // Perform the optimization
        PointValuePair result = optimizer.optimize(function);

        // Verify the result
        assertEquals(0.0, result.getValue(), 0.001);
        assertArrayEquals(new double[] {0.0, 0.0}, result.getPoint(), 0.001);

    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testConstructorWithInvalidRelativeThreshold() {
        ConvergenceChecker<PointValuePair> checker = null;
        new PowellOptimizer(0.000000001, 0.0001, checker);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorWithInvalidAbsoluteThreshold() {
        ConvergenceChecker<PointValuePair> checker = null;
        new PowellOptimizer(0.0001, -0.000000001, checker);
    }

    @Test
    public void testConstructorWithValidArguments() {
        ConvergenceChecker<PointValuePair> checker = null;
        PowellOptimizer optimizer = new PowellOptimizer(0.0001, 0.0001, checker);
        assertNotNull(optimizer);
    }

}