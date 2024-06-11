package org.apache.commons.math.ode.nonstiff.test;

import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.SingularMatrixException;
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.events.Event;
import org.apache.commons.math3.ode.events.EventException;
import org.apache.commons.math3.ode.events.RelativeSizeEvent;
import org.apache.commons.math3.ode.nonstiff.RungeKuttaIntegrator;
import org.apache.commons.math3.ode.sampling.StepHandler;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class RungeKuttaIntegratorTest {
    private RungeKuttaIntegrator integrator;
    private FirstOrderDifferentialEquations equations;
    private double[] initialConditions;
    private double startTime, endTime;

    @BeforeEach
    public void setup() {
        integrator = new EulerIntegrator(0.1, new EulerStepInterpolator(), 0.01);
        equations = new MyDifferentialEquations();
        initialConditions = new double[] {0.0, 1.0};
        startTime = 0.0;
        endTime = 10.0;
    }

    @Test
    public void testIntegration() throws Exception {
        double[] result = new double[initialConditions.length];
        double step = integrator.integrate(equations, startTime, initialConditions, endTime, result);
        Assertions.assertArrayEquals(new double[]{5.25886951457239, 3.0}, result, 0.00001);
        Assertions.assertEquals(endTime, step);
    }

    @Test
    public void testIntegrationWithEvents() throws Exception {
        StepHandler stepHandler = new EventDetectionStepHandler();
        integrator = new RungeKutta255Dopri5Integrator(0.01, new DummyStepInterpolator(), stepHandler);
        equations = new MyDifferentialEquationsWithEvent();
        initialConditions = new double[] {0.0, 1.0};
        startTime = 0.0;
        endTime = 5.0;

        double[] result = new double[initialConditions.length];
        integrator.integrate(equations, startTime, initialConditions, endTime, result);

        Assertions.assertEquals(3, stepHandler.getEventCount());
        Assertions.assertEquals(2.75, stepHandler.getEventTimes()[0], 0.0001);
        Assertions.assertEquals(3.5, stepHandler.getEventTimes()[1], 0.0001);
        Assertions.assertEquals(4.25, stepHandler.getEventTimes()[2], 0.0001);
    }

    @Test
    public void testSingularMatrixException() {
        equations = new SingularMatrixExceptionDifferentialEquations();
        Assertions.assertThrows(SingularMatrixException.class, () -> integrator.integrate(equations, startTime, initialConditions, endTime, result));
    }

    @Test
    public void testMaxCountExceededException() {
        equations = new MaxCountExceededExceptionDifferentialEquations();
        Assertions.assertThrows(MaxCountExceededException.class, () -> integrator.integrate(equations, startTime, initialConditions, endTime, result));
    }
}

class MyDifferentialEquations implements FirstOrderDifferentialEquations {
    @Override
    public int getOrder() {
        return 1;
    }

    @Override
    public int getDimension() {
        return initialConditions.length;
    }

    @Override
    public void computeDerivatives(double t, double[] y, double[] yPrime) {
        for (int i = 0; i < y.length; i++) {
            yPrime[i] = y[i] * (1.0 + 0.1 * (i + 1));
        }
    }
}

class MyDifferentialEquationsWithEvent implements FirstOrderDifferentialEquations {
    private double eventTime = 2.5;

    @Override
    public int getOrder() {
        return 1;
    }

    @Override
    public int getDimension() {
        return initialConditions.length;
    }

    @Override
    public void computeDerivatives(double t, double[] y, double[] yPrime) {
        for (int i = 0; i < y.length; i++) {
            yPrime[i] = y[i] * (1.0 + 0.1 * (i + 1));
        }
    }

    @Override
    public Event[] getEvents(double t, double[] y, double[] yDot, double[] parameters) {
        if (t >= eventTime) {
            return new Event[] {new RelativeSizeEvent(t, 0.01)};
        }
        return new Event[0];
    }
}

class SingularMatrixExceptionDifferentialEquations implements FirstOrderDifferentialEquations {
    @Override
    public int getOrder() {
        return 1;
    }

    @Override
    public int getDimension() {
        return initialConditions.length;
    }

    @Override
    public void computeDerivatives(double t, double[] y, double[] yPrime) {
        throw new SingularMatrixException("Singular matrix");
    }
}

class MaxCountExceededExceptionDifferentialEquations implements FirstOrderDifferentialEquations {
    private int maxEvaluations = 10;

    @Override
    public int getOrder() {
        return 1;
    }

    @Override
    public int getDimension() {
        return initialConditions.length;
    }

    @Override
    public void computeDerivatives(double t, double[] y, double[] yPrime) {
        if (++evaluations >= maxEvaluations) {
            throw new MaxCountExceededException("Maximum number of evaluations reached");
        }

        for (int i = 0; i < y.length; i++) {
            yPrime[i] = y[i] * (1.0 + 0.1 * (i + 1));
        }
    }

    private int evaluations = 0;
}

class EventDetectionStepHandler implements StepHandler {
    private int eventCount = 0;
    private double[] eventTimes = new double[10];

    @Override
    public void handleStep(double t, double[] y, double[] yPrime) {
        if (t > eventTimes[eventCount]) {
            eventTimes[eventCount++] = t;
        }
    }

    public int getEventCount() {
        return eventCount;
    }

    public double[] getEventTimes() {
        return eventTimes;
    }
}