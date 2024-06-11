package org.apache.commons.math.optimization.direct;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Comparator;

import org.apache.commons.math3.exception.MaxEvalExceededException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.exception.PrecisionException;
import org.apache.commons.math3.optim.InitialGuess;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.RealPointValuePair;
import org.apache.commons.math3.optim.SimpleBounds;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.ObjectiveFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.SolverOptions;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.BFGS;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.NelderMeadSimplex;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.NonLinearConstraint;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.SimpleBoundingBox;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.StoppingCriteria;
import org.junit.Before;
import org.junit.Test;

public class MultiDirectionalTest {

    private MultiDirectional optimizer;
    private Comparator<RealPointValuePair> comparator;
    private Function1D function;
    private ObjectiveFunction objFunction;
    private PointValuePair initialPoint;
    private SimpleBounds bounds;
    private double[] expectedMinimum;

    @Before
    public void setUp() {
        optimizer = new MultiDirectional();
        comparator = new Comparator<RealPointValuePair>() {
            @Override
            public int compare(RealPointValuePair o1, RealPointValuePair o2) {
                return Double.compare(o1.getValue(), o2.getValue());
            }
        };
        function = new Function1D() {
            @Override
            public double value(double x) {
                return Math.pow(Math.sin(x), 2);
            }
        };
        objFunction = new ObjectiveFunction() {
            @Override
            public double value(PointValuePair point) {
                return function.value(point.getPoint()[0]);
            }

            @Override
            public GoalType getGoalType() {
                return GoalType.MINIMIZE;
            }
        };
        initialPoint = new RealPointValuePair(new double[] {0}, Double.NaN);
        bounds = new SimpleBounds(new double[] {-10, 10}, new double[] {10, 10});
        expectedMinimum = new double[] {-3.14159265359};
    }

    @Test
    public void testIterateSimplexDefault() throws Exception {
        NelderMeadSimplex simplex = new NelderMeadSimplex(10, comparator);
        optimizer.iterateSimplex(comparator);

        // Check that the iterator has returned
        assertTrue(optimizer.isOptimized());

        // Check that the best point is the expected minimum
        final RealPointValuePair best = optimizer.getBestPoint();
        assertArrayEquals(expectedMinimum, best.getPointRef(), 1e-8);
        assertEquals(function.value(expectedMinimum[0]), best.getValue(), 1e-8);

        // Check that the simplex is sorted
        PointValuePair[] simplexPoints = optimizer.getSimpleX();
        assertFalse(Arrays.stream(simplexPoints).anyMatch(p -> p instanceof RealPointValuePair && comparator.compare(p, best) > 0));
    }

    @Test
    public void testIterateSimplexCustom() throws Exception {
        NelderMeadSimplex simplex = new NelderMeadSimplex(10, comparator);
        optimizer = new MultiDirectional(2.5, 0.7);
        optimizer.iterateSimplex(comparator);

        // Check that the iterator has returned
        assertTrue(optimizer.isOptimized());

        // Check that the best point is the expected minimum
        final RealPointValuePair best = optimizer.getBestPoint();
        assertArrayEquals(expectedMinimum, best.getPointRef(), 1e-8);
        assertEquals(function.value(expectedMinimum[0]), best.getValue(), 1e-8);

        // Check that the simplex is sorted
        PointValuePair[] simplexPoints = optimizer.getSimpleX();
        assertFalse(Arrays.stream(simplexPoints).anyMatch(p -> p instanceof RealPointValuePair && comparator.compare(p, best) > 0));
    }

    @Test(expected = FunctionEvaluationException.class)
    public void testIterateSimplexEvaluationException() throws Exception {
        NelderMeadSimplex simplex = new NelderMeadSimplex(10, comparator);
        ObjectiveFunction objFunctionWithException = new ObjectiveFunction() {
            @Override
            public double value(PointValuePair point) throws NumberIsTooLargeException {
                throw new NumberIsTooLargeException();
            }

            @Override
            public GoalType getGoalType() {
                return GoalType.MINIMIZE;
            }
        };

        optimizer.setObjectiveFunction(objFunctionWithException);
        optimizer.iterateSimplex(comparator);
    }

    @Test(expected = OptimizationException.class)
    public void testIterateSimplexOptimizationException() throws Exception {
        NelderMeadSimplex simplex = new NelderMeadSimplex(10, comparator);
        ObjectiveFunction objFunctionWithException = new ObjectiveFunction() {
            @Override
            public double value(PointValuePair point) throws PrecisionException {
                throw new PrecisionException();
            }

            @Override
            public GoalType getGoalType() {
                return GoalType.MINIMIZE;
            }
        };

        optimizer.setObjectiveFunction(objFunctionWithException);
        optimizer.iterateSimplex(comparator);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIterateSimplexIllegalArgumentException() throws Exception {
        NelderMeadSimplex simplex = new NelderMeadSimplex(10, comparator);
        ObjectiveFunction objFunctionWithNullValue = new ObjectiveFunction() {
            @Override
            public double value(PointValuePair point) {
                return null;
            }

            @Override
            public GoalType getGoalType() {
                return GoalType.MINIMIZE;
            }
        };

        optimizer.setObjectiveFunction(objFunctionWithNullValue);
        optimizer.iterateSimplex(comparator);
    }

    @Test
    public void testEvaluateNewSimplexWithOnePoint() throws Exception {
        NelderMeadSimplex simplex = new NelderMeadSimplex(1, comparator);
        optimizer.iterateSimplex(comparator);

        final RealPointValuePair reflected = optimizer.evaluateNewSimplex(simplex.getSimpleX(), 1.0, comparator);
        final RealPointValuePair expectedReflected = new RealPointValuePair(new double[] {0.5}, function.value(0.5), false);
        assertArrayEquals(expectedReflected.getPointRef(), reflected.getPointRef(), 1e-8);
        assertEquals(expectedReflected.getValue(), reflected.getValue(), 1e-8);

        final RealPointValuePair expanded = optimizer.evaluateNewSimplex(simplex.getSimpleX(), 2.0, comparator);
        final RealPointValuePair expectedExpanded = new RealPointValuePair(new double[] {1.5}, function.value(1.5), false);
        assertArrayEquals(expectedExpanded.getPointRef(), expanded.getPointRef(), 1e-8);
        assertEquals(expectedExpanded.getValue(), expanded.getValue(), 1e-8);

        optimizer.iterateSimplex(comparator);

        final RealPointValuePair contracted = optimizer.evaluateNewSimplex(simplex.getSimpleX(), 0.5, comparator);
        final RealPointValuePair expectedContracted = new RealPointValuePair(new double[] {0}, Double.NaN, false);
        assertArrayEquals(expectedContracted.getPointRef(), contracted.getPointRef());
        assertNull(contracted.getValue());
    }

    @Test
    public void testEvaluateNewSimplexWithTwoPoints() throws Exception {
        NelderMeadSimplex simplex = new NelderMeadSimplex(2, comparator);
        optimizer.iterateSimplex(comparator);

        final RealPointValuePair original = simplex[0];
        final RealPointValuePair reflected = optimizer.evaluateNewSimplex(simplex, 1.0, comparator);
        final RealPointValuePair expanded = optimizer.evaluateNewSimplex(simplex, 2.0, comparator);

        // Check that the reflected point is better than the original
        assertTrue(comparator.compare(reflected, original) < 0);

        // Check that the expanded point is better than the reflected point
        assertTrue(comparator.compare(expanded, reflected) <= 0);

        // Check that the contracted point is not better than the original
        final RealPointValuePair contracted = optimizer.evaluateNewSimplex(simplex, 0.5, comparator);
        assertTrue(comparator.compare(contracted, original) >= 0);

        // Check that the contracted point is between the original and the reflected points
        assertTrue(comparator.compare(original, contracted) < 0);
        assertTrue(comparator.compare(contracted, reflected) < 0);
    }

    @Test
    public void testEvaluateNewSimplexWithThreePoints() throws Exception {
        NelderMeadSimplex simplex = new NelderMeadSimplex(3, comparator);
        optimizer.iterateSimplex(comparator);

        final RealPointValuePair original = simplex[0];
        final RealPointValuePair reflected = optimizer.evaluateNewSimplex(simplex, 1.0, comparator);
        final RealPointValuePair expanded = optimizer.evaluateNewSimplex(simplex, 2.0, comparator);

        // Check that the reflected point is better than the original
        assertTrue(comparator.compare(reflected, original) < 0);

        // Check that the expanded point is better than the reflected point
        assertTrue(comparator.compare(expanded, reflected) <= 0);

        // Check that the contracted point is not better than the original
        final RealPointValuePair contracted = optimizer.evaluateNewSimplex(simplex, 0.5, comparator);
        assertFalse(comparator.compare(contracted, original) < 0);

        // Check that the contracted point is between the original and the reflected points
        assertTrue(comparator.compare(original, contracted) <= 0);
        assertTrue(comparator.compare(contracted, reflected) < 0);
    }

    @Test(expected = MaxEvalExceededException.class)
    public void testEvaluateNewSimplexMaxEvalExceededException() throws Exception {
        NelderMeadSimplex simplex = new NelderMeadSimplex(10, comparator);
        SolverOptions options = new SolverOptions();
        options.setMaxEvaluations(1);

        optimizer.setObjectiveFunction(objFunction);
        optimizer.setInitialGuess(new InitialGuess(initialPoint));
        optimizer.setBounds(bounds);
        optimizer.setSolver(new BFGS());
        optimizer.setStoppingCriteria(new StoppingCriteria(options));

        optimizer.iterateSimplex(comparator);

        optimizer.evaluateNewSimplex(simplex.getSimpleX(), 1.0, comparator);
    }

}