package org.apache.commons.math3.optimization.direct;

import static org.junit.Assert.*;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.PointValuePair;
import org.apache.commons.math3.optimization.MultivariateOptimizer;
import org.apache.commons.math3.optimization.univariate.BrentOptimizer;
import org.apache.commons.math3.optimization.univariate.SimpleUnivariateValueChecker;
import org.apache.commons.math3.optimization.univariate.BracketFinder;
import org.junit.Test;

public class PowellOptimizerTest {

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorWithInvalidArguments() {
        new PowellOptimizer(-0.1, 0.0);
    }

    @Test
    public void testConstructorWithDefaults() {
        PowellOptimizer optimizer = new PowellOptimizer(1e-6, 1e-8);
        assertNotNull(optimizer.getConvergenceChecker());
        assertEquals(1e-6, optimizer.getRelativeThreshold(), 1e-8);
        assertEquals(1e-8, optimizer.getAbsoluteThreshold(), 1e-8);
        assertEquals(1e-6, optimizer.getLineSearch().getRelativeThreshold(), 1e-8);
        assertEquals(1e-8, optimizer.getLineSearch().getAbsoluteThreshold(), 1e-8);
    }

    @Test
    public void testConstructorWithCustomCheckers() {
        ConvergenceChecker<PointValuePair> customChecker = mock(ConvergenceChecker.class);
        PowellOptimizer optimizer = new PowellOptimizer(1e-6, 1e-8, customChecker);
        assertSame(customChecker, optimizer.getConvergenceChecker());
    }

    @Test
    public void testOptimizeWithDefaultTolerances() {
        PowellOptimizer optimizer = new PowellOptimizer(1e-6, 1e-8);
        MultivariateFunction function = mock(MultivariateFunction.class);
        PointValuePair result = optimizer.optimize(function, GoalType.MINIMIZE, new double[]{}, null);
        assertNotNull(result);
        assertArrayEquals(new double[0], result.getPoint(), 1e-8);
        verify(function).value(result.getPoint());
    }

    @Test
    public void testOptimizeWithCustomTolerances() {
        PowellOptimizer optimizer = new PowellOptimizer(1e-5, 1e-7);
        MultivariateFunction function = mock(MultivariateFunction.class);
        PointValuePair result = optimizer.optimize(function, GoalType.MINIMIZE, new double[]{}, null);
        assertNotNull(result);
        assertArrayEquals(new double[0], result.getPoint(), 1e-7);
        verify(function).value(result.getPoint());
    }

    @Test
    public void testOptimizeWithCustomLineSearchTolerances() {
        PowellOptimizer optimizer = new PowellOptimizer(1e-6, 1e-8, 1e-5, 1e-7);
        MultivariateFunction function = mock(MultivariateFunction.class);
        PointValuePair result = optimizer.optimize(function, GoalType.MINIMIZE, new double[]{}, null);
        assertNotNull(result);
        assertArrayEquals(new double[0], result.getPoint(), 1e-7);
        verify(function).value(result.getPoint());
    }

    @Test
    public void testOptimizeWithCustomConvergenceChecker() {
        ConvergenceChecker<PointValuePair> customChecker = mock(ConvergenceChecker.class);
        PowellOptimizer optimizer = new PowellOptimizer(1e-6, 1e-8, customChecker);
        MultivariateFunction function = mock(MultivariateFunction.class);
        PointValuePair result = optimizer.optimize(function, GoalType.MINIMIZE, new double[]{}, null);
        assertNotNull(result);
        verify(customChecker).converged(anyInt(), any(PointValuePair.class), any(PointValuePair.class));
    }

    @Test
    public void testOptimizeWithCustomFunction() {
        MultivariateFunction function = new MultivariateFunction() {
            @Override
            public double value(double[] point) {
                return Math.pow(point[0], 2) + Math.pow(point[1], 2);
            }

            @Override
            public int getNumberOfVariables() {
                return 2;
            }
        };
        PowellOptimizer optimizer = new PowellOptimizer(1e-6, 1e-8);
        PointValuePair result = optimizer.optimize(function, GoalType.MINIMIZE, new double[]{1, 1}, null);
        assertArrayEquals(new double[]{0, 0}, result.getPoint(), 1e-6);
        assertEquals(0.0, result.getValue(), 1e-6);
    }

    @Test
    public void testOptimizeWithCustomStartPoint() {
        MultivariateFunction function = new MultivariateFunction() {
            @Override
            public double value(double[] point) {
                return Math.pow(point[0] - 1, 2) + Math.pow(point[1] - 1, 2);
            }

            @Override
            public int getNumberOfVariables() {
                return 2;
            }
        };
        PowellOptimizer optimizer = new PowellOptimizer(1e-6, 1e-8);
        PointValuePair result = optimizer.optimize(function, GoalType.MINIMIZE, new double[]{1.5, 1.5}, null);
        assertArrayEquals(new double[]{1, 1}, result.getPoint(), 1e-6);
        assertEquals(1.0, result.getValue(), 1e-6);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOptimizeWithNullFunction() {
        PowellOptimizer optimizer = new PowellOptimizer(1e-6, 1e-8);
        optimizer.optimize(null, GoalType.MINIMIZE, new double[]{}, null);
    }

    @Test
    public void testOptimizeWithNullStartPoint() {
        PowellOptimizer optimizer = new PowellOptimizer(1e-6, 1e-8);
        MultivariateFunction function = mock(MultivariateFunction.class);
        optimizer.optimize(function, GoalType.MINIMIZE, null, null);
    }

    @Test
    public void testOptimizeWithNullGoalType() {
        PowellOptimizer optimizer = new PowellOptimizer(1e-6, 1e-8);
        MultivariateFunction function = mock(MultivariateFunction.class);
        optimizer.optimize(function, null, new double[]{}, null);
    }

    @Test
    public void testLineSearch() {
        MultivariateFunction function = new MultivariateFunction() {
            @Override
            public double value(double[] point) {
                return Math.pow(point[0], 2) + Math.pow(point[1], 2);
            }

            @Override
            public int getNumberOfVariables() {
                return 2;
            }
        };
        PowellOptimizer optimizer = new PowellOptimizer(1e-6, 1e-8);
        double[] p = new double[]{1, 1};
        double[] d = new double[]{1, 1};
        UnivariatePointValuePair result = optimizer.getLineSearch().search(p, d);
        assertNotNull(result);
        assertEquals(1.0, result.getValue(), 1e-6);
        assertEquals(Math.sqrt(2) / 2, result.getPoint(), 1e-6);
    }
}