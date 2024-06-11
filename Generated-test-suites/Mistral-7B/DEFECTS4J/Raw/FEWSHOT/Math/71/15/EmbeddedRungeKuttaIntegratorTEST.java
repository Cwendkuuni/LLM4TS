package org.apache.commons.math.ode.nonstiff;

import static org.junit.Assert.*;

import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.events.DefaultValueHolder;
import org.apache.commons.math3.ode.events.Event;
import org.apache.commons.math3.ode.events.EventManager;
import org.apache.commons.math3.ode.events.RootFunction;
import org.apache.commons.math3.ode.sampling.StepInterpolator;
import org.junit.Test;

public class EmbeddedRungeKuttaIntegratorTest {

    @Test
    public void testEmbeddedRungeKuttaIntegrator() {
        // Test with default values
        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("RK23", false,
                new double[]{0.25, 0.5}, new double[][]{{0.25, 0.25}, {0.5, 0.5}},
                new double[]{0.25, 0.5}, new RungeKuttaStepInterpolator(), 0.001, 0.01, null, null) {
            @Override
            protected int getOrder() {
                return 2;
            }

            @Override
            protected double estimateError(double[][] yDotK, double[] y0, double[] y1, double h) {
                return Math.abs(y1[0] - y0[0]) / (h + 1e-20);
            }
        };

        double[] initialCondition = new double[]{0.0};
        double[] finalCondition = new double[]{1.0};

        FirstOrderDifferentialEquations equations = new DummyDifferentialEquations();
        double tStart = 0.0;
        double tEnd = 1.0;

        assertEquals(1.0, integrator.integrate(equations, tStart, initialCondition, tEnd, finalCondition)[0], 1e-6);

        // Test with custom values
        integrator = new EmbeddedRungeKuttaIntegrator("RK45", true,
                new double[]{0.2, 0.3, 0.4, 0.5, 0.6, 0.7}, new double[][]{{0.2, 0, 0.27, 0.46666666666666667, 0.5333333333333333, 0.6},
                        {0.3, 0.4, 0.296, 0.615, 0.7233333333333333, 0.804}},
                new double[]{0.3, 0.3, 0.3, 0.3, 0.3, 0.3}, new RungeKuttaStepInterpolator(),
                0.001, 0.01, new double[]{1e-6, 1e-6, 1e-6, 1e-6, 1e-6, 1e-6},
                new double[]{1e-6, 1e-6, 1e-6, 1e-6, 1e-6, 1e-6}) {
            @Override
            protected int getOrder() {
                return 4;
            }

            @Override
            protected double estimateError(double[][] yDotK, double[] y0, double[] y1, double h) {
                return Math.abs(y1[0] - y0[0]) / (h + 1e-20);
            }
        };

        initialCondition = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0};
        finalCondition = new double[]{1.0, 1.0, 1.0, 1.0, 1.0, 1.0};

        EventManager eventManager = new EventManager();
        eventManager.addEvent(new RootFunction(new DefaultValueHolder(1.0), 0.5) {
            @Override
            public double value(double t, double y) {
                return y - 1.0;
            }
        });

        assertEquals(1.0, integrator.integrate(equations, tStart, initialCondition, tEnd, finalCondition)[0], 1e-6);
        assertEquals(5.0, eventManager.size(), 1);
        Event event = eventManager.getEvent(0);
        assertEquals(0.5, event.getTime(), 1e-6);
    }

}

class DummyDifferentialEquations implements FirstOrderDifferentialEquations {

    @Override
    public int getNumberOfEquations() {
        return 1;
    }

    @Override
    public void computeDerivatives(double t, double[] y, double[] yPrime) {
        yPrime[0] = y[0];
    }
}