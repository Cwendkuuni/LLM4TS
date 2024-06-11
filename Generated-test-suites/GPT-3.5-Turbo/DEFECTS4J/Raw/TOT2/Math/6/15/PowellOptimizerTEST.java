import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.math3.optimization.direct.PowellOptimizer;
import org.apache.commons.math3.optimization.PointValuePair;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.ConvergenceChecker;

public class PowellOptimizerTest {

    @Test
    public void testConstructorWithCustomConvergenceChecker() {
        ConvergenceChecker<PointValuePair> checker = new MockConvergenceChecker();
        PowellOptimizer optimizer = new PowellOptimizer(1e-6, 1e-6, checker);
        assertNotNull(optimizer);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorWithNegativeAbsoluteThreshold() {
        new PowellOptimizer(1e-6, -1e-6);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testConstructorWithSmallRelativeThreshold() {
        new PowellOptimizer(1e-16, 1e-6);
    }

    @Test
    public void testDoOptimize() {
        PowellOptimizer optimizer = new PowellOptimizer(1e-6, 1e-6);
        optimizer.setStartPoint(new double[]{1.0, 1.0});
        optimizer.setGoalType(GoalType.MINIMIZE);
        PointValuePair result = optimizer.optimize();
        assertNotNull(result);
    }

    @Test
    public void testNewPointAndDirection() {
        PowellOptimizer optimizer = new PowellOptimizer(1e-6, 1e-6);
        double[] p = {1.0, 2.0};
        double[] d = {3.0, 4.0};
        double alpha = 2.0;
        double[][] result = optimizer.newPointAndDirection(p, d, alpha);
        assertNotNull(result);
        assertEquals(2, result.length);
    }

    @Test
    public void testLineSearch() {
        PowellOptimizer optimizer = new PowellOptimizer(1e-6, 1e-6);
        double[] p = {1.0, 2.0};
        double[] d = {3.0, 4.0};
        optimizer.setStartPoint(p);
        PointValuePair result = optimizer.line.search(p, d);
        assertNotNull(result);
    }

    // Mock Convergence Checker for testing purposes
    private class MockConvergenceChecker implements ConvergenceChecker<PointValuePair> {
        public boolean converged(int i, PointValuePair previous, PointValuePair current) {
            return false;
        }
    }

}