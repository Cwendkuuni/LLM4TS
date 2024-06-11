package org.apache.commons.math.ode.nonstiff;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.events.Event;
import org.apache.commons.math3.ode.events.When;
import org.apache.commons.math3.ode.sampling.StepHandler;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests for {@link EmbeddedRungeKuttaIntegrator} class.
 */
public class EmbeddedRungeKuttaIntegratorTest {

    private EmbeddedRungeKuttaIntegrator integrator;
    private FirstOrderDifferentialEquations equations;
    private double[] initialValues;

    @Before
    public void setUp() {
        integrator = new RungeKuttaFehlberg78Method(); // Use any EmbeddedRungeKuttaIntegrator subclass
        equations = new DummyDifferentialEquations(); // Use any FirstOrderDifferentialEquations implementation
        initialValues = new double[] {0.0, 1.0};
    }

    @Test
    public void testGetOrder() {
        assertEquals(7, integrator.getOrder());
    }

    @Test
    public void testGetSafety() {
        assertEquals(0.9, integrator.getSafety(), 0.001);
    }

    @Test
    public void testSetSafety() {
        integrator.setSafety(0.8);
        assertEquals(0.8, integrator.getSafety(), 0.001);
    }

    @Test
    public void testGetMinReduction() {
        assertEquals(0.2, integrator.getMinReduction(), 0.001);
    }

    @Test
    public void testSetMinReduction() {
        integrator.setMinReduction(0.3);
        assertEquals(0.3, integrator.getMinReduction(), 0.001);
    }

    @Test
    public void testGetMaxGrowth() {
        assertEquals(10.0, integrator.getMaxGrowth(), 0.001);
    }

    @Test
    public void testSetMaxGrowth() {
        integrator.setMaxGrowth(5.0);
        assertEquals(5.0, integrator.getMaxGrowth(), 0.001);
    }

    @Test
    public void testIntegrateSimple() throws Exception {
        double[] y = initialValues.clone();
        double tStart = 0.0;
        double tEnd = 1.0;
        double result = integrator.integrate(equations, tStart, y, tEnd, new double[y.length]);
        assertEquals(tEnd, result, 1e-12);
        assertTrue(Arrays.equals(y, equations.computeDerivatives(tEnd, y), 1e-12));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIntegrateMinimalStepNegative() throws Exception {
        integrator.setMinStep(-1.0);
        integrator.integrate(equations, 0.0, initialValues, 0.0, new double[y.length]);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIntegrateMinimalStepZero() throws Exception {
        integrator.setMinStep(0.0);
        integrator.integrate(equations, 0.0, initialValues, 0.0, new double[y.length]);
    }

    @Test
    public void testIntegrateMaximalStep() throws Exception {
        double[] y = initialValues.clone();
        double tStart = 0.0;
        double tEnd = 1.0;
        double maxStep = 100.0;
        integrator.setMaxStep(maxStep);
        double result = integrator.integrate(equations, tStart, y, tEnd, new double[y.length]);
        assertTrue(result > tEnd);
        assertTrue(result < tStart + maxStep);
    }

    @Test
    public void testIntegrateWithEvents() throws Exception {
        double[] y = initialValues.clone();
        FirstOrderDifferentialEquations equations = new DummyDifferentialEquationsWithEvent();
        double tStart = 0.0;
        double tEnd = 10.0;

        integrator.addStepHandler(new DummyStepHandler());

        integrator.integrate(equations, tStart, y, tEnd, new double[y.length]);

        StepHandler stepHandler = integrator.getStepHandler();
        assertEquals(1, stepHandler.getNumberOfStepsTaken());
    }

    @Test(expected = IntegratorException.class)
    public void testIntegrateWithException() throws Exception {
        double[] y = initialValues.clone();
        FirstOrderDifferentialEquations equations = new DummyDifferentialEquationsWithException();
        double tStart = 0.0;
        double tEnd = 1.0;

        integrator.integrate(equations, tStart, y, tEnd, new double[y.length]);
    }

    @Test
    public void testResetInternalState() {
        double[] y = initialValues.clone();
        integrator.integrate(equations, 0.0, y, 1.0, new double[y.length]);
        integrator.resetInternalState();
        assertNull(integrator.getLastStepHandler());
        assertEquals(0.0, integrator.getStepStart(), 0.001);
        assertEquals(0.0, integrator.getStepSize(), 0.001);
    }

}

class DummyDifferentialEquations implements FirstOrderDifferentialEquations {

    @Override
    public void computeDerivatives(double t, double[] y, double[] yPrime) {
        System.arraycopy(y, 0, yPrime, 0, y.length);
    }

}

class DummyDifferentialEquationsWithEvent implements FirstOrderDifferentialEquations {

    private final double eventTime = 5.0;

    @Override
    public void computeDerivatives(double t, double[] y, double[] yPrime) {
        System.arraycopy(y, 0, yPrime, 0, y.length);
    }

    @Override
    public Event[] getEvents(double t, double[] y, double h) {
        if (t >= eventTime) {
            return new Event[]{new When(t, new DummyEvent())};
        }
        return new Event[0];
    }

}

class DummyEvent implements Event {

}

class DummyStepHandler implements StepHandler {

    private int stepsTaken = 0;

    @Override
    public void handleStep(StepHandler event, int flag) {
        stepsTaken++;
    }

    public int getNumberOfStepsTaken() {
        return stepsTaken;
    }

}

class DummyStepInterpolator implements AbstractStepInterpolator {

    private boolean forward;

    @Override
    public void reinitialize(EmbeddedRungeKuttaIntegrator integrator, double[] yTmp, double[][] yDotK, boolean forward) {
        this.forward = forward;
    }

    @Override
    public void storeTime(double time) {
        // empty implementation
    }

    @Override
    public double interpolate(double time, int stage) {
        return 0.0;
    }

}