import org.junit.Test;
import static org.junit.Assert.*;

public class BaseSecantSolverTest {

    // Create an instance of BaseSecantSolver
    private BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);

    @Test
    public void testSolve() {
        // Test case 1: Function value at min is 0
        UnivariateRealFunction f1 = new UnivariateRealFunction() {
            public double value(double x) {
                return x;
            }
        };
        double min1 = 0;
        double max1 = 100;
        double result1 = solver.solve(100, f1, min1, max1, BaseSecantSolver.AllowedSolution.ANY_SIDE);
        assertEquals(min1, result1, 1e-6);

        // Test case 2: Function value at max is 0
        UnivariateRealFunction f2 = new UnivariateRealFunction() {
            public double value(double x) {
                return x - max1;
            }
        };
        double result2 = solver.solve(100, f2, min1, max1, BaseSecantSolver.AllowedSolution.ANY_SIDE);
        assertEquals(max1, result2, 1e-6);

        // Test case 3: Function value at startValue is 0
        double startValue3 = (min1 + max1) / 2;
        UnivariateRealFunction f3 = new UnivariateRealFunction() {
            public double value(double x) {
                return x - startValue3;
            }
        };
        double result3 = solver.solve(100, f3, min1, max1, startValue3, BaseSecantSolver.AllowedSolution.ANY_SIDE);
        assertEquals(startValue3, result3, 1e-6);
    }

    @Test
    public void testDoSolve() {
        // Test case 1: Function value at min is 0
        UnivariateRealFunction f1 = new UnivariateRealFunction() {
            public double value(double x) {
                return x;
            }
        };
        double min1 = 0;
        double max1 = 100;
        double result1 = solver.solve(100, f1, min1, max1, BaseSecantSolver.AllowedSolution.ANY_SIDE);
        assertEquals(min1, result1, 1e-6);

        // Test case 2: Function value at max is 0
        UnivariateRealFunction f2 = new UnivariateRealFunction() {
            public double value(double x) {
                return x - max1;
            }
        };
        double result2 = solver.solve(100, f2, min1, max1, BaseSecantSolver.AllowedSolution.ANY_SIDE);
        assertEquals(max1, result2, 1e-6);

        // Test case 3: Function value at startValue is 0
        double startValue3 = (min1 + max1) / 2;
        UnivariateRealFunction f3 = new UnivariateRealFunction() {
            public double value(double x) {
                return x - startValue3;
            }
        };
        double result3 = solver.solve(100, f3, min1, max1, startValue3, BaseSecantSolver.AllowedSolution.ANY_SIDE);
        assertEquals(startValue3, result3, 1e-6);
    }

    @Test
    public void testDoSolveAllowedSide() {
        // Test case 1: Solution is on the left side
        UnivariateRealFunction f1 = new UnivariateRealFunction() {
            public double value(double x) {
                return x - 10;
            }
        };
        double min1 = 0;
        double max1 = 100;
        double result1 = solver.solve(100, f1, min1, max1, BaseSecantSolver.AllowedSolution.LEFT_SIDE);
        assertTrue(result1 < 10);

        // Test case 2: Solution is on the right side
        UnivariateRealFunction f2 = new UnivariateRealFunction() {
            public double value(double x) {
                return x - 90;
            }
        };
        double result2 = solver.solve(100, f2, min1, max1, BaseSecantSolver.AllowedSolution.RIGHT_SIDE);
        assertTrue(result2 > 90);

        // Test case 3: Solution is below
        double startValue3 = 50;
        UnivariateRealFunction f3 = new UnivariateRealFunction() {
            public double value(double x) {
                return x - startValue3;
            }
        };
        double result3 = solver.solve(100, f3, min1, max1, startValue3, BaseSecantSolver.AllowedSolution.BELOW_SIDE);
        assertTrue(result3 < startValue3);

        // Test case 4: Solution is above
        UnivariateRealFunction f4 = new UnivariateRealFunction() {
            public double value(double x) {
                return x - startValue3;
            }
        };
        double result4 = solver.solve(100, f4, min1, max1, startValue3, BaseSecantSolver.AllowedSolution.ABOVE_SIDE);
        assertTrue(result4 > startValue3);
    }
}