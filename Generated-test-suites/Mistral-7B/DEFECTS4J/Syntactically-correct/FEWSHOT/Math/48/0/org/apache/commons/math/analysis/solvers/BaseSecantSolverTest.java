package org.apache.commons.cli.test;

import static org.junit.Assert.*;
import org.apache.commons.commons.math.analysis.functions.SimplePolynomialFunction;
import org.apache.commons.commons.math.exception.MaxEvalExceededException;
import org.apache.commons.commons.math.exception.UnbracketedException;
import org.apache.commons.commons.math.util.Precision;
import org.apache.commons.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.commons.math.analysis.solvers.BaseSecantSolver;
import org.apache.commons.commons.math.analysis.solvers.BracketedUnivariateRealSolver;
import org.apache.commons.commons.math.analysis.solvers.RegulaFalsiSolver;
import org.apache.commons.commons.math.analysis.solvers.IllinoisSolver;
import org.apache.commons.commons.math.analysis.solvers.PegasusSolver;

/**
 * Test cases for the BaseSecantSolver class.
 */
public class BaseSecantSolverTest {

    @Test(expected = UnsupportedOperationException.class)
    public void testUnsupportedMethod() {
        BaseSecantSolver solver = new RegulaFalsiSolver(0.001, new SimplePolynomialFunction(0.0, 1.0, -1.0));
        solver.getBracketingFunction();
    }

    @Test
    public void testRegulaFalsiSolver() {
        BaseSecantSolver solver = new RegulaFalsiSolver(0.001, new SimplePolynomialFunction(0.0, 1.0, -1.0));
        double root = solver.solve(100);
        assertEquals(0.0, root, 1e-6);
    }

    @Test(expected = MaxEvalExceededException.class)
    public void testRegulaFalsiSolverMaxEval() {
        BaseSecantSolver solver = new RegulaFalsiSolver(0.001, new SimplePolynomialFunction(0.0, 1.0, -1.0));
        solver.solve(1, new SimplePolynomialFunction(0.0, 1.0, -1.0));
    }

    @Test(expected = UnbracketedException.class)
    public void testRegulaFalsiSolverUnbracketed() {
        BaseSecantSolver solver = new RegulaFalsiSolver(0.001, new SimplePolynomialFunction(0.0, 1.0, 0.0));
        solver.solve(100);
    }

    @Test
    public void testIllinoisSolver() {
        BaseSecantSolver solver = new IllinoisSolver(0.001, new SimplePolynomialFunction(0.0, 1.0, -1.0));
        double root = solver.solve(100);
        assertEquals(0.0, root, 1e-6);
    }

    @Test(expected = MaxEvalExceededException.class)
    public void testIllinoisSolverMaxEval() {
        BaseSecantSolver solver = new IllinoisSolver(0.001, new SimplePolynomialFunction(0.0, 1.0, -1.0));
        solver.solve(1, new SimplePolynomialFunction(0.0, 1.0, -1.0));
    }

    @Test(expected = UnbracketedException.class)
    public void testIllinoisSolverUnbracketed() {
        BaseSecantSolver solver = new IllinoisSolver(0.001, new SimplePolynomialFunction(0.0, 1.0, 0.0));
        solver.solve(100);
    }

    @Test
    public void testPegasusSolver() {
        BaseSecantSolver solver = new PegasusSolver(0.001, 0.0001, new SimplePolynomialFunction(0.0, 1.0, -1.0));
        double root = solver.solve(100);
        assertEquals(0.0, root, 1e-6);
    }

    @Test(expected = MaxEvalExceededException.class)
    public void testPegasusSolverMaxEval() {
        BaseSecantSolver solver = new PegasusSolver(0.001, 0.0001, new SimplePolynomialFunction(0.0, 1.0, -1.0));
        solver.solve(1, new SimplePolynomialFunction(0.0, 1.0, -1.0));
    }

    @Test(expected = UnbracketedException.class)
    public void testPegasusSolverUnbracketed() {
        BaseSecantSolver solver = new PegasusSolver(0.001, 0.0001, new SimplePolynomialFunction(0.0, 1.0, 0.0));
        solver.solve(100);
    }

    @Test
    public void testRegulaFalsiSolverBracketing() {
        BaseSecantSolver solver = new RegulaFalsiSolver(0.001, new SimplePolynomialFunction(0.0, 1.0, -1.0));
        double root = solver.solve(100);
        assertEquals(0.0, root, 1e-6);
        assertTrue(solver.isBracketing());
    }

    @Test
    public void testIllinoisSolverBracketing() {
        BaseSecantSolver solver = new IllinoisSolver(0.001, new SimplePolynomialFunction(0.0, 1.0, -1.0));
        double root = solver.solve(100);
        assertEquals(0.0, root, 1e-6);
        assertTrue(solver.isBracketing());
    }

    @Test
    public void testPegasusSolverBracketing() {
        BaseSecantSolver solver = new PegasusSolver(0.001, 0.0001, new SimplePolynomialFunction(0.0, 1.0, -1.0));
        double root = solver.solve(100);
        assertEquals(0.0, root, 1e-6);
        assertTrue(solver.isBracketing());
    }

    @Test
    public void testGetAbsoluteAccuracy() {
        BaseSecantSolver solver = new RegulaFalsiSolver(0.001, new SimplePolynomialFunction(0.0, 1.0, -1.0));
        assertEquals(1e-6, solver.getAbsoluteAccuracy(), 1e-9);
    }

    @Test
    public void testGetFunctionValueAccuracy() {
        BaseSecantSolver solver = new RegulaFalsiSolver(0.001, new SimplePolynomialFunction(0.0, 1.0, -1.0));
        assertEquals(0.0, solver.getFunctionValueAccuracy(), 1e-9);
    }

    @Test
    public void testGetMaxIterations() {
        BaseSecantSolver solver = new RegulaFalsiSolver(0.001, new SimplePolynomialFunction(0.0, 1.0, -1.0));
        assertEquals(-1, solver.getMaxIterations(), 1e-9);
    }

    @Test
    public void testGetMaxIterationsWithMaxEval() {
        BaseSecantSolver solver = new RegulaFalsiSolver(1, new SimplePolynomialFunction(0.0, 1.0, -1.0));
        assertEquals(1, solver.getMaxIterations(), 1e-9);
    }

    @Test
    public void testRelativeAccuracy() {
        BaseSecantSolver solver = new RegulaFalsiSolver(0.01, new SimplePolynomialFunction(0.0, 1.0, -1.0));
        assertEquals(0.01, solver.getRelativeAccuracy(), 1e-9);
    }
}