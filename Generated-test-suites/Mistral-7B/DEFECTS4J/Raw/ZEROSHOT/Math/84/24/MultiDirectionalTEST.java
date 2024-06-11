package org.apache.commons.math.optimization.direct;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.Comparator;

import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.optim.RealPointValuePair;
import org.apache.commons.math3.optim.SimpleValuesFinder;
import org.apache.commons.math3.optim.point.PointValuePair;
import org.apache.commons.math3.optim.univariate.DifferencableUnivariateFunction;
import org.apache.commons.math3.optim.univariate.UnivariateFunction;
import org.apache.commons.math3.optim.univariate.UnivariateFunctionValue;
import org.apache.commons.math3.optim.univariate.noderiv.NewtonCubic;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests for {@link MultiDirectional} class.
 */
public class MultiDirectionalTest {

    private MultiDirectional searcher;
    private Comparator<RealPointValuePair> comparator;
    private double epsilon = 1e-10;

    @Before
    public void setUp() {
        searcher = new MultiDirectional();
        comparator = new Comparator<RealPointValuePair>() {
            @Override
            public int compare(RealPointValuePair o1, RealPointValuePair o2) {
                return Double.compare(o1.getValue(), o2.getValue());
            }
        };
    }

    /**
     * Tests the minimization of a unimodal quadratic function.
     */
    @Test
    public void testQuadraticFunctionMin() {
        UnivariateFunction function = new DifferencableUnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2) + 2;
            }

            @Override
            public double derivative(double x) {
                return 2 * x;
            }
        };
        RealPointValuePair solution = new RealPointValuePair(new double[] {2.0}, 2.0);
        try {
            searcher.optimize(function, new SimpleValuesFinder(), comparator);
            RealPointValuePair result = searcher.getBestSolution();
            assertEquals(solution.getValue(), result.getValue(), epsilon);
            assertArrayEquals(solution.getPoint(), result.getPoint(), epsilon);
        } catch (Exception e) {
            fail("Optimization failed: " + e.getMessage());
        }
    }

    /**
     * Tests the minimization of a function with multiple local minima.
     */
    @Test
    public void testMultipleLocalMinima() {
        UnivariateFunction function = new DifferencableUnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x - 1.0, 2) + 0.1 * Math.sin(Math.PI * x);
            }

            @Override
            public double derivative(double x) {
                return 2 * (x - 1.0) - Math.PI * Math.cos(Math.PI * x);
            }
        };
        RealPointValuePair solution = new RealPointValuePair(new double[] {1.0}, 0.0);
        try {
            searcher.optimize(function, new SimpleValuesFinder(), comparator);
            RealPointValuePair result = searcher.getBestSolution();
            assertEquals(solution.getValue(), result.getValue(), epsilon);
            assertArrayEquals(solution.getPoint(), result.getPoint(), epsilon);
        } catch (Exception e) {
            fail("Optimization failed: " + e.getMessage());
        }
    }

    /**
     * Tests the minimization of a non-differentiable function.
     */
    @Test
    public void testNonDifferentiableFunction() {
        UnivariateFunction function = new UnivariateFunction() {
            @Override
            public UnivariateFunctionValue value(double x, int n) throws Exception {
                double result = Math.abs(x - 1.0);
                if (Double.isNaN(result)) {
                    throw new FunctionEvaluationException("Cannot evaluate function at point x = " + x);
                }
                return new UnivariateFunctionValue(result);
            }
        };
        RealPointValuePair solution = new RealPointValuePair(new double[] {1.0}, 0.0);
        try {
            searcher.optimize(function, new SimpleValuesFinder(), comparator);
            RealPointValuePair result = searcher.getBestSolution();
            assertEquals(solution.getValue(), result.getValue(), epsilon);
            assertArrayEquals(solution.getPoint(), result.getPoint(), epsilon);
        } catch (Exception e) {
            fail("Optimization failed: " + e.getMessage());
        }
    }

    /**
     * Tests the minimization of a function with a large search domain.
     */
    @Test
    public void testLargeSearchDomain() {
        UnivariateFunction function = new DifferencableUnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2) + 2;
            }

            @Override
            public double derivative(double x) {
                return 2 * x;
            }
        };
        RealPointValuePair solution = new RealPointValuePair(new double[] {1000.0}, 1000001.0);
        try {
            searcher.optimize(function, new SimpleValuesFinder(), comparator);
            RealPointValuePair result = searcher.getBestSolution();
            PointValuePair pv = new PointValuePair(1000.0, Double.POSITIVE_INFINITY);
            assertEquals(Precision.isClose(solution.getValue(), result.getValue(), epsilon), true);
            assertEquals(Arrays.deepEquals(solution.getPoint(), pv.getPoint()), true);
        } catch (Exception e) {
            fail("Optimization failed: " + e.getMessage());
        }
    }

    /**
     * Tests the minimization of a function with a large number of dimensions.
     */
    @Test
    public void testMultiDimensionalFunction() {
        UnivariateFunction function = new DifferencableUnivariateFunction() {
            @Override
            public double value(double[] x) {
                double sum = 0.0;
                for (int i = 0; i < x.length; ++i) {
                    sum += Math.pow(x[i] - 1.0, 2);
                }
                return sum;
            }

            @Override
            public double[] derivative(double[] x) {
                double[] result = new double[x.length];
                for (int i = 0; i < x.length; ++i) {
                    result[i] = 2 * (x[i] - 1.0);
                }
                return result;
            }
        };
        RealPointValuePair solution = new RealPointValuePair(new double[] {1.0, 1.0}, 2.0);
        try {
            searcher.setMaxIterations(100);
            searcher.optimize(new NewtonCubic(function), comparator);
            RealPointValuePair result = searcher.getBestSolution();
            assertEquals(solution.getValue(), result.getValue(), epsilon);
            assertArrayEquals(solution.getPoint(), result.getPoint(), epsilon);
        } catch (Exception e) {
            fail("Optimization failed: " + e.getMessage());
        }
    }

}