package org.apache.commons.math3.optimization.general;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.MathIllegalStateException;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleValueChecker;
import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.apache.commons.math3.optim.nonlinear.conjgrad.ConjugateGradientFormula;
import org.apache.commons.math3.optim.nonlinear.conjgrad.IdentityPreconditioner;
import org.apache.commons.math3.optim.nonlinear.conjgrad.Preconditioner;
import org.apache.commons.math3.optim.nonlinear.conjgrad.PolakRibiereConjugateGradientFormula;
import org.apache.commons.math3.optim.nonlinear.conjgrad.FletcherReevesConjugateGradientFormula;
import org.apache.commons.math3.optim.nonlinear.convergence.ConvergenceChecker;
import org.apache.commons.math3.optim.nonlinear.solvers.BrentSolver;
import org.apache.commons.math3.optim.nonlinear.solvers.LevenbergMarquardtSolver;
import org.apache.commons.math3.optim.nonlinear.solvers.SimpleLineSearch;
import org.apache.commons.math3.optim.nonlinear.solvers.ScottSheridanDennisSolver;
import org.junit.Test;

public class NonLinearConjugateGradientOptimizerTest {

    @Test
    public void testConstructorWithFletcherReevesFormula() {
        ConjugateGradientFormula updateFormula = FletcherReevesConjugateGradientFormula.FLETCHER_REEVES;
        UnivariateOptimizer optimizer = new NonLinearConjugateGradientOptimizer(updateFormula);
        assertNotNull(optimizer);
    }

    @Test
    public void testConstructorWithPolakRibiereFormula() {
        ConjugateGradientFormula updateFormula = PolakRibiereConjugateGradientFormula.POLAK_RIBIERE;
        UnivariateOptimizer optimizer = new NonLinearConjugateGradientOptimizer(updateFormula);
        assertNotNull(optimizer);
    }

    @Test
    public void testConstructorWithSimpleValueChecker() {
        ConjugateGradientFormula updateFormula = FletcherReevesConjugateGradientFormula.FLETCHER_REEVES;
        ConvergenceChecker<PointValuePair> convergenceChecker = new SimpleValueChecker(1e-6, 100);
        UnivariateSolver lineSearchSolver = new SimpleLineSearch();
        Preconditioner preconditioner = new IdentityPreconditioner();
        UnivariateOptimizer optimizer = new NonLinearConjugateGradientOptimizer(updateFormula, convergenceChecker, lineSearchSolver, preconditioner);
        assertNotNull(optimizer);
    }

    @Test
    public void testConstructorWithBrentSolver() {
        ConjugateGradientFormula updateFormula = FletcherReevesConjugateGradientFormula.FLETCHER_REEVES;
        ConvergenceChecker<PointValuePair> convergenceChecker = new SimpleValueChecker(1e-6, 100);
        Preconditioner preconditioner = new IdentityPreconditioner();
        UnivariateOptimizer optimizer = new NonLinearConjugateGradientOptimizer(updateFormula, convergenceChecker, new BrentSolver(), preconditioner);
        assertNotNull(optimizer);
    }

    @Test
    public void testConstructorWithLevenbergMarquardtSolver() {
        ConjugateGradientFormula updateFormula = FletcherReevesConjugateGradientFormula.FLETCHER_REEVES;
        ConvergenceChecker<PointValuePair> convergenceChecker = new SimpleValueChecker(1e-6, 100);
        Preconditioner preconditioner = new IdentityPreconditioner();
        UnivariateOptimizer optimizer = new NonLinearConjugateGradientOptimizer(updateFormula, convergenceChecker, new LevenbergMarquardtSolver(), preconditioner);
        assertNotNull(optimizer);
    }

    @Test
    public void testConstructorWithScottSheridanDennisSolver() {
        ConjugateGradientFormula updateFormula = FletcherReevesConjugateGradientFormula.FLETCHER_REEVES;
        ConvergenceChecker<PointValuePair> convergenceChecker = new SimpleValueChecker(1e-6, 100);
        Preconditioner preconditioner = new IdentityPreconditioner();
        UnivariateOptimizer optimizer = new NonLinearConjugateGradientOptimizer(updateFormula, convergenceChecker, new ScottSheridanDennisSolver(), preconditioner);
        assertNotNull(optimizer);
    }

    @Test
    public void testSetInitialStep() {
        ConjugateGradientFormula updateFormula = FletcherReevesConjugateGradientFormula.FLETCHER_REEVES;
        ConvergenceChecker<PointValuePair> convergenceChecker = new SimpleValueChecker(1e-6, 100);
        UnivariateSolver lineSearchSolver = new SimpleLineSearch();
        Preconditioner preconditioner = new IdentityPreconditioner();
        UnivariateOptimizer optimizer = new NonLinearConjugateGradientOptimizer(updateFormula, convergenceChecker, lineSearchSolver, preconditioner);
        optimizer.setInitialStep(1.5);
        assertEquals(1.5, optimizer.getInitialStep(), 1e-6);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetInitialStepNegative() {
        ConjugateGradientFormula updateFormula = FletcherReevesConjugateGradientFormula.FLETCHER_REEVES;
        ConvergenceChecker<PointValuePair> convergenceChecker = new SimpleValueChecker(1e-6, 100);
        UnivariateSolver lineSearchSolver = new SimpleLineSearch();
        Preconditioner preconditioner = new IdentityPreconditioner();
        UnivariateOptimizer optimizer = new NonLinearConjugateGradientOptimizer(updateFormula, convergenceChecker, lineSearchSolver, preconditioner);
        optimizer.setInitialStep(-1.0);
    }

    @Test
    public void testOptimizeWithIdentityPreconditioner() {
        final UnivariateFunction rosenbrock = new UnivariateFunction() {
            @Override
            public double value(double x) {
                double a = 1.0;
                double b = 2.0;
                double c = 1.0;
                return a * (x - 1.0) * (x - 1.0) + c * (x - 2.0) * (x - 2.0);
            }

            @Override
            public double value(double[] x) {
                return this.value(x[0]);
            }

            @Override
            public double[] gradient(double x) {
                return new double[]{2 * (x - 1.0), 2 * (x - 2.0)};
            }

            @Override
            public double[] gradient(double[] x) {
                return new double[]{this.gradient(x[0])[0], this.gradient(x[0])[1]};
            }
        };

        ConjugateGradientFormula updateFormula = FletcherReevesConjugateGradientFormula.FLETCHER_REEVES;
        ConvergenceChecker<PointValuePair> convergenceChecker = new SimpleValueChecker(1e-6, 100);
        UnivariateSolver lineSearchSolver = new SimpleLineSearch();
        Preconditioner preconditioner = new IdentityPreconditioner();
        UnivariateOptimizer optimizer = new NonLinearConjugateGradientOptimizer(updateFormula, convergenceChecker, lineSearchSolver, preconditioner);
        PointValuePair result = optimizer.optimize(rosenbrock, 1.0, null);
        assertEquals(1.0, result.getX(), 1e-6);
        assertEquals(0.0, result.getFunctionValue(), 1e-6);
    }

    @Test
    public void testOptimizeWithCustomPreconditioner() {
        final UnivariateFunction rosenbrock = new UnivariateFunction() {
            @Override
            public double value(double x) {
                double a = 1.0;
                double b = 2.0;
                double c = 1.0;
                return a * (x - 1.0) * (x - 1.0) + c * (x - 2.0) * (x - 2.0);
            }

            @Override
            public double value(double[] x) {
                return this.value(x[0]);
            }

            @Override
            public double[] gradient(double x) {
                return new double[]{2 * (x - 1.0), 2 * (x - 2.0)};
            }

            @Override
            public double[] gradient(double[] x) {
                return new double[]{this.gradient(x[0])[0], this.gradient(x[0])[1]};
            }
        };

        ConjugateGradientFormula updateFormula = FletcherReevesConjugateGradientFormula.FLETCHER_REEVES;
        ConvergenceChecker<PointValuePair> convergenceChecker = new SimpleValueChecker(1e-6, 100);
        UnivariateSolver lineSearchSolver = new SimpleLineSearch();
        Preconditioner preconditioner = new Preconditioner() {
            @Override
            public double[] precondition(double[] variables, double[] r) {
                double[] p = r.clone();
                double factor = 2.0;
                for (int i = 0; i < p.length; i++) {
                    p[i] *= factor;
                }
                return p;
            }
        };
        UnivariateOptimizer optimizer = new NonLinearConjugateGradientOptimizer(updateFormula, convergenceChecker, lineSearchSolver, preconditioner);
        PointValuePair result = optimizer.optimize(rosenbrock, 1.0, null);
        assertEquals(1.0, result.getX(), 1e-6);
        assertEquals(0.0, result.getFunctionValue(), 1e-6);
    }

    @Test(expected = MathIllegalStateException.class)
    public void testOptimizeWithNullFunction() {
        ConjugateGradientFormula updateFormula = FletcherReevesConjugateGradientFormula.FLETCHER_REEVES;
        ConvergenceChecker<PointValuePair> convergenceChecker = new SimpleValueChecker(1e-6, 100);
        UnivariateSolver lineSearchSolver = new SimpleLineSearch();
        Preconditioner preconditioner = new IdentityPreconditioner();
        UnivariateOptimizer optimizer = new NonLinearConjugateGradientOptimizer(updateFormula, convergenceChecker, lineSearchSolver, preconditioner);
        optimizer.optimize(null, 0.0, null);
    }
}