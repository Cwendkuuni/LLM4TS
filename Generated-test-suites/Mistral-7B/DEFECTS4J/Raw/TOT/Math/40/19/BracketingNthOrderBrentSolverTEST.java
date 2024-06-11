package org.apache.commons.math.analysis.solvers;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.NoBracketingException;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;
import static org.junit.Assert.*;

public class BracketingNthOrderBrentSolverTest {

    @Test
    public void testSimpleRoot() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-6, 5);
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return x * x - 2;
            }
        };
        double root = solver.solve(100, f, -3.0, 3.0, AllowedSolution.ANY_SIDE);
        assertEquals(0.0, root, 1e-6);
    }

    @Test(expected = NoBracketingException.class)
    public void testNoBracketing() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-6, 5);
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) * Math.cos(x);
            }
        };
        solver.solve(100, f, -3.0, 3.0, AllowedSolution.ANY_SIDE);
    }

    @Test(expected = MathIllegalArgumentException.class)
    public void testLowMaximalOrder() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-6, 0);
    }

    @Test
    public void testEdgeCases() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-6, 5);
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.exp(-x) - Math.exp(-(x - 1));
            }
        };
        double root = solver.solve(100, f, 0, 1, AllowedSolution.LEFT_SIDE);
        assertTrue(Precision.isClose(root, 0.36787944117144233, 1e-7));
        root = solver.solve(100, f, 1, 2, AllowedSolution.RIGHT_SIDE);
        assertTrue(Precision.isClose(root, 1.0, 1e-6));
    }

    @Test
    public void testAllowedSolutions() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-6, 5);
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2) - 4;
            }
        };
        double rootBelow = solver.solve(100, f, -3.0, 1.0, AllowedSolution.BELOW_SIDE);
        assertTrue(Precision.isClose(rootBelow, -2.0, 1e-6));
        double rootAbove = solver.solve(100, f, 1.0, 3.0, AllowedSolution.ABOVE_SIDE);
        assertTrue(Precision.isClose(rootAbove, 2.0, 1e-6));
    }

}