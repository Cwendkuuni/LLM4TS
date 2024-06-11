package org.apache.commons.math.ode.nonstiff.test;

import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.events.Event;
import org.apache.commons.math3.ode.events.When;
import org.apache.commons.math3.ode.nonstiff.EmbeddedRungeKuttaIntegrator;
import org.apache.commons.math3.ode.nonstiff.EmbeddedRungeKuttaIntegratorFactory;
import org.apache.commons.math3.ode.sampling.RungeKuttaStepInterpolator;
import org.apache.commons.math3.special.BesselFunctions;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Test cases for the EmbeddedRungeKuttaIntegrator class.
 */
public class EmbeddedRungeKuttaIntegratorTest {

    private static final double TOLERANCE = 1.0e-10;
    private static final double ABSOLUTE_TOLERANCE = 1.0e-10;
    private static final double RELATIVE_TOLERANCE = 1.0e-10;

    private EmbeddedRungeKuttaIntegrator integrator;

    @Before
    public void setUp() throws Exception {
        integrator = new RK45Integrator(true, new double[]{-0.5, 0.5, 0.5, 0.5, 0.5, 0.5},
                new double[]{1.0/6.0, 1.0/3.0, 1.0/2.0, 1.0/2.0, 1.0/3.0, 1.0/6.0},
                new double[]{1.0/6.0, 1.0/3.0, 1.0/3.0, 1.0/3.0, 1.0/3.0, 1.0/3.0},
                new RungeKuttaStepInterpolator(), 0.001, 0.01, ABSOLUTE_TOLERANCE, RELATIVE_TOLERANCE);
    }

    @Test
    public void testIntegrateSimpleEquation() throws Exception {
        double y0 = 0.0;
        double[] y = new double[]{y0};

        FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yPrime) {
                yPrime[0] = 1.0;
            }
        };

        double t0 = 0.0;
        double t1 = 1.0;
        double result = integrator.integrate(equations, t0, y, t1, null);
        Assert.assertEquals(t1, result, TOLERANCE);
    }

    @Test
    public void testIntegrateEquationWithEvent() throws Exception {
        double y0 = 0.0;
        double[] y = new double[]{y0};

        FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yPrime) {
                yPrime[0] = y[0];
            }
        };

        double t0 = 0.0;
        double t1 = 1.0;
        double t2 = 2.0;

        CombinedEventsManager eventsManager = new CombinedEventsManager();
        eventsManager.addEvent(new When(t -> t >= 1.0, new Event() {
            @Override
            public void handle(double t, double[] y) {
                y[0] = 2.0;
            }
        }));

        integrator = new EmbeddedRungeKuttaIntegratorFactory()
                .createIntegrator("RK45", true, new double[]{-0.5, 0.5, 0.5, 0.5, 0.5, 0.5},
                        new double[]{1.0/6.0, 1.0/3.0, 1.0/2.0, 1.0/2.0, 1.0/3.0, 1.0/6.0},
                        new double[]{1.0/6.0, 1.0/3.0, 1.0/3.0, 1.0/3.0, 1.0/3.0, 1.0/3.0},
                        new RungeKuttaStepInterpolator(), 0.001, 0.01, ABSOLUTE_TOLERANCE, RELATIVE_TOLERANCE);

        integrator.setEventsManager(eventsManager);

        double result = integrator.integrate(equations, t0, y, t2, null);
        Assert.assertEquals(t2, result, TOLERANCE);
        Assert.assertEquals(2.0, y[0], TOLERANCE);
    }

    @Test(expected = DerivativeException.class)
    public void testIntegrateEquationWithInvalidDerivative() throws Exception {
        double y0 = 0.0;
        double[] y = new double[]{y0};

        FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yPrime) {
                throw new UnsupportedOperationException();
            }
        };

        double t0 = 0.0;
        double t1 = 1.0;

        integrator.integrate(equations, t0, y, t1, null);
    }

    @Test(expected = IntegratorException.class)
    public void testIntegrateEquationWithEventException() throws Exception {
        double y0 = 0.0;
        double[] y = new double[]{y0};

        FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yPrime) {
                yPrime[0] = y[0];
            }
        };

        double t0 = 0.0;
        double t1 = 1.0;
        double t2 = 2.0;

        CombinedEventsManager eventsManager = new CombinedEventsManager();
        eventsManager.addEvent(new When(t -> t >= 1.0, new Event() {
            @Override
            public void handle(double t, double[] y) {
                throw new RuntimeException();
            }
        }));

        integrator = new EmbeddedRungeKuttaIntegratorFactory()
                .createIntegrator("RK45", true, new double[]{-0.5, 0.5, 0.5, 0.5, 0.5, 0.5},
                        new double[]{1.0/6.0, 1.0/3.0, 1.0/2.0, 1.0/2.0, 1.0/3.0, 1.0/6.0},
                        new double[]{1.0/6.0, 1.0/3.0, 1.0/3.0, 1.0/3.0, 1.0/3.0, 1.0/3.0},
                        new RungeKuttaStepInterpolator(), 0.001, 0.01, ABSOLUTE_TOLERANCE, RELATIVE_TOLERANCE);

        integrator.setEventsManager(eventsManager);

        try {
            integrator.integrate(equations, t0, y, t2, null);
        } catch (DerivativeException e) {
            throw e;
        }
    }

    @Test
    public void testIntegrateBesselEquation() throws Exception {
        double y0 = 0.0;
        double[] y = new double[]{y0};

        FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yPrime) {
                yPrime[0] = t * BesselFunctions.j0(BesselFunctions.SQUARE_ROOT_PI * y[0]);
            }
        };

        double t0 = 0.0;
        double t1 = 2.0;

        integrator = new EmbeddedRungeKuttaIntegratorFactory()
                .createIntegrator("RKF45", false, new double[]{0.33333333333333333333, 0.33333333333333333333,
                        0.33333333333333333333, 0.33333333333333333333, 0.33333333333333333333, 0.33333333333333333333},
                        new double[]{0.2369268850561891, 0.12821344253112211, 0.0968526794923671, 0.0749859569545964,
                                    0.0612320432834795, 0.0502884956131959},
                        new double[]{0.2369268850561891, 0.12821344253112211, 0.12821344253112211, 0.12821344253112211,
                                    0.12821344253112211, 0.12821344253112211},
                        new RungeKuttaStepInterpolator(), 0.001, 0.01, ABSOLUTE_TOLERANCE, RELATIVE_TOLERANCE);

        double result = integrator.integrate(equations, t0, y, t1, null);
        Assert.assertEquals(Math.sqrt(Math.PI) * BesselFunctions.j0(Math.sqrt(Math.PI) * y[0]), y[0], TOLERANCE);
        Assert.assertEquals(BesselFunctions.j0(Math.sqrt(Math.PI) * y[0]) * Math.sqrt(Math.PI), y[0], TOLERANCE);
        Assert.assertEquals(t1, result, TOLERANCE);
    }
}