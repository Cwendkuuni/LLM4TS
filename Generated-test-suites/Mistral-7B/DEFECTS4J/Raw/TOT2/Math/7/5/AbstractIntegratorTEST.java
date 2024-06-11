import org.apache.commons.math3.ode.AbstractIntegrator;
import org.apache.commons.math3.ode.ExpandableStatefulODE;
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.events.EventHandler;
import org.apache.commons.math3.ode.sampling.StepHandler;
import org.apache.commons.math3.ode.sampling.StepInterpolator;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Test cases for AbstractIntegrator class.
 */
public class AbstractIntegratorTest {

    private AbstractIntegrator integrator;
    private ExpandableStatefulODE equations;

    /**
     * Initialize the integrator and equation objects for testing.
     */
    @Before
    public void setUp() {
        integrator = new AbstractIntegrator() {
            @Override
            public void integrate(ExpandableStatefulODE equations, double t)
                    throws NumberIsTooSmallException, DimensionMismatchException,
                           MaxCountExceededException, NoBracketingException {
                // no-op
            }
        };
        equations = new ExpandableStatefulODE() {
            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yDot) {
                yDot[0] = y[0] * 2;
            }
        };
    }

    /**
     * Test basic functionality of integrator with no events or step handlers.
     */
    @Test
    public void testBasicFunctionality() {
        integrator.initIntegration(0.0, new double[] { 1.0 }, 10.0);
        integrator.integrate(equations, 10.0);
        Assert.assertEquals(10.0, integrator.getCurrentStepStart(), 1e-12);
        Assert.assertArrayEquals(new double[] { 20.0 }, integrator.getState(), 1e-12);
    }

    /**
     * Test edge case: integration time is less than starting time.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testIntegrationTimeLessThanStartingTime() {
        integrator.initIntegration(0.0, new double[] { 1.0 }, 5.0);
        integrator.integrate(equations, 4.0);
    }

    /**
     * Test edge case: initial state vector and final state vector have different dimensions.
     */
    @Test(expected = DimensionMismatchException.class)
    public void testDifferentDimensions() {
        integrator = new AbstractIntegrator() {
            @Override
            public void integrate(ExpandableStatefulODE equations, double t)
                    throws NumberIsTooSmallException, DimensionMismatchException,
                           MaxCountExceededException, NoBracketingException {
                // no-op
            }
        };
        integrator.initIntegration(0.0, new double[] { 1.0, 1.0 }, 10.0);
        equations.setDimension(2);
        integrator.integrate(equations, 10.0);
    }

    /**
     * Test error handling: MaxCountExceededException.
     */
    @Test(expected = MaxCountExceededException.class)
    public void testMaxCountExceededException() {
        integrator = new AbstractIntegrator() {
            @Override
            public int getMaxEvaluations() {
                return 1;
            }

            @Override
            public void integrate(ExpandableStatefulODE equations, double t)
                    throws NumberIsTooSmallException, DimensionMismatchException,
                           MaxCountExceededException, NoBracketingException {
                // no-op
            }
        };
        integrator.initIntegration(0.0, new double[] { 1.0 }, 10.0);
        for (int i = 0; i < integrator.getMaxEvaluations(); i++) {
            integrator.computeDerivatives(integrator.getCurrentStepStart(), integrator.getState(), null);
        }
        integrator.integrate(equations, 10.0);
    }

    /**
     * Test error handling: NoBracketingException.
     */
    @Test(expected = NoBracketingException.class)
    public void testNoBracketingException() {
        integrator = new AbstractIntegrator() {
            @Override
            public void integrate(ExpandableStatefulODE equations, double t)
                    throws NumberIsTooSmallException, DimensionMismatchException,
                           MaxCountExceededException, NoBracketingException {
                // no-op
            }

            @Override
            public int getMaxEvaluations() {
                return 1;
            }
        };
        integrator.initIntegration(0.0, new double[] { 1.0 }, 10.0);
        integrator.setMaxEvaluations(0);
        integrator.integrate(equations, 10.0);
    }

    /**
     * Test step handlers: handleStep is called at each step.
     */
    @Test
    public void testStepHandlers() {
        integrator = new AbstractIntegrator() {
            private int stepCount = 0;

            @Override
            public void integrate(ExpandableStatefulODE equations, double t)
                    throws NumberIsTooSmallException, DimensionMismatchException,
                           MaxCountExceededException, NoBracketingException {
                // no-op
            }

            @Override
            public void handleStep(StepHandler handler, boolean lastStep) {
                stepCount++;
            }
        };
        integrator.addStepHandler(new StepHandler() {
            @Override
            public void handleStep(StepHandler handler, boolean lastStep) {
                // no-op
            }
        });
        integrator.initIntegration(0.0, new double[] { 1.0 }, 10.0);
        integrator.integrate(equations, 10.0);
        Assert.assertEquals(2, integrator.getStepHandlers().size());
        Assert.assertEquals(3, integrator.getStepHandlers().get(0).getCallCount());
        Assert.assertEquals(1, integrator.getStepHandlers().get(1).getCallCount());
    }

    /**
     * Test events: event occurs during first step.
     */
    @Test
    public void testEventOccursDuringFirstStep() {
        integrator = new AbstractIntegrator() {
            @Override
            public void integrate(ExpandableStatefulODE equations, double t)
                    throws NumberIsTooSmallException, DimensionMismatchException,
                           MaxCountExceededException, NoBracketingException {
                // no-op
            }
        };
        integrator.addEventHandler(new EventHandler() {
            @Override
            public boolean evaluateStep(StepInterpolator interpolator) {
                return interpolator.getGlobalCurrentTime() > 1.5;
            }

            @Override
            public void handleEvent(double t, double[] y) {
                System.out.println("Event occurred at t=" + t);
            }
        }, 1.0, 1e-6, 10, null);
        integrator.initIntegration(0.0, new double[] { 1.0 }, 3.0);
        integrator.integrate(equations, 3.0);
        Assert.assertTrue(integrator.isLastStep());
    }

    /**
     * Test events: event occurs during intermediate step.
     */
    @Test
    public void testEventOccursDuringIntermediateStep() {
        integrator = new AbstractIntegrator() {
            @Override
            public void integrate(ExpandableStatefulODE equations, double t)
                    throws NumberIsTooSmallException, DimensionMismatchException,
                           MaxCountExceededException, NoBracketingException {
                // no-op
            }
        };
        integrator.addEventHandler(new EventHandler() {
            @Override
            public boolean evaluateStep(StepInterpolator interpolator) {
                return interpolator.getGlobalCurrentTime() > 1.5;
            }

            @Override
            public void handleEvent(double t, double[] y) {
                System.out.println("Event occurred at t=" + t);
            }
        }, 1.0, 1e-6, 10, null);
        integrator.initIntegration(0.0, new double[] { 1.0 }, 5.0);
        integrator.integrate(equations, 5.0);
        Assert.assertFalse(integrator.isLastStep());
    }

    /**
     * Test events: event occurs during last step.
     */
    @Test
    public void testEventOccursDuringLastStep() {
        integrator = new AbstractIntegrator() {
            @Override
            public void integrate(ExpandableStatefulODE equations, double t)
                    throws NumberIsTooSmallException, DimensionMismatchException,
                           MaxCountExceededException, NoBracketingException {
                // no-op
            }
        };
        integrator.addEventHandler(new EventHandler() {
            @Override
            public boolean evaluateStep(StepInterpolator interpolator) {
                return interpolator.getGlobalCurrentTime() > 2.5;
            }

            @Override
            public void handleEvent(double t, double[] y) {
                System.out.println("Event occurred at t=" + t);
            }
        }, 1.0, 1e-6, 10, null);
        integrator.initIntegration(0.0, new double[] { 1.0 }, 6.0);
        integrator.integrate(equations, 6.0);
        Assert.assertTrue(integrator.isLastStep());
    }
}