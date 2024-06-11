package org.apache.commons.math.ode.test;

import org.apache.commons.math3.analysis.FunctionUtilities;
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.ExpandableStatefulODE;
import org.apache.commons.math3.ode.events.EventHandler;
import org.apache.commons.math3.ode.events.GenericEvent;
import org.apache.commons.math3.ode.interpolators.StepInterpolator;
import org.apache.commons.math3.ode.sampling.StepHandler;
import org.junit.Assert;
import org.junit.Test;

import org.apache.commons.math.ode.AbstractIntegrator;
import org.apache.commons.math.ode.events.EventState;

/**
 * Test cases for AbstractIntegrator class
 */
public class AbstractIntegratorTest {

    @Test
    public void testBasicFunctionality() {
        AbstractIntegrator integrator = new AbstractIntegrator("test") {
            @Override
            protected void integrate(ExpandableStatefulODE equations, double t)
                    throws MathIllegalStateException, MathIllegalArgumentException {
                equations.integrate(t);
            }
        };

        // Test addStepHandler
        integrator.addStepHandler(new StepHandler() {
            @Override
            public void handleStep(StepHandler stepHandler, boolean isLastStep) {
                Assert.assertTrue(isLastStep);
            }
        });

        // Test integrate method
        FirstOrderDifferentialEquations equations = new DummyDifferentialEquations();
        double[] y0 = new double[]{0.0, 0.0};
        double t = 1.0;
        double[] y = new double[]{1.0, 1.0};

        integrator.integrate(equations, y0[0], y0, t, y);
        Assert.assertArrayEquals(y, new double[]{1.0, 1.0});
        Assert.assertEquals(t, integrator.getCurrentStepStart(), 0.0);

        // Test clearStepHandlers
        integrator.clearStepHandlers();
        integrator.integrate(equations, y0[0], y0, t, y);
        Assert.assertFalse(integrator.getStepHandlers().isEmpty());
    }

    @Test
    public void testEventHandler() {
        AbstractIntegrator integrator = new AbstractIntegrator("test") {
            @Override
            protected void integrate(ExpandableStatefulODE equations, double t)
                    throws MathIllegalStateException, MathIllegalArgumentException {
                equations.integrate(t);
            }
        };

        // Test addEventHandler
        EventHandler eventHandler = new EventHandler() {
            @Override
            public boolean evaluatesToLocalMinimum(double t, double y, double yPrime) {
                return false;
            }

            @Override
            public boolean evaluatesToLocalMaximum(double t, double y, double yPrime) {
                return y > 1.0;
            }

            @Override
            public void handleEvent(double t, double y) {
                Assert.assertTrue(y > 1.0);
            }
        };
        integrator.addEventHandler(eventHandler, 0.5, 1.1, 5);

        // Test integrate method with event
        ExpandableStatefulODE equations = new DummyDifferentialEquationsWithEvent();
        double[] y0 = new double[]{0.0, 0.0};
        double tStart = 0.0;
        double tEnd = 1.5;

        integrator.setEquations(equations);
        integrator.integrate(tStart, y0, tEnd);

        // Test that event was triggered
        Assert.assertEquals(1, integrator.getEventHandlers().size());
        EventState eventState = integrator.getEventHandlers().iterator().next();
        Assert.assertEquals(EventState.Type.LOCAL_MAXIMUM, eventState.getType());
        Assert.assertEquals(0.75, eventState.getEventTime(), 0.001);
        Assert.assertArrayEquals(new double[]{1.2, 1.2}, eventState.getEventState(), 0.001);
    }

    @Test(expected = MathIllegalStateException.class)
    public void testIntegrateWithNegativeStepSize() {
        AbstractIntegrator integrator = new AbstractIntegrator("test") {
            @Override
            protected void integrate(ExpandableStatefulODE equations, double t)
                    throws MathIllegalStateException, MathIllegalArgumentException {
                equations.integrate(t);
            }
        };

        // Test integrate method with negative step size
        ExpandableStatefulODE equations = new DummyDifferentialEquations();
        double[] y0 = new double[]{0.0, 0.0};
        double t = 1.0;
        double[] y = new double[]{1.0, 1.0};

        integrator.integrate(equations, y0[0], y0, t, y, -0.1);
    }

    @Test(expected = MathIllegalStateException.class)
    public void testIntegrateWithMaxEvaluationsReached() {
        AbstractIntegrator integrator = new AbstractIntegrator("test") {
            @Override
            protected void integrate(ExpandableStatefulODE equations, double t)
                    throws MathIllegalStateException, MathIllegalArgumentException {
                equations.integrate(t);
            }
        };

        // Test integrate method with max evaluations reached
        ExpandableStatefulODE equations = new DummyDifferentialEquations();
        double[] y0 = new double[]{0.0, 0.0};
        double t = 1.0;
        double[] y = new double[]{1.0, 1.0};

        integrator.setMaxEvaluations(1);
        integrator.integrate(equations, y0[0], y0, t, y);
    }

    @Test
    public void testResetEvaluations() {
        AbstractIntegrator integrator = new AbstractIntegrator("test") {
            @Override
            protected void integrate(ExpandableStatefulODE equations, double t)
                    throws MathIllegalStateException, MathIllegalArgumentException {
                equations.integrate(t);
            }
        };

        // Test resetEvaluations method
        integrator.integrate(new DummyDifferentialEquations(), 0.0, new double[]{0.0, 0.0}, 1.0);
        integrator.resetEvaluations();
        Assert.assertEquals(0, integrator.getEvaluations());
    }

    private static class DummyDifferentialEquations implements FirstOrderDifferentialEquations {

        @Override
        public int getDimension() {
            return 2;
        }

        @Override
        public void computeDerivatives(double t, double[] y, double[] yDot) {
            yDot[0] = y[1];
            yDot[1] = y[0];
        }

        @Override
        public void integrate(double t) {
            // empty method
        }

        @Override
        public void setTime(double t) {
            // empty method
        }

        @Override
        public void setPrimaryState(double[] y) {
            // empty method
        }

    }

    private static class DummyDifferentialEquationsWithEvent implements ExpandableStatefulODE {

        @Override
        public int getDimension() {
            return 2;
        }

        @Override
        public void computeDerivatives(double t, double[] y, double[] yDot) {
            yDot[0] = y[1];
            yDot[1] = y[0];
        }

        @Override
        public void integrate(double t) {
            // empty method
        }

        @Override
        public void setTime(double t) {
            this.t = t;
        }

        @Override
        public void setPrimaryState(double[] y) {
            this.y = y;
        }

        private double t;
        private double[] y;

        @Override
        public double getTime() {
            return t;
        }

        @Override
        public double[] getPrimaryState() {
            return y;
        }

        @Override
        public double getPrimaryStateComponent(int index) {
            return y[index];
        }

        @Override
        public void setPrimaryStateComponent(int index, double value) {
            y[index] = value;
        }

        @Override
        public double[] getDerivative() {
            double[] derivative = new double[getDimension()];
            computeDerivatives(t, y, derivative);
            return derivative;
        }

        @Override
        public double getDerivativeComponent(int index) {
            return getDerivative()[index];
        }

        @Override
        public void setDerivativeComponent(int index, double value) {
            throw new UnsupportedOperationException();
        }

        @Override
        public double[] getState() {
            return new double[]{getTime(), FunctionUtilities.arrayMax(getPrimaryState())};
        }

        @Override
        public void setState(double[] state) {
            setTime(state[0]);
            setPrimaryState(FunctionUtilities.subtract(state, 1));
        }

        @Override
        public double[] getStateComponent(int index) {
            return new double[]{getTime(), getPrimaryStateComponent(0)};
        }

        @Override
        public void setStateComponent(int index, double value) {
            if (index == 0) {
                setTime(value);
            } else {
                setPrimaryStateComponent(0, value);
            }
        }

        @Override
        public double[] getStateDerivative() {
            return new double[]{1.0, getDerivativeComponent(0)};
        }

        @Override
        public void setStateDerivative(double[] stateDerivative) {
            setDerivativeComponent(0, stateDerivative[1]);
        }

        @Override
        public void setStepSize(double stepSize) {
            // empty method
        }

        @Override
        public double getStepSize() {
            return 0.1;
        }

        @Override
        public double getAbsoluteTolerance() {
            return 1e-9;
        }

        @Override
        public void setAbsoluteTolerance(double absoluteTolerance) {
            // empty method
        }

        @Override
        public double getRelativeTolerance() {
            return 1e-9;
        }

        @Override
        public void setRelativeTolerance(double relativeTolerance) {
            // empty method
        }

        @Override
        public int getMaxEvaluations() {
            return 1000;
        }

        @Override
        public void setMaxEvaluations(int maxEvaluations) {
            // empty method
        }

        @Override
        public double getCurrentTime() {
            return t;
        }

        @Override
        public double[] getCurrentState() {
            return y;
        }

        @Override
        public double[] getCurrentDerivative() {
            return new double[]{1.0, getDerivativeComponent(0)};
        }

        @Override
        public void setCurrentState(double[] state) {
            setPrimaryState(FunctionUtilities.subtract(state, 1));
            setTime(state[0]);
        }

        @Override
        public void setCurrentDerivative(double[] derivative) {
            setDerivativeComponent(0, derivative[1]);
        }

        @Override
        public void setStepInterpolator(StepInterpolator interpolator) {
            // empty method
        }

        @Override
        public void setStepHandler(StepHandler stepHandler) {
            // empty method
        }

        @Override
        public void setEventHandler(EventHandler eventHandler) {
            addEventHandler(eventHandler, 0.5, 1.1, 5);
        }

        @Override
        public Collection<EventHandler> getEventHandlers() {
            return Collections.unmodifiableCollection(eventsStates);
        }

        @Override
        public boolean hasEventHandler() {
            return !eventsStates.isEmpty();
        }

        @Override
        public void clearEventHandlers() {
            eventsStates.clear();
        }

        @Override
        public double getEventTime() {
            return 0.0;
        }

        @Override
        public double[] getEventState() {
            return new double[]{0.0, 0.0};
        }

        @Override
        public EventState getLastEventState() {
            return null;
        }

        @Override
        public void setEventState(double[] eventState) {
            // empty method
        }

        @Override
        public void setEventStateComponent(int index, double value) {
            // empty method
        }

        @Override
        public void setLastEventState(EventState eventState) {
            // empty method
        }

        @Override
        public void setLastEventStateComponent(int index, double value) {
            // empty method
        }

        @Override
        public String getName() {
            return "DummyDifferentialEquationsWithEvent";
        }

        @Override
        public void reset() {
            // empty method
        }

        private List<EventState> eventsStates = new ArrayList<>();

    }
}