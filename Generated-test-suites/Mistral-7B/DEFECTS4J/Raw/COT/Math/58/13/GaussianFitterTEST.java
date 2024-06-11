package org.apache.commons.math.optimization.fitting.test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.math3.analysis.function.Gaussian;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.PointsValuePair;
import org.apache.commons.math3.optim.SimpleBounds;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.ObjectiveFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.point.DoublePointValuePair;
import org.apache.commons.math3.optim.nonlinear.scalar.point.GoalTypePointValuePair;
import org.apache.commons.math3.optim.nonlinear.scalar.points.AbstractPointGenerator;
import org.apache.commons.math3.optim.nonlinear.scalar.points.PointsGenerator;
import org.apache.commons.math3.optim.nonlinear.scalar.points.RandomPointGenerator;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import org.apache.commons.math.optimization.fitting.CurveFitter;
import org.apache.commons.math.optimization.fitting.GaussianFitter;
import org.apache.commons.math.optimization.fitting.WeightedObservedPoint;

/**
 * Test class for {@link GaussianFitter}.
 */
@RunWith(Parameterized.class)
public class GaussianFitterTest {

    private static final double[] INITIAL_GUESS = new double[] { 1.0, 2.0, 0.5 };
    private static final double[] PARAMETERS = new double[] { 2.0, 3.0, 0.5 };

    private PointsGenerator pointGenerator;
    private double[][] points;

    public GaussianFitterTest(PointsGenerator pointGenerator, double[][] points) {
        this.pointGenerator = pointGenerator;
        this.points = points;
    }

    @Parameterized.Parameters(name = "{1}")
    public static List<Object[]> data() {
        return Arrays.asList(new Object[][] {
                new Object[] { new RandomPointGenerator(10), new double[][] {
                        new DoublePointValuePair(1.0, 10.0),
                        new DoublePointValuePair(2.0, 4.0),
                        new DoublePointValuePair(3.0, 1.0)
                }},
                new Object[] { new RandomPointGenerator(10), new double[][] {
                        new GoalTypePointValuePair(GoalType.MINIMIZE, new MaxEval(new ObjectiveFunction() {
                            public double value(double x) {
                                return Math.pow(x - 3.0, 2.0);
                            }
                        }), new DoublePointValuePair(2.0, 1.0), 1e-6),
                        new GoalTypePointValuePair(GoalType.MINIMIZE, new MaxEval(new ObjectiveFunction() {
                            public double value(double x) {
                                return Math.pow(x - 3.0, 2.0);
                            }
                        }), new DoublePointValuePair(4.0, 2.0), 1e-6),
                        new GoalTypePointValuePair(GoalType.MINIMIZE, new MaxEval(new ObjectiveFunction() {
                            public double value(double x) {
                                return Math.pow(x - 3.0, 2.0);
                            }
                        }), new DoublePointValuePair(5.0, 3.0), 1e-6)
                }},
                new Object[] { new AbstractPointGenerator() {
                    @Override
                    public PointsValuePair generateNextPoint(SimpleBounds bounds) {
                        double[] p = new double[] { bounds.getMinX() + Math.random() * (bounds.getMaxX() - bounds.getMinX()),
                                                    bounds.getMinY() + Math.random() * (bounds.getMaxY() - bounds.getMinY()) };
                        return new DoublePointValuePair(p[0], p[1]);
                    }

                    @Override
                    public boolean hasMoreElements() {
                        return true;
                    }
                }, new double[][] {
                        new DoublePointValuePair(1.0, 1.0),
                        new DoublePointValuePair(2.0, 4.0),
                        new DoublePointValuePair(3.0, 1.0),
                        new DoublePointValuePair(4.0, 9.0),
                        new DoublePointValuePair(5.0, 2.0)
                }}
        });
    }

    @Test
    public void testFit() {
        GaussianFitter fitter = new GaussianFitter(new LevenbergMarquardtOptimizer());
        for (WeightedObservedPoint point : createObservedPoints()) {
            fitter.addObservedPoint(point);
        }
        double[] result = fitter.fit();
        assertEquals(3, result.length);
        assertArrayEquals(PARAMETERS, result, 0.001);
    }

    @Test(expected = NullArgumentException.class)
    public void testFitWithNullPoints() {
        GaussianFitter fitter = new GaussianFitter(new LevenbergMarquardtOptimizer());
        fitter.fit((WeightedObservedPoint[]) null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFitWithEmptyPoints() {
        GaussianFitter fitter = new GaussianFitter(new LevenbergMarquardtOptimizer());
        fitter.fit(new WeightedObservedPoint[0]);
    }

    @Test
    public void testFitWithInvalidInitialGuess() {
        GaussianFitter fitter = new GaussianFitter(new LevenbergMarquardtOptimizer());
        try {
            fitter.fit(new double[] { 1.0, -1.0, 0.5 });
            assert false;
        } catch (IllegalArgumentException e) {
            assertEquals("The initial guess must have strictly positive norm.", e.getMessage());
        }
    }

    @Test
    public void testFitWithFewerThanThreePoints() {
        GaussianFitter fitter = new GaussianFitter(new LevenbergMarquardtOptimizer());
        try {
            fitter.fit(createObservedPoints().subList(0, 2).toArray(new WeightedObservedPoint[0]));
            assert false;
        } catch (IllegalArgumentException e) {
            assertEquals("The number of observed points is less than 3.", e.getMessage());
        }
    }

    @Test
    public void testFitWithInvalidNumberOfPoints() {
        GaussianFitter fitter = new GaussianFitter(new LevenbergMarquardtOptimizer());
        try {
            fitter.fit(createObservedPoints().toArray(new WeightedObservedPoint[0]));
            assert false;
        } catch (IllegalArgumentException e) {
            assertEquals("The number of observed points is not an array of length 3.", e.getMessage());
        }
    }

    @Test
    public void testFitWithNullInitialGuess() {
        GaussianFitter fitter = new GaussianFitter(new LevenbergMarquardtOptimizer());
        try {
            fitter.fit(null);
            assert false;
        } catch (NullArgumentException e) {
            assertEquals("The initial guess must not be null.", e.getMessage());
        }
    }

    private WeightedObservedPoint[] createObservedPoints() {
        WeightedObservedPoint[] observedPoints = new WeightedObservedPoint[points.length];
        for (int i = 0; i < points.length; i++) {
            observedPoints[i] = new WeightedObservedPoint(points[i][0], points[i][1], 1.0);
        }
        return observedPoints;
    }
}