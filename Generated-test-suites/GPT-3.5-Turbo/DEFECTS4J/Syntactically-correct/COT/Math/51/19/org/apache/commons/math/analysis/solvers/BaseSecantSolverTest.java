import org.junit.Before;
import org.junit.Test;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.exception.MathInternalError;
import org.apache.commons.math.analysis.solvers.BaseSecantSolver;
import org.apache.commons.math.util.FastMath;

import static org.junit.Assert.*;

public class BaseSecantSolverTest {

    private static final double EPSILON = 1e-9;

    private static class TestFunction implements UnivariateRealFunction {

        public double value(double x) {
            return x * x - 4;
        }
    }

    private BaseSecantSolver solver;
    private UnivariateRealFunction testFunction;

    @Before
    public void setUp() {
        solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);
        testFunction = new TestFunction();
    }

    @Test
    public void testSolve() {
        double result = solver.solve(100, testFunction, 0, 10);
        assertEquals(2.0, result, EPSILON);
    }

    @Test
    public void testSolveWithAllowedSolutionAnySide() {
        solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);
        double result = solver.solve(100, testFunction, 0, 2, BaseSecantSolver.AllowedSolution.ANY_SIDE);
        assertTrue(result >= 1.999999 && result <= 2.000001);
    }

    @Test
    public void testSolveWithAllowedSolutionLeftSide() {
        solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);
        double result = solver.solve(100, testFunction, 0, 2, BaseSecantSolver.AllowedSolution.LEFT_SIDE);
        assertTrue(result >= 1.999999 && result <= 2.000001);
    }

    @Test
    public void testSolveWithAllowedSolutionRightSide() {
        solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);
        double result = solver.solve(100, testFunction, 0, 2, BaseSecantSolver.AllowedSolution.RIGHT_SIDE);
        assertTrue(result >= 1.999999 && result <= 2.000001);
    }

    @Test
    public void testSolveWithAllowedSolutionBelowSide() {
        solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);
        double result = solver.solve(100, testFunction, 0, 2, BaseSecantSolver.AllowedSolution.BELOW_SIDE);
        assertTrue(result >= 1.999999 && result <= 2.000001);
    }

    @Test
    public void testSolveWithAllowedSolutionAboveSide() {
        solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);
        double result = solver.solve(100, testFunction, 0, 2, BaseSecantSolver.AllowedSolution.ABOVE_SIDE);
        assertTrue(result >= 1.999999 && result <= 2.000001);
    }

    @Test(expected = MathInternalError.class)
    public void testSolveWithUnknownAllowedSolution() {
        solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);
        solver.solve(100, testFunction, 0, 2, BaseSecantSolver.AllowedSolution.UNKNOWN);
    }

    @Test(expected = MathInternalError.class)
    public void testSolveWithInvalidBracketing() {
        solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);
        solver.solve(100, testFunction, 0, 0);
    }

    @Test
    public void testComputeObjectiveValue() {
        double result = solver.solve(100, testFunction, 0, 2, BaseSecantSolver.AllowedSolution.ANY_SIDE);
        double functionValue = solver.computeObjectiveValue(result);
        assertEquals(0, functionValue, EPSILON);
    }

    @Test
    public void testComputeObjectiveValueWhenFunctionValueIsCloseToZero() {
        double result = solver.solve(100, testFunction, 0, 10, BaseSecantSolver.AllowedSolution.ANY_SIDE);
        double functionValue = solver.computeObjectiveValue(result);
        assertTrue(FastMath.abs(functionValue) < 1e-6);
    }

}