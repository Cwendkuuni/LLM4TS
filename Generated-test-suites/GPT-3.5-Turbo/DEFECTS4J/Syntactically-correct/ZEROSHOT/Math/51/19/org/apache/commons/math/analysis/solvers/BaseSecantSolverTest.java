import org.junit.Assert;
import org.junit.Test;

public class BaseSecantSolverTest {
    
    @Test
    public void testSolve() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);
        double solution = solver.solve(100, new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        }, 0, 5);
        Assert.assertEquals(2.0000019073486328, solution, 1e-6);
    }
}