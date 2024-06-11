import org.apache.commons.math3.ode.*;
import org.apache.commons.math3.ode.events.EventHandler;
import org.apache.commons.math3.ode.events.MockEventHandler;
import org.apache.commons.math3.ode.sampling.AbstractStepInterpolator;
import org.apache.commons.math3.ode.sampling.StepHandler;
import org.apache.commons.math3.ode.sampling.StepInterpolatorAdapter;
import org.junit.Assert;
import org.junit.Test;

import java.util.*;

public class AbstractIntegratorTest {

    @Test
    public void testAbstractIntegrator_getName_ReturnsName() {
        AbstractIntegrator integrator = new MyAbstractIntegrator("MyIntegrator");
        Assert.assertEquals("MyIntegrator", integrator.getName());
    }

    @Test
    public void testAbstractIntegrator_addStepHandler_AddsStepHandler() {
        AbstractIntegrator integrator = new MyAbstractIntegrator();
        StepHandler handler = new MockStepHandler();
        integrator.addStepHandler(handler);
        Collection<StepHandler> stepHandlers = integrator.getStepHandlers();
        Assert.assertFalse(stepHandlers.isEmpty());
        Assert.assertTrue(stepHandlers.contains(handler));
    }

    @Test
    public void testAbstractIntegrator_integrate_ThrowsExceptionWhenInvalidEquations() {
        AbstractIntegrator integrator = new MyAbstractIntegrator();
        integrator.setEquations(null);
        try {
            integrator.integrate(new FirstOrderDifferentialEquations() {
                @Override
                public int getDimension() {
                    return 1;
                }

                @Override
                public void computeDerivatives(double t, double[] y, double[] yDot) {
                    yDot[0] = y[0] + 1;
                }
            }, 0, new double[]{0}, 1, new double[1]);
            Assert.fail();
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("Equations cannot be null", e.getMessage());
        }
    }

    @Test
    public void testAbstractIntegrator_integrate_ThrowsExceptionWhenInvalidStepSize() {
        AbstractIntegrator integrator = new MyAbstractIntegrator();
        integrator.setMaxEvaluations(1);
        StepInterpolatorAdapter interpolator = new StepInterpolatorAdapter(1, 0.1);
        FirstOrderDifferentialEquations equations = new MockDifferentialEquations();

        try {
            integrator.integrate(equations, 0, new double[]{1}, 1, new double[1]);
            Assert.fail();
        } catch (MaxCountExceededException e) {
            // Expected exception
        }

        try {
            integrator.integrate(equations, 0, new double[]{1}, 1, new double[1], interpolator);
            Assert.fail();
        } catch (NumberIsTooSmallException e) {
            // Expected exception
            Assert.assertEquals("Step size is too small", e.getMessage());
        }
    }

    @Test
    public void testAbstractIntegrator_integrate_ThrowsExceptionWhenInvalidEventHandler() {
        AbstractIntegrator integrator = new MyAbstractIntegrator();
        integrator.addEventHandler(null, 0, 1, 10);
        try {
            integrator.integrate(new MockDifferentialEquations(), 0, new double[]{0}, 1, new double[1]);
            Assert.fail();
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("Event handler cannot be null", e.getMessage());
        }
    }

    @Test
    public void testAbstractIntegrator_integrate_IntegratesSimpleEquations() {
        AbstractIntegrator integrator = new MyAbstractIntegrator();
        integrator.addEventHandler(new MockEventHandler(), 0, 1, 10);
        FirstOrderDifferentialEquations equations = new MockDifferentialEquations();

        double[] y = new double[]{1};
        double t = 1.1;
        integrator.integrate(equations, 0, y, t, y);
        MockEventHandler eventHandler = (MockEventHandler) integrator.getEventHandler();
        Assert.assertEquals(1, eventHandler.getCallCount());
    }

    @Test
    public void testAbstractIntegrator_integrate_IntegratesWithStepHandlers() {
        AbstractIntegrator integrator = new MyAbstractIntegrator();
        integrator.addStepHandler(new MockStepHandler1());
        integrator.addStepHandler(new MockStepHandler2());
        FirstOrderDifferentialEquations equations = new MockDifferentialEquations();

        double[] y = new double[]{1};
        double t = 1.1;
        integrator.integrate(equations, 0, y, t, y);

        MockStepHandler1 stepHandler1 = (MockStepHandler1) integrator.getStepHandlers().iterator().next();
        MockStepHandler2 stepHandler2 = (MockStepHandler2) integrator.getStepHandlers().iterator().next();
        Assert.assertEquals(1, stepHandler1.getCallCount());
        Assert.assertEquals(1, stepHandler2.getCallCount());
    }

    @Test
    public void testAbstractIntegrator_integrate_IntegratesWithEventHandler() {
        AbstractIntegrator integrator = new MyAbstractIntegrator();
        integrator.addEventHandler(new MockEventHandler(), 0, 1, 10);
        FirstOrderDifferentialEquations equations = new MockDifferentialEquations();

        double[] y = new double[]{1};
        double t = 1.1;
        integrator.integrate(equations, 0, y, t, y);
        MockEventHandler eventHandler = (MockEventHandler) integrator.getEventHandler();
        Assert.assertEquals(1, eventHandler.getCallCount());
    }

    @Test
    public void testAbstractIntegrator_integrate_IntegratesWithStepHandlersAndEventHandler() {
        AbstractIntegrator integrator = new MyAbstractIntegrator();
        integrator.addStepHandler(new MockStepHandler1());
        integrator.addStepHandler(new MockStepHandler2());
        integrator.addEventHandler(new MockEventHandler(), 0, 1, 10);
        FirstOrderDifferentialEquations equations = new MockDifferentialEquations();

        double[] y = new double[]{1};
        double t = 1.1;
        integrator.integrate(equations, 0, y, t, y);

        MockEventHandler eventHandler = (MockEventHandler) integrator.getEventHandler();
        MockStepHandler1 stepHandler1 = (MockStepHandler1) integrator.getStepHandlers().iterator().next();
        MockStepHandler2 stepHandler2 = (MockStepHandler2) integrator.getStepHandlers().iterator().next();

        Assert.assertEquals(1, eventHandler.getCallCount());
        Assert.assertEquals(1, stepHandler1.getCallCount());
        Assert.assertEquals(1, stepHandler2.getCallCount());
    }

    @Test
    public void testAbstractIntegrator_resetOccurred_ReturnsTrueWhenResetOccurs() {
        AbstractIntegrator integrator = new MyAbstractIntegrator();
        integrator.addEventHandler(new MockEventHandler() {
            @Override
            public void reset(double t, double[] y) {
                super.reset(t, y);
                resetOccurred = true;
            }
        }, 0, 1, 10);
        FirstOrderDifferentialEquations equations = new MockDifferentialEquations();

        double[] y = new double[]{1};
        double t = 1.1;
        integrator.integrate(equations, 0, y, t, y);
        Assert.assertTrue(integrator.isResetOccurred());
    }

    static class MockEventHandler implements EventHandler {
        private int callCount = 0;

        @Override
        public void handleState(double t, double[] y) {
        }

        @Override
        public void handleEvent(double t, double[] y) {
        }

        @Override
        public void init(double t, double[] y, double tEnd) {
        }

        @Override
        public void reinitializeBegin(AbstractStepInterpolator interpolator) {
        }

        public int getCallCount() {
            return callCount;
        }

        public void stepAccepted(double t, double[] y) {
            callCount++;
        }

        public void reset(double t, double[] y) {
            resetOccurred = true;
        }

        public void stop() {
        }

        boolean resetOccurred = false;
    }

    static class MockDifferentialEquations implements FirstOrderDifferentialEquations {
        @Override
        public int getDimension() {
            return 1;
        }

        @Override
        public void computeDerivatives(double t, double[] y, double[] yDot) {
            yDot[0] = y[0] + 1;
        }
    }

    static class MockStepHandler1 implements StepHandler {
        private int callCount = 0;

        @Override
        public void handleStep(StepInterpolator interpolator, boolean isLastStep) {
            callCount++;
        }

        public int getCallCount() {
            return callCount;
        }
    }

    static class MockStepHandler2 implements StepHandler {
        private int callCount = 0;

        @Override
        public void handleStep(StepInterpolator interpolator, boolean isLastStep) {
            callCount++;
        }

        public int getCallCount() {
            return callCount;
        }
    }

    static class MyAbstractIntegrator extends AbstractIntegrator {
        public MyAbstractIntegrator() {
            super();
        }

        public MyAbstractIntegrator(String name) {
            super(name);
        }

        @Override
        protected void initIntegration(double t0, double[] y0, double t) {
            super.initIntegration(t0, y0, t);
        }

        @Override
        protected void setEquations(ExpandableStatefulODE equations) {
            super.setEquations(equations);
        }

        @Override
        public void integrate(ExpandableStatefulODE equations, double t)
                throws NumberIsTooSmallException, DimensionMismatchException,
                       MaxCountExceededException, NoBracketingException {
            super.integrate(equations, t);
        }

        @Override
        public void computeDerivatives(double t, double[] y, double[] yDot)
                throws MaxCountExceededException, DimensionMismatchException {
            super.computeDerivatives(t, y, yDot);
        }

        @Override
        protected void setStateInitialized(boolean stateInitialized) {
            super.setStateInitialized(stateInitialized);
        }

        public boolean isResetOccurred() {
            return resetOccurred;
        }
    }
}