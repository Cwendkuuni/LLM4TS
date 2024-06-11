import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.ComputableFunction;
import org.spaceroots.mantissa.functions.scalar.HarmonicOscillator;
import org.spaceroots.mantissa.functions.FunctionException;

public class BrentSolverTest {
    
    private static BrentSolver solver;
    
    @BeforeClass
    public static void setUp() {
        solver = new BrentSolver();
    }
    
    @Test
    public void testFindRoot_typicalCase() throws FunctionException {
        // Typical case: Harmonic Oscillator with known root between -10 and 10
        ComputableFunction function = new HarmonicOscillator(1.0, 0.0);
        ConvergenceChecker checker = new ConvergenceChecker();
        double x0 = -10.0;
        double f0 = function.valueAt(x0);
        double x1 = 10.0;
        double f1 = function.valueAt(x1);

        // Expected result near root zero
        boolean expected = true;
        double expectedRoot = -0.0;
        
        // Perform test
        boolean result = solver.findRoot(function, checker, 100, x0, f0, x1, f1);
        
        // Verify result
        Assert.assertEquals(expected, result);
        Assert.assertEquals(expectedRoot, solver.getRoot(), 1e-9);
    }
    
    @Test
    public void testFindRoot_edgeCase() throws FunctionException {
        // Edge case: Harmonic Oscillator with known root on boundary
        ComputableFunction function = new HarmonicOscillator(1.0, 0.0);
        ConvergenceChecker checker = new ConvergenceChecker();
        double x0 = -10.0;
        double f0 = function.valueAt(x0);
        double x1 = 0.0;
        double f1 = function.valueAt(x1);

        // Expected result on boundary
        boolean expected = true;
        double expectedRoot = 0.0;
        
        // Perform test
        boolean result = solver.findRoot(function, checker, 100, x0, f0, x1, f1);
        
        // Verify result
        Assert.assertEquals(expected, result);
        Assert.assertEquals(expectedRoot, solver.getRoot(), 1e-9);
    }
    
    @Test
    public void testFindRoot_errorScenario() throws FunctionException {
        // Error scenario: Quadratic function without roots
        ComputableFunction function = new QuadraticFunction(1.0, 0.0, 1.0);
        ConvergenceChecker checker = new ConvergenceChecker();
        double x0 = -10.0;
        double f0 = function.valueAt(x0);
        double x1 = 10.0;
        double f1 = function.valueAt(x1);

        // Expected result with no root found
        boolean expected = false;
        
        // Perform test
        boolean result = solver.findRoot(function, checker, 100, x0, f0, x1, f1);
        
        // Verify result
        Assert.assertEquals(expected, result);
    }
}