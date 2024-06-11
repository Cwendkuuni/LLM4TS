package org.apache.commons.math.analysis.solvers;

import static org.junit.Assert.assertEquals;
import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.MaxEvalException;
import org.apache.commons.math3.exception.NoBracketingException;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;

public class BaseSecantSolverTest {

    @Test
    public void testSolve_RegulaFalsi() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 2;
            }
        };
        BaseSecantSolver solver = new RegulaFalsiSolver(1e-6);
        try {
            double root = solver.solve(10, f, -2.5, 2.5);
            assertEquals(0.0, root, 1e-6);
        } catch (MaxEvalException | NoBracketingException e) {
            throw new RuntimeException("Test failed", e);
        }
    }

    @Test
    public void testSolve_Illinois() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) - 0.5;
            }
        };
        BaseSecantSolver solver = new IllinoisSolver(1e-6);
        try {
            double root = solver.solve(10, f, -10, 10);
            assertEquals(Math.PI / 2, root, Precision.PI / 100.0);
        } catch (MaxEvalException | NoBracketingException e) {
            throw new RuntimeException("Test failed", e);
        }
    }

    @Test
    public void testSolve_Pegasus() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.exp(-x) - 0.5;
            }
        };
        BaseSecantSolver solver = new PegasusSolver(1e-6);
        try {
            double root = solver.solve(10, f, -5, 5);
            assertEquals(Math.log(0.5), root, 1e-6);
        } catch (MaxEvalException | NoBracketingException e) {
            throw new RuntimeException("Test failed", e);
        }
    }

}