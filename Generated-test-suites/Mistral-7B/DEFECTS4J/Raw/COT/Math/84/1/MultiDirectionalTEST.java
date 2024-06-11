package org.apache.commons.math.optimization.direct;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.Comparator;

import org.apache.commons.math3.exception.MaxEvalExceededException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.RealFunction;
import org.apache.commons.math3.optim.SimpleValueFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.ObjectiveFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.FibonacciSearch;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.SimpleBoundingBoxMinimizer;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.SimpleLineMinimizer;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.SimpleSimplexMinimizer;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.SimplexIteration;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.SimplexIterationEvent;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.SimplexIterationStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Test cases for MultiDirectional class
 */
public class MultiDirectionalTest {

    private Comparator<PointValuePair> comparator;
    private RealFunction function;
    private double[] xSmallest;
    private double[] xTransformed;

    @Before
    public void setUp() {
        comparator = new Comparator<PointValuePair>() {
            @Override
            public int compare(PointValuePair o1, PointValuePair o2) {
                return (Double.compare(o1.getValue(), o2.getValue()) < 0) ? -1 : 1;
            }
        };
        function = new SimpleValueFunction("function", x -> Math.pow(x[0], 2) + Math.pow(x[1], 2));
    }

    @After
    public void tearDown() {
        simplex = null;
    }

    /**
     * Test default constructor
     */
    @Test
    public void testDefaultConstructor() throws Exception {
        MultiDirectional optimizer = new MultiDirectional();
        try {
            optimizer.optimize(function, new PointValuePair(new double[]{0.0, 0.0}, 0.0), GoalType.MINIMIZE,
                               new RealConvergenceChecker(1.0e-6, 100, 100));
            fail("Expected OptimizationException");
        } catch (OptimizationException e) {
            // expected
        }
    }

    /**
     * Test constructor with specified coefficients
     */
    @Test
    public void testConstructorWithSpecifiedCoefficients() throws Exception {
        MultiDirectional optimizer = new MultiDirectional(2.5, 0.7);
        try {
            optimizer.optimize(function, new PointValuePair(new double[]{0.0, 0.0}, 0.0), GoalType.MINIMIZE,
                               new RealConvergenceChecker(1.0e-6, 100, 100));
            fail("Expected OptimizationException");
        } catch (OptimizationException e) {
            // expected
        }
    }

    /**
     * Test iterateSimplex method with reflection
     */
    @Test
    public void testIterateSimplexReflection() throws Exception {
        MultiDirectional optimizer = new MultiDirectional();
        PointValuePair initialPoint = new PointValuePair(new double[]{1.0, 1.0}, function.value(new double[]{1.0, 1.0}));
        double[] reflectionPoint = new double[]{2.0, 1.0};
        double[] expectedReflectionPoint = new double[]{3.0, 2.0};
        double[] transformedReflectionPoint = new double[2];

        optimizer.optimize(function, initialPoint, GoalType.MINIMIZE,
                new RealConvergenceChecker(1.0e-6, 100, 100));

        xSmallest = initialPoint.getPointRef();
        optimizer.iterateSimplex(comparator);

        xTransformed = optimizer.simplex[1].getPointRef();
        Arrays.stream(xTransformed).parallel().forEachIndexed(i -> transformedReflectionPoint[i] = xSmallest[i] + 1.0 * (xSmallest[i] - xTransformed[i]));

        PointValuePair reflectedPoint = new PointValuePair(transformedReflectionPoint, function.value(transformedReflectionPoint));

        assertEquals(reflectedPoint, optimizer.simplex[1]);
        assertEquals(expectedReflectionPoint, transformedReflectionPoint);
    }

    /**
     * Test iterateSimplex method with expansion
     */
    @Test
    public void testIterateSimplexExpansion() throws Exception {
        MultiDirectional optimizer = new MultiDirectional();
        PointValuePair initialPoint = new PointValuePair(new double[]{1.0, 1.0}, function.value(new double[]{1.0, 1.0}));
        double[] expansionPoint = new double[]{3.0, 3.0};
        double[] expectedExpansionPoint = new double[]{6.0, 6.0};
        double[] transformedExpansionPoint = new double[2];

        optimizer.optimize(function, initialPoint, GoalType.MINIMIZE,
                new RealConvergenceChecker(1.0e-6, 100, 100));

        xSmallest = initialPoint.getPointRef();
        optimizer.iterateSimplex(comparator);
        optimizer.iterateSimplex(comparator);

        xTransformed = optimizer.simplex[2].getPointRef();
        Arrays.stream(xTransformed).parallel().forEachIndexed(i -> transformedExpansionPoint[i] = xSmallest[i] + 2.0 * (xSmallest[i] - xTransformed[i]));

        PointValuePair expandedPoint = new PointValuePair(transformedExpansionPoint, function.value(transformedExpansionPoint));

        assertEquals(expandedPoint, optimizer.simplex[2]);
        assertEquals(expectedExpansionPoint, transformedExpansionPoint);
    }

    /**
     * Test iterateSimplex method with contraction
     */
    @Test
    public void testIterateSimplexContraction() throws Exception {
        MultiDirectional optimizer = new MultiDirectional();
        PointValuePair initialPoint = new PointValuePair(new double[]{1.0, 1.0}, function.value(new double[]{1.0, 1.0}));
        double[] contractionPoint = new double[]{0.5, 0.5};
        double[] expectedContractionPoint = new double[]{0.5, 0.5};

        optimizer.optimize(function, initialPoint, GoalType.MINIMIZE,
                new RealConvergenceChecker(1.0e-6, 100, 100));

        xSmallest = initialPoint.getPointRef();
        optimizer.iterateSimplex(comparator);

        xTransformed = optimizer.simplex[1].getPointRef();
        Arrays.stream(xTransformed).parallel().forEachIndexed(i -> transformedContractionPoint[i] = xSmallest[i] - 0.5 * (xSmallest[i] - xTransformed[i]));

        PointValuePair contractedPoint = new PointValuePair(transformedContractionPoint, function.value(transformedContractionPoint));

        assertEquals(contractedPoint, optimizer.simplex[1]);
        assertEquals(expectedContractionPoint, transformedContractionPoint);
    }

    /**
     * Test evaluateNewSimplex method with reflection
     */
    @Test
    public void testEvaluateNewSimplexReflection() throws Exception {
        MultiDirectional optimizer = new MultiDirectional();
        RealPointValuePair[] original = new RealPointValuePair[]{
            new RealPointValuePair(new double[]{0.0, 0.0}, 0.0),
            new RealPointValuePair(new double[]{1.0, 1.0}, 2.0)
        };
        RealPointValuePair expectedBestPoint = new RealPointValuePair(new double[]{1.0, 1.0}, 2.0);

        RealPointValuePair bestPoint = optimizer.evaluateNewSimplex(original, 1.0, comparator);

        assertEquals(expectedBestPoint, bestPoint);
    }

    /**
     * Test evaluateNewSimplex method with expansion
     */
    @Test
    public void testEvaluateNewSimplexExpansion() throws Exception {
        MultiDirectional optimizer = new MultiDirectional();
        RealPointValuePair[] original = new RealPointValuePair[]{
            new RealPointValuePair(new double[]{0.0, 0.0}, 0.0),
            new RealPointValuePair(new double[]{1.0, 1.0}, 2.0)
        };
        RealPointValuePair expectedBestPoint = new RealPointValuePair(new double[]{2.0, 2.0}, 8.0);

        RealPointValuePair bestPoint = optimizer.evaluateNewSimplex(original, 2.0, comparator);

        assertEquals(expectedBestPoint, bestPoint);
    }

    /**
     * Test evaluateNewSimplex method with contraction
     */
    @Test
    public void testEvaluateNewSimplexContraction() throws Exception {
        MultiDirectional optimizer = new MultiDirectional();
        RealPointValuePair[] original = new RealPointValuePair[]{
            new RealPointValuePair(new double[]{0.0, 0.0}, 0.0),
            new RealPointValuePair(new double[]{1.0, 1.0}, 2.0)
        };
        RealPointValuePair expectedBestPoint = new RealPointValuePair(new double[]{0.5, 0.5}, 0.5);

        RealPointValuePair bestPoint = optimizer.evaluateNewSimplex(original, 0.5, comparator);

        assertEquals(expectedBestPoint, bestPoint);
    }

    /**
     * Test evaluateNewSimplex method with function evaluation exception
     */
    @Test(expected = FunctionEvaluationException.class)
    public void testEvaluateNewSimplexFunctionEvaluationException() throws Exception {
        MultiDirectional optimizer = new MultiDirectional();
        RealPointValuePair[] original = new RealPointValuePair[]{
            new RealPointValuePair(new double[]{0.0, 0.0}, 0.0),
            new RealPointValuePair(new double[]{1.0, 1.0}, 2.0)
        };
        ObjectiveFunction objectiveFunction = () -> {
            throw new FunctionEvaluationException("Function evaluation exception");
        };

        optimizer.setObjectiveFunction(objectiveFunction);

        RealPointValuePair bestPoint = optimizer.evaluateNewSimplex(original, 1.0, comparator);
    }

    /**
     * Test evaluateNewSimplex method with optimization exception
     */
    @Test(expected = OptimizationException.class)
    public void testEvaluateNewSimplexOptimizationException() throws Exception {
        MultiDirectional optimizer = new MultiDirectional();
        RealPointValuePair[] original = new RealPointValuePair[]{
            new RealPointValuePair(new double[]{0.0, 0.0}, 0.0),
            new RealPointValuePair(new double[]{1.0, 1.0}, 2.0)
        };
        ObjectiveFunction objectiveFunction = () -> {
            throw new OptimizationException("Optimization exception");
        };

        optimizer.setObjectiveFunction(objectiveFunction);

        RealPointValuePair bestPoint = optimizer.evaluateNewSimplex(original, 1.0, comparator);
    }

    /**
     * Test evaluateNewSimplex method with illegal argument exception
     */
    @Test(expected = IllegalArgumentException.class)
    public void testEvaluateNewSimplexIllegalArgumentException() throws Exception {
        MultiDirectional optimizer = new MultiDirectional();
        RealPointValuePair[] original = new RealPointValuePair[0];

        optimizer.evaluateNewSimplex(original, 1.0, comparator);
    }

}