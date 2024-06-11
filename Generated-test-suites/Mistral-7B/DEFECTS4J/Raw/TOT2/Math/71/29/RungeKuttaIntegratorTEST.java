import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.events.RateEvent;
import org.apache.commons.math3.ode.nonstiff.RungeKuttaIntegrator;
import org.apache.commons.math3.ode.sampling.StepInterpolator;
import org.junit.Assert;
import org.junit.Test;

/**
 * Tests for {@link RungeKuttaIntegrator} class.
 */
public class RungeKuttaIntegratorTest {

    /**
     * Test for {@link RungeKuttaIntegrator#integrate(FirstOrderDifferentialEquations, double, double[], double, double[])}.
     */
    @Test
    public void testIntegrate() {
        final double[] initialValues = new double[] {0.0, 1.0};
        final double step = 0.1;
        final double start = 0.0;
        final double end = 2.0;

        final FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return initialValues.length;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] dy) {
                for (int i = 0; i < initialValues.length; ++i) {
                    dy[i] = 2.0 * y[i];
                }
            }
        };

        final RungeKuttaIntegrator integrator = new RungeKuttaIntegrator(
                "RK4",
                new double[] {0.25, 0.25, 0.5, 0.5},
                new double[][] {
                        {0.0, 0.0, 0.5, 0.5},
                        {0.25, 0.25, 0.25, 0.25},
                        {0.5, 0.5, 0.0, 0.0},
                        {0.5, 0.5, 0.0, 0.0}
                },
                new double[] {0.25, 0.25, 0.25, 0.25},
                new RungeKuttaStepInterpolator() {
                    @Override
                    public StepInterpolator copy() {
                        return new RungeKuttaStepInterpolator() {};
                    }
                },
                step
        ) {
            @Override
            protected void sanityChecks(FirstOrderDifferentialEquations equations,
                                        double t0, double[] y0, double t, double[] y) {
                // no checks needed
            }
        };

        final double result = integrator.integrate(equations, start, initialValues, end, new double[initialValues.length]);
        Assert.assertEquals(end, result, 1e-10);
    }

    /**
     * Test for {@link RungeKuttaIntegrator#integrate(FirstOrderDifferentialEquations, double, double[], double, double[])}.
     * Test for handling a rate event.
     */
    @Test
    public void testIntegrateWithEvent() {
        final double[] initialValues = new double[] {0.0, 1.0};
        final double step = 0.1;
        final double start = 0.0;
        final double end = 1.0;

        final FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return initialValues.length;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] dy) {
                for (int i = 0; i < initialValues.length; ++i) {
                    dy[i] = 2.0 * y[i];
                }
            }

            @Override
            public boolean hasEvent(double t, double[] y) {
                return y[0] >= 1.0;
            }

            @Override
            public double eventTime(double t, double[] y) {
                return 1.0;
            }

            @Override
            public void handleEvent(double t, double[] y, double[] dy) {
                dy[0] = 3.0;
            }
        };

        final RateEvent event = new RateEvent(1.0);

        final RungeKuttaIntegrator integrator = new RungeKuttaIntegrator(
                "RK4",
                new double[] {0.25, 0.25, 0.5, 0.5},
                new double[][] {
                        {0.0, 0.0, 0.5, 0.5},
                        {0.25, 0.25, 0.25, 0.25},
                        {0.5, 0.5, 0.0, 0.0},
                        {0.5, 0.5, 0.0, 0.0}
                },
                new double[] {0.25, 0.25, 0.25, 0.25},
                new RungeKuttaStepInterpolator() {
                    @Override
                    public StepInterpolator copy() {
                        return new RungeKuttaStepInterpolator() {};
                    }
                },
                step, new CombinedEventsManager() {
                    @Override
                    public void addEvent(RateEvent event) {
                        manager.addEvent(event);
                    }
                }
        ) {
            @Override
            protected void sanityChecks(FirstOrderDifferentialEquations equations,
                                        double t0, double[] y0, double t, double[] y) {
                // no checks needed
            }
        };

        integrator.eventsHandlersManager.addEvent(event);

        final double result = integrator.integrate(equations, start, initialValues, end, new double[initialValues.length]);
        Assert.assertEquals(end, result, 1e-10);
        Assert.assertArrayEquals(new double[] {1.0, 3.0}, integrator.getY(), 1e-10);
    }

}