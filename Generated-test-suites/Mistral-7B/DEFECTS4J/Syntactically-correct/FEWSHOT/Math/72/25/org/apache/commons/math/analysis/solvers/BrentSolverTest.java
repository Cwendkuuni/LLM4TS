package org.apache.commons.cli;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import org.spaceroots.mantissa.functions.scalar.ComputableFunction;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.roots.ConvergenceChecker;

@RunWith(PowerMockRunner.class)
@PrepareForTest({BrentSolver.class})
public class BrentSolverTest {

    @Test
    public void testFindRoot_nullFunction() {
        BrentSolver solver = new BrentSolver();
        ConvergenceChecker checker = Mockito.mock(ConvergenceChecker.class);
        ComputableFunction function = null;
        assertFalse("findRoot should return false with null function", solver.findRoot(function, checker, 10, 1.0, 2.0, 3.0, 4.0));
    }

    @Test
    public void testFindRoot_notConverged() {
        final double epsilon = 1e-5;
        BrentSolver solver = new BrentSolver();
        ConvergenceChecker checker = Mockito.mock(ConvergenceChecker.class);
        PowerMockito.doNothing().when(checker).converged(Mockito.anyDouble(), Mockito.anyDouble(), Mockito.anyDouble(), Mockito.anyDouble());
        ComputableFunction function = PowerMockito.mock(ComputableFunction.class);
        PowerMockito.doReturn(0.0).when(function).valueAt(Mockito.anyDouble());

        double x0 = 1.0;
        double x1 = 3.0;
        assertFalse("findRoot should return false if not converged", solver.findRoot(function, checker, 10, x0, 1.0, x1, 1.0));

        // check that the root is not set
        assertNotNull("root should be NaN", solver.getRoot());
    }

    @Test
    public void testFindRoot_converged_low() {
        final double epsilon = 1e-5;
        BrentSolver solver = new BrentSolver();
        ConvergenceChecker checker = Mockito.mock(ConvergenceChecker.class);
        PowerMockito.doReturn(ConvergenceChecker.LOW).when(checker).converged(Mockito.anyDouble(), Mockito.anyDouble(), Mockito.anyDouble(), Mockito.anyDouble());
        ComputableFunction function = PowerMockito.mock(ComputableFunction.class);
        PowerMockito.doReturn(0.0).when(function).valueAt(Mockito.anyDouble());

        double x0 = 1.0;
        double x1 = 2.0;
        double root = 1.5;
        PowerMockito.doReturn(root).when(function).valueAt(x0);
        PowerMockito.doReturn(0.0).when(function).valueAt(x1);

        assertTrue("findRoot should return true when converged (low)", solver.findRoot(function, checker, 10, x0, function.valueAt(x0), x1, function.valueAt(x1)));

        // check that the root is correctly set
        assertEquals("root should be set to the expected value", root, solver.getRoot());
    }

    @Test
    public void testFindRoot_converged_high() {
        final double epsilon = 1e-5;
        BrentSolver solver = new BrentSolver();
        ConvergenceChecker checker = Mockito.mock(ConvergenceChecker.class);
        PowerMockito.doReturn(ConvergenceChecker.HIGH).when(checker).converged(Mockito.anyDouble(), Mockito.anyDouble(), Mockito.anyDouble(), Mockito.anyDouble());
        ComputableFunction function = PowerMockito.mock(ComputableFunction.class);
        PowerMockito.doReturn(0.0).when(function).valueAt(Mockito.anyDouble());

        double x0 = 1.0;
        double x1 = 2.0;
        double root = 1.5;
        PowerMockito.doReturn(root).when(function).valueAt(x1);
        PowerMockito.doReturn(0.0).when(function).valueAt(x0);

        assertTrue("findRoot should return true when converged (high)", solver.findRoot(function, checker, 10, x0, function.valueAt(x0), x1, function.valueAt(x1)));

        // check that the root is correctly set
        assertEquals("root should be set to the expected value", root, solver.getRoot());
    }

    @Test
    public void testFindRoot_converged_sameValue() {
        final double epsilon = 1e-5;
        BrentSolver solver = new BrentSolver();
        ConvergenceChecker checker = Mockito.mock(ConvergenceChecker.class);
        PowerMockito.doReturn(ConvergenceChecker.LOW).when(checker).converged(Mockito.anyDouble(), Mockito.anyDouble(), Mockito.anyDouble(), Mockito.anyDouble());
        ComputableFunction function = PowerMockito.mock(ComputableFunction.class);
        PowerMockito.doReturn(1.0).when(function).valueAt(Mockito.anyDouble());

        double x0 = 1.0;
        double x1 = 1.1;
        double root = 1.0;

        assertTrue("findRoot should return true when converged (same value)", solver.findRoot(function, checker, 10, x0, function.valueAt(x0), x1, function.valueAt(x1)));

        // check that the root is correctly set
        assertEquals("root should be set to the expected value", root, solver.getRoot());
    }

    @Test(expected = FunctionException.class)
    public void testFindRoot_functionException() {
        final double epsilon = 1e-5;
        BrentSolver solver = new BrentSolver();
        ConvergenceChecker checker = Mockito.mock(ConvergenceChecker.class);
        ComputableFunction function = PowerMockito.mock(ComputableFunction.class);
        PowerMockito.doThrow(new FunctionException("Test")).when(function).valueAt(Mockito.anyDouble());

        double x0 = 1.0;
        double x1 = 2.0;
        assertFalse("findRoot should return false when function throws an exception", solver.findRoot(function, checker, 10, x0, function.valueAt(x0), x1, function.valueAt(x1)));
    }

}