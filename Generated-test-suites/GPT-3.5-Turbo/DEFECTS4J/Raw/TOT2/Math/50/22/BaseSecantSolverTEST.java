import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.exception.MathInternalError;

public class BaseSecantSolverTest {

    @Test
    public void testSolve_MaxEval() {
        // Test solving with maximum evaluations
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        };
        double result = solver.solve(10, f, -10, 10, BaseSecantSolver.AllowedSolution.ANY_SIDE);
        assertEquals(2.0, result, 1e-6);
    }

    @Test
    public void testSolve_MaxEvalOutOfRange() {
        // Test solving out of range with maximum evaluations
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.ILLINOIS);
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x;
            }
        };
        try {
            double result = solver.solve(10, f, -10, 10, BaseSecantSolver.AllowedSolution.ANY_SIDE);
            fail("MathInternalError expected");
        } catch (MathInternalError e) {
            // Test passed
        }
    }

    @Test
    public void testSolve_Accuracy() {
        // Test solving with specific accuracy
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, 1e-6, 1e-3, BaseSecantSolver.Method.PEGASUS);
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        };
        double result = solver.solve(100, f, -10, 10, BaseSecantSolver.AllowedSolution.ANY_SIDE);
        assertEquals(2.0, result, 1e-6);
    }

    @Test
    public void testSolve_InvalidFunction() {
        // Test solving with invalid function
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);
        UnivariateRealFunction f = null;
        try {
            double result = solver.solve(100, f, -10, 10, BaseSecantSolver.AllowedSolution.ANY_SIDE);
            fail("NullPointerException expected");
        } catch (NullPointerException e) {
            // Test passed
        }
    }

}