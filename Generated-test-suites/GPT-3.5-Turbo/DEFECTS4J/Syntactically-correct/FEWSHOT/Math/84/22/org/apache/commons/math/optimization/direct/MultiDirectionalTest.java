package org.apache.commons.math.optimization.direct;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.Comparator;

import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.optimization.OptimizationException;
import org.apache.commons.math.optimization.RealConvergenceChecker;
import org.apache.commons.math.optimization.RealPointValuePair;
import org.junit.Before;
import org.junit.Test;

public class MultiDirectionalTest {

    private MultiDirectional optimizer;

    @Before
    public void setUp() {
        optimizer = new MultiDirectional();
    }

    @Test
    public void testConstructorWithDefaultCoefficients() {
        assertEquals(2.0, optimizer.khi, 0);
        assertEquals(0.5, optimizer.gamma, 0);
    }

    @Test
    public void testConstructorWithSpecifiedCoefficients() {
        MultiDirectional optimizer = new MultiDirectional(1.0, 0.1);
        assertEquals(1.0, optimizer.khi, 0);
        assertEquals(0.1, optimizer.gamma, 0);
    }

    @Test
    public void testIterateSimplex() throws FunctionEvaluationException, OptimizationException, IllegalArgumentException {
        Comparator<RealPointValuePair> comparator = new Comparator<RealPointValuePair>() {
            public int compare(RealPointValuePair p1, RealPointValuePair p2) {
                return p1.getValue() < p2.getValue() ? -1 : (p1.getValue() > p2.getValue() ? 1 : 0);
            }
        };
        
        // Set up initial simplex
        double[] x1 = {1.0, 2.0};
        double[] x2 = {2.0, 3.0};
        double[] x3 = {3.0, 4.0};
        RealPointValuePair[] simplex = {
            new RealPointValuePair(x1, 0),
            new RealPointValuePair(x2, 0),
            new RealPointValuePair(x3, 0)
        };
        optimizer.simplex = simplex;

        optimizer.iterateSimplex(comparator);

        // Check the result
        RealPointValuePair[] expectedSimplex = {
            new RealPointValuePair(x1, 0),
            new RealPointValuePair(x2, 0),
            new RealPointValuePair(x3, 0)
        };
        assertEquals(expectedSimplex, optimizer.simplex);
    }

    @Test
    public void testEvaluateNewSimplex() throws FunctionEvaluationException, OptimizationException {
        Comparator<RealPointValuePair> comparator = new Comparator<RealPointValuePair>() {
            public int compare(RealPointValuePair p1, RealPointValuePair p2) {
                return p1.getValue() < p2.getValue() ? -1 : (p1.getValue() > p2.getValue() ? 1 : 0);
            }
        };

        // Set up original simplex
        double[] x1 = {1.0, 2.0};
        double[] x2 = {2.0, 3.0};
        double[] x3 = {3.0, 4.0};
        RealPointValuePair[] originalSimplex = {
            new RealPointValuePair(x1, 0),
            new RealPointValuePair(x2, 0),
            new RealPointValuePair(x3, 0)
        };

        double coeff = 0.5;
        
        RealPointValuePair best = optimizer.evaluateNewSimplex(originalSimplex, coeff, comparator);

        // Check the result
        double[] xTransformed1 = {0.5, 1.0};
        double[] xTransformed2 = {1.0, 1.5};
        double[] xTransformed3 = {1.5, 2.0};
        RealPointValuePair[] expectedSimplex = {
            new RealPointValuePair(xTransformed1, Double.NaN, false),
            new RealPointValuePair(xTransformed2, Double.NaN, false),
            new RealPointValuePair(xTransformed3, Double.NaN, false)
        };
        assertEquals(expectedSimplex, optimizer.simplex);

        RealPointValuePair expectedBest = new RealPointValuePair(xTransformed1, Double.NaN, false);
        assertEquals(expectedBest, best);
    }
}