package org.apache.commons.math.ode.nonstiff.test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.events.Event;
import org.apache.commons.math3.ode.events.When;
import org.apache.commons.math3.ode.sampling.StepHandler;
import org.apache.commons.math3.ode.test.AbstractODETest;
import org.junit.Test;
import org.apache.commons.math.ode.nonstiff.RungeKuttaIntegrator;
import org.apache.commons.math.ode.nonstiff.RungeKuttaIntegratorTest;
import org.apache.commons.math.ode.nonstiff.RungeKuttaStepInterpolator;

/**
 * Tests for {@link RungeKuttaIntegrator}.
 */
public class RungeKuttaIntegratorTest extends AbstractODETest {

    /**
     * Constructs the test case.
     */
    public RungeKuttaIntegratorTest() {
        super();
    }

    /**
     * Test constructor.
     */
    @Test
    public void testConstructor() {
        final RungeKuttaIntegrator integrator = new RungeKuttaIntegrator("RK4", new double[] { 0.25, 0.25, 0.5, 0.5 },
                                                                         new double[][] { { 1.0 / 6.0, 1.0 / 2.0, 1.0 / 2.0, 1.0 },
                                                                                          { 1.0 / 6.0, 1.0 / 2.0, 1.0 / 2.0, 0.0 },
                                                                                          { 1.0 / 6.0, 0.0, 1.0 / 2.0, 1.0 / 6.0 },
                                                                                          { 1.0 / 6.0, 0.0, 1.0 / 2.0, 1.0 / 6.0 },
                                                                                          { 1.0, 0.0, 0.0, 0.0 } },
                                                                         new double[] { 1.0 / 6.0, 1.0 / 2.0, 1.0 / 2.0, 1.0 / 6.0 });
        assertNotNull(integrator);
        assertEquals("RK4", integrator.getName());
        assertEquals(new double[] { 0.25, 0.25, 0.5, 0.5 }, integrator.getC());
        assertEquals(new double[][] { { 1.0 / 6.0, 1.0 / 2.0, 1.0 / 2.0, 1.0 },
                                      { 1.0 / 6.0, 1.0 / 2.0, 1.0 / 2.0, 0.0 },
                                      { 1.0 / 6.0, 0.0, 1.0 / 2.0, 1.0 / 6.0 },
                                      { 1.0 / 6.0, 0.0, 1.0 / 2.0, 1.0 / 6.0 },
                                      { 1.0, 0.0, 0.0, 0.0 } }, integrator.getA());
        assertEquals(new double[] { 1.0 / 6.0, 1.0 / 2.0, 1.0 / 2.0, 1.0 / 6.0 }, integrator.getB());
        assertEquals(RungeKuttaStepInterpolator.class, integrator.getPrototype().getClass());
        assertEquals(1.0, integrator.getStep(), 0.0);
    }

    /**
     * Test method {@link RungeKuttaIntegrator#integrate(FirstOrderDifferentialEquations, double, double[], double, double[])}.
     */
    @Test
    public void testIntegrate() {
        final double[] initY = new double[] { 1.0 };
        final double[] expectedY = new double[] { 1.5, 1.75, 1.984, 2.0 };
        final double[] tempY = new double[4];
        final FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {

            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] dy) {
                dy[0] = y[0] * 0.5;
            }
        };

        final RungeKuttaIntegrator integrator = new RungeKuttaIntegrator("RK4", new double[] { 0.25, 0.25, 0.5, 0.5 },
                                                                         new double[][] { { 1.0 / 6.0, 1.0 / 2.0, 1.0 / 2.0, 1.0 },
                                                                                          { 1.0 / 6.0, 1.0 / 2.0, 1.0 / 2.0, 0.0 },
                                                                                          { 1.0 / 6.0, 0.0, 1.0 / 2.0, 1.0 / 6.0 },
                                                                                          { 1.0 / 6.0, 0.0, 1.0 / 2.0, 1.0 / 6.0 },
                                                                                          { 1.0, 0.0, 0.0, 0.0 } },
                                                                         new double[] { 1.0 / 6.0, 1.0 / 2.0, 1.0 / 2.0, 1.0 / 6.0 });

        assertEquals(1.0, integrator.integrate(equations, 0.0, initY, 1.0, tempY)[0], 0.0);
        assertArrayEquals(expectedY, tempY, 0.01);

        // Test with events
        final double eventTime = 0.5;
        final double eventY = 2.0;
        final StepHandler stepHandler = new StepHandler() {

            @Override
            public void handleStep(StepHandlerContext context, int stepNumber) {
                if (context.isLastStep()) {
                    assertArrayEquals(new double[] { 2.0 }, context.getY(), 0.0);
                    assertTrue(context.isStepAccepted());
                } else {
                    assertNotNull(context.getDerivatives());
                }
            }

            @Override
            public void handleStepStart(StepHandlerContext context) {
                // not used
            }

            @Override
            public void handleStepEnd(StepHandlerContext context) {
                // not used
            }
        };
        integrator.addStepHandler(stepHandler);
        final Event event = new When(eventTime) {

            @Override
            public boolean evaluateStep(double t, double[] y) {
                return Arrays.equals(y, new double[] { eventY });
            }

            @Override
            public void handleEvent(double t, double[] y) {
                // not used
            }
        };
        integrator.addEvent(event);
        integrator.integrate(equations, 0.0, initY, 1.5, tempY);
        assertFalse(integrator.isLastStep());
        assertArrayEquals(new double[] { 1.75 }, tempY, 0.001);
        integrator.integrate(equations, 1.5, tempY, eventTime, tempY);
        assertTrue(integrator.isLastStep());
        assertArrayEquals(new double[] { 2.0 }, tempY, 0.0);
        assertNull(equations);
        assertNull(initY);
        assertNull(tempY);
        integrator = null;
    }

    /**
     * Test method {@link RungeKuttaIntegrator#integrate(FirstOrderDifferentialEquations, double, double[], double, double[])}.
     */
    @Test(expected = IntegratorException.class)
    public void testIntegrateException() {
        final double[] initY = new double[] { 1.0 };
        final FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {

            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] dy) {
                throw new MathIllegalArgumentException("Test exception");
            }
        };

        final RungeKuttaIntegrator integrator = new RungeKuttaIntegrator("RK4", new double[] { 0.25, 0.25, 0.5, 0.5 },
                                                                         new double[][] { { 1.0 / 6.0, 1.0 / 2.0, 1.0 / 2.0, 1.0 },
                                                                                          { 1.0 / 6.0, 1.0 / 2.0, 1.0 / 2.0, 0.0 },
                                                                                          { 1.0 / 6.0, 0.0, 1.0 / 2.0, 1.0 / 6.0 },
                                                                                          { 1.0 / 6.0, 0.0, 1.0 / 2.0, 1.0 / 6.0 },
                                                                                          { 1.0, 0.0, 0.0, 0.0 } },
                                                                         new double[] { 1.0 / 6.0, 1.0 / 2.0, 1.0 / 2.0, 1.0 / 6.0 });

        integrator.integrate(equations, 0.0, initY, 1.0, new double[4]);
    }
}