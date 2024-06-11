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
import org.apache.commons.math3.optimization.ConjugateGradientFormula;
import org.apache.commons.math3.optimization.Preconditioner;
import org.apache.commons.math3.util.FastMath;
import org.junit.Assert;
import org.junit.Test;

/**
 * Tests for {@link NonLinearConjugateGradientOptimizer} class.
 */
public class NonLinearConjugateGradientOptimizerTest {

    /**
     * Test for constructor with default {@link SimpleValueChecker checker},
     * {@link BrentSolver line search solver} and
     * {@link IdentityPreconditioner preconditioner}.
     */
    @Test
    public void testConstructorDefault() {
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.FLETCHER_REEVES);
        Assert.assertEquals(ConjugateGradientFormula.FLETCHER_REEVES, optimizer.getUpdateFormula());
        Assert.assertNotNull(optimizer.getConvergenceChecker());
        Assert.assertSame(BrentSolver.class, optimizer.getLineSearchSolver().getClass());
        Assert.assertNull(optimizer.getPreconditioner());
    }

    /**
     * Test for constructor with default {@link IdentityPreconditioner preconditioner}.
     */
    @Test
    public void testConstructorWithCheckerAndIdentityPreconditioner() {
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(
                ConjugateGradientFormula.FLETCHER_REEVES,
                new SimpleValueChecker());
        Assert.assertEquals(ConjugateGradientFormula.FLETCHER_REEVES, optimizer.getUpdateFormula());
        Assert.assertNotNull(optimizer.getConvergenceChecker());
        Assert.assertSame(BrentSolver.class, optimizer.getLineSearchSolver().getClass());
        Assert.assertNotNull(optimizer.getPreconditioner());
        Assert.assertTrue(optimizer.getPreconditioner() instanceof IdentityPreconditioner);
    }

    /**
     * Test for constructor with custom {@link ConvergenceChecker checker},
     * {@link BrentSolver line search solver} and
     * {@link IdentityPreconditioner preconditioner}.
     */
    @Test
    public void testConstructorWithCustomCheckerAndIdentityPreconditioner() {
        final ConvergenceChecker<PointValuePair> customChecker = new ConvergenceChecker<PointValuePair>() {
            @Override
            public boolean converged(int iteration, PointValuePair startValue, PointValuePair currentValue) {
                // Custom convergence check
                return iteration > 10;
            }
        };
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(
                ConjugateGradientFormula.FLETCHER_REEVES,
                customChecker,
                new BrentSolver());
        Assert.assertEquals(ConjugateGradientFormula.FLETCHER_REEVES, optimizer.getUpdateFormula());
        Assert.assertSame(customChecker, optimizer.getConvergenceChecker());
        Assert.assertSame(BrentSolver.class, optimizer.getLineSearchSolver().getClass());
        Assert.assertNotNull(optimizer.getPreconditioner());
        Assert.assertTrue(optimizer.getPreconditioner() instanceof IdentityPreconditioner);
    }

    /**
     * Test for constructor with custom {@link ConvergenceChecker checker},
     * {@link BrentSolver line search solver} and
     * {@link MyPreconditioner preconditioner}.
     */
    @Test
    public void testConstructorWithCustomCheckerAndCustomPreconditioner() {
        class MyPreconditioner implements Preconditioner {
            @Override
            public double[] precondition(double[] variables, double[] r) {
                // Custom preconditioning logic
                double[] p = new double[variables.length];
                for (int i = 0; i < p.length; i++) {
                    p[i] = r[i] * 2;
                }
                return p;
            }
        }
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(
                ConjugateGradientFormula.FLETCHER_REEVES,
                new SimpleValueChecker(),
                new BrentSolver(),
                new MyPreconditioner());
        Assert.assertEquals(ConjugateGradientFormula.FLETCHER_REEVES, optimizer.getUpdateFormula());
        Assert.assertNotNull(optimizer.getConvergenceChecker());
        Assert.assertSame(BrentSolver.class, optimizer.getLineSearchSolver().getClass());
        Assert.assertNotNull(optimizer.getPreconditioner());
        Assert.assertTrue(optimizer.getPreconditioner() instanceof MyPreconditioner);
    }

    /**
     * Test setting initial step.
     */
    @Test
    public void testSetInitialStep() {
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(
                ConjugateGradientFormula.FLETCHER_REEVES,
                new SimpleValueChecker(),
                new BrentSolver(),
                new IdentityPreconditioner());
        optimizer.setInitialStep(2.0);
        Assert.assertEquals(2.0, optimizer.getInitialStep(), 0);
    }
}