import org.junit.Test;
import static org.junit.Assert.*;

public class NonLinearConjugateGradientOptimizerTest {

    @Test
    public void testSetInitialStep() {
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.FLETCHER_REEVES);
        optimizer.setInitialStep(0.5);
        double initialStep = optimizer.getInitialStep();
        assertEquals(0.5, initialStep, 1e-15);
    }

    @Test
    public void testFindUpperBound() {
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.FLETCHER_REEVES);
        UnivariateFunction f = new LineSearchFunction(new double[]{1, 2, 3});
        double upperBound = optimizer.findUpperBound(f, 0, 1);
        assertEquals(1.0, upperBound, 1e-15);
    }

    @Test(expected = MathIllegalStateException.class)
    public void testFindUpperBoundNoBracket() {
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.FLETCHER_REEVES);
        UnivariateFunction f = new LineSearchFunction(new double[]{1, 2, 3});
        optimizer.findUpperBound(f, 0, 0.5);
    }

    @Test
    public void testIdentityPreconditioner() {
        NonLinearConjugateGradientOptimizer.IdentityPreconditioner preconditioner =
                new NonLinearConjugateGradientOptimizer.IdentityPreconditioner();
        double[] variables = {1, 2, 3};
        double[] r = {4, 5, 6};
        double[] result = preconditioner.precondition(variables, r);
        assertArrayEquals(r, result, 1e-15);
    }

    @Test
    public void testLineSearchFunction() {
        double[] searchDirection = {1, 2, 3};
        double[] point = {4, 5, 6};
        NonLinearConjugateGradientOptimizer.LineSearchFunction lsf =
                new NonLinearConjugateGradientOptimizer.LineSearchFunction(searchDirection);
        double value = lsf.value(1);
        assertEquals(32.0, value, 1e-15);
    }
}