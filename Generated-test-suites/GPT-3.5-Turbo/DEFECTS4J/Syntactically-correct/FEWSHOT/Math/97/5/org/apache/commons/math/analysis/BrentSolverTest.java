package org.spaceroots.mantissa.roots;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.ComputableFunction;
import org.spaceroots.mantissa.functions.FunctionException;

public class BrentSolverTest {

    private BrentSolver solver;
    private ComputableFunction function;
    private ConvergenceChecker checker;

    @Before
    public void setUp() {
        solver = new BrentSolver();
        function = mock(ComputableFunction.class);
        checker = mock(ConvergenceChecker.class);
    }

    @Test
    public void testFindRoot() throws FunctionException {
        when(checker.converged(anyDouble(), anyDouble(), anyDouble(), anyDouble()))
                .thenReturn(ConvergenceChecker.NO_CONVERGENCE)
                .thenReturn(ConvergenceChecker.LOW);

        assertTrue(solver.findRoot(function, checker, 100, 1.0, 2.0, 3.0, 4.0));
        verify(function, times(1)).valueAt(2.0);
        assertEquals(2.0, solver.getRoot(), 0.0);

        assertFalse(solver.findRoot(function, checker, 100, 1.0, 2.0, 3.0, 4.0));
        verify(function, times(2)).valueAt(2.0);
        assertEquals(Double.NaN, solver.getRoot(), 0.0);
    }

    @Test
    public void testGetRoot() {
        assertEquals(Double.NaN, solver.getRoot(), 0.0);
    }

}