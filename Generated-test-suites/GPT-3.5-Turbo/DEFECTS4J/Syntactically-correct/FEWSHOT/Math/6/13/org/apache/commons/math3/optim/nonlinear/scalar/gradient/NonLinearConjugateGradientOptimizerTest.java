package org.apache.commons.math3.optimization.general;

import static org.junit.Assert.*;
import org.junit.Test;

public class NonLinearConjugateGradientOptimizerTest {
    @Test
    public void testConstructor() {
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.FLETCHER_REEVES);
        assertNotNull(optimizer);
    }

    @Test
    public void testSetInitialStep() {
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.POLAK_RIBIERE);
        optimizer.setInitialStep(1.5);
    }

    @Test
    public void testDoOptimize() {
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.FLETCHER_REEVES);
        optimizer.setInitialStep(1.5);
        double[] startPoint = {1, 2, 3};
        optimizer.setStartPoint(startPoint);
        optimizer.setConvergenceChecker(new SimpleValueChecker());
        optimizer.setMaxEvaluations(100);
        PointValuePair result = optimizer.doOptimize();
        assertNotNull(result);
    }

    @Test
    public void testIdentityPreconditioner() {
        NonLinearConjugateGradientOptimizer.IdentityPreconditioner preconditioner = new NonLinearConjugateGradientOptimizer.IdentityPreconditioner();
        double[] variables = {1, 2, 3};
        double[] r = {2, 3, 4};
        double[] result = preconditioner.precondition(variables, r);
        assertArrayEquals(r, result, 0.0);
    }

    @Test
    public void testLineSearchFunction() {
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.POLAK_RIBIERE);
        double[] searchDirection = {1, 2, 3};
        NonLinearConjugateGradientOptimizer.LineSearchFunction function = optimizer.new LineSearchFunction(searchDirection);
        double result = function.value(1.5);
        assertEquals(14.5, result, 0.0);
    }
}