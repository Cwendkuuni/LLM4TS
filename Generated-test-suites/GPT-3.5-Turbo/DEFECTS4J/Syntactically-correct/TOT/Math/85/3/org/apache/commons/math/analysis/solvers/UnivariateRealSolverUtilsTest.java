
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.ConvergenceException;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.analysis.solvers.UnivariateRealSolverUtils;
import org.junit.Test;

public class UnivariateRealSolverUtilsTest {
    
    private static final double EPSILON = 1e-10;
    
    @Test
    public void testSolve_WithValidInput_ReturnsCorrectResult() throws ConvergenceException, FunctionEvaluationException {
        // Arrange
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) throws FunctionEvaluationException {
                return x * x - 4;
            }
        };
        double x0 = 0;
        double x1 = 1;
        
        // Act
        double result = UnivariateRealSolverUtils.solve(f, x0, x1);
        
        // Assert
        assertEquals(2, result, EPSILON);
    }
    
    @Test
    public void testSolve_WithInvalidInput_ThrowsIllegalArgumentException() {
        // Arrange
        UnivariateRealFunction f = null;
        double x0 = 0;
        double x1 = 1;
        
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            UnivariateRealSolverUtils.solve(f, x0, x1);
        });
    }
    
    @Test
    public void testBracket_WithValidInput_ReturnsCorrectResult() throws ConvergenceException, FunctionEvaluationException {
        // Arrange
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) throws FunctionEvaluationException {
                return x * x - 4;
            }
        };
        double initial = 0.5;
        double lowerBound = -5;
        double upperBound = 5;
        int maximumIterations = 100;
        
        // Act
        double[] result = UnivariateRealSolverUtils.bracket(f, initial, lowerBound, upperBound, maximumIterations);
        
        // Assert
        assertEquals(-2, result[0], EPSILON);
        assertEquals(2, result[1], EPSILON);
    }
    
    @Test
    public void testBracket_WithInvalidInput_ThrowsIllegalArgumentException() {
        // Arrange
        UnivariateRealFunction f = null;
        double initial = 0;
        double lowerBound = 0;
        double upperBound = 1;
        int maximumIterations = 100;
        
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            UnivariateRealSolverUtils.bracket(f, initial, lowerBound, upperBound, maximumIterations);
        });
    }
    
    @Test
    public void testMidpoint_ReturnsCorrectResult() {
        // Arrange
        double a = 0;
        double b = 2;
        
        // Act
        double result = UnivariateRealSolverUtils.midpoint(a, b);
        
        // Assert
        assertEquals(1, result, EPSILON);
    }
}

