import org.apache.commons.math.analysis.solvers.BaseSecantSolver;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.util.FastMath;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class BaseSecantSolverTest {

    private BaseSecantSolver solver;

    @Before
    public void setup() {
        solver = new TestSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);
    }

    @Test
    public void testSolve() {
        UnivariateRealFunction f = new TestFunction();
        double result = solver.solve(100, f, 0, 10);
        assertEquals(2.0, result, 1e-6);
    }

    private static class TestFunction implements UnivariateRealFunction {

        public double value(double x) {
            return x - 2.0;
        }
    }

    private static class TestSecantSolver extends BaseSecantSolver {

        public TestSecantSolver(double absoluteAccuracy, Method method) {
            super(absoluteAccuracy, method);
        }

        public double computeObjectiveValue(double x) {
            return x - 2.0;
        }
    }
}