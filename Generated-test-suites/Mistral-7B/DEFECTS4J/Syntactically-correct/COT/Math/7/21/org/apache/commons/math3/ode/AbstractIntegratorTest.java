import org.apache.commons.math3.ode.AbstractIntegrator;
import org.apache.commons.math3.ode.ExpandableStatefulODE;
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.events.EventHandler;
import org.apache.commons.math3.ode.events.MockEventHandler;
import org.apache.commons.math3.ode.sampling.AbstractStepInterpolator;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.apache.commons.math3.ode.events.EventState.EventType.FIRING;

public class AbstractIntegratorTest {

    private AbstractIntegrator integrator;

    @Before
    public void setUp() {
        integrator = new AbstractIntegrator() {
            @Override
            public void integrate(ExpandableStatefulODE equations, double t)
                    throws NumberIsTooSmallException, DimensionMismatchException,
                           MaxCountExceededException, NoBracketingException {
                // empty implementation
            }
        };

        integrator.addEventHandler(new MockEventHandler(FIRING), 0.0, 0.001, 10, null);
    }

    @Test
    public void testAddStepHandler() {
        integrator.addStepHandler(new MockStepHandler());
        Assert.assertEquals(1, integrator.getStepHandlers().size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddStepHandlerNull() {
        integrator.addStepHandler(null);
    }

    @Test
    public void testAddEventHandler() {
        integrator.addEventHandler(new MockEventHandler(FIRING), 0.0, 0.001, 10, null);
        Assert.assertEquals(1, integrator.getEventHandlers().size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddEventHandlerNullEventHandler() {
        integrator.addEventHandler(null, 0.0, 0.001, 10, null);
    }

    @Test
    public void testAddEventHandlerNullEventHandlerSolver() {
        integrator.addEventHandler(new MockEventHandler(FIRING), 0.0, 0.001, 10, null);
        integrator.addEventHandler(new MockEventHandler(FIRING), 0.0, 0.001, 10, null);
        integrator.addEventHandler(new MockEventHandler(FIRING), 0.0, 0.001, 10, null);
        integrator.addEventHandler(new MockEventHandler(FIRING), 0.0, 0.001, 10, null);

        integrator.setStepSize(0.01);
        integrator.integrate(new MockODE(0.0, new double[]{0.0}), 1.0, new double[1], new double[1]);

        Assert.assertEquals(4, integrator.getEventHandlers().size());
    }

    @Test
    public void testClearStepHandlers() {
        integrator.addStepHandler(new MockStepHandler());
        integrator.addStepHandler(new MockStepHandler());
        integrator.clearStepHandlers();
        Assert.assertEquals(0, integrator.getStepHandlers().size());
    }

    @Test
    public void testClearEventHandlers() {
        integrator.addEventHandler(new MockEventHandler(FIRING), 0.0, 0.001, 10, null);
        integrator.clearEventHandlers();
        Assert.assertEquals(0, integrator.getEventHandlers().size());
    }

    @Test
    public void testGetCurrentSignedStepsize() {
        Assert.assertEquals(Double.NaN, integrator.getCurrentSignedStepsize());
        integrator.setStepSize(0.01);
        Assert.assertEquals(0.01, integrator.getCurrentSignedStepsize());
    }

    @Test
    public void testGetMaxEvaluations() {
        Assert.assertEquals(-1, integrator.getMaxEvaluations());
    }

    @Test
    public void testGetEvaluations() {
        Assert.assertEquals(0, integrator.getEvaluations());
    }

    @Test
    public void testIntegrate() throws Exception {
        integrator.setStepSize(0.01);
        integrator.addEventHandler(new MockEventHandler(FIRING), 0.0, 0.001, 10, null);
        integrator.addStepHandler(new MockStepHandler());

        integrator.setEquations(new MockODE(0.0, new double[]{0.0}));
        integrator.integrate(integrator.getEquations(), 1.0);

        Assert.assertEquals(1, integrator.getEventHandlers().size());
        Assert.assertEquals(1, integrator.getStepHandlers().size());
        Assert.assertEquals(1, integrator.getEvaluations());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIntegrateNullEquations() {
        integrator.integrate(null, 1.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIntegrateNullT() {
        integrator.integrate(new MockODE(0.0, new double[]{0.0}), null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIntegrateNullInitY() {
        integrator.integrate(new MockODE(0.0, new double[]{0.0}), 1.0, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIntegrateNullY() {
        integrator.integrate(new MockODE(0.0, new double[]{0.0}), 1.0, new double[1]);
    }

    @Test
    public void testIntegrateMaxEvaluationsReached() throws Exception {
        integrator.setMaxEvaluations(1);
        integrator.addEventHandler(new MockEventHandler(FIRING), 0.0, 0.001, 10, null);
        integrator.addStepHandler(new MockStepHandler());

        integrator.setEquations(new MockODE(0.0, new double[]{0.0}));
        integrator.integrate(integrator.getEquations(), 1.0);

        Assert.assertEquals(Exception.class, integrator.getEventHandlers().get(0).getClass());
    }

    @Test
    public void testIntegrateNoBracketing() throws Exception {
        integrator.setMaxEvaluations(10000);
        integrator.addEventHandler(new MockEventHandler(FIRING), 0.0, 0.001, 10, null);
        integrator.addStepHandler(new MockStepHandler());
        integrator.addEventHandler(new MockEventHandler(FIRING), 1.0, 0.001, 10, null);

        integrator.setEquations(new MockODE(0.0, new double[]{0.0}));
        integrator.integrate(integrator.getEquations(), 1.0);

        Assert.assertEquals(Exception.class, integrator.getEventHandlers().get(0).getClass());
    }

    @Test
    public void testIntegrateSuccess() throws Exception {
        integrator.setMaxEvaluations(10000);
        integrator.addEventHandler(new MockEventHandler(FIRING), 0.0, 0.001, 10, null);
        integrator.addStepHandler(new MockStepHandler());
        integrator.addEventHandler(new MockEventHandler(FIRING), 1.0, 0.001, 10, null);

        integrator.setEquations(new MockODE(0.0, new double[]{0.0}));
        integrator.integrate(integrator.getEquations(), 1.0);

        Assert.assertEquals(0, integrator.getEventHandlers().size());
        Assert.assertEquals(1, integrator.getStepHandlers().size());
        Assert.assertEquals(1, integrator.getEvaluations());
    }

    @Test
    public void testSetMaxEvaluations() {
        integrator.setMaxEvaluations(10);
        Assert.assertEquals(10, integrator.getMaxEvaluations());
    }

    @Test
    public void testSetMaxEvaluationsNegative() {
        integrator.setMaxEvaluations(-1);
        Assert.assertEquals(-1, integrator.getMaxEvaluations());
    }

    @Test
    public void testSetMaxEvaluationsZero() {
        integrator.setMaxEvaluations(0);
        Assert.assertEquals(0, integrator.getMaxEvaluations());
    }

    @Test
    public void testSetMaxEvaluationsNull() {
        integrator.setMaxEvaluations(null);
        Assert.assertEquals(-1, integrator.getMaxEvaluations());
    }

    @Test
    public void testComputeDerivatives() throws Exception {
        integrator.setEquations(new MockODE(0.0, new double[]{0.0}));
        integrator.computeDerivatives(0.0, new double[]{0.0}, new double[]{0.0});
        Assert.assertEquals(1, integrator.getEvaluations());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testComputeDerivativesNullT() {
        integrator.computeDerivatives(null, new double[]{0.0}, new double[]{0.0});
    }

    @Test(expected = IllegalArgumentException.class)
    public void testComputeDerivativesNullY() {
        integrator.computeDerivatives(0.0, null, new double[]{0.0});
    }

    @Test(expected = IllegalArgumentException.class)
    public void testComputeDerivativesNullYDot() {
        integrator.computeDerivatives(0.0, new double[]{0.0}, null);
    }

    @Test
    public void testSetStateInitialized() {
        integrator.setStateInitialized(false);
        Assert.assertFalse(integrator.isStateInitialized());

        integrator.setStateInitialized(true);
        Assert.assertTrue(integrator.isStateInitialized());
    }

    @Test
    public void testIsStateInitialized() {
        integrator.setStateInitialized(false);
        Assert.assertFalse(integrator.isStateInitialized());

        integrator.setStateInitialized(true);
        Assert.assertTrue(integrator.isStateInitialized());
    }

    @Test
    public void testAcceptStep() throws Exception {
        integrator.setMaxEvaluations(10000);
        integrator.addEventHandler(new MockEventHandler(FIRING), 0.0, 0.001, 10, null);
        integrator.addStepHandler(new MockStepHandler());
        integrator.addEventHandler(new MockEventHandler(FIRING), 1.0, 0.001, 10, null);
        integrator.addEventHandler(new MockEventHandler(FIRING), 2.0, 0.001, 10, null);

        integrator.setEquations(new MockODE(0.0, new double[]{0.0}));
        double currentT = 0.0;
        double[] currentY = new double[]{0.0};
        double[] yDot = new double[1];

        AbstractStepInterpolator interpolator = new MockStepInterpolator(currentT, currentY);

        for (int i = 0; i < 5; i++) {
            double tEnd = currentT + 0.05;
            integrator.acceptStep(interpolator, currentY, yDot, tEnd);
            currentT = tEnd;
        }

        Assert.assertEquals(5, integrator.getEventHandlers().size());
        Assert.assertEquals(6, integrator.getStepHandlers().size());
        Assert.assertEquals(6, integrator.getEvaluations());
    }

    private static class MockEventHandler implements EventHandler {
        private final EventState.EventType eventType;
        private final double eventTime;
        private final double convergence;
        private final int maxIterationCount;
        private final UnivariateSolver solver;

        public MockEventHandler(EventState.EventType eventType, double eventTime, double convergence, int maxIterationCount, UnivariateSolver solver) {
            this.eventType = eventType;
            this.eventTime = eventTime;
            this.convergence = convergence;
            this.maxIterationCount = maxIterationCount;
            this.solver = solver;
        }

        @Override
        public EventState getEventHandler() {
            return new EventState(this, eventTime, convergence, maxIterationCount, solver);
        }

        public boolean evaluateStep(AbstractStepInterpolator interpolator) {
            return eventTime == interpolator.getGlobalCurrentTime();
        }

        public void stepAccepted(double eventTime, double[] eventY) {
            // empty implementation
        }

        public boolean stop() {
            return eventType == EventState.EventType.STOP;
        }

        public void reset(double eventTime, double[] eventY) {
            // empty implementation
        }
    }

    private static class MockStepHandler implements StepHandler {
        @Override
        public void handleStep(AbstractStepInterpolator interpolator, boolean last) {
            // empty implementation
        }
    }

    private static class MockStepInterpolator extends AbstractStepInterpolator {
        private double softPreviousTime;
        private double softCurrentTime;

        public MockStepInterpolator(double currentTime, double[] currentY) {
            super(currentTime, currentY);
            this.softPreviousTime = currentTime;
            this.softCurrentTime = currentTime;
        }

        @Override
        public double getGlobalPreviousTime() {
            return softPreviousTime;
        }

        @Override
        public double getGlobalCurrentTime() {
            return softCurrentTime;
        }

        @Override
        public void setSoftPreviousTime(double previousTime) {
            this.softPreviousTime = previousTime;
        }

        @Override
        public void setSoftCurrentTime(double currentTime) {
            this.softCurrentTime = currentTime;
        }

        public void setInterpolatedTime(double time) {
            softCurrentTime = time;
        }

        public double[] getInterpolatedState() {
            return getPrimaryState();
        }
    }

    private static class MockODE implements ExpandableStatefulODE {
        private double time;

        public MockODE(double initialTime, double[] initialState) {
            setTime(initialTime);
            setPrimaryState(initialState);
        }

        @Override
        public void computeDerivatives(double t, double[] y, double[] yDot) {
            System.arraycopy(y, 0, yDot, 0, y.length);
        }

        @Override
        public void setTime(double time) {
            this.time = time;
        }

        @Override
        public double getTime() {
            return time;
        }

        @Override
        public int getDimension() {
            return 1;
        }

        @Override
        public double[] getPrimaryState() {
            return new double[]{0.0};
        }
    }

}