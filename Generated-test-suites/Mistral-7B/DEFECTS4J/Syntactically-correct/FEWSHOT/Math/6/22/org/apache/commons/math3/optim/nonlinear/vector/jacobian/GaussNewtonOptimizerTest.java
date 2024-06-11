package org.apache.commons.math3.optimization.general;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.optim.PointValuePair;
import org.junit.Test;

public class GaussNewtonOptimizerTest {

    @Test
    public void testGaussNewtonOptimizer() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(true, new SimpleVectorValueChecker());
        assertNull(optimizer.getPoint());
        assertEquals(0, optimizer.getCost(), 0.0);

        double[] data = {1.0, 2.0, 3.0};
        double[] expectedResult = {1.5, 2.5, 3.5};
        optimizer.setFunction(new QuadraticFunction(data));
        optimizer.setStartPoint(new ArrayRealVector(expectedResult));
        optimizer.optimize();

        RealVector actualPoint = optimizer.getPoint();
        assertArrayEquals(expectedResult, actualPoint.toArray());
        assertEquals(0, optimizer.getCost(), 0.0);
    }

    @Test
    public void testGaussNewtonOptimizerLUDecomposition() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(true, new SimpleVectorValueChecker());
        double[] data = {1.0, 2.0, 3.0};
        double[] expectedResult = {1.5, 2.5, 3.5};
        optimizer.setFunction(new QuadraticFunction(data));
        optimizer.setStartPoint(new ArrayRealVector(new double[3]));

        RealMatrix weight = new BlockRealMatrix(new double[][]{{1.0, 0.0, 0.0}, {0.0, 1.0, 0.0}, {0.0, 0.0, 1.0}});
        optimizer.setWeight(weight);

        optimizer.optimize();

        RealVector actualPoint = optimizer.getPoint();
        assertArrayEquals(expectedResult, actualPoint.toArray());
        assertEquals(0, optimizer.getCost(), 0.0);
    }

    @Test
    public void testGaussNewtonOptimizerQRDecomposition() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(false, new SimpleVectorValueChecker());
        double[] data = {1.0, 2.0, 3.0};
        double[] expectedResult = {1.5, 2.5, 3.5};
        optimizer.setFunction(new QuadraticFunction(data));
        optimizer.setStartPoint(new ArrayRealVector(new double[3]));

        RealMatrix weight = new BlockRealMatrix(new double[][]{{1.0, 0.0, 0.0}, {0.0, 1.0, 0.0}, {0.0, 0.0, 1.0}});
        optimizer.setWeight(weight);

        optimizer.optimize();

        RealVector actualPoint = optimizer.getPoint();
        assertArrayEquals(expectedResult, actualPoint.toArray());
        assertEquals(0, optimizer.getCost(), 0.0);
    }

    @Test(expected = NullArgumentException.class)
    public void testNullConvergenceChecker() {
        new GaussNewtonOptimizer(true, null);
    }

    @Test(expected = NullArgumentException.class)
    public void testNullFunction() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(true, new SimpleVectorValueChecker());
        optimizer.setFunction(null);
    }

    @Test(expected = NullArgumentException.class)
    public void testNullStartPoint() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(true, new SimpleVectorValueChecker());
        optimizer.setStartPoint(null);
    }

    @Test(expected = NullArgumentException.class)
    public void testNullWeight() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(true, new SimpleVectorValueChecker());
        optimizer.setWeight(null);
    }

    @Test
    public void testConvergenceChecker() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(true, new SimpleVectorValueChecker());
        optimizer.setFunction(new QuadraticFunction(new double[0]));
        optimizer.setStartPoint(new ArrayRealVector(new double[0]));
        optimizer.setWeight(new ArrayRealVector(new double[0]));
        optimizer.setConvergenceChecker(new CustomConvergenceChecker(0.001));

        optimizer.optimize();

        double[] actualPoint = optimizer.getPoint().toArray();
        assertEquals(0, actualPoint.length);
        assertEquals(0, optimizer.getCost(), 0.0);
    }

    private static class CustomConvergenceChecker implements ConvergenceChecker<PointValuePair> {
        private final double tolerance;

        public CustomConvergenceChecker(double tolerance) {
            this.tolerance = tolerance;
        }

        @Override
        public boolean converged(int iteration, PointValuePair previous, PointValuePair current) {
            double[] prevPoint = previous.getPoint().toArray();
            double[] currPoint = current.getPoint().toArray();
            double prevCost = previous.getValue();
            double currCost = current.getValue();

            double maxDistance = 0;
            for (int i = 0; i < prevPoint.length; i++) {
                double diff = Math.abs(prevPoint[i] - currPoint[i]);
                maxDistance = Math.max(maxDistance, diff);
            }

            return maxDistance < tolerance && Math.abs(prevCost - currCost) < tolerance;
        }
    }

    private static class QuadraticFunction implements Function {
        private final double[] data;

        public QuadraticFunction(double[] data) {
            this.data = data;
        }

        @Override
        public double value(RealVector x) {
            double sum = 0;
            for (int i = 0; i < data.length; i++) {
                double xi = x.getEntry(i);
                sum += Math.pow(xi - data[i], 2);
            }
            return sum;
        }

        @Override
        public RealVector gradient(RealVector x) {
            RealVector gradient = new ArrayRealVector(new double[data.length]);
            for (int i = 0; i < data.length; i++) {
                double xi = x.getEntry(i);
                gradient.setEntry(i, 2 * (xi - data[i]));
            }
            return gradient;
        }
    }

}