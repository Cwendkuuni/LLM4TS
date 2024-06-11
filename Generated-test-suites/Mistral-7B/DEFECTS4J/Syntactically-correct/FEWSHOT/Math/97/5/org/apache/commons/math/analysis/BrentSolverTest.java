package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.function.Function;

import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.FunctionException;
import org.spaceroots.mantissa.functions.scalar.RootsFinder;
import org.spaceroots.mantissa.functions.scalar.test.ConvergenceChecker;
import org.spaceroots.mantissa.functions.scalar.test.DummyFunction;

public class BrentSolverTest {

    @Test
    public void testFindRoot_converged_Low() throws FunctionException {
        // given
        ComputableFunction function = mock(ComputableFunction.class);
        double x0 = 1.0;
        double x1 = 3.0;
        double expectedRoot = 2.0;
        ConvergenceChecker checker = new ConvergenceChecker() {
            @Override
            public int converged(double x, double fx, double xPrev, double fxPrev) {
                return ConvergenceChecker.LOW;
            }
        };

        when(function.valueAt(x0)).thenReturn(1.0);
        when(function.valueAt(x1)).thenReturn(-1.0);

        BrentSolver solver = new BrentSolver();

        // when
        boolean result = solver.findRoot(function, checker, 10, x0, 1.0, x1, -1.0);

        // then
        assertTrue(result);
        assertEquals(expectedRoot, solver.getRoot(), 0.001);
    }

    @Test
    public void testFindRoot_converged_High() throws FunctionException {
        // given
        ComputableFunction function = mock(ComputableFunction.class);
        double x0 = 1.0;
        double x1 = 3.0;
        double expectedRoot = 2.0;
        ConvergenceChecker checker = new ConvergenceChecker() {
            @Override
            public int converged(double x, double fx, double xPrev, double fxPrev) {
                return ConvergenceChecker.HIGH;
            }
        };

        when(function.valueAt(x0)).thenReturn(1.0);
        when(function.valueAt(x1)).thenReturn(-1.0);

        BrentSolver solver = new BrentSolver();

        // when
        boolean result = solver.findRoot(function, checker, 10, x0, 1.0, x1, -1.0);

        // then
        assertTrue(result);
        assertEquals(expectedRoot, solver.getRoot(), 0.001);
    }

    @Test
    public void testFindRoot_converged_Zero() throws FunctionException {
        // given
        ComputableFunction function = mock(ComputableFunction.class);
        double x0 = 1.0;
        double x1 = 3.0;
        double expectedRoot = 1.5;
        ConvergenceChecker checker = new ConvergenceChecker() {
            @Override
            public int converged(double x, double fx, double xPrev, double fxPrev) {
                return ConvergenceChecker.ZERO;
            }
        };

        when(function.valueAt(x0)).thenReturn(1.0);
        when(function.valueAt(x1)).thenReturn(0.0);

        BrentSolver solver = new BrentSolver();

        // when
        boolean result = solver.findRoot(function, checker, 10, x0, 1.0, x1, 0.0);

        // then
        assertTrue(result);
        assertEquals(expectedRoot, solver.getRoot(), 0.001);
    }

    @Test
    public void testFindRoot_convergenceNotReached() throws FunctionException {
        // given
        ComputableFunction function = mock(ComputableFunction.class);
        double x0 = 1.0;
        double x1 = 3.0;
        ConvergenceChecker checker = mock(ConvergenceChecker.class);
        when(function.valueAt(x0)).thenReturn(1.0);
        when(function.valueAt(x1)).thenReturn(-1.0);
        when(checker.converged(anyDouble(), anyDouble(), anyDouble(), anyDouble())).thenReturn(ConvergenceChecker.NOT_REACHED);

        BrentSolver solver = new BrentSolver();

        // when
        boolean result = solver.findRoot(function, checker, 10, x0, 1.0, x1, -1.0);

        // then
        assertFalse(result);
        assertEquals(Double.NaN, solver.getRoot());
    }

    @Test
    public void testFindRoot_iterationLimitExceeded() throws FunctionException {
        // given
        ComputableFunction function = mock(ComputableFunction.class);
        double x0 = 1.0;
        double x1 = 3.0;
        ConvergenceChecker checker = mock(ConvergenceChecker.class);
        when(function.valueAt(x0)).thenReturn(1.0);
        when(function.valueAt(x1)).thenReturn(-1.0);
        when(checker.converged(anyDouble(), anyDouble(), anyDouble(), anyDouble())).thenReturn(ConvergenceChecker.NOT_REACHED);

        BrentSolver solver = new BrentSolver();

        // when
        boolean result = solver.findRoot(function, checker, 0, x0, 1.0, x1, -1.0);

        // then
        assertFalse(result);
        assertEquals(Double.NaN, solver.getRoot());
    }

    @Test
    public void testFindRoot_divergentFunction() throws FunctionException {
        // given
        ComputableFunction function = new DummyFunction(-1.0);
        double x0 = 1.0;
        double x1 = 3.0;
        ConvergenceChecker checker = mock(ConvergenceChecker.class);
        when(checker.converged(anyDouble(), anyDouble(), anyDouble(), anyDouble())).thenReturn(ConvergenceChecker.NOT_REACHED);

        BrentSolver solver = new BrentSolver();

        // when
        Function<Double, Boolean> exceptionThrown = () -> solver.findRoot(function, checker, 10, x0, 1.0, x1, -1.0);

        // then
        assertTrue(exceptionThrown.get().isPresent());
        assertEquals(FunctionException.RootNotFound.class, exceptionThrown.get().get().getClass());
    }

}

class ConvergenceChecker {
    public static final int NOT_REACHED = 0;
    public static final int LOW = 1;
    public static final int HIGH = 2;
    public static final int ZERO = 3;

    public abstract int converged(double x, double fx, double xPrev, double fxPrev);
}

class DummyFunction implements ComputableFunction {
    private final double value;

    public DummyFunction(double value) {
        this.value = value;
    }

    @Override
    public double valueAt(double x) {
        return value;
    }
}