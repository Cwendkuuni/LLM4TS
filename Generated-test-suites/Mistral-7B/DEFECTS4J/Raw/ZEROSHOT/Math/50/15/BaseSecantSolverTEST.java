package org.apache.commons.math.analysis.solvers;

import static org.junit.Assert.*;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.util.Precision;

public class BaseSecantSolverTest {

    private UnivariateRealFunction f;

    @org.junit.Before
    public void setup() {
        f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 2;
            }
        };
    }

    @org.junit.Test
    public void testSolver() {
        final double epsilon = 1e-9;
        final int maxEval = 10;

        final BaseSecantSolver solver = new IllinoisSolver(epsilon);
        double root = solver.solve(maxEval, f, -2.0, 2.0, 0.0);
        assertEquals("Illinois Solver should find the square root of 2",
                      Precision.round(Math.sqrt(2), epsilon), root, epsilon);

        solver.dispose();

        solver = new RegulaFalsiSolver(epsilon);
        root = solver.solve(maxEval, f, -2.0, 2.0, 0.0);
        assertEquals("Regula Falsi Solver should find the square root of 2",
                      Precision.round(Math.sqrt(2), epsilon), root, epsilon);

        solver.dispose();

        solver = new BaseSecantSolver(epsilon, Method.PEGASUS);
        root = solver.solve(maxEval, f, -2.0, 2.0, 0.0);
        assertEquals("Pegasus Solver should find the square root of 2",
                      Precision.round(Math.sqrt(2), epsilon), root, epsilon);

        solver.dispose();
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testSolver_InvalidMethod() {
        final double epsilon = 1e-9;
        final int maxEval = 10;

        new BaseSecantSolver(epsilon, Method.INVALID);
    }

    @org.junit.Test
    public void testSolver_AllowedSolution() {
        final double epsilon = 1e-9;
        final int maxEval = 10;

        final BaseSecantSolver solver = new IllinoisSolver(AllowedSolution.ABOVE_SIDE, epsilon);
        double root = solver.solve(maxEval, f, -2.0, 2.0, 0.0);
        assertFalse("Illinois Solver should not find the square root of 2 for allowed solution ABOVE_SIDE",
                    Precision.isClose(Math.sqrt(2), root, epsilon));
        solver.dispose();

        solver = new RegulaFalsiSolver(AllowedSolution.LEFT_SIDE, epsilon);
        root = solver.solve(maxEval, f, -2.0, 2.0, 0.0);
        assertTrue("Regula Falsi Solver should find the square root of 2 for allowed solution LEFT_SIDE",
                    Precision.isClose(Math.sqrt(2), root, epsilon));
        solver.dispose();

        solver = new BaseSecantSolver(epsilon, Method.PEGASUS, AllowedSolution.BELOW_SIDE);
        root = solver.solve(maxEval, f, -2.0, 2.0, 0.0);
        assertFalse("Pegasus Solver should not find the square root of 2 for allowed solution BELOW_SIDE",
                    Precision.isClose(Math.sqrt(2), root, epsilon));
        solver.dispose();
    }
}