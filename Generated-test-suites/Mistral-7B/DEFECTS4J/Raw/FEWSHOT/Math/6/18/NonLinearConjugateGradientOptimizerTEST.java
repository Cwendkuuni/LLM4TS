package org.apache.commons.cli;

import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleValueChecker;
import org.apache.commons.math3.optim.goaltype.GoalType;
import org.apache.commons.math3.optim.nonlinear.ConjugateGradientFormula;
import org.apache.commons.math3.optim.preconditions.IdentityPreconditioner;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalTypeFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalTypeGradient;
import org.apache.commons.math3.optim.nonlinear.scalar.LinearSearchFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.LineSearchSolver;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.BrentSolver;
import org.apache.commons.math3.optim.PointValues;

public class NonLinearConjugateGradientOptimizerTest {

    @Test
    public void testNonLinearConjugateGradientOptimizer() {
        ConjugateGradientOptimizer optimizer = new ConjugateGradientOptimizer(
                ConjugateGradientFormula.FLETCHER_REEVES,
                new SimpleValueChecker(),
                new BrentSolver(),
                new IdentityPreconditioner());

        PointValuePair result = optimizer.optimize(new GoalTypeFunction("f", x -> Math.pow(x, 2)), new GoalTypeGradient("x", 1.0), new PointValues(0.5));
        assertEquals(0.0, result.getValue(), 1e-6);
        assertEquals(0.5, result.getPoint()[0], 1e-6);
    }

    @Test
    public void testNonLinearConjugateGradientOptimizerPolakRibiere() {
        ConjugateGradientOptimizer optimizer = new ConjugateGradientOptimizer(
                ConjugateGradientFormula.POLAK_RIBIERE,
                new SimpleValueChecker(),
                new BrentSolver(),
                new IdentityPreconditioner());

        PointValuePair result = optimizer.optimize(new GoalTypeFunction("g", x -> Math.pow(Math.abs(x - 1.0), 2)), new GoalTypeGradient("x", 1.0), new PointValues(2.0));
        assertEquals(0.0, result.getValue(), 1e-6);
        assertEquals(1.0, result.getPoint()[0], 1e-6);
    }

    @Test
    public void testNonLinearConjugateGradientOptimizerBrentSolver() {
        ConjugateGradientOptimizer optimizer = new ConjugateGradientOptimizer(
                ConjugateGradientFormula.FLETCHER_REEVES,
                new SimpleValueChecker(),
                new BrentSolver(),
                new IdentityPreconditioner());

        PointValuePair result = optimizer.optimize(new GoalTypeFunction("f", x -> Math.pow(x, 3) - 2 * x), new GoalTypeGradient("x", 1.0), new PointValues(1.0));
        assertEquals(-1.0, result.getValue(), 1e-6);
        assertEquals(-1.0, result.getPoint()[0], 1e-6);
    }

    @Test
    public void testNonLinearConjugateGradientOptimizerCustomPreconditioner() {
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

        ConjugateGradientOptimizer optimizer = new ConjugateGradientOptimizer(
                ConjugateGradientFormula.FLETCHER_REEVES,
                new SimpleValueChecker(),
                new BrentSolver(),
                customPreconditioner);

        PointValuePair result = optimizer.optimize(new GoalTypeFunction("f", x -> Math.pow(x, 2)), new GoalTypeGradient("x", 1.0), new PointValues(0.5));
        assertEquals(0.0, result.getValue(), 1e-6);
        assertEquals(0.25, result.getPoint()[0], 1e-6);
    }

    @Test(expected = MathIllegalStateException.class)
    public void testNonLinearConjugateGradientOptimizerNoLineSearchSolver() {
        ConjugateGradientOptimizer optimizer = new ConjugateGradientOptimizer(
                ConjugateGradientFormula.FLETCHER_REEVES,
                new SimpleValueChecker(),
                null,
                new IdentityPreconditioner());

        optimizer.optimize(new GoalTypeFunction("f", x -> Math.pow(x, 2)), new GoalTypeGradient("x", 1.0), new PointValues(0.5));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNonLinearConjugateGradientOptimizerInvalidUpdateFormula() {
        ConjugateGradientOptimizer optimizer = new ConjugateGradientOptimizer(
                "INVALID_UPDATE_FORMULA",
                new SimpleValueChecker(),
                new BrentSolver(),
                new IdentityPreconditioner());

        optimizer.optimize(new GoalTypeFunction("f", x -> Math.pow(x, 2)), new GoalTypeGradient("x", 1.0), new PointValues(0.5));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNonLinearConjugateGradientOptimizerNullPreconditioner() {
        ConjugateGradientOptimizer optimizer = new ConjugateGradientOptimizer(
                ConjugateGradientFormula.FLETCHER_REEVES,
                new SimpleValueChecker(),
                new BrentSolver(),
                null);

        optimizer.optimize(new GoalTypeFunction("f", x -> Math.pow(x, 2)), new GoalTypeGradient("x", 1.0), new PointValues(0.5));
    }
}

class Preconditioner implements org.apache.commons.math3.optim.nonlinear.scalar.Preconditioner {

}