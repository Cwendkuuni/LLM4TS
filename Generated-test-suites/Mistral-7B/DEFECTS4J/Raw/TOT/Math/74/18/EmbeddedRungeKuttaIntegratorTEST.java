package org.apache.commons.math.ode.nonstiff.test;

import junit.framework.TestCase;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.NumberFormatException;
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.events.Event;
import org.apache.commons.math3.ode.events.EventList;
import org.apache.commons.math3.ode.events.When;
import org.apache.commons.math3.ode.nonstiff.EmbeddedRungeKuttaIntegrator;
import org.apache.commons.math3.ode.nonstiff.RK4Integrator;
import org.apache.commons.math3.ode.sampling.RungeKuttaStepInterpolator;

/**
 * Tests for {@link EmbeddedRungeKuttaIntegrator}.
 */
public class EmbeddedRungeKuttaIntegratorTest extends TestCase {

    /**
     * Test that the constructor does not throw an exception.
     */
    public void testConstructor() {
        EmbeddedRungeKuttaIntegrator integrator =
            new EmbeddedRungeKuttaIntegrator(
                "RKF45", false,
                new double[]{0.25, 0.4666666666666666, 0.232, 0.0},
                new double[][]{{0.25, 0.5}, {0.4666666666666666, 0.232},
                                {0.232, 0.688}, {0.0, 1.0}},
                new double[]{0.25, 0.25, 0.25, 0.0},
                new RungeKuttaStepInterpolator(),
                0.1, 10.0, null, null) {
                    @Override
                    public int getOrder() {
                        return 4;
                    }
                };
        assertNotNull(integrator);
    }

    /**
     * Test that the constructor does not throw an exception with vectors.
     */
    public void testConstructorWithVectors() {
        double[] absoluteTolerance = new double[]{1.0e-5, 1.0e-5};
        double[] relativeTolerance  = new double[]{1.0e-6, 1.0e-6};
        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator(
                "RKF45", false,
                new double[]{0.25, 0.4666666666666666, 0.232, 0.0},
                new double[][]{{0.25, 0.5}, {0.4666666666666666, 0.232},
                                {0.232, 0.688}, {0.0, 1.0}},
                new double[]{0.25, 0.25, 0.25, 0.0},
                new RungeKuttaStepInterpolator(),
                0.1, 10.0, absoluteTolerance, relativeTolerance) {
                    @Override
                    public int getOrder() {
                        return 4;
                    }
                };
        assertNotNull(integrator);
    }

    /**
     * Test that an exception is thrown if the Butcher array is wrong.
     */
    public void testWrongButcherArray() {
        try {
            new EmbeddedRungeKuttaIntegrator(
                "RKF45", false,
                new double[]{0.25, 0.4666666666666666, 0.232, 0.0},
                new double[][]{{0.25, 0.5}, {0.4666666666666666, 1.232},
                                {0.232, 0.688}, {0.0, 1.0}},
                new double[]{0.25, 0.25, 0.25, 0.0},
                new RungeKuttaStepInterpolator(),
                0.1, 10.0, null, null) {
                    @Override
                    public int getOrder() {
                        return 4;
                    }
                };
            fail("Should have thrown an exception");
        } catch (MathIllegalArgumentException e) {
            // Expected
        }
    }

    /**
     * Test that the integrator correctly integrates a simple function.
     */
    public void testSimpleFunction() {
        double[] y0 = new double[]{0.0, 1.0};
        double[] y1 = new double[]{1.0, 2.0};
        double[] y2 = new double[]{2.0, 3.0};
        double[] y3 = new double[]{3.0, 4.0};
        double[] y4 = new double[]{4.0, 5.0};
        double[] y5 = new double[]{5.0, 6.0};
        FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return 2;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yPrime) {
                yPrime[0] = y[1];
                yPrime[1] = 2.0 * y[1];
            }
        };
        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator(
                "RKF45", false,
                new double[]{0.25, 0.4666666666666666, 0.232, 0.0},
                new double[][]{{0.25, 0.5}, {0.4666666666666666, 0.232},
                                {0.232, 0.688}, {0.0, 1.0}},
                new double[]{0.25, 0.25, 0.25, 0.0},
                new RungeKuttaStepInterpolator(),
                0.1, 10.0, null, null) {
                    @Override
                    public int getOrder() {
                        return 4;
                    }
                };
        double[] result = new double[]{6.0, 15.0};
        double tStart = 0.0;
        double tStop  = 4.0;
        try {
            integrator.integrate(equations, tStart, y0, tStop, y1);
        } catch (DeriveException e) {
            fail("Should not have thrown an exception");
        }
        assertArrayEquals(result, y1);
        reset(integrator);
        try {
            integrator.integrate(equations, tStart, y1, tStop, y2);
        } catch (DeriveException e) {
            fail("Should not have thrown an exception");
        }
        assertArrayEquals(result, y2);
        reset(integrator);
        try {
            integrator.integrate(equations, tStart, y2, tStop, y3);
        } catch (DeriveException e) {
            fail("Should not have thrown an exception");
        }
        assertArrayEquals(result, y3);
        reset(integrator);
        try {
            integrator.integrate(equations, tStart, y3, tStop, y4);
        } catch (DeriveException e) {
            fail("Should not have thrown an exception");
        }
        assertArrayEquals(result, y4);
        reset(integrator);
        try {
            integrator.integrate(equations, tStart, y4, tStop, y5);
        } catch (DeriveException e) {
            fail("Should not have thrown an exception");
        }
        assertArrayEquals(result, y5);
    }

    /**
     * Test that the integrator correctly integrates a function with events.
     */
    public void testFunctionWithEvents() {
        double[] y0 = new double[]{0.0, 1.0};
        double[] y1 = new double[]{1.0, 2.0};
        double[] y2 = new double[]{2.0, 3.0};
        double[] y3 = new double[]{3.0, 4.0};
        double[] y4 = new double[]{4.0, 5.0};
        double[] y5 = new double[]{5.0, 6.0};
        FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return 2;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yPrime) {
                yPrime[0] = y[1];
                yPrime[1] = 2.0 * y[1];
            }
        };
        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator(
                "RKF45", false,
                new double[]{0.25, 0.4666666666666666, 0.232, 0.0},
                new double[][]{{0.25, 0.5}, {0.4666666666666666, 0.232},
                                {0.232, 0.688}, {0.0, 1.0}},
                new double[]{0.25, 0.25, 0.25, 0.0},
                new RungeKuttaStepInterpolator(),
                0.1, 10.0, null, null) {
                    @Override
                    public int getOrder() {
                        return 4;
                    }
                };
        double[] result = new double[]{6.0, 15.0};
        double tStart = 0.0;
        double tStop  = 4.0;

        EventList events = new EventList();
        events.addEvent(new When(t -> t >= 2.0, 2.0, new double[]{1.0, 2.0}));
        integrator.setEventsManager(new CombinedEventsManager(events));

        try {
            integrator.integrate(equations, tStart, y0, tStop, y1);
        } catch (DeriveException e) {
            fail("Should not have thrown an exception");
        }
        assertArrayEquals(result, y1);
        reset(integrator);

        try {
            integrator.integrate(equations, tStart, y1, tStop, y2);
        } catch (DeriveException e) {
            fail("Should not have thrown an exception");
        }
        assertArrayEquals(result, y2);
        reset(integrator);

        try {
            integrator.integrate(equations, tStart, y2, tStop, y3);
        } catch (DeriveException e) {
            fail("Should not have thrown an exception");
        }
        assertArrayEquals(result, y3);
        reset(integrator);

        try {
            integrator.integrate(equations, tStart, y3, tStop, y4);
        } catch (DeriveException e) {
            fail("Should not have thrown an exception");
        }
        assertArrayEquals(result, y4);
        reset(integrator);

        try {
            integrator.integrate(equations, tStart, y4, tStop, y5);
        } catch (DeriveException e) {
            fail("Should not have thrown an exception");
        }
        assertArrayEquals(result, y5);

        try {
            integrator.integrate(equations, tStart, y5, tStop, y5);
        } catch (DeriveException e) {
            fail("Should have thrown an exception");
        }
    }

    /**
     * Test that the integrator correctly handles an out of bounds input.
     */
    public void testOutOfBoundsInput() {
        try {
            EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator(
                    "RKF45", false,
                    new double[]{0.25, 0.4666666666666666, 0.232, 0.0},
                    new double[][]{{0.25, 0.5}, {0.4666666666666666, 0.232},
                                    {0.232, 0.688}, {0.0, 1.0}},
                    new double[]{0.25, 0.25, 0.25, 0.0},
                    new RungeKuttaStepInterpolator(),
                    0.1, 10.0, null, null) {
                        @Override
                        public int getOrder() {
                            return 4;
                        }
                    };
            fail("Should have thrown an exception");
        } catch (NumberFormatException e) {
            // Expected
        }
    }

    /**
     * Test that the integrator correctly handles a null input.
     */
    public void testNullInput() {
        try {
            EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator(
                    "RKF45", false,
                    new double[]{0.25, 0.4666666666666666, 0.232, 0.0},
                    null,
                    new double[]{0.25, 0.25, 0.25, 0.0},
                    new RungeKuttaStepInterpolator(),
                    0.1, 10.0, null, null) {
                        @Override
                        public int getOrder() {
                            return 4;
                        }
                    };
            fail("Should have thrown an exception");
        } catch (NullPointerException e) {
            // Expected
        }
    }

    /**
     * Test that the integrator correctly handles zero step size.
     */
    public void testZeroStepSize() {
        try {
            EmbeddedRungeKuttaIntegrator integrator = new RK4Integrator();
            integrator.integrate(
                    new FirstOrderDifferentialEquations() {
                        @Override
                        public int getDimension() {
                            return 2;
                        }

                        @Override
                        public void computeDerivatives(double t, double[] y, double[] yPrime) {
                            yPrime[0] = y[1];
                            yPrime[1] = 2.0 * y[1];
                        }
                    },
                    0.0, new double[]{0.0, 1.0}, 0.0, new double[2]);
            fail("Should have thrown an exception");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    /**
     * Reset the internal state of the integrator.
     */
    public void reset(EmbeddedRungeKuttaIntegrator integrator) {
        integrator.setEquations(null);
        integrator.resetEvaluations();
        integrator.resetStepHandlers();
        integrator.resetEventsHandlersManager();
    }

}