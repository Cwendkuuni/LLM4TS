package org.spaceroots.mantissa.roots;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.spaceroots.mantissa.functions.FunctionException;
import org.spaceroots.mantissa.functions.scalar.ComputableFunction;
import org.spaceroots.mantissa.roots.ConvergenceChecker.RootStatus;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class BrentSolverTest {

    private BrentSolver solver;
    private ComputableFunction function;

    @Before
    public void setUp() {
        solver = new BrentSolver();
        function = new ComputableFunction() {
            @Override
            public double valueAt(double x) throws FunctionException {
                return x * x - 4;
            }
        };
    }

    @After
    public void tearDown() {
        solver = null;
        function = null;
    }

    @Test
    public void testFindRoot() throws FunctionException {
        double x0 = 1.0;
        double f0 = function.valueAt(x0);
        double x1 = 3.0;
        double f1 = function.valueAt(x1);

        assertTrue(solver.findRoot(function, new ConvergenceChecker() {
            @Override
            public RootStatus converged(double x1, double fx1, double x2, double fx2) {
                if (Math.abs(x2 - x1) < 1e-6 || Math.abs(fx1) < 1e-6) {
                    return RootStatus.LOW;
                } else if (Math.abs(x1 - x2) < 1e-6 || Math.abs(fx2) < 1e-6) {
                    return RootStatus.HIGH;
                } else {
                    return RootStatus.NOT_CONVERGED;
                }
            }
        }, 100, x0, f0, x1, f1));

        assertEquals(2.0, solver.getRoot(), 1e-6);
    }

    @Test
    public void testFindRootNoRootInInterval() throws FunctionException {
        double x0 = -2.0;
        double f0 = function.valueAt(x0);
        double x1 = -1.0;
        double f1 = function.valueAt(x1);

        assertFalse(solver.findRoot(function, new ConvergenceChecker() {
            @Override
            public RootStatus converged(double x1, double fx1, double x2, double fx2) {
                if (Math.abs(x2 - x1) < 1e-6 || Math.abs(fx1) < 1e-6) {
                    return RootStatus.LOW;
                } else if (Math.abs(x1 - x2) < 1e-6 || Math.abs(fx2) < 1e-6) {
                    return RootStatus.HIGH;
                } else {
                    return RootStatus.NOT_CONVERGED;
                }
            }
        }, 100, x0, f0, x1, f1));
    }

    @Test
    public void testFindRootMaxIterationsExceeded() throws FunctionException {
        double x0 = 1.0;
        double f0 = function.valueAt(x0);
        double x1 = 3.0;
        double f1 = function.valueAt(x1);

        assertFalse(solver.findRoot(function, new ConvergenceChecker() {
            @Override
            public RootStatus converged(double x1, double fx1, double x2, double fx2) {
                if (Math.abs(x2 - x1) < 1e-6 || Math.abs(fx1) < 1e-6) {
                    return RootStatus.LOW;
                } else if (Math.abs(x1 - x2) < 1e-6 || Math.abs(fx2) < 1e-6) {
                    return RootStatus.HIGH;
                } else {
                    return RootStatus.NOT_CONVERGED;
                }
            }
        }, 1, x0, f0, x1, f1));
    }

    @Test
    public void testGetRoot() {
        solver.getRoot();
    }
}