package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.analysis.solvers.BrentSolver;
import org.apache.commons.math3.analysis.solvers.LinearSolver;
import org.apache.commons.math3.analysis.solvers.UnivariateSolver;
import org.apache.commons.math3.exception.MathIllegalStateException;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.PointValuePair;
import org.apache.commons.math3.optimization.SimpleValueChecker;
import org.apache.commons.math3.optimization.nonlinear.NonlinearOptimizer;
import org.apache.commons.math3.optimization.nonlinear.conjugate.ConjugateGradientFormula;
import org.apache.commons.math3.optimization.nonlinear.conjugate.Preconditioner;
import org.apache.commons.math3.optimization.nonlinear.conjugate.ScalarDifferentiableOptimizer;
import org.apache.commons.math3.optimization.nonlinear.conjugate.NonLinearConjugateGradientOptimizer;
import org.apache.commons.math3.optimization.linear.IdentityPreconditioner;
import org.apache.commons.math3.util.FastMath;
import org.junit.Test;

public class NonLinearConjugateGradientOptimizerTest {
    @Test
    public void testNonLinearConjugateGradientOptimizer() {
        ConjugateGradientFormula updateFormula = ConjugateGradientFormula.FLETCHER_REEVES;
        Preconditioner preconditioner = new IdentityPreconditioner();
        ConvergenceChecker<PointValuePair> checker = new SimpleValueChecker(1e-10);
        UnivariateSolver lineSearchSolver = new BrentSolver();
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(updateFormula, checker, lineSearchSolver, preconditioner);
        assertNull(optimizer.getPreconditioner());
        assertNotNull(optimizer.getSolver());
        assertEquals(updateFormula, optimizer.getUpdateFormula());
        assertEquals(checker, optimizer.getConvergenceChecker());
        assertEquals(lineSearchSolver, optimizer.getSolver());
        assertEquals(preconditioner, optimizer.getPreconditioner());
        assertNull(optimizer.getPoint());
        optimizer.setInitialStep(2.0);
        assertEquals(2.0, optimizer.getInitialStep(), 0.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNonLinearConjugateGradientOptimizerInvalidUpdateFormula() {
        ConvergenceChecker<PointValuePair> checker = new SimpleValueChecker();
        ConjugateGradientFormula updateFormula = ConjugateGradientFormula.INVALID;
        UnivariateSolver lineSearchSolver = new BrentSolver();
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(updateFormula, checker, lineSearchSolver, new IdentityPreconditioner());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNonLinearConjugateGradientOptimizerNullPreconditioner() {
        ConjugateGradientFormula updateFormula = ConjugateGradientFormula.FLETCHER_REEVES;
        ConvergenceChecker<PointValuePair> checker = new SimpleValueChecker();
        UnivariateSolver lineSearchSolver = new BrentSolver();
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(updateFormula, checker, lineSearchSolver, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNonLinearConjugateGradientOptimizerNullLineSearchSolver() {
        ConjugateGradientFormula updateFormula = ConjugateGradientFormula.FLETCHER_REEVES;
        Preconditioner preconditioner = new IdentityPreconditioner();
        ConvergenceChecker<PointValuePair> checker = new SimpleValueChecker();
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(updateFormula, checker, null, preconditioner);
    }

    @Test
    public void testDoOptimize() {
        final UnivariateFunction objectiveFunction = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(Math.sin(x), 2);
            }

            @Override
            public double derivative(double x) {
                return 2 * Math.sin(x) * Math.cos(x);
            }
        };

        final double[] startPoint = new double[] { 1.0 };
        NonlinearOptimizer<UnivariateFunction, Double> optimizer = new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.FLETCHER_REEVES,
                new SimpleValueChecker<>(1e-10), new BrentSolver(), new IdentityPreconditioner());

        optimizer.setInitialStep(1.0);
        PointValuePair result = optimizer.optimize(objectiveFunction, startPoint);

        assertEquals(GoalType.MINIMIZE, optimizer.getGoalType());
        assertEquals(1.0, result.getPoint()[0], 1e-5);
        assertEquals(-0.408248290463863, result.getValue(), 1e-5);
    }
}