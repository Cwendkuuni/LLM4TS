package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.MaxEvalException;
import org.apache.commons.math3.exception.RootFindersException;
import org.junit.Test;

public class BaseSecantSolverTest {

    @Test
    public void testDoSolve_RegulaFalsi() {
        BaseSecantSolver solver = new RegulaFalsiSolver(1e-6, RegulaFalsiSolver.RegulaFalsiMethod.REGULA_FALSI);
        double x = solver.solve(10, new MyFunction(), 0.0, 2.0);
        assertEquals(1.0, x, 1e-6);
    }

    @Test
    public void testDoSolve_Illinois() {
        BaseSecantSolver solver = new IllinoisSolver(1e-6, IllinoisSolver.IllinoisMethod.ILLINOIS);
        double x = solver.solve(10, new MyFunction(), 0.0, 2.0);
        assertEquals(1.0, x, 1e-6);
    }

    @Test
    public void testDoSolve_Pegasus() {
        BaseSecantSolver solver = new PegasusSolver(1e-6, PegasusSolver.PegasusMethod.PEGASUS);
        double x = solver.solve(10, new MyFunction(), 0.0, 2.0);
        assertEquals(1.0, x, 1e-6);
    }

    @Test(expected = RootFindersException.class)
    public void testDoSolve_FailedBracketing() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI) {
            @Override
            protected void verifyBracketing(double min, double max) {
                // Do nothing, just test that the method still throws an exception
            }
        };
        solver.solve(10, new MyFunction(), 0.0, 0.0);
    }

    @Test(expected = MaxEvalException.class)
    public void testDoSolve_MaxEval() {
        BaseSecantSolver solver = new BaseSecantSolver(1, BaseSecantSolver.Method.ILLINOIS) {
            @Override
            public double solve(int maxEval, UnivariateRealFunction f, double min, double max, double startValue) {
                throw new UnsupportedOperationException();
            }
        };
        solver.solve(1, new MyFunction(), 0.0, 2.0, 0.0);
    }

    @Test
    public void testGetMethods() {
        assertEquals(3, BaseSecantSolver.Method.values().length);
    }

    @Test
    public void testAllowedSolutions() {
        assertArrayEquals(new AllowedSolution[] { AllowedSolution.ANY_SIDE, AllowedSolution.LEFT_SIDE,
            AllowedSolution.RIGHT_SIDE, AllowedSolution.BELOW_SIDE, AllowedSolution.ABOVE_SIDE },
            BaseSecantSolver.AllowedSolution.values());
    }

    @Test
    public void testAllowedSolutions_AnySide() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);
        AllowedSolution allowed = solver.getAllowedSolution();
        assertEquals(AllowedSolution.ANY_SIDE, allowed);
    }

    @Test
    public void testAllowedSolutions_LeftSide() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI, AllowedSolution.LEFT_SIDE);
        AllowedSolution allowed = solver.getAllowedSolution();
        assertEquals(AllowedSolution.LEFT_SIDE, allowed);
    }

    @Test
    public void testAllowedSolutions_RightSide() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI, AllowedSolution.RIGHT_SIDE);
        AllowedSolution allowed = solver.getAllowedSolution();
        assertEquals(AllowedSolution.RIGHT_SIDE, allowed);
    }

    @Test
    public void testAllowedSolutions_BelowSide() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI, AllowedSolution.BELOW_SIDE);
        AllowedSolution allowed = solver.getAllowedSolution();
        assertEquals(AllowedSolution.BELOW_SIDE, allowed);
    }

    @Test
    public void testAllowedSolutions_AboveSide() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI, AllowedSolution.ABOVE_SIDE);
        AllowedSolution allowed = solver.getAllowedSolution();
        assertEquals(AllowedSolution.ABOVE_SIDE, allowed);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAllowedSolutions_Invalid() {
        new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI, 123);
    }

    @Test
    public void testAllowedSolutions_Switch() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI) {
            @Override
            public AllowedSolution getAllowedSolution() {
                return AllowedSolution.BELOW_SIDE;
            }
        };
        AllowedSolution allowed = solver.getAllowedSolution();
        assertEquals(AllowedSolution.BELOW_SIDE, allowed);

        solver.setAllowedSolution(AllowedSolution.RIGHT_SIDE);
        allowed = solver.getAllowedSolution();
        assertEquals(AllowedSolution.RIGHT_SIDE, allowed);
    }

    @Test
    public void testSetAllowedSolution() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI) {
            @Override
            public AllowedSolution getAllowedSolution() {
                return AllowedSolution.LEFT_SIDE;
            }
        };
        solver.setAllowedSolution(AllowedSolution.RIGHT_SIDE);
        AllowedSolution allowed = solver.getAllowedSolution();
        assertEquals(AllowedSolution.RIGHT_SIDE, allowed);
    }

    @Test
    public void testSetAllowedSolution_Invalid() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI) {
            @Override
            public AllowedSolution getAllowedSolution() {
                return AllowedSolution.LEFT_SIDE;
            }
        };
        solver.setAllowedSolution(123);
    }

    private static class MyFunction implements UnivariateRealFunction {
        @Override
        public double value(double x) {
            return Math.pow(x, 2) - 1.0;
        }

        @Override
        public int getNumberOfVariables() {
            return 1;
        }
    }
}