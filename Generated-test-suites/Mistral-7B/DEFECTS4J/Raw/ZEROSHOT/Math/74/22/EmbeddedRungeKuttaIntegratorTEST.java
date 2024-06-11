package org.apache.commons.math.ode.nonstiff.EmbeddedRungeKuttaIntegrator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.events.DefaultValueEvent;
import org.apache.commons.math3.ode.events.Event;
import org.apache.commons.math3.ode.events.EventManager;
import org.apache.commons.math3.ode.events.IncrementEvent;
import org.apache.commons.math3.ode.sampling.RungeKuttaStepInterpolator;
import org.apache.commons.math3.ode.test.TestODE;
import org.apache.commons.math3.special.BesselFunctions;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class EmbeddedRungeKuttaIntegratorTest {

    private static FirstOrderDifferentialEquations eq = new TestODE();

    @org.junit.Test
    public void testRungeKuttaFehlberg78() {

        final EmbeddedRungeKuttaIntegrator rk78 =
                new EmbeddedRungeKuttaIntegrator("Fehlberg78", false,
                        new double[] { 0, 0.3, 0.47, 0.0, 0.2598, 0.0568, 0.0129, 0.0, 0.0 },
                        new double[][] { { 0.3466, 0.4934 },
                                         { 0.2369, 0.6789 },
                                         { 0.0, 0.8335 },
                                         { 0.5555, 0.7721 },
                                         { 0.8335, 0.4444 },
                                         { 1.0, 1.0 },
                                         { 0.3466, 0.0 },
                                         { 0.6789, 0.0 },
                                         { 0.8335, 0.0 } },
                        new double[] { 0.3466, 0.2369, 0.6789, 0.8335 },
                        new RungeKuttaStepInterpolator(),
                        1.0e-12, 1.0e-12) {
                    @Override
                    protected int getOrder() {
                        return 7;
                    }
                };

        final double[] initialValue = new double[] { 1.0, 1.0 };
        final double[] expectedValue = new double[] { 2.718281828, 2.718281828 };

        // Test integration with a simple ODE
        double stepSize = 1.0;
        double t = 1.0;
        double[] y = new double[2];
        for (int i = 0; i < 2; i++) {
            y[i] = initialValue[i];
        }
        double stopTime = rk78.integrate(eq, 0.0, initialValue, t, y);
        assertEquals(expectedValue[0], y[0], 1.0e-12);
        assertEquals(expectedValue[1], y[1], 1.0e-12);
        assertEquals(t, stopTime, 1.0e-12);

        // Test integration with events
        EventManager eventManager = new EventManager();
        eventManager.addRelatedEvents(
                Arrays.asList(new IncrementEvent(1.5),
                             new DefaultValueEvent(2.0)));
        rk78.setEventsHandlersManager(eventManager);
        stepSize = 1.0;
        t = 1.0;
        y = new double[2];
        for (int i = 0; i < 2; i++) {
            y[i] = initialValue[i];
        }
        stopTime = rk78.integrate(eq, 0.0, initialValue, t, y);
        assertFalse(eventManager.isEventActive());
        assertEquals(expectedValue[0], y[0], 1.0e-12);
        assertEquals(expectedValue[1], y[1], 1.0e-12);
        assertEquals(t, stopTime, 1.0e-12);

        stepSize = stepSize / 2.0;
        t = stopTime;
        y = new double[2];
        for (int i = 0; i < 2; i++) {
            y[i] = initialValue[i];
        }
        stopTime = rk78.integrate(eq, t, y, t + stepSize, y);
        assertTrue(eventManager.isEventActive());
        assertEquals(2.0, y[0], 1.0e-12);
        assertEquals(BesselFunctions.BesselI(0, 2.0), y[1], 1.0e-12);
        assertEquals(t + stepSize, stopTime, 1.0e-12);

        stepSize = rk78.getStepSize();
        assertTrue(stepSize > 1.0e-12);

        rk78.setMinReduction(0.5);
        rk78.setMaxGrowth(2.0);
        stepSize = stepSize / 4.0;
        assertArrayEquals(new double[] { 0.25, 0.5 }, rk78.getC(), 1.0e-12);
        assertArrayEquals(new double[][] { { 0.2369, 0.6789 },
                                          { 0.3466, 0.4934 },
                                          { 0.7721, 0.5555 },
                                          { 0.8335, 0.0 },
                                          { 1.0, 1.0 },
                                          { 0.3466, 0.0 },
                                          { 0.6789, 0.0 },
                                          { 0.8335, 0.0 } }, rk78.getA(), 1.0e-12);
        assertEquals(new double[] { 0.3466, 0.2369, 0.6789, 0.8335 }, rk78.getB(), 1.0e-12);
        assertEquals(new double[] { 1.0 / 7.0, 1.0 / 16.0, 1.0 / 12.0, 1.0 / 24.0, 1.0 / 6.0, 1.0 / 24.0, 0.0, 0.0, 0.0 }, rk78.getExpCoefs(), 1.0e-12);

        rk78.setSafety(0.8);
        double minStep = rk78.getMinStep();
        assertTrue(minStep > 0.0);

        rk78.resetInternalState();
        stepSize = rk78.getStepSize();
        assertTrue(stepSize > 1.0e-12);

        rk78 = null;

    }

}