package org.apache.commons.math.ode;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math.analysis.solvers.BracketingNthOrderBrentSolver;
import org.apache.commons.math.analysis.solvers.UnivariateRealSolver;
import org.apache.commons.math.exception.DimensionMismatchException;
import org.apache.commons.math.exception.MathIllegalArgumentException;
import org.apache.commons.math.exception.MathIllegalStateException;
import org.apache.commons.math.exception.MaxCountExceededException;
import org.apache.commons.math.exception.NumberIsTooSmallException;
import org.apache.commons.math.ode.AbstractIntegrator;
import org.apache.commons.math.ode.ExpandableStatefulODE;
import org.apache.commons.math.ode.events.EventHandler;
import org.apache.commons.math.ode.events.EventState;
import org.apache.commons.math.ode.sampling.AbstractStepInterpolator;
import org.apache.commons.math.ode.sampling.StepHandler;
import org.apache.commons.math.util.FastMath;
import org.apache.commons.math.util.Incrementor;
import org.junit.Test;

/**
 * Test cases for {@link AbstractIntegrator} class.
 */
public class AbstractIntegratorTest {

    private static final double EPSILON = 1e-10;

    /**
     * Test of {@link AbstractIntegrator#AbstractIntegrator(String)} constructor.
     */
    @Test
    public void testAbstractIntegrator() {
        final AbstractIntegrator integrator = new AbstractIntegrator();
        assertEquals("", integrator.getName());
        assertNull(integrator.getStepHandlers());
        assertNull(integrator.getCurrentStepStart());
        assertNull(integrator.getCurrentSignedStepsize());
        assertFalse(integrator.isLastStep());
        assertFalse(integrator.resetOccurred);
        assertNull(integrator.getEventHandlers());
        assertEquals(-1, integrator.getMaxEvaluations());
        assertEquals(0, integrator.getEvaluations());
        assertNull(integrator.getEquations());
    }

    /**
     * Test of {@link AbstractIntegrator#AbstractIntegrator(String, String)} constructor.
     */
    @Test
    public void testAbstractIntegratorString() {
        final AbstractIntegrator integrator = new AbstractIntegrator("Name");
        assertEquals("Name", integrator.getName());
        assertNull(integrator.getStepHandlers());
        assertNull(integrator.getCurrentStepStart());
        assertNull(integrator.getCurrentSignedStepsize());
        assertFalse(integrator.isLastStep());
        assertFalse(integrator.resetOccurred);
        assertNull(integrator.getEventHandlers());
        assertEquals(-1, integrator.getMaxEvaluations());
        assertEquals(0, integrator.getEvaluations());
        assertNull(integrator.getEquations());
    }

    /**
     * Test of {@link AbstractIntegrator#addStepHandler(StepHandler)} method.
     */
    @Test
    public void testAddStepHandler() {
        final AbstractIntegrator integrator = new AbstractIntegrator();
        integrator.addStepHandler(new StepHandler() {
            @Override
            public void handleStep(StepHandler handler, boolean lastStep) {
                // no-op
            }
        });
        assertEquals(1, integrator.getStepHandlers().size());
        integrator.clearStepHandlers();
        assertEquals(0, integrator.getStepHandlers().size());
    }

    /**
     * Test of {@link AbstractIntegrator#getCurrentStepStart()} method.
     */
    @Test
    public void testgetCurrentStepStart() {
        final AbstractIntegrator integrator = new AbstractIntegrator();
        assertNull(integrator.getCurrentStepStart());
        integrator.setCurrentStepStart(1.0);
        assertEquals(1.0, integrator.getCurrentStepStart(), EPSILON);
    }

    /**
     * Test of {@link AbstractIntegrator#getCurrentSignedStepsize()} method.
     */
    @Test
    public void testgetCurrentSignedStepsize() {
        final AbstractIntegrator integrator = new AbstractIntegrator();
        assertNull(integrator.getCurrentSignedStepsize());
        integrator.setCurrentSignedStepsize(1.0);
        assertEquals(1.0, integrator.getCurrentSignedStepsize(), EPSILON);
    }

    /**
     * Test of {@link AbstractIntegrator#isLastStep()} method.
     */
    @Test
    public void testisLastStep() {
        final AbstractIntegrator integrator = new AbstractIntegrator();
        assertFalse(integrator.isLastStep());
        integrator.setLastStep(true);
        assertTrue(integrator.isLastStep());
    }

    /**
     * Test of {@link AbstractIntegrator#resetOccurred} property.
     */
    @Test
    public void testResetOccurred() {
        final AbstractIntegrator integrator = new AbstractIntegrator();
        assertFalse(integrator.resetOccurred);
        integrator.setResetOccurred(true);
        assertTrue(integrator.resetOccurred);
    }

    /**
     * Test of {@link AbstractIntegrator#getEventHandlers()} method.
     */
    @Test
    public void testGetEventHandlers() {
        final AbstractIntegrator integrator = new AbstractIntegrator();
        assertEquals(0, integrator.getEventHandlers().size());
        integrator.addEventHandler(new EventHandler() {
            @Override
            public boolean evaluateStep(AbstractStepInterpolator interpolator) {
                return false;
            }

            @Override
            public void stepAccepted(double t, double[] y) {
                // no-op
            }

            @Override
            public void reinitializeBegin(AbstractStepInterpolator interpolator) {
                // no-op
            }
        }, 0.0, 1.0, 1, new BracketingNthOrderBrentSolver(1.0, 5));
        assertEquals(1, integrator.getEventHandlers().size());
        integrator.clearEventHandlers();
        assertEquals(0, integrator.getEventHandlers().size());
    }

    /**
     * Test of {@link AbstractIntegrator#getMaxEvaluations()} method.
     */
    @Test
    public void testGetMaxEvaluations() {
        final AbstractIntegrator integrator = new AbstractIntegrator();
        assertEquals(-1, integrator.getMaxEvaluations());
        integrator.setMaxEvaluations(5);
        assertEquals(5, integrator.getMaxEvaluations());
    }

    /**
     * Test of {@link AbstractIntegrator#getEvaluations()} method.
     */
    @Test
    public void testGetEvaluations() {
        final AbstractIntegrator integrator = new AbstractIntegrator();
        assertEquals(0, integrator.getEvaluations());
        integrator.computeDerivatives(1.0, new double[0], new double[0]);
        assertEquals(1, integrator.getEvaluations());
    }

    /**
     * Test of {@link AbstractIntegrator#setEquations(ExpandableStatefulODE)} method.
     */
    @Test
    public void testSetEquations() {
        final AbstractIntegrator integrator = new AbstractIntegrator();
        final ExpandableStatefulODE equations = new ExpandableStatefulODE(() -> 0.0);
        integrator.setEquations(equations);
        assertEquals(equations, integrator.getEquations());
    }

    /**
     * Test of {@link AbstractIntegrator#integrate(ExpandableStatefulODE, double)} method.
     */
    @Test
    public void testIntegrate() throws MathIllegalStateException, MathIllegalArgumentException {
        final AbstractIntegrator integrator = new AbstractIntegrator();
        final ExpandableStatefulODE equations = new ExpandableStatefulODE(() -> 0.0);
        integrator.setEquations(equations);
        integrator.integrate(equations, 0.0, new double[0], 1.0, new double[0]);
        assertEquals(1.0, integrator.getCurrentStepStart(), EPSILON);
        assertEquals(1.0, integrator.getCurrentSignedStepsize(), EPSILON);
        assertArrayEquals(new double[0], integrator.getPrimaryState(), EPSILON);
    }

    /**
     * Test of {@link AbstractIntegrator#integrate(ExpandableStatefulODE, double)} method with negative step size.
     */
    @Test(expected = MathIllegalArgumentException.class)
    public void testIntegrateNegativeStepSize() throws MathIllegalStateException, MathIllegalArgumentException {
        final AbstractIntegrator integrator = new AbstractIntegrator();
        final ExpandableStatefulODE equations = new ExpandableStatefulODE(() -> 0.0);
        integrator.setEquations(equations);
        integrator.integrate(equations, 0.0, new double[0], -1.0, new double[0]);
    }

    /**
     * Test of {@link AbstractIntegrator#integrate(ExpandableStatefulODE, double)} method with illegal initial state.
     */
    @Test(expected = DimensionMismatchException.class)
    public void testIntegrateIllegalInitialState() throws MathIllegalStateException, MathIllegalArgumentException {
        final AbstractIntegrator integrator = new AbstractIntegrator();
        final ExpandableStatefulODE equations = new ExpandableStatefulODE(() -> 0.0);
        integrator.setEquations(equations);
        integrator.integrate(equations, 0.0, new double[1], 1.0, new double[2]);
    }

    /**
     * Test of {@link AbstractIntegrator#integrate(ExpandableStatefulODE, double)} method with illegal final state.
     */
    @Test(expected = DimensionMismatchException.class)
    public void testIntegrateIllegalFinalState() throws MathIllegalStateException, MathIllegalArgumentException {
        final AbstractIntegrator integrator = new AbstractIntegrator();
        final ExpandableStatefulODE equations = new ExpandableStatefulODE(() -> 0.0);
        integrator.setEquations(equations);
        integrator.integrate(equations, 0.0, new double[0], 1.0, new double[1]);
    }

    /**
     * Test of {@link AbstractIntegrator#integrate(ExpandableStatefulODE, double)} method with illegal integration span.
     */
    @Test(expected = NumberIsTooSmallException.class)
    public void testIntegrateIllegalIntegrationSpan() throws MathIllegalStateException, MathIllegalArgumentException {
        final AbstractIntegrator integrator = new AbstractIntegrator();
        final ExpandableStatefulODE equations = new ExpandableStatefulODE(() -> 0.0);
        integrator.setEquations(equations);
        integrator.integrate(equations, 0.0, new double[0], 0.0, new double[0]);
    }

    /**
     * Test of {@link AbstractIntegrator#computeDerivatives(double, double[], double[])} method.
     */
    @Test
    public void testComputeDerivatives() throws MaxCountExceededException {
        final AbstractIntegrator integrator = new AbstractIntegrator();
        final ExpandableStatefulODE equations = new ExpandableStatefulODE(() -> 0.0);
        integrator.setEquations(equations);
        integrator.setMaxEvaluations(1);
        integrator.computeDerivatives(1.0, new double[0], new double[0]);
        assertEquals(1, integrator.getEvaluations());
    }

    /**
     * Test of {@link AbstractIntegrator#setMaxEvaluations(int)} method.
     */
    @Test
    public void testSetMaxEvaluations() {
        final AbstractIntegrator integrator = new AbstractIntegrator();
        integrator.setMaxEvaluations(5);
        assertEquals(5, integrator.getMaxEvaluations());
        integrator.setMaxEvaluations(10);
        assertEquals(10, integrator.getMaxEvaluations());
    }

    /**
     * Test of {@link AbstractIntegrator#resetEvaluations()} method.
     */
    @Test
    public void testResetEvaluations() {
        final AbstractIntegrator integrator = new AbstractIntegrator();
        integrator.computeDerivatives(1.0, new double[0], new double[0]);
        integrator.computeDerivatives(1.0, new double[0], new double[0]);
        integrator.resetEvaluations();
        assertEquals(0, integrator.getEvaluations());
    }

}