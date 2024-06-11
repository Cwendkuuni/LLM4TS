package org.apache.commons.math3.optimization.general;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.analysis.solvers.BrentSolver;
import org.apache.commons.math3.analysis.solvers.UnivariateSolver;
import org.apache.commons.math3.exception.MathIllegalStateException;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleValueChecker;
import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.apache.commons.math3.optim.initializer.AveragedStartingPointGenerator;
import org.apache.commons.math3.optim.point.PointValuePairChecker;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.NonLinearConstraint;
import org.apache.commons.math3.optim.nonlinear.scalar.NonLinearConstraintSet;
import org.apache.commons.math3.optim.nonlinear.scalar.NonLinearObjectiveFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.NonLinearOptimizer;
import org.apache.commons.math3.optim.nonlinear.scalar.Preconditioner;
import org.apache.commons.math3.optim.nonlinear.scalar.convergence.ConvergenceChecker;
import org.apache.commons.math3.optim.nonlinear.scalar.linear.IdentityPreconditioner;
import org.apache.commons.math3.special.SpecialFunctions;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

public class NonLinearConjugateGradientOptimizerTest {

    /** Test function: f(x) = x^3 + 2x^2 - 3x + 4*/
    private static final NonLinearObjectiveFunction TEST_FUNCTION = new NonLinearObjectiveFunction() {
        @Override
        public double value(double[] x) {
            return SpecialFunctions.cubic(x[0]) + 2 * Math.pow(x[0], 2) - 3 * x[0] + 4;
        }

        @Override
        public double[] gradient(double[] x) {
            double[] gradient = new double[x.length];
            gradient[0] = 3 * Math.pow(x[0], 2) + 4 * x[0] - 3;
            return gradient;
        }
    };

    /** Test function constraints */
    private static final NonLinearConstraintSet TEST_CONSTRAINTS = new NonLinearConstraintSet();
    static {
        TEST_CONSTRAINTS.addConstraint(new NonLinearConstraint(1.0));
        TEST_CONSTRAINTS.addConstraint(new NonLinearConstraint(2.0));
    }

    /** Test optimizer */
    private NonLinearOptimizer optimizer;

    /** Initial point generator */
    private AveragedStartingPointGenerator startingPointGenerator;

    /** Convergence checker */
    private ConvergenceChecker<PointValuePair> convergenceChecker;

    /**
     * Set up the optimizer for testing
     */
    @Before
    public void setUp() {
        optimizer = new NonLinearConjugateGradientOptimizer(
            ConjugateGradientFormula.FLETCHER_REEVES,
            new SimpleValueChecker(),
            new BrentSolver(),
            new IdentityPreconditioner());
        optimizer.setInitialStep(1.0);
        optimizer.setMaxIterations(100);
        optimizer.setMaxEvaluations(1000);

        convergenceChecker = new ConvergenceChecker<PointValuePair>(
                1e-6, 1.0e-6, 1.0e-6, 100, false);

        startingPointGenerator = new AveragedStartingPointGenerator(3);
    }

    /**
     * Test optimizer with default parameters
     */
    @Test
    public void testOptimizerDefault() {
        PointValuePair result = optimizer.optimize(TEST_FUNCTION, null, null, convergenceChecker, startingPointGenerator);
        Assert.assertEquals(3.0, result.getPoint()[0], 1e-6);
        Assert.assertEquals(12.0, result.getValue(), 1e-6);
    }

    /**
     * Test optimizer with custom convergence checker
     */
    @Test
    public void testOptimizerCustomCheckers() {
        ConvergenceChecker<PointValuePair> customChecker = new ConvergenceChecker<PointValuePair>(
                1e-5, 1.0e-5, 1.0e-5, 100, true);

        PointValuePair result = optimizer.optimize(TEST_FUNCTION, null, null, customChecker, startingPointGenerator);
        Assert.assertEquals(3.0, result.getPoint()[0], 1e-6);
        Assert.assertEquals(12.0, result.getValue(), 1e-6);
    }

    /**
     * Test optimizer with constraints
     */
    @Test(expected = MathIllegalStateException.class)
    public void testOptimizerWithConstraints() {
        PointValuePair result = optimizer.optimize(TEST_FUNCTION, TEST_CONSTRAINTS, null, convergenceChecker, startingPointGenerator);
        Assert.fail("Expected MathIllegalStateException");
    }

    /**
     * Test optimizer with custom preconditioner
     */
    @Test
    public void testOptimizerCustomPreconditioner() {
        Preconditioner preconditioner = new Preconditioner() {
            @Override
            public double[] precondition(double[] variables, double[] r) {
                double[] result = r.clone();
                result[0] += 1.0;
                return result;
            }
        };
        NonLinearConjugateGradientOptimizer optimizerWithPreconditioner =
                new NonLinearConjugateGradientOptimizer(
                        ConjugateGradientFormula.FLETCHER_REEVES,
                        new SimpleValueChecker(),
                        null,
                        preconditioner);

        PointValuePair result = optimizerWithPreconditioner.optimize(TEST_FUNCTION, null, null, convergenceChecker, startingPointGenerator);
        Assert.assertEquals(4.0, result.getPoint()[0], 1e-6);
        Assert.assertEquals(24.0, result.getValue(), 1e-6);
    }
}