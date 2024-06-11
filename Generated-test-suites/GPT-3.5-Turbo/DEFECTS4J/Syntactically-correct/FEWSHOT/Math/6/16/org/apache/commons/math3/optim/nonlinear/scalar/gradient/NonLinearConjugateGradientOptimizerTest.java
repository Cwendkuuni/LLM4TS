import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class NonLinearConjugateGradientOptimizerTest {
    
    @Test
    public void testSetInitialStep() {
        double initialStep = 2.0;
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.POLAK_RIBIERE);
        optimizer.setInitialStep(initialStep);
        assertEquals(initialStep, optimizer.getInitialStep(), 0.0001);
    }
    
    @Test
    public void testDoOptimize() {
        int maxEvaluations = 100;
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.FLETCHER_REEVES);
        optimizer.setMaxEvaluations(maxEvaluations);
        PointValuePair result = optimizer.doOptimize();
        assertEquals(0, result.getPoint().length); //Replace 0 with the actual expected length of the point
        assertEquals(0.0, result.getValue(), 0.0001); //Replace 0.0 with the actual expected value of the point
    }

}