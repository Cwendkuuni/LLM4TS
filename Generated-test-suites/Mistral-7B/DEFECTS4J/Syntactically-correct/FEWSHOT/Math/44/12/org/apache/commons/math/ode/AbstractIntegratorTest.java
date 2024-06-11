package org.apache.commons.math.ode;

import static org.junit.Assert.*;

import org.apache.commons.math3.analysis.solvers.BracketingNthOrderBrentSolver;
import org.apache.commons.math3.analysis.solvers.UnivariateRealSolver;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.ode.AbstractStepInterpolator;
import org.apache.commons.math3.ode.ExpandableStatefulODE;
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.events.AbstractEventHandler;
import org.apache.commons.math3.ode.events.EventHandler;
import org.apache.commons.math3.ode.events.EventState;
import org.apache.commons.math3.ode.sampling.StepHandler;
import org.apache.commons.math3.util.FastMath;
import org.apache.commons.math3.util.Incrementor;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.SortedSet;
import org.apache.commons.math3.util.TreeSet;

public class AbstractIntegratorTest {

    @Test
    public void testIntegratorName() {
        AbstractIntegrator integrator = new AbstractIntegrator("RK4Integrator") {};
        assertEquals("RK4Integrator", integrator.getName());
    }

    @Test
    public void testAddStepHandlers() {
        AbstractIntegrator integrator = new AbstractIntegrator("TestIntegrator") {};
        StepHandler stepHandler = mock(StepHandler.class);
        integrator.addStepHandler(stepHandler);
        assertEquals(1, integrator.getStepHandlers().size());
        integrator.clearStepHandlers();
        assertEquals(0, integrator.getStepHandlers().size());
    }

    @Test
    public void testAddEventHandler() {
        AbstractIntegrator integrator = new AbstractIntegrator("TestIntegrator") {};
        EventHandler eventHandler = mock(EventHandler.class);
        integrator.addEventHandler(eventHandler, 0.1, 1e-6, 10);
        assertEquals(1, integrator.getEventHandlers().size());
        integrator.clearEventHandlers();
        assertEquals(0, integrator.getEventHandlers().size());
    }

    @Test
    public void testGetCurrentStepStart() {
        AbstractIntegrator integrator = new AbstractIntegrator("TestIntegrator") {};
        assertEquals(Double.NaN, integrator.getCurrentStepStart(), 0);
    }

    @Test
    public void testGetCurrentSignedStepsize() {
        AbstractIntegrator integrator = new AbstractIntegrator("TestIntegrator") {};
        assertEquals(Double.NaN, integrator.getCurrentSignedStepsize(), 0);
    }

    @Test
    public void testSetMaxEvaluations() {
        AbstractIntegrator integrator = new AbstractIntegrator("TestIntegrator") {};
        integrator.setMaxEvaluations(100);
        assertEquals(100, integrator.getMaxEvaluations());
        integrator.setMaxEvaluations(-1);
        assertEquals(-1, integrator.getMaxEvaluations());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetNegativeMaxEvaluations() {
        AbstractIntegrator integrator = new AbstractIntegrator("TestIntegrator") {};
        integrator.setMaxEvaluations(-1);
    }

    @Test
    public void testGetEvaluations() {
        AbstractIntegrator integrator = new AbstractIntegrator("TestIntegrator") {};
        integrator.resetEvaluations();
        assertEquals(0, integrator.getEvaluations());
    }

    @Test
    public void testIntegrate() {
        //TODO: Implement testIntegrate method
    }

    @Test
    public void testAcceptStep() throws MathIllegalStateException, MathIllegalArgumentException, MaxCountExceededException, NumberIsTooSmallException {
        //TODO: Implement testAcceptStep method
    }

    @Test
    public void testSetEquations() {
        AbstractIntegrator integrator = new AbstractIntegrator("TestIntegrator") {};
        ExpandableStatefulODE equations = mock(ExpandableStatefulODE.class);
        integrator.setEquations(equations);
        assertEquals(equations, integrator.getEquations());
    }

    @Test(expected = IllegalStateException.class)
    public void testIntegrateWithoutEquations() {
        AbstractIntegrator integrator = new AbstractIntegrator("TestIntegrator") {};
        integrator.integrate(null, 0);
    }

    @Test
    public void testComputeDerivatives() throws MaxCountExceededException {
        //TODO: Implement testComputeDerivatives method
    }

    @Test
    public void testSetStateInitialized() {
        AbstractIntegrator integrator = new AbstractIntegrator("TestIntegrator") {};
        integrator.setStateInitialized(true);
        assertTrue(integrator.isStateInitialized());
        integrator.setStateInitialized(false);
        assertFalse(integrator.isStateInitialized());
    }

    @Test
    public void testAcceptStepWithEvents() throws MathIllegalStateException, MathIllegalArgumentException, MaxCountExceededException, NumberIsTooSmallException {
        //TODO: Implement testAcceptStepWithEvents method
    }

    @Test
    public void testSanityChecks() throws NumberIsTooSmallException {
        //TODO: Implement testSanityChecks method
    }

    @Test(expected = DimensionMismatchException.class)
    public void testIntegrateWithMismatchedDimensions() {
        ExpandableStatefulODE equations = mock(ExpandableStatefulODE.class);
        when(equations.getDimension()).thenReturn(3);

        AbstractIntegrator integrator = new AbstractIntegrator("TestIntegrator") {};
        integrator.setEquations(equations);

        double[] y0 = new double[2];
        double[] y = new double[2];
        double t = 0;

        try {
            integrator.integrate(equations, t, y0, t, y);
            fail();
        } catch (MathIllegalArgumentException e) {
            // expected
        }
    }

}