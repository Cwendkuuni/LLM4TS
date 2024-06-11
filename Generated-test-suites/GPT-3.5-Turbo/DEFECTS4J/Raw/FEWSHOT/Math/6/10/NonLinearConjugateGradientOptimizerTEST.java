package org.apache.commons.math3.optimization.general;

import static org.junit.Assert.assertEquals;

import org.junit.BeforeClass;
import org.junit.Test;

public class NonLinearConjugateGradientOptimizerTest {

    private static NonLinearConjugateGradientOptimizer optimizer;
    
    @BeforeClass
    public static void setUp() {
        optimizer = new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.FLETCHER_REEVES);
    }
    
    @Test
    public void testSetInitialStep() {
        optimizer.setInitialStep(0.5);
        assertEquals(0.5, optimizer.getInitialStep(), 0.0);
        
        optimizer.setInitialStep(-1.0);
        assertEquals(1.0, optimizer.getInitialStep(), 0.0);
    }
    
    @Test
    public void testDoOptimize() {
        // TODO: Write test cases to comprehensively test doOptimize() method
    }
    
    @Test
    public void testFindUpperBound() {
        // TODO: Write test cases to test findUpperBound() method
    }
    
    @Test
    public void testIdentityPreconditioner() {
        NonLinearConjugateGradientOptimizer.IdentityPreconditioner preconditioner = new NonLinearConjugateGradientOptimizer.IdentityPreconditioner();
        double[] variables = {1.0, 2.0, 3.0};
        double[] r = {1.0, 2.0, 3.0};
        double[] result = preconditioner.precondition(variables, r);
        
        assertEquals(r.length, result.length);
        for (int i = 0; i < r.length; i++) {
            assertEquals(r[i], result[i], 0.0);
        }
    }
    
    @Test
    public void testLineSearchFunction() {
        double[] searchDirection = {1.0, -1.0, 0.0};
        NonLinearConjugateGradientOptimizer.LineSearchFunction function = optimizer.new LineSearchFunction(searchDirection);
        
        double x = 0.5;
        double expectedResult = searchDirection[0] + (-1) * searchDirection[1] + 0 * searchDirection[2];
        double result = function.value(x);
        
        assertEquals(expectedResult, result, 0.0);
    }
}