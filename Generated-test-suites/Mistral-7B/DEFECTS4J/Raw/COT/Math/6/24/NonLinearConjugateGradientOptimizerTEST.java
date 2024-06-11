package org.apache.commons.math3.optimization.general;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.analysis.solvers.BrentSolver;
import org.apache.commons.math3.analysis.solvers.UnivariateSolver;
import org.apache.commons.math3.exception.MathIllegalStateException;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleValueChecker;
import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.apache.commons.math3.optim.initializer.AverageInitializer;
import org.apache.commons.math3.optim.preconditioner.IdentityPreconditioner;
import org.apache.commons.math3.optim.preconditioner.Preconditioner;
import org.apache.commons.math3.optim.provider.GradientProvider;
import org.apache.commons.math3.optim.provider.ObjectiveFunctionProvider;
import org.apache.commons.math3.optim.provider.SimpleGradientProvider;
import org.apache.commons.math3.optim.provider.SimpleObjectiveFunctionProvider;
import org.apache.commons.math3.optim.provider.UnivariateObjectiveFunction;
import org.apache.commons.math3.test.AbstractTest;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Arrays;

/**
 * Tests for {@link NonLinearConjugateGradientOptimizer}.
 */
public class NonLinearConjugateGradientOptimizerTest extends AbstractTest {

    /**
     * Tests constructor with default settings.
     */
    @Test
    public void testDefaultConstructor() {
        ConjugateGradientFormula formula = ConjugateGradientFormula.FLETCHER_REEVES;
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(formula);
        assertNotNull(optimizer);
    }

    /**
     * Tests constructor with custom settings.
     */
    @Test
    public void testCustomConstructor() {
        ConjugateGradientFormula formula = ConjugateGradientFormula.FLETCHER_REEVES;
        ConvergenceChecker<PointValuePair> checker = new SimpleValueChecker();
        UnivariateSolver solver = new BrentSolver();
        Preconditioner preconditioner = new IdentityPreconditioner();
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(formula, checker, solver, preconditioner);
        assertNotNull(optimizer);
    }

    /**
     * Tests setInitialStep method.
     */
    @Test
    public void testSetInitialStep() {
        ConjugateGradientFormula formula = ConjugateGradientFormula.FLETCHER_REEVES;
        ConvergenceChecker<PointValuePair> checker = new SimpleValueChecker();
        UnivariateSolver solver = new BrentSolver();
        Preconditioner preconditioner = new IdentityPreconditioner();
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(formula, checker, solver, preconditioner);

        optimizer.setInitialStep(2.5);
        assertEquals(2.5, optimizer.getInitialStep(), 0.001);

        optimizer.setInitialStep(-1.0);
        assertEquals(1.0, optimizer.getInitialStep(), 0.001);
    }

    /**
     * Tests doOptimize method with simple test function.
     */
    @Test
    public void testDoOptimizeSimpleFunction() {
        ObjectiveFunctionProvider objectiveFunctionProvider = new SimpleObjectiveFunctionProvider(new QuadraticFunction(), new AverageInitializer(5));
        GradientProvider gradientProvider = new SimpleGradientProvider(objectiveFunctionProvider);
        ConjugateGradientFormula formula = ConjugateGradientFormula.FLETCHER_REEVES;
        ConvergenceChecker<PointValuePair> checker = new SimpleValueChecker();
        UnivariateSolver solver = new BrentSolver();
        Preconditioner preconditioner = new IdentityPreconditioner();
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(formula, checker, solver, preconditioner);

        optimizer.optimize(gradientProvider);

        double[] expectedSolution = {1.0, 1.0};
        PointValuePair solution = optimizer.getPoint();
        assertArrayEquals(expectedSolution, solution.getPoint(), 0.01);
        assertEquals(0.0, solution.getValue(), 0.01);
    }

    /**
     * Tests doOptimize method with complex test function.
     */
    @Test
    public void testDoOptimizeComplexFunction() {
        ObjectiveFunctionProvider objectiveFunctionProvider = new SimpleObjectiveFunctionProvider(new RastriginFunction(), new AverageInitializer(10));
        GradientProvider gradientProvider = new SimpleGradientProvider(objectiveFunctionProvider);
        ConjugateGradientFormula formula = ConjugateGradientFormula.FLETCHER_REEVES;
        ConvergenceChecker<PointValuePair> checker = new SimpleValueChecker();
        UnivariateSolver solver = new BrentSolver();
        Preconditioner preconditioner = new IdentityPreconditioner();
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(formula, checker, solver, preconditioner);

        optimizer.optimize(gradientProvider);

        double[] expectedSolution = {-3.1415926536, -2.718281828};
        PointValuePair solution = optimizer.getPoint();
        assertArrayEquals(expectedSolution, solution.getPoint(), 0.01);
        assertEquals(0.0, solution.getValue(), 0.01);
    }

    /**
     * Tests exception handling when the optimizer cannot bracket the optimum.
     */
    @Test(expected = MathIllegalStateException.class)
    public void testCannotBracketOptimum() {
        ObjectiveFunctionProvider objectiveFunctionProvider = new SimpleObjectiveFunctionProvider(new RastriginFunction(), new AverageInitializer(2));
        GradientProvider gradientProvider = new SimpleGradientProvider(objectiveFunctionProvider);
        ConjugateGradientFormula formula = ConjugateGradientFormula.FLETCHER_REEVES;
        ConvergenceChecker<PointValuePair> checker = new SimpleValueChecker();
        UnivariateSolver solver = new BrentSolver();
        Preconditioner preconditioner = new IdentityPreconditioner();
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(formula, checker, solver, preconditioner);

        optimizer.optimize(gradientProvider);
    }

    /**
     * Tests exception handling when the optimizer encounters an illegal state.
     */
    @Test(expected = MathIllegalStateException.class)
    public void testIllegalState() {
        ObjectiveFunctionProvider objectiveFunctionProvider = new SimpleObjectiveFunctionProvider(new QuadraticFunction(), new AverageInitializer(5));
        GradientProvider gradientProvider = new SimpleGradientProvider(objectiveFunctionProvider);
        ConjugateGradientFormula formula = null;
        ConvergenceChecker<PointValuePair> checker = new SimpleValueChecker();
        UnivariateSolver solver = new BrentSolver();
        Preconditioner preconditioner = new IdentityPreconditioner();
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(formula, checker, solver, preconditioner);

        optimizer.optimize(gradientProvider);
    }

    /**
     * Tests exception handling when the convergence checker is not provided.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNullConvergenceChecker() {
        ObjectiveFunctionProvider objectiveFunctionProvider = new SimpleObjectiveFunctionProvider(new QuadraticFunction(), new AverageInitializer(5));
        GradientProvider gradientProvider = new SimpleGradientProvider(objectiveFunctionProvider);
        ConjugateGradientFormula formula = ConjugateGradientFormula.FLETCHER_REEVES;
        UnivariateSolver solver = new BrentSolver();
        Preconditioner preconditioner = new IdentityPreconditioner();
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(formula, null, solver, preconditioner);

        optimizer.optimize(gradientProvider);
    }

    /**
     * Tests exception handling when the line search solver is not provided.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNullLineSearchSolver() {
        ObjectiveFunctionProvider objectiveFunctionProvider = new SimpleObjectiveFunctionProvider(new QuadraticFunction(), new AverageInitializer(5));
        GradientProvider gradientProvider = new SimpleGradientProvider(objectiveFunctionProvider);
        ConjugateGradientFormula formula = ConjugateGradientFormula.FLETCHER_REEVES;
        ConvergenceChecker<PointValuePair> checker = new SimpleValueChecker();
        Preconditioner preconditioner = new IdentityPreconditioner();
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(formula, checker, null, preconditioner);

        optimizer.optimize(gradientProvider);
    }

    /**
     * Tests exception handling when the preconditioner is not provided.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNullPreconditioner() {
        ObjectiveFunctionProvider objectiveFunctionProvider = new SimpleObjectiveFunctionProvider(new QuadraticFunction(), new AverageInitializer(5));
        GradientProvider gradientProvider = new SimpleGradientProvider(objectiveFunctionProvider);
        ConjugateGradientFormula formula = ConjugateGradientFormula.FLETCHER_REEVES;
        ConvergenceChecker<PointValuePair> checker = new SimpleValueChecker();
        UnivariateSolver solver = new BrentSolver();
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(formula, checker, solver, null);

        optimizer.optimize(gradientProvider);
    }

    /**
     * Tests exception handling when the goal type is not provided.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNullGoalType() {
        ObjectiveFunctionProvider objectiveFunctionProvider = new SimpleObjectiveFunctionProvider(new QuadraticFunction(), new AverageInitializer(5));
        GradientProvider gradientProvider = new SimpleGradientProvider(objectiveFunctionProvider);
        ConjugateGradientFormula formula = ConjugateGradientFormula.FLETCHER_REEVES;
        ConvergenceChecker<PointValuePair> checker = new SimpleValueChecker();
        UnivariateSolver solver = new BrentSolver();
        Preconditioner preconditioner = new IdentityPreconditioner();
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(formula, checker, solver, preconditioner, null);

        optimizer.optimize(gradientProvider);
    }

    /**
     * Tests QuadraticFunction with Fletcher-Reeves update formula.
     */
    private static class QuadraticFunction implements UnivariateObjectiveFunction {

        @Override
        public double value(double x) {
            return x * x;
        }

        @Override
        public double[] gradient(double x) {
            return new double[]{2.0 * x};
        }
    }

    /**
     * Tests RastriginFunction with Fletcher-Reeves update formula.
     */
    private static class RastriginFunction implements UnivariateObjectiveFunction {

        @Override
        public double value(double x) {
            double sum = 0;
            for (int i = 0; i <= 10; ++i) {
                double term = Math.pow(Math.PI, 2) * Math.pow(i, 2);
                sum += term + (i - Math.abs(Math.sin(i * Math.PI * x) * 2));
            }
            sum *= 10;
            return sum;
        }

        @Override
        public double[] gradient(double x) {
            double[] gradient = new double[11];
            for (int i = 0; i <= 10; ++i) {
                double term1 = Math.PI * Math.PI * i * 2;
                double term2 = Math.sin(i * Math.PI * x) * 2;
                gradient[i] = -2 * term1 * Math.cos(i * Math.PI * x) - term2 * i * 2 * Math.PI;
            }
            gradient[0] = -2 * Math.PI * Math.sin(Math.PI * x);
            return gradient;
        }
    }
}