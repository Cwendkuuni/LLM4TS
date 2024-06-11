package org.apache.commons.math.ode.nonstiff;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.events.When;
import org.apache.commons.math3.ode.sampling.StepHandler;
import org.apache.commons.math3.ode.stepper.RungeKuttaStepInterpolator;
import org.apache.commons.math3.special.Constants;

public class RungeKuttaIntegratorTest {

    @Test(expected = UnsupportedOperationException.class)
    public void testAbstractMethod() {
        new RungeKuttaIntegrator("EulerIntegrator", new double[]{}, new double[][]{{}}, new double[]{0.0}, (RungeKuttaStepInterpolator) null, 0.0) {
            @Override
            protected void computeDerivatives(double t, double[] y, double[] yPrime) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        }.integrate(new DummyDifferentialEquations(), 0.0, new double[]{0.0}, 1.0, new double[1]);
    }

    @Test
    public void testIntegrateNoEvents() {
        RungeKuttaIntegrator integrator = new ClassicalRungeKuttaIntegrator(1.0e-12, new double[]{0.5, 0.5}, new double[][]{{0.5, 0.5}, {0.5, 0.5}}, new double[]{1.0, 1.0}, new RungeKuttaStepInterpolator(), 1.0e-12);
        FirstOrderDifferentialEquations equations = new DummyDifferentialEquations();
        double[] y0 = new double[]{0.0};
        double tStart = 0.0;
        double tEnd = 2.0;
        double[] yEnd = new double[]{Math.exp(2.0)};
        integrator.integrate(equations, tStart, y0, tEnd, yEnd);
        double tActual = integrator.getStepStart();
        assertFalse(Double.isNaN(tActual));
        assertEquals(tEnd, tActual, 1e-12);
        assertArrayEquals(yEnd, y0, 1e-12);
    }

    @Test(expected = MaxCountExceededException.class)
    public void testMaxCountExceeded() {
        RungeKuttaIntegrator integrator = new ClassicalRungeKuttaIntegrator(1.0e-12, new double[]{}, new double[][]{{}}, new double[]{0.0}, new RungeKuttaStepInterpolator(), 1.0e-12);
        FirstOrderDifferentialEquations equations = new DummyDifferentialEquations();
        double[] y0 = new double[]{0.0};
        double tStart = 0.0;
        double tEnd = Constants.PI * 2.0;
        integrator.setMaxStepCount(1);
        integrator.integrate(equations, tStart, y0, tEnd, new double[1]);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testNumberIsTooSmall() {
        RungeKuttaIntegrator integrator = new ClassicalRungeKuttaIntegrator(1.0e-12, new double[]{}, new double[][]{{}}, new double[]{0.0}, new RungeKuttaStepInterpolator(), 1.0e-30);
        FirstOrderDifferentialEquations equations = new DummyDifferentialEquations();
        double[] y0 = new double[]{0.0};
        double tStart = 0.0;
        double tEnd = 1.0;
        integrator.integrate(equations, tStart, y0, tEnd, new double[1]);
    }

    @Test
    public void testIntegrateWithEvents() {
        RungeKuttaIntegrator integrator = new ClassicalRungeKuttaIntegrator(1.0e-12, new double[]{0.5, 0.5}, new double[][]{{0.5, 0.5}, {0.5, 0.5}}, new double[]{1.0, 1.0}, new RungeKuttaStepInterpolator(), 1.0e-12);
        FirstOrderDifferentialEquations equations = new DummyDifferentialEquations();
        StepHandler stepHandler = new MockStepHandler();
        CombinedEventsManager eventsManager = new CombinedEventsManager();
        eventsManager.addEvent(new When(t -> t > 1.5), stepHandler);
        double[] y0 = new double[]{0.0};
        double tStart = 0.0;
        double tEnd = 2.0;
        double[] yEnd = new double[]{Math.exp(2.0)};
        integrator.setEventsManager(eventsManager);
        integrator.integrate(equations, tStart, y0, tEnd, yEnd);
        assertTrue(stepHandler.wasCalled());
    }

}

class DummyDifferentialEquations implements FirstOrderDifferentialEquations {
    @Override
    public int getNumberOfEquations() {
        return 1;
    }

    @Override
    public void computeDerivatives(double t, double[] y, double[] yPrime) {
        yPrime[0] = y[0] * 2.0;
    }
}

class MockStepHandler implements StepHandler {
    private boolean called = false;

    @Override
    public void handleStep(StepHandlerContext context, int stepNumber) {
        called = true;
    }

    public boolean wasCalled() {
        return called;
    }
}