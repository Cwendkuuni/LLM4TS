import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.exception.MathInternalError;
import org.junit.Assert;
import org.junit.Test;

public class BaseSecantSolverTest {
    
    private static final double ABSOLUTE_ERROR = 1e-6;
    
    private class TestFunction implements UnivariateRealFunction {
        
        public double value(double x) {
            return x * x - 4;
        }
    }
    
    private class ErrorFunction implements UnivariateRealFunction {
        
        public double value(double x) {
            throw new MathInternalError();
        }
    }
    
    @Test
    public void testSolveAnySide() {
        BaseSecantSolver solver = new BaseSecantSolver(ABSOLUTE_ERROR, BaseSecantSolver.Method.REGULA_FALSI);
        double result = solver.solve(100, new TestFunction(), 0, 5, BaseSecantSolver.AllowedSolution.ANY_SIDE);
        Assert.assertEquals(2.0, result, ABSOLUTE_ERROR);
    }
    
    @Test
    public void testSolveLeftSide() {
        BaseSecantSolver solver = new BaseSecantSolver(ABSOLUTE_ERROR, BaseSecantSolver.Method.REGULA_FALSI);
        double result = solver.solve(100, new TestFunction(), 0, 5, BaseSecantSolver.AllowedSolution.LEFT_SIDE);
        Assert.assertEquals(2.0, result, ABSOLUTE_ERROR);
    }
    
    @Test
    public void testSolveRightSide() {
        BaseSecantSolver solver = new BaseSecantSolver(ABSOLUTE_ERROR, BaseSecantSolver.Method.REGULA_FALSI);
        double result = solver.solve(100, new TestFunction(), 0, 5, BaseSecantSolver.AllowedSolution.RIGHT_SIDE);
        Assert.assertEquals(2.0, result, ABSOLUTE_ERROR);
    }
    
    @Test
    public void testSolveBelowSide() {
        BaseSecantSolver solver = new BaseSecantSolver(ABSOLUTE_ERROR, BaseSecantSolver.Method.REGULA_FALSI);
        double result = solver.solve(100, new TestFunction(), 0, 5, BaseSecantSolver.AllowedSolution.BELOW_SIDE);
        Assert.assertEquals(2.0, result, ABSOLUTE_ERROR);
    }
    
    @Test
    public void testSolveAboveSide() {
        BaseSecantSolver solver = new BaseSecantSolver(ABSOLUTE_ERROR, BaseSecantSolver.Method.REGULA_FALSI);
        double result = solver.solve(100, new TestFunction(), 0, 5, BaseSecantSolver.AllowedSolution.ABOVE_SIDE);
        Assert.assertEquals(2.0, result, ABSOLUTE_ERROR);
    }
    
    @Test(expected = MathInternalError.class)
    public void testSolve_Error() {
        BaseSecantSolver solver = new BaseSecantSolver(ABSOLUTE_ERROR, BaseSecantSolver.Method.REGULA_FALSI);
        solver.solve(100, new ErrorFunction(), 0, 5, BaseSecantSolver.AllowedSolution.ANY_SIDE);
    }
}