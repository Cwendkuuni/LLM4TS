package org.apache.commons.math.ode.nonstiff.test;

import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.events.Event;
import org.apache.commons.math3.ode.events.When;
import org.apache.commons.math3.ode.nonstiff.EmbeddedRungeKuttaIntegrator;
import org.apache.commons.math3.ode.nonstiff.RK4Integrator;
import org.apache.commons.math3.ode.sampling.StepHandler;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class EmbeddedRungeKuttaIntegratorTest {

    private EmbeddedRungeKuttaIntegrator integrator;
    private FirstOrderDifferentialEquations equations;

    @Before
    public void setUp() {
        equations = new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yPrime) {
                yPrime[0] = y[0] * y[0];
            }

            @Override
            public void setEquations(FirstOrderDifferentialEquations equations) {
                // do nothing
            }
        };

        integrator = new RK4Integrator("RK4", true, // fsal
                new double[] { 0.25, 0.45, 0.25, 0.05 }, // c
                new double[][] { { 0, 0.25, 0.5, 0.875 },
                                { 0, 0.45, 0.875, 0.6 },
                                { 0, 1, 0.9, 0.65 },
                                { 0.25, 0.45, 0.6, 0.275 } }, // a
                new double[] { 0.25, 0.45, 0.45, 0.275 }, // b
                new RK4Integrator.DenseOutputStepInterpolator(), // prototype
                0.001, 0.01, // minStep, maxStep
                1e-9, 1e-8); // scalAbsoluteTolerance, scalRelativeTolerance
    }

    @Test
    public void testIntegrate() {
        double y0 = 1.0;
        double t0 = 0.0;
        double t1 = 1.0;
        double[] y1 = new double[1];

        double result = integrator.integrate(equations, t0, new double[]{ y0 }, t1, y1);

        Assert.assertEquals(t1, result, 1e-10);
        Assert.assertTrue(y1[0] > 0);
    }

    @Test(expected = MathIllegalArgumentException.class)
    public void testIntegrateWithNegativeStepSize() {
        integrator.setMinStep(-0.01);
        integrator.integrate(equations, 0.0, new double[]{ 1.0 }, 1.0, new double[1]);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testIntegrateWithTooSmallStepSize() {
        integrator.setMinStep(0.0);
        integrator.integrate(equations, 0.0, new double[]{ 1.0 }, 1.0, new double[1]);
    }

    @Test
    public void testIntegrateWithEvent() {
        double[] y0 = new double[]{ 1.0 };
        double t0 = 0.0;
        double t1 = 1.0;

        StepHandler eventHandler = new StepHandler() {
            @Override
            public void handleStep(StepHandlerContext context, int index) {
                if (context.getStepNumber() == 2) {
                    throw new EventOccurredException();
                }
            }
        };

        integrator.addStepHandler(eventHandler);

        integrator.setEventsManager(new CombinedEventsManager() {
            @Override
            public Event getNextEvent(double t, double[] y) {
                return new When(t, 1.2) {
                    @Override
                    public void handleEvent(double t, double[] y) {
                        // do nothing
                    }
                };
            }
        });

        try {
            integrator.integrate(equations, t0, y0, t1, null);
            Assert.fail();
        } catch (EventOccurredException e) {
            // expected
        }
    }
}