package org.apache.commons.cli;

import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.MathIllegalStateException;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleValueChecker;
import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.apache.commons.math3.optim.nonlinear.ConjugateGradient;
import org.apache.commons.math3.optim.nonlinear.conjgrad.ConjugateGradientFormula;
import org.apache.commons.math3.optim.nonlinear.conjgrad.Preconditioner;
import org.apache.commons.math3.optim.nonlinear.linear.*;
import org.apache.commons.math3.optim.nonlinear.solvers.BrentSolver;
import org.apache.commons.math3.optim.nonlinear.solvers.LevenbergMarquardtSolver;
import org.apache.commons.math3.optim.nonlinear.solvers.LineSearch;
import org.apache.commons.math3.optim.nonlinear.solvers.LineSearchFunction;
import org.apache.commons.math3.optim.nonlinear.solvers.NelderMeadSimplexSolver;
import org.apache.commons.math3.optim.nonlinear.solvers.RegulaFalsiSolver;
import org.apache.commons.math3.optim.nonlinear.solvers.SparseLineSearchFunction;
import org.apache.commons.math3.optim.nonlinear.solvers.SingleVariableFunction;
import org.apache.commons.math3.optim.nonlinear.solvers.impl.BrentSolver;
import org.apache.commons.math3.optim.nonlinear.solvers.impl.LevenbergMarquardtSolver;
import org.apache.commons.math3.optim.nonlinear.solvers.impl.NelderMeadSimplexSolver;
import org.apache.commons.math3.optim.nonlinear.solvers.impl.RegulaFalsiSolver;
import org.apache.commons.math3.optim.nonlinear.solvers.impl.SparseLineSearchFunction;

public class NonLinearConjugateGradientOptimizerTest {

    @Test
    public void testConstructor() {
        ConjugateGradientFormula updateFormula = ConjugateGradientFormula.FLETCHER_REEVES;
        SimpleValueChecker checker = new SimpleValueChecker();
        BrentSolver lineSearchSolver = new BrentSolver();
        IdentityPreconditioner preconditioner = new IdentityPreconditioner();
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(updateFormula, checker, lineSearchSolver, preconditioner);
        assertNotNull(optimizer);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorNullUpdateFormula() {
        SimpleValueChecker checker = new SimpleValueChecker();
        BrentSolver lineSearchSolver = new BrentSolver();
        IdentityPreconditioner preconditioner = new IdentityPreconditioner();
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(null, checker, lineSearchSolver, preconditioner);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorNullChecker() {
        ConjugateGradientFormula updateFormula = ConjugateGradientFormula.FLETCHER_REEVES;
        BrentSolver lineSearchSolver = new BrentSolver();
        IdentityPreconditioner preconditioner = new IdentityPreconditioner();
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(updateFormula, null, lineSearchSolver, preconditioner);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorNullLineSearchSolver() {
        ConjugateGradientFormula updateFormula = ConjugateGradientFormula.FLETCHER_REEVES;
        SimpleValueChecker checker = new SimpleValueChecker();
        IdentityPreconditioner preconditioner = new IdentityPreconditioner();
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(updateFormula, checker, null, preconditioner);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorNullPreconditioner() {
        ConjugateGradientFormula updateFormula = ConjugateGradientFormula.FLETCHER_REEVES;
        SimpleValueChecker checker = new SimpleValueChecker();
        BrentSolver lineSearchSolver = new BrentSolver();
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(updateFormula, checker, lineSearchSolver, null);
    }

    @Test
    public void testSetInitialStep() {
        ConjugateGradientFormula updateFormula = ConjugateGradientFormula.FLETCHER_REEVES;
        SimpleValueChecker checker = new SimpleValueChecker();
        BrentSolver lineSearchSolver = new BrentSolver();
        IdentityPreconditioner preconditioner = new IdentityPreconditioner();
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(updateFormula, checker, lineSearchSolver, preconditioner);
        optimizer.setInitialStep(2.0);
        assertEquals(2.0, optimizer.getInitialStep(), 0.0);
        optimizer.setInitialStep(-1.0);
        assertEquals(1.0, optimizer.getInitialStep(), 0.0);
    }

    @Test
    public void testOptimize_FletcherReeves() throws MathIllegalStateException {
        ConjugateGradientFormula updateFormula = ConjugateGradientFormula.FLETCHER_REEVES;
        SimpleValueChecker checker = new SimpleValueChecker();
        BrentSolver lineSearchSolver = new BrentSolver();
        IdentityPreconditioner preconditioner = new IdentityPreconditioner();
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(updateFormula, checker, lineSearchSolver, preconditioner);

        UnivariateFunction targetFunction = new SquareFunction();
        double[] startPoint = new double[]{0.5};
        PointValuePair result = optimizer.optimize(targetFunction, startPoint);

        assertEquals(0.0, result.getValue(), 1e-6);
        assertEquals(1.0, result.getPoint()[0], 1e-6);
    }

    @Test
    public void testOptimize_PolakRibiere() throws MathIllegalStateException {
        ConjugateGradientFormula updateFormula = ConjugateGradientFormula.POLAK_RIBIERE;
        SimpleValueChecker checker = new SimpleValueChecker();
        BrentSolver lineSearchSolver = new BrentSolver();
        IdentityPreconditioner preconditioner = new IdentityPreconditioner();
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(updateFormula, checker, lineSearchSolver, preconditioner);

        UnivariateFunction targetFunction = new SquareFunction();
        double[] startPoint = new double[]{0.5};
        PointValuePair result = optimizer.optimize(targetFunction, startPoint);

        assertEquals(0.0, result.getValue(), 1e-6);
        assertEquals(1.0, result.getPoint()[0], 1e-6);
    }

    @Test
    public void testOptimize_FletcherReevesWithPreconditioner() throws MathIllegalStateException {
        ConjugateGradientFormula updateFormula = ConjugateGradientFormula.FLETCHER_REEVES;
        SimpleValueChecker checker = new SimpleValueChecker();
        BrentSolver lineSearchSolver = new BrentSolver();
        Preconditioner preconditioner = new IdentityPreconditioner();
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(updateFormula, checker, lineSearchSolver, preconditioner);
        optimizer.setPreconditioner(new IdentityWithMultiplicationPreconditioner());

        UnivariateFunction targetFunction = new SquareFunction();
        double[] startPoint = new double[]{0.5};
        PointValuePair result = optimizer.optimize(targetFunction, startPoint);

        assertEquals(0.0, result.getValue(), 1e-6);
        assertEquals(1.0, result.getPoint()[0], 1e-6);
    }

    @Test
    public void testOptimize_PolakRibiereWithPreconditioner() throws MathIllegalStateException {
        ConjugateGradientFormula updateFormula = ConjugateGradientFormula.POLAK_RIBIERE;
        SimpleValueChecker checker = new SimpleValueChecker();
        BrentSolver lineSearchSolver = new BrentSolver();
        Preconditioner preconditioner = new IdentityPreconditioner();
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(updateFormula, checker, lineSearchSolver, preconditioner);
        optimizer.setPreconditioner(new IdentityWithMultiplicationPreconditioner());

        UnivariateFunction targetFunction = new SquareFunction();
        double[] startPoint = new double[]{0.5};
        PointValuePair result = optimizer.optimize(targetFunction, startPoint);

        assertEquals(0.0, result.getValue(), 1e-6);
        assertEquals(1.0, result.getPoint()[0], 1e-6);
    }

    @Test
    public void testOptimize_FletcherReevesWithDifferentLineSearchSolver() throws MathIllegalStateException {
        ConjugateGradientFormula updateFormula = ConjugateGradientFormula.FLETCHER_REEVES;
        SimpleValueChecker checker = new SimpleValueChecker();
        NelderMeadSimplexSolver lineSearchSolver = new NelderMeadSimplexSolver();
        IdentityPreconditioner preconditioner = new IdentityPreconditioner();
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(updateFormula, checker, lineSearchSolver, preconditioner);

        UnivariateFunction targetFunction = new SquareFunction();
        double[] startPoint = new double[]{0.5};
        PointValuePair result = optimizer.optimize(targetFunction, startPoint);

        assertEquals(0.0, result.getValue(), 1e-6);
        assertEquals(1.0, result.getPoint()[0], 1e-6);
    }

    @Test
    public void testOptimize_PolakRibiereWithDifferentLineSearchSolver() throws MathIllegalStateException {
        ConjugateGradientFormula updateFormula = ConjugateGradientFormula.POLAK_RIBIERE;
        SimpleValueChecker checker = new SimpleValueChecker();
        LevenbergMarquardtSolver lineSearchSolver = new LevenbergMarquardtSolver();
        IdentityPreconditioner preconditioner = new IdentityPreconditioner();
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(updateFormula, checker, lineSearchSolver, preconditioner);

        UnivariateFunction targetFunction = new SquareFunction();
        double[] startPoint = new double[]{0.5};
        PointValuePair result = optimizer.optimize(targetFunction, startPoint);

        assertEquals(0.0, result.getValue(), 1e-6);
        assertEquals(1.0, result.getPoint()[0], 1e-6);
    }

    @Test
    public void testFindUpperBound() {
        UnivariateFunction targetFunction = new SquareFunction();
        double a = 0.0;
        double h = 1.0;
        double b = findUpperBound(targetFunction, a, h);
        assertGreater(b, a);
    }

    @Test
    public void testIdentityPreconditioner() {
        double[] variables = new double[]{1.0, 2.0};
        double[] r = new double[]{-1.0, 2.0};
        double[] preconditioned = identityPreconditioner.precondition(variables, r);
        assertArrayEquals(r, preconditioned);
    }

    @Test
    public void testIdentityWithMultiplicationPreconditioner() {
        double[] variables = new double[]{1.0, 2.0};
        double[] r = new double[]{-1.0, 2.0};
        double[] preconditioned = identityWithMultiplicationPreconditioner.precondition(variables, r);
        assertArrayEquals(r, preconditioned);
    }

    private double findUpperBound(UnivariateFunction f, double a, double h) {
        double yA = f.value(a);
        double yB = yA;
        double step = h;
        while (yA * yB > 0) {
            double b = a + step;
            yB = f.value(b);
            step *= Math.max(2.0, Math.abs(yA / yB));
        }
        return a + step;
    }

    private Preconditioner identityPreconditioner = new IdentityPreconditioner();
    private Preconditioner identityWithMultiplicationPreconditioner = new IdentityWithMultiplicationPreconditioner();

    private static class SquareFunction implements UnivariateFunction {

        @Override
        public double value(double x) {
            return x * x;
        }

        @Override
        public int getDimension() {
            return 1;
        }
    }
}