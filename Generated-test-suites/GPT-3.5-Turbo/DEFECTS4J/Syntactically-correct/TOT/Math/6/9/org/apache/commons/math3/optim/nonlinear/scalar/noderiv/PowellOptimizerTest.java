import org.junit.Test;
import org.apache.commons.math3.optimization.direct.PowellOptimizer;
import org.apache.commons.math3.optimization.PointValuePair;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class PowellOptimizerTest {

    @Test
    public void testOptimization() {
        PowellOptimizer optimizer = new PowellOptimizer(1e-10, 1e-10);

        double[] startPoint = {1, 1};
        double[] expectedPoint = {0, 0};

        PointValuePair result = optimizer.optimize(100, new TestFunction(), startPoint);

        assertEquals(expectedPoint[0], result.getPoint()[0], 1e-10);
        assertEquals(expectedPoint[1], result.getPoint()[1], 1e-10);
        assertEquals(0, result.getValue(), 1e-10);
    }

    @Test
    public void testMaxIterationsReached() {
        PowellOptimizer optimizer = new PowellOptimizer(1e-10, 1e-10);

        double[] startPoint = {10, 10};
        double[] expectedPoint = {10, 10};

        PointValuePair result = optimizer.optimize(10, new TestFunction(), startPoint);

        assertEquals(expectedPoint[0], result.getPoint()[0], 1e-10);
        assertEquals(expectedPoint[1], result.getPoint()[1], 1e-10);
        assertTrue(Double.isNaN(result.getValue()));
    }

    @Test
    public void testConvergenceChecker() {
        PowellOptimizer optimizer = new PowellOptimizer(1e-10, 1e-10, new TestConvergenceChecker());

        double[] startPoint = {1, 1};
        double[] expectedPoint = {0, 0};

        PointValuePair result = optimizer.optimize(100, new TestFunction(), startPoint);

        assertEquals(expectedPoint[0], result.getPoint()[0], 1e-10);
        assertEquals(expectedPoint[1], result.getPoint()[1], 1e-10);
        assertEquals(0, result.getValue(), 1e-10);
    }

    private static class TestFunction implements org.apache.commons.math3.analysis.MultivariateFunction {

        public double value(double[] point) {
            return point[0] * point[0] + point[1] * point[1];
        }
    }
    
    private static class TestConvergenceChecker implements org.apache.commons.math3.optimization.ConvergenceChecker<org.apache.commons.math3.optimization.PointValuePair> {

        public boolean converged(int iteration, PointValuePair previous, PointValuePair current) {
            double tolerance = 1e-10;
            return Math.abs(current.getValue() - previous.getValue()) < tolerance;
        }
    }
}