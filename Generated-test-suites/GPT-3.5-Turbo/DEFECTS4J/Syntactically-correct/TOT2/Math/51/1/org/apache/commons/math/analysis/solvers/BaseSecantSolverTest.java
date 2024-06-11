import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class BaseSecantSolverTest {

    @Test
    public void testSolveWithMaxEval() {
        BaseSecantSolver solver = new DummySecantSolver(0.0001, BaseSecantSolver.Method.REGULA_FALSI);
        UnivariateRealFunction f = new DummyFunction();
        double result = solver.solve(100, f, 1, 2, BaseSecantSolver.AllowedSolution.ANY_SIDE);
        assertEquals(1.5, result, 0.0001);
    }

    @Test
    public void testSolveWithStartValue() {
        BaseSecantSolver solver = new DummySecantSolver(0.0001, BaseSecantSolver.Method.ILLINOIS);
        UnivariateRealFunction f = new DummyFunction();
        double result = solver.solve(100, f, 1, 2, 1.5, BaseSecantSolver.AllowedSolution.ANY_SIDE);
        assertEquals(1.5, result, 0.0001);
    }

    @Test
    public void testSolveWithAllowedSolution() {
        BaseSecantSolver solver = new DummySecantSolver(0.0001, BaseSecantSolver.Method.PEGASUS);
        UnivariateRealFunction f = new DummyFunction();
        double result = solver.solve(100, f, 1, 2, 1.5, BaseSecantSolver.AllowedSolution.LEFT_SIDE);
        assertTrue(result < 1.5);
    }

    @Test
    public void testSolveExactRoot() {
        BaseSecantSolver solver = new DummySecantSolver(0.0001, BaseSecantSolver.Method.REGULA_FALSI);
        UnivariateRealFunction f = new DummyExactRootFunction();
        double result = solver.solve(100, f, 1, 2, BaseSecantSolver.AllowedSolution.ANY_SIDE);
        assertEquals(1.0, result, 0.0001);
    }

    @Test
    public void testSolveSmallFunctionValue() {
        BaseSecantSolver solver = new DummySecantSolver(0.0001, BaseSecantSolver.Method.ILLINOIS);
        UnivariateRealFunction f = new DummySmallFunctionValueFunction();
        double result = solver.solve(100, f, 1, 2, BaseSecantSolver.AllowedSolution.ANY_SIDE);
        assertEquals(1.5, result, 0.0001);
    }

    // Add more test cases for edge cases and exception handling scenarios

}