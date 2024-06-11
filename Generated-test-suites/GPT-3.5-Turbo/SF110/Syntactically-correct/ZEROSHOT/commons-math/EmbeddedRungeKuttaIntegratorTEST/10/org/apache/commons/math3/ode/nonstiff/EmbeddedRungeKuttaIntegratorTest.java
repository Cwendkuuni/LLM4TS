package org.apache.commons.math3.ode.nonstiff;

import org.apache.commons.math3.ode.*;
import org.apache.commons.math3.util.*;
import org.apache.commons.math3.ode.sampling.*;
import org.apache.commons.math3.exception.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class EmbeddedRungeKuttaIntegratorTest {

    @Test
    public void testGetSafety() {
        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegratorImpl();
        double safety = integrator.getSafety();
        assertEquals(0.9, safety, 0.0001);
    }

    @Test
    public void testSetSafety() {
        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegratorImpl();
        integrator.setSafety(0.8);
        double safety = integrator.getSafety();
        assertEquals(0.8, safety, 0.0001);
    }
    
    @Test
    public void testGetMinReduction() {
        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegratorImpl();
        double minReduction = integrator.getMinReduction();
        assertEquals(0.2, minReduction, 0.0001);
    }

    @Test
    public void testSetMinReduction() {
        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegratorImpl();
        integrator.setMinReduction(0.3);
        double minReduction = integrator.getMinReduction();
        assertEquals(0.3, minReduction, 0.0001);
    }

    @Test
    public void testGetMaxGrowth() {
        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegratorImpl();
        double maxGrowth = integrator.getMaxGrowth();
        assertEquals(10.0, maxGrowth, 0.0001);
    }

    @Test
    public void testSetMaxGrowth() {
        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegratorImpl();
        integrator.setMaxGrowth(15.0);
        double maxGrowth = integrator.getMaxGrowth();
        assertEquals(15.0, maxGrowth, 0.0001);
    }

    private static class EmbeddedRungeKuttaIntegratorImpl extends EmbeddedRungeKuttaIntegrator {
        
        public EmbeddedRungeKuttaIntegratorImpl() {
            super("Embedded Runge Kutta Integrator", true, new double[]{}, new double[][]{{}}, new double[]{}, new RungeKuttaStepInterpolator() {
                @Override
                public StepInterpolator copy() {
                    return null;
                }

                @Override
                public void reinitialize(AbstractIntegrator integrator, double[] y, double[][] yDotK, boolean forward, EquationsMapper primaryMapper, EquationsMapper[] secondaryMappers) {

                }

                @Override
                public void storeTime(double t) {

                }

                @Override
                public double[] getInterpolatedState(double time) throws MaxCountExceededException {

                    return null;
                }

                @Override
                public double[] getInterpolatedDerivatives(double time) throws MaxCountExceededException {

                    return null;
                }

                @Override
                public double[] getCurrentState() throws MaxCountExceededException {

                    return null;
                }

                @Override
                public double[] getCurrentDerivatives() throws MaxCountExceededException {

                    return null;
                }
            }, 0.0, 0.0, 0.0, 0.0);
        }

        @Override
        public int getOrder() {
            return 0;
        }

        @Override
        protected double estimateError(double[][] p0, double[] p1, double[] p2, double p3) {
            return 0;
        }
    }
}