package org.apache.commons.math.ode.nonstiff;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.events.DefaultValueEvent;
import org.apache.commons.math3.ode.events.Event;
import org.apache.commons.math3.ode.events.ValuesSTEP;
import org.apache.commons.math3.ode.sampling.RungeKuttaStepInterpolator;
import org.junit.Before;
import org.junit.Test;

/**
 * Test cases for {@link EmbeddedRungeKuttaIntegrator}.
 */
public class EmbeddedRungeKuttaIntegratorTest {

    private EmbeddedRungeKuttaIntegrator integrator;
    private FirstOrderDifferentialEquations equations;
    private double[] initValues;
    private double[] controlValues;
    private double stepSize;
    private double[] y;
    private RungeKuttaStepInterpolator interpolator;
    private double minStep;
    private double maxStep;
    private double tolerance;
    private double absTolerance;
    private double relTolerance;
    private Event event;

    @Before
    public void setUp() {
        integrator = new EmbeddedRungeKuttaIntegrator("RK23", false, new double[]{0.25, 0.692},
                new double[][]{new double[]{0.6, 0.275}, new double[]{1.0 - 0.6, 0.275}},
                new double[]{0.25, 0}, new RungeKuttaStepInterpolator(2, 0.0, 0.0, 1.0)) {
            @Override
            public int getOrder() {
                return 2;
            }

            @Override
            public double getSafety() {
                return 0.9;
            }

            @Override
            public void setSafety(double safety) {
                // no-op
            }

            @Override
            public double getMinReduction() {
                return 0.2;
            }

            @Override
            public void setMinReduction(double minReduction) {
                // no-op
            }

            @Override
            public double getMaxGrowth() {
                return 10.0;
            }

            @Override
            public void setMaxGrowth(double maxGrowth) {
                // no-op
            }

            @Override
            public double estimateError(double[][] yDotK, double[] y0, double[] y1, double h) {
                double error = 0.0;
                for (int i = 0; i < y0.length; i++) {
                    error += Math.pow(Math.abs(y1[i] - y0[i]), 2);
                }
                return Math.sqrt(error) / h;
            }
        };
        equations = new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return y.length;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yPrime) {
                // no-op
            }
        };
        initValues = new double[]{1.0, 2.0};
        controlValues = new double[]{3.0, 4.0};
        y = new double[y.length];
        stepSize = 0.1;
        interpolator = new RungeKuttaStepInterpolator(2, 0.0, 0.0, 1.0);
        minStep = 0.01;
        maxStep = 10.0;
        tolerance = 0.01;
        absTolerance = 0.01;
        relTolerance = 0.05;
        event = new ValuesSTEP(2.5, new DefaultValueEvent(2.5));
    }

    @Test
    public void testIntegrate() throws Exception {
        integrator.setEquations(equations);
        integrator.initialize(initValues, minStep, maxStep, tolerance, absTolerance, relTolerance);
        integrator.integrate(equations, 0.0, initValues, 1.0, y);
        assertEquals(controlValues[0], y[0], absTolerance);
        assertEquals(controlValues[1], y[1], absTolerance);
    }

    @Test(expected = MathIllegalArgumentException.class)
    public void testIntegrateMinStepNegative() throws Exception {
        integrator.setMinStep(-0.1);
        integrator.integrate(equations, 0.0, initValues, 1.0, y);
    }

    @Test(expected = MathIllegalArgumentException.class)
    public void testIntegrateMaxStepNegative() throws Exception {
        integrator.setMaxStep(-1.0);
        integrator.integrate(equations, 0.0, initValues, 1.0, y);
    }

    @Test(expected = MathIllegalArgumentException.class)
    public void testIntegrateMinStepZero() throws Exception {
        integrator.setMinStep(0.0);
        integrator.integrate(equations, 0.0, initValues, 1.0, y);
    }

    @Test
    public void testIntegrateWithEvent() throws Exception {
        integrator.setEquations(equations);
        integrator.initialize(initValues, minStep, maxStep, tolerance, absTolerance, relTolerance);
        integrator.addStepHandler(event -> {
            assertEquals(2.5, event.getTime(), 0.0);
            assertTrue(event.isTriggered());
            assertFalse(event.isDone());
        });
        integrator.integrate(equations, 0.0, initValues, 3.0, y);
    }

    @Test(expected = IntegratorException.class)
    public void testIntegrateRejectedStep() throws Exception {
        integrator.setEquations(equations);
        integrator.initialize(initValues, minStep, maxStep, tolerance, absTolerance, relTolerance);
        integrator.addStepHandler(event -> {
            assertEquals(1.5, event.getTime(), 0.0);
            assertTrue(event.isTriggered());
            assertTrue(event.isDone());
        });
        integrator.integrate(equations, 0.0, initValues, 1.5, y);
    }

    @Test
    public void testIntegrateAcceptedStep() throws Exception {
        integrator.setEquations(equations);
        integrator.initialize(initValues, minStep, maxStep, tolerance, absTolerance, relTolerance);
        integrator.addStepHandler(event -> {
            assertEquals(1.5, event.getTime(), 0.0);
            assertTrue(event.isTriggered());
            assertFalse(event.isDone());
        });
        integrator.integrate(equations, 0.0, initValues, 1.6, y);
    }

    @Test(expected = IntegratorException.class)
    public void testIntegrateWithError() throws Exception {
        integrator.setEquations(equations);
        integrator.initialize(initValues, minStep, maxStep, tolerance, absTolerance, relTolerance);
        integrator.integrate(equations, 0.0, initValues, 0.1, y);
    }

}