package org.apache.commons.math.ode;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math.exception.MathIllegalArgumentException;
import org.apache.commons.math.exception.MathIllegalStateException;
import org.junit.Before;
import org.junit.Test;

public class AbstractIntegratorTest {
    private static final double TOLERANCE = 1e-10;

    private AbstractIntegrator integrator;
    private double[] y;

    @Before
    public void setUp() {
        integrator = new AbstractIntegrator() {
            @Override
            public void integrate(ExpandableStatefulODE equations, double t) throws MathIllegalStateException, MathIllegalArgumentException {
                // Dummy implementation
            }
        };
        y = new double[] { 1.0, 2.0, 3.0 };
    }

    @Test
    public void testComputeDerivatives() throws MathIllegalStateException, MathIllegalArgumentException {
        double[] yDot = new double[y.length];

        integrator.computeDerivatives(0.0, y, yDot);

        double[] expected = new double[] { -Math.sin(1.0), Math.sin(y[0]) * y[1], Math.sin(y[0]) * y[0] };
        assertArrayEquals(expected, yDot, TOLERANCE);
    }

    @Test
    public void testEvaluations() throws MathIllegalStateException, MathIllegalArgumentException {
        assertEquals(0, integrator.getEvaluations());

        integrator.computeDerivatives(0.0, y, new double[y.length]);
        assertEquals(1, integrator.getEvaluations());

        integrator.computeDerivatives(1.0, y, new double[y.length]);
        assertEquals(2, integrator.getEvaluations());

        integrator.computeDerivatives(2.0, y, new double[y.length]);
        assertEquals(3, integrator.getEvaluations());

        integrator.computeDerivatives(3.0, y, new double[y.length]);
        assertEquals(4, integrator.getEvaluations());
    }

    @Test
    public void testMaxEvaluations() throws MathIllegalStateException, MathIllegalArgumentException {
        integrator.setMaxEvaluations(3);
        integrator.computeDerivatives(0.0, y, new double[y.length]);

        integrator.computeDerivatives(1.0, y, new double[y.length]);
        integrator.computeDerivatives(2.0, y, new double[y.length]);
        try {
            integrator.computeDerivatives(3.0, y, new double[y.length]);
            assertTrue(false);
        } catch (MathIllegalStateException e) {
            assertTrue(true);
        }
    }
}