package org.apache.commons.math3.optimization.general;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;

public class GaussNewtonOptimizerTest {
    
    private GaussNewtonOptimizer optimizer;

    @Before
    public void setUp() {
        optimizer = new GaussNewtonOptimizer();
    }

    @Test
    public void testConstructor() {
        assertFalse(optimizer.useLU());
        assertNotNull(optimizer.getConvergenceChecker());
    }

    @Test
    public void testConstructorWithChecker() {
        ConvergenceChecker<PointVectorValuePair> checker = new SimpleVectorValueChecker();
        GaussNewtonOptimizer optimizerWithChecker = new GaussNewtonOptimizer(checker);
        
        assertFalse(optimizerWithChecker.useLU());
        assertEquals(checker, optimizerWithChecker.getConvergenceChecker());
    }

    @Test
    public void testConstructorWithUseLU() {
        GaussNewtonOptimizer optimizerWithUseLU = new GaussNewtonOptimizer(true);
        
        assertTrue(optimizerWithUseLU.useLU());
        assertNotNull(optimizerWithUseLU.getConvergenceChecker());
    }

    @Test
    public void testConstructorWithUseLUAndChecker() {
        ConvergenceChecker<PointVectorValuePair> checker = new SimpleVectorValueChecker();
        GaussNewtonOptimizer optimizerWithUseLUAndChecker = new GaussNewtonOptimizer(true, checker);
        
        assertTrue(optimizerWithUseLUAndChecker.useLU());
        assertEquals(checker, optimizerWithUseLUAndChecker.getConvergenceChecker());
    }

    @Test(expected = NullArgumentException.class)
    public void testDoOptimizeNullChecker() {
        optimizer.removeConvergenceChecker();
        optimizer.doOptimize();
    }

}