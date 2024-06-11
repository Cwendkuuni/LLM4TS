import org.junit.Test;
import static org.junit.Assert.*;

public class BaseSecantSolverTest {

    @Test
    public void testSolve_DefaultConstructor() {
        BaseSecantSolver solver = new ConcreteSecantSolver();
        assertEquals(0.0, solver.solve(100, f, 1.0, 2.0), 1e-6);
    }

    @Test
    public void testSolve_CustomConstructor() {
        BaseSecantSolver solver = new ConcreteSecantSolver(1e-8, BaseSecantSolver.Method.REGULA_FALSI);
        assertEquals(0.0, solver.solve(100, f, 1.0, 2.0), 1e-8);
    }

    @Test
    public void testSolve_AnySide() {
        BaseSecantSolver solver = new ConcreteSecantSolver(1e-6, BaseSecantSolver.Method.ILLINOIS);
        assertEquals(0.0, solver.solve(100, f, -1.0, 1.0, BaseSecantSolver.AllowedSolution.ANY_SIDE), 1e-6);
    }

    @Test
    public void testSolve_LeftSide() {
        BaseSecantSolver solver = new ConcreteSecantSolver(1e-6, BaseSecantSolver.Method.PEGASUS);
        assertEquals(0.0, solver.solve(100, f, -1.0, 1.0, BaseSecantSolver.AllowedSolution.LEFT_SIDE), 1e-6);
    }

    @Test
    public void testSolve_RightSide() {
        BaseSecantSolver solver = new ConcreteSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);
        assertEquals(0.0, solver.solve(100, f, -1.0, 1.0, BaseSecantSolver.AllowedSolution.RIGHT_SIDE), 1e-6);
    }

    @Test
    public void testSolve_BelowSide() {
        BaseSecantSolver solver = new ConcreteSecantSolver(1e-6, BaseSecantSolver.Method.ILLINOIS);
        assertEquals(0.0, solver.solve(100, f, -1.0, 1.0, BaseSecantSolver.AllowedSolution.BELOW_SIDE), 1e-6);
    }

    @Test
    public void testSolve_AboveSide() {
        BaseSecantSolver solver = new ConcreteSecantSolver(1e-6, BaseSecantSolver.Method.PEGASUS);
        assertEquals(0.0, solver.solve(100, f, -1.0, 1.0, BaseSecantSolver.AllowedSolution.ABOVE_SIDE), 1e-6);
    }

    private UnivariateRealFunction f = new UnivariateRealFunction() {
        public double value(double x) {
            return x * x - 4; // Function x^2 - 4 has roots at x = -2 and x = 2
        }
    };

    private static class ConcreteSecantSolver extends BaseSecantSolver {
        ConcreteSecantSolver(double absoluteAccuracy, Method method) {
            super(absoluteAccuracy, method);
        }

        @Override
        protected double computeObjectiveValue(double x) {
            return f.value(x);
        }
    }
}