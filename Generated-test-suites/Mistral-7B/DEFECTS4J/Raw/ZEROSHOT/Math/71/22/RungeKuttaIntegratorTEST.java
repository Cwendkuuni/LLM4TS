package org.apache.commons.math.ode.nonstiff;

import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.NoIntegratorFoundException;
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.Integrator;
import org.apache.commons.math3.ode.events.Event;
import org.apache.commons.math3.ode.events.DefaultCombinedEventsManager;
import org.apache.commons.math3.ode.events.StepHandler;
import org.apache.commons.math3.ode.sampling.RungeKuttaStepInterpolator;
import org.apache.commons.math3.ode.sampling.DummyStepInterpolator;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * This class tests the RungeKuttaIntegrator abstract class.
 */
public class RungeKuttaIntegratorTest {

    private static RungeKuttaIntegrator integrator;
    private static FirstOrderDifferentialEquations equations;
    private static double[] initialValues;
    private static double startTime;
    private static double endTime;

    @BeforeClass
    public static void setUpOnce() {
        // Define a simple differential equation system
        equations = new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yPrime) {
                yPrime[0] = y[0] * (1 - y[0]);
            }
        };

        // Create a Runge-Kutta integrator with a step size of 0.1
        initialValues = new double[] { 0.1 };
        startTime = 0.0;
        endTime = 1.0;
        integrator = new RungeKuttaIntegrator("RK4",
                                             new double[] { 0.5, 0.5, 0.5, 0.5 },
                                             new double[][] { { 0.25, 0.25 },
                                                             { 0.25, 0 },
                                                             { 0, 0.25 },
                                                             { 0, 0.25 } },
                                             new double[] { 0.25, 0.25, 0.25, 0.25 },
                                             new RungeKuttaStepInterpolator(),
                                             0.1) {
            @Override
            public Integrator createIntegratorInstance() {
                return this;
            }
        };

    }

    /**
     * Test the integration of a simple differential equation system using a Runge-Kutta integrator.
     */
    @Test
    public void testIntegration() {
        DefaultCombinedEventsManager eventsManager = new DefaultCombinedEventsManager();
        StepHandler stepHandler = new StepHandler() {
            @Override
            public void handleStep(StepInterpolator interpolator, int stepNumber) {
                double t = interpolator.getTime();
                double y = interpolator.getValue(0);
                System.out.println("t = " + t + ", y = " + y);
            }
        };
        eventsManager.addStepHandler(stepHandler);

        try {
            integrator.integrate(equations, startTime, initialValues, endTime, new double[initialValues.length]);
            Assert.assertEquals(endTime, integrator.getStepStart(), 1e-12);

            // Check the results with a tolerance of 1e-6
            double[] result = integrator.getSolution();
            Assert.assertArrayEquals(new double[] { 0.7408245563491615, }, result, 1e-6);

        } catch (DerivativeException | IntegratorException e) {
            Assert.fail("Integration failed: " + e.getMessage());
        }

    }

    /**
     * Test the integration of a differential equation system with a step size that is too large.
     */
    @Test(expected = IntegratorException.class)
    public void testIntegrationTooLargeStepSize() {
        integrator.setStep(1.0);
        try {
            integrator.integrate(equations, startTime, initialValues, endTime, new double[initialValues.length]);
        } catch (DerivativeException e) {
            Assert.fail("Derivative computation failed: " + e.getMessage());
        }
    }

    /**
     * Test the integration of a differential equation system with a negative step size.
     */
    @Test(expected = MathIllegalArgumentException.class)
    public void testIntegrationNegativeStepSize() {
        integrator.setStep(-0.1);
        try {
            integrator.integrate(equations, startTime, initialValues, endTime, new double[initialValues.length]);
        } catch (DerivativeException e) {
            Assert.fail("Derivative computation failed: " + e.getMessage());
        } catch (IntegratorException e) {
            Assert.fail("Integration failed: " + e.getMessage());
        }
    }

    /**
     * Test the integration of a differential equation system with a step size that is too small.
     */
    @Test
    public void testIntegrationTooSmallStepSize() {
        integrator.setStep(0.0001);
        try {
            integrator.integrate(equations, startTime, initialValues, endTime, new double[initialValues.length]);
            // Check the results with a tolerance of 1e-6
            double[] result = integrator.getSolution();
            Assert.assertArrayEquals(new double[] { 0.7408245563491615, }, result, 1e-6);

        } catch (DerivativeException | IntegratorException e) {
            Assert.fail("Integration failed: " + e.getMessage());
        }
    }

    /**
     * Test the integration of a differential equation system with events.
     */
    @Test
    public void testIntegrationWithEvents() {
        Double eventTime = 0.5;
        Double eventValue = 2.0;
        Event event = new Event(eventTime, eventValue) {
            @Override
            public boolean equals(Object obj) {
                if (obj == this) {
                    return true;
                } else if (!(obj instanceof Event)) {
                    return false;
                }
                Event otherEvent = (Event) obj;
                return otherEvent.getTime() == eventTime && otherEvent.getValue() == eventValue;
            }
        };
        DefaultCombinedEventsManager eventsManager = new DefaultCombinedEventsManager();
        StepHandler stepHandler = new StepHandler() {
            @Override
            public void handleStep(StepInterpolator interpolator, int stepNumber) {
                double t = interpolator.getTime();
                double y = interpolator.getValue(0);
                System.out.println("t = " + t + ", y = " + y);
                if (t >= eventTime) {
                    eventsManager.addEvent(event);
                    integrator.stopIntegration();
                }
            }
        };
        eventsManager.addStepHandler(stepHandler);
        integrator.addEventHandler(eventsManager);

        try {
            integrator.integrate(equations, startTime, initialValues, endTime, new double[initialValues.length]);
            Assert.fail("Integration should have stopped at the event.");
        } catch (IntegratorException e) {
            // Expected exception
        }

        Assert.assertEquals(eventTime, integrator.getLastEventTime(), 1e-12);
        Assert.assertEquals(eventValue, integrator.getLastEventValue(), 1e-12);

    }

}