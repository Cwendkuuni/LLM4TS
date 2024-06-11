
package org.apache.commons.math3.ode.nonstiff;

import org.apache.commons.math3.ode.*;
import org.apache.commons.math3.util.*;
import org.apache.commons.math3.ode.sampling.*;
import org.apache.commons.math3.exception.*;
import org.junit.Test;

import static org.junit.Assert.*;

public class EmbeddedRungeKuttaIntegratorTest {
    
    @Test
    public void testGetName() {
        EmbeddedRungeKuttaIntegrator integrator = new CustomRungeKuttaIntegrator();
        assertEquals("CustomRungeKutta", integrator.getName());
    }
    
    @Test
    public void testGetOrder() {
        EmbeddedRungeKuttaIntegrator integrator = new CustomRungeKuttaIntegrator();
        assertEquals(4, integrator.getOrder());
    }
    
    @Test
    public void testGetSafety() {
        EmbeddedRungeKuttaIntegrator integrator = new CustomRungeKuttaIntegrator();
        assertEquals(0.9, integrator.getSafety(), 0.0);
    }
    
    @Test
    public void testSetSafety() {
        EmbeddedRungeKuttaIntegrator integrator = new CustomRungeKuttaIntegrator();
        integrator.setSafety(0.8);
        assertEquals(0.8, integrator.getSafety(), 0.0);
    }
    
    @Test
    public void testGetMinReduction() {
        EmbeddedRungeKuttaIntegrator integrator = new CustomRungeKuttaIntegrator();
        assertEquals(0.2, integrator.getMinReduction(), 0.0);
    }
    
    @Test
    public void testSetMinReduction() {
        EmbeddedRungeKuttaIntegrator integrator = new CustomRungeKuttaIntegrator();
        integrator.setMinReduction(0.1);
        assertEquals(0.1, integrator.getMinReduction(), 0.0);
    }
    
    @Test
    public void testGetMaxGrowth() {
        EmbeddedRungeKuttaIntegrator integrator = new CustomRungeKuttaIntegrator();
        assertEquals(10.0, integrator.getMaxGrowth(), 0.0);
    }
    
    @Test
    public void testSetMaxGrowth() {
        EmbeddedRungeKuttaIntegrator integrator = new CustomRungeKuttaIntegrator();
        integrator.setMaxGrowth(20.0);
        assertEquals(20.0, integrator.getMaxGrowth(), 0.0);
    }
    
    @Test
    public void testEstimateError() {
        EmbeddedRungeKuttaIntegrator integrator = new CustomRungeKuttaIntegrator();
        double[][] p0 = new double[][]{{1.0, 2.0}, {3.0, 4.0}};
        double[] p1 = new double[]{5.0, 6.0};
        double[] p2 = new double[]{7.0, 8.0};
        double p3 = 0.1;
        double error = integrator.estimateError(p0, p1, p2, p3);
        assertEquals(9.0, error, 0.0);
    }
    
    private class CustomRungeKuttaIntegrator extends EmbeddedRungeKuttaIntegrator {
        
        public CustomRungeKuttaIntegrator() {
            super("CustomRungeKutta", true, new double[]{1.0, 2.0}, new double[][]{{3.0, 4.0}, {5.0, 6.0}}, new double[]{7.0, 8.0}, null, 0.0, 0.0, 0.0, 0.0);
        }
        
        public int getOrder() {
            return 4;
        }
        
        public double estimateError(double[][] p0, double[] p1, double[] p2, double p3) {
            return 9.0;
        }
        
        protected double filterStep(double p0, boolean p1, boolean p2) {
            return p0;
        }
        
        protected void computeDerivatives(double p0, double[] p1, double[] p2) {
            // implementation
        }
        
        protected double initializeStep(boolean p0, int p1, double[] p2, double p3, double[] p4, double[] p5, double[] p6, double[] p7) {
            return 0.0;
        }
        
        protected double acceptStep(RungeKuttaStepInterpolator p0, double[] p1, double[] p2, double p3) {
            return 0.0;
        }
        
        protected void sanityChecks(ExpandableStatefulODE p0, double p1) throws NumberIsTooSmallException, DimensionMismatchException, MaxCountExceededException, NoBracketingException {
            // implementation
        }
        
        protected void setEquations(ExpandableStatefulODE p0) {
            // implementation
        }
        
        protected void initIntegration(double p0, double[] p1, double p2) throws DimensionMismatchException, NumberIsTooSmallException {
            // implementation
        }
        
        protected double[] getC() {
            return null;
        }
        
        protected double[][] getA() {
            return null;
        }
        
        protected double[] getB() {
            return null;
        }
        
        protected double getStepSizeControl1() {
            return 0.0;
        }
        
        protected double getStepSizeControl2() {
            return 0.0;
        }
        
        protected void resetInternalState() {
            // implementation
        }
    }
}
