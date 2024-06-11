import static org.junit.Assert.*;
import org.apache.commons.math.analysis.UnivariateFunction;
import org.apache.commons.math.exception.NumberIsTooSmallException;
import org.apache.commons.math.exception.MathInternalError;
import org.apache.commons.math.analysis.solvers.BracketingNthOrderBrentSolver;
import org.junit.Before;
import org.junit.Test;

public class BracketingNthOrderBrentSolverTest {
    
    private BracketingNthOrderBrentSolver solver;
    
    @Before
    public void setUp() {
        solver = new BracketingNthOrderBrentSolver();
    }
    
    @Test
    public void testNoBracketingInterval() {
        UnivariateFunction function = new UnivariateFunction() {
            public double value(double x) {
                return x;
            }
        };
        
        try {
            solver.solve(100, function, 0, 10);
            fail("An exception should have been thrown");
        } catch(NumberIsTooSmallException e) {
            // expected behavior
        }
    }
    
    @Test
    public void testExactRoot() {
        UnivariateFunction function = new UnivariateFunction() {
            public double value(double x) {
                return x - 5;
            }
        };
        
        double root = solver.solve(100, function, 0, 10);
        assertEquals(5, root, 0);
    }
    
    @Test
    public void testNoSolution() {
        UnivariateFunction function = new UnivariateFunction() {
            public double value(double x) {
                return x * x;
            }
        };
        
        try {
            solver.solve(100, function, 0, 10);
            fail("An exception should have been thrown");
        } catch(MathInternalError e) {
            // expected behavior
        }
    }
}