import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class BaseSecantSolverTest {

    @Test
    public void testDefaultConstructor() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);
        assertEquals(BaseSecantSolver.Method.REGULA_FALSI, solver.getMethod());
    }

    @Test
    public void testRelativeAccuracyConstructor() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-10, 1e-6, BaseSecantSolver.Method.ILLINOIS);
        assertEquals(1e-10, solver.getRelativeAccuracy(), 0.0);
    }

    @Test
    public void testAbsoluteAccuracyConstructor() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-10, 1e-6, 1e-12, BaseSecantSolver.Method.PEGASUS);
        assertEquals(1e-12, solver.getFunctionValueAccuracy(), 0.0);
    }

    @Test
    public void testSolve() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);
        double root = solver.solve(100, new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 4;
            }
        }, 1.0, 3.0, BaseSecantSolver.AllowedSolution.ANY_SIDE);
        assertEquals(2.0, root, 1e-6);
    }

}