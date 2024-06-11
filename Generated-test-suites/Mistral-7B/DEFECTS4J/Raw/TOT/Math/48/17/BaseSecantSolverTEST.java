package org.apache.commons.math.analysis.solvers.tests;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.NoBracketingException;
import org.apache.commons.math3.util.Precision;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

public class BaseSecantSolverTest {

    /** A simple quadratic function for testing. */
    private static final UnivariateRealFunction FUNCTION = new UnivariateRealFunction() {
        @Override
        public double value(double x) {
            return x * x - 2 * x + 1;
        }

        @Override
        public double derivative(double x) {
            return 2 * x - 2;
        }
    };

    /** A test for the {@link BaseSecantSolver} with the {@link RegulaFalsiSolver} method. */
    @Test
    public void testRegulaFalsi() {
        final double maxEval = 20;
        final double relativeAccuracy = 1e-3;
        final double absoluteAccuracy = 1e-4;
        final double functionValueAccuracy = 1e-4;
        final BaseSecantSolver solver = new RegulaFalsiSolver(relativeAccuracy, absoluteAccuracy, functionValueAccuracy);
        final double root = 1.0;

        // Test with bounds that bracket the root
        final double[] bounds = new double[] {-1.5, 2.5};
        final double result = solver.solve(maxEval, FUNCTION, bounds[0], bounds[1], null);
        assertEquals(root, Precision.round(result, 10), functionValueAccuracy);

        // Test with bounds that do not bracket the root
        final double[] wrongBounds = new double[] {-1.5, 1.5};
        assertThrows(NoBracketingException.class, () -> solver.solve(maxEval, FUNCTION, wrongBounds[0], wrongBounds[1], null));

        // Test with maximum number of iterations exceeded
        final int maxIterations = 5;
        assertThrows(MaxIterationsExceededException.class, () -> solver.solve(maxIterations, FUNCTION, bounds[0], bounds[1], null));

        // Test with a convergent function
        final double[] convergentBounds = new double[] {-0.5, 0.5};
        final double convergentRoot = 0.0;
        final double convergentResult = solver.solve(maxEval, FUNCTION, convergentBounds[0], convergentBounds[1], null);
        assertEquals(convergentRoot, Precision.round(convergentResult, 10), functionValueAccuracy);

        // Test with a non-convergent function
        final UnivariateRealFunction nonConvergentFunction = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.sin(Math.PI * x) / x;
            }

            @Override
            public double derivative(double x) {
                return Math.cos(Math.PI * x) / x - Math.PI * Math.sin(Math.PI * x) / x;
            }
        };
        final double[] nonConvergentBounds = new double[] {-1.0, 1.0};
        assertThrows(ConvergenceException.class, () -> solver.solve(maxEval, nonConvergentFunction, nonConvergentBounds[0], nonConvergentBounds[1], null));

        // Test with a function with a singularity at the root
        final UnivariateRealFunction singularFunction = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.sqrt(x - 1) * Math.log(x);
            }

            @Override
            public double derivative(double x) {
                return (1.0 / (2.0 * Math.sqrt(x - 1)) - Math.log(x) / x);
            }
        };
        final double[] singularBounds = new double[] {1.5, 2.0};
        final double singularResult = 1.0;
        final double singularTolerance = 1e-4;
        final double singularAbsoluteTolerance = 1e-5;
        final BaseSecantSolver singularSolver = new RegulaFalsiSolver(relativeAccuracy, singularAbsoluteTolerance);
        final double singularResultCalc = singularSolver.solve(maxEval, singularFunction, singularBounds[0], singularBounds[1], null);
        assertTrue(Precision.isClose(singularResult, singularResultCalc, singularTolerance));
    }

    /** A test for the {@link BaseSecantSolver} with the {@link IllinoisSolver} method. */
    @Test
    public void testIllinois() {
        final double maxEval = 20;
        final double relativeAccuracy = 1e-3;
        final double absoluteAccuracy = 1e-4;
        final double functionValueAccuracy = 1e-4;
        final BaseSecantSolver solver = new IllinoisSolver(relativeAccuracy, absoluteAccuracy, functionValueAccuracy);
        final double root = 1.0;

        // Test with bounds that bracket the root
        final double[] bounds = new double[] {-1.5, 2.5};
        final double result = solver.solve(maxEval, FUNCTION, bounds[0], bounds[1], null);
        assertEquals(root, Precision.round(result, 10), functionValueAccuracy);

        // Test with bounds that do not bracket the root
        final double[] wrongBounds = new double[] {-1.5, 1.5};
        assertThrows(NoBracketingException.class, () -> solver.solve(maxEval, FUNCTION, wrongBounds[0], wrongBounds[1], null));

        // Test with maximum number of iterations exceeded
        final int maxIterations = 5;
        assertThrows(MaxIterationsExceededException.class, () -> solver.solve(maxIterations, FUNCTION, bounds[0], bounds[1], null));

        // Test with a convergent function
        final double[] convergentBounds = new double[] {-0.5, 0.5};
        final double convergentRoot = 0.0;
        final double convergentResult = solver.solve(maxEval, FUNCTION, convergentBounds[0], convergentBounds[1], null);
        assertEquals(convergentRoot, Precision.round(convergentResult, 10), functionValueAccuracy);

        // Test with a non-convergent function
        final UnivariateRealFunction nonConvergentFunction = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.sin(Math.PI * x) / x;
            }

            @Override
            public double derivative(double x) {
                return Math.cos(Math.PI * x) / x - Math.PI * Math.sin(Math.PI * x) / x;
            }
        };
        final double[] nonConvergentBounds = new double[] {-1.0, 1.0};
        assertThrows(ConvergenceException.class, () -> solver.solve(maxEval, nonConvergentFunction, nonConvergentBounds[0], nonConvergentBounds[1], null));

        // Test with a function with a singularity at the root
        final UnivariateRealFunction singularFunction = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.sqrt(x - 1) * Math.log(x);
            }

            @Override
            public double derivative(double x) {
                return (1.0 / (2.0 * Math.sqrt(x - 1)) - Math.log(x) / x);
            }
        };
        final double[] singularBounds = new double[] {1.5, 2.0};
        final double singularResult = 1.0;
        final double singularTolerance = 1e-4;
        final double singularAbsoluteTolerance = 1e-5;
        final BaseSecantSolver singularSolver = new IllinoisSolver(relativeAccuracy, singularAbsoluteTolerance);
        final double singularResultCalc = singularSolver.solve(maxEval, singularFunction, singularBounds[0], singularBounds[1], null);
        assertTrue(Precision.isClose(singularResult, singularResultCalc, singularTolerance));
    }

    /** A test for the {@link BaseSecantSolver} with the {@link PegasusSolver} method. */
    @Test
    public void testPegasus() {
        final double maxEval = 20;
        final double relativeAccuracy = 1e-3;
        final double absoluteAccuracy = 1e-4;
        final double functionValueAccuracy = 1e-4;
        final BaseSecantSolver solver = new PegasusSolver(relativeAccuracy, absoluteAccuracy, functionValueAccuracy);
        final double root = 1.0;

        // Test with bounds that bracket the root
        final double[] bounds = new double[] {-1.5, 2.5};
        final double result = solver.solve(maxEval, FUNCTION, bounds[0], bounds[1], null);
        assertEquals(root, Precision.round(result, 10), functionValueAccuracy);

        // Test with bounds that do not bracket the root
        final double[] wrongBounds = new double[] {-1.5, 1.5};
        assertThrows(NoBracketingException.class, () -> solver.solve(maxEval, FUNCTION, wrongBounds[0], wrongBounds[1], null));

        // Test with maximum number of iterations exceeded
        final int maxIterations = 5;
        assertThrows(MaxIterationsExceededException.class, () -> solver.solve(maxIterations, FUNCTION, bounds[0], bounds[1], null));

        // Test with a convergent function
        final double[] convergentBounds = new double[] {-0.5, 0.5};
        final double convergentRoot = 0.0;
        final double convergentResult = solver.solve(maxEval, FUNCTION, convergentBounds[0], convergentBounds[1], null);
        assertEquals(convergentRoot, Precision.round(convergentResult, 10), functionValueAccuracy);

        // Test with a non-convergent function
        final UnivariateRealFunction nonConvergentFunction = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.sin(Math.PI * x) / x;
            }

            @Override
            public double derivative(double x) {
                return Math.cos(Math.PI * x) / x - Math.PI * Math.sin(Math.PI * x) / x;
            }
        };
        final double[] nonConvergentBounds = new double[] {-1.0, 1.0};
        assertThrows(ConvergenceException.class, () -> solver.solve(maxEval, nonConvergentFunction, nonConvergentBounds[0], nonConvergentBounds[1], null));

        // Test with a function with a singularity at the root
        final UnivariateRealFunction singularFunction = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.sqrt(x - 1) * Math.log(x);
            }

            @Override
            public double derivative(double x) {
                return (1.0 / (2.0 * Math.sqrt(x - 1)) - Math.log(x) / x);
            }
        };
        final double[] singularBounds = new double[] {1.5, 2.0};
        final double singularResult = 1.0;
        final double singularTolerance = 1e-4;
        final double singularAbsoluteTolerance = 1e-5;
        final BaseSecantSolver singularSolver = new PegasusSolver(relativeAccuracy, singularAbsoluteTolerance);
        final double singularResultCalc = singularSolver.solve(maxEval, singularFunction, singularBounds[0], singularBounds[1], null);
        assertTrue(Precision.isClose(singularResult, singularResultCalc, singularTolerance));
    }

    /** Test method to verify bracketing of initial solution. */
    @Test
    public void testVerifyBracketing() {
        final BaseSecantSolver solver = new RegulaFalsiSolver(1.0, 1.0);
        final double x0 = 1.5;
        final double x1 = 1.0;
        final double f0 = solver.computeObjectiveValue(x0);
        final double f1 = solver.computeObjectiveValue(x1);
        solver.verifyBracketing(x0, x1);
        assertFalse(f1 * f0 > 0);
    }
}