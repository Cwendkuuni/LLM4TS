import static org.junit.Assert.*;
import org.junit.Test;

public class PowellOptimizerTest {

    @Test
    public void testConstructorWithConvergenceChecker() {
        // Test constructor with convergence checker
        ConvergenceChecker<PointValuePair> checker = new PointValuePairChecker(1e-5, 1e-5);
        PowellOptimizer optimizer = new PowellOptimizer(1e-6, 1e-6, checker);
        assertNotNull(optimizer);
    }

    @Test
    public void testConstructorWithoutConvergenceChecker() {
        // Test constructor without convergence checker
        PowellOptimizer optimizer = new PowellOptimizer(1e-6, 1e-6);
        assertNotNull(optimizer);
    }

    @Test
    public void testDoOptimize() {
        // Test the doOptimize method
        PowellOptimizer optimizer = new PowellOptimizer(1e-6, 1e-6);
        optimizer.setConvergenceChecker(new PointValuePairChecker(1e-5, 1e-5));
        optimizer.setMaxEvaluations(1000);
        optimizer.optimize(10, new SinFunction(), GoalType.MINIMIZE, new double[]{0.0});
    }

    @Test
    public void testNewPointAndDirection() {
        // Test the newPointAndDirection method
        PowellOptimizer optimizer = new PowellOptimizer(1e-6, 1e-6);
        double[] p = {1.0, 2.0, 3.0};
        double[] d = {0.5, 0.5, 0.5};
        double[][] result = optimizer.newPointAndDirection(p, d, 0.5);
        assertNotNull(result);
        assertEquals(3, result.length);
        assertArrayEquals(new double[]{1.25, 2.25, 3.25}, result[0], 1e-10);
        assertArrayEquals(new double[]{0.25, 0.25, 0.25}, result[1], 1e-10);
    }

    @Test
    public void testLineSearch() {
        // Test the LineSearch method
        PowellOptimizer optimizer = new PowellOptimizer(1e-6, 1e-6);
        LineSearch lineSearch = optimizer.new LineSearch(1e-5, 1e-5);
        double[] p = {1.0, 2.0, 3.0};
        double[] d = {0.5, 0.5, 0.5};
        UnivariatePointValuePair result = lineSearch.search(p, d);
        assertNotNull(result);
    }
}