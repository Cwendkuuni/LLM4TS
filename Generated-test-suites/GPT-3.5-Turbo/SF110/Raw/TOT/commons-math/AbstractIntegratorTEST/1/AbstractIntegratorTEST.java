import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class AbstractIntegratorTest {

    private AbstractIntegrator abstractIntegrator;

    @Before
    public void setUp() {
        abstractIntegrator = new TestAbstractIntegrator();
    }

    @Test
    public void testGetName() {
        assertEquals("Test Abstract Integrator", abstractIntegrator.getName());
    }

    @Test
    public void testAddStepHandler() {
        StepHandler stepHandler = new TestStepHandler();
        abstractIntegrator.addStepHandler(stepHandler);
        assertTrue(abstractIntegrator.getStepHandlers().contains(stepHandler));
    }

    @Test
    public void testClearStepHandlers() {
        StepHandler stepHandler = new TestStepHandler();
        abstractIntegrator.addStepHandler(stepHandler);
        abstractIntegrator.clearStepHandlers();
        assertTrue(abstractIntegrator.getStepHandlers().isEmpty());
    }

    @Test
    public void testAddEventHandler() {
        EventHandler eventHandler = new TestEventHandler();
        abstractIntegrator.addEventHandler(eventHandler, 0.1, 0.01, 10);
        assertTrue(abstractIntegrator.getEventHandlers().contains(eventHandler));
    }

    @Test
    public void testClearEventHandlers() {
        EventHandler eventHandler = new TestEventHandler();
        abstractIntegrator.addEventHandler(eventHandler, 0.1, 0.01, 10);
        abstractIntegrator.clearEventHandlers();
        assertTrue(abstractIntegrator.getEventHandlers().isEmpty());
    }

    @Test
    public void testGetMaxEvaluations() {
        abstractIntegrator.setMaxEvaluations(100);
        assertEquals(100, abstractIntegrator.getMaxEvaluations());
    }

    @Test
    public void testGetEvaluations() {
        abstractIntegrator.integrate(equations, 0, y0, 1, y);
        assertEquals(10, abstractIntegrator.getEvaluations());
    }

    @Test
    public void testSetEquations() {
        ExpandableStatefulODE equations = new TestExpandableStatefulODE();
        abstractIntegrator.setEquations(equations);
        assertEquals(equations, abstractIntegrator.getExpandable());
    }

    @Test
    public void testIntegrate() {
        FirstOrderDifferentialEquations equations = new TestFirstOrderDifferentialEquations();
        double t0 = 0;
        double[] y0 = {1.0, 2.0};
        double t = 1;
        double[] y = new double[2];
        double result = abstractIntegrator.integrate(equations, t0, y0, t, y);
        assertEquals(1.0, result, 1e-10);
        assertArrayEquals(new double[]{4.0, 3.0}, y, 1e-10);
    }

    @Test
    public void testComputeDerivatives() {
        double t = 0;
        double[] y = {1.0, 2.0};
        double[] yDot = new double[2];
        abstractIntegrator.computeDerivatives(t, y, yDot);
        assertArrayEquals(new double[]{2.0, 1.0}, yDot, 1e-10);
    }

    @Test
    public void testAcceptStep() {
        AbstractStepInterpolator interpolator = new TestAbstractStepInterpolator();
        double[] y = {1.0, 2.0};
        double[] yDot = {3.0, 4.0};
        double tEnd = 2.0;
        double result = abstractIntegrator.acceptStep(interpolator, y, yDot, tEnd);
        assertEquals(2.0, result, 1e-10);
        assertArrayEquals(new double[]{2.0, 1.0}, y, 1e-10);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testSanityChecks() {
        ExpandableStatefulODE equations = new TestExpandableStatefulODE();
        double t = 0.0;
        abstractIntegrator.sanityChecks(equations, t);
    }

    private static class TestAbstractIntegrator extends AbstractIntegrator {

        @Override
        public void integrate(ExpandableStatefulODE p0, double p1) throws NumberIsTooSmallException, DimensionMismatchException, MaxCountExceededException, NoBracketingException {

        }
    }

    private static class TestStepHandler implements StepHandler {

        @Override
        public void init(double v, double[] doubles, double v1) {

        }

        @Override
        public void handleStep(StepInterpolator stepInterpolator, boolean b) {

        }
    }

    private static class TestEventHandler implements EventHandler {

        @Override
        public void init(double v, double[] doubles, double v1) {

        }

        @Override
        public double g(double v, double[] doubles) {
            return 0;
        }

        @Override
        public Action eventOccurred(double v, double[] doubles, boolean b) {
            return null;
        }

        @Override
        public void resetState(double v, double[] doubles) {

        }
    }

    private static class TestExpandableStatefulODE extends ExpandableStatefulODE {

        @Override
        public double[] getPrimaryState() {
            return new double[]{4.0, 3.0};
        }

        @Override
        public void computeDerivatives(double v, double[] doubles, double[] doubles1) throws MaxCountExceededException, DimensionMismatchException {

        }
    }

    private static class TestFirstOrderDifferentialEquations implements FirstOrderDifferentialEquations {

        @Override
        public int getDimension() {
            return 2;
        }

        @Override
        public void computeDerivatives(double v, double[] doubles, double[] doubles1) throws MaxCountExceededException, DimensionMismatchException {
            doubles1[0] = doubles[1];
            doubles1[1] = doubles[0] - v * doubles[1];
        }
    }

    private static class TestAbstractStepInterpolator implements AbstractStepInterpolator {

        @Override
        public double getGlobalPreviousTime() {
            return 0;
        }

        @Override
        public double getGlobalCurrentTime() {
            return 1;
        }

        @Override
        protected StepInterpolator doCopy() {
            return null;
        }

        @Override
        protected void shift() {

        }

        @Override
        protected void writeBaseExternal(ObjectOutput objectOutput) {

        }

        @Override
        protected void readBaseExternal(ObjectInput objectInput) {

        }

        @Override
        protected void writeStateExternal(ObjectOutput objectOutput) throws IOException {

        }

        @Override
        protected void readStateExternal(ObjectInput objectInput) throws IOException, ClassNotFoundException {

        }

        @Override
        public void writeExternal(ObjectOutput objectOutput) throws IOException {

        }

        @Override
        public void readExternal(ObjectInput objectInput) throws IOException, ClassNotFoundException {

        }
    }
}