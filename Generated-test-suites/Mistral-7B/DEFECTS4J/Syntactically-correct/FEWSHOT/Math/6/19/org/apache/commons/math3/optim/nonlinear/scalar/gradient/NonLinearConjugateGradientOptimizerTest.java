package org.apache.commons.cli;

import static org.junit.Assert.*;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.optimization.*;
import org.apache.commons.math3.optimization.linear.*;
import org.apache.commons.math3.optimization.nonlinear.scalar.*;
import org.apache.commons.math3.optimization.nonlinear.scalar.noderiv.*;
import org.apache.commons.math3.optimization.solvers.*;
import org.apache.commons.math3.util.*;

import java.util.function.UnaryOperator;

public class NonLinearConjugateGradientOptimizerTest {

    @FunctionalInterface
    private interface CustomFunction extends UnivariateFunction {
        double value(double[] x);
    }

    @Test
    public void testOptimize() {
        final NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(
                ConjugateGradientFormula.FLETCHER_REEVES,
                new SimpleValueChecker(),
                new BrentSolver(),
                new IdentityPreconditioner());

        final double[] startPoint = {1.0, 2.0};
        final CustomFunction objectiveFunction = x -> Math.pow(x[0] - 1.0, 2.0) + Math.pow(x[1] - 1.0, 2.0);
        final GoalType goalType = GoalType.MINIMIZE;

        PointValuePair result = optimizer.optimize(new ScalarFunction(objectiveFunction), startPoint, goalType);
        assertEquals(new PointValuePair(new double[]{1.0, 1.0}, 0.0), result, 0.001);

        // Test with a function that requires preconditioning
        final Preconditioner preconditioner = new LinearPreconditioner(new IdentityMatrix(2));
        optimizer = new NonLinearConjugateGradientOptimizer(
                ConjugateGradientFormula.POLAK_RIBIERE,
                new SimpleValueChecker(),
                new BrentSolver(),
                preconditioner);

        final CustomFunction preconditionedObjectiveFunction = x -> Math.pow(Math.sin(x[0]) - 1.0, 2.0)
                + Math.pow(Math.cos(x[1]) - 1.0, 2.0);
        final PointValuePair preconditionedStartPoint = new PointValuePair(new double[]{Math.PI / 4.0, Math.PI / 4.0}, 1.0);

        result = optimizer.optimize(new ScalarFunction(preconditionedObjectiveFunction), preconditionedStartPoint, goalType);
        assertEquals(new PointValuePair(new double[]{Math.PI / 2.0, Math.PI / 2.0}, 0.0), result, 0.001);
    }

    @Test(expected = MathIllegalStateException.class)
    public void testIllegalStateException() {
        final NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(
                ConjugateGradientFormula.FLETCHER_REEVES,
                new SimpleValueChecker(),
                null,
                new IdentityPreconditioner());

        optimizer.optimize(new ScalarFunction(x -> 0.0), new double[]{0.0}, GoalType.MINIMIZE);
    }

    @Test
    public void testSetInitialStep() {
        final NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(
                ConjugateGradientFormula.FLETCHER_REEVES,
                new SimpleValueChecker(),
                new BrentSolver(),
                new IdentityPreconditioner());

        optimizer.setInitialStep(2.0);
        assertEquals(2.0, optimizer.getInitialStep(), 0.0);
    }
}