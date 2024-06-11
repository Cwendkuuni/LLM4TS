package org.apache.commons.math3.ode.integrator;

import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.ExpandableStatefulODE;
import org.apache.commons.math3.ode.events.AbstractEvent;
import org.apache.commons.math3.ode.events.Event;
import org.apache.commons.math3.ode.events.EventHandler;
import org.apache.commons.math3.ode.events.EventState;
import org.apache.commons.math3.ode.step.AbstractStepInterpolator;
import org.apache.commons.math3.ode.step.StepHandler;
import org.junit.Before;
import org.junit.Test;
import org.junit.Assert;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Test for {@link AbstractIntegrator}.
 */
public class AbstractIntegratorTest {

    private AbstractIntegrator integrator;
    private FirstOrderDifferentialEquations equations;
    private double[] initialValues;
    private double targetTime;
    private double[] expectedValues;

    /**
     * Initialize integrator, equations, initial values and target time.
     * @throws DimensionMismatchException
     */
    @Before
    public void setUp() throws DimensionMismatchException {
        integrator = new AbstractIntegrator() {
            @Override
            public void integrate(ExpandableStatefulODE equations, double t)
                    throws NumberIsTooSmallException, DimensionMismatchException,
                           MaxCountExceededException, NoBracketingException {
                // no-op
            }
        };
        equations = mock(FirstOrderDifferentialEquations.class);
        initialValues = new double[] {0.0, 1.0};
        targetTime = 3.0;
        expectedValues = new double[] {1.5, 2.5};
        when(equations.getDimension()).thenReturn(initialValues.length);
        when(equations.getDerivatives(initialValues)).thenReturn(initialValues);
        integrator.setEquations(equations);
    }

    /**
     * Test integration with no events or step handlers.
     */
    @Test
    public void testIntegrationNoEventsNoStepHandlers() {
        integrator.initIntegration(0.0, initialValues, targetTime);
        double[] actualValues = new double[initialValues.length];
        integrator.integrate(equations, targetTime, initialValues, actualValues);
        Assert.assertArrayEquals(expectedValues, actualValues);
    }

    /**
     * Test integration with an event that does not trigger a reset.
     */
    @Test
    public void testIntegrationEventNoReset() {
        integrator.addEventHandler(mock(EventHandler.class), 1.0, 0.01, 10, null);
        integrator.initIntegration(0.0, initialValues, targetTime);
        double[] actualValues = new double[initialValues.length];
        integrator.integrate(equations, targetTime, initialValues, actualValues);
        Assert.assertArrayEquals(expectedValues, actualValues);
    }

    /**
     * Test integration with an event that triggers a reset.
     */
    @Test
    public void testIntegrationEventWithReset() {
        integrator.addEventHandler(mock(EventHandler.class), 1.0, 0.01, 10, null);
        integrator.addEventHandler(mock(EventHandler.class), 2.0, 0.01, 10, null);
        integrator.initIntegration(0.0, initialValues, targetTime);
        double[] actualValues = new double[initialValues.length];
        integrator.integrate(equations, targetTime, initialValues, actualValues);
        Assert.assertArrayEquals(new double[] {1.0, 1.5}, actualValues);
    }

    /**
     * Test integration with a step handler.
     */
    @Test
    public void testIntegrationStepHandler() {
        StepHandler stepHandler = mock(StepHandler.class);
        integrator.addStepHandler(stepHandler);
        integrator.initIntegration(0.0, initialValues, targetTime);
        integrator.integrate(equations, targetTime, initialValues, null);
        when(stepHandler.getStepSize(0.0, initialValues, targetTime)).thenReturn(0.5);
        when(stepHandler.handleStep(IntegratorStepTest.TEST_INTERPOLATOR, false)).thenReturn(false);
        integrator.integrate(equations, targetTime, initialValues, null);
        when(stepHandler.handleStep(IntegratorStepTest.TEST_INTERPOLATOR, true)).thenReturn(true);
        integrator.integrate(equations, targetTime, initialValues, null);
        Assert.assertEquals(2.0, integrator.getCurrentStepStart(), 1e-14);
    }

    /**
     * Test integration with an event and a step handler.
     */
    @Test
    public void testIntegrationEventStepHandler() {
        integrator.addEventHandler(mock(EventHandler.class), 1.0, 0.01, 10, null);
        integrator.addStepHandler(mock(StepHandler.class));
        integrator.initIntegration(0.0, initialValues, targetTime);
        integrator.integrate(equations, targetTime, initialValues, null);
        integrator.integrate(equations, targetTime, initialValues, null);
        integrator.integrate(equations, targetTime, initialValues, null);
        integrator.integrate(equations, targetTime, initialValues, null);
        Assert.assertEquals(2.0, integrator.getCurrentStepStart(), 1e-14);
    }

    /**
     * Test integration with an event, a step handler and a reset.
     */
    @Test
    public void testIntegrationEventStepHandlerWithReset() {
        integrator.addEventHandler(mock(EventHandler.class), 1.0, 0.01, 10, null);
        integrator.addEventHandler(mock(EventHandler.class), 2.0, 0.01, 10, null);
        integrator.addStepHandler(mock(StepHandler.class));
        integrator.initIntegration(0.0, initialValues, targetTime);
        integrator.integrate(equations, targetTime, initialValues, null);
        integrator.integrate(equations, targetTime, initialValues, null);
        integrator.integrate(equations, targetTime, initialValues, null);
        integrator.integrate(equations, targetTime, initialValues, null);
        integrator.integrate(equations, targetTime, initialValues, null);
        integrator.integrate(equations, targetTime, initialValues, null);
        Assert.assertEquals(2.0, integrator.getCurrentStepStart(), 1e-14);
        Assert.assertTrue(integrator.isLastStep());
    }

    /**
     * Test integration with an event that stops integration.
     */
    @Test(expected = NoBracketingException.class)
    public void testIntegrationEventStop() {
        integrator.addEventHandler(mock(AbstractEvent.class), 1.0, 0.01, 10, null);
        integrator.initIntegration(0.0, initialValues, targetTime);
        integrator.integrate(equations, targetTime, initialValues, null);
    }

    /**
     * Test integration with a step size too small.
     */
    @Test(expected = NumberIsTooSmallException.class)
    public void testIntegrationStepSizeTooSmall() {
        integrator.setMaxEvaluations(1);
        integrator.integrate(equations, targetTime, initialValues, null);
    }

}

class IntegratorStepTest {

    /**
     * Test interpolator for integration step.
     */
    static AbstractStepInterpolator TEST_INTERPOLATOR = new AbstractStepInterpolator() {
        @Override
        public double getStepSize(double time, double[] state) {
            return 0.5;
        }

        @Override
        public double[] getInterpolatedState(double time) {
            return new double[] {1.0, 1.5};
        }

        @Override
        public double getGlobalCurrentTime() {
            return 0.0;
        }

        @Override
        public double getGlobalPreviousTime() {
            return 0.0;
        }

        @Override
        public void setInterpolatedTime(double time) {
            // no-op
        }

        @Override
        public void setSoftCurrentTime(double time) {
            // no-op
        }

        @Override
        public void setSoftPreviousTime(double time) {
            // no-op
        }

        @Override
        public void setInterpolatedState(double[] state) {
            // no-op
        }

        @Override
        public void setGlobalCurrentTime(double time) {
            // no-op
        }

        @Override
        public void setGlobalPreviousTime(double time) {
            // no-op
        }
    };

}