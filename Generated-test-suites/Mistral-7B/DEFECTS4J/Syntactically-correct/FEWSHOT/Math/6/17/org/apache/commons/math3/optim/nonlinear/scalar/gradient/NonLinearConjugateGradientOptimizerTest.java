package org.apache.commons.math3.optimization.general;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.analysis.solvers.BrentSolver;
import org.apache.commons.math3.analysis.solvers.UnivariateSolver;
import org.apache.commons.math3.exception.MathIllegalStateException;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.MaxEval;
import org.apache.commons.math3.optimization.PointValuePair;
import org.apache.commons.math3.optimization.SimpleValueChecker;
import org.apache.commons.math3.optimization.ConvergenceChecker;
import org.apache.commons.math3.optimization.NonLinearConjugateGradientOptimizer;
import org.apache.commons.math3.optimization.Preconditioner;
import org.apache.commons.math3.optimization.ConjugateGradientFormula;
import org.apache.commons.math3.optimization.LinearConstraints;
import org.apache.commons.math3.optimization.InitialGuess;
import org.apache.commons.math3.optim.PointValuePairList;
import org.apache.commons.math3.optim.SimpleValueChecker;
import org.apache.commons.math3.optim.initialization.PointValuePairGenerator;

public class NonLinearConjugateGradientOptimizerTest {

    @Test
    public void testDefaultConstructor() {
        ConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.FLETCHER_REEVES);
        assertNull(optimizer.getPreconditioner());
        assertNull(optimizer.getSolver());
        assertEquals(ConjugateGradientFormula.FLETCHER_REEVES, optimizer.getUpdateFormula());
        assertFalse(optimizer.isPreconditioned());
    }

    @Test
    public void testFunctionWithIdentityPreconditioner() throws MathIllegalStateException {
        UnivariateFunction function = f -> Math.pow(Math.E, f);
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(
                ConjugateGradientFormula.FLETCHER_REEVES,
                new SimpleValueChecker(1e-6),
                new BrentSolver(),
                new IdentityPreconditioner());
        optimizer.setInitialStep(1.0);

        PointValuePair initialPoint = new PointValuePair(0.0, function.value(0.0));
        optimizer.optimize(initialPoint, new PointValuePairGenerator());

        double x = optimizer.getPoint()[0];
        double fx = optimizer.getObjectiveValue();
        assertEquals(Math.log(2.718281828), fx, 1e-6);
        assertEquals(1.718281828, x, 1e-6);
    }

    @Test
    public void testFunctionWithPolakRibiereUpdateFormula() throws MathIllegalStateException {
        UnivariateFunction function = f -> Math.pow(Math.E, f) - 5 * Math.log(Math.E + 0.1 * f);
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(
                ConjugateGradientFormula.POLAK_RIBIERE,
                new SimpleValueChecker(1e-6),
                new BrentSolver(),
                new IdentityPreconditioner());
        optimizer.setInitialStep(1.0);

        PointValuePair initialPoint = new PointValuePair(0.0, function.value(0.0));
        optimizer.optimize(initialPoint, new PointValuePairGenerator());

        double x = optimizer.getPoint()[0];
        double fx = optimizer.getObjectiveValue();
        assertEquals(0.412246163, fx, 1e-6);
        assertEquals(1.215318443, x, 1e-6);
    }

    @Test
    public void testFunctionWithPreconditioner() throws MathIllegalStateException {
        UnivariateFunction function = f -> Math.pow(Math.E, f);
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(
                ConjugateGradientFormula.FLETCHER_REEVES,
                new SimpleValueChecker(1e-6),
                new BrentSolver(),
                new Preconditioner() {
                    @Override
                    public double[] precondition(double[] variables, double[] r) {
                        double[] preconditioned = r.clone();
                        for (int i = 0; i < preconditioned.length; i++) {
                            preconditioned[i] *= 2;
                        }
                        return preconditioned;
                    }
                });
        optimizer.setInitialStep(1.0);

        PointValuePair initialPoint = new PointValuePair(0.0, function.value(0.0));
        optimizer.optimize(initialPoint, new PointValuePairGenerator());

        double x = optimizer.getPoint()[0];
        double fx = optimizer.getObjectiveValue();
        assertEquals(Math.log(Math.E * Math.E), fx, 1e-6);
        assertEquals(Math.log(Math.E * Math.E), x, 1e-6);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithInvalidUpdateFormula() {
        new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.INVALID);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNullPreconditioner() {
        new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.FLETCHER_REEVES, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNullSolver() {
        new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.FLETCHER_REEVES, null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNullChecker() {
        new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.FLETCHER_REEVES, null, new BrentSolver(), null);
    }

    @Test
    public void testSetInitialStep() {
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(
                ConjugateGradientFormula.FLETCHER_REEVES,
                new SimpleValueChecker(1e-6),
                new BrentSolver(),
                new IdentityPreconditioner());
        optimizer.setInitialStep(1.5);
        assertEquals(1.5, optimizer.getInitialStep(), 1e-6);
    }

    @Test
    public void testGetStepSizeAfterLineSearch() throws MathIllegalStateException {
        UnivariateFunction function = f -> Math.pow(Math.E, f) - 5 * Math.log(Math.E + 0.1 * f);
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(
                ConjugateGradientFormula.POLAK_RIBIERE,
                new SimpleValueChecker(1e-6),
                new BrentSolver(),
                new IdentityPreconditioner());
        optimizer.setInitialStep(1.0);

        PointValuePair initialPoint = new PointValuePair(0.0, function.value(0.0));
        optimizer.optimize(initialPoint, new PointValuePairGenerator());

        double x = optimizer.getPoint()[0];
        double fx = optimizer.getObjectiveValue();
        assertEquals(0.412246163, fx, 1e-6);
        assertEquals(1.215318443, x, 1e-6);

        double stepSize = optimizer.getStepSize();
        assertTrue(stepSize > 0.0);
        assertTrue(stepSize < 1.215318443);
    }
}