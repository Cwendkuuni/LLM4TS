import org.junit.Test;
import static org.junit.Assert.*;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.functions.scalar.ComputableFunction;
import org.spaceroots.mantissa.roots.ConvergenceChecker;

public class BrentSolverTest {

    @Test
    public void testFindRoot() {
        BrentSolver solver = new BrentSolver();
        double root = solver.getRoot();
        assertEquals(Double.NaN, root, 0.0);

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
                    return CONTINUE;
                }
            }
        };

        boolean rootFound = solver.findRoot(function, checker, 100, 1.0, -3.0, 2.0, 4.0);
        assertTrue(rootFound);

        double expectedRoot = 2.0;
        assertEquals(expectedRoot, solver.getRoot(), 1e-6);
    }

    @Test
    public void testGetRoot() {
        BrentSolver solver = new BrentSolver();
        double root = solver.getRoot();
        assertEquals(Double.NaN, root, 0.0);
    }

}