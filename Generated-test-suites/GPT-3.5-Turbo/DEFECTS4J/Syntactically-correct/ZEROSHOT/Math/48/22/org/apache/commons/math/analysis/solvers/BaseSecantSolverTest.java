import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class BaseSecantSolverTest {

    private BaseSecantSolver solver;

    @Before
    public void setup() {
        solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);
    }

    @Test
    public void testSolve() {
        double result = solver.solve(100, new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        }, 0, 3);

        Assert.assertEquals(2.0, result, 1e-6);
    }

    @Test
    public void testSolveWithAllowedSolution() {
        double result = solver.solve(100, new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        }, 0, 3, BaseSecantSolver.AllowedSolution.ABOVE_SIDE);

        Assert.assertEquals(2.0, result, 1e-6);
    }

    @Test(expected = ConvergenceException.class)
    public void testSolveConvergenceException() {
        solver.solve(10, new UnivariateRealFunction() {
            public double value(double x) {
                return x;
            }
        }, 0, 3);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSolveIllegalArgumentException() {
        solver.solve(100, null, 0, 3);
    }
}