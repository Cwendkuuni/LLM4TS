import org.junit.Before;
import org.junit.Test;
import org.apache.commons.math3.analysis.MultivariateFunction;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.ConvergenceChecker;
import org.apache.commons.math3.optimization.PointValuePair;
import org.apache.commons.math3.optimization.SimpleValueChecker;
import org.apache.commons.math3.optimization.MultivariateOptimizer;
import org.apache.commons.math3.optimization.OptimizationData;

import static org.junit.Assert.*;

public class SimplexOptimizerTest {

    private SimplexOptimizer optimizer;

    @Before
    public void setup() {
        optimizer = new SimplexOptimizer();
    }

    @Test
    public void testConstructor() {
        assertNotNull(optimizer);
        assertTrue(optimizer.getConvergenceChecker() instanceof SimpleValueChecker);
    }

    @Test
    public void testConstructorWithChecker() {
        ConvergenceChecker<PointValuePair> checker = new SimpleValueChecker();
        optimizer = new SimplexOptimizer(checker);
        assertNotNull(optimizer);
        assertEquals(checker, optimizer.getConvergenceChecker());
    }

    @Test
    public void testConstructorWithThresholds() {
        double rel = 0.0001;
        double abs = 0.000001;
        optimizer = new SimplexOptimizer(rel, abs);
        assertNotNull(optimizer);
        ConvergenceChecker<PointValuePair> checker = optimizer.getConvergenceChecker();
        assertTrue(checker instanceof SimpleValueChecker);
        SimpleValueChecker valueChecker = (SimpleValueChecker) checker;
        assertEquals(rel, valueChecker.getRelativeThreshold(), 0.0);
        assertEquals(abs, valueChecker.getAbsoluteThreshold(), 0.0);
    }

    @Test
    public void testSetSimplex() {
        AbstractSimplex simplex = new Simplex(new double[] {1.0, 2.0});
        optimizer.setSimplex(simplex);
        assertSame(simplex, optimizer.getSimplex());
    }

    @Test(expected = NullArgumentException.class)
    public void testSetSimplexNull() {
        optimizer.setSimplex(null);
    }

    @Test
    public void testOptimizeInternal() {
        int maxEval = 1000;
        MultivariateFunction f = new MultivariateFunction() {
            @Override
            public double value(double[] point) {
                return point[0] * point[0] + point[1] * point[1];
            }
        };
        GoalType goalType = GoalType.MINIMIZE;
        OptimizationData[] optData = new OptimizationData[] {
                new InitialGuess(new double[] {1.0, 2.0})
        };
        PointValuePair result = optimizer.optimizeInternal(maxEval, f, goalType, optData);
        assertEquals(0.0, result.getPoint()[0], 0.0);
        assertEquals(0.0, result.getPoint()[1], 0.0);
        assertEquals(0.0, result.getValue(), 0.0);
    }

    @Test(expected = NullArgumentException.class)
    public void testOptimizeInternalNoSimplex() {
        int maxEval = 1000;
        MultivariateFunction f = new MultivariateFunction() {
            @Override
            public double value(double[] point) {
                return point[0] * point[0] + point[1] * point[1];
            }
        };
        GoalType goalType = GoalType.MINIMIZE;
        optimizationData[] optData = new optimizationData[0];
        optimizer.optimizeInternal(maxEval, f, goalType, optData);
    }

    @Test
    public void testDoOptimize() {
        AbstractSimplex simplex = new Simplex(new double[] {1.0, 2.0});
        optimizer.setSimplex(simplex);

        PointValuePair result = optimizer.doOptimize();

        assertEquals(0.0, result.getPoint()[0], 0.0);
        assertEquals(0.0, result.getPoint()[1], 0.0);
        assertEquals(0.0, result.getValue(), 0.0);
    }

}