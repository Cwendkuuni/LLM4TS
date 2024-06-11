import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class BaseSecantSolverTest {

    @Test
    public void testSolve() {
        BaseSecantSolver solver = new BaseSecantSolverImpl(1e-6, BaseSecantSolver.Method.REGULA_FALSI);

        // Test case 1
        double result1 = solver.solve(100, new UnivariateRealFunction() {
            public double value(double x) {
                return x*x - 4;
            }
        }, 1, 10);
        assertEquals(2.0, result1, 1e-6);

        // Test case 2
        double result2 = solver.solve(100, new UnivariateRealFunction() {
            public double value(double x) {
                return x - 3;
            }
        }, 1, 10);
        assertEquals(3.0, result2, 1e-6);
    }

    private class BaseSecantSolverImpl extends BaseSecantSolver {
        public BaseSecantSolverImpl(double absoluteAccuracy, Method method) {
            super(absoluteAccuracy, method);
        }

        protected double computeObjectiveValue(double x) {
            return 0.0;
        }
    }
}