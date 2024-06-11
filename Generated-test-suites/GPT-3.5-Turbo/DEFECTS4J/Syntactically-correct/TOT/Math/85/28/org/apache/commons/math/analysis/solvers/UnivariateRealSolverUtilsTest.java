
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.analysis.solvers.ConvergenceException;
import org.apache.commons.math.analysis.solvers.UnivariateRealSolver;
import org.apache.commons.math.analysis.solvers.UnivariateRealSolverFactory;
import org.junit.Assert;
import org.junit.Test;

public class UnivariateRealSolverUtilsTest {
    @Test
    public void testSolve_WithValidParameters_ShouldReturnZeroValue() throws ConvergenceException, FunctionEvaluationException {
        // Arrange
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x - 1;
            }
        };
        double x0 = 0;
        double x1 = 2;
        
        // Act
        double result = UnivariateRealSolverUtils.solve(f, x0, x1);
        
        // Assert
        Assert.assertEquals(1, result, 0);
    }
    
    @Test(expected = ConvergenceException.class)
    public void testSolve_WithInvalidInterval_ShouldThrowConvergenceException() throws ConvergenceException, FunctionEvaluationException {
        // Arrange
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x - 1;
            }
        };
        double x0 = 2;
        double x1 = 0;
        
        // Act
        UnivariateRealSolverUtils.solve(f, x0, x1);
    }
    
    @Test
    public void testSolve_WithAbsoluteAccuracy_ShouldReturnZeroValue() throws ConvergenceException, FunctionEvaluationException {
        // Arrange
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x - 1;
            }
        };
        double x0 = 0;
        double x1 = 2;
        double absoluteAccuracy = 0.001;
        
        // Act
        double result = UnivariateRealSolverUtils.solve(f, x0, x1, absoluteAccuracy);
        
        // Assert
        Assert.assertEquals(1, result, absoluteAccuracy);
    }
    
    @Test
    public void testBracket_WithValidParameters_ShouldReturnValidBracket() throws ConvergenceException, FunctionEvaluationException {
        // Arrange
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2) - 1;
            }
        };
        double initial = 1.5;
        double lowerBound = -2;
        double upperBound = 2;
        int maximumIterations = 100;
        
        // Act
        double[] result = UnivariateRealSolverUtils.bracket(f, initial, lowerBound, upperBound, maximumIterations);
        
        // Assert
        Assert.assertTrue(result[0] < result[1]);
        Assert.assertTrue(result[0] >= lowerBound);
        Assert.assertTrue(result[1] <= upperBound);
        Assert.assertTrue(f.value(result[0]) * f.value(result[1]) <= 0);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testBracket_WithInvalidFunction_ShouldThrowIllegalArgumentException() throws ConvergenceException, FunctionEvaluationException {
        // Arrange
        UnivariateRealFunction f = null;
        double initial = 1.5;
        double lowerBound = -2;
        double upperBound = 2;
        int maximumIterations = 100;
        
        // Act
        UnivariateRealSolverUtils.bracket(f, initial, lowerBound, upperBound, maximumIterations);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testBracket_WithInvalidMaximumIterations_ShouldThrowIllegalArgumentException() throws ConvergenceException, FunctionEvaluationException {
        // Arrange
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2) - 1;
            }
        };
        double initial = 1.5;
        double lowerBound = -2;
        double upperBound = 2;
        int maximumIterations = -1;
        
        // Act
        UnivariateRealSolverUtils.bracket(f, initial, lowerBound, upperBound, maximumIterations);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testBracket_WithInvalidInterval_ShouldThrowIllegalArgumentException() throws ConvergenceException, FunctionEvaluationException {
        // Arrange
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2) - 1;
            }
        };
        double initial = 1.5;
        double lowerBound = 2;
        double upperBound = -2;
        int maximumIterations = 100;
        
        // Act
        UnivariateRealSolverUtils.bracket(f, initial, lowerBound, upperBound, maximumIterations);
    }
    
    @Test(expected = ConvergenceException.class)
    public void testBracket_WhenNoRootExists_ShouldThrowConvergenceException() throws ConvergenceException, FunctionEvaluationException {
        // Arrange
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.abs(x) - 2;
            }
        };
        double initial = 1.5;
        double lowerBound = -2;
        double upperBound = 2;
        int maximumIterations = 100;
        
        // Act
        UnivariateRealSolverUtils.bracket(f, initial, lowerBound, upperBound, maximumIterations);
    }
    
    @Test
    public void testMidpoint_ShouldReturnMidpointValue() {
        // Arrange
        double a = 0;
        double b = 2;
        
        // Act
        double result = UnivariateRealSolverUtils.midpoint(a, b);
        
        // Assert
        Assert.assertEquals(1, result, 0);
    }
}
