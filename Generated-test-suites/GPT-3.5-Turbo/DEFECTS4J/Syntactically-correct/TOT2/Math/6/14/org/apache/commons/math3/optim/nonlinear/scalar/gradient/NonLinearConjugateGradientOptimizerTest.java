import org.junit.Test;
import static org.junit.Assert.*;

public class NonLinearConjugateGradientOptimizerTest {

    @Test
    public void testConstructorUpdateFormula() {
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.FLETCHER_REEVES);
        assertNotNull(optimizer);
    }

    @Test
    public void testSetInitialStep() {
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.FLETCHER_REEVES);
        optimizer.setInitialStep(0.0);
        assertEquals(1.0, optimizer.initialStep, 0);
    }

    @Test
    public void testFindUpperBound() {
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.FLETCHER_REEVES);
        UnivariateFunction f = (x) -> x - 2;
        double upperBound = optimizer.findUpperBound(f, 1.0, 1.0);
        assertEquals(3.0, upperBound, 0);
    }

    @Test
    public void testPreconditioner() {
        NonLinearConjugateGradientOptimizer.IdentityPreconditioner precon = new NonLinearConjugateGradientOptimizer.IdentityPreconditioner();
        double[] variables = {1.0, 2.0, 3.0};
        double[] r = {1.0, 2.0, 3.0};
        assertArrayEquals(r, precon.precondition(variables, r), 0);
    }

    @Test
    public void testLineSearchFunction() {
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.FLETCHER_REEVES);
        NonLinearConjugateGradientOptimizer.LineSearchFunction lsf = optimizer.new LineSearchFunction(new double[]{1.0, 2.0, 3.0});
        double value = lsf.value(0.5);
        assertEquals(14.5, value, 0);
    }

    // Add more test cases for other methods as needed...

}