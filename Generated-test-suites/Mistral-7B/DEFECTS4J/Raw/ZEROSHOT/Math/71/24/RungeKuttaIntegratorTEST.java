package org.apache.commons.math.ode.nonstiff.test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.SingularMatrixException;
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.events.RelativeEvent;
import org.apache.commons.math3.ode.nonstiff.RungeKuttaIntegrator;
import org.apache.commons.math3.ode.nonstiff.RungeKuttaStepInterpolator;
import org.apache.commons.math3.ode.sampling.StepHandler;
import org.junit.Test;

/**
 * Test class for {@link RungeKuttaIntegrator}.
 */
public class RungeKuttaIntegratorTest {

    /**
     * Test method for {@link RungeKuttaIntegrator#RungeKuttaIntegrator(String, double[], double[][], double[], RungeKuttaStepInterpolator, double)}.
     */
    @Test
    public void testRungeKuttaIntegratorConstruction() {
        // create a Runge-Kutta integrator with given parameters
        final double[] c = {0.0, 0.5, 0.5, 0.5};
        final double[][] a = {{0.0, 0.0}, {0.5, 0.5}, {0.5, 0.0}, {0.5, 0.5}};
        final double[] b = {0.25, 0.25, 0.25, 0.5};
        final RungeKuttaStepInterpolator prototype = new RungeKuttaStepInterpolator(2, 4);
        final double step = 0.1;
        RungeKuttaIntegrator integrator = new RungeKuttaIntegrator("RK4", c, a, b, prototype, step);
        assertEquals("RK4", integrator.getName());
        assertEquals(c, integrator.getC());
        assertEquals(a, integrator.getA());
        assertEquals(b, integrator.getB());
        assertEquals(prototype, integrator.getPrototype());
        assertEquals(step, integrator.getStep(), 0.0);
    }

    /**
     * Test method for {@link RungeKuttaIntegrator# integrate(FirstOrderDifferentialEquations, double, double[], double, double[])}.
     */
    @Test
    public void testIntegrate() throws Exception {
        // test a simple differential equation
        final FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
            @Override
            public int getNumberOfEquations() {
                return 1;
            }

            @Override
            public void calculateDerivative(double t, double[] y, double[] result) {
                result[0] = y[0] * (1.0 - y[0]);
            }
        };

        // create a Runge-Kutta integrator with given parameters
        final double[] c = {0.0, 0.5, 0.5, 0.5};
        final double[][] a = {{0.0, 0.0}, {0.5, 0.5}, {0.5, 0.0}, {0.5, 0.5}};
        final double[] b = {0.25, 0.25, 0.25, 0.5};
        final RungeKuttaStepInterpolator prototype = new RungeKuttaStepInterpolator(2, 4);
        final double step = 0.1;
        RungeKuttaIntegrator integrator = new RungeKuttaIntegrator("RK4", c, a, b, prototype, step);

        // integrate the differential equation from t0=0.0 to t=1.0
        final double t0 = 0.0;
        final double[] y0 = {0.5};
        final double t = 1.0;
        final double[] y = new double[y0.length];
        double result = integrator.integrate(equations, t0, y0, t, y);
        assertEquals(t, result, 0.0);
        assertArrayEquals(new double[] {0.51964558462567106}, y, 0.0);

        // test a differential equation with an event
        final FirstOrderDifferentialEquations equationsWithEvent = new FirstOrderDifferentialEquations() {
            @Override
            public int getNumberOfEquations() {
                return 1;
            }

            @Override
            public void calculateDerivative(double t, double[] y, double[] result) {
                result[0] = y[0] * (1.0 - y[0]);
            }

            @Override
            public void calculateJacobian(double t, double[] y, double[][] jacobian) {
                jacobian[0][0] = -(1.0 - 2.0 * y[0]);
            }

            @Override
            public double[] getInitialValue(int index) {
                return new double[] {0.5};
            }

            @Override
            public RelativeEvent locateNextEvent(double t, double[] y, double step, double[] jacobian, double[] yDot)
                    throws MaxIterationsExceededException, SingularMatrixException, NumberIsTooSmallException {
                // locate the next event when y=0.1
                if (y[0] >= 0.1) {
                    return new RelativeEvent(t + 0.1, "Event");
                }
                return null;
            }
        };

        // integrate the differential equation from t0=0.0 to t=1.0 with an event
        final double[] y0Event = {0.5};
        final StepHandler stepHandler = new StepHandler() {
            @Override
            public void handleStep(StepHandlerContext context, int stepNumber) {
                System.out.println("Step " + stepNumber + ": " + context.getStepSize());
            }
        };
        integrator.addStepHandler(stepHandler);
        integrator.integrate(equationsWithEvent, t0, y0Event, t, y);
        assertEquals(1.0, result, 0.0);
        assertArrayEquals(new double[] {0.10000000000000001}, y, 0.0);
        assertEquals(1, stepHandler.getNumberOfSteps());

        // test a differential equation with an event that occurs immediately after the initial condition
        final FirstOrderDifferentialEquations equationsWithImmediateEvent = new FirstOrderDifferentialEquations() {
            @Override
            public int getNumberOfEquations() {
                return 1;
            }

            @Override
            public void calculateDerivative(double t, double[] y, double[] result) {
                result[0] = 1.0;
            }

            @Override
            public double[] getInitialValue(int index) {
                return new double[] {0.0};
            }

            @Override
            public RelativeEvent locateNextEvent(double t, double[] y, double step, double[] jacobian, double[] yDot)
                    throws MaxIterationsExceededException, SingularMatrixException, NumberIsTooSmallException {
                // locate the next event when y=1.0
                if (y[0] >= 1.0) {
                    return new RelativeEvent(t, "Event");
                }
                return null;
            }
        };
        integrator.setEventsHandlersManager(new CombinedEventsManager());
        integrator.addEndTimeChecker(new RelativeEvent(0.0, "End"));
        integrator.integrate(equationsWithImmediateEvent, t0, y0, t, y);
        assertEquals(0.0, result, 0.0);
        assertEquals(1, integrator.getNumberOfSteps());
    }

}

class RungeKuttaStepInterpolator extends AbstractStepInterpolator {

    /** Number of stages for the Runge-Kutta method. */
    private final int stages;

    /**
     * Constructor for {@link RungeKuttaStepInterpolator}.
     * @param stages number of stages for the Runge-Kutta method
     */
    public RungeKuttaStepInterpolator(final int stages) {
        this.stages = stages;
    }

    @Override
    protected void initialize(double[][] yDot, boolean forward) {
        // do nothing
    }

    @Override
    protected void copyParametersFrom(AbstractStepInterpolator other) {
        // do nothing
    }

    @Override
    protected void copyParametersTo(AbstractStepInterpolator other) {
        // do nothing
    }

    @Override
    protected double interpolate(double t) {
        // return the value of the step at the given time t
        double result = t;
        for (int i = 0; i < stages; ++i) {
            result += b[i] * yDot[i + 1][0];
        }
        return result;
    }

    @Override
    protected void storeTime(double t) {
        // store the time t for future interpolation
        super.storeTime(t);
    }

    @Override
    protected void shift() {
        // shift the stored time by one step
        super.shift();
    }

    @Override
    protected void reinitialize(RungeKuttaIntegrator integrator, double[] yTmp, double[][] yDotK, boolean forward) {
        super.reinitialize(integrator, yTmp, yDotK, forward);
        this.initialize(yDotK[stages], forward);
    }

}