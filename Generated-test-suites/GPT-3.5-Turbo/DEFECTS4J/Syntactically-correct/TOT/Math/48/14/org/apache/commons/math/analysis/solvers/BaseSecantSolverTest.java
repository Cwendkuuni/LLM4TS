import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.analysis.solvers.AllowedSolution;
import org.apache.commons.math.analysis.solvers.BaseSecantSolver;
import org.junit.Assert;
import org.junit.Test;

public class BaseSecantSolverTest {

    private static final double EPSILON = 1e-6;

    private class TestFunction implements UnivariateRealFunction {
        public double value(double x) {
            return x * x - 4;
        }
    }

    @Test
    public void testSolve() {
        TestFunction testFunction = new TestFunction();
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);
        double result = solver.solve(100, testFunction, 0, 5, AllowedSolution.ANY_SIDE);
        Assert.assertEquals(2, result, EPSILON);
    }

    @Test
    public void testSolve_withMinEqualMax() {
        TestFunction testFunction = new TestFunction();
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);
        double result = solver.solve(100, testFunction, 2, 2, AllowedSolution.ANY_SIDE);
        Assert.assertEquals(2, result, EPSILON);
    }

    @Test
    public void testSolve_withResultOnLeftSide() {
        TestFunction testFunction = new TestFunction();
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.ILLINOIS);
        double result = solver.solve(100, testFunction, -5, 0, AllowedSolution.LEFT_SIDE);
        Assert.assertEquals(-2, result, EPSILON);
    }

    @Test
    public void testSolve_withResultOnRightSide() {
        TestFunction testFunction = new TestFunction();
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.ILLINOIS);
        double result = solver.solve(100, testFunction, 0, 5, AllowedSolution.RIGHT_SIDE);
        Assert.assertEquals(2, result, EPSILON);
    }

    @Test
    public void testSolve_withResultBelow() {
        TestFunction testFunction = new TestFunction();
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.PEGASUS);
        double result = solver.solve(100, testFunction, 0, 5, AllowedSolution.BELOW_SIDE);
        Assert.assertTrue(result < 2);
        Assert.assertEquals(testFunction.value(result), 0, EPSILON);
    }

    @Test
    public void testSolve_withResultAbove() {
        TestFunction testFunction = new TestFunction();
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.PEGASUS);
        double result = solver.solve(100, testFunction, -5, 0, AllowedSolution.ABOVE_SIDE);
        Assert.assertTrue(result > -2);
        Assert.assertEquals(testFunction.value(result), 0, EPSILON);
    }

}