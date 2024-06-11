package org.apache.commons.math.ode.nonstiff;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.ode.ExpandableStatefulODE;
import org.apache.commons.math3.ode.builder.AbstractODEBuilder;
import org.apache.commons.math3.ode.events.IntervalEvent;
import org.apache.commons.math3.ode.events.SimpleRegularEvent;
import org.apache.commons.math3.ode.sampling.StepHandler;
import org.junit.Test;

/**
 * Tests for {@link EmbeddedRungeKuttaIntegrator}.
 */
public class EmbeddedRungeKuttaIntegratorTest {

    /**
     * Build a simple ODE to test the integrator.
     * @return the ODE
     */
    private static ExpandableStatefulODE buildODE() {
        return new AbstractODEBuilder()
            .setName("test")
            .setTimeVariable("t")
            .addState("y", 0.0)
            .addDerivative("yPrime", 1.0, t -> 1.0)
            .build();
    }

    /**
     * Test the basic integration.
     */
    @Test
    public void testBasicIntegration() throws Exception {
        final EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKutta4Integrator(
            0.25, new RungeKuttaStepInterpolator() {
                @Override
                public int getOrder() {
                    return 4;
                }
            },
            0.0, new double[] {0.25, 0.25, 0.5, 0.5},
            new double[][] {
                {0.0, 0.0, 0.25, 0.25},
                {0.25, 0.25, 0.5, 0.5},
                {0.5, 0.5, 1.0, 1.0}
            },
            new double[] {0.25, 0.25, 0.5, 0.5},
            new RungeKuttaStepInterpolator() {
                @Override
                public int getOrder() {
                    return 4;
                }
            }
        );

        ExpandableStatefulODE equations = buildODE();
        double start = 0.0;
        double end   = 1.0;

        // test integration
        integrator.integrate(equations, start);
        assertEquals(1.0, equations.getCompleteState()[0], 1e-6);

        // test integration with an event
        StepHandler handler = new StepHandler() {
            @Override
            public int handle(double t, double[] y, double[] yDot, int direction, double h, boolean first)
                throws Exception {
                throw new IntervalEvent(t + h, new SimpleRegularEvent());
            }
        };
        integrator.addStepHandler(handler);
        integrator.integrate(equations, start, end);
        assertTrue(handler.isHandled());
    }

    /**
     * Test the integration with a step size that is too large.
     */
    @Test(expected = MathIllegalArgumentException.class)
    public void testStepSizeTooLarge() throws Exception {
        final EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKutta4Integrator(
            0.25, new RungeKuttaStepInterpolator() {
                @Override
                public int getOrder() {
                    return 4;
                }
            },
            0.0, new double[] {0.25, 0.25, 0.5, 0.5},
            new double[][] {
                {0.0, 0.0, 0.25, 0.25},
                {0.25, 0.25, 0.5, 0.5},
                {0.5, 0.5, 1.0, 1.0}
            },
            new double[] {0.25, 0.25, 0.5, 0.5},
            new RungeKuttaStepInterpolator() {
                @Override
                public int getOrder() {
                    return 4;
                }
            }
        );

        ExpandableStatefulODE equations = buildODE();

        double stepSize = 10.0;
        integrator.setMinStep(stepSize);
        integrator.setMaxStep(stepSize);

        integrator.integrate(equations, 0.0);
    }

    /**
     * Test the integration with a step size that is too small.
     */
    @Test(expected = MathIllegalArgumentException.class)
    public void testStepSizeTooSmall() throws Exception {
        final EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKutta4Integrator(
            0.25, new RungeKuttaStepInterpolator() {
                @Override
                public int getOrder() {
                    return 4;
                }
            },
            0.0, new double[] {0.25, 0.25, 0.5, 0.5},
            new double[][] {
                {0.0, 0.0, 0.25, 0.25},
                {0.25, 0.25, 0.5, 0.5},
                {0.5, 0.5, 1.0, 1.0}
            },
            new double[] {0.25, 0.25, 0.5, 0.5},
            new RungeKuttaStepInterpolator() {
                @Override
                public int getOrder() {
                    return 4;
                }
            }
        );

        ExpandableStatefulODE equations = buildODE();

        double stepSize = 0.001;
        integrator.setMinStep(stepSize);
        integrator.setMaxStep(stepSize);

        integrator.integrate(equations, 0.0);
    }

}