package org.apache.commons.math3.ode.nonstiff;

import org.apache.commons.math3.ode.*;
import org.apache.commons.math3.util.*;
import org.apache.commons.math3.ode.sampling.*;
import org.apache.commons.math3.exception.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class EmbeddedRungeKuttaIntegratorTest {
    
    @Test
    public void testGetOrder() {
        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("name", true, new double[]{}, new double[][]{}, new double[]{}, new RungeKuttaStepInterpolator(), 0.0, 1.0, 0.0, 0.0) {
            @Override
            public int getOrder() {
                return 0;
            }
            @Override
            protected double estimateError(double[][] p0, double[] p1, double[] p2, double p3) {
                return 0.0;
            }
        };
        
        int order = integrator.getOrder();
        
        assertEquals(0, order);
    }
    
    @Test
    public void testGetSafety() {
        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("name", true, new double[]{}, new double[][]{}, new double[]{}, new RungeKuttaStepInterpolator(), 0.0, 1.0, 0.0, 0.0) {
            @Override
            public int getOrder() {
                return 0;
            }
            @Override
            protected double estimateError(double[][] p0, double[] p1, double[] p2, double p3) {
                return 0.0;
            }
        };
        
        double safety = integrator.getSafety();
        
        assertEquals(0.9, safety, 0.0);
    }
    
    @Test
    public void testSetSafety() {
        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("name", true, new double[]{}, new double[][]{}, new double[]{}, new RungeKuttaStepInterpolator(), 0.0, 1.0, 0.0, 0.0) {
            @Override
            public int getOrder() {
                return 0;
            }
            @Override
            protected double estimateError(double[][] p0, double[] p1, double[] p2, double p3) {
                return 0.0;
            }
        };
        
        integrator.setSafety(0.8);
        
        assertEquals(0.8, integrator.getSafety(), 0.0);
    }
    
    @Test
    public void testGetMinReduction() {
        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("name", true, new double[]{}, new double[][]{}, new double[]{}, new RungeKuttaStepInterpolator(), 0.0, 1.0, 0.0, 0.0) {
            @Override
            public int getOrder() {
                return 0;
            }
            @Override
            protected double estimateError(double[][] p0, double[] p1, double[] p2, double p3) {
                return 0.0;
            }
        };
        
        double minReduction = integrator.getMinReduction();
        
        assertEquals(0.2, minReduction, 0.0);
    }
    
    @Test
    public void testSetMinReduction() {
        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("name", true, new double[]{}, new double[][]{}, new double[]{}, new RungeKuttaStepInterpolator(), 0.0, 1.0, 0.0, 0.0) {
            @Override
            public int getOrder() {
                return 0;
            }
            @Override
            protected double estimateError(double[][] p0, double[] p1, double[] p2, double p3) {
                return 0.0;
            }
        };
        
        integrator.setMinReduction(0.3);
        
        assertEquals(0.3, integrator.getMinReduction(), 0.0);
    }
    
    @Test
    public void testGetMaxGrowth() {
        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("name", true, new double[]{}, new double[][]{}, new double[]{}, new RungeKuttaStepInterpolator(), 0.0, 1.0, 0.0, 0.0) {
            @Override
            public int getOrder() {
                return 0;
            }
            @Override
            protected double estimateError(double[][] p0, double[] p1, double[] p2, double p3) {
                return 0.0;
            }
        };
        
        double maxGrowth = integrator.getMaxGrowth();
        
        assertEquals(10.0, maxGrowth, 0.0);
    }
    
    @Test
    public void testSetMaxGrowth() {
        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("name", true, new double[]{}, new double[][]{}, new double[]{}, new RungeKuttaStepInterpolator(), 0.0, 1.0, 0.0, 0.0) {
            @Override
            public int getOrder() {
                return 0;
            }
            @Override
            protected double estimateError(double[][] p0, double[] p1, double[] p2, double p3) {
                return 0.0;
            }
        };
        
        integrator.setMaxGrowth(11.0);
        
        assertEquals(11.0, integrator.getMaxGrowth(), 0.0);
    }
}