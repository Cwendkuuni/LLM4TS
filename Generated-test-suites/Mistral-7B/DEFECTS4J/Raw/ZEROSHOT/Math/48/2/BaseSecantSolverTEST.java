package org.apache.commons.math.analysis.solvers.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.analysis.solvers.BracketedUnivariateRealSolver;
import org.apache.commons.math3.analysis.solvers.BracketedUnivariateRealSolver.AllowedSolution;
import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.MaxEvalExceededException;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.special.Erf;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.RandomDataGenerator;
import org.junit.Test;

public class BaseSecantSolverTest {

    @Test
    public void testSolve_RegulaFalsi() throws ConvergenceException,
            MathIllegalArgumentException {
        UnivariateFunction function = new FunctionSqrt();
        double min = -10.0;
        double max = 10.0;
        double startValue = 0.0;
        BracketedUnivariateRealSolver<UnivariateRealFunction> solver = new RegulaFalsiSolver(1.0e-4,
                new RegulaFalsiSolver.RegulaFalsiMethod());
        double result = solver.solve(100, function, min, max, startValue,
                AllowedSolution.LEFT_SIDE);
        assertEquals(2.0, Precision.round(result, 4));
    }

    @Test
    public void testSolve_Illinois() throws ConvergenceException,
            MathIllegalArgumentException {
        UnivariateFunction function = new FunctionSqrt();
        double min = -10.0;
        double max = 10.0;
        double startValue = 0.0;
        BracketedUnivariateRealSolver<UnivariateRealFunction> solver = new IllinoisSolver(1.0e-4,
                1.0e-4, 1.0e-4, new IllinoisSolver.IllinoisMethod());
        double result = solver.solve(100, function, min, max, startValue,
                AllowedSolution.LEFT_SIDE);
        assertEquals(2.0, Precision.round(result, 4));
    }

    @Test
    public void testSolve_Pegasus() throws ConvergenceException,
            MathIllegalArgumentException {
        UnivariateFunction function = new FunctionCbrt();
        double min = -10.0;
        double max = 10.0;
        double startValue = 0.0;
        BracketedUnivariateRealSolver<UnivariateRealFunction> solver = new PegasusSolver(1.0e-4,
                1.0e-4, 1.0e-4, new PegasusSolver.PegasusMethod());
        double result = solver.solve(100, function, min, max, startValue,
                AllowedSolution.LEFT_SIDE);
        assertEquals(1.5, Precision.round(result, 4));
    }

    @Test(expected = MaxEvalExceededException.class)
    public void testSolve_MaxEvalExceeded() throws ConvergenceException,
            MathIllegalArgumentException {
        UnivariateFunction function = new FunctionSin();
        double min = -10.0;
        double max = 10.0;
        double startValue = 0.0;
        BracketedUnivariateRealSolver<UnivariateRealFunction> solver = new RegulaFalsiSolver(1.0e-4,
                new RegulaFalsiSolver.RegulaFalsiMethod());
        solver.solve(0, function, min, max, startValue, AllowedSolution.LEFT_SIDE);
    }

    @Test(expected = MathIllegalArgumentException.class)
    public void testSolve_InvalidFunction() throws ConvergenceException,
            MathIllegalArgumentException {
        UnivariateRealFunction function = null;
        double min = 0.0;
        double max = 1.0;
        double startValue = 1.0;
        BracketedUnivariateRealSolver<UnivariateRealFunction> solver = new RegulaFalsiSolver(1.0e-4,
                new RegulaFalsiSolver.RegulaFalsiMethod());
        solver.solve(10, function, min, max, startValue, AllowedSolution.LEFT_SIDE);
    }

    @Test(expected = MathIllegalArgumentException.class)
    public void testSolve_InvalidStartValue() throws ConvergenceException,
            MathIllegalArgumentException {
        UnivariateFunction function = new FunctionSqrt();
        double min = -10.0;
        double max = 10.0;
        double startValue = 10.0;
        BracketedUnivariateRealSolver<UnivariateRealFunction> solver = new RegulaFalsiSolver(1.0e-4,
                new RegulaFalsiSolver.RegulaFalsiMethod());
        solver.solve(10, function, min, max, startValue, AllowedSolution.LEFT_SIDE);
    }

    @Test(expected = MathIllegalArgumentException.class)
    public void testSolve_InvalidAllowedSolution() throws ConvergenceException,
            MathIllegalArgumentException {
        UnivariateFunction function = new FunctionSqrt();
        double min = -10.0;
        double max = 10.0;
        double startValue = 0.0;
        BracketedUnivariateRealSolver<UnivariateRealFunction> solver = new RegulaFalsiSolver(1.0e-4,
                new RegulaFalsiSolver.RegulaFalsiMethod());
        solver.solve(10, function, min, max, startValue, AllowedSolution.RIGHT_SIDE);
    }

    @Test
    public void testSolve_InvalidAllowedSolution_RegulaFalsi() throws ConvergenceException,
            MathIllegalArgumentException {
        UnivariateFunction function = new FunctionSqrt();
        double min = -10.0;
        double max = 10.0;
        double startValue = 0.0;
        BracketedUnivariateRealSolver<UnivariateRealFunction> solver = new RegulaFalsiSolver(1.0e-4,
                new RegulaFalsiSolver.RegulaFalsiMethod());
        assertThrows(MathIllegalArgumentException.class,
                () -> solver.solve(10, function, min, max, startValue, AllowedSolution.RIGHT_SIDE));
    }

    @Test
    public void testSolve_InvalidAllowedSolution_Illinois() throws ConvergenceException,
            MathIllegalArgumentException {
        UnivariateFunction function = new FunctionSqrt();
        double min = -10.0;
        double max = 10.0;
        double startValue = 0.0;
        BracketedUnivariateRealSolver<UnivariateRealFunction> solver = new IllinoisSolver(1.0e-4,
                1.0e-4, 1.0e-4, new IllinoisSolver.IllinoisMethod());
        assertThrows(MathIllegalArgumentException.class,
                () -> solver.solve(10, function, min, max, startValue, AllowedSolution.RIGHT_SIDE));
    }

    @Test
    public void testSolve_InvalidAllowedSolution_Pegasus() throws ConvergenceException,
            MathIllegalArgumentException {
        UnivariateFunction function = new FunctionSqrt();
        double min = -10.0;
        double max = 10.0;
        double startValue = 0.0;
        BracketedUnivariateRealSolver<UnivariateRealFunction> solver = new PegasusSolver(1.0e-4,
                1.0e-4, 1.0e-4, new PegasusSolver.PegasusMethod());
        assertThrows(MathIllegalArgumentException.class,
                () -> solver.solve(10, function, min, max, startValue, AllowedSolution.RIGHT_SIDE));
    }

    private static class FunctionSqrt implements UnivariateFunction {

        @Override
        public double value(double x) {
            return Math.sqrt(x);
        }

        @Override
        public double derive(double x) {
            return 0.5 * Math.sqrt(x);
        }
    }

    private static class FunctionCbrt implements UnivariateFunction {

        @Override
        public double value(double x) {
            return Math.cbrt(x);
        }

        @Override
        public double derive(double x) {
            return 0.333333333 * Math.cbrt(x);
        }
    }

    private static class FunctionSin implements UnivariateFunction {

        @Override
        public double value(double x) {
            return Math.sin(x);
        }

        @Override
        public double derive(double x) {
            return Math.cos(x);
        }
    }
}

class RandomDataGenerator {

    public double nextGaussian() {
        double v1, v2, s;
        do {
            v1 = 2 * Math.random() - 1;
            v2 = 2 * Math.random() - 1;
            s = v1 * v1 + v2 * v2;
        } while (s >= 1 || s == 0);

        double z0 = Math.sqrt(-2 * Math.log(s) / s);
        return v1 * z0;
    }
}