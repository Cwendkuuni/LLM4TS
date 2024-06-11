package org.apache.commons.math3.ode;

import static org.junit.Assert.*;

import org.apache.commons.math3.ode.ExpandableStatefulODE;
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.step.AbstractStepInterpolator;
import org.apache.commons.math3.ode.step.RungeKuttaFehlberg78StepInterpolator;
import org.apache.commons.math3.ode.util.ODETestUtils;
import org.apache.commons.math3.util.Precision;
import org.junit.Before;
import org.junit.Test;

public class AbstractIntegratorTest {

    private AbstractIntegrator integrator;
    private FirstOrderDifferentialEquations equations;
    private ExpandableStatefulODE expandableEquations;
    private AbstractStepInterpolator stepInterpolator;

    @Before
    public void setUp() {
        integrator = new AbstractIntegrator() {
            @Override
            public void integrate(ExpandableStatefulODE equations, double t)
                    throws NumberIsTooSmallException, DimensionMismatchException,
                           MaxCountExceededException, NoBracketingException {
                // do nothing
            }
        };
        equations = new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return 2;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yDot) {
                yDot[0] = y[1];
                yDot[1] = -y[0];
            }
        };
        expandableEquations = new ExpandableStatefulODE(equations);
        stepInterpolator = new RungeKuttaFehlberg78StepInterpolator();
    }

    @Test
    public void testInitializeIntegration() {
        integrator.initIntegration(0.0, new double[] { 1.0, 0.5 }, 1.0);
        assertFalse(integrator.isLastStep());
        assertNull(integrator.getEvents());
        assertEquals(0, integrator.getEvaluations());
        assertEquals(0.0, integrator.getCurrentStepStart(), 1e-12);
        assertEquals(Double.NaN, integrator.getCurrentSignedStepsize(), 1e-12);
    }

    @Test
    public void testIntegration() throws Exception {
        integrator.setEquations(expandableEquations);
        integrator.setMaxEvaluations(50);

        double tEnd = 3.0;
        double[] y0 = { 1.0, 0.5 };
        double[] y = new double[2];
        double t = integrator.integrate(equations, 0.0, y0, tEnd, y);

        assertEquals(tEnd, t, 1e-12);
        assertArrayEquals(new double[] { 1.543080634820353, 0.866025383658233 }, y, 1e-12);
        assertEquals(50, integrator.getEvaluations());
    }

    @Test(expected = DimensionMismatchException.class)
    public void testIntegrationWithMismatchingDimensions() throws Exception {
        integrator.setEquations(expandableEquations);
        integrator.setMaxEvaluations(50);

        double tEnd = 3.0;
        double[] y0 = { 1.0, 0.5, 2.5 };
        double[] y = new double[1];
        integrator.integrate(equations, 0.0, y0, tEnd, y);
    }

    @Test(expected = MaxCountExceededException.class)
    public void testMaxEvaluationsExceeded() throws Exception {
        integrator.setEquations(expandableEquations);
        integrator.setMaxEvaluations(1);

        double tEnd = 3.0;
        double[] y0 = { 1.0, 0.5 };
        double[] y = new double[2];
        integrator.integrate(equations, 0.0, y0, tEnd, y);
    }

    @Test(expected = NoBracketingException.class)
    public void testNoBracketingException() throws Exception {
        integrator.setEquations(expandableEquations);
        integrator.setMaxEvaluations(50);

        double[] y0 = { 1.0, 0.5 };
        double tEnd = 2.0;

        // Integrate from 2.0 to 3.0
        try {
            integrator.integrate(equations, 2.0, y0, tEnd, new double[2]);
        } catch (NoBracketingException e) {
            // expected
        }
    }

    @Test
    public void testAcceptStep() throws Exception {
        integrator.setMaxEvaluations(5);

        double stepSize = 0.1;
        AbstractStepInterpolator stepInterpolator = new RungeKuttaFehlberg78StepInterpolator();
        double[] y = new double[2];
        double[] yDot = new double[2];

        integrator.integrate(expandableEquations, 0.0, y, 1.0, y);
        double t = integrator.getCurrentStepStart();
        integrator.acceptStep(stepInterpolator, y, yDot, 1.0);

        assertEquals(1.0, integrator.getCurrentStepStart(), 1e-12);
        assertTrue(integrator.isLastStep());
        assertFalse(integrator.resetOccurred());
        assertEquals(1, integrator.getEvaluations());

        integrator.reset();
        integrator.integrate(expandableEquations, 0.0, y, 1.0, y);
        t = integrator.getCurrentStepStart();
        integrator.acceptStep(stepInterpolator, y, yDot, 1.0);

        assertEquals(1.0, integrator.getCurrentStepStart(), 1e-12);
        assertFalse(integrator.isLastStep());
        assertFalse(integrator.resetOccurred());
        assertEquals(2, integrator.getEvaluations());

        integrator.reset();
        integrator.addEventHandler(ODETestUtils.createTestEventHandler(1.5), 0.1, 0.01, 5);
        integrator.integrate(expandableEquations, 0.0, y, 1.0, y);
        t = integrator.getCurrentStepStart();
        integrator.acceptStep(stepInterpolator, y, yDot, 1.0);

        assertEquals(1.0, integrator.getCurrentStepStart(), 1e-12);
        assertFalse(integrator.isLastStep());
        assertTrue(integrator.resetOccurred());
        assertEquals(3, integrator.getEvaluations());
    }

    @Test
    public void testAcceptStepWithEventHandlers() throws Exception {
        integrator.setMaxEvaluations(5);

        double stepSize = 0.1;
        AbstractStepInterpolator stepInterpolator = new RungeKuttaFehlberg78StepInterpolator();
        double[] y = new double[2];
        double[] yDot = new double[2];

        integrator.integrate(expandableEquations, 0.0, y, 1.0, y);
        double t = integrator.getCurrentStepStart();
        integrator.acceptStep(stepInterpolator, y, yDot, 1.0);

        assertEquals(1.0, integrator.getCurrentStepStart(), 1e-12);
        assertTrue(integrator.isLastStep());
        assertFalse(integrator.resetOccurred());
        assertEquals(1, integrator.getEvaluations());

        integrator.reset();
        integrator.addEventHandler(ODETestUtils.createTestEventHandler(1.5), 0.1, 0.01, 5);
        integrator.integrate(expandableEquations, 0.0, y, 1.0, y);
        t = integrator.getCurrentStepStart();
        integrator.acceptStep(stepInterpolator, y, yDot, 1.0);

        assertEquals(1.0, integrator.getCurrentStepStart(), 1e-12);
        assertFalse(integrator.isLastStep());
        assertTrue(integrator.resetOccurred);
        assertEquals(3, integrator.getEvaluations());

        integrator.reset();
        integrator.addEventHandler(ODETestUtils.createTestEventHandler(1.5), 0.1, 0.01, 5);
        integrator.integrate(expandableEquations, 0.0, y, 2.0, y);
        t = integrator.getCurrentStepStart();
        integrator.acceptStep(stepInterpolator, y, yDot, 2.0);

        assertEquals(2.0, integrator.getCurrentStepStart(), 1e-12);
        assertFalse(integrator.isLastStep());
        assertFalse(integrator.resetOccurred);
        assertEquals(5, integrator.getEvaluations());
    }

}