
import org.junit.*;
import static org.junit.Assert.*;

public class BracketingNthOrderBrentSolverTest {
    
    private BracketingNthOrderBrentSolver solver;
    
    @Before
    public void setUp() {
        solver = new BracketingNthOrderBrentSolver();
    }
    
    @Test
    public void testSolve_ExactRoot() {
        UnivariateFunction f = new UnivariateFunction() {
            public double value(double x) {
                return x - 2;
            }
        };
        double solution = solver.solve(100, f, 0, 4);
        assertEquals(2.0, solution, 0);
    }
    
    @Test
    public void testSolve_NoRoot() {
        UnivariateFunction f = new UnivariateFunction() {
            public double value(double x) {
                return x + 5;
            }
        };
        double solution = solver.solve(100, f, 0, 4);
        assertTrue(Double.isNaN(solution));
    }
    
    @Test
    public void testSolve_EvaluatesFunctionCorrectly() {
        UnivariateFunction f = new UnivariateFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        };
        double solution = solver.solve(100, f, 0, 4);
        assertEquals(2.0, solution, 0.00001);
    }
    
    @Test(expected = NoBracketingException.class)
    public void testSolve_ThrowsNoBracketingException() {
        UnivariateFunction f = new UnivariateFunction() {
            public double value(double x) {
                return x - 2;
            }
        };
        double solution = solver.solve(100, f, 0, 1);
    }
}
