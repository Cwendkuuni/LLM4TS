package org.apache.commons.math3.optimization.general;

import static org.junit.Assert.*;
import org.junit.Test;

public class NonLinearConjugateGradientOptimizerTest {
    
    @Test
    public void testSetInitialStep() {
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.FLETCHER_REEVES);
        optimizer.setInitialStep(1.5);
        assertEquals(1.5, optimizer.getInitialStep(), 0);
    }
    
    @Test
    public void testDoOptimize() {
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.FLETCHER_REEVES);
        optimizer.setMaxEvaluations(1000);
        
        double[] startPoint = new double[]{1.0, 2.0, 3.0};
        optimizer.setStartPoint(startPoint);
        
        PointValuePair result = optimizer.optimize();
        
        assertNotNull(result);
        assertNotNull(result.getPoint());
        assertNotNull(result.getValue());
    }
}