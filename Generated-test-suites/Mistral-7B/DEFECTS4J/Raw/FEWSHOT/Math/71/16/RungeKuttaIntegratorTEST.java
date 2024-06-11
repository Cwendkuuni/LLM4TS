package org.apache.commons.math.ode.nonstiff;

import static org.junit.Assert.*;

import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.events.Event;
import org.apache.commons.math3.ode.events.When;
import org.apache.commons.math3.ode.sampling.StepHandler;
import org.apache.commons.math3.ode.sampling.StepInterpolator;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class RungeKuttaIntegratorTest {

    private FirstOrderDifferentialEquations equations;
    private RungeKuttaIntegrator integrator;
    private double[] initialState;
    private double[] finalState;
    private double initialTime;
    private double finalTime;
    private double step;
    private StepInterpolator interpolator;
    private StepHandler stepHandler;

    @Before
    public void setUp() {
        equations = new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return initialState.length;
            }

            @Override
            public void computeDerivatives(double time, double[] y, double[] yPrime) {
                System.arraycopy(y, 0, yPrime, 0, initialState.length);
            }
        };
        integrator = new RungeKuttaIntegrator(
                        "RK4",
                        new double[] { 0.0, 0.25, 0.5, 0.875, 1.0 },
                        new double[][] { { 0.0, 0.25, 0.25, 0.25, 0.25, 0.25 },
                                          { 0.25, 0.0, 0.5, 0.5, 0.5, 0.5 },
                                          { 0.25, 0.5, 0.0, 0.5, 0.5, 0.5 },
                                          { 0.25, 0.5, 0.5, 0.0, 0.5, 0.5 },
                                          { 0.25, 0.5, 0.5, 0.5, 0.0, 0.5 },
                                          { 0.25, 0.5, 0.5, 0.5, 0.5, 0.0 } },
                        new double[] { 1.0 / 6.0, 1.0 / 2.0, 1.0 / 2.0, 1.0 / 2.0, 1.0 / 6.0 },
                        new RungeKuttaStepInterpolator() {
                            @Override
                            public int getDimension() {
                                return initialState.length;
                            }
                        });
        initialState = new double[] { 1.0, 1.0 };
        finalState = new double[] { 2.0, 2.0 };
        initialTime = 0.0;
        finalTime = 1.0;
        step = 0.1;
        interpolator = integrator.getPrototype();
        stepHandler = new StepHandler() {
            @Override
            public void handleStep(StepInterpolator interpolator, int stepNumber) {
                double time = interpolator.getLocalTime();
                double[] y = interpolator.getLocalState();
                System.out.println("Time: " + time + ", State: " + java.util.Arrays.toString(y));
            }
        };
    }

    @After
    public void tearDown() {
        equations = null;
        integrator = null;
        initialState = null;
        finalState = null;
        initialTime = 0.0;
        finalTime = 0.0;
        step = 0.0;
        interpolator = null;
        stepHandler = null;
    }

    @Test
    public void testIntegration() {
        integrator.setStepSize(step);
        integrator.setEquations(equations);
        integrator.addStepHandler(stepHandler);
        integrator.integrate(initialTime, initialState, finalTime);
        assertEquals(finalState[0], integrator.getState()[0]);
        assertEquals(finalState[1], integrator.getState()[1]);
        assertEquals(finalTime, integrator.getLocalTime());
    }

    @Test
    public void testEventHandling() {
        integrator.setStepSize(step);
        integrator.setEquations(equations);
        integrator.addStepHandler(stepHandler);
        integrator.addEventsManager(new CombinedEventsManager() {
            @Override
            public void addEvent(Event event) {
                super.addEvent(event);
                System.out.println("Event: " + event.getClass().getName());
            }
        });
        integrator.addEvent(new When(0.5), () -> {
            throw new IllegalStateException("Test exception");
        });
        integrator.integrate(initialTime, initialState, finalTime);
        // The test should terminate with an IntegratorException
    }

    @Test(expected = IntegratorException.class)
    public void testEventHandlingException() {
        integrator.setStepSize(step);
        integrator.setEquations(equations);
        integrator.addStepHandler(stepHandler);
        integrator.addEventsManager(new CombinedEventsManager() {
            @Override
            public void addEvent(Event event) {
                super.addEvent(event);
                System.out.println("Event: " + event.getClass().getName());
            }
        });
        integrator.addEvent(new When(0.5), () -> {
            throw new RuntimeException("Test exception");
        });
        integrator.integrate(initialTime, initialState, finalTime);
    }

}