package org.apache.commons.math.ode.nonstiff;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.events.BeginEvent;
import org.apache.commons.math3.ode.events.ContinuousIntegratorEvent;
import org.apache.commons.math3.ode.events.EndEvent;
import org.apache.commons.math3.ode.events.StepHandler;
import org.apache.commons.math3.ode.events.When;
import org.apache.commons.math3.ode.sampling.StepInterpolator;
import org.apache.commons.math3.ode.sampling.StepHandlerAdapter;
import org.junit.Test;

public class EmbeddedRungeKuttaIntegratorTest {

    @Test
    public void testIntegration() throws Exception {
        double[][] a = new double[][]{{0.0, 0.0, 0.5, 0.5}, {0.0, 0.5, 0.5, 0.0}, {0.5, 0.5, 0.0, 0.5}, {0.5, 0.0, 0.5, 0.5}};
        double[] b = new double[]{0.0, 0.5, 0.5, 0.5, 0.5};
        double[] c = new double[]{0.0, 0.5, 0.5, 0.5, 0.5};
        double[][] yDotK = new double[5][3];
        double[] y0 = new double[]{0.0, 0.0, 0.0};
        double[] y1 = new double[]{1.0, 1.0, 1.0};
        double[] vecAbsoluteTolerance = new double[]{1e-10, 1e-10, 1e-10};
        double minStep = 0.001;
        double maxStep = 0.01;
        RungeKuttaStepInterpolator prototype = new RungeKuttaStepInterpolator(a, b, c);
        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaFixedStep(
                "RKF45", false, a, b, prototype, minStep, maxStep, null, vecAbsoluteTolerance);

        FirstOrderDifferentialEquations equations = new DummyDifferentialEquations();

        double initialTime = 0.0;
        double finalTime = 2.0;
        double stepStart = initialTime;

        StepHandler stepHandler = new StepHandlerAdapter() {
            @Override
            public void handleStep(StepInterpolator interpolator, int index) {
                double[] y = interpolator.getY();
                assertEquals(y1[0], y[0], 1e-9);
                assertEquals(y1[1], y[1], 1e-9);
                assertEquals(y1[2], y[2], 1e-9);
                assertTrue(index == (int) Math.ceil((finalTime - initialTime) / minStep));
            }
        };

        integrator.addStepHandler(stepHandler);

        integrator.integrate(equations, initialTime, y0, finalTime, y1);

    }

    @Test
    public void testIntegrationWithEvents() throws Exception {
        double[][] a = new double[][]{{0.0, 0.0, 0.5, 0.5}, {0.0, 0.5, 0.5, 0.0}, {0.5, 0.5, 0.0, 0.5}, {0.5, 0.0, 0.5, 0.5}};
        double[] b = new double[]{0.0, 0.5, 0.5, 0.5, 0.5};
        double[] c = new double[]{0.0, 0.5, 0.5, 0.5, 0.5};
        double[][] yDotK = new double[5][3];
        double[] y0 = new double[]{0.0, 0.0, 0.0};
        double[] y1 = new double[]{1.0, 1.0, 1.0};
        double[] vecAbsoluteTolerance = new double[]{1e-10, 1e-10, 1e-10};
        double minStep = 0.001;
        double maxStep = 0.01;
        RungeKuttaStepInterpolator prototype = new RungeKuttaStepInterpolator(a, b, c);
        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaFixedStep(
                "RKF45", false, a, b, prototype, minStep, maxStep, null, vecAbsoluteTolerance);

        FirstOrderDifferentialEquations equations = new DummyDifferentialEquations();

        double initialTime = 0.0;
        double finalTime = 2.0;
        double stepStart = initialTime;

        ContinuousIntegratorEvent event = new BeginEvent(1.5, new When() {
            @Override
            public boolean isSatisfied(double t, double[] y) {
                return y[0] > 0.5;
            }
        });

        integrator.addStepHandler(event);

        StepHandler stepHandler = new StepHandlerAdapter() {
            @Override
            public void handleStep(StepInterpolator interpolator, int index) {
                double[] y = interpolator.getY();
                assertEquals(y1[0], y[0], 1e-9);
                assertEquals(y1[1], y[1], 1e-9);
                assertEquals(y1[2], y[2], 1e-9);
                assertTrue(index == (int) Math.ceil((finalTime - initialTime) / minStep));
            }
        };

        integrator.addStepHandler(stepHandler);

        integrator.integrate(equations, initialTime, y0, finalTime, y1);

        assertEquals(1, event.getEventCount());

    }

    @Test
    public void testIntegrationWithEndEvent() throws Exception {
        double[][] a = new double[][]{{0.0, 0.0, 0.5, 0.5}, {0.0, 0.5, 0.5, 0.0}, {0.5, 0.5, 0.0, 0.5}, {0.5, 0.0, 0.5, 0.5}};
        double[] b = new double[]{0.0, 0.5, 0.5, 0.5, 0.5};
        double[] c = new double[]{0.0, 0.5, 0.5, 0.5, 0.5};
        double[][] yDotK = new double[5][3];
        double[] y0 = new double[]{0.0, 0.0, 0.0};
        double[] y1 = new double[]{1.0, 1.0, 1.0};
        double[] vecAbsoluteTolerance = new double[]{1e-10, 1e-10, 1e-10};
        double minStep = 0.001;
        double maxStep = 0.01;
        RungeKuttaStepInterpolator prototype = new RungeKuttaStepInterpolator(a, b, c);
        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaFixedStep(
                "RKF45", false, a, b, prototype, minStep, maxStep, null, vecAbsoluteTolerance);

        FirstOrderDifferentialEquations equations = new DummyDifferentialEquations();

        double initialTime = 0.0;
        double finalTime = 2.0;
        double stepStart = initialTime;

        EndEvent endEvent = new EndEvent();
        integrator.addStepHandler(endEvent);

        StepHandler stepHandler = new StepHandlerAdapter() {
            @Override
            public void handleStep(StepInterpolator interpolator, int index) {
                double[] y = interpolator.getY();
                assertEquals(y1[0], y[0], 1e-9);
                assertEquals(y1[1], y[1], 1e-9);
                assertEquals(y1[2], y[2], 1e-9);
                assertTrue(index == (int) Math.ceil((finalTime - initialTime) / minStep));
            }
        };

        integrator.addStepHandler(stepHandler);

        integrator.integrate(equations, initialTime, y0, finalTime, y1);

        assertTrue(endEvent.isDone());

    }

}

class DummyDifferentialEquations implements FirstOrderDifferentialEquations {
    @Override
    public int getDimension() {
        return 3;
    }

    @Override
    public void derive(double t, double[] y, double[] yPrime) {
        System.arraycopy(y, 0, yPrime, 0, y.length);
    }
}