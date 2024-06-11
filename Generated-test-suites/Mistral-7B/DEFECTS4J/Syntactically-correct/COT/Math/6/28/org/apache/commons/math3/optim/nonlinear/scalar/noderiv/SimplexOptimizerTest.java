package org.apache.commons.math3.optimization.direct;

import static org.junit.Assert.*;

import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.GoalType;
import org.apache.commons.math3.optim.InitialGuess;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleValueChecker;
import org.apache.commons.math3.optim.SimpleSimplex;
import org.apache.commons.math3.optim.nederroydmead. NelderMeadSimplex;
import org.apache.commons.math3.optim.initialization.StartPointGenerator;
import org.apache.commons.math3.optim.point.PointValuePairGenerator;
import org.apache.commons.math3.optim.convergence.ConvergenceChecker;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType.MINIMIZE;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType.MAXIMIZE;
import org.apache.commons.math3.optim.PointValuePairList;
import org.junit.Before;
import org.junit.Test;

/**
 * Test class for SimplexOptimizer.
 */
public class SimplexOptimizerTest {
    private MultivariateFunction function;
    private SimplexOptimizer optimizer;
    private ConvergenceChecker<PointValuePair> convergenceChecker;

    @Before
    public void setup() {
        function = new PolynomialFunction(new double[]{-1.0, 1.0}, 2, 3.0);
        optimizer = new SimplexOptimizer(new SimpleValueChecker(1E-8, 1E-8));
        convergenceChecker = new SimpleValueChecker(1E-8, 1E-8);
    }

    /**
     * Test constructor with default convergence checker.
     */
    @Test
    public void testDefaultConstructor() {
        assertNotNull(new SimplexOptimizer());
    }

    /**
     * Test constructor with custom convergence checker.
     */
    @Test
    public void testCustomConstructor() {
        assertNotNull(new SimplexOptimizer(convergenceChecker));
    }

    /**
     * Test constructor with relative and absolute thresholds.
     */
    @Test
    public void testConstructorWithThresholds() {
        assertNotNull(new SimplexOptimizer(1E-8, 1E-8));
    }

    /**
     * Test setSimplex method.
     */
    @Test
    public void testSetSimplex() {
        SimplexOptimizer optimizer = new SimplexOptimizer(new SimpleValueChecker());
        assertNull(optimizer.getSimplex());

        SimpleSimplex simplex = new SimpleSimplex(3);
        optimizer.setSimplex(simplex);
        assertSame(simplex, optimizer.getSimplex());
    }

    /**
     * Test optimization with a simple function.
     */
    @Test
    public void testOptimizeSimpleFunction() {
        PointValuePair result = optimizer.optimize(new MaxEval(100), function, MINIMIZE,
                                                   new double[]{-1.0, -1.0});
        PointValuePair expected = new PointValuePair(-1.0, -1.0, 3.0);
        assertEquals(expected, result);
    }

    /**
     * Test optimization with a function having a minimum.
     */
    @Test
    public void testOptimizeFunctionWithMinimum() {
        PointValuePair result = optimizer.optimize(new MaxEval(100), function, MINIMIZE,
                                                   new double[]{-1.0, 1.0});
        PointValuePair expected = new PointValuePair(1.0, 1.0, 3.0);
        assertEquals(expected, result);
    }

    /**
     * Test optimization with a function having a maximum.
     */
    @Test
    public void testOptimizeFunctionWithMaximum() {
        optimizer = new SimplexOptimizer(new SimpleValueChecker(1E-8, 1E-8), MAXIMIZE);
        PointValuePair result = optimizer.optimize(new MaxEval(100), function, MAXIMIZE,
                                                   new double[]{-1.0, -1.0});
        PointValuePair expected = new PointValuePair(-1.0, -1.0, -3.0);
        assertEquals(expected, result);
    }

    /**
     * Test optimization with a non-null optimization data.
     */
    @Test
    public void testOptimizeWithOptimizationData() {
        PointValuePairGenerator pointValuePairGenerator = new PointValuePairList();
        PointValuePair[] points = pointValuePairGenerator.generate(10,
                new UnivariateFunction() {
                    public double value(double x) {
                        return Math.sin(x);
                    }
                });

        InitialGuess initialGuess = new InitialGuess(points);
        SimpleSimplex simplex = new SimpleSimplex(points.length);

        PointValuePair result = optimizer.optimize(new MaxEval(100), function, MINIMIZE,
                new OptimizationData[]{initialGuess, simplex});

        PointValuePair expected = simplex.getPoint(0);
        assertEquals(expected, result);
    }

    /**
     * Test optimization with a null optimization data.
     */
    @Test(expected = NullArgumentException.class)
    public void testOptimizeWithNullOptimizationData() {
        optimizer.optimize(new MaxEval(100), function, MINIMIZE, null);
    }

    /**
     * Test optimization with invalid optimization data.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testOptimizeWithInvalidOptimizationData() {
        SimpleSimplex simplex = new SimpleSimplex(3);
        optimizer.optimize(new MaxEval(100), function, MINIMIZE, new Object[]{simplex});
    }

    /**
     * Test optimization with a null function.
     */
    @Test(expected = NullArgumentException.class)
    public void testOptimizeWithNullFunction() {
        optimizer.optimize(new MaxEval(100), null, MINIMIZE,
                new double[]{-1.0, -1.0});
    }

    /**
     * Test optimization with invalid goalType.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testOptimizeWithInvalidGoalType() {
        optimizer.optimize(new MaxEval(100), function, null, new double[]{-1.0, -1.0});
    }

    /**
     * Test optimization with invalid start point.
     */
    @Test(expected = NullArgumentException.class)
    public void testOptimizeWithNullStartPoint() {
        optimizer.optimize(new MaxEval(100), function, MINIMIZE, null);
    }

    /**
     * Test optimization with an empty optimization data list.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testOptimizeWithEmptyOptimizationData() {
        optimizer.optimize(new MaxEval(100), function, MINIMIZE, (OptimizationData[]) new Object[0]);
    }
}