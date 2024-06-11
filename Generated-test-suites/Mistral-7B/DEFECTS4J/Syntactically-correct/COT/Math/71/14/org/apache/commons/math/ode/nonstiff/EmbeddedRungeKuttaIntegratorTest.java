package org.apache.commons.math.ode.nonstiff.test;

import static org.junit.Assert.*;

import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.events.Event;
import org.apache.commons.math3.ode.events.When;
import org.apache.commons.math3.ode.sampling.StepHandler;
import org.apache.commons.math3.ode.sampling.StepInterpolator;
import org.apache.commons.math3.ode.testcharts.SinCosTestChart;
import org.apache.commons.math3.ode.nonstiff.EmbeddedRungeKuttaIntegrator;
import org.apache.commons.math3.ode.nonstiff.RungeKuttaStepInterpolator;
import org.apache.commons.math3.ode.nonstiff.EmbeddedRungeKuttaMethod;
import org.junit.Test;
import org.junit.Before;
import org.junit.After;

/**
 * Test cases for the EmbeddedRungeKuttaIntegrator class.
 */
public class EmbeddedRungeKuttaIntegratorTest {

    private EmbeddedRungeKuttaIntegrator integrator;
    private FirstOrderDifferentialEquations equations;
    private double[] initialValue;

    @Before
    public void setUp() {
        initialValue = new double[] {0.0, 1.0};
        equations = new SinCosTestChart();
        integrator = new EmbeddedRungeKutta23(1.0e-6, 1.0e-6, new RungeKuttaStepInterpolator());
    }

    @After
    public void tearDown() {
        integrator = null;
        equations = null;
    }

    /**
     * Test the getOrder method.
     */
    @Test
    public void testGetOrder() {
        assertEquals(2, integrator.getOrder());
    }

    /**
     * Test the integrate method with normal inputs.
     * @throws Exception if an unexpected exception is thrown
     */
    @Test
    public void testIntegrateNormalInput() throws Exception {
        double[] y = new double[] {1.0, 1.5};
        double tStart = 0.0;
        double tEnd   = 10.0;
        double result = integrator.integrate(equations, tStart, initialValue, tEnd, y);
        assertEquals(10.0, result, 0.001);
    }

    /**
     * Test the integrate method with a fixed step size.
     * @throws Exception if an unexpected exception is thrown
     */
    @Test
    public void testIntegrateFixedStepSize() throws Exception {
        double tStart = 0.0;
        double tEnd   = 10.0;
        double h      = 1.0;
        double[] y    = new double[2];

        integrator.setStepSize(h);
        integrator.integrate(equations, tStart, initialValue, tEnd, y);

        double[] expectedValues = new double[] {1.57079632679489661, 1.156293124999285};
        assertArrayEquals(expectedValues, y, 0.001);
    }

    /**
     * Test the integrate method with an initial value that does not match.
     * @throws Exception if an unexpected exception is thrown
     */
    @Test(expected = MathIllegalArgumentException.class)
    public void testIntegrateInitialValueMismatch() throws Exception {
        double[] initialValueMismatch = new double[] {1.0, 2.0};
        double tStart = 0.0;
        double tEnd   = 10.0;

        integrator.integrate(equations, tStart, initialValueMismatch, tEnd, null);
    }

    /**
     * Test the integrate method with an event that should be triggered.
     * @throws Exception if an unexpected exception is thrown
     */
    @Test
    public void testIntegrateEvent() throws Exception {
        double tStart = 0.0;
        double tEnd   = 1.0;
        double step   = 0.1;
        double[] y    = new double[2];

        StepHandler stepHandler = new StepHandler() {
            @Override
            public void handleStep(StepInterpolator interpolator, int stepNumber) {
                double t = interpolator.getStepTime();
                if (t >= 0.5) {
                    interpolator.setAcceptStep(false);
                }
            }
        };

        integrator.addStepHandler(stepHandler);

        integrator.integrate(equations, tStart, initialValue, tEnd, y);
        assertEquals(4, stepHandler.getStepNumber());
    }

    /**
     * Test the integrate method with an event that should stop the integration.
     * @throws Exception if an unexpected exception is thrown
     */
    @Test
    public void testIntegrateEventStop() throws Exception {
        double tStart = 0.0;
        double tEnd   = 1.0;
        double step   = 0.1;

        StepHandler stepHandler = new StepHandler() {
            @Override
            public void handleStep(StepInterpolator interpolator, int stepNumber) {
                double t = interpolator.getStepTime();
                if (t >= 0.5) {
                    interpolator.stopIntegration();
                }
            }
        };

        integrator.addStepHandler(stepHandler);

        integrator.integrate(equations, tStart, initialValue, tEnd, null);
        assertEquals(1, stepHandler.getStepNumber());
    }

    /**
     * Test the integrate method with an event that should be triggered and
     * stop the integration.
     * @throws Exception if an unexpected exception is thrown
     */
    @Test
    public void testIntegrateEventStopAndTrigger() throws Exception {
        double tStart = 0.0;
        double tEnd   = 1.0;
        double step   = 0.1;

        StepHandler stepHandler = new StepHandler() {
            @Override
            public void handleStep(StepInterpolator interpolator, int stepNumber) {
                double t = interpolator.getStepTime();
                if (t >= 0.5) {
                    interpolator.stopIntegration();
                    interpolator.addEvent(new When(t + 0.1), new Event() {
                        @Override
                        public void integrateEvent(double time, double[] state) {
                            assertEquals(1.57079632679489661, state[0], 0.001);
                            assertEquals(1.156293124999285, state[1], 0.001);
                        }
                    });
                }
            }
        };

        integrator.addStepHandler(stepHandler);

        integrator.integrate(equations, tStart, initialValue, tEnd, null);
        assertEquals(2, stepHandler.getStepNumber());
    }

    /**
     * Test the integrate method with an event that should be triggered twice.
     * @throws Exception if an unexpected exception is thrown
     */
    @Test
    public void testIntegrateEventTriggerTwice() throws Exception {
        double tStart = 0.0;
        double tEnd   = 1.0;
        double step   = 0.1;

        StepHandler stepHandler = new StepHandler() {
            int triggerCount = 0;

            @Override
            public void handleStep(StepInterpolator interpolator, int stepNumber) {
                double t = interpolator.getStepTime();
                if (t >= 0.5) {
                    interpolator.addEvent(new When(t + 0.1), new Event() {
                        @Override
                        public void integrateEvent(double time, double[] state) {
                            assertEquals(1.57079632679489661, state[0], 0.001);
                            assertEquals(1.156293124999285, state[1], 0.001);
                            triggerCount++;
                        }
                    });
                    interpolator.addEvent(new When(t + 0.2), new Event() {
                        @Override
                        public void integrateEvent(double time, double[] state) {
                            assertEquals(1.57079632679489661, state[0], 0.001);
                            assertEquals(1.156293124999285, state[1], 0.001);
                            triggerCount++;
                        }
                    });
                }
            }

            @Override
            public int getStepNumber() {
                return triggerCount;
            }

        };

        integrator.addStepHandler(stepHandler);

        integrator.integrate(equations, tStart, initialValue, tEnd, null);
        assertEquals(2, stepHandler.getStepNumber());
    }
}