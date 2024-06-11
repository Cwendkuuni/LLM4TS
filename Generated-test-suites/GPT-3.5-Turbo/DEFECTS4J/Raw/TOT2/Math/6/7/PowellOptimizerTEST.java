import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.math3.optimization.direct.PowellOptimizer;
import org.apache.commons.math3.optimization.PointValuePair;
import org.apache.commons.math3.optimization.univariate.UnivariatePointValuePair;

public class PowellOptimizerTest {

    @Test
    public void testConstructor() {
        PowellOptimizer optimizer = new PowellOptimizer(1e-6, 1e-8);
        assertNotNull(optimizer);
    }

    @Test
    public void testConstructorWithCustomChecker() {
        PowellOptimizer optimizer = new PowellOptimizer(1e-6, 1e-8, new MyConvergenceChecker());
        assertNotNull(optimizer);
    }

    @Test
    public void testDoOptimize() {
        PowellOptimizer optimizer = new PowellOptimizer(1e-6, 1e-8);
        PointValuePair result = optimizer.doOptimize();

        assertNotNull(result);
        assertEquals(2, result.getPoint().length);
        assertTrue(Double.isFinite(result.getValue()));
    }

    @Test
    public void testLineSearch() {
        PowellOptimizer.LineSearch search = new PowellOptimizer.LineSearch(1e-6, 1e-8);
        double[] p = {1.0, 2.0};
        double[] d = {0.5, -0.5};
        UnivariatePointValuePair result = search.search(p, d);

        assertNotNull(result);
        assertTrue(Double.isFinite(result.getValue()));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidConstructorArgs() {
        new PowellOptimizer(-1e-6, 1e-8);
    }

    @Test
    public void testEdgeCases() {
        // Testing edge cases here
    }

    @Test
    public void testErrorScenarios() {
        // Testing error scenarios here
    }

    private static class MyConvergenceChecker implements ConvergenceChecker<PointValuePair> {
        @Override
        public boolean converged(int iteration, PointValuePair previous, PointValuePair current) {
            return false; // Custom convergence criteria
        }
    }
}