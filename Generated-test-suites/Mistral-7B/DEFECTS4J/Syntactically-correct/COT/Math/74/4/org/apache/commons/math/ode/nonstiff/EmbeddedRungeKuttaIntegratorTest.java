package org.apache.commons.math.ode.nonstiff;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.events.Event;
import org.apache.commons.math3.ode.events.When;
import org.apache.commons.math3.ode.nonstiff.EmbeddedRungeKuttaIntegrator;
import org.apache.commons.math3.ode.nonstiff.RungeKuttaStepInterpolator;
import org.apache.commons.math3.ode.sampling.StepHandler;
import org.apache.commons.math3.ode.sampling.StepInterpolator;
import org.junit.Test;

/**
 * Test cases for {@link EmbeddedRungeKuttaIntegrator}.
 */
public class EmbedgedRungeKuttaIntegratorTest {

    @Test
    public void testConstructor() {
        // Test constructor with default arguments
        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("RK4", false,
                new double[] { 0.25, 0.692, 0.327, 1.0 },
                new double[][] { { 0.25, 0 }, { 0.692, 0.6 }, { 0.327, 0.2764 }, { 1.0, 0.4366 } },
                new double[] { 0.25, 0.6, 0.327, 1.0 }, new RungeKuttaStepInterpolator());
        assertEquals("RK4", integrator.getName());
        assertFalse(integrator.isFsal());
        assertArrayEquals(new double[] { 0.25, 0.692, 0.327, 1.0 }, integrator.getC(), 0.0);
        assertArrayEquals(new double[][] { { 0.25, 0 }, { 0.692, 0.6 }, { 0.327, 0.2764 }, { 1.0, 0.4366 } },
                integrator.getA(), 0.0);
        assertArrayEquals(new double[] { 0.25, 0.6, 0.327, 1.0 }, integrator.getB(), 0.0);
        assertEquals(0.5, integrator.getExp(), 1e-15);
        assertArrayEquals(new double[] { 0.9, 0.2, 0.2, 1.0 }, integrator.getSafety(), 0.0);
        assertArrayEquals(new double[] { 0.2, 0.5 }, integrator.getMinReduction(), 0.0);
        assertArrayEquals(new double[] { 10.0 }, integrator.getMaxGrowth(), 0.0);

        // Test constructor with custom tolerances
        integrator = new EmbeddedRungeKuttaIntegrator("RK4", false,
                new double[] { 0.25, 0.692, 0.327, 1.0 },
                new double[][] { { 0.25, 0 }, { 0.692, 0.6 }, { 0.327, 0.2764 }, { 1.0, 0.4366 } },
                new double[] { 0.25, 0.6, 0.327, 1.0 }, new RungeKuttaStepInterpolator(),
                0.1, 0.001, new double[] { 1e-5, 1e-5, 1e-5, 1e-5 },
                new double[] { 1e-5, 1e-5, 1e-5, 1e-5 });
        assertEquals("RK4", integrator.getName());
        assertFalse(integrator.isFsal());
        assertArrayEquals(new double[] { 0.25, 0.692, 0.327, 1.0 }, integrator.getC(), 0.0);
        assertArrayEquals(new double[][] { { 0.25, 0 }, { 0.692, 0.6 }, { 0.327, 0.2764 }, { 1.0, 0.4366 } },
                integrator.getA(), 0.0);
        assertArrayEquals(new double[] { 0.25, 0.6, 0.327, 1.0 }, integrator.getB(), 0.0);
        assertEquals(0.5, integrator.getExp(), 1e-15);
        assertArrayEquals(new double[] { 0.9, 0.2, 0.2, 1.0 }, integrator.getSafety(), 0.0);
        assertArrayEquals(new double[] { 0.2, 0.5 }, integrator.getMinReduction(), 0.0);
        assertArrayEquals(new double[] { 10.0 }, integrator.getMaxGrowth(), 0.0);
        assertArrayEquals(new double[] { 1e-5, 1e-5, 1e-5, 1e-5 }, integrator.getAbsTols(), 0.0);
        assertArrayEquals(new double[] { 1e-5, 1e-5, 1e-5, 1e-5 }, integrator.getRelTols(), 0.0);
    }

    @Test
    public void testGetOrder() {
        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("RK4", false,
                new double[] { 0.25, 0.692, 0.327, 1.0 },
                new double[][] { { 0.25, 0 }, { 0.692, 0.6 }, { 0.327, 0.2764 }, { 1.0, 0.4366 } },
                new double[] { 0.25, 0.6, 0.327, 1.0 }, new RungeKuttaStepInterpolator());
        assertEquals(4, integrator.getOrder(), 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidConstructor() {
        new EmbeddedRungeKuttaIntegrator(null, true, new double[0], null, null, null);
    }

    @Test
    public void testIntegrateBasic() throws Exception {
        double[] x0 = new double[] { 0.0 };
        double[] y0 = new double[] { 1.0 };
        double tStart = 0.0;
        double tEnd = 1.0;

        FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public void computeDerivative(double t, double[] y, double[] yPrime) {
                yPrime[0] = y[0] * 2;
            }
        };

        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("RK4", false,
                new double[] { 0.25, 0.692, 0.327, 1.0 },
                new double[][] { { 0.25, 0 }, { 0.692, 0.6 }, { 0.327, 0.2764 }, { 1.0, 0.4366 } },
                new double[] { 0.25, 0.6, 0.327, 1.0 }, new RungeKuttaStepInterpolator());

        double[] y = new double[1];
        StepInterpolator interpolator = new RungeKuttaStepInterpolator();

        integrator.integrate(equations, tStart, y0, tEnd, y);

        assertArrayEquals(y0, y, 0.0);
        assertEquals(1.0, y[0], 1e-15);
    }

    @Test
    public void testIntegrateStepSizeControl() throws Exception {
        double[] x0 = new double[] { 0.0 };
        double[] y0 = new double[] { 1.0 };
        double tStart = 0.0;
        double tEnd = 1.0;

        FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public void computeDerivative(double t, double[] y, double[] yPrime) {
                yPrime[0] = Math.exp(y[0]);
            }
        };

        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("RK5", true,
                new double[] { 0.2, 0.375, 0.45, 0.55, 0.8 },
                new double[][] { { 0.2, 0 }, { 0.375, 0.45 }, { 0.45, 0.6 }, { 0.55, 0.5 }, { 0.8, 1.0 } },
                new double[] { 0.2, 0.375, 0.45, 0.55, 0.8 }, new RungeKuttaStepInterpolator());

        integrator.setSafety(0.5);
        integrator.setMinReduction(0.1);
        integrator.setMaxGrowth(10.0);

        double[] y = new double[1];
        StepInterpolator interpolator = new RungeKuttaStepInterpolator();

        integrator.integrate(equations, tStart, y0, tEnd, y);

        assertEquals(1.0, y[0], 1e-15);
    }

    @Test
    public void testIntegrateWithEvents() throws Exception {
        double[] x0 = new double[] { 0.0 };
        double[] y0 = new double[] { 1.0 };
        double tStart = 0.0;
        double tEnd = 2.0;

        FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public void computeDerivative(double t, double[] y, double[] yPrime) {
                yPrime[0] = y[0];
            }

            @Override
            public int getStateSize() {
                return 1;
            }
        };

        Event event = new When(1.5, new Event() {
            @Override
            public void evaluateEvent(double t, double[] y, double[] yDot, double[] data) {
                yDot[0] = 0.0;
            }
        });

        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("RK4", false,
                new double[] { 0.25, 0.692, 0.327, 1.0 },
                new double[][] { { 0.25, 0 }, { 0.692, 0.6 }, { 0.327, 0.2764 }, { 1.0, 0.4366 } },
                new double[] { 0.25, 0.6, 0.327, 1.0 }, new RungeKuttaStepInterpolator());

        integrator.setStepHandlers(new StepHandler[] { new StepHandler() {
            @Override
            public void handleStep(StepInterpolator interpolator, int stepNumber) {
                if (stepNumber >= 2) {
                    throw new MaxCountExceededException("Maximum number of steps reached");
                }
            }
        }});

        integrator.addEventHandlers(event);

        double[] y = new double[1];
        StepInterpolator interpolator = new RungeKuttaStepInterpolator();

        integrator.integrate(equations, tStart, y0, tEnd, y);

        assertArrayEquals(new double[] { 1.0 }, y, 0.0);

        // The event should have been triggered at least once
        assertTrue(event.isSet());

        // The derivative should be reset to zero after the event
        assertArrayEquals(new double[] { 0.0 }, y, 0.0);
    }

    @Test
    public void testIntegrateWithInvalidStepSize() throws Exception {
        double[] x0 = new double[] { 0.0 };
        double[] y0 = new double[] { 1.0 };
        double tStart = 0.0;
        double tEnd = 1.0;

        FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public void computeDerivative(double t, double[] y, double[] yPrime) {
                yPrime[0] = y[0] * 2;
            }
        };

        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("RK4", false,
                new double[] { 0.25, 0.692, 0.327, 1.0 },
                new double[][] { { 0.25, 0 }, { 0.692, 0.6 }, { 0.327, 0.2764 }, { 1.0, 0.4366 } },
                new double[] { 0.25, 0.6, 0.327, 1.0 }, new RungeKuttaStepInterpolator());

        integrator.setStepSize(0.0);

        double[] y = new double[1];

        try {
            integrator.integrate(equations, tStart, y0, tEnd, y);
            fail("Exception expected");
        } catch (NumberIsTooSmallException e) {
            // expected
        }
    }

}