package org.apache.commons.math.analysis.solvers.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;

public class BracketingNthOrderBrentSolverTest {

    @Test
    public void testConstructorWithDefaults() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        assertEquals(1e-6, solver.getAbsoluteAccuracy(), 1e-10);
        assertEquals(5, solver.getMaximalOrder());
    }

    @Test
    public void testConstructorWithCustomValues() {
        double relativeAccuracy = 0.001;
        double absoluteAccuracy = 0.01;
        int maximalOrder = 10;
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(relativeAccuracy, absoluteAccuracy, maximalOrder);
        assertEquals(relativeAccuracy, solver.getRelativeAccuracy(), 1e-10);
        assertEquals(absoluteAccuracy, solver.getAbsoluteAccuracy(), 1e-10);
        assertEquals(maximalOrder, solver.getMaximalOrder());
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testConstructorWithInvalidMaximalOrder() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(0.1, 1.0, 1);
    }

    @Test
    public void testGetMaximalOrder() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        assertEquals(5, solver.getMaximalOrder());
    }

    @Test
    public void testSolveSimpleExample() {
        UnivariateFunction f = new PolynomialFunction(new double[] { 1, -2, 1 }, new double[] { 1, 0, 1 });
        double min = -2;
        double max = 2;
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        double root = solver.solve(100, f, min, max);
        assertEquals(0, root, 1e-6);
    }

    @Test
    public void testSolveWithCustomValues() {
        UnivariateFunction f = new PolynomialFunction(new double[] { 1, -2, 1 }, new double[] { 1, 0, 1 });
        double min = -2;
        double max = 2;
        double startValue = 1.5;
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(0.001, 1e-6, 10);
        double root = solver.solve(100, f, min, max, startValue);
        assertEquals(0, root, 1e-6);
    }

    @Test
    public void testSolveWithBracketingInterval() {
        UnivariateFunction f = new PolynomialFunction(new double[] { 1, -2, 1 }, new double[] { 1, 0, 1 });
        double min = -2;
        double max = 2;
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        assertThrows(NoBracketingException.class, () -> solver.solve(100, f, max, min));
    }

    @Test
    public void testSolveWithOutOfRangeValues() {
        UnivariateFunction f = new PolynomialFunction(new double[] { 1, -2, 1 }, new double[] { 1, 0, 1 });
        double min = -2;
        double max = 2;
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        assertThrows(IllegalArgumentException.class, () -> solver.solve(100, f, Double.NEGATIVE_INFINITY, min));
        assertThrows(IllegalArgumentException.class, () -> solver.solve(100, f, max, Double.POSITIVE_INFINITY));
    }

    @Test
    public void testSolveWithFunctionThatHasNoRoots() {
        UnivariateFunction f = new PolynomialFunction(new double[] { 1, 1, 1 }, new double[] { 1, 1, 1 });
        double min = -2;
        double max = 2;
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        assertThrows(NoBracketingException.class, () -> solver.solve(100, f, min, max));
    }

}