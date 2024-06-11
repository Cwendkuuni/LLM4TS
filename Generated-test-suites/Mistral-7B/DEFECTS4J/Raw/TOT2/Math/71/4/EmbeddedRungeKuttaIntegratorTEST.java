import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.events.Event;
import org.apache.commons.math3.ode.events.MockEvent;
import org.apache.commons.math3.ode.nonstiff.EmbeddedRungeKuttaIntegrator;
import org.apache.commons.math3.ode.nonstiff.RungeKuttaStepInterpolator;
import org.apache.commons.math3.ode.sampling.StepHandler;
import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

/**
 * Tests for {@link EmbeddedRungeKuttaIntegrator}.
 */
public class EmbeddedRungeKuttaIntegratorTest {

    /**
     * Test for {@link EmbeddedRungeKuttaIntegrator#integrate(FirstOrderDifferentialEquations, double, double[], double, double[])}.
     */
    @Test
    public void testIntegrate() {
        final double[] y0 = new double[]{0.0, 1.0};
        final double[] y1 = new double[]{1.0, 2.0};
        final double[][] yDotK = new double[][]{{0.1, 0.2}, {0.2, 0.3}};

        final FirstOrderDifferentialEquations equations = new MockDifferentialEquations();
        final EmbeddedRungeKuttaIntegrator integrator = new RK4Integrator();
        integrator.setMinStep(0.01);
        integrator.setMaxStep(0.05);
        final double t0 = 0.0;
        final double t = 0.1;
        final double[] result = integrator.integrate(equations, t0, y0, t, new double[y0.length]);
        assertArrayEquals(y1, result, 1e-10);

        // test with events
        final Event event = new MockEvent(0.15, 2.0);
        final CombinedEventsManager manager = new CombinedEventsManager();
        manager.addStepHandler(new MockStepHandler());
        integrator.setEventsManager(manager);
        integrator.integrate(equations, t0, y0, t, new double[y0.length]);
        assertEquals(2.0, result[1], 1e-10);
        assertEquals(1.0, result[0], 1e-10);
    }

    /**
     * Test for {@link EmbeddedRungeKuttaIntegrator#integrate(FirstOrderDifferentialEquations, double, double[], double, double[], double[], double[], double[], double[])} with step handlers.
     */
    @Test
    public void testIntegrateWithStepHandlers() {
        final double[] y0 = new double[]{0.0, 1.0};
        final double[] y1 = new double[]{1.0, 2.0};
        final double[][] yDotK = new double[][]{{0.1, 0.2}, {0.2, 0.3}};

        final FirstOrderDifferentialEquations equations = new MockDifferentialEquations();
        final EmbeddedRungeKuttaIntegrator integrator = new RK4Integrator();
        integrator.setMinStep(0.01);
        integrator.setMaxStep(0.05);
        final double t0 = 0.0;
        final double t = 0.1;
        final double[] result = integrator.integrate(equations, t0, y0, t, new double[y0.length],
                                                   new double[y0.length], new double[y0.length], new double[y0.length], new double[y0.length]);
        assertArrayEquals(y1, result, 1e-10);

        // test with events and step handlers
        final Event event = new MockEvent(0.15, 2.0);
        final CombinedEventsManager manager = new CombinedEventsManager();
        manager.addStepHandler(new MockStepHandler());
        integrator.setEventsManager(manager);
        integrator.integrate(equations, t0, y0, t, new double[y0.length],
                            new double[y0.length], new double[y0.length], new double[y0.length], new double[y0.length]);
        assertEquals(2.0, result[1], 1e-10);
        assertEquals(1.0, result[0], 1e-10);
    }

    /**
     * Mock differential equations.
     */
    static class MockDifferentialEquations implements FirstOrderDifferentialEquations {

        @Override
        public int getNumberOfEquations() {
            return 2;
        }

        @Override
        public void computeDerivatives(double t, double[] y, double[] yPrime) {
            System.arraycopy(y, 0, yPrime, 0, y.length);
            yPrime[0] += 0.1;
            yPrime[1] += 0.2;
        }

    }

    /**
     * Mock step handler.
     */
    static class MockStepHandler implements StepHandler {

        private int count = 0;

        @Override
        public void handleStep(StepHandlerContext context, int stepNumber) {
            count++;
        }

        public int getCount() {
            return count;
        }

        @Override
        public void reset() {
            count = 0;
        }
    }

}