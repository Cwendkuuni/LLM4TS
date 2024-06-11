import org.apache.commons.math3.ode.*;
import org.apache.commons.math3.util.*;
import org.apache.commons.math3.ode.sampling.*;
import org.apache.commons.math3.exception.*;

import org.junit.Test;
import org.junit.Assert;

public class EmbeddedRungeKuttaIntegratorTest {
    
    @Test
    public void testGetOrder() {
        EmbeddedRungeKuttaIntegrator integrator = new TestEmbeddedRungeKuttaIntegrator();
        Assert.assertEquals(0, integrator.getOrder());
    }
    
    @Test
    public void testGetSafety() {
        EmbeddedRungeKuttaIntegrator integrator = new TestEmbeddedRungeKuttaIntegrator();
        Assert.assertEquals(0.9, integrator.getSafety(), 0.0001);
    }
    
    @Test
    public void testSetSafety() {
        EmbeddedRungeKuttaIntegrator integrator = new TestEmbeddedRungeKuttaIntegrator();
        integrator.setSafety(0.8);
        Assert.assertEquals(0.8, integrator.getSafety(), 0.0001);
    }
    
    @Test
    public void testIntegrate() {
        EmbeddedRungeKuttaIntegrator integrator = new TestEmbeddedRungeKuttaIntegrator();
        ExpandableStatefulODE equations = new ExpandableStatefulODE();
        double t = 0.0;
        try {
            integrator.integrate(equations, t);
        } catch (Exception e) {
            Assert.fail("Exception thrown during integration: " + e.getMessage());
        }
    }
    
    @Test
    public void testGetMinReduction() {
        EmbeddedRungeKuttaIntegrator integrator = new TestEmbeddedRungeKuttaIntegrator();
        Assert.assertEquals(0.2, integrator.getMinReduction(), 0.0001);
    }
    
    @Test
    public void testSetMinReduction() {
        EmbeddedRungeKuttaIntegrator integrator = new TestEmbeddedRungeKuttaIntegrator();
        integrator.setMinReduction(0.1);
        Assert.assertEquals(0.1, integrator.getMinReduction(), 0.0001);
    }
    
    @Test
    public void testGetMaxGrowth() {
        EmbeddedRungeKuttaIntegrator integrator = new TestEmbeddedRungeKuttaIntegrator();
        Assert.assertEquals(10.0, integrator.getMaxGrowth(), 0.0001);
    }
    
    @Test
    public void testSetMaxGrowth() {
        EmbeddedRungeKuttaIntegrator integrator = new TestEmbeddedRungeKuttaIntegrator();
        integrator.setMaxGrowth(20.0);
        Assert.assertEquals(20.0, integrator.getMaxGrowth(), 0.0001);
    }
    
    private class TestEmbeddedRungeKuttaIntegrator extends EmbeddedRungeKuttaIntegrator {
        public TestEmbeddedRungeKuttaIntegrator() {
            super("", true, null, null, null, null, 0, 0, 0, 0);
        }
        
        public int getOrder() {
            return 0;
        }
        
        public double estimateError(final double[][] p0, final double[] p1, final double[] p2, final double p3) {
            return 0;
        }
    }
}