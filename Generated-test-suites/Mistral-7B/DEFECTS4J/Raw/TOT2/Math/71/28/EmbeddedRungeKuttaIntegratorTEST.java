package org.apache.commons.math.ode.nonstiff.test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.events.Event;
import org.apache.commons.math3.ode.events.EventManager;
import org.apache.commons.math3.ode.events.When;
import org.apache.commons.math3.ode.sampling.StepHandler;
import org.apache.commons.math3.ode.sampling.StepInterpolator;
import org.apache.commons.math3.ode.tsanner.ClassicalRungeKuttaIntegrator;
import org.apache.commons.math3.ode.tsanner.DormandPrince54;
import org.apache.commons.math3.ode.tsanner.RungeKuttaStepInterpolator;
import org.apache.commons.math3.ode.tsanner.StepHandlerAdapter;
import org.apache.commons.math3.ode.tsanner.StepInterpolatorAdapter;
import org.apache.commons.math3.ode.nonstiff.EmbeddedRungeKuttaIntegrator;
import org.apache.commons.math3.ode.nonstiff.RungeKuttaIntegrator;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;

/**
 * Test for {@link EmbeddedRungeKuttaIntegrator} class.
 */
public class EmbeddedRungeKuttaIntegratorTest {

    /**
     * Test of the basic functionality of the integrator.
     */
    @Test
    public void testEmbeddedRungeKuttaIntegrator() {
        final double[][] coefficients = {
            {0.0, 0.0},
            {0.3, 0.47},
            {0.46875, 0.0146875},
            {0.25, 0.45},
            {0.625, 0.175},
            {1.0, 0.0}
        };
        final RungeKuttaStepInterpolator prototype = new DormandPrince54();
        final RungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("EmbeddedRK5(4,5)", true,
                coefficients[0], coefficients[1], coefficients[2], prototype,
                0.0, Double.POSITIVE_INFINITY, null, null);
        final FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
            @Override
            public int getDimensions() {
                return 1;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yPrime) {
                yPrime[0] = Math.sin(y[0]);
            }
        };

        final double initialCondition = Math.PI;
        final double[] result = integrator.integrate(equations, 0.0,
                new double[] {initialCondition}, 10.0, new double[1]);
        assertEquals(Math.E, result[0], Precision.SAFE_EPSILON);
    }

    /**
     * Test of the behaviour when the integrator is unable to take a step.
     */
    @Test(expected = MaxCountExceededException.class)
    public void testMaxCountExceededException() {
        final double[][] coefficients = {
            {0.0, 0.0},
            {0.3, 0.47},
            {0.46875, 0.0146875},
            {0.25, 0.45},
            {0.625, 0.175},
            {1.0, 0.0}
        };
        final RungeKuttaStepInterpolator prototype = new DormandPrince54();
        final RungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("EmbeddedRK5(4,5)", true,
                coefficients[0], coefficients[1], coefficients[2], prototype,
                0.0, Double.POSITIVE_INFINITY, null, null);
        final FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
            @Override
            public int getDimensions() {
                return 1;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yPrime) {
                yPrime[0] = -1.0 / (2.0 * Math.PI);
                if (Math.abs(y[0]) > 1.0) {
                    throw new MathIllegalArgumentException(
                            "y[0] = " + y[0] + " is out of bounds [0, 1]");
                }
            }
        };

        final double initialCondition = 1.1;
        integrator.integrate(equations, 0.0, new double[] {initialCondition}, 1.0, new double[1]);
    }

    /**
     * Test of the behaviour when the integrator receives an event.
     */
    @Test
    public void testEvent() {
        final double[][] coefficients = {
            {0.0, 0.0},
            {0.3, 0.47},
            {0.46875, 0.0146875},
            {0.25, 0.45},
            {0.625, 0.175},
            {1.0, 0.0}
        };
        final RungeKuttaStepInterpolator prototype = new DormandPrince54();
        final RungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("EmbeddedRK5(4,5)", true,
                coefficients[0], coefficients[1], coefficients[2], prototype,
                0.0, Double.POSITIVE_INFINITY, null, null) {
            @Override
            protected double estimateError(double[][] yDotK, double[] y0, double[] y1, double h) {
                final double error = super.estimateError(yDotK, y0, y1, h);
                if (Math.abs(y1[0] - y0[0]) < 1e-12) {
                    return Math.max(error, 1.1 * h);
                }
                return error;
            }
        };

        final double[][] events = {{2.0, new When(2.0), new StepHandlerAdapter() {
            @Override
            public void handleStep(StepInterpolator interpolator, int index) {
                interpolator.stopIntegration();
            }
        }}};
        final FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
            @Override
            public int getDimensions() {
                return 1;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yPrime) {
                yPrime[0] = 0.0;
            }
        };

        final double initialCondition = 0.0;
        integrator.integrate(equations, 0.0, new double[] {initialCondition}, 3.0, new double[1],
                events);
        assertFalse(integrator.isStepActive());
    }

}