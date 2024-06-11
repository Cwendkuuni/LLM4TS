package org.apache.commons.math3.optimization.general;

import static org.junit.Assert.*;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.analysis.solvers.BrentSolver;
import org.apache.commons.math3.exception.MathIllegalStateException;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.PointValuePair;
import org.apache.commons.math3.optimization.SimpleValueChecker;
import org.apache.commons.math3.optimization.ConvergenceChecker;
import org.apache.commons.math3.optimization.NonLinearConjugateGradientOptimizer;
import org.apache.commons.math3.optimization.ConjugateGradientFormula;
import org.apache.commons.math3.optimization.Preconditioner;
import org.apache.commons.math3.util.Arrays;
import org.apache.commons.math3.analysis.polynomials.PolynomialFunction;
import org.apache.commons.math3.analysis.solvers.BisectionSolver;

/**
 * Test class for {@link NonLinearConjugateGradientOptimizer}.
 */
public class NonLinearConjugateGradientOptimizerTest {

    /**
     * Test method for {@link NonLinearConjugateGradientOptimizer#NonLinearConjugateGradientOptimizer(ConjugateGradientFormula, ConvergenceChecker, UnivariateSolver, Preconditioner)}
     * and {@link NonLinearConjugateGradientOptimizer#setInitialStep(double)}.
     */
    @ org.junit.Test
    public void testNonLinearConjugateGradientOptimizer() {
        // Test with Fletcher-Reeves update formula
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(
                ConjugateGradientFormula.FLETCHER_REEVES,
                new SimpleValueChecker(),
                new BrentSolver(),
                new IdentityPreconditioner());
        optimizer.setInitialStep(2.0);

        // Test with Polak-Ribiére update formula
        NonLinearConjugateGradientOptimizer optimizer2 = new NonLinearConjugateGradientOptimizer(
                ConjugateGradientFormula.POLAK_RIBIERE,
                new SimpleValueChecker(),
                new BrentSolver(),
                new IdentityPreconditioner());

        // Test with custom convergence checker
        ConvergenceChecker<PointValuePair> convergenceChecker = new ConvergenceChecker<PointValuePair>() {
            @Override
            public boolean converged(int iterations, PointValuePair startValue, PointValuePair currentValue) {
                return currentValue.getValue() <= 1e-6;
            }
        };
        NonLinearConjugateGradientOptimizer optimizer3 = new NonLinearConjugateGradientOptimizer(
                ConjugateGradientFormula.FLETCHER_REEVES,
                convergenceChecker,
                new BrentSolver(),
                new IdentityPreconditioner());

        // Test with custom line search solver
        UnivariateSolver customLineSearchSolver = new BisectionSolver();
        NonLinearConjugateGradientOptimizer optimizer4 = new NonLinearConjugateGradientOptimizer(
                ConjugateGradientFormula.FLETCHER_REEVES,
                new SimpleValueChecker(),
                customLineSearchSolver,
                new IdentityPreconditioner());

        // Test with custom preconditioner
        Preconditioner customPreconditioner = new Preconditioner() {
            @Override
            public double[] precondition(double[] variables, double[] r) {
                double[] result = new double[variables.length];
                for (int i = 0; i < variables.length; ++i) {
                    result[i] = variables[i] + r[i] * 0.5;
                }
                return result;
            }
        };
        NonLinearConjugateGradientOptimizer optimizer5 = new NonLinearConjugateGradientOptimizer(
                ConjugateGradientFormula.FLETCHER_REEVES,
                new SimpleValueChecker(),
                new BrentSolver(),
                customPreconditioner);

        // Test optimization of a simple quadratic function
        UnivariateFunction function = new PolynomialFunction(new double[]{-1, 2, -1});
        double[] startPoint = {0.0};
        PointValuePair result = optimizer.optimize(function, startPoint, GoalType.MINIMIZE);
        assertEquals(0.5, result.getKey()[0], 1e-6);
        assertEquals(0.25, result.getValue(), 1e-6);

        // Test optimization with custom convergence checker
        result = optimizer3.optimize(function, startPoint, GoalType.MINIMIZE);
        assertEquals(0.5, result.getKey()[0], 1e-6);
        assertEquals(0.25, result.getValue(), 1e-6);

        // Test optimization with custom line search solver
        result = optimizer4.optimize(function, startPoint, GoalType.MINIMIZE);
        assertEquals(0.5, result.getKey()[0], 1e-6);
        assertEquals(0.25, result.getValue(), 1e-6);

        // Test optimization with custom preconditioner
        result = optimizer5.optimize(function, startPoint, GoalType.MINIMIZE);
        assertEquals(0.5, result.getKey()[0], 1e-6);
        assertEquals(0.25, result.getValue(), 1e-6);

        // Test optimization with incorrect update formula
        try {
            NonLinearConjugateGradientOptimizer optimizer6 = new NonLinearConjugateGradientOptimizer(
                    ConjugateGradientFormula.POLAK_RIBIERE,
                    new SimpleValueChecker(),
                    new BrentSolver(),
                    new IdentityPreconditioner());
            optimizer6.setUpdateFormula(ConjugateGradientFormula.FLETCHER_REEVES);
            optimizer6.optimize(function, startPoint, GoalType.MINIMIZE);
            fail("Unexpected optimization result");
        } catch (IllegalStateException e) {
            // Expected
        }

        // Test optimization with null convergence checker
        try {
            NonLinearConjugateGradientOptimizer optimizer7 = new NonLinearConjugateGradientOptimizer(
                    ConjugateGradientFormula.FLETCHER_REEVES,
                    null,
                    new BrentSolver(),
                    new IdentityPreconditioner());
            optimizer7.optimize(function, startPoint, GoalType.MINIMIZE);
            fail("Unexpected optimization result");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Test optimization with null line search solver
        try {
            NonLinearConjugateGradientOptimizer optimizer8 = new NonLinearConjugateGradientOptimizer(
                    ConjugateGradientFormula.FLETCHER_REEVES,
                    new SimpleValueChecker(),
                    null,
                    new IdentityPreconditioner());
            optimizer8.optimize(function, startPoint, GoalType.MINIMIZE);
            fail("Unexpected optimization result");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Test optimization with null preconditioner
        try {
            NonLinearConjugateGradientOptimizer optimizer9 = new NonLinearConjugateGradientOptimizer(
                    ConjugateGradientFormula.FLETCHER_REEVES,
                    new SimpleValueChecker(),
                    new BrentSolver(),
                    null);
            optimizer9.optimize(function, startPoint, GoalType.MINIMIZE);
            fail("Unexpected optimization result");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Test optimization with incorrect goal type
        try {
            NonLinearConjugateGradientOptimizer optimizer10 = new NonLinearConjugateGradientOptimizer(
                    ConjugateGradientFormula.FLETCHER_REEVES,
                    new SimpleValueChecker(),
                    new BrentSolver(),
                    new IdentityPreconditioner());
            optimizer10.setGoalType(GoalType.MAXIMIZE);
            optimizer10.optimize(function, startPoint, GoalType.MINIMIZE);
            fail("Unexpected optimization result");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Test optimization with incorrect initialization step
        try {
            NonLinearConjugateGradientOptimizer optimizer11 = new NonLinearConjugateGradientOptimizer(
                    ConjugateGradientFormula.FLETCHER_REEVES,
                    new SimpleValueChecker(),
                    new BrentSolver(),
                    new IdentityPreconditioner());
            optimizer11.setInitialStep(-1.0);
            optimizer11.optimize(function, startPoint, GoalType.MINIMIZE);
            fail("Unexpected optimization result");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Test optimization with incorrect number of variables
        UnivariateFunction function2 = new PolynomialFunction(new double[]{0.0, 0.0, -1, 2, -1});
        double[] startPoint2 = {0.0, 0.0};
        try {
            NonLinearConjugateGradientOptimizer optimizer12 = new NonLinearConjugateGradientOptimizer(
                    ConjugateGradientFormula.FLETCHER_REEVES,
                    new SimpleValueChecker(),
                    new BrentSolver(),
                    new IdentityPreconditioner());
            optimizer12.optimize(function2, startPoint2, GoalType.MINIMIZE);
            fail("Unexpected optimization result");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Test optimization with incorrect number of variables in preconditioner
        Preconditioner customPreconditioner2 = new Preconditioner() {
            @Override
            public double[] precondition(double[] variables, double[] r) {
                double[] result = new double[variables.length];
                for (int i = 0; i < variables.length; ++i) {
                    result[i] = variables[i] + r[i] * 0.5;
                    result[i + 1] = variables[i] + r[i] * 0.5;
                }
                return result;
            }
        };
        try {
            NonLinearConjugateGradientOptimizer optimizer13 = new NonLinearConjugateGradientOptimizer(
                    ConjugateGradientFormula.FLETCHER_REEVES,
                    new SimpleValueChecker(),
                    new BrentSolver(),
                    customPreconditioner2);
            optimizer13.optimize(function, startPoint, GoalType.MINIMIZE);
            fail("Unexpected optimization result");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }
}

class IdentityPreconditioner implements Preconditioner {
    @Override
    public double[] precondition(double[] variables, double[] r) {
        return r.clone();
    }
}