package org.apache.commons.math.ode.nonstiff;

import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.events.Event;
import org.apache.commons.math3.ode.events.When;
import org.apache.commons.math3.ode.nonstiff.EmbeddedRungeKuttaIntegrator;
import org.apache.commons.math3.ode.nonstiff.RungeKuttaStepInterpolator;
import org.apache.commons.math3.ode.sampling.StepHandler;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Tests for {@link EmbeddedRungeKuttaIntegrator} class.
 */
public class EmbeddedRungeKuttaIntegratorTest {

    private EmbeddedRungeKuttaIntegrator integrator;
    private FirstOrderDifferentialEquations equations;

    /**
     * Sets up the test.
     */
    @Before
    public void setUp() {
        equations = new MyDifferentialEquations();
        integrator = new RkFehlberg23Fehlberg87Integrator(1.0,
                1.0, new RungeKuttaStepInterpolator(1.0, 1.0, 10.0));
    }

    /**
     * Test the integration of simple equations with no events.
     */
    @Test
    public void testIntegration() {
        double y0[] = {0.0, 0.0};
        double tStart = 0.0;
        double tEnd = 1.0;
        double result[] = {1.0, 1.0};

        double y[] = integrator.integrate(equations, tStart, y0, tEnd, new double[2]);
        assertEquals(0.0, y[0] - result[0], 1e-14);
        assertEquals(0.0, y[1] - result[1], 1e-14);
    }

    /**
     * Test the integration of simple equations with events.
     */
    @Test
    public void testIntegrationWithEvents() {
        double y0[] = {0.0, 0.0};
        double tStart = 0.0;
        double tEnd = 2.0;

        final StepHandler stepHandler = new StepHandler() {
            @Override
            public void handleStep(StepHandlerContext context, int stepNumber) {
                if (context.getY()[0] >= 1.0) {
                    context.stopIntegration();
                }
            }
        };

        CombinedEventsManager manager = new CombinedEventsManager();
        manager.addEvent(new When(1.0, stepHandler));

        integrator.setStepHandlers(stepHandlers -> {
            stepHandlers.add(stepHandler);
        });

        double y[] = integrator.integrate(equations, tStart, y0, tEnd, new double[2]);
        assertEquals(0.0, y[0], 1e-14);
        assertEquals(0.0, y[1], 1e-14);
        assertTrue(integrator.isLastStepRejected());
    }

    /**
     * Test the integration with negative step size.
     */
    @Test(expected = MathIllegalArgumentException.class)
    public void testNegativeStepSize() {
        double y0[] = {0.0, 0.0};
        double tStart = 0.0;
        double tEnd = 1.0;
        double stepSize = -1.0;

        integrator.integrate(equations, tStart, y0, tEnd, new double[2]);
    }

    /**
     * Test the integration with very small step size.
     */
    @Test(expected = NumberIsTooSmallException.class)
    public void testVerySmallStepSize() {
        double y0[] = {0.0, 0.0};
        double tStart = 0.0;
        double tEnd = 1.0;
        double stepSize = 1e-15;

        integrator.integrate(equations, tStart, y0, tEnd, new double[2]);
    }

    /**
     * Test the integration with very large step size.
     */
    @Test
    public void testVeryLargeStepSize() {
        double y0[] = {0.0, 0.0};
        double tStart = 0.0;
        double tEnd = 1.0;
        double stepSize = 100.0;

        double y[] = integrator.integrate(equations, tStart, y0, tEnd, new double[2]);
        assertEquals(0.0, y[0], 1e-14);
        assertEquals(6.123234375, y[1], 1e-14);
    }

}