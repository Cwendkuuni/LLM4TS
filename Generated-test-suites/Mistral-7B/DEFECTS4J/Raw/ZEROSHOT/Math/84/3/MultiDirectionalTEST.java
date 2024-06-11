package org.apache.commons.math.optimization.direct;

import static org.junit.Assert.*;

import org.apache.commons.math3.exception.MaxEvalExceededException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.RealPointValuePair;
import org.apache.commons.math3.optim.SimpleBounds;
import org.apache.commons.math3.optim.nonlinear.function.LinearFunction;
import org.apache.commons.math3.optim.nonlinear.function.PolynomialFunction;
import org.apache.commons.math3.optim.nonlinear.function.UnivariateFunction;
import org.apache.commons.math3.optim.nonlinear.simplextools.SimplexPoint;
import org.apache.commons.math3.optim.nonlinear.vector.go.RealVectorValueFunction;
import org.apache.commons.math3.optim.nonlinear.vector.go.SimplexSearch;
import org.apache.commons.math3.optim.nonlinear.vector.go.SimplexTabuSearch;
import org.apache.commons.math3.optim.point.PointValuePairList;
import org.apache.commons.math3.optim.point.RealPointValuePairList;
import org.apache.commons.math3.optim.convergence.ConvergenceChecker;
import org.apache.commons.math3.optim.convergence.StallWarning;
import org.apache.commons.math3.optim.initialization.SimplexStartingPoint;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MultiDirectionalTest {

    /**
     * Test method for {@link MultiDirectional#MultiDirectional()}.
     */
    @org.junit.Test
    public void testDefaultConstructor() {
        MultiDirectional optimizer = new MultiDirectional();
        assertNotNull(optimizer);
        assertEquals(2.0, optimizer.khi, 0.0);
        assertEquals(0.5, optimizer.gamma, 0.0);
    }

    /**
     * Test method for {@link MultiDirectional#MultiDirectional(double, double)}.
     */
    @org.junit.Test
    public void testCustomConstructor() {
        MultiDirectional optimizer = new MultiDirectional(1.5, 0.3);
        assertNotNull(optimizer);
        assertEquals(1.5, optimizer.khi, 0.0);
        assertEquals(0.3, optimizer.gamma, 0.0);
    }

    /**
     * Test method for {@link MultiDirectional#iterateSimplex(Comparator)}.
     */
    @org.junit.Test(expected = FunctionEvaluationException.class)
    public void testIterateSimplexFunctionEvaluationException() throws Exception {
        MultiDirectional optimizer = new MultiDirectional();
        UnivariateFunction function = new PolynomialFunction(new double[] {-1.0}, new double[] {1.0}, 2);
        SimpleBounds bounds = new SimpleBounds(-1.0, 1.0);
        optimizer.optimize(function, bounds);
    }

    /**
     * Test method for {@link MultiDirectional#iterateSimplex(Comparator)}.
     */
    @org.junit.Test(expected = OptimizationException.class)
    public void testIterateSimplexOptimizationException() throws Exception {
        MultiDirectional optimizer = new MultiDirectional();
        UnivariateFunction function = new LinearFunction(new double[] {0.0}, new double[] {1.0}, 0);
        SimpleBounds bounds = new SimpleBounds(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
        optimizer.setMaxIterations(1);
        optimizer.optimize(function, bounds);
    }

    /**
     * Test method for {@link MultiDirectional#iterateSimplex(Comparator)}.
     */
    @org.junit.Test
    public void testIterateSimplexNoImprovement() throws Exception {
        MultiDirectional optimizer = new MultiDirectional();
        UnivariateFunction function = new PolynomialFunction(new double[] {0.0}, new double[] {1.0}, 0);
        SimpleBounds bounds = new SimpleBounds(-1.0, 1.0);
        optimizer.optimize(function, bounds);
        PointValuePair best = optimizer.getBestPoint();
        assertEquals(0.0, best.getPoint()[0], 0.0);
    }

    /**
     * Test method for {@link MultiDirectional#iterateSimplex(Comparator)}.
     */
    @org.junit.Test
    public void testIterateSimplexSingleVariable() throws Exception {
        MultiDirectional optimizer = new MultiDirectional();
        UnivariateFunction function = new PolynomialFunction(new double[] {0.0}, new double[] {1.0}, 0);
        SimpleBounds bounds = new SimpleBounds(-1.0, 1.0);
        optimizer.optimize(function, bounds);
        PointValuePair best = optimizer.getBestPoint();
        assertEquals(0.0, best.getPoint()[0], 0.0);
        assertEquals(0.0, best.getValue(), 0.0);
    }

    /**
     * Test method for {@link MultiDirectional#iterateSimplex(Comparator)}.
     */
    @org.junit.Test
    public void testIterateSimplexMultipleVariables() throws Exception {
        MultiDirectional optimizer = new MultiDirectional();
        UnivariateFunction[] functions = new UnivariateFunction[2];
        for (int i = 0; i < 2; i++) {
            functions[i] = new PolynomialFunction(new double[] {i}, new double[] {1.0}, 2);
        }
        SimpleBounds bounds = new SimpleBounds(-1.0, 1.0);
        optimizer.optimize(functions, bounds);
        PointValuePair best = optimizer.getBestPoint();
        double[] expectedBest = new double[2];
        expectedBest[0] = 0.5;
        expectedBest[1] = 0.5;
        assertArrayEquals(expectedBest, best.getPoint(), 0.0);
        double expectedValue = 0.25;
        assertEquals(expectedValue, best.getValue(), 0.0);
    }

    /**
     * Test method for {@link MultiDirectional#iterateSimplex(Comparator)}.
     */
    @org.junit.Test
    public void testIterateSimplexConvergence() throws Exception {
        MultiDirectional optimizer = new MultiDirectional();
        UnivariateFunction function = new PolynomialFunction(new double[] {0.0}, new double[] {1.0}, 0);
        SimpleBounds bounds = new SimpleBounds(-1.0, 1.0);
        RealConvergenceChecker convergenceChecker = new RealConvergenceChecker(1e-6);
        SimplexTabuSearch tabuSearch = new SimplexTabuSearch(function, bounds, new SimplexPoint(new double[] {0.0}),
                convergenceChecker);
        optimizer.setMaxIterations(100);
        optimizer.setConvergenceChecker(convergenceChecker);
        optimizer.optimize(function, bounds);
        PointValuePair best = optimizer.getBestPoint();
        assertEquals(0.0, best.getPoint()[0], 0.0);
        assertEquals(0.0, best.getValue(), 0.0);
        assertTrue(convergenceChecker.isConverged());
        assertFalse(tabuSearch.isConverged());
    }

    /**
     * Test method for {@link MultiDirectional#iterateSimplex(Comparator)}.
     */
    @org.junit.Test
    public void testIterateSimplexStallWarning() throws Exception {
        MultiDirectional optimizer = new MultiDirectional();
        UnivariateFunction function = new PolynomialFunction(new double[] {0.0}, new double[] {1.0}, 0);
        SimpleBounds bounds = new SimpleBounds(-1.0, 1.0);
        SimplexSearch simplexSearch = new SimplexSearch(function, bounds, new SimplexPoint(new double[] {0.0}));
        optimizer.setMaxIterations(10);
        optimizer.setConvergenceChecker(new ConvergenceChecker() {
            @Override
            public boolean isConverged(RealPointValuePairList currentPoint, RealPointValuePairList previousPoint,
                                       int iterationNumber) {
                if (iterationNumber >= 8) {
                    throw new StallWarning();
                }
                return false;
            }
        });
        optimizer.optimize(function, bounds);
        PointValuePair best = optimizer.getBestPoint();
        assertEquals(0.0, best.getPoint()[0], 0.0);
        assertEquals(0.0, best.getValue(), 0.0);
    }

    /**
     * Test method for {@link MultiDirectional#iterateSimplex(Comparator)}.
     */
    @org.junit.Test
    public void testIterateSimplexMaxEvalExceededException() throws Exception {
        MultiDirectional optimizer = new MultiDirectional();
        UnivariateFunction function = new PolynomialFunction(new double[] {0.0}, new double[] {1.0}, 0);
        SimpleBounds bounds = new SimpleBounds(-1.0, 1.0);
        optimizer.setMaxIterations(1);
        optimizer.optimize(function, bounds);
        PointValuePair best = optimizer.getBestPoint();
        assertNull(best);
    }

    /**
     * Test method for {@link MultiDirectional#iterateSimplex(Comparator)}.
     */
    @org.junit.Test
    public void testIterateSimplexNumberIsTooSmallException() throws Exception {
        MultiDirectional optimizer = new MultiDirectional();
        UnivariateFunction function = new PolynomialFunction(new double[] {0.0}, new double[] {1.0}, 0);
        SimpleBounds bounds = new SimpleBounds(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
        optimizer.optimize(function, bounds);
        PointValuePair best = optimizer.getBestPoint();
        assertNull(best);
    }

    /**
     * Test method for {@link MultiDirectional#evaluateNewSimplex(RealPointValuePair[], double, Comparator)}.
     */
    @org.junit.Test(expected = FunctionEvaluationException.class)
    public void testEvaluateNewSimplexFunctionEvaluationException() throws Exception {
        MultiDirectional optimizer = new MultiDirectional();
        RealVectorValueFunction[] functions = new RealVectorValueFunction[2];
        for (int i = 0; i < 2; i++) {
            functions[i] = new PolynomialFunction(new double[] {i}, new double[] {1.0}, 3);
        }
        RealPointValuePair[] originals = new RealPointValuePair[3];
        originals[0] = new RealPointValuePair(new double[] {0.0, 0.0}, 0.0);
        originals[1] = new RealPointValuePair(new double[] {1.0, 1.0}, 4.0);
        originals[2] = new RealPointValuePair(new double[] {-1.0, -1.0}, 9.0);
        SimplexPoint startingPoint = new SimplexPoint(originals);
        optimizer.optimize(functions, new SimpleBounds(startingPoint.getBounds()), startingPoint);
        optimizer.evaluateNewSimplex(originals, 1.0, new Comparator<RealPointValuePair>() {
            @Override
            public int compare(RealPointValuePair o1, RealPointValuePair o2) {
                return (int) (Double.compare(o1.getValue(), o2.getValue()));
            }
        });
    }

    /**
     * Test method for {@link MultiDirectional#evaluateNewSimplex(RealPointValuePair[], double, Comparator)}.
     */
    @org.junit.Test
    public void testEvaluateNewSimplex() throws Exception {
        MultiDirectional optimizer = new MultiDirectional();
        RealVectorValueFunction[] functions = new RealVectorValueFunction[2];
        for (int i = 0; i < 2; i++) {
            functions[i] = new PolynomialFunction(new double[] {i}, new double[] {1.0}, 0);
        }
        RealPointValuePair[] originals = new RealPointValuePair[3];
        originals[0] = new RealPointValuePair(new double[] {0.0, 0.0}, 0.0);
        originals[1] = new RealPointValuePair(new double[] {1.0, 1.0}, 4.0);
        originals[2] = new RealPointValuePair(new double[] {-1.0, -1.0}, 9.0);
        SimplexPoint startingPoint = new SimplexPoint(originals);
        optimizer.optimize(functions, new SimpleBounds(startingPoint.getBounds()), startingPoint);
        RealPointValuePair reflected = optimizer.evaluateNewSimplex(originals, 1.0, new Comparator<RealPointValuePair>() {
            @Override
            public int compare(RealPointValuePair o1, RealPointValuePair o2) {
                return (int) (Double.compare(o1.getValue(), o2.getValue()));
            }
        });
        PointValuePair expectedReflected = new PointValuePair(new double[] {0.5, 0.5}, 1.0);
        assertEquals(expectedReflected.getPoint()[0], reflected.getPoint()[0], 0.0);
        assertEquals(expectedReflected.getPoint()[1], reflected.getPoint()[1], 0.0);
        assertEquals(expectedReflected.getValue(), reflected.getValue(), 0.0);
    }

}