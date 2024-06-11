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

    private MultiDirectional multiDirectional;

    @Before
    public void setUp() {
        multiDirectional = new MultiDirectional();
    }

    @Test
    public void testIterateSimplex() throws FunctionEvaluationException, OptimizationException, IllegalArgumentException {
        RealPointValuePair[] original = new RealPointValuePair[3];
        original[0] = new RealPointValuePair(new double[]{1.0, 2.0, 3.0}, Double.NaN);
        original[1] = new RealPointValuePair(new double[]{4.0, 5.0, 6.0}, Double.NaN);
        original[2] = new RealPointValuePair(new double[]{7.0, 8.0, 9.0}, Double.NaN);

        Comparator<RealPointValuePair> comparator = new Comparator<RealPointValuePair>() {
            @Override
            public int compare(RealPointValuePair o1, RealPointValuePair o2) {
                return Double.compare(o1.getValue(), o2.getValue());
            }
        };

        multiDirectional.iterateSimplex(comparator);

        RealPointValuePair[] expected = new RealPointValuePair[3];
        expected[0] = new RealPointValuePair(new double[]{1.0, 2.0, 3.0}, Double.NaN);
        expected[1] = new RealPointValuePair(new double[]{4.0, 5.0, 6.0}, Double.NaN);
        expected[2] = new RealPointValuePair(new double[]{7.0, 8.0, 9.0}, Double.NaN);

        for (int i = 0; i < original.length; i++) {
            assertEquals(expected[i].getValue(), original[i].getValue(), 0.0001);
        }
    }

    @Test
    public void testEvaluateNewSimplex() throws FunctionEvaluationException, OptimizationException {
        RealPointValuePair[] original = new RealPointValuePair[3];
        original[0] = new RealPointValuePair(new double[]{1.0, 2.0, 3.0}, Double.NaN);
        original[1] = new RealPointValuePair(new double[]{4.0, 5.0, 6.0}, Double.NaN);
        original[2] = new RealPointValuePair(new double[]{7.0, 8.0, 9.0}, Double.NaN);

        double coeff = 2.0;

        Comparator<RealPointValuePair> comparator = new Comparator<RealPointValuePair>() {
            @Override
            public int compare(RealPointValuePair o1, RealPointValuePair o2) {
                return Double.compare(o1.getValue(), o2.getValue());
            }
        };

        RealPointValuePair result = multiDirectional.evaluateNewSimplex(original, coeff, comparator);
        RealPointValuePair expected = new RealPointValuePair(new double[]{-3.0, -6.0, -9.0}, Double.NaN);

        assertEquals(expected.getValue(), result.getValue(), 0.0001);
        for (int i = 0; i < expected.getPoint().length; i++) {
            assertEquals(expected.getPoint()[i], result.getPoint()[i], 0.0001);
        }
    }
}