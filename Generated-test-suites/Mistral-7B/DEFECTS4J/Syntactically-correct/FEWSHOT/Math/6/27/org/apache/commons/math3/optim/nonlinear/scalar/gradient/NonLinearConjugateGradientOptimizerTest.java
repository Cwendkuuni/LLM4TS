package org.apache.commons.cli;

import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.optim.*;
import org.apache.commons.math3.optim.initializer.AroundMinimumPointInitializer;
import org.apache.commons.math3.optim.initializer.InitialPointValue;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.LinearConstraint;
import org.apache.commons.math3.optim.nonlinear.scalar.SimpleConstraintsChecker;
import org.apache.commons.math3.optim.nonlinear.scalar.SimpleValueChecker;
import org.apache.commons.math3.optim.nonlinear.scalar.scalarfunction.LinearFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.scalarsolver.BrentSolver;
import org.apache.commons.math3.optim.nonlinear.scalar.scalarsolver.ScaledBrentSolver;
import org.apache.commons.math3.optim.nonlinear.scalar.util.NonLinearConstraintUtils;
import org.apache.commons.math3.optim.nonlinear.scalar.util.NonLinearOptimizationTestUtilities;
import org.apache.commons.math3.optim.preconditioner.IdentityPreconditioner;
import org.apache.commons.math3.optim.nonlinear.scalar.conjugate.ConjugateGradientFormula;
import org.apache.commons.math3.optim.nonlinear.scalar.conjugate.NonLinearConjugateGradientOptimizer;

public class NonLinearConjugateGradientOptimizerTest {

    /**
     * Test constructor with default values.
     */
    @Test
    public void testDefaultConstructor() {
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.FLETCHER_REEVES);
        assertNotNull(optimizer.getPreconditioner());
        assertInstanceOf(IdentityPreconditioner.class, optimizer.getPreconditioner());
        assertNull(optimizer.getSolver());
        assertEquals(1.0, optimizer.getInitialStep(), 0.0);
    }

    /**
     * Test constructor with custom values.
     */
    @Test
    public void testCustomConstructor() {
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(
                ConjugateGradientFormula.FLETCHER_REEVES,
                new SimpleValueChecker(),
                new BrentSolver(),
                new IdentityPreconditioner()
        );
        assertNotNull(optimizer.getPreconditioner());
        assertInstanceOf(IdentityPreconditioner.class, optimizer.getPreconditioner());
        assertNotNull(optimizer.getSolver());
        assertInstanceOf(BrentSolver.class, optimizer.getSolver());
        assertEquals(1.0, optimizer.getInitialStep(), 0.0);
    }

    /**
     * Test setInitialStep method.
     */
    @Test
    public void testSetInitialStep() {
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(
                ConjugateGradientFormula.FLETCHER_REEVES,
                new SimpleValueChecker(),
                new BrentSolver(),
                new IdentityPreconditioner()
        );
        optimizer.setInitialStep(2.0);
        assertEquals(2.0, optimizer.getInitialStep(), 0.0);
    }

    /**
     * Test optimize method with a simple function.
     */
    @Test
    public void testOptimizeSimpleFunction() {
        UnivariateFunction function = new LinearFunction(new double[]{-1.0, 1.0}, 0.0);
        double[] startPoint = {1.0};
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(
                ConjugateGradientFormula.FLETCHER_REEVES,
                new SimpleValueChecker(),
                new BrentSolver(),
                new IdentityPreconditioner()
        );
        OptimizationResult result = optimizer.optimize(function, startPoint);
        assertEquals(0.0, result.getValue(), 1e-14);
        assertEquals(0.0, result.getPoint()[0], 1e-14);
    }

    /**
     * Test optimize method with a non-linear function.
     */
    @Test
    public void testOptimizeNonLinearFunction() {
        UnivariateFunction function = x -> Math.pow(Math.sin(x), 2.0) + Math.pow(Math.cos(x), 2.0);
        double[] startPoint = {1.0};
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(
                ConjugateGradientFormula.POLAK_RIBIERE,
                new SimpleValueChecker(),
                new ScaledBrentSolver(0.1),
                new IdentityPreconditioner()
        );
        OptimizationResult result = optimizer.optimize(function, startPoint);
        assertEquals(3.141592653589793, result.getValue(), 1e-14);
        assertEquals(-1.4137158266556295, result.getPoint()[0], 1e-14);
    }

    /**
     * Test optimize method with custom initial point and constraints.
     */
    @Test
    public void testOptimizeWithConstraints() {
        UnivariateFunction function = x -> Math.pow(x - 2.0, 2.0);
        double[] startPoint = {3.0};
        LinearConstraint constraint = new LinearConstraint(new double[]{1.0}, 0.0);
        ConvergenceChecker<OptimizationResult> checker = new SimpleConstraintsChecker();
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(
                ConjugateGradientFormula.FLETCHER_REEVES,
                new SimpleValueChecker(),
                new BrentSolver(),
                new IdentityPreconditioner()
        );
        OptimizationResult result = optimizer.optimize(function, checker, new AroundMinimumPointInitializer<>(startPoint), constraint);
        assertEquals(4.0, result.getValue(), 1e-14);
        assertEquals(2.0, result.getPoint()[0], 1e-14);
    }

    /**
     * Test optimize method with custom goal type.
     */
    @Test
    public void testOptimizeWithGoalType() {
        UnivariateFunction function = x -> Math.pow(Math.sin(x), 2.0);
        double[] startPoint = {1.0};
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(
                ConjugateGradientFormula.POLAK_RIBIERE,
                new SimpleValueChecker(),
                new BrentSolver(),
                new IdentityPreconditioner()
        );
        optimizer.setGoalType(GoalType.MAXIMIZE);
        OptimizationResult result = optimizer.optimize(function, startPoint);
        assertEquals(-3.141592653589793, result.getValue(), 1e-14);
        assertEquals(-1.4137158266556295, result.getPoint()[0], 1e-14);
    }

    /**
     * Test optimize method with custom formula.
     */
    @Test
    public void testOptimizeWithCustomFormula() {
        UnivariateFunction function = x -> Math.pow(Math.sin(x), 2.0);
        double[] startPoint = {1.0};
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(
                ConjugateGradientFormula.POLAK_RIBIERE,
                new SimpleValueChecker(),
                new BrentSolver(),
                new IdentityPreconditioner()
        );
        optimizer.setUpdateFormula(ConjugateGradientFormula.POLAK_RIBIERE_STRONG);
        OptimizationResult result = optimizer.optimize(function, startPoint);
        assertEquals(-3.141592653589793, result.getValue(), 1e-14);
        assertEquals(-1.4137158266556295, result.getPoint()[0], 1e-14);
    }

    /**
     * Test optimize method with custom solver.
     */
    @Test
    public void testOptimizeWithCustomSolver() {
        UnivariateFunction function = x -> Math.pow(Math.sin(x), 2.0);
        double[] startPoint = {1.0};
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(
                ConjugateGradientFormula.POLAK_RIBIERE,
                new SimpleValueChecker(),
                new CustomBrentSolver(),
                new IdentityPreconditioner()
        );
        OptimizationResult result = optimizer.optimize(function, startPoint);
        assertEquals(-3.141592653589793, result.getValue(), 1e-14);
        assertEquals(-1.4137158266556295, result.getPoint()[0], 1e-14);
    }

    /**
     * Custom BrentSolver for testing.
     */
    private static class CustomBrentSolver extends BrentSolver {
        @Override
        public double bracketingBrent(UnivariateFunction f, double a, double b, double[] x) {
            return super.bracketingBrent(f, a, b, x) + 1.0;
        }
    }
}