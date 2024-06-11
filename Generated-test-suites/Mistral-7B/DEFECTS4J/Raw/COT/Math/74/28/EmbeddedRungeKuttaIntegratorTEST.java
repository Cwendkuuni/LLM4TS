package org.apache.commons.math.ode.nonstiff.test;

import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.events.Event;
import org.apache.commons.math3.ode.events.When;
import org.apache.commons.math3.ode.nonstiff.EmbeddedRungeKuttaIntegrator;
import org.apache.commons.math3.ode.nonstiff.RungeKuttaStepInterpolator;
import org.apache.commons.math3.ode.sampling.StepHandler;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Test class for EmbeddedRungeKuttaIntegrator
 */
public class EmbeddedRungeKuttaIntegratorTest {

    private static final double ABS_TOL = 1E-10;
    private static final double REL_TOL = 1E-10;

    private EmbeddedRungeKuttaIntegrator integrator;
    private FirstOrderDifferentialEquations equations;
    private StepHandler stepHandler;

    @Before
    public void setup() {
        integrator = new EmbeddedRungeKuttaIntegrator("testIntegrator", true,
                new double[]{0.3, 0.5, 0.8}, new double[][]{{0.0, 0.25, 0.64},
                {0.0, 0.25, 0.64}, {0.0, 1.0, 1.0}}, new double[]{0.0, 0.5, 0.5},
                new RungeKuttaStepInterpolator(), 1.0, 10.0, ABS_TOL, REL_TOL) {
            @Override
            public int getOrder() {
                return 2;
            }

            @Override
            protected double estimateError(double[][] yDotK, double[] y0, double[] y1, double h) {
                double error = 0;
                for (int i = 0; i < y0.length; i++) {
                    error += Math.pow(Math.abs(yDotK[yDotK.length - 1][i] - yDotK[0][i]), 2);
                }
                return Math.sqrt(error);
            }
        };
        equations = new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yPrime) {
                yPrime[0] = y[0] + Math.sin(y[0]);
            }
        };
        stepHandler = new StepHandler() {
            @Override
            public void handleStep(StepHandler.InterpolatedValues values, int stepNumber) {
                if (stepNumber > 1) {
                    Assert.assertEquals(values.getY()[0], 2.0, 0.01);
                }
            }
        };
    }

    @Test
    public void testIntegrator() {
        Assert.assertEquals(0.0, integrator.integrate(equations, 0.0, new double[]{0.0}, 2.0, new double[1]));
    }

    @Test(expected = DerivativeException.class)
    public void testDerivativeException() {
        assertEquals(0.0, integrator.integrate(new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yPrime) {
                throw new DerivativeException("Test exception");
            }
        }, 0.0, new double[]{0.0}, 2.0, new double[1]));
    }

    @Test(expected = IntegratorException.class)
    public void testIntegratorException() {
        assertEquals(0.0, integrator.integrate(equations, 0.0, new double[]{0.0}, 2.0, new double[1], new CombinedEventsManager() {
            @Override
            public boolean evaluateStep(StepHandler handler) {
                throw new IntegratorException("Test exception");
            }
        }));
    }

    @Test
    public void testStepHandler() {
        integrator.integrate(equations, 0.0, new double[]{0.0}, 4.0, new double[1], stepHandler);
    }

    @Test
    public void testStepSizeControl() {
        integrator.setMinStep(0.1);
        integrator.setMaxStep(0.5);
        integrator.setMinReduction(0.1);
        integrator.setMaxGrowth(1.5);
        integrator.setSafety(0.9);
        assertEquals(0.0, integrator.integrate(equations, 0.0, new double[]{0.0}, 2.0, new double[1]));
    }

    @Test
    public void testEdgeCases() {
        integrator.setMinStep(0.0);
        integrator.setMaxStep(0.0);
        integrator.setMinReduction(0.0);
        integrator.setMaxGrowth(0.0);
        integrator.setSafety(0.0);
        assertEquals(0.0, integrator.integrate(equations, 0.0, new double[]{0.0}, 2.0, new double[1]));

        integrator.setMinStep(-1.0);
        integrator.setMaxStep(-1.0);
        integrator.setMinReduction(-1.0);
        integrator.setMaxGrowth(-1.0);
        integrator.setSafety(-1.0);
        try {
            integrator.integrate(equations, 0.0, new double[]{0.0}, 2.0, new double[1]);
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }

        integrator.setMinStep(1.0);
        integrator.setMaxStep(0.0);
        integrator.setMinReduction(1.0);
        integrator.setMaxGrowth(0.0);
        integrator.setSafety(1.0);
        try {
            integrator.integrate(equations, 0.0, new double[]{0.0}, 2.0, new double[1]);
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }

        integrator.setMinStep(0.0);
        integrator.setMaxStep(1.0);
        integrator.setMinReduction(0.0);
        integrator.setMaxGrowth(1.0);
        integrator.setSafety(0.0);
        try {
            integrator.integrate(equations, 0.0, new double[]{0.0}, 2.0, new double[1]);
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }

        integrator.setMinStep(0.0);
        integrator.setMaxStep(0.0);
        integrator.setMinReduction(1.0);
        integrator.setMaxGrowth(1.0);
        integrator.setSafety(0.0);
        try {
            integrator.integrate(equations, 0.0, new double[]{0.0}, 2.0, new double[1]);
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }

        integrator.setMinStep(0.0);
        integrator.setMaxStep(0.0);
        integrator.setMinReduction(0.0);
        integrator.setMaxGrowth(1.0);
        integrator.setSafety(1.0);
        try {
            integrator.integrate(equations, 0.0, new double[]{0.0}, 2.0, new double[1]);
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }

        integrator.setMinStep(0.0);
        integrator.setMaxStep(0.0);
        integrator.setMinReduction(0.0);
        integrator.setMaxGrowth(0.0);
        integrator.setSafety(1.1);
        try {
            integrator.integrate(equations, 0.0, new double[]{0.0}, 2.0, new double[1]);
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }

        integrator.setMinStep(0.0);
        integrator.setMaxStep(0.0);
        integrator.setMinReduction(0.1);
        integrator.setMaxGrowth(0.0);
        integrator.setSafety(0.0);
        try {
            integrator.integrate(equations, 0.0, new double[]{0.0}, 2.0, new double[1]);
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }

        integrator.setMinStep(0.0);
        integrator.setMaxStep(0.0);
        integrator.setMinReduction(0.0);
        integrator.setMaxGrowth(1.1);
        integrator.setSafety(0.0);
        try {
            integrator.integrate(equations, 0.0, new double[]{0.0}, 2.0, new double[1]);
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testEvents() {
        final double eventX = 1.5;
        integrator.setMinStep(0.1);
        integrator.setMaxStep(1.0);
        integrator.setMinReduction(0.1);
        integrator.setMaxGrowth(1.0);
        integrator.setSafety(0.9);

        CombinedEventsManager manager = new CombinedEventsManager();
        manager.addEvent(new When(eventX, new Event() {
            @Override
            public boolean occurs(double t, double[] y, double[] yPrime) {
                return true;
            }
        }));

        integrator.setEventsManager(manager);
        integrator.integrate(equations, 0.0, new double[]{0.0}, 3.0, new double[1]);
    }

}