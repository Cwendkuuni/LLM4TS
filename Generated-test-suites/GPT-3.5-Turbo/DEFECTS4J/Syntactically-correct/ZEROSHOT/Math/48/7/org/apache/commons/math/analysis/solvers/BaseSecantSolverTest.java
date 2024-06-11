import org.junit.Assert;
import org.junit.Test;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.exception.ConvergenceException;
import org.apache.commons.math.exception.MathInternalError;

public class BaseSecantSolverTest {

    public class TestFunction implements UnivariateRealFunction {

        public double value(double x) {
            return x * x - 4;
        }
    }

    @Test
    public void testSolve() {
        UnivariateRealFunction f = new TestFunction();
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);

        // Test solution within the range
        Assert.assertEquals(2.0, solver.solve(100, f, -10, 10, 3.0), 1e-6);

        // Test solution at the lower bound
        Assert.assertEquals(-2.0, solver.solve(100, f, -10, 10, -10.0), 1e-6);

        // Test solution at the upper bound
        Assert.assertEquals(2.0, solver.solve(100, f, -10, 10, 10.0), 1e-6);

        // Test solution with minimum absolute accuracy
        Assert.assertEquals(2.0, solver.solve(100, f, -10, 10, 1.999999, BaseSecantSolver.AllowedSolution.ANY_SIDE), 1e-6);

        // Test solution with maximum relative accuracy
        Assert.assertEquals(2.0, solver.solve(100, f, -10, 10, 2.000001, BaseSecantSolver.AllowedSolution.ANY_SIDE), 1e-6);

        // Test solution with maximum function value accuracy
        Assert.assertEquals(2.0, solver.solve(100, f, -10, 10, 2.0, BaseSecantSolver.AllowedSolution.ANY_SIDE), 1e-6);

        // Test solution outside the range - should throw ConvergenceException
        try {
            solver.solve(100, f, -4, 4, 5.0);
            Assert.fail("Expected ConvergenceException");
        } catch (ConvergenceException e) {
            // Expected exception
        }
    }

    @Test
    public void testDoSolve() {
        UnivariateRealFunction f = new TestFunction();
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);
        
        // Test solution within the range
        solver.setInterval(-10, 10);
        solver.setMaxEvaluations(100);
        Assert.assertEquals(2.0, solver.doSolve(), 1e-6);

        // Test solution at the lower endpoint
        solver.setInterval(-10, 10);
        solver.setMaxEvaluations(100);
        solver.setStartValue(-10);
        Assert.assertEquals(-2.0, solver.doSolve(), 1e-6);

        // Test solution at the upper endpoint
        solver.setInterval(-10, 10);
        solver.setMaxEvaluations(100);
        solver.setStartValue(10);
        Assert.assertEquals(2.0, solver.doSolve(), 1e-6);
        
        // Test solution within the range using different method
        solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.ILLINOIS);
        solver.setInterval(-10, 10);
        solver.setMaxEvaluations(100);
        Assert.assertEquals(2.0, solver.doSolve(), 1e-6);

        // Test solution at the lower endpoint using different method
        solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.ILLINOIS);
        solver.setInterval(-10, 10);
        solver.setMaxEvaluations(100);
        solver.setStartValue(-10);
        Assert.assertEquals(-2.0, solver.doSolve(), 1e-6);

        // Test solution at the upper endpoint using different method
        solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.ILLINOIS);
        solver.setInterval(-10, 10);
        solver.setMaxEvaluations(100);
        solver.setStartValue(10);
        Assert.assertEquals(2.0, solver.doSolve(), 1e-6);
    }
}