package org.apache.commons.math3.optimization.direct;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.optim.*;
import org.apache.commons.math3.optim.initializer.AveragedStartingPointGenerator;
import org.apache.commons.math3.optim.point.PointValuePair;
import org.apache.commons.math3.optim.univariate.BracketFinder;
import org.apache.commons.math3.optim.univariate.BrentOptimizer;
import org.apache.commons.math3.optim.univariate.SimpleUnivariateValueChecker;
import org.apache.commons.math3.optim.univariate.UnivariatePointValuePair;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;

public class PowellOptimizerTest {

    @Test
    public void testDefaultConstructor() {
        PowellOptimizer optimizer = new PowellOptimizer();
        assertNotNull(optimizer);
    }

    @Test
    public void testConstructorWithDefaultConvergenceChecker() {
        PowellOptimizer optimizer = new PowellOptimizer(0.001, 1.0);
        assertNotNull(optimizer);
    }

    @Test
    public void testConstructorWithCustomConvergenceChecker() {
        PowellOptimizer optimizer = new PowellOptimizer(0.001, 1.0, new CustomConvergenceChecker());
        assertNotNull(optimizer);
    }

    @Test
    public void testOptimizeWithUnivariateFunctionAndDefaultParameters() {
        PowellOptimizer optimizer = new PowellOptimizer();
        UnivariateFunction function = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return x * x;
            }
        };
        PointValuePair result = optimizer.optimize(function);
        assertEquals(0.0, result.getPoint(), 0.01);
        assertEquals(0.0, result.getValue(), 0.01);
    }

    @Test
    public void testOptimizeWithUnivariateFunctionAndCustomParameters() {
        PowellOptimizer optimizer = new PowellOptimizer(0.01, 1.0);
        UnivariateFunction function = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return x * x;
            }
        };
        PointValuePair result = optimizer.optimize(function);
        assertEquals(0.0, result.getPoint(), 0.01);
        assertEquals(0.0, result.getValue(), 0.01);
    }

    @Test
    public void testOptimizeWithMultivariateFunctionAndDefaultParameters() {
        PowellOptimizer optimizer = new PowellOptimizer();
        MultivariateFunction function = new MultivariateFunction1D() {
            @Override
            public double value(double[] x) {
                return x[0] * x[0];
            }
        };
        PointValuePair result = optimizer.optimize(function, new double[]{1.0});;
        assertEquals(0.0, result.getPoint()[0], 0.01);
        assertEquals(0.0, result.getValue(), 0.01);
    }

    @Test
    public void testOptimizeWithMultivariateFunctionAndCustomParameters() {
        PowellOptimizer optimizer = new PowellOptimizer(0.01, 1.0);
        MultivariateFunction function = new MultivariateFunction1D() {
            @Override
            public double value(double[] x) {
                return x[0] * x[0];
            }
        };
        PointValuePair result = optimizer.optimize(function, new double[]{1.0});
        assertEquals(0.0, result.getPoint()[0], 0.01);
        assertEquals(0.0, result.getValue(), 0.01);
    }

    @Test
    public void testOptimizeWithFunctionThatHasNoRoots() {
        PowellOptimizer optimizer = new PowellOptimizer();
        MultivariateFunction function = new MultivariateFunction1D() {
            @Override
            public double value(double[] x) {
                return Math.sin(Math.PI * x[0]);
            }
        };
        PointValuePair result = optimizer.optimize(function, new double[]{0.0});
        assertFalse(result.isOptimized());
        assertThrows(UnsatisfiedConstraintException.class, () -> optimizer.optimize(function, new double[]{0.0}));
    }

    @Test
    public void testOptimizeWithFunctionThatHasMultipleRoots() {
        PowellOptimizer optimizer = new PowellOptimizer();
        MultivariateFunction function = new MultivariateFunction1D() {
            @Override
            public double value(double[] x) {
                return Math.pow(x[0] - 1.0, 2);
            }
        };
        PointValuePair result = optimizer.optimize(function, new double[]{2.0});
        assertEquals(-1.0, result.getPoint()[0], 0.01);
        assertEquals(0.0, result.getValue(), 0.01);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorWithInvalidAbsoluteThreshold() {
        PowellOptimizer optimizer = new PowellOptimizer(0.001, -1.0);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testConstructorWithInvalidRelativeThreshold() {
        PowellOptimizer optimizer = new PowellOptimizer(-1.0, 1.0);
    }

    @Test
    public void testLineSearch() {
        UnivariateFunction function = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2) + Math.sin(x);
            }
        };
        double[] startPoint = new double[]{1.0};
        PowellOptimizer.LineSearch lineSearch = new PowellOptimizer.LineSearch(0.01, 1.0);
        UnivariatePointValuePair result = lineSearch.search(startPoint, new double[]{1.0, -1.0});
        assertEquals(-1.257625975393722, result.getPoint(), 0.01);
        assertEquals(-3.257625975393722, result.getValue(), 0.01);
    }

    @Test
    public void testNewPointAndDirection() {
        double[] startPoint = new double[]{1.0};
        double[] direction = new double[]{1.0};
        double optimizationPoint = 2.0;
        double[][] result = PowellOptimizer.newPointAndDirection(startPoint, direction, optimizationPoint);
        double[] newPoint = result[0];
        double[] newDirection = result[1];
        assertEquals(2.0, newPoint[0], 0.01);
        assertArrayEquals(new double[]{2.0}, newDirection);
    }

    private static class CustomConvergenceChecker implements ConvergenceChecker<PointValuePair> {
        @Override
        public boolean converged(int iterations, PointValuePair previous, PointValuePair current) {
            double error = current.getValue() - previous.getValue();
            boolean converged = error < 1e-4;
            return converged;
        }
    }

    private static interface MultivariateFunction1D extends MultivariateFunction {
        @Override
        default int getNumberOfVariables() {
            return 1;
        }

        @Override
        default int getNumberOfConstraints() {
            return 0;
        }

        @Override
        double[] value(double[] x);
    }
}