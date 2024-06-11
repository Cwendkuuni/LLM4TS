
import org.apache.commons.math.analysis.solvers.*;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class BaseSecantSolverTest {

    private static final double EPSILON = 1e-6;

    private BaseSecantSolver solver;

    @Before
    public void setUp() {
        solver = new BaseSecantSolverStub();
    }

    @Test
    public void testSolve() {
        TestFunction function = new TestFunction();
        double result = solver.solve(100, function, 0, 10);
        assertEquals(3.0, result, EPSILON);
    }

    @Test
    public void testSolveWithStartValue() {
        TestFunction function = new TestFunction();
        double result = solver.solve(100, function, 0, 10, 5);
        assertEquals(3.0, result, EPSILON);
    }

    @Test
    public void testSolveWithAllowedSolution() {
        TestFunction function = new TestFunction();
        double result = solver.solve(100, function, 0, 10, BaseSecantSolver.AllowedSolution.LEFT_SIDE);
        assertEquals(2.0, result, EPSILON);
    }

    @Test(expected = RuntimeException.class)
    public void testSolveWithMaxEvalExceeded() {
        TestFunction function = new TestFunction();
        solver.solve(10, function, 0, 10);
    }

    private static class BaseSecantSolverStub extends BaseSecantSolver {
        BaseSecantSolverStub() {
            super(DEFAULT_ABSOLUTE_ACCURACY, Method.REGULA_FALSI);
        }

        @Override
        protected double computeObjectiveValue(double x) {
            // Stub implementation for testing purposes
            return x - 3.0;
        }

        @Override
        protected void verifyBracketing(double x0, double x1) {
            // Stub implementation for testing purposes
        }
    }

    private static class TestFunction implements UnivariateRealFunction {
        @Override
        public double value(double x) {
            // Stub implementation for testing purposes
            return x - 3.0;
        }
    }

}
