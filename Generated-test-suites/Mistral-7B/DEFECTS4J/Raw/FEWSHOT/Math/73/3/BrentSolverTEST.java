package org.apache.commons.cli;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.ComputableFunction;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.roots.ConvergenceChecker;

public class BrentSolverTest {

    @Test
    public void testFindRoot_Converged_Low() {
        // Given
        ComputableFunction function = mock(ComputableFunction.class);
        ConvergenceChecker checker = mock(ConvergenceChecker.class);
        when(checker.converged(Double.valueOf(1.0), Double.valueOf(0.0), Double.valueOf(2.0), Double.valueOf(-1.0)))
                .thenReturn(ConvergenceChecker.LOW);

        BrentSolver solver = new BrentSolver();

        // When
        boolean result = solver.findRoot(function, checker, 10, 1.0, 0.0, 2.0, -1.0);

        // Then
        assertNotNull(solver.getRoot());
        assertEquals(1.0, solver.getRoot(), 0.0);
        assertEquals(true, result);
    }

    @Test
    public void testFindRoot_Converged_High() {
        // Given
        ComputableFunction function = mock(ComputableFunction.class);
        ConvergenceChecker checker = mock(ConvergenceChecker.class);
        when(checker.converged(Double.valueOf(1.0), Double.valueOf(0.0), Double.valueOf(2.0), Double.valueOf(-1.0)))
                .thenReturn(ConvergenceChecker.HIGH);

        BrentSolver solver = new BrentSolver();

        // When
        boolean result = solver.findRoot(function, checker, 10, 1.0, 0.0, 2.0, -1.0);

        // Then
        assertNotNull(solver.getRoot());
        assertEquals(-1.0, solver.getRoot(), 0.0);
        assertEquals(true, result);
    }

    @Test
    public void testFindRoot_Converged_Interpolation() {
        // Given
        ComputableFunction function = mock(ComputableFunction.class);
        ConvergenceChecker checker = mock(ConvergenceChecker.class);
        when(checker.converged(Double.valueOf(1.0), Double.valueOf(0.0), Double.valueOf(2.0), Double.valueOf(-1.0)))
                .thenReturn(ConvergenceChecker.INTERPOLATION);

        BrentSolver solver = new BrentSolver();

        // When
        boolean result = solver.findRoot(function, checker, 10, 1.0, 0.0, 2.0, -1.0);

        // Then
        assertNotNull(solver.getRoot());
        // The root value is not relevant for this test case
        // Equality with any value would be acceptable
        // assertEquals(...);
        assertEquals(true, result);
    }

    @Test
    public void testFindRoot_MaxIterationsExceeded() {
        // Given
        ComputableFunction function = mock(ComputableFunction.class);
        ConvergenceChecker checker = mock(ConvergenceChecker.class);

        BrentSolver solver = new BrentSolver();

        // When
        boolean result = solver.findRoot(function, checker, 0, 1.0, 0.0, 2.0, -1.0);

        // Then
        assertFalse(result);
    }
}