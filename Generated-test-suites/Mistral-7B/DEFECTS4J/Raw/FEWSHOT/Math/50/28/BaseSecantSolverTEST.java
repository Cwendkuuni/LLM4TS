package org.apache.commons.cli.test;

import static org.junit.Assert.*;
import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.MaxEvalExceededException;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.MathInternalError;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.Pair;
import org.junit.Test;

public class BaseSecantSolverTest {

    @Test
    public void testSolve_RegulaFalsi() {
        UnivariateRealFunction function = new PolynomialFunction(new double[]{-1.0, 1.0, 1.0}, new double[]{0.0});
        BaseSecantSolver solver = new IllinoisSolver(1e-6, Method.REGULA_FALSI);
        try {
            double root = solver.solve(10, function, -1.0, 1.0, 0.0);
            assertEquals(0.0, root, 1e-8);
        } catch (MaxEvalExceededException e) {
            fail("Max evaluation exception should not be thrown.");
        }
    }

    @Test
    public void testSolve_Illinois() {
        UnivariateRealFunction function = new PolynomialFunction(new double[]{-1.0, 1.0, 1.0}, new double[]{0.0});
        BaseSecantSolver solver = new IllinoisSolver(1e-6, Method.ILLINOIS);
        try {
            double root = solver.solve(10, function, -1.0, 1.0, 0.0);
            assertEquals(0.0, root, 1e-8);
        } catch (MaxEvalExceededException e) {
            fail("Max evaluation exception should not be thrown.");
        }
    }

    @Test
    public void testSolve_Pegasus() {
        UnivariateRealFunction function = new PolynomialFunction(new double[]{-1.0, 1.0, 1.0}, new double[]{0.0});
        BaseSecantSolver solver = new PegasusSolver(1e-6, Method.PEGASUS);
        try {
            double root = solver.solve(10, function, -1.0, 1.0, 0.0);
            assertEquals(0.0, root, 1e-8);
        } catch (MaxEvalExceededException e) {
            fail("Max evaluation exception should not be thrown.");
        }
    }

    @Test
    public void testSolve_IllegalArguments() {
        UnivariateRealFunction function = new PolynomialFunction(new double[]{-1.0, 1.0, 1.0}, new double[]{0.0});

        // Test no arguments
        assertThrows(MathIllegalArgumentException.class, () -> new BaseSecantSolver(1e-6, Method.REGULA_FALSI).solve(10, function));
        assertThrows(MathIllegalArgumentException.class, () -> new BaseSecantSolver(1e-6, Method.ILLINOIS).solve(10, function));
        assertThrows(MathIllegalArgumentException.class, () -> new BaseSecantSolver(1e-6, Method.PEGASUS).solve(10, function));

        // Test null function
        assertThrows(MathIllegalArgumentException.class, () -> new BaseSecantSolver(1e-6, Method.REGULA_FALSI).solve(10, null));
        assertThrows(MathIllegalArgumentException.class, () -> new BaseSecantSolver(1e-6, Method.ILLINOIS).solve(10, null));
        assertThrows(MathIllegalArgumentException.class, () -> new BaseSecantSolver(1e-6, Method.PEGASUS).solve(10, null));

        // Test invalid method
        assertThrows(MathInternalError.class, () -> new BaseSecantSolver(1e-6, (Method) null).solve(10, function));
    }

    @Test
    public void testSolve_MaxEvalExceeded() {
        UnivariateRealFunction function = new PolynomialFunction(new double[]{1.0, -1.0, 1.0}, new double[]{1.0});
        BaseSecantSolver solver = new PegasusSolver(1e-6, Method.PEGASUS);
        try {
            solver.solve(0, function, -1.0, 1.0, 0.0);
            fail("MaxEvalExceededException should be thrown.");
        } catch (MaxEvalExceededException e) {
            // Expected
        }
    }
}