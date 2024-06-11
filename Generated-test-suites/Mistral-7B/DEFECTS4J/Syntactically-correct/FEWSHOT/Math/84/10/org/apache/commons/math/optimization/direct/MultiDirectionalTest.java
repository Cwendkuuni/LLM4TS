package org.apache.commons.math.optimization.direct;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.RealPointValuePair;
import org.junit.Test;

public class MultiDirectionalTest {

    @Test
    public void testMultiDirectionalDefaultConstructor() {
        MultiDirectional optimizer = new MultiDirectional();
        assertEquals(2.0, optimizer.khi, 0.001);
        assertEquals(0.5, optimizer.gamma, 0.001);
    }

    @Test
    public void testMultiDirectionalCustomConstructor() {
        MultiDirectional optimizer = new MultiDirectional(3.0, 0.6);
        assertEquals(3.0, optimizer.khi, 0.001);
        assertEquals(0.6, optimizer.gamma, 0.001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMultiDirectionalNegativeKhi() {
        new MultiDirectional(-1.5, 0.6);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMultiDirectionalNegativeGamma() {
        new MultiDirectional(2.1, -0.5);
    }

    @Test
    public void testIterateSimplex() throws Exception {
        MultiDirectional optimizer = new MultiDirectional(1.5, 0.7);
        double[][] testFunctions = {
                {-2.0, -2.0},
                {-1.0, -1.0},
                {-0.5, -0.5},
                {0.0, 0.0},
                {1.0, 1.0}
        };

        PointValuePair[] expected = new PointValuePair[testFunctions.length];
        for (int i = 0; i < testFunctions.length; i++) {
            expected[i] = new RealPointValuePair(testFunctions[i], Math.pow(-i, 2.0));
        }

        optimizer.setMaxEvaluations(testFunctions.length * 10);
        optimizer.optimize(new SimpleFunction(-1.0, 1.0), testFunctions, new IdentityComparator());

        assertArrayEquals(expected, optimizer.getSolutionPoints());
    }

    private static class SimpleFunction implements org.apache.commons.math3.optim.UnivariateFunction {

        private final double lowerBound;
        private final double upperBound;

        public SimpleFunction(double lowerBound, double upperBound) {
            this.lowerBound = lowerBound;
            this.upperBound = upperBound;
        }

        @Override
        public double value(double point) throws FunctionEvaluationException {
            if (point < lowerBound || point > upperBound) {
                throw new FunctionEvaluationException("Point out of bounds");
            }
            return Math.pow(point, 2.0);
        }

        @Override
        public double value(double[] point) throws FunctionEvaluationException {
            return value(point[0]);
        }

        @Override
        public int getNumberOfVariables() {
            return 1;
        }
    }

    private static class IdentityComparator implements Comparator<PointValuePair> {

        @Override
        public int compare(PointValuePair o1, PointValuePair o2) {
            return Double.compare(o1.getValue(), o2.getValue());
        }
    }

}