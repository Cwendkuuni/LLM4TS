import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.exception.MathInternalError;
import org.apache.commons.math.util.FastMath;
import org.junit.Test;

public class BaseSecantSolverTest {
    
    private class TestUnivariateRealFunction implements UnivariateRealFunction {
        
        public double value;

        public TestUnivariateRealFunction(double value) {
            this.value = value;
        }

        @Override
        public double value(double x) {
            return value;
        }
    }
    
    private double relativeAccuracy = 1e-6;
    private double absoluteAccuracy = 1e-6;
    private double functionValueAccuracy = 1e-6;
    
    @Test
    public void testSolve() {
        BaseSecantSolver solver = new BaseSecantSolver(relativeAccuracy, absoluteAccuracy, functionValueAccuracy, BaseSecantSolver.Method.REGULA_FALSI);
        
        // Test case where x0 is the exact root
        TestUnivariateRealFunction f1 = new TestUnivariateRealFunction(0.0);
        double root1 = solver.solve(100, f1, -10, 10, 0.0);
        assertEquals(0.0, root1, absoluteAccuracy);
        
        // Test case where x1 is the exact root
        TestUnivariateRealFunction f2 = new TestUnivariateRealFunction(0.0);
        double root2 = solver.solve(100, f2, -10, 10, 10.0);
        assertEquals(10.0, root2, absoluteAccuracy);
        
        // Test case where function value of last approximation is too small
        TestUnivariateRealFunction f3 = new TestUnivariateRealFunction(1e-7);
        double root3 = solver.solve(100, f3, -10, 10, 0.0, BaseSecantSolver.AllowedSolution.ANY_SIDE);
        assertEquals(0.0, root3, absoluteAccuracy);
        
        // Test case where current interval is within given accuracies
        TestUnivariateRealFunction f4 = new TestUnivariateRealFunction(1e-9);
        double root4 = solver.solve(100, f4, -10, 10, 0.0, BaseSecantSolver.AllowedSolution.ANY_SIDE);
        assertEquals(0.0, root4, absoluteAccuracy);
    }
    
    @Test(expected = MathInternalError.class)
    public void testSolveInternalError() {
        BaseSecantSolver solver = new BaseSecantSolver(relativeAccuracy, absoluteAccuracy, functionValueAccuracy, BaseSecantSolver.Method.REGULA_FALSI) {
            @Override
            protected final double doSolve() {
                throw new MathInternalError();
            }
        };
        
        TestUnivariateRealFunction f = new TestUnivariateRealFunction(1.0);
        solver.solve(100, f, -10, 10, 0.0);
    }
}