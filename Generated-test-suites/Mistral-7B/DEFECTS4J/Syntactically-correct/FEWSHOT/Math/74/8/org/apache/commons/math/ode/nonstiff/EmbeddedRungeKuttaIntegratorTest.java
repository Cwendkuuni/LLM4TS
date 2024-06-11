package org.apache.commons.math.ode.nonstiff;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.events.DefaultValueProtector;
import org.apache.commons.math3.ode.events.EmptyEventsManager;
import org.apache.commons.math3.ode.events.Event;
import org.apache.commons.math3.ode.events.StepHandler;
import org.apache.commons.math3.ode.events. VallisWolfeEvent;
import org.apache.commons.math3.ode.sampling.RungeKuttaStepInterpolator;
import org.apache.commons.math3.ode.util.ODEStateAndDerivative;
import org.junit.Test;

public class EmbeddedRungeKuttaIntegratorTest {

    @Test
    public void testBasicFunctionality() {
        double[][] coefficients = {
            {0.0, 0.0},
            {0.3, 0.47360189841288048E-2},
            {0.6, 0.14885156184799734E-2},
            {0.87, 0.36744186546514243E-3}
        };
        double[] b = {0.3, 0.49688307365267562E-2, 0.32512337594637438E-2, 0.20535544440221125E-2};
        double minStep = 0.001;
        double maxStep = 10.0;
        double scalAbsoluteTolerance = 1E-12;
        double scalRelativeTolerance = 1E-12;
        boolean fsal = false;
        RungeKuttaStepInterpolator prototype = new RungeKuttaStepInterpolator(4);

        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("RK4", fsal, coefficients, b, prototype, minStep, maxStep, scalAbsoluteTolerance, scalRelativeTolerance) {
            @Override
            protected double estimateError(double[][] yDotK, double[] y0, double[] y1, double h) {
                double error = 0.0;
                for (int i = 0; i < y0.length; i++) {
                    error += Math.pow(Math.abs(yDotK[0][i] - yDotK[yDotK.length - 1][i]), 2);
                }
                return Math.sqrt(error);
            }
        };

        FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yDot) {
                yDot[0] = Math.sin(y[0]);
            }
        };

        double initialTime = 0.0;
        double[] initialState = {0.0};
        double targetTime = 2.0 * Math.PI;
        double[] targetState = {Math.sin(targetTime)};

        ODEStateAndDerivative state = new ODEStateAndDerivative(initialState, initialState);
        integrator.integrate(equations, initialTime, state.getY(), targetTime, state.getY());

        assertEquals(targetState[0], state.getY()[0], 1E-11);
    }

    @Test
    public void testFsal() {
        double[][] coefficients = {
            {0.0, 0.0},
            {0.3, 0.47360189841288048E-2},
            {0.6, 0.14885156184799734E-2},
            {0.87, 0.36744186546514243E-3},
            {0.87, 0.87}
        };
        double[] b = {0.3, 0.49688307365267562E-2, 0.32512337594637438E-2, 0.20535544440221125E-2, 0.20535544440221125E-2};
        double minStep = 0.001;
        double maxStep = 10.0;
        double scalAbsoluteTolerance = 1E-12;
        double scalRelativeTolerance = 1E-12;
        RungeKuttaStepInterpolator prototype = new RungeKuttaStepInterpolator(5);

        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("RKF45", true, coefficients, b, prototype, minStep, maxStep, scalAbsoluteTolerance, scalRelativeTolerance) {
            @Override
            protected double estimateError(double[][] yDotK, double[] y0, double[] y1, double h) {
                double error = 0.0;
                for (int i = 0; i < y0.length; i++) {
                    error += Math.pow(Math.abs(yDotK[0][i] - yDotK[yDotK.length - 1][i]), 2);
                }
                return Math.sqrt(error);
            }
        };

        FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yDot) {
                yDot[0] = Math.sin(y[0]);
            }
        };

        double initialTime = 0.0;
        double[] initialState = {0.0};
        double targetTime = 2.0 * Math.PI;
        double[] targetState = {Math.sin(targetTime)};

        ODEStateAndDerivative state = new ODEStateAndDerivative(initialState, initialState);
        integrator.integrate(equations, initialTime, state.getY(), targetTime, state.getY());

        assertEquals(targetState[0], state.getY()[0], 1E-11);

        double previousDerivative = state.getY()[0];
        integrator.integrate(equations, state.getY()[0], state.getY(), state.getY()[0] + 0.01, state.getY());
        double currentDerivative = state.getY()[0];

        assertTrue(Math.abs(previousDerivative - currentDerivative) < 1E-11);
    }

    @Test
    public void testEventHandling() {
        double[][] coefficients = {
            {0.0, 0.0},
            {0.3, 0.47360189841288048E-2},
            {0.6, 0.14885156184799734E-2},
            {0.87, 0.36744186546514243E-3}
        };
        double[] b = {0.3, 0.49688307365267562E-2, 0.32512337594637438E-2, 0.20535544440221125E-2};
        double minStep = 0.01;
        double maxStep = 10.0;
        double scalAbsoluteTolerance = 1E-12;
        double scalRelativeTolerance = 1E-12;
        RungeKuttaStepInterpolator prototype = new RungeKuttaStepInterpolator(4);

        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("RK4", false, coefficients, b, prototype, minStep, maxStep, scalAbsoluteTolerance, scalRelativeTolerance) {
            @Override
            protected double estimateError(double[][] yDotK, double[] y0, double[] y1, double h) {
                double error = 0.0;
                for (int i = 0; i < y0.length; i++) {
                    error += Math.pow(Math.abs(yDotK[0][i] - yDotK[yDotK.length - 1][i]), 2);
                }
                return Math.sqrt(error);
            }
        };

        EmptyEventsManager eventsManager = new EmptyEventsManager();
        DefaultValueProtector valueProtector = new DefaultValueProtector();
        integrator.setEventsManager(eventsManager);
        integrator.setEventHandler(new StepHandler() {
            @Override
            public void handleStep(StepHandlerContext context, int stepNumber) {
                if (context.getStepNumber() == 2) {
                    VallisWolfeEvent event = new VallisWolfeEvent(context.getInterval()[0] + context.getStepSize(), 0.0, 1.0);
                    context.addEvent(event);
                }
            }
        });

        FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yDot) {
                yDot[0] = Math.sin(y[0]);
            }
        };

        double initialTime = 0.0;
        double[] initialState = {0.0};
        double targetTime = 2.0 * Math.PI;
        double[] targetState = {Math.sin(targetTime)};

        ODEStateAndDerivative state = new ODEStateAndDerivative(initialState, initialState);
        integrator.integrate(equations, initialTime, state.getY(), targetTime, state.getY());

        Event event = eventsManager.getLastEvent();
        assertEquals(event.getClass(), VallisWolfeEvent.class);
        assertEquals(event.getTime(), 2.0, 1E-11);
        assertEquals(event.getValue(), 1.0, 1E-11);
    }
}