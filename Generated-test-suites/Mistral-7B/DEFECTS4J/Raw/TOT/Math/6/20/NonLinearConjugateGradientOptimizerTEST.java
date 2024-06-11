package org.apache.commons.math3.optimization.general;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.analysis.solvers.BrentSolver;
import org.apache.commons.math3.analysis.solvers.UnivariateSolver;
import org.apache.commons.math3.exception.MathIllegalStateException;
import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleValueChecker;
import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.apache.commons.math3.optim.nonlinear.conjugategradient.NonlinearConjugateGradientOptimizer;
import org.apache.commons.math3.optim.nonlinear.conjugategradient.ConjugateGradientFormula;
import org.apache.commons.math3.optim.nonlinear.conjugategradient.IdentityPreconditioner;
import org.apache.commons.math3.optim.nonlinear.conjugategradient.Preconditioner;
import org.apache.commons.math3.optim.nonlinear.conjugategradient.LineSearchFunction;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Test class for {@link NonLinearConjugateGradientOptimizer}.
 */
public class NonLinearConjugateGradientOptimizerTest {

    private UnivariateFunction testFunction;
    private UnivariateOptimizer optimizer;

    @Before
    public void setUp() {
        testFunction = new MyTestFunction();
        optimizer = new NonlinearConjugateGradientOptimizer(ConjugateGradientFormula.POLAK_RIBIERE,
                new SimpleValueChecker(), new BrentSolver());
    }

    /**
     * Test optimizer with identity preconditioner.
     * @throws Exception if optimization fails.
     */
    @Test
    public void testIdentityPreconditioner() throws Exception {
        optimizer.setInitialStep(1.0);
        PointValuePair result = optimizer.optimize(testFunction, null);
        Assert.assertEquals(3.0, result.getPoint()[0], 1e-6);
        Assert.assertEquals(0.0, result.getValue(), 1e-6);
    }

    /**
     * Test optimizer with custom preconditioner.
     * @throws Exception if optimization fails.
     */
    @Test
    public void testCustomPreconditioner() throws Exception {
        Preconditioner customPreconditioner = new CustomPreconditioner();
        optimizer = new NonlinearConjugateGradientOptimizer(ConjugateGradientFormula.POLAK_RIBIERE,
                new SimpleValueChecker(), new BrentSolver(), customPreconditioner);
        optimizer.setInitialStep(1.0);
        PointValuePair result = optimizer.optimize(testFunction, null);
        Assert.assertEquals(2.0, result.getPoint()[0], 1e-6);
        Assert.assertEquals(2.0, result.getValue(), 1e-6);
    }

    /**
     * Test optimization with wrong update formula.
     * @throws Exception if optimization is not thrown.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testWrongUpdateFormula() throws Exception {
        NonlinearConjugateGradientOptimizer optimizer = new NonlinearConjugateGradientOptimizer(null,
                new SimpleValueChecker(), new BrentSolver(), new IdentityPreconditioner());
    }

    /**
     * Test optimization with null line search solver.
     * @throws Exception if optimization is not thrown.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNullLineSearchSolver() throws Exception {
        NonlinearConjugateGradientOptimizer optimizer = new NonlinearConjugateGradientOptimizer(
                ConjugateGradientFormula.POLAK_RIBIERE,
                new SimpleValueChecker(), null, new IdentityPreconditioner());
    }

    /**
     * Test optimization with null convergence checker.
     * @throws Exception if optimization is not thrown.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNullConvergenceChecker() throws Exception {
        NonlinearConjugateGradientOptimizer optimizer = new NonlinearConjugateGradientOptimizer(
                ConjugateGradientFormula.POLAK_RIBIERE, null, new BrentSolver(), new IdentityPreconditioner());
    }

    /**
     * Test optimization with null preconditioner.
     * @throws Exception if optimization is not thrown.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNullPreconditioner() throws Exception {
        NonlinearConjugateGradientOptimizer optimizer = new NonlinearConjugateGradientOptimizer(
                ConjugateGradientFormula.POLAK_RIBIERE,
                new SimpleValueChecker(), new BrentSolver(), null);
    }

    /**
     * Test optimization with non-positive initial step.
     * @throws Exception if optimization is not thrown.
     */
    @Test(expected = MathIllegalStateException.class)
    public void testNegativeInitialStep() throws Exception {
        optimizer.setInitialStep(-1.0);
        optimizer.optimize(testFunction, null);
    }

    /**
     * Test optimization with wrong goal type.
     * @throws Exception if optimization is not thrown.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testWrongGoalType() throws Exception {
        NonlinearConjugateGradientOptimizer optimizer = new NonlinearConjugateGradientOptimizer(
                ConjugateGradientFormula.POLAK_RIBIERE,
                new SimpleValueChecker(), new BrentSolver(), new IdentityPreconditioner()) {
            @Override
            public GoalType getGoalType() {
                return GoalType.MAXIMIZE;
            }
        };
        optimizer.optimize(testFunction, null);
    }

    /**
     * Test optimization with large number of evaluations.
     * @throws Exception if optimization is not thrown.
     */
    @Test
    public void testLargeNumberOfEvaluations() throws Exception {
        optimizer.setMaxEvaluations(MaxEval.UNLIMITED);
        PointValuePair result = optimizer.optimize(testFunction, null);
        Assert.assertEquals(0.0, result.getValue(), 1e-6);
    }

    /**
     * Test optimization with wrong convergence checker.
     * @throws Exception if optimization is not thrown.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testWrongConvergenceChecker() throws Exception {
        NonlinearConjugateGradientOptimizer optimizer = new NonlinearConjugateGradientOptimizer(
                ConjugateGradientFormula.POLAK_RIBIERE,
                null, new BrentSolver(), new IdentityPreconditioner()) {
            @Override
            public ConvergenceChecker<PointValuePair> getConvergenceChecker() {
                return null;
            }
        };
        optimizer.optimize(testFunction, null);
    }

    /**
     * Test optimization with wrong solver.
     * @throws Exception if optimization is not thrown.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testWrongSolver() throws Exception {
        NonlinearConjugateGradientOptimizer optimizer = new NonlinearConjugateGradientOptimizer(
                ConjugateGradientFormula.POLAK_RIBIERE,
                new SimpleValueChecker(), null, new IdentityPreconditioner()) {
            @Override
            public UnivariateSolver getSolver() {
                return null;
            }
        };
        optimizer.optimize(testFunction, null);
    }

    private static class MyTestFunction implements UnivariateFunction {

        @Override
        public double value(double x) {
            return Math.pow(x, 3) + Math.sin(x);
        }

        @Override
        public double derivative(double x) {
            return 3 * Math.pow(x, 2) + Math.cos(x);
        }
    }

    private static class CustomPreconditioner implements Preconditioner {

        @Override
        public double[] precondition(double[] variables, double[] r) {
            for (int i = 0; i < r.length; ++i) {
                r[i] *= 2;
            }
            return r;
        }
    }
}