import org.junit.Test;
import static org.junit.Assert.*;

public class BrentSolverTest {

    @Test
    public void testFindRoot() {
        // Typical use case
        BrentSolver solver = new BrentSolver();
        
        double[] coefficients = {1, -3, 1}; // x^2 - 3x + 1
        ComputableFunction function = new PolynomialFunction(coefficients);
        
        ConvergenceChecker checker = new DefaultConvergenceChecker();
        
        double x0 = 0.0;
        double f0 = function.valueAt(x0);
        double x1 = 2.0;
        double f1 = function.valueAt(x1);
        
        int maxIter = 100;
        
        assertTrue(solver.findRoot(function, checker, maxIter, x0, f0, x1, f1));
        assertEquals(2.0, solver.getRoot(), 1e-10);
        
        // Edge case: Function with no root in the interval
        double[] coefficients2 = {1, 1, 1}; // x^2 + x + 1
        ComputableFunction function2 = new PolynomialFunction(coefficients2);
        
        double x0_2 = 0.0;
        double f0_2 = function2.valueAt(x0_2);
        double x1_2 = 1.0;
        double f1_2 = function2.valueAt(x1_2);
        
        assertFalse(solver.findRoot(function2, checker, maxIter, x0_2, f0_2, x1_2, f1_2));
        
        // Exception handling: FunctionException
        double[] coefficients3 = {1}; // x
        ComputableFunction function3 = new PolynomialFunction(coefficients3);
        
        double x0_3 = 0.0;
        double f0_3 = function3.valueAt(x0_3);
        
        assertThrows(FunctionException.class, () -> solver.findRoot(function3, checker, maxIter, x0_3, f0_3, x1, f1));
    }

    @Test
    public void testGetRoot() {
        BrentSolver solver = new BrentSolver();
        
        double[] coefficients = {1, -3, 1}; // x^2 - 3x + 1
        ComputableFunction function = new PolynomialFunction(coefficients);
        
        ConvergenceChecker checker = new DefaultConvergenceChecker();
        
        double x0 = 0.0;
        double f0 = function.valueAt(x0);
        double x1 = 2.0;
        double f1 = function.valueAt(x1);
        
        int maxIter = 100;
        
        assertTrue(solver.findRoot(function, checker, maxIter, x0, f0, x1, f1));
        assertEquals(2.0, solver.getRoot(), 1e-10);
    }

}