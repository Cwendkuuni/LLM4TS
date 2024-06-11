import org.junit.*;
import static org.junit.Assert.*;

public class BaseSecantSolverTest {

    private BaseSecantSolver solver;

    @Before
    public void setUp() {
        solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);
    }

    @Test
    public void testSolve() {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            public double value(double x) {
                return x - 2;
            }
        };

        double solution = solver.solve(100, function, 0, 5, BaseSecantSolver.AllowedSolution.ANY_SIDE);
        assertEquals(2.0, solution, 1e-6);
    }

    @Test
    public void testSolveWithMaxEval() {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            public double value(double x) {
                return x - 2;
            }
        };

        double solution = solver.solve(10, function, 0, 5, BaseSecantSolver.AllowedSolution.ANY_SIDE);
        assertEquals(2.0, solution, 1e-6);
    }

    @Test(expected = ConvergenceException.class)
    public void testSolveWithConvergenceException() {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            public double value(double x) {
                return x;
            }
        };

        solver.solve(100, function, 0, 5, BaseSecantSolver.AllowedSolution.ANY_SIDE);
    }

}