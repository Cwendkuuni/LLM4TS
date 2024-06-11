import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class BisectionSolverTest {

    private BisectionSolver solver;

    @Before
    public void setUp() {
        solver = new BisectionSolver();
    }

    @Test
    public void testSolve_WhenValidFunctionAndInRange_ReturnsCorrectResult() {
        // Arrange
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x - 5;
            }
        };
        double min = 0;
        double max = 10;
        double expected = 5;

        // Act
        double result = solver.solve(f, min, max);

        // Assert
        assertEquals(expected, result, 1E-6);
    }

    @Test
    public void testSolve_WhenValidFunctionAndOutOfRange_ThrowsMaxIterationsExceededException() {
        // Arrange
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x - 5;
            }
        };
        double min = 0;
        double max = 1;
        
        // Assert
        assertThrows(MaxIterationsExceededException.class, () -> {
            // Act
            solver.solve(f, min, max);
        });
    }

    @Test
    public void testSolve_WhenValidFunctionAndInitialValue_UsesInitialValue() {
        // Arrange
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x - 5;
            }
        };
        double min = 0;
        double max = 10;
        double initial = 7;
        double expected = 5;

        // Act
        double result = solver.solve(f, min, max, initial);

        // Assert
        assertEquals(expected, result, 1E-6);
    }

    @Test
    public void testSolve_WhenInvalidFunction_ThrowsFunctionEvaluationException() {
        // Arrange
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                throw new FunctionEvaluationException();
            }
        };
        double min = 0;
        double max = 10;
        
        // Assert
        assertThrows(FunctionEvaluationException.class, () -> {
            // Act
            solver.solve(f, min, max);
        });
    }

    @Test
    public void testSolve_WhenMaxIterationsExceeded_ThrowsMaxIterationsExceededException() {
        // Arrange
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x;
            }
        };
        double min = 0;
        double max = 10;
        
        // Assert
        assertThrows(MaxIterationsExceededException.class, () -> {
            // Act
            solver.solve(f, min, max);
        });
    }

    @Test
    public void testSolve_WhenMinEqualsMax_ReturnsMin() {
        // Arrange
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x - 5;
            }
        };
        double min = 5;
        double max = 5;
        double expected = 5;

        // Act
        double result = solver.solve(f, min, max);

        // Assert
        assertEquals(expected, result, 1E-6);
    }

    @Test
    public void testSolve_WhenFunctionDoesNotBracketRoot_ThrowsMaxIterationsExceededException() {
        // Arrange
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x - 5;
            }
        };
        double min = 0;
        double max = 2;

        // Assert
        assertThrows(MaxIterationsExceededException.class, () -> {
            // Act
            solver.solve(f, min, max);
        });
    }

}