package org.apache.commons.math3.optimization.general;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.analysis.FunctionUtils;
import org.apache.commons.math3.analysis.solvers.BracketedRealRootFinder;
import org.apache.commons.math3.analysis.solvers.BrentSolver;
import org.apache.commons.math3.analysis.solvers.LagrangeSolver;
import org.apache.commons.math3.analysis.solvers.SecantSolver;
import org.apache.commons.math3.exception.MaxEvalExceededException;
import org.apache.commons.math3.exception.NoBracketException;
import org.apache.commons.math3.exception.UnsupportedOperationException;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleValueChecker;
import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.NonLinearConjugateGradientOptimizer;
import org.apache.commons.math3.optim.nonlinear.scalar.Preconditioner;
import org.apache.commons.math3.optim.nonlinear.scalar.ScalarDifferentiableOptimizer;
import org.apache.commons.math3.optim.nonlinear.scalar.NLinearConjugateGradientFormula;
import org.apache.commons.math3.optim.nonlinear.scalar.LBFGSPreconditioner;
import org.apache.commons.math3.optim.nonlinear.scalar.IdentityPreconditioner;
import org.apache.commons.math3.optim.nonlinear.scalar.NLinearFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.NLinearFunctionGradient;
import org.apache.commons.math3.optim.nonlinear.scalar.ObjectiveFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.ObjectiveFunctionGradient;
import org.apache.commons.math3.special.E;
import org.apache.commons.math3.special.MathConstants;

/**
 * Tests for {@link NonLinearConjugateGradientOptimizer}.
 */
public class NonLinearConjugateGradientOptimizerTest {

    /**
     * Test of the constructor with default parameters.
     */
    @org.junit.Test
    public void testNonLinearConjugateGradientOptimizerConstructorDefault() {
        final ConjugateGradientFormula formula = NLinearConjugateGradientFormula.POLAK_RIBIERE;

        final NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(formula);
        assertNotNull(optimizer);
        assertEquals(formula, optimizer.getUpdateFormula());
        assertNull(optimizer.getPreconditioner());
        assertNull(optimizer.getSolver());
        assertEquals(1.0, optimizer.getInitialStep(), 0.0);
    }

    /**
     * Test of the constructor with custom parameters.
     */
    @org.junit.Test
    public void testNonLinearConjugateGradientOptimizerConstructorCustom() {
        final ConjugateGradientFormula formula = NLinearConjugateGradientFormula.FLETCHER_REEVES;
        final ConvergenceChecker<PointValuePair> checker = new SimpleValueChecker(1.0e-6, 1.0e-6);
        final UnivariateSolver solver = new BrentSolver();
        final Preconditioner preconditioner = new IdentityPreconditioner();

        final NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(formula, checker, solver, preconditioner);
        assertNotNull(optimizer);
        assertEquals(formula, optimizer.getUpdateFormula());
        assertNotNull(optimizer.getPreconditioner());
        assertNotNull(optimizer.getSolver());
        assertEquals(1.0, optimizer.getInitialStep(), 0.0);
    }

    /**
     * Test of the {@link NonLinearConjugateGradientOptimizer#setInitialStep(double)} method.
     */
    @org.junit.Test
    public void testNonLinearConjugateGradientOptimizerSetInitialStep() {
        final ConjugateGradientFormula formula = NLinearConjugateGradientFormula.FLETCHER_REEVES;
        final ConvergenceChecker<PointValuePair> checker = new SimpleValueChecker(1.0e-6, 1.0e-6);
        final UnivariateSolver solver = new BrentSolver();
        final Preconditioner preconditioner = new IdentityPreconditioner();
        final NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(formula, checker, solver, preconditioner);

        optimizer.setInitialStep(2.0);
        assertEquals(2.0, optimizer.getInitialStep(), 0.0);
    }

    /**
     * Test of the {@link NonLinearConjugateGradientOptimizer#optimize()} method with a simple function.
     */
    @org.junit.Test
    public void testNonLinearConjugateGradientOptimizerOptimizeSimpleFunction() {
        final ObjectiveFunction objFunc = new NLinearFunction(MathConstants.PI, new double[]{-MathConstants.PI, MathConstants.PI}, a -> Math.pow(Math.sin(a), 2));
        final ConvergenceChecker<PointValuePair> checker = new SimpleValueChecker(1.0e-6, 1.0e-6);
        final NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(NLinearConjugateGradientFormula.POLAK_RIBIERE, checker, new BrentSolver());

        final PointValuePair optPoint = optimizer.optimize(objFunc, new PointValuePair[]{new PointValuePair(0.0, objFunc.value(0.0))});
        assertEquals(Math.PI / 2, optPoint.getPoint()[0], 1.0e-3);
        assertEquals(-Math.pow(Math.SIN_PI_HALF, 2), optPoint.getValue(), 1.0e-3);
    }

    /**
     * Test of the {@link NonLinearConjugateGradientOptimizer#optimize()} method with a function that has multiple roots.
     */
    @org.junit.Test
    public void testNonLinearConjugateGradientOptimizerOptimizeMultipleRootsFunction() {
        final ObjectiveFunction objFunc = new NLinearFunction(0.0, new double[]{-2.0, 2.0}, a -> Math.pow(Math.sin(a) - 1, 2));
        final ConvergenceChecker<PointValuePair> checker = new SimpleValueChecker(1.0e-6, 1.0e-6);
        final NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(NLinearConjugateGradientFormula.FLETCHER_REEVES, checker, new BrentSolver());

        final PointValuePair optPoint = optimizer.optimize(objFunc, new PointValuePair[]{new PointValuePair(0.0, objFunc.value(0.0)), new PointValuePair(Math.PI, objFunc.value(Math.PI))});
        assertEquals(-1.0, optPoint.getPoint()[0], 1.0e-3);
        assertEquals(0.0, optPoint.getValue(), 1.0e-3);
    }

    /**
     * Test of the {@link NonLinearConjugateGradientOptimizer#optimize()} method with a function that has no root.
     */
    @org.junit.Test(expected = UnsupportedOperationException.class)
    public void testNonLinearConjugateGradientOptimizerOptimizeNoRootFunction() {
        final ObjectiveFunction objFunc = a -> Math.pow(a, 2) - 1;
        final ConvergenceChecker<PointValuePair> checker = new SimpleValueChecker(1.0e-6, 1.0e-6);
        final NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(NLinearConjugateGradientFormula.POLAK_RIBIERE, checker, new BrentSolver());

        optimizer.optimize(objFunc, new PointValuePair[]{new PointValuePair(0.0, objFunc.value(0.0))});
    }

    /**
     * Test of the {@link NonLinearConjugateGradientOptimizer#optimize()} method with a function that has a root but optimization fails due to max evaluations.
     */
    @org.junit.Test(expected = MaxEvalExceededException.class)
    public void testNonLinearConjugateGradientOptimizerOptimizeMaxEvalFunction() {
        final ObjectiveFunction objFunc = a -> Math.pow(Math.sin(a), 2);
        final ConvergenceChecker<PointValuePair> checker = new SimpleValueChecker(1.0e-6, 1.0e-6);
        final NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(NLinearConjugateGradientFormula.FLETCHER_REEVES, checker, new BrentSolver());

        optimizer.setMaxEvaluations(1);
        optimizer.optimize(objFunc, new PointValuePair[]{new PointValuePair(0.0, objFunc.value(0.0))});
    }

    /**
     * Test of the {@link NonLinearConjugateGradientOptimizer#optimize()} method with a function that has a root but optimization fails due to no bracket.
     */
    @org.junit.Test(expected = NoBracketException.class)
    public void testNonLinearConjugateGradientOptimizerOptimizeNoBracketFunction() {
        final ObjectiveFunction objFunc = a -> Math.pow(Math.sin(a), 2);
        final ConvergenceChecker<PointValuePair> checker = new SimpleValueChecker(1.0e-6, 1.0e-6);
        final NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(NLinearConjugateGradientFormula.FLETCHER_REEVES, checker, new SecantSolver());

        optimizer.optimize(objFunc, new PointValuePair[]{new PointValuePair(0.0, objFunc.value(0.0))});
    }

    /**
     * Test of the {@link NonLinearConjugateGradientOptimizer#optimize()} method with a function that has a root but optimization fails due to non-convergence.
     */
    @org.junit.Test
    public void testNonLinearConjugateGradientOptimizerOptimizeNonConvergenceFunction() {
        final ObjectiveFunction objFunc = a -> Math.pow(Math.sin(a), 2);
        final ConvergenceChecker<PointValuePair> checker = new SimpleValueChecker(1.0e-6, 1.0e-6);
        final NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(NLinearConjugateGradientFormula.FLETCHER_REEVES, checker, new LagrangeSolver());

        assertFalse(optimizer.optimize(objFunc, new PointValuePair[]{new PointValuePair(0.0, objFunc.value(0.0))}) != null);
    }

    /**
     * Test of the {@link NonLinearConjugateGradientOptimizer#optimize()} method with a function that has a root and different preconditioners.
     */
    @org.junit.Test
    public void testNonLinearConjugateGradientOptimizerOptimizeWithPreconditioners() {
        final ObjectiveFunction objFunc = a -> Math.pow(Math.sin(a), 2);
        final ConvergenceChecker<PointValuePair> checker = new SimpleValueChecker(1.0e-6, 1.0e-6);
        final NonLinearConjugateGradientOptimizer optimizerIdentity = new NonLinearConjugateGradientOptimizer(NLinearConjugateGradientFormula.POLAK_RIBIERE, checker, new BrentSolver(), new IdentityPreconditioner());
        final NonLinearConjugateGradientOptimizer optimizerLBFGS = new NonLinearConjugateGradientOptimizer(NLinearConjugateGradientFormula.FLETCHER_REEVES, checker, new BrentSolver(), new LBFGSPreconditioner());

        final PointValuePair optPointIdentity = optimizerIdentity.optimize(objFunc, new PointValuePair[]{new PointValuePair(0.0, objFunc.value(0.0))});
        final PointValuePair optPointLBFGS = optimizerLBFGS.optimize(objFunc, new PointValuePair[]{new PointValuePair(0.0, objFunc.value(0.0))});

        assertEquals(Math.PI / 2, optPointIdentity.getPoint()[0], 1.0e-3);
        assertEquals(-Math.pow(Math.SIN_PI_HALF, 2), optPointIdentity.getValue(), 1.0e-3);

        assertEquals(Math.PI / 2, optPointLBFGS.getPoint()[0], 1.0e-3);
        assertEquals(-Math.pow(Math.SIN_PI_HALF, 2), optPointLBFGS.getValue(), 1.0e-3);

        assertArrayEquals(new double[]{1.0, 0.0}, optimizerLBFGS.getPreconditioner().precondition(new double[]{0.0, 1.0}, new double[]{1.0, 0.0}), 1.0e-3);
    }

    /**
     * Test of the {@link NonLinearConjugateGradientOptimizer#optimize()} method with a function that has a root and custom goal type.
     */
    @org.junit.Test
    public void testNonLinearConjugateGradientOptimizerOptimizeCustomGoalTypeFunction() {
        final ObjectiveFunction objFunc = a -> Math.pow(Math.sin(a), 2);
        final ConvergenceChecker<PointValuePair> checker = new SimpleValueChecker(1.0e-6, 1.0e-6);
        final NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(NLinearConjugateGradientFormula.FLETCHER_REEVES, checker, new BrentSolver(), new IdentityPreconditioner(), GoalType.MAXIMIZE);

        final PointValuePair optPoint = optimizer.optimize(objFunc, new PointValuePair[]{new PointValuePair(0.0, objFunc.value(0.0))});
        assertEquals(-Math.PI / 2, optPoint.getPoint()[0], 1.0e-3);
        assertEquals(Math.pow(Math.SIN_PI_HALF, 2), optPoint.getValue(), 1.0e-3);
    }

}