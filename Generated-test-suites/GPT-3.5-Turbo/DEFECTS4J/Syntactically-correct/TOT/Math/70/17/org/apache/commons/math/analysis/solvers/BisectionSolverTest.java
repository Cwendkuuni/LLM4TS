import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.analysis.QuinticFunction;
import org.apache.commons.math.analysis.QuadraticFunction;
import org.apache.commons.math.MaxIterationsExceededException;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class BisectionSolverTest {

    private BisectionSolver bisectionSolver;
    private UnivariateRealFunction function;
    
    @Before
    public void setUp() {
        bisectionSolver = new BisectionSolver();
    }
    
    // Test cases for solve(double min, double max)
    
    @Test
    public void testSolve_MinGreaterThanMax() {
        double min = 3.0;
        double max = 2.0;
        
        try {
            bisectionSolver.solve(min, max);
            fail("MaxIterationsExceededException should be thrown");
        } catch (MaxIterationsExceededException e) {
            // Expected exception
        }
    }
    
    @Test
    public void testSolve_MinEqualsMax() {
        double min = 2.0;
        double max = 2.0;
        
        try {
            double result = bisectionSolver.solve(min, max);
            assertEquals(min, result, 0.0);
        } catch (MaxIterationsExceededException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }
    
    @Test
    public void testSolve_QuadraticFunction() {
        double min = -10.0;
        double max = 10.0;
        function = new QuadraticFunction(1, 0, -1);
        
        try {
            double result = bisectionSolver.solve(function, min, max);
            assertEquals(1.0, result, 0.000001);
        } catch (MaxIterationsExceededException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }
    
    // Test cases for solve(double min, double max, double initial)
    
    @Test
    public void testSolve_InitialApproximation() {
        double min = -10.0;
        double max = 10.0;
        double initial = 5.0;
        function = new QuinticFunction(1, 0, 0, 0, 0, -1);
        
        try {
            double result = bisectionSolver.solve(function, min, max, initial);
            assertEquals(0.905208, result, 0.000001);
        } catch (MaxIterationsExceededException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }
    
    @Test
    public void testSolve_InitialOutsideInterval() {
        double min = 0.0;
        double max = 1.0;
        double initial = 2.0;
        function = new QuadraticFunction(1, -2, -1);
        
        try {
            bisectionSolver.solve(function, min, max, initial);
            fail("MaxIterationsExceededException should be thrown");
        } catch (MaxIterationsExceededException e) {
            // Expected exception
        }
    }

}