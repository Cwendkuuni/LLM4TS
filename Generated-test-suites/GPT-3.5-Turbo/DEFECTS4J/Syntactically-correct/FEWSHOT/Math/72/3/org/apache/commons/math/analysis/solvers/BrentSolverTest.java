import org.spaceroots.mantissa.functions.scalar.ComputableFunction;
import org.spaceroots.mantissa.functions.FunctionException;
import org.junit.Test;

import static org.junit.Assert.*;

public class BrentSolverTest {

    @Test
    public void testFindRoot() throws FunctionException {
        BrentSolver solver = new BrentSolver();

        // Test case 1
        ComputableFunction function1 = new ComputableFunction() {
            public double valueAt(double x) {
                return x * x - 4;
            }
        };
        ConvergenceChecker checker1 = new ConvergenceChecker() {
            public int converged(double x1, double y1, double x2, double y2) {
                return Math.abs(x1 - x2) < 1e-10 ? ConvergenceChecker.LOW : ConvergenceChecker.NOT_CONVERGED;
            }
        };
        double x01 = 1.0;
        double f01 = function1.valueAt(x01);
        double x11 = 3.0;
        double f11 = function1.valueAt(x11);
        boolean rootFound1 = solver.findRoot(function1, checker1, 100, x01, f01, x11, f11);
        assertTrue(rootFound1);
        assertEquals(2.0, solver.getRoot(), 1e-10);

        // Test case 2
        ComputableFunction function2 = new ComputableFunction() {
            public double valueAt(double x) {
                return Math.sin(x);
            }
        };
        ConvergenceChecker checker2 = new ConvergenceChecker() {
            public int converged(double x1, double y1, double x2, double y2) {
                return Math.abs(y2) < 1e-10 ? ConvergenceChecker.LOW : ConvergenceChecker.NOT_CONVERGED;
            }
        };
        double x02 = 0.0;
        double f02 = function2.valueAt(x02);
        double x12 = 1.5;
        double f12 = function2.valueAt(x12);
        boolean rootFound2 = solver.findRoot(function2, checker2, 100, x02, f02, x12, f12);
        assertTrue(rootFound2);
        assertEquals(Math.PI, solver.getRoot(), 1e-10);

        // Test case 3
        ComputableFunction function3 = new ComputableFunction() {
            public double valueAt(double x) {
                return x * x + 1;
            }
        };
        ConvergenceChecker checker3 = new ConvergenceChecker() {
            public int converged(double x1, double y1, double x2, double y2) {
                return Math.abs(y2) < 1e-10 ? ConvergenceChecker.LOW : ConvergenceChecker.NOT_CONVERGED;
            }
        };
        double x03 = -2.0;
        double f03 = function3.valueAt(x03);
        double x13 = 0.0;
        double f13 = function3.valueAt(x13);
        boolean rootFound3 = solver.findRoot(function3, checker3, 100, x03, f03, x13, f13);
        assertFalse(rootFound3);
    }
}