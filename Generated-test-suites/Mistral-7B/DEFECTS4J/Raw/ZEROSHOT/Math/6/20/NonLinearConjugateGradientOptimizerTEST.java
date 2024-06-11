package org.apache.commons.math3.optimization.general;

import static org.junit.Assert.*;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.MathIllegalStateException;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleValueChecker;
import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.NonLinearConstraint;
import org.apache.commons.math3.optim.nonlinear.scalar.NonLinearObjectiveFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.NonLinearPointValuePair;
import org.apache.commons.math3.optim.nonlinear.scalar.NonLinearSolver;
import org.apache.commons.math3.optim.nonlinear.scalar.linear.IdentityPreconditioner;
import org.apache.commons.math3.optim.nonlinear.scalar.nonlinearfunction.LinearConstraint;
import org.apache.commons.math3.optim.nonlinear.scalar.nonlinearfunction.PolynomialFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.BrentSolver;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.ConjugateGradientFormula;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.ConjugateGradientPreconditioner;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.LinearSearchFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.NonLinearConjugateGradientOptimizer;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.Preconditioner;

import java.util.Arrays;

public class NonLinearConjugateGradientOptimizerTest {

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private static NonLinearObjectiveFunction createTestFunction() {
        final UnivariateFunction f = new PolynomialFunction(3, new double[]{-1.0, 1.0, -1.0, 1.0});
        return new NonLinearObjectiveFunction() {
            @Override
            public double value(double x) {
                return f.value(x);
            }

            @Override
            public double[] gradient(double x) {
                return f.derivative(x);
            }
        };
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private static NonLinearObjectiveFunction createTestFunctionWithConstraints() {
        final UnivariateFunction f = new PolynomialFunction(3, new double[]{-1.0, 1.0, -1.0, 1.0});
        final double[] lowerBounds = new double[]{-3.0};
        final double[] upperBounds = new double[]{3.0};
        NonLinearConstraint constraint = new LinearConstraint(1, 2, 2);

        return new NonLinearObjectiveFunction() {
            @Override
            public double value(double[] x) {
                return f.value(x[0]);
            }

            @Override
            public double[] gradient(double[] x) {
                return f.derivative(x[0]);
            }

            @Override
            public NonLinearConstraint[] getConstraints() {
                return new NonLinearConstraint[]{constraint};
            }
        };
    }

    @org.junit.Test
    public void testDefaultConstructor() {
        try {
            final NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.FLETCHER_REEVES);
            fail("Expected IllegalArgumentException due to missing arguments.");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @org.junit.Test
    public void testConstructorWithMissingArguments() {
        try {
            final NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(
                    ConjugateGradientFormula.FLETCHER_REEVES, new SimpleValueChecker());
            fail("Expected IllegalArgumentException due to missing arguments.");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @org.junit.Test
    public void testConstructorWithAllArguments() {
        final ConjugateGradientFormula updateFormula = ConjugateGradientFormula.FLETCHER_REEVES;
        final Preconditioner preconditioner = new IdentityPreconditioner();
        final UnivariateSolver lineSearchSolver = new BrentSolver();

        final NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(
                updateFormula, new SimpleValueChecker(), lineSearchSolver, preconditioner);

        assertEquals(updateFormula, optimizer.getUpdateFormula());
        assertEquals(preconditioner, optimizer.getPreconditioner());
        assertEquals(lineSearchSolver, optimizer.getLineSearchSolver());
        assertNull(optimizer.getInitialStep());
        assertArrayEquals(new double[]{0.0}, optimizer.getStartPoint(), 0.0);
    }

    @org.junit.Test
    public void testSetInitialStep() {
        final NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(
                ConjugateGradientFormula.FLETCHER_REEVES, new SimpleValueChecker(),
                new BrentSolver(), new IdentityPreconditioner());

        optimizer.setInitialStep(-1.0);
        assertEquals(1.0, optimizer.getInitialStep(), 0.0);

        optimizer.setInitialStep(2.5);
        assertEquals(2.5, optimizer.getInitialStep(), 0.0);
    }

    @org.junit.Test
    public void testOptimize() {
        final NonLinearObjectiveFunction function = createTestFunction();
        final NonLinearSolver optimizer = new NonLinearConjugateGradientOptimizer(
                ConjugateGradientFormula.FLETCHER_REEVES, new SimpleValueChecker(),
                new BrentSolver(), new IdentityPreconditioner());
        final double[] startPoint = new double[]{1.0};
        final double tolerance = 1e-12;

        optimizer.optimize(function, startPoint, tolerance);

        final double[] expectedResult = new double[]{1.0};
        final double[] result = optimizer.getSolution();
        assertArrayEquals(expectedResult, result, tolerance);

        final double objective = function.value(result);
        final double expectedObjective = function.value(expectedResult);
        assertEquals(expectedObjective, objective, tolerance);
    }

    @org.junit.Test
    public void testOptimizeWithConstraints() {
        final NonLinearObjectiveFunction function = createTestFunctionWithConstraints();
        final NonLinearSolver optimizer = new NonLinearConjugateGradientOptimizer(
                ConjugateGradientFormula.FLETCHER_REEVES, new SimpleValueChecker(),
                new BrentSolver(), new ConjugateGradientPreconditioner(new IdentityPreconditioner()));
        final double[] startPoint = new double[]{1.0};
        final double tolerance = 1e-12;

        optimizer.optimize(function, startPoint, tolerance);

        final double[] expectedResult = new double[]{1.0};
        final double[] result = optimizer.getSolution();
        assertArrayEquals(expectedResult, result, tolerance);

        final double objective = function.value(result);
        final double expectedObjective = function.value(expectedResult);
        assertEquals(expectedObjective, objective, tolerance);

        final double[] gradient = function.gradient(result);
        final double[] expectedGradient = function.gradient(expectedResult);
        assertArrayEquals(expectedGradient, gradient, tolerance);

        final double[][] constraints = optimizer.getConstraints();
        assertEquals(1, constraints.length);
        assertEquals(1, constraints[0].getDimensions());
        assertEquals(2, constraints[0].getConstraints().length);
        assertEquals(1.0, constraints[0].getConstraints()[0], tolerance);
        assertEquals(2.0, constraints[0].getConstraints()[1], tolerance);
    }

    @org.junit.Test(expected = MathIllegalStateException.class)
    public void testOptimizeWithDivergingLineSearch() {
        final NonLinearObjectiveFunction function = new NonLinearObjectiveFunction() {
            @Override
            public double value(double x) {
                return Math.log(1 + Math.abs(x));
            }

            @Override
            public double[] gradient(double x) {
                return new double[]{1 / (1 + Math.abs(x))};
            }
        };
        final NonLinearSolver optimizer = new NonLinearConjugateGradientOptimizer(
                ConjugateGradientFormula.POLAK_RIBIERE, new SimpleValueChecker(),
                new BrentSolver(), new IdentityPreconditioner());
        final double[] startPoint = new double[]{1.0};
        final double tolerance = 1e-12;

        optimizer.optimize(function, startPoint, tolerance);

        fail("Expected MathIllegalStateException due to diverging line search.");
    }

}