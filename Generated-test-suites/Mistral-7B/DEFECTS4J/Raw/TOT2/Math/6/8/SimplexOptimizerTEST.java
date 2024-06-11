package org.apache.commons.math3.optimization.direct;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.analysis.MultivariateFunction;
import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.optim.InitialGuess;
import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleValueChecker;
import org.apache.commons.math3.optim.StartPoint;
import org.apache.commons.math3.optim.goaltype.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalTypeWeightedSum;
import org.apache.commons.math3.optim.nonlinear.scalar.ObjectiveFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.WeightedSum;
import org.apache.commons.math3.optim.simple.SimpleValueCheckerFactory;
import org.apache.commons.math3.optim.simplext.NelderMeadSimplex;

import org.junit.Before;
import org.junit.Test;

/**
 * Tests for the {@link SimplexOptimizer} class.
 */
public class SimplexOptimizerTest {

    private MultivariateFunction function;
    private PointValuePair optimum;
    private SimplexOptimizer optimizer;
    private AbstractSimplex simplex;
    private double[] startPoint;

    /**
     * Sets up the test environment.
     */
    @Before
    public void setUp() {
        function = new PolynomialFunction(new double[][]{{-1.0, 1.0}, {1.0, 1.0},
                                                           {1.0, -1.0}}, 3);
        optimum = new PointValuePair(new double[]{0.0, 0.0}, 0.0);
        simplex = new NelderMeadSimplex();
        optimizer = new SimplexOptimizer(new SimpleValueChecker());
    }

    /**
     * Tests the default constructor.
     */
    @Test
    public void testDefaultConstructor() {
        assertNull(optimizer.getSimplex());
    }

    /**
     * Tests the constructor accepting a {@link ConvergenceChecker}.
     */
    @Test
    public void testConstructorWithConvertergenceChecker() {
        ConvergenceChecker<PointValuePair> checker = new SimpleValueChecker(0.001, 0.0);
        optimizer = new SimplexOptimizer(checker);
        assertEquals(checker, optimizer.getConvergenceChecker());
    }

    /**
     * Tests the constructor accepting a relative and absolute tolerance.
     */
    @Test
    public void testConstructorWithTolerances() {
        optimizer = new SimplexOptimizer(0.001, 0.0);
        ConvergenceChecker<PointValuePair> checker = optimizer.getConvergenceChecker();
        assertTrue(checker instanceof SimpleValueChecker);
        SimpleValueChecker simpleChecker = (SimpleValueChecker) checker;
        assertEquals(0.001, simpleChecker.getRelativeTolerance(), 0.0);
        assertEquals(0.0, simpleChecker.getAbsoluteTolerance(), 0.0);
    }

    /**
     * Tests the setSimplex method.
     */
    @Test
    public void testSetSimplex() {
        optimizer.setSimplex(simplex);
        assertEquals(simplex, optimizer.getSimplex());
    }

    /**
     * Tests the optimize method with a null function.
     */
    @Test
    public void testOptimizeWithNullFunction() {
        assertThrows(NullArgumentException.class, () -> optimizer.optimize(1, null, GoalType.MINIMIZE, new double[0]));
    }

    /**
     * Tests the optimize method with an empty start point.
     */
    @Test
    public void testOptimizeWithEmptyStartPoint() {
        assertThrows(NullArgumentException.class, () -> optimizer.optimize(1, function, GoalType.MINIMIZE, new double[0]));
    }

    /**
     * Tests the optimize method with no evaluations.
     */
    @Test
    public void testOptimizeWithNoEvaluations() {
        startPoint = new double[]{1.0, 1.0};
        PointValuePair result = optimizer.optimize(0, function, GoalType.MINIMIZE, new double[]{startPoint[0], startPoint[1]});
        assertNull(result);
    }

    /**
     * Tests the optimize method with one evaluation.
     */
    @Test
    public void testOptimizeWithOneEvaluation() {
        startPoint = new double[]{1.0, 1.0};
        optimizer.optimize(1, function, GoalType.MINIMIZE, new double[]{startPoint[0], startPoint[1]});
        assertFalse(optimizer.isOptimized());
    }

    /**
     * Tests the optimize method with a single call.
     */
    @Test
    public void testOptimizeWithSingleCall() {
        startPoint = new double[]{1.0, 1.0};
        optimizer.optimize(MaxEval.UNLIMITED, function, GoalType.MINIMIZE, new double[]{startPoint[0], startPoint[1]});
        PointValuePair result = optimizer.getLastPointValuePair();
        assertEquals(optimum.getValue(), result.getValue(), 0.0);
        assertArrayEquals(optimum.getPoint(), result.getPoint(), 0.0);
    }

    /**
     * Tests the optimize method with multiple calls.
     */
    @Test
    public void testOptimizeWithMultipleCalls() {
        startPoint = new double[]{1.0, 1.0};
        GoalType goalType = GoalType.MINIMIZE;
        GoalTypeWeightedSum goal = new GoalTypeWeightedSum(new double[]{0.5, 0.5}, goalType);
        WeightedSum weights = new WeightedSum(new double[]{0.5, 0.5});
        OptimizationData[] data = {new InitialGuess(startPoint), simplex};
        optimizer.optimize(MaxEval.UNLIMITED, function, goal, data);
        PointValuePair result = optimizer.getLastPointValuePair();
        assertEquals(optimum.getValue(), result.getValue(), 0.0);
        assertArrayEquals(optimum.getPoint(), result.getPoint(), 0.0);
    }

    /**
     * Tests the optimize method with different goals.
     */
    @Test
    public void testOptimizeWithDifferentGoals() {
        startPoint = new double[]{1.0, 1.0};
        optimizer.optimize(MaxEval.UNLIMITED, function, GoalType.MAXIMIZE, new double[]{startPoint[0], startPoint[1]});
        PointValuePair result = optimizer.getLastPointValuePair();
        assertEquals(function.value(optimum.getPoint()), result.getValue(), 0.0);
        assertArrayEquals(optimum.getPoint(), result.getPoint(), 0.0);
    }

    /**
     * Tests the optimize method with a non-simplext simplex.
     */
    @Test
    public void testOptimizeWithNonSimplextSimplex() {
        AbstractSimplex wrongSimplex = new AbstractSimplex() {
            @Override
            public int getSize() {
                return 1;
            }

            @Override
            public PointValuePair getPoint(int index) {
                return new PointValuePair(new double[]{0.0}, 0.0);
            }

            @Override
            public void build(double[] startPoint) {
            }

            @Override
            public void evaluate(MultivariateFunction function, Comparator<PointValuePair> comparator) {
            }

            @Override
            public void iterate(MultivariateFunction function, Comparator<PointValuePair> comparator) {
            }
        };
        optimizer.setSimplex(wrongSimplex);
        startPoint = new double[]{1.0, 1.0};
        assertThrows(RuntimeException.class, () -> optimizer.optimize(MaxEval.UNLIMITED, function, GoalType.MINIMIZE, new double[]{startPoint[0], startPoint[1]}));
    }
}