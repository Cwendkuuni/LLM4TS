import org.junit.Test;

public class BrentSolverTest {

    @Test
    public void testFindRoot() {
        BrentSolver solver = new BrentSolver();
        ComputableFunction function = new ComputableFunction() {
            public double valueAt(double x) {
                return x * x - 4;
            }
        };
        ConvergenceChecker checker = new ConvergenceChecker() {
            public int converged(double xLow, double fLow, double xHigh, double fHigh) {
                if (fLow < 0 && fHigh > 0) {
                    return ConvergenceChecker.HIGH;
                } else if (fLow > 0 && fHigh < 0) {
                    return ConvergenceChecker.LOW;
                } else {
                    return ConvergenceChecker.NONE;
                }
            }
        };

        double x0 = 1.0;
        double f0 = function.valueAt(x0);
        double x1 = 3.0;
        double f1 = function.valueAt(x1);

        boolean rootFound = solver.findRoot(function, checker, 100, x0, f0, x1, f1);
        double root = solver.getRoot();

        assert(rootFound);
        assert(Math.abs(root - 2.0) < BrentSolver.epsilon);
    }

}