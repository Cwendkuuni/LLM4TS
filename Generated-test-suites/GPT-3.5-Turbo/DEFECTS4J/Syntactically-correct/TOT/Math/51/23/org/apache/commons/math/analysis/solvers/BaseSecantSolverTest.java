import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class BaseSecantSolverTest {

    private BaseSecantSolver solver;

    @Before
    public void setUp() {
        solver = new BaseSecantSolverTest.MockBaseSecantSolver();
    }

    @Test
    public void testSolve_AnySide() {
        double result = solver.solve(100, new BaseSecantSolverTest.TestFunction(), 0, 10,
                BaseSecantSolver.AllowedSolution.ANY_SIDE);
        assertEquals(5.0, result, 1e-6);
    }

    @Test
    public void testSolve_LeftSide() {
        double result = solver.solve(100, new BaseSecantSolverTest.TestFunction(), 0, 10,
                BaseSecantSolver.AllowedSolution.LEFT_SIDE);
        assertEquals(1.0, result, 1e-6);
    }

    @Test
    public void testSolve_RightSide() {
        double result = solver.solve(100, new BaseSecantSolverTest.TestFunction(), 0, 10,
                BaseSecantSolver.AllowedSolution.RIGHT_SIDE);
        assertEquals(9.0, result, 1e-6);
    }

    @Test
    public void testSolve_BelowSide() {
        double result = solver.solve(100, new BaseSecantSolverTest.TestFunction(), 0, 10,
                BaseSecantSolver.AllowedSolution.BELOW_SIDE);
        assertEquals(5.0, result, 1e-6);
    }

    @Test
    public void testSolve_AboveSide() {
        double result = solver.solve(100, new BaseSecantSolverTest.TestFunction(), 0, 10,
                BaseSecantSolver.AllowedSolution.ABOVE_SIDE);
        assertEquals(5.0, result, 1e-6);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSolve_InvalidInterval() {
        solver.solve(100, new BaseSecantSolverTest.TestFunction(), 10, 0,
                BaseSecantSolver.AllowedSolution.ANY_SIDE);
    }

    @Test
    public void testSolve_CustomFunction() {
        BaseSecantSolver solver = new BaseSecantSolverTest.MockBaseSecantSolver();
        double result = solver.solve(100, new BaseSecantSolverTest.TestFunction(),
                0, 10, BaseSecantSolver.AllowedSolution.ANY_SIDE);
        assertEquals(5.0, result, 1e-6);
    }

    private static class MockBaseSecantSolver extends BaseSecantSolver {

        public MockBaseSecantSolver() {
            super(1e-6, Method.REGULA_FALSI);
        }

        @Override
        protected double computeObjectiveValue(double point) {
            return point - 5;
        }
    }

    private static class TestFunction implements UnivariateRealFunction {

        public double value(double x) {
            return x - 5;
        }

    }
}