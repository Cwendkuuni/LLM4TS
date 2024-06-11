package org.apache.commons.math.analysis.solvers;

import org.apache.commons.math.util.FastMath;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.exception.MathInternalError;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class BaseSecantSolverTest {

    @Test
    public void testRegulaFalsiSolver() {
        // Initialize solver
        BaseSecantSolver solver = new RegulaFalsiSolver(DEFAULT_ABSOLUTE_ACCURACY, Method.REGULA_FALSI);

        // Test case 1
        UnivariateRealFunction f1 = x -> x - 1;
        AllowedSolution allowed1 = AllowedSolution.ANY_SIDE;
        double result1 = solver.solve(100, f1, 0, 1, 0.5, allowed1);
        assertEquals(1, result1, DEFAULT_ABSOLUTE_ACCURACY);

        // Test case 2
        UnivariateRealFunction f2 = x -> x * x - 1;
        AllowedSolution allowed2 = AllowedSolution.RIGHT_SIDE;
        double result2 = solver.solve(100, f2, 0, 2, 1.5, allowed2);
        assertEquals(1, result2, DEFAULT_ABSOLUTE_ACCURACY);

        // Test case 3
        UnivariateRealFunction f3 = x -> FastMath.exp(x) - 1;
        AllowedSolution allowed3 = AllowedSolution.LEFT_SIDE;
        double result3 = solver.solve(100, f3, -2, 0, -1.5, allowed3);
        assertEquals(-0, result3, DEFAULT_ABSOLUTE_ACCURACY);
    }

    @Test
    public void testIllinoisSolver() {
        // Initialize solver
        BaseSecantSolver solver = new IllinoisSolver(DEFAULT_ABSOLUTE_ACCURACY, Method.ILLINOIS);

        // Test case 1
        UnivariateRealFunction f1 = x -> x - 1;
        AllowedSolution allowed1 = AllowedSolution.ANY_SIDE;
        double result1 = solver.solve(100, f1, 0, 1, 0.5, allowed1);
        assertEquals(1, result1, DEFAULT_ABSOLUTE_ACCURACY);

        // Test case 2
        UnivariateRealFunction f2 = x -> x * x - 1;
        AllowedSolution allowed2 = AllowedSolution.RIGHT_SIDE;
        double result2 = solver.solve(100, f2, 0, 2, 1.5, allowed2);
        assertEquals(1, result2, DEFAULT_ABSOLUTE_ACCURACY);

        // Test case 3
        UnivariateRealFunction f3 = x -> FastMath.exp(x) - 1;
        AllowedSolution allowed3 = AllowedSolution.LEFT_SIDE;
        double result3 = solver.solve(100, f3, -2, 0, -1.5, allowed3);
        assertEquals(-0, result3, DEFAULT_ABSOLUTE_ACCURACY);
    }

    @Test
    public void testPegasusSolver() {
        // Initialize solver
        BaseSecantSolver solver = new PegasusSolver(DEFAULT_ABSOLUTE_ACCURACY, Method.PEGASUS);

        // Test case 1
        UnivariateRealFunction f1 = x -> x - 1;
        AllowedSolution allowed1 = AllowedSolution.ANY_SIDE;
        double result1 = solver.solve(100, f1, 0, 1, 0.5, allowed1);
        assertEquals(1, result1, DEFAULT_ABSOLUTE_ACCURACY);

        // Test case 2
        UnivariateRealFunction f2 = x -> x * x - 1;
        AllowedSolution allowed2 = AllowedSolution.RIGHT_SIDE;
        double result2 = solver.solve(100, f2, 0, 2, 1.5, allowed2);
        assertEquals(1, result2, DEFAULT_ABSOLUTE_ACCURACY);

        // Test case 3
        UnivariateRealFunction f3 = x -> FastMath.exp(x) - 1;
        AllowedSolution allowed3 = AllowedSolution.LEFT_SIDE;
        double result3 = solver.solve(100, f3, -2, 0, -1.5, allowed3);
        assertEquals(-0, result3, DEFAULT_ABSOLUTE_ACCURACY);
    }

    private static final double DEFAULT_ABSOLUTE_ACCURACY = 1e-6;

}