package org.apache.commons.math3.optimization.general;

import static org.junit.Assert.*;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.analysis.solvers.BrentSolver;
import org.apache.commons.math3.analysis.solvers.UnivariateSolver;
import org.apache.commons.math3.exception.MathIllegalStateException;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.PointValuePair;
import org.apache.commons.math3.optimization.SimpleValueChecker;
import org.apache.commons.math3.optimization.ConvergenceChecker;
import org.apache.commons.math3.optimization.NonLinearConjugateGradientOptimizer;
import org.apache.commons.math3.optimization.ConjugateGradientFormula;
import org.apache.commons.math3.optimization.Preconditioner;

/**
 * Test cases for {@link NonLinearConjugateGradientOptimizer} class.
 */
public class NonLinearConjugateGradientOptimizerTest {

    /**
     * Test constructor with default {@link SimpleValueChecker checker},
     * {@link BrentSolver line search solver} and
     * {@link IdentityPreconditioner preconditioner}.
     */
    @Test
    public void testDefaultConstructor() {
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.FLETCHER_REEVES);
        assertNotNull(optimizer.getPreconditioner());
        assertInstanceOf(IdentityPreconditioner.class, optimizer.getPreconditioner());
        assertNotNull(optimizer.getSolver());
        assertInstanceOf(BrentSolver.class, optimizer.getSolver());
        assertEquals(ConjugateGradientFormula.FLETCHER_REEVES, optimizer.getUpdateFormula());
        assertInstanceOf(SimpleValueChecker.class, optimizer.getConvergenceChecker());
    }

    /**
     * Test constructor with specified {@link ConvergenceChecker checker},
     * {@link BrentSolver line search solver} and
     * {@link IdentityPreconditioner preconditioner}.
     */
    @Test
    public void testCustomCheckerConstructor() {
        ConvergenceChecker<PointValuePair> customChecker = new ConvergenceChecker<PointValuePair>() {
            @Override
            public boolean converged(int iteration, PointValuePair current, PointValuePair previous) {
                return (current.getValue() < 0.01);
            }
        };
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(
                ConjugateGradientFormula.FLETCHER_REEVES, customChecker);
        assertSame(customChecker, optimizer.getConvergenceChecker());
    }

    /**
     * Test constructor with specified {@link ConjugateGradientFormula formula},
     * {@link SimpleValueChecker checker},
     * {@link BrentSolver line search solver} and
     * {@link IdentityPreconditioner preconditioner}.
     */
    @Test
    public void testCustomFormulaConstructor() {
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(
                ConjugateGradientFormula.POLAK_RIBIERE, new SimpleValueChecker(), new BrentSolver());
        assertEquals(ConjugateGradientFormula.POLAK_RIBIERE, optimizer.getUpdateFormula());
    }

    /**
     * Test constructor with specified {@link ConjugateGradientFormula formula},
     * {@link SimpleValueChecker checker},
     * {@link BrentSolver line search solver} and
     * {@link CustomPreconditioner preconditioner}.
     */
    @Test
    public void testCustomPreconditionerConstructor() {
        Preconditioner customPreconditioner = new CustomPreconditioner();
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(
                ConjugateGradientFormula.FLETCHER_REEVES, new SimpleValueChecker(), new BrentSolver(), customPreconditioner);
        assertSame(customPreconditioner, optimizer.getPreconditioner());
    }

    /**
     * Test constructor with specified {@link ConjugateGradientFormula formula},
     * {@link ConvergenceChecker checker},
     * {@link CustomLineSearchSolver line search solver} and
     * {@link IdentityPreconditioner preconditioner}.
     */
    @Test
    public void testCustomLineSearchSolverConstructor() {
        UnivariateSolver customLineSearchSolver = new CustomLineSearchSolver();
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(
                ConjugateGradientFormula.FLETCHER_REEVES, new SimpleValueChecker(), customLineSearchSolver, new IdentityPreconditioner());
        assertSame(customLineSearchSolver, optimizer.getSolver());
    }

    /**
     * Test setting the initial step.
     */
    @Test
    public void testSetInitialStep() {
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(
                ConjugateGradientFormula.FLETCHER_REEVES, new SimpleValueChecker(), new BrentSolver());
        optimizer.setInitialStep(2.0);
        assertEquals(2.0, optimizer.getInitialStep());
    }

    /**
     * Test optimization with simple test function.
     */
    @Test
    public void testOptimization() {
        UnivariateFunction testFunction = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2);
            }

            @Override
            public double derivative(double x) {
                return 2 * x;
            }
        };
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(
                ConjugateGradientFormula.FLETCHER_REEVES, new SimpleValueChecker(), new BrentSolver());
        double[] startPoint = {1.0};
        optimizer.optimize(testFunction, startPoint, GoalType.MINIMIZE);
        assertEquals(0.0, optimizer.getPoint()[0], 1e-6);
    }

    /**
     * Test optimization with custom preconditioner.
     */
    @Test
    public void testOptimizationWithCustomPreconditioner() {
        UnivariateFunction testFunction = new UnivariateFunction() {
            @Override
            public double value(double[] x) {
                double sum = 0;
                for (int i = 0; i < x.length; i++) {
                    sum += Math.pow(x[i], 2);
                }
                return sum;
            }

            @Override
            public double[] derivative(double[] x) {
                double[] gradient = new double[x.length];
                for (int i = 0; i < x.length; i++) {
                    gradient[i] = 2 * x[i];
                }
                return gradient;
            }
        };
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(
                ConjugateGradientFormula.FLETCHER_REEVES, new SimpleValueChecker(), new BrentSolver());
        optimizer.setPreconditioner(new CustomPreconditioner(testFunction));
        double[] startPoint = {1.0, 1.0};
        optimizer.optimize(testFunction, startPoint, GoalType.MINIMIZE);
        assertEquals(1.0, optimizer.getPoint()[0], 1e-6);
        assertEquals(1.0, optimizer.getPoint()[1], 1e-6);
    }

    /**
     * Custom preconditioner implementation.
     */
    private static class CustomPreconditioner implements Preconditioner {

        private final UnivariateFunction function;

        public CustomPreconditioner(UnivariateFunction function) {
            this.function = function;
        }

        @Override
        public double[] precondition(double[] variables, double[] r) {
            double[] grad = function.derivative(variables);
            double[] preconditioned = new double[variables.length];
            for (int i = 0; i < variables.length; i++) {
                preconditioned[i] = r[i] / (grad[i] - r[i] * function.value(variables)[i]);
            }
            return preconditioned;
        }
    }

    /**
     * Custom line search solver implementation.
     */
    private static class CustomLineSearchSolver implements UnivariateSolver {

        @Override
        public double solve(int maxIterations, UnivariateFunction function, double initialGuess, double stepSize, double tolerance) {
            throw new UnsupportedOperationException("Custom line search solver not implemented.");
        }

        @Override
        public int getIterations() {
            return 0;
        }

        @Override
        public int getEvaluations() {
            return 0;
        }

        @Override
        public double getBracketEnd() {
            return Double.MAX_VALUE;
        }
    }
}