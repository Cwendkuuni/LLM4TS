package org.apache.commons.math3.optimization.general;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.MathIllegalStateException;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleValueChecker;
import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.apache.commons.math3.optim.initializer.AverageInitializer;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.Preconditioner;
import org.apache.commons.math3.optim.nonlinear.scalar.ScalarOptimizationProblem;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.BrentSolver;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.IdentityPreconditioner;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.LineSearchFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.NonLinearConjugateGradientOptimizer;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.SimpleValueCheckerBrent;
import org.apache.commons.math3.special.Fraction;
import org.junit.Test;

/**
 * Tests for the {@link NonLinearConjugateGradientOptimizer} class.
 */
public class NonLinearConjugateGradientOptimizerTest {

    /**
     * Tests constructor with default solver, checker and preconditioner.
     */
    @Test
    public void testDefaultConstructor() {
        final NonLinearConjugateGradientOptimizer optimizer =
                new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.FLETCHER_REEVES);
        assertNotNull(optimizer);
    }

    /**
     * Tests constructor with custom solver, checker and preconditioner.
     */
    @Test
    public void testCustomConstructors() {
        final UnivariateFunction function = f -> Math.pow(f, 2);
        final ScalarOptimizationProblem problem = new ScalarOptimizationProblem(0.0, 1.0, function, GoalType.MINIMIZE);
        final Preconditioner preconditioner = new IdentityPreconditioner();
        final ConvergenceChecker<PointValuePair> checker = new SimpleValueCheckerBrent();
        final UnivariateSolver solver = new BrentSolver();

        final NonLinearConjugateGradientOptimizer optimizer =
                new NonLinearConjugateGradientOptimizer(
                        ConjugateGradientFormula.FLETCHER_REEVES,
                        checker,
                        solver,
                        preconditioner);
        assertNotNull(optimizer);
    }

    /**
     * Tests setInitialStep method.
     */
    @Test
    public void testSetInitialStep() {
        final UnivariateFunction function = f -> Math.pow(f, 2);
        final ScalarOptimizationProblem problem = new ScalarOptimizationProblem(0.0, 1.0, function, GoalType.MINIMIZE);
        final Preconditioner preconditioner = new IdentityPreconditioner();
        final ConvergenceChecker<PointValuePair> checker = new SimpleValueCheckerBrent();
        final UnivariateSolver solver = new BrentSolver();
        final NonLinearConjugateGradientOptimizer optimizer =
                new NonLinearConjugateGradientOptimizer(
                        ConjugateGradientFormula.FLETCHER_REEVES,
                        checker,
                        solver,
                        preconditioner);

        optimizer.setInitialStep(0.5);
        assertEquals(0.5, optimizer.getInitialStep(), 0.0);

        optimizer.setInitialStep(-1.0);
        assertEquals(1.0, optimizer.getInitialStep(), 0.0);
    }

    /**
     * Tests optimization with default values.
     */
    @Test
    public void testOptimizationWithDefaultValues() {
        final UnivariateFunction function = f -> Math.pow(f, 2);
        final ScalarOptimizationProblem problem = new ScalarOptimizationProblem(0.0, 1.0, function, GoalType.MINIMIZE);
        final NonLinearConjugateGradientOptimizer optimizer =
                new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.FLETCHER_REEVES);

        final PointValuePair result = optimizer.optimize(problem);
        assertNotNull(result);
        assertEquals(new Fraction(0, 1), result.getPoint(), 0.001);
        assertEquals(0.0, result.getValue(), 0.001);
    }

    /**
     * Tests optimization with custom solver, checker and preconditioner.
     */
    @Test
    public void testOptimizationCustomValues() {
        final UnivariateFunction function = f -> Math.pow(f, 2);
        final ScalarOptimizationProblem problem = new ScalarOptimizationProblem(0.0, 1.0, function, GoalType.MINIMIZE);
        final Preconditioner preconditioner = new IdentityPreconditioner();
        final ConvergenceChecker<PointValuePair> checker = new SimpleValueCheckerBrent();
        final UnivariateSolver solver = new BrentSolver();
        final NonLinearConjugateGradientOptimizer optimizer =
                new NonLinearConjugateGradientOptimizer(
                        ConjugateGradientFormula.FLETCHER_REEVES,
                        checker,
                        solver,
                        preconditioner);

        final PointValuePair result = optimizer.optimize(problem);
        assertNotNull(result);
        assertEquals(new Fraction(0, 1), result.getPoint(), 0.001);
        assertEquals(0.0, result.getValue(), 0.001);
    }

    /**
     * Tests optimization with goal type MAXIMIZE.
     */
    @Test
    public void testOptimizationWithGoalTypeMaximize() {
        final UnivariateFunction function = f -> 1.0 / (f + 1.0);
        final ScalarOptimizationProblem problem = new ScalarOptimizationProblem(0.0, 1.0, function, GoalType.MAXIMIZE);
        final NonLinearConjugateGradientOptimizer optimizer =
                new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.FLETCHER_REEVES);

        final PointValuePair result = optimizer.optimize(problem);
        assertNotNull(result);
        assertEquals(1.0, result.getPoint(), 0.001);
        assertEquals(1.0, result.getValue(), 0.001);
    }

    /**
     * Tests optimization with custom solver, checker and preconditioner for
     * goal type MAXIMIZE.
     */
    @Test
    public void testOptimizationCustomValuesWithGoalTypeMaximize() {
        final UnivariateFunction function = f -> 1.0 / (f + 1.0);
        final ScalarOptimizationProblem problem = new ScalarOptimizationProblem(0.0, 1.0, function, GoalType.MAXIMIZE);
        final Preconditioner preconditioner = new IdentityPreconditioner();
        final ConvergenceChecker<PointValuePair> checker = new SimpleValueCheckerBrent();
        final UnivariateSolver solver = new BrentSolver();
        final NonLinearConjugateGradientOptimizer optimizer =
                new NonLinearConjugateGradientOptimizer(
                        ConjugateGradientFormula.FLETCHER_REEVES,
                        checker,
                        solver,
                        preconditioner);

        final PointValuePair result = optimizer.optimize(problem);
        assertNotNull(result);
        assertEquals(1.0, result.getPoint(), 0.001);
        assertEquals(1.0, result.getValue(), 0.001);
    }

    /**
     * Tests optimization with invalid arguments.
     */
    @Test(expected = MathIllegalStateException.class)
    public void testOptimizationInvalidArguments() {
        final UnivariateFunction function = f -> Math.pow(f, 2);
        final ScalarOptimizationProblem problem = new ScalarOptimizationProblem(0.0, 1.0, function, GoalType.MINIMIZE);
        final NonLinearConjugateGradientOptimizer optimizer =
                new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.POLAK_RIBIERE);

        optimizer.optimize(problem);
    }

    /**
     * Tests getter and setter for updateFormula.
     */
    @Test
    public void testGetterAndSetterForUpdateFormula() {
        final NonLinearConjugateGradientOptimizer optimizer =
                new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.FLETCHER_REEVES);
        assertEquals(ConjugateGradientFormula.FLETCHER_REEVES, optimizer.getUpdateFormula());

        optimizer.setUpdateFormula(ConjugateGradientFormula.POLAK_RIBIERE);
        assertEquals(ConjugateGradientFormula.POLAK_RIBIERE, optimizer.getUpdateFormula());
    }

    /**
     * Tests getter and setter for preconditioner.
     */
    @Test
    public void testGetterAndSetterForPreconditioner() {
        final NonLinearConjugateGradientOptimizer optimizer =
                new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.FLETCHER_REEVES);
        assertNull(optimizer.getPreconditioner());

        final Preconditioner preconditioner = new IdentityPreconditioner();
        optimizer.setPreconditioner(preconditioner);
        assertSame(preconditioner, optimizer.getPreconditioner());
    }

    /**
     * Tests getter and setter for solver.
     */
    @Test
    public void testGetterAndSetterForSolver() {
        final NonLinearConjugateGradientOptimizer optimizer =
                new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.FLETCHER_REEVES);
        assertNull(optimizer.getSolver());

        final UnivariateSolver solver = new BrentSolver();
        optimizer.setSolver(solver);
        assertSame(solver, optimizer.getSolver());
    }

    /**
     * Tests getter for initial step.
     */
    @Test
    public void testGetterForInitialStep() {
        final NonLinearConjugateGradientOptimizer optimizer =
                new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.FLETCHER_REEVES);
        assertEquals(1.0, optimizer.getInitialStep(), 0.0);
    }

    /**
     * Tests setter for initial step.
     */
    @Test
    public void testSetterForInitialStep() {
        final NonLinearConjugateGradientOptimizer optimizer =
                new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.FLETCHER_REEVES);
        optimizer.setInitialStep(2.0);
        assertEquals(2.0, optimizer.getInitialStep(), 0.0);
    }

    /**
     * Tests optimization with line search function.
     */
    @Test
    public void testOptimizationWithLineSearchFunction() {
        final UnivariateFunction function = x -> Math.pow(x, 2) - 2 * x + 1;
        final ScalarOptimizationProblem problem = new ScalarOptimizationProblem(0.0, 1.0, function, GoalType.MINIMIZE);
        final NonLinearConjugateGradientOptimizer optimizer =
                new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.FLETCHER_REEVES);

        final Preconditioner preconditioner = new IdentityPreconditioner();
        optimizer.setPreconditioner(preconditioner);

        final UnivariateSolver solver = new BrentSolver();
        optimizer.setSolver(solver);

        final PointValuePair result = optimizer.optimize(problem);
        assertNotNull(result);
        assertEquals(0.5, result.getPoint(), 0.001);
        assertEquals(-0.25, result.getValue(), 0.001);

        final LineSearchFunction lineSearchFunction = new LineSearchFunction(new double[]{-1.0, 1.0});
        assertArrayEquals(new double[]{-1.0, 1.0}, lineSearchFunction.getSearchDirection());
        assertFalse(lineSearchFunction.value(0.5) > 0);
    }
}