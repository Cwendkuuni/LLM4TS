import org.junit.Test;

import static org.junit.Assert.*;

public class NonLinearConjugateGradientOptimizerTest {

    @Test
    public void testOptimize() {
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.FLETCHER_REEVES);

        // Test 1
        double[] startPoint1 = {1, 2, 3, 4};
        double[] expectedPoint1 = {1, 2, 3, 4};
        double expectedValue1 = 0;
        optimizer.setStartPoint(startPoint1);
        optimizer.setMaxEvaluations(100);
        optimizer.optimize();

        assertArrayEquals(expectedPoint1, optimizer.getPoint(), 0.0001);
        assertEquals(expectedValue1, optimizer.getValue(), 0.0001);

        // Test 2
        double[] startPoint2 = {1, 1, 1, 1};
        double[] expectedPoint2 = {1, 1, 1, 1};
        double expectedValue2 = 0;
        optimizer.setStartPoint(startPoint2);
        optimizer.setMaxEvaluations(100);
        optimizer.optimize();

        assertArrayEquals(expectedPoint2, optimizer.getPoint(), 0.0001);
        assertEquals(expectedValue2, optimizer.getValue(), 0.0001);
    }

    @Test
    public void testSetInitialStep() {
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.FLETCHER_REEVES);

        double initialStep = 0;
        optimizer.setInitialStep(initialStep);
        assertEquals(1.0, optimizer.getInitialStep(), 0.0001);

        double initialStep2 = 5;
        optimizer.setInitialStep(initialStep2);
        assertEquals(initialStep2, optimizer.getInitialStep(), 0.0001);
    }

    @Test
    public void testPrecondition() {
        NonLinearConjugateGradientOptimizer.IdentityPreconditioner preconditioner = new NonLinearConjugateGradientOptimizer.IdentityPreconditioner();

        double[] variables = {1, 2, 3, 4};
        double[] r = {1, 2, 3, 4};
        double[] preconditionedR = preconditioner.precondition(variables, r);

        assertArrayEquals(r, preconditionedR, 0.0001);
    }

    @Test
    public void testFindUpperBound() {
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.FLETCHER_REEVES);
        NonLinearConjugateGradientOptimizer.LineSearchFunction function = optimizer.new LineSearchFunction(new double[]{1, 2, 3, 4});

        double a = 0;
        double h = 1;
        double expectedBound = Double.MAX_VALUE;

        double bound = optimizer.findUpperBound(function, a, h);
        assertEquals(expectedBound, bound, 0.0001);
    }

    @Test
    public void testLineSearchFunction() {
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.FLETCHER_REEVES);
        NonLinearConjugateGradientOptimizer.LineSearchFunction function = optimizer.new LineSearchFunction(new double[]{1, 2, 3 ,4});

        double x = 0.5;
        double expectedValue = 10;

        double value = function.value(x);
        assertEquals(expectedValue, value, 0.0001);
    }
}