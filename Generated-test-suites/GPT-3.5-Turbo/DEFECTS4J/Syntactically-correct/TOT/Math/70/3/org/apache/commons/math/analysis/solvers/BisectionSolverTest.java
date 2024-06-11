
import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.MaxIterationsExceededException;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.analysis.solvers.BisectionSolver;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class BisectionSolverTest {
    
    private BisectionSolver solver;
    
    @Before
    public void setUp() {
        solver = new BisectionSolver();
    }
    
    @Test
    public void testSolve_WhenRootExists_ReturnsCorrectResult() throws FunctionEvaluationException, MaxIterationsExceededException {
        // Arrange
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) throws FunctionEvaluationException {
                return x - 1;
            }
        };
        
        double min = 0.0;
        double max = 2.0;
        double expectedRoot = 1.0;
        
        // Act
        double result = solver.solve(f, min, max);
        
        // Assert
        assertEquals(expectedRoot, result, 1E-6);
    }
    
    @Test
    public void testSolve_WhenRootDoesNotExist_ThrowsMaxIterationsExceededException() throws FunctionEvaluationException {
        // Arrange
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) throws FunctionEvaluationException {
                return x * x + 1;
            }
        };
        
        double min = 0.0;
        double max = 1.0;
        
        try {
            // Act
            solver.solve(f, min, max);
            
            // Assert
            fail("Expected MaxIterationsExceededException");
        } catch (MaxIterationsExceededException e) {
            // Pass
        }
    }
}
