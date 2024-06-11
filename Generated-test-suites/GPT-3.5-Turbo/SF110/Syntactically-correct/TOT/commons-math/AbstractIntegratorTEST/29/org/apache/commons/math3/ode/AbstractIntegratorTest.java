package org.apache.commons.math3.ode;

import org.junit.Before;
import org.junit.Test;
import org.junit.Assert;

public class AbstractIntegratorTest {

    private AbstractIntegrator integrator;

    @Before
    public void setup() {
        integrator = new TestIntegrator();
    }

    @Test
    public void testGetName() {
        String expectedName = "Test Integrator";
        Assert.assertEquals(expectedName, integrator.getName());
    }

    @Test
    public void testAddStepHandler() {
        StepHandler handler = new TestStepHandler();
        integrator.addStepHandler(handler);

        Assert.assertTrue(integrator.getStepHandlers().contains(handler));
    }

    @Test
    public void testGetStepHandlers() {
        StepHandler handler1 = new TestStepHandler();
        StepHandler handler2 = new TestStepHandler();

        integrator.addStepHandler(handler1);
        integrator.addStepHandler(handler2);

        Assert.assertEquals(2, integrator.getStepHandlers().size());
        Assert.assertTrue(integrator.getStepHandlers().contains(handler1));
        Assert.assertTrue(integrator.getStepHandlers().contains(handler2));
    }

    @Test
    public void testClearStepHandlers() {
        StepHandler handler1 = new TestStepHandler();
        StepHandler handler2 = new TestStepHandler();

        integrator.addStepHandler(handler1);
        integrator.addStepHandler(handler2);

        integrator.clearStepHandlers();

        Assert.assertEquals(0, integrator.getStepHandlers().size());
    }

    @Test
    public void testAddEventHandler() {
        EventHandler eventHandler = new TestEventHandler();
        double maxCheckInterval = 0.1;
        double convergence = 0.01;
        int maxIterationCount = 5;

        integrator.addEventHandler(eventHandler, maxCheckInterval, convergence, maxIterationCount);

        Collection<EventHandler> eventHandlers = integrator.getEventHandlers();
        Assert.assertEquals(1, eventHandlers.size());
        Assert.assertTrue(eventHandlers.contains(eventHandler));
    }

    @Test
    public void testGetEventHandlers() {
        EventHandler eventHandler1 = new TestEventHandler();
        EventHandler eventHandler2 = new TestEventHandler();
        double maxCheckInterval = 0.1;
        double convergence = 0.01;
        int maxIterationCount = 5;

        integrator.addEventHandler(eventHandler1, maxCheckInterval, convergence, maxIterationCount);
        integrator.addEventHandler(eventHandler2, maxCheckInterval, convergence, maxIterationCount);

        Collection<EventHandler> eventHandlers = integrator.getEventHandlers();
        Assert.assertEquals(2, eventHandlers.size());
        Assert.assertTrue(eventHandlers.contains(eventHandler1));
        Assert.assertTrue(eventHandlers.contains(eventHandler2));
    }

    @Test
    public void testClearEventHandlers() {
        EventHandler eventHandler1 = new TestEventHandler();
        EventHandler eventHandler2 = new TestEventHandler();
        double maxCheckInterval = 0.1;
        double convergence = 0.01;
        int maxIterationCount = 5;

        integrator.addEventHandler(eventHandler1, maxCheckInterval, convergence, maxIterationCount);
        integrator.addEventHandler(eventHandler2, maxCheckInterval, convergence, maxIterationCount);

        integrator.clearEventHandlers();

        Assert.assertEquals(0, integrator.getEventHandlers().size());
    }

    @Test
    public void testGetMaxEvaluations() {
        int expectedMaxEvaluations = Integer.MAX_VALUE;
        Assert.assertEquals(expectedMaxEvaluations, integrator.getMaxEvaluations());
    }

    @Test
    public void testSetMaxEvaluations() {
        int maxEvaluations = 1000;
        integrator.setMaxEvaluations(maxEvaluations);

        Assert.assertEquals(maxEvaluations, integrator.getMaxEvaluations());
    }

    @Test
    public void testGetEvaluations() {
        int expectedEvaluations = 0;
        Assert.assertEquals(expectedEvaluations, integrator.getEvaluations());
    }

    @Test
    public void testIntegrate() {
        // test case implementation
    }

    @Test
    public void testComputeDerivatives() {
        // test case implementation
    }

    @Test
    public void testAcceptStep() {
        // test case implementation
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testSanityChecksDimensionMismatchException() {
        double[] y0 = {1.0, 2.0, 3.0};
        double[] y = {0.0, 0.0, 0.0};
        double[] yDot = new double[3];
        int dimension = 4;

        ExpandableStatefulODE equations = new ExpandableStatefulODE(new TestEquations());
        equations.setTime(0.0);
        equations.setPrimaryState(y0);

        integrator.sanityChecks(equations, 1.0);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testSanityChecksNumberIsTooSmallException() {
        double[] y0 = {1.0, 2.0, 3.0};
        double[] y = {0.0, 0.0, 0.0};
        double[] yDot = new double[3];

        ExpandableStatefulODE equations = new ExpandableStatefulODE(new TestEquations());
        equations.setTime(0.0);
        equations.setPrimaryState(y0);

        integrator.sanityChecks(equations, 0.00000001);
    }

    private class TestIntegrator extends AbstractIntegrator {

        @Override
        public void integrate(ExpandableStatefulODE equations, double t) throws NumberIsTooSmallException, DimensionMismatchException, MaxCountExceededException, NoBracketingException {
            // test case implementation
        }
    }

    private class TestStepHandler implements StepHandler {

        @Override
        public void init(double t0, double[] y0, double t) {
            // test case implementation
        }

        @Override
        public void handleStep(StepInterpolator interpolator, boolean isLastStep) {
            // test case implementation
        }
    }

    private class TestEventHandler implements EventHandler {

        @Override
        public void init(double t0, double[] y0, double t) {
            // test case implementation
        }

        @Override
        public double g(double t, double[] y) {
            // test case implementation
        }

        @Override
        public Action eventOccurred(double t, double[] y, boolean increasing) {
            // test case implementation
        }

        @Override
        public void resetState(double t, double[] y) {
            // test case implementation
        }
    }

    private class TestEquations implements FirstOrderDifferentialEquations {

        @Override
        public int getDimension() {
            return 3;
        }

        @Override
        public void computeDerivatives(double t, double[] y, double[] yDot) {
            // test case implementation
        }
    }
}