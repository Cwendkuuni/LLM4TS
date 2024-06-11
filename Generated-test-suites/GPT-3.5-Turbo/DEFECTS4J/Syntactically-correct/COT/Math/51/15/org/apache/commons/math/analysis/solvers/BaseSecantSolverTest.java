import org.apache.commons.math.analysis.solvers.*;
import org.apache.commons.math.exception.MathInternalError;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.junit.Test;
import static org.junit.Assert.*;

public class BaseSecantSolverTest {

    // Some sample functions for testing
    private UnivariateRealFunction constantFunction = new UnivariateRealFunction() {
        public double value(double x) {
            return 2.0;
        }
    };
    
    private UnivariateRealFunction linearFunction = new UnivariateRealFunction() {
        public double value(double x) {
            return x;
        }
    };
    
    private UnivariateRealFunction quadraticFunction = new UnivariateRealFunction() {
        public double value(double x) {
            return x*x;
        }
    };
    
    private UnivariateRealFunction cubicFunction = new UnivariateRealFunction() {
        public double value(double x) {
            return x*x*x - 2*x;
        }
    };
    
    @Test
    public void testSolveConstantFunction() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);
        double root = solver.solve(100, constantFunction, 0, 1);
        assertEquals(0.0, root, 1e-6);
    }
    
    @Test
    public void testSolveLinearFunction() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);
        double root = solver.solve(100, linearFunction, 0, 1);
        assertEquals(0.0, root, 1e-6);
    }
    
    @Test
    public void testSolveQuadraticFunction() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);
        double root = solver.solve(100, quadraticFunction, 0, 1);
        assertEquals(0.0, root, 1e-6);
    }
    
    @Test
    public void testSolveCubicFunction() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);
        double root = solver.solve(100, cubicFunction, 0, 1);
        assertEquals(0.378568, root, 1e-6);
    }
    
    @Test(expected = MathInternalError.class)
    public void testInvalidMethod() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, null);
    }
    
}