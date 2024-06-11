import static org.apache.commons.math3.ode.AbstractIntegrator.AbstractStepInterpolator.Kind.DOP853;
import static org.apache.commons.math3.ode.AbstractIntegrator.AbstractStepInterpolator.Kind.RK4;
import static org.apache.commons.math3.ode.AbstractIntegrator.StepHandler.Type.ANALYTIC;
import static org.apache.commons.math3.ode.AbstractIntegrator.StepHandler.Type.EVENT;
import static org.apache.commons.math3.ode.AbstractIntegrator.StepHandler.Type.VALUE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.math3.analysis.PolynomialFunctions;
import org.apache.commons.math3.analysis.SplineInterpolator;
import org.apache.commons.math3.analysis.function.ParametricFunction;
import org.apache.commons.math3.ode.AbstractIntegrator;
import org.apache.commons.math3.ode.ExpandableStatefulODE;
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.events.Event;
import org.apache.commons.math3.ode.events.EventRateFunction;
import org.apache.commons.math3.ode.events.NullEvent;
import org.apache.commons.math3.ode.events.StateChangedEvent;
import org.apache.commons.math3.ode.events.StateTransitionEvent;
import org.apache.commons.math3.ode.events.TriggerEvent;
import org.apache.commons.math3.ode.sampling.StepHandler;
import org.apache.commons.math3.ode.stephandlers.StepHandlerImpl;
import org.apache.commons.math3.ode.stepinterpolators.AbstractStepInterpolator;
import org.apache.commons.math3.ode.stepper.ClassicalRungeKuttaStepper;
import org.apache.commons.math3.ode.stepper.Stepper;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;

/**
 * Test class for AbstractIntegrator.
 */
public class AbstractIntegratorTest {

    /**
     * Test method for {@link AbstractIntegrator#AbstractIntegrator(String)}.
     */
    @Test
    public void testConstructor() {
        final AbstractIntegrator integrator = new AbstractIntegrator("testIntegrator");
        assertNotNull(integrator);
        assertEquals("testIntegrator", integrator.getName());
        assertEquals(0, integrator.getMaxEvaluations());
        assertEquals(0, integrator.getEvaluations());
    }

    /**
     * Test method for {@link AbstractIntegrator#addStepHandler(StepHandler)}.
     */
    @Test
    public void testAddStepHandler() {
        final AbstractIntegrator integrator = new AbstractIntegrator("testIntegrator");
        final StepHandler stepHandler = new StepHandlerImpl(VALUE, 1.0, 2.0);
        integrator.addStepHandler(stepHandler);
        assertEquals(1, integrator.getStepHandlers().size());
        assertTrue(integrator.getStepHandlers().contains(stepHandler));
    }

    /**
     * Test method for {@link AbstractIntegrator#clearStepHandlers()}.
     */
    @Test
    public void testClearStepHandlers() {
        final AbstractIntegrator integrator = new AbstractIntegrator("testIntegrator");
        final StepHandler stepHandler1 = new StepHandlerImpl(VALUE, 1.0, 2.0);
        final StepHandler stepHandler2 = new StepHandlerImpl(VALUE, 2.0, 3.0);
        integrator.addStepHandler(stepHandler1);
        integrator.addStepHandler(stepHandler2);
        assertEquals(2, integrator.getStepHandlers().size());
        integrator.clearStepHandlers();
        assertEquals(0, integrator.getStepHandlers().size());
    }

    /**
     * Test method for {@link AbstractIntegrator#initIntegration(double, double[], double)}.
     */
    @Test
    public void testInitIntegration() {
        final AbstractIntegrator integrator = new AbstractIntegrator("testIntegrator");
        final double t0 = 0.0;
        final double[] y0 = { 1.0, 2.0 };
        final double t = 1.0;

        integrator.initIntegration(t0, y0, t);
        assertTrue(integrator.getStateInitialized());
        assertEquals(t0, integrator.getCurrentStepStart());
        assertEquals(Precision.safeEqual(t, integrator.getNextStepStart(), 1e-12), integrator.getCurrentSignedStepsize() < 0);
        assertEquals(0, integrator.getEvaluations());
    }

    /**
     * Test method for {@link AbstractIntegrator#setEquations(ExpandableStatefulODE)}.
     */
    @Test
    public void testSetEquations() {
        final AbstractIntegrator integrator = new AbstractIntegrator("testIntegrator");
        final ExpandableStatefulODE equations = new ExpandableStatefulODE() {

            @Override
            public int getDimension() {
                return 2;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yDot) {
                yDot[0] = y[1];
                yDot[1] = 2.0 * y[1];
            }

        };
        integrator.setEquations(equations);
        assertEquals(equations, integrator.getEquations());
    }

    /**
     * Test method for {@link AbstractIntegrator#integrate(ExpandableStatefulODE, double)}.
     */
    @Test
    public void testIntegrate() throws Exception {
        final AbstractIntegrator integrator = new AbstractIntegrator("testIntegrator");

        // Test with empty equations
        integrator.integrate(new ExpandableStatefulODE() {}, 1.0);
        assertEquals(Double.NaN, integrator.getCurrentStepStart(), 0);
        assertEquals(Double.NaN, integrator.getCurrentSignedStepsize(), 0);

        // Test with simple equations
        final ExpandableStatefulODE equations = new ExpandableStatefulODE() {

            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yDot) {
                yDot[0] = y[0] * 0.1;
            }

            @Override
            public void setTime(double t) {
                this.time = t;
            }

            @Override
            public void setPrimaryState(double[] y) {
                this.state = y;
            }

            @Override
            public double[] getPrimaryState() {
                return this.state;
            }

            @Override
            public double getTime() {
                return this.time;
            }

            private double[] state;
            private double time;

        };

        integrator.setEquations(equations);
        integrator.initIntegration(0.0, new double[] { 1.0 }, 1.0);
        final double result = integrator.integrate(equations, 1.0);
        assertEquals(1.0, result, 1e-12);
        assertEquals(1.0, integrator.getCurrentStepStart(), 1e-12);
        assertEquals(1.0, integrator.getCurrentSignedStepsize(), 1e-12);
        assertEquals(1, integrator.getEvaluations());

        // Test with step handlers
        integrator.clearStepHandlers();
        integrator.addStepHandler(new StepHandlerImpl(EVENT, 0.5, new NullEvent()));
        integrator.addStepHandler(new StepHandlerImpl(ANALYTIC, 0.75, 0.5));

        integrator.initIntegration(0.0, new double[] { 1.0 }, 1.5);
        integrator.integrate(equations, 1.5);
        assertEquals(1.5, integrator.getCurrentStepStart(), 1e-12);
        assertEquals(0.5, integrator.getCurrentSignedStepsize(), 1e-12);
        assertEquals(2, integrator.getEvaluations());

        // Test with events
        integrator.clearStepHandlers();
        integrator.addStepHandler(new StepHandlerImpl(VALUE, 0.5, 0.5));
        integrator.addEventHandler(new TriggerEvent(0.5, 1.0, new ParametricFunction(t -> t * t, new double[] { 0.5 })), new StepHandlerImpl(VALUE, 1.0, 2.0));

        integrator.initIntegration(0.0, new double[] { 1.0 }, 1.5);
        integrator.integrate(equations, 1.5);
        assertEquals(1.5, integrator.getCurrentStepStart(), 1e-12);
        assertEquals(1.0, integrator.getCurrentSignedStepsize(), 1e-12);
        assertEquals(3, integrator.getEvaluations());

        // Test with state changed event
        integrator.clearStepHandlers();
        integrator.addStepHandler(new StepHandlerImpl(VALUE, 0.5, 0.5));
        integrator.addEventHandler(new StateChangedEvent(0.5, 1.0, new double[] { 1.0, 2.0 }, new double[] { 2.0, 3.0 }), new StepHandlerImpl(VALUE, 1.0, 3.0));

        integrator.initIntegration(0.0, new double[] { 1.0, 2.0 }, 1.5);
        integrator.integrate(equations, 1.5);
        assertEquals(1.5, integrator.getCurrentStepStart(), 1e-12);
        assertEquals(1.0, integrator.getCurrentSignedStepsize(), 1e-12);
        assertEquals(3, integrator.getEvaluations());
        assertArrayEquals(new double[] { 2.0, 3.0 }, integrator.getState(), 1e-12);

        // Test with state transition event
        integrator.clearStepHandlers();
        integrator.addStepHandler(new StepHandlerImpl(VALUE, 0.5, 0.5));
        integrator.addEventHandler(new StateTransitionEvent(0.5, 1.0, new double[] { 1.0, 2.0 }, new double[] { 3.0, 4.0 }, null, null), new StepHandlerImpl(VALUE, 1.0, 4.0));

        integrator.initIntegration(0.0, new double[] { 1.0, 2.0 }, 1.5);
        integrator.integrate(equations, 1.5);
        assertEquals(1.5, integrator.getCurrentStepStart(), 1e-12);
        assertEquals(1.0, integrator.getCurrentSignedStepsize(), 1e-12);
        assertEquals(3, integrator.getEvaluations());
        assertArrayEquals(new double[] { 3.0, 4.0 }, integrator.getState(), 1e-12);

        // Test with event rate function
        integrator.clearStepHandlers();
        integrator.addStepHandler(new StepHandlerImpl(VALUE, 0.5, 0.5));
        integrator.addEventHandler(new EventRateFunction() {

            @Override
            public double evaluateRate(double t, double[] y) {
                return y[0] >= 1.5 ? 0.5 : 1.0;
            }

        }, new StepHandlerImpl(VALUE, 1.0, 2.0));

        integrator.initIntegration(0.0, new double[] { 0.5 }, 2.0);
        integrator.integrate(equations, 2.0);
        assertEquals(2.0, integrator.getCurrentStepStart(), 1e-12);
        assertEquals(1.0, integrator.getCurrentSignedStepsize(), 1e-12);
        assertEquals(4, integrator.getEvaluations());
        assertEquals(1, (int) integrator.getEventStates().size());

        final EventState eventState = integrator.getEventStates().iterator().next();
        assertEquals(1.5, eventState.getEventTime(), 1e-12);

        // Test with stepper
        integrator.clearStepHandlers();
        integrator.addStepHandler(new StepHandlerImpl(VALUE, 0.5, 1.0));
        integrator.setStepper(new ClassicalRungeKuttaStepper(RK4));

        integrator.initIntegration(0.0, new double[] { 0.5 }, 2.0);
        integrator.integrate(equations, 2.0);
        assertEquals(2.0, integrator.getCurrentStepStart(), 1e-12);
        assertEquals(1.0, integrator.getCurrentSignedStepsize(), 1e-12);
        assertEquals(8, integrator.getEvaluations());

        // Test with adaptive step size integrator
        integrator.clearStepHandlers();
        integrator.addStepHandler(new StepHandlerImpl(VALUE, 0.5, 0.5));
        integrator.setStepper(new AbstractIntegrator.AbstractStepInterpolator(DOP853) {

            @Override
            public int getOrder() {
                return 8;
            }

            @Override
            public int getStepCount() {
                return 5;
            }

            @Override
            public AbstractStepInterpolator clone() {
                return this;
            }

        });

        integrator.initIntegration(0.0, new double[] { 0.5 }, 2.0);
        integrator.integrate(equations, 2.0);
        assertEquals(2.0, integrator.getCurrentStepStart(), 1e-12);
        assertEquals(1.0, integrator.getCurrentSignedStepsize(), 1e-12);
        assertEquals(24, integrator.getEvaluations());

    }

}