import static org.junit.Assert.*;
import org.apache.commons.math3.ode.*;
import org.apache.commons.math3.ode.events.AbstractEvent;
import org.apache.commons.math3.ode.events.ContinuousIntegratorEvent;
import org.apache.commons.math3.ode.sampling.StepHandler;
import org.apache.commons.math3.ode.sampling.StepInterpolator;
import org.apache.commons.math3.ode.test.AbstractODETest;
import org.apache.commons.math3.ode.test.ODETestUtils;
import org.junit.Before;
import org.junit.Test;

/**
 * Test class for {@link AbstractIntegrator}.
 */
public class AbstractIntegratorTest extends AbstractODETest {

    /** Integrator instance. */
    private AbstractIntegrator integrator;

    /** Test equations. */
    private ExpandableStatefulODE equations;

    /** Integration time. */
    private double t;

    /** Initial state. */
    private double[] y0;

    /** Final state. */
    private double[] y;

    /**
     * Set up.
     */
    @Before
    public void setUp() {
        integrator = new AbstractIntegrator() {
            @Override
            public void integrate(ExpandableStatefulODE equations, double t)
                    throws NumberIsTooSmallException, DimensionMismatchException,
                           MaxCountExceededException, NoBracketingException {
                super.integrate(equations, t);
                // no-op
            }
        };
        integrator.setMaxEvaluations(100);
        equations = new ExpandableStatefulODE() {
            @Override
            public int getDimension() {
                return 2;
            }

            @Override
            public void computeDerivatives(double time, double[] state, double[] derivatives) {
                derivatives[0] = state[1];
                derivatives[1] = -state[0];
            }
        };
        y0 = new double[]{0.1, 0.2};
        t = 2.0;
    }

    /**
     * Test basic functionality of the integrator.
     * @throws Exception if integration fails
     */
    @Test
    public void testIntegration() throws Exception {
        integrator.initIntegration(0.0, y0, 2.0);
        integrator.addStepHandler(step -> {
            assertEquals(y0[0], step.getPrimaryState()[0], 1e-6);
            assertEquals(y0[1], step.getPrimaryState()[1], 1e-6);
            step.setAccepted(true);
        });
        integrator.integrate(equations, t, y0, y);
        assertEquals(0.1 * Math.exp(-1.0), y[0], 1e-6);
        assertEquals(0.2 * Math.exp(-1.0), y[1], 1e-6);
    }

    /**
     * Test integration with events.
     * @throws Exception if integration fails
     */
    @Test
    public void testIntegrationWithEvents() throws Exception {
        integrator.initIntegration(0.0, y0, 2.0);
        integrator.addEventHandler(new ContinuousIntegratorEvent() {
            @Override
            public boolean isSatisfied(double time, double[] state) {
                return state[0] > 0.5;
            }

            @Override
            public void evaluateState(double time, double[] state, double[] newState) {
                newState[0] = 1.0;
                newState[1] = 0.0;
            }
        }, 1.0, 0.01, 10, new BracketingNthOrderBrentSolver(0.01, 5));
        integrator.integrate(equations, t, y0, y);
        assertEquals(1.0, y[0], 1e-6);
        assertEquals(0.0, y[1], 1e-6);
    }

    /**
     * Test integration with step handlers that change the step size.
     * @throws Exception if integration fails
     */
    @Test
    public void testIntegrationWithVariableStepSize() throws Exception {
        integrator.initIntegration(0.0, y0, 2.0);
        integrator.addStepHandler(step -> {
            if (step.getEvaluationCount() % 2 == 0) {
                step.setStepSize(step.getStepSize() * 2.0);
            }
            step.setAccepted(true);
        });
        integrator.integrate(equations, t, y0, y);
        // The number of evaluations is odd, so the last step size was not changed
        assertEquals(1 + (int) Math.ceil(t / (2.0 * evaluations.getCount())),
                     evaluations.getCount(), 1);
    }

    /**
     * Test integration with events that stop the integration.
     * @throws Exception if integration fails
     */
    @Test
    public void testIntegrationWithStoppingEvents() throws Exception {
        integrator.initIntegration(0.0, y0, 2.0);
        integrator.addEventHandler(new ContinuousIntegratorEvent() {
            @Override
            public boolean isSatisfied(double time, double[] state) {
                return state[0] > 1.0;
            }

            @Override
            public void evaluateState(double time, double[] state, double[] newState) {
                // no-op
            }
        }, 1.5, 0.01, 10, new BracketingNthOrderBrentSolver(0.01, 5));
        integrator.addEventHandler(new ContinuousIntegratorEvent() {
            @Override
            public boolean isSatisfied(double time, double[] state) {
                return state[0] > 2.0;
            }

            @Override
            public void evaluateState(double time, double[] state, double[] newState) {
                throw new AssertionError("Should not reach this point");
            }
        }, 2.0, 0.01, 10, new BracketingNthOrderBrentSolver(0.01, 5));
        integrator.integrate(equations, t, y0, y);
        assertEquals(1.5, integrator.getCurrentStepStart(), 1e-6);
    }

    /**
     * Test integration with events that reset the state.
     * @throws Exception if integration fails
     */
    @Test
    public void testIntegrationWithResettingEvents() throws Exception {
        integrator.initIntegration(0.0, y0, 2.0);
        integrator.addEventHandler(new ContinuousIntegratorEvent() {
            @Override
            public boolean isSatisfied(double time, double[] state) {
                return state[0] > 0.5;
            }

            @Override
            public void evaluateState(double time, double[] state, double[] newState) {
                newState[0] = 0.0;
                newState[1] = 0.0;
            }
        }, 1.0, 0.01, 10, new BracketingNthOrderBrentSolver(0.01, 5));
        integrator.integrate(equations, t, y0, y);
        assertEquals(0.0, y[0], 1e-6);
        assertEquals(0.0, y[1], 1e-6);
    }

    /**
     * Test integration with wrong number of states.
     * @throws Exception if integration fails
     */
    @Test(expected = DimensionMismatchException.class)
    public void testIntegrationWithWrongNumberOfStates() throws Exception {
        integrator.initIntegration(0.0, new double[]{0.1, 0.2, 0.3}, 2.0);
        integrator.integrate(equations, t, new double[]{0.1, 0.2, 0.3}, y);
    }

    /**
     * Test integration with wrong number of derivatives.
     * @throws Exception if integration fails
     */
    @Test(expected = DimensionMismatchException.class)
    public void testIntegrationWithWrongNumberOfDerivatives() throws Exception {
        integrator.initIntegration(0.0, y0, 2.0);
        integrator.integrate(new ExpandableStatefulODE() {
            @Override
            public int getDimension() {
                return 3;
            }

            @Override
            public void computeDerivatives(double time, double[] state, double[] derivatives) {
                derivatives[0] = state[1];
                derivatives[1] = -state[0];
                derivatives[2] = state[0];
            }
        }, t, y0, y);
    }

    /**
     * Test integration with max evaluation limit reached.
     * @throws Exception if integration fails
     */
    @Test(expected = MaxCountExceededException.class)
    public void testIntegrationWithMaxEvaluationsReached() throws Exception {
        integrator.setMaxEvaluations(5);
        integrator.integrate(equations, t, y0, y);
    }

    /**
     * Test integration with no bracketing.
     * @throws Exception if integration fails
     */
    @Test(expected = NoBracketingException.class)
    public void testIntegrationWithNoBracketing() throws Exception {
        integrator.initIntegration(0.0, y0, 2.0);
        integrator.addEventHandler(new ContinuousIntegratorEvent() {
            @Override
            public boolean isSatisfied(double time, double[] state) {
                throw new AssertionError("Should not reach this point");
            }
        }, 1.0, 0.01, 10, new BracketingNthOrderBrentSolver(0.01, 5));
        integrator.integrate(equations, t, y0, y);
    }

}