import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class BaseSecantSolverTest {

    private BaseSecantSolver solver;

    @Before
    public void setup() {
        solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.ILLINOIS);
    }

    @Test
    public void testSolve_WithValidFunction_ReturnsRoot() {
        // Arrange
        UnivariateRealFunction function = x -> Math.pow(x, 2) - 4;
        double min = -10;
        double max = 10;
        double expectedResult = 2.0;

        // Act
        double result = solver.solve(100, function, min, max);

        // Assert
        Assert.assertEquals(expectedResult, result, 1e-6);
    }

    @Test
    public void testSolve_MinAndMaxAreRoots_ReturnsMin() {
        // Arrange
        UnivariateRealFunction function = x -> Math.pow(x, 2);
        double min = -2;
        double max = 2;
        double expectedResult = -2.0;

        // Act
        double result = solver.solve(100, function, min, max);

        // Assert
        Assert.assertEquals(expectedResult, result, 1e-6);
    }

    @Test
    public void testSolve_WithInvalidFunction_ThrowsMathInternalError() {
        // Arrange
        UnivariateRealFunction function = x -> 1 / x;
        double min = -1;
        double max = 0;

        // Act & Assert
        try {
            solver.solve(100, function, min, max);
            Assert.fail("Expected MathInternalError to be thrown.");
        } catch (MathInternalError e) {
            // Test passed
        }
    }

    @After
    public void teardown() {
        solver = null;
    }

}