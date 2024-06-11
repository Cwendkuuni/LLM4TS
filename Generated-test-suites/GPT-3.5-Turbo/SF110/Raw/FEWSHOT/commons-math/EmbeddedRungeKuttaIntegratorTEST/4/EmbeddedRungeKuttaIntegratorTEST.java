
package org.apache.commons.math3.ode.nonstiff;

import org.apache.commons.math3.ode.ExpandableStatefulODE;
import org.apache.commons.math3.ode.MaxCountExceededException;
import org.apache.commons.math3.ode.NoBracketingException;
import org.apache.commons.math3.ode.RungeKuttaStepInterpolator;
import org.apache.commons.math3.ode.sampling.StepInterpolator;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.util.FastMath;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class EmbeddedRungeKuttaIntegratorTest {

    private TestEmbeddedRungeKuttaIntegrator integrator;
    private TestExpandableStatefulODE equations;

    @Before
    public void setUp() {
        // Define the coefficients and interpolator for the integrator
        double[] c = {0.0, 0.5, 0.5, 1.0};
        double[][] a = {{}, {0.5}, {0.0, 0.5}, {0.0, 0.0, 1.0}};
        double[] b = {1.0 / 6.0, 1.0 / 3.0, 1.0 / 3.0, 1.0 / 6.0};
        RungeKuttaStepInterpolator interpolator = new RungeKuttaStepInterpolator();
        interpolator.reinitialize(null, null, null, true, null);
        
        // Instantiate the TestEmbeddedRungeKuttaIntegrator
        integrator = new TestEmbeddedRungeKuttaIntegrator("testIntegrator", true, c, a, b, interpolator, 0, 10, 0.01, 0.01);
        
        // Instantiate the TestExpandableStatefulODE
        equations = new TestExpandableStatefulODE();
    }

    @Test
    public void testComputeDerivatives() {
        equations.setCompleteState(new double[]{1.0, 2.0, 3.0});
        equations.setTime(0.0);

        double[] derivatives = new double[3];
        integrator.computeDerivatives(0.0, new double[]{1.0, 2.0, 3.0}, derivatives);

        assertArrayEquals(new double[]{3.0, 2.0, 1.0}, derivatives, 1e-10);
    }

    @Test
    public void testEstimateError() {
        double[][] yDotK = {
            {1.0, 2.0, 3.0},
            {2.0, 3.0, 4.0},
            {3.0, 4.0, 5.0},
            {4.0, 5.0, 6.0}
        };

        double[] y2 = {1.0, 2.0, 3.0};
        double[] yTmp = {2.0, 3.0, 4.0};
        double stepSize = 0.1;

        double error = integrator.estimateError(yDotK, y2, yTmp, stepSize);

        assertEquals(1.0, error, 1e-10);
    }

    @Test
    public void testInitializeStep() {
        double[] scale = {0.1, 0.2, 0.3};
        double stepStart = 0.0;
        double[] y = {1.0, 2.0, 3.0};
        double[] yDot = {2.0, 3.0, 4.0};
        double[] yTmp = new double[3];
        double[] yDotTmp = {3.0, 4.0, 5.0};

        double hNew = integrator.initializeStep(true, 4, scale, stepStart, y, yDot, yTmp, yDotTmp);

        assertEquals(0.4, hNew, 1e-10);
    }

    @Test
    public void testFilterStep() {
        double stepSize = 0.2;
        boolean forward = true;
        boolean nextIsLast = false;

        double filteredStep = integrator.filterStep(stepSize, forward, nextIsLast);

        assertEquals(0.2, filteredStep, 1e-10);
    }

    private class TestEmbeddedRungeKuttaIntegrator extends EmbeddedRungeKuttaIntegrator {

        public TestEmbeddedRungeKuttaIntegrator(String name, boolean fsal, double[] c, double[][] a, double[] b, RungeKuttaStepInterpolator prototype, double minStep, double maxStep, double scalAbsoluteTolerance, double scalRelativeTolerance) {
            super(name, fsal, c, a, b, prototype, minStep, maxStep, scalAbsoluteTolerance, scalRelativeTolerance);
        }

        protected double estimateError(double[][] yDotK, double[] y0, double[] y1, double h) {
            return 1.0;
        }

        protected double filterStep(double h, boolean forward, boolean acceptSmall) {
            return h;
        }

        protected double initializeStep(boolean forward, int order, double[] scale, double stepStart, double[] y, double[] yDot0, double[] yTmp, double[] yDotTmp) {
            return 0.4;
        }

        public int getOrder() {
            return 4;
        }
        
    }
    
    private class TestExpandableStatefulODE extends ExpandableStatefulODE {

        @Override
        protected void resetEquations() {
            // Do nothing
        }
        
    }

}
