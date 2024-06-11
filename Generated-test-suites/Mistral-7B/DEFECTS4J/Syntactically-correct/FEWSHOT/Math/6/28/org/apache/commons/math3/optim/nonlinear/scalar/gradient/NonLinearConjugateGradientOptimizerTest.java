package org.apache.commons.math3.optimization.general;

import static org.junit.Assert.*;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.analysis.solvers.BrentSolver;
import org.apache.commons.math3.analysis.solvers.LinearSolver;
import org.apache.commons.math3.analysis.solvers.UnivariateSolver;
import org.apache.commons.math3.exception.MathIllegalStateException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.optim.*;
import org.apache.commons.math3.optim.initializer.StartPointInitializer;
import org.apache.commons.math3.optim.nonlinear.scalardifferentiable.ConjugateGradientOptimizer;
import org.apache.commons.math3.optim.nonlinear.scalardifferentiable.ConjugateGradientFormula;
import org.apache.commons.math3.optim.point.PointValuePair;
import org.apache.commons.math3.optim.simple.SimpleValueChecker;
import org.apache.commons.math3.optim.initializer.AroundMinimumInitializer;
import org.apache.commons.math3.optim.linear.*;
import org.apache.commons.math3.util.Precision;

public class NonLinearConjugateGradientOptimizerTest {

    private static final UnivariateFunction TEST_FUNCTION = new TestFunction();

    @Test
    public void testDefaultConstructor() {
        ConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.FLETCHER_REEVES);
        assertNull(optimizer.getPreconditioner());
        assertNull(optimizer.getSolver());
        assertEquals(ConjugateGradientFormula.FLETCHER_REEVES, optimizer.getUpdateFormula());
    }

    @Test
    public void testConstructorWithChecker() {
        ConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.FLETCHER_REEVES, new SimpleValueChecker());
        assertNull(optimizer.getPreconditioner());
        assertEquals(ConjugateGradientFormula.FLETCHER_REEVES, optimizer.getUpdateFormula());
        assertNotNull(optimizer.getConvergenceChecker());
    }

    @Test
    public void testConstructorWithCheckerAndSolver() {
        ConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.FLETCHER_REEVES, new SimpleValueChecker(), new BrentSolver());
        assertNull(optimizer.getPreconditioner());
        assertEquals(ConjugateGradientFormula.FLETCHER_REEVES, optimizer.getUpdateFormula());
        assertNotNull(optimizer.getConvergenceChecker());
        assertNotNull(optimizer.getSolver());
    }

    @Test
    public void testConstructorWithCheckerAndSolverAndPreconditioner() {
        LinearOperator preconditioner = new IdentityOperator(2);
        ConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.FLETCHER_REEVES, new SimpleValueChecker(), new BrentSolver(), preconditioner);
        assertEquals(ConjugateGradientFormula.FLETCHER_REEVES, optimizer.getUpdateFormula());
        assertNotNull(optimizer.getConvergenceChecker());
        assertNotNull(optimizer.getSolver());
        assertNotNull(optimizer.getPreconditioner());
    }

    @Test
    public void testSetInitialStep() {
        ConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.FLETCHER_REEVES, new SimpleValueChecker(), new BrentSolver());
        optimizer.setInitialStep(2.0);
        assertEquals(2.0, optimizer.getInitialStep());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetInitialStepNegative() {
        ConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.FLETCHER_REEVES, new SimpleValueChecker(), new BrentSolver());
        optimizer.setInitialStep(-1.0);
    }

    @Test
    public void testOptimize() throws OptimizationException {
        ConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.FLETCHER_REEVES, new SimpleValueChecker(), new BrentSolver());

        double[] startPoint = {1.0, 1.0};
        PointValuePair result = optimizer.optimize(TEST_FUNCTION, startPoint);

        // The test function has a minimum at (0.5, 0.5)
        double[] expectedResult = {0.5, 0.5};
        assertArrayEquals(expectedResult, result.getPoint(), 0.001);
        assertEquals(TEST_FUNCTION.value(expectedResult), result.getValue(), 0.001);
    }

    @Test
    public void testOptimizeWithCustomStartPointInitializer() throws OptimizationException {
        ConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.FLETCHER_REEVES, new SimpleValueChecker(), new BrentSolver());

        double[] startPoint = {1.5, 1.5};
        PointValuePair result = optimizer.optimize(TEST_FUNCTION, new AroundMinimumInitializer(TEST_FUNCTION, startPoint));

        // The test function has a minimum at (0.5, 0.5)
        double[] expectedResult = {0.5, 0.5};
        assertArrayEquals(expectedResult, result.getPoint(), 0.001);
        assertEquals(TEST_FUNCTION.value(expectedResult), result.getValue(), 0.001);
    }

    @Test(expected = MathIllegalStateException.class)
    public void testOptimizeWithNullFunction() {
        ConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.FLETCHER_REEVES, new SimpleValueChecker(), new BrentSolver());
        optimizer.optimize(null, new double[2]);
    }

    @Test(expected = OptimizationException.class)
    public void testOptimizeWithMaxEvaluationsReached() throws OptimizationException {
        ConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.FLETCHER_REEVES, new SimpleValueChecker(), new BrentSolver());

        OptimizationProblem problem = new ScalarOptimizationProblem(TEST_FUNCTION, new double[2]) {
            @Override
            public int getMaxEvaluations() {
                return 0;
            }
        };

        optimizer.optimize(TEST_FUNCTION, new double[2]);
    }

    @Test(expected = MaxCountExceededException.class)
    public void testOptimizeWithMaxEvaluationsReachedInBrentSolver() throws OptimizationException, MaxCountExceededException {
        ConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.FLETCHER_REEVES, new SimpleValueChecker(), new BrentSolver());

        OptimizationProblem problem = new ScalarOptimizationProblem(TEST_FUNCTION, new double[2]) {
            @Override
            public int getMaxEvaluations() {
                return 5;
            }
        };

        optimizer.optimize(TEST_FUNCTION, new double[2]);
    }

    @Test
    public void testOptimizeWithStepSizeZero() throws OptimizationException {
        ConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.FLETCHER_REEVES, new SimpleValueChecker(), new BrentSolver());

        double[] startPoint = new double[2];
        optimizer.setInitialStep(0.0);
        PointValuePair result = optimizer.optimize(TEST_FUNCTION, startPoint);
        assertEquals(startPoint, result.getPoint());
        assertEquals(TEST_FUNCTION.value(startPoint), result.getValue());
    }

    @Test
    public void testOptimizeWithStepSizeZeroAndMaxEvaluations() throws OptimizationException {
        ConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.FLETCHER_REEVES, new SimpleValueChecker(), new BrentSolver());

        double[] startPoint = new double[2];
        optimizer.setInitialStep(0.0);
        OptimizationProblem problem = new ScalarOptimizationProblem(TEST_FUNCTION, startPoint) {
            @Override
            public int getMaxEvaluations() {
                return 1;
            }
        };

        try {
            optimizer.optimize(TEST_FUNCTION, startPoint);
            fail();
        } catch (OptimizationException e) {
            // expected
        }
    }

    @Test
    public void testOptimizeWithStepSizeZeroAndMaxIterations() throws OptimizationException {
        ConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.FLETCHER_REEVES, new SimpleValueChecker(), new BrentSolver());

        double[] startPoint = new double[2];
        optimizer.setInitialStep(0.0);
        OptimizationProblem problem = new ScalarOptimizationProblem(TEST_FUNCTION, startPoint) {
            @Override
            public int getMaxIterations() {
                return 1;
            }
        };

        PointValuePair result = optimizer.optimize(TEST_FUNCTION, startPoint);
        assertEquals(startPoint, result.getPoint());
        assertEquals(TEST_FUNCTION.value(startPoint), result.getValue());
    }

    @Test
    public void testOptimizeWithPreconditioner() throws OptimizationException {
        LinearOperator preconditioner = new IdentityOperator(2);
        ConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.FLETCHER_REEVES, new SimpleValueChecker(), new BrentSolver(), preconditioner);

        double[] startPoint = {1.0, 1.0};
        PointValuePair result = optimizer.optimize(TEST_FUNCTION, startPoint);

        // The test function has a minimum at (0.5, 0.5)
        double[] expectedResult = {0.5, 0.5};
        assertArrayEquals(expectedResult, result.getPoint(), 0.001);
        assertEquals(TEST_FUNCTION.value(expectedResult), result.getValue(), 0.001);
    }

    @Test
    public void testOptimizeWithPreconditionerAndCustomStartPointInitializer() throws OptimizationException {
        LinearOperator preconditioner = new IdentityOperator(2);
        ConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.FLETCHER_REEVES, new SimpleValueChecker(), new BrentSolver(), preconditioner);

        double[] startPoint = {1.5, 1.5};
        PointValuePair result = optimizer.optimize(TEST_FUNCTION, new AroundMinimumInitializer(TEST_FUNCTION, startPoint));

        // The test function has a minimum at (0.5, 0.5)
        double[] expectedResult = {0.5, 0.5};
        assertArrayEquals(expectedResult, result.getPoint(), 0.001);
        assertEquals(TEST_FUNCTION.value(expectedResult), result.getValue(), 0.001);
    }

    @Test
    public void testOptimizeWithPreconditionerAndCustomSolver() throws OptimizationException {
        LinearOperator preconditioner = new IdentityOperator(2);
        ConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.FLETCHER_REEVES, new SimpleValueChecker(), new LinearSolverWrapper(new GaussSeidelSolver(2)), preconditioner);

        double[] startPoint = {1.0, 1.0};
        PointValuePair result = optimizer.optimize(TEST_FUNCTION, startPoint);

        // The test function has a minimum at (0.5, 0.5)
        double[] expectedResult = {0.5, 0.5};
        assertArrayEquals(expectedResult, result.getPoint(), 0.001);
        assertEquals(TEST_FUNCTION.value(expectedResult), result.getValue(), 0.001);
    }

    @Test
    public void testOptimizeWithPreconditionerAndCustomSolverAndCustomStartPointInitializer() throws OptimizationException {
        LinearOperator preconditioner = new IdentityOperator(2);
        ConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.FLETCHER_REEVES, new SimpleValueChecker(), new LinearSolverWrapper(new GaussSeidelSolver(2)), preconditioner);

        double[] startPoint = {1.5, 1.5};
        PointValuePair result = optimizer.optimize(TEST_FUNCTION, new AroundMinimumInitializer(TEST_FUNCTION, startPoint));

        // The test function has a minimum at (0.5, 0.5)
        double[] expectedResult = {0.5, 0.5};
        assertArrayEquals(expectedResult, result.getPoint(), 0.001);
        assertEquals(TEST_FUNCTION.value(expectedResult), result.getValue(), 0.001);
    }

    private static class TestFunction implements UnivariateFunction {
        @Override
        public double value(double x) {
            double[] xArray = {x, x};
            double[] gradient = computeObjectiveGradient(xArray);
            return gradient[0] * x + gradient[1] * x + 1;
        }

        @Override
        public int getDimension() {
            return 2;
        }
    }

    private static double[] computeObjectiveGradient(double[] x) {
        double[] gradient = new double[x.length];
        for (int i = 0; i < x.length; ++i) {
            gradient[i] = 2 * x[i] + 2 * x[i];
        }
        return gradient;
    }
}

class LinearSolverWrapper implements UnivariateSolver {
    private final UnivariateSolver solver;

    public LinearSolverWrapper(UnivariateSolver solver) {
        this.solver = solver;
    }

    @Override
    public double solve(double x, double tolerance) throws UnivariateSolver.CannotSolveNonlinearEquationException {
        throw new UnsupportedOperationException("This solver wrapper does not support direct calls to solve()");
    }

    @Override
    public double solve(double[] x, double tolerance) throws UnivariateSolver.CannotSolveNonlinearEquationException {
        solver.solve(x);
        return x[0];
    }

    @Override
    public int getMaxIterations() {
        return solver.getMaxIterations();
    }

    @Override
    public void setMaxIterations(int maxIterations) {
        solver.setMaxIterations(maxIterations);
    }

    @Override
    public int getMaxEvaluations() {
        return solver.getMaxEvaluations();
    }

    @Override
    public void setMaxEvaluations(int maxEvaluations) {
        solver.setMaxEvaluations(maxEvaluations);
    }
}