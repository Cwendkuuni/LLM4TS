package org.apache.commons.math3.optimization.direct;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.MinEval;
import org.apache.commons.math3.optim.GoalType;
import org.apache.commons.math3.optim.InitialGuess;
import org.apache.commons.math3.optim.NonUnivariateOptimizer;
import org.apache.commons.math3.optim.univariate.BracketFinder;
import org.apache.commons.math3.optim.univariate.BrentOptimizer;
import org.apache.commons.math3.optim.univariate.UnivariateOptimizer;
import org.apache.commons.math3.optim.univariate.SimpleUnivariateValueChecker;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.junit.Test;
import static org.junit.Assert.*;

public class PowellOptimizerTest {

    @Test
    public void testMinimization() {
        MultivariateFunction function = (x) -> Math.pow(Math.sin(x[0]) + Math.sin(x[1]), 2.0);
        PowellOptimizer optimizer = new PowellOptimizer(1e-6, 1e-6);

        PointValuePair result = optimizer.optimize(function,
                new GoalType(GoalType.MINIMIZE),
                new InitialGuess(new double[]{1.0, 1.0}),
                new BracketFinder(),
                new SimpleUnivariateValueChecker(1e-5, 1e-3));

        assertEquals(0.0, result.getValue(), 1e-6);
        assertArrayEquals(new double[]{Math.PI / 2, Math.PI / 4}, result.getPoint(), 1e-6);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testNegativeRelativeThreshold() {
        new PowellOptimizer(-1e-6, 1e-6);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testSmallRelativeThreshold() {
        new PowellOptimizer(1e-8, 1e-6);
    }

    @Test
    public void testZeroAbsoluteThreshold() {
        PowellOptimizer optimizer = new PowellOptimizer(1e-6, 0);
        assertThrows(NumberIsTooSmallException.class, () -> optimizer.optimize(new UnivariateFunction() {
            public double value(double x) {
                return Math.exp(-Math.pow(x, 2.0));
            }
        }, new GoalType(GoalType.MINIMIZE),
                new InitialGuess(0),
                new BracketFinder(),
                optimizer.getConvergenceChecker()));
    }

    @Test
    public void testMaximization() {
        MultivariateFunction function = (x) -> Math.pow(Math.cos(x[0]) + Math.cos(x[1]), 2.0);
        PowellOptimizer optimizer = new PowellOptimizer(1e-6, 1e-6);

        PointValuePair result = optimizer.optimize(function,
                new GoalType(GoalType.MAXIMIZE),
                new InitialGuess(new double[]{1.0, 1.0}),
                new BracketFinder(),
                new SimpleUnivariateValueChecker(1e-5, 1e-3));

        assertEquals(1.0, result.getValue(), 1e-6);
        assertArrayEquals(new double[]{-Math.PI / 2, -Math.PI / 4}, result.getPoint(), 1e-6);
    }

    @Test
    public void testCustomLineSearch() {
        MultivariateFunction function = (x) -> Math.pow(Math.sin(x[0]) + Math.sin(x[1]), 2.0);
        PowellOptimizer optimizer = new PowellOptimizer(1e-6, 1e-6, new CustomLineSearch());

        PointValuePair result = optimizer.optimize(function,
                new GoalType(GoalType.MINIMIZE),
                new InitialGuess(new double[]{1.0, 1.0}),
                new BracketFinder(),
                optimizer.getConvergenceChecker());

        assertEquals(0.0, result.getValue(), 1e-6);
        assertArrayEquals(new double[]{Math.PI / 2, Math.PI / 4}, result.getPoint(), 1e-6);
    }

    private class CustomLineSearch extends BrentOptimizer {
        @Override
        protected int getMaxEvaluations() {
            return MaxEval.UNLIMITED;
        }
    }
}