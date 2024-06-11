
import org.junit.Assert;
import org.junit.Test;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.ConvergenceException;
import org.apache.commons.math.analysis.solvers.UnivariateRealSolverUtils;

public class UnivariateRealSolverUtilsTest {
    private static final double EPSILON = 1e-15;

    @Test
    public void solve_default() throws FunctionEvaluationException, ConvergenceException {
        // Arrange
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                return x * x - 4;
            }
        };
        double x0 = 0;
        double x1 = 3;
        
        // Act
        double result = UnivariateRealSolverUtils.solve(f, x0, x1);
        
        // Assert
        Assert.assertEquals(2, result, EPSILON);
    }
    
    @Test
    public void solve_withAccuracy() throws FunctionEvaluationException, ConvergenceException {
        // Arrange
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                return x * x - 4;
            }
        };
        double x0 = 0;
        double x1 = 3;
        double absoluteAccuracy = 0.01;
        
        // Act
        double result = UnivariateRealSolverUtils.solve(f, x0, x1, absoluteAccuracy);
        
        // Assert
        Assert.assertEquals(2, result, EPSILON);
    }
    
    @Test
    public void bracket() throws FunctionEvaluationException, ConvergenceException {
        // Arrange
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                return x * x - 4;
            }
        };
        double initial = 2;
        double lowerBound = 0;
        double upperBound = 3;
        
        // Act
        double[] result = UnivariateRealSolverUtils.bracket(f, initial, lowerBound, upperBound);
        
        // Assert
        Assert.assertEquals(1, result[0], EPSILON);
        Assert.assertEquals(3, result[1], EPSILON);
    }
    
    @Test(expected = ConvergenceException.class)
    public void bracket_noBracket() throws FunctionEvaluationException, ConvergenceException {
        // Arrange
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                return x * x + 4;
            }
        };
        double initial = 2;
        double lowerBound = 0;
        double upperBound = 3;
        
        // Act
        UnivariateRealSolverUtils.bracket(f, initial, lowerBound, upperBound);
    }
    
    @Test
    public void midpoint() {
        // Arrange
        double a = 1;
        double b = 3;
        
        // Act
        double result = UnivariateRealSolverUtils.midpoint(a, b);
        
        // Assert
        Assert.assertEquals(2, result, EPSILON);
    }
}
