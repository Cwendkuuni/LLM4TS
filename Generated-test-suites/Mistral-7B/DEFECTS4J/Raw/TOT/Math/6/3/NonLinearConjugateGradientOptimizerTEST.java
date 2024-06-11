package org.apache.commons.math3.optimization.general;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.analysis.solvers.BrentSolver;
import org.apache.commons.math3.analysis.solvers.UnivariateSolver;
import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.MaxEvalException;
import org.apache.commons.math3.exception.NoBracketException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.UnsupportedOperationException;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleValueChecker;
import org.apache.commons.math3.optim.UnconstrainedOptimizationProblem;
import org.apache.commons.math3.optim.initializer.AroundMinimumInitializer;
import org.apache.commons.math3.optim.nonlinear.NonlinearOptimizer;
import org.apache.commons.math3.optim.nonlinear.conjgrad.NonlinearConjugateGradientOptimizer;
import org.apache.commons.math3.optim.nonlinear.conjgrad.ConjugateGradientFormula;
import org.apache.commons.math3.optim.nonlinear.conjgrad.IdentityPreconditioner;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class NonLinearConjugateGradientOptimizerTest {

    private NonlinearOptimizer optimizer;
    private UnivariateFunction function;
    private UnconstrainedOptimizationProblem problem;

    @Before
    public void setUp() {
        function = new MyTestFunction();
        problem = new UnconstrainedOptimizationProblem(function, new double[]{1.0});

        optimizer = new NonlinearConjugateGradientOptimizer(ConjugateGradientFormula.FLETCHER_REEVES,
                new SimpleValueChecker(1e-8), new BrentSolver());
    }

    @Test
    public void testOptimize() throws Exception {
        optimizer.setInitialStep(0.1);
        PointValuePair solution = optimizer.optimize(problem);
        assertEquals("Optimum not found.", solution.getValue(), function.value(2.0), 1e-8);
        assertEquals("Incorrect number of dimensions.", solution.getPoint().length, 1, 0);
    }

    @Test(expected = NullArgumentException.class)
    public void testNullFunction() {
        new NonlinearConjugateGradientOptimizer(ConjugateGradientFormula.FLETCHER_REEVES,
                new SimpleValueChecker(1e-8), new BrentSolver(), null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testNoGradient() {
        new NonlinearConjugateGradientOptimizer(ConjugateGradientFormula.FLETCHER_REEVES,
                new SimpleValueChecker(1e-8), new BrentSolver(), new MyTestFunctionNoGradient());
    }

    @Test(expected = ConvergenceException.class)
    public void testConvergenceException() throws Exception {
        optimizer = new NonlinearConjugateGradientOptimizer(ConjugateGradientFormula.FLETCHER_REEVES,
                new SimpleValueChecker(1e-8), new BrentSolver());

        optimizer.optimize(new UnconstrainedOptimizationProblem(new MyTestFunctionWithConvergenceException(),
                new double[]{1.0}));
    }

    @Test(expected = NoBracketException.class)
    public void testNoBracketException() throws Exception {
        optimizer = new NonlinearConjugateGradientOptimizer(ConjugateGradientFormula.FLETCHER_REEVES,
                new SimpleValueChecker(1e-8), new BrentSolver());

        optimizer.optimize(new UnconstrainedOptimizationProblem(new MyTestFunctionWithNoBracket(),
                new double[]{1.0}));
    }

    @Test(expected = MaxEvalException.class)
    public void testMaxEvalException() throws Exception {
        optimizer = new NonlinearConjugateGradientOptimizer(ConjugateGradientFormula.FLETCHER_REEVES,
                new SimpleValueChecker(1e-8), new BrentSolver(),
                new MyTestFunctionWithMaxEvalException(), 1);
    }

    @Test
    public void testOptimizeWithPreconditioner() throws Exception {
        optimizer = new NonlinearConjugateGradientOptimizer(ConjugateGradientFormula.FLETCHER_REEVES,
                new SimpleValueChecker(1e-8), new BrentSolver(),
                new IdentityPreconditioner());

        PointValuePair solution = optimizer.optimize(problem);
        assertEquals("Optimum not found.", solution.getValue(), function.value(2.0), 1e-8);
        assertEquals("Incorrect number of dimensions.", solution.getPoint().length, 1, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidUpdateFormula() {
        new NonlinearConjugateGradientOptimizer("INVALID", null, null, null);
    }

    private static class MyTestFunction implements UnivariateFunction {

        @Override
        public double value(double x) {
            return Math.pow(Math.sin(x), 2);
        }

        @Override
        public double[] gradient(double x) {
            double[] result = new double[1];
            result[0] = 2 * Math.sin(x) * Math.cos(x);
            return result;
        }
    }

    private static class MyTestFunctionNoGradient implements UnivariateFunction {

        @Override
        public double value(double x) {
            return Math.pow(Math.sin(x), 2);
        }

        @Override
        public double[] gradient(double x) {
            throw new UnsupportedOperationException();
        }
    }

    private static class MyTestFunctionWithConvergenceException extends MyTestFunction {

        @Override
        public double value(double x) throws ConvergenceException {
            throw new ConvergenceException("Convergence Exception.");
        }
    }

    private static class MyTestFunctionWithNoBracket extends MyTestFunction {

        @Override
        public double value(double x) {
            double value = Math.pow(Math.sin(x), 2);
            if (value > 0) {
                throw new NoBracketException();
            }
            return value;
        }
    }

    private static class MyTestFunctionWithMaxEvalException extends MyTestFunction {

        private int maxEvaluations;

        public MyTestFunctionWithMaxEvalException() {
            this.maxEvaluations = 0;
        }

        @Override
        public double value(double x) throws MaxEvalException {
            maxEvaluations++;
            if (maxEvaluations > 1) {
                throw new MaxEvalException("Maximum number of evaluations exceeded.");
            }
            return Math.pow(Math.sin(x), 2);
        }

        public int getMaxEvaluations() {
            return maxEvaluations;
        }
    }
}