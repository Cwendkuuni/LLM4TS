import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class BrentSolverTest {

    BrentSolver solver;
    double epsilon = Math.pow(2.0, -52);

    @Before
    public void setup() {
        solver = new BrentSolver();
    }

    // Test findRoot method

    // Typical use case: finding root in an interval
    @Test
    public void testFindRoot_typicalUseCase() {
        ComputableFunction function = new ComputableFunction() {
            public double valueAt(double x) {
                return x * x - 4;
            }
        };
        ConvergenceChecker checker = new ConvergenceChecker() {
            public int converged(double xLow, double fLow, double xHigh, double fHigh) {
                if (Math.abs(fLow) < epsilon) {
                    return ConvergenceChecker.LOW;
                } else if (Math.abs(fHigh) < epsilon) {
                    return ConvergenceChecker.HIGH;
                } else {
                    return ConvergenceChecker.NONE;
                }
            }
        };
        int maxIter = 100;
        double x0 = 0;
        double f0 = -4;
        double x1 = 4;
        double f1 = 12;

        boolean result = solver.findRoot(function, checker, maxIter, x0, f0, x1, f1);
        double root = solver.getRoot();

        assertTrue(result);
        assertEquals(2.0, root, epsilon);
    }

    // Edge case: interval contains multiple roots
    @Test
    public void testFindRoot_multipleRoots() {
        ComputableFunction function = new ComputableFunction() {
            public double valueAt(double x) {
                return x * x - 4;
            }
        };
        ConvergenceChecker checker = new ConvergenceChecker() {
            public int converged(double xLow, double fLow, double xHigh, double fHigh) {
                if (Math.abs(fLow) < epsilon) {
                    return ConvergenceChecker.LOW;
                } else if (Math.abs(fHigh) < epsilon) {
                    return ConvergenceChecker.HIGH;
                } else {
                    return ConvergenceChecker.NONE;
                }
            }
        };
        int maxIter = 100;
        double x0 = -3;
        double f0 = 5;
        double x1 = 3;
        double f1 = 5;

        boolean result = solver.findRoot(function, checker, maxIter, x0, f0, x1, f1);
        double root = solver.getRoot();

        assertTrue(result);
        assertEquals(-2.0, root, epsilon);
    }

    // Error scenario: function always returns constant
    @Test
    public void testFindRoot_constantFunction() {
        ComputableFunction function = new ComputableFunction() {
            public double valueAt(double x) {
                return 1;
            }
        };
        ConvergenceChecker checker = new ConvergenceChecker() {
            public int converged(double xLow, double fLow, double xHigh, double fHigh) {
                if (Math.abs(fLow) < epsilon) {
                    return ConvergenceChecker.LOW;
                } else if (Math.abs(fHigh) < epsilon) {
                    return ConvergenceChecker.HIGH;
                } else {
                    return ConvergenceChecker.NONE;
                }
            }
        };
        int maxIter = 100;
        double x0 = -10;
        double f0 = 1;
        double x1 = 10;
        double f1 = 1;

        boolean result = solver.findRoot(function, checker, maxIter, x0, f0, x1, f1);
        double root = solver.getRoot();

        assertFalse(result);
        assertTrue(Double.isNaN(root));
    }

    // Test getRoot method

    // Typical use case: root has been found
    @Test
    public void testGetRoot_typicalUseCase() {
        solver.getRoot = 2.0;
        double root = solver.getRoot();

        assertEquals(2.0, root, epsilon);
    }

    // Edge case: root is NaN
    @Test
    public void testGetRoot_NaNRoot() {
        solver.getRoot = Double.NaN;
        double root = solver.getRoot();

        assertTrue(Double.isNaN(root));
    }

}