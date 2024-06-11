package org.apache.commons.cli;

import static org.junit.Assert.*;
import org.junit.Test;

import org.apache.commons.math3.optimization.general.NonLinearConjugateGradientOptimizer;
import org.apache.commons.math3.optimization.general.ConjugateGradientFormula;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.PointValuePair;
import org.apache.commons.math3.optimization.SimpleValueChecker;
import org.apache.commons.math3.optimization.UnivariateSolver;
import org.apache.commons.math3.optimization.convergence.IterationNumber;
import org.apache.commons.math3.optimization.nonlinear.scalar.GoalType;

public class NonLinearConjugateGradientOptimizerTest {

    @Test
    public void testEmptyConstructor() {
        try {
            new NonLinearConjugateGradientOptimizer(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testMinimizerConstructorWithFletcherReevesUpdateFormula() {
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.FLETCHER_REEVES);
        assertNotNull(optimizer.getUpdateFormula());
        assertNull(optimizer.getPreconditioner());
        assertNull(optimizer.getSolver());
        assertNull(optimizer.getInitialStep());
        assertNull(optimizer.getPoint());
    }

    @Test
    public void testMinimizerConstructorWithPolakRibiereUpdateFormula() {
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.POLAK_RIBIERE);
        assertNotNull(optimizer.getUpdateFormula());
        assertNull(optimizer.getPreconditioner());
        assertNull(optimizer.getSolver());
        assertNull(optimizer.getInitialStep());
        assertNull(optimizer.getPoint());
    }

    @Test
    public void testMinimizerConstructorWithSimpleValueChecker() {
        ConvergenceChecker<PointValuePair> checker = new SimpleValueChecker();
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.FLETCHER_REEVES, checker);
        assertNotNull(optimizer.getUpdateFormula());
        assertSame(checker, optimizer.getConvergenceChecker());
        assertNull(optimizer.getSolver());
        assertNull(optimizer.getPreconditioner());
        assertNull(optimizer.getInitialStep());
        assertNull(optimizer.getPoint());
    }

    @Test
    public void testMinimizerConstructorWithBrentSolver() {
        UnivariateSolver solver = new BrentSolver();
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.FLETCHER_REEVES, new SimpleValueChecker(), solver);
        assertNotNull(optimizer.getUpdateFormula());
        assertNull(optimizer.getConvergenceChecker());
        assertSame(solver, optimizer.getSolver());
        assertNull(optimizer.getPreconditioner());
        assertNull(optimizer.getInitialStep());
        assertNull(optimizer.getPoint());
    }

    @Test
    public void testMinimizerConstructorWithIdentityPreconditioner() {
        Preconditioner preconditioner = new NonLinearConjugateGradientOptimizer.IdentityPreconditioner();
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.FLETCHER_REEVES, new SimpleValueChecker(), null, preconditioner);
        assertNotNull(optimizer.getUpdateFormula());
        assertNull(optimizer.getConvergenceChecker());
        assertNull(optimizer.getSolver());
        assertSame(preconditioner, optimizer.getPreconditioner());
        assertNull(optimizer.getInitialStep());
        assertNull(optimizer.getPoint());
    }

    @Test
    public void testMinimizerConstructorWithAllArguments() {
        ConvergenceChecker<PointValuePair> checker = new SimpleValueChecker();
        UnivariateSolver solver = new BrentSolver();
        Preconditioner preconditioner = new NonLinearConjugateGradientOptimizer.IdentityPreconditioner();
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.FLETCHER_REEVES, checker, solver, preconditioner);
        assertNotNull(optimizer.getUpdateFormula());
        assertSame(checker, optimizer.getConvergenceChecker());
        assertSame(solver, optimizer.getSolver());
        assertSame(preconditioner, optimizer.getPreconditioner());
        assertNull(optimizer.getInitialStep());
        assertNull(optimizer.getPoint());
    }

    @Test
    public void testSetInitialStep() {
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.FLETCHER_REEVES, new SimpleValueChecker(), new BrentSolver(), new IdentityPreconditioner());
        optimizer.setInitialStep(3.0);
        assertEquals(3.0, optimizer.getInitialStep());
        optimizer.setInitialStep(-1.0);
        assertEquals(1.0, optimizer.getInitialStep());
    }

    @Test
    public void testDoOptimizeWithZeroFunction() {
        UnivariateFunction f = x -> 0.0;
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.FLETCHER_REEVES, new SimpleValueChecker(), new BrentSolver(), new IdentityPreconditioner());
        optimizer.setInitialStep(1.0);
        optimizer.optimize(f, new PointValuePair(new double[] { 0.0 }, 0.0));
        assertEquals(0.0, optimizer.getPoint()[0], 1e-15);
    }

    @Test
    public void testDoOptimize() {
        UnivariateFunction f = x -> Math.sin(x) + 2 * x;
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.FLETCHER_REEVES, new SimpleValueChecker(), new BrentSolver(), new IdentityPreconditioner());
        optimizer.setInitialStep(1.0);
        optimizer.optimize(f, new PointValuePair(new double[] { 0.0 }, 0.0));
        assertArrayEquals(new double[] { 1.1111111111111113, -1.1111111111111113 }, optimizer.getPoint(), 1e-15);
        assertEquals(Math.PI / 2, optimizer.getValue(), 1e-15);
    }

    @Test
    public void testDoOptimizeWithCustomPreconditioner() {
        UnivariateFunction f = x -> Math.sin(x) + 2 * x;
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.FLETCHER_REEVES, new SimpleValueChecker(), new BrentSolver(), new Preconditioner() {
            @Override
            public double[] precondition(double[] variables, double[] r) {
                double[] result = new double[variables.length];
                for (int i = 0; i < variables.length; ++i) {
                    result[i] = variables[i] + r[i];
                }
                return result;
            }
        });
        optimizer.setInitialStep(1.0);
        optimizer.optimize(f, new PointValuePair(new double[] { 0.0 }, 0.0));
        assertArrayEquals(new double[] { 1.1111111111111113, -1.1111111111111113 }, optimizer.getPoint(), 1e-15);
        assertEquals(Math.PI / 2, optimizer.getValue(), 1e-15);
    }

    @Test(expected = MathIllegalStateException.class)
    public void testDoOptimizeWithInvalidUpdateFormula() {
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.INVALID, new SimpleValueChecker(), new BrentSolver(), new IdentityPreconditioner());
        optimizer.optimize(new UnivariateFunction() {
            @Override
            public double value(double x) {
                return 0.0;
            }
        }, new PointValuePair(new double[] { 0.0 }, 0.0));
    }

    @Test(expected = MathIllegalStateException.class)
    public void testDoOptimizeWithNullFunction() {
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.FLETCHER_REEVES, new SimpleValueChecker(), new BrentSolver(), new IdentityPreconditioner());
        optimizer.optimize(null, new PointValuePair(new double[] { 0.0 }, 0.0));
    }

    @Test(expected = MathIllegalStateException.class)
    public void testDoOptimizeWithNullInitialPoint() {
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.FLETCHER_REEVES, new SimpleValueChecker(), new BrentSolver(), new IdentityPreconditioner());
        optimizer.optimize(new UnivariateFunction() {
            @Override
            public double value(double x) {
                return 0.0;
            }
        }, null);
    }

    @Test
    public void testDoOptimizeWithIterationLimit() {
        UnivariateFunction f = x -> Math.sin(x) + 2 * x;
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.FLETCHER_REEVES, new SimpleValueChecker(), new BrentSolver(), new IdentityPreconditioner());
        optimizer.setMaxIterations(1);
        optimizer.optimize(f, new PointValuePair(new double[] { 0.0 }, 0.0));
        assertArrayEquals(new double[] { 0.0 }, optimizer.getPoint(), 1e-15);
        assertEquals(Double.POSITIVE_INFINITY, optimizer.getValue(), 1e-15);
    }

    @Test
    public void testDoOptimizeWithMultipleStartingPoints() {
        UnivariateFunction f = x -> Math.sin(x) + 2 * x;
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.FLETCHER_REEVES, new SimpleValueChecker(), new BrentSolver(), new IdentityPreconditioner());
        optimizer.optimize(f, new PointValuePair(new double[] { 0.5 }, 1.5));
        optimizer.optimize(f, new PointValuePair(new double[] { 1.5 }, 2.5));
        assertArrayEquals(new double[] { 1.1111111111111113, -1.1111111111111113 }, optimizer.getPoint(), 1e-15);
        assertEquals(Math.PI / 2, optimizer.getValue(), 1e-15);
    }

    @Test
    public void testDoOptimizeWithCustomConvergenceChecker() {
        UnivariateFunction f = x -> Math.sin(x) + 2 * x;
        ConvergenceChecker<PointValuePair> convergenceChecker = new ConvergenceChecker<PointValuePair>() {
            @Override
            public boolean isConverged(IterationNumber iterationNumber, PointValuePair previousValue, PointValuePair currentValue) {
                return currentValue.getValue() < 1e-6;
            }
        };
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.FLETCHER_REEVES, convergenceChecker, new BrentSolver(), new IdentityPreconditioner());
        optimizer.optimize(f, new PointValuePair(new double[] { 0.0 }, 0.0));
        assertArrayEquals(new double[] { 1.1111111111111113, -1.1111111111111113 }, optimizer.getPoint(), 1e-15);
        assertEquals(Math.PI / 2, optimizer.getValue(), 1e-15);
    }

    @Test
    public void testDoOptimizeWithCustomSolver() {
        UnivariateFunction f = x -> Math.sin(x) + 2 * x;
        UnivariateSolver solver = new SecantSolver(1.0, 1.5);
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.FLETCHER_REEVES, new SimpleValueChecker(), solver, new IdentityPreconditioner());
        optimizer.optimize(f, new PointValuePair(new double[] { 0.0 }, 0.0));
        assertArrayEquals(new double[] { 1.1111111111111113, -1.1111111111111113 }, optimizer.getPoint(), 1e-15);
        assertEquals(Math.PI / 2, optimizer.getValue(), 1e-15);
    }
}