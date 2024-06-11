import org.apache.commons.math3.ode.*;
import org.apache.commons.math3.ode.events.EventHandler;
import org.apache.commons.math3.ode.events.EventState;
import org.apache.commons.math3.ode.sampling.AbstractStepInterpolator;
import org.apache.commons.math3.ode.sampling.StepHandler;
import org.apache.commons.math3.util.Precision;
import org.junit.*;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class AbstractIntegratorTest {

    private AbstractIntegrator integrator;
    private FirstOrderDifferentialEquations equations;
    private AbstractStepInterpolator interpolator;
    private ExpandableStatefulODE expandableODE;
    private double[] initialState;
    private double[] finalState;

    @Before
    public void setup() {
        integrator = new AbstractIntegrator() {
            @Override
            public void integrate(ExpandableStatefulODE equations, double t)
                    throws NumberIsTooSmallException, DimensionMismatchException,
                    MaxCountExceededException, NoBracketingException {
                sanityChecks(equations, t);
            }
        };
        equations = mock(FirstOrderDifferentialEquations.class);
        interpolator = mock(AbstractStepInterpolator.class);
        expandableODE = mock(ExpandableStatefulODE.class);
        when(expandableODE.getPrimaryState()).thenReturn(new double[0]);
        when(expandableODE.getTime()).thenReturn(0);
        when(equations.getDimension()).thenReturn(1);
        when(equations.getTime()).thenReturn(0);
        when(equations.getDerivatives(anyDoubleArray(), anyDoubleArray())).thenReturn(new double[0]);

        integrator.initIntegration(0, initialState = new double[1], 1);
        integrator.setEquations(expandableODE);
    }

    @Test
    public void testAddStepHandler() {
        integrator.addStepHandler(mock(StepHandler.class));
        assertEquals(1, integrator.getStepHandlers().size());
    }

    @Test
    public void testClearStepHandlers() {
        integrator.addStepHandler(mock(StepHandler.class));
        assertEquals(1, integrator.getStepHandlers().size());
        integrator.clearStepHandlers();
        assertEquals(0, integrator.getStepHandlers().size());
    }

    @Test
    public void testGetCurrentSignedStepsize() {
        assertEquals(Double.NaN, integrator.getCurrentSignedStepsize());
    }

    @Test(expected = IllegalStateException.class)
    public void testIntegrateNullEquations() {
        integrator.integrate(null, 1);
    }

    @Test
    public void testIntegrate() throws Exception {
        when(interpolator.getIntegrator()).thenReturn(integrator);
        when(interpolator.getSoftPreviousTime()).thenReturn(0);
        when(interpolator.getSoftCurrentTime()).thenReturn(1);
        when(interpolator.getGlobalPreviousTime()).thenReturn(0);
        when(interpolator.getGlobalCurrentTime()).thenReturn(1);
        when(interpolator.getInterpolatedState()).thenReturn(new double[]{1});

        integrator.addEventHandler(mock(EventHandler.class), 0, 1, 1);

        integrator.integrate(expandableODE, 1);

        verify(expandableODE).computeDerivatives(1, anyDoubleArray(), anyDoubleArray());
        verify(expandableODE).setTime(1);
        verify(expandableODE).setPrimaryState(anyDoubleArray());
        verifyZeroInteractions(interpolator);
    }

    @Test
    public void testIntegrateWithEvent() throws Exception {
        when(interpolator.getIntegrator()).thenReturn(integrator);
        when(interpolator.getSoftPreviousTime()).thenReturn(0);
        when(interpolator.getSoftCurrentTime()).thenReturn(1);
        when(interpolator.getGlobalPreviousTime()).thenReturn(0);
        when(interpolator.getGlobalCurrentTime()).thenReturn(1.5);
        when(interpolator.getInterpolatedState()).thenReturn(new double[]{1.5});

        integrator.addEventHandler(mock(EventHandler.class), 1, 1, 1);

        integrator.integrate(expandableODE, 1.5);

        verify(expandableODE).computeDerivatives(1.5, anyDoubleArray(), anyDoubleArray());
        verify(expandableODE).setTime(1.5);
        verify(expandableODE).setPrimaryState(new double[]{1.5});
        verify(mock(EventHandler.class)).handleStep(interpolator, true);
    }

    @Test
    public void testIntegrateWithReset() throws Exception {
        when(interpolator.getIntegrator()).thenReturn(integrator);
        when(interpolator.getSoftPreviousTime()).thenReturn(0);
        when(interpolator.getSoftCurrentTime()).thenReturn(1);
        when(interpolator.getGlobalPreviousTime()).thenReturn(0);
        when(interpolator.getGlobalCurrentTime()).thenReturn(1.5);
        when(interpolator.getInterpolatedState()).thenReturn(new double[]{1.5});

        integrator.addEventHandler(mock(EventHandler.class), 1, 1, 1);
        integrator.addStepHandler(mock(StepHandler.class));

        integrator.integrate(expandableODE, 1.5);

        verify(expandableODE).computeDerivatives(1.5, anyDoubleArray(), anyDoubleArray());
        verify(expandableODE).setTime(1.5);
        verify(expandableODE).setPrimaryState(new double[]{1.5});
        verify(mock(EventHandler.class)).handleStep(interpolator, true);
        verify(mock(StepHandler.class)).handleStep(interpolator, false);
    }

    @Test
    public void testIntegrateWithStop() throws Exception {
        when(interpolator.getIntegrator()).thenReturn(integrator);
        when(interpolator.getSoftPreviousTime()).thenReturn(0);
        when(interpolator.getSoftCurrentTime()).thenReturn(1);
        when(interpolator.getGlobalPreviousTime()).thenReturn(0);
        when(interpolator.getGlobalCurrentTime()).thenReturn(1.5);
        when(interpolator.getInterpolatedState()).thenReturn(new double[]{1.5});

        integrator.addEventHandler(mock(EventHandler.class), 1, 1, 1);
        integrator.addStepHandler(mock(StepHandler.class));
        doThrow(RuntimeException.class).when(mock(EventHandler.class)).handleStep(interpolator, anyBoolean());

        integrator.integrate(expandableODE, 1.5);

        verify(expandableODE).computeDerivatives(1.5, anyDoubleArray(), anyDoubleArray());
        verify(expandableODE).setTime(1.5);
        verify(expandableODE).setPrimaryState(new double[]{1.5});
        verify(mock(EventHandler.class)).handleStep(interpolator, true);
        verify(mock(StepHandler.class)).handleStep(interpolator, false);
        verify(mock(EventHandler.class)).handleStep(interpolator, false);
    }

    @Test
    public void testComputeDerivatives() throws Exception {
        integrator.integrate(expandableODE, 1);
        integrator.computeDerivatives(1, initialState, finalState);
        assertEquals(0, finalState.length);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testComputeDerivativesWithNullEquations() throws Exception {
        integrator.computeDerivatives(1, null, finalState);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testComputeDerivativesWithNullState() throws Exception {
        integrator.computeDerivatives(1, initialState, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testComputeDerivativesWithNullStateDot() throws Exception {
        integrator.computeDerivatives(1, initialState, finalState);
        integrator.computeDerivatives(1, initialState, null);
    }

    @Test
    public void testResetOccurredFlag() throws Exception {
        integrator.integrate(expandableODE, 1);
        integrator.computeDerivatives(1, initialState, finalState);
        assertTrue(integrator.resetOccurred);
        integrator.reset();
        assertFalse(integrator.resetOccurred);
    }

    @Test(expected = IllegalStateException.class)
    public void testIntegrateWithNullInitialState() throws Exception {
        integrator.initIntegration(0, null, 1);
        integrator.integrate(expandableODE, 1);
    }

    @Test(expected = IllegalStateException.class)
    public void testIntegrateWithNullFinalState() throws Exception {
        integrator.integrate(expandableODE, 1);
        integrator.computeDerivatives(1, initialState, null);
    }

    @Test(expected = IllegalStateException.class)
    public void testIntegrateWithNullIntegrationTime() throws Exception {
        integrator.integrate(expandableODE, Double.NaN);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIntegrateWithNegativeIntegrationTime() throws Exception {
        integrator.integrate(expandableODE, -1);
    }

    @Test
    public void testStateInitializedFlag() throws Exception {
        integrator.integrate(expandableODE, 1);
        assertFalse(integrator.isStateInitialized());
        integrator.setStateInitialized(true);
        assertTrue(integrator.isStateInitialized());
    }

    @Test(expected = IllegalStateException.class)
    public void testIntegrateWithNonStatefulODE() throws Exception {
        integrator = new AbstractIntegrator();
        integrator.integrate(mock(ODE.class), 1);
    }

    @Test(expected = IllegalStateException.class)
    public void testIntegrateWithAdaptiveStepSizeIntegratorWithNullODE() throws Exception {
        integrator = new AbstractIntegrator() {
            @Override
            public void integrate(ExpandableStatefulODE equations, double t)
                    throws NumberIsTooSmallException, DimensionMismatchException,
                    MaxCountExceededException, NoBracketingException {
                sanityChecks(equations, t);
            }

            @Override
            public void integrate(ODE ode, double t)
                    throws NumberIsTooSmallException, DimensionMismatchException,
                    MaxCountExceededException, NoBracketingException {
                throw new UnsupportedOperationException("Not supported by this integrator.");
            }
        };
        integrator.integrate(null, 1);
    }

    @Test
    public void testIntegrateWithAdaptiveStepSizeIntegrator() throws Exception {
        integrator = new AbstractIntegrator() {
            @Override
            public void integrate(ExpandableStatefulODE equations, double t)
                    throws NumberIsTooSmallException, DimensionMismatchException,
                    MaxCountExceededException, NoBracketingException {
                sanityChecks(equations, t);
            }

            @Override
            public void integrate(ODE ode, double t)
                    throws NumberIsTooSmallException, DimensionMismatchException,
                    MaxCountExceededException, NoBracketingException {
                integrator.integrate(new ExpandableStatefulODE(ode), t);
            }
        };
        integrator.integrate(mock(ODE.class), 1);
    }
}