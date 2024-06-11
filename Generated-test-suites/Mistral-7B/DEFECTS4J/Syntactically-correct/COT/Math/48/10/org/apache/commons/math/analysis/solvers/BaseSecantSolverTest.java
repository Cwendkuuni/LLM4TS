import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.junit.Assert;
import org.junit.Test;

public class BaseSecantSolverTest {

    /**
     * Test constructor with default arguments.
     */
    @Test
    public void testDefaultConstructor() {
        BaseSecantSolver solver = new BaseSecantSolver(BaseSecantSolver.Method.REGULA_FALSI);
        Assert.assertEquals(BaseSecantSolver.DEFAULT_ABSOLUTE_ACCURACY, solver.getAbsoluteAccuracy(), 0);
        Assert.assertEquals(BaseSecantSolver.Method.REGULA_FALSI, solver.getMethod());
    }

    /**
     * Test constructor with custom arguments.
     */
    @Test
    public void testCustomConstructor() {
        UnivariateRealFunction testFunction = new TestFunction();
        BaseSecantSolver solver = new BaseSecantSolver(0.001, 0.0001, BaseSecantSolver.Method.ILLINOIS, testFunction);
        Assert.assertEquals(0.001, solver.getRelativeAccuracy(), 0);
        Assert.assertEquals(0.0001, solver.getAbsoluteAccuracy(), 0);
        Assert.assertEquals(BaseSecantSolver.Method.ILLINOIS, solver.getMethod());
    }

    /**
     * Test solve method with default arguments and a simple test function.
     */
    @Test
    public void testSolveWithDefaultArgumentsAndSimpleFunction() {
        UnivariateRealFunction testFunction = new TestFunction();
        BaseSecantSolver solver = new BaseSecantSolver(BaseSecantSolver.Method.REGULA_FALSI);
        double root = solver.solve(testFunction, 0.0, 1.0);
        Assert.assertEquals(testFunction.findRoot(), root, 0.001);
    }

    /**
     * Test solve method with custom arguments and a simple test function.
     */
    @Test
    public void testSolveWithCustomArgumentsAndSimpleFunction() {
        UnivariateRealFunction testFunction = new TestFunction();
        BaseSecantSolver solver = new BaseSecantSolver(0.001, 0.0001, BaseSecantSolver.Method.ILLINOIS, testFunction);
        double root = solver.solve(testFunction, 0.0, 1.0);
        Assert.assertEquals(testFunction.findRoot(), root, 0.0001);
    }

    /**
     * Test solve method with a test function containing a root between the bounds.
     */
    @Test
    public void testSolveWithFunctionContainingRootBetweenBounds() {
        UnivariateRealFunction testFunction = new TestFunctionWithRootBetweenBounds();
        BaseSecantSolver solver = new BaseSecantSolver(BaseSecantSolver.Method.REGULA_FALSI);

        try {
            solver.solve(testFunction, 0.0, 2.0);
            Assert.fail();
        } catch (ConvergenceException e) {
            // Expected
        }
    }

    /**
     * Test solve method with a test function containing a root outside the bounds.
     */
    @Test
    public void testSolveWithFunctionContainingRootOutsideBounds() {
        UnivariateRealFunction testFunction = new TestFunctionWithRootOutsideBounds();
        BaseSecantSolver solver = new BaseSecantSolver(BaseSecantSolver.Method.REGULA_FALSI);

        try {
            solver.solve(testFunction, 0.0, 2.0);
            Assert.fail();
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    /**
     * Test solve method with a test function containing a root at a bound.
     */
    @Test
    public void testSolveWithFunctionContainingRootAtBound() {
        UnivariateRealFunction testFunction = new TestFunctionWithRootAtBound();
        BaseSecantSolver solver = new BaseSecantSolver(BaseSecantSolver.Method.REGULA_FALSI);

        try {
            solver.solve(testFunction, 0.0, 0.0);
            Assert.fail();
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    
}