package org.apache.commons.math.ode.nonstiff;

import static org.junit.Assert.*;

import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.events.Event;
import org.apache.commons.math3.ode.events.When;
import org.apache.commons.math3.ode.sampling.StepHandler;
import org.apache.commons.math3.ode.nonstiff.EmbeddedRungeKuttaIntegrator;
import org.apache.commons.math3.ode.nonstiff.EmbeddedRungeKuttaTest;
import org.apache.commons.math3.ode.nonstiff.RungeKuttaStepInterpolator;
import org.apache.commons.math3.ode.sampling.DummyStepInterpolator;
import org.junit.Before;
import org.junit.Test;

/**
 * Test cases for {@link EmbeddedRungeKuttaIntegrator}.
 */
public class EmbeddedRungeKuttaIntegratorTest extends EmbeddedRungeKuttaTest {

    private EmbeddedRungeKuttaIntegrator integrator;

    @Before
    public void setup() {
        integrator = new EmbeddedRungeKuttaClassic54Integrator();
    }

    @Test
    public void testIntegrate() {
        double t0 = 0.0;
        double[] y0 = {0.0, 1.0};
        double t = 1.0;
        double[] y = {1.0, 0.0};

        double result = integrator.integrate(createEquations(), t0, y0, t, y);
        assertEquals(t, result, 1e-15);
        assertArrayEquals(y, y0, 1e-15);
    }

    @Test(expected = IntegratorException.class)
    public void testIntegrateWithEvent() {
        double t0 = 0.0;
        double[] y0 = {0.0, 0.0};
        double t = 1.0;
        double[] y = {1.0, 1.0};

        FirstOrderDifferentialEquations equations = createEquations();
        equations.addEvent(new When(1.0) {
            @Override
            public void handleEvent(double t, double[] y) {
                throw new RuntimeException();
            }
        });

        integrator.integrate(equations, t0, y0, t, y);
    }

    @Test(expected = DerivativeException.class)
    public void testIntegrateWithDerivativeException() {
        double t0 = 0.0;
        double[] y0 = {0.0, 0.0};
        double t = 1.0;
        double[] y = {1.0, 1.0};

        FirstOrderDifferentialEquations equations = createEquationsWithException();

        integrator.integrate(equations, t0, y0, t, y);
    }

    @Test
    public void testIntegrateWithStepHandlers() {
        double t0 = 0.0;
        double[] y0 = {0.0, 1.0};
        double t = 1.0;
        double[] y = {1.0, 0.0};

        FirstOrderDifferentialEquations equations = createEquations();
        StepHandler handler = new StepHandler() {
            @Override
            public void handleStep(StepHandlerContext context, int stepNumber, double[] y) {
                assertEquals(y[0], 0.5 * (y[0] + y[1]), 1e-15);
                assertEquals(y[1], 0.5 * (y[1] + y[0]), 1e-15);
            }
        };

        integrator.addStepHandler(handler);

        integrator.integrate(equations, t0, y0, t, y);
    }

    @Test
    public void testIntegrateWithDifferentStepSizes() {
        double t0 = 0.0;
        double[] y0 = {0.0, 1.0};
        double t1 = 0.5;
        double[] y1 = {0.5, 0.5};
        double t2 = 1.0;
        double[] y2 = {1.0, 0.0};

        FirstOrderDifferentialEquations equations = createEquations();

        integrator.integrate(equations, t0, y0, t1, y1, new RungeKuttaStepInterpolator(integrator));
        integrator.integrate(equations, t1, y1, t2, y2, new RungeKuttaStepInterpolator(integrator));

        assertEquals(t2, t, 1e-15);
        assertArrayEquals(y2, y, 1e-15);
    }

    @Test
    public void testIntegrateWithDifferentStepInterpolators() {
        double t0 = 0.0;
        double[] y0 = {0.0, 1.0};
        double t1 = 0.5;
        double[] y1 = {0.5, 0.5};
        double t2 = 1.0;
        double[] y2 = {1.0, 0.0};

        FirstOrderDifferentialEquations equations = createEquations();

        integrator.integrate(equations, t0, y0, t1, y1, new RungeKuttaStepInterpolator(integrator));
        integrator.integrate(equations, t1, y1, t2, y2, new DummyStepInterpolator());

        assertEquals(t2, t, 1e-15);
        assertArrayEquals(y2, y, 1e-15);
    }
}