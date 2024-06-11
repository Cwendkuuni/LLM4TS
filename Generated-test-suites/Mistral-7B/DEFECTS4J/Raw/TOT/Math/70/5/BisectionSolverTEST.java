package org.apache.commons.math.analysis.solvers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.PrecisionRequirementExceededException;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;

/**
 * Tests for the {@link BisectionSolver} class.
 */
public class BisectionSolverTest {

    /** A simple function for testing. */
    private static final UnivariateRealFunction FUNCTION = new UnivariateRealFunction() {
        @Override
        public double value(double x) {
            return x * x - 1.0;
        }
    };

    /**
     * Test solver with a simple function and a reasonable interval.
     */
    @Test
    public void testSimple() {
        BisectionSolver solver = new BisectionSolver();
        double zero = solver.solve(FUNCTION, -2.0, 2.0);
        assertEquals(0.0, zero, 1.0E-6);
    }

    /**
     * Test solver with a simple function and invalid interval.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testInvalidInterval() {
        BisectionSolver solver = new BisectionSolver();
        solver.solve(FUNCTION, -2.0, -2.0);
    }

    /**
     * Test solver with a simple function and initial guess out of interval.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testInitialOutOfInterval() {
        BisectionSolver solver = new BisectionSolver();
        solver.solve(FUNCTION, -2.0, 2.0, 3.0);
    }

    /**
     * Test solver with a simple function and max iterations exceeded.
     */
    @Test(expected = MaxIterationsExceededException.class)
    public void testMaxIterationsExceeded() {
        BisectionSolver solver = new BisectionSolver(2);
        solver.solve(FUNCTION, -2.0, 2.0);
    }

    /**
     * Test solver with a function that has no root in the interval.
     */
    @Test
    public void testNoRoot() {
        BisectionSolver solver = new BisectionSolver();
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) / x;
            }
        };
        assertThrows(FunctionEvaluationException.class, () -> {
            solver.solve(function, -1.0, 1.0);
        });
    }

    /**
     * Test solver with a function that throws an exception on evaluation.
     */
    @Test(expected = PrecisionRequirementExceededException.class)
    public void testExceptionOnEvaluation() {
        BisectionSolver solver = new BisectionSolver();
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) throws NumberIsTooSmallException {
                if (Precision.isZero(x, 1.0E-6)) {
                    throw new NumberIsTooSmallException();
                }
                return Math.log(x);
            }
        };
        solver.solve(function, -1.0, 1.0);
    }
}