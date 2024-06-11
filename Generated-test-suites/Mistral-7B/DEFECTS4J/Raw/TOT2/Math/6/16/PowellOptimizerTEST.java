package org.apache.commons.math3.optimization.direct;

import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.UnivariateFunction;
import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.PointValuePairChecker;
import org.apache.commons.math3.optim.GoalType;
import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Test for {@link PowellOptimizer} class.
 */
public class PowellOptimizerTest {

    /**
     * Test method for {@link PowellOptimizer#PowellOptimizer(double, double)} with default values.
     */
    @Test
    public void testDefaultConstructor() {
        PowellOptimizer powellOptimizer = new PowellOptimizer(1e-4, 1e-6);
        assertEquals(1e-4, powellOptimizer.getRelativeThreshold(), 0.001);
        assertEquals(1e-6, powellOptimizer.getAbsoluteThreshold(), 0.001);
        assertFalse(powellOptimizer.isConvergenceCheckerSet());
    }

    /**
     * Test method for {@link PowellOptimizer#PowellOptimizer(double, double, ConvergenceChecker)}
     */
    @Test
    public void testCustomConstructor() {
        CustomConvergenceChecker customConvergenceChecker = new CustomConvergenceChecker();
        PowellOptimizer powellOptimizer = new PowellOptimizer(1e-4, 1e-6, customConvergenceChecker);
        assertTrue(powellOptimizer.isConvergenceCheckerSet());
        assertEquals(1e-4, powellOptimizer.getRelativeThreshold(), 0.001);
        assertEquals(1e-6, powellOptimizer.getAbsoluteThreshold(), 0.001);
        assertEquals(customConvergenceChecker, powellOptimizer.getConvergenceChecker());
    }

    /**
     * Test method for {@link PowellOptimizer#optimize()} with a simple function.
     */
    @Test
    public void testOptimizeSimpleFunction() {
        PowellOptimizer powellOptimizer = new PowellOptimizer(1e-4, 1e-6);
        double[] startPoint = new double[]{1.0, 2.0};
        UnivariateFunction function = new SineFunction();
        PointValuePair solution = powellOptimizer.optimize(startPoint, function);
        assertArrayEquals(new double[]{0.0, 0.0}, solution.getPoint(), 0.001);
        assertEquals(0.0, solution.getValue(), 0.001);
    }

    /**
     * Test method for {@link PowellOptimizer#optimize()} with a multimodal function.
     */
    @Test
    public void testOptimizeMultimodalFunction() {
        PowellOptimizer powellOptimizer = new PowellOptimizer(1e-4, 1e-6);
        double[] startPoint = new double[]{2.0, 3.0};
        UnivariateFunction function = new RastriginFunction();
        PointValuePair solution = powellOptimizer.optimize(startPoint, function);
        assertArrayEquals(new double[]{1.0, 1.0}, solution.getPoint(), 0.001);
        assertEquals(0.0, solution.getValue(), 0.001);
    }

    /**
     * Test method for {@link PowellOptimizer#optimize()} with a discontinuous function.
     */
    @Test(expected = NumberIsTooSmallException.class)
    public void testOptimizeDiscontinuousFunction() {
        PowellOptimizer powellOptimizer = new PowellOptimizer(1e-4, 1e-6);
        double[] startPoint = new double[]{0.0, 0.0};
        UnivariateFunction function = new AbsFunction();
        powellOptimizer.optimize(startPoint, function);
    }

    /**
     * Test method for {@link PowellOptimizer#optimize()} with a function which does not converge.
     */
    @Test(expected = MaxEval.TooManyEvaluationsException.class)
    public void testOptimizeDivergentFunction() {
        PowellOptimizer powellOptimizer = new PowellOptimizer(1e-4, 1e-6);
        double[] startPoint = new double[]{0.0, 0.0};
        UnivariateFunction function = new DivergentFunction();
        powellOptimizer.optimize(startPoint, function);
    }

    /**
     * Test method for {@link PowellOptimizer#optimize()} with a function which has a local minimum.
     */
    @Test
    public void testOptimizeLocalMinimum() {
        PowellOptimizer powellOptimizer = new PowellOptimizer(1e-4, 1e-6);
        double[] startPoint = new double[]{1.0, 1.0};
        UnivariateFunction function = new SineFunction();
        PointValuePair solution = powellOptimizer.optimize(startPoint, function);
        assertArrayEquals(new double[]{0.0, 0.0}, solution.getPoint(), 0.001);
        assertEquals(Math.PI / 2.0, solution.getValue(), 0.01);
    }

    /**
     * Test method for {@link PowellOptimizer#optimize()} with a function which has a global minimum.
     */
    @Test
    public void testOptimizeGlobalMinimum() {
        PowellOptimizer powellOptimizer = new PowellOptimizer(1e-4, 1e-6);
        double[] startPoint = new double[]{3.0, 4.0};
        UnivariateFunction function = new RosenbrockFunction();
        PointValuePair solution = powellOptimizer.optimize(startPoint, function);
        assertArrayEquals(new double[]{1.0, 1.0}, solution.getPoint(), 0.001);
        assertEquals(1.0, solution.getValue(), 0.01);
    }
}

class CustomConvergenceChecker implements ConvergenceChecker<PointValuePair> {
    @Override
    public boolean converged(int iteration, PointValuePair previous, PointValuePair current) {
        double fPrevious = previous.getValue();
        double fCurrent = current.getValue();
        double relativeDifference = Math.abs(fPrevious - fCurrent) / (Math.abs(fPrevious) + Math.abs(fCurrent) + 0.001);
        return relativeDifference < 1e-3;
    }
}

class SineFunction implements UnivariateFunction {
    @Override
    public double value(double x) {
        return Math.sin(x);
    }
}

class RastriginFunction implements UnivariateFunction {
    @Override
    public double value(double x) {
        double sum = 0.0;
        for (int i = 0; i < x.length; i++) {
            sum += Math.pow(x[i] * Math.PI, 2.0) - Math.pow(Math.sin(2 * Math.PI * x[i]), 2.0) + 10.0 * Math.abs(x[i] - Math.PI);
        }
        sum *= 2.0;
        return sum;
    }
}

class AbsFunction implements UnivariateFunction {
    @Override
    public double value(double x) {
        throw new NumberIsTooSmallException(x);
    }
}

class DivergentFunction implements UnivariateFunction {
    @Override
    public double value(double x) {
        if (x > 0.5) {
            return Math.pow(x - 0.5, 2.0);
        } else {
            return -Math.pow(x, 2.0);
        }
    }
}