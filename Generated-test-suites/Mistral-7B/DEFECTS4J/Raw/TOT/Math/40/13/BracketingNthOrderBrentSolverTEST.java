package org.apache.commons.math.analysis.solvers;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NoBracketingException;
import org.junit.Test;
import static org.junit.Assert.*;

public class BracketingNthOrderBrentSolverTest {

    @Test
    public void testBrentSolverSimple() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-6, 5);
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) - 0.5 * x;
            }
        };

        double root = solver.solve(100, f, -3.0, 3.0, AllowedSolution.ANY_SIDE);
        assertEquals(Math.PI / 2, root, 1e-5);
    }

    @Test(expected = NoBracketingException.class)
    public void testBrentSolverNoBracketing() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-6, 5);
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) - x;
            }
        };

        solver.solve(100, f, -3.0, 3.0, AllowedSolution.ANY_SIDE);
    }

    @Test(expected = MathInternalError.class)
    public void testBrentSolverInternalError() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-6, 5);
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                throw new RuntimeException();
            }
        };

        solver.solve(100, f, -3.0, 3.0, AllowedSolution.ANY_SIDE);
    }

    @Test
    public void testBrentSolverMaxEval() throws MaxCountExceededException {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-6, 5);
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(Math.E, -x) - Math.log(x);
            }
        };

        double root = solver.solve(5, f, 1.0, 2.0, AllowedSolution.ANY_SIDE);
        assertEquals(1.0, root, 1e-5);
    }

}