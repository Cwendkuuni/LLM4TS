import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.exception.MathInternalError;
import org.junit.Test;

public class BaseSecantSolverTest {
    
    @Test
    public void testSolve_WithDummyFunction_ShouldReturnCorrectRoot() {
        UnivariateRealFunction dummyFunction = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x - 5; // Dummy function gives root at x = 5
            }
        };
        
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);
        double root = solver.solve(100, dummyFunction, 0, 10, BaseSecantSolver.AllowedSolution.ANY_SIDE);
        
        assertEquals(5.0, root, 1e-6);
    }
    
    @Test
    public void testSolve_WithZeroFunctionValueForInitialBounds_ShouldReturnRoot() {
        UnivariateRealFunction zeroFunction = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return 0.0; // Function always returns 0
            }
        };
        
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.ILLINOIS);
        double root = solver.solve(100, zeroFunction, -10, 10, BaseSecantSolver.AllowedSolution.ANY_SIDE);
        
        assertEquals(0.0, root, 1e-6);
    }
    
    @Test
    public void testSolve_WithFunctionValueBelowTolerance_ShouldReturnApproximation() {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) + 1e-7; // Small deviation from sine function to test approximation
            }
        };
        
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, 1e-8, 1e-10, BaseSecantSolver.Method.PEGASUS);
        double root = solver.solve(100, function, -10, 10, BaseSecantSolver.AllowedSolution.ANY_SIDE);
        
        assertEquals(Math.PI, root, 1e-6);
    }
    
    @Test
    public void testSolve_WithInvalidMethod_ShouldThrowMathInternalError() {
        UnivariateRealFunction dummyFunction = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x - 5;
            }
        };
        
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);
        
        try {
            solver.solve(100, dummyFunction, 0, 10, BaseSecantSolver.AllowedSolution.ANY_SIDE);
            fail("Expected MathInternalError was not thrown");
        } catch (MathInternalError e) {
            // Expected behavior
        }
    }
}