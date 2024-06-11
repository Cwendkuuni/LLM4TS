import org.junit.Assert;
import org.junit.Test;
import org.apache.commons.math.analysis.solvers.BaseSecantSolver;
import org.apache.commons.math.analysis.solvers.AllowedSolution;
import org.apache.commons.math.analysis.UnivariateRealFunction;

public class BaseSecantSolverTest {

    // Test computeObjectiveValue method
    
    @Test
    public void testComputeObjectiveValue() {
        BaseSecantSolver solver = new BaseSecantSolverStub();
        UnivariateRealFunction f = new UnivariateRealFunctionStub();
        
        double x = 1.0;
        double result = solver.computeObjectiveValue(x, f);
        
        Assert.assertEquals(4.0, result, 0.0);
    }
    
    // Test verifyBracketing method
    
    @Test
    public void testVerifyBracketing() {
        BaseSecantSolver solver = new BaseSecantSolverStub();
        
        double x0 = 0.0;
        double x1 = 1.0;
        
        // Test passing case
        solver.verifyBracketing(x0, x1);
        
        // Test failing case
        x0 = 1.0;
        x1 = 0.0;
        try {
            solver.verifyBracketing(x0, x1);
            Assert.fail("Exception not thrown");
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("Function values at endpoints do not have different signs.", e.getMessage());
        }
    }

    // Test solve method
    
    @Test
    public void testSolve() {
        BaseSecantSolver solver = new BaseSecantSolverStub();
        UnivariateRealFunction f = new UnivariateRealFunctionStub();
        
        double result = solver.solve(100, f, 0.0, 2.0, AllowedSolution.ANY_SIDE);
        
        Assert.assertEquals(1.0, result, 0.0);
    }
    
    // Test doSolve method
    
    @Test
    public void testDoSolve() {
        BaseSecantSolver solver = new BaseSecantSolverStub();
        
        // Test normal case
        solver.setMin(0.0);
        solver.setMax(1.0);
        solver.setStartValue(0.5);
        double result = solver.doSolve();
        Assert.assertEquals(0.6180339887, result, 0.0000001);
        
        // Test function value is exact root
        solver.setMin(0.0);
        solver.setMax(1.0);
        solver.setStartValue(1.0);
        result = solver.doSolve();
        Assert.assertEquals(1.0, result, 0.0);
        
        // Test function value is too small
        solver.setMin(0.0);
        solver.setMax(1.0);
        solver.setStartValue(0.5);
        solver.setFunctionValueAccuracy(1e-8);
        result = solver.doSolve();
        Assert.assertEquals(0.6180339887, result, 0.0000001);
        
        // Test current interval is within given accuracies
        solver.setMin(0.0);
        solver.setMax(1.0);
        solver.setStartValue(0.5);
        solver.setFunctionValueAccuracy(1e-20);
        result = solver.doSolve();
        Assert.assertEquals(0.6180339887, result, 0.0000001);
    }
}

class BaseSecantSolverStub extends BaseSecantSolver {

    public BaseSecantSolverStub() {
        super(1e-6, Method.REGULA_FALSI);
    }

    @Override
    protected double computeObjectiveValue(double point, UnivariateRealFunction function) {
        return 4.0;
    }

    @Override
    protected void verifyBracketing(double lower, double upper) {
        if (lower >= upper) {
            throw new IllegalArgumentException("Function values at endpoints do not have different signs.");
        }
    }
}

class UnivariateRealFunctionStub implements UnivariateRealFunction {

    @Override
    public double value(double x) {
        return Math.pow(x, 2) - 1;
    }
}