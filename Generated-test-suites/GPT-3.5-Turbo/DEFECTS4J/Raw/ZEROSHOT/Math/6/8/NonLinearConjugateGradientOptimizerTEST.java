import org.junit.Test;
import static org.junit.Assert.*;

public class NonLinearConjugateGradientOptimizerTest {
    
    @Test
    public void testConstructor() {
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.FLETCHER_REEVES);
        assertNotNull(optimizer);
        
        optimizer = new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.POLAK_RIBIERE);
        assertNotNull(optimizer);
    }
    
    @Test
    public void testSetInitialStep() {
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.FLETCHER_REEVES);
        
        // Test with positive initialStep
        optimizer.setInitialStep(10);
        double initialStep = optimizer.getInitialStep();
        assertEquals(10, initialStep, 0);
        
        // Test with negative initialStep
        optimizer.setInitialStep(-10);
        initialStep = optimizer.getInitialStep();
        assertEquals(1, initialStep, 0);
        
        // Test with zero initialStep
        optimizer.setInitialStep(0);
        initialStep = optimizer.getInitialStep();
        assertEquals(1, initialStep, 0);
    }
    
    @Test
    public void testDoOptimize() {
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.FLETCHER_REEVES);
        optimizer.setInitialStep(1);
        
        double[] startPoint = {0, 0, 0};
        optimizer.setStartPoint(startPoint);
        
        optimizer.setMaxEvaluations(100);
        
        PointValuePair result = optimizer.doOptimize();
        
        assertNotNull(result);
    }
    
    @Test
    public void testIdentityPreconditioner() {
        NonLinearConjugateGradientOptimizer.IdentityPreconditioner preconditioner = new NonLinearConjugateGradientOptimizer.IdentityPreconditioner();
        assertNotNull(preconditioner);
        
        double[] variables = {1, 2, 3};
        double[] r = {4, 5, 6};
        
        double[] result = preconditioner.precondition(variables, r);
        
        assertArrayEquals(r.clone(), result, 0);
    }
    
    @Test
    public void testLineSearchFunction() {
        double[] searchDirection = {1, 2, 3};
        NonLinearConjugateGradientOptimizer.LineSearchFunction func = new NonLinearConjugateGradientOptimizer.LineSearchFunction(searchDirection);
        
        double value = func.value(10);
        
        assertEquals(60, value, 0);
    }
    
}