package org.apache.commons.math.optimization.direct;

import static org.junit.Assert.assertEquals;

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
    public void testIterateSimplex() throws FunctionEvaluationException, OptimizationException, IllegalArgumentException {
        // Create a comparator for testing
        Comparator<RealPointValuePair> comparator = new Comparator<RealPointValuePair>() {
            public int compare(RealPointValuePair o1, RealPointValuePair o2) {
                return Double.compare(o1.getValue(), o2.getValue());
            }
        };

        // Create the simplex
        RealPointValuePair[] simplex = new RealPointValuePair[2];
        simplex[0] = new RealPointValuePair(new double[] {0.5, 0.0}, 1.0);
        simplex[1] = new RealPointValuePair(new double[] {0.0, 0.5}, 2.0);

        // Set the simplex for optimization
        optimizer.setSimplex(simplex);

        // Perform iteration on the simplex
        optimizer.iterateSimplex(comparator);

        // Check the result
        RealPointValuePair[] resultSimplex = optimizer.getSimplex();
        assertEquals(2, resultSimplex.length);
        assertEquals(0.0, resultSimplex[0].getPointRef()[0], 0.0);
        assertEquals(0.0, resultSimplex[0].getPointRef()[1], 0.0);
        assertEquals(1.0, resultSimplex[0].getValue(), 0.0);
        assertEquals(0.25, resultSimplex[1].getPointRef()[0], 0.0);
        assertEquals(0.25, resultSimplex[1].getPointRef()[1], 0.0);
        assertEquals(0.625, resultSimplex[1].getValue(), 0.0);
    }

    @Test
    public void testEvaluateNewSimplex() throws FunctionEvaluationException, OptimizationException {
        // Create a comparator for testing
        Comparator<RealPointValuePair> comparator = new Comparator<RealPointValuePair>() {
            public int compare(RealPointValuePair o1, RealPointValuePair o2) {
                return Double.compare(o1.getValue(), o2.getValue());
            }
        };
        
        // Create the original simplex
        RealPointValuePair[] original = new RealPointValuePair[4];
        original[0] = new RealPointValuePair(new double[] {0.5, 0.0}, 1.0);
        original[1] = new RealPointValuePair(new double[] {0.0, 0.5}, 2.0);
        original[2] = new RealPointValuePair(new double[] {-0.5, 0.0}, 3.0);
        original[3] = new RealPointValuePair(new double[] {0.0, -0.5}, 4.0);

        // Set the original simplex for evaluation
        optimizer.setSimplex(original);

        // Evaluate the new simplex
        RealPointValuePair result = optimizer.evaluateNewSimplex(original, 0.5, comparator);

        // Check the result
        assertEquals(1.25, result.getPointRef()[0], 0.0);
        assertEquals(1.25, result.getPointRef()[1], 0.0);
        assertEquals(1.53125, result.getValue(), 0.0);
    }
}