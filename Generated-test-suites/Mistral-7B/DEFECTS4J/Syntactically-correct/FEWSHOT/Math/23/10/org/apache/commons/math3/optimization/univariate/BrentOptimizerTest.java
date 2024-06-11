package org.apache.commons.cli.optimization.univariate;

import static org.junit.Assert.*;

import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.UnivariateFunction;
import org.apache.commons.math3.optim.UnivariatePointValuePair;
import org.apache.commons.math3.optim.SimpleBounds;
import org.junit.Test;

import static org.mockito.Mockito.*;

public class BrentOptimizerTest {
    private UnivariateFunction function;
    private ConvergenceChecker<UnivariatePointValuePair> checker;
    private BrentOptimizer optimizer;

    @Test
    public void testBrentOptimizer() {
        function = mock(UnivariateFunction.class);
        checker = mock(ConvergenceChecker.class);

        optimizer = new BrentOptimizer(1e-4, 1e-4, checker);
        assertEquals(1e-4, optimizer.getRelativeThreshold(), 0.0);
        assertEquals(1e-4, optimizer.getAbsoluteThreshold(), 0.0);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testBrentOptimizerNegativeAbsoluteThreshold() {
        function = mock(UnivariateFunction.class);
        checker = mock(ConvergenceChecker.class);

        new BrentOptimizer(1e-4, -1e-4, checker);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testBrentOptimizerRelativeThresholdTooSmall() {
        function = mock(UnivariateFunction.class);
        checker = mock(ConvergenceChecker.class);

        new BrentOptimizer(1e-9, 1e-4, checker);
    }

    @Test
    public void testDoOptimizeWithMinFunction() {
        double[] expectedX = {2.0, 1.5, 1.25, 1.1666666666666667, 1.1234567891234567};
        double[] expectedFx = {6.0, 2.25, 1.1215151515151515, 0.5, 0.1234567891234567};

        function = args -> 6.0 * Math.pow(args[0] - 2.0, 2.0);
        checker = args -> true;

        optimizer = new BrentOptimizer(1e-4, 1e-4, checker);

        SimpleBounds bounds = new SimpleBounds(0.1, 3.1);
        UnivariatePointValuePair result = optimizer.optimize(function, bounds, null);

        for (int i = 0; i < expectedX.length; i++) {
            assertEquals(expectedX[i], result.getX(), 0.0001);
            assertEquals(expectedFx[i], result.getValue(), 0.0001);
        }
    }

    @Test
    public void testDoOptimizeWithMinAndConvergenceChecker() {
        double[] expectedX = {1.5, 1.25, 1.1666666666666667, 1.1234567891234567, 1.1111111111111111};
        double[] expectedFx = {2.25, 1.1215151515151515, 0.5, 0.1234567891234567, 0.1111111111111111};

        function = args -> Math.pow(args[0] - 1.5, 2.0);
        checker = args -> Math.abs(args.getCurrentX() - args.getPreviousX()) < 0.01;

        optimizer = new BrentOptimizer(1e-4, 1e-4, checker);

        SimpleBounds bounds = new SimpleBounds(1.0, 2.0);
        UnivariatePointValuePair result = optimizer.optimize(function, bounds, null);

        for (int i = 0; i < expectedX.length; i++) {
            assertEquals(expectedX[i], result.getX(), 0.0001);
            assertEquals(expectedFx[i], result.getValue(), 0.0001);
        }
    }

    @Test
    public void testDoOptimizeWithMaxFunction() {
        double[] expectedX = {1.0, 1.125, 1.1875, 1.25, 1.34375};
        double[] expectedFx = {5.0, 1.321525, 1.215, 1.0, 0.65625};

        function = args -> 5.0 * Math.pow(args[0] - 1.0, 2.0);
        checker = args -> true;

        optimizer = new BrentOptimizer(1e-4, 1e-4, checker);

        SimpleBounds bounds = new SimpleBounds(0.1, 2.1);
        UnivariatePointValuePair result = optimizer.optimize(function, bounds, null);

        for (int i = 0; i < expectedX.length; i++) {
            assertEquals(expectedX[i], result.getX(), 0.0001);
            assertEquals(expectedFx[i], result.getValue(), 0.0001);
        }
    }

    @Test
    public void testDoOptimizeWithMaxAndConvergenceChecker() {
        double[] expectedX = {1.125, 1.1875, 1.25, 1.34375, 1.421875};
        double[] expectedFx = {1.321525, 1.215, 1.0, 0.65625, 0.51171875};

        function = args -> Math.pow(args[0] - 1.125, 2.0);
        checker = args -> Math.abs(args.getCurrentX() - args.getPreviousX()) < 0.01;

        optimizer = new BrentOptimizer(1e-4, 1e-4, checker);

        SimpleBounds bounds = new SimpleBounds(1.0, 2.0);
        UnivariatePointValuePair result = optimizer.optimize(function, bounds, null);

        for (int i = 0; i < expectedX.length; i++) {
            assertEquals(expectedX[i], result.getX(), 0.0001);
            assertEquals(expectedFx[i], result.getValue(), 0.0001);
        }
    }
}