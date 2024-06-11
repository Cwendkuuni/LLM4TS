import org.junit.Test;
import static org.junit.Assert.*;

public class NonLinearConjugateGradientOptimizerTest {

    @Test
    public void testSetInitialStep() {
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.FLETCHER_REEVES);
        optimizer.setInitialStep(0.0);
        assertEquals(1.0, optimizer.getInitialStep(), 1e-15);
    }

    @Test
    public void testFindUpperBound() {
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.FLETCHER_REEVES);
        LineSearchFunction testFunction = optimizer.new LineSearchFunction(new double[] {1.0, 2.0});
        double upperBound = optimizer.findUpperBound(testFunction, 0.0, 1.0);
        assertEquals(2.0, upperBound, 1e-15);
    }

    @Test
    public void testPreconditioner() {
        NonLinearConjugateGradientOptimizer.IdentityPreconditioner preconditioner = new NonLinearConjugateGradientOptimizer.IdentityPreconditioner();
        double[] input = {1.0, 2.0};
        double[] result = preconditioner.precondition(input, input);
        assertArrayEquals(input, result, 1e-15);
    }

    // Add more test cases as needed

}