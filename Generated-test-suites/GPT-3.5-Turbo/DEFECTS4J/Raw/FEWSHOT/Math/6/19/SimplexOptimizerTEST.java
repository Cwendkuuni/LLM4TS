import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.analysis.MultivariateFunction;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.optimization.PointValuePair;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.ConvergenceChecker;
import org.apache.commons.math3.optimization.SimpleValueChecker;
import org.apache.commons.math3.optimization.MultivariateOptimizer;
import org.apache.commons.math3.optimization.OptimizationData;
import org.junit.Test;

public class SimplexOptimizerTest {

    @Test
    public void testOptimizeInternal() {
        SimplexOptimizer optimizer = new SimplexOptimizer();

        // Test with no optimization data
        PointValuePair result = optimizer.optimizeInternal(100, new MultivariateFunction() {
            @Override
            public double value(double[] point) {
                return point[0] + point[1]; // Simple linear function
            }
        }, GoalType.MINIMIZE);

        assertEquals(0.0, result.getPoint()[0], 1e-10);
        assertEquals(0.0, result.getPoint()[1], 1e-10);
        assertEquals(0.0, result.getValue(), 1e-10);

        // Test with initial guess
        OptimizationData[] optData = { new InitialGuess(new double[] { 1.0, 2.0 }) };
        result = optimizer.optimizeInternal(100, new MultivariateFunction() {
            @Override
            public double value(double[] point) {
                return point[0] + point[1]; // Simple linear function
            }
        }, GoalType.MINIMIZE, optData);

        assertEquals(0.0, result.getPoint()[0], 1e-10);
        assertEquals(0.0, result.getPoint()[1], 1e-10);
        assertEquals(0.0, result.getValue(), 1e-10);

        // Test with custom convergence checker
        ConvergenceChecker<PointValuePair> checker = new SimpleValueChecker(1e-5, 1e-5);
        optimizer.setConvergenceChecker(checker);

        optData = new OptimizationData[] { new InitialGuess(new double[] { 1.0, 2.0 }) };
        result = optimizer.optimizeInternal(100, new MultivariateFunction() {
            @Override
            public double value(double[] point) {
                return point[0] + point[1]; // Simple linear function
            }
        }, GoalType.MINIMIZE, optData);

        assertEquals(0.0, result.getPoint()[0], 1e-5);
        assertEquals(0.0, result.getPoint()[1], 1e-5);
        assertEquals(0.0, result.getValue(), 1e-5);
    }

    @Test(expected = NullArgumentException.class)
    public void testOptimizeInternal_NullSimplex() {
        SimplexOptimizer optimizer = new SimplexOptimizer();
        optimizer.setSimplex(null);

        optimizer.optimizeInternal(100, new MultivariateFunction() {
            @Override
            public double value(double[] point) {
                return point[0] + point[1]; // Simple linear function
            }
        }, GoalType.MINIMIZE);
    }

    @Test
    public void testDoOptimize() {
        SimplexOptimizer optimizer = new SimplexOptimizer();
        optimizer.setSimplex(new AbstractSimplex(new double[] { 1.0, 2.0 }));

        PointValuePair result = optimizer.doOptimize();

        assertEquals(0.0, result.getPoint()[0], 1e-10);
        assertEquals(0.0, result.getPoint()[1], 1e-10);
        assertEquals(0.0, result.getValue(), 1e-10);
    }

    @Test(expected = NullArgumentException.class)
    public void testDoOptimize_NullSimplex() {
        SimplexOptimizer optimizer = new SimplexOptimizer();
        optimizer.setSimplex(null);

        optimizer.doOptimize();
    }

    @Test
    public void testParseOptimizationData() {
        SimplexOptimizer optimizer = new SimplexOptimizer();

        AbstractSimplex simplex = new AbstractSimplex(new double[] { 1.0, 2.0 });
        OptimizationData[] optData = { simplex };
        optimizer.parseOptimizationData(optData);

        assertTrue(optimizer.getSimplex() == simplex);
    }
}