import static org.junit.Assert.*;
import org.apache.commons.math3.ode.AbstractIntegrator;
import org.apache.commons.math3.ode.ExpandableStatefulODE;
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.events.EventHandler;
import org.apache.commons.math3.ode.events.EventState;
import org.apache.commons.math3.ode.sampling.AbstractStepInterpolator;
import org.apache.commons.math3.ode.sampling.StepHandler;
import org.junit.Before;
import org.junit.Test;

/**
 * Test class for AbstractIntegrator.
 */
public class AbstractIntegratorTest {

    private AbstractIntegrator integrator;

    /**
     * Set up test.
     */
    @Before
    public void setUp() {
        integrator = new AbstractIntegrator() {
            @Override
            protected void integrate(ExpandableStatefulODE equations, double t)
                    throws NumberIsTooSmallException, DimensionMismatchException,
                           MaxCountExceededException, NoBracketingException {
                // no-op
            }
        };
    }

    /**
     * Test addStepHandler.
     */
    @Test
    public void testAddStepHandler() {
        StepHandler stepHandler = mock(StepHandler.class);
        integrator.addStepHandler(stepHandler);
        assertEquals(stepHandler, integrator.getStepHandlers().iterator().next());
    }

    /**
     * Test addEventHandler.
     */
    @Test
    public void testAddEventHandler() {
        EventHandler eventHandler = mock(EventHandler.class);
        integrator.addEventHandler(eventHandler, 0.1, 1.0e-5, 10);
        assertEquals(eventHandler, integrator.getEventHandlers().iterator().next());
    }

    /**
     * Test getCurrentSignedStepsize.
     */
    @Test
    public void testgetCurrentSignedStepsize() {
        integrator.setCurrentSignedStepsize(1.0);
        assertEquals(1.0, integrator.getCurrentSignedStepsize(), 0.0);
    }

    /**
     * Test integrate.
     */
    @Test(expected = IllegalStateException.class)
    public void testIntegrate() {
        ExpandableStatefulODE equations = new ExpandableStatefulODE(new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yDot) {
                yDot[0] = 1.0;
            }
        });
        integrator.initIntegration(0.0, new double[1], 1.0);
        integrator.integrate(equations, 1.0);
    }

    /**
     * Test computeDerivatives.
     */
    @Test(expected = IllegalStateException.class)
    public void testComputeDerivatives() {
        integrator.computeDerivatives(0.0, new double[1], new double[1]);
    }

    /**
     * Test setMaxEvaluations.
     */
    @Test
    public void testSetMaxEvaluations() {
        integrator.setMaxEvaluations(10);
        assertEquals(10, integrator.getMaxEvaluations());
    }

    /**
     * Test getEvaluations.
     */
    @Test
    public void testGetEvaluations() {
        integrator.evaluations.incrementCount();
        integrator.evaluations.resetCount();
        integrator.evaluations.incrementCount();
        integrator.evaluations.incrementCount();
        integrator.evaluations.incrementCount();
        integrator.evaluations.incrementCount();
        integrator.evaluations.incrementCount();
        integrator.evaluations.incrementCount();
        integrator.evaluations.incrementCount();
        integrator.evaluations.incrementCount();
        assertEquals(10, integrator.getEvaluations());
    }

    /**
     * Test setEquations.
     */
    @Test
    public void testSetEquations() {
        ExpandableStatefulODE equations = new ExpandableStatefulODE(new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yDot) {
                yDot[0] = 1.0;
            }
        });
        integrator.setEquations(equations);
        assertEquals(equations, integrator.expandable);
    }

    /**
     * Test acceptStep.
     */
    @Test
    public void testAcceptStep() throws Exception {
        ExpandableStatefulODE equations = new ExpandableStatefulODE(new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yDot) {
                yDot[0] = 1.0;
            }
        });
        integrator.setEquations(equations);
        integrator.initIntegration(0.0, new double[1], 1.0);

        AbstractStepInterpolator interpolator = mock(AbstractStepInterpolator.class);
        when(interpolator.getGlobalPreviousTime()).thenReturn(0.0);
        when(interpolator.getGlobalCurrentTime()).thenReturn(0.5);
        when(interpolator.getInterpolatedState()).thenReturn(new double[1]);
        when(interpolator.getInterpolatedTime()).thenReturn(0.5);

        double[] y = new double[1];
        double[] yDot = new double[1];

        integrator.acceptStep(interpolator, y, yDot, 1.0);
        assertEquals(0.5, integrator.getCurrentStepStart(), 0.0);
        assertEquals(0.5, y[0], 0.0);
        assertEquals(1.0, yDot[0], 0.0);

        verify(interpolator).setInterpolatedTime(0.5);
        verify(interpolator).setSoftCurrentTime(0.5);
    }

}