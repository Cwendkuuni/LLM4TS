package org.apache.commons.math.ode.nonstiff.test;

import static org.junit.Assert.*;

import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.events.Event;
import org.apache.commons.math3.ode.events.When;
import org.apache.commons.math3.ode.nonstiff.RungeKuttaIntegrator;
import org.apache.commons.math3.ode.sampling.StepHandler;
import org.apache.commons.math3.ode.test.DummyStepHandler;
import org.apache.commons.math3.ode.test.DummyStepInterpolator;
import org.apache.commons.math3.special.BesselFunctions;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests for {@link RungeKuttaIntegrator} class.
 */
public class RungeKuttaIntegratorTest {

    private RungeKuttaIntegrator integrator;
    private FirstOrderDifferentialEquations equations;
    private double[] initialCondition;
    private double startTime, endTime;

    @Before
    public void setUp() {
        integrator = new EulerIntegrator(0.1, new DummyStepInterpolator(), 0.1);
        equations = new DummyDifferentialEquations();
        initialCondition = new double[] {0.0, 1.0};
        startTime = 0.0;
        endTime = 1.0;
    }

    /**
     * Test method for {@link RungeKuttaIntegrator#integrate(FirstOrderDifferentialEquations, double, double[], double, double[])}.
     * @throws Exception if an error occurred
     */
    @Test
    public void testIntegrate() throws Exception {
        double[] y = integrator.integrate(equations, startTime, initialCondition, endTime, new double[initialCondition.length]);
        assertEquals(initialCondition.length, y.length);
        assertArrayEquals(initialCondition, y, 1e-12);
    }

    /**
     * Test method for {@link RungeKuttaIntegrator#integrate(FirstOrderDifferentialEquations, double, double[], double, double[])}.
     * @throws Exception if an error occurred
     */
    @Test(expected = NumberIsTooSmallException.class)
    public void testIntegrateWithStepSizeTooSmall() throws Exception {
        integrator = new EulerIntegrator(0.0001, new DummyStepInterpolator(), 0.01);
        integrator.integrate(equations, startTime, initialCondition, endTime, new double[initialCondition.length]);
    }

    /**
     * Test method for {@link RungeKuttaIntegrator#integrate(FirstOrderDifferentialEquations, double, double[], double, double[])}.
     * @throws Exception if an error occurred
     */
    @Test(expected = MaxCountExceededException.class)
    public void testIntegrateWithMaxCountExceeded() throws Exception {
        integrator = new RungeKuttaIntegrator(10, new DummyStepInterpolator(), new double[0], new double[0], new DummyStepInterpolator(), 1.0 / (1e10)) {
            @Override
            protected void computeDerivatives(double currentTime, double[] currentState, double[] derivatives) {
                throw new RuntimeException();
            }
        };
        integrator.integrate(equations, startTime, initialCondition, endTime, new double[initialCondition.length]);
    }

    /**
     * Test method for {@link RungeKuttaIntegrator#integrate(FirstOrderDifferentialEquations, double, double[], double, double[])}.
     * @throws Exception if an error occurred
     */
    @Test(expected = IntegratorException.class)
    public void testIntegrateWithDerivativeException() throws Exception {
        integrator = new RungeKuttaIntegrator(10, new DummyStepInterpolator(), new double[0], new double[0], new DummyStepInterpolator(), 0.1) {
            @Override
            protected void computeDerivatives(double currentTime, double[] currentState, double[] derivatives) throws DerivativeException {
                throw new DerivativeException("Test Exception");
            }
        };
        integrator.integrate(equations, startTime, initialCondition, endTime, new double[initialCondition.length]);
    }

    /**
     * Test method for {@link RungeKuttaIntegrator#integrate(FirstOrderDifferentialEquations, double, double[], double, double[])}.
     * @throws Exception if an error occurred
     */
    @Test
    public void testIntegrateWithStepHandler() throws Exception {
        integrator = new EulerIntegrator(0.1, new DummyStepInterpolator(), new DummyStepHandler());
        integrator.integrate(equations, startTime, initialCondition, endTime, new double[initialCondition.length]);
    }

    /**
     * Test method for {@link RungeKuttaIntegrator#integrate(FirstOrderDifferentialEquations, double, double[], double, double[])}.
     * @throws Exception if an error occurred
     */
    @Test
    public void testIntegrateWithEventsHandler() throws Exception {
        integrator = new EulerIntegrator(0.1, new DummyStepInterpolator(), new DummyStepInterpolator(), new CombinedEventsManager()) {
            @Override
            protected void computeDerivatives(double currentTime, double[] currentState, double[] derivatives) {
                derivatives[0] = currentState[1];
                derivatives[1] = -BesselFunctions.besselI(1, currentState[1]) * currentState[0];
            }
        };
        integrator.addEvent(new When(1.0).setHandler(new DummyStepHandler()));
        integrator.integrate(new DummyDifferentialEquations() {
            @Override
            public void computeDerivatives(double time, double[] state, double[] derivatives) {
                derivatives[0] = 0.5 * state[0];
                derivatives[1] = state[1] + state[0];
            }
        }, startTime, initialCondition, endTime, new double[initialCondition.length]);
    }

}

class DummyStepInterpolator extends DummyStepInterpolator {
    // empty
}

class DummyStepHandler implements StepHandler {
    // empty
}

class DummyDifferentialEquations implements FirstOrderDifferentialEquations {
    @Override
    public void computeDerivatives(double time, double[] state, double[] derivatives) {
        // empty
    }
}