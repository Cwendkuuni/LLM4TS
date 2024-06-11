package org.apache.commons.math.ode.test;

import org.apache.commons.math3.analysis.FunctionUtils;
import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.analysis.solvers.BrentSolver;
import org.apache.commons.math3.ode.AbstractStepInterpolator;
import org.apache.commons.math3.ode.ExpandableStatefulODE;
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.StepHandler;
import org.apache.commons.math3.ode.events.EventHandler;
import org.apache.commons.math3.ode.events.EventState;
import org.apache.commons.math3.ode.sampling.StepHandlerAdapter;
import org.apache.commons.math3.special.BesselFunctions;
import org.apache.commons.math3.test.AbstractMathTestCase;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Test class for AbstractIntegrator.
 */
public class AbstractIntegratorTest extends AbstractMathTestCase {

    /**
     * The integrator under test.
     */
    private AbstractIntegrator integrator;

    /**
     * The equations to integrate.
     */
    private FirstOrderDifferentialEquations equations;

    /**
     * The integrator step handlers.
     */
    private StepHandlerAdapter stepHandler;

    /**
     * The event handlers.
     */
    private EventHandler eventHandler;

    /**
     * Set up the test.
     */
    @Before
    public void setUp() {
        integrator = new AbstractIntegrator() {
            @Override
            public void integrate(ExpandableStatefulODE equations, double t)
                    throws MathIllegalStateException, MathIllegalArgumentException {
                // Empty implementation
            }
        };

        equations = new ExpandableStatefulODE() {
            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yDot) {
                yDot[0] = 2 * y[0];
            }

            @Override
            public double getTime() {
                return 0;
            }

            @Override
            public void setPrimaryState(double[] y) {
                this.y = y;
            }

            @Override
            public double[] getPrimaryState() {
                return y;
            }

            @Override
            public void setTime(double t) {
                this.t = t;
            }

            private double y[];
            private double t;
        };

        stepHandler = new StepHandlerAdapter();
        integrator.addStepHandler(stepHandler);
    }

    /**
     * Test the constructor.
     */
    @Test
    public void testConstructor() {
        assertNotNull(integrator);
    }

    /**
     * Test the addStepHandler method.
     */
    @Test
    public void testAddStepHandler() {
        integrator.addStepHandler(stepHandler);
        assertTrue(integrator.getStepHandlers().contains(stepHandler));
    }

    /**
     * Test the clearStepHandlers method.
     */
    @Test
    public void testClearStepHandlers() {
        integrator.addStepHandler(stepHandler);
        integrator.clearStepHandlers();
        assertFalse(integrator.getStepHandlers().contains(stepHandler));
    }

    /**
     * Test the getCurrentSignedStepsize method.
     */
    @Test
    public void testGetCurrentSignedStepsize() {
        integrator.setMaxEvaluations(10);
        integrator.setCurrentSignedStepsize(1.0);
        assertEquals(1.0, integrator.getCurrentSignedStepsize(), 1.0e-10);
    }

    /**
     * Test the setEquations method.
     */
    @Test
    public void testSetEquations() {
        integrator.setEquations(equations);
        assertNotNull(integrator.getEquations());
        assertEquals(1, integrator.getEquations().getDimension());
    }

    /**
     * Test the integrate method with a simple differential equation.
     */
    @Test
    public void testIntegrateSimple() throws Exception {
        final double t0 = 0.0;
        final double[] y0 = {1.0};
        final double t = 2.0;

        // Integrate the equations
        integrator.integrate(equations, t);

        // Check the results
        final double y = y0[0] * Math.exp(2.0 * (t - t0));
        assertEquals(y, equations.getPrimaryState()[0], 1.0e-10);
        assertEquals(t, integrator.getCurrentStepStart(), 1.0e-10);
    }

    /**
     * Test the integrate method with an exception.
     */
    @Test(expected = MathIllegalArgumentException.class)
    public void testIntegrateException() throws Exception {
        final double t0 = 0.0;
        final double[] y0 = {1.0};
        final double t = -1.0;

        // Integrate the equations
        integrator.integrate(equations, t);
    }

    /**
     * Test the integrate method with a differential equation with an event.
     */
    @Test
    public void testIntegrateEvent() throws Exception {
        final double t0 = 0.0;
        final double[] y0 = {1.0};
        final double t1 = 1.0;
        final double t2 = 2.0;

        // Define an event handler
        eventHandler = new EventHandler() {
            @Override
            public void evaluateState(double t, double[] y, EventState state) {
                state.setTriggered(y[0] > 1.5);
            }

            @Override
            public void handleEvent(double t, double[] y) {
                y[0] = 0.0;
            }
        };

        integrator.addEventHandler(eventHandler, 1.0, 0.1, 10, new BrentSolver());

        // Integrate the equations
        integrator.integrate(equations, t1);
        integrator.setCurrentStepStart(t1);

        // Check that the event is triggered
        assertEquals(y0[0], equations.getPrimaryState()[0], 1.0e-10);
        assertFalse(eventHandler.isTriggered());

        integrator.integrate(equations, t2);

        // Check the results
        assertEquals(0.0, equations.getPrimaryState()[0], 1.0e-10);
        assertEquals(t2, integrator.getCurrentStepStart(), 1.0e-10);
        assertTrue(eventHandler.isTriggered());
    }

    /**
     * Test the integrate method with a differential equation with a step handler.
     */
    @Test
    public void testIntegrateStepHandler() throws Exception {
        final double t0 = 0.0;
        final double[] y0 = {1.0};
        final double t1 = 1.0;
        final double t2 = 2.0;

        // Define a step handler
        stepHandler = new StepHandlerAdapter() {
            @Override
            public void handleStep(double t, double[] y, double[] yDot) {
                yDot[0] += 1.0;
            }
        };

        integrator.addStepHandler(stepHandler);

        // Integrate the equations
        integrator.integrate(equations, t0);

        // Check the results
        assertEquals(y0[0], equations.getPrimaryState()[0], 1.0e-10);
        assertEquals(t0, integrator.getCurrentStepStart(), 1.0e-10);
        assertArrayEquals(new double[] {2.0}, equations.getDerivatives(), 1.0e-10);

        integrator.integrate(equations, t1);

        // Check the results
        assertEquals(2.0, equations.getPrimaryState()[0], 1.0e-10);
        assertEquals(t1, integrator.getCurrentStepStart(), 1.0e-10);
        assertArrayEquals(new double[] {3.0}, equations.getDerivatives(), 1.0e-10);

        integrator.integrate(equations, t2);

        // Check the results
        assertEquals(3.0, equations.getPrimaryState()[0], 1.0e-10);
        assertEquals(t2, integrator.getCurrentStepStart(), 1.0e-10);
        assertArrayEquals(new double[] {4.0}, equations.getDerivatives(), 1.0e-10);
    }

    /**
     * Test the getEvaluations method.
     */
    @Test
    public void testGetEvaluations() {
        integrator.resetEvaluations();
        assertEquals(0, integrator.getEvaluations());

        integrator.computeDerivatives(0.0, new double[] {0.0}, new double[] {0.0});
        assertEquals(1, integrator.getEvaluations());

        integrator.computeDerivatives(0.0, new double[] {0.0}, new double[] {0.0});
        assertEquals(2, integrator.getEvaluations());
    }

    /**
     * Test the setMaxEvaluations method.
     */
    @Test
    public void testSetMaxEvaluations() {
        integrator.setMaxEvaluations(5);
        assertEquals(5, integrator.getMaxEvaluations());
    }

    /**
     * Test the resetEvaluations method.
     */
    @Test
    public void testResetEvaluations() {
        integrator.computeDerivatives(0.0, new double[] {0.0}, new double[] {0.0});
        integrator.computeDerivatives(0.0, new double[] {0.0}, new double[] {0.0});
        assertEquals(2, integrator.getEvaluations());

        integrator.resetEvaluations();
        assertEquals(0, integrator.getEvaluations());
    }

    /**
     * Test the acceptStep method.
     */
    @Test
    public void testAcceptStep() throws Exception {
        final double[] initialY = new double[] {0.0};
        final double[] initialYDot = new double[] {0.0};

        final AbstractStepInterpolator interpolator = new AbstractStepInterpolator() {
            @Override
            public double getInterpolatedTime() {
                return 0.0;
            }

            @Override
            public double[] getInterpolatedState() {
                return initialY;
            }

            @Override
            public double[] getInterpolatedDerivatives() {
                return initialYDot;
            }

            @Override
            public double getGlobalPreviousTime() {
                return 0.0;
            }

            @Override
            public double getGlobalCurrentTime() {
                return 0.0;
            }
        };

        integrator.integrate(equations, 1.0);

        integrator.acceptStep(interpolator, initialY, initialYDot, 0.1);

        // Check the results
        assertEquals(0.1, integrator.getCurrentStepStart(), 1.0e-10);
        assertEquals(integrator.getEquations().getDerivatives()[0], initialYDot[0], 1.0e-10);
        assertTrue(integrator.isLastStep());
    }

    /**
     * Test the acceptStep method with an exception.
     */
    @Test(expected = MathIllegalStateException.class)
    public void testAcceptStepException() throws Exception {
        final double[] initialY = new double[] {0.0};
        final double[] initialYDot = new double[] {0.0};

        final AbstractStepInterpolator interpolator = new AbstractStepInterpolator() {
            @Override
            public double getInterpolatedTime() {
                return 0.0;
            }

            @Override
            public double[] getInterpolatedState() {
                return initialY;
            }

            @Override
            public double[] getInterpolatedDerivatives() {
                return initialYDot;
            }

            @Override
            public double getGlobalPreviousTime() {
                return 0.0;
            }

            @Override
            public double getGlobalCurrentTime() {
                throw new RuntimeException();
            }
        };

        integrator.integrate(equations, 1.0);

        integrator.acceptStep(interpolator, initialY, initialYDot, 0.1);
    }

    /**
     * Test the acceptStep method with a step that triggers a reset.
     */
    @Test
    public void testAcceptStepReset() throws Exception {
        final double[] initialY = new double[] {1.0};
        final double[] initialYDot = new double[] {1.0};

        final AbstractStepInterpolator interpolator = new AbstractStepInterpolator() {
            @Override
            public double getInterpolatedTime() {
                return 1.0;
            }

            @Override
            public double[] getInterpolatedState() {
                return new double[] {2.0};
            }

            @Override
            public double[] getInterpolatedDerivatives() {
                return new double[] {1.5};
            }

            @Override
            public double getGlobalPreviousTime() {
                return 0.0;
            }

            @Override
            public double getGlobalCurrentTime() {
                return 1.0;
            }
        };

        integrator.integrate(equations, 0.0);

        integrator.acceptStep(interpolator, initialY, initialYDot, 1.0);

        // Check the results
        assertEquals(1.0, integrator.getCurrentStepStart(), 1.0e-10);
        assertArrayEquals(new double[] {2.0}, equations.getPrimaryState(), 1.0e-10);
        assertArrayEquals(new double[] {1.5}, equations.getDerivatives(), 1.0e-10);
        assertTrue(integrator.isLastStep());
        assertFalse(integrator.resetOccurred());

        integrator.acceptStep(interpolator, initialY, initialYDot, 1.0);

        // Check the results
        assertEquals(1.0, integrator.getCurrentStepStart(), 1.0e-10);
        assertArrayEquals(new double[] {1.0}, equations.getPrimaryState(), 1.0e-10);
        assertArrayEquals(new double[] {2.0}, equations.getDerivatives(), 1.0e-10);
        assertTrue(integrator.isLastStep());
        assertTrue(integrator.resetOccurred());
    }
}