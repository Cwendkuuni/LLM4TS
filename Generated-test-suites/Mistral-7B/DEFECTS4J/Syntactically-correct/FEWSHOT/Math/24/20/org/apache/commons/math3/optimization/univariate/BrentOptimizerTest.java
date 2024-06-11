package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.optimization.UnivariatePointValuePair;
import org.apache.commons.math3.optimization.goaltype.GoalType;
import org.apache.commons.math3.optimization.univariate.BrentOptimizer;
import org.apache.commons.math3.optimization.univariate.BrentOptimizer.ConvergenceChecker;
import org.apache.commons.math3.optimization.univariate.BracketedUnivariateFunction;
import org.apache.commons.math3.special.E;
import org.junit.Test;

public class BrentOptimizerTest {

    @Test
    public void testBrentOptimizer() {
        BracketedUnivariateFunction function = new BracketedUnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.exp(Math.sin(x)) - E.E;
            }
        };

        BrentOptimizer optimizer = new BrentOptimizer(1e-5, 1e-6);
        UnivariatePointValuePair solution = optimizer.optimize(function, GoalType.MINIMIZE);

        assertEquals(Math.PI / 2, solution.getPoint(), 1e-5);
        assertEquals(-1.648721269889538e-11, solution.getValue(), 1e-12);
    }

    @Test
    public void testBrentOptimizerNoConvergenceChecker() {
        BracketedUnivariateFunction function = new BracketedUnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(Math.sin(x), 2);
            }
        };

        BrentOptimizer optimizer = new BrentOptimizer(0.01, 0.02);
        UnivariatePointValuePair solution = optimizer.optimize(function, GoalType.MINIMIZE);

        assertEquals(Math.PI / 2, solution.getPoint(), 1e-3);
        assertEquals(1, solution.getValue(), 1e-3);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testBrentOptimizerInvalidAbsThreshold() {
        BrentOptimizer optimizer = new BrentOptimizer(-1, 0.01, null);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testBrentOptimizerInvalidRelThreshold() {
        BrentOptimizer optimizer = new BrentOptimizer(0.0, 0.01, null);
    }

    @Test
    public void testBrentOptimizerWithConvergenceChecker() {
        BracketedUnivariateFunction function = new BracketedUnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(Math.sin(x), 2);
            }
        };

        ConvergenceChecker<UnivariatePointValuePair> checker = new ConvergenceChecker<UnivariatePointValuePair>() {
            @Override
            public boolean converged(int iter, UnivariatePointValuePair previous, UnivariatePointValuePair current) {
                return current.getValue() < 0.001;
            }
        };

        BrentOptimizer optimizer = new BrentOptimizer(0.01, 0.02, checker);
        UnivariatePointValuePair solution = optimizer.optimize(function, GoalType.MINIMIZE);

        assertEquals(Math.PI / 2, solution.getPoint(), 1e-3);
        assertEquals(0, solution.getValue(), 1e-3);
    }

    @Test
    public void testBrentOptimizerWithConvergenceCheckerIterationLimit() {
        BracketedUnivariateFunction function = new BracketedUnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) * Math.cos(x);
            }
        };

        ConvergenceChecker<UnivariatePointValuePair> checker = new ConvergenceChecker<UnivariatePointValuePair>() {
            @Override
            public boolean converged(int iter, UnivariatePointValuePair previous, UnivariatePointValuePair current) {
                return iter > 10;
            }
        };

        BrentOptimizer optimizer = new BrentOptimizer(0.01, 0.02, checker);
        UnivariatePointValuePair solution = optimizer.optimize(function, GoalType.MINIMIZE);

        assertNull(solution);
    }

    @Test
    public void testBrentOptimizerWithDifferentGoalTypes() {
        BracketedUnivariateFunction function = new BracketedUnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(Math.sin(x), 2);
            }
        };

        BrentOptimizer optimizerMin = new BrentOptimizer(0.01, 0.02);
        UnivariatePointValuePair solutionMin = optimizerMin.optimize(function, GoalType.MINIMIZE);

        BrentOptimizer optimizerMax = new BrentOptimizer(0.01, 0.02);
        UnivariatePointValuePair solutionMax = optimizerMax.optimize(function, GoalType.MAXIMIZE);

        assertTrue(solutionMin.getValue() < solutionMax.getValue());
    }

    @Test
    public void testBrentOptimizerWithEmptyRange() {
        BracketedUnivariateFunction function = new BracketedUnivariateFunction() {
            @Override
            public double value(double x) {
                return 0;
            }
        };

        BrentOptimizer optimizer = new BrentOptimizer(0, 0);
        UnivariatePointValuePair solution = optimizer.optimize(function, GoalType.MINIMIZE);

        assertNull(solution);
    }
}