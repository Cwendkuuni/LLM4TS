package org.apache.commons.math.ode.nonstiff.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.PrecisionExceededException;
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.events.Event;
import org.apache.commons.math3.ode.events.IntervalEvent;
import org.apache.commons.math3.ode.nonstiff.EmbeddedRungeKuttaIntegrator;
import org.apache.commons.math3.ode.nonstiff.RungeKuttaStepInterpolator;
import org.apache.commons.math3.ode.sampling.StepHandler;
import org.apache.commons.math3.ode.test.DummyEquations;
import org.apache.commons.math3.ode.test.TestEmbeddedRungeKuttaIntegrator;
import org.junit.Test;

/**
 * Test cases for the {@link EmbeddedRungeKuttaIntegrator} class.
 */
public class EmbeddedRungeKuttaIntegratorTest {

    /**  */

    @Test
    public void testConstructor() {
        EmbeddedRungeKuttaIntegrator integrator = new TestEmbeddedRungeKuttaIntegrator(
                2, new double[]{0.23, 0.36, 0.52}, new double[][]{
                        {0.23, 0.46, 0.32}, {0.36, 0.67, 0.38}, {0.52, 0.83, 0.49}},
                        new double[]{0.23, 0.47, 0.47}, new RungeKuttaStepInterpolator());
        assertNotNull(integrator);
    }

    @Test
    public void testGetOrder() {
        EmbeddedRungeKuttaIntegrator integrator = new TestEmbeddedRungeKuttaIntegrator(2, null, null, null, null, null, 1.2, null, null);
        assertEquals(2, integrator.getOrder());
    }

    @Test
    public void testGetSafety() {
        EmbeddedRungeKuttaIntegrator integrator = new TestEmbeddedRungeKuttaIntegrator(2, null, null, null, null, null, 1.2, null, null);
        assertEquals(0.9, integrator.getSafety(), 0.001);
    }

    @Test
    public void testSetSafety() {
        EmbeddedRungeKuttaIntegrator integrator = new TestEmbeddedRungeKuttaIntegrator(2, null, null, null, null, null, 1.2, null, null);
        integrator.setSafety(0.8);
        assertEquals(0.8, integrator.getSafety(), 0.001);
    }

    @Test
    public void testGetMinReduction() {
        EmbeddedRungeKuttaIntegrator integrator = new TestEmbeddedRungeKuttaIntegrator(2, null, null, null, null, null, 1.2, null, null);
        assertEquals(0.2, integrator.getMinReduction(), 0.001);
    }

    @Test
    public void testSetMinReduction() {
        EmbeddedRungeKuttaIntegrator integrator = new TestEmbeddedRungeKuttaIntegrator(2, null, null, null, null, null, 1.2, null, null);
        integrator.setMinReduction(0.3);
        assertEquals(0.3, integrator.getMinReduction(), 0.001);
    }

    @Test
    public void testGetMaxGrowth() {
        EmbeddedRungeKuttaIntegrator integrator = new TestEmbeddedRungeKuttaIntegrator(2, null, null, null, null, null, 1.2, null, null);
        assertEquals(10.0, integrator.getMaxGrowth(), 0.001);
    }

    @Test
    public void testSetMaxGrowth() {
        EmbeddedRungeKuttaIntegrator integrator = new TestEmbeddedRungeKuttaIntegrator(2, null, null, null, null, null, 1.2, null, null);
        integrator.setMaxGrowth(5.0);
        assertEquals(5.0, integrator.getMaxGrowth(), 0.001);
    }

    @Test
    public void testIntegrateWithNoStepHandlers() {
        EmbeddedRungeKuttaIntegrator integrator = new TestEmbeddedRungeKuttaIntegrator(2, null, null, null, null, null, 1.2, null, null);
        FirstOrderDifferentialEquations equations = new DummyEquations();
        double[] y0 = {1.0, 1.0};
        double t = 10.0;
        double result = integrator.integrate(equations, 0.0, y0, t, new double[y0.length]);
        assertEquals(t, result, 1e-9);
    }

    @Test
    public void testIntegrateWithEndTimeEvent() {
        EmbeddedRungeKuttaIntegrator integrator = new TestEmbeddedRungeKuttaIntegrator(2, null, null, null, null, null, 1.2, null, null);
        FirstOrderDifferentialEquations equations = new DummyEquations();
        double[] y0 = {1.0, 1.0};
        double t = 5.0;
        double step = 0.1;
        integrator.addStepHandler(new StepHandler() {
            @Override
            public void handleStep(StepHandlerContext context) {
                if (context.getStepNumber() == 5 && context.isLastStep()) {
                    throw new InterruptedException("Test Interrupted Exception");
                }
            }
        });
        integrator.addEndTimeChecker(t, t, new StepHandler[0]);
        StepHandler[] stepHandlers = integrator.getStepHandlers();
        assertEquals(1, stepHandlers.length);
        double[] y = new double[y0.length];
        try {
            integrator.integrate(equations, 0.0, y0, t, y);
            fail("Expected IntegratorException");
        } catch (IntegratorException e) {
            // pass
        }
    }

    @Test
    public void testIntegrateWithEventInMiddle() {
        EmbeddedRungeKuttaIntegrator integrator = new TestEmbeddedRungeKuttaIntegrator(2, null, null, null, null, null, 1.2, null, null);
        FirstOrderDifferentialEquations equations = new DummyEquations();
        double[] y0 = {1.0, 1.0};
        double t = 10.0;
        double step = 0.1;
        integrator.addStepHandler(new StepHandler() {
            @Override
            public void handleStep(StepHandlerContext context) {
                if (context.getStepNumber() == 5) {
                    throw new InterruptedException("Test Interrupted Exception");
                }
            }
        });
        integrator.addEvent(new IntervalEvent(4.0, 4.5, new Event() {
            @Override
            public void evaluateEvent(double t, double[] y, double[] yDot) {
                throw new MaxCountExceededException();
            }
        }));
        StepHandler[] stepHandlers = integrator.getStepHandlers();
        assertEquals(2, stepHandlers.length);
        double[] y = new double[y0.length];
        try {
            integrator.integrate(equations, 0.0, y0, t, y);
            fail("Expected PrecisionExceededException or IntegratorException");
        } catch (Exception e) {
            // pass
        }
    }

    @Test
    public void testIntegrateWithStepSizeControl() {
        EmbeddedRungeKuttaIntegrator integrator = new TestEmbeddedRungeKuttaIntegrator(2, null, null, null, null, null, 1.2, null, null);
        FirstOrderDifferentialEquations equations = new DummyEquations();
        double[] y0 = {1.0, 1.0};
        double t = 10.0;
        double step = 0.1;
        integrator.addStepHandler(new StepHandler() {
            @Override
            public void handleStep(StepHandlerContext context) {
                if (context.isLastStep()) {
                    throw new InterruptedException("Test Interrupted Exception");
                }
            }
        });
        integrator.addEndTimeChecker(t, t, new StepHandler[0]);
        StepHandler[] stepHandlers = integrator.getStepHandlers();
        assertEquals(2, stepHandlers.length);
        double h = 0.1;
        double stepSize = integrator.filterStep(h, true, false);
        assertTrue(h > stepSize);
        h = 0.01;
        stepSize = integrator.filterStep(h, true, false);
        assertTrue(h > stepSize);
        h = 0.001;
        stepSize = integrator.filterStep(h, true, false);
        assertTrue(h > stepSize);
        h = 0.0001;
        stepSize = integrator.filterStep(h, true, false);
        assertTrue(h > stepSize);
        h = 0.00001;
        stepSize = integrator.filterStep(h, true, false);
        assertTrue(h > stepSize);
        h = 1.0;
        stepSize = integrator.filterStep(h, true, true);
        assertTrue(h <= stepSize);
        h = 2.0;
        stepSize = integrator.filterStep(h, true, true);
        assertTrue(h <= stepSize);
        h = 5.0;
        stepSize = integrator.filterStep(h, true, true);
        assertTrue(h <= stepSize);
    }

    @Test
    public void testIntegrateWithNoEvents() {
        EmbeddedRungeKuttaIntegrator integrator = new TestEmbeddedRungeKuttaIntegrator(2, null, null, null, null, null, 1.2, null, null);
        FirstOrderDifferentialEquations equations = new DummyEquations();
        double[] y0 = {1.0, 1.0};
        double t = 10.0;
        double step = 0.1;
        integrator.addStepHandler(new StepHandler() {
            @Override
            public void handleStep(StepHandlerContext context) {
                if (context.isLastStep()) {
                    throw new InterruptedException("Test Interrupted Exception");
                }
            }
        });
        integrator.addEndTimeChecker(t, t, new StepHandler[0]);
        StepHandler[] stepHandlers = integrator.getStepHandlers();
        assertEquals(2, stepHandlers.length);
        double[] y = new double[y0.length];
        try {
            integrator.integrate(equations, 0.0, y0, t, y);
        } catch (Exception e) {
            fail("Expected no exception");
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorNullPrototype() {
        new TestEmbeddedRungeKuttaIntegrator(2, null, null, null, null, null, 1.2, null, null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorNegativeOrder() {
        new TestEmbeddedRungeKuttaIntegrator(-1, null, null, null, null, null, 1.2, null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorNegativeMinStep() {
        new TestEmbeddedRungeKuttaIntegrator(2, null, null, null, null, null, 1.2, -1.0, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorNegativeMaxStep() {
        new TestEmbeddedRungeKuttaIntegrator(2, null, null, null, null, null, 1.2, null, -1.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorNullScalAbsoluteTolerance() {
        new TestEmbeddedRungeKuttaIntegrator(2, null, null, null, null, null, 1.2, null, null, null, (double[]) null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorNullScalRelativeTolerance() {
        new TestEmbeddedRungeKuttaIntegrator(2, null, null, null, null, null, 1.2, null, null, null, null, (double[]) null);
    }

    /**  */
}