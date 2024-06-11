import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.math3.ode.nonstiff.EmbeddedRungeKuttaIntegrator;
import org.apache.commons.math3.ode.ExpandableStatefulODE;
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.nonstiff.RungeKuttaStepInterpolator;
import org.apache.commons.math3.exception.*;

public class EmbeddedRungeKuttaIntegratorTest {
    
    private static final double DEFAULT_ABSOLUTE_TOLERANCE = 1e-6;
    private static final double DEFAULT_RELATIVE_TOLERANCE = 1e-6;

    @Test
    public void testGetOrder() {
        EmbeddedRungeKuttaIntegrator integrator = new TestEmbeddedRungeKuttaIntegrator();
        int expectedOrder = 4;
        int actualOrder = integrator.getOrder();
        assertEquals(expectedOrder, actualOrder);
    }

    @Test
    public void testGetSafety() {
        EmbeddedRungeKuttaIntegrator integrator = new TestEmbeddedRungeKuttaIntegrator();
        double expectedSafety = 0.9;
        double actualSafety = integrator.getSafety();
        assertEquals(expectedSafety, actualSafety, 0.0);
    }

    @Test
    public void testSetSafety() {
        EmbeddedRungeKuttaIntegrator integrator = new TestEmbeddedRungeKuttaIntegrator();
        double newSafety = 0.8;
        integrator.setSafety(newSafety);
        double actualSafety = integrator.getSafety();
        assertEquals(newSafety, actualSafety, 0.0);
    }

    @Test
    public void testSetSafetyNegativeValue() {
        EmbeddedRungeKuttaIntegrator integrator = new TestEmbeddedRungeKuttaIntegrator();
        double newSafety = -0.1;
        integrator.setSafety(newSafety);
        double actualSafety = integrator.getSafety();
        assertEquals(0.9, actualSafety, 0.0); // Safety value should not change when providing negative input
    }

    @Test
    public void testIntegrate() throws DimensionMismatchException, MaxCountExceededException, NoBracketingException {
        EmbeddedRungeKuttaIntegrator integrator = new TestEmbeddedRungeKuttaIntegrator();
        TestEquations equations = new TestEquations();
        ExpandableStatefulODE expandableODE = new ExpandableStatefulODE(equations);
        double tStart = 0.0;
        double[] y0 = {0.0, 1.0}; // Initial state
        double tEnd = 1.0; // Time to integrate to
        integrator.integrate(expandableODE, tEnd);
        double[] yFinal = expandableODE.getCompleteState();
        double[] expectedY = equations.computeDerivatives(tEnd, y0); // Expected final state based on the equation
        for (int i = 0; i < yFinal.length; i++) {
            assertEquals(expectedY[i], yFinal[i], DEFAULT_ABSOLUTE_TOLERANCE);
        }
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testIntegrateSmallStepSize() throws DimensionMismatchException, MaxCountExceededException, NoBracketingException {
        EmbeddedRungeKuttaIntegrator integrator = new TestEmbeddedRungeKuttaIntegrator();
        TestEquations equations = new TestEquations();
        ExpandableStatefulODE expandableODE = new ExpandableStatefulODE(equations);
        double tStart = 0.0;
        double[] y0 = {0.0, 1.0}; // Initial state
        double tEnd = 1.0; // Time to integrate to
        // Set very small step size to trigger exception
        double minStepSize = 1e-10;
        double maxStepSize = 1e-5;
        integrator = new TestEmbeddedRungeKuttaIntegrator(minStepSize, maxStepSize, DEFAULT_ABSOLUTE_TOLERANCE, DEFAULT_RELATIVE_TOLERANCE);
        integrator.integrate(expandableODE, tEnd);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testIntegrateInvalidStateSize() throws DimensionMismatchException, MaxCountExceededException, NoBracketingException {
        EmbeddedRungeKuttaIntegrator integrator = new TestEmbeddedRungeKuttaIntegrator();
        TestEquations equations = new TestEquations();
        ExpandableStatefulODE expandableODE = new ExpandableStatefulODE(equations);
        double tStart = 0.0;
        double[] y0 = {0.0, 1.0, 2.0}; // Initial state with invalid size
        double tEnd = 1.0; // Time to integrate to
        integrator.integrate(expandableODE, tEnd);
    }

    private class TestEmbeddedRungeKuttaIntegrator extends EmbeddedRungeKuttaIntegrator {
        public TestEmbeddedRungeKuttaIntegrator() {
            super("TestEmbeddedRungeKuttaIntegrator", true, null, null, null, null, 0.01, 1.0, DEFAULT_ABSOLUTE_TOLERANCE, DEFAULT_RELATIVE_TOLERANCE);
        }
        public TestEmbeddedRungeKuttaIntegrator(double minStep, double maxStep, double absTol, double relTol) {
            super("TestEmbeddedRungeKuttaIntegrator", true, null, null, null, null, minStep, maxStep, absTol, relTol);
        }
        public int getOrder() {
            return 4;
        }
        protected double estimateError(double[][] yDotK, double[] y0, double[] y1, double h) {
            return DEFAULT_ABSOLUTE_TOLERANCE;
        }
        protected void computeDerivatives(double t, double[] y, double[] yDot) {
            // Do nothing for testing purposes
        }
    }

    private class TestEquations implements FirstOrderDifferentialEquations {
        public int getDimension() {
            return 2;
        }
        public void computeDerivatives(double t, double[] y, double[] yDot) {
            double y0dot = y[1];
            double y1dot = -y[0]; // Simple harmonic oscillator equation
            yDot[0] = y0dot;
            yDot[1] = y1dot;
        }
        public double[] computeDerivatives(double t, double[] y) {
            double[] yDot = new double[y.length];
            computeDerivatives(t, y, yDot);
            return yDot;
        }
    }

}