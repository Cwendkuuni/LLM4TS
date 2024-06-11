package org.apache.commons.math.ode.nonstiff.test;

import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.OutOfBoundsException;
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.Integrator;
import org.apache.commons.math3.ode.events.RelativisticStepCondition;
import org.apache.commons.math3.ode.sampling.StepHandler;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

@RunWith(Parameterized.class)
public class EmbeddedRungeKuttaIntegratorTest {

    private EmbeddedRungeKuttaIntegrator integrator;
    private double[] initialCondition;
    private double stepSize;
    private double startTime;
    private double endTime;

    @Parameterized.Parameters(name = "{index}: testIntegrator with initialCondition={0}, startTime={1}, endTime={2}")
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {new double[]{0.0}, 0.0, 1.0},
                {new double[]{1.0}, 0.0, 2.0},
                {new double[]{1.0, 1.0}, 0.0, 2.0},
                {new double[]{1.0, 1.0, 1.0}, 0.0, 3.0},
                {new double[]{1.0, 1.0, 1.0, 1.0}, 0.0, 4.0},
                {new double[]{1.0, 1.0, 1.0, 1.0, 1.0}, 0.0, 5.0},
                {new double[]{1.0, 1.0, 1.0, 1.0, 1.0, 1.0}, 0.0, 10.0},
                {new double[]{1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0}, 0.0, Double.POSITIVE_INFINITY},
                {new double[]{1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0}, Double.NEGATIVE_INFINITY, 0.0},
                {new double[]{1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0}, Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY},
                {new double[]{1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0}, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY}
        });
    }

    @Parameterized.Constructor
    public EmbeddedRungeKuttaIntegratorTest(double[] initialCondition, double startTime, double endTime) {
        this.initialCondition = initialCondition;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    @Before
    public void setUp() {
        integrator = new MyEmbeddedRungeKuttaIntegrator(
                "testMethod", false,
                new double[]{0.5, 0.5, 0.5, 0.5, 0.5, 0.5, 0.5},
                new double[][]{{0.0, 1.0}, {0.25, 0.25}, {0.5, 0.0}, {0.875, 0.0}, {0.5, 0.5}, {0.8125, 0.0}, {0.9375, 0.125}},
                new double[]{0.0, 0.6, 0.3, 0.3, 0.6, 0.3, 0.6},
                new RungeKuttaStepInterpolator(new RungeKuttaMethod(/*...*/)) {
                    @Override
                    protected double estimateError(double[][] yDotK, double[] y0, double[] y1, double h) {
                        // the actual implementation of the error estimation method
                        // should be put here
                        return 1.0;
                    }
                },
                0.001, 1.0,
                new double[]{1.0e-10, 1.0e-10}
        );
    }

    @Test
    public void testIntegrator() throws Exception {
        FirstOrderDifferentialEquations equations = new DummyFirstOrderDifferentialEquations();
        double[] output = integrator.integrate(equations, startTime, initialCondition, endTime);
        Assert.assertArrayEquals(new double[]{initialCondition[0], initialCondition[1], initialCondition[1], initialCondition[0], initialCondition[1], initialCondition[1], initialCondition[1]}, output);
    }

    @Test(expected = OutOfBoundsException.class)
    public void testNegativeStepSize() throws Exception {
        integrator.setStepSize(-1.0);
        integrator.integrate(new DummyFirstOrderDifferentialEquations(), startTime, initialCondition, endTime);
    }

    @Test(expected = OutOfBoundsException.class)
    public void testZeroStepSize() throws Exception {
        integrator.setStepSize(0.0);
        integrator.integrate(new DummyFirstOrderDifferentialEquations(), startTime, initialCondition, endTime);
    }

    @Test(expected = MaxCountExceededException.class)
    public void testMaxStepCount() throws Exception {
        integrator.setMaxStepCount(1);
        integrator.integrate(new DummyFirstOrderDifferentialEquations(), startTime, initialCondition, endTime);
    }

    @Test
    public void testEventsHandling() throws Exception {
        StepHandler stepHandler = new EventStepHandler();
        integrator.addStepHandler(stepHandler);
        integrator.integrate(new DummyFirstOrderDifferentialEquations(), startTime, initialCondition, endTime);
        Assert.assertTrue(stepHandler.isStepHandled());
    }

    @Test
    public void testRelativisticStepCondition() throws Exception {
        integrator.setEventsManager(new RelativisticStepCondition(1.0));
        integrator.integrate(new DummyFirstOrderDifferentialEquations(), startTime, initialCondition, endTime);
        // additional assertions for the relativistic step condition should be put here
    }

}

class MyEmbeddedRungeKuttaIntegrator extends EmbeddedRungeKuttaIntegrator {

    public MyEmbeddedRungeKuttaIntegrator(String name, boolean fsal, double[] c, double[][] a, double[] b,
                                         RungeKuttaStepInterpolator prototype, double minStep, double maxStep,
                                         double[] scalAbsoluteTolerance, double[] scalRelativeTolerance) {
        super(name, fsal, c, a, b, prototype, minStep, maxStep, scalAbsoluteTolerance, scalRelativeTolerance);
    }

    @Override
    public int getOrder() {
        // the actual order of the method should be put here
        return 5;
    }

}

class DummyFirstOrderDifferentialEquations implements FirstOrderDifferentialEquations {

    @Override
    public int getNumberOfEquations() {
        return initialCondition.length;
    }

    @Override
    public void computeDerivatives(double t, double[] y, double[] yPrime) {
        System.arraycopy(y, 0, yPrime, 0, y.length);
    }

}

class EventStepHandler implements StepHandler {

    private boolean isHandled = false;

    @Override
    public void handleStep(StepHandler event, int type) {
        isHandled = true;
    }

    public boolean isStepHandled() {
        return isHandled;
    }

}