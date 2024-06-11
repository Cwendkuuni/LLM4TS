package org.apache.commons.cli;

import static org.junit.Assert.*;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleValueChecker;
import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.apache.commons.math3.optim.nonlinear.ConjugateGradient;
import org.apache.commons.math3.optim.nonlinear.general.*;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.LinearConstraint;
import org.apache.commons.math3.optim.nonlinear.scalar.ObjectiveFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.LinearConstraints;
import org.apache.commons.math3.optim.nonlinear.scalar.Preconditioner;
import org.apache.commons.math3.optim.nonlinear.scalar.UnivariateSolver;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.Brent;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.IdentityPreconditioner;

import java.util.ArrayList;
import java.util.List;

public class NonLinearConjugateGradientOptimizerTest {

    @Test
    public void testOptimize() {
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(
                ConjugateGradientFormula.POLAK_RIBIERE,
                new SimpleValueChecker(),
                new Brent(),
                new IdentityPreconditioner());

        double[] startPoint = new double[]{1.0, 2.0};
        ObjectiveFunction objectiveFunction = f -> Math.pow(f[0] - 1.0, 2.0) + Math.pow(f[1] - 2.0, 2.0);

        PointValuePair result = optimizer.optimize(objectiveFunction, startPoint);

        assertEquals(GoalType.MINIMIZE, optimizer.getGoalType());
        assertEquals(0.0, result.getValue(), 1e-6);
        assertArrayEquals(new double[]{1.0, 2.0}, startPoint, 1e-6);
    }

    @Test
    public void testOptimize_withLinearConstraints() {
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(
                ConjugateGradientFormula.POLAK_RIBIERE,
                new SimpleValueChecker(),
                new Brent(),
                new IdentityPreconditioner());

        double[] startPoint = new double[]{1.0, 2.0};
        ObjectiveFunction objectiveFunction = f -> Math.pow(f[0] - 1.0, 2.0) + Math.pow(f[1] - 2.0, 2.0);

        LinearConstraints constraints = new LinearConstraints();
        constraints.addEquality(new LinearConstraint(1.0, new double[]{1.0, 1.0}, 0.0));
        constraints.addEquality(new LinearConstraint(1.0, new double[]{1.0, 2.0}, 3.0));

        PointValuePair result = optimizer.optimize(objectiveFunction, startPoint, constraints);

        assertEquals(GoalType.MINIMIZE, optimizer.getGoalType());
        assertEquals(0.0, result.getValue(), 1e-6);
        assertArrayEquals(new double[]{1.0, 2.0}, startPoint, 1e-6);
        assertEquals(3.0, result.getPoint()[1], 1e-6);
    }

    @Test(expected = MathIllegalStateException.class)
    public void testOptimize_whenNoSolution() {
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(
                ConjugateGradientFormula.POLAK_RIBIERE,
                new SimpleValueChecker(),
                new Brent(),
                new IdentityPreconditioner());

        double[] startPoint = new double[]{1.0, 2.0};
        ObjectiveFunction objectiveFunction = f -> Math.pow(f[0] - 1.0, 2.0) + Math.pow(f[1] + 2.0, 2.0);

        PointValuePair result = optimizer.optimize(objectiveFunction, startPoint);

        fail("Should have thrown an exception");
    }

    @Test
    public void testPreconditioner() {
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(
                ConjugateGradientFormula.POLAK_RIBIERE,
                new SimpleValueChecker(),
                new Brent(),
                new Preconditioner() {
                    @Override
                    public double[] precondition(double[] variables, double[] r) {
                        double[] result = r.clone();
                        for (int i = 0; i < result.length; i++) {
                            result[i] *= 2.0;
                        }
                        return result;
                    }
                });

        double[] startPoint = new double[]{1.0, 2.0};
        ObjectiveFunction objectiveFunction = f -> Math.pow(f[0] - 1.0, 2.0) + Math.pow(f[1] - 2.0, 2.0);

        PointValuePair result = optimizer.optimize(objectiveFunction, startPoint);

        assertEquals(GoalType.MINIMIZE, optimizer.getGoalType());
        assertEquals(0.0, result.getValue(), 1e-6);
        assertArrayEquals(new double[]{1.0, 2.0}, startPoint, 1e-6);
        for (int i = 0; i < result.getPoint().length; i++) {
            assertEquals(2.0 * result.getPoint()[i], startPoint[i], 1e-6);
        }
    }
}