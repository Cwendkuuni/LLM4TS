import org.junit.Before;
import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.ComputableFunction;
import org.spaceroots.mantissa.roots.BrentSolver;

import static org.junit.Assert.*;

public class BrentSolverTest {

    private BrentSolver solver;

    @Before
    public void setUp() {
        solver = new BrentSolver();
    }

    @Test
    public void testFindRoot_SuccessfulRootInInterval_ReturnsTrue() {
        // Arrange
        ComputableFunction function = new ComputableFunction() {
            @Override
            public double valueAt(double x) {
                return x * x - 4;
            }
        };
        int maxIter = 100;
        double x0 = -10;
        double f0 = function.valueAt(x0);
        double x1 = 10;
        double f1 = function.valueAt(x1);

        // Act
        boolean result = solver.findRoot(function, new ConvergenceCheckerMock(), maxIter, x0, f0, x1, f1);

        // Assert
        assertTrue(result);
        assertEquals(2.0, solver.getRoot(), 0.0001);
    }

    @Test
    public void testFindRoot_NoRootInRange_ReturnsFalse() {
        // Arrange
        ComputableFunction function = new ComputableFunction() {
            @Override
            public double valueAt(double x) {
                return x + 1;
            }
        };
        int maxIter = 100;
        double x0 = -10;
        double f0 = function.valueAt(x0);
        double x1 = -5;
        double f1 = function.valueAt(x1);

        // Act
        boolean result = solver.findRoot(function, new ConvergenceCheckerMock(), maxIter, x0, f0, x1, f1);

        // Assert
        assertFalse(result);
    }

    @Test
    public void testFindRoot_MaximumIterationsExceeded_ReturnsFalse() {
        // Arrange
        ComputableFunction function = new ComputableFunction() {
            @Override
            public double valueAt(double x) {
                return Math.cos(x) - x;
            }
        };
        int maxIter = 10;
        double x0 = -10;
        double f0 = function.valueAt(x0);
        double x1 = 10;
        double f1 = function.valueAt(x1);

        // Act
        boolean result = solver.findRoot(function, new ConvergenceCheckerMock(), maxIter, x0, f0, x1, f1);

        // Assert
        assertFalse(result);
    }

    @Test
    public void testGetRoot_RootNotComputed_ReturnsNaN() {
        // Act
        double root = solver.getRoot();

        // Assert
        assertTrue(Double.isNaN(root));
    }

    class ConvergenceCheckerMock implements ConvergenceChecker {
        @Override
        public int converged(double xLow, double fLow, double xHigh, double fHigh) {
            if (Math.abs(fLow) < 0.001) {
                return LOW;
            } else if (Math.abs(fHigh) < 0.001) {
                return HIGH;
            } else {
                return NEITHER;
            }
        }
    }
}