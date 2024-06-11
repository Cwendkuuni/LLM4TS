package org.apache.commons.cli;

import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.analysis.solvers.BrentSolver;
import org.apache.commons.math3.analysis.solvers.UnivariateSolver;
import org.apache.commons.math3.exception.MathIllegalStateException;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.PointValuePair;
import org.apache.commons.math3.optimization.SimpleValueChecker;
import org.apache.commons.math3.optimization.ConvergenceChecker;
import org.apache.commons.math3.optimization.NonLinearConjugateGradientOptimizer;
import org.apache.commons.math3.optimization.general.ConjugateGradientFormula;
import org.apache.commons.math3.optimization.general.Preconditioner;

/**
 * Test cases for the NonLinearConjugateGradientOptimizer class.
 */
public class NonLinearConjugateGradientOptimizerTest {

    /**
     * Test constructor with default {@link SimpleValueChecker checker},
     * {@link BrentSolver line search solver} and
     * {@link IdentityPreconditioner preconditioner}.
     * @throws Exception if optimization fails.
     */
    @Test
    public void testConstructorWithDefaultSettings() throws Exception {
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.FLETCHER_REEVES);
        assertNotNull(optimizer.getSolver());
        assertNotNull(optimizer.getPreconditioner());
        assertEquals(ConjugateGradientFormula.FLETCHER_REEVES, optimizer.getUpdateFormula());
        assertSame(SimpleValueChecker.class, optimizer.getConvergenceChecker().getClass());
        assertSame(BrentSolver.class, optimizer.getSolver().getClass());
        assertSame(IdentityPreconditioner.class, optimizer.getPreconditioner().getClass());
    }

    /**
     * Test constructor with custom {@link ConvergenceChecker checker},
     * {@link BrentSoler line search solver} and
     * {@link IdentityPreconditioner preconditioner}.
     * @throws Exception if optimization fails.
     */
    @Test
    public void testConstructorWithCustomSettings() throws Exception {
        ConvergenceChecker<PointValuePair> customChecker = new SimpleValueChecker();
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.FLETCHER_REEVES, customChecker);
        assertNotNull(optimizer.getSolver());
        assertNotNull(optimizer.getPreconditioner());
        assertEquals(ConjugateGradientFormula.FLETCHER_REEVES, optimizer.getUpdateFormula());
        assertSame(customChecker.getClass(), optimizer.getConvergenceChecker().getClass());
        assertSame(BrentSolver.class, optimizer.getSolver().getClass());
        assertSame(IdentityPreconditioner.class, optimizer.getPreconditioner().getClass());
    }

    /**
     * Test constructor with default {@link IdentityPreconditioner preconditioner}.
     * @throws Exception if optimization fails.
     */
    @Test
    public void testConstructorWithDefaultPreconditioner() throws Exception {
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.FLETCHER_REEVES, new SimpleValueChecker(), new BrentSolver());
        assertNotNull(optimizer.getSolver());
        assertNotNull(optimizer.getPreconditioner());
        assertEquals(ConjugateGradientFormula.FLETCHER_REEVES, optimizer.getUpdateFormula());
        assertSame(SimpleValueChecker.class, optimizer.getConvergenceChecker().getClass());
        assertSame(BrentSolver.class, optimizer.getSolver().getClass());
        assertSame(IdentityPreconditioner.class, optimizer.getPreconditioner().getClass());
    }

    /**
     * Test constructor with custom {@link UnivariateSolver solver},
     * {@link IdentityPreconditioner preconditioner} and
     * {@link SimpleValueChecker checker}.
     * @throws Exception if optimization fails.
     */
    @Test
    public void testConstructorWithCustomSolver() throws Exception {
        UnivariateSolver customSolver = new BrentSolver();
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.FLETCHER_REEVES, new SimpleValueChecker(), customSolver, new IdentityPreconditioner());
        assertNotNull(optimizer.getSolver());
        assertNotNull(optimizer.getPreconditioner());
        assertEquals(ConjugateGradientFormula.FLETCHER_REEVES, optimizer.getUpdateFormula());
        assertSame(SimpleValueChecker.class, optimizer.getConvergenceChecker().getClass());
        assertSame(customSolver.getClass(), optimizer.getSolver().getClass());
        assertSame(IdentityPreconditioner.class, optimizer.getPreconditioner().getClass());
    }

    /**
     * Test constructor with custom {@link Preconditioner preconditioner},
     * {@link SimpleValueChecker checker} and
     * {@link BrentSolver solver}.
     * @throws Exception if optimization fails.
     */
    @Test
    public void testConstructorWithCustomPreconditioner() throws Exception {
        Preconditioner customPreconditioner = new CustomPreconditioner();
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.FLETCHER_REEVES, new SimpleValueChecker(), new BrentSolver(), customPreconditioner);
        assertNotNull(optimizer.getSolver());
        assertNotNull(optimizer.getPreconditioner());
        assertEquals(ConjugateGradientFormula.FLETCHER_REEVES, optimizer.getUpdateFormula());
        assertSame(SimpleValueChecker.class, optimizer.getConvergenceChecker().getClass());
        assertSame(BrentSolver.class, optimizer.getSolver().getClass());
        assertSame(customPreconditioner.getClass(), optimizer.getPreconditioner().getClass());
    }

    /**
     * Test setInitialStep method.
     * @throws Exception if optimization fails.
     */
    @Test
    public void testSetInitialStep() throws Exception {
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.FLETCHER_REEVES, new SimpleValueChecker(), new BrentSolver(), new IdentityPreconditioner());
        optimizer.setInitialStep(2.5);
        assertEquals(2.5, optimizer.getInitialStep());
        optimizer.setInitialStep(-1);
        assertEquals(1.0, optimizer.getInitialStep());
    }

    /**
     * Test optimization with simple test function.
     * @throws Exception if optimization fails.
     */
    @Test
    public void testOptimizationWithSimpleTestFunction() throws Exception {
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.FLETCHER_REEVES, new SimpleValueChecker(), new BrentSolver(), new IdentityPreconditioner());
        UnivariateFunction testFunction = new TestFunction();
        PointValuePair result = optimizer.optimize(testFunction, null, GoalType.MINIMIZE);
        assertEquals(1.0, result.getPoint()[0], 1e-9);
        assertEquals(-1.0, result.getValue(), 1e-9);
    }

    /**
     * Test optimization with custom test function.
     * @throws Exception if optimization fails.
     */
    @Test
    public void testOptimizationWithCustomTestFunction() throws Exception {
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.POLAK_RIBIERE, new SimpleValueChecker(), new BrentSolver(), new CustomPreconditioner());
        UnivariateFunction testFunction = new CustomTestFunction();
        PointValuePair result = optimizer.optimize(testFunction, null, GoalType.MINIMIZE);
        assertEquals(2.0, result.getPoint()[0], 1e-9);
        assertEquals(-4.0, result.getValue(), 1e-9);
    }

    /**
     * Test optimization with custom preconditioner.
     * @throws Exception if optimization fails.
     */
    @Test
    public void testOptimizationWithCustomPreconditioner() throws Exception {
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.FLETCHER_REEVES, new SimpleValueChecker(), new BrentSolver(), new CustomPreconditioner());
        UnivariateFunction testFunction = new TestFunction();
        PointValuePair result = optimizer.optimize(testFunction, null, GoalType.MINIMIZE);
        assertEquals(1.0, result.getPoint()[0], 1e-9);
        assertEquals(-1.0, result.getValue(), 1e-9);
    }

    private static class TestFunction implements UnivariateFunction {
        @Override
        public double value(double x) {
            return x * x - 2 * x + 1;
        }

        @Override
        public double derivative(double x) {
            return 2 * x - 2;
        }
    }

    private static class CustomTestFunction implements UnivariateFunction {
        @Override
        public double value(double x) {
            return Math.pow(Math.sin(x), 2) - Math.pow(Math.cos(x), 2);
        }

        @Override
        public double derivative(double x) {
            return 2 * Math.sin(x) * Math.cos(x) - 2 * Math.cos(x) * Math.sin(x);
        }
    }

    private static class CustomPreconditioner implements Preconditioner {
        @Override
        public double[] precondition(double[] variables, double[] r) {
            double[] preconditioned = new double[variables.length];
            for (int i = 0; i < variables.length; i++) {
                preconditioned[i] = variables[i] + r[i];
            }
            return preconditioned;
        }
    }
}