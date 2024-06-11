package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.NoBracketingException;
import org.apache.commons.math3.util.Precision;

public class BracketingNthOrderBrentSolverTest {

    @Test
    public void testConstructor() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        assertEquals(1e-6, solver.getAbsoluteAccuracy(), 1e-9);
        assertEquals(5, solver.getMaximalOrder());

        BracketingNthOrderBrentSolver solverRelative = new BracketingNthOrderBrentSolver(0.01, 1e-6, 5);
        assertEquals(0.01, solverRelative.getRelativeAccuracy(), 1e-9);
        assertEquals(1e-6, solverRelative.getAbsoluteAccuracy(), 1e-9);
        assertEquals(5, solverRelative.getMaximalOrder());

        BracketingNthOrderBrentSolver solverAbsolute = new BracketingNthOrderBrentSolver(1e-6, 5);
        assertEquals(1e-6, solverAbsolute.getAbsoluteAccuracy(), 1e-9);
        assertEquals(5, solverAbsolute.getMaximalOrder());

        BracketingNthOrderBrentSolver solverFunctionValue = new BracketingNthOrderBrentSolver(1e-6, 1e-6, 0.001, 5);
        assertEquals(1e-6, solverFunctionValue.getAbsoluteAccuracy(), 1e-9);
        assertEquals(1e-6, solverFunctionValue.getFunctionValueAccuracy(), 1e-9);
        assertEquals(5, solverFunctionValue.getMaximalOrder());

        BracketingNthOrderBrentSolver solverMaximalOrderTooLow = new BracketingNthOrderBrentSolver(1e-6, 1);
        assertThrows(NumberIsTooSmallException.class, () -> solverMaximalOrderTooLow.getMaximalOrder());

        BracketingNthOrderBrentSolver solverAllowedNull = new BracketingNthOrderBrentSolver(1e-6, 5, null);
        assertNull(solverAllowedNull.getAllowed());
    }

    @Test
    public void testGetters() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-6, 5);
        assertEquals(1e-6, solver.getAbsoluteAccuracy(), 1e-9);
        assertEquals(5, solver.getMaximalOrder());

        BracketingNthOrderBrentSolver solverRelative = new BracketingNthOrderBrentSolver(0.01, 1e-6, 5);
        assertEquals(0.01, solverRelative.getRelativeAccuracy(), 1e-9);
        assertEquals(1e-6, solverRelative.getAbsoluteAccuracy(), 1e-9);
        assertEquals(5, solverRelative.getMaximalOrder());

        BracketingNthOrderBrentSolver solverFunctionValue = new BracketingNthOrderBrentSolver(1e-6, 1e-6, 0.001, 5);
        assertEquals(1e-6, solverFunctionValue.getAbsoluteAccuracy(), 1e-9);
        assertEquals(1e-6, solverFunctionValue.getFunctionValueAccuracy(), 1e-9);
        assertEquals(5, solverFunctionValue.getMaximalOrder());
    }

    @Test
    public void testSolveWithUnivariateFunction() throws NoBracketingException {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-6, 5);

        UnivariateFunction f = new PolynomialFunction(new double[]{-1, 1, -2}, new double[]{1, 1, 1});
        double root = solver.solve(100, f, -2, 0);
        assertEquals(0, root, 1e-9);

        root = solver.solve(100, f, -2, 1);
        assertEquals(1, root, 1e-9);

        root = solver.solve(100, f, 0, 1);
        assertEquals(1, root, 1e-9);

        UnivariateFunction fNegative = new PolynomialFunction(
                new double[]{-1, 1, -2}, new double[]{-1, 1, 1});
        root = solver.solve(100, fNegative, -2, 0);
        assertEquals(-1, root, 1e-9);
    }

    @Test
    public void testSolveWithStartValue() throws NoBracketingException {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-6, 5);

        UnivariateFunction f = new PolynomialFunction(new double[]{-1, 1, -2}, new double[]{1, 1, 1});
        double root = solver.solve(100, f, -2, 0, 1);
        assertEquals(0, root, 1e-9);

        root = solver.solve(100, f, -2, 0, 2);
        assertEquals(1, root, 1e-9);

        root = solver.solve(100, f, 0, 1, 1);
        assertEquals(1, root, 1e-9);

        UnivariateFunction fNegative = new PolynomialFunction(
                new double[]{-1, 1, -2}, new double[]{-1, 1, 1});
        root = solver.solve(100, fNegative, -2, 0, 1);
        assertEquals(-1, root, 1e-9);
    }

    @Test
    public void testBracketing() throws NoBracketingException {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-6, 5);

        UnivariateFunction f = new PolynomialFunction(new double[]{-1, 1, -2}, new double[]{1, 1, 1});
        double[] x = {-2, 0, 1};
        double[] y = {1, 0, 1};
        double[] expectedX = {-1.63209316134, -0.16057246851, 0.16057246851, 1.63209316134};
        double[] expectedY = {0.20000000000, 0.00000000000, 0.00000000000, 0.20000000000};
        double tolerance = 1e-6;

        for (int i = 0; i < x.length; ++i) {
            double root = solver.solve(100, f, x[i], x[i], AllowedSolution.ANY_SIDE);
            assertEquals(x[i], root, tolerance);
            assertEquals(y[i], solver.computeObjectiveValue(root), tolerance);
        }

        assertArrayEquals(expectedX, solver.getBracketingInterval(f), tolerance);
        assertArrayEquals(expectedY, solver.getBracketingValues(f), tolerance);
    }

    @Test
    public void testAllowedSolutions() throws NoBracketingException {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-6, 5);

        UnivariateFunction f = new PolynomialFunction(new double[]{-1, 1, -2}, new double[]{1, 1, 1});
        double[] x = {-2, 0, 1};

        for (AllowedSolution allowed : AllowedSolution.values()) {
            for (int i = 0; i < x.length; ++i) {
                double root = solver.solve(100, f, x[i], x[i], allowed);
                if (allowed == AllowedSolution.LEFT_SIDE) {
                    assertEquals(x[i], root, 1e-9);
                } else if (allowed == AllowedSolution.RIGHT_SIDE) {
                    assertNotEquals(x[i], root, 1e-9);
                } else {
                    assertEquals(allowed == AllowedSolution.ABOVE_SIDE
                            ? (solver.computeObjectiveValue(x[i]) < 0 ? x[i] : x[i + 1])
                            : (solver.computeObjectiveValue(x[i]) > 0 ? x[i] : x[i + 1]), root, 1e-9);
                }
            }
        }
    }

    @Test
    public void testNoBracketingException() throws NoBracketingException {
        UnivariateFunction f = new PolynomialFunction(new double[]{-1, 1}, new double[]{1, 1});
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-6, 5);

        assertThrows(NoBracketingException.class, () -> solver.solve(100, f, -1, 0));
        assertThrows(NoBracketingException.class, () -> solver.solve(100, f, 1, 0));

        UnivariateFunction fNegative = new PolynomialFunction(new double[]{-1, 1}, new double[]{-1, 1});
        assertThrows(NoBracketingException.class, () -> solver.solve(100, fNegative, 0, 1));
    }

    @Test
    public void testMathIllegalArgumentException() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-6, 5);

        assertThrows(MathIllegalArgumentException.class, () -> solver.solve(-1, new PolynomialFunction(new double[]{}, new double[]{}), 0, 0));
        assertThrows(MathIllegalArgumentException.class, () -> solver.solve(100, null, 0, 0));
    }

}