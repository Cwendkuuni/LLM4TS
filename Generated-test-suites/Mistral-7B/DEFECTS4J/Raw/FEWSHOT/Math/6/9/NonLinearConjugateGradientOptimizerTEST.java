package org.apache.commons.math3.optimization.general;

import static org.junit.Assert.*;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.analysis.solvers.BrentSolver;
import org.apache.commons.math3.analysis.solvers.UnivariateSolver;
import org.apache.commons.math3.exception.MathIllegalStateException;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleValueChecker;
import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.apache.commons.math3.optim.nonlinear.conjugategradient.ConjugateGradientFormula;
import org.apache.commons.math3.optim.nonlinear.conjugategradient.ConjugateGradientOptimizer;
import org.apache.commons.math3.optim.nonlinear.conjugategradient.IdentityPreconditioner;
import org.apache.commons.math3.optim.nonlinear.conjugategradient.Preconditioner;
import org.apache.commons.math3.optim.simple.TwiceDifferentiableTestFunctions;

/**
 * Test cases for {@link ConjugateGradientOptimizer} class.
 */
public class NonLinearConjugateGradientOptimizerTest {

    /**
     * Test optimization with simple value checker and BrentSolver.
     */
    @Test
    public void testOptimizeSimpleCheckerBrentSolver() {
        UnivariateFunction function = TwiceDifferentiableTestFunctions.ROSENBROCK;
        ConjugateGradientOptimizer optimizer = new ConjugateGradientOptimizer(
                ConjugateGradientFormula.POLAK_RIBIERE,
                new SimpleValueChecker(),
                new BrentSolver());

        PointValuePair solution = optimizer.optimize(function, 0.0, null);
        assertNotNull(solution);
        assertEquals(TwiceDifferentiableTestFunctions.ROSENBROCK_MINIMUM, solution.getValue(), 1e-6);
        assertArrayEquals(TwiceDifferentiableTestFunctions.ROSENBROCK_MINIMUM_POINT, solution.getPoint(), 1e-6);
    }

    /**
     * Test optimization with simple value checker and provided solver.
     */
    @Test
    public void testOptimizeSimpleCheckerCustomSolver() {
        UnivariateFunction function = TwiceDifferentiableTestFunctions.RIDDER;
        ConjugateGradientOptimizer optimizer = new ConjugateGradientOptimizer(
                ConjugateGradientFormula.POLAK_RIBIERE,
                new SimpleValueChecker(),
                null);
        UnivariateSolver solver = new BrentSolver();
        optimizer.setSolver(solver);

        PointValuePair solution = optimizer.optimize(function, 0.0, null);
        assertNotNull(solution);
        assertEquals(TwiceDifferentiableTestFunctions.RIDDER_MINIMUM, solution.getValue(), 1e-6);
        assertArrayEquals(TwiceDifferentiableTestFunctions.RIDDER_MINIMUM_POINT, solution.getPoint(), 1e-6);
    }

    /**
     * Test optimization with provided convergence checker and BrentSolver.
     */
    @Test
    public void testOptimizeCustomCheckerBrentSolver() {
        UnivariateFunction function = TwiceDifferentiableTestFunctions.POWER_FUNCTION;
        ConvergenceChecker<PointValuePair> checker = new ConvergenceChecker<PointValuePair>() {
            @Override
            public boolean converged(int iteration, PointValuePair start, PointValuePair end) {
                if (iteration > 100) {
                    return true;
                }
                return false;
            }
        };
        ConjugateGradientOptimizer optimizer = new ConjugateGradientOptimizer(
                ConjugateGradientFormula.FLETCHER_REEVES,
                checker,
                new BrentSolver());

        PointValuePair solution = optimizer.optimize(function, 1.0, null);
        assertNotNull(solution);
        assertEquals(TwiceDifferentiableTestFunctions.POWER_FUNCTION_MINIMUM, solution.getValue(), 1e-6);
        assertArrayEquals(TwiceDifferentiableTestFunctions.POWER_FUNCTION_MINIMUM_POINT, solution.getPoint(), 1e-6);
    }

    /**
     * Test optimization with provided convergence checker and provided solver.
     */
    @Test
    public void testOptimizeCustomCheckerCustomSolver() {
        UnivariateFunction function = TwiceDifferentiableTestFunctions.SINE;
        ConvergenceChecker<PointValuePair> checker = new ConvergenceChecker<PointValuePair>() {
            @Override
            public boolean converged(int iteration, PointValuePair start, PointValuePair end) {
                if (iteration > 100) {
                    return true;
                }
                return false;
            }
        };
        ConjugateGradientOptimizer optimizer = new ConjugateGradientOptimizer(
                ConjugateGradientFormula.POLAK_RIBIERE,
                checker,
                null);
        UnivariateSolver solver = new BrentSolver();
        optimizer.setSolver(solver);

        PointValuePair solution = optimizer.optimize(function, 0.0, null);
        assertNotNull(solution);
        assertEquals(TwiceDifferentiableTestFunctions.SINE_MINIMUM, solution.getValue(), 1e-6);
        assertArrayEquals(TwiceDifferentiableTestFunctions.SINE_MINIMUM_POINT, solution.getPoint(), 1e-6);
    }

    /**
     * Test optimization with provided preconditioner and BrentSolver.
     */
    @Test
    public void testOptimizeCustomPreconditionerBrentSolver() {
        UnivariateFunction function = TwiceDifferentiableTestFunctions.SINE;
        Preconditioner preconditioner = new Preconditioner() {
            @Override
            public double[] precondition(double[] variables, double[] r) {
                double[] result = new double[variables.length];
                for (int i = 0; i < variables.length; i++) {
                    result[i] = r[i] / 10.0;
                }
                return result;
            }
        };
        ConjugateGradientOptimizer optimizer = new ConjugateGradientOptimizer(
                ConjugateGradientFormula.POLAK_RIBIERE,
                new SimpleValueChecker(),
                new BrentSolver());
        optimizer.setPreconditioner(preconditioner);

        PointValuePair solution = optimizer.optimize(function, 0.0, null);
        assertNotNull(solution);
        assertEquals(TwiceDifferentiableTestFunctions.SINE_MINIMUM, solution.getValue(), 1e-6);
        assertArrayEquals(TwiceDifferentiableTestFunctions.SINE_MINIMUM_POINT, solution.getPoint(), 1e-6);
    }

    /**
     * Test optimization with provided preconditioner and provided solver.
     */
    @Test
    public void testOptimizeCustomPreconditionerCustomSolver() {
        UnivariateFunction function = TwiceDifferentiableTestFunctions.SINE;
        Preconditioner preconditioner = new Preconditioner() {
            @Override
            public double[] precondition(double[] variables, double[] r) {
                double[] result = new double[variables.length];
                for (int i = 0; i < variables.length; i++) {
                    result[i] = r[i] / 10.0;
                }
                return result;
            }
        };
        ConjugateGradientOptimizer optimizer = new ConjugateGradientOptimizer(
                ConjugateGradientFormula.POLAK_RIBIERE,
                new SimpleValueChecker(),
                null);
        optimizer.setPreconditioner(preconditioner);
        UnivariateSolver solver = new BrentSolver();
        optimizer.setSolver(solver);

        PointValuePair solution = optimizer.optimize(function, 0.0, null);
        assertNotNull(solution);
        assertEquals(TwiceDifferentiableTestFunctions.SINE_MINIMUM, solution.getValue(), 1e-6);
        assertArrayEquals(TwiceDifferentiableTestFunctions.SINE_MINIMUM_POINT, solution.getPoint(), 1e-6);
    }

    /**
     * Test optimization with provided initial step.
     */
    @Test
    public void testOptimizeInitialStep() {
        UnivariateFunction function = TwiceDifferentiableTestFunctions.PARABOLIC;
        ConjugateGradientOptimizer optimizer = new ConjugateGradientOptimizer(
                ConjugateGradientFormula.POLAK_RIBIERE,
                new SimpleValueChecker(),
                new BrentSolver());
        optimizer.setInitialStep(0.1);

        PointValuePair solution = optimizer.optimize(function, 1.0, null);
        assertNotNull(solution);
        assertEquals(TwiceDifferentiableTestFunctions.PARABOLIC_MINIMUM, solution.getValue(), 1e-6);
        assertArrayEquals(TwiceDifferentiableTestFunctions.PARABOLIC_MINIMUM_POINT, solution.getPoint(), 1e-6);
    }

    /**
     * Test optimization with provided convergence checker and provided solver
     * and provided initial step.
     */
    @Test
    public void testOptimizeCustomCheckerCustomSolverInitialStep() {
        UnivariateFunction function = TwiceDifferentiableTestFunctions.RIDDER;
        ConvergenceChecker<PointValuePair> checker = new ConvergenceChecker<PointValuePair>() {
            @Override
            public boolean converged(int iteration, PointValuePair start, PointValuePair end) {
                if (iteration > 100) {
                    return true;
                }
                return false;
            }
        };
        ConjugateGradientOptimizer optimizer = new ConjugateGradientOptimizer(
                ConjugateGradientFormula.FLETCHER_REEVES,
                checker,
                null);
        optimizer.setInitialStep(0.1);
        UnivariateSolver solver = new BrentSolver();
        optimizer.setSolver(solver);

        PointValuePair solution = optimizer.optimize(function, 0.0, null);
        assertNotNull(solution);
        assertEquals(TwiceDifferentiableTestFunctions.RIDDER_MINIMUM, solution.getValue(), 1e-6);
        assertArrayEquals(TwiceDifferentiableTestFunctions.RIDDER_MINIMUM_POINT, solution.getPoint(), 1e-6);
    }
}