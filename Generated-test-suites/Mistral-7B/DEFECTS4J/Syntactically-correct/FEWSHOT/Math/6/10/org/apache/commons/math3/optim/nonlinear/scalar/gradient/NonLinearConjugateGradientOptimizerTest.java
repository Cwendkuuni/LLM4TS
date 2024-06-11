package org.apache.commons.math3.optimization.general;

import static org.junit.Assert.*;
import org.junit.Test;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.analysis.solvers.BrentSolver;
import org.apache.commons.math3.analysis.solvers.LevenbergMarquardtIterativeSolver;
import org.apache.commons.math3.analysis.solvers.UnivariateSolver;
import org.apache.commons.math3.exception.MathIllegalStateException;
import org.apache.commons.math3.exception.NoBracketingException;
import org.apache.commons.math3.optim.*;
import org.apache.commons.math3.optim.InitialGuess;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleValueChecker;
import org.apache.commons.math3.optim.convergence.ConvergenceChecker;
import org.apache.commons.math3.optim.initializer.AroundMinimumInitialGuesser;
import org.apache.commons.math3.optim.nonlinear.gradient.ConjugateGradientOptimizer;
import org.apache.commons.math3.optim.preconditions.IdentityPreconditioner;
import org.apache.commons.math3.optim.preconditions.NoPreconditioning;
import org.apache.commons.math3.optim.preconditions.Preconditioner;
import org.apache.commons.math3.optim.solvers.SimpleLineSearch;
import org.apache.commons.math3.optim.nonlinear.functions.PolynomialFunction;

public class NonLinearConjugateGradientOptimizerTest {

    @Test
    public void testNonLinearConjugateGradientOptimizer() {
        ConjugateGradientFormula updateFormula = ConjugateGradientFormula.POLAK_RIBIERE;
        ConvergenceChecker<PointValuePair> checker = new SimpleValueChecker(1e-6);
        UnivariateSolver lineSearchSolver = new BrentSolver();
        Preconditioner preconditioner = new IdentityPreconditioner();
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(updateFormula, checker, lineSearchSolver, preconditioner);
        double[] startPoint = {1.0, 1.0};
        GoalType goalType = GoalType.MINIMIZE;

        UnivariateFunction function = new PolynomialFunction(2, -1, 1, 0.1);
        OptimizationProblem problem = new OptimizationProblem(function, startPoint);
        PointValuePair solution = optimizer.optimize(problem);
        assertEquals(goalType, problem.getGoalType());
        assertEquals(goalType, solution.getGoalType());
        double[] expectedSolution = {1.0, 0.5};
        assertEquals(expectedSolution[0], solution.getPoint()[0], 1e-6);
        assertEquals(expectedSolution[1], solution.getPoint()[1], 1e-6);
        assertEquals(function.value(expectedSolution), solution.getValue(), 1e-6);
    }

    @Test(expected = MathIllegalStateException.class)
    public void testNonLinearConjugateGradientOptimizerNullFunction() {
        ConjugateGradientFormula updateFormula = ConjugateGradientFormula.POLAK_RIBIERE;
        ConvergenceChecker<PointValuePair> checker = new SimpleValueChecker(1e-6);
        UnivariateSolver lineSearchSolver = new BrentSolver();
        Preconditioner preconditioner = new IdentityPreconditioner();
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(updateFormula, checker, lineSearchSolver, preconditioner);
        OptimizationProblem problem = null;
        PointValuePair solution = optimizer.optimize(problem);
    }

    @Test(expected = NoBracketingException.class)
    public void testNonLinearConjugateGradientOptimizerNoBracketing() throws Exception {
        ConjugateGradientFormula updateFormula = ConjugateGradientFormula.POLAK_RIBIERE;
        ConvergenceChecker<PointValuePair> checker = new SimpleValueChecker(1e-6);
        UnivariateSolver lineSearchSolver = new BrentSolver();
        Preconditioner preconditioner = new NoPreconditioning();
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(updateFormula, checker, lineSearchSolver, preconditioner);
        UnivariateFunction function = new PolynomialFunction(2, -1, 1, 0.1);
        OptimizationProblem problem = new OptimizationProblem(function, new double[] {1.0, 1.0});
        problem.setMaxEvaluations(0);
        PointValuePair solution = optimizer.optimize(problem);
    }

    @Test
    public void testNonLinearConjugateGradientOptimizerPreconditions() {
        ConjugateGradientFormula updateFormula = ConjugateGradientFormula.POLAK_RIBIERE;
        ConvergenceChecker<PointValuePair> checker = new SimpleValueChecker(1e-6);
        UnivariateSolver lineSearchSolver = new BrentSolver();
        Preconditioner preconditioner = new IdentityPreconditioner();
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(updateFormula, checker, lineSearchSolver, preconditioner);
        UnivariateFunction function = new PolynomialFunction(2, -1, 1, 0.1);
        OptimizationProblem problem = new OptimizationProblem(function, new double[] {1.0, 1.0});
        problem.setMaxEvaluations(100);
        PointValuePair solution = optimizer.optimize(problem);
        assertEquals(solution.getValue(), 0.0, 1e-6);

        Preconditioner customPreconditioner = new Preconditioner() {
            @Override
            public double[] precondition(double[] variables, double[] r) {
                double[] result = r.clone();
                for (int i = 0; i < result.length; i++) {
                    result[i] *= 2;
                }
                return result;
            }
        };
        NonLinearConjugateGradientOptimizer optimizerWithCustomPreconditioner = new NonLinearConjugateGradientOptimizer(updateFormula, checker, lineSearchSolver, customPreconditioner);
        PointValuePair solutionWithCustomPreconditioner = optimizerWithCustomPreconditioner.optimize(problem);
        assertNotEquals(solution.getValue(), solutionWithCustomPreconditioner.getValue(), 1e-6);
    }

    @Test
    public void testNonLinearConjugateGradientOptimizerLineSearch() {
        ConjugateGradientFormula updateFormula = ConjugateGradientFormula.POLAK_RIBIERE;
        ConvergenceChecker<PointValuePair> checker = new SimpleValueChecker(1e-6);
        UnivariateSolver lineSearchSolver = new LevenbergMarquardtIterativeSolver();
        Preconditioner preconditioner = new IdentityPreconditioner();
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(updateFormula, checker, lineSearchSolver, preconditioner);
        UnivariateFunction function = new PolynomialFunction(2, -1, 1, 0.1);
        OptimizationProblem problem = new OptimizationProblem(function, new double[] {1.0, 1.0});
        problem.setMaxEvaluations(100);
        PointValuePair solution = optimizer.optimize(problem);
        assertEquals(solution.getValue(), 0.0, 1e-6);

        UnivariateSolver customLineSearchSolver = new SimpleLineSearch();
        NonLinearConjugateGradientOptimizer optimizerWithCustomLineSearchSolver = new NonLinearConjugateGradientOptimizer(updateFormula, checker, customLineSearchSolver, preconditioner);
        PointValuePair solutionWithCustomLineSearchSolver = optimizerWithCustomLineSearchSolver.optimize(problem);
        assertNotEquals(solution.getValue(), solutionWithCustomLineSearchSolver.getValue(), 1e-6);
    }

    @Test
    public void testNonLinearConjugateGradientOptimizerInitialGuesser() {
        ConjugateGradientFormula updateFormula = ConjugateGradientFormula.POLAK_RIBIERE;
        ConvergenceChecker<PointValuePair> checker = new SimpleValueChecker(1e-6);
        UnivariateSolver lineSearchSolver = new BrentSolver();
        Preconditioner preconditioner = new IdentityPreconditioner();
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(updateFormula, checker, lineSearchSolver, preconditioner);
        UnivariateFunction function = new PolynomialFunction(2, -1, 1, 0.1);
        OptimizationProblem problem = new OptimizationProblem(function, null);
        problem.setInitialGuess(new AroundMinimumInitialGuesser(function, 1.0));
        problem.setMaxEvaluations(100);
        PointValuePair solution = optimizer.optimize(problem);
        assertEquals(solution.getValue(), 0.0, 1e-6);
    }

    @Test
    public void testNonLinearConjugateGradientOptimizerInitialStep() {
        ConjugateGradientFormula updateFormula = ConjugateGradientFormula.POLAK_RIBIERE;
        ConvergenceChecker<PointValuePair> checker = new SimpleValueChecker(1e-6);
        UnivariateSolver lineSearchSolver = new BrentSolver();
        Preconditioner preconditioner = new IdentityPreconditioner();
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(updateFormula, checker, lineSearchSolver, preconditioner);
        UnivariateFunction function = new PolynomialFunction(2, -1, 1, 0.1);
        OptimizationProblem problem = new OptimizationProblem(function, new double[] {1.0, 1.0});
        problem.setInitialStep(2.0);
        problem.setMaxEvaluations(100);
        PointValuePair solution = optimizer.optimize(problem);
        assertEquals(solution.getValue(), 0.0, 1e-6);
    }
}