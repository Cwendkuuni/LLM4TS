package org.apache.commons.math.analysis.solvers;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.analysis.FunctionUtils;
import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.RootFindersException;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;

public class BracketingNthOrderBrentSolverTest {

    @Test
    public void testSolve() throws NumberIsTooSmallException, RootFindersException {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-12, 5);

        UnivariateFunction f = new PolynomialFunction(new double[]{-1, 1, -2, 1}, new int[]{3, 2, 1, 0});

        double[] x = {-3.0, 0.0, 3.0};
        double[] y = {-1.0, 1.0, -1.0};
        double min = -3.0;
        double max = 3.0;

        assertTrue(FunctionUtils.isRoot(f, solver.solve(100, f, min, max, AllowedSolution.ANY_SIDE)));

        double[] root = solver.solve(100, f, min, max, 0.0, AllowedSolution.ANY_SIDE);
        assertArrayEquals(x, root, 1e-13);

        solver.setAllowedSolution(AllowedSolution.LEFT_SIDE);
        root = solver.solve(100, f, min, max, 0.0);
        assertTrue(root[0] < x[0]);
        assertFalse(Precision.isClose(root[0], x[1], 1e-13));

        solver.setAllowedSolution(AllowedSolution.RIGHT_SIDE);
        root = solver.solve(100, f, min, max, 0.0);
        assertTrue(root[0] > x[1]);
        assertFalse(Precision.isClose(root[0], x[2], 1e-13));

        solver.setAllowedSolution(AllowedSolution.BELOW_SIDE);
        root = solver.solve(100, f, min, max, 0.0);
        assertTrue(root[0] < x[0]);
        assertFalse(Precision.isClose(root[1], y[0], 1e-13));

        solver.setAllowedSolution(AllowedSolution.ABOVE_SIDE);
        root = solver.solve(100, f, min, max, 0.0);
        assertTrue(root[0] > x[1]);
        assertFalse(Precision.isClose(root[1], y[2], 1e-13));

        // Test with negative argument
        double min2 = -3.0;
        double max2 = 3.0;
        solver.setMin(min2);
        solver.setMax(max2);

        root = solver.solve(100, f, min2, max2, 0.0, AllowedSolution.ANY_SIDE);
        assertTrue(functionValuesAreClose(f, root[0], 1e-13));
        assertArrayEquals(x, root, 1e-13);

        // Test with relative accuracy
        BracketingNthOrderBrentSolver solver2 = new BracketingNthOrderBrentSolver(1e-7, 5);
        root = solver2.solve(100, f, min, max, 0.0, AllowedSolution.ANY_SIDE);
        assertArrayEquals(x, root, 1e-7);

        // Test with function value accuracy
        BracketingNthOrderBrentSolver solver3 = new BracketingNthOrderBrentSolver(1e-12, 5, 1e-7);
        root = solver3.solve(100, f, min, max, 0.0, AllowedSolution.ANY_SIDE);
        assertArrayEquals(x, root, 1e-13);

        // Test with custom initial interval
        double min3 = -4.0;
        double max3 = 2.0;
        solver3.setMin(min3);
        solver3.setMax(max3);
        root = solver3.solve(100, f, min3, max3, 0.0, AllowedSolution.ANY_SIDE);
        assertTrue(functionValuesAreClose(f, root[0], 1e-13));

        // Test with custom start value
        double startValue = 1.5;
        root = solver3.solve(100, f, min3, max3, startValue, AllowedSolution.ANY_SIDE);
        assertTrue(functionValuesAreClose(f, root[0], 1e-13));
        assertTrue(Precision.isClose(root[0], startValue, 1e-6));
    }

    private boolean functionValuesAreClose(UnivariateFunction f, double x, double tolerance) {
        double y = f.value(x);
        double expected = FunctionUtils.root(f, 0.0);
        return Precision.isClose(y, expected, tolerance);
    }

}