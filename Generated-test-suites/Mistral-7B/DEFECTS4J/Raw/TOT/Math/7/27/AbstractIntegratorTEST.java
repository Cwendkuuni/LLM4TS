package org.apache.commons.math3.ode.integrator.test;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.ode.AbstractStepInterpolator;
import org.apache.commons.math3.ode.FirstOrderIntegrator;
import org.apache.commons.math3.ode.ExpandableStatefulODE;
import org.apache.commons.math3.ode.events.DefaultEventHandler;
import org.apache.commons.math3.ode.events.EventState;
import org.apache.commons.math3.ode.events.StoppingEvent;
import org.apache.commons.math3.ode.sampling.StepHandler;
import org.apache.commons.math3.ode.sampling.StepInterpolator;
import org.apache.commons.math3.ode.util.ODETestUtils;
import org.apache.commons.math3.ode.util.StepInterpolatorBuilder;
import org.apache.commons.math3.util.Precision;

import org.apache.commons.math3.ode.AbstractIntegrator;
import org.apache.commons.math3.ode.integrator.AbstractIntegratorTest;

/**
 * Test for {@link AbstractIntegrator}.
 * @since 2.2
 */
public class AbstractIntegratorTest extends TestCase {

    /**
     * Test method for {@link AbstractIntegrator#AbstractIntegrator(String)}
     */
    public void testConstructor() {
        final AbstractIntegrator integrator = new AbstractIntegrator("TestIntegrator");
        assertNotNull(integrator);
    }

    /**
     * Test method for {@link AbstractIntegrator#getName()}
     */
    public void testGetName() {
        final AbstractIntegrator integrator = new AbstractIntegrator("TestIntegrator");
        assertEquals("TestIntegrator", integrator.getName());
    }

    /**
     * Test method for {@link AbstractIntegrator#addStepHandler(StepHandler)}
     */
    public void testAddStepHandler() {
        final AbstractIntegrator integrator = new AbstractIntegrator("TestIntegrator");
        final StepHandler handler = new StepHandler() {
            public void handleStep(StepInterpolator interpolator, boolean isLastStep) {
                // do nothing
            }
        };
        integrator.addStepHandler(handler);
        assertEquals(1, integrator.getStepHandlers().size());
    }

    /**
     * Test method for {@link AbstractIntegrator#clearStepHandlers()}
     */
    public void testClearStepHandlers() {
        final AbstractIntegrator integrator = new AbstractIntegrator("TestIntegrator");
        final StepHandler handler = new StepHandler() {
            public void handleStep(StepInterpolator interpolator, boolean isLastStep) {
                // do nothing
            }
        };
        integrator.addStepHandler(handler);
        integrator.clearStepHandlers();
        assertEquals(0, integrator.getStepHandlers().size());
    }

    /**
     * Test method for {@link AbstractIntegrator#integrate(ExpandableStatefulODE, double)}
     */
    public void testIntegrate() throws Exception {
        final double t0 = 0.0;
        final double[] y0 = { 1.0, 2.0 };
        final double t = 2.0;
        final ExpandableStatefulODE equations = new ExpandableStatefulODE() {
            public void computeDerivatives(double t, double[] y, double[] yDot) {
                yDot[0] = y[0];
                yDot[1] = y[1];
            }
        };
        equations.setTime(t0);
        equations.setPrimaryState(y0);

        final AbstractIntegrator integrator = new AbstractIntegrator("TestIntegrator") {
            @Override
            protected void integrate(ExpandableStatefulODE equations, double t)
                throws NumberIsTooSmallException, DimensionMismatchException,
                       MaxCountExceededException, NoBracketingException {
                // do nothing
            }
        };
        integrator.setStepSize(0.1);
        integrator.addStepHandler(new StepHandler() {
            public void handleStep(StepInterpolator interpolator, boolean isLastStep) {
                if (isLastStep) {
                    Assert.assertArrayEquals(new double[] { 3.0, 4.0 },
                                             interpolator.getInterpolatedState());
                }
            }
        });
        integrator.setMaxEvaluations(10);

        integrator.initIntegration(t0, y0, t);
        integrator.integrate(equations, t);

        assertEquals(10, integrator.getEvaluations());
    }

    /**
     * Test method for {@link AbstractIntegrator#integrate(ExpandableStatefulODE, double)}
     * with {@link StoppingEvent}
     */
    public void testIntegrateWithStoppingEvent() throws Exception {
        final double t0 = 0.0;
        final double[] y0 = { 1.0, 2.0 };
        final double t = 1.0;
        final ExpandableStatefulODE equations = new ExpandableStatefulODE() {
            public void computeDerivatives(double t, double[] y, double[] yDot) {
                yDot[0] = y[0];
                yDot[1] = y[1];
            }
        };
        equations.setTime(t0);
        equations.setPrimaryState(y0);

        final AbstractIntegrator integrator = new AbstractIntegrator("TestIntegrator") {
            @Override
            protected void integrate(ExpandableStatefulODE equations, double t)
                throws NumberIsTooSmallException, DimensionMismatchException,
                       MaxCountExceededException, NoBracketingException {
                // do nothing
            }
        };
        integrator.setStepSize(0.1);
        integrator.addStepHandler(new StepHandler() {
            public void handleStep(StepInterpolator interpolator, boolean isLastStep) {
                if (isLastStep) {
                    fail("Integration should have been stopped before reaching the target time.");
                }
            }
        });
        integrator.addEventHandler(new DefaultEventHandler() {
            public void handleEvent(double t, double[] y, double[] yDot, int type) {
                if (type == EventState.TYPE_STOP) {
                    assertEquals(1.0, t, Precision.SAFE_EPSILON);
                    Assert.assertArrayEquals(new double[] { 1.0, 1.0 }, y);
                }
            }
        }, 0.5, 1.0e-6, 10, new PolynomialFunction(1.0));
        integrator.setMaxEvaluations(10);

        integrator.initIntegration(t0, y0, t);
        integrator.integrate(equations, t);

        assertEquals(10, integrator.getEvaluations());
    }

    /**
     * Test method for {@link AbstractIntegrator#integrate(ExpandableStatefulODE, double)}
     * with {@link StoppingEvent} and {@link StepInterpolatorBuilder}
     */
    public void testIntegrateWithStoppingEventAndInterpolatorBuilder() throws Exception {
        final double t0 = 0.0;
        final double[] y0 = { 1.0, 2.0 };
        final double t = 1.0;
        final ExpandableStatefulODE equations = new ExpandableStatefulODE() {
            public void computeDerivatives(double t, double[] y, double[] yDot) {
                yDot[0] = y[0];
                yDot[1] = y[1];
            }
        };
        equations.setTime(t0);
        equations.setPrimaryState(y0);

        final AbstractIntegrator integrator = new AbstractIntegrator("TestIntegrator") {
            @Override
            protected void integrate(ExpandableStatefulODE equations, double t)
                throws NumberIsTooSmallException, DimensionMismatchException,
                       MaxCountExceededException, NoBracketingException {
                // do nothing
            }
        };
        integrator.setStepSize(0.1);
        integrator.addStepHandler(new StepHandler() {
            public void handleStep(StepInterpolator interpolator, boolean isLastStep) {
                if (isLastStep) {
                    fail("Integration should have been stopped before reaching the target time.");
                }
            }
        });
        integrator.addEventHandler(new DefaultEventHandler() {
            public void handleEvent(double t, double[] y, double[] yDot, int type) {
                if (type == EventState.TYPE_STOP) {
                    assertEquals(1.0, t, Precision.SAFE_EPSILON);
                    Assert.assertArrayEquals(new double[] { 1.0, 1.0 }, y);
                }
            }
        }, 0.5, 1.0e-6, 10, new PolynomialFunction(1.0));
        integrator.setMaxEvaluations(10);

        final StepInterpolator interpolator = new StepInterpolator(new AbstractStepInterpolator() {
            protected double interpolate(double t, double[] y) {
                // do nothing
                return t;
            }
        });

        integrator.initIntegration(t0, y0, t, interpolator);
        integrator.integrate(equations, t);

        assertEquals(10, integrator.getEvaluations());
    }

}