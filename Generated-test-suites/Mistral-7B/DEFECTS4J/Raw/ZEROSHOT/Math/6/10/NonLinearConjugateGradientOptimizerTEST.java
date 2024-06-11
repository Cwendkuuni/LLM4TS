package org.apache.commons.math3.optimization.general;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.analysis.solvers.BrentSolver;
import org.apache.commons.math3.analysis.solvers.UnivariateSolver;
import org.apache.commons.math3.exception.MathIllegalStateException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.PointValuePair;
import org.apache.commons.math3.optimization.SimpleValueChecker;
import org.apache.commons.math3.optimization.ConvergenceChecker;
import org.apache.commons.math3.optimization.NonLinearConjugateGradientOptimizer;
import org.apache.commons.math3.optimization.ConjugateGradientFormula;
import org.apache.commons.math3.optimization.Preconditioner;
import org.apache.commons.math3.util.FastMath;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test class for {@link NonLinearConjugateGradientOptimizer}.
 */
public class NonLinearConjugateGradientOptimizerTest {

    /**
     * Test constructor and optimization with identity preconditioner and
     * default solver.
     */
    @Test
    public void testIdentityPreconditionerAndDefaultSolver() {
        Function f = new Function("f(x) = x^3 - 4x^2 + 3x - 2");
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(
                ConjugateGradientFormula.FLETCHER_REEVES);
        optimizer.setInitialStep(1.5);
        PointValuePair result = optimizer.optimize(f, -5, 5);
        Assert.assertEquals(1.0, result.getPoint()[0], 0.001);
        Assert.assertEquals(-2.0, result.getValue(), 0.001);
    }

    /**
     * Test constructor with Polak-Ribière update formula, identity
     * preconditioner and default solver.
     */
    @Test
    public void testPolakRibiereUpdateFormulaAndIdentityPreconditioner() {
        Function f = new Function("f(x) = x^3 - 4x^2 + 3x - 2");
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(
                ConjugateGradientFormula.POLAK_RIBIERE);
        optimizer.setInitialStep(1.5);
        PointValuePair result = optimizer.optimize(f, -5, 5);
        Assert.assertEquals(1.0, result.getPoint()[0], 0.001);
        Assert.assertEquals(-2.0, result.getValue(), 0.001);
    }

    /**
     * Test constructor with Fletcher-Reeves update formula, custom
     * preconditioner and default solver.
     */
    @Test
    public void testFletcherReevesUpdateFormulaAndCustomPreconditioner() {
        Function f = new Function("f(x) = x^3 - 4x^2 + 3x - 2");
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(
                ConjugateGradientFormula.FLETCHER_REEVES);
        optimizer.setInitialStep(1.5);
        SimpleValueChecker checker = new SimpleValueChecker(1e-5);
        optimizer.setConvergenceChecker(checker);
        Preconditioner preconditioner = new Preconditioner() {
            @Override
            public double[] precondition(double[] variables, double[] r) {
                for (int i = 0; i < r.length; ++i) {
                    r[i] *= 2;
                }
                return r;
            }
        };
        optimizer.setPreconditioner(preconditioner);
        PointValuePair result = optimizer.optimize(f, -5, 5);
        Assert.assertEquals(1.5, result.getPoint()[0], 0.001);
        Assert.assertEquals(-0.625, result.getValue(), 0.001);
    }

    /**
     * Test constructor with Fletcher-Reeves update formula, custom
     * preconditioner and Brent solver.
     */
    @Test
    public void testFletcherReevesUpdateFormulaAndCustomPreconditionerWithBrentSolver() {
        Function f = new Function("f(x) = x^3 - 4x^2 + 3x - 2");
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(
                ConjugateGradientFormula.FLETCHER_REEVES);
        optimizer.setInitialStep(1.5);
        SimpleValueChecker checker = new SimpleValueChecker(1e-5);
        optimizer.setConvergenceChecker(checker);
        Preconditioner preconditioner = new Preconditioner() {
            @Override
            public double[] precondition(double[] variables, double[] r) {
                for (int i = 0; i < r.length; ++i) {
                    r[i] *= 2;
                }
                return r;
            }
        };
        optimizer.setPreconditioner(preconditioner);
        UnivariateSolver solver = new BrentSolver();
        optimizer.setSolver(solver);
        PointValuePair result = optimizer.optimize(f, -5, 5);
        Assert.assertEquals(1.5, result.getPoint()[0], 0.001);
        Assert.assertEquals(-0.625, result.getValue(), 0.001);
    }

    /**
     * Test optimization with null preconditioner, Fletcher-Reeves update
     * formula and default solver.
     */
    @Test(expected = MathIllegalStateException.class)
    public void testNullPreconditioner() {
        Function f = new Function("f(x) = x^3 - 4x^2 + 3x - 2");
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(
                ConjugateGradientFormula.FLETCHER_REEVES);
        optimizer.setInitialStep(1.5);
        optimizer.setPreconditioner(null);
        optimizer.optimize(f, -5, 5);
    }

    /**
     * Test optimization with null solver, Fletcher-Reeves update formula and
     * identity preconditioner.
     */
    @Test(expected = MathIllegalStateException.class)
    public void testNullSolver() {
        Function f = new Function("f(x) = x^3 - 4x^2 + 3x - 2");
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(
                ConjugateGradientFormula.FLETCHER_REEVES);
        optimizer.setInitialStep(1.5);
        optimizer.setSolver(null);
        optimizer.optimize(f, -5, 5);
    }

    /**
     * Test optimization with a function that does not have a root in the
     * search interval.
     */
    @Test
    public void testFunctionWithoutRoot() {
        Function f = new Function("x -> x^2");
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(
                ConjugateGradientFormula.FLETCHER_REEVES);
        optimizer.setInitialStep(1.5);
        PointValuePair result = optimizer.optimize(f, -5, 5);
        Assert.assertNull(result);
    }

    /**
     * Simple function to test optimization.
     */
    private static class Function implements UnivariateFunction {
        private final String name;

        public Function(String name) {
            this.name = name;
        }

        @Override
        public double value(double x) {
            switch (name.charAt(0)) {
                case 'f':
                    return x * x * x - 4 * x * x + 3 * x - 2;
                default:
                    throw new IllegalArgumentException("Unknown function name.");
            }
        }

        @Override
        public String toString() {
            return name;
        }
    }
}

class Preconditioner implements NonLinearConjugateGradientOptimizer.Preconditioner {
    @Override
    public double[] precondition(double[] variables, double[] r) {
        return r;
    }
}