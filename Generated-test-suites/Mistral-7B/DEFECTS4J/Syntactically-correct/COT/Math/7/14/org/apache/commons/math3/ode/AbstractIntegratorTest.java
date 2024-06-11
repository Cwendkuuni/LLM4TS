package org.apache.commons.math3.ode.test;

import junit.framework.Assert;
import org.apache.commons.math3.ode.*;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * Test class for AbstractIntegrator.
 */
public class AbstractIntegratorTest {
    @Test
    public void testConstructors() {
        AbstractIntegrator integrator = new AbstractIntegrator();
        AbstractIntegrator integratorWithName = new AbstractIntegrator("IntegratorTest");
        Assert.assertNull(integrator.getName());
        Assert.assertEquals("IntegratorTest", integratorWithName.getName());
    }

    @Test
    public void testAddAndGetStepHandlers() {
        AbstractIntegrator integrator = new AbstractIntegrator();
        StepHandler stepHandler = new StepHandler() {
            @Override
            public void handleStep(StepHandlerContext context, boolean lastStep) {
                // do nothing
            }
        };
        integrator.addStepHandler(stepHandler);
        Collection<StepHandler> stepHandlers = integrator.getStepHandlers();
        Assert.assertEquals(1, stepHandlers.size());
        Assert.assertEquals(stepHandler, stepHandlers.iterator().next());
    }

    @Test
    public void testAddAndGetEventHandlers() {
        AbstractIntegrator integrator = new AbstractIntegrator();
        EventHandler eventHandler = new EventHandler() {
            @Override
            public void handleEvent(double time, double[] state) {
                // do nothing
            }

            @Override
            public EventState reinitializeBegin(StepInterpolator interpolator) {
                return new EventState() {
                    @Override
                    public boolean evaluateStep(StepInterpolator interpolator) {
                        return false;
                    }

                    @Override
                    public void stepAccepted(double time, double[] state) {
                        // do nothing
                    }

                    @Override
                    public void stop() {
                        // do nothing
                    }

                    @Override
                    public boolean reset(double time, double[] state) {
                        return false;
                    }

                    @Override
                    public double getEventTime() {
                        return 0;
                    }

                    @Override
                    public EventHandler getEventHandler() {
                        return this;
                    }
                };
            }
        };
        integrator.addEventHandler(eventHandler, 0, 1, 10, new BracketingNthOrderBrentSolver(1, 5));
        Collection<EventHandler> eventHandlers = integrator.getEventHandlers();
        Assert.assertEquals(1, eventHandlers.size());
        Assert.assertEquals(eventHandler, eventHandlers.iterator().next().getEventHandler());
    }

    @Test
    public void testInitializeIntegration() {
        AbstractIntegrator integrator = new AbstractIntegrator();
        integrator.initIntegration(0, new double[0], 1);
        Assert.assertFalse(integrator.isStateInitialized());
    }

    @Test(expected = DimensionMismatchException.class)
    public void testIntegrateWithDimensionMismatch() throws Exception {
        AbstractIntegrator integrator = new AbstractIntegrator();
        ExpandableStatefulODE equations = new ExpandableStatefulODE() {
            @Override
            public int getDimension() {
                return 2;
            }

            @Override
            public void computeDerivatives(double time, double[] state, double[] derivatives) {
                // do nothing
            }
        };
        integrator.setEquations(equations);
        integrator.integrate(equations, 0, new double[1], 1, new double[1]);
    }

    @Test
    public void testIntegrateHappyPath() throws Exception {
        AbstractIntegrator integrator = new AbstractIntegrator();
        ExpandableStatefulODE equations = new ExpandableStatefulODE() {
            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public void computeDerivatives(double time, double[] state, double[] derivatives) {
                derivatives[0] = state[0] * 0.1;
            }
        };
        integrator.setEquations(equations);
        double initialTime = 0;
        double[] initialState = {1};
        double finalTime = 3;
        double[] finalState = {1.3};
        integrator.integrate(equations, initialTime, initialState, finalTime, finalState);
        assertEquals(initialState[0], finalState[0]);
        assertEquals(FastMath.round(finalState[0] * 100.0) / 100.0, 1.3, 0.001);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testIntegrateWithSmallStep() throws Exception {
        AbstractIntegrator integrator = new AbstractIntegrator();
        ExpandableStatefulODE equations = new ExpandableStatefulODE() {
            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public void computeDerivatives(double time, double[] state, double[] derivatives) {
                derivatives[0] = state[0] * 0.1;
            }
        };
        integrator.setEquations(equations);
        integrator.setMaxEvaluations(-1);
        double initialTime = 0;
        double[] initialState = {1};
        double finalTime = 1e-10;
        double[] finalState = {1};
        integrator.integrate(equations, initialTime, initialState, finalTime, finalState);
    }

    @Test(expected = MaxCountExceededException.class)
    public void testIntegrateWithMaxEvaluations() throws Exception {
        AbstractIntegrator integrator = new AbstractIntegrator();
        ExpandableStatefulODE equations = new ExpandableStatefulODE() {
            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public void computeDerivatives(double time, double[] state, double[] derivatives) {
                derivatives[0] = state[0] * 0.1;
            }
        };
        integrator.setEquations(equations);
        integrator.setMaxEvaluations(1);
        double initialTime = 0;
        double[] initialState = {1};
        double finalTime = 3;
        double[] finalState = {1.3};
        integrator.integrate(equations, initialTime, initialState, finalTime, finalState);
    }

    @Test(expected = NoBracketingException.class)
    public void testIntegrateWithNoBracketing() throws Exception {
        AbstractIntegrator integrator = new AbstractIntegrator();
        ExpandableStatefulODE equations = new ExpandableStatefulODE() {
            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public void computeDerivatives(double time, double[] state, double[] derivatives) {
                derivatives[0] = state[0] * 0.1;
            }

            @Override
            public void setTime(double time) {
                // do nothing
            }

            @Override
            public void setPrimaryState(double[] state) {
                // do nothing
            }

            @Override
            public double[] getPrimaryState() {
                return new double[0];
            }

            @Override
            public double getTime() {
                return 0;
            }
        };
        integrator.setEquations(equations);
        integrator.integrate(equations, 0, new double[1], 1, new double[1]);
    }

    @Test
    public void testAcceptStep() throws Exception {
        AbstractIntegrator integrator = new AbstractIntegrator();
        StepHandler stepHandler = new StepHandler() {
            @Override
            public void handleStep(StepHandlerContext context, boolean lastStep) {
                Assert.assertTrue(lastStep);
            }
        };
        integrator.addStepHandler(stepHandler);
        ExpandableStatefulODE equations = new ExpandableStatefulODE() {
            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public void computeDerivatives(double time, double[] state, double[] derivatives) {
                derivatives[0] = state[0] * 0.1;
            }
        };
        integrator.setEquations(equations);
        integrator.setMaxEvaluations(-1);
        double initialTime = 0;
        double[] initialState = {1};
        double finalTime = 3;
        double[] finalState = {1.3};
        AbstractStepInterpolator interpolator = new AbstractStepInterpolator() {
            @Override
            public double getGlobalPreviousTime() {
                return initialTime;
            }

            @Override
            public double getGlobalCurrentTime() {
                return initialTime;
            }

            @Override
            public double interpolate(double time) {
                return initialTime;
            }

            @Override
            public double[] interpolateState(double time) {
                return initialState;
            }

            @Override
            public double[] interpolateDerivatives(double time) {
                return new double[0];
            }

            @Override
            public void initialize(double time, double[] state, double[] derivatives) {
                // do nothing
            }
        };
        integrator.acceptStep(interpolator, initialState, new double[1], finalTime);
        stepHandler.handleStep(null, true);
    }

    @Test
    public void testSanityChecks() throws Exception {
        AbstractIntegrator integrator = new AbstractIntegrator();
        ExpandableStatefulODE equations = new ExpandableStatefulODE() {
            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public void computeDerivatives(double time, double[] state, double[] derivatives) {
                derivatives[0] = state[0] * 0.1;
            }
        };
        integrator.setEquations(equations);
        integrator.sanityChecks(equations, 1);
    }
}