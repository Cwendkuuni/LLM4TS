package org.apache.commons.cli.test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.apache.commons.commons.math.analysis.FunctionUtils;
import org.apache.commons.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.commons.math.exception.ConvergenceException;
import org.apache.commons.commons.math.exception.MaxIterationsExceededException;
import org.apache.commons.commons.math.exception.MathInternalError;
import org.apache.commons.commons.math.util.Precision;
import org.apache.commons.commons.math.util.FastMath;
import org.apache.commons.commons.math.analysis.solvers.BaseSecantSolver;
import org.apache.commons.commons.math.analysis.solvers.BracketedUnivariateRealSolver;
import org.apache.commons.commons.math.analysis.solvers.RegulaFalsiSolver;
import org.apache.commons.commons.math.analysis.solvers.IllinoisSolver;
import org.apache.commons.commons.math.analysis.solvers.PegasusSolver;

public class BaseSecantSolverTest {

    @Test
    public void testInstance() {
        BaseSecantSolver solver = new RegulaFalsiSolver(0.001, 1e-6);
        assertEquals("RegulaFalsiSolver", solver.getClass().getSimpleName());

        solver = new IllinoisSolver(0.001, 1e-6);
        assertEquals("IllinoisSolver", solver.getClass().getSimpleName());

        solver = new PegasusSolver(0.001, 1e-6);
        assertEquals("PegasusSolver", solver.getClass().getSimpleName());
    }

    @Test
    public void testAllowedSolution() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.PEGASUS) {
            @Override
            protected double doSolve() {
                throw new UnsupportedOperationException();
            }
        };

        assertEquals(BaseSecantSolver.AllowedSolution.ANY_SIDE, solver.getAllowedSolution());

        solver.setAllowedSolution(BaseSecantSolver.AllowedSolution.LEFT_SIDE);
        assertEquals(BaseSecantSolver.AllowedSolution.LEFT_SIDE, solver.getAllowedSolution());

        solver.setAllowedSolution(BaseSecantSolver.AllowedSolution.RIGHT_SIDE);
        assertEquals(BaseSecantSolver.AllowedSolution.RIGHT_SIDE, solver.getAllowedSolution());

        solver.setAllowedSolution(BaseSecantSolver.AllowedSolution.BELOW_SIDE);
        assertEquals(BaseSecantSolver.AllowedSolution.BELOW_SIDE, solver.getAllowedSolution());

        solver.setAllowedSolution(BaseSecantSolver.AllowedSolution.ABOVE_SIDE);
        assertEquals(BaseSecantSolver.AllowedSolution.ABOVE_SIDE, solver.getAllowedSolution());

        try {
            solver.setAllowedSolution(new Enum[]{BaseSecantSolver.AllowedSolution.LEFT_SIDE, BaseSecantSolver.AllowedSolution.RIGHT_SIDE}[0]);
            fail();
        } catch (IllegalArgumentException ex) {
            // Expected
        }

        try {
            solver.setAllowedSolution(null);
            fail();
        } catch (NullPointerException ex) {
            // Expected
        }
    }

    @Test
    public void testSolve_RegulaFalsi() {
        UnivariateRealFunction f = f -> 1.0 - Math.pow(f, 2);

        BaseSecantSolver solver = new RegulaFalsiSolver(0.001, 1e-6);

        double[] roots = {-1.0, 1.0};
        Double[] expectedSolutions = {-0.9953394186371764, 0.9953394186371765};

        for (int i = 0; i < roots.length; i++) {
            double solution = solver.solve(10, f, -1e6, 1e6, roots[i]);
            assertEquals(expectedSolutions[i].toString(), solution, 1e-7);
        }
    }

    @Test
    public void testSolve_Illinois() {
        UnivariateRealFunction f = f -> 1.0 / (1.0 + Math.pow(f, 2));

        BaseSecantSolver solver = new IllinoisSolver(0.001, 1e-6);

        double[] roots = {-1.0, 1.0};
        Double[] expectedSolutions = {-0.9953394186371764, 0.9953394186371765};

        for (int i = 0; i < roots.length; i++) {
            double solution = solver.solve(10, f, -1e6, 1e6, roots[i]);
            assertEquals(expectedSolutions[i].toString(), solution, 1e-7);
        }
    }

    @Test
    public void testSolve_Pegasus() {
        UnivariateRealFunction f = f -> Math.sin(f) - 0.5 * Math.pow(f, 3);

        BaseSecantSolver solver = new PegasusSolver(0.001, 1e-6);

        double[] roots = {-1.0, 1.0};
        Double[] expectedSolutions = {-0.7493912544516203, 0.7493912544516204};

        for (int i = 0; i < roots.length; i++) {
            double solution = solver.solve(10, f, -1e6, 1e6, roots[i]);
            assertEquals(expectedSolutions[i].toString(), solution, 1e-5);
        }
    }

    @Test(expected = ConvergenceException.class)
    public void testSolve_RegulaFalsi_ConvergenceException() {
        UnivariateRealFunction f = f -> Math.pow(Math.E, f) - 2.0;

        BaseSecantSolver solver = new RegulaFalsiSolver(0.001, 1e-6);
        solver.solve(10, f, -1e6, 1e6, 0.0);
    }

    @Test(expected = MaxIterationsExceededException.class)
    public void testSolve_RegulaFalsi_MaxIterationsExceededException() {
        UnivariateRealFunction f = f -> Math.pow(Math.E, f) - 2.0;

        BaseSecantSolver solver = new RegulaFalsiSolver(0.001, 1e-6) {
            @Override
            protected int getMaxIterations() {
                return 0;
            }
        };
        solver.solve(10, f, -1e6, 1e6, 0.0);
    }

    @Test(expected = MathInternalError.class)
    public void testSolve_RegulaFalsi_MathInternalError() {
        UnivariateRealFunction f = f -> Math.pow(Math.E, f) - 2.0;

        BaseSecantSolver solver = new RegulaFalsiSolver(0.001, 1e-6) {
            @Override
            protected double doSolve() {
                throw new RuntimeException();
            }
        };
        solver.solve(10, f, -1e6, 1e6, 0.0);
    }
}