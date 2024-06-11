package org.apache.commons.math3.ode.test;

import static org.junit.Assert.*;

import org.apache.commons.math3.ode.*;
import org.apache.commons.math3.ode.events.*;
import org.apache.commons.math3.ode.sampling.*;
import org.apache.commons.math3.ode.test.AbstractIntegratorTestCase;
import org.apache.commons.math3.ode.util.*;
import org.apache.commons.math3.util.*;

/**
 * Test class for AbstractIntegrator.
 */
public class AbstractIntegratorTest extends AbstractIntegratorTestCase {

    /**
     * Constructor.
     */
    public AbstractIntegratorTest() {
        super();
    }

    /**
     * Test integration of a simple differential equation.
     */
    public void testIntegration() {
        final double[] y0 = new double[] { 1.0, 0.0 };
        final double tEnd = 10.0;
        final double[] yExpected = new double[] { 2.718281828, 2.718281828 };
        final FirstOrderDifferentialEquations equations = new SimpleDifferentialEquations();
        try {
            final double[] y = integrator.integrate(equations, 0.0, y0, tEnd, new double[equations.getDimension()]);
            assertArrayEquals("Integration result is incorrect", yExpected, y);
        } catch (final Exception e) {
            fail("Integration failed unexpectedly: " + e.getMessage());
        }
    }

    /**
     * Test integration with step handler.
     */
    public void testIntegrationWithStepHandler() {
        final double[] y0 = new double[] { 1.0, 0.0 };
        final double tEnd = 10.0;
        final double[] yExpected = new double[] { 2.718281828, 2.718281828 };
        final FirstOrderDifferentialEquations equations = new SimpleDifferentialEquations();
        final StepHandler stepHandler = new StepHandler() {

            /**
             * Handle step.
             * @param interpolator the interpolator
             * @param isLastStep true if it's the last step
             */
            public void handleStep(StepInterpolator interpolator, boolean isLastStep) {
                if (isLastStep) {
                    System.out.println("Last step: y = " + Arrays.toString(interpolator.getInterpolatedState()));
                }
            }

        };
        try {
            integrator.addStepHandler(stepHandler);
            final double[] y = integrator.integrate(equations, 0.0, y0, tEnd, new double[equations.getDimension()]);
            assertArrayEquals("Integration result is incorrect", yExpected, y);
        } catch (final Exception e) {
            fail("Integration failed unexpectedly: " + e.getMessage());
        } finally {
            integrator.clearStepHandlers();
        }
    }

    /**
     * Test integration with event handler.
     */
    public void testIntegrationWithEventHandler() {
        final double[] y0 = new double[] { 1.0, 0.0 };
        final double tEnd = 10.0;
        final double[] yExpected = new double[] { 2.718281828, 2.718281828 };
        final FirstOrderDifferentialEquations equations = new SimpleDifferentialEquations();
        final EventHandler eventHandler = new EventHandler() {

            /**
             * Initialize.
             * @param t0 the initial time
             * @param y0 the initial state
             * @param t the target time
             */
            public void init(double t0, double[] y0, double t) {
                // empty implementation
            }

            /**
             * Evaluate event.
             * @param interpolator the interpolator
             * @return true if the event occurs during the step
             */
            public boolean evaluateEvent(StepInterpolator interpolator) {
                return interpolator.getInterpolatedTime() > 5.0;
            }

            /**
             * Accept event.
             * @param t the event time
             * @param y the event state
             */
            public void acceptEvent(double t, double[] y) {
                System.out.println("Event occurred at t = " + t + ", y = " + Arrays.toString(y));
                System.arraycopy(y, 0, y, 0, y.length);
            }

        };
        try {
            integrator.addEventHandler(eventHandler, 0.1, 0.01, 10);
            integrator.setMaxEvaluations(100);
            final double[] y = integrator.integrate(equations, 0.0, y0, tEnd, new double[equations.getDimension()]);
            assertArrayEquals("Integration result is incorrect", yExpected, y);
        } catch (final Exception e) {
            fail("Integration failed unexpectedly: " + e.getMessage());
        } finally {
            integrator.clearEventHandlers();
        }
    }

    /**
     * Test integration with event and step handlers.
     */
    public void testIntegrationWithEventAndStepHandlers() {
        final double[] y0 = new double[] { 1.0, 0.0 };
        final double tEnd = 10.0;
        final double[] yExpected = new double[] { 2.718281828, 2.718281828 };
        final FirstOrderDifferentialEquations equations = new SimpleDifferentialEquations();
        final EventHandler eventHandler = new EventHandler() {

            /**
             * Initialize.
             * @param t0 the initial time
             * @param y0 the initial state
             * @param t the target time
             */
            public void init(double t0, double[] y0, double t) {
                // empty implementation
            }

            /**
             * Evaluate event.
             * @param interpolator the interpolator
             * @return true if the event occurs during the step
             */
            public boolean evaluateEvent(StepInterpolator interpolator) {
                return interpolator.getInterpolatedTime() > 5.0;
            }

            /**
             * Accept event.
             * @param t the event time
             * @param y the event state
             */
            public void acceptEvent(double t, double[] y) {
                System.out.println("Event occurred at t = " + t + ", y = " + Arrays.toString(y));
                System.arraycopy(y, 0, y, 0, y.length);
            }

        };
        final StepHandler stepHandler = new StepHandler() {

            /**
             * Handle step.
             * @param interpolator the interpolator
             * @param isLastStep true if it's the last step
             */
            public void handleStep(StepInterpolator interpolator, boolean isLastStep) {
                if (isLastStep) {
                    System.out.println("Last step: y = " + Arrays.toString(interpolator.getInterpolatedState()));
                }
            }

        };
        try {
            integrator.addEventHandler(eventHandler, 0.1, 0.01, 10);
            integrator.addStepHandler(stepHandler);
            integrator.setMaxEvaluations(100);
            final double[] y = integrator.integrate(equations, 0.0, y0, tEnd, new double[equations.getDimension()]);
            assertArrayEquals("Integration result is incorrect", yExpected, y);
        } catch (final Exception e) {
            fail("Integration failed unexpectedly: " + e.getMessage());
        } finally {
            integrator.clearEventHandlers();
            integrator.clearStepHandlers();
        }
    }

    /**
     * Test integration with event, step handlers, and exceptions.
     */
    public void testIntegrationWithEventStepHandlersAndExceptions() {
        final double[] y0 = new double[] { 1.0, 0.0 };
        final double tEnd = 10.0;
        final FirstOrderDifferentialEquations equations = new SimpleDifferentialEquations();
        try {
            integrator.addEventHandler(new ExceptionThrowingEventHandler(), 0.1, 0.01, 10);
            integrator.addStepHandler(new ExceptionThrowingStepHandler());
            integrator.setMaxEvaluations(1);
            integrator.integrate(equations, 0.0, y0, tEnd, new double[equations.getDimension()]);
            fail("Integration should have thrown an exception");
        } catch (final Exception e) {
            // expected
        } finally {
            integrator.clearEventHandlers();
            integrator.clearStepHandlers();
        }
    }

}