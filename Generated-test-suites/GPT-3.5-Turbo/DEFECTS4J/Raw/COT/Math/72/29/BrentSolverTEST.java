import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.ComputableFunction;
import org.spaceroots.mantissa.functions.FunctionException;
import org.spaceroots.mantissa.roots.BrentSolver;

public class BrentSolverTest {

    @Test
    public void testFindRoot() {
        // Test case 1: Function has a root in the given interval
        BrentSolver solver = new BrentSolver();
        ComputableFunction function = new ComputableFunction() {
            public double valueAt(double x) {
                return x*x - 4.0;
            }
        };
        ConvergenceChecker checker = new ConvergenceChecker() {
            public int converged(double xLow, double fLow, double xHigh, double fHigh) {
                if (fLow*fHigh < 0) {
                    return LOW;
                } else if (fLow*fHigh > 0) {
                    return HIGH;
                } else {
                    return NO_CONVERGENCE;
                }
            }
        };
        double x0 = 1.0;
        double f0 = function.valueAt(x0);
        double x1 = 3.0;
        double f1 = function.valueAt(x1);
        int maxIter = 100;
        boolean rootFound = false;
        try {
            rootFound = solver.findRoot(function, checker, maxIter, x0, f0, x1, f1);
        } catch (FunctionException e) {
            e.printStackTrace();
        }
        double root = solver.getRoot();
        assert(rootFound);
        assert(Math.abs(root - 2.0) < 1e-6);

        // Test case 2: Function has no root in the given interval
        BrentSolver solver2 = new BrentSolver();
        ComputableFunction function2 = new ComputableFunction() {
            public double valueAt(double x) {
                return x*x - 1.0;
            }
        };
        ConvergenceChecker checker2 = new ConvergenceChecker() {
            public int converged(double xLow, double fLow, double xHigh, double fHigh) {
                if (fLow*fHigh < 0) {
                    return LOW;
                } else if (fLow*fHigh > 0) {
                    return HIGH;
                } else {
                    return NO_CONVERGENCE;
                }
            }
        };
        double x02 = -1.0;
        double f02 = function2.valueAt(x02);
        double x12 = 0.5;
        double f12 = function2.valueAt(x12);
        int maxIter2 = 100;
        boolean rootFound2 = false;
        try {
            rootFound2 = solver2.findRoot(function, checker, maxIter, x02, f02, x12, f12);
        } catch (FunctionException e) {
            e.printStackTrace();
        }
        double root2 = solver2.getRoot();
        assert(!rootFound2);
        assert(Double.isNaN(root2));
    }

    @Test
    public void testGetRoot() {
        // Test case 1: root has been found
        BrentSolver solver = new BrentSolver();
        solver.root = 2.0;
        assert(solver.getRoot() == 2.0);

        // Test case 2: root has not been found
        BrentSolver solver2 = new BrentSolver();
        assert(Double.isNaN(solver2.getRoot()));
    }

}