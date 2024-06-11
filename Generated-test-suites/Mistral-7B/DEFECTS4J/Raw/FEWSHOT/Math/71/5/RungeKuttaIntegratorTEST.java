package org.apache.commons.math.ode.nonstiff;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.events.DefaultValueEvent;
import org.apache.commons.math3.ode.events.Event;
import org.apache.commons.math3.ode.events.ForcedEvent;
import org.apache.commons.math3.ode.events.GoalType;
import org.apache.commons.math3.ode.events.RelativisticEvent;
import org.apache.commons.math3.ode.sampling.StepHandler;
import org.junit.Test;

public class RungeKuttaIntegratorTest {

    @Test
    public void testIntegrate() throws Exception {
        RungeKuttaIntegrator integrator = new EulerIntegrator(0.01, 1.0, new EulerStepInterpolator(), 0.01);
        double[] y0 = {0.0};
        double t0 = 0.0;
        double tEnd = 1.0;
        double[] yEnd = {1.0};

        FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yPrime) {
                yPrime[0] = y[0] * 0.1;
            }
        };

       CombinedEventsManager eventsManager = new CombinedEventsManager();
        StepHandler stepHandler = new StepHandler() {
            @Override
            public void handleStep(StepHandler.StepEvent event) {
                System.out.println("Step handled: " + event.getLocalTime());
            }
        };
        StepHandler stopHandler = new StepHandler() {
            @Override
            public void handleStep(StepHandler.StepEvent event) {
                if (event.getLocalTime() >= tEnd - 1e-12) {
                    event.setHandled(true);
                }
            }
        };
        eventsManager.addStepHandler(stopHandler);
        eventsManager.addStepHandler(stepHandler);

        integrator.integrate(equations, t0, y0, tEnd, yEnd);

        assertEquals(yEnd[0], yEnd[0], 1e-12);
        assertEquals(tEnd, integrator.getStepStart(), 1e-12);
        assertEquals(tEnd, integrator.getStep(), 1e-12);
        assertArrayEquals(yEnd, integrator.getStepEnd(), 1e-12);
        assertEquals(1, stepHandler.getStepCount());
        assertEquals(0, stopHandler.getStepCount());

        //Test for MaxCountExceededException
        integrator = new RungeKuttaIntegrator(1000, 0.01, new EulerStepInterpolator(), 0.01) {
            @Override
            public int getStepLimit() {
                return 10000;
            }
        };
        try {
            integrator.integrate(equations, t0, y0, tEnd, yEnd);
            fail("Should have thrown MaxCountExceededException");
        } catch (MaxCountExceededException e) {
            //Expected
        }

        //Test for RelativisticEvent
        FirstOrderDifferentialEquations relativisticEquations = new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yPrime) {
                yPrime[0] = y[0] * 0.1;
            }

            @Override
            public Event getEvent(double t, double[] y, double tEvent) {
                return new RelativisticEvent(tEvent, GoalType.REACH, 1.0);
            }

            @Override
            public double getEventTime(double t, double[] y) {
                return 1.0;
            }
        };

        integrator = new RungeKuttaIntegrator(0.01, 0.0, new EulerStepInterpolator(), 0.01) {
            @Override
            public void initializeStep(double t, double[] y, double step) {
                super.initializeStep(t, y, step);
                addEvent(new DefaultValueEvent(t, 0.0));
                addEvent(new ForcedEvent(t + 0.1, 1.0));
            }
        };
        integrator.setEventsManager(eventsManager);
        integrator.integrate(relativisticEquations, 0.0, y0, tEnd, yEnd);
        assertEquals(1, stepHandler.getStepCount());
        assertEquals(1, stopHandler.getStepCount());

    }

}