import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math3.analysis.MultivariateFunction;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.optimization.ConvergenceChecker;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.PointValuePair;
import org.apache.commons.math3.optimization.SimpleValueChecker;
import org.apache.commons.math3.optimization.direct.AbstractSimplex;
import org.apache.commons.math3.optimization.direct.SimplexOptimizer;
import org.junit.Before;
import org.junit.Test;

public class SimplexOptimizerTest {

    private static final double EPSILON = 1e-6;

    private MultivariateFunction function;
    private AbstractSimplex simplex;
    private ConvergenceChecker<PointValuePair> checker;
    private SimplexOptimizer optimizer;

    @Before
    public void setUp() {
        // Create the function to be optimized
        function = new MultivariateFunction() {
            public double value(double[] point) {
                return point[0] * point[0] + point[1] * point[1];
            }
        };

        // Create the simplex
        simplex = new AbstractSimplex() {
            public void build(double[] startPoint) {
                double[][] simplexArray = new double[][] {
                    {startPoint[0], startPoint[1]},
                    {startPoint[0] + 1.0, startPoint[1]},
                    {startPoint[0], startPoint[1] + 1.0}
                };
                setPoints(simplexArray);
            }
        };

        // Create the convergence checker
        checker = new SimpleValueChecker(EPSILON, EPSILON);

        // Create the optimizer
        optimizer = new SimplexOptimizer(checker);
        optimizer.setSimplex(simplex);
    }

    @Test
    public void testOptimize() {
        try {
            PointValuePair result = optimizer.optimize(1000, function, GoalType.MINIMIZE);
            assertEquals(0.0, result.getValue(), EPSILON);
            assertEquals(0.0, result.getPoint()[0], EPSILON);
            assertEquals(0.0, result.getPoint()[1], EPSILON);
        } catch (Exception e) {
            fail("No exception should be thrown");
        }
    }

    @Test
    public void testOptimizeMaxEvalReached() {
        try {
            PointValuePair result = optimizer.optimize(1, function, GoalType.MAXIMIZE);
            assertEquals(1.0, result.getValue(), EPSILON);
            assertTrue(result.getPoint()[0] > 0.0);
            assertTrue(result.getPoint()[1] > 0.0);
        } catch (Exception e) {
            fail("No exception should be thrown");
        }
    }

    @Test(expected = NullArgumentException.class)
    public void testOptimizeNullSimplex() {
        optimizer.setSimplex(null);
        optimizer.optimize(1000, function, GoalType.MINIMIZE);
    }

    @Test
    public void testOptimizeConvergenceChecker() {
        try {
            checker = new ConvergenceChecker<PointValuePair>() {
                public boolean converged(int iteration, PointValuePair previous, PointValuePair current) {
                    return Math.abs(previous.getValue() - current.getValue()) < EPSILON;
                }
            };
            optimizer.setConvergenceChecker(checker);
            PointValuePair result = optimizer.optimize(1000, function, GoalType.MINIMIZE);
            assertEquals(0.0, result.getValue(), EPSILON);
            assertEquals(0.0, result.getPoint()[0], EPSILON);
            assertEquals(0.0, result.getPoint()[1], EPSILON);
        } catch (Exception e) {
            fail("No exception should be thrown");
        }
    }

    @Test
    public void testOptimizeInternal() {
        int maxEval = 1000;
        GoalType goalType = GoalType.MINIMIZE;
        PointValuePair result = optimizer.optimizeInternal(maxEval, function, goalType);
        assertEquals(0.0, result.getValue(), EPSILON);
        assertEquals(0.0, result.getPoint()[0], EPSILON);
        assertEquals(0.0, result.getPoint()[1], EPSILON);
    }

    @Test(expected = NullArgumentException.class)
    public void testParseOptimizationDataNullArgument() {
        optimizer.parseOptimizationData(null);
    }

    @Test
    public void testParseOptimizationDataEmptyArray() {
        OptimizationData[] optData = new OptimizationData[0];
        optimizer.parseOptimizationData(optData);
    }

    @Test
    public void testParseOptimizationData() {
        OptimizationData[] optData = new OptimizationData[1];
        optData[0] = simplex;
        optimizer.parseOptimizationData(optData);
    }

}