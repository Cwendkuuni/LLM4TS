package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertEquals;

import org.apache.commons.math.UnivariateRealFunction;
import org.apache.commons.math3.analysis.BisectionSolver;
import org.apache.commons.math3.analysis.FunctionUtils;
import org.apache.commons.math3.analysis.InterpolatingPolynomial;
import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.analysis.Solver;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.MaxEvalExceededException;
import org.apache.commons.math3.exception.NoBracketingException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.PrecisionExceededException;
import org.apache.commons.math3.exception.ZeroException;
import org.junit.Test;

public class UnivariateRealSolverUtilsTest {

    @Test
    public void testSolve() throws Exception {
        UnivariateRealFunction f = new PolynomialFunction(new double[]{-1.0, 2.0, -1.0, 1.0, 0.0});
        double solution = UnivariateRealSolverUtils.solve(f, -2.0, 2.0);
        assertEquals(0.0, solution, 1e-9);

        f = new InterpolatingPolynomial(new double[]{-1.0, 1.0, 2.0, 3.0, 3.5}, new double[]{1.0, 4.0, 5.0, 6.0, 7.0});
        solution = UnivariateRealSolverUtils.solve(f, -1.0, 4.0);
        assertEquals(2.5, solution, 1e-9);

        UnivariateFunction f1 = new FunctionUtils.SineFunction();
        Solver solver = LazyHolder.FACTORY.newBisectionSolver();
        solution = UnivariateRealSolverUtils.solve(f1, 0.0, Math.PI / 2.0);
        assertEquals(Math.PI / 4.0, solution, 1e-9);
    }

    @Test
    public void testSolveWithAbsoluteAccuracy() throws Exception {
        UnivariateRealFunction f = new PolynomialFunction(new double[]{-1.0, 2.0, -1.0, 1.0, 0.0});
        double solution = UnivariateRealSolverUtils.solve(f, -2.0, 2.0, 1e-6);
        assertEquals(0.0, solution, 1e-9);

        f = new InterpolatingPolynomial(new double[]{-1.0, 1.0, 2.0, 3.0, 3.5}, new double[]{1.0, 4.0, 5.0, 6.0, 7.0});
        solution = UnivariateRealSolverUtils.solve(f, -1.0, 4.0, 1e-6);
        assertEquals(2.5, solution, 1e-9);

        UnivariateFunction f1 = new FunctionUtils.SineFunction();
        Solver solver = LazyHolder.FACTORY.newBisectionSolver();
        solution = UnivariateRealSolverUtils.solve(f1, 0.0, Math.PI / 2.0, 1e-6);
        assertEquals(Math.PI / 4.0, solution, 1e-9);
    }

    @Test
    public void testSolveWithNegativeAbsoluteAccuracy() throws Exception {
        UnivariateRealFunction f = new PolynomialFunction(new double[]{-1.0, 2.0, -1.0, 1.0, 0.0});
        assertThrows(IllegalArgumentException.class, () -> UnivariateRealSolverUtils.solve(f, -2.0, 2.0, -1e-6));

        f = new InterpolatingPolynomial(new double[]{-1.0, 1.0, 2.0, 3.0, 3.5}, new double[]{1.0, 4.0, 5.0, 6.0, 7.0});
        assertThrows(IllegalArgumentException.class, () -> UnivariateRealSolverUtils.solve(f, -1.0, 4.0, -1e-6));

        UnivariateFunction f1 = new FunctionUtils.SineFunction();
        Solver solver = LazyHolder.FACTORY.newBisectionSolver();
        assertThrows(IllegalArgumentException.class, () -> UnivariateRealSolverUtils.solve(f1, 0.0, Math.PI / 2.0, -1e-6));
    }

    @Test
    public void testBracket() throws Exception {
        UnivariateRealFunction function = new PolynomialFunction(new double[]{-1.0, 2.0, -1.0, 1.0, 0.0});
        double[] bracketingValues = UnivariateRealSolverUtils.bracket(function, 0.0, -1.0, 1.0);
        assertArrayEquals(new double[]{0.0, 1.0}, bracketingValues);

        function = new InterpolatingPolynomial(new double[]{-1.0, 1.0, 2.0, 3.0, 3.5}, new double[]{1.0, 4.0, 5.0, 6.0, 7.0});
        bracketingValues = UnivariateRealSolverUtils.bracket(function, 0.0, 0.0, 8.0);
        assertArrayEquals(new double[]{2.5, 8.0}, bracketingValues);

        function = new FunctionUtils.SineFunction();
        bracketingValues = UnivariateRealSolverUtils.bracket(function, 0.0, 0.0, Math.PI);
        assertArrayEquals(new double[]{Math.PI / 2.0, Math.PI}, bracketingValues);
    }

    @Test
    public void testBracketWithMaxIterations() throws Exception {
        UnivariateRealFunction function = new PolynomialFunction(new double[]{-1.0, 2.0, -1.0, 1.0, 0.0});
        assertThrows(NoBracketingException.class, () -> UnivariateRealSolverUtils.bracket(function, 0.0, -1.0, 1.0, 0));

        function = new InterpolatingPolynomial(new double[]{-1.0, 1.0, 2.0, 3.0, 3.5}, new double[]{1.0, 4.0, 5.0, 6.0, 7.0});
        assertThrows(NoBracketingException.class, () -> UnivariateRealSolverUtils.bracket(function, 0.0, 0.0, 8.0, 0));

        function = new FunctionUtils.SineFunction();
        double[] bracketingValues = UnivariateRealSolverUtils.bracket(function, 0.0, 0.0, Math.PI, 1);
        assertArrayEquals(new double[]{0.0, Math.PI}, bracketingValues);
    }

    @Test
    public void testBracketWithNegativeMaxIterations() throws Exception {
        UnivariateRealFunction function = new PolynomialFunction(new double[]{-1.0, 2.0, -1.0, 1.0, 0.0});
        assertThrows(IllegalArgumentException.class, () -> UnivariateRealSolverUtils.bracket(function, 0.0, -1.0, 1.0, -1));

        function = new InterpolatingPolynomial(new double[]{-1.0, 1.0, 2.0, 3.0, 3.5}, new double[]{1.0, 4.0, 5.0, 6.0, 7.0});
        assertThrows(IllegalArgumentException.class, () -> UnivariateRealSolverUtils.bracket(function, 0.0, 0.0, 8.0, -1));

        function = new FunctionUtils.SineFunction();
        assertThrows(IllegalArgumentException.class, () -> UnivariateRealSolverUtils.bracket(function, 0.0, 0.0, Math.PI, -1));
    }

    @Test
    public void testMidpoint() throws Exception {
        assertEquals(0.5, UnivariateRealSolverUtils.midpoint(1.0, 2.0), 1e-9);
        assertEquals(-0.5, UnivariateRealSolverUtils.midpoint(-1.0, 0.0), 1e-9);
        assertEquals(0.0, UnivariateRealSolverUtils.midpoint(0.0, 0.0), 1e-9);
    }

    @Test
    public void testSetup() throws Exception {
        UnivariateRealFunction f = null;
        assertThrows(NullArgumentException.class, () -> UnivariateRealSolverUtils.setup(f));
    }

    private static class LazyHolder {
        /** Cached solver factory */
        static final UnivariateRealSolverFactory FACTORY = UnivariateRealSolverFactory.newInstance();
    }
}