import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.math.analysis.UnivariateRealFunction;

public class BaseSecantSolverTest {

    @Test
    public void testSolve_WithValidInput_ReturnsRoot() {
        BaseSecantSolver solver = new ConcreteSecantSolver();
        UnivariateRealFunction f = new TestFunction();
        double root = solver.solve(100, f, 1.0, 2.0, 1.5);
        assertEquals(1.0, root, 1e-6);
    }

    @Test
    public void testSolve_WithInvalidInterval_ThrowsConvergenceException() {
        BaseSecantSolver solver = new ConcreteSecantSolver();
        UnivariateRealFunction f = new TestFunction();
        try {
            double root = solver.solve(100, f, 2.0, 1.0, 1.5);
            fail("Expected ConvergenceException");
        } catch (ConvergenceException e) {
            // Expected exception
        }
    }

    @Test
    public void testSolve_WithExactRoot_ReturnsRoot() {
        BaseSecantSolver solver = new ConcreteSecantSolver();
        UnivariateRealFunction f = new TestFunctionExactRoot();
        double root = solver.solve(100, f, 0.0, 10.0, 5.0);
        assertEquals(5.0, root, 1e-6);
    }

    @Test
    public void testSolve_WithFunctionValueTooSmall_ReturnsApproximation() {
        BaseSecantSolver solver = new ConcreteSecantSolver();
        UnivariateRealFunction f = new TestFunctionValueTooSmall();
        double root = solver.solve(100, f, 1.0, 2.0, 1.5);
        assertEquals(1.7, root, 1e-6);
    }

}

class ConcreteSecantSolver extends BaseSecantSolver {

    protected ConcreteSecantSolver() {
        super(1e-6, Method.REGULA_FALSI);
    }

    @Override
    protected double computeObjectiveValue(double x) {
        // Evaluate test function at x
        return 2 * x - 3;
    }

}

class TestFunction implements UnivariateRealFunction {

    @Override
    public double value(double x) {
        return x - 1;
    }

}

class TestFunctionExactRoot implements UnivariateRealFunction {

    @Override
    public double value(double x) {
        return x - 5;
    }

}

class TestFunctionValueTooSmall implements UnivariateRealFunction {

    @Override
    public double value(double x) {
        return 1e-7;
    }

}