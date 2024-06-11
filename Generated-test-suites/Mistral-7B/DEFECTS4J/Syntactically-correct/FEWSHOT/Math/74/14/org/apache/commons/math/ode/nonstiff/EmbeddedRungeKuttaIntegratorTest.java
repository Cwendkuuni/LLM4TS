package org.apache.commons.math.ode.nonstiff;

import static org.junit.Assert.*;

import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.events.AbstractEvent;
import org.apache.commons.math3.ode.events.Event;
import org.apache.commons.math3.ode.sampling.StepHandler;
import org.junit.Test;

public class EmbeddedRungeKuttaIntegratorTest {

    @Test
    public void testEmbeddedRungeKuttaIntegrator() {
        EmbeddedRungeKuttaIntegrator integrator = new MyEmbeddedRungeKuttaIntegrator("MyIntegrator", false, new double[]{0.3, 0.4, 0.3},
                new double[][]{{0.25, 0}, {0.25, 0.25}, {0.5, 0.25}}, new double[]{0.5, 0.5, 0.5},
                new RungeKuttaStepInterpolator(), 0.001, 0.01, new double[]{1e-9, 1e-9}, new double[]{1e-9, 1e-9});
        FirstOrderDifferentialEquations equations = new MyDifferentialEquations();

        double[] y0 = {0.0, 0.0};
        double t0 = 0.0;
        double t1 = 1.0;
        double[] y1 = {1.0, 1.0};

        StepHandler stepHandler = new StepHandler() {
            @Override
            public void handleStep(StepHandlerContext context, int stepNumber) {
                System.out.println("Step " + stepNumber + " accepted.");
            }
        };

        double result = integrator.integrate(equations, t0, y0, t1, y1);
        assertEquals(t1, result, 1e-9);

        // Test with an event
        equations.setDifferentialEquations(new MyDifferentialEquationsWithEvent());
        Event event = new MyEvent(1.5);
        integrator.addEvent(event);

        result = integrator.integrate(equations, t0, y0, t1, y1);
        assertEquals(1.5, integrator.getLastEventTime(), 1e-9);
    }

}

class MyEmbeddedRungeKuttaIntegrator extends EmbeddedRungeKuttaIntegrator {

    public MyEmbeddedRungeKuttaIntegrator(String name, boolean fsal, double[] c, double[][] a, double[] b,
                                         RungeKuttaStepInterpolator prototype, double minStep, double maxStep,
                                         double[] vecAbsoluteTolerance, double[] vecRelativeTolerance) {
        super(name, fsal, c, a, b, prototype, minStep, maxStep, vecAbsoluteTolerance, vecRelativeTolerance);
    }

    @Override
    protected double estimateError(double[][] yDotK, double[] y0, double[] y1, double h) {
        double error = 0.0;
        for (int i = 0; i < y0.length; i++) {
            error += Math.pow(Math.abs(y1[i] - y0[i]), 2);
        }
        return Math.sqrt(error);
    }

}

class MyDifferentialEquations implements FirstOrderDifferentialEquations {

    @Override
    public int getNumberOfEquations() {
        return 2;
    }

    @Override
    public void computeDerivatives(double t, double[] y, double[] yPrime) {
        yPrime[0] = y[1];
        yPrime[1] = y[0];
    }

}

class MyDifferentialEquationsWithEvent implements FirstOrderDifferentialEquations {

    private final double eventTime;

    public MyDifferentialEquationsWithEvent(double eventTime) {
        this.eventTime = eventTime;
    }

    @Override
    public int getNumberOfEquations() {
        return 2;
    }

    @Override
    public void computeDerivatives(double t, double[] y, double[] yPrime) {
        if (t >= eventTime) {
            yPrime[0] = 0.0;
            yPrime[1] = 1.0;
        } else {
            yPrime[0] = y[1];
            yPrime[1] = y[0];
        }
    }

}

class MyEvent extends AbstractEvent {

    private final double eventTime;

    public MyEvent(double eventTime) {
        this.eventTime = eventTime;
    }

    @Override
    public boolean isSatisfied(double t, double[] y) {
        return t >= eventTime;
    }

}