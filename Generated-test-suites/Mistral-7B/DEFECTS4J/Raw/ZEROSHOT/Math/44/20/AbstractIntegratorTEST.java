import org.apache.commons.math3.analysis.solvers.BracketingNthOrderBrentSolver;
import org.apache.commons.math3.analysis.solvers.UnivariateRealSolver;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.MathIllegalStateException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.ode.ExpandableStatefulODE;
import org.apache.commons.math3.ode.events.AbstractStepInterpolator;
import org.apache.commons.math3.ode.events.EventHandler;
import org.apache.commons.math3.ode.events.EventState;
import org.apache.commons.math3.ode.sampling.StepHandler;
import org.apache.commons.math3.util.FastMath;
import org.apache.commons.math3.util.Incrementor;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.mockito.Mockito.*;

public class AbstractIntegratorTest {

    private AbstractIntegrator integrator;

    @Before
    public void setUp() {
        integrator = new AbstractIntegrator() {
            @Override
            public void integrate(ExpandableStatefulODE equations, double t)
                    throws MathIllegalStateException, MathIllegalArgumentException {
                // not implemented
            }

            @Override
            protected void acceptStep(AbstractStepInterpolator interpolator, double[] y, double[] yDot, double tEnd)
                    throws MathIllegalStateException {
                // not implemented
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yDot)
                    throws MaxCountExceededException {
                // not implemented
            }
        };
    }

    @Test
    public void testAddStepHandler() {
        StepHandler mockHandler = mock(StepHandler.class);
        integrator.addStepHandler(mockHandler);
        assertEquals(1, integrator.getStepHandlers().size());
        verify(mockHandler).handleStep(any(), eq(false));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testClearStepHandlers() {
        integrator.clearStepHandlers();
        integrator.getStepHandlers().clear();
        integrator.addStepHandler(mock(StepHandler.class));
        integrator.clearStepHandlers();
        assertEquals(0, integrator.getStepHandlers().size());
    }

    @Test
    public void testGetStepHandlers() {
        StepHandler mockHandler1 = mock(StepHandler.class);
        StepHandler mockHandler2 = mock(StepHandler.class);
        integrator.addStepHandler(mockHandler1);
        integrator.addStepHandler(mockHandler2);
        List<StepHandler> handlers = integrator.getStepHandlers();
        assertEquals(2, handlers.size());
        assertArrayEquals(new Object[]{mockHandler1, mockHandler2}, handlers.toArray());
    }

    @Test
    public void testIntegrate() {
        ExpandableStatefulODE equations = new ExpandableStatefulODE(new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yDot) {
                yDot[0] = y[0] + 1;
            }
        });
        integrator.setEquations(equations);
        integrator.setMaxEvaluations(1);
        integrator.integrate(equations, 0, new double[1], 1, new double[1]);
        verify(equations).setTime(1);
        verify(equations).setPrimaryState(new double[]{1});
        assertEquals(1, integrator.getEvaluations());
    }

    @Test(expected = MathIllegalStateException.class)
    public void testIntegrateWithMaxEvaluationsReached() {
        ExpandableStatefulODE equations = new ExpandableStatefulODE(new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yDot) {
                yDot[0] = y[0] + 1;
            }
        });
        integrator.setEquations(equations);
        integrator.setMaxEvaluations(0);
        integrator.integrate(equations, 0, new double[1], 1, new double[1]);
        verify(equations).setTime(0);
        verify(equations, never()).setPrimaryState(anyArray());
        assertEquals(0, integrator.getEvaluations());
    }

    @Test
    public void testIntegrateWithMaxEvaluationsNotReached() {
        ExpandableStatefulODE equations = new ExpandableStatefulODE(new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yDot) {
                yDot[0] = y[0] + 1;
            }
        });
        integrator.setEquations(equations);
        integrator.setMaxEvaluations(2);
        integrator.integrate(equations, 0, new double[1], 2, new double[1]);
        verify(equations).setTime(2);
        verify(equations).setPrimaryState(anyArray());
        verify(equations, times(2)).computeDerivatives(anyDouble(), anyArray(), anyArray());
        assertEquals(2, integrator.getEvaluations());
    }

    @Test
    public void testAddEventHandler() {
        EventHandler mockEventHandler = mock(EventHandler.class);
        integrator.addEventHandler(mockEventHandler, 0, 1, 1);
        assertEquals(1, integrator.getEventHandlers().size());
        verify(mockEventHandler).init(any());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddEventHandlerWithNegativeConvergence() {
        EventHandler mockEventHandler = mock(EventHandler.class);
        integrator.addEventHandler(mockEventHandler, 0, -1, 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddEventHandlerWithZeroConvergence() {
        EventHandler mockEventHandler = mock(EventHandler.class);
        integrator.addEventHandler(mockEventHandler, 0, 0, 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddEventHandlerWithNegativeMaxIterationCount() {
        EventHandler mockEventHandler = mock(EventHandler.class);
        integrator.addEventHandler(mockEventHandler, 0, 1, -1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddEventHandlerWithZeroMaxIterationCount() {
        EventHandler mockEventHandler = mock(EventHandler.class);
        integrator.addEventHandler(mockEventHandler, 0, 1, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddEventHandlerWithNullSolver() {
        EventHandler mockEventHandler = mock(EventHandler.class);
        integrator.addEventHandler(mockEventHandler, 0, 1, 1, null);
    }

    @Test
    public void testAddEventHandlerWithSolver() {
        EventHandler mockEventHandler = mock(EventHandler.class);
        UnivariateRealSolver mockSolver = mock(UnivariateRealSolver.class);
        integrator.addEventHandler(mockEventHandler, 0, 1, 1, mockSolver);
        assertEquals(1, integrator.getEventHandlers().size());
        verify(mockEventHandler).init(mockSolver);
    }

    @Test
    public void testClearEventHandlers() {
        EventHandler mockEventHandler1 = mock(EventHandler.class);
        EventHandler mockEventHandler2 = mock(EventHandler.class);
        integrator.addEventHandler(mockEventHandler1, 0, 1, 1, null);
        integrator.addEventHandler(mockEventHandler2, 0, 1, 1, null);
        integrator.clearEventHandlers();
        assertEquals(0, integrator.getEventHandlers().size());
    }

    @Test
    public void testGetEventHandlers() {
        EventHandler mockEventHandler1 = mock(EventHandler.class);
        EventHandler mockEventHandler2 = mock(EventHandler.class);
        integrator.addEventHandler(mockEventHandler1, 0, 1, 1, null);
        integrator.addEventHandler(mockEventHandler2, 0, 1, 1, null);
        List<EventHandler> handlers = integrator.getEventHandlers();
        assertEquals(2, handlers.size());
        assertArrayEquals(new Object[]{mockEventHandler1, mockEventHandler2}, handlers.toArray());
    }

    @Test
    public void testGetCurrentStepStart() {
        double currentStepStart = integrator.getCurrentStepStart();
        assertEquals(Double.NaN, currentStepStart, 0);
    }

    @Test
    public void testGetCurrentSignedStepsize() {
        double currentSignedStepsize = integrator.getCurrentSignedStepsize();
        assertEquals(Double.NaN, currentSignedStepsize, 0);
    }

    @Test
    public void testSetMaxEvaluations() {
        integrator.setMaxEvaluations(-1);
        assertEquals(-1, integrator.getMaxEvaluations());
        integrator.setMaxEvaluations(10);
        assertEquals(10, integrator.getMaxEvaluations());
    }

    @Test
    public void testResetEvaluations() {
        integrator.setMaxEvaluations(1);
        integrator.integrate(any(ExpandableStatefulODE.class), anyDouble(), anyArray(), anyDouble(), anyArray());
        integrator.resetEvaluations();
        assertEquals(0, integrator.getEvaluations());
    }

    @Test
    public void testAcceptStep() throws MathIllegalStateException {
        ExpandableStatefulODE equations = new ExpandableStatefulODE(new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yDot) {
                yDot[0] = y[0] + 1;
            }
        });
        integrator.setEquations(equations);
        integrator.setMaxEvaluations(1);
        AbstractStepInterpolator interpolator = new AbstractStepInterpolator() {
            @Override
            public double getGlobalPreviousTime() {
                return 0;
            }

            @Override
            public double getGlobalCurrentTime() {
                return 0.1;
            }

            @Override
            public double getInterpolatedTime() {
                return 0.1;
            }

            @Override
            public double[] getInterpolatedState() {
                return new double[]{0.1};
            }
        };
        double[] y = new double[]{0};
        double[] yDot = new double[]{0};
        double tEnd = 0.2;
        integrator.acceptStep(interpolator, y, yDot, tEnd);
        verify(equations).setTime(tEnd);
        verify(equations).setPrimaryState(new double[]{0.1});
        verify(equations, times(1)).computeDerivatives(anyDouble(), anyArray(), anyArray());
        assertEquals(1, integrator.getEvaluations());
    }

    @Test(expected = MathIllegalStateException.class)
    public void testAcceptStepWithResetOccurred() throws MathIllegalStateException {
        ExpandableStatefulODE equations = new ExpandableStatefulODE(new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yDot) {
                yDot[0] = y[0] + 1;
            }
        });
        integrator.setEquations(equations);
        integrator.setMaxEvaluations(1);
        AbstractStepInterpolator interpolator = new AbstractStepInterpolator() {
            @Override
            public double getGlobalPreviousTime() {
                return 0;
            }

            @Override
            public double getGlobalCurrentTime() {
                return 0.1;
            }

            @Override
            public double getInterpolatedTime() {
                return 0.1;
            }

            @Override
            public double[] getInterpolatedState() {
                return new double[]{0.1};
            }
        };
        double[] y = new double[]{0};
        double[] yDot = new double[]{0};
        double tEnd = 0.2;
        integrator.acceptStep(interpolator, y, yDot, tEnd);
        integrator.setStateInitialized(false);
        integrator.acceptStep(interpolator, y, yDot, tEnd);
        verify(equations).setTime(tEnd);
        verify(equations, times(2)).setPrimaryState(anyArray());
        verify(equations, times(2)).computeDerivatives(anyDouble(), anyArray(), anyArray());
        assertTrue(integrator.isLastStep());
        assertFalse(integrator.resetOccurred);

        integrator.acceptStep(interpolator, y, yDot, tEnd);
        integrator.setStateInitialized(false);
        integrator.acceptStep(interpolator, y, yDot, tEnd);
        verify(equations).setTime(tEnd);
        verify(equations, times(3)).setPrimaryState(anyArray());
        verify(equations, times(3)).computeDerivatives(anyDouble(), anyArray(), anyArray());
        assertTrue(integrator.isLastStep());
        assertTrue(integrator.resetOccurred);
    }

}