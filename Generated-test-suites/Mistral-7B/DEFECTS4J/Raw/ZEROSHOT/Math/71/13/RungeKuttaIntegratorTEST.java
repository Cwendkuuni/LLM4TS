package org.apache.commons.math.ode.nonstiff.test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.events.Event;
import org.apache.commons.math3.ode.events.When;
import org.apache.commons.math3.ode.nonstiff.RungeKuttaIntegrator;
import org.apache.commons.math3.ode.nonstiff.RungeKuttaStepInterpolator;
import org.apache.commons.math3.ode.sampling.StepHandler;
import org.apache.commons.math3.special.BesselFunctions;
import org.junit.Before;
import org.junit.Test;

/**
 * Test class for {@link RungeKuttaIntegrator}.
 */
public class RungeKuttaIntegratorTest {

    private static final double EPSILON = 1e-12;
    private static final double START_TIME = 0.0;
    private static final double END_TIME = Math.PI;
    private static final double STEP_SIZE = 0.1;

    private RungeKuttaIntegrator integrator;
    private FirstOrderDifferentialEquations equations;
    private double[] y0;

    @Before
    public void setUp() {
        integrator = new EulerIntegrator(0.01, new EulerStepInterpolator(), STEP_SIZE);
        equations = new BesselODE();
        y0 = new double[]{0.0, 1.0};
    }

    @Test
    public void testIntegrate() throws Exception {
        double[] y = new double[y0.length];
        double t = integrator.integrate(equations, START_TIME, y0, END_TIME, y);
        assertEquals(END_TIME, t, EPSILON);
        assertArrayEquals(y0, y, EPSILON);
    }

    @Test(expected = MathIllegalArgumentException.class)
    public void testNegativeStepSize() {
        integrator = new EulerIntegrator(0.01, new EulerStepInterpolator(), -STEP_SIZE);
    }

    @Test(expected = MaxCountExceededException.class)
    public void testMaxCountExceeded() throws Exception {
        integrator = new EulerIntegrator(0.01, new EulerStepInterpolator(), 2 * Math.PI / STEP_SIZE);
        integrator.integrate(equations, START_TIME, y0, END_TIME, y);
    }

    @Test
    public void testIntegrateWithEvents() throws Exception {
        final double eventTime = Math.PI / 2;
        StepHandler stepHandler = new StepHandler() {
            @Override
            public void handleStep(StepHandlerContext context, int stepNumber) {
                if (context.isLastStep()) {
                    assertEquals(eventTime, context.getLocalStepSize(), EPSILON);
                    assertEquals(1.0, context.getLocalY()[0], EPSILON);
                    assertEquals(-1.0, context.getLocalY()[1], EPSILON);
                }
            }
        };
        integrator = new RungeKuttaIntegrator(new RK4Method(), new RungeKuttaStepInterpolator(), STEP_SIZE) {
            @Override
            protected void resetEvaluations() {
                // no op
            }

            @Override
            protected void setEquations(FirstOrderDifferentialEquations equations) {
                this.equations = equations;
            }
        };
        equations = new BesselODEWithEvents();
        y0 = new double[]{0.0, 1.0};
        integrator.addStepHandler(stepHandler);
        integrator.integrate(equations, START_TIME, y0, END_TIME, y);
    }

}

class BesselODE implements FirstOrderDifferentialEquations {

    @Override
    public int getDimension() {
        return 2;
    }

    @Override
    public void calculateDerivative(double t, double[] y, double[] yPrime) {
        yPrime[0] = y[1];
        yPrime[1] = -y[0] * BesselFunctions.BesselJ(0, t) / BesselFunctions.BesselJ(1, t);
    }

}

class BesselODEWithEvents implements FirstOrderDifferentialEquations {

    private final double eventTime;

    public BesselODEWithEvents(double eventTime) {
        this.eventTime = eventTime;
    }

    @Override
    public int getDimension() {
        return 2;
    }

    @Override
    public void calculateDerivative(double t, double[] y, double[] yPrime) {
        yPrime[0] = y[1];
        yPrime[1] = -y[0] * BesselFunctions.BesselJ(0, t) / BesselFunctions.BesselJ(1, t);
    }

    @Override
    public Event getEvent(double t) {
        if (t == eventTime) {
            return new When(t);
        }
        return null;
    }

}