package org.apache.commons.math.analysis.solvers.test;

import static org.junit.Assert.*;

import org.apache.commons.math3.analysis.FunctionUtils;
import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NoBracketingException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.solvers.BracketingNthOrderBrentSolver;
import org.apache.commons.math3.solvers.BracketedUnivariateFunction;
import org.apache.commons.math3.util.Precision;

/**
 * Tests for {@link BracketingNthOrderBrentSolver}.
 */
public class BracketingNthOrderBrentSolverTest {

    /**
     * Test method for {@link BracketingNthOrderBrentSolver#BracketingNthOrderBrentSolver()}.
     */
    @Test
    public void testBracketingNthOrderBrentSolverConstruction() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        assertEquals(1e-6, solver.getAbsoluteAccuracy(), 1e-8);
        assertEquals(5, solver.getMaximalOrder());
    }

    /**
     * Test method for {@link BracketingNthOrderBrentSolver#BracketingNthOrderBrentSolver(double, int)}.
     */
    @Test
    public void testBracketingNthOrderBrentSolverConstructionWithAccuracyAndOrder() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-8, 3);
        assertEquals(1e-8, solver.getAbsoluteAccuracy(), 1e-8);
        assertEquals(3, solver.getMaximalOrder());
    }

    /**
     * Test method for {@link BracketingNthOrderBrentSolver#BracketingNthOrderBrentSolver(double, double, int)}.
     */
    @Test
    public void testBracketingNthOrderBrentSolverConstructionWithRelativeAccuracyAndOrder() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-8, 1e-6, 3);
        assertEquals(1e-8, solver.getAbsoluteAccuracy(), 1e-8);
        assertEquals(3, solver.getMaximalOrder());
        assertEquals(1e-6, solver.getRelativeAccuracy(), 1e-8);
    }

    /**
     * Test method for {@link BracketingNthOrderBrentSolver#BracketingNthOrderBrentSolver(double, double, double, int)}.
     */
    @Test
    public void testBracketingNthOrderBrentSolverConstructionWithRelativeAccuracyFunctionValueAccuracyAndOrder() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-8, 1e-6, 1e-7, 3);
        assertEquals(1e-8, solver.getAbsoluteAccuracy(), 1e-8);
        assertEquals(3, solver.getMaximalOrder());
        assertEquals(1e-6, solver.getRelativeAccuracy(), 1e-8);
        assertEquals(1e-7, solver.getFunctionValueAccuracy(), 1e-8);
    }

    /**
     * Test method for {@link BracketingNthOrderBrentSolver#getMaximalOrder()}.
     */
    @Test
    public void testGetMaximalOrder() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        assertEquals(5, solver.getMaximalOrder());
    }

    /**
     * Test method for {@link BracketingNthOrderBrentSolver#solve(int, UnivariateFunction, double, double)}.
     */
    @Test
    public void testSolveWithDefaultParameters() throws NumberIsTooSmallException, NoBracketingException,
            MaxCountExceededException {
        UnivariateFunction function = new PolynomialFunction(x -> x * x * x - 2 * x * x + 2);
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        double solution = solver.solve(function, -2, 2);
        assertEquals(0, solution, 1e-8);
    }

    /**
     * Test method for {@link BracketingNthOrderBrentSolver#solve(int, UnivariateFunction, double, double, double)}.
     */
    @Test
    public void testSolveWithStartValue() throws NumberIsTooSmallException, NoBracketingException,
            MaxCountExceededException {
        UnivariateFunction function = new PolynomialFunction(x -> x * x * x - 2 * x * x + 2);
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        double solution = solver.solve(function, -2, 2, 1);
        assertEquals(1, solution, 1e-8);
    }

    /**
     * Test method for {@link BracketingNthOrderBrentSolver#solve(int, UnivariateFunction, double, double, double, AllowedSolution)}.
     */
    @Test
    public void testSolveWithAllowedSolution() throws NumberIsTooSmallException, NoBracketingException,
            MaxCountExceededException {
        UnivariateFunction function = new PolynomialFunction(x -> x * x * x - 2 * x * x + 2);
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        double solution = solver.solve(function, -2, 2, 1, AllowedSolution.LEFT_SIDE);
        assertEquals(-1, solution, 1e-8);
    }

    /**
     * Test method for {@link BracketingNthOrderBrentSolver#solve(int, BracketedUnivariateFunction, double)}.
     */
    @Test
    public void testSolveWithBracketedFunction() throws NumberIsTooSmallException, NoBracketingException,
            MaxCountExceededException {
        UnivariateFunction unbracketedFunction = new PolynomialFunction(x -> x * x * x - 2 * x * x + 2);
        BracketedUnivariateFunction bracketedFunction = new BracketedUnivariateFunction(unbracketedFunction, -2, 2);
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        double solution = solver.solve(bracketedFunction);
        assertEquals(0, solution, 1e-8);
    }

    /**
     * Test method for {@link BracketingNthOrderBrentSolver#solve(int, BracketedUnivariateFunction, double, AllowedSolution)}.
     */
    @Test
    public void testSolveWithBracketedFunctionAndAllowedSolution() throws NumberIsTooSmallException, NoBracketingException,
            MaxCountExceededException {
        UnivariateFunction unbracketedFunction = new PolynomialFunction(x -> x * x * x - 2 * x * x + 2);
        BracketedUnivariateFunction bracketedFunction = new BracketedUnivariateFunction(unbracketedFunction, -2, 2);
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        double solution = solver.solve(bracketedFunction, AllowedSolution.LEFT_SIDE);
        assertEquals(-1, solution, 1e-8);
    }

}