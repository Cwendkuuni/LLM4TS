import org.apache.commons.math3.ode.*;
import org.apache.commons.math3.ode.events.EventHandler;
import org.apache.commons.math3.ode.events.EventState;
import org.apache.commons.math3.ode.sampling.StepHandler;
import org.apache.commons.math3.ode.sampling.AbstractStepInterpolator;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.MathIllegalStateException;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests for the AbstractIntegrator class.
 */
public class AbstractIntegratorTest {

    /** Integrator under test. */
    private AbstractIntegrator integrator;

    /** Event handler mock. */
    private EventHandler eventHandlerMock;

    /** Step handler mock. */
    private StepHandler stepHandlerMock;

    /**
     * Sets up the test: creates an integrator instance.
     */
    @Before
    public void setUp() {
        integrator = new AbstractIntegrator() {
            @Override
            public void integrate(ExpandableStatefulODE equations, double t)
                    throws MathIllegalStateException, MathIllegalArgumentException {
                // empty implementation
            }
        };
        eventHandlerMock = new EventHandler() {
            @Override
            public boolean evaluateStep(AbstractStepInterpolator interpolator) {
                return false;
            }

            @Override
            public void stepAccepted(double eventTime, double[] eventY) {
                // empty implementation
            }

            @Override
            public boolean stop() {
                return false;
            }
        };
        stepHandlerMock = new StepHandler() {
            @Override
            public void handleStep(AbstractStepInterpolator interpolator, boolean lastStep) {
                // empty implementation
            }
        };
        integrator.addEventHandler(eventHandlerMock, 0.0, 1.0e-6, 10, new BracketingNthOrderBrentSolver(1.0e-6, 5));
        integrator.addStepHandler(stepHandlerMock);
    }

    /**
     * Test method for {@link AbstractIntegrator#addEventHandler(EventHandler, double, double, int, UnivariateRealSolver)}.
     */
    @Test
    public void testAddEventHandler() {
        integrator.addEventHandler(eventHandlerMock, 0.0, 1.0e-6, 10, new BracketingNthOrderBrentSolver(1.0e-6, 5));
        Assert.assertEquals(1, integrator.getEventHandlers().size());
        integrator.clearEventHandlers();
        Assert.assertEquals(0, integrator.getEventHandlers().size());
    }

    /**
     * Test method for {@link AbstractIntegrator#clearEventHandlers()}.
     */
    @Test
    public void testClearEventHandlers() {
        integrator.addEventHandler(eventHandlerMock, 0.0, 1.0e-6, 10, new BracketingNthOrderBrentSolver(1.0e-6, 5));
        integrator.clearEventHandlers();
        Assert.assertEquals(0, integrator.getEventHandlers().size());
    }

    /**
     * Test method for {@link AbstractIntegrator#addStepHandler(StepHandler)}.
     */
    @Test
    public void testAddStepHandler() {
        integrator.addStepHandler(stepHandlerMock);
        Assert.assertEquals(1, integrator.getStepHandlers().size());
        integrator.clearStepHandlers();
        Assert.assertEquals(0, integrator.getStepHandlers().size());
    }

    /**
     * Test method for {@link AbstractIntegrator#clearStepHandlers()}.
     */
    @Test
    public void testClearStepHandlers() {
        integrator.addStepHandler(stepHandlerMock);
        integrator.clearStepHandlers();
        Assert.assertEquals(0, integrator.getStepHandlers().size());
    }

    /**
     * Test method for {@link AbstractIntegrator#getName()}.
     */
    @Test
    public void testGetName() {
        Assert.assertNull(integrator.getName());
        integrator = new AbstractIntegrator("testName") {
            // empty implementation
        };
        Assert.assertEquals("testName", integrator.getName());
    }

    /**
     * Test method for {@link AbstractIntegrator#integrate(ExpandableStatefulODE, double)}.
     */
    @Test(expected = MathIllegalStateException.class)
    public void testIntegrateWithNullEquations() {
        integrator.integrate(null, 0.0);
    }

    /**
     * Test method for {@link AbstractIntegrator#integrate(ExpandableStatefulODE, double)}.
     */
    @Test(expected = MathIllegalArgumentException.class)
    public void testIntegrateWithInvalidTime() {
        ExpandableStatefulODE equations = new ExpandableStatefulODE() {
            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yDot) {
                yDot[0] = Math.sin(y[0]);
            }
        };
        integrator.setEquations(equations);
        integrator.integrate(equations, Double.NaN);
    }

    /**
     * Test method for {@link AbstractIntegrator#integrate(ExpandableStatefulODE, double)}.
     */
    @Test(expected = MathIllegalStateException.class)
    public void testIntegrateWithNotInitializedEquations() {
        ExpandableStatefulODE equations = new ExpandableStatefulODE() {
            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yDot) {
                yDot[0] = Math.sin(y[0]);
            }
        };
        integrator.integrate(equations, 0.0);
    }

    /**
     * Test method for {@link AbstractIntegrator#integrate(ExpandableStatefulODE, double)}.
     */
    @Test(expected = MathIllegalArgumentException.class)
    public void testIntegrateWithInvalidInitialState() {
        ExpandableStatefulODE equations = new ExpandableStatefulODE() {
            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yDot) {
                yDot[0] = Math.sin(y[0]);
            }
        };
        integrator.setEquations(equations);
        integrator.integrate(equations, 0.0, new double[] {-1.0}, 0.0);
    }

    /**
     * Test method for {@link AbstractIntegrator#integrate(ExpandableStatefulODE, double)}.
     */
    @Test
    public void testIntegrateWithValidArguments() {
        ExpandableStatefulODE equations = new ExpandableStatefulODE() {
            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yDot) {
                yDot[0] = Math.sin(y[0]);
            }
        };
        integrator.setEquations(equations);
        integrator.integrate(equations, 0.0, new double[] {0.0}, 1.0);
    }

    /**
     * Test method for {@link AbstractIntegrator#integrate(ExpandableStatefulODE, double)}.
     */
    @Test(expected = MathIllegalStateException.class)
    public void testIntegrateWithMaxEvaluationsReached() {
        ExpandableStatefulODE equations = new ExpandableStatefulODE() {
            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yDot) {
                yDot[0] = Math.sin(y[0]);
            }
        };
        integrator.setEquations(equations);
        integrator.setMaxEvaluations(1);
        integrator.integrate(equations, 0.0, new double[] {0.0}, 1.0);
    }

    /**
     * Test method for {@link AbstractIntegrator#integrate(ExpandableStatefulODE, double)}.
     */
    @Test
    public void testIntegrateWithMaxEvaluationsNotReached() {
        ExpandableStatefulODE equations = new ExpandableStatefulODE() {
            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yDot) {
                yDot[0] = Math.sin(y[0]);
            }
        };
        integrator.setEquations(equations);
        integrator.setMaxEvaluations(100);
        integrator.integrate(equations, 0.0, new double[] {0.0}, 10.0);
    }

    /**
     * Test method for {@link AbstractIntegrator#acceptStep(AbstractStepInterpolator, double[], double[], double)}.
     */
    @Test(expected = MathIllegalStateException.class)
    public void testAcceptStepWithNullInterpolator() {
        integrator.acceptStep(null, new double[] {0.0}, new double[] {0.0}, 0.0);
    }

    /**
     * Test method for {@link AbstractIntegrator#acceptStep(AbstractStepInterpolator, double[], double[], double)}.
     */
    @Test(expected = MathIllegalStateException.class)
    public void testAcceptStepWithNullState() {
        AbstractStepInterpolator interpolator = new AbstractStepInterpolator() {
            @Override
            public double getGlobalPreviousTime() {
                return 0.0;
            }

            @Override
            public double getGlobalCurrentTime() {
                return 0.0;
            }

            @Override
            public void setInterpolatedTime(double interpolatedTime) {
                // empty implementation
            }

            @Override
            public double[] getInterpolatedState() {
                return new double[0];
            }

            @Override
            public double[] getInterpolatedDerivatives() {
                return new double[0];
            }

            @Override
            public void setSoftPreviousTime(double softPreviousTime) {
                // empty implementation
            }

            @Override
            public void setSoftCurrentTime(double softCurrentTime) {
                // empty implementation
            }
        };
        integrator.acceptStep(interpolator, null, new double[] {0.0}, 0.0);
    }

    /**
     * Test method for {@link AbstractIntegrator#acceptStep(AbstractStepInterpolator, double[], double[], double)}.
     */
    @Test(expected = MathIllegalStateException.class)
    public void testAcceptStepWithNullDerivatives() {
        AbstractStepInterpolator interpolator = new AbstractStepInterpolator() {
            @Override
            public double getGlobalPreviousTime() {
                return 0.0;
            }

            @Override
            public double getGlobalCurrentTime() {
                return 0.0;
            }

            @Override
            public void setInterpolatedTime(double interpolatedTime) {
                // empty implementation
            }

            @Override
            public double[] getInterpolatedState() {
                return new double[] {0.0};
            }

            @Override
            public double[] getInterpolatedDerivatives() {
                return null;
            }

            @Override
            public void setSoftPreviousTime(double softPreviousTime) {
                // empty implementation
            }

            @Override
            public void setSoftCurrentTime(double softCurrentTime) {
                // empty implementation
            }
        };
        integrator.acceptStep(interpolator, new double[] {0.0}, null, 0.0);
    }

    /**
     * Test method for {@link AbstractIntegrator#acceptStep(AbstractStepInterpolator, double[], double[], double)}.
     */
    @Test
    public void testAcceptStepWithValidArguments() {
        AbstractStepInterpolator interpolator = new AbstractStepInterpolator() {
            @Override
            public double getGlobalPreviousTime() {
                return 0.0;
            }

            @Override
            public double getGlobalCurrentTime() {
                return 0.0;
            }

            @Override
            public void setInterpolatedTime(double interpolatedTime) {
                // empty implementation
            }

            @Override
            public double[] getInterpolatedState() {
                return new double[] {0.0};
            }

            @Override
            public double[] getInterpolatedDerivatives() {
                return new double[] {1.0};
            }

            @Override
            public void setSoftPreviousTime(double softPreviousTime) {
                // empty implementation
            }

            @Override
            public void setSoftCurrentTime(double softCurrentTime) {
                // empty implementation
            }
        };
        integrator.acceptStep(interpolator, new double[] {0.0}, new double[] {1.0}, 0.1);
    }

}