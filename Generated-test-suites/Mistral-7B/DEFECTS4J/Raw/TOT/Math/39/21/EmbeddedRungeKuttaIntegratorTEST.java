package org.apache.commons.math.ode.nonstiff.test;

import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.ode.ExpandableStatefulODE;
import org.apache.commons.math3.ode.continuous.DerivativeProvider;
import org.apache.commons.math3.ode.nonstiff.EmbeddedRungeKuttaIntegrator;
import org.apache.commons.math3.ode.nonstiff.RungeKuttaStepInterpolator;
import org.apache.commons.math3.special.E;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test suite for {@link EmbeddedRungeKuttaIntegrator}.
 */
public class EmbeddedRungeKuttaIntegratorTest {

    /**
     * Test simple integrator with a simple ODE.
     */
    @Test
    public void testSimpleIntegrator() {
        final double timeStart = 0.0;
        final double timeEnd   = 1.0;
        final double stepStart = 0.1;
        final double stepSize  = 0.2;
        final double relativeTolerance = 1e-6;
        final double absoluteTolerance = 1e-6;

        final DerivativeProvider provider = new DerivativeProvider() {
            @Override
            public double[] derive(double t, double[] y) {
                final double y1 = y[0];
                return new double[] {y1 * E.E};
            }
        };

        final ExpandableStatefulODE equations =
            new ExpandableStatefulODE(provider, 1) {
                @Override
                public double getMinStep() {
                    return stepStart;
                }

                @Override
                public double getMaxStep() {
                    return stepSize;
                }
            };

        final EmbeddedRungeKuttaIntegrator integrator = new RungeKuttaFehlberg78Method(
            7, new RungeKuttaStepInterpolator(7), timeStart, false,
            stepStart, stepSize, relativeTolerance, absoluteTolerance);

        integrator.integrate(equations, timeEnd);

        final double[] yExpected = new double[] {1.718281828};
        final double[] yActual = equations.getCompleteState();

        assertArrayEquals(yExpected, yActual, 1e-6);
    }

    /**
     * Test simple integrator with a singular ODE.
     */
    @Test(expected = MathIllegalArgumentException.class)
    public void testSingularODE() {
        final double timeStart = 0.0;
        final double timeEnd   = 1.0;
        final double stepStart = 0.1;
        final double stepSize  = 0.2;
        final double relativeTolerance = 1e-6;
        final double absoluteTolerance = 1e-6;

        final DerivativeProvider provider = new DerivativeProvider() {
            @Override
            public double[] derive(double t, double[] y) {
                final double y1 = y[0];
                return new double[] {1.0 / y1};
            }
        };

        final ExpandableStatefulODE equations =
            new ExpandableStatefulODE(provider, 1) {
                @Override
                public double getMinStep() {
                    return stepStart;
                }

                @Override
                public double getMaxStep() {
                    return stepSize;
                }
            };

        final EmbeddedRungeKuttaIntegrator integrator = new RungeKuttaFehlberg78Method(
            7, new RungeKuttaStepInterpolator(7), timeStart, false,
            stepStart, stepSize, relativeTolerance, absoluteTolerance);

        integrator.integrate(equations, timeEnd);

    }

    /**
     * Test simple integrator with an ODE with a large integration range.
     */
    @Test
    public void testLargeODE() {
        final double timeStart = 0.0;
        final double timeEnd   = Math.PI;
        final double stepStart = Math.PI / 16.0;
        final double stepSize  = Math.PI / 8.0;
        final double relativeTolerance = 1e-10;
        final double absoluteTolerance = 1e-10;

        final DerivativeProvider provider = new DerivativeProvider() {
            @Override
            public double[] derive(double t, double[] y) {
                final double y1 = y[0];
                return new double[] {-y1 * y1};
            }
        };

        final ExpandableStatefulODE equations =
            new ExpandableStatefulODE(provider, 1) {
                @Override
                public double getMinStep() {
                    return stepStart;
                }

                @Override
                public double getMaxStep() {
                    return stepSize;
                }
            };

        final EmbeddedRungeKuttaIntegrator integrator = new RungeKuttaFehlberg78Method(
            7, new RungeKuttaStepInterpolator(7), timeStart, false,
            stepStart, stepSize, relativeTolerance, absoluteTolerance);

        integrator.integrate(equations, timeEnd);

        final double[] yExpected = new double[] {Math.sin(Math.PI / 4.0)};
        final double[] yActual = equations.getCompleteState();
        assertEquals(yExpected[0], yActual[0], 1e-10);
    }

    /**
     * Test simple integrator with a large integration range and a small stepsize.
     */
    @Test
    public void testLargeODESmallStep() {
        final double timeStart = 0.0;
        final double timeEnd   = Math.PI;
        final double stepStart = Math.PI / 32.0;
        final double stepSize  = Math.PI / 16.0;
        final double relativeTolerance = 1e-14;
        final double absoluteTolerance = 1e-14;

        final DerivativeProvider provider = new DerivativeProvider() {
            @Override
            public double[] derive(double t, double[] y) {
                final double y1 = y[0];
                return new double[] {-y1 * y1};
            }
        };

        final ExpandableStatefulODE equations =
            new ExpandableStatefulODE(provider, 1) {
                @Override
                public double getMinStep() {
                    return stepStart;
                }

                @Override
                public double getMaxStep() {
                    return stepSize;
                }
            };

        final EmbeddedRungeKuttaIntegrator integrator = new RungeKuttaFehlberg78Method(
            7, new RungeKuttaStepInterpolator(7), timeStart, false,
            stepStart, stepSize, relativeTolerance, absoluteTolerance);

        integrator.integrate(equations, timeEnd);

        final double[] yExpected = new double[] {Math.sin(Math.PI / 4.0)};
        final double[] yActual = equations.getCompleteState();
        assertEquals(yExpected[0], yActual[0], 1e-14);
    }

    /**
     * Test simple integrator with a large integration range and a very small tolerance.
     */
    @Test
    public void testLargeODEVerySmallTolerance() {
        final double timeStart = 0.0;
        final double timeEnd   = Math.PI;
        final double stepStart = Math.PI / 32.0;
        final double stepSize  = Math.PI / 16.0;
        final double relativeTolerance = 1e-18;
        final double absoluteTolerance = 1e-18;

        final DerivativeProvider provider = new DerivativeProvider() {
            @Override
            public double[] derive(double t, double[] y) {
                final double y1 = y[0];
                return new double[] {-y1 * y1};
            }
        };

        final ExpandableStatefulODE equations =
            new ExpandableStatefulODE(provider, 1) {
                @Override
                public double getMinStep() {
                    return stepStart;
                }

                @Override
                public double getMaxStep() {
                    return stepSize;
                }
            };

        final EmbeddedRungeKuttaIntegrator integrator = new RungeKuttaFehlberg78Method(
            7, new RungeKuttaStepInterpolator(7), timeStart, false,
            stepStart, stepSize, relativeTolerance, absoluteTolerance);

        integrator.integrate(equations, timeEnd);

        final double[] yExpected = new double[] {Math.sin(Math.PI / 4.0)};
        final double[] yActual = equations.getCompleteState();
        assertEquals(yExpected[0], yActual[0], 1e-18);
    }

    /**
     * Test simple integrator with a large integration range and a very large tolerance.
     */
    @Test
    public void testLargeODEVeryLargeTolerance() {
        final double timeStart = 0.0;
        final double timeEnd   = Math.PI;
        final double stepStart = Math.PI / 32.0;
        final double stepSize  = Math.PI / 16.0;
        final double relativeTolerance = 1e-2;
        final double absoluteTolerance = 1e-2;

        final DerivativeProvider provider = new DerivativeProvider() {
            @Override
            public double[] derive(double t, double[] y) {
                final double y1 = y[0];
                return new double[] {-y1 * y1};
            }
        };

        final ExpandableStatefulODE equations =
            new ExpandableStatefulODE(provider, 1) {
                @Override
                public double getMinStep() {
                    return stepStart;
                }

                @Override
                public double getMaxStep() {
                    return stepSize;
                }
            };

        final EmbeddedRungeKuttaIntegrator integrator = new RungeKuttaFehlberg78Method(
            7, new RungeKuttaStepInterpolator(7), timeStart, false,
            stepStart, stepSize, relativeTolerance, absoluteTolerance);

        integrator.integrate(equations, timeEnd);

        final double[] yExpected = new double[] {Math.sin(Math.PI / 4.0)};
        final double[] yActual = equations.getCompleteState();
        assertArrayEquals(yExpected, yActual, 1e-2);
    }

    /**
     * Test simple integrator with a positively increasing ODE.
     */
    @Test
    public void testPositivelyIncreasingODE() {
        final double timeStart = 0.0;
        final double timeEnd   = 1.0;
        final double stepStart = 0.1;
        final double stepSize  = 0.2;
        final double relativeTolerance = 1e-6;
        final double absoluteTolerance = 1e-6;

        final DerivativeProvider provider = new DerivativeProvider() {
            @Override
            public double[] derive(double t, double[] y) {
                final double y1 = y[0];
                return new double[] {y1 * 2.0};
            }
        };

        final ExpandableStatefulODE equations =
            new ExpandableStatefulODE(provider, 1) {
                @Override
                public double getMinStep() {
                    return stepStart;
                }

                @Override
                public double getMaxStep() {
                    return stepSize;
                }
            };

        final EmbeddedRungeKuttaIntegrator integrator = new RungeKuttaFehlberg78Method(
            7, new RungeKuttaStepInterpolator(7), timeStart, false,
            stepStart, stepSize, relativeTolerance, absoluteTolerance);

        integrator.integrate(equations, timeEnd);

        final double[] yExpected = new double[] {Math.exp(2.0)};
        final double[] yActual = equations.getCompleteState();

        assertArrayEquals(yExpected, yActual, 1e-6);
    }

    /**
     * Test simple integrator with a negatively increasing ODE.
     */
    @Test
    public void testNegativelyIncreasingODE() {
        final double timeStart = 0.0;
        final double timeEnd   = 1.0;
        final double stepStart = 0.1;
        final double stepSize  = 0.2;
        final double relativeTolerance = 1e-6;
        final double absoluteTolerance = 1e-6;

        final DerivativeProvider provider = new DerivativeProvider() {
            @Override
            public double[] derive(double t, double[] y) {
                final double y1 = y[0];
                return new double[] {-y1 * 2.0};
            }
        };

        final ExpandableStatefulODE equations =
            new ExpandableStatefulODE(provider, 1) {
                @Override
                public double getMinStep() {
                    return stepStart;
                }

                @Override
                public double getMaxStep() {
                    return stepSize;
                }
            };

        final EmbeddedRungeKuttaIntegrator integrator = new RungeKuttaFehlberg78Method(
            7, new RungeKuttaStepInterpolator(7), timeStart, false,
            stepStart, stepSize, relativeTolerance, absoluteTolerance);

        integrator.integrate(equations, timeEnd);

        final double[] yExpected = new double[] {Math.exp(-2.0)};
        final double[] yActual = equations.getCompleteState();

        assertArrayEquals(yExpected, yActual, 1e-6);
    }

}