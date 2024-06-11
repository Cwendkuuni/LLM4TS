
import org.junit.Test;
import org.apache.commons.math.analysis.UnivariateFunction;
import org.apache.commons.math.exception.NumberIsTooSmallException;
import org.apache.commons.math.exception.NoBracketingException;

public class BracketingNthOrderBrentSolverTest {

    @Test
    public void testSolve() {
        try {
            BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
            // Test case 1
            solver.solve(100, new UnivariateFunction() {
                public double value(double x) {
                    return x * x - 4;
                }
            }, -2, 2, 0);
            
        } catch (NumberFormatException e) {
            
        } catch (NoBracketingException e) {
            
        } catch (NumberIsTooSmallException e) {
            
        }
    }
    
    @Test
    public void testSolveWithMaxEval() {
        try {
            BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
            // Test case 1
            solver.solve(100, new UnivariateFunction() {
                public double value(double x) {
                    return x * x - 4;
                }
            }, -2, 2, 0, AllowedSolution.ANY_SIDE);
            
        } catch (NumberFormatException e) {
            
        } catch (NoBracketingException e) {
            
        } catch (NumberIsTooSmallException e) {
            
        }
    }
}
