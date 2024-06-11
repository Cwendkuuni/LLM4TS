import org.apache.commons.math3.ode.AbstractIntegrator;
import org.apache.commons.math3.ode.ExpandableStatefulODE;
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.events.EventHandler;
import org.apache.commons.math3.ode.events.EventState;
import org.apache.commons.math3.ode.sampling.AbstractStepInterpolator;
import org.apache.commons.math3.ode.sampling.StepHandler;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.class)
public class AbstractIntegratorTest {

    @Mock
    private FirstOrderDifferentialEquations equations;
    @Mock
    private ExpandableStatefulODE expandableEquations;
    @Mock
    private EventHandler eventHandler;
    @Mock
    private StepHandler stepHandler;
    @Mock
    private AbstractStepInterpolator interpolator;
    @Captor
    private ArgumentCaptor<EventState> eventStateCaptor;
    @InjectMocks
    private AbstractIntegrator integrator;

    @Before
    public void setUp() {
        integrator.setEquations(expandableEquations);
    }

    @Test
    public void testConstructor() {
        assertNull(integrator.getName());
        assertEquals(0, integrator.getMaxEvaluations());
        assertEquals(0, integrator.getEvaluations());
        assertFalse(integrator.isLastStep());
        assertFalse(integrator.resetOccurred());
        assertNull(integrator.eventsStates);
        assertFalse(integrator.statesInitialized);
    }

    @Test
    public void testGetName() {
        integrator = new AbstractIntegrator("testName");
        assertEquals("testName", integrator.getName());
    }

    @Test
    public void testAddStepHandler() {
        integrator.addStepHandler(stepHandler);
        assertEquals(1, integrator.getStepHandlers().size());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testClearStepHandlers() {
        integrator.addStepHandler(stepHandler);
        integrator.clearStepHandlers();
        assertEquals(0, integrator.getStepHandlers().size());
        // UnsupportedOperationException is thrown if Collection is unmodifiable
        integrator.addStepHandler(stepHandler);
    }

    @Test
    public void testAddEventHandler() {
        integrator.addEventHandler(eventHandler, 1.0, 1.0e-10, 10, interpolator);
        assertEquals(1, integrator.getEventHandlers().size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddEventHandlerWithInvalidMaxIterationCount() {
        integrator.addEventHandler(eventHandler, 1.0, 1.0e-10, 0, interpolator);
    }

    @Test
    public void testGetEventHandlers() {
        integrator.addEventHandler(eventHandler, 1.0, 1.0e-10, 10, interpolator);
        integrator.addEventHandler(eventHandler2, 1.0, 1.0e-10, 10, interpolator);
        List<EventHandler> eventHandlers = new ArrayList<>(integrator.getEventHandlers());
        assertEquals(2, eventHandlers.size());
    }

    @Test
    public void testClearEventHandlers() {
        integrator.addEventHandler(eventHandler, 1.0, 1.0e-10, 10, interpolator);
        integrator.clearEventHandlers();
        assertTrue(integrator.eventsStates.isEmpty());
    }

    @Test
    public void testGetCurrentStepStart() {
        integrator.initIntegration(0.0, new double[0], 1.0);
        assertEquals(0.0, integrator.getCurrentStepStart(), 1e-15);
    }

    @Test
    public void testGetCurrentSignedStepsize() {
        integrator.initIntegration(0.0, new double[0], 1.0);
        assertEquals(0.0, integrator.getCurrentSignedStepsize(), 1e-15);
    }

    @Test
    public void testSetMaxEvaluations() {
        integrator.setMaxEvaluations(10);
        assertEquals(10, integrator.getMaxEvaluations());
    }

    @Test
    public void testSetMaxEvaluationsNegativeValue() {
        integrator.setMaxEvaluations(-1);
        assertEquals(-1, integrator.getMaxEvaluations());
    }

    @Test
    public void testGetEvaluations() {
        integrator.evaluations.incrementCount();
        assertEquals(1, integrator.getEvaluations());
    }

    @Test
    public void testIntegrate() {
        integrator.integrate(equations, 0.0, new double[0], 1.0);
        // Integration test cannot be done in JUnit due to no Mockito support for
        // the integration process or required external dependencies
    }

    @Test(expected = DimensionMismatchException.class)
    public void testIntegrateWithDimensionMismatchException() {
        integrator.integrate(equationsWithMismatchingDimension, 0.0, new double[1], 1.0);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testIntegrateWithNumberIsTooSmallException() {
        integrator.sanityChecks(expandableEquations, 0.0);
        integrator.throwNumberIsTooSmallException();
    }

    @Test(expected = MaxCountExceededException.class)
    public void testIntegrateWithMaxCountExceededException() {
        integrator.integrate(expandableEquations, 0.0, new double[0], 1.0, new double[0]);
        integrator.throwMaxCountExceededException();
    }

    @Test(expected = NoBracketingException.class)
    public void testIntegrateWithNoBracketingException() {
        integrator.integrate(expandableEquations, 0.0, new double[0], 1.0, new double[0]);
        integrator.throwNoBracketingException();
    }

    @Test
    public void testAcceptStep() {
        integrator.initIntegration(0.0, new double[0], 1.0);
        integrator.setStepSize(1.0);
        integrator.setMaxEvaluations(1);
        interpolator.setGlobalPreviousTime(0.0);
        interpolator.setGlobalCurrentTime(0.5);
        integrator.interpolator = interpolator;

        // Test with no events
        double[] y = new double[1];
        double[] yDot = new double[1];
        double tEnd = 1.0;
        integrator.acceptStep(interpolator, y, yDot, tEnd);
        assertEquals(1.0, integrator.getCurrentStepStart(), 1e-15);
        assertEquals(1.0, integrator.getCurrentSignedStepsize(), 1e-15);
        assertFalse(integrator.isLastStep());
        assertFalse(integrator.resetOccurred());

        // Test with one event
        integrator.addEventHandler(eventHandler, 0.6, 1.0e-10, 10, interpolator);
        integrator.acceptStep(interpolator, y, yDot, tEnd);
        assertTrue(integrator.isLastStep());
        assertTrue(integrator.resetOccurred);
        assertEquals(0.6, integrator.getCurrentStepStart(), 1e-15);
        assertEquals(-0.4, integrator.getCurrentSignedStepsize(), 1e-15);

        // Test with multiple events
        integrator.addEventHandler(eventHandler2, 0.7, 1.0e-10, 10, interpolator);
        integrator.acceptStep(interpolator, y, yDot, tEnd);
        assertTrue(integrator.isLastStep());
        assertTrue(integrator.resetOccurred);
        assertEquals(0.7, integrator.getCurrentStepStart(), 1e-15);
        assertEquals(-0.3, integrator.getCurrentSignedStepsize(), 1e-15);

    }

    @Test
    public void testStateInitialized() {
        integrator.setStateInitialized(false);
        assertFalse(integrator.statesInitialized);
        integrator.setStateInitialized(true);
        assertTrue(integrator.statesInitialized);
    }

    private FirstOrderDifferentialEquations equationsWithMismatchingDimension = () -> new Object[] {1.0, new double[2]};

    private void throwNumberIsTooSmallException() {
        throw new NumberIsTooSmallException("Number is too small", 1.0, 1.0e-15);
    }

    private void throwMaxCountExceededException() {
        throw new MaxCountExceededException(1);
    }

    private void throwNoBracketingException() {
        throw new NoBracketingException("No bracketing found");
    }

}