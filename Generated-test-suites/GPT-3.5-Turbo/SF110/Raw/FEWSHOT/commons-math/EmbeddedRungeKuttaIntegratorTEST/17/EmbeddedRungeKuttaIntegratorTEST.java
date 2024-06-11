package org.apache.commons.math3.ode.nonstiff;

import static org.junit.Assert.assertEquals;

import org.apache.commons.math3.ode.DimensionMismatchException;
import org.apache.commons.math3.ode.MaxCountExceededException;
import org.apache.commons.math3.ode.NoBracketingException;
import org.apache.commons.math3.ode.nonstiff.EmbeddedRungeKuttaIntegrator;
import org.apache.commons.math3.ode.sampling.StepHandler;
import org.apache.commons.math3.ode.sampling.StepInterpolator;
import org.apache.commons.math3.ode.sampling.StepNormalizer;
import org.apache.commons.math3.util.FastMath;
import org.junit.Before;
import org.junit.Test;

public class EmbeddedRungeKuttaIntegratorTest {

    private static final double ABSOLUTE_TOLERANCE = 1e-10;
    private static final double RELATIVE_TOLERANCE = 1e-10;
    private static final double MIN_STEP = 0;
    private static final double MAX_STEP = 0.1;

    private EmbeddedRungeKuttaIntegrator integrator;

    @Before
    public void setUp() {
        integrator = new EmbeddedRungeKuttaIntegrator("", false, null, null, null, null, MIN_STEP, MAX_STEP,
                ABSOLUTE_TOLERANCE, RELATIVE_TOLERANCE) {
            @Override
	    public int getOrder() {
                return 0;
            }

            @Override
            protected double estimateError(double[][] p0, double[] p1, double[] p2, double p3) {
                return 0;
            }

            @Override
            protected void computeDerivatives(double p0, double[] p1, double[] p2) throws MaxCountExceededException, DimensionMismatchException {
                
            }
        };
    }

    @Test
    public void testGetSafety() {
        assertEquals(0.9, integrator.getSafety(), 0);
    }

    @Test
    public void testSetSafety() {
        integrator.setSafety(0.85);
        assertEquals(0.85, integrator.getSafety(), 0);
    }

    @Test
    public void testIntegrate() {
        final StepNormalizer stepNormalizer = new StepNormalizer(0.1, new StepHandler() {
            @Override
            public void handleStep(StepInterpolator interpolator, boolean isLast) {
            }

            @Override
            public void init(double t0, double[] y0, double t) {
            }
        });
        integrator.addStepHandler(stepNormalizer);
    }

    @Test
    public void testGetMinReduction() {
        assertEquals(0.2, integrator.getMinReduction(), 0);
    }

    @Test
    public void testSetMinReduction() {
        integrator.setMinReduction(0.1);
        assertEquals(0.1, integrator.getMinReduction(), 0);
    }

    @Test
    public void testGetMaxGrowth() {
        assertEquals(10.0, integrator.getMaxGrowth(), 0);
    }

    @Test
    public void testSetMaxGrowth() {
        integrator.setMaxGrowth(15.0);
        assertEquals(15.0, integrator.getMaxGrowth(), 0);
    }
}