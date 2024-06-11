import static org.junit.Assert.*;
import org.apache.commons.math3.ode.AbstractIntegrator;
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.ExpandableStatefulODE;
import org.apache.commons.math3.ode.events.EventHandler;
import org.apache.commons.math3.ode.sampling.AbstractStepInterpolator;
import org.apache.commons.math3.ode.sampling.StepHandler;

public class AbstractIntegratorTest {

    @SuppressWarnings("unchecked")
    private static class DummyEventHandler implements EventHandler {
        private double eventTime;
        private double[] eventState;

        public boolean evaluateStep(AbstractStepInterpolator interpolator) {
            return true;
        }

        public void stepAccepted(double eventTime, double[] eventState) {
            this.eventTime = eventTime;
            this.eventState = eventState;
        }

        public double getEventTime() {
            return eventTime;
        }

        public double[] getEventState() {
            return eventState;
        }
    }

    @SuppressWarnings("unchecked")
    private static class DummyStepHandler implements StepHandler {
        private int count;

        public void handleStep(AbstractStepInterpolator interpolator, boolean lastStep) {
            count++;
        }

        public int getCount() {
            return count;
        }

        public void reset() {
            count = 0;
        }
    }

    @SuppressWarnings("unchecked")
    private static class DummyStepInterpolator extends AbstractStepInterpolator {
        private double time;
        private double[] state;

        public DummyStepInterpolator(double time, double[] state) {
            this.time = time;
            this.state = state;
        }

        public double getGlobalCurrentTime() {
            return time;
        }

        public double getGlobalPreviousTime() {
            return time - 0.1;
        }

        public void setInterpolatedState(double[] state) {
            this.state = state;
        }

        public double[] getInterpolatedState() {
            return state;
        }

        public void setInterpolatedTime(double time) {
            this.time = time;
        }

        public double getTime() {
            return time;
        }

        public double getStepSize() {
            return 0.1;
        }

        public void setSoftCurrentTime(double time) {
            this.time = time;
        }

        public void setSoftPreviousTime(double time) {
            this.time = time;
        }
    }

    @Test
    public void testIntegrator() throws Exception {
        AbstractIntegrator integrator = new AbstractIntegrator() {
            @Override
            public void integrate(ExpandableStatefulODE equations, double t)
                    throws NumberIsTooSmallException, DimensionMismatchException,
                           MaxCountExceededException, NoBracketingException {
                // empty implementation
            }
        };

        FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yDot) {
                yDot[0] = y[0] * 0.1;
            }
        };

        integrator.initIntegration(0.0, new double[] { 1.0 }, 1.5);

        ExpandableStatefulODE expandable = new ExpandableStatefulODE(equations);
        expandable.setTime(0.0);
        expandable.setPrimaryState(new double[] { 1.0 });

        AbstractStepInterpolator interpolator = new DummyStepInterpolator(0.0, new double[] { 1.0 });

        integrator.integrate(expandable, 1.5, new double[] { 1.0 }, interpolator);

        assertEquals(1.5, interpolator.getTime(), 1e-9);
        assertEquals(1.157670834, interpolator.getInterpolatedState()[0], 1e-7);

        integrator.addEventHandler(new DummyEventHandler(), 0.5, 1e-6, 1, null);

        integrator.integrate(expandable, 1.5, new double[] { 1.0 }, interpolator);

        assertEquals(1.5, interpolator.getTime(), 1e-9);
        assertEquals(1.157670834, interpolator.getInterpolatedState()[0], 1e-7);
        assertEquals(0.5, ((DummyEventHandler) integrator.getEventHandler()).getEventTime(), 1e-9);
        assertArrayEquals(new double[] { 0.678698282 }, ((DummyEventHandler) integrator.getEventHandler()).getEventState(), 1e-7);

        integrator.clearEventHandler();

        integrator.integrate(expandable, 1.5, new double[] { 1.0 }, interpolator);

        assertEquals(1.5, interpolator.getTime(), 1e-9);
        assertEquals(1.157670834, interpolator.getInterpolatedState()[0], 1e-7);

        integrator.addStepHandler(new DummyStepHandler());

        integrator.integrate(expandable, 1.5, new double[] { 1.0 }, interpolator);

        assertEquals(1.5, interpolator.getTime(), 1e-9);
        assertEquals(1.157670834, interpolator.getInterpolatedState()[0], 1e-7);
        assertEquals(1, ((DummyStepHandler) integrator.getStepHandlers().iterator().next()).getCount());

        integrator.clearStepHandlers();

        integrator.integrate(expandable, 1.5, new double[] { 1.0 }, interpolator);

        assertEquals(1.5, interpolator.getTime(), 1e-9);
        assertEquals(1.157670834, interpolator.getInterpolatedState()[0], 1e-7);
        assertEquals(0, ((List<StepHandler>) integrator.getStepHandlers()).size());

    }

    @Test(expected = DimensionMismatchException.class)
    public void testIntegratorDimensionMismatch() throws Exception {
        AbstractIntegrator integrator = new AbstractIntegrator() {
            @Override
            public void integrate(ExpandableStatefulODE equations, double t)
                    throws NumberIsTooSmallException, DimensionMismatchException,
                           MaxCountExceededException, NoBracketingException {
                // empty implementation
            }
        };

        FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return 2;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yDot) {
                yDot[0] = y[0] * 0.1;
            }
        };

        integrator.initIntegration(0.0, new double[] { 1.0, 2.0 }, 1.5);

        ExpandableStatefulODE expandable = new ExpandableStatefulODE(equations);
        expandable.setTime(0.0);
        expandable.setPrimaryState(new double[] { 1.0, 2.0 });

        AbstractStepInterpolator interpolator = new DummyStepInterpolator(0.0, new double[] { 1.0, 2.0 });

        integrator.integrate(expandable, 1.5, new double[] { 1.0, 2.0 }, interpolator);

    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testIntegratorNumberIsTooSmallException() throws Exception {
        AbstractIntegrator integrator = new AbstractIntegrator() {
            @Override
            public void integrate(ExpandableStatefulODE equations, double t)
                    throws NumberIsTooSmallException, DimensionMismatchException,
                           MaxCountExceededException, NoBracketingException {
                // empty implementation
            }
        };

        FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yDot) {
                yDot[0] = y[0] * 0.1;
            }
        };

        integrator.setMaxEvaluations(1);

        integrator.initIntegration(0.0, new double[] { 1.0 }, 1.5);

        ExpandableStatefulODE expandable = new ExpandableStatefulODE(equations);
        expandable.setTime(0.0);
        expandable.setPrimaryState(new double[] { 1.0 });

        AbstractStepInterpolator interpolator = new DummyStepInterpolator(0.0, new double[] { 1.0 });

        integrator.integrate(expandable, 1.5, new double[] { 1.0 }, interpolator);

    }

    @Test(expected = NoBracketingException.class)
    public void testIntegratorNoBracketingException() throws Exception {
        AbstractIntegrator integrator = new AbstractIntegrator() {
            @Override
            public void integrate(ExpandableStatefulODE equations, double t)
                    throws NumberIsTooSmallException, DimensionMismatchException,
                           MaxCountExceededException, NoBracketingException {
                // empty implementation
            }
        };

        FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yDot) {
                yDot[0] = y[0] * 0.1;
            }
        };

        integrator.initIntegration(0.0, new double[] { 1.0 }, 1.5);

        ExpandableStatefulODE expandable = new ExpandableStatefulODE(equations);
        expandable.setTime(0.0);
        expandable.setPrimaryState(new double[] { 1.0 });

        integrator.addEventHandler(new DummyEventHandler(), 1.0, 1e-6, 1, null);

        integrator.integrate(expandable, 1.5, new double[] { 1.0 }, new DummyStepInterpolator(0.0, new double[] { 1.0 }));

    }

}