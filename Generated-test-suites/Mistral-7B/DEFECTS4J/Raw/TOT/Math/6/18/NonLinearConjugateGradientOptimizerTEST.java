package org.apache.commons.math3.optimization.general;

import org.apache.commons.math3.analysis.FunctionUtilities;
import org.apache.commons.math3.analysis.solvers.BracketBracketBrentSolver;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleValueChecker;
import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.apache.commons.math3.optim.NonLinearConstraint;
import org.apache.commons.math3.optim.NonLinearConstraintCollection;
import org.apache.commons.math3.optim.NonLinearOptimizationException;
import org.apache.commons.math3.optim.PointValuePairCollection;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test cases for {@link NonLinearConjugateGradientOptimizer}.
 */
public class NonLinearConjugateGradientOptimizerTest {

    /**
     * Test optimization with a simple quadratic function.
     */
    @Test
    public void testQuadraticFunction() {
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return x * x;
            }

            @Override
            public double derivative(double x) {
                return 2 * x;
            }
        };

        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(
            ConjugateGradientFormula.FLETCHER_REEVES,
            new SimpleValueChecker(1e-9, 1e-9),
            new BracketBracketBrentSolver());
        optimizer.setInitialStep(0.1);

        optimizer.optimize(f, new double[]{0.5}, GoalType.MINIMIZE);

        PointValuePair result = optimizer.getOptimum();
        assertEquals(0.0, result.getValue(), 1e-8);
        assertEquals(0.0, result.getPoint()[0], 1e-8);
    }

    /**
     * Test optimization of a non-quadratic function.
     */
    @Test
    public void testNonQuadraticFunction() {
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return FastMath.log(1 + x * x) / x;
            }

            @Override
            public double derivative(double x) {
                double result = (FastMath.log(1 + x * x) / (x * (x + 1)) - 1 / x) * (1 + x * x);
                return result;
            }
        };

        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(
            ConjugateGradientFormula.FLETCHER_REEVES,
            new SimpleValueChecker(1e-9, 1e-9),
            new BracketBracketBrentSolver());
        optimizer.setInitialStep(0.1);

        optimizer.optimize(f, new double[]{1.0}, GoalType.MINIMIZE);

        PointValuePair result = optimizer.getOptimum();
        assertEquals(-1.0, result.getValue(), 1e-8);
        assertEquals(1.0, result.getPoint()[0], 1e-8);
    }

    /**
     * Test optimization with non-linear constraints.
     */
    @Test
    public void testNonLinearConstraints() {
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return x * x;
            }

            @Override
            public double derivative(double x) {
                return 2 * x;
            }
        };

        NonLinearConstraintCollection constraints = new NonLinearConstraintCollection();
        constraints.add(new NonLinearConstraint() {
            @Override
            public double value(double x) {
                return x - 2;
            }

            @Override
            public double[] gradient(double x) {
                return new double[]{1};
            }
        });

        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(
            ConjugateGradientFormula.FLETCHER_REEVES,
            new SimpleValueChecker(1e-9, 1e-9),
            new BracketBracketBrentSolver());
        optimizer.setInitialStep(0.1);

        try {
            optimizer.optimize(f, new double[]{1.5}, GoalType.MINIMIZE, constraints);
            fail("Expected NonLinearOptimizationException");
        } catch (NonLinearOptimizationException e) {
            // expected
        }

        optimizer.optimize(f, new double[]{1.0}, GoalType.MINIMIZE, constraints);

        PointValuePair result = optimizer.getOptimum();
        assertEquals(0.0, result.getValue(), 1e-8);
        assertEquals(1.0, result.getPoint()[0], 1e-8);
    }

    /**
     * Test optimization with a custom preconditioner.
     */
    @Test
    public void testCustomPreconditioner() {
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(Math.sin(x), 2);
            }

            @Override
            public double derivative(double x) {
                return 2 * Math.sin(x) * Math.cos(x);
            }
        };

        PointValuePair initialGuess = new PointValuePair(Math.PI / 2, 1.0);

        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(
            ConjugateGradientFormula.FLETCHER_REEVES,
            new SimpleValueChecker(1e-9, 1e-9),
            new BracketBracketBrentSolver(),
            new Preconditioner() {
                @Override
                public double[] precondition(double[] variables, double[] r) {
                    double[] preconditioned = r.clone();
                    for (int i = 0; i < preconditioned.length; ++i) {
                        preconditioned[i] /= (1 + variables[i] * variables[i]);
                    }
                    return preconditioned;
                }
            });
        optimizer.setInitialStep(0.1);

        optimizer.optimize(f, initialGuess.getPoint(), GoalType.MINIMIZE);

        PointValuePair result = optimizer.getOptimum();
        double expectedResult = Math.PI;
        assertEquals(expectedResult, result.getValue(), 1e-8);
        assertEquals(expectedResult, result.getPoint()[0], 1e-8);
    }

    /**
     * Test optimization with a preconditioner that does not implement {@link Preconditioner} interface.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testInvalidPreconditioner() {
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return x * x;
            }

            @Override
            public double derivative(double x) {
                return 2 * x;
            }
        };

        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(
            ConjugateGradientFormula.FLETCHER_REEVES,
            new SimpleValueChecker(1e-9, 1e-9),
            new BracketBracketBrentSolver(),
            new Object() {});
        optimizer.setInitialStep(0.1);

        optimizer.optimize(f, new double[]{1.0}, GoalType.MINIMIZE);
        fail("Expected IllegalArgumentException");
    }
}