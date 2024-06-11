package org.apache.commons.math.optimization.direct;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;

import java.util.Arrays;
import java.util.Comparator;

import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.MaxEvalExceededException;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleBounds;
import org.apache.commons.math3.optim.RealVector;
import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.GoalType;
import org.apache.commons.math3.optim.NonSmoothedPointValuesProvider;
import org.apache.commons.math3.optim.PointValuesProvider;
import org.apache.commons.math3.optim.RealVectorValuePair;
import org.apache.commons.math3.optim.NonSmoothedPointValues;
import org.apache.commons.math3.optim.InitialGuess;
import org.junit.Test;

/**
 * JUnit test for MultiDirectional class
 * @author [Your Name]
 */
public class MultiDirectionalTest {

    /** MultiDirectional instance for testing */
    private MultiDirectional multiDirectional;

    /** Test function for optimization */
    private double testFunction(Double[] x) {
        double result = 0.0;
        for (int i = 0; i < x.length; i++) {
            result += Math.pow(x[i] - 2.0, 2.0);
        }
        return result;
    }

    /** Set up test environment */
    @Before
    public void setUp() {
        this.multiDirectional = new MultiDirectional();
    }

    /** Test constructor with default coefficients */
    @Test
    public void testMultiDirectionalDefaultConstructor() {
        assertNotNull(this.multiDirectional);
        assertEquals(2.0, this.multiDirectional.khi, 0.001);
        assertEquals(0.5, this.multiDirectional.gamma, 0.001);
    }

    /** Test constructor with specified coefficients */
    @Test
    public void testMultiDirectionalCustomConstructor() {
        MultiDirectional multiDirectionalWithCustomParams = new MultiDirectional(3.0, 0.7);
        assertEquals(3.0, multiDirectionalWithCustomParams.khi, 0.001);
        assertEquals(0.7, multiDirectionalWithCustomParams.gamma, 0.001);
    }

    /** Test iterateSimplex method with default comparator */
    @Test
    public void testIterateSimplexDefaultComparator()
        throws MaxEvalExceededException, ConvergenceException, FunctionEvaluationException {
        Double[] initialPoints = {1.0, 1.0, 1.0};
        RealVector initialVector = new ArrayRealVector(initialPoints);
        SimpleBounds bounds = new SimpleBounds(new ArrayRealVector(initialPoints), new ArrayRealVector(initialPoints));
        this.multiDirectional.optimize(testFunction, bounds, new SimpleGoalType(), new MaxEval(100), new NonSmoothedPointValuesProvider());
        PointValuePair bestPoint = this.multiDirectional.getBestPoint();
        assertEquals(new ArrayRealVector(new double[] {-1.0, -1.0, -1.0}), bestPoint.getPoint());
    }

    /** Test iterateSimplex method with custom comparator */
    @Test
    public void testIterateSimplexCustomComparator()
        throws MaxEvalExceededException, ConvergenceException, FunctionEvaluationException, IllegalArgumentException {
        Double[] initialPoints = {1.0, 1.0, 1.0};
        RealVector initialVector = new ArrayRealVector(initialPoints);
        SimpleBounds bounds = new SimpleBounds(new ArrayRealVector(initialPoints), new ArrayRealVector(initialPoints));
        final Comparator<PointValuePair> comparator = new Comparator<PointValuePair>() {
            @Override
            public int compare(PointValuePair o1, PointValuePair o2) {
                return o1.getValue().compareTo(o2.getValue());
            }
        };
        this.multiDirectional.optimize(testFunction, bounds, new SimpleGoalType(), new MaxEval(100), new NonSmoothedPointValuesProvider(), comparator);
        PointValuePair bestPoint = this.multiDirectional.getBestPoint();
        assertEquals(new ArrayRealVector(new double[] {-1.0, -1.0, -1.0}), bestPoint.getPoint());
    }

    /** Test evaluateNewSimplex method with original simplex and reflection step */
    @Test
    public void testEvaluateNewSimplexReflectionStep()
        throws FunctionEvaluationException, OptimizationException {
        Double[] originalSimplex = {1.0, 1.0, 1.0};
        RealPointValuePair[] simplex = new RealPointValuePair[3];
        for (int i = 0; i < 3; i++) {
            simplex[i] = new RealPointValuePair(new ArrayRealVector(originalSimplex), Double.NaN, false);
        }
        Comparator<RealPointValuePair> comparator = new Comparator<RealPointValuePair>() {
            @Override
            public int compare(RealPointValuePair o1, RealPointValuePair o2) {
                return o1.compareTo(o2);
            }
        };
        RealPointValuePair reflectedPoint = this.multiDirectional.evaluateNewSimplex(simplex, 1.0, comparator);
        assertEquals(-1.0, reflectedPoint.getPointRef()[0], 0.001);
        assertEquals(-1.0, reflectedPoint.getPointRef()[1], 0.001);
        assertEquals(-1.0, reflectedPoint.getPointRef()[2], 0.001);
    }

    /** Test evaluateNewSimplex method with original simplex and expansion step */
    @Test
    public void testEvaluateNewSimplexExpansionStep()
        throws FunctionEvaluationException, OptimizationException {
        Double[] originalSimplex = {1.0, 1.0, 1.0};
        RealPointValuePair[] simplex = new RealPointValuePair[3];
        for (int i = 0; i < 3; i++) {
            simplex[i] = new RealPointValuePair(new ArrayRealVector(originalSimplex), Double.NaN, false);
        }
        Comparator<RealPointValuePair> comparator = new Comparator<RealPointValuePair>() {
            @Override
            public int compare(RealPointValuePair o1, RealPointValuePair o2) {
                return o1.compareTo(o2);
            }
        };
        RealPointValuePair expandedPoint = this.multiDirectional.evaluateNewSimplex(simplex, 2.0, comparator);
        assertFalse(expandedPoint.isFeasible());
    }

    /** Test evaluateNewSimplex method with original simplex and contraction step */
    @Test
    public void testEvaluateNewSimplexContractionStep()
        throws FunctionEvaluationException, OptimizationException {
        Double[] originalSimplex = {1.0, 1.0, 1.0};
        RealPointValuePair[] simplex = new RealPointValuePair[3];
        for (int i = 0; i < 3; i++) {
            simplex[i] = new RealPointValuePair(new ArrayRealVector(originalSimplex), Double.NaN, false);
        }
        Comparator<RealPointValuePair> comparator = new Comparator<RealPointValuePair>() {
            @Override
            public int compare(RealPointValuePair o1, RealPointValuePair o2) {
                return o1.compareTo(o2);
            }
        };
        RealPointValuePair contractedPoint = this.multiDirectional.evaluateNewSimplex(simplex, 0.5, comparator);
        assertArrayEquals(originalSimplex, contractedPoint.getPointRef(), 0.001);
    }

}