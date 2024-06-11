import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.math.analysis.UnivariateFunction;
import org.apache.commons.math.analysis.solvers.BracketingNthOrderBrentSolver;
import org.apache.commons.math.exception.NumberIsTooSmallException;

public class BracketingNthOrderBrentSolverTest {
    
    private BracketingNthOrderBrentSolver solver;
    
    @Before
    public void setUp() {
        solver = new BracketingNthOrderBrentSolver();
    }
    
    @Test
    public void testSolve() {
        UnivariateFunction f = new TestFunction();
        double min = 0.0;
        double max = 10.0;
        double expected = 5.0;
        double result = solver.solve(100, f, min, max);
        assertEquals(expected, result, 1e-6);
    }
    
    @Test(expected = NumberIsTooSmallException.class)
    public void testSolveException() {
        UnivariateFunction f = new TestFunction();
        double min = 0.0;
        double max = 10.0;
        int maximalOrder = 1;
        solver = new BracketingNthOrderBrentSolver(1e-6, maximalOrder);
        solver.solve(100, f, min, max);
    }
    
    private class TestFunction implements UnivariateFunction {
        @Override
        public double value(double x) {
            return x - 5.0;
        }
    }
}