package org.apache.commons.math.optimization.direct;

import static org.junit.Assert.*;

import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleBounds;
import org.apache.commons.math3.optim.univariate.UnivariateFunction;
import org.apache.commons.math3.optim.univariate.UnivariateFunctionUtils;
import org.apache.commons.math3.optim.initializer.AverageInitializer;
import org.apache.commons.math3.optim.initializer.GreedyInitializer;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.ObjectiveFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.NelderMeadSimplex;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.NelderMeadSimplexInitializer;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.Polyhedron;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.SimplexMethod;
import org.apache.commons.math3.optim.point.PointValuePairList;
import org.apache.commons.math3.optim.point.RealPointValuePair;
import org.junit.Before;
import org.junit.Test;

public class MultiDirectionalTest {

    private MultiDirectional optimizer;
    private PointValuePairList pvps;
    private SimpleBounds bounds;
    private UnivariateFunction function;

    @Before
    public void setUp() {
        optimizer = new MultiDirectional();
        pvps = new PointValuePairList();
        bounds = new SimpleBounds(1.0, 2.0, 10.0, 20.0);
        function = new MyFunction();
    }

    @Test
    public void testMultiDirectionalOptimizer() throws Exception {
        optimizer.setMaxEvaluations(100);
        optimizer.setMaxIterations(100);
        optimizer.setConvergenceChecker(new RealConvergenceChecker(1.0E-7));

        double[] x = new double[] {15.0, 15.0};
        RealPointValuePair initialPoint = new RealPointValuePair(x);
        optimizer.optimize(function, bounds, initialPoint, GoalType.MINIMIZE);
        PointValuePair bestPoint = optimizer.getBestPoint();
        double bestValue = bestPoint.getValue();
        double expectedBestValue = -28.999999999999995;
        assertEquals("Unexpected best value: " + bestValue, expectedBestValue, bestValue, 1e-8);

        PointValuePairList allPoints = new PointValuePairList(optimizer.getAllPoints());
        int expectedNumberOfIterations = 27;
        int numberOfIterations = allPoints.size();
        assertEquals("Unexpected number of iterations: " + numberOfIterations, expectedNumberOfIterations, numberOfIterations);
        RealPointValuePair[] points = allPoints.toArray(new RealPointValuePair[0]);
        for (RealPointValuePair point : points) {
            double[] xValue = point.getPointRef();
            double x0 = xValue[0];
            double x1 = xValue[1];
            double yValue = point.getValue();

            // check that all points are inside the bounds
            double lowerBound = Math.min(bounds.getMin(), bounds.getMax());
            double upperBound = Math.max(bounds.getMin(), bounds.getMax());
            assertTrue("Point (" + x0 + ", " + x1 + ") is not within bounds [" + lowerBound + ", " + upperBound + "]: " + yValue,
                      x0 >= lowerBound && x0 <= upperBound && x1 >= lowerBound && x1 <= upperBound);

            // check that the function value is valid
            double functionValue = UnivariateFunctionUtils.value(function, xValue);
            assertEquals("Unexpected function value for point (" + x0 + ", " + x1 + "): " + yValue, functionValue, yValue, 1e-8);
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullFunction() {
        optimizer.optimize(null, bounds, initialPoint, GoalType.MINIMIZE);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullBounds() {
        optimizer.optimize(function, null, initialPoint, GoalType.MINIMIZE);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullInitialPoint() {
        optimizer.optimize(function, bounds, null, GoalType.MINIMIZE);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullGoalType() {
        optimizer.optimize(function, bounds, initialPoint, null);
    }

    @Test
    public void testSimplexShapePreservation() throws Exception {
        // create a simple function that will yield a simplex with a vertex
        // at each corner and a vertex at the centroid
        UnivariateFunction function = new MyFunctionWithCentroid();
        optimizer.setMaxEvaluations(15);
        optimizer.setMaxIterations(15);
        optimizer.setConvergenceChecker(new RealConvergenceChecker(1.0E-6));

        double[] x = new double[] {1.0, 1.0};
        RealPointValuePair initialPoint = new RealPointValuePair(x);
        optimizer.optimize(function, bounds, initialPoint, GoalType.MINIMIZE);
        PointValuePairList allPoints = new PointValuePairList(optimizer.getAllPoints());
        RealPointValuePair[] points = allPoints.toArray(new RealPointValuePair[0]);
        int n = points.length;
        Polyhedron simplex = new Polyhedron(n);
        for (int i = 0; i < n; ++i) {
            double[] xValue = points[i].getPointRef();
            simplex.addPoint(xValue);
        }
        NelderMeadSimplex initialSimplex = new NelderMeadSimplex(n);
        initialSimplex.setPoint(0, initialPoint.getPointRef());
        NelderMeadSimplex expectedSimplex = new NelderMeadSimplex(n);
        expectedSimplex.setPoint(0, simplex.getPoint(0));
        for (int i = 1; i < n; ++i) {
            expectedSimplex.setPoint(i, simplex.getCentroid());
        }
        NelderMeadSimplexInitializer initializer = new NelderMeadSimplexInitializer(initialSimplex);
        NelderMeadSimplex actualSimplex = optimizer.getSimplex();
        NelderMeadSimplex expectedActualSimplex = new NelderMeadSimplex(n);
        expectedActualSimplex.setPoint(0, initialPoint.getPointRef());
        for (int i = 1; i < n; ++i) {
            PointValuePair p = allPoints.get(i);
            double[] xValue = p.getPointRef();
            expectedActualSimplex.setPoint(i, xValue);
        }
        NelderMeadSimplex expectedFinalSimplex = new NelderMeadSimplex(n);
        expectedFinalSimplex.setPoint(0, simplex.getPoint(0));
        for (int i = 1; i < n; ++i) {
            expectedFinalSimplex.setPoint(i, simplex.getPoint(i));
        }
        assertTrue("Unexpected initial simplex: " + initialSimplex, initialSimplex.equals(expectedInitialSimplex));
        assertTrue("Unexpected actual simplex: " + actualSimplex, actualSimplex.equals(expectedActualSimplex));
        assertTrue("Unexpected final simplex: " + expectedFinalSimplex, expectedFinalSimplex.equals(expectedSimplex));
    }

    private static class MyFunction implements ObjectiveFunction {
        @Override
        public double value(double[] arg0) {
            double x = arg0[0];
            double y = arg0[1];
            return 1.0 / (x * x + y * y);
        }
    }

    private static class MyFunctionWithCentroid implements ObjectiveFunction {
        @Override
        public double value(double[] arg0) {
            double x = arg0[0];
            double y = arg0[1];
            double centroidX = 1.0 / 3.0;
            double centroidY = 1.0 / 3.0;
            if (x == centroidX && y == centroidY) {
                // this is the centroid, return 0
                return 0.0;
            }
            return 1.0 / (x * x + y * y);
        }
    }

}