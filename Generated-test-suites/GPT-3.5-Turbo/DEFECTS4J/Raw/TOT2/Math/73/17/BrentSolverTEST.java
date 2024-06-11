import static org.junit.Assert.*;

import org.junit.Test;

public class BrentSolverTest {

    @Test
    public void testFindRoot() {
        // Test typical use case
        // Create a simple function
        ComputableFunction function = new ComputableFunction() {
            @Override
            public double valueAt(double x) {
                return x * x - 4;
            }
        };

        ConvergenceChecker checker = new ConvergenceChecker() {
            @Override
            public int converged(double xLow, double fLow, double xHigh, double fHigh) {
                if (Math.abs(fLow) < 1e-6) {
                    return LOW;
                } else if (Math.abs(fHigh) < 1e-6) {
                    return HIGH;
                } else {
                    return NONE;
                }
            }
        };

        BrentSolver solver = new BrentSolver();
        boolean rootFound = solver.findRoot(function, checker, 100, 0, function.valueAt(0), 3, function.valueAt(3));

        assertTrue(rootFound);
        assertEquals(2.0, solver.getRoot(), 1e-6);
    }

    // Add more test cases for other public methods and edge cases

}