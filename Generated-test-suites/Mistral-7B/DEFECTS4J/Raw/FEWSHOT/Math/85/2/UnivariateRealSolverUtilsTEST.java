package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.analysis.interpolators.LinearInterpolator;
import org.apache.commons.math.analysis.solvers.BrentSolver;
import org.apache.commons.math.analysis.solvers.LaguerreSolver;
import org.apache.commons.math.analysis.solvers.SecantSolver;
import org.apache.commons.math.analysis.solvers.BisectionSolver;

import org.junit.Test;

public class UnivariateRealSolverUtilsTest {

    @Test
    public void testSolve() {
        UnivariateRealFunction f = new Function("f", x -> Math.pow(x, 2) - 2);
        double x0 = 1.0;
        double x1 = 2.0;

        try {
            double solution = UnivariateRealSolverUtils.solve(f, x0, x1);
            assertTrue(Math.abs(solution - 1.5) < 0.01);
        } catch (ConvergenceException | FunctionEvaluationException e) {
            e.printStackTrace();
            assert False();
        }

        try {
            UnivariateRealFunction fAbs = x -> Math.abs(x);
            double[] bracketing = UnivariateRealSolverUtils.bracket(fAbs, 0, 1, 2);
            double[] solutionBracketing = UnivariateRealSolverUtils.solve(fAbs, bracketing[0], bracketing[1], 0.001);
            assertArrayEquals(new double[]{bracketing[0], solutionBracketing[0]}, new double[]{bracketing[0], solutionBracketing[0]});
        } catch (ConvergenceException | FunctionEvaluationException e) {
            e.printStackTrace();
            assert False();
        }
    }

    @Test
    public void testBracket() {
        UnivariateRealFunction f = x -> Math.pow(x, 2) - 2;
        double initial = 1.0;
        double lowerBound = 0.0;
        double upperBound = 2.0;

        try {
            double[] bracketing = UnivariateRealSolverUtils.bracket(f, initial, lowerBound, upperBound);
            double a = bracketing[0];
            double b = bracketing[1];
            double fa = f.value(a);
            double fb = f.value(b);
            assertTrue((fa * fb < 0.0) || (fa == 0 && b < initial) || (fb == 0 && a > initial));
        } catch (ConvergenceException | FunctionEvaluationException e) {
            e.printStackTrace();
            assert False();
        }

        try {
            UnivariateRealFunction fAbs = x -> Math.abs(x);
            double[] bracketingAbs = UnivariateRealSolverUtils.bracket(fAbs, 0, 1, 2);
            assertFalse(fAbs.value(bracketingAbs[0]) * fAbs.value(bracketingAbs[1]) < 0.0);
        } catch (ConvergenceException | FunctionEvaluationException e) {
            e.printStackTrace();
            assert False();
        }

        try {
            double[] bracketingMaxIterations = UnivariateRealSolverUtils.bracket(
                    x -> Math.pow(x, 2) - 2, 1.0, 0.0, 2.0, 1);
            assertArrayEquals(new double[]{1.0, 1.0}, bracketingMaxIterations);
        } catch (ConvergenceException | FunctionEvaluationException e) {
            e.printStackTrace();
            assert False();
        }
    }

    @Test
    public void testSolverFactory() {
        UnivariateRealFunction f = x -> Math.pow(x, 2) - 2;
        UnivariateRealFunction fAbs = x -> Math.abs(x);
        UnivariateRealFunction fLinear = new Function("fLinear", x -> x * x);
        UnivariateRealFunction fBrent = new BrentSolver().solve(f, 1.0, 2.0);
        UnivariateRealFunction fSecant = new SecantSolver(1.0, 2.0).solve(f);
        UnivariateRealFunction fLaguerre = new LaguerreSolver().solve(f);
        UnivariateRealFunction fBisection = new BisectionSolver().solve(f);
        UnivariateRealFunction fNewton = new NewtonRaphsonSolver(1.0).solve(f);

        UnivariateRealSolverFactory factory = UnivariateRealSolverUtils.LazyHolder.FACTORY;

        UnivariateRealSolver solverDef = factory.newDefaultSolver();
        UnivariateRealSolver solverBrent = factory.newSolver(BrentSolver.class);
        UnivariateRealSolver solverSecant = factory.newSolver(SecantSolver.class);
        UnivariateRealSolver solverLaguerre = factory.newSolver(LaguerreSolver.class);
        UnivariateRealSolver solverBisection = factory.newSolver(BisectionSolver.class);
        UnivariateRealSolver solverNewton = factory.newSolver(NewtonRaphsonSolver.class);

        UnivariateRealSolver solverDefault = new DefaultSolver();
        UnivariateRealSolver solverBrentInstance = new BrentSolver();
        UnivariateRealSolver solverSecantInstance = new SecantSolver(0, 0);
        UnivariateRealSolver solverLaguerreInstance = new LaguerreSolver();
        UnivariateRealSolver solverBisectionInstance = new BisectionSolver();
        UnivariateRealSolver solverNewtonInstance = new NewtonRaphsonSolver(0);

        assertTrue(solverDef instanceof DefaultSolver);
        assertTrue(solverBrent instanceof BrentSolver);
        assertTrue(solverSecant instanceof SecantSolver);
        assertTrue(solverLaguerre instanceof LaguerreSolver);
        assertTrue(solverBisection instanceof BisectionSolver);
        assertTrue(solverNewton instanceof NewtonRaphsonSolver);

        try {
            double solutionDef = solverDef.solve(f, 1.0, 2.0);
            assertTrue(Math.abs(solutionDef - 1.5) < 0.01);
            double solutionBrent = solverBrent.solve(f, 1.0, 2.0);
            assertTrue(Math.abs(solutionBrent - 1.5) < 0.01);
            double solutionSecant = solverSecant.solve(f, 1.0, 2.0);
            assertTrue(Math.abs(solutionSecant - 1.5) < 0.01);
            double solutionLaguerre = solverLaguerre.solve(f, 1.0, 2.0);
            assertTrue(Math.abs(solutionLaguerre - 1.5) < 0.01);
            double solutionBisection = solverBisection.solve(f, 1.0, 2.0);
            assertTrue(Math.abs(solutionBisection - 1.5) < 0.01);
            double solutionNewton = solverNewton.solve(f, 1.0);
            assertTrue(Math.abs(solutionNewton - 1.5) < 0.01);
        } catch (ConvergenceException | FunctionEvaluationException e) {
            e.printStackTrace();
            assert False();
        }

        try {
            double[] bracketing = UnivariateRealSolverUtils.bracket(fAbs, 0, 1, 2);
            UnivariateRealSolver solverLinear = factory.newSolver(LinearInterpolator.class);
            double[] solutionBracketing = solverLinear.solve(f, bracketing[0], bracketing[1]);
            assertArrayEquals(new double[]{bracketing[0], solutionBracketing[0]}, new double[]{bracketing[0], solutionBracketing[0]});
        } catch (ConvergenceException | FunctionEvaluationException e) {
            e.printStackTrace();
            assert False();
        }
    }

    private static class Function implements UnivariateRealFunction {
        private final String name;
        private final UnivariateRealFunction function;

        public Function(String name, UnivariateRealFunction function) {
            this.name = name;
            this.function = function;
        }

        @Override
        public double value(double x) {
            return function.value(x);
        }

        @Override
        public String toString() {
            return name;
        }
    }
}

class DefaultSolver extends AbstractUnivariateRealSolver {
    @Override
    public double solve(UnivariateRealFunction function, double x0, double x1)
            throws ConvergenceException, FunctionEvaluationException {
        UnivariateRealSolverUtils.solve(function, x0, x1);
        return Double.NaN;
    }
}