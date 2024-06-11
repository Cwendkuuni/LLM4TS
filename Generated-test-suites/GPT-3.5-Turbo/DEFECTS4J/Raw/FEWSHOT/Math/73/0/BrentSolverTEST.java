import org.junit.Assert;
import org.junit.Test;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.functions.scalar.ComputableFunction;
import org.spaceroots.mantissa.functions.FunctionException;

public class BrentSolverTest {
    
    @Test
    public void testFindRoot() throws FunctionException {
        // Set up
        BrentSolver solver = new BrentSolver();
        ComputableFunction function = new ComputableFunction() {
            public double valueAt(double x) throws FunctionException {
                return x * x - 4;
            }
        };
        double x0 = 1.0;
        double f0 = function.valueAt(x0);
        double x1 = 4.0;
        double f1 = function.valueAt(x1);
        
        // Test
        boolean result = solver.findRoot(function, new ConvergenceChecker(), 100, x0, f0, x1, f1);
        
        // Verify
        Assert.assertTrue(result);
        Assert.assertEquals(2.0, solver.getRoot(), 0.0001);
    }
}