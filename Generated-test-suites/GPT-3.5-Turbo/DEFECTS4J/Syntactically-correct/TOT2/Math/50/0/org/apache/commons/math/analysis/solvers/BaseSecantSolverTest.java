import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.analysis.solvers.*;

public class BaseSecantSolverTest {

    @Test
    public void testSolve_Default() {
        BaseSecantSolver solver = new SecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x + 1;
            }
        };
        double root = solver.solve(100, f, -10, 10);
        assertEquals(-1.0, root, 1e-6);
    }

    @Test
    public void testSolve_MaxEvaluations() {
        BaseSecantSolver solver = new SecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 16;
            }
        };
        double root = solver.solve(5, f, -10, 10);
        assertTrue(Double.isNaN(root));
    }

    @Test
    public void testSolve_InvalidBounds() {
        BaseSecantSolver solver = new SecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 16;
            }
        };
        double root = solver.solve(100, f, 10, -10);
        assertTrue(Double.isNaN(root));
    }

    @Test
    public void testSolve_OnRoot() {
        BaseSecantSolver solver = new SecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return FastMath.sin(x);
            }
        };
        double root = solver.solve(100, f, -1, 1);
        assertEquals(0.0, root, 1e-6);
    }

    @Test(expected = MathInternalError.class)
    public void testSolve_InternalError() {
        BaseSecantSolver solver = new SecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI) {
            protected double computeObjectiveValue(double x) {
                throw new MathInternalError();
            }
        };
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 16;
            }
        };
        double root = solver.solve(100, f, -10, 10);
    }

}