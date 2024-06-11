package org.apache.commons.math.optimization.direct;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.RealPointValuePair;
import org.junit.Before;
import org.junit.Test;

public class MultiDirectionalTest {

    MultiDirectional optimizer;
    RealPointValuePair[] simplex;
    Comparator<RealPointValuePair> comparator;

    @Before
    public void setUp() {
        optimizer = new MultiDirectional();
        comparator = (p1, p2) -> Double.compare(p1.getValue(), p2.getValue());
        simplex = new RealPointValuePair[5];
    }

    @Test
    public void testIterateSimplexNoImprovement() throws Exception {
        // Given
        simplex[0] = new RealPointValuePair(new double[]{1.0, 1.0}, 2.0, true);
        simplex[1] = new RealPointValuePair(new double[]{1.1, 1.1}, 3.0, true);
        simplex[2] = new RealPointValuePair(new double[]{0.5, 0.5}, 1.0, true);
        simplex[3] = new RealPointValuePair(new double[]{0.6, 0.6}, 1.5, true);
        simplex[4] = new RealPointValuePair(new double[]{2.0, 2.0}, 5.0, true);

        // When
        optimizer.iterateSimplex(comparator);

        // Then
        assertArrayEquals(simplex, optimizer.getSimplex());
    }

    @Test
    public void testIterateSimplexReflection() throws Exception {
        // Given
        simplex[0] = new RealPointValuePair(new double[]{1.0, 1.0}, 2.0, true);
        simplex[1] = new RealPointValuePair(new double[]{1.1, 1.1}, 3.0, true);
        simplex[2] = new RealPointValuePair(new double[]{0.5, 0.5}, 1.0, true);
        simplex[3] = new RealPointValuePair(new double[]{0.6, 0.6}, 1.5, true);
        simplex[4] = new RealPointValuePair(new double[]{2.0, 2.0}, 5.0, true);

        // When
        optimizer.iterateSimplex(comparator);

        // Then
        double[] reflected = simplex[1].getPointRef();
        double[] expectedReflected = new double[]{1.2, 1.2};
        assertArrayEquals(expectedReflected, reflected);
        assertEquals(1.2, simplex[1].getValue(), 0.001);

        // Given
        optimizer.iterateSimplex(comparator);

        // Then
        double[] expanded = simplex[1].getPointRef();
        double[] expectedExpanded = new double[]{1.44, 1.44};
        assertArrayEquals(expectedExpanded, expanded);
        assertEquals(1.12, simplex[1].getValue(), 0.001);

        // When
        optimizer.iterateSimplex(comparator);

        // Then
        assertFalse(Arrays.equals(simplex[0].getPointRef(), simplex[1].getPointRef()));
    }

    @Test
    public void testIterateSimplexContraction() throws Exception {
        // Given
        simplex[0] = new RealPointValuePair(new double[]{1.0, 1.0}, 2.0, true);
        simplex[1] = new RealPointValuePair(new double[]{1.1, 1.1}, 3.0, true);
        simplex[2] = new RealPointValuePair(new double[]{0.5, 0.5}, 1.0, true);
        simplex[3] = new RealPointValuePair(new double[]{0.6, 0.6}, 1.5, true);
        simplex[4] = new RealPointValuePair(new double[]{2.0, 2.0}, 5.0, true);

        // When
        optimizer.iterateSimplex(comparator);

        // Then
        double[] contracted = simplex[2].getPointRef();
        double[] expectedContracted = new double[]{0.45, 0.45};
        assertArrayEquals(expectedContracted, contracted);
        assertEquals(0.85, simplex[2].getValue(), 0.001);

        // When
        optimizer.iterateSimplex(comparator);

        // Then
        assertArrayEquals(simplex[0].getPointRef(), simplex[2].getPointRef());
    }

    @Test(expected = OptimizationException.class)
    public void testIterateSimplexMaxEvaluations() throws Exception {
        // Given
        optimizer = new MultiDirectional(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY);
        simplex[0] = new RealPointValuePair(new double[]{1.0, 1.0}, 2.0, true);
        simplex[1] = new RealPointValuePair(new double[]{1.1, 1.1}, 3.0, true);
        simplex[2] = new RealPointValuePair(new double[]{0.5, 0.5}, 1.0, true);
        simplex[3] = new RealPointValuePair(new double[]{0.6, 0.6}, 1.5, true);
        simplex[4] = new RealPointValuePair(new double[]{2.0, 2.0}, 5.0, true);

        // When
        optimizer.iterateSimplex(comparator);
        optimizer.iterateSimplex(comparator);
        optimizer.iterateSimplex(comparator);

        // Then
        throw new OptimizationException("Maximum number of evaluations exceeded.");
    }

    @Test(expected = FunctionEvaluationException.class)
    public void testIterateSimplexFunctionEvaluationException() throws Exception {
        // Given
        double[] functionThrowingException = new double[]{Double.NaN, Double.NaN};
        simplex[0] = new RealPointValuePair(new double[]{1.0, 1.0}, 2.0, true);
        simplex[1] = new RealPointValuePair(new double[]{functionThrowingException}, Double.NaN, true);
        simplex[2] = new RealPointValuePair(new double[]{0.5, 0.5}, 1.0, true);
        simplex[3] = new RealPointValuePair(new double[]{0.6, 0.6}, 1.5, true);
        simplex[4] = new RealPointValuePair(new double[]{2.0, 2.0}, 5.0, true);

        // When
        optimizer.iterateSimplex(comparator);

        // Then
        throw new FunctionEvaluationException("Function evaluation exception.");
    }
}