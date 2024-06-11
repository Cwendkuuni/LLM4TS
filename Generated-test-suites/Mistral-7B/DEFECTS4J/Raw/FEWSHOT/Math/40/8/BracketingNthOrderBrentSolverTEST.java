package org.apache.commons.math.analysis.solvers;

import static org.junit.Assert.*;

import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.junit.Test;

public class BracketingNthOrderBrentSolverTest {

    @Test
    public void testSolveWithDefaultConstructor() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        assertNotNull(solver);
        assertEquals(5, solver.getMaximalOrder());
        assertEquals(1e-6, solver.getAbsoluteAccuracy(), 0.0);
    }

    @Test
    public void testSolveWithCustomConstructor() {
        double relativeAccuracy = 1e-3;
        double absoluteAccuracy = 1e-8;
        int maximalOrder = 3;
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(relativeAccuracy, absoluteAccuracy, maximalOrder);
        assertNotNull(solver);
        assertEquals(maximalOrder, solver.getMaximalOrder());
        assertEquals(relativeAccuracy, solver.getRelativeAccuracy(), 0.0);
        assertEquals(absoluteAccuracy, solver.getAbsoluteAccuracy(), 0.0);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testSolveWithLowerMaximalOrder() {
        double relativeAccuracy = 1e-3;
        double absoluteAccuracy = 1e-8;
        int maximalOrder = 1;
        BracketingNthOrderBrentSolver solver = null;
        try {
            solver = new BracketingNthOrderBrentSolver(relativeAccuracy, absoluteAccuracy, maximalOrder);
        } finally {
            assertNull(solver);
        }
    }

    @Test
    public void testSolveWithPolynomialFunction() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        UnivariateFunction function = new PolynomialFunction(new double[]{-1, 1, -2, 3}, new double[]{1, 3, 2, 1});
        double min = -5;
        double max = 5;
        double solution = solver.solve(100, function, min, max, AllowedSolution.ANY_SIDE);
        assertEquals(1.0, solution, 1e-6);
    }

    @Test
    public void testSolveWithPolynomialFunctionAndStartValue() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        UnivariateFunction function = new PolynomialFunction(new double[]{-1, 1, -2, 3}, new double[]{1, 3, 2, 1});
        double min = -5;
        double max = 5;
        double startValue = 2.5;
        double solution = solver.solve(100, function, min, max, startValue, AllowedSolution.ANY_SIDE);
        assertEquals(1.0, solution, 1e-6);
    }

    @Test(expected = MathIllegalArgumentException.class)
    public void testSolveWithNonBracketingInterval() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        UnivariateFunction function = new PolynomialFunction(new double[]{-1, 1, -2, 3}, new double[]{1, 3, 2, 1});
        double min = 5;
        double max = 5;
        solver.solve(100, function, min, max, AllowedSolution.ANY_SIDE);
    }

}