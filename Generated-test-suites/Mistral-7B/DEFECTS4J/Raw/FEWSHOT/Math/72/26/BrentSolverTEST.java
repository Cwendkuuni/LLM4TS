package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.ComputableFunction;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.roots.ConvergenceChecker;

/**
 * Test cases for the BrentSolver class
 */
public class BrentSolverTest {

    private ComputableFunction function;
    private ConvergenceChecker checker;
    private BrentSolver solver;

    @Before
    public void setup() {
        function = mock(ComputableFunction.class);
        checker = mock(ConvergenceChecker.class);
        solver = new BrentSolver();
    }

    /**
     * Test empty constructor
     */
    @Test
    public void testEmptyConstructor() {
        assertTrue(solver.getRoot() == Double.NaN);
    }

    /**
     * Test findRoot method with convergence
     */
    @Test
    public void testFindRootConvergence() throws FunctionException {
        double x0 = 1.0;
        double f0 = 4.0;
        double x1 = 2.0;
        double f1 = -3.0;

        when(function.valueAt(x0)).thenReturn(f0);
        when(function.valueAt(x1)).thenReturn(f1);

        // Arrange
        ConvergenceChecker.ConvergenceStatus expectedStatus = ConvergenceChecker.LOW;
        when(checker.converged(x0, f0, x1, f1)).thenReturn(expectedStatus);

        // Act
        boolean rootFound = solver.findRoot(function, checker, 10, x0, f0, x1, f1);

        // Assert
        assertTrue(rootFound);
        assertTrue(solver.getRoot() != Double.NaN);
    }

    /**
     * Test findRoot method without convergence
     */
    @Test
    public void testFindRootNoConvergence() throws FunctionException {
        double x0 = 1.0;
        double f0 = 4.0;
        double x1 = 2.0;
        double f1 = -3.0;

        when(function.valueAt(x0)).thenReturn(f0);
        when(function.valueAt(x1)).thenReturn(f1);

        // Arrange
        ConvergenceChecker.ConvergenceStatus expectedStatus = ConvergenceChecker.NONE;
        when(checker.converged(x0, f0, x1, f1)).thenReturn(expectedStatus);

        // Act
        boolean rootFound = solver.findRoot(function, checker, 10, x0, f0, x1, f1);

        // Assert
        assertFalse(rootFound);
        assertTrue(solver.getRoot() == Double.NaN);
    }

    /**
     * Test findRoot method with multiple iterations and different interval bounds
     */
    @Test
    public void testFindRootMultipleIterations() throws FunctionException {
        double x0 = 1.0;
        double f0 = 4.0;
        double x1 = 2.0;
        double f1 = -3.0;
        double x2 = 3.0;
        double f2 = 2.0;

        when(function.valueAt(x0)).thenReturn(f0);
        when(function.valueAt(x1)).thenReturn(f1);
        when(function.valueAt(x2)).thenReturn(f2);

        // Arrange
        ConvergenceChecker.ConvergenceStatus expectedStatus;
        when(checker.converged(x0, f0, x1, f1)).thenReturn(ConvergenceChecker.NONE);
        when(checker.converged(x1, f1, x2, f2)).thenReturn(ConvergenceChecker.LOW);

        // Act
        boolean rootFound = solver.findRoot(function, checker, 10, x0, f0, x1, f1);

        // Assert
        assertFalse(rootFound);
        assertTrue(solver.getRoot() == Double.NaN);

        // Reset checker and solver states
        solver.setRoot(Double.NaN);

        // Act
        rootFound = solver.findRoot(function, checker, 10, x1, f1, x2, f2);

        // Assert
        assertTrue(rootFound);
        assertTrue(solver.getRoot() != Double.NaN);
    }

    /**
     * Test findRoot method with different convergence statuses
     */
    @Test
    public void testFindRootDifferentConvergenceStatuses() throws FunctionException {
        double x0 = 1.0;
        double f0 = 4.0;
        double x1 = 2.0;
        double f1 = -3.0;
        double x2 = 3.0;
        double f2 = 2.0;

        when(function.valueAt(x0)).thenReturn(f0);
        when(function.valueAt(x1)).thenReturn(f1);
        when(function.valueAt(x2)).thenReturn(f2);

        // Arrange
        ConvergenceChecker.ConvergenceStatus lowStatus = ConvergenceChecker.LOW;
        ConvergenceChecker.ConvergenceStatus highStatus = ConvergenceChecker.HIGH;
        ConvergenceChecker.ConvergenceStatus noneStatus = ConvergenceChecker.NONE;

        when(checker.converged(x0, f0, x1, f1)).thenReturn(noneStatus);
        when(checker.converged(x1, f1, x2, f2)).thenReturn(lowStatus);

        // Act
        boolean rootFound = solver.findRoot(function, checker, 10, x0, f0, x1, f1);

        // Assert
        assertFalse(rootFound);
        assertTrue(solver.getRoot() == Double.NaN);

        // Act
        rootFound = solver.findRoot(function, checker, 10, x1, f1, x2, f2);

        // Assert
        assertTrue(rootFound);
        assertTrue(solver.getRoot() != Double.NaN);
    }

}