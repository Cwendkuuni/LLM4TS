package org.apache.commons.math3.optimization.general;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.analysis.solvers.BrentSolver;
import org.apache.commons.math3.analysis.solvers.UnivariateSolver;
import org.apache.commons.math3.exception.MathIllegalStateException;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.PointValuePair;
import org.apache.commons.math3.optimization.SimpleValueChecker;
import org.apache.commons.math3.optimization.ConvergenceChecker;
import org.apache.commons.math3.optimization.NonLinearConjugateGradientOptimizer;
import org.apache.commons.math3.optimization.ConjugateGradientFormula;
import org.apache.commons.math3.optimization.Preconditioner;
import org.apache.commons.math3.optimization.AbstractScalarDifferentiableOptimizer;
import org.apache.commons.math3.optimization.SimpleBounds;
import org.apache.commons.math3.optimization.linear.*;
import org.apache.commons.math3.util.FastMath;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Test suite for {@link NonLinearConjugateGradientOptimizer}.
 */
public class NonLinearConjugateGradientOptimizerTest {

    /** Test function for optimization. */
    private static final UnivariateFunction testFunction = new QuadraticFunction(-1.0, 2.0, 1.0);

    /** Test goal type. */
    private static final GoalType GOAL_TYPE = GoalType.MINIMIZE;

    /** Test convergence checker. */
    private static final SimpleValueChecker checker = new SimpleValueChecker(1.0e-10);

    /** Test preconditioner. */
    private static final IdentityPreconditioner preconditioner = new IdentityPreconditioner();

    /** Test optimizer. */
    private NonLinearConjugateGradientOptimizer optimizer;

    /** Test solver. */
    private final UnivariateSolver solver = new BrentSolver();

    /** Test point. */
    private double[] point;

    /** Test objective value. */
    private double objectiveValue;

    /** Test gradient. */
    private double[] gradient;

    /** Test initial step. */
    private double initialStep;

    /** Set up the test environment. */
    @Before
    public void setUp() {
        initialStep = 1.0;
        point = new double[]{-1.0, -1.0};
        objectiveValue = testFunction.value(point[0], point[1]);
        gradient = testFunction.valuePartials(point[0], point[1]);
        optimizer = new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.FLETCHER_REEVES, checker, solver, preconditioner);
        optimizer.setInitialStep(initialStep);
    }

    /** Test the constructor with default update formula. */
    @Test
    public void testDefaultConstructor() {
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer();
        Assert.assertEquals(ConjugateGradientFormula.FLETCHER_REEVES, optimizer.getUpdateFormula());
    }

    /** Test optimization process. */
    @Test
    public void testOptimize() {
        optimizer.optimize();
        Assert.assertArrayEquals(optimizer.getPoint(), new double[]{testFunction.findRoot(testFunction.valuePartials(optimizer.getPoint()))}, 0.0);
        Assert.assertEquals(GOAL_TYPE, optimizer.getGoalType());
        Assert.assertTrue(optimizer.isConverged());
    }

    /** Test optimization with initial point outside of the bounds. */
    @Test(expected = MathIllegalStateException.class)
    public void testOptimizeOutsideBounds() {
        optimizer = new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.FLETCHER_REEVES, checker, solver, preconditioner);
        optimizer.optimize(new SimpleBounds(-10.0, 10.0));
    }

    /** Test optimization with a non-positive initial step. */
    @Test
    public void testOptimizeWithNonPositiveInitialStep() {
        optimizer = new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.FLETCHER_REEVES, checker, solver, preconditioner);
        optimizer.setInitialStep(-1.0);
        optimizer.optimize();
        Assert.assertEquals(initialStep, optimizer.getInitialStep(), 0.0);
    }

    /** Test optimization with custom update formula. */
    @Test
    public void testOptimizeWithPolakRibiereUpdateFormula() {
        optimizer = new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.POLAK_RIBIERE, checker, solver, preconditioner);
        optimizer.optimize();
        Assert.assertEquals(GOAL_TYPE, optimizer.getGoalType());
        Assert.assertTrue(optimizer.isConverged());
    }

    /** Test optimization with custom convergence checker. */
    @Test
    public void testOptimizeWithCustomConvergenceChecker() {
        ConvergenceChecker<PointValuePair> customChecker = new CustomConvergenceChecker();
        optimizer = new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.FLETCHER_REEVES, customChecker, solver, preconditioner);
        optimizer.optimize();
        Assert.assertTrue(customChecker.isConverged(optimizer.getIterations(), optimizer.getPreviousPoint(), optimizer.getCurrentPoint()));
    }

    /** Test optimization with custom line search solver. */
    @Test
    public void testOptimizeWithCustomLineSearchSolver() {
        UnivariateSolver customSolver = new QuadraticSolver();
        optimizer = new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.FLETCHER_REEVES, checker, customSolver, preconditioner);
        optimizer.optimize();
        Assert.assertEquals(GOAL_TYPE, optimizer.getGoalType());
        Assert.assertTrue(optimizer.isConverged());
    }

    /** Test optimization with custom preconditioner. */
    @Test
    public void testOptimizeWithCustomPreconditioner() {
        Preconditioner customPreconditioner = new QuadraticPreconditioner();
        optimizer = new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.FLETCHER_REEVES, checker, solver, customPreconditioner);
        optimizer.optimize();
        Assert.assertEquals(GOAL_TYPE, optimizer.getGoalType());
        Assert.assertTrue(optimizer.isConverged());
    }

    /** Test optimization failure due to no bracketing. */
    @Test(expected = MathIllegalStateException.class)
    public void testOptimizeFailure() {
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.FLETCHER_REEVES, checker, solver, preconditioner);
        optimizer.optimize(new SimpleBounds(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY));
    }

    /** Test optimization failure due to no convergence. */
    @Test
    public void testOptimizeNoConvergence() {
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.FLETCHER_REEVES, checker, solver, preconditioner);
        optimizer.setMaxIterations(0);
        optimizer.optimize();
        Assert.assertFalse(optimizer.isConverged());
    }

    /** Test optimization failure due to max evaluations. */
    @Test
    public void testOptimizeMaxEvaluations() {
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.FLETCHER_REEVES, checker, solver, preconditioner);
        optimizer.setMaxIterations(Integer.MAX_VALUE);
        optimizer.setMaxEvaluations(Integer.MAX_VALUE / 2);
        optimizer.optimize();
        Assert.assertFalse(optimizer.isConverged());
    }

    /** Test optimization failure due to no bracketing in line search. */
    @Test(expected = MathIllegalStateException.class)
    public void testOptimizeNoBracketingInLineSearch() {
        optimizer = new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.FLETCHER_REEVES, checker, solver, preconditioner);
        optimizer.optimize(new SimpleBounds(-10.0, -10.0));
    }
}

/** Custom convergence checker for testing. */
class CustomConvergenceChecker implements ConvergenceChecker<PointValuePair> {

    /** {@inheritDoc} */
    public boolean isConverged(int iterations, PointValuePair previous, PointValuePair current) {
        double error = FastMath.max(FastMath.abs(previous.getValue() - current.getValue()), FastMath.abs(previous.getPoint()[0] - current.getPoint()[0]));
        return error <= 1.0e-6;
    }
}

/** Quadratic function for testing. */
class QuadraticFunction implements UnivariateFunction {

    /** Coefficient a. */
    private final double a;

    /** Coefficient b. */
    private final double b;

    /** Coefficient c. */
    private final double c;

    /** Constructor.
     * @param a coefficient a
     * @param b coefficient b
     * @param c coefficient c
     */
    public QuadraticFunction(double a, double b, double c) {
        this.a = a;
        this.b = b;
        this.c = c;
    }

    /** {@inheritDoc} */
    public double value(double x) {
        return a * x * x + b * x + c;
    }

    /** {@inheritDoc} */
    public double valuePartials(double x) {
        return new LinearFunction(-2.0 * a * x - b).value(1.0);
    }

    /** {@inheritDoc} */
    public double findRoot(double guess) {
        return -b / (2.0 * a);
    }
}

/** Linear function for testing. */
class LinearFunction implements UnivariateFunction {

    /** Coefficient a. */
    private final double a;

    /** Constructor.
     * @param a coefficient a
     */
    public LinearFunction(double a) {
        this.a = a;
    }

    /** {@inheritDoc} */
    public double value(double x) {
        return a * x;
    }
}

/** Quadratic preconditioner for testing. */
class QuadraticPreconditioner implements Preconditioner {

    /** {@inheritDoc} */
    public double[] precondition(double[] variables, double[] r) {
        double[] preconditioned = new double[variables.length];
        for (int i = 0; i < variables.length; ++i) {
            preconditioned[i] = r[i] / (2.0 * testFunction.valuePartials(variables[i]));
        }
        return preconditioned;
    }
}

/** Quadratic solver for testing. */
class QuadraticSolver implements UnivariateSolver {

    /** {@inheritDoc} */
    public double solve(int maxIterations, UnivariateFunction func, double initialGuess, double lowerBound, double upperBound) {
        for (int i = 0; i < maxIterations; ++i) {
            double x = initialGuess;
            double fx = func.value(x);
            double fpx = func.valuePartials(x);
            double xNew = x - fx / (2.0 * fpx);
            double error = FastMath.abs(x - xNew);
            if (error <= 1.0e-6) {
                return xNew;
            }
            x = xNew;
        }
        throw new IllegalStateException("Failed to converge.");
    }
}