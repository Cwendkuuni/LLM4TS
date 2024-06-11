package org.apache.commons.math.ode.nonstiff;

import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.events.Event;
import org.apache.commons.math3.ode.events.When;
import org.apache.commons.math3.ode.nonstiff.EmbeddedRungeKuttaIntegrator;
import org.apache.commons.math3.ode.nonstiff.RungeKuttaEventAdapter;
import org.apache.commons.math3.ode.nonstiff.RungeKuttaStepInterpolator;
import org.apache.commons.math3.ode.sampling.StepHandler;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Test the EmbeddedRungeKuttaIntegrator class.
 */
public class EmbeddedRungeKuttaIntegratorTest {

    private EmbeddedRungeKuttaIntegrator integrator;
    private FirstOrderDifferentialEquations equations;

    @Before
    public void setUp() throws Exception {
        integrator = new RungeKuttaFehlberg78MethodIntegrator(2.0, new RungeKuttaStepInterpolator());
        equations = new SimpleDifferentialEquations();
    }

    @Test
    public void testIntegrate() throws Exception {
        // Basic functionality test
        double[] y0 = {1.0, 1.0};
        double t0 = 0.0;
        double t1 = 1.0;
        double[] y1 = {e, e}; // e is Euler's number, the solution for this equation
        integrator.integrate(equations, t0, y0, t1, new double[y0.length]);
        Assert.assertArrayEquals(y1, y0);

        // Test with a custom step handlers
        StepHandler stepHandler = new StepHandler() {
            @Override
            public void handleStep(StepHandler handler, int stepNumber) {
                System.out.println("Step number: " + stepNumber);
            }
        };
        integrator.setStepHandlers(stepHandler);
        integrator.integrate(equations, t0, y0, t1, new double[y0.length]);

        // Test with events
        equations.addEvent(new When(1.5, new RungeKuttaEventAdapter() {
            @Override
            public int eventType() {
                return Event.TYPE_BREAKPOINT;
            }

            @Override
            public void action(double time, double[] state, double[] derivatives) {
                throw new MaxCountExceededException();
            }
        }));
        try {
            integrator.integrate(equations, 0.0, new double[2], 2.0, new double[2]);
            Assert.fail();
        } catch (NumberIsTooSmallException e) {
            // Expected
        }

        // Test with custom tolerance
        integrator = new RungeKuttaFehlberg78MethodIntegrator(2.0, new RungeKuttaStepInterpolator()) {
            @Override
            protected double estimateError(double[][] yDotK, double[] y0, double[] y1, double h) {
                // Custom error estimation logic
                return 1.1;
            }
        };
        integrator.setAbsTolerance(1e-5);
        integrator.integrate(equations, 0.0, new double[2], 1.0, new double[2]);
        Assert.assertTrue(integrator.hasConverged());
    }

}

class SimpleDifferentialEquations implements FirstOrderDifferentialEquations {
    @Override
    public int getDimension() {
        return 2;
    }

    @Override
    public void computeDerivatives(double t, double[] y, double[] yPrime) {
        yPrime[0] = y[1];
        yPrime[1] = -y[0];
    }
}