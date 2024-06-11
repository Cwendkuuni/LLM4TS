package org.apache.commons.math.ode.nonstiff;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.junit.Before;
import org.junit.Test;

import org.apache.commons.math.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math.ode.events.When;
import org.apache.commons.math.ode.sampling.RungeKuttaStepInterpolator;

/**
 * Test cases for {@link EmbeddedRungeKuttaIntegrator}.
 */
public class EmbeddedRungeKuttaIntegratorTest {

    private EmbeddedRungeKuttaIntegrator integrator;

    @Before
    public void setUp() {
        integrator = new EmbeddedRungeKuttaIntegrator("test", false,
            new double[] { 0.3, 0.4, 0.1 },
            new double[][] { { 0.666666666666667, 0.333333333333333 },
                              { 0.8, 0 },
                              { 0, 1 },
                              { 1 - 0.666666666666667, 0.333333333333333 } },
            new double[] { 0.6, 0.4, 0.2 },
            new RungeKuttaStepInterpolator() {
                @Override
                public int getOrder() {
                    return 3;
                }
                @Override
                public boolean requiresDenseOutput() {
                    return false;
                }
                @Override
                public void reinitialize(EmbeddedRungeKuttaIntegrator integrator,
                                          double[] temp, double[][] yDot, boolean forward) {
                    // empty
                }
            },
            0.001, 0.01, null, null);
    }

    @Test
    public void testIntegrate() throws Exception {
        double t0 = 0.0;
        double[] y0 = { 1.0, 1.0 };
        double t = 1.0;
        double[] y = new double[y0.length];
        FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return y0.length;
            }
            @Override
            public void computeDerivatives(double t, double[] y, double[] yPrime) {
                for (int i = 0; i < y.length; i++) {
                    yPrime[i] = y[i] * 2.0;
                }
            }
        };
        integrator.integrate(equations, t0, y0, t, y);
        assertEquals(1.0, y[0], 1e-10);
        assertEquals(1.0, y[1], 1e-10);
    }

    @Test
    public void testIntegrateWithEvents() throws Exception {
        double t0 = 0.0;
        double[] y0 = { 1.0, 1.0 };
        double t = 1.0;
        double[] y = new double[y0.length];
        FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return y0.length;
            }
            @Override
            public void computeDerivatives(double t, double[] y, double[] yPrime) {
                for (int i = 0; i < y.length; i++) {
                    yPrime[i] = y[i] * 2.0;
                }
            }
        };
        CombinedEventsManager eventsManager = new CombinedEventsManager();
        When event = new When() {
            @Override
            public boolean isSatisfied(double t, double[] y) {
                return y[0] > 1.1;
            }
            @Override
            public void handleEvent(double t, double[] y) {
                throw new MaxCountExceededException();
            }
        };
        eventsManager.addEvent(event);
        integrator.setEventsHandlersManager(eventsManager);
        try {
            integrator.integrate(equations, t0, y0, t, y);
            fail("Expected MaxCountExceededException to be thrown.");
        } catch (MaxCountExceededException e) {
            // expected
        }

        // check that state is correct before event
        assertArrayEquals(y0, y, 1e-10);
    }

    @Test
    public void testIntegrateWithSmallStep() throws Exception {
        double t0 = 0.0;
        double[] y0 = { 1.0, 1.0 };
        double t = 0.1;
        double[] y = new double[y0.length];
        FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return y0.length;
            }
            @Override
            public void computeDerivatives(double t, double[] y, double[] yPrime) {
                for (int i = 0; i < y.length; i++) {
                    yPrime[i] = y[i] * 2.0;
                }
            }
        };
        integrator.integrate(equations, t0, y0, t, y);
        assertEquals(1.0009999999999999, y[0], 1e-5);
        assertEquals(1.001, y[1], 1e-5);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testIntegrateWithTooSmallStep() throws Exception {
        double t0 = 0.0;
        double[] y0 = { 1.0, 1.0 };
        double t = 0.1;
        double[] y = new double[y0.length];
        FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return y0.length;
            }
            @Override
            public void computeDerivatives(double t, double[] y, double[] yPrime) {
                for (int i = 0; i < y.length; i++) {
                    yPrime[i] = y[i] * 2.0;
                }
            }
        };
        integrator.setMinStep(0.0001);
        integrator.integrate(equations, t0, y0, t, y);
    }

    @Test
    public void testGetOrder() {
        assertEquals(3, integrator.getOrder());
    }

    @Test
    public void testGetSafety() {
        assertEquals(0.9, integrator.getSafety(), 1e-5);
    }

    @Test
    public void testSetSafety() {
        integrator.setSafety(0.8);
        assertEquals(0.8, integrator.getSafety(), 1e-5);
    }

    @Test
    public void testGetMinReduction() {
        assertEquals(0.2, integrator.getMinReduction(), 1e-5);
    }

    @Test
    public void testSetMinReduction() {
        integrator.setMinReduction(0.15);
        assertEquals(0.15, integrator.getMinReduction(), 1e-5);
    }

    @Test
    public void testGetMaxGrowth() {
        assertEquals(10.0, integrator.getMaxGrowth(), 1e-5);
    }

    @Test
    public void testSetMaxGrowth() {
        integrator.setMaxGrowth(5.0);
        assertEquals(5.0, integrator.getMaxGrowth(), 1e-5);
    }

    @Test
    public void testIsFSAL() {
        assertTrue(integrator.isFSAL());
    }

    @Test
    public void testIsNotFSAL() {
        integrator = new EmbeddedRungeKuttaIntegrator("test2", false,
            new double[] { 0.3, 0.4, 0.1 },
            new double[][] { { 0.6, 0.3 },
                              { 0.8, 0 },
                              { 0, 1 },
                              { 1 - 0.6, 0.3 } },
            new double[] { 0.6, 0.4, 0.2 },
            new RungeKuttaStepInterpolator() {
                @Override
                public int getOrder() {
                    return 3;
                }
                @Override
                public boolean requiresDenseOutput() {
                    return false;
                }
                @Override
                public void reinitialize(EmbeddedRungeKuttaIntegrator integrator,
                                          double[] temp, double[][] yDot, boolean forward) {
                    // empty
                }
            },
            0.001, 0.01, null, null);
        assertFalse(integrator.isFSAL());
    }

}