import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.events.Event;
import org.apache.commons.math3.ode.events.When;
import org.apache.commons.math3.ode.nonstiff.EmbeddedRungeKuttaIntegrator;
import org.apache.commons.math3.ode.nonstiff.RungeKuttaStepInterpolator;
import org.apache.commons.math3.ode.sampling.StepHandler;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Tests for {@link EmbeddedRungeKuttaIntegrator}.
 */
public class EmbeddedRungeKuttaIntegratorTest {

    /**
     * A simple test of the first order Runge-Kutta method.
     */
    @Test
    public void testFirstOrderRungeKutta() {
        final double a = 1.0;
        final double b = 1.0;
        final double[] y0 = {0.0};
        final double[] y1 = {1.0};
        final double t0 = 0.0;
        final double t1 = 1.0;

        final FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public void computeDerivatives(double time, double[] y, double[] derivatives) {
                derivatives[0] = a * y[0];
            }
        };

        final EmbeddedRungeKuttaIntegrator integrator =
                new EmbeddedRungeKuttaIntegrator("RKF45", false,
                        new double[]{0.25, 0.6923066488493555, 0.5555555555555556, 0.2777777777777778},
                        new double[][]{{0.25, 0, 0.6923066488493555, 0, 0.5555555555555556, 0.2777777777777778},
                                {0, 0.25, 0, 0.6923066488493555, 0.5555555555555556, 0.2777777777777778},
                                {0, 0, 0.25, 0, 0.6923066488493555, 0.2777777777777778},
                                {0, 0, 0, 0.25, 0.5555555555555556, 0.2777777777777778},
                                {0, 0, 0, 0, 0.25, 0.2777777777777778}},
                        new double[]{0.25, 0.25, 0.25, 0.25, 0.25, 0.25},
                        new RungeKuttaStepInterpolator(4),
                        0.01, 10.0,
                        1.0e-12, 1.0e-12) {
                    @Override
                    public int getOrder() {
                        return 4;
                    }
                };

        StepHandler stepHandler = new StepHandler() {
            @Override
            public void handleStep(AbstractStepInterpolator interpolator, int stepNumber) {
                final double[] y = interpolator.getY();
                System.out.printf("%s: %s%n", stepNumber, y[0]);
            }
        };

        CombinedEventsManager eventsManager = new CombinedEventsManager();
        eventsManager.addEvent(new When(t1) {
            @Override
            public void handleEvent(Event event) {
                assertEquals(y1[0], interpolator.getY()[0], 1.0e-12);
                fail("Should not reach this point");
            }
        });

        integrator.setStepHandlers(stepHandler, eventsManager);
        integrator.integrate(equations, t0, y0, t1, y1);

    }

    /**
     * A test of the embedded Runge-Kutta method with an event.
     */
    @Test
    public void testEmbeddedRungeKuttaWithEvent() {
        final double a = 1.0;
        final double b = 1.0;
        final double[] y0 = {0.0};
        final double[] y1 = {1.0};
        final double t0 = 0.0;
        final double t1 = 1.0;

        final FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public void computeDerivatives(double time, double[] y, double[] derivatives) {
                derivatives[0] = a * y[0];
            }
        };

        final EmbeddedRungeKuttaIntegrator integrator =
                new EmbeddedRungeKuttaIntegrator("RKF45", false,
                        new double[]{0.25, 0.6923066488493555, 0.5555555555555556, 0.2777777777777778},
                        new double[][]{{0.25, 0, 0.6923066488493555, 0, 0.5555555555555556, 0.2777777777777778},
                                {0, 0.25, 0, 0.6923066488493555, 0.5555555555555556, 0.2777777777777778},
                                {0, 0, 0.25, 0, 0.6923066488493555, 0.2777777777777778},
                                {0, 0, 0, 0.25, 0.5555555555555556, 0.2777777777777778},
                                {0, 0, 0, 0, 0.25, 0.2777777777777778}},
                        new double[]{0.25, 0.25, 0.25, 0.25, 0.25, 0.25},
                        new RungeKuttaStepInterpolator(4),
                        0.01, 10.0,
                        1.0e-12, 1.0e-12) {
                    @Override
                    public int getOrder() {
                        return 4;
                    }

                    @Override
                    public double estimateError(double[][] yDotK, double[] y0, double[] y1, double h) {
                        return 1.0;
                    }
                };

        StepHandler stepHandler = new StepHandler() {
            @Override
            public void handleStep(AbstractStepInterpolator interpolator, int stepNumber) {
                final double[] y = interpolator.getY();
                System.out.printf("%s: %s%n", stepNumber, y[0]);
            }
        };

        CombinedEventsManager eventsManager = new CombinedEventsManager();
        eventsManager.addEvent(new When(t1) {
            @Override
            public void handleEvent(Event event) {
                assertEquals(y1[0], interpolator.getY()[0], 1.0e-12);
                fail("Should not reach this point");
            }
        });

        integrator.setStepHandlers(stepHandler, eventsManager);
        integrator.integrate(equations, t0, y0, t1, y1);

        assertEquals(y1[0], integrator.getY()[0], 1.0e-12);

    }
}