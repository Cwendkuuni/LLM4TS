import org.apache.commons.math.analysis.solvers.BaseSecantSolver;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class BaseSecantSolverTest {

    private BaseSecantSolver solver;

    @Before
    public void setUp() {
        solver = new BaseSecantSolver(BaseSecantSolver.DEFAULT_ABSOLUTE_ACCURACY, BaseSecantSolver.Method.REGULA_FALSI);
    }

    @Test
    public void testSolve() {
        // Test case 1
        UnivariateRealFunction f1 = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        };
        double solution1 = solver.solve(100, f1, 1, 3);
        assertEquals(2.0, solution1, 0.00001);

        // Test case 2
        UnivariateRealFunction f2 = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x * x - 10;
            }
        };
        double solution2 = solver.solve(100, f2, 1, 3);
        assertEquals(2.1544346, solution2, 0.00001);

        // Test case 3
        UnivariateRealFunction f3 = new UnivariateRealFunction() {
            public double value(double x) {
                return Math.sin(x) - x;
            }
        };
        double solution3 = solver.solve(100, f3, 0, 2);
        assertEquals(1.8954943, solution3, 0.00001);
    }

    @Test
    public void testSolveWithAllowedSolution() {
        // Test case 1
        UnivariateRealFunction f1 = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        };
        double solution1 = solver.solve(100, f1, 1, 3, BaseSecantSolver.AllowedSolution.ANY_SIDE);
        assertEquals(2.0, solution1, 0.00001);

        // Test case 2
        UnivariateRealFunction f2 = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x * x - 10;
            }
        };
        double solution2 = solver.solve(100, f2, 1, 3, BaseSecantSolver.AllowedSolution.ANY_SIDE);
        assertEquals(2.1544346, solution2, 0.00001);

        // Test case 3
        UnivariateRealFunction f3 = new UnivariateRealFunction() {
            public double value(double x) {
                return Math.sin(x) - x;
            }
        };
        double solution3 = solver.solve(100, f3, 0, 2, BaseSecantSolver.AllowedSolution.ANY_SIDE);
        assertEquals(1.8954943, solution3, 0.00001);
    }

}