import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.math3.optimization.direct.PowellOptimizer;
import org.apache.commons.math3.optimization.PointValuePair;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;

public class PowellOptimizerTest {

    @Test
    public void testConstructor1() {
        PowellOptimizer optimizer = new PowellOptimizer(0.001, 0.01);
        assertNotNull(optimizer);
    }

    @Test
    public void testConstructor2() {
        PowellOptimizer optimizer = new PowellOptimizer(0.001, 0.01, new TestConvergenceChecker());
        assertNotNull(optimizer);
    }

    @Test
    public void testConstructor3() {
        try {
            PowellOptimizer optimizer = new PowellOptimizer(0.001, 0.01, -0.1, 0.5);
            fail("Expected NotStrictlyPositiveException");
        } catch (NotStrictlyPositiveException e) {
            assertTrue(true);
        }
    }

    @Test
    public void testConstructor4() {
        try {
            PowellOptimizer optimizer = new PowellOptimizer(-0.1, 0.5);
            fail("Expected NumberIsTooSmallException");
        } catch (NumberIsTooSmallException e) {
            assertTrue(true);
        }
    }

    @Test
    public void testDoOptimize() {
        PowellOptimizer optimizer = new PowellOptimizer(0.001, 0.01);
        optimizer.setStartPoint(new double[]{0.0, 0.0});
        PointValuePair result = optimizer.optimize(GoalType.MINIMIZE, new TestMultivariateFunction());
        assertNotNull(result);
    }

    // Custom Convergence Checker for testing
    private class TestConvergenceChecker implements ConvergenceChecker<PointValuePair> {
        public boolean converged(int iterations, PointValuePair previous, PointValuePair current) {
            return iterations > 10;
        }
    }

    // Custom Multivariate Function for testing
    private class TestMultivariateFunction implements MultivariateFunction {
        public double value(double[] point) {
            return Math.pow(point[0], 2) + Math.pow(point[1], 2);
        }
    }
}