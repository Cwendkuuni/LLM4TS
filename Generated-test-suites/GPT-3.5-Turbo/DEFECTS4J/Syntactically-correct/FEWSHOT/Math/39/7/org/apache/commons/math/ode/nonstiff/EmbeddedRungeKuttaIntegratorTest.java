package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class EmbeddedRungeKuttaIntegratorTest {
    @Test
    public void testEmbeddedRungeKuttaIntegrator() {
        // Set up test data
        double[] c = {1.0};
        double[][] a = {{0.0}};
        double[] b = {1.0};
        RungeKuttaStepInterpolator prototype = null;
        double minStep = 0.0;
        double maxStep = 0.0;
        double scalAbsoluteTolerance = 0.0;
        double scalRelativeTolerance = 0.0;
        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("test", false, c, a, b, prototype, minStep, maxStep, scalAbsoluteTolerance, scalRelativeTolerance);
        
        // Test the getOrder() method
        assertEquals(0, integrator.getOrder());
        
        // Test the getSafety() method
        assertEquals(0.0, integrator.getSafety(), 0.0);
        
        // Test the setSafety() method
        integrator.setSafety(1.0);
        assertEquals(1.0, integrator.getSafety(), 0.0);
        
        // Test the getMinReduction() method
        assertEquals(0.0, integrator.getMinReduction(), 0.0);
        
        // Test the setMinReduction() method
        integrator.setMinReduction(0.1);
        assertEquals(0.1, integrator.getMinReduction(), 0.0);
        
        // Test the getMaxGrowth() method
        assertEquals(0.0, integrator.getMaxGrowth(), 0.0);
        
        // Test the setMaxGrowth() method
        integrator.setMaxGrowth(10.0);
        assertEquals(10.0, integrator.getMaxGrowth(), 0.0);
    }
}