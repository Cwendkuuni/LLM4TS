import org.junit.Test;
import static org.junit.Assert.*;

public class BaseSecantSolverTest {

    // Test objects
    private BaseSecantSolver solver;

    @Test
    public void testSolve() {
        // Test case 1
        solver = new BaseSecantSolver(0.000001, BaseSecantSolver.Method.REGULA_FALSI);
        double result1 = solver.solve(100, new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        }, 1, 3, BaseSecantSolver.AllowedSolution.ANY_SIDE);
        assertEquals(2.0, result1, 0.000001);

        // Test case 2
        solver = new BaseSecantSolver(0.000001, BaseSecantSolver.Method.ILLINOIS);
        double result2 = solver.solve(100, new UnivariateRealFunction() {
            public double value(double x) {
                return x * x * x - 3 * x - 1;
            }
        }, -10, 10, BaseSecantSolver.AllowedSolution.ANY_SIDE);
        assertEquals(1.0, result2, 0.000001);

        // Test case 3
        solver = new BaseSecantSolver(0.000001, BaseSecantSolver.Method.PEGASUS);
        double result3 = solver.solve(100, new UnivariateRealFunction() {
            public double value(double x) {
                return Math.exp(x) - 3 * x * x;
            }
        }, -10, 10, BaseSecantSolver.AllowedSolution.ANY_SIDE);
        assertEquals(0.619061, result3, 0.000001);
    }

    @Test
    public void testSolveWithMaxEval() {
        solver = new BaseSecantSolver(0.000001, BaseSecantSolver.Method.REGULA_FALSI);
        double result = solver.solve(10, new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        }, 1, 3, BaseSecantSolver.AllowedSolution.ANY_SIDE);
        assertEquals(result, Double.NaN, 0.000001);
    }

    @Test
    public void testSolveWithBracketing() {
        solver = new BaseSecantSolver(0.000001, BaseSecantSolver.Method.REGULA_FALSI);
        double result = solver.solve(100, new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        }, -10, -1, BaseSecantSolver.AllowedSolution.ANY_SIDE);
        assertEquals(result, Double.NaN, 0.000001);
    }

    @Test
    public void testSolveWithAllowedSolution() {
        solver = new BaseSecantSolver(0.000001, BaseSecantSolver.Method.REGULA_FALSI);
        double result1 = solver.solve(100, new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        }, 1, 3, BaseSecantSolver.AllowedSolution.LEFT_SIDE);
        assertEquals(1.999999, result1, 0.000001);

        double result2 = solver.solve(100, new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        }, 1, 3, BaseSecantSolver.AllowedSolution.RIGHT_SIDE);
        assertEquals(2.000001, result2, 0.000001);

        double result3 = solver.solve(100, new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        }, 1, 3, BaseSecantSolver.AllowedSolution.BELOW_SIDE);
        assertEquals(2.0, result3, 0.000001);

        double result4 = solver.solve(100, new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        }, 1, 3, BaseSecantSolver.AllowedSolution.ABOVE_SIDE);
        assertEquals(2.0, result4, 0.000001);
    }

    @Test
    public void testSolveWithRelativeAccuracy() {
        solver = new BaseSecantSolver(0.000001, BaseSecantSolver.Method.REGULA_FALSI);
        solver.setRelativeAccuracy(0.0001);
        double result = solver.solve(100, new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        }, 1, 3, BaseSecantSolver.AllowedSolution.ANY_SIDE);
        assertEquals(2.0, result, 0.0001);
    }

    @Test
    public void testSolveWithFunctionValueAccuracy() {
        solver = new BaseSecantSolver(0.000001, BaseSecantSolver.Method.REGULA_FALSI);
        solver.setFunctionValueAccuracy(0.0001);
        double result = solver.solve(100, new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        }, 1, 3, BaseSecantSolver.AllowedSolution.ANY_SIDE);
        assertEquals(2.0, result, 0.0001);
    }
}