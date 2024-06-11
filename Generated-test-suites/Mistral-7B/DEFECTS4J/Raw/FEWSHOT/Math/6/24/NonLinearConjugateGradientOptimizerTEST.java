package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.MathIllegalStateException;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleValueChecker;
import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.apache.commons.math3.optim.goal.GoalType;
import org.apache.commons.math3.optim.nonlinear.ConjugateGradientFormula;
import org.apache.commons.math3.optim.nonlinear.Preconditioner;
import org.apache.commons.math3.optim.nonlinear.simple.IdentityPreconditioner;
import org.apache.commons.math3.optim.nonlinear.simple.LineSearchFunction;
import org.apache.commons.math3.optim.nonlinear.simple.NLinearConjugateGradientOptimizer;
import org.apache.commons.math3.optim.nonlinear.simple.NLinearFunction;
import org.apache.commons.math3.optim.point.PointValuePairCollection;
import org.apache.commons.math3.optim.simple.BrentSolver;
import org.apache.commons.math3.optim.simple.SimpleValueChecker;
import org.apache.commons.math3.special.Fraction;
import org.junit.Before;
import org.junit.Test;

public class NonLinearConjugateGradientOptimizerTest {
    private NLinearConjugateGradientOptimizer optimizer;
    private ConvergenceChecker<PointValuePair> checker;
    private UnivariateSolver solver;
    private Preconditioner preconditioner;

    @Before
    public void setUp() {
        optimizer = new NLinearConjugateGradientOptimizer(ConjugateGradientFormula.FLETCHER_REEVES, new SimpleValueChecker(), new BrentSolver(), new IdentityPreconditioner());
        checker = new SimpleValueChecker(1.0e-6);
        solver = new BrentSolver();
        preconditioner = new IdentityPreconditioner();
    }

    @Test
    public void testOptimizeSimple() {
        PointValuePairCollection solutions = optimizefunction(new NLinearFunction(x -> x * x), new double[]{-10.0, 10.0}, optimizer, checker);
        PointValuePair pointValuePair = solutions.getPoint(0);
        assertEquals(0.0, pointValuePair.getPoint()[0], 1e-6);
        assertEquals(0.0, pointValuePair.getValue(), 1e-6);
    }

    @Test
    public void testOptimizeWithPreconditioner() {
        Preconditioner preconditioner = new Preconditioner() {
            @Override
            public double[] precondition(double[] variables, double[] r) {
                double[] result = new double[r.length];
                for (int i = 0; i < result.length; i++) {
                    result[i] = r[i] * 2.0;
                }
                return result;
            }
        };
        optimizer = new NLinearConjugateGradientOptimizer(ConjugateGradientFormula.FLETCHER_REEVES, checker, solver, preconditioner);

        PointValuePairCollection solutions = optimizefunction(new NLinearFunction(x -> x * x * x), new double[]{-10.0, 10.0}, optimizer, checker);
        PointValuePair pointValuePair = solutions.getPoint(0);
        assertEquals(-Fraction.ONE_HALF.doubleValue(), pointValuePair.getPoint()[0], 1e-6);
        assertEquals(Fraction.ONE_QUARTER.doubleValue(), pointValuePair.getValue(), 1e-6);
    }

    @Test(expected = MathIllegalStateException.class)
    public void testOptimizeFailedConvergence() {
        UnivariateFunction function = x -> 1.0 / (x * x);
        double[] startPoint = new double[]{1.0};
        optimizer = new NLinearConjugateGradientOptimizer(ConjugateGradientFormula.FLETCHER_REEVES, checker, solver, new IdentityPreconditioner());
        optimizer.setInitialStep(0.5);
        PointValuePairCollection solutions = optimizefunction(new NLinearFunction(function), startPoint, optimizer, checker);
        assertTrue(solutions.isEmpty());
    }

    @Test
    public void testSetInitialStep() {
        optimizer = new NLinearConjugateGradientOptimizer(ConjugateGradientFormula.FLETCHER_REEVES, checker, solver, new IdentityPreconditioner());
        optimizer.setInitialStep(1.5);
        assertEquals(1.5, optimizer.getInitialStep(), 1e-6);
    }

    private PointValuePairCollection optimizefunction(UnivariateFunction function, double[] startPoint, UnivariateOptimizer optimizer, ConvergenceChecker<PointValuePair> checker) {
        optimizer.setInitialPoint(startPoint);
        optimizer.setMaxIterations(100);
        optimizer.setConvergenceChecker(checker);
        optimizer.optimize();
        return optimizer.getSolutions();
    }
}