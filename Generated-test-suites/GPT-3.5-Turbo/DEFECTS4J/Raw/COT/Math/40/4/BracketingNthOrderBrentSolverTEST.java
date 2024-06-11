import org.apache.commons.math.exception.NumberIsTooSmallException;
import org.apache.commons.math.analysis.solvers.BracketingNthOrderBrentSolver;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class BracketingNthOrderBrentSolverTest {
    
    private BracketingNthOrderBrentSolver solver;
    
    @Before
    public void setUp() throws NumberIsTooSmallException {
        solver = new BracketingNthOrderBrentSolver();
    }
    
    @Test
    public void testSolve_withMaximalOrder() {
        // provided test case
        double result = solver.solve(100, f, 1.0, 2.0);
        assertEquals(1.6666666666666665, result, 1e-6);
    }
    
    /* Add more test cases for each public method */
    
    
    
    /* Helper function for the test cases */
    private final UnivariateFunction f = new UnivariateFunction() {
        public double value(double x) {
            return x - 2;
        }
    };
}