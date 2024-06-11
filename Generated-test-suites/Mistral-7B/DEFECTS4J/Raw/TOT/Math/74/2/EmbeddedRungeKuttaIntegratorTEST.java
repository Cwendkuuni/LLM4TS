package org.apache.commons.math.ode.nonstiff;

import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.events.Event;
import org.apache.commons.math3.ode.events.When;
import org.apache.commons.math3.ode.sampling.RungeKuttaStepInterpolator;
import org.apache.commons.math3.ode.sampling.StepHandler;
import org.apache.commons.math3.ode.nonstiff.EmbeddedRungeKuttaIntegrator;
import org.junit.Test;
import org.junit.Assert;

public class EmbeddedRungeKuttaIntegratorTest {

    @Test
    public void testDormandPrince853() {
        EmbeddedRungeKuttaIntegrator integrator = new DormandPrince853Integrator(5);
        double[] initialValues = {0.0, 0.1};
        double stepSize = 0.1;
        double finalTime = 1.0;

        FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return initialValues.length;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] derivatives) {
                derivatives[0] = y[1];
                derivatives[1] = -y[0] / 2.0;
            }
        };

        double[] result = integrator.integrate(equations, 0.0, initialValues, finalTime, new double[initialValues.length]);

        double[] expected = {0.25, 0.0};
        Assert.assertArrayEquals(expected, result, 1e-8);

        // Check that the internal state has been reset
        Assert.assertArrayEquals(initialValues, integrator.getInitialValues(), 1e-8);
    }

    @Test
    public void testDormandPrince853WithEvents() {
        EmbeddedRungeKuttaIntegrator integrator = new DormandPrince853Integrator(5);
        double[] initialValues = {0.0, 0.1};
        double stepSize = 0.1;
        double finalTime = 1.0;

        FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return initialValues.length;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] derivatives) {
                derivatives[0] = y[1];
                derivatives[1] = -y[0] / 2.0;
            }
        };

        StepHandler stepHandler = new StepHandler() {
            @Override
            public void handleStep(StepHandlerContext context) {
                double[] step = context.getStep();
                Assert.assertEquals(step[0], stepSize, 1e-8);
            }
        };

        When event = new When(0.5) {
            @Override
            public void handleEvent(double t, double[] y, double[] derivatives) {
                Assert.assertArrayEquals(initialValues, y, 1e-8);
            }
        };

        integrator.addStepHandler(stepHandler);
        integrator.addEventHandler(event);

        try {
            integrator.integrate(equations, 0.0, initialValues, finalTime, new double[initialValues.length]);
            Assert.fail("Expected an IntegratorException due to the event at t = 0.5");
        } catch (IntegratorException e) {
            // Expected
        }

        // Check that the internal state has been reset
        Assert.assertArrayEquals(initialValues, integrator.getInitialValues(), 1e-8);
    }

    @Test(expected = MathIllegalArgumentException.class)
    public void testNullDimensions() {
        EmbeddedRungeKuttaIntegrator integrator = new DormandPrince853Integrator(null);
    }

    @Test(expected = MathIllegalArgumentException.class)
    public void testNegativeDimensions() {
        EmbeddedRungeKuttaIntegrator integrator = new DormandPrince853Integrator(-1);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testNegativeStepSize() {
        EmbeddedRungeKuttaIntegrator integrator = new DormandPrince853Integrator(5);
        double[] initialValues = {0.0, 0.1};
        double stepSize = -0.1;
        double finalTime = 1.0;

        FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return initialValues.length;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] derivatives) {
                derivatives[0] = y[1];
                derivatives[1] = -y[0] / 2.0;
            }
        };

        integrator.setStepSize(stepSize);
        integrator.integrate(equations, 0.0, initialValues, finalTime, new double[initialValues.length]);
    }
}