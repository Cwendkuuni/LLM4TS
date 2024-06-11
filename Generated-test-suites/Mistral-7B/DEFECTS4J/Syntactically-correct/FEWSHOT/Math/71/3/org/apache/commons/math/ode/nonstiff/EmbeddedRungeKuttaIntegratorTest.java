package org.apache.commons.math.ode.nonstiff;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.events.DefaultEventHandler;
import org.apache.commons.math3.ode.events.Event;
import org.apache.commons.math3.ode.events.EventRate;
import org.apache.commons.math3.ode.events.EventType;
import org.apache.commons.math3.ode.events.MultipleEventsManager;
import org.apache.commons.math3.ode.events.StepHandler;
import org.apache.commons.math3.ode.events.WynnStevensonEvent;
import org.apache.commons.math3.ode.sampling.StepInterpolator;
import org.apache.commons.math3.ode.sampling.StepHandlerAdapter;
import org.apache.commons.math3.ode.sampling.StepHandlerCollection;
import org.apache.commons.math3.ode.sampling.StepHandlerFactory;
import org.junit.Before;
import org.junit.Test;

public class EmbeddedRungeKuttaIntegratorTest {

    private static final double ACCURACY = 1e-8;
    private static final double TOLERANCE = 1e-8;
    private static final double INITIAL_STEP = 0.01;
    private static final double MAX_STEP = 10.0;
    private static final double MIN_STEP = 0.001;

    private EmbeddedRungeKuttaIntegrator integrator;
    private FirstOrderDifferentialEquations equations;
    private double[] y0;
    private double[] y1;
    private double[] y2;
    private double[][] yDotK;
    private double[][] a;
    private double[] c;

    @Before
    public void setUp() throws Exception {
        a = new double[][]{
            {0.0, 0.0},
            {0.3, 0.4},
            {0.6, 0.2},
            {1.0, 0.0}
        };
        c = new double[]{0.0, 0.3, 0.6, 1.0};
        integrator = new RK23FE(
                "RK23FE",
                true,
                c,
                a,
                new RKStepInterpolator(),
                INITIAL_STEP,
                MAX_STEP,
                MIN_STEP,
                new double[]{ACCURACY, TOLERANCE},
                new double[]{ACCURACY, TOLERANCE});
        equations = new DummyDifferentialEquations();
        y0 = new double[]{0.0, 0.0};
        y1 = new double[]{1.0, 1.0};
        y2 = new double[]{2.0, 2.0};
        yDotK = new double[5][2];
    }

    @Test
    public void testIntegrate() throws Exception {
        double t = 0.0;
        double[] y = new double[]{0.0, 0.0};
        integrator.integrate(equations, t, y0, t, y);
        assertEquals(y0, y, 0.0);
        t += INITIAL_STEP;
        integrator.integrate(equations, t, y, t, y);
        assertEquals(y1, y, ACCURACY);
    }

    @Test
    public void testIntegrateWithEvents() throws Exception {
        MultipleEventsManager eventsManager = new MultipleEventsManager();
        eventsManager.addEvent(new WynnStevensonEvent(1.0, EventType.FIRE_ON_ENTRY, new DefaultEventHandler()));
        integrator = new RK45(
                "RK45",
                true,
                c,
                a,
                new RKStepInterpolator(),
                INITIAL_STEP,
                MAX_STEP,
                MIN_STEP,
                new double[]{ACCURACY, TOLERANCE},
                new double[]{ACCURACY, TOLERANCE},
                new StepHandlerFactory() {
                    @Override
                    public StepHandler createStepHandler(int i, double t0, double[] y0, double stepSize) {
                        return new StepHandlerAdapter() {
                            @Override
                            public void handleStep(StepInterpolator interpolator, int step) {
                                eventsManager.handleEvent(interpolator.getTime(), new Event() {});
                            }
                        };
                    }
                });
        integrator.setEventsManager(eventsManager);

        double t = 0.0;
        double[] y = new double[]{0.0, 0.0};
        integrator.integrate(equations, t, y0, t, y);
        assertEquals(y0, y, 0.0);
        t += INITIAL_STEP;
        integrator.integrate(equations, t, y, t, y);
        assertTrue(eventsManager.getNextEvent().isFire());
        eventsManager.clearNextEvent();
        integrator.integrate(equations, t, y, t, y);
        assertTrue(eventsManager.getNextEvent().isFire());
    }

    @Test
    public void testIntegrateWithEventsDuringStep() throws Exception {
        MultipleEventsManager eventsManager = new MultipleEventsManager();
        eventsManager.addEvent(new WynnStevensonEvent(1.0, EventType.FIRE_ON_ENTRY, new DefaultEventHandler()));
        integrator = new RK45(
                "RK45",
                true,
                c,
                a,
                new RKStepInterpolator(),
                INITIAL_STEP,
                MAX_STEP,
                MIN_STEP,
                new double[]{ACCURACY, TOLERANCE},
                new double[]{ACCURACY, TOLERANCE},
                new StepHandlerFactory() {
                    @Override
                    public StepHandler createStepHandler(int i, double t0, double[] y0, double stepSize) {
                        return new StepHandlerAdapter() {
                            @Override
                            public void handleStep(StepInterpolator interpolator, int step) {
                                if (step % 2 == 0) {
                                    eventsManager.addEvent(new Event(EventType.FIRE_ON_ENTRY));
                                }
                            }
                        };
                    }
                });
        integrator.setEventsManager(eventsManager);

        double t = 0.0;
        double[] y = new double[]{0.0, 0.0};
        integrator.integrate(equations, t, y0, t, y);
        assertEquals(y0, y, 0.0);
        t += INITIAL_STEP;
        integrator.integrate(equations, t, y, t, y);
        assertTrue(eventsManager.getNextEvent().isFire());
        eventsManager.clearNextEvent();
        assertEquals(y1, y, ACCURACY);
        t += INITIAL_STEP;
        integrator.integrate(equations, t, y, t, y);
        assertTrue(eventsManager.getNextEvent().isFire());
        eventsManager.clearNextEvent();
        assertEquals(y2, y, ACCURACY);
        t += INITIAL_STEP;
        integrator.integrate(equations, t, y, t, y);
        assertTrue(eventsManager.getNextEvent().isFire());
        eventsManager.clearNextEvent();
        assertTrue(eventsManager.eventOccurred(EventType.FIRE_ON_ENTRY, 4));
    }

    @Test
    public void testIntegrateWithEventsAndDenseOutput() throws Exception {
        MultipleEventsManager eventsManager = new MultipleEventsManager();
        eventsManager.addEvent(new WynnStevensonEvent(1.0, EventType.FIRE_ON_ENTRY, new DefaultEventHandler()));
        integrator = new RKFehlberg85(
                "RKFehlberg85",
                true,
                c,
                a,
                new RKStepInterpolator(),
                INITIAL_STEP,
                MAX_STEP,
                MIN_STEP,
                new double[]{ACCURACY, TOLERANCE},
                new double[]{ACCURACY, TOLERANCE},
                new StepHandlerFactory() {
                    @Override
                    public StepHandler createStepHandler(int i, double t0, double[] y0, double stepSize) {
                        return new StepHandlerAdapter() {
                            @Override
                            public void handleStep(StepInterpolator interpolator, int step) {
                                if (step % 2 == 0) {
                                    eventsManager.addEvent(new Event(EventType.FIRE_ON_ENTRY));
                                }
                            }
                        };
                    }
                },
                new StepHandlerCollection());
        integrator.setEventsManager(eventsManager);

        double t = 0.0;
        double[] y = new double[]{0.0, 0.0};
        integrator.integrate(equations, t, y0, t, y);
        assertEquals(y0, y, 0.0);
        t += INITIAL_STEP;
        integrator.integrate(equations, t, y, t, y);
        assertTrue(eventsManager.getNextEvent().isFire());
        eventsManager.clearNextEvent();
        assertEquals(y1, y, ACCURACY);
        t += INITIAL_STEP;
        integrator.integrate(equations, t, y, t, y);
        assertTrue(eventsManager.getNextEvent().isFire());
        eventsManager.clearNextEvent();
        assertEquals(y2, y, ACCURACY);
        t += INITIAL_STEP;
        integrator.integrate(equations, t, y, t, y);
        assertTrue(eventsManager.getNextEvent().isFire());
        eventsManager.clearNextEvent();
        assertTrue(eventsManager.eventOccurred(EventType.FIRE_ON_ENTRY, 4));
        double[] expected = new double[]{1.0, 1.0};
        StepHandlerAdapter handler = (StepHandlerAdapter) integrator.getStepHandlers().get(0);
        assertArrayEquals(expected, handler.getValues(), ACCURACY);
    }

}

class DummyDifferentialEquations implements FirstOrderDifferentialEquations {

    @Override
    public int getNumberOfEquations() {
        return 2;
    }

    @Override
    public void computeDerivatives(double t, double[] y, double[] yPrime) {
        for (int i = 0; i < yPrime.length; i++) {
            yPrime[i] = 0;
        }
    }
}

class RK23FE extends EmbeddedRungeKuttaIntegrator {

    public RK23FE(String name, boolean fsal, double[] c, double[][] a,
                 RungeKuttaStepInterpolator prototype, double minStep, double maxStep,
                 double scalAbsoluteTolerance, double[] scalRelativeTolerance) {
        super(name, fsal, c, a, prototype, minStep, maxStep, scalAbsoluteTolerance, scalRelativeTolerance);
    }

    @Override
    protected double estimateError(double[][] yDotK, double[] y0, double[] y1, double h) {
        double[] y0Dot = yDotK[0];
        double[] y1Dot = yDotK[1];
        double[] y0Diff = new double[2];
        double[] y1Diff = new double[2];
        System.arraycopy(y1, 0, y1Diff, 0, 2);
        System.arraycopy(y0, 0, y0Diff, 0, 2);
        for (int i = 0; i < 2; i++) {
            y0Diff[i] -= y1Diff[i];
        }
        double sum = 0;
        for (int i = 0; i < 2; i++) {
            sum += Math.pow(Math.abs(y0Diff[i]) / (1 + i * h), 2);
        }
        return sum;
    }

}

class RKStepInterpolator implements StepInterpolator {

    @Override
    public void interpolate(double t, double[][] yDot) {
        for (int i = 0; i < yDot.length; i++) {
            for (int j = 0; j < yDot[i].length; j++) {
                yDot[i][j] = 0;
            }
        }
    }

    @Override
    public void interpolate(double t, double[] y, double[] yDot) {
        for (int i = 0; i < y.length; i++) {
            yDot[i] = 0;
        }
    }

}

class RK45 extends EmbeddedRungeKuttaIntegrator {

    public RK45(String name, boolean fsal, double[] c, double[][] a,
               RungeKuttaStepInterpolator prototype, double minStep, double maxStep,
               double scalAbsoluteTolerance, double[] scalRelativeTolerance) {
        super(name, fsal, c, a, prototype, minStep, maxStep, scalAbsoluteTolerance, scalRelativeTolerance);
    }

    @Override
    protected double estimateError(double[][] yDotK, double[] y0, double[] y1, double h) {
        double sum = 0;
        for (int i = 0; i < 2; i++) {
            double error = Math.abs(y1[i] - y0[i]) * Math.max(1.0, Math.abs(h));
            sum += Math.pow(error, 2);
        }
        return sum;
    }

}

class RKFehlberg85 extends EmbeddedRungeKuttaIntegrator {

    public RKFehlberg85(String name, boolean fsal, double[] c, double[][] a,
                       RungeKuttaStepInterpolator prototype, double minStep, double maxStep,
                       double scalAbsoluteTolerance, double[] scalRelativeTolerance) {
        super(name, fsal, c, a, prototype, minStep, maxStep, scalAbsoluteTolerance, scalRelativeTolerance);
    }

    @Override
    protected double estimateError(double[][] yDotK, double[] y0, double[] y1, double h) {
        double sum = 0;
        for (int i = 0; i < 2; i++) {
            double error = Math.abs(y1[i] - y0[i]) * Math.max(1.0, Math.abs(h));
            sum += Math.pow(error, 2);
        }
        for (int i = 1; i < 5; i++) {
            double y = y1[i - 1];
            double yDot = yDotK[i][0];
            double error = Math.abs(yDot) * Math.max(1.0, Math.abs(h));
            sum += Math.pow(error, 2);
        }
        return sum;
    }

}