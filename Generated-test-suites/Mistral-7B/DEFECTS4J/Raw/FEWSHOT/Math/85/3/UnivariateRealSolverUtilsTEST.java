package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.analysis.polynomials.PolynomialFunction;
import org.apache.commons.math.analysis.solvers.BisectionSolver;
import org.apache.commons.math.analysis.solvers.BrentSolver;
import org.apache.commons.math.analysis.solvers.CubicSolver;
import org.apache.commons.math.analysis.solvers.PowellSolver;
import org.apache.commons.math.analysis.solvers.SecantSolver;
import org.junit.Test;

public class UnivariateRealSolverUtilsTest {

    @Test
    public void testSolve() {
        UnivariateRealFunction f = new PolynomialFunction(new double[]{-1d, 1d, 1d});
        double x0 = 1.0;
        double x1 = 0.0;
        try {
            double root = UnivariateRealSolverUtils.solve(f, x0, x1);
            assertTrue("Root not correctly found", root > 0.0);
        } catch (ConvergenceException e) {
            e.printStackTrace();
        } catch (FunctionEvaluationException e) {
            e.printStackTrace();
        }

        UnivariateRealFunction f1 = new PolynomialFunction(new double[]{-1d, 1d, -1d, 1d});
        double x01 = 1.0;
        double x11 = 2.0;

        try {
            double root1 = UnivariateRealSolverUtils.solve(f1, x01, x11, 1e-5);
            assertTrue("Root not correctly found", root1 > 0.0);
        } catch (ConvergenceException e) {
            e.printStackTrace();
        } catch (FunctionEvaluationException e) {
            e.printStackTrace();
        }

        UnivariateRealFunction f2 = new PolynomialFunction(new double[]{-1d, 1d, -1d, 1d, 1d, -1d, 1d});
        double x02 = 1.0;
        double x12 = 3.0;

        try {
            double root2 = UnivariateRealSolverUtils.solve(f2, x02, x12, 1e-5);
            assertTrue("Root not correctly found", root2 > 0.0);
        } catch (ConvergenceException e) {
            e.printStackTrace();
        } catch (FunctionEvaluationException e) {
            e.printStackTrace();
        }

        UnivariateRealFunction f3 = new PolynomialFunction(new double[]{1d, 1d, 1d, 1d});
        double x03 = 0.0;
        double x13 = 2.0;

        try {
            double root3 = UnivariateRealSolverUtils.solve(f3, x03, x13, 1e-5);
            assertNull("Root should not be found", root3);
        } catch (ConvergenceException e) {
            e.printStackTrace();
        } catch (FunctionEvaluationException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testBracket() {
        UnivariateRealFunction f = new PolynomialFunction(new double[]{1d, -1d, 1d});
        double initial = 1.0;
        double lowerBound = 0.0;
        double upperBound = 2.0;

        try {
            double[] roots = UnivariateRealSolverUtils.bracket(f, initial, lowerBound, upperBound);
            assertArrayEquals("Roots not correctly found", new double[]{0.0, 1.0}, roots);
        } catch (ConvergenceException e) {
            e.printStackTrace();
        } catch (FunctionEvaluationException e) {
            e.printStackTrace();
        }

        UnivariateRealFunction f1 = new PolynomialFunction(new double[]{-1d, 1d, -1d, 1d});
        double initial1 = 1.0;
        double lowerBound1 = 1.0;
        double upperBound1 = 2.0;

        try {
            double[] roots1 = UnivariateRealSolverUtils.bracket(f1, initial1, lowerBound1, upperBound1, 100);
            assertArrayEquals("Roots not correctly found", new double[]{1.0, 2.0}, roots1);
        } catch (ConvergenceException e) {
            e.printStackTrace();
        } catch (FunctionEvaluationException e) {
            e.printStackTrace();
        }

        UnivariateRealFunction f2 = new PolynomialFunction(new double[]{-1d, 1d, -1d, 1d, 1d, -1d, 1d});
        double initial2 = 1.0;
        double lowerBound2 = 1.0;
        double upperBound2 = 3.0;

        try {
            double[] roots2 = UnivariateRealSolverUtils.bracket(f2, initial2, lowerBound2, upperBound2, 100);
            assertArrayEquals("Roots not correctly found", new double[]{1.5, 2.5}, roots2);
        } catch (ConvergenceException e) {
            e.printStackTrace();
        } catch (FunctionEvaluationException e) {
            e.printStackTrace();
        }

        UnivariateRealFunction f3 = new PolynomialFunction(new double[]{1d, 1d, 1d, 1d});
        double initial3 = 0.0;
        double lowerBound3 = 0.0;
        double upperBound3 = 2.0;

        try {
            double[] roots3 = UnivariateRealSolverUtils.bracket(f3, initial3, lowerBound3, upperBound3);
            assertArrayEquals("Roots not correctly found", new double[]{NA}, roots3);
        } catch (ConvergenceException e) {
            e.printStackTrace();
        } catch (FunctionEvaluationException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testSetup() {
        UnivariateRealFunction f = null;

        try {
            UnivariateRealSolverUtils.setup(f);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    private static final double NA = Double.NaN;
}