package org.apache.commons.math.ode.nonstiff;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.events.DefaultValueEvent;
import org.apache.commons.math3.ode.events.Event;
import org.apache.commons.math3.ode.events.EventManager;
import org.apache.commons.math3.ode.events.TwoFloatValuesEvent;
import org.apache.commons.math3.ode.sampling.RungeKuttaStepInterpolator;
import org.apache.commons.math3.ode.util.ODETestUtils;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;

public class EmbeddedRungeKuttaIntegratorTest {

    @Test
    public void testEmbeddedRungeKuttaIntegrator() {
        Double[][] coefficients = new Double[][] {
            {0.0, 0.0},
            {0.3, 0.27},
            {0.45, 0.375, 0.0},
            {0.55, 0.3, 0.45, 0.075},
            {0.7, 0.36, 0.48, 0.27, 0.1}
        };
        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("RKF45", true,
                new double[] {0.0, 0.3, 0.55, 0.7}, coefficients[0], coefficients[1],
                new RungeKuttaStepInterpolator(), 0.001, 0.01, null, null) {

            @Override
            protected double estimateError(double[][] yDotK, double[] y0, double[] y1, double h) {
                double error = 0.0;
                for (int i = 0; i < y0.length; i++) {
                    error += Precision.safeMax(Precision.safeMax(Math.abs(yDotK[0][i] - yDotK[yDotK.length - 1][i]),
                            Math.abs(y1[i] - y0[i])), 1.0e-15);
                }
                return error;
            }
        };

        FirstOrderDifferentialEquations equations = ODETestUtils.createMockODESystem(2);
        double[] y0 = {0.0, 0.0};
        double tStart = 0.0;
        double tEnd = 10.0;
        double[] yEnd = {1.0, 1.0};
        double stepSize = 0.01;

        double result = integrator.integrate(equations, tStart, y0, tEnd, yEnd);
        assertNotNull(result);
        assertEquals(tEnd, result, 1.0e-9);
        assertEquals(yEnd[0], y0[0], 1.0e-9);
        assertEquals(yEnd[1], y0[1], 1.0e-9);

        EventManager<TwoFloatValuesEvent> manager = new EventManager<>();
        Event<TwoFloatValuesEvent> event = new DefaultValueEvent(1.5, 2.5);
        manager.addEvent(event);
        integrator = new EmbeddedRungeKuttaIntegrator("RKF45", true,
                new double[] {0.0, 0.3, 0.55, 0.7}, coefficients[0], coefficients[1],
                new RungeKuttaStepInterpolator(), 0.001, 0.01, null, manager.getEvents()) {

            @Override
            protected double estimateError(double[][] yDotK, double[] y0, double[] y1, double h) {
                double error = 0.0;
                for (int i = 0; i < y0.length; i++) {
                    error += Precision.safeMax(Precision.safeMax(Math.abs(yDotK[0][i] - yDotK[yDotK.length - 1][i]),
                            Math.abs(y1[i] - y0[i])), 1.0e-15);
                }
                return error;
            }
        };

        double result2 = integrator.integrate(equations, tStart, y0, tEnd, yEnd);
        assertNotNull(result2);
        assertEquals(tEnd, result2, 1.0e-9);
        assertEquals(yEnd[0], y0[0], 1.0e-9);
        assertEquals(yEnd[1], y0[1], 1.0e-9);
        assertNotEquals(result, result2);
    }

}