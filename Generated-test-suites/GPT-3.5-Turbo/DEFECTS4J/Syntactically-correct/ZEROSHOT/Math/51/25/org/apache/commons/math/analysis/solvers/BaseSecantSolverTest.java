import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class BaseSecantSolverTest {

    private BaseSecantSolver solver;

    @Before
    public void setUp() {
        solver = new TestSecantSolver();
    }

    @Test
    public void testSolve() {
        double root = solver.solve(100, new TestFunction(), 0, 10);
        assertEquals(0.0, root, 1e-6);
    }

    @Test
    public void testSolveWithMaxEval() {
        double root = solver.solve(100, new TestFunction(), 0, 10, AllowedSolution.ANY_SIDE);
        assertEquals(0.0, root, 1e-6);
    }

    @Test
    public void testSolveWithAllowedSolution() {
        double root = solver.solve(100, new TestFunction(), 0, 10, AllowedSolution.LEFT_SIDE);
        assertEquals(0.0, root, 1e-6);
    }

    @Test
    public void testSolveWithStartValue() {
        double root = solver.solve(100, new TestFunction(), 0, 10, 5);
        assertEquals(0.0, root, 1e-6);
    }

    @Test
    public void testSolveWithStartValueAndAllowedSolution() {
        double root = solver.solve(100, new TestFunction(), 0, 10, 5, AllowedSolution.RIGHT_SIDE);
        assertEquals(0.0, root, 1e-6);
    }

    private class TestFunction implements UnivariateRealFunction {

        public double value(double x) {
            return x;
        }
    }

    private class TestSecantSolver extends BaseSecantSolver {

        public TestSecantSolver() {
            super(1e-6, Method.REGULA_FALSI);
        }

        protected double computeObjectiveValue(double x) {
            return x;
        }
    }
}