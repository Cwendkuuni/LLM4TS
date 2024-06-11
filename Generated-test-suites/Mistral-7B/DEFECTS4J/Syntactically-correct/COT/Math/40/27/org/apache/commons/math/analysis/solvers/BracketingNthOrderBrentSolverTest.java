package org.apache.commons.math.analysis.solvers;

import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NoBracketingException;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.TestDoubleArray;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

public class BracketingNthOrderBrentSolverTest {

    private BracketingNthOrderBrentSolver solver;
    private UnivariateFunction function;
    private double[] xTest;
    private double[] yTest;

    @Before
    public void setUp() {
        solver = new BracketingNthOrderBrentSolver(1e-6, 5);
        function = new PolynomialFunction(new double[]{-1, 1, -1}, new double[]{0, 1, 0});
        xTest = new double[]{0.3, 1.0, 2.0};
        yTest = new double[]{0.4, 1.0, 0.6};
    }

    // Test for basic functionality

    @Test
    public void testBasicFunctionality() throws NoBracketingException, InterruptedException {
        double root = solver.solve(100, function, xTest[0], xTest[1]);
        assertFalse(Precision.equals(root, xTest[0], 1e-4));
        assertFalse(Precision.equals(root, xTest[1], 1e-4));
        assertTrue(Precision.isClose(function.value(root), 0.0, 1e-4));
    }

    @Test
    public void testBasicFunctionalityWithStartValue() throws NoBracketingException, InterruptedException {
        double startValue = 1.5;
        double root = solver.solve(100, function, xTest[0], xTest[1], startValue);
        assertFalse(Precision.equals(root, xTest[0], 1e-4));
        assertFalse(Precision.equals(root, xTest[1], 1e-4));
        assertTrue(Precision.isClose(function.value(root), 0.0, 1e-4));
        assertTrue(Precision.isClose(root, startValue, 1e-4));
    }

    // Test for edge cases

    @Test(expected = NoBracketingException.class)
    public void testNoBracketingException() {
        solver.solve(100, function, 1.0, 2.0);
    }

    @Test
    public void testMaxEvalException() {
        assertThrows(MaxCountExceededException.class, () -> solver.solve(0, function, 0.0, 1.0));
    }

    @Test
    public void testNullFunction() {
        assertThrows(NullPointerException.class, () -> solver.solve(100, null, 0.0, 1.0));
    }

    @Test
    public void testNullInterval() {
        assertThrows(NullPointerException.class, () -> solver.solve(100, function, null, null));
    }

    @Test
    public void testNullAllowedSolution() {
        assertThrows(NullPointerException.class, () -> solver.solve(100, function, xTest[0], xTest[1], null));
    }

    // Test for exceptions handling

    @Test(expected = NumberIsTooSmallException.class)
    public void testNumberIsTooSmallException() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(0.0, 5);
    }

    @Test
    public void testIllegalArguments() {
        assertNotNull(solver.getFunction());
        assertThrows(IllegalArgumentException.class, () -> solver.solve(-1, function, 0.0, 1.0));
        assertThrows(IllegalArgumentException.class, () -> solver.solve(100, function, null, 1.0));
        assertThrows(IllegalArgumentException.class, () -> solver.solve(100, function, 0.0, null));
        assertThrows(IllegalArgumentException.class, () -> solver.solve(100, function, null, null));
    }

}