package org.apache.commons.math.ode.nonstiff;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.events.EmptyEventManager;
import org.apache.commons.math3.ode.events.ImpulsiveEvent;
import org.apache.commons.math3.ode.events.StepHandler;
import org.apache.commons.math3.ode.sampling.AbstractStepInterpolator;
import org.apache.commons.math3.ode.sampling.DummyStepInterpolator;
import org.apache.commons.math3.ode.sampling.StepHandlerAdapter;
import org.junit.Test;

public class RungeKuttaIntegratorTest {

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidConstructorInput() {
        new RungeKuttaIntegrator("", null, null, null, null, 0);
    }

    @Test
    public void testEmptyConstructor() {
        RungeKuttaIntegrator integrator = new RungeKuttaIntegrator("Empty", new double[0], new double[][]{}, new double[0], new RungeKuttaStepInterpolator(1), 1);
        assertEquals("Empty", integrator.getName());
        assertArrayEquals(new double[0], integrator.getC());
        assertArrayEquals(new double[][]{}, integrator.getA());
        assertArrayEquals(new double[0], integrator.getB());
        assertNull(integrator.getPrototype());
        assertEquals(1, integrator.getStep());
    }

    @Test
    public void testGetters() {
        double[] c = {0.25, 0.5};
        double[][] a = {
            {0.5, 0},
            {0, 0.5}};
        double[] b = {0.5, 0.5};
        RungeKuttaIntegrator integrator = new RungeKuttaIntegrator("RK2", c, a, b, new RungeKuttaStepInterpolator(2), 1);
        assertEquals("RK2", integrator.getName());
        assertArrayEquals(c, integrator.getC());
        assertArrayEquals(a, integrator.getA());
        assertArrayEquals(b, integrator.getB());
        assertEquals(2, integrator.getPrototype().getStages());
        assertEquals(1, integrator.getStep());
    }

    @Test
    public void testIntegration() throws DeriveException, IntegratorException {
        FirstOrderDifferentialEquations equations = new DummyDifferentialEquations();
        double[] initialValues = new double[]{1.0, 1.0};
        double[] finalValues = new double[]{2.0, 2.0};
        double startTime = 0.0;
        double endTime = 2.0;
        RungeKuttaIntegrator integrator = new RungeKuttaIntegrator("RK2", new double[]{0.25, 0.5}, new double[][]{{0.5, 0}, {0, 0.5}}, new double[]{0.5, 0.5}, new RungeKuttaStepInterpolator(2), 1.0);
        double result = integrator.integrate(equations, startTime, initialValues, endTime, finalValues);
        assertEquals(endTime, result);
        assertArrayEquals(finalValues, initialValues);
    }

    @Test(expected = DeriveException.class)
    public void testIntegrationDeriveException() throws IntegratorException {
        FirstOrderDifferentialEquations equations = new DummyDifferentialEquationsWithDeriveException();
        double[] initialValues = new double[]{1.0, 1.0};
        double[] finalValues = new double[]{2.0, 2.0};
        double startTime = 0.0;
        double endTime = 2.0;
        RungeKuttaIntegrator integrator = new RungeKuttaIntegrator("RK2", new double[]{0.25, 0.5}, new double[][]{{0.5, 0}, {0, 0.5}}, new double[]{0.5, 0.5}, new RungeKuttaStepInterpolator(2), 1.0);
        integrator.integrate(equations, startTime, initialValues, endTime, finalValues);
    }

    @Test(expected = IntegratorException.class)
    public void testIntegrationIntegratorException() throws DeriveException {
        FirstOrderDifferentialEquations equations = new DummyDifferentialEquationsWithIntegratorException();
        double[] initialValues = new double[]{1.0, 1.0};
        double[] finalValues = new double[]{2.0, 2.0};
        double startTime = 0.0;
        double endTime = 2.0;
        RungeKuttaIntegrator integrator = new RungeKuttaIntegrator("RK2", new double[]{0.25, 0.5}, new double[][]{{0.5, 0}, {0, 0.5}}, new double[]{0.5, 0.5}, new RungeKuttaStepInterpolator(2), 1.0);
        integrator.integrate(equations, startTime, initialValues, endTime, finalValues);
    }

    @Test
    public void testIntegrationStepHandlers() throws DeriveException, IntegratorException {
        FirstOrderDifferentialEquations equations = new DummyDifferentialEquationsWithStepHandlers();
        double[] initialValues = new double[]{1.0, 1.0};
        double[] finalValues = new double[]{2.0, 2.0};
        double startTime = 0.0;
        double endTime = 2.0;
        RungeKuttaIntegrator integrator = new RungeKuttaIntegrator("RK2", new double[]{0.25, 0.5}, new double[][]{{0.5, 0}, {0, 0.5}}, new double[]{0.5, 0.5}, new RungeKuttaStepInterpolator(2), 1.0) {
            @Override
            protected void computeDerivatives(double time, double[] y, double[] yDot) throws DeriveException {
                if (y[0] > 1.5) {
                    throw new DeriveException("Derivative exception.");
                }
                super.computeDerivatives(time, y, yDot);
            }
        };
        StepHandler stepHandler = new StepHandlerAdapter() {
            @Override
            public void handleStep(AbstractStepInterpolator interpolator, int step) {
                if (step % 2 == 0) {
                    throw new NumberIsTooSmallException("Step size is too small.");
                }
            }
        };
        integrator.addStepHandler(stepHandler);
        try {
            integrator.integrate(equations, startTime, initialValues, endTime, finalValues);
            fail("Expected IntegratorException or DeriveException.");
        } catch (IntegratorException e) {
            // Expected
        } catch (DeriveException e) {
            // Expected
        }
    }

    @Test
    public void testIntegrationMaxCountExceededException() throws DeriveException, IntegratorException {
        FirstOrderDifferentialEquations equations = new DummyDifferentialEquationsWithMaxCountExceededException();
        double[] initialValues = new double[]{1.0, 1.0};
        double[] finalValues = new double[]{2.0, 2.0};
        double startTime = 0.0;
        double endTime = 2.0;
        RungeKuttaIntegrator integrator = new RungeKuttaIntegrator("RK2", new double[]{0.25, 0.5}, new double[][]{{0.5, 0}, {0, 0.5}}, new double[]{0.5, 0.5}, new RungeKuttaStepInterpolator(2), 1.0);
        integrator.setMaxStepCount(1);
        try {
            integrator.integrate(equations, startTime, initialValues, endTime, finalValues);
            fail("Expected MaxCountExceededException.");
        } catch (MaxCountExceededException e) {
            // Expected
        }
    }

    @Test
    public void testIntegrationNoEvents() throws DeriveException, IntegratorException {
        FirstOrderDifferentialEquations equations = new DummyDifferentialEquationsNoEvents();
        double[] initialValues = new double[]{1.0, 1.0};
        double[] finalValues = new double[]{2.0, 2.0};
        double startTime = 0.0;
        double endTime = 2.0;
        RungeKuttaIntegrator integrator = new RungeKuttaIntegrator("RK2", new double[]{0.25, 0.5}, new double[][]{{0.5, 0}, {0, 0.5}}, new double[]{0.5, 0.5}, new RungeKuttaStepInterpolator(2), 1.0);
        EmptyEventManager eventManager = new EmptyEventManager();
        integrator.setEventsHandlerManager(eventManager);
        double result = integrator.integrate(equations, startTime, initialValues, endTime, finalValues);
        assertEquals(endTime, result);
        assertArrayEquals(finalValues, initialValues);
    }

    @Test
    public void testIntegrationImpulsiveEvent() throws DeriveException, IntegratorException {
        FirstOrderDifferentialEquations equations = new DummyDifferentialEquationsImpulsiveEvent();
        double[] initialValues = new double[]{1.0, 1.0};
        double[] finalValues = new double[]{2.0, 2.0};
        double startTime = 0.0;
        double endTime = 2.0;
        RungeKuttaIntegrator integrator = new RungeKuttaIntegrator("RK2", new double[]{0.25, 0.5}, new double[][]{{0.5, 0}, {0, 0.5}}, new double[]{0.5, 0.5}, new RungeKuttaStepInterpolator(2), 1.0);
        ImpulsiveEvent event = new ImpulsiveEvent(1.5, 1.0, new double[]{0.1, 0.1});
        integrator.addEvent(event);
        double result = integrator.integrate(equations, startTime, initialValues, endTime, finalValues);
        assertEquals(endTime, result);
        assertArrayEquals(new double[]{2.0 + 0.1, 2.0 + 0.1}, finalValues);
    }

}

class DummyDifferentialEquations implements FirstOrderDifferentialEquations {
    @Override
    public int getDimension() {
        return 2;
    }

    @Override
    public void computeDerivatives(double time, double[] y, double[] yDot) {
        yDot[0] = y[1];
        yDot[1] = -y[0];
    }
}

class DummyDifferentialEquationsWithDeriveException extends DummyDifferentialEquations {
    @Override
    public void computeDerivatives(double time, double[] y, double[] yDot) throws DeriveException {
        if (y[0] > 1.5) {
            throw new DeriveException("Derivative exception.");
        }
        super.computeDerivatives(time, y, yDot);
    }
}

class DummyDifferentialEquationsWithIntegratorException extends DummyDifferentialEquations {
    @Override
    public void computeDerivatives(double time, double[] y, double[] yDot) throws IntegratorException {
        throw new IntegratorException("Integrator exception.");
    }
}

class DummyDifferentialEquationsNoEvents implements FirstOrderDifferentialEquations {

}

class DummyDifferentialEquationsImpulsiveEvent extends DummyDifferentialEquations {
    @Override
    public void computeDerivatives(double time, double[] y, double[] yDot) {
        if (time > 1.5) {
            System.arraycopy(new double[]{0.1, 0.1}, 0, yDot, 0, 2);
        } else {
            super.computeDerivatives(time, y, yDot);
        }
    }
}