package org.apache.commons.math.ode.nonstiff;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.events.MockEvent;
import org.apache.commons.math3.ode.events.MockEventsManager;
import org.apache.commons.math3.ode.sampling.DummyStepInterpolator;
import org.apache.commons.math3.ode.stephandlers.MockStepHandler;

import java.util.ArrayList;
import java.util.List;

public class EmbeddedRungeKuttaIntegratorTest {

    @Test
    public void testBasicIntegration() throws Exception {
        double[][] coefficients = {{0.0, 0.5, 0.5, 1.0},
                                   {0.0, 0.3, 0.6, 1.0},
                                   {0.0, 0.4, 0.4, 1.0}};
        double[] absoluteTolerance = {1.0E-12, 1.0E-12};
        double[] relativeTolerance = {1.0E-6, 1.0E-6};
        double minStep = 0.001;
        double maxStep = 0.01;

        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator(
                "default", false,
                coefficients[0], coefficients[1], coefficients[2],
                new RungeKuttaStepInterpolator(minStep),
                minStep, maxStep,
                absoluteTolerance, relativeTolerance) {
            @Override
            protected double estimateError(double[][] yDotK,
                                           double[] y0, double[] y1,
                                           double h) {
                double error = 0.0;
                for (int i = 0; i < y1.length; i++) {
                    error += Math.pow(Math.abs(y1[i] - y0[i]), 2);
                }
                error = Math.sqrt(error);
                return error;
            }
        };

        FirstOrderDifferentialEquations equations = new MockDifferentialEquations();
        double[] initialValues = {1.0, 2.0};

        MockEventsManager eventsManager = new MockEventsManager();
        List<MockEvent> events = new ArrayList<>();
        events.add(new MockEvent(1.5, 1.0));
        eventsManager.addEvent(new MockStepHandler() {
            @Override
            public void handleStep(StepHandlerContext context) {
                assertTrue(context.isLastStep());
                assertEquals(1.5, context.getLocalTime(), 0.0);
            }
        }, events);

        integrator.setEventsHandlersManager(eventsManager);

        double[] result = integrator.integrate(equations, 0.0, initialValues, 2.0, new double[initialValues.length]);

        assertEquals(2.0, result[0], 0.0);
        assertEquals(3.0, result[1], 0.0);
    }

    @Test
    public void testStepRejection() throws Exception {
        double[][] coefficients = {{0.0, 0.5, 0.5, 1.0},
                                   {0.0, 0.3, 0.6, 1.0},
                                   {0.0, 0.4, 0.4, 1.0}};
        double[] absoluteTolerance = {1.0E-12, 1.0E-12};
        double[] relativeTolerance = {1.0E-6, 1.0E-6};
        double minStep = 0.001;
        double maxStep = 0.01;

        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator(
                "default", false,
                coefficients[0], coefficients[1], coefficients[2],
                new RungeKuttaStepInterpolator(minStep),
                minStep, maxStep,
                absoluteTolerance, relativeTolerance) {
            @Override
            protected double estimateError(double[][] yDotK,
                                           double[] y0, double[] y1,
                                           double h) {
                double error = 0.0;
                for (int i = 0; i < y1.length; i++) {
                    error += Math.pow(Math.abs(y1[i] - y0[i]), 2);
                }
                error = Math.sqrt(error);
                return error;
            }
        };

        FirstOrderDifferentialEquations equations = new MockDifferentialEquations();
        double[] initialValues = {1.0, 2.0};

        MockEventsManager eventsManager = new MockEventsManager();
        integrator.setEventsHandlersManager(eventsManager);

        integrator.setSafety(0.5);
        integrator.setMaxGrowth(2.0);

        double[] result = integrator.integrate(equations, 0.0, initialValues, 1.5, new double[initialValues.length]);

        assertNull(result);

        assertEquals(1, eventsManager.size());
        MockEvent event = eventsManager.getEvent(0);
        assertEquals(1.5, event.getLocalTime(), 0.0);
    }

    @Test
    public void testStepAcceptance() throws Exception {
        double[][] coefficients = {{0.0, 0.5, 0.5, 1.0},
                                   {0.0, 0.3, 0.6, 1.0},
                                   {0.0, 0.4, 0.4, 1.0}};
        double[] absoluteTolerance = {1.0E-12, 1.0E-12};
        double[] relativeTolerance = {1.0E-6, 1.0E-6};
        double minStep = 0.001;
        double maxStep = 0.01;

        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator(
                "default", false,
                coefficients[0], coefficients[1], coefficients[2],
                new RungeKuttaStepInterpolator(minStep),
                minStep, maxStep,
                absoluteTolerance, relativeTolerance) {
            @Override
            protected double estimateError(double[][] yDotK,
                                           double[] y0, double[] y1,
                                           double h) {
                double error = 0.0;
                for (int i = 0; i < y1.length; i++) {
                    error += Math.pow(Math.abs(y1[i] - y0[i]), 2);
                }
                error = Math.sqrt(error);
                return error;
            }
        };

        FirstOrderDifferentialEquations equations = new MockDifferentialEquations();
        double[] initialValues = {1.0, 2.0};

        MockEventsManager eventsManager = new MockEventsManager();
        integrator.setEventsHandlersManager(eventsManager);

        integrator.setSafety(0.9);
        integrator.setMinReduction(0.2);
        integrator.setMaxGrowth(10.0);

        double[] result = integrator.integrate(equations, 0.0, initialValues, 2.0, new double[initialValues.length]);

        assertEquals(2.0, result[0], 0.0);
        assertEquals(3.0, result[1], 0.0);

        assertEquals(1, eventsManager.size());
        MockEvent event = eventsManager.getEvent(0);
        assertFalse(event.isAccepted());
    }

    @Test
    public void testStepHandlers() throws Exception {
        double[][] coefficients = {{0.0, 0.5, 0.5, 1.0},
                                   {0.0, 0.3, 0.6, 1.0},
                                   {0.0, 0.4, 0.4, 1.0}};
        double[] absoluteTolerance = {1.0E-12, 1.0E-12};
        double[] relativeTolerance = {1.0E-6, 1.0E-6};
        double minStep = 0.001;
        double maxStep = 0.01;

        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator(
                "default", false,
                coefficients[0], coefficients[1], coefficients[2],
                new RungeKuttaStepInterpolator(minStep),
                minStep, maxStep,
                absoluteTolerance, relativeTolerance) {
            @Override
            protected double estimateError(double[][] yDotK,
                                           double[] y0, double[] y1,
                                           double h) {
                double error = 0.0;
                for (int i = 0; i < y1.length; i++) {
                    error += Math.pow(Math.abs(y1[i] - y0[i]), 2);
                }
                error = Math.sqrt(error);
                return error;
            }
        };

        FirstOrderDifferentialEquations equations = new MockDifferentialEquations();
        double[] initialValues = {1.0, 2.0};

        MockStepHandler stepHandler = new MockStepHandler() {
            int stepCount = 0;

            @Override
            public void handleStep(StepHandlerContext context) {
                assertEquals(integrator, context.getIntegrator());
                assertEquals(initialValues, context.getOldValues());
                assertEquals(integrator.getStepSize(), context.getStepSize(), 0.0);
                assertEquals(1.0, context.getLocalTime(), 0.0);
                stepCount++;
            }
        };

        integrator.addStepHandler(stepHandler);

        MockEventsManager eventsManager = new MockEventsManager();
        integrator.setEventsHandlersManager(eventsManager);

        integrator.integrate(equations, 0.0, initialValues, 2.0, new double[initialValues.length]);

        assertEquals(1, stepHandler.getStepCount());
    }

    @Test
    public void testDenseOutput() throws Exception {
        double[][] coefficients = {{0.0, 0.5, 0.5, 1.0},
                                   {0.0, 0.3, 0.6, 1.0},
                                   {0.0, 0.4, 0.4, 1.0}};
        double[] absoluteTolerance = {1.0E-12, 1.0E-12};
        double[] relativeTolerance = {1.0E-6, 1.0E-6};
        double minStep = 0.001;
        double maxStep = 0.01;

        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator(
                "default", false,
                coefficients[0], coefficients[1], coefficients[2],
                new RungeKuttaStepInterpolator(minStep),
                minStep, maxStep,
                absoluteTolerance, relativeTolerance) {
            @Override
            protected double estimateError(double[][] yDotK,
                                           double[] y0, double[] y1,
                                           double h) {
                double error = 0.0;
                for (int i = 0; i < y1.length; i++) {
                    error += Math.pow(Math.abs(y1[i] - y0[i]), 2);
                }
                error = Math.sqrt(error);
                return error;
            }
        };

        FirstOrderDifferentialEquations equations = new MockDifferentialEquations();
        double[] initialValues = {1.0, 2.0};

        MockEventsManager eventsManager = new MockEventsManager();
        integrator.setEventsHandlersManager(eventsManager);

        integrator.setRequireDenseOutput(true);

        double[] result = integrator.integrate(equations, 0.0, initialValues, 2.0, new double[initialValues.length]);

        assertEquals(2.0, result[0], 0.0);
        assertEquals(3.0, result[1], 0.0);

        MockStepInterpolator interpolator = (MockStepInterpolator) integrator.getPrototype();
        assertEquals(interpolator.getOrder(), integrator.getOrder());
        assertTrue(interpolator.isDenseOutput());
    }

}

class MockDifferentialEquations implements FirstOrderDifferentialEquations {
    @Override
    public int getNumberOfEquations() {
        return 2;
    }

    @Override
    public void computeDerivatives(double t, double[] y, double[] yPrime) {
        yPrime[0] = y[1];
        yPrime[1] = -y[0];
    }
}

class MockStepInterpolator extends DummyStepInterpolator {
    boolean isDenseOutput = false;

    public MockStepInterpolator() {
        this(0.001);
    }

    public MockStepInterpolator(double minStep) {
        super(minStep);
    }

    @Override
    public boolean isDenseOutput() {
        return isDenseOutput;
    }

    @Override
    public void reinitialize(EmbeddedRungeKuttaIntegrator integrator, double[] yTmp, double[][] yDotK, boolean forward) {
        super.reinitialize(integrator, yTmp, forward);
        isDenseOutput = integrator.requiresDenseOutput();
    }
}

class MockEvent extends AbstractODEEvent {
    private double localTime;

    public MockEvent(double time, double value) {
        super(time, value);
        this.localTime = time;
    }

    public double getLocalTime() {
        return localTime;
    }
}

class MockStepHandler implements StepHandler {
    int stepCount = 0;

    @Override
    public void handleStep(StepHandlerContext context) {
        // implementation
    }

    public int getStepCount() {
        return stepCount;
    }
}

class MockEventsManager extends CombinedEventsManager {
    List<MockEvent> events = new ArrayList<>();

    @Override
    public void addEvent(StepHandler handler, AbstractODEEvent event) {
        super.addEvent(handler, event);
        events.add((MockEvent) event);
    }

    @Override
    public int size() {
        return events.size();
    }

    @Override
    public AbstractODEEvent getEvent(int index) {
        return events.get(index);
    }
}