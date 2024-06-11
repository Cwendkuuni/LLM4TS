import org.apache.commons.math.analysis.solvers.*;
import org.apache.commons.math.util.FastMath;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.exception.MathInternalError;
import org.junit.Test;
import static org.junit.Assert.*;

public class BaseSecantSolverTest {
    
    @Test
    public void testSolve_ExactRoot_ReturnsRoot() {
        BaseSecantSolver solver = new TestSecantSolver(0.0, BaseSecantSolver.Method.ILLINOIS);
        double result = solver.solve(100, new TestFunction(), 1.0, 2.0, 1.5);
        assertEquals(1.5, result, 0.0);
    }
    
    @Test
    public void testSolve_BracketingSolution_ReturnsRoot() {
        BaseSecantSolver solver = new TestSecantSolver(0.0, BaseSecantSolver.Method.ILLINOIS);
        double result = solver.solve(100, new TestFunction(), 1.0, 2.0, 1.5, BaseSecantSolver.AllowedSolution.ANY_SIDE);
        assertEquals(1.5, result, 0.0);
    }
    
    @Test
    public void testSolve_NotBracketingSolution_ThrowsMathInternalError() {
        BaseSecantSolver solver = new TestSecantSolver(0.0, BaseSecantSolver.Method.ILLINOIS);
        try {
            solver.solve(100, new TestFunction(), 1.0, 2.0, 3.0, BaseSecantSolver.AllowedSolution.ANY_SIDE);
            fail("Expected MathInternalError to be thrown");
        } catch (MathInternalError e) {
            // Passed
        }
    }
    
    @Test
    public void testSolve_FunctionValueTooSmall_ReturnsApproximation() {
        BaseSecantSolver solver = new TestSecantSolver(0.0, BaseSecantSolver.Method.ILLINOIS);
        double result = solver.solve(100, new TestFunction(), 1.0, 2.0, 3.0, BaseSecantSolver.AllowedSolution.ANY_SIDE);
        assertEquals(3.0, result, 0.0);
    }
    
    @Test
    public void testSolve_WithinAccuracies_ReturnsApproximation() {
        BaseSecantSolver solver = new TestSecantSolver(0.0, BaseSecantSolver.Method.ILLINOIS);
        double result = solver.solve(100, new TestFunction(), 1.0, 2.0, 1.5, BaseSecantSolver.AllowedSolution.ANY_SIDE);
        assertEquals(1.5, result, 0.0);
    }
    
    private class TestSecantSolver extends BaseSecantSolver {
        
        public TestSecantSolver(double absoluteAccuracy, Method method) {
            super(absoluteAccuracy, method);
        }
        
        protected double computeObjectiveValue(double x) {
            return x - 1.5; // Root is at 1.5
        }
        
        protected void verifyBracketing(double lower, double upper) {
            // Check if the bounds contain the root
            if (computeObjectiveValue(lower) * computeObjectiveValue(upper) >= 0.0) {
                throw new MathInternalError();
            }
        }
        
    }
    
    private class TestFunction implements UnivariateRealFunction {
        
        public double value(double x) {
            return x - 1.5; // Root is at 1.5
        }
        
    }
    
}