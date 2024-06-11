package org.apache.commons.math.analysis.solvers.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.MathInternalError;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.TestDouble;
import org.junit.Before;
import org.junit.Test;

import org.apache.commons.math.analysis.solvers.BaseSecantSolver;
import org.apache.commons.math.analysis.solvers.BracketedUnivariateRealSolver;
import org.apache.commons.math.analysis.solvers.AllowedSolution;
import org.apache.commons.math.analysis.solvers.RegulaFalsiSolver;
import org.apache.commons.math.analysis.solvers.IllinoisSolver;
import org.apache.commons.math.analysis.solvers.PegasusSolver;

/**
 * Test class for {@link BaseSecantSolver}.
 */
public class BaseSecantSolverTest {

    private UnivariateRealFunction f1;
    private UnivariateRealFunction f2;
    private UnivariateRealFunction f3;

    @Before
    public void setUp() {
        f1 = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 2;
            }
        };

        f2 = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.log(Math.abs(x - 1));
            }
        };

        f3 = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 3) - 2;
            }
        };
    }

    @Test
    public void testSolver_RegulaFalsi() {
        final double absAccuracy = 1e-5;
        final double relAccuracy = 1e-4;

        final BaseSecantSolver solver = new RegulaFalsiSolver(absAccuracy, f1);
        assertEquals(0.0, solver.solve(100), absAccuracy);

        final BaseSecantSolver solver1 = new RegulaFalsiSolver(relAccuracy, absAccuracy, f1);
        assertEquals(0.0, solver1.solve(100), relAccuracy);

        final BaseSecantSolver solver2 = new RegulaFalsiSolver(relAccuracy, absAccuracy, 1e-4, f1);
        assertEquals(0.0, solver2.solve(100), relAccuracy);

        final BaseSecantSolver solver3 = new RegulaFalsiSolver(relAccuracy, absAccuracy, 1e-4, f1, AllowedSolution.LEFT_SIDE);
        assertEquals(-1.0, solver3.solve(100), relAccuracy);

        final BaseSecantSolver solver4 = new RegulaFalsiSolver(relAccuracy, absAccuracy, 1e-4, f1, AllowedSolution.RIGHT_SIDE);
        assertEquals(1.0, solver4.solve(100), relAccuracy);

        final BaseSecantSolver solver5 = new RegulaFalsiSolver(relAccuracy, absAccuracy, 1e-4, f1, AllowedSolution.BELOW_SIDE);
        assertEquals(-Math.sqrt(2.0), solver5.solve(100), relAccuracy);

        final BaseSecantSolver solver6 = new RegulaFalsiSolver(relAccuracy, absAccuracy, 1e-4, f1, AllowedSolution.ABOVE_SIDE);
        assertEquals(Math.sqrt(2.0), solver6.solve(100), relAccuracy);

        // Test exception when min > max
        assertThrows(MathIllegalArgumentException.class, () -> new RegulaFalsiSolver(1.0, f1, 5.0, 3.0));

        // Test exception when startValue is outside brackets
        assertThrows(MathIllegalArgumentException.class, () -> new RegulaFalsiSolver(1.0, f1, 3.0, 5.0, 2.0));

        // Test exception when allowed solution is not supported
        assertThrows(UnsupportedOperationException.class, () -> new RegulaFalsiSolver(1.0, f1, 3.0, 5.0, AllowedSolution.ABOVE_BELOW));
    }

    @Test
    public void testSolver_Illinois() {
        final double absAccuracy = 1e-5;
        final double relAccuracy = 1e-4;

        final BaseSecantSolver solver = new IllinoisSolver(absAccuracy, f2);
        assertEquals(0.0, solver.solve(100), absAccuracy);

        final BaseSecantSolver solver1 = new IllinoisSolver(relAccuracy, absAccuracy, f2);
        assertEquals(0.0, solver1.solve(100), relAccuracy);

        final BaseSecantSolver solver2 = new IllinoisSolver(relAccuracy, absAccuracy, 1e-4, f2);
        assertEquals(0.0, solver2.solve(100), relAccuracy);

        final BaseSecantSolver solver3 = new IllinoisSolver(relAccuracy, absAccuracy, 1e-4, f2, AllowedSolution.LEFT_SIDE);
        assertEquals(-1.0, solver3.solve(100), relAccuracy);

        final BaseSecantSolver solver4 = new IllinoisSolver(relAccuracy, absAccuracy, 1e-4, f2, AllowedSolution.RIGHT_SIDE);
        assertEquals(1.0, solver4.solve(100), relAccuracy);

        final BaseSecantSolver solver5 = new IllinoisSolver(relAccuracy, absAccuracy, 1e-4, f2, AllowedSolution.BELOW_SIDE);
        assertEquals(1.0, solver5.solve(100), relAccuracy);

        final BaseSecantSolver solver6 = new IllinoisSolver(relAccuracy, absAccuracy, 1e-4, f2, AllowedSolution.ABOVE_SIDE);
        assertEquals(-1.0, solver6.solve(100), relAccuracy);

        // Test exception when min > max
        assertThrows(MathIllegalArgumentException.class, () -> new IllinoisSolver(1.0, f2, 5.0, 3.0));

        // Test exception when startValue is outside brackets
        assertThrows(MathIllegalArgumentException.class, () -> new IllinoisSolver(1.0, f2, 3.0, 5.0, 2.0));

        // Test exception when allowed solution is not supported
        assertThrows(UnsupportedOperationException.class, () -> new IllinoisSolver(1.0, f2, 3.0, 5.0, AllowedSolution.ABOVE_BELOW));
    }

    @Test
    public void testSolver_Pegasus() {
        final double absAccuracy = 1e-5;
        final double relAccuracy = 1e-4;

        final BaseSecantSolver solver = new PegasusSolver(absAccuracy, f3);
        assertEquals(-Math.pow(0.5, 1.0 / 3.0), solver.solve(100), absAccuracy);

        final BaseSecantSolver solver1 = new PegasusSolver(relAccuracy, absAccuracy, f3);
        assertEquals(-Math.pow(0.5, 1.0 / 3.0), solver1.solve(100), relAccuracy);

        final BaseSecantSolver solver2 = new PegasusSolver(relAccuracy, absAccuracy, 1e-4, f3);
        assertEquals(-Math.pow(0.5, 1.0 / 3.0), solver2.solve(100), relAccuracy);

        final BaseSecantSolver solver3 = new PegasusSolver(relAccuracy, absAccuracy, 1e-4, f3, AllowedSolution.LEFT_SIDE);
        assertEquals(-Math.pow(0.5, 1.0 / 3.0), solver3.solve(100), relAccuracy);

        final BaseSecantSolver solver4 = new PegasusSolver(relAccuracy, absAccuracy, 1e-4, f3, AllowedSolution.RIGHT_SIDE);
        assertEquals(-Math.pow(0.5, 1.0 / 3.0), solver4.solve(100), relAccuracy);

        final BaseSecantSolver solver5 = new PegasusSolver(relAccuracy, absAccuracy, 1e-4, f3, AllowedSolution.BELOW_SIDE);
        assertEquals(-Math.pow(0.5, 1.0 / 3.0), solver5.solve(100), relAccuracy);

        final BaseSecantSolver solver6 = new PegasusSolver(relAccuracy, absAccuracy, 1e-4, f3, AllowedSolution.ABOVE_SIDE);
        assertEquals(-Math.pow(0.5, 1.0 / 3.0), solver6.solve(100), relAccuracy);

        // Test exception when min > max
        assertThrows(MathIllegalArgumentException.class, () -> new PegasusSolver(1.0, f3, 5.0, 3.0));

        // Test exception when startValue is outside brackets
        assertThrows(MathIllegalArgumentException.class, () -> new PegasusSolver(1.0, f3, 3.0, 5.0, 2.0));

        // Test exception when allowed solution is not supported
        assertThrows(UnsupportedOperationException.class, () -> new PegasusSolver(1.0, f3, 3.0, 5.0, AllowedSolution.ABOVE_BELOW));

        // Test convergence exception
        final BaseSecantSolver solver7 = new PegasusSolver(1e-10, f3, 0.0, 10.0);
        assertThrows(ConvergenceException.class, () -> solver7.solve(100));
    }

    @Test
    public void testSolver_ConvergenceException() {
        final double absAccuracy = 1e-5;
        final double relAccuracy = 1e-4;

        final UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return (Math.pow(x, 2) - 1) / (x - 2);
            }
        };

        final BaseSecantSolver solver = new PegasusSolver(relAccuracy, absAccuracy, 0.0, 10.0, f);
        assertThrows(ConvergenceException.class, () -> solver.solve(100));
    }
}