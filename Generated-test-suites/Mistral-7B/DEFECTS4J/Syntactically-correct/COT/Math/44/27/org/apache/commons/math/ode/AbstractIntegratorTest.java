import org.apache.commons.math3.ode.*;
import org.apache.commons.math3.ode.events.EventHandler;
import org.apache.commons.math3.ode.events.EventState;
import org.apache.commons.math3.ode.step.AbstractStepInterpolator;
import org.junit.Test;
import static org.junit.Assert.*;

public class AbstractIntegratorTest {

    @Test
    public void testAddStepHandler() {
        AbstractIntegrator integrator = new AbstractIntegrator() {
            @Override
            protected void integrate(ExpandableStatefulODE equations, double t)
                    throws MathIllegalStateException, MathIllegalArgumentException {
                // no implementation needed
            }
        };
        StepHandler stepHandler = mock(StepHandler.class);
        integrator.addStepHandler(stepHandler);
        assertTrue(integrator.getStepHandlers().contains(stepHandler));
    }

    @Test
    public void testClearStepHandlers() {
        AbstractIntegrator integrator = new AbstractIntegrator() {
            @Override
            protected void integrate(ExpandableStatefulODE equations, double t)
                    throws MathIllegalStateException, MathIllegalArgumentException {
                // no implementation needed
            }
        };
        StepHandler stepHandler1 = mock(StepHandler.class);
        integrator.addStepHandler(stepHandler1);
        StepHandler stepHandler2 = mock(StepHandler.class);
        integrator.addStepHandler(stepHandler2);
        integrator.clearStepHandlers();
        assertTrue(integrator.getStepHandlers().isEmpty());
    }

    @Test
    public void testAddEventHandler() {
        AbstractIntegrator integrator = new AbstractIntegrator() {
            @Override
            protected void integrate(ExpandableStatefulODE equations, double t)
                    throws MathIllegalStateException, MathIllegalArgumentException {
                // no implementation needed
            }
        };
        EventHandler eventHandler = mock(EventHandler.class);
        integrator.addEventHandler(eventHandler, 0.0, 0.01, 10);
        assertTrue(integrator.getEventHandlers().contains(eventHandler));
    }

    @Test
    public void testClearEventHandlers() {
        AbstractIntegrator integrator = new AbstractIntegrator() {
            @Override
            protected void integrate(ExpandableStatefulODE equations, double t)
                    throws MathIllegalStateException, MathIllegalArgumentException {
                // no implementation needed
            }
        };
        EventHandler eventHandler1 = mock(EventHandler.class);
        integrator.addEventHandler(eventHandler1, 0.0, 0.01, 10);
        EventHandler eventHandler2 = mock(EventHandler.class);
        integrator.addEventHandler(eventHandler2, 0.0, 0.01, 10);
        integrator.clearEventHandlers();
        assertTrue(integrator.getEventHandlers().isEmpty());
    }

    @Test
    public void testIntegrateWithNoEventHandlerOrStepHandler() {
        AbstractIntegrator integrator = new AbstractIntegrator() {
            @Override
            protected void integrate(ExpandableStatefulODE equations, double t)
                    throws MathIllegalStateException, MathIllegalArgumentException {
                // no implementation needed
            }
        };

        // Given
        FirstOrderDifferentialEquations equations = mock(FirstOrderDifferentialEquations.class);
        when(equations.getDimension()).thenReturn(1);
        double t0 = 0.0;
        double[] y0 = {0.0};
        double t = 1.0;
        double[] y = {1.0};

        // When
        try {
            integrator.integrate(equations, t0, y0, t, y);
            fail("Should have thrown MathIllegalStateException");
        } catch (MathIllegalStateException e) {
            // Then
            assertTrue(e.getMessage().contains("No StepHandlers or EventHandlers have been set"));
        }
    }

    @Test
    public void testIntegrateWithEventHandler() {
        AbstractIntegrator integrator = new AbstractIntegrator() {
            @Override
            protected void integrate(ExpandableStatefulODE equations, double t)
                    throws MathIllegalStateException, MathIllegalArgumentException {
                // no implementation needed
            }
        };

        // Given
        FirstOrderDifferentialEquations equations = mock(FirstOrderDifferentialEquations.class);
        when(equations.getDimension()).thenReturn(1);
        double t0 = 0.0;
        double[] y0 = {0.0};
        double t = 1.0;
        double[] y = {1.0};
        EventHandler eventHandler = mock(EventHandler.class);
        integrator.addEventHandler(eventHandler, 0.0, 0.01, 10);

        // When
        try {
            integrator.integrate(equations, t0, y0, t, y);
            fail("Should have thrown MathIllegalStateException");
        } catch (MathIllegalStateException e) {
            // Then
            assertTrue(e.getMessage().contains("No StepHandlers have been set"));
        }

        // Given
        StepHandler stepHandler = mock(StepHandler.class);
        integrator.addStepHandler(stepHandler);

        // When
        try {
            integrator.integrate(equations, t0, y0, t, y);
            fail("Should have thrown MathIllegalStateException");
        } catch (MathIllegalStateException e) {
            // Then
            assertTrue(e.getMessage().contains("No EventHandlers have been set"));
        }

        // Given
        EventState eventState = mock(EventState.class);
        when(eventHandler.getEvent()).thenReturn(eventState);

        // When
        integrator.addEventHandler(eventHandler, 0.5, 0.01, 10);
        integrator.setStepSize(0.1);
        integrator.integrate(equations, t0, y0, t, y);

        // Then
        verify(eventHandler).handleEvent(eq(eventState), anyDouble(), anyDouble(), anyDouble(), anyDouble());
        verify(stepHandler).handleStep(any(AbstractStepInterpolator.class), eq(false));
    }

    @Test
    public void testIntegrateWithStepHandler() {
        AbstractIntegrator integrator = new AbstractIntegrator() {
            @Override
            protected void integrate(ExpandableStatefulODE equations, double t)
                    throws MathIllegalStateException, MathIllegalArgumentException {
                // no implementation needed
            }
        };

        // Given
        FirstOrderDifferentialEquations equations = mock(FirstOrderDifferentialEquations.class);
        when(equations.getDimension()).thenReturn(1);
        double t0 = 0.0;
        double[] y0 = {0.0};
        double t = 1.0;
        double[] y = {1.0};
        StepHandler stepHandler = mock(StepHandler.class);
        integrator.addStepHandler(stepHandler);

        // When
        try {
            integrator.integrate(equations, t0, y0, t, y);
            fail("Should have thrown MathIllegalStateException");
        } catch (MathIllegalStateException e) {
            // Then
            assertTrue(e.getMessage().contains("No EventHandlers have been set"));
        }

        // Given
        EventHandler eventHandler = mock(EventHandler.class);
        integrator.addEventHandler(eventHandler, 0.5, 0.01, 10);

        // When
        try {
            integrator.integrate(equations, t0, y0, t, y);
            fail("Should have thrown MathIllegalStateException");
        } catch (MathIllegalStateException e) {
            // Then
            assertTrue(e.getMessage().contains("No StepHandlers have been set"));
        }

        // Given
        EventState eventState = mock(EventState.class);
        when(eventHandler.getEvent()).thenReturn(eventState);

        // When
        integrator.addEventHandler(eventHandler, 0.5, 0.01, 10);
        integrator.setStepSize(0.1);
        integrator.integrate(equations, t0, y0, t, y);

        // Then
        verify(stepHandler).handleStep(any(AbstractStepInterpolator.class), eq(false));
    }

    @Test
    public void testIntegrateWithEventHandlerAndStepHandler() {
        AbstractIntegrator integrator = new AbstractIntegrator() {
            @Override
            protected void integrate(ExpandableStatefulODE equations, double t)
                    throws MathIllegalStateException, MathIllegalArgumentException {
                // no implementation needed
            }
        };

        // Given
        FirstOrderDifferentialEquations equations = mock(FirstOrderDifferentialEquations.class);
        when(equations.getDimension()).thenReturn(1);
        double t0 = 0.0;
        double[] y0 = {0.0};
        double t = 1.0;
        double[] y = {1.0};
        StepHandler stepHandler = mock(StepHandler.class);
        integrator.addStepHandler(stepHandler);
        EventHandler eventHandler = mock(EventHandler.class);
        integrator.addEventHandler(eventHandler, 0.5, 0.01, 10);

        // When
        integrator.integrate(equations, t0, y0, t, y);

        // Then
        verify(stepHandler).handleStep(any(AbstractStepInterpolator.class), eq(false));
        verify(eventHandler).handleEvent(eq(any(EventState.class)), anyDouble(), anyDouble(), anyDouble(), anyDouble());
    }

    @Test
    public void testSetMaxEvaluations() {
        AbstractIntegrator integrator = new AbstractIntegrator() {
            @Override
            protected void integrate(ExpandableStatefulODE equations, double t)
                    throws MathIllegalStateException, MathIllegalArgumentException {
                // no implementation needed
            }
        };

        // When
        integrator.setMaxEvaluations(10);

        // Then
        assertEquals(10, integrator.getMaxEvaluations());
        assertEquals(10, integrator.getEvaluations());
    }

    @Test
    public void testResetEvaluations() {
        AbstractIntegrator integrator = new AbstractIntegrator() {
            @Override
            protected void integrate(ExpandableStatefulODE equations, double t)
                    throws MathIllegalStateException, MathIllegalArgumentException {
                // no implementation needed
            }
        };

        // When
        integrator.setMaxEvaluations(10);
        integrator.integrate(mock(ExpandableStatefulODE.class), 0.0, new double[0], 1.0, new double[0]);
        integrator.resetEvaluations();

        // Then
        assertEquals(0, integrator.getEvaluations());
    }

    @Test
    public void testSanityChecksWithValidIntegrationSpan() {
        AbstractIntegrator integrator = new AbstractIntegrator() {
            @Override
            protected void integrate(ExpandableStatefulODE equations, double t)
                    throws MathIllegalStateException, MathIllegalArgumentException {
                // no implementation needed
            }
        };

        // Given
        FirstOrderDifferentialEquations equations = mock(FirstOrderDifferentialEquations.class);
        when(equations.getDimension()).thenReturn(1);
        double t0 = 0.0;
        double[] y0 = {0.0};
        double t = 0.1;

        // When
        integrator.setEquations(equations);
        integrator.integrate(equations, t0, y0, t, y0);

        // Then
        // No exception should be thrown
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testSanityChecksWithInvalidIntegrationSpan() {
        AbstractIntegrator integrator = new AbstractIntegrator() {
            @Override
            protected void integrate(ExpandableStatefulODE equations, double t)
                    throws MathIllegalStateException, MathIllegalArgumentException {
                // no implementation needed
            }
        };

        // Given
        FirstOrderDifferentialEquations equations = mock(FirstOrderDifferentialEquations.class);
        when(equations.getDimension()).thenReturn(1);
        double t0 = 0.0;
        double[] y0 = {0.0};
        double t = 1.0e-10; // Integration span is too small

        // When
        integrator.setEquations(equations);

        // Then
        integrator.sanityChecks(equations, t);
    }
}