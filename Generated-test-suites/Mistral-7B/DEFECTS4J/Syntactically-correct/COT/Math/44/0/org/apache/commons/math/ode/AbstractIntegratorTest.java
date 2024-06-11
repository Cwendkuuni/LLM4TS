import org.apache.commons.math3.ode.AbstractIntegrator;
import org.apache.commons.math3.ode.ExpandableStatefulODE;
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.events.EventHandler;
import org.apache.commons.math3.ode.events.EventState;
import org.apache.commons.math3.ode.sampling.AbstractStepInterpolator;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class AbstractIntegratorTest {

    @Test
    public void testConstructor() {
        AbstractIntegrator integrator = new AbstractIntegrator();
        assertNotNull(integrator);
        assertEquals("", integrator.getName());
        assertEquals(0, integrator.getStepHandlers().size());
        assertEquals(Double.NaN, integrator.getCurrentStepStart(), 0.0);
        assertEquals(Double.NaN, integrator.getCurrentSignedStepsize(), 0.0);
        assertEquals(false, integrator.isLastStep());
        assertEquals(false, integrator.resetOccurred);
        assertEquals(0, integrator.getEvents().size());
        assertEquals(false, integrator.isStateInitialized());
        assertEquals(0, integrator.getEvaluations());
        assertNull(integrator.getEquations());
    }

    @Test
    public void testAddStepHandler() {
        AbstractIntegrator integrator = new AbstractIntegrator();
        integrator.addStepHandler(new StepHandler() {
            @Override
            public void handleStep(AbstractStepInterpolator interpolator, boolean isLastStep) {
                // no-op
            }
        });
        assertEquals(1, integrator.getStepHandlers().size());
    }

    @Test
    public void testClearStepHandlers() {
        AbstractIntegrator integrator = new AbstractIntegrator();
        integrator.addStepHandler(new StepHandler() {
            @Override
            public void handleStep(AbstractStepInterpolator interpolator, boolean isLastStep) {
                // no-op
            }
        });
        integrator.clearStepHandlers();
        assertEquals(0, integrator.getStepHandlers().size());
    }

    @Test
    public void testAddEventHandler() {
        AbstractIntegrator integrator = new AbstractIntegrator();
        integrator.addEventHandler(new EventHandler() {
            @Override
            public void evaluateStep(AbstractStepInterpolator interpolator) {
                return false;
            }

            @Override
            public void stepAccepted(double eventTime, double[] eventY) {
                // no-op
            }

            @Override
            public void reset(double eventTime, double[] eventY) {
                return false;
            }

            @Override
            public double getEventTime() {
                return 0.0;
            }
        }, 0.0, 1.0e-6, 5, new BracketingNthOrderBrentSolver(1.0e-6, 5));
        assertEquals(1, integrator.getEventHandlers().size());
    }

    @Test
    public void testClearEventHandlers() {
        AbstractIntegrator integrator = new AbstractIntegrator();
        integrator.addEventHandler(new EventHandler() {
            @Override
            public void evaluateStep(AbstractStepInterpolator interpolator) {
                return false;
            }

            @Override
            public void stepAccepted(double eventTime, double[] eventY) {
                // no-op
            }

            @Override
            public void reset(double eventTime, double[] eventY) {
                return false;
            }

            @Override
            public double getEventTime() {
                return 0.0;
            }
        }, 0.0, 1.0e-6, 5, new BracketingNthOrderBrentSolver(1.0e-6, 5));
        integrator.clearEventHandlers();
        assertEquals(0, integrator.getEventHandlers().size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetMaxEvaluationsNegative() {
        AbstractIntegrator integrator = new AbstractIntegrator();
        integrator.setMaxEvaluations(-1);
    }

    @Test
    public void testGetMaxEvaluations() {
        AbstractIntegrator integrator = new AbstractIntegrator();
        integrator.setMaxEvaluations(10);
        assertEquals(10, integrator.getMaxEvaluations());
    }

    @Test
    public void testGetEvaluations() {
        AbstractIntegrator integrator = new AbstractIntegrator();
        assertEquals(0, integrator.getEvaluations());
    }

    @Test
    public void testResetEvaluations() {
        AbstractIntegrator integrator = new AbstractIntegrator();
        integrator.setMaxEvaluations(10);
        integrator.integrate(new DummyODE(), 0.0, new double[0], 1.0, new double[0]);
        integrator.resetEvaluations();
        assertEquals(0, integrator.getEvaluations());
    }

    @Test
    public void testSetEquations() {
        AbstractIntegrator integrator = new AbstractIntegrator();
        integrator.setEquations(new DummyODE());
        assertNotNull(integrator.getEquations());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIntegrateNullEquations() {
        AbstractIntegrator integrator = new AbstractIntegrator();
        integrator.integrate(null, 0.0, new double[0], 1.0, new double[0]);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIntegrateNullY() {
        AbstractIntegrator integrator = new AbstractIntegrator();
        integrator.integrate(new DummyODE(), 0.0, null, 1.0, new double[0]);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIntegrateNullYDot() {
        AbstractIntegrator integrator = new AbstractIntegrator();
        integrator.integrate(new DummyODE(), 0.0, new double[0], 1.0, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIntegrateInvalidIntegrationTime() {
        AbstractIntegrator integrator = new AbstractIntegrator();
        integrator.integrate(new DummyODE(), Double.NEGATIVE_INFINITY, new double[0], 0.0, new double[0]);
    }

    @Test
    public void testIntegrate() throws IllegalStateException, IllegalArgumentException {
        AbstractIntegrator integrator = new AbstractIntegrator();
        integrator.setMaxEvaluations(1);
        ExpandableStatefulODE equations = new ExpandableStatefulODE(new DummyDifferentialEquations());
        double[] y0 = new double[1];
        double y = integrator.integrate(equations, 0.0, y0, 1.0, new double[1]);
        assertEquals(1.0, y, 1.0e-6);
        assertEquals(1, integrator.getEvaluations());
    }

    @Test(expected = IllegalStateException.class)
    public void testIntegrateDuringInitialization() throws IllegalStateException, IllegalArgumentException {
        AbstractIntegrator integrator = new AbstractIntegrator();
        integrator.integrate(new DummyODE(), 0.0, new double[0], 1.0, new double[0]);
    }

    @Test(expected = IllegalStateException.class)
    public void testIntegrateStateNotInitialized() throws IllegalStateException, IllegalArgumentException {
        AbstractIntegrator integrator = new AbstractIntegrator();
        ExpandableStatefulODE equations = new ExpandableStatefulODE(new DummyODE());
        integrator.integrate(equations, 0.0, new double[0], 1.0, new double[0]);
        integrator.setStateInitialized(false);
        integrator.integrate(equations, 0.0, new double[0], 1.0, new double[0]);
    }

    @Test(expected = IllegalStateException.class)
    public void testIntegrateStepHandlerNotAccepted() throws IllegalStateException, IllegalArgumentException {
        AbstractIntegrator integrator = new AbstractIntegrator() {
            @Override
            protected void acceptStep(AbstractStepInterpolator interpolator, double[] y, double[] yDot, double tEnd)
                throws MathIllegalStateException {
                throw new MathIllegalStateException("Test exception");
            }
        };
        ExpandableStatefulODE equations = new ExpandableStatefulODE(new DummyODE());
        integrator.integrate(equations, 0.0, new double[0], 1.0, new double[0]);
    }

    @Test(expected = MathIllegalStateException.class)
    public void testIntegrateStepHandlerReset() throws IllegalStateException, IllegalArgumentException {
        AbstractIntegrator integrator = new AbstractIntegrator() {
            @Override
            protected void acceptStep(AbstractStepInterpolator interpolator, double[] y, double[] yDot, double tEnd)
                throws MathIllegalStateException {
                y[0] = 1.0;
                throw new MathIllegalStateException("Test exception");
            }
        };
        ExpandableStatefulODE equations = new ExpandableStatefulODE(new DummyODE());
        integrator.integrate(equations, 0.0, new double[0], 1.0, new double[0]);
        integrator.setStateInitialized(true);
        integrator.integrate(equations, 0.0, new double[0], 1.0, new double[0]);
    }

    @Test(expected = MathIllegalArgumentException.class)
    public void testIntegrateStepHandlerMaxEvaluations() throws IllegalStateException, IllegalArgumentException {
        AbstractIntegrator integrator = new AbstractIntegrator() {
            @Override
            protected void acceptStep(AbstractStepInterpolator interpolator, double[] y, double[] yDot, double tEnd)
                throws MathIllegalStateException {
                throw new MathIllegalStateException("Test exception");
            }
        };
        ExpandableStatefulODE equations = new ExpandableStatefulODE(new DummyODE());
        integrator.setMaxEvaluations(0);
        integrator.integrate(equations, 0.0, new double[0], 1.0, new double[0]);
    }

    @Test(expected = MaxCountExceededException.class)
    public void testIntegrateMaxEvaluations() throws IllegalStateException, IllegalArgumentException {
        AbstractIntegrator integrator = new AbstractIntegrator() {
            @Override
            protected void computeDerivatives(double t, double[] y, double[] yDot)
                throws MaxCountExceededException {
                throw new MaxCountExceededException();
            }
        };
        ExpandableStatefulODE equations = new ExpandableStatefulODE(new DummyODE());
        integrator.integrate(equations, 0.0, new double[0], 1.0, new double[0]);
    }

    @Test
    public void testComputeDerivatives() throws MaxCountExceededException {
        AbstractIntegrator integrator = new AbstractIntegrator() {
            @Override
            protected void computeDerivatives(double t, double[] y, double[] yDot)
                throws MaxCountExceededException {
                yDot[0] = 0.0;
            }
        };
        ExpandableStatefulODE equations = new ExpandableStatefulODE(new DummyODE());
        integrator.setMaxEvaluations(10);
        integrator.computeDerivatives(0.0, new double[0], new double[0]);
        assertEquals(10, integrator.getEvaluations());
    }

    @Test
    public void testSetStateInitialized() {
        AbstractIntegrator integrator = new AbstractIntegrator();
        integrator.setStateInitialized(true);
        assertTrue(integrator.isStateInitialized());
    }

}

class DummyODE implements FirstOrderDifferentialEquations {

    @Override
    public int getDimension() {
        return 1;
    }

    @Override
    public void computeDerivatives(double t, double[] y, double[] yDot) {
        yDot[0] = y[0] * t;
    }

}

class DummyDifferentialEquations extends DummyODE implements ExpandableStatefulODE {

    @Override
    public double getTime() {
        return 0.0;
    }

    @Override
    public void setTime(double t) {
        // no-op
    }

    @Override
    public double[] getPrimaryState() {
        return new double[0];
    }

    @Override
    public void setPrimaryState(double[] state) {
        // no-op
    }

}