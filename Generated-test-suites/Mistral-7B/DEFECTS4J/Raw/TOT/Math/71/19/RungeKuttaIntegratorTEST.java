package org.apache.commons.math.ode.nonstiff.test;

import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.events.RelativetimeEvent;
import org.apache.commons.math3.ode.nonstiff.RungeKuttaIntegrator;
import org.apache.commons.math3.ode.sampling.StepHandler;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests for {@link RungeKuttaIntegrator}.
 */
public class RungeKuttaIntegratorTest {

    /**
     * Test integration of a simple system of ODEs.
     */
    @Test
    public void testSimpleIntegration() {
        final double[][] coefficients = {
            {0.0, 0.0, 0.5, 0.5},
            {0.5, 0.0, 0.0, 0.5},
            {0.5, 0.5, 0.0, 0.0}
        };
        final double[] initialValues = {1.0, 1.0};
        final double stepSize = 0.1;
        final double startTime = 0.0;
        final double endTime = 2.0;
        final RungeKuttaIntegrator integrator = new ClassicalRungeKuttaIntegrator(3, coefficients, new DummyStepInterpolator(), stepSize);

        assertEquals("Name mismatch.", "ClassicalRungeKuttaIntegrator", integrator.getName());
        assertEquals("Step size mismatch.", stepSize, integrator.getStep(), 1e-12);

        // test integration
        double[] result = integrator.integrate(new SimpleDifferentialEquations(), startTime, initialValues, endTime);

        // check results
        double[] expected = {3.0, 3.0};
        assertArrayEquals("Results mismatch.", expected, result, 1e-12);

        // test integration with events handling
        final StepHandler stepHandler = new StepHandler() {
            @Override
            public void handleStep(StepHandlerContext context, int stepNumber) {
                if (context.getStepNumber() % 10 == 0) {
                    context.setStopRequested(true);
                }
            }
        };
        integrator.addStepHandler(stepHandler);
        integrator.setEventsManager(new CombinedEventsManager());
        integrator.addEvent(new RelativetimeEvent(1.5, 1.0));

        result = integrator.integrate(new SimpleDifferentialEquations(), startTime, initialValues, endTime, null);
        assertTrue("Integration should have stopped.", integrator.isStopRequested());
        assertEquals("Stop time mismatch.", 1.5, integrator.getStepStart(), 1e-12);

        // test integration with non-positive step size
        try {
            final RungeKuttaIntegrator integrator2 = new ClassicalRungeKuttaIntegrator(3, coefficients, new DummyStepInterpolator(), -stepSize);
            fail("Should have thrown exception.");
        } catch (NumberIsTooSmallException e) {
            // expected
        }

        // test integration with wrong number of coefficients
        try {
            final double[][] coefficients2 = {
                {0.0, 0.0, 0.5},
                {0.5, 0.0, 0.5},
                {0.5, 0.5, 0.0},
                {0.5, 0.0, 0.5}
            };
            final RungeKuttaIntegrator integrator3 = new ClassicalRungeKuttaIntegrator(3, coefficients2, new DummyStepInterpolator(), stepSize);
            fail("Should have thrown exception.");
        } catch (MathIllegalArgumentException e) {
            // expected
        }
    }

}

/**
 * A simple system of ODEs.
 */
class SimpleDifferentialEquations implements FirstOrderDifferentialEquations {

    @Override
    public int getNumberOfEquations() {
        return 2;
    }

    @Override
    public void computeDerivatives(double t, double[] y, double[] yPrime) {
        yPrime[0] = y[1];
        yPrime[1] = -y[0];
    }

}