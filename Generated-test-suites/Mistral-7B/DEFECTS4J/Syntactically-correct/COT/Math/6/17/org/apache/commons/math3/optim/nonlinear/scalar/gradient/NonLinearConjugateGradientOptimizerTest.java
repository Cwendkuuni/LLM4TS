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
import org.apache.commons.math3.optimization.Preconditioner;
import org.apache.commons.math3.util.FastMath;

/**
 * Tests for {@link NonLinearConjugateGradientOptimizer} class.
 */
public class NonLinearConjugateGradientOptimizerTest {

    

    @org.junit.Test
    public void testDefaultConstructor() {
        try {
            new NonLinearConjugateGradientOptimizer(null);
            org.junit.Assert.fail("Should have thrown an IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
            // Expected exception.
        }

        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.FLETCHER_REEVES);
        org.junit.Assert.assertNotNull(optimizer);
    }

    @org.junit.Test
    public void testConstructorWithCheckerAndSolver() {
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(
                ConjugateGradientFormula.FLETCHER_REEVES,
                new SimpleValueChecker(),
                new BrentSolver());
        org.junit.Assert.assertNotNull(optimizer);
    }

    @org.junit.Test
    public void testConstructorWithCheckerAndSolverAndPreconditioner() {
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(
                ConjugateGradientFormula.FLETCHER_REEVES,
                new SimpleValueChecker(),
                new BrentSolver(),
                new IdentityPreconditioner());
        org.junit.Assert.assertNotNull(optimizer);
    }

    @org.junit.Test
    public void testSetInitialStep() {
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(
                ConjugateGradientFormula.FLETCHER_REEVES,
                new SimpleValueChecker(),
                new BrentSolver(),
                new IdentityPreconditioner());
        optimizer.setInitialStep(-1);
        org.junit.Assert.assertEquals(1.0, optimizer.getInitialStep(), 0.0);
        optimizer.setInitialStep(2.5);
        org.junit.Assert.assertEquals(2.5, optimizer.getInitialStep(), 0.0);
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testSetInitialStepNegative() {
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(
                ConjugateGradientFormula.FLETCHER_REEVES,
                new SimpleValueChecker(),
                new BrentSolver(),
                new IdentityPreconditioner());
        optimizer.setInitialStep(-1);
    }

    @org.junit.Test
    public void testOptimizeHarmonicFunction() {
        UnivariateFunction function = f -> Math.pow(1.0 / (1.0 + f), 2.0);
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(
                ConjugateGradientFormula.FLETCHER_REEVES,
                new SimpleValueChecker(),
                new BrentSolver(),
                new IdentityPreconditioner());
        double[] startPoint = {1.0};
        PointValuePair result = optimizer.optimize(function, startPoint, null);
        org.junit.Assert.assertArrayEquals(new double[] {0.0}, result.getPoint(), 0.0);
        org.junit.Assert.assertEquals(0.25, result.getValue(), 0.0);
    }

    @org.junit.Test
    public void testOptimizeTrigonometricFunction() {
        UnivariateFunction function = f -> Math.pow(Math.sin(Math.PI * f), 2.0);
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(
                ConjugateGradientFormula.FLETCHER_REEVES,
                new SimpleValueChecker(),
                new BrentSolver(),
                new IdentityPreconditioner());
        double[] startPoint = {0.5};
        PointValuePair result = optimizer.optimize(function, startPoint, null);
        org.junit.Assert.assertArrayEquals(new double[] {1.0}, result.getPoint(), 0.0);
        org.junit.Assert.assertEquals(0.25, result.getValue(), 0.0);
    }

    @org.junit.Test(expected = MathIllegalStateException.class)
    public void testOptimizeDivergingFunction() {
        UnivariateFunction function = f -> Math.log(1.0 + f) / f;
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(
                ConjugateGradientFormula.FLETCHER_REEVES,
                new SimpleValueChecker(),
                new BrentSolver(),
                new IdentityPreconditioner());
        double[] startPoint = {1.0};
        optimizer.optimize(function, startPoint, null);
    }

    @org.junit.Test
    public void testOptimizeFunctionWithNaNInput() {
        UnivariateFunction function = f -> {
            throw new ArithmeticException("Division by zero");
        };
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(
                ConjugateGradientFormula.FLETCHER_REEVES,
                new SimpleValueChecker(),
                new BrentSolver(),
                new IdentityPreconditioner());
        double[] startPoint = {Double.NaN};
        try {
            optimizer.optimize(function, startPoint, null);
        } catch (MathIllegalStateException e) {
            org.junit.Assert.assertEquals(LocalizedFormats.FUNCTION_VALUE_IS_NA_OR_INFINITE, e.getMessage());
        }
    }

    @org.junit.Test
    public void testOptimizeFunctionWithNegativeStepSize() {
        UnivariateFunction function = f -> Math.pow(1.0 + f, 2.0);
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(
                ConjugateGradientFormula.FLETCHER_REEVES,
                new SimpleValueChecker(),
                new BrentSolver(),
                new IdentityPreconditioner());
        double[] startPoint = {1.0};
        optimizer.setInitialStep(-1.0);
        PointValuePair result = optimizer.optimize(function, startPoint, null);
        org.junit.Assert.assertArrayEquals(new double[] {1.0}, result.getPoint(), 0.0);
        org.junit.Assert.assertEquals(1.0, result.getValue(), 0.0);
    }

    
}