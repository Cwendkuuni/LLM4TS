import org.junit.Assert;
import org.junit.Test;

public class BaseSecantSolverTest {

    @Test
    public void testSolve_WhenFunctionHasRoot_ReturnsRoot() {
        BaseSecantSolver solver = new DummySecantSolver(1e-6, DummySecantSolver.Method.REGULA_FALSI);
        UnivariateRealFunction function = createFunctionWithRoot(2.0);
        double min = -10.0;
        double max = 10.0;
        double expectedRoot = 2.0;
        
        double actualRoot = solver.solve(100, function, min, max);
        
        Assert.assertEquals(expectedRoot, actualRoot, 1e-6);
    }
    
    @Test
    public void testSolve_WhenFunctionHasNoRoot_ConvergenceExceptionIsThrown() {
        BaseSecantSolver solver = new DummySecantSolver(1e-6, DummySecantSolver.Method.REGULA_FALSI);
        UnivariateRealFunction function = createFunctionWithoutRoot();
        double min = -10.0;
        double max = 10.0;
        
        try {
            solver.solve(100, function, min, max);
            Assert.fail("ConvergenceException expected but not thrown");
        } catch (ConvergenceException e) {
            Assert.assertTrue(true);
        } catch (Exception e) {
            Assert.fail("Unexpected exception thrown: " + e.getMessage());
        }
    }
    
    @Test
    public void testSolve_WhenFunctionHasRoot_AllowedSolutionIsLEFT_SIDE_ReturnsRootAtLeftSide() {
        BaseSecantSolver solver = new DummySecantSolver(1e-6, DummySecantSolver.Method.REGULA_FALSI);
        UnivariateRealFunction function = createFunctionWithRoot(2.0);
        double min = -10.0;
        double max = 10.0;
        double expectedRoot = 2.0;
        
        double actualRoot = solver.solve(100, function, min, max, AllowedSolution.LEFT_SIDE);
        
        Assert.assertEquals(expectedRoot, actualRoot, 1e-6);
    }
    
    @Test
    public void testSolve_WhenFunctionHasRoot_AllowedSolutionIsRIGHT_SIDE_ReturnsRootAtRightSide() {
        BaseSecantSolver solver = new DummySecantSolver(1e-6, DummySecantSolver.Method.REGULA_FALSI);
        UnivariateRealFunction function = createFunctionWithRoot(2.0);
        double min = -10.0;
        double max = 10.0;
        double expectedRoot = 2.0;
        
        double actualRoot = solver.solve(100, function, min, max, AllowedSolution.RIGHT_SIDE);
        
        Assert.assertEquals(expectedRoot, actualRoot, 1e-6);
    }
    
    @Test
    public void testSolve_WhenFunctionHasRoot_AllowedSolutionIsBELOW_SIDE_ReturnsRootBelow() {
        BaseSecantSolver solver = new DummySecantSolver(1e-6, DummySecantSolver.Method.REGULA_FALSI);
        UnivariateRealFunction function = createFunctionWithRoot(-2.0);
        double min = -10.0;
        double max = 10.0;
        double expectedRoot = -2.0;
        
        double actualRoot = solver.solve(100, function, min, max, AllowedSolution.BELOW_SIDE);
        
        Assert.assertEquals(expectedRoot, actualRoot, 1e-6);
    }
    
    @Test
    public void testSolve_WhenFunctionHasRoot_AllowedSolutionIsABOVE_SIDE_ReturnsRootAbove() {
        BaseSecantSolver solver = new DummySecantSolver(1e-6, DummySecantSolver.Method.REGULA_FALSI);
        UnivariateRealFunction function = createFunctionWithRoot(2.0);
        double min = -10.0;
        double max = 10.0;
        double expectedRoot = 2.0;
        
        double actualRoot = solver.solve(100, function, min, max, AllowedSolution.ABOVE_SIDE);
        
        Assert.assertEquals(expectedRoot, actualRoot, 1e-6);
    }
    
    private UnivariateRealFunction createFunctionWithRoot(double root) {
        return new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x - root;
            }
        };
    }
    
    private UnivariateRealFunction createFunctionWithoutRoot() {
        return new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x + 1.0;
            }
        };
    }
    
    private static class DummySecantSolver extends BaseSecantSolver {

        public DummySecantSolver(double absoluteAccuracy, Method method) {
            super(absoluteAccuracy, method);
        }
    
        @Override
        protected double computeObjectiveValue(double x) {
            // Dummy implementation for testing purposes
            return x * x;
        }
    
        @Override
        protected void verifyBracketing(double x0, double x1) {
            // Dummy implementation for testing purposes
        }
    }
}