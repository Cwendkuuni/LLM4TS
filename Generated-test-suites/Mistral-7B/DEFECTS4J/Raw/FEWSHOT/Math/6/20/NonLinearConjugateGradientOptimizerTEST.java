package org.apache.commons.cli;

import static org.junit.Assert.*;
import org.junit.Test;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleValueChecker;
import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.apache.commons.math3.optim.initializer.AroundMinimumInitializer;
import org.apache.commons.math3.optim.nonlinear.ConjugateGradient;
import org.apache.commons.math3.optim.nonlinear.convergence.ConvergenceChecker;
import org.apache.commons.math3.optim.nonlinear.convergence.SimpleStoppingCondition;
import org.apache.commons.math3.optim.nonlinear.function.LinearFunction;
import org.apache.commons.math3.optim.nonlinear.gradient.Gradient;
import org.apache.commons.math3.optim.point.PointValuePair;
import org.apache.commons.math3.optim.preconditions.IdentityPreconditioner;
import org.apache.commons.math3.optim.solvers.BrentSolver;
import org.apache.commons.math3.optim.InitialGuess;
import org.apache.commons.math3.optim.NonlinearOptimizer;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.initializer.InitialGuess;
import org.apache.commons.math3.optim.nonlinear.function.AbstractUnivariateFunction;
import org.apache.commons.math3.optim.preconditions.Preconditioner;

public class NonLinearConjugateGradientOptimizerTest {

    @Test
    public void testOptimize() {
        ConjugateGradientFormula updateFormula = ConjugateGradientFormula.POLAK_RIBIERE;
        UnivariateFunction function = new LinearFunction(1.0);
        Preconditioner preconditioner = new IdentityPreconditioner();
        ConvergenceChecker<PointValuePair> checker = new SimpleStoppingCondition(1e-12);
        UnivariateSolver solver = new BrentSolver();
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(updateFormula, checker, solver, preconditioner);

        double[] startPoint = {0.0};
        PointValuePair solution = optimizer.optimize(function, startPoint, new AroundMinimumInitializer(startPoint[0], 1e-2));
        assertEquals(1.0, solution.getValue(), 1e-12);
        assertArrayEquals(new double[]{1.0}, solution.getPoint(), 1e-12);
    }

    @Test
    public void testUpdateFormulaFletcherReeves() {
        ConjugateGradientFormula updateFormula = ConjugateGradientFormula.FLETCHER_REEVES;
        UnivariateFunction function = new LinearFunction(1.0);
        Preconditioner preconditioner = new IdentityPreconditioner();
        ConvergenceChecker<PointValuePair> checker = new SimpleStoppingCondition(1e-12);
        UnivariateSolver solver = new BrentSolver();
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(updateFormula, checker, solver, preconditioner);

        double[] startPoint = {0.0};
        PointValuePair solution = optimizer.optimize(function, startPoint, new AroundMinimumInitializer(startPoint[0], 1e-2));
        assertEquals(1.0, solution.getValue(), 1e-12);
        assertArrayEquals(new double[]{1.0}, solution.getPoint(), 1e-12);

        double[] initialPoint = {0.0, 1.0};
        PointValuePair initialSolution = optimizer.optimize(function, initialPoint, new AroundMinimumInitializer(initialPoint[0], 1e-2));
        assertEquals(1.0, initialSolution.getValue(), 1e-12);
        assertArrayEquals(new double[]{1.0}, initialSolution.getPoint(), 1e-12);

        double[] newPoint = initialPoint.clone();
        newPoint[0] += 0.1;
        PointValuePair newSolution = optimizer.optimize(function, newPoint, new AroundMinimumInitializer(newPoint[0], 1e-2));
        assertEquals(1.0, newSolution.getValue(), 1e-12);
        assertArrayEquals(new double[]{1.1}, newSolution.getPoint(), 1e-12);

        double beta1 = optimizer.getBeta();
        double beta2 = optimizer.getBeta();

        // Fletcher-Reeves update formula: beta = (g' * g) / (g' * f')
        double g1 = 1.0;
        double g2 = 0.1;
        double f1 = 1.0;
        double f2 = 0.9;

        double expectedBeta1 = (g1 * g1) / (g1 * f1);
        double expectedBeta2 = (g2 * g2) / (g2 * f2);

        assertEquals("Unexpected beta value", expectedBeta1, beta1, 1e-12);
        assertEquals("Unexpected beta value", expectedBeta2, beta2, 1e-12);
    }

    @Test
    public void testUpdateFormulaPolakRibiere() {
        ConjugateGradientFormula updateFormula = ConjugateGradientFormula.POLAK_RIBIERE;
        UnivariateFunction function = new LinearFunction(1.0);
        Preconditioner preconditioner = new IdentityPreconditioner();
        ConvergenceChecker<PointValuePair> checker = new SimpleStoppingCondition(1e-12);
        UnivariateSolver solver = new BrentSolver();
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(updateFormula, checker, solver, preconditioner);

        double[] startPoint = {0.0};
        PointValuePair solution = optimizer.optimize(function, startPoint, new AroundMinimumInitializer(startPoint[0], 1e-2));
        assertEquals(1.0, solution.getValue(), 1e-12);
        assertArrayEquals(new double[]{1.0}, solution.getPoint(), 1e-12);

        double[] initialPoint = {0.0, 1.0};
        PointValuePair initialSolution = optimizer.optimize(function, initialPoint, new AroundMinimumInitializer(initialPoint[0], 1e-2));
        assertEquals(1.0, initialSolution.getValue(), 1e-12);
        assertArrayEquals(new double[]{1.0}, initialSolution.getPoint(), 1e-12);

        double[] newPoint = initialPoint.clone();
        newPoint[0] += 0.1;
        PointValuePair newSolution = optimizer.optimize(function, newPoint, new AroundMinimumInitializer(newPoint[0], 1e-2));
        assertEquals(1.0, newSolution.getValue(), 1e-12);
        assertArrayEquals(new double[]{1.1}, newSolution.getPoint(), 1e-12);

        double beta1 = optimizer.getBeta();
        double beta2 = optimizer.getBeta();

        // Polak-Ribiere update formula: beta = (g' * (g - s)) / (g' * f')
        double g1 = 1.0;
        double g2 = 0.1;
        double s1 = 0.1;
        double f1 = 1.0;

        double expectedBeta1 = (g1 * (g1 - s1)) / (g1 * f1);
        double expectedBeta2 = (g2 * (g2 - s2)) / (g2 * f2);

        assertEquals("Unexpected beta value", expectedBeta1, beta1, 1e-12);
        assertEquals("Unexpected beta value", expectedBeta2, beta2, 1e-12);
    }

    @Test
    public void testPreconditioner() {
        UnivariateFunction function = new LinearFunction(1.0);
        Preconditioner preconditioner = new IdentityPreconditioner();
        ConvergenceChecker<PointValuePair> checker = new SimpleStoppingCondition(1e-12);
        UnivariateSolver solver = new BrentSolver();
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.FLETCHER_REEVES, checker, solver, preconditioner);

        double[] startPoint = {1.0};
        PointValuePair solution = optimizer.optimize(function, startPoint, new AroundMinimumInitializer(startPoint[0], 1e-2));
        assertEquals(1.0, solution.getValue(), 1e-12);
        assertArrayEquals(new double[]{1.0}, solution.getPoint(), 1e-12);

        double[] preconditionedPoint = preconditioner.precondition(startPoint, new double[]{1.0});
        assertEquals(1.0, preconditionedPoint[0], 1e-12);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullPreconditioner() {
        ConvergenceChecker<PointValuePair> checker = new SimpleStoppingCondition(1e-12);
        UnivariateSolver solver = new BrentSolver();
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.POLAK_RIBIERE, checker, solver, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullUpdateFormula() {
        Preconditioner preconditioner = new IdentityPreconditioner();
        ConvergenceChecker<PointValuePair> checker = new SimpleStoppingCondition(1e-12);
        UnivariateSolver solver = new BrentSolver();
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(null, checker, solver, preconditioner);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullSolver() {
        ConvergenceChecker<PointValuePair> checker = new SimpleStoppingCondition(1e-12);
        Preconditioner preconditioner = new IdentityPreconditioner();
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.POLAK_RIBIERE, checker, null, preconditioner);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullFunction() {
        Preconditioner preconditioner = new IdentityPreconditioner();
        ConvergenceChecker<PointValuePair> checker = new SimpleStoppingCondition(1e-12);
        UnivariateSolver solver = new BrentSolver();
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.POLAK_RIBIERE, checker, solver, preconditioner);
        optimizer.optimize(null, new double[]{0.0}, new AroundMinimumInitializer(0.0, 1e-2));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullStartPoint() {
        UnivariateFunction function = new LinearFunction(1.0);
        Preconditioner preconditioner = new IdentityPreconditioner();
        ConvergenceChecker<PointValuePair> checker = new SimpleStoppingCondition(1e-12);
        UnivariateSolver solver = new BrentSolver();
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.POLAK_RIBIERE, checker, solver, preconditioner);
        optimizer.optimize(function, null, new AroundMinimumInitializer(0.0, 1e-2));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullInitialGuess() {
        UnivariateFunction function = new LinearFunction(1.0);
        Preconditioner preconditioner = new IdentityPreconditioner();
        ConvergenceChecker<PointValuePair> checker = new SimpleStoppingCondition(1e-12);
        UnivariateSolver solver = new BrentSolver();
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.POLAK_RIBIERE, checker, solver, preconditioner);
        optimizer.optimize(function, new double[]{0.0}, null);
    }
}