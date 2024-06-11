package org.apache.commons.math.ode.nonstiff;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NoBracketingException;
import org.apache.commons.math3.exception.UnsupportedOperationException;
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.events.Event;
import org.apache.commons.math3.ode.events.EventException;
import org.apache.commons.math3.ode.events.RootFunction;
import org.apache.commons.math3.ode.sampling.StepHandler;
import org.apache.commons.math3.ode.stepper.RungeKuttaStepInterpolator;
import org.apache.commons.math3.ode.testcharts.SinCosTestChart;
import org.apache.commons.math3.ode.testcharts.TestChart;
import org.apache.commons.math3.ode.testcharts.TestChartProvider;
import org.apache.commons.math3.ode.testutils.ODETestUtils;
import org.junit.Test;

/**
 * Tests for {@link EmbeddedRungeKuttaIntegrator}.
 */
public class EmbeddedRungeKuttaIntegratorTest {

    /**
     * Test a simple Runge-Kutta 2-3-2 integrator.
     */
    @Test
    public void testRungeKutta232() {
        final double[] c = {0.0, 0.5, 0.5, 1.0};
        final double[][] a = {
            {0.0, 0.0, 0.0},
            {0.5, 0.5, 0.0},
            {0.5, 0.0, 0.5},
            {1.0, 0.0, 1.0}
        };
        final double[] b = {0.25, 0.25, 0.5};
        final RungeKuttaStepInterpolator prototype = new RungeKuttaStepInterpolator(3);
        final EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("RK232", true, c, a, b, prototype, 1e-6, 1e6, null, null);

        // sanity check
        assertEquals("Error: wrong integrator order.", 2.0, integrator.getOrder());
        assertEquals("Error: wrong safety factor.", 0.9, integrator.getSafety(), 1e-3);
        assertEquals("Error: wrong minimal reduction factor.", 0.2, integrator.getMinReduction(), 1e-3);
        assertEquals("Error: wrong maximal growth factor.", 10.0, integrator.getMaxGrowth(), 1e-3);

        // test integration of a simple equation
        final double[] y0 = new double[] {0.0, 0.0};
        final double  t0 = 0.0;
        final double  t1 = 1.0;
        final FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return y0.length;
            }

            @Override
            public void computeDerivatives(double t, double[] y) {
                for (int i = 0; i < y.length; ++i) {
                    y[i] = y[i] + 0.1 * y[i] * (1 - y[i]) * t;
                }
            }
        };
        try {
            double result = integrator.integrate(equations, t0, y0, t1, new double[y0.length]);
            assertEquals("Error: integration result.", 1.0, result, 1e-3);
        } catch (DerivativeException | IntegratorException e) {
            throw new RuntimeException(e);
        }

        // test handling of events
        final RootFunction rootFunction = new RootFunction() {
            @Override
            public double value(double t, double[] y) {
                return Math.sin(y[0]) - Math.sin(0.2);
            }

            @Override
            public int getDimension() {
                return y[0].length;
            }
        };
        final int numEvents = 10;
        final double[][] yEvts = new double[numEvents][y0.length];
        final double[] tEvts = new double[numEvents];
        final double[][] yDotEvts = new double[numEvents][y0.length];
        for (int i = 0; i < numEvents; ++i) {
            tEvts[i] = Math.PI * (i + 1) / 2.0;
            yEvts[i] = new double[] {Math.sin(tEvts[i])};
            yDotEvts[i] = new double[] {Math.cos(tEvts[i])};
        }
        final Event[] events = new Event[numEvents];
        for (int i = 0; i < numEvents; ++i) {
            events[i] = new Event(tEvts[i], rootFunction, yEvts[i]);
        }
        final StepHandler stepHandler = new StepHandler() {
            @Override
            public void handleStep(StepHandlerContext context, int stepNumber, double[] work) {
                int index = (int) (stepNumber / 2);
                assertEquals("Error: wrong step number.", tEvts[index], context.getStepTime(), 1e-4);
                System.arraycopy(yEvts[index], 0, context.getY(), 0, yEvts[index].length);
                System.arraycopy(yDotEvts[index], 0, context.getYDot(), 0, yDotEvts[index].length);
            }
        };
        final CombinedEventsManager manager = new CombinedEventsManager();
        manager.addStepHandler(stepHandler);
        for (Event event : events) {
            manager.addEvent(event);
        }
        try {
            double result = integrator.integrate(equations, t0, y0, t1, new double[y0.length], manager);
            assertEquals("Error: integration result.", 1.0, result, 1e-3);
        } catch (DerivativeException | IntegratorException | EventException | MaxCountExceededException | NoBracketingException e) {
            throw new RuntimeException(e);
        }

        // test integration with dense output
        final TestChart chart = new SinCosTestChart(100);
        TestChartProvider chartProvider = new TestChartProvider() {
            @Override
            public TestChart getTestChart() {
                return chart;
            }
        };
        integrator.setRequireDenseOutput(true);
        integrator.setEventsHandlersManager(chartProvider);
        try {
            double[] y = new double[y0.length];
            double result = integrator.integrate(equations, t0, y0, t1, new double[y0.length]);
            assertEquals("Error: integration result.", 1.0, result, 1e-3);
            assertTrue("Error: integration did not fill the chart.", chart.isIntegrated(t0, y0, t1, y));
        } catch (DerivativeException | IntegratorException | MaxCountExceededException | NoBracketingException e) {
            throw new RuntimeException(e);
        }

        // test integration with no output
        integrator.setRequireDenseOutput(false);
        try {
            integrator.integrate(equations, t0, y0, t1, null);
        } catch (UnsupportedOperationException e) {
            // expected
        }
    }

}