package org.apache.commons.math.analysis.solvers.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.NoRootsException;
import org.apache.commons.math3.solvers.BrentSolver;
import org.apache.commons.math3.solvers.BrentSolver.BrentSolverOptions;
import org.apache.commons.math3.solvers.BracketingNthOrderBrentSolver;
import org.apache.commons.math3.solvers.BracketingNthOrderBrentSolver.BracketingNthOrderBrentSolverOptions;
import org.apache.commons.math3.solvers.Precision;
import org.junit.Test;

public class UnivariateRealSolverUtilsTest {

    /** A simple polynomial function */
    private static final UnivariateFunction testFunction = new PolynomialFunction(new double[]{-1.0, 4.0, 4.0, -1.0});

    /** Test solver with default settings */
    @Test
    public void testSolveDefault() {
        double root = UnivariateRealSolverUtils.solve(testFunction, 0.0, 2.0);
        assertEquals(1.0, root, Precision.EPSILON);
    }

    /** Test solver with custom absolute accuracy */
    @Test
    public void testSolveCustomAccuracy() {
        double root = UnivariateRealSolverUtils.solve(testFunction, 0.0, 2.0, 0.001);
        assertEquals(1.0, root, 0.001);
    }

    /** Test solver with invalid arguments */
    @Test
    public void testSolveInvalidArguments() {
        assertThrows(MathIllegalArgumentException.class, () -> UnivariateRealSolverUtils.solve(null, 0.0, 2.0));
        assertThrows(MathIllegalArgumentException.class, () -> UnivariateRealSolverUtils.solve(testFunction, 2.0, 0.0));
        assertThrows(ConvergenceException.class, () -> UnivariateRealSolverUtils.solve(testFunction, 1.0, 1.0));
    }

    /** Test bracketing with default settings */
    @Test
    public void testBracketDefault() {
        double[] roots = UnivariateRealSolverUtils.bracket(testFunction, 0.0, 2.0);
        assertEquals(1.0, roots[0], Precision.EPSILON);
        assertEquals(1.0, roots[1], Precision.EPSILON);
    }

    /** Test bracketing with custom maximum iterations */
    @Test
    public void testBracketCustomIterations() {
        double[] roots = UnivariateRealSolverUtils.bracket(testFunction, 0.0, 2.0, 20);
        assertEquals(1.0, roots[0], Precision.EPSILON);
        assertEquals(1.0, roots[1], Precision.EPSILON);
    }

    /** Test bracketing with no roots */
    @Test
    public void testBracketNoRoots() {
        UnivariateFunction function = new PolynomialFunction(new double[]{0.0, 0.0, 0.0, 0.0});
        assertThrows(NoRootsException.class, () -> UnivariateRealSolverUtils.bracket(function, 0.0, 2.0));
    }

    /** Test bracketing with invalid arguments */
    @Test
    public void testBracketInvalidArguments() {
        assertThrows(MathIllegalArgumentException.class, () -> UnivariateRealSolverUtils.bracket(null, 0.0, 2.0));
        assertThrows(MathIllegalArgumentException.class, () -> UnivariateRealSolverUtils.bracket(testFunction, 2.0, 0.0));
        assertThrows(MathIllegalArgumentException.class, () -> UnivariateRealSolverUtils.bracket(testFunction, 1.0, 1.0));
        assertThrows(MathIllegalArgumentException.class, () -> UnivariateRealSolverUtils.bracket(testFunction, 0.0, 1.0, -1));
    }

    /** Test Brent solver */
    @Test
    public void testBrentSolver() {
        BrentSolverOptions options = new BrentSolverOptions();
        BrentSolver solver = new BrentSolver(options);
        double root = solver.solve(testFunction, 0.0, 2.0);
        assertEquals(1.0, root, Precision.EPSILON);
    }

    /** Test Brent solver with custom settings */
    @Test
    public void testBrentSolverCustomSettings() {
        BrentSolverOptions options = new BrentSolverOptions();
        options.setMaxIterations(1000);
        BrentSolver solver = new BrentSolver(options);
        double root = solver.solve(testFunction, 0.0, 2.0);
        assertEquals(1.0, root, Precision.EPSILON);
    }

    /** Test Brent solver with no roots */
    @Test
    public void testBrentSolverNoRoots() {
        UnivariateFunction function = new PolynomialFunction(new double[]{0.0, 0.0, 0.0, 0.0});
        assertThrows(NoRootsException.class, () -> new BrentSolver().solve(function, 0.0, 2.0));
    }

    /** Test Bracketing Nth Order Brent solver */
    @Test
    public void testBracketingNthOrderBrentSolver() {
        BracketingNthOrderBrentSolverOptions options = new BracketingNthOrderBrentSolverOptions();
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(options);
        double[] roots = solver.solve(testFunction, 0.0, 2.0);
        assertEquals(1.0, roots[0], Precision.EPSILON);
        assertEquals(1.0, roots[1], Precision.EPSILON);
    }

    /** Test Bracketing Nth Order Brent solver with custom settings */
    @Test
    public void testBracketingNthOrderBrentSolverCustomSettings() {
        BracketingNthOrderBrentSolverOptions options = new BracketingNthOrderBrentSolverOptions();
        options.setMaxIterations(1000);
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(options);
        double[] roots = solver.solve(testFunction, 0.0, 2.0);
        assertEquals(1.0, roots[0], Precision.EPSILON);
        assertEquals(1.0, roots[1], Precision.EPSILON);
    }

    /** Test Bracketing Nth Order Brent solver with no roots */
    @Test
    public void testBracketingNthOrderBrentSolverNoRoots() {
        UnivariateFunction function = new PolynomialFunction(new double[]{0.0, 0.0, 0.0, 0.0});
        assertThrows(NoRootsException.class, () -> new BracketingNthOrderBrentSolver().solve(function, 0.0, 2.0));
    }

    /** Test bracketing with maximum iterations */
    @Test
    public void testBracketMaxIterations() {
        double[] roots = UnivariateRealSolverUtils.bracket(testFunction, 0.0, 2.0, 10);
        assertEquals(1.0, roots[0], Precision.EPSILON);
        assertEquals(1.0, roots[1], Precision.EPSILON);
    }

    /** Test bracketing with no root bracketed */
    @Test
    public void testBracketNoRootBracketed() {
        UnivariateFunction function = new PolynomialFunction(new double[]{-1.0, 4.0, 4.0, -1.0});
        assertThrows(MaxIterationsExceededException.class, () -> UnivariateRealSolverUtils.bracket(function, 0.0, 2.0, 1));
    }
}