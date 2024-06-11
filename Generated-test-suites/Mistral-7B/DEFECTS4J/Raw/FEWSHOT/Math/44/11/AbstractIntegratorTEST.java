package org.apache.commons.math.ode;

import static org.junit.Assert.*;

import org.apache.commons.math.ode.ExpandableStatefulODE;
import org.apache.commons.math.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math.ode.StepHandler;
import org.apache.commons.math.ode.events.EventHandler;
import org.apache.commons.math.ode.events.EventState;
import org.apache.commons.math.ode.sampling.AbstractStepInterpolator;
import org.apache.commons.math.ode.sampling.StepHandlerAdapter;
import org.junit.Test;

public class AbstractIntegratorTest {

    @Test
    public void testAddStepHandler() {
        AbstractIntegrator integrator = new AbstractIntegrator() {
            @Override
            public void integrate(ExpandableStatefulODE equations, double t) throws MathIllegalStateException, MathIllegalArgumentException {
                // not implemented
            }
        };

        StepHandler handler = new StepHandlerAdapter();
        integrator.addStepHandler(handler);
        assertEquals(1, integrator.getStepHandlers().size());
        integrator.clearStepHandlers();
        assertEquals(0, integrator.getStepHandlers().size());
    }

    @Test
    public void testAddEventHandler() {
        AbstractIntegrator integrator = new AbstractIntegrator() {
            @Override
            public void integrate(ExpandableStatefulODE equations, double t) throws MathIllegalStateException, MathIllegalArgumentException {
                // not implemented
            }
        };

        EventHandler handler = (equation, stepSize) -> {};
        integrator.addEventHandler(handler, 1.0, 1.0e-6, 10);
        assertEquals(1, integrator.getEventHandlers().size());
        integrator.clearEventHandlers();
        assertEquals(0, integrator.getEventHandlers().size());
    }

    @Test
    public void testIntegrate() throws Exception {
        AbstractIntegrator integrator = new AbstractIntegrator() {
            @Override
            public void integrate(ExpandableStatefulODE equations, double t) throws MathIllegalStateException, MathIllegalArgumentException {
                // not implemented
            }
        };

        ExpandableStatefulODE ode = new ExpandableStatefulODE(new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yDot) {
                yDot[0] = y[0] * 0.1;
            }

            @Override
            public void setTime(double time) {
                // not implemented
            }

            @Override
            public void setPrimaryState(double[] state) {
                // not implemented
            }
        });
        integrator.setEquations(ode);

        double t0 = 0.0;
        double[] y0 = {1.0};
        double t1 = 1.0;
        double[] y1 = {2.0};

        integrator.integrate(ode, t1);
        assertArrayEquals(y1, y0);
        assertEquals(t1, ode.getTime());

        // test with step handler
        StepHandler handler = new StepHandlerAdapter();
        integrator.addStepHandler(handler);
        integrator.integrate(ode, t1);
        handler.assertHandledStep(AbstractIntegrator.StepEvent.END);

        // test with event handler
        integrator.addEventHandler((equation, stepSize) -> {
            assertEquals(t1, equation.getTime());
            assertArrayEquals(y1, equation.getPrimaryState());
        }, 1.0, 1.0e-6, 10);
        integrator.integrate(ode, t1);

    }

    @Test(expected = MathIllegalStateException.class)
    public void testIntegrateWithInvalidInitialValues() throws Exception {
        AbstractIntegrator integrator = new AbstractIntegrator() {
            @Override
            public void integrate(ExpandableStatefulODE equations, double t) throws MathIllegalStateException, MathIllegalArgumentException {
                // not implemented
            }
        };

        ExpandableStatefulODE ode = new ExpandableStatefulODE(new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yDot) {
                yDot[0] = y[0] * 0.1;
            }

            @Override
            public void setTime(double time) {
                // not implemented
            }

            @Override
            public void setPrimaryState(double[] state) {
                // not implemented
            }
        });
        integrator.setEquations(ode);

        double t0 = 0.0;
        double[] y0 = {1.0, 2.0}; // invalid initial values

        integrator.integrate(ode, 1.0);

    }

    @Test(expected = MathIllegalArgumentException.class)
    public void testIntegrateWithInvalidTargetValues() throws Exception {
        AbstractIntegrator integrator = new AbstractIntegrator() {
            @Override
            public void integrate(ExpandableStatefulODE equations, double t) throws MathIllegalStateException, MathIllegalArgumentException {
                // not implemented
            }
        };

        ExpandableStatefulODE ode = new ExpandableStatefulODE(new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yDot) {
                yDot[0] = y[0] * 0.1;
            }

            @Override
            public void setTime(double time) {
                // not implemented
            }

            @Override
            public void setPrimaryState(double[] state) {
                // not implemented
            }
        });
        integrator.setEquations(ode);

        double t0 = 0.0;
        double[] y0 = {1.0};
        double t1 = 2.0; // invalid target value

        integrator.integrate(ode, t1);

    }

    @Test
    public void testAcceptStep() throws Exception {
        AbstractIntegrator integrator = new AbstractIntegrator() {
            @Override
            public void integrate(ExpandableStatefulODE equations, double t) throws MathIllegalStateException, MathIllegalArgumentException {
                // not implemented
            }

            @Override
            protected double acceptStep(AbstractStepInterpolator interpolator, double[] y, double[] yDot, double tEnd)
                    throws MathIllegalStateException {
                return tEnd;
            }
        };

        ExpandableStatefulODE ode = new ExpandableStatefulODE(new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yDot) {
                yDot[0] = y[0] * 0.1;
            }

            @Override
            public void setTime(double time) {
                // not implemented
            }

            @Override
            public void setPrimaryState(double[] state) {
                // not implemented
            }
        });
        integrator.setEquations(ode);

        double t0 = 0.0;
        double[] y0 = {1.0};
        double t1 = 1.0;
        double[] y1 = {2.0};

        AbstractStepInterpolator interpolator = new AbstractStepInterpolator() {
            @Override
            public double getGlobalPreviousTime() {
                return t0;
            }

            @Override
            public double getGlobalCurrentTime() {
                return t0;
            }
        };

        integrator.integrate(ode, t1);
        assertEquals(t1, interpolator.getInterpolatedTime());
        assertEquals(y1, interpolator.getInterpolatedState());

        interpolator.setGlobalCurrentTime(t0);
        interpolator.setSoftPreviousTime(t0);
        interpolator.setSoftCurrentTime(t1);

        integrator.acceptStep(interpolator, y1, null, t1);
        assertTrue(integrator.isLastStep);
    }

}