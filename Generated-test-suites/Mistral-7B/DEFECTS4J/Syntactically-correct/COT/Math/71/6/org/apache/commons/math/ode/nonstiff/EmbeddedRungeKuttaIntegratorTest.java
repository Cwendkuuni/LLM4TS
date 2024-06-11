package org.apache.commons.math.ode.nonstiff;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.NumberFormatException;
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.ModelAdapter;
import org.apache.commons.math3.ode.events.DefaultValueEvent;
import org.apache.commons.math3.ode.events.Event;
import org.apache.commons.math3.ode.events.ValueEvent;
import org.apache.commons.math3.ode.sampling.StepHandler;
import org.apache.commons.math3.ode.sampling.StepInterpolator;
import org.apache.commons.math3.ode.util.ODETestUtils;
import org.apache.commons.math3.special.BesselFunctions;
import org.apache.commons.math3.util.Arrays;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;

/**
 * Test suite for {@link EmbeddedRungeKuttaIntegrator}.
 */
public class EmbeddedRungeKuttaIntegratorTest {

    /**
     * Test constructor without exception.
     */
    @Test
    public void testConstructor() {
        // Create a new instance
        EmbeddedRungeKuttaIntegrator integrator = new RungeKuttaFehlberg78Method();
        assert (integrator != null);
    }

    /**
     * Test getOrder() method.
     */
    @Test
    public void testGetOrder() {
        // Create a new instance
        EmbeddedRungeKuttaIntegrator integrator = new RungeKuttaFehlberg78Method();
        int order = integrator.getOrder();
        // Check the result
        assertEquals(7, order);
    }

    /**
     * Test getSafety() method.
     */
    @Test
    public void testGetSafety() {
        // Create a new instance
        EmbeddedRungeKuttaIntegrator integrator = new RungeKuttaFehlberg78Method();
        double safety = integrator.getSafety();
        // Check the result
        assertEquals(0.9, safety, 0.001);
    }

    /**
     * Test setSafety() method.
     */
    @Test
    public void testSetSafety() {
        // Create a new instance
        EmbeddedRungeKuttaIntegrator integrator = new RungeKuttaFehlberg78Method();
        // Set the safety factor
        integrator.setSafety(0.8);
        double safety = integrator.getSafety();
        // Check the result
        assertEquals(0.8, safety, 0.001);
    }

    /**
     * Test getMinReduction() method.
     */
    @Test
    public void testGetMinReduction() {
        // Create a new instance
        EmbeddedRungeKuttaIntegrator integrator = new RungeKuttaFehlberg78Method();
        double minReduction = integrator.getMinReduction();
        // Check the result
        assertEquals(0.2, minReduction, 0.001);
    }

    /**
     * Test setMinReduction() method.
     */
    @Test
    public void testSetMinReduction() {
        // Create a new instance
        EmbeddedRungeKuttaIntegrator integrator = new RungeKuttaFehlberg78Method();
        // Set the minimal reduction factor
        integrator.setMinReduction(0.3);
        double minReduction = integrator.getMinReduction();
        // Check the result
        assertEquals(0.3, minReduction, 0.001);
    }

    /**
     * Test getMaxGrowth() method.
     */
    @Test
    public void testGetMaxGrowth() {
        // Create a new instance
        EmbeddedRungeKuttaIntegrator integrator = new RungeKuttaFehlberg78Method();
        double maxGrowth = integrator.getMaxGrowth();
        // Check the result
        assertEquals(10.0, maxGrowth, 0.001);
    }

    /**
     * Test setMaxGrowth() method.
     */
    @Test
    public void testSetMaxGrowth() {
        // Create a new instance
        EmbeddedRungeKuttaIntegrator integrator = new RungeKuttaFehlberg78Method();
        // Set the maximal growth factor
        integrator.setMaxGrowth(15.0);
        double maxGrowth = integrator.getMaxGrowth();
        // Check the result
        assertEquals(15.0, maxGrowth, 0.001);
    }

    /**
     * Test integrate() method with simple differential equation.
     */
    @Test
    public void testIntegrateSimple() {
        // Define a simple differential equation
        double[] y0 = new double[] {1.0, 0.0};
        FirstOrderDifferentialEquations equations = new ModelAdapter() {
            @Override
            public int getDimension() {
                return y0.length;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] dy) {
                for (int i = 0; i < y.length; i++) {
                    dy[i] = y[i] * 1.0;
                }
            }
        };
        // Set the integration range
        double t0 = 0.0;
        double tFinal = 1.0;
        // Create a new instance
        EmbeddedRungeKuttaIntegrator integrator = new RungeKuttaFehlberg78Method();
        // Integrate the differential equation
        double result = integrator.integrate(equations, t0, y0, tFinal, new double[y0.length]);
        // Check the result
        double[] yExpected = new double[] {1.278374879532133557824715595331, 1.278374879532133557824715595331};
        Arrays.assertEquals(yExpected, y0, 1e-8);
        assertEquals(1.0, result, 1e-8);
    }

    /**
     * Test integrate() method with differential equation with event.
     */
    @Test
    public void testIntegrateWithEvent() {
        // Define a differential equation with event
        double[] y0 = new double[] {1.0, 0.0};
        final double eventTime = 1.0;
        FirstOrderDifferentialEquations equations = new ModelAdapter() {
            @Override
            public int getDimension() {
                return y0.length;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] dy) {
                for (int i = 0; i < y.length; i++) {
                    dy[i] = y[i] * 1.0;
                }
            }

            @Override
            public void addEvent(StepHandler handler, double t, double[] y, double[] yPrime, double inflectionPoint) {
                handler.handleEvent(new ValueEvent(t, this, y));
            }
        };
        equations.addEvent(new StepHandler() {
            @Override
            public void handleEvent(Event event) {
                if (event instanceof ValueEvent) {
                    ValueEvent ve = (ValueEvent) event;
                    double[] y = ve.getValues();
                    double[] yPrime = ve.getYPrime();
                    double t = ve.getTime();
                    System.out.println("Event detected at time: " + t);
                    assertEquals(eventTime, t, 1e-8);
                    Arrays.assertEquals(new double[] {1.7320508075688772935274467522831, 1.7320508075688772935274467522831}, y, 1e-8);
                    Arrays.assertEquals(new double[] {0.0, 0.0}, yPrime, 1e-8);
                }
            }
        }, new DefaultValueEvent(1.0, equations, y0));
        // Set the integration range
        double t0 = 0.0;
        double tFinal = 2.0;
        // Create a new instance
        EmbeddedRungeKuttaIntegrator integrator = new RungeKuttaFehlberg78Method();
        // Integrate the differential equation
        integrator.integrate(equations, t0, y0, tFinal, new double[y0.length]);
    }

    /**
     * Test integrate() method with negative step size.
     */
    @Test(expected = MathIllegalArgumentException.class)
    public void testIntegrateNegativeStepSize() {
        // Define a simple differential equation
        double[] y0 = new double[] {1.0, 0.0};
        FirstOrderDifferentialEquations equations = new ModelAdapter() {
            @Override
            public int getDimension() {
                return y0.length;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] dy) {
                for (int i = 0; i < y.length; i++) {
                    dy[i] = y[i] * 1.0;
                }
            }
        };
        // Set the integration range
        double t0 = 0.0;
        double tFinal = 1.0;
        // Create a new instance
        EmbeddedRungeKuttaIntegrator integrator = new RungeKuttaFehlberg78Method();
        // Attempt to integrate with negative step size
        integrator.integrate(equations, t0, y0, tFinal, new double[] {-0.1, 0.0});
    }

    /**
     * Test integrate() method with large step size.
     */
    @Test(expected = MathIllegalArgumentException.class)
    public void testIntegrateLargeStepSize() {
        // Define a simple differential equation
        double[] y0 = new double[] {1.0, 0.0};
        FirstOrderDifferentialEquations equations = new ModelAdapter() {
            @Override
            public int getDimension() {
                return y0.length;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] dy) {
                for (int i = 0; i < y.length; i++) {
                    dy[i] = y[i] * 1.0;
                }
            }
        };
        // Set the integration range
        double t0 = 0.0;
        double tFinal = 1.0;
        // Create a new instance
        EmbeddedRungeKuttaIntegrator integrator = new RungeKuttaFehlberg78Method();
        // Attempt to integrate with large step size
        integrator.setMinStep(1e20);
        integrator.integrate(equations, t0, y0, tFinal, new double[y0.length]);
    }

    /**
     * Test integrate() method with non-finite step size.
     */
    @Test(expected = NumberFormatException.class)
    public void testIntegrateNonFiniteStepSize() {
        // Define a simple differential equation
        double[] y0 = new double[] {1.0, 0.0};
        FirstOrderDifferentialEquations equations = new ModelAdapter() {
            @Override
            public int getDimension() {
                return y0.length;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] dy) {
                for (int i = 0; i < y.length; i++) {
                    dy[i] = y[i] * 1.0;
                }
            }
        };
        // Set the integration range
        double t0 = 0.0;
        double tFinal = 1.0;
        // Create a new instance
        EmbeddedRungeKuttaIntegrator integrator = new RungeKuttaFehlberg78Method();
        // Attempt to integrate with non-finite step size
        integrator.setMinStep("NaN");
        integrator.integrate(equations, t0, y0, tFinal, new double[y0.length]);
    }

    /**
     * Test estimateError() method.
     */
    @Test
    public void testEstimateError() {
        // Create a new instance
        EmbeddedRungeKuttaIntegrator integrator = new RungeKuttaFehlberg78Method();
        double[] yDotK = new double[11][2];
        double[] y = new double[] {1.0, 1.0};
        double[] y1 = new double[] {1.1, 1.1};
        double h = 0.02;
        // Set up the integrator
        integrator.setMinStep(h);
        integrator.resetInternalState();
        // Compute the derivatives for the first step
        integrator.computeDerivatives(0.0, y, yDotK[0]);
        // Compute the error ratio
        double errorRatio = integrator.estimateError(yDotK, y, y1, h);
        // Check the result
        assertEquals(1.006872935283140898338074811683, errorRatio, 1e-11);
    }

}