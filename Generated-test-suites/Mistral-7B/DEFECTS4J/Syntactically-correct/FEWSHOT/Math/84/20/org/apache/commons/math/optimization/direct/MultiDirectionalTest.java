package org.apache.commons.math.optimization.direct;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.exception.MaxEvalExceededException;
import org.apache.commons.math3.exception.NoBracketingException;
import org.apache.commons.math3.exception.UnsupportedOperationException;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.RealFunction;
import org.apache.commons.math3.optim.SimpleBounds;
import org.apache.commons.math3.optim.initializer.AveragedStartingPointInitializer;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.ObjectiveFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.BrentOptimizer;
import org.junit.Test;

public class MultiDirectionalTest {

    @Test(expected = UnsupportedOperationException.class)
    public void testDefaultConstructorWithNoArguments() {
        new MultiDirectional();
    }

    @Test
    public void testMultiDirectionalWithDefaultCoefficients() {
        final Function f = new Function() {
            @Override
            public double value(double x) {
                return Math.pow(Math.sin(x), 2) + Math.pow(Math.cos(x), 2);
            }
        };

        final MultiDirectional md = new MultiDirectional();
        final RealFunction optimizer = new BrentOptimizer(new ObjectiveFunction(f), new SimpleBounds(-5, 5), GoalType.MINIMIZE);
        final PointValuePair result = md.optimize(optimizer);
        assertEquals(-1.41372, result.getPoint()[0], 1e-6);
        assertEquals(-3.24663, result.getValue(), 1e-6);
    }

    @Test
    public void testMultiDirectionalWithCustomCoefficients() {
        final Function f = new Function() {
            @Override
            public double value(double x) {
                return Math.pow(Math.sin(x), 2) + Math.pow(Math.cos(x), 2);
            }
        };

        final MultiDirectional md = new MultiDirectional(3.0, 0.2);
        final RealFunction optimizer = new BrentOptimizer(new ObjectiveFunction(f), new SimpleBounds(-5, 5), GoalType.MINIMIZE);
        final PointValuePair result = md.optimize(optimizer);
        assertEquals(-1.41372, result.getPoint()[0], 1e-6);
        assertEquals(-3.24663, result.getValue(), 1e-6);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIterateSimplexWithNullComparator() {
        final MultiDirectional md = new MultiDirectional();
        md.iterateSimplex(null);
    }

    @Test
    public void testIterateSimplex() throws Exception {
        final Function f = new Function() {
            @Override
            public double value(double x) {
                return Math.pow(Math.sin(x), 2) + Math.pow(Math.cos(x), 2);
            }
        };

        final MultiDirectional md = new MultiDirectional();
        final RealFunction optimizer = new BrentOptimizer(new ObjectiveFunction(f), new SimpleBounds(-5, 5), GoalType.MINIMIZE);
        final PointValuePair initialPoint = new PointValuePair(new double[]{0.5}, Double.NaN, false);
        final AveragedStartingPointInitializer initializer = new AveragedStartingPointInitializer(3);
        initializer.initialize(optimizer, new double[]{1.0, 1.5, 2.0}, null);
        md.setStartingPoint(initialPoint);

        md.iterateSimplex(new Comparator<PointValuePair>() {
            @Override
            public int compare(PointValuePair o1, PointValuePair o2) {
                return o1.getValue().compareTo(o2.getValue());
            }
        });

        final PointValuePair result = md.getBestPoint();
        assertEquals(-1.41372, result.getPoint()[0], 1e-6);
        assertEquals(-3.24663, result.getValue(), 1e-6);
    }

    @Test
    public void testEvaluateNewSimplex() throws Exception {
        final Function f = new Function() {
            @Override
            public double value(double[] x) {
                return Math.pow(Math.sin(x[0]), 2) + Math.pow(Math.cos(x[1]), 2);
            }
        };

        final MultiDirectional md = new MultiDirectional();
        final double[] xSmallest = new double[]{0.5, 0.5};
        final double[][] original = new double[][]{
                {0.5, 0.5},
                {1.0, 1.5},
                {2.0, 1.5},
                {1.5, 0.5}
        };

        final RealFunction optimizer = new BrentOptimizer(new ObjectiveFunction(f), new SimpleBounds(new double[]{-5, -5}, new double[]{5, 5}), GoalType.MINIMIZE);
        final Comparator<PointValuePair> comparator = new Comparator<PointValuePair>() {
            @Override
            public int compare(PointValuePair o1, PointValuePair o2) {
                return o1.getValue().compareTo(o2.getValue());
            }
        };

        final RealPointValuePair[] reflected = md.evaluateNewSimplex(original, 1.0, comparator).getSimplex();
        assertArrayEquals(new double[][]{
                {1.5, 1.5},
                {0.5, 0.5},
                {1.0, 1.5},
                {1.0, 0.5}
        }, reflected, 0.0);

        final RealPointValuePair[] reflectedSimplex = md.evaluateNewSimplex(original, 2.0, comparator).getSimplex();
        assertArrayEquals(new double[][]{
                {1.5, 1.5},
                {1.0, 0.5},
                {3.0, 0.5},
                {2.0, 1.5}
        }, reflectedSimplex, 0.0);

        final RealPointValuePair[] contracted = md.evaluateNewSimplex(original, 0.5, comparator).getSimplex();
        assertArrayEquals(new double[][]{
                {0.5, 0.5},
                {1.0, 1.5},
                {0.5, 1.5},
                {1.5, 0.5}
        }, contracted, 0.0);
    }

    @Test(expected = FunctionEvaluationException.class)
    public void testEvaluateNewSimplexWithFunctionEvaluationException() throws Exception {
        final Function f = new Function() {
            @Override
            public double value(double[] x) {
                throw new FunctionEvaluationException();
            }
        };

        final MultiDirectional md = new MultiDirectional();
        final double[] xSmallest = new double[]{0.5, 0.5};
        final double[][] original = new double[][]{
                {0.5, 0.5},
                {1.0, 1.5},
                {2.0, 1.5},
                {1.5, 0.5}
        };

        final RealFunction optimizer = new BrentOptimizer(new ObjectiveFunction(f), new SimpleBounds(new double[]{-5, -5}, new double[]{5, 5}), GoalType.MINIMIZE);
        final Comparator<PointValuePair> comparator = new Comparator<PointValuePair>() {
            @Override
            public int compare(PointValuePair o1, PointValuePair o2) {
                return o1.getValue().compareTo(o2.getValue());
            }
        };

        md.evaluateNewSimplex(original, 1.0, comparator);
    }

    @Test(expected = OptimizationException.class)
    public void testEvaluateNewSimplexWithOptimizationException() throws Exception {
        final Function f = new Function() {
            @Override
            public double value(double[] x) throws Exception {
                throw new OptimizationException();
            }
        };

        final MultiDirectional md = new MultiDirectional();
        final double[] xSmallest = new double[]{0.5, 0.5};
        final double[][] original = new double[][]{
                {0.5, 0.5},
                {1.0, 1.5},
                {2.0, 1.5},
                {1.5, 0.5}
        };

        final RealFunction optimizer = new BrentOptimizer(new ObjectiveFunction(f), new SimpleBounds(new double[]{-5, -5}, new double[]{5, 5}), GoalType.MINIMIZE);
        final Comparator<PointValuePair> comparator = new Comparator<PointValuePair>() {
            @Override
            public int compare(PointValuePair o1, PointValuePair o2) {
                return o1.getValue().compareTo(o2.getValue());
            }
        };

        md.evaluateNewSimplex(original, 1.0, comparator);
    }

    @Test(expected = MaxEvalExceededException.class)
    public void testEvaluateNewSimplexWithMaxEvalExceededException() throws Exception {
        final Function f = new Function() {
            @Override
            public double value(double[] x) {
                for (int i = 0; i < 1000000; i++) {
                    if (i % 2 == 0) {
                        x[0] += 0.1;
                    } else {
                        x[1] += 0.1;
                    }
                }
                return 0.0;
            }
        };

        final MultiDirectional md = new MultiDirectional();
        final double[] xSmallest = new double[]{0.5, 0.5};
        final double[][] original = new double[][]{
                {0.5, 0.5},
                {1.0, 1.5},
                {2.0, 1.5},
                {1.5, 0.5}
        };

        final RealFunction optimizer = new BrentOptimizer(new ObjectiveFunction(f), new SimpleBounds(new double[]{-5, -5}, new double[]{5, 5}), GoalType.MINIMIZE);
        final Comparator<PointValuePair> comparator = new Comparator<PointValuePair>() {
            @Override
            public int compare(PointValuePair o1, PointValuePair o2) {
                return o1.getValue().compareTo(o2.getValue());
            }
        };

        md.evaluateNewSimplex(original, 1.0, comparator);
    }

    @Test(expected = NoBracketingException.class)
    public void testEvaluateNewSimplexWithNoBracketingException() throws Exception {
        final Function f = new Function() {
            @Override
            public double value(double[] x) {
                if (x[0] < 0) {
                    return Double.NEGATIVE_INFINITY;
                } else if (x[0] > 1) {
                    return Double.POSITIVE_INFINITY;
                } else {
                    return Math.sin(Math.PI / 2 * x[0]);
                }
            }
        };

        final MultiDirectional md = new MultiDirectional();
        final double[] xSmallest = new double[]{0.5};
        final double[][] original = new double[][]{
                {0.5},
                {1.0},
                {0.0},
                {1.5}
        };

        final RealFunction optimizer = new BrentOptimizer(new ObjectiveFunction(f), new SimpleBounds(new double[]{-5, -5}, new double[]{5, 5}), GoalType.MINIMIZE);
        final Comparator<PointValuePair> comparator = new Comparator<PointValuePair>() {
            @Override
            public int compare(PointValuePair o1, PointValuePair o2) {
                return o1.getValue().compareTo(o2.getValue());
            }
        };

        md.evaluateNewSimplex(original, 1.0, comparator);
    }

}