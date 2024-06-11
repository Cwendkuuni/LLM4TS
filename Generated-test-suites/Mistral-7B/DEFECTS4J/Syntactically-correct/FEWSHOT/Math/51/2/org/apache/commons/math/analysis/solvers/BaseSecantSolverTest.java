package org.apache.commons.cli.test;

import static org.junit.Assert.*;

import org.apache.commons.commons.math.analysis.function.FunctionUtils;
import org.apache.commons.commons.math.analysis.solvers.BaseSecantSolver;
import org.apache.commons.commons.math.analysis.solvers.BracketedUnivariateRealSolver;
import org.apache.commons.commons.math.analysis.solvers.RegulaFalsiSolver;
import org.apache.commons.commons.math.analysis.solvers.SecantSolver;
import org.apache.commons.commons.math.exception.MaxEvalExceededException;
import org.apache.commons.commons.math.exception.UnbracketedException;
import org.apache.commons.commons.math.util.FastMath;
import org.junit.Test;

/**
 * Test cases for {@link BaseSecantSolver} class.
 */
public class BaseSecantSolverTest {

    /**
     * Test solver with {@code RegulaFalsiSolver} method.
     * @throws UnbracketedException
     * @throws MaxEvalExceededException
     */
    @Test
    public void testRegulaFalsiSolver() {
        UnivariateRealFunction f = FunctionUtils.sin(x -> Math.PI * x / 2);
        BaseSecantSolver solver = new RegulaFalsiSolver(1e-6);
        double solution = solver.solve(f, -1.0, 1.0);
        assertEquals(0.0, solution, 1e-6);
    }

    /**
     * Test solver with {@code IllinoisSolver} method.
     * @throws UnbracketedException
     * @throws MaxEvalExceededException
     */
    @Test
    public void testIllinoisSolver() {
        UnivariateRealFunction f = x -> Math.pow(x, 2) - 2;
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, Method.ILLINOIS);
        double solution = solver.solve(f, -1.0, 1.0);
        assertEquals(-1.0, solution, 1e-6);
    }

    /**
     * Test solver with {@code PegasusSolver} method.
     * @throws UnbracketedException
     * @throws MaxEvalExceededException
     */
    @Test
    public void testPegasusSolver() {
        UnivariateRealFunction f = x -> Math.exp(-x) - 0.5;
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, Method.PEGASUS);
        double solution = solver.solve(f, 0.0, 1.0);
        assertEquals(0.4054651081082285, solution, 1e-6);
    }

    /**
     * Test solver with {@code SecantSolver} method.
     * @throws MaxEvalExceededException
     */
    @Test
    public void testSecantSolver() {
        UnivariateRealFunction f = x -> Math.pow(x, 2) - 2;
        BaseSecantSolver solver = new SecantSolver(1e-6);
        double solution = solver.solve(f, 1.0, 2.0);
        assertEquals(-1.0, solution, 1e-6);
    }

    /**
     * Test solver with {@code allowedSolution} parameter.
     * @throws UnbracketedException
     * @throws MaxEvalExceededException
     */
    @Test
    public void testAllowedSolution() {
        UnivariateRealFunction f = x -> Math.pow(x, 2) - 2;
        BaseSecantSolver solver = new RegulaFalsiSolver(1e-6);
        double solution = solver.solve(f, -1.0, 1.0, AllowedSolution.LEFT_SIDE);
        assertEquals(-1.0, solution, 1e-6);

        solver = new RegulaFalsiSolver(1e-6);
        solution = solver.solve(f, -1.0, 1.0, AllowedSolution.RIGHT_SIDE);
        assertNotEquals(-1.0, solution, 1e-6);
    }

}