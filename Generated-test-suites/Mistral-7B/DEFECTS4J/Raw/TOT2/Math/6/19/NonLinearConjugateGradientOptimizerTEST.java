package org.apache.commons.math3.optimization.general;

import static org.junit.Assert.*;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.analysis.solvers.BrentSolver;
import org.apache.commons.math3.exception.MathIllegalStateException;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleValueChecker;
import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.apache.commons.math3.optim.nonlinear.NonlinearConstraint;
import org.apache.commons.math3.optim.nonlinear.NonlinearProgram;
import org.apache.commons.math3.optim.nonlinear.NonlinearProblem;
import org.apache.commons.math3.optim.nonlinear.conjugategradient.NonlinearConjugateGradientOptimizer;
import org.apache.commons.math3.optim.nonlinear.conjugategradient.ConjugateGradientFormula;
import org.apache.commons.math3.optim.nonlinear.conjugategradient.IdentityPreconditioner;
import org.apache.commons.math3.optim.nonlinear.conjugategradient.PolakRibiereUpdateRule;

/**
 * Tests for {@link NonLinearConjugateGradientOptimizer}.
 */
public class NonLinearConjugateGradientOptimizerTest {

    /**
     * Test for {@link NonLinearConjugateGradientOptimizer#NonLinearConjugateGradientOptimizer(ConjugateGradientFormula)}.
     */
    @org.junit.Test
    public void testBasicConstructor() {
        ConjugateGradientFormula updateFormula = ConjugateGradientFormula.FLETCHER_REEVES;
        UnoptimizedUnivariateFunction f = new UnoptimizedUnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 3) + Math.pow(x, 2) - 2 * x;
            }

            @Override
            public double derivative(double x) {
                return 3 * Math.pow(x, 2) + 2;
            }
        };
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(updateFormula);
        UnivariateOptimizer result = optimizer.optimize(f, new SimpleValueChecker(1e-10));
        assertEquals("Root not found", -1.0, result.getPoint()[0], 1e-6);
        assertEquals("Function value not equal to zero", 0.0, result.getValue(), 1e-6);
    }

    /**
     * Test for {@link NonLinearConjugateGradientOptimizer#NonLinearConjugateGradientOptimizer(ConjugateGradientFormula, ConvergenceChecker)}.
     */
    @org.junit.Test
    public void testConstructorWithChecker() {
        ConjugateGradientFormula updateFormula = ConjugateGradientFormula.FLETCHER_REEVES;
        ConvergenceChecker<PointValuePair> checker = new ConvergenceChecker<PointValuePair>() {
            @Override
            public boolean converged(int iter, PointValuePair start, PointValuePair current) {
                return iter > 10 || Math.abs(start.getValue() - current.getValue()) < 1e-10;
            }
        };
        UnoptimizedUnivariateFunction f = new UnoptimizedUnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 3) + Math.pow(x, 2) - 2 * x;
            }

            @Override
            public double derivative(double x) {
                return 3 * Math.pow(x, 2) + 2;
            }
        };
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(updateFormula, checker);
        UnivariateOptimizer result = optimizer.optimize(f, new SimpleValueChecker(1e-10));
        assertEquals("Root not found", -1.0, result.getPoint()[0], 1e-6);
        assertEquals("Function value not equal to zero", 0.0, result.getValue(), 1e-6);
    }

    /**
     * Test for {@link NonLinearConjugateGradientOptimizer#NonLinearConjugateGradientOptimizer(ConjugateGradientFormula, ConvergenceChecker, UnivariateSolver)}.
     */
    @org.junit.Test
    public void testConstructorWithSolver() {
        ConjugateGradientFormula updateFormula = ConjugateGradientFormula.POLAK_RIBIERE;
        ConvergenceChecker<PointValuePair> checker = new ConvergenceChecker<PointValuePair>() {
            @Override
            public boolean converged(int iter, PointValuePair start, PointValuePair current) {
                return iter > 10 || Math.abs(start.getValue() - current.getValue()) < 1e-10;
            }
        };
        UnoptimizedUnivariateFunction f = new UnoptimizedUnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 3) + Math.pow(x, 2) - 2 * x;
            }

            @Override
            public double derivative(double x) {
                return 3 * Math.pow(x, 2) + 2;
            }
        };
        BrentSolver solver = new BrentSolver();
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(updateFormula, checker, solver);
        UnivariateOptimizer result = optimizer.optimize(f, new SimpleValueChecker(1e-10));
        assertEquals("Root not found", -1.0, result.getPoint()[0], 1e-6);
        assertEquals("Function value not equal to zero", 0.0, result.getValue(), 1e-6);
    }

    /**
     * Test for {@link NonLinearConjugateGradientOptimizer#NonLinearConjugateGradientOptimizer(ConjugateGradientFormula, ConvergenceChecker, UnivariateSolver, Preconditioner)}.
     */
    @org.junit.Test
    public void testConstructorWithAllArguments() {
        ConjugateGradientFormula updateFormula = ConjugateGradientFormula.POLAK_RIBIERE;
        ConvergenceChecker<PointValuePair> checker = new ConvergenceChecker<PointValuePair>() {
            @Override
            public boolean converged(int iter, PointValuePair start, PointValuePair current) {
                return iter > 10 || Math.abs(start.getValue() - current.getValue()) < 1e-10;
            }
        };
        UnoptimizedUnivariateFunction f = new UnoptimizedUnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 3) + Math.pow(x, 2) - 2 * x;
            }

            @Override
            public double derivative(double x) {
                return 3 * Math.pow(x, 2) + 2;
            }
        };
        BrentSolver solver = new BrentSolver();
        IdentityPreconditioner preconditioner = new IdentityPreconditioner();
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(updateFormula, checker, solver, preconditioner);
        UnivariateOptimizer result = optimizer.optimize(f, new SimpleValueChecker(1e-10));
        assertEquals("Root not found", -1.0, result.getPoint()[0], 1e-6);
        assertEquals("Function value not equal to zero", 0.0, result.getValue(), 1e-6);
    }

    /**
     * Test for {@link NonLinearConjugateGradientOptimizer#setInitialStep(double)}.
     */
    @org.junit.Test
    public void testSetInitialStep() {
        ConjugateGradientFormula updateFormula = ConjugateGradientFormula.FLETCHER_REEVES;
        ConvergenceChecker<PointValuePair> checker = new ConvergenceChecker<PointValuePair>() {
            @Override
            public boolean converged(int iter, PointValuePair start, PointValuePair current) {
                return iter > 10 || Math.abs(start.getValue() - current.getValue()) < 1e-10;
            }
        };
        UnoptimizedUnivariateFunction f = new UnoptimizedUnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 3) + Math.pow(x, 2) - 2 * x;
            }

            @Override
            public double derivative(double x) {
                return 3 * Math.pow(x, 2) + 2;
            }
        };
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(updateFormula, checker);
        optimizer.setInitialStep(2.0);
        UnivariateOptimizer result = optimizer.optimize(f, new SimpleValueChecker(1e-10));
        assertEquals("Root not found", -1.0, result.getPoint()[0], 1e-6);
        assertEquals("Function value not equal to zero", 0.0, result.getValue(), 1e-6);
    }

    /**
     * Test for {@link NonLinearConjugateGradientOptimizer#optimize(UnivariateFunction, ConvergenceChecker)}.
     */
    @org.junit.Test(expected = MathIllegalStateException.class)
    public void testOptimizeWithNullFunction() {
        ConjugateGradientFormula updateFormula = ConjugateGradientFormula.FLETCHER_REEVES;
        ConvergenceChecker<PointValuePair> checker = new ConvergenceChecker<PointValuePair>() {
            @Override
            public boolean converged(int iter, PointValuePair start, PointValuePair current) {
                return iter > 10 || Math.abs(start.getValue() - current.getValue()) < 1e-10;
            }
        };
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(updateFormula, checker);
        optimizer.optimize(null, new SimpleValueChecker(1e-10));
    }

    /**
     * Test for {@link NonLinearConjugateGradientOptimizer#optimize(NonlinearProgram)}.
     */
    @org.junit.Test
    public void testOptimizeWithNonlinearProgram() {
        ConjugateGradientFormula updateFormula = ConjugateGradientFormula.FLETCHER_REEVES;
        ConvergenceChecker<PointValuePair> checker = new ConvergenceChecker<PointValuePair>() {
            @Override
            public boolean converged(int iter, PointValuePair start, PointValuePair current) {
                return iter > 10 || Math.abs(start.getValue() - current.getValue()) < 1e-10;
            }
        };
        UnoptimizedUnivariateFunction f = new UnoptimizedUnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 3) + Math.pow(x, 2) - 2 * x;
            }

            @Override
            public double derivative(double x) {
                return 3 * Math.pow(x, 2) + 2;
            }
        };
        NonlinearConstraint constraint = new NonlinearConstraint() {
            @Override
            public double value(double[] point) {
                return point[0] * point[0] - 1;
            }

            @Override
            public double[] value(double[] point, double[] grad) {
                grad[0] = 2 * point[0];
                return new double[]{ value(point) };
            }

            @Override
            public int getNumberOfEquations() {
                return 1;
            }
        };
        NonlinearProblem problem = new NonlinearProblem() {
            @Override
            public UnivariateFunction getObjectiveFunction() {
                return f;
            }

            @Override
            public NonlinearConstraint[] getConstraints() {
                return new NonlinearConstraint[]{ constraint };
            }
        };
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(updateFormula, checker);
        UnivariateOptimizer result = optimizer.optimize(problem, new SimpleValueChecker(1e-10));
        assertEquals("Root not found", -1.0, result.getPoint()[0], 1e-6);
        assertEquals("Function value not equal to zero", 0.0, result.getValue(), 1e-6);
        assertEquals("Constraint not satisfied", 0.0, constraint.value(result.getPoint()), 1e-6);
    }
}