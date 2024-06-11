package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.analysis.interpolators.LinearInterpolator;
import org.apache.commons.math.analysis.solvers.BrentSolver;
import org.apache.commons.math.analysis.solvers.BracketingBrentSolver;
import org.apache.commons.math.analysis.solvers.LaguerreSolver;
import org.apache.commons.math.analysis.solvers.SecantSolver;
import org.apache.commons.math.analysis.solvers.BisectionSolver;

import java.util.Arrays;

public class UnivariateRealSolverUtilsTest {

    @Test
    public void testSolve() throws Exception {
        UnivariateRealFunction testFunction = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(Math.sin(x), 2) - 0.5 * Math.pow(x, 2) + 2;
            }
        };

        double x0 = 0.0;
        double x1 = 2.0;
        double root = UnivariateRealSolverUtils.solve(testFunction, x0, x1);
        assertTrue(Math.abs(root - 1.27324024629672) < 1e-6);

        UnivariateRealFunction testFunction2 = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.exp(x) - Math.sin(x) - 1;
            }
        };

        x0 = -1.0;
        x1 = 1.0;
        try {
            UnivariateRealSolverUtils.solve(testFunction2, x0, x1);
            fail("Expected FunctionEvaluationException");
        } catch (FunctionEvaluationException e) {
            // expected
        }

        UnivariateRealFunction testFunction3 = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return 1 / (x * x) - 1 / (x + 1);
            }
        };

        x0 = 1.0;
        x1 = 3.0;
        root = UnivariateRealSolverUtils.solve(testFunction3, x0, x1);
        assertTrue(Math.abs(root - 0.36602540378444) < 1e-6);

        UnivariateRealFunction testFunction4 = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(Math.E, x) - Math.sin(x) - 1;
            }
        };

        x0 = -3.0;
        x1 = -1.0;
        root = UnivariateRealSolverUtils.solve(testFunction4, x0, x1);
        assertTrue(Math.abs(root + 1.1107200133914) < 1e-6);

        // Test with different solvers
        UnivariateRealFunction testFunctionBrent = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(Math.sin(x), 2) - 0.5 * Math.pow(x, 2) + 2;
            }
        };

        x0 = 0.0;
        x1 = 2.0;
        root = UnivariateRealSolverUtils.solve(testFunctionBrent, x0, x1,
                new BrentSolver());
        assertTrue(Math.abs(root - 1.27324024629672) < 1e-6);

        UnivariateRealFunction testFunctionSecant = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(Math.sin(x), 2) - 0.5 * Math.pow(x, 2) + 2;
            }
        };

        x0 = 0.0;
        x1 = 1.5;
        double x2 = 1.0;
        root = UnivariateRealSolverUtils.solve(testFunctionSecant, x0, x1,
                new SecantSolver(x1, x2));
        assertTrue(Math.abs(root - 1.27324024629672) < 1e-6);

        UnivariateRealFunction testFunctionBracketingBrent = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(Math.sin(x), 2) - 0.5 * Math.pow(x, 2) + 2;
            }
        };

        x0 = 0.0;
        x1 = 2.0;
        double[] bracketingRoot = UnivariateRealSolverUtils.bracket(testFunctionBracketingBrent, x0, x0, x1)[0];
        root = UnivariateRealSolverUtils.solve(testFunctionBracketingBrent, x0, x1,
                new BracketingBrentSolver());
        assertArrayEquals(new double[]{bracketingRoot[0], root}, new double[]{1.27324024629672, 1.27324024629672});

        UnivariateRealFunction testFunctionLaguerre = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2) - 2 * x + 1;
            }
        };

        x0 = 1.0;
        x1 = 3.0;
        root = UnivariateRealSolverUtils.solve(testFunctionLaguerre, x0, x1,
                new LaguerreSolver());
        assertTrue(Math.abs(root - 2.0) < 1e-6);

        UnivariateRealFunction testFunctionBisection = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) - x;
            }
        };

        x0 = -1.0;
        x1 = 1.0;
        root = UnivariateRealSolverUtils.solve(testFunctionBisection, x0, x1,
                new BisectionSolver());
        assertTrue(Math.abs(root) < 1e-6);
    }

    @Test
    public void testBracket() throws Exception {
        UnivariateRealFunction testFunction = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(Math.sin(x), 2) - 0.5 * Math.pow(x, 2) + 2;
            }
        };

        double[] bracketingRoot = UnivariateRealSolverUtils.bracket(testFunction, 0.0, 2.0)[0];
        assertArrayEquals(new double[]{1.27324024629672, 1.42193433602273}, bracketingRoot, 0.001);

        UnivariateRealFunction testFunction2 = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return 1 / (x * x) - 1 / (x + 1);
            }
        };

        bracketingRoot = UnivariateRealSolverUtils.bracket(testFunction2, 1.0, 3.0)[0];
        assertArrayEquals(new double[]{0.36602540378444, 0.42098598635376}, bracketingRoot, 0.001);

        UnivariateRealFunction testFunction3 = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.exp(x) - Math.sin(x) - 1;
            }
        };

        bracketingRoot = UnivariateRealSolverUtils.bracket(testFunction3, -3.0, -1.0)[0];
        assertArrayEquals(new double[]{-1.1107200133914, -0.93221465753594}, bracketingRoot, 0.001);
    }
}