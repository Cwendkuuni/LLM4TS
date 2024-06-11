import org.junit.Assert;
import org.junit.Test;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.exception.MathInternalError;
import org.apache.commons.math.analysis.solvers.BaseSecantSolver;

public class BaseSecantSolverTest {

    @Test
    public void testSolve() {
        BaseSecantSolver solver = new BaseSecantSolverTestImpl(1e-6, BaseSecantSolver.Method.REGULA_FALSI);
        
        // Test root within range
        UnivariateRealFunction function = new UnivariateRealFunction() {
            public double value(double x) {
                return x - 2;
            }
        };
        double root = solver.solve(100, function, 1, 3);
        Assert.assertEquals(2.0, root, 1e-6);
        
        // Test root not within range, but on left side
        root = solver.solve(100, function, 0, 1);
        Assert.assertEquals(2.0, root, 1e-6);
        
        // Test root not within range, but on right side
        root = solver.solve(100, function, 3, 4);
        Assert.assertEquals(2.0, root, 1e-6);
    }
    
    @Test
    public void testComputeObjectiveValue() {
        BaseSecantSolver solver = new BaseSecantSolverTestImpl(1e-6, BaseSecantSolver.Method.REGULA_FALSI);
        
        UnivariateRealFunction function = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x;
            }
        };
        
        double result = solver.computeObjectiveValue(2.0);
        Assert.assertEquals(4.0, result, 1e-6);
    }
    
    @Test
    public void testVerifyBracketing() {
        BaseSecantSolver solver = new BaseSecantSolverTestImpl(1e-6, BaseSecantSolver.Method.REGULA_FALSI);
        
        // Test when min is larger than max
        try {
            solver.verifyBracketing(2, 1);
            Assert.fail();
        } catch (MathInternalError e) {
            // Expected exception
        }
        
        // Test when min equals max
        try {
            solver.verifyBracketing(2, 2);
            Assert.fail();
        } catch (MathInternalError e) {
            // Expected exception
        }
        
        // Test when f(min) and f(max) have same sign
        UnivariateRealFunction function = new UnivariateRealFunction() {
            public double value(double x) {
                return x - 2;
            }
        };
        try {
            solver.verifyBracketing(0, 1);
            Assert.fail();
        } catch (MathInternalError e) {
            // Expected exception
        }
    }
    
    private class BaseSecantSolverTestImpl extends BaseSecantSolver {
        
        public BaseSecantSolverTestImpl(double absoluteAccuracy, Method method) {
            super(absoluteAccuracy, method);
        }
        
        public double computeObjectiveValue(double x) {
            return getFunction().value(x);
        }
        
        public void verifyBracketing(double min, double max) {
            super.verifyBracketing(min, max);
        }
    }
}