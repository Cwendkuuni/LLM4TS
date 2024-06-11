package org.apache.commons.cli.test;

import static org.junit.Assert.*;

import org.apache.commons.commons.math.analysis.functions.Sin;
import org.apache.commons.commons.math.analysis.solvers.BaseSecantSolver;
import org.apache.commons.commons.math.analysis.solvers.BracketedUnivariateRealSolver;
import org.apache.commons.commons.math.analysis.solvers.RegulaFalsiSolver;
import org.junit.Test;

public class BaseSecantSolverTest {

    // Test case for RegulaFalsiSolver method
    @Test
    public void testRegulaFalsiSolver() {
        double relativeAccuracy = 0.01;
        double absoluteAccuracy = 1.0;

        UnivariateRealFunction f = new Sin(Math.PI / 2.0);

        BracketedUnivariateRealSolver<UnivariateRealFunction> solver = new RegulaFalsiSolver(relativeAccuracy, absoluteAccuracy);
        double root = solver.solve(new Sin(Math.PI), 1.0, -1.0);
        assertEquals(1.0, root, 0.01);
    }

    // Test case for IllinoisSolver method
    @Test
    public void testIllinoisSolver() {
        double relativeAccuracy = 0.01;
        double absoluteAccuracy = 1.0;

        UnivariateRealFunction f = new Sin(Math.PI / 2.0);

        BracketedUnivariateRealSolver<UnivariateRealFunction> solver = new RegulaFalsiSolver(relativeAccuracy, absoluteAccuracy, RegulaFalsiSolver.Method.ILLINOIS);
        double root = solver.solve(new Sin(Math.PI), 1.0, -1.0);
        assertEquals(1.0, root, 0.01);
    }

    // Test case for PegasusSolver method
    @Test
    public void testPegasusSolver() {
        double relativeAccuracy = 0.01;
        double absoluteAccuracy = 1.0;

        UnivariateRealFunction f = new Sin(Math.PI / 2.0);

        BracketedUnivariateRealSolver<UnivariateRealFunction> solver = new RegulaFalsiSolver(relativeAccuracy, absoluteAccuracy, RegulaFalsiSolver.Method.PEGASUS);
        double root = solver.solve(new Sin(Math.PI), 1.0, -1.0);
        assertEquals(1.0, root, 0.01);
    }

    // Test case for solving a quadratic equation
    @Test
    public void testQuadraticEquation() {
        double relativeAccuracy = 0.01;
        double absoluteAccuracy = 1.0;

        UnivariateRealFunction f = new QuadraticFunction(-1.0, 4.0, 4.0);

        BracketedUnivariateRealSolver<UnivariateRealFunction> solver = new RegulaFalsiSolver(relativeAccuracy, absoluteAccuracy);
        double root = solver.solve(f, -5.0, 5.0);
        assertEquals(2.0, root, 0.01);
    }
}