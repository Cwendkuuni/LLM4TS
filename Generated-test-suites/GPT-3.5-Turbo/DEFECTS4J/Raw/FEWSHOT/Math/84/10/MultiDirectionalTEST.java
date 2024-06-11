package org.apache.commons.math.optimization.direct;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

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
    public void testConstructor1() {
        assertEquals(2.0, optimizer.khi, 0.0);
        assertEquals(0.5, optimizer.gamma, 0.0);
    }

    @Test
    public void testConstructor2() {
        MultiDirectional customOptimizer = new MultiDirectional(3.0, 0.2);
        assertEquals(3.0, customOptimizer.khi, 0.0);
        assertEquals(0.2, customOptimizer.gamma, 0.0);
    }

    @Test
    public void testIterateSimplex() throws FunctionEvaluationException, OptimizationException, IllegalArgumentException {
        optimizer.simplex = new RealPointValuePair[3];
        optimizer.simplex[0] = new RealPointValuePair(new double[] { 0.0, 0.0 }, 0.0, false);
        optimizer.simplex[1] = new RealPointValuePair(new double[] { 1.0, 0.0 }, 0.1, false);
        optimizer.simplex[2] = new RealPointValuePair(new double[] { 0.0, 1.0 }, 0.2, false);

        Comparator<RealPointValuePair> comparator = new Comparator<RealPointValuePair>() {
            public int compare(RealPointValuePair o1, RealPointValuePair o2) {
                return Double.compare(o1.getValue(), o2.getValue());
            }
        };

        optimizer.iterateSimplex(comparator);

        assertNotNull(optimizer.simplex);
        assertEquals(3, optimizer.simplex.length);
    }

    @Test
    public void testEvaluateNewSimplex() throws FunctionEvaluationException, OptimizationException {
        optimizer.simplex = new RealPointValuePair[3];
        optimizer.simplex[0] = new RealPointValuePair(new double[] { 0.0, 0.0 }, 0.0, false);
        optimizer.simplex[1] = new RealPointValuePair(new double[] { 1.0, 0.0 }, 0.1, false);
        optimizer.simplex[2] = new RealPointValuePair(new double[] { 0.0, 1.0 }, 0.2, false);

        Comparator<RealPointValuePair> comparator = new Comparator<RealPointValuePair>() {
            public int compare(RealPointValuePair o1, RealPointValuePair o2) {
                return Double.compare(o1.getValue(), o2.getValue());
            }
        };

        RealPointValuePair newSimplex = optimizer.evaluateNewSimplex(optimizer.simplex, 0.5, comparator);

        assertNotNull(newSimplex);
        assertNotNull(optimizer.simplex);
        assertEquals(3, optimizer.simplex.length);
        assertTrue(Double.isNaN(newSimplex.getValue()));
    }
}