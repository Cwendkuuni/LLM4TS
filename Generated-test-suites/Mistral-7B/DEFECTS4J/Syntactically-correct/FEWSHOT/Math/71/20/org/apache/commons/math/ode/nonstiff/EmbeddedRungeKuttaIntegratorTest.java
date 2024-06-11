package org.apache.commons.math.ode.nonstiff;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.events.Event;
import org.apache.commons.math3.ode.events.When;
import org.apache.commons.math3.ode.sampling.RungeKuttaStepInterpolator;
import org.junit.Test;

public class EmbeddedRungeKuttaIntegratorTest {

    @Test
    public void testIntegratorBasic() {
        double[][] a = {
                {0.0, 0.0, 0.5},
                {0.0, 0.5, 0.5},
                {0.5, 0.5, 0.5}
        };
        double[] b = {0.0, 0.5, 0.5};
        double[] c = {0.0, 0.5, 1.0};
        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKutta23(
                "EmbeddedRungeKutta23", true, c, a, b,
                new RungeKuttaStepInterpolator(), 0.01, 0.05, null, null);

        FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
            @Override
            public int getNumberOfEquations() {
                return 1;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yd) {
                yd[0] = Math.sin(y[0]);
            }
        };

        double[] y0 = {0.0};
        double t0 = 0.0;
        double t1 = Math.PI / 2;

        double result = integrator.integrate(equations, t0, y0, t1, null);
        assertEquals(t1, result, 1e-6);
        assertArrayEquals(new double[]{Math.sin(Math.PI / 4)}, y0, 1e-6);
    }

    @Test
    public void testIntegratorWithEvent() {
        double[][] a = {
                {0.0, 0.0, 0.5},
                {0.0, 0.5, 0.5},
                {0.5, 0.5, 0.5}
        };
        double[] b = {0.0, 0.5, 0.5};
        double[] c = {0.0, 0.5, 1.0};
        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKutta23(
                "EmbeddedRungeKutta23", true, c, a, b,
                new RungeKuttaStepInterpolator(), 0.01, 0.05, null, null);

        FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
            @Override
            public int getNumberOfEquations() {
                return 1;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yd) {
                yd[0] = y[0];
            }
        };

        double[] y0 = {0.0};
        double t0 = 0.0;
        double t1 = 1.0;

        double[] eventTimes = {0.5};
        StepHandler stepHandler = new StepHandler() {
            @Override
            public void handleStep(StepHandlerContext context, int stepNumber) {
                assertTrue(context.isLastStep());
                assertEquals(eventTimes[0], context.getLocalTime(), 1e-6);
            }
        };

        When event = new When(eventTimes[0]) {
            @Override
            public Event handle(double t, double[] y, double[] yd, double[] dyds) {
                return null;
            }
        };
        integrator.addStepHandler(stepHandler);
        integrator.addEventHandler(event);

        double result = integrator.integrate(equations, t0, y0, t1, null);
        assertEquals(t1, result, 1e-6);
        assertArrayEquals(new double[]{0.5}, y0, 1e-6);
    }

}