
import org.junit.Test;
import static org.junit.Assert.*;

public class BisectionSolverTest {

    @Test
    public void testSolve_midpointBetweenMinAndMax() {
        // Arrange
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x - 5;
            }
        };
        BisectionSolver solver = new BisectionSolver(f);
        double min = 4;
        double max = 6;

        // Act
        double result = solver.solve(min, max);

        // Assert
        assertEquals(5.0, result, 0.00001);
    }

    @Test
    public void testSolve_minEqualsMax() {
        // Arrange
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x - 5;
            }
        };
        BisectionSolver solver = new BisectionSolver(f);
        double min = 5;
        double max = 5;

        // Act
        double result = solver.solve(min, max);

        // Assert
        assertEquals(5.0, result, 0.00001);
    }

    @Test(expected = MaxIterationsExceededException.class)
    public void testSolve_maxIterationsExceeded() {
        // Arrange
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x - 5;
            }
        };
        BisectionSolver solver = new BisectionSolver(f);
        double min = 0;
        double max = 10;
        int maximalIterationCount = 5;
        solver.setMaximalIterationCount(maximalIterationCount);

        // Act
        double result = solver.solve(min, max);

        // Assert (Exception expected)
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSolve_invalidInterval() {
        // Arrange
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x - 5;
            }
        };
        BisectionSolver solver = new BisectionSolver(f);
        double min = 10;
        double max = 0;

        // Act
        double result = solver.solve(min, max);

        // Assert (Exception expected)
    }

}
