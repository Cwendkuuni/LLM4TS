package org.apache.commons.math.ode.nonstiff;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.events.MockEvent;
import org.apache.commons.math3.ode.events.MockEventsManager;
import org.apache.commons.math3.ode.events.StepHandler;
import org.apache.commons.math3.ode.sampling.AbstractStepInterpolator;
import org.apache.commons.math3.ode.sampling.DummyStepInterpolator;
import org.apache.commons.math3.ode.sampling.StepHandlerAdapter;
import org.apache.commons.math3.ode.nonstiff.EmbeddedRungeKuttaIntegrator;
import org.apache.commons.math3.ode.nonstiff.RungeKuttaStepInterpolator;
import org.junit.Before;
import org.junit.Test;

/**
 * Test cases for {@link EmbeddedRungeKuttaIntegrator}.
 */
public class EmbeddedRungeKuttaIntegratorTest {

    private EmbeddedRungeKuttaIntegrator integrator;
    private FirstOrderDifferentialEquations equations;
    private StepHandler handler;

    @Before
    public void setUp() {
        handler = mock(StepHandler.class);
        equations = mock(FirstOrderDifferentialEquations.class);
        integrator = new EmbeddedRungeKuttaIntegrator("RK4", true, new double[] { 0.25, 0.25, 0.25, 0.25 },
                new double[][] { { 0.25, 0 }, { 0, 0.25 }, { 0.25, 0.25 }, { 0, 0.25 } },
                new double[] { 0.25, 0.25, 0.25, 0.25 },
                new RungeKuttaStepInterpolator(), 0.001, 0.01, null, null) {

            @Override
            protected double estimateError(double[][] yDotK, double[] y0, double[] y1, double h) {
                return 1.0 + h / 10.0;
            }

            @Override
            public int getOrder() {
                return 4;
            }
        };
    }

    @Test
    public void testIntegration() throws Exception {
        double[] y0 = new double[] { 1.0, 1.0 };
        double t0 = 0.0;
        double t1 = 1.0;
        double[] y1 = new double[] { 2.0, 2.0 };
        when(equations.computeDerivatives(t0, y0)).thenCallRealMethod();

        integrator.setEquations(equations);
        integrator.addStepHandler(new StepHandlerAdapter(handler));

        double result = integrator.integrate(equations, t0, y0, t1, new double[y0.length]);
        assertEquals(t1, result, 1e-14);
        assertArrayEquals(y1, integrator.getY(), 1e-14);

        verify(handler).handleStep(integrator, true);
    }

    @Test
    public void testEventHandling() throws Exception {
        double[] y0 = new double[] { 1.0, 1.0 };
        double t0 = 0.0;
        double[] y1 = new double[] { 2.0, 2.0 };
        double t1 = 1.0;

        MockEventsManager manager = new MockEventsManager();
        MockEvent event = new MockEvent(2.0, 2.0);
        manager.addEvent(event);
        when(equations.computeDerivatives(eq(anyDouble()), eq(anyArray(double.class)))).thenCallRealMethod();

        integrator.setEquations(equations);
        integrator.addStepHandler(new StepHandlerAdapter(handler));
        integrator.setEventsManager(manager);

        integrator.integrate(equations, t0, y0, t1, new double[y0.length]);

        verify(manager).evaluateStep(integrator);
        verify(handler).handleStep(integrator, false);
        verify(event).isTriggered();
    }

    @Test
    public void testStepControl() throws Exception {
        double[] y0 = new double[] { 1.0, 1.0 };
        double t0 = 0.0;
        double t1 = 1.0;
        double[] y1 = new double[] { 2.0, 2.0 };
        when(equations.computeDerivatives(t0, y0)).thenCallRealMethod();

        integrator.setEquations(equations);
        integrator.addStepHandler(new StepHandlerAdapter(handler));

        integrator.integrate(equations, t0, y0, t1, new double[y0.length]);

        verify(handler).handleStep(integrator, true);

        integrator.setMinReduction(0.5);
        integrator.setMaxGrowth(2.0);

        integrator.integrate(equations, t0, y0, t1, new double[y0.length]);

        verify(handler).handleStep(integrator, false);
    }

    @Test
    public void testDenseOutput() throws Exception {
        double[] y0 = new double[] { 1.0, 1.0 };
        double t0 = 0.0;
        double t1 = 1.0;
        double[] y1 = new double[] { 2.0, 2.0 };
        when(equations.computeDerivatives(t0, y0)).thenCallRealMethod();

        integrator.setEquations(equations);
        integrator.addStepHandler(new StepHandlerAdapter(handler));

        integrator.setDenseOutput(true);

        integrator.integrate(equations, t0, y0, t1, new double[y0.length]);

        verify(handler).handleStep(integrator, true);

        double[][] result = integrator.getY();
        assertNotNull(result);
        assertArrayEquals(y1, result[result.length - 1], 1e-14);
    }

    @Test
    public void testNoDenseOutput() throws Exception {
        double[] y0 = new double[] { 1.0, 1.0 };
        double t0 = 0.0;
        double t1 = 1.0;
        double[] y1 = new double[] { 2.0, 2.0 };
        when(equations.computeDerivatives(t0, y0)).thenCallRealMethod();

        integrator.setEquations(equations);
        integrator.addStepHandler(new StepHandlerAdapter(handler));

        integrator.integrate(equations, t0, y0, t1, new double[y0.length]);

        verify(handler).handleStep(integrator, true);

        integrator.setDenseOutput(false);

        integrator.integrate(equations, t0, y0, t1, new double[y0.length]);

        verifyNoInteractions(handler);
    }

    @Test
    public void testEmptyEventsManager() throws Exception {
        double[] y0 = new double[] { 1.0, 1.0 };
        double t0 = 0.0;
        double t1 = 1.0;
        double[] y1 = new double[] { 2.0, 2.0 };
        when(equations.computeDerivatives(t0, y0)).thenCallRealMethod();

        integrator.setEquations(equations);
        integrator.addStepHandler(new StepHandlerAdapter(handler));

        integrator.setEventsManager(new MockEventsManager());

        integrator.integrate(equations, t0, y0, t1, new double[y0.length]);

        verifyNoInteractions(handler);
    }
}