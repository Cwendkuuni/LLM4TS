import static org.junit.Assert.*;
import org.junit.*;

public class BaseSecantSolverTest {

    private BaseSecantSolver solver;

    @Before
    public void setup() {
        solver = new DummySecantSolver();
    }

    @Test
    public void testSolve_withValidInputs_returnsCorrectResult() {
        // Arrange
        double expected = 2.0;
        UnivariateRealFunction f = new DummyFunction();
        double min = 1.0;
        double max = 3.0;
        double startValue = 1.5;

        // Act
        double actual = solver.solve(100, f, min, max, startValue);

        // Assert
        assertEquals(expected, actual, 1e-6);
    }

    @Test
    public void testSolve_withInvalidInputs_throwsIllegalArgumentException() {
        // Arrange
        UnivariateRealFunction f = new DummyFunction();
        double min = 3.0;
        double max = 1.0;
        double startValue = 1.5;

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            solver.solve(100, f, min, max, startValue);
        });
    }

    private class DummySecantSolver extends BaseSecantSolver {

        public DummySecantSolver() {
            super(1e-6, Method.REGULA_FALSI);
        }

        @Override
        protected double computeObjectiveValue(double x) {
            return x * x - 4;
        }

        @Override
        protected void verifyBracketing(double x0, double x1) {
            if (x0 >= x1) {
                throw new IllegalArgumentException("Invalid bracketing");
            }
        }
    }

    private class DummyFunction implements UnivariateRealFunction {

        @Override
        public double value(double x) {
            return x * x - 4;
        }
    }
}