import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class BaseSecantSolverTest {

    @Test
    public void testConstructor_withAbsoluteAccuracy() {
        BaseSecantSolver solver = new BaseSecantSolver(0.001, BaseSecantSolver.Method.REGULA_FALSI);
        assertEquals(0.001, solver.getAbsoluteAccuracy(), 0.0);
    }

    @Test
    public void testConstructor_withMultipleAccuracies() {
        BaseSecantSolver solver = new BaseSecantSolver(0.1, 0.01, 0.001, BaseSecantSolver.Method.ILLINOIS);
        assertEquals(0.1, solver.getRelativeAccuracy(), 0.0);
        assertEquals(0.01, solver.getAbsoluteAccuracy(), 0.0);
        assertEquals(0.001, solver.getFunctionValueAccuracy(), 0.0);
    }

    @Test
    public void testSolve_withMaxEvalAndAllowedSolution() {
        BaseSecantSolver solver = new BaseSecantSolver(0.1, 0.01, 0.001, BaseSecantSolver.Method.PEGASUS);
        double result = solver.solve(100, x -> x * x - 4, 1, 3, BaseSecantSolver.AllowedSolution.BELOW_SIDE);
        assertEquals(2.0, result, 0.0);
    }

    @Test
    public void testSolve_withMaxEvalAndAllowedSolution_invalidStartValue() {
        BaseSecantSolver solver = new BaseSecantSolver(0.1, 0.01, 0.001, BaseSecantSolver.Method.REGULA_FALSI);
        double result = solver.solve(100, x -> x * x - 4, 1, 3, 5, BaseSecantSolver.AllowedSolution.ABOVE_SIDE);
        assertEquals(Double.NaN, result, 0.0);
    }

    @Test
    public void testDoSolve_exactRoot() {
        BaseSecantSolver solver = new BaseSecantSolver(0.1, 0.01, 0.001, BaseSecantSolver.Method.ILLINOIS) {
            @Override
            protected double computeObjectiveValue(double x) {
                if (x == 3.0) {
                    return 0.0;
                }
                return super.computeObjectiveValue(x);
            }
        };
        double result = solver.doSolve();
        assertEquals(3.0, result, 0.0);
    }

    // Add more test cases for other methods and scenarios

}