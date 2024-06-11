package org.apache.commons.math.optimization.direct;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleValueFunction;
import org.apache.commons.math3.optim.nonlinear.GoalType;
import org.apache.commons.math3.optim.nonlinear.vector.linear. ArrayRealVector;
import org.junit.Test;

import org.apache.commons.cli.util.MultiDirectional;

public class MultiDirectionalTest {

    private static final double DELTA = 0.001;
    private static final double EPSILON = 1.0e-9;

    @Test
    public void testMultiDirectionalDefaultConstructor() {
        MultiDirectional optimizer = new MultiDirectional();
        assertEquals(2.0, optimizer.getKhi(), EPSILON);
        assertEquals(0.5, optimizer.getGamma(), EPSILON);
    }

    @Test
    public void testMultiDirectionalCustomConstructor() {
        MultiDirectional optimizer = new MultiDirectional(3.0, 0.7);
        assertEquals(3.0, optimizer.getKhi(), EPSILON);
        assertEquals(0.7, optimizer.getGamma(), EPSILON);
    }

    @Test
    public void testIterateSimplex() throws Exception {
        MultiDirectional optimizer = new MultiDirectional(3.0, 0.7);

        double[] x = {1.0, 2.0};
        SimpleValueFunction<ArrayRealVector> function = new SimpleValueFunction<ArrayRealVector>() {
            @Override
            public double value(ArrayRealVector point) {
                return point.getEntry(0) * point.getEntry(0) + point.getEntry(1) * point.getEntry(1);
            }
        };
        GoalType goalType = GoalType.MINIMIZE;

        PointValuePair initialPoint = new PointValuePair(new ArrayRealVector(x), function.value(new ArrayRealVector(x)));
        optimizer.optimize(function, initialPoint, goalType, new RealConvergenceChecker(DELTA), new Comparator<PointValuePair>() {
            @Override
            public int compare(PointValuePair o1, PointValuePair o2) {
                return Double.compare(o1.getValue(), o2.getValue());
            }
        });

        double[] expected = {1.0, 1.0};
        double expectedValue = 1.0;
        assertArrayEquals(expected, optimizer.getSolution().getPoint().toArray(), DELTA);
        assertEquals(expectedValue, optimizer.getSolution().getValue(), EPSILON);
    }

    @Test
    public void testEvaluateNewSimplex() throws Exception {
        MultiDirectional optimizer = new MultiDirectional(3.0, 0.7);

        double[] x = {1.0, 1.0};
        SimpleValueFunction<ArrayRealVector> function = new SimpleValueFunction<ArrayRealVector>() {
            @Override
            public double value(ArrayRealVector point) {
                return point.getEntry(0) * point.getEntry(0) + point.getEntry(1) * point.getEntry(1);
            }
        };
        GoalType goalType = GoalType.MINIMIZE;

        PointValuePair initialPoint = new PointValuePair(new ArrayRealVector(x), function.value(new ArrayRealVector(x)));
        optimizer.optimize(function, initialPoint, goalType, new RealConvergenceChecker(DELTA), new Comparator<PointValuePair>() {
            @Override
            public int compare(PointValuePair o1, PointValuePair o2) {
                return Double.compare(o1.getValue(), o2.getValue());
            }
        });

        double[] original = optimizer.getSolution().getPoint().toArray();
        double[] reflected1 = new double[] {original[0], original[1] + 2.0};
        double[] reflected2 = new double[] {original[0] + 1.0, original[1] + 2.0};
        double[] contracted = new double[] {original[0] - 0.5 * (original[0] - original[1]), original[1] - 0.5 * (original[1] - original[0])};

        PointValuePair reflected1Pair = new PointValuePair(new ArrayRealVector(reflected1), Double.NaN, false);
        PointValuePair reflected2Pair = new PointValuePair(new ArrayRealVector(reflected2), Double.NaN, false);
        PointValuePair contractedPair = new PointValuePair(new ArrayRealVector(contracted), Double.NaN, false);

        optimizer.setComparator(new Comparator<PointValuePair>() {
            @Override
            public int compare(PointValuePair o1, PointValuePair o2) {
                return Double.compare(o1.getValue(), o2.getValue());
            }
        });

        PointValuePair reflected1Result = optimizer.evaluateNewSimplex(new PointValuePair[] {initialPoint, reflected1Pair, reflected2Pair, contractedPair}, 1.0, optimizer.getComparator());
        PointValuePair reflected2Result = optimizer.evaluateNewSimplex(new PointValuePair[] {initialPoint, reflected1Pair, reflected2Pair, contractedPair}, 3.0, optimizer.getComparator());

        if (reflected1Result.getValue() < reflected2Result.getValue()) {
            assertArrayEquals(reflected1, optimizer.getSolution().getPoint().toArray(), DELTA);
            assertEquals(reflected1Result.getValue(), optimizer.getSolution().getValue(), EPSILON);
        } else {
            assertArrayEquals(reflected2, optimizer.getSolution().getPoint().toArray(), DELTA);
            assertEquals(reflected2Result.getValue(), optimizer.getSolution().getValue(), EPSILON);
        }

        // Check the contracted simplex is not the best solution
        assertFalse(optimizer.getSolution().equals(contractedPair));

    }
}