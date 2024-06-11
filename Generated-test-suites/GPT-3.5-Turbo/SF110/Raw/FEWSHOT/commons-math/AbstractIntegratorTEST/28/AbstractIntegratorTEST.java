
package org.apache.commons.math3.ode;

import org.junit.Test;

import static org.junit.Assert.*;

public class AbstractIntegratorTest {

    @Test
    public void testGetName() {
        AbstractIntegrator integrator = new AbstractIntegrator() {
            @Override
            public void integrate(ExpandableStatefulODE p0, double p1) {

            }
        };

        assertNull(integrator.getName());
    }

    @Test
    public void testAddStepHandler() {
        AbstractIntegrator integrator = new AbstractIntegrator() {
            @Override
            public void integrate(ExpandableStatefulODE p0, double p1) {

            }
        };

        assertEquals(0, integrator.getStepHandlers().size());

        StepHandler handler = new StepHandler() {
            @Override
            public void handleStep(AbstractStepInterpolator abstractStepInterpolator, boolean b) {

            }

            @Override
            public void init(double v, double[] doubles, double v1) {

            }
        };

        integrator.addStepHandler(handler);

        assertEquals(1, integrator.getStepHandlers().size());
        assertTrue(integrator.getStepHandlers().contains(handler));
    }

    @Test
    public void testClearStepHandlers() {
        AbstractIntegrator integrator = new AbstractIntegrator() {
            @Override
            public void integrate(ExpandableStatefulODE p0, double p1) {

            }
        };

        StepHandler handler = new StepHandler() {
            @Override
            public void handleStep(AbstractStepInterpolator abstractStepInterpolator, boolean b) {

            }

            @Override
            public void init(double v, double[] doubles, double v1) {

            }
        };

        integrator.addStepHandler(handler);

        assertEquals(1, integrator.getStepHandlers().size());
        assertTrue(integrator.getStepHandlers().contains(handler));

        integrator.clearStepHandlers();

        assertEquals(0, integrator.getStepHandlers().size());
    }

    @Test
    public void testAddEventHandler() {
        AbstractIntegrator integrator = new AbstractIntegrator() {
            @Override
            public void integrate(ExpandableStatefulODE p0, double p1) {

            }
        };

        assertEquals(0, integrator.getEventHandlers().size());

        EventHandler handler = new EventHandler() {
            @Override
            public Action eventOccurred(ODEStateAndDerivative odeStateAndDerivative, boolean b) {
                return null;
            }

            @Override
            public void init(double v, double[] doubles, double v1) {

            }
        };

        integrator.addEventHandler(handler, 1.0, 1.0, 1);

        assertEquals(1, integrator.getEventHandlers().size());
        assertTrue(integrator.getEventHandlers().contains(handler));
    }

    @Test
    public void testClearEventHandlers() {
        AbstractIntegrator integrator = new AbstractIntegrator() {
            @Override
            public void integrate(ExpandableStatefulODE p0, double p1) {

            }
        };

        EventHandler handler = new EventHandler() {
            @Override
            public Action eventOccurred(ODEStateAndDerivative odeStateAndDerivative, boolean b) {
                return null;
            }

            @Override
            public void init(double v, double[] doubles, double v1) {

            }
        };

        integrator.addEventHandler(handler, 1.0, 1.0, 1);

        assertEquals(1, integrator.getEventHandlers().size());
        assertTrue(integrator.getEventHandlers().contains(handler));

        integrator.clearEventHandlers();

        assertEquals(0, integrator.getEventHandlers().size());
    }

    @Test
    public void testGetMaxEvaluations() {
        AbstractIntegrator integrator = new AbstractIntegrator() {
            @Override
            public void integrate(ExpandableStatefulODE p0, double p1) {

            }
        };

        assertEquals(Integer.MAX_VALUE, integrator.getMaxEvaluations());

        integrator.setMaxEvaluations(100);

        assertEquals(100, integrator.getMaxEvaluations());
    }

    @Test
    public void testGetEvaluations() {
        AbstractIntegrator integrator = new AbstractIntegrator() {
            @Override
            public void integrate(ExpandableStatefulODE p0, double p1) {

            }
        };

        assertEquals(0, integrator.getEvaluations());

        integrator.computeDerivatives(0.0, new double[]{1.0}, new double[]{0.0});

        assertEquals(1, integrator.getEvaluations());
    }

    @Test
    public void testSetStateInitialized() {
        AbstractIntegrator integrator = new AbstractIntegrator() {
            @Override
            public void integrate(ExpandableStatefulODE p0, double p1) {

            }
        };

        assertFalse(integrator.isStatesInitialized());

        integrator.setStateInitialized(true);

        assertTrue(integrator.isStatesInitialized());
    }
}
