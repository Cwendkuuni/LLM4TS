package org.apache.commons.math3.optimization.general;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.analysis.solvers.BrentSolver;
import org.apache.commons.math3.analysis.solvers.UnivariateSolver;
import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.MathIllegalStateException;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.PointValuePair;
import org.apache.commons.math3.optimization.SimpleValueChecker;
import org.apache.commons.math3.optimization.ConvergenceChecker;
import org.apache.commons.math3.optimization.NonLinearConjugateGradientOptimizer;
import org.apache.commons.math3.optimization.Preconditioner;
import org.apache.commons.math3.optimization.ConjugateGradientFormula;
import org.apache.commons.math3.optimization.AbstractScalarDifferentiableOptimizer;
import org.junit.Assert;
import org.junit.Test;

public class NonLinearConjugateGradientOptimizerTest {

    @Test
    public void testDefaultConstructor() {
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.FLETCHER_REEVES);
        Assert.assertNotNull(optimizer.getPreconditioner());
        Assert.assertEquals(ConjugateGradientFormula.FLETCHER_REEVES, optimizer.getUpdateFormula());
        Assert.assertNotNull(optimizer.getSolver());
        Assert.assertEquals(1.0, optimizer.getInitialStep(), 0.001);
    }

    @Test
    public void testConstructorWithChecker() {
        SimpleValueChecker checker = new SimpleValueChecker();
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.FLETCHER_REEVES, checker);
        Assert.assertSame(checker, optimizer.getConvergenceChecker());
        Assert.assertEquals(ConjugateGradientFormula.FLETCHER_REEVES, optimizer.getUpdateFormula());
        Assert.assertNotNull(optimizer.getSolver());
        Assert.assertEquals(1.0, optimizer.getInitialStep(), 0.001);
    }

    @Test
    public void testConstructorWithSolver() {
        BrentSolver solver = new BrentSolver();
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.FLETCHER_REEVES, null, solver);
        Assert.assertEquals(ConjugateGradientFormula.FLETCHER_REEVES, optimizer.getUpdateFormula());
        Assert.assertSame(solver, optimizer.getSolver());
        Assert.assertEquals(1.0, optimizer.getInitialStep(), 0.001);
    }

    @Test
    public void testConstructorWithAllParams() {
        SimpleValueChecker checker = new SimpleValueChecker();
        BrentSolver solver = new BrentSolver();
        IdentityPreconditioner preconditioner = new IdentityPreconditioner();
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.FLETCHER_REEVES, checker, solver, preconditioner);
        Assert.assertSame(checker, optimizer.getConvergenceChecker());
        Assert.assertEquals(ConjugateGradientFormula.FLETCHER_REEVES, optimizer.getUpdateFormula());
        Assert.assertSame(solver, optimizer.getSolver());
        Assert.assertSame(preconditioner, optimizer.getPreconditioner());
        Assert.assertEquals(1.0, optimizer.getInitialStep(), 0.001);
    }

    @Test(expected = MathIllegalArgumentException.class)
    public void testInvalidUpdateFormula() {
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer("InvalidUpdateFormula");
    }

    @Test
    public void testSetInitialStep() {
        SimpleValueChecker checker = new SimpleValueChecker();
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.FLETCHER_REEVES, checker, null, null);
        optimizer.setInitialStep(-1.0);
        Assert.assertEquals(1.0, optimizer.getInitialStep(), 0.001);
        optimizer.setInitialStep(0.5);
        Assert.assertEquals(0.5, optimizer.getInitialStep(), 0.001);
    }

    @Test
    public void testDoOptimize_Success() throws Exception {
        SimpleValueChecker checker = new SimpleValueChecker(1.0e-8);
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.FLETCHER_REEVES, checker, null, null);
        UnivariateFunction function = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return x * x;
            }

            @Override
            public double derivative(double x) {
                return 2 * x;
            }
        };
        PointValuePair result = optimizer.optimize(function, 1.0, GoalType.MINIMIZE);
        Assert.assertTrue(checker.isConverged(result, 10));
        Assert.assertEquals(0.0, result.getValue(), 0.001);
    }

    @Test(expected = MathIllegalStateException.class)
    public void testDoOptimize_Failure() throws Exception {
        SimpleValueChecker checker = new SimpleValueChecker(1.0e-8);
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.FLETCHER_REEVES, checker, null, null);
        UnivariateFunction function = new UnivariateFunction() {
            @Override
            public double value(double x) {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public double derivative(double x) {
                return 0.0;
            }
        };
        optimizer.optimize(function, 1.0, GoalType.MINIMIZE);
    }

    @Test(expected = ConvergenceException.class)
    public void testDoOptimize_ConvergenceException() throws Exception {
        SimpleValueChecker checker = new SimpleValueChecker(1.0e-3);
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.FLETCHER_REEVES, checker, null, null);
        UnivariateFunction function = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(Math.E, -x);
            }

            @Override
            public double derivative(double x) {
                return -Math.pow(Math.E, -x);
            }
        };
        optimizer.optimize(function, 0.0, GoalType.MINIMIZE);
    }
}