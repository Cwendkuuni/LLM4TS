package org.apache.commons.math.ode.nonstiff;

import org.apache.commons.math.ode.nonstiff.EmbeddedRungeKuttaIntegrator;
import org.junit.Assert;
import org.junit.Test;

public class EmbeddedRungeKuttaIntegratorTest {
    
    @Test
    public void testSucceeds() {
        double[] c = {1.0, 1.0 / 2.0};
        double[][] a = {{1.0}, {1.0 / 2.0, 1.0 / 2.0}};
        double[] b = {1.0 / 2.0, 1.0};
        RungeKuttaStepInterpolator prototype = new RungeKuttaStepInterpolator();
        double minStep = 0.001;
        double maxStep = 0.1;
        double scalAbsoluteTolerance = 1e-6;
        double scalRelativeTolerance = 1e-6;
        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("Test", true, c, a, b, prototype, minStep, maxStep, scalAbsoluteTolerance, scalRelativeTolerance) {
            public int getOrder() {
                return 2;
            }
            protected double estimateError(double[][] yDotK, double[] y0, double[] y1, double h) {
                return 0;
            }
        };
        
        Assert.assertEquals(2, integrator.getOrder());
    }
}