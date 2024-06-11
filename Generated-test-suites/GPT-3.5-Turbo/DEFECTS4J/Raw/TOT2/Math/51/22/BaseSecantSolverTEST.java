import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class BaseSecantSolverTest {

    @Test
    public void testSolve() {
        BaseSecantSolver solver = new BaseSecantSolver(DEFAULT_ABSOLUTE_ACCURACY, BaseSecantSolver.Method.REGULA_FALSI);

        double result = solver.solve(100, new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x - 5;
            }
        }, 0, 10);

        assertEquals(5.0, result, 1e-6);
    }

    @Test
    public void testSolveWithMaxEval() {
        BaseSecantSolver solver = new BaseSecantSolver(DEFAULT_ABSOLUTE_ACCURACY, BaseSecantSolver.Method.ILLINOIS);

        double result = solver.solve(100, new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 4;
            }
        }, 0, 3);

        assertEquals(2.0, result, 1e-6);
    }

    // Test edge cases and exceptions
    @Test(expected = MathInternalError.class)
    public void testSolveWithInvalidMethod() {
        BaseSecantSolver solver = new BaseSecantSolver(DEFAULT_ABSOLUTE_ACCURACY, BaseSecantSolver.Method.PEGASUS);

        solver.solve(100, new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x;
            }
        }, 0, 10);
    }

}