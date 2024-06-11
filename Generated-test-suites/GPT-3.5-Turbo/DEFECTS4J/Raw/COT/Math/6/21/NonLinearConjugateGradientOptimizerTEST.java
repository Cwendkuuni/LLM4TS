import org.junit.Test;
import static org.junit.Assert.*;

public class NonLinearConjugateGradientOptimizerTest {

    @Test
    public void testOptimize() {
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.FLETCHER_REEVES);
        
        // Test case 1
        double[] startPoint1 = {1, 2, 3};
        PointValuePair expected1 = new PointValuePair(startPoint1, 0);
        PointValuePair result1 = optimizer.optimize(startPoint1);
        assertArrayEquals(expected1.getPoint(), result1.getPoint(), 1e-15);
        assertEquals(expected1.getValue(), result1.getValue(), 1e-15);
        
        // Test case 2
        double[] startPoint2 = {4, 5, 6};
        PointValuePair expected2 = new PointValuePair(startPoint2, 0);
        PointValuePair result2 = optimizer.optimize(startPoint2);
        assertArrayEquals(expected2.getPoint(), result2.getPoint(), 1e-15);
        assertEquals(expected2.getValue(), result2.getValue(), 1e-15);
    }

    @Test
    public void testSetInitialStep() {
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.POLAK_RIBIERE);
        
        // Test case 1: initial step > 0
        double initialStep1 = 0.5;
        optimizer.setInitialStep(initialStep1);
        assertEquals(initialStep1, optimizer.getInitialStep(), 1e-15);
        
        // Test case 2: initial step <= 0, default value should be used
        double initialStep2 = 0;
        optimizer.setInitialStep(initialStep2);
        assertEquals(1.0, optimizer.getInitialStep(), 1e-15);
    }

    @Test
    public void testIdentityPreconditioner() {
        NonLinearConjugateGradientOptimizer.IdentityPreconditioner preconditioner = new NonLinearConjugateGradientOptimizer.IdentityPreconditioner();
        double[] variables = {1, 2, 3};
        double[] r = {4, 5, 6};
        double[] result = preconditioner.precondition(variables, r);
        assertArrayEquals(r, result, 1e-15);
    }
}