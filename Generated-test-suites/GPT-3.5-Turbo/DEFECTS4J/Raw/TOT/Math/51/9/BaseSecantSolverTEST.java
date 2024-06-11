import org.junit.Assert;
import org.junit.Test;

import org.apache.commons.math.analysis.UnivariateRealFunction;

public class BaseSecantSolverTest {
    
    @Test
    public void testParentClassMethods() {
        MockSolver solver = new MockSolver(1e-4, BaseSecantSolver.Method.REGULA_FALSI);
        
        // Test solve(maxEval, f, min, max, allowedSolution)
        double result1 = solver.solve(100, new MockFunction(), 0, 10, BaseSecantSolver.AllowedSolution.ANY_SIDE);
        Assert.assertEquals(3.0, result1, 1e-4);
        
        // Test solve(maxEval, f, min, max, startValue, allowedSolution)
        double result2 = solver.solve(100, new MockFunction(), 0, 10, 2.0, BaseSecantSolver.AllowedSolution.ANY_SIDE);
        Assert.assertEquals(3.0, result2, 1e-4);
        
        // Test solve(maxEval, f, min, max, startValue)
        double result3 = solver.solve(100, new MockFunction(), 0, 10, 2.0);
        Assert.assertEquals(3.0, result3, 1e-4);
        
    }
    
    @Test
    public void testDoSolve() {
        MockSolver solver = new MockSolver(1e-4, BaseSecantSolver.Method.REGULA_FALSI);
        
        // Test case where initial approximation is the exact root
        double result1 = solver.doSolve(2, 6, 1, 0);
        Assert.assertEquals(2.0, result1, 1e-4);
        
        // Test case where current function value is too small
        double result2 = solver.doSolve(1, 6, 2, 3);
        Assert.assertEquals(2.0625, result2, 1e-4);
        
        // Test case where the current interval is within the given accuracies
        double result3 = solver.doSolve(2, 10, 4, 5);
        Assert.assertEquals(4.088, result3, 1e-3);
        
        // Test case where no convergence is reached
        double result4 = solver.doSolve(3, 10, 4, 5);
        Assert.assertEquals(Double.NaN, result4, 1e-4);
        
    }
    
    private class MockFunction implements UnivariateRealFunction {
        @Override
        public double value(double x) {
            return x * x - 9;
        }
    }
    
    private class MockSolver extends BaseSecantSolver {
        public MockSolver(double absoluteAccuracy, Method method) {
            super(absoluteAccuracy, method);
        }
        
        public double doSolve(double x0, double x1, double f0, double f1) {
            setMin(x0);
            setMax(x1);
            setFunctionValueAccuracy(1e-4);
            return super.doSolve();
        }
        
        @Override
        protected double computeObjectiveValue(double x) {
            return x * x - 9;
        }
    }
    
    
}