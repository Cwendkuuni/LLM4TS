import org.junit.Test;

public class BaseSecantSolverTest {

    @Test
    public void testSolve() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);
        double result = solver.solve(100, new TestFunction(), 0, 10, BaseSecantSolver.AllowedSolution.ANY_SIDE);
        Assert.assertEquals(5.0, result, 1e-6);
    }

    private static class TestFunction implements UnivariateRealFunction {

        public double value(double x) {
            return x - 5;
        }
    }
}