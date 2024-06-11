import static org.junit.Assert.*;

import org.apache.commons.math3.ode.*;
import org.apache.commons.math3.ode.events.*;
import org.apache.commons.math3.ode.sampling.*;
import org.apache.commons.math3.util.Precision;
import org.junit.Before;
import org.junit.Test;

/**
 * Test class for AbstractIntegrator.
 */
public class AbstractIntegratorTest {

    private static final double EPSILON = 1e-10;
    private AbstractIntegrator integrator;
    private FirstOrderDifferentialEquations equations;

    /**
     * Set up test environment.
     */
    @Before
    public void setUp() {
        integrator = new AbstractIntegrator() {
            @Override
            protected void integrate(ExpandableStatefulODE equations, double t)
                    throws NumberIsTooSmallException, DimensionMismatchException,
                           MaxCountExceededException, NoBracketingException {
                // simply do nothing
            }
        };
        equations = new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yDot) {
                yDot[0] = y[0] * t;
            }
        };
    }

    /**
     * Test constructor.
     */
    @Test
    public void testConstructor() {
        assertNotNull(integrator);
    }

    /**
     * Test addStepHandler.
     */
    @Test
    public void testAddStepHandler() {
        integrator.addStepHandler(new StepHandler() {
            @Override
            public void handleStep(StepContext context, int stepNumber) {
                assertEquals(1.0, context.getStepSize(), EPSILON);
            }
        });
        integrator.addStepHandler(new StepHandler() {
            @Override
            public void handleStep(StepContext context, int stepNumber) {
                assertEquals(2.0, context.getStepSize(), EPSILON);
            }
        });
        assertEquals(2, integrator.getStepHandlers().size());
    }

    /**
     * Test getName.
     */
    @Test
    public void testGetName() {
        assertEquals(integrator.getClass().getSimpleName(), integrator.getName());
    }

    /**
     * Test addEventHandler.
     */
    @Test
    public void testAddEventHandler() {
        integrator.addEventHandler(new EventHandler() {
            @Override
            public void evaluateEvent(double t, double[] y, double[] yDot, double eventTime,
                                     double eventDirection, int eventType, EventState eventState) {
                assertEquals(t, eventTime, EPSILON);
            }
        }, 0.0, 0.0, 1, new BracketingNthOrderBrentSolver(0.001, 5));
        assertEquals(1, integrator.getEventHandlers().size());
    }

    /**
     * Test getEventHandlers.
     */
    @Test
    public void testGetEventHandlers() {
        integrator.addEventHandler(new EventHandler() {
            @Override
            public void evaluateEvent(double t, double[] y, double[] yDot, double eventTime,
                                     double eventDirection, int eventType, EventState eventState) {
                assertEquals(t, eventTime, EPSILON);
            }
        }, 0.0, 0.0, 1, new BracketingNthOrderBrentSolver(0.001, 5));
        EventHandler eventHandler = integrator.getEventHandlers().iterator().next();
        assertEquals(eventHandler.getClass(), integrator.getEventHandlers().get(0).getClass());
    }

    /**
     * Test clearEventHandlers.
     */
    @Test
    public void testClearEventHandlers() {
        integrator.addEventHandler(new EventHandler() {
            @Override
            public void evaluateEvent(double t, double[] y, double[] yDot, double eventTime,
                                     double eventDirection, int eventType, EventState eventState) {
                assertEquals(t, eventTime, EPSILON);
            }
        }, 0.0, 0.0, 1, new BracketingNthOrderBrentSolver(0.001, 5));
        integrator.clearEventHandlers();
        assertTrue(integrator.getEventHandlers().isEmpty());
    }

    /**
     * Test getCurrentStepStart.
     */
    @Test
    public void testGetCurrentStepStart() {
        integrator.setCurrentStepStart(1.0);
        assertEquals(1.0, integrator.getCurrentStepStart(), EPSILON);
    }

    /**
     * Test getCurrentSignedStepsize.
     */
    @Test
    public void testGetCurrentSignedStepsize() {
        integrator.setCurrentSignedStepsize(1.0);
        assertEquals(1.0, integrator.getCurrentSignedStepsize(), EPSILON);
    }

    /**
     * Test setMaxEvaluations.
     */
    @Test
    public void testSetMaxEvaluations() {
        integrator.setMaxEvaluations(5);
        assertEquals(5, integrator.getMaxEvaluations());
    }

    /**
     * Test getMaxEvaluations.
     */
    @Test
    public void testGetMaxEvaluations() {
        integrator.setMaxEvaluations(5);
        assertEquals(5, integrator.getMaxEvaluations());
    }

    /**
     * Test getEvaluations.
     */
    @Test
    public void testGetEvaluations() {
        integrator.setMaxEvaluations(5);
        integrator.evaluate(equations, 0.0, new double[1], 1.0, new double[1]);
        integrator.evaluate(equations, 0.0, new double[1], 1.0, new double[1]);
        integrator.evaluate(equations, 0.0, new double[1], 1.0, new double[1]);
        integrator.evaluate(equations, 0.0, new double[1], 1.0, new double[1]);
        integrator.evaluate(equations, 0.0, new double[1], 1.0, new double[1]);
        assertEquals(5, integrator.getEvaluations());
    }

    /**
     * Test initIntegration.
     */
    @Test
    public void testInitIntegration() {
        integrator.initIntegration(0.0, new double[1], 1.0);
        assertFalse(integrator.isStateInitialized());
    }

    /**
     * Test integrate.
     */
    @Test(expected = DimensionMismatchException.class)
    public void testIntegrateDimensionMismatchException() {
        integrator.integrate(new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return 2;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yDot) {
                // not implemented
            }
        }, 0.0, new double[1], 1.0, new double[1]);
    }

    /**
     * Test integrate with maximum number of evaluations exceeded.
     */
    @Test(expected = MaxCountExceededException.class)
    public void testIntegrateMaxCountExceededException() {
        integrator.setMaxEvaluations(1);
        integrator.integrate(equations, 0.0, new double[1], 1.0, new double[1]);
        integrator.integrate(equations, 0.0, new double[1], 1.0, new double[1]);
    }

    /**
     * Test integrate with no bracketing.
     */
    @Test(expected = NoBracketingException.class)
    public void testIntegrateNoBracketingException() {
        integrator.setStepHandler(new StepHandler() {
            @Override
            public void handleStep(StepContext context, int stepNumber) {
                context.setAccepted(false);
            }
        });
        integrator.integrate(equations, 0.0, new double[1], 1.0, new double[1]);
    }

    /**
     * Test integrate.
     */
    @Test
    public void testIntegrate() throws NumberIsTooSmallException, DimensionMismatchException,
            MaxCountExceededException, NoBracketingException {
        integrator.integrate(equations, 0.0, new double[1], 2.0, new double[1]);
        double[] result = new double[1];
        integrator.getPrimaryState(result);
        assertEquals(1.0, result[0], EPSILON);
    }

    /**
     * Test computeDerivatives.
     */
    @Test
    public void testComputeDerivatives() throws MaxCountExceededException, DimensionMismatchException {
        integrator.computeDerivatives(1.0, new double[1], new double[1]);
        double[] result = new double[1];
        integrator.getPrimaryState(result);
        assertEquals(1.0, result[0], EPSILON);
        assertEquals(1.0 * 1.0, result[1], EPSILON);
    }

    /**
     * Test setStateInitialized.
     */
    @Test
    public void testSetStateInitialized() {
        integrator.setStateInitialized(true);
        assertTrue(integrator.isStateInitialized());
    }

    /**
     * Test acceptStep.
     */
    @Test
    public void testAcceptStep() throws NumberIsTooSmallException, DimensionMismatchException,
            MaxCountExceededException, NoBracketingException {
        integrator.addStepHandler(new StepHandler() {
            @Override
            public void handleStep(StepContext context, int stepNumber) {
                assertEquals(1.0, context.getStepSize(), EPSILON);
            }
        });
        integrator.setMaxEvaluations(1);
        integrator.integrate(equations, 0.0, new double[1], 1.0, new double[1]);
        integrator.acceptStep(new AbstractStepInterpolator() {}, new double[1], new double[1], 1.0);
        integrator.getPrimaryState(new double[1]);
        integrator.evaluate(equations, 1.0, new double[1], 1.0, new double[1]);
        integrator.getPrimaryState(new double[1]);
        assertEquals(2, integrator.getEvaluations());
    }

}