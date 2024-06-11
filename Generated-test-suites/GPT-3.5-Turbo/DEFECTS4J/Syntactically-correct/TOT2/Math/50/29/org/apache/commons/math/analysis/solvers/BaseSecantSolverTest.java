import static org.junit.Assert.*;
import org.junit.Test;

public class BaseSecantSolverTest {

    @Test
    public void testSolve_ExactRoot() {
        BaseSecantSolver solver = new TestSecantSolver();
        double root = solver.solve(100, new TestFunction(), 0.0, 3.0, BaseSecantSolver.AllowedSolution.ANY_SIDE);
        assertEquals(2.0, root, 1e-6);
    }

    @Test
    public void testSolve_MaxEvaluationsReached() {
        BaseSecantSolver solver = new TestSecantSolver();
        double root = solver.solve(1, new TestFunction(), 0.0, 3.0, BaseSecantSolver.AllowedSolution.ANY_SIDE);
        assertTrue(Double.isNaN(root));
    }

    @Test
    public void testSolve_FunctionHasNoRootInInterval() {
        BaseSecantSolver solver = new TestSecantSolver();
        double root = solver.solve(100, new TestFunction(), -1.0, 1.0, BaseSecantSolver.AllowedSolution.ANY_SIDE);
        assertTrue(Double.isNaN(root));
    }

    @Test
    public void testSolve_WithAllowedSolution() {
        BaseSecantSolver solver = new TestSecantSolver();
        double root = solver.solve(100, new TestFunction(), -5.0, 5.0, 0.0, BaseSecantSolver.AllowedSolution.ABOVE_SIDE);
        assertTrue(root > 0.0);
    }

    @Test
    public void testDoSolve_FunctionValueTooSmall() {
        BaseSecantSolver solver = new TestSecantSolver();
        double root = solver.doSolve();
        // Assumes function value accuracy is set to 1e-6
        assertTrue(Double.isNaN(root));
    }

    @Test
    public void testDoSolve_AccuracyReached() {
        BaseSecantSolver solver = new TestSecantSolver();
        solver.setAbsoluteAccuracy(0.5);
        solver.setMaximumFunctionCalls(100);
        double root = solver.doSolve();
        // Assumes relative accuracy is not reached but absolute accuracy is
        assertEquals(2.0, root, 0.5);
    }

    @Test
    public void testDoSolve_AccuracyNotReached() {
        BaseSecantSolver solver = new TestSecantSolver();
        solver.setAbsoluteAccuracy(0.0001);
        solver.setRelativeAccuracy(0.0001);
        double root = solver.doSolve();
        // Assumes maximum function calls are reached without accuracy
        assertTrue(Double.isNaN(root));
    }
}

class TestFunction implements UnivariateRealFunction {
    public double value(double x) {
        return x - 2.0;
    }
}

class TestSecantSolver extends BaseSecantSolver {
    public TestSecantSolver() {
        super(1e-6, Method.REGULA_FALSI);
    }

    @Override
    protected double computeObjectiveValue(double x) {
        return x - 2.0;
    }
}