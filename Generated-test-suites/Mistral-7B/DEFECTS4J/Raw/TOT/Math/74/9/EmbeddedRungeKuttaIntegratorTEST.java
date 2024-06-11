package org.apache.commons.math.ode.nonstiff.test;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.events.Event;
import org.apache.commons.math3.ode.events.When;
import org.apache.commons.math3.ode.sampling.StepHandler;
import org.apache.commons.math3.ode.nonstiff.EmbeddedRungeKuttaIntegrator;
import org.apache.commons.math3.ode.nonstiff.EmbeddedRungeKuttaIntegratorFactory;
import org.apache.commons.math3.ode.nonstiff.RungeKuttaStepInterpolator;
import org.apache.commons.math3.ode.events.RelativisticEvent;
import org.apache.commons.math3.ode.events.StepInterpolator;
import org.apache.commons.math3.ode.events.StepInterpolatingRunner;
import org.apache.commons.math3.util.Precision;

/**
 * Test cases for {@link EmbeddedRungeKuttaIntegrator}.
 */
public class EmbeddedRungeKuttaIntegratorTest extends TestCase {

    /**
     * Constructs a new test case.
     * @param name name of the test case
     */
    public EmbeddedRungeKuttaIntegratorTest(String name) {
        super(name);
    }

    /**
     * Constructs a test suite.
     * @return the test suite
     */
    public static Test suite() {
        return new TestSuite(EmbeddedRungeKuttaIntegratorTest.class);
    }

    /**
     * Test the <code>RK4</code> integrator.
     */
    public void testRK4() {
        final double a = 1.0;
        final double b = 0.0;
        final double c = 0.25;
        final double[] d = new double[] { 0.5, 0, 0.5, 0 };
        final double[] e = new double[] { 0.25, 0.25, 0.25, 0.25 };
        final double[] f = new double[] { 0.25, 0.25, 0.25, 0.25 };
        final double[] g = new double[] { 1.0 / 6.0, 1.0 / 3.0, 1.0 / 3.0, 1.0 / 6.0 };

        final EmbeddedRungeKuttaIntegratorFactory factory =
            new EmbeddedRungeKuttaIntegratorFactory("RK4", false, a, null, d, e, f, g,
                                                   null, null, 0.0);
        EmbeddedRungeKuttaIntegrator integrator = factory.createIntegrator();

        final FirstOrderDifferentialEquations equations =
            new FirstOrderDifferentialEquations() {

                public void computeDerivatives(double t, double[] y, double[] dy) {
                    dy[0] = y[1];
                    dy[1] = -a * y[0];
                }

            };

        final double[] y0 = new double[] { 1.0, 0.0 };
        final double t0 = 0.0;
        final double t1 = 1.0;
        final double[] y1 = new double[] { 0.636666687037387, -0.3183333435186936 };

        assertEquals(y1[0], integrator.integrate(equations, t0, y0, t1, null), 1e-5);
        assertEquals(y1[1], integrator.integrate(equations, t0, y0, t1, null)[1], 1e-5);

        // test with initial condition outside the stability region
        final double y0_minus = 2.0;
        final double[] y0_minus_array = new double[] { y0_minus, 0.0 };
        assertEquals(y0_minus, integrator.integrate(equations, t0, y0_minus_array, t1, null)[0], 1e-5);

        // test with small tolerance
        final double[] y0_small = new double[] { 1.0 + 1e-6, 0.0 };
        final double[] y0_small_array = new double[] { y0_small[0], y0_small[1] };
        final double[] y_small = new double[] { 0.6366311519833964, -0.318325568999602 };
        assertEquals(y_small[0], integrator.integrate(equations, t0, y0_small_array, t1, null)[0], 1e-9);
        assertEquals(y_small[1], integrator.integrate(equations, t0, y0_small_array, t1, null)[1], 1e-9);

        // test with large tolerance
        final double[] y0_large = new double[] { 1.0 + 1e6, 0.0 };
        final double[] y0_large_array = new double[] { y0_large[0], y0_large[1] };
        final double[] y_large = new double[] { 0.636666687037387, -0.3183333435186936 };
        assertEquals(y_large[0], integrator.integrate(equations, t0, y0_large_array, t1, null)[0], 1e-5);
        assertEquals(y_large[1], integrator.integrate(equations, t0, y0_large_array, t1, null)[1], 1e-5);

        // test with large step size
        final double h = 1.01;
        final double[] y_large_h = new double[] { 1.0, 0.0 };
        final double[] y_large_h_array = new double[] { y_large_h[0], y_large_h[1] };
        final double[] result = integrator.integrate(equations, t0, y_large_h_array, t1, null);
        assertEquals(y_large[0], result[0], 1e-5);
        assertEquals(y_large[1], result[1], 1e-5);

        // test with small step size
        final double h_small = 1e-3;
        final double[] y_small_h = new double[] { 1.0, 0.0 };
        final double[] y_small_h_array = new double[] { y_small_h[0], y_small_h[1] };
        final double[] result_small = integrator.integrate(equations, t0, y_small_h_array, t1, null);
        assertEquals(y_small[0], result_small[0], 1e-9);
        assertEquals(y_small[1], result_small[1], 1e-9);

        // test event handling
        final double[] y0_event = new double[] { 1.0, 0.0 };
        final double[] y1_event = new double[] { 0.636666687037387, -0.3183333435186936 };
        final double t2 = 2.0;
        final double[] y2 = new double[] { 0.1213144623139835, -0.0606572311569918 };
        final double[] eventsTime = new double[] { 1.01, 1.5 };
        final double[] eventsValues = new double[][] { new double[] { 1.0 }, new double[] { 2.0 } };
        final StepHandler handler = new StepHandler() {
            public void handleStep(StepInterpolator interpolator, int step) {
                double tInt = interpolator.getT();
                for (int i = 0; i < eventsTime.length; i++) {
                    if (Math.abs(tInt - eventsTime[i]) < 1e-6) {
                        assertEquals(eventsValues[i][0], interpolator.getY()[0], 1e-6);
                    }
                }
            }
        };
        final StepInterpolatingRunner runner = new StepInterpolatingRunner(equations, 1e-5, handler);
        final double[][] input = new double[][] { y0_event, new double[] { t2 } };
        runner.doStep(input, 0, eventsHandlersManager);

        final EmbeddedRungeKuttaIntegrator integratorEvent = factory.createIntegrator();
        final double[] output = integratorEvent.integrate(equations, t0, y0_event, t1, null);
        assertEquals(y1_event[0], output[0], 1e-6);
        assertEquals(y1_event[1], output[1], 1e-6);

        // test relativistic event handling
        final double e0 = 1.0;
        final double m0 = 1.0;
        final double x0 = 0.0;
        final double v0 = 0.0;
        final double t3 = 1.0;
        final double m1 = 0.1;
        final double v1 = 1.0;
        final double e1 = 0.5;
        final double x1 = 1.0;
        final double t4 = 2.0;
        final double m2 = 0.2;
        final double v2 = -1.0;
        final double e2 = 0.25;
        final double x2 = 2.0;
        final double t5 = 3.0;
        final double m3 = 0.3;
        final double v3 = 0.5;
        final double e3 = 1.0 / 3.0;
        final double x3 = 3.0;
        final double t6 = 4.0;
        final double m4 = 0.4;
        final double v4 = -0.5;
        final double e4 = 1.0 / 4.0;
        final double x4 = 4.0;
        final double t7 = 5.0;
        final double m5 = 0.5;
        final double v5 = 0.25;
        final double e5 = 1.0 / 5.0;
        final double x5 = 5.0;
        final double t8 = 6.0;
        final double m6 = 0.6;
        final double v6 = -0.25;
        final double e6 = 1.0 / 6.0;
        final double x6 = 6.0;
        final double[] y0_rel = new double[] { m0, v0, x0, e0 };
        final double[] y1_rel = new double[] { m1, v1, x1, e1 };
        final double[] y2_rel = new double[] { m2, v2, x2, e2 };
        final double[] y3_rel = new double[] { m3, v3, x3, e3 };
        final double[] y4_rel = new double[] { m4, v4, x4, e4 };
        final double[] y5_rel = new double[] { m5, v5, x5, e5 };
        final double[] y6_rel = new double[] { m6, v6, x6, e6 };
        final double[] eventsRelTime = new double[] { t3, t4, t5, t6, t7, t8 };
        final double[] eventsRelValues = new double[][] { new double[] { m1, v1, x1, e1 },
                                                           new double[] { m2, v2, x2, e2 },
                                                           new double[] { m3, v3, x3, e3 },
                                                           new double[] { m4, v4, x4, e4 },
                                                           new double[] { m5, v5, x5, e5 },
                                                           new double[] { m6, v6, x6, e6 },
                                                           new double[] { m6, -v6, x6, e6 } };
        final Event event1 = new RelativisticEvent(t3, new double[] { m1, v1, x1, e1 });
        final Event event2 = new RelativisticEvent(t4, new double[] { m2, v2, x2, e2 });
        final Event event3 = new RelativisticEvent(t5, new double[] { m3, v3, x3, e3 });
        final Event event4 = new RelativisticEvent(t6, new double[] { m4, v4, x4, e4 });
        final Event event5 = new RelativisticEvent(t7, new double[] { m5, v5, x5, e5 });
        final Event event6 = new RelativisticEvent(t8, new double[] { m6, v6, x6, e6 });
        final Event event7 = new RelativisticEvent(t8, new double[] { m6, -v6, x6, e6 });

        final StepHandler handlerRel = new StepHandler() {
            public void handleStep(StepInterpolator interpolator, int step) {
                double tInt = interpolator.getT();
                for (int i = 0; i < eventsRelTime.length; i++) {
                    if (Math.abs(tInt - eventsRelTime[i]) < 1e-6) {
                        assertEquals(eventsRelValues[i][0], interpolator.getY()[0], 1e-6);
                        assertEquals(eventsRelValues[i][1], interpolator.getY()[1], 1e-6);
                        assertEquals(eventsRelValues[i][2], interpolator.getY()[3], 1e-6);
                        assertEquals(eventsRelValues[i][3], interpolator.getY()[4], 1e-6);
                    }
                }
            }
        };
        final double[][] inputRel = new double[][] { y0_rel, new double[] { t1 } };
        runner.doStep(inputRel, 0, eventsHandlersManager);

        final EmbeddedRungeKuttaIntegrator integratorRel = factory.createIntegrator();
        final double[] outputRel = integratorRel.integrate(equations, t0, y0_rel, t1, null);
        assertEquals(y1_rel[0], outputRel[0], 1e-6);
        assertEquals(y1_rel[1], outputRel[1], 1e-6);
        assertEquals(y1_rel[2], outputRel[2], 1e-6);
        assertEquals(y1_rel[3], outputRel[3], 1e-6);

    }

}