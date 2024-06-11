package org.apache.commons.math.analysis.solvers;

import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.MaxEvalException;
import org.apache.commons.math3.exception.RootFinderException;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;
import static org.junit.Assert.*;

public class BaseSecantSolverTest {

    @Test
    public void testSolver_RegulaFalsi() throws RootFinderException, MaxEvalException {
        final UnivariateFunction f = new PolynomialFunction(x -> Math.pow(x, 2) - 4);
        final BaseSecantSolver solver = new IllinoisSolver(1e-6, f.getValue(2.0));
        final double result = solver.solve(10, f, -5.0, 5.0, 0.0);
        assertEquals(2.0, Precision.round(result, 5));
    }

    @Test
    public void testSolver_Illinois() throws RootFinderException, MaxEvalException {
        final UnivariateFunction f = x -> x * x - 4;
        final BaseSecantSolver solver = new IllinoisSolver(1e-6, f.getValue(2.0), 1e-6, 1e-6, 1e-6);
        final double result = solver.solve(10, f, -5.0, 5.0, 0.0);
        assertEquals(2.0, Precision.round(result, 5));
    }

    @Test
    public void testSolver_Pegasus() throws RootFinderException, MaxEvalException {
        final UnivariateFunction f = x -> Math.pow(x, 3) - 2;
        final BaseSecantSolver solver = new PegasusSolver(1e-6, f.getValue(1.0), 1e-6, 1e-6, 1e-6);
        final double result = solver.solve(10, f, -3.0, 3.0, -1.0);
        assertEquals(-1.0, Precision.round(result, 5));
    }

    @Test(expected = RootFinderException.class)
    public void testSolver_FunctionValueException() throws RootFinderException, MaxEvalException {
        final UnivariateFunction f = x -> 1.0 / (x - 0.0);
        final BaseSecantSolver solver = new IllinoisSolver(1e-6, f.getValue(1.0));
        solver.solve(10, f, -1.0, 1.0, 0.0);
    }

    @Test(expected = MaxEvalException.class)
    public void testSolver_MaxEvalException() throws RootFinderException, MaxEvalException {
        final UnivariateFunction f = x -> Math.pow(x, 2) - 1;
        final BaseSecantSolver solver = new IllinoisSolver(1e-6, f.getValue(2.0));
        solver.solve(0, f, -5.0, 5.0, 0.0);
    }

    @Test
    public void testSolver_AllowedSolution_BelowSide() throws RootFinderException, MaxEvalException {
        final UnivariateFunction f = x -> Math.pow(x, 2) - 4;
        final BaseSecantSolver solver = new IllinoisSolver(1e-6, f.getValue(2.0), AllowedSolution.BELOW_SIDE);
        final double result = solver.solve(10, f, -5.0, 5.0, 0.0);
        assertEquals(2.0, Precision.round(result, 5));
    }

    @Test
    public void testSolver_AllowedSolution_AboveSide() throws RootFinderException, MaxEvalException {
        final UnivariateFunction f = x -> Math.pow(x, 2) - 4;
        final BaseSecantSolver solver = new IllinoisSolver(1e-6, f.getValue(2.0), AllowedSolution.ABOVE_SIDE);
        final double result = solver.solve(10, f, -5.0, 5.0, 0.0);
        assertEquals(2.0, Precision.round(result, 5));
    }

    @Test
    public void testSolver_AllowedSolution_LeftSide() throws RootFinderException, MaxEvalException {
        final UnivariateFunction f = x -> Math.pow(x, 2) - 4;
        final BaseSecantSolver solver = new IllinoisSolver(1e-6, f.getValue(2.0), AllowedSolution.LEFT_SIDE);
        final double result = solver.solve(10, f, -5.0, 5.0, 0.0);
        assertEquals(2.0, Precision.round(result, 5));
    }

    @Test
    public void testSolver_AllowedSolution_RightSide() throws RootFinderException, MaxEvalException {
        final UnivariateFunction f = x -> Math.pow(x, 2) - 4;
        final BaseSecantSolver solver = new IllinoisSolver(1e-6, f.getValue(2.0), AllowedSolution.RIGHT_SIDE);
        final double result = solver.solve(10, f, -5.0, 5.0, 0.0);
        assertEquals(2.0, Precision.round(result, 5));
    }
}