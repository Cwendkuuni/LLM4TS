package org.apache.commons.math.ode.nonstiff;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.exception.MaxCountExceededException;
import org.junit.Test;

import org.apache.commons.math.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math.ode.events.DefaultValueEvent;
import org.apache.commons.math.ode.events.Event;
import org.apache.commons.math.ode.events.When;
import org.apache.commons.math.ode.sampling.StepHandler;

public class RungeKuttaIntegratorTest {

    @Test(expected = IllegalArgumentException.class)
    public void testNullConstructorArguments() {
        new RungeKuttaIntegrator(null, null, null, null, null, 0.0);
    }

    @Test
    public void testEmptyButcherArray() {
        double[] c = {};
        double[][] a = {};
        double[] b = {};
        RungeKuttaStepInterpolator prototype = new DummyRungeKuttaStepInterpolator();
        RungeKuttaIntegrator integrator = new RungeKuttaIntegrator("empty", c, a, b, prototype, 0.001);
        assertFalse(integrator.requiresDenseOutput());
        assertNull(integrator.getStepHandler());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeStepIntegrator() {
        double[] c = {};
        double[][] a = {};
        double[] b = {};
        RungeKuttaStepInterpolator prototype = new DummyRungeKuttaStepInterpolator();
        RungeKuttaIntegrator integrator = new RungeKuttaIntegrator("negativeStepIntegrator", c, a, b, prototype, -0.001);
    }

    @Test
    public void testStepIntegrator() {
        double[] c = {0.5, 0.5, 0.5};
        double[][] a = {
                {0.0, 0.0}, {0.5, 0.5}, {1.0, 1.0}
        };
        double[] b = {0.25, 0.25, 0.5};
        RungeKuttaStepInterpolator prototype = new DummyRungeKuttaStepInterpolator();
        RungeKuttaIntegrator integrator = new RungeKuttaIntegrator("stepIntegrator", c, a, b, prototype, 0.001);
        assertTrue(integrator.requiresDenseOutput());
        StepHandler handler = (step, lastStep) -> {};
        integrator.setStepHandler(handler);
        assertNull(integrator.getStepHandler());
    }

    @Test
    public void testIntegrate() throws IntegratorException, DerivativeException {
        double[] c = {0.5, 0.5, 0.5};
        double[][] a = {
                {0.0, 0.0}, {0.5, 0.5}, {1.0, 1.0}
        };
        double[] b = {0.25, 0.25, 0.5};
        RungeKuttaStepInterpolator prototype = new DummyRungeKuttaStepInterpolator();
        RungeKuttaIntegrator integrator = new RungeKuttaIntegrator("integrate", c, a, b, prototype, 0.001);
        FirstOrderDifferentialEquations equations = (t, y) -> new double[] {y[0] + y[1], y[0] - y[1]};
        double[] initialCondition = {1.0, 1.0};
        double startTime = 0.0;
        double endTime = 2.0;
        double stepSize = 0.1;

        integrator.setEquations(equations);
        integrator.setInitialCondition(initialCondition);
        integrator.setStepSize(stepSize);

        double result = integrator.integrate(equations, startTime, initialCondition, endTime, null);
        double expectedResult = 2.0;
        assertEquals(expectedResult, result, 1e-5);
    }

    @Test(expected = MaxCountExceededException.class)
    public void testIntegrateWithTooManyEvents() throws IntegratorException, DerivativeException {
        double[] c = {0.5, 0.5, 0.5};
        double[][] a = {
                {0.0, 0.0}, {0.5, 0.5}, {1.0, 1.0}
        };
        double[] b = {0.25, 0.25, 0.5};
        RungeKuttaStepInterpolator prototype = new DummyRungeKuttaStepInterpolator();
        RungeKuttaIntegrator integrator = new RungeKuttaIntegrator("integrateWithTooManyEvents", c, a, b, prototype, 0.001);
        FirstOrderDifferentialEquations equations = (t, y) -> new double[] {y[0] + y[1], y[0] - y[1]};
        double[] initialCondition = {1.0, 1.0};
        double startTime = 0.0;
        double endTime = 0.01;
        double stepSize = 0.001;

        integrator.setEquations(equations);
        integrator.setInitialCondition(initialCondition);
        integrator.setStepSize(stepSize);

        CombinedEventsManager eventsManager = new CombinedEventsManager();
        DefaultValueEvent event = new DefaultValueEvent(0.0001, new When() {
            @Override
            public boolean equals(Event o) {
                return false;
            }

            @Override
            public int hashCode() {
                return 0;
            }

            @Override
            public boolean isSatisfied(double t, double[] y) {
                return false;
            }
        });
        eventsManager.addEqualityCondition(event);

        integrator.setEventsManager(eventsManager);
        integrator.integrate(equations, startTime, initialCondition, endTime, null);
    }

}

class DummyRungeKuttaStepInterpolator extends RungeKuttaStepInterpolator {
    @Override
    public void reinitialize(RungeKuttaIntegrator integrator, double[] yTmp, double[][] yDotK, boolean forward) {
        // empty implementation
    }
}