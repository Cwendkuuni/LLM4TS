package org.apache.commons.cli.test;

import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.MaxEvalExceededException;
import org.apache.commons.math3.exception.RootFindersException;
import org.apache.commons.math3.solver.BracketedUnivariateRealSolver;
import org.apache.commons.math3.solver.LaguerreSolver;
import org.apache.commons.math3.solver.SecantSolver;

public class BaseSecantSolverTest {

    @Test
    public void testRegulaFalsi() {
        BracketedUnivariateRealSolver<UnivariateRealFunction> solver = new RegulaFalsiSolver(1e-6);
        UnivariateRealFunction f = new PolynomialFunction(new double[]{-1, 1, 1, 1}, new double[]{0, 1, 1, 0});
        double root = 1.0;
        try {
            root = solver.solve(100, f, -2.0, 2.0, 0.0);
        } catch (MaxEvalExceededException | RootFindersException e) {
            fail("Solver should have found root within given tolerance.");
        }
        assertEquals(root, 1.0, 1e-6);
    }

    @Test
    public void testIllinois() {
        BracketedUnivariateRealSolver<UnivariateRealFunction> solver = new IllinoisSolver(1e-6);
        UnivariateRealFunction f = new PolynomialFunction(new double[]{-1, 1, 1, 1}, new double[]{0, 1, 1, 0});
        double root = 1.0;
        try {
            root = solver.solve(100, f, -2.0, 2.0, 0.0);
        } catch (MaxEvalExceededException | RootFindersException e) {
            fail("Solver should have found root within given tolerance.");
        }
        assertEquals(root, 1.0, 1e-6);
    }

    @Test
    public void testPegasus() {
        BracketedUnivariateRealSolver<UnivariateRealFunction> solver = new PegasusSolver(1e-6);
        UnivariateRealFunction f = new PolynomialFunction(new double[]{-1, 1, 1, 1}, new double[]{0, 1, 1, 0});
        double root = 1.0;
        try {
            root = solver.solve(100, f, -2.0, 2.0, 0.0);
        } catch (MaxEvalExceededException | RootFindersException e) {
            fail("Solver should have found root within given tolerance.");
        }
        assertEquals(root, 1.0, 1e-6);
    }

    @Test
    public void testSecant() {
        SecantSolver solver = new SecantSolver(1e-6);
        UnivariateRealFunction f = new PolynomialFunction(new double[]{0}, new double[]{1});
        double root = 0.0;
        try {
            root = solver.solve(100, f, -1.0, 1.0);
        } catch (MaxEvalExceededException e) {
            fail("Solver should have found root within given tolerance.");
        }
        assertEquals(root, 0.0, 1e-6);
    }
}

class RegulaFalsiSolver extends BaseSecantSolver {

    public RegulaFalsiSolver(double accuracy) {
        super(accuracy, Method.REGULA_FALSI);
    }
}

class IllinoisSolver extends BaseSecantSolver {

    public IllinoisSolver(double accuracy) {
        super(accuracy, Method.ILLINOIS);
    }
}

class PegasusSolver extends BaseSecantSolver {

    public PegasusSolver(double accuracy) {
        super(accuracy, Method.PEGASUS);
    }
}