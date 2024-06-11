package org.apache.commons.math.ode.nonstiff;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.noopt.StepHandler;
import org.apache.commons.math3.ode.noopt.events.StepEvent;
import org.apache.commons.math3.ode.noopt.events.WarningsFlag;
import org.apache.commons.math3.ode.sampling.RungeKuttaStepInterpolator;
import org.junit.Test;

public class EmbeddedRungeKuttaIntegratorTest {

    @Test
    public void testErrorEstimation() {
        // Given
        double[] c = {0.0, 0.3, 0.6, 1.0};
        double[][] a = {
                {0.0, 0.0, 0.0, 0.0},
                {0.3, 0.48, 0.147, 0.0},
                {0.6, 0.656, 0.422, 0.24},
                {1.0, 0.8203, 0.612, 0.36}
        };
        double[] b = {0.25, 0.443, 0.647, 0.846};
        double safety = 0.9;
        double minReduction = 0.1;
        double maxGrowth = 10.0;

        EmbeddedRungeKuttaIntegrator integrator = new MyEmbeddedRungeKuttaIntegrator(
                "MyEmbeddedRungeKuttaIntegrator", false, c, a, b,
                new RungeKuttaStepInterpolator(), 0.001, 0.01, null, null);

        FirstOrderDifferentialEquations equations = new DummyEquations();

        double t0 = 0.0;
        double[] y0 = {1.0, 1.0};

        double t = 1.0;
        double[] yExpect = {2.0, Math.exp(1.0)};

        // When
        double actual = integrator.integrate(equations, t0, y0, t, new double[y0.length]);

        // Then
        assertEquals(t, actual, 1e-9);
        assertArrayEquals(yExpect, y0, 1e-9);

        // Check error estimation
        double[] yDotK = new double[5][y0.length];
        integrator.integrate(equations, t0, y0, t, yDotK);
        assertEquals(1.0, integrator.getErrorEstimate(yDotK), 1e-9);

        // Check error estimation for fsal method
        integrator = new MyEmbeddedRungeKuttaIntegrator(
                "MyEmbeddedRungeKuttaIntegrator", true, c, a, b,
                new RungeKuttaStepInterpolator(), 0.001, 0.01, null, null);
        integrator.integrate(equations, t0, y0, t, yExpect);
        assertEquals(1.0, integrator.getErrorEstimate(yDotK), 1e-9);
    }

    @Test
    public void testStepControl() {
        // Given
        double[] c = {0.0, 0.3, 0.6, 1.0};
        double[][] a = {
                {0.0, 0.0, 0.0, 0.0},
                {0.3, 0.48, 0.147, 0.0},
                {0.6, 0.656, 0.422, 0.24},
                {1.0, 0.8203, 0.612, 0.36}
        };
        double[] b = {0.25, 0.443, 0.647, 0.846};
        double safety = 0.9;
        double minReduction = 0.1;
        double maxGrowth = 10.0;

        EmbeddedRungeKuttaIntegrator integrator = new MyEmbeddedRungeKuttaIntegrator(
                "MyEmbeddedRungeKuttaIntegrator", false, c, a, b,
                new RungeKuttaStepInterpolator(), 0.001, 0.01, null, null);

        FirstOrderDifferentialEquations equations = new DummyEquations();

        double t0 = 0.0;
        double[] y0 = {1.0, 1.0};

        double t = 1.0;
        double[] yExpect = {2.0, Math.exp(1.0)};

        // When
        integrator.setMinReduction(0.05);
        integrator.setMaxGrowth(5.0);

        integrator.integrate(equations, t0, y0, t, new double[y0.length]);

        // Then
        assertEquals(t, integrator.getCurrentStep(), 1e-9);
        assertTrue(integrator.getLastStepAccepted());

        // Check step control for small error
        integrator.setMinReduction(0.01);
        integrator.setMaxGrowth(2.0);
        integrator.integrate(equations, t0, y0, t, new double[y0.length]);
        assertEquals(Math.sqrt(2.0), integrator.getCurrentStep(), 1e-9);

        // Check step control for large error
        integrator.setMinReduction(0.05);
        integrator.setMaxGrowth(10.0);
        integrator.integratorStep(equations, y0, t, new double[y0.length]);
        integrator.integratorStep(equations, y0, t, new double[y0.length]);
        integrator.integratorStep(equations, y0, t, new double[y0.length]);
        assertEquals(2.0, integrator.getCurrentStep(), 1e-9);

        // Check step control for error too large
        integrator.setMinReduction(0.01);
        integrator.setMaxGrowth(10.0);
        integrator.integratorStep(equations, y0, t, new double[y0.length]);
        integrator.integratorStep(equations, y0, t, new double[y0.length]);
        integrator.integratorStep(equations, y0, t, new double[y0.length]);
        integrator.integratorStep(equations, y0, t, new double[y0.length]);
        integrator.integratorStep(equations, y0, t, new double[y0.length]);
        integrator.integratorStep(equations, y0, t, new double[y0.length]);
        assertFalse(integrator.getLastStepAccepted());
    }

    @Test
    public void testEvents() {
        // Given
        double[] c = {0.0, 0.3, 0.6, 1.0};
        double[][] a = {
                {0.0, 0.0, 0.0, 0.0},
                {0.3, 0.48, 0.147, 0.0},
                {0.6, 0.656, 0.422, 0.24},
                {1.0, 0.8203, 0.612, 0.36}
        };
        double[] b = {0.25, 0.443, 0.647, 0.846};
        double safety = 0.9;
        double minReduction = 0.1;
        double maxGrowth = 10.0;

        EmbeddedRungeKuttaIntegrator integrator = new MyEmbeddedRungeKuttaIntegrator(
                "MyEmbeddedRungeKuttaIntegrator", false, c, a, b,
                new RungeKuttaStepInterpolator(), 0.001, 0.01, null, null);

        FirstOrderDifferentialEquations equations = new DummyEquationsWithEvent();

        double t0 = 0.0;
        double[] y0 = {1.0, 1.0};

        double t = 1.0;
        double[] yExpect = {2.0, Math.exp(1.0)};

        // When
        integrator.addStepHandler(new StepHandler() {
            @Override
            public void handleStep(StepHandler handler, int step) {
                handler.consume(new StepEvent(t, y0, WarningsFlag.NONE));
            }
        });

        integrator.integrate(equations, t0, y0, t, new double[y0.length]);

        // Then
        assertTrue(equations.getEventCount() == 1);
    }

}

class MyEmbeddedRungeKuttaIntegrator extends EmbeddedRungeKuttaIntegrator {

    public MyEmbeddedRungeKuttaIntegrator(String name, boolean fsal, double[] c, double[][] a, double[] b,
                                         RungeKuttaStepInterpolator prototype, double minStep, double maxStep,
                                         double[] scalAbsoluteTolerance, double[] scalRelativeTolerance) {
        super(name, fsal, c, a, b, prototype, minStep, maxStep, scalAbsoluteTolerance, scalRelativeTolerance);
    }

    @Override
    protected double estimateError(double[][] yDotK, double[] y0, double[] y1, double h) {
        // Implement your own error estimation method here
    }
}

class DummyEquations implements FirstOrderDifferentialEquations {

    @Override
    public int getDimension() {
        return 2;
    }

    @Override
    public void computeDerivatives(double t, double[] y, double[] yPrime) {
        yPrime[0] = y[1];
        yPrime[1] = y[0];
    }
}

class DummyEquationsWithEvent implements FirstOrderDifferentialEquations {

    private int eventCount;

    @Override
    public int getDimension() {
        return 2;
    }

    @Override
    public void computeDerivatives(double t, double[] y, double[] yPrime) {
        yPrime[0] = y[1];
        yPrime[1] = y[0];
    }

    @Override
    public int getEventIndex(double t, double[] y) {
        if (t >= 0.5) {
            eventCount++;
            return 1;
        }
        return 0;
    }
}