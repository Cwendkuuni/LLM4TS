import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class BaseSecantSolverTest {

    @Test
    public void testSolve_Method_With_Default_Accuracy() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);
        // Test typical use case
        double result = solver.solve(100, new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x - 5;
            }
        }, 0, 10, AllowedSolution.ANY_SIDE);
        assertEquals(5.0, result, 1e-6);

        // Test edge case
        result = solver.solve(100, new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 4;
            }
        }, -10, 10, AllowedSolution.ANY_SIDE);
        assertEquals(2.0, result, 1e-6);

        // Test error scenario
        result = solver.solve(100, new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x + 5;
            }
        }, 0, 10, AllowedSolution.ANY_SIDE);
        assertTrue(Double.isNaN(result));
    }

    @Test
    public void testSolve_Method_With_Custom_Accuracy() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.ILLINOIS);
        // Test typical use case
        double result = solver.solve(100, new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x - 5;
            }
        }, 0, 10, AllowedSolution.ANY_SIDE);
        assertEquals(5.0, result, 1e-6);

        // Test edge case
        result = solver.solve(100, new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 4;
            }
        }, -10, 10, AllowedSolution.ANY_SIDE);
        assertEquals(2.0, result, 1e-6);

        // Test error scenario
        result = solver.solve(100, new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x + 5;
            }
        }, 0, 10, AllowedSolution.ANY_SIDE);
        assertTrue(Double.isNaN(result));
    }

    @Test
    public void testSolve_Method_With_Max_Eval() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.PEGASUS);
        // Test typical use case
        double result = solver.solve(10, new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x - 5;
            }
        }, 0, 10, AllowedSolution.ANY_SIDE);
        assertEquals(5.0, result, 1e-6);

        // Test edge case
        result = solver.solve(10, new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 4;
            }
        }, -10, 10, AllowedSolution.ANY_SIDE);
        assertEquals(2.0, result, 1e-6);

        // Test error scenario
        result = solver.solve(10, new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x + 5;
            }
        }, 0, 10, AllowedSolution.ANY_SIDE);
        assertTrue(Double.isNaN(result));
    }
}