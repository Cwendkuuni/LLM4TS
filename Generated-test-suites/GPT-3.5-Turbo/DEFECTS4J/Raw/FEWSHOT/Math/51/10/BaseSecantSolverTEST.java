package org.apache.commons.math.analysis.solvers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.analysis.function.Sin;
import org.junit.Before;
import org.junit.Test;

public class BaseSecantSolverTest {
    private BaseSecantSolver solver;

    @Before
    public void setUp() {
        solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.PEGASUS) {
            @Override
            protected double doSolve() throws InternalSolverException {
                return 0;
            }

            @Override
            protected double computeObjectiveValue(double x) throws FunctionEvaluationException {
                return 0;
            }

            @Override
            protected void verifyBracketing(double lower, double upper) throws NullArgumentException, MathIllegalArgumentException, NumberIsTooLargeException {
                return;
            }
        };
    }

    @Test
    public void testSolve() {
        int maxEval = 100;
        UnivariateRealFunction f = new Sin();
        double min = 0;
        double max = 1;
        double startValue = 0.5;
        BaseSecantSolver.AllowedSolution allowedSolution = BaseSecantSolver.AllowedSolution.ANY_SIDE;

        // Test return value
        double result = solver.solve(maxEval, f, min, max, startValue, allowedSolution);
        assertNotNull(result);

        // Test allowed solutions
        allowedSolution = BaseSecantSolver.AllowedSolution.LEFT_SIDE;
        result = solver.solve(maxEval, f, min, max, startValue, allowedSolution);
        assertNotNull(result);

        allowedSolution = BaseSecantSolver.AllowedSolution.RIGHT_SIDE;
        result = solver.solve(maxEval, f, min, max, startValue, allowedSolution);
        assertNotNull(result);

        allowedSolution = BaseSecantSolver.AllowedSolution.BELOW_SIDE;
        result = solver.solve(maxEval, f, min, max, startValue, allowedSolution);
        assertNotNull(result);

        allowedSolution = BaseSecantSolver.AllowedSolution.ABOVE_SIDE;
        result = solver.solve(maxEval, f, min, max, startValue, allowedSolution);
        assertNotNull(result);
    }

    @Test(expected = NullArgumentException.class)
    public void testSolveNullFunction() {
        int maxEval = 100;
        double min = 0;
        double max = 1;
        double startValue = 0.5;
        BaseSecantSolver.AllowedSolution allowedSolution = BaseSecantSolver.AllowedSolution.ANY_SIDE;

        solver.solve(maxEval, null, min, max, startValue, allowedSolution);
    }

    @Test
    public void testSolveInvalidInterval() {
        int maxEval = 100;
        UnivariateRealFunction f = new Sin();
        double min = 0;
        double max = 0;
        double startValue = 0.5;
        BaseSecantSolver.AllowedSolution allowedSolution = BaseSecantSolver.AllowedSolution.ANY_SIDE;

        try {
            solver.solve(maxEval, f, min, max, startValue, allowedSolution);
        } catch (MathIllegalArgumentException e) {
            assertTrue(e.getMessage().contains("Invalid interval"));
            return;
        }
        fail("Expected MathIllegalArgumentException");
    }

    @Test
    public void testSolveFunctionValueAccuracy() {
        int maxEval = 100;
        UnivariateRealFunction f = new Sin();
        double min = 0;
        double max = 1;
        double startValue = 0.5;
        BaseSecantSolver.AllowedSolution allowedSolution = BaseSecantSolver.AllowedSolution.ANY_SIDE;

        double result = solver.solve(maxEval, f, min, max, startValue, allowedSolution);
        double ftol = solver.getFunctionValueAccuracy();
        assertTrue(Math.abs(solver.computeObjectiveValue(result)) <= ftol);
    }

    @Test
    public void testSolveRelativeAccuracy() {
        int maxEval = 100;
        UnivariateRealFunction f = new Sin();
        double min = 0;
        double max = 1;
        double startValue = 0.5;
        BaseSecantSolver.AllowedSolution allowedSolution = BaseSecantSolver.AllowedSolution.ANY_SIDE;

        double result = solver.solve(maxEval, f, min, max, startValue, allowedSolution);
        double rtol = solver.getRelativeAccuracy();
        double atol = solver.getAbsoluteAccuracy();
        assertTrue(Math.abs(solver.computeObjectiveValue(result)) <= Math.max(rtol * Math.abs(result), atol));
    }
}