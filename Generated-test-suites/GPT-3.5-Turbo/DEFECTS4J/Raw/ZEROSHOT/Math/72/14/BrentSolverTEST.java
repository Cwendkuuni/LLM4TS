import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.spaceroots.mantissa.functions.FunctionException;
import org.spaceroots.mantissa.functions.scalar.ComputableFunction;
import org.spaceroots.mantissa.roots.BrentSolver;

public class BrentSolverTest {

    private BrentSolver solver;

    @Before
    public void setUp() {
        solver = new BrentSolver();
    }

    @Test
    public void testFindRoot() throws FunctionException {
        // Test case 1
        ComputableFunction function1 = new ComputableFunction() {
            public double valueAt(double x) {
                return x - 2;
            }
        };
        
        double x0 = 1.0;
        double f0 = -1.0;
        double x1 = 3.0;
        double f1 = 1.0;
        int maxIter = 100;
        boolean rootFound = solver.findRoot(function1, new ConvergenceChecker(), maxIter, x0, f0, x1, f1);
        double root = solver.getRoot();
        Assert.assertTrue(rootFound);
        Assert.assertEquals(2.0, root, 1e-5);
        
        // Test case 2
        ComputableFunction function2 = new ComputableFunction() {
            public double valueAt(double x) {
                return Math.sin(x) - 0.5;
            }
        };
        
        x0 = 0.0;
        f0 = -0.5;
        x1 = 2.0;
        f1 = 0.132;
        maxIter = 100;
        rootFound = solver.findRoot(function2, new ConvergenceChecker(), maxIter, x0, f0, x1, f1);
        root = solver.getRoot();
        Assert.assertTrue(rootFound);
        Assert.assertEquals(0.5236, root, 1e-5);
        
        // Add more test cases as needed
    }
}