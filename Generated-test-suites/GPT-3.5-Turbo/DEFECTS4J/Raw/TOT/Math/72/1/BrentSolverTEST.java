import org.junit.Test;
import org.spaceroots.mantissa.functions.FunctionException;
import org.spaceroots.mantissa.functions.scalar.ComputableFunction;
import static org.junit.Assert.*;

public class BrentSolverTest {

    @Test
    public void testFindRoot() throws FunctionException {
        // Test case 1
        ComputableFunction function1 = new ComputableFunction() {
            @Override
            public double valueAt(double x) throws FunctionException {
                return x * x - 4;
            }
        };
        ConvergenceChecker checker1 = new ConvergenceChecker() {
            @Override
            public int converged(double xLow, double fLow, double xHigh, double fHigh) {
                if (Math.abs(fLow) < Math.abs(fHigh)) {
                    return ConvergenceChecker.LOW;
                } else if (Math.abs(fHigh) < Math.abs(fLow)) {
                    return ConvergenceChecker.HIGH;
                } else {
                    return ConvergenceChecker.NEITHER;
                }
            }
        };
        int maxIter1 = 100;
        double x01 = 1;
        double f01 = -3;
        double x11 = 3;
        double f11 = 5;
        BrentSolver solver1 = new BrentSolver();
        boolean rootFound1 = solver1.findRoot(function1, checker1, maxIter1, x01, f01, x11, f11);
        assertTrue(rootFound1);
        assertEquals(2, solver1.getRoot(), 1e-10);

        // Test case 2
        ComputableFunction function2 = new ComputableFunction() {
            @Override
            public double valueAt(double x) throws FunctionException {
                return Math.sin(x);
            }
        };
        ConvergenceChecker checker2 = new ConvergenceChecker() {
            @Override
            public int converged(double xLow, double fLow, double xHigh, double fHigh) {
                if (Math.abs(fLow) < Math.abs(fHigh)) {
                    return ConvergenceChecker.LOW;
                } else if (Math.abs(fHigh) < Math.abs(fLow)) {
                    return ConvergenceChecker.HIGH;
                } else {
                    return ConvergenceChecker.NEITHER;
                }
            }
        };
        int maxIter2 = 100;
        double x02 = 0;
        double f02 = 0;
        double x12 = 3;
        double f12 = 1;
        BrentSolver solver2 = new BrentSolver();
        boolean rootFound2 = solver2.findRoot(function2, checker2, maxIter2, x02, f02, x12, f12);
        assertTrue(rootFound2);
        assertEquals(1.5707963267948966, solver2.getRoot(), 1e-10);

        // Test case 3
        ComputableFunction function3 = new ComputableFunction() {
            @Override
            public double valueAt(double x) throws FunctionException {
                return x * x - 9;
            }
        };
        ConvergenceChecker checker3 = new ConvergenceChecker() {
            @Override
            public int converged(double xLow, double fLow, double xHigh, double fHigh) {
                if (Math.abs(fLow) < Math.abs(fHigh)) {
                    return ConvergenceChecker.LOW;
                } else if (Math.abs(fHigh) < Math.abs(fLow)) {
                    return ConvergenceChecker.HIGH;
                } else {
                    return ConvergenceChecker.NEITHER;
                }
            }
        };
        int maxIter3 = 100;
        double x03 = -3;
        double f03 = 0;
        double x13 = 3;
        double f13 = 0;
        BrentSolver solver3 = new BrentSolver();
        boolean rootFound3 = solver3.findRoot(function3, checker3, maxIter3, x03, f03, x13, f13);
        assertTrue(rootFound3);
        assertEquals(3, solver3.getRoot(), 1e-10);
    }

}