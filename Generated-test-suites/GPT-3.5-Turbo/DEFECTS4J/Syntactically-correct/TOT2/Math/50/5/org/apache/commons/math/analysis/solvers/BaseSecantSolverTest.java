import org.junit.Test;
import org.junit.Assert;
import org.apache.commons.math.analysis.solvers.BaseSecantSolver;

public class BaseSecantSolverTest {

    @Test
    public void testDefaultConstructor() {
        BaseSecantSolver solver = new BaseSecantSolver();  // Using default constructor
        Assert.assertNotNull(solver);
    }

    @Test
    public void testSolveWithAllowedSolution() {
        BaseSecantSolver solver = new BaseSecantSolver();
        double result = solver.solve(100, Math::cos, 0.0, Math.PI, BaseSecantSolver.AllowedSolution.ANY_SIDE);
        Assert.assertEquals(1.5707963267948966, result, 0.0001);
    }

    @Test
    public void testSolveWithBracketing() {
        BaseSecantSolver solver = new BaseSecantSolver();
        double result = solver.solve(100, Math::sin, 0.0, 2 * Math.PI, 0.5, BaseSecantSolver.AllowedSolution.ANY_SIDE);
        Assert.assertEquals(Math.PI, result, 0.0001);
    }

    @Test
    public void testDoSolve() {
        BaseSecantSolver solver = new BaseSecantSolver();
        double result = solver.doSolve();
        Assert.assertNotNull(result);
    }

    @Test
    public void testSolveWithInvalidBounds() {
        BaseSecantSolver solver = new BaseSecantSolver();
        double result = solver.solve(100, Math::log, 0.0, 1.0, BaseSecantSolver.AllowedSolution.ANY_SIDE);
        Assert.assertEquals(Double.NaN, result, 0.0001);
    }

    @Test
    public void testSolveWithInfinity() {
        BaseSecantSolver solver = new BaseSecantSolver();
        double result = solver.solve(100, Math::exp, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, BaseSecantSolver.AllowedSolution.ANY_SIDE);
        Assert.assertTrue(Double.isInfinite(result));
    }

}