
import static org.junit.Assert.*;
import org.junit.Test;

public class BaseSecantSolverTest {

    @Test
    public void testSolve() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);
        double x = solver.solve(100, new UnivariateRealFunction() {
            public double value(double x) {
                return x - 2;
            }
        }, 0, 4);
        assertEquals(2.0, x, 0.000001);
    }

    @Test
    public void testSolve_MaximumIterationsExceeded() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);
        double x = solver.solve(10, new UnivariateRealFunction() {
            public double value(double x) {
                return x - 2;
            }
        }, 0, 4);
        assertEquals(Double.NaN, x, 0.000001);
    }

    @Test
    public void testSolve_BracketingIntervalInvalid() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);
        double x = solver.solve(100, new UnivariateRealFunction() {
            public double value(double x) {
                return x - 2;
            }
        }, 4, 0);
        assertEquals(Double.NaN, x, 0.000001);
    }

    @Test
    public void testSolve_SolutionOutsideBracketingInterval() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);
        double x = solver.solve(100, new UnivariateRealFunction() {
            public double value(double x) {
                return x - 3;
            }
        }, 0, 2);
        assertEquals(3.0, x, 0.000001);
    }

    @Test
    public void testSolve_FunctionValueAccuracyReached() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);
        double x = solver.solve(100, new UnivariateRealFunction() {
            public double value(double x) {
                return x - 2;
            }
        }, 0, 4, BaseSecantSolver.AllowedSolution.ABOVE_SIDE);
        assertEquals(Double.NaN, x, 0.000001);
    }

    @Test
    public void testSolve_AbsoluteAccuracyReached() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);
        double x = solver.solve(100, new UnivariateRealFunction() {
            public double value(double x) {
                return x - 2;
            }
        }, 0, 4, BaseSecantSolver.AllowedSolution.ANY_SIDE);
        assertEquals(Double.NaN, x, 0.000001);
    }

    @Test
    public void testSolve_RelativeAccuracyReached() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);
        double x = solver.solve(100, new UnivariateRealFunction() {
            public double value(double x) {
                return 100 * x - 200;
            }
        }, 0, 4, BaseSecantSolver.AllowedSolution.ANY_SIDE);
        assertEquals(2.0, x, 0.000001);
    }

}
